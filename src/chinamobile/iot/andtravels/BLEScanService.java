package chinamobile.iot.andtravels;

import com.aprilbrother.aprilbrothersdk.BeaconManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.BeaconManager.MonitoringListener;
import com.aprilbrother.aprilbrothersdk.BeaconManager.RangingListener;
import com.aprilbrother.aprilbrothersdk.Region;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;

public class BLEScanService extends Service implements Runnable {

	private static final String TAG = "Scan BeaconList";

	private boolean mIsScanning = false;
	private boolean mIsScaned = false;
	private String mFoundUUID;
	private BeaconManager beaconManager;
	private ArrayList<Beacon> mfindBeacons;
	private List<Map<String, Object>> beaconDistanceList = new ArrayList<Map<String, Object>>();

	private static final int REQUEST_ENABLE_BT = 1234;

	private static final Region ALL_BEACONS_REGION = new Region("apr", null, null, null);

	private final LocalBinder mBinder = new LocalBinder();

	private String mCurPlayUrl;

	private MediaPlayer mediaPlayer;

	public static final String strACT = "chinamobile.iot.andtravels.BLEScanService.UserAction";
	private UserActionReceiver recv;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
				.detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
				.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		initBeaconScanDistance();
		initService();
		// 初始化接受用户动作
		IntentFilter filter = new IntentFilter(strACT);
		recv = new UserActionReceiver();
		registerReceiver(recv, filter);
	}

	@Override
	public void onDestroy() {
		stopMedia();
		stopScanBle();
		unregisterReceiver(recv);
		super.onDestroy();
	}

	public class LocalBinder extends Binder {
		BLEScanService getService() {
			return BLEScanService.this;
		}
	}

	private String getRecentBeacon(ArrayList<Beacon> list) {
		Log.e(TAG, "获取到距离最近的beacon");
		String strBeaconID = "", curBeaconDistance = "";
		int minDistance = 0;
		int index = -1;
		for (int i = 0; i < list.size(); i++) {
			Beacon beacon = (Beacon) list.get(i);
			if (i == 0) {
				minDistance = (int) beacon.getDistance();
			} else {
				if (minDistance > (int) beacon.getDistance()) {
					minDistance = (int) beacon.getDistance();
				}
			}

			// 如果手机和beacon的距离小于1米，就播放音频
			if (minDistance < 2 && minDistance >= 0) {
				index = i;
			}

			Log.e(TAG,
					"找到的beacon name: " + beacon.getName() + "distance：" + beacon.getDistance() + "Size:" + list.size());

		}

		Log.e(TAG, "找到index:" + index);
		if (index >= 0) {
			// strBeaconID =
			// ((Beacon)list.get(index).get("beacon")).getProximityUUID();
			strBeaconID += (index + 1);
			Log.e(TAG, "找到最新的index:" + index);
		}

		return strBeaconID;
	}

	private void initBeaconScanDistance() {
		// 此接口主要是从配置文件去读取beacon扫描范围，先暂时写死了，后期在修改
		String strID = "num_";
		for (int i = 0; i < 2; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			strID = strID + i;
			map.put("ID", strID);
			map.put("Distance", 1);
			beaconDistanceList.add(map);
		}

	}

	private void initService() {

		mfindBeacons = new ArrayList<Beacon>();
		beaconManager = new BeaconManager(this);
		beaconManager.setForegroundScanPeriod(10000, 0);

		beaconManager.setRangingListener(new RangingListener() {

			@Override
			public void onBeaconsDiscovered(Region arg0, List<Beacon> arg1) {
				// TODO Auto-generated method stub
				mfindBeacons.clear();

				if (arg1 != null && arg1.size() > 0) {
					for (Beacon beacon : arg1) {
						Log.e("扫到蓝牙设备如下", "");
						Log.i(TAG, "uuid = " + beacon.getProximityUUID() + "name = " + beacon.getName() + "+++major = "
								+ beacon.getMajor() + "+++minor = " + beacon.getMinor());
						Log.i(TAG, "power = " + beacon.getPower());

						// 只处理四月兄弟的beacon
						if (beacon.getName().contains("AprilBeacon")) {
							mfindBeacons.add(beacon);
						}

					}

					mFoundUUID = getRecentBeacon(mfindBeacons);
					// mFoundUUID = beacon.getProximityUUID();
					Log.i(TAG, "获取到最近的蓝牙Id：" + mFoundUUID);

					// 找到了就播放相应的音频文件
					try {
						// GetUrl(mFoundUUID);
						getUrlForTest(mFoundUUID);

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					mIsScaned = false;
				}

			}
		});
	}

	public void startScanBle() {
		Log.e("Tag", "BLEService startScanBle()");

		if (!beaconManager.hasBluetooth()) {
			Log.e("Tag", "没有找到蓝牙设备");
			return;
		}

		if (!beaconManager.isBluetoothEnabled()) {
			Log.e("Tag", "蓝牙设备没有打开,停止扫描");
		} else {
			Log.e("Tag", "蓝牙设备已经打开,开始扫描");
			beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
				@Override
				public void onServiceReady() {
					try {
						beaconManager.startRanging(ALL_BEACONS_REGION);
					} catch (RemoteException e) {
						Log.e("Tag", "蓝牙设备扫描失败");
					}
				}
			});

		}

		mIsScanning = true;
	}

	public void stopScanBle() {
		try {
			mfindBeacons.clear();
			beaconManager.stopRanging(ALL_BEACONS_REGION);
		} catch (RemoteException e) {
			Log.d(TAG, "Error while stopping ranging", e);
		}

		mIsScanning = false;

	}

	private void getUrl(String strId) {
		String url = "http://172.16.0.11:8080/AndTravel/beacondatasearch/onekeyguide/" + strId;
		RequestQueue mQueue = Volley.newRequestQueue(this);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.d("TAG", "服务器正确返回小景点数据");
				// 此处只处理景点的声音
				try {
					if (response.getString("code").equals("1")) {
						JSONArray jsonArryData = (JSONArray) response.getJSONArray("data");
						for (int i = 0; i < jsonArryData.length(); i++) {
							final String soundUrl = (String) ((JSONObject) jsonArryData.getJSONObject(i))
									.getString("url");
							playMedia(soundUrl);
							stopScanBle();
						}
					} else {
						/* 没有获取到数据就不处理了 */
					}
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Log.e(TAG, "没有从服务器上获取到需要的景点信息");
			}
		});

		mQueue.add(jsonObjectRequest);

	}

	private void getUrlForTest(String beconId) {
		File filePath = Environment.getExternalStorageDirectory();
		String url = null;// filePath.getPath() + "/orange.mp3";
		File file = new File(filePath, "orange.mp3");
		url = file.getAbsolutePath();
		if (mIsScanning) {
			playMedia(url);
			mCurPlayUrl = url;
			// 在播放音频时，停止Ble扫描
			stopScanBle();
		}

	}

	public void playMedia(String url) {
		// url
		// ="http://play.baidu.com/?__m=mboxCtrl.playSong&__a=130238461&__o=song/130238461||playBtn&fr=-1||www.sowang.com#";

		// Uri uri = Uri.parse(url);
		Log.e(TAG, "播放音频文件" + url);
		try {
			mediaPlayer = new MediaPlayer();
			File file = new File(url);
			FileInputStream fis = new FileInputStream(file);
			mediaPlayer.setDataSource(fis.getFD());
			// mediaPlayer.setDataSource(url);
			mediaPlayer.prepare();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					// 音频播放完了就启动扫描
					mediaPlayer.release();
					mediaPlayer = null;
					startScanBle();
				}

			});

			mediaPlayer.start();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}

	}

	public void stopMedia() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}

		mediaPlayer.release();
		mediaPlayer = null;

	}

	public void pauseMedia() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Service------->onStartCommand");
		startScanBle();
		return super.onStartCommand(intent, flags, startId);
	}

	// 读取蓝牙Beacon配置文件
	private void readConfigFile() {
		String file = "mainlistitems.txt";
		String content = ""; // 文件内容字符串

		try {
			InputStream instream = new FileInputStream(file);

			if (instream != null) {
				InputStreamReader inputreader = new InputStreamReader(instream);
				BufferedReader buffreader = new BufferedReader(inputreader);
				String line;

				while ((line = buffreader.readLine()) != null) {
					content += line;
				}
				instream.close();

			}
		} catch (java.io.FileNotFoundException e) {
			Log.e(TAG, "配置文件没有找到");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class UserActionReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i(TAG, "Service 获取到客户操作信息");
			String action = intent.getStringExtra("usrAction");
			if (action.compareToIgnoreCase("start") == 0) {
				Log.e(TAG, "获取客户点击开始按钮");
				stopMedia();
				playMedia(mCurPlayUrl);
			} else if (action.compareToIgnoreCase("pause") == 0) {
				Log.e(TAG, "获取客户点击了暂停按钮");
				pauseMedia();
			} else {

			}
		}
	}

}

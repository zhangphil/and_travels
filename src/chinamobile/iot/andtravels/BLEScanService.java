package chinamobile.iot.andtravels;

import com.aprilbrother.aprilbrothersdk.BeaconManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

public class BLEScanService extends Service implements Runnable {

	private static final String TAG = "Scan BeaconList";

	private boolean mIsScanning = false;
	private boolean mIsScaned = false;
	private String mFoundUUID = "";
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
	private RequestQueue mRequestQueue;
	private final int PLAY_AUDIO_MESSAGE = 10000;
	private Handler handler;
	//区分是否是demo版本
	private boolean mTest = false;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initBeaconScanDistance();
		initService();
		// 初始化接受用户动作
		IntentFilter filter = new IntentFilter(strACT);
		recv = new UserActionReceiver();
		registerReceiver(recv, filter);
		
		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		 
		handler = new Handler(){
			@Override  
	        public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case PLAY_AUDIO_MESSAGE:
					playMedia(mCurPlayUrl);
					break;
				}
	             
	             
	        } 
		 };
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
		String strBeaconName = "";
		String strBeaconID = "", curBeaconDistance = "";
		double minDistance = 0.0, compareDistance = 1.0;
		int minIndex = -1, index = -1;

		for (int i = 0; i < list.size(); i++) {
			Beacon beacon = (Beacon) list.get(i);
			if (i == 0) {
				minDistance = beacon.getDistance();
				index = i;
			} else {
				BigDecimal distance = new BigDecimal(beacon.getDistance());
				if (distance.compareTo(new BigDecimal(minDistance)) < 0) {
					minDistance = beacon.getDistance();
					index = i;
				}
			}

			// 如果手机和beacon的距离小于1米，就播放音频
			if ((new BigDecimal(minDistance)).compareTo(new BigDecimal(compareDistance)) < 0) {
				minIndex = index;
			}

			Log.e(TAG, "找到的beacon name: " + beacon.getName() + "distance：" + beacon.getDistance() + "Size:"
					+ list.size() + " minDistance " + minDistance);

		}

		Log.e(TAG, "找到index:" + minIndex);
		if (minIndex >= 0) {
			// strBeaconID =
			// ((Beacon)list.get(index).get("beacon")).getProximityUUID();
			if(mTest){
				strBeaconName = list.get(minIndex).getName();
				Log.e(TAG, "找到的距离最短的beacon name: " + strBeaconName);
				if (strBeaconName.contains("abeacon_FB3E")) {
				strBeaconID += 1;
				}else if (strBeaconName.contains("abeacon_FAE9")) {
					strBeaconID += 2;
				}else {
					strBeaconID += 3;
				}
			}else{
				strBeaconID = ((Beacon)list.get(index)).getName();
			}
			
			Log.e(TAG, "找到最新的index:" + minIndex);
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
						if(beacon.getName().contains("abeacon_"))
						{
							mfindBeacons.add(beacon);
						}

					}

					String strFoundUUID = getRecentBeacon(mfindBeacons);
					// mFoundUUID = beacon.getProximityUUID();
					Log.i(TAG, "获取到最近的蓝牙Id：" + strFoundUUID);

					// 找到了就播放相应的音频文件
					// GetUrl(mFoundUUID);
					if (!strFoundUUID.equalsIgnoreCase("")) {
						if(mTest){
							getUrlForTest(strFoundUUID);
						}else{
							try {
								if (mIsScanning && (mFoundUUID == null || (mFoundUUID != null && !mFoundUUID.equalsIgnoreCase(strFoundUUID)))) {
									mFoundUUID = strFoundUUID;
									fetchPlayAudioUrl(strFoundUUID);
									// 在播放音频时，停止Ble扫描
									stopScanBle();
								}
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
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
			beaconManager.disconnect();
		} catch (RemoteException e) {
			Log.d(TAG, "Error while stopping ranging", e);
		}

		mIsScanning = false;

	}

	private void getUrl(String strId) {
		String url = "http://172.16.0.11:8080/AndTravel/beacondatasearch/onekeyguide/" + strId;
		
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

		mRequestQueue.add(jsonObjectRequest);

	}

	private void getUrlForTest(String beconId) {
		File filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		String url = filePath.getPath() + "/yinpin" + beconId + ".m4a";
		Log.e(TAG, "cur play url" + url);
		Log.e(TAG, "last play url" + mCurPlayUrl);
		if (mIsScanning && (mCurPlayUrl == null || (mCurPlayUrl != null && !mCurPlayUrl.equalsIgnoreCase(url)))) {
			playMedia(url);
			mCurPlayUrl = url;
			// 在播放音频时，停止Ble扫描
			stopScanBle();
		}

	}

	private void fetchPlayAudioUrl(String strBeaconId ) throws JSONException{
		String url = "http://172.16.0.138:8080/AndTravel/spot/getcontent/bybeacon/";
		url = url + strBeaconId;
	
		Log.e(TAG, "获取到最近的蓝牙BeaconID对应音频文件地址：" + url);
	   	RequestQueue mQueue = Volley.newRequestQueue(this);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.d(TAG, "从服务器获取到蓝牙Beacon对应音频文件");
				try {
					if (response.getString("code").equals("1")) {
						Log.d(TAG, "从服务器已经成功获取到蓝牙Beacon对应音频文件了！！！！");
						JSONObject message = response.getJSONObject("message");
			        	JSONArray contentArray = message.getJSONArray("content");
			        	if(contentArray.length() > 0){
			        		Log.d(TAG, "从服务器已经成功获取到蓝牙Beacon对应文件大小：" + contentArray.length());
			        		for(int i = 0; i < contentArray.length(); i++){
			        			JSONObject content = (JSONObject) contentArray.get(i);
			        			if(content.getString("contentType").equals("1")){
			        				String audioUrl = content.getString("contentUrl");
			        				Log.e(TAG, "从服务器获取到播放音频数据URL：" + audioUrl);
					        		if(audioUrl.isEmpty()){
					        			Log.e(TAG, "从服务器获取到播放音频数据URL为空");
					        		}else{
					        			//暂时先从服务器下载音频文件到本地，然后在进行播放
					        			FetchAudioFile(audioUrl);
					        			//playMedia(filePath);
					        		}
			        			}else{
			        				/*服务只处理音频文件*/
			        			}
			        		}
			        	}else{
			        		Log.e(TAG, "从服务器获取到播放音频数据为空");
			        	}
					} else {
						Toast.makeText(BLEScanService.this, "从服务器获取导览的语音数据失败", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "error = " + error.toString());
				Log.e(TAG, "从服务器获取导览音频文件失败");
			}
		});

		mQueue.add(jsonObjectRequest);
		
	}
	
	private void  FetchAudioFile(String audioUrl){
		String audioPath = null;
		final String remoteAudioUrl = audioUrl;
		if(!remoteAudioUrl.isEmpty()){
			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					File filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
					Random ran = new Random(System.currentTimeMillis()); 
					int randomNum = ran.nextInt(10000);
					String audioPath = filePath.getPath() + "/" + randomNum + ".m4a";
					
					File file = new File(audioPath);  
					Log.e(TAG,"存储到手机上的音频文件的路径 :"+audioPath); 
					if(file.exists())  
					{  
					    file.delete();  
					}  
					try {  
				         URL httpUrl = new URL(remoteAudioUrl);      
				         URLConnection con = httpUrl.openConnection();  
				         int contentLength = con.getContentLength();  
				         Log.e(TAG,"从服务器上获取音频文件的长度 :"+contentLength);  
				         InputStream is = con.getInputStream();    
				         byte[] bs = new byte[1024];     
				         int len;     
				         OutputStream os = new FileOutputStream(audioPath);     
				         while ((len = is.read(bs)) != -1) {     
				             os.write(bs, 0, len);     
				         } 
				         os.close();    
				         is.close();  
				         
				         mCurPlayUrl = audioPath;
				         
				         handler.sendEmptyMessage(PLAY_AUDIO_MESSAGE);
					              
					} catch (Exception e) { 
						Log.e(TAG, "从服务器下载音频文件时，写入手机上出错了！！！" + e.toString());
					       
					}  
				}
				
			}).start();
		}else{
			Log.e(TAG, "从服务器获取到音频文件Url为空");
		}
	
	}
	
	public void playMedia(String url) {
	
		Log.e(TAG, "播放音频文件" + url);
		try {
			mediaPlayer = new MediaPlayer();
			if(mTest){
				try{
					File file = new File(url);
					FileInputStream fis = new FileInputStream(file);
					mediaPlayer.setDataSource(fis.getFD());
					fis.close();
				}catch(Exception e){
					Log.e(TAG,"打开音频文件异常：" + e);
				}
			}else{
				mediaPlayer.setDataSource(url);
			}
			
			mediaPlayer.prepare();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					// 音频播放完了就启动扫描
					Log.e(TAG, "景区的音频文件播放完了！！！！");
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
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}

	}

	public void pauseMedia() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
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
			} else if (action.compareToIgnoreCase("stop") == 0) {
				Log.e(TAG, "获取客户点击了暂停按钮");
				pauseMedia();
			} else {

			}
		}
	}

}

package chinamobile.iot.andtravels;

import com.aprilbrother.aprilbrothersdk.BeaconManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aprilbrother.aprilbrothersdk.BeaconManager;

import chinamobile.iot.andtravels.BLEScanService;
import chinamobile.iot.andtravels.ContainerActivity.MyViewPagerTabHost;
import chinamobile.iot.andtravels.utils.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.BeaconManager.MonitoringListener;
import com.aprilbrother.aprilbrothersdk.BeaconManager.RangingListener;
import com.aprilbrother.aprilbrothersdk.Region;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.LruCache;

public class StartActivity extends FragmentActivity {


	private final String LOG_TAG = "StartActivity";
	
	private static final int REQUEST_ENABLE_BT = 1234;
	private BluetoothAdapter mBluetoothAdapter;
	private BeaconManager beaconManager = new BeaconManager(this);
	private Fragment newFragment;
	
	public static final String strBroadcastMessage = "chinamobile.iot.andtravels.SetLogin";
	private UserLoginReceiver recv;
	private boolean mIsLogin = false;
	
	//增加缓存机制，用户存放从服务器上获取的图片
	private ImageLoader mImageLoader;
	private RequestQueue mQueue;
	private ImageBitmapCache mImageCache = new ImageBitmapCache();
	private ArrayList<String> mImageUrlList = new ArrayList<String>();
	private Handler handle;
	private final int DOWN_LOAD_IMAGE = 1000;
	private ArrayList<ImageView> mImageViewList = new ArrayList<ImageView>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.start_main_fragment);
		
		newFragment = new StartMainFragment();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment, newFragment);
		transaction.commit();
		
		startBle();
		
		// 初始化用户登录的广播
		Log.e(LOG_TAG, "注册广播了哈！！！！");
		IntentFilter userLoginfilter = new IntentFilter(strBroadcastMessage);
		recv = new UserLoginReceiver();
		registerReceiver(recv, userLoginfilter);
		IntentFilter curCityfilter = new IntentFilter(Constants.ACTION_CHINAMOBILE_IOT_ANDTRAVELS_BROADCAST);
		registerReceiver(recv, curCityfilter);
		
		handle = new Handler(){
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case DOWN_LOAD_IMAGE:
					downloadImageFile();
					break;
				}
			};
		};
		
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.e(LOG_TAG, "onResume 重新绘制fragment");
		//newFragment = new StartMainFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment, newFragment);
		transaction.commit();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				Log.e("Tag", "蓝牙设备打开了,开始扫描");

			} else {
				Log.e("终端启动蓝牙失败", "");
			}
		} else {
			Log.e("Tag", "启动参数不是启动蓝牙");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void startBle() {

		if (!beaconManager.hasBluetooth()) {
			Log.e("Tag", "没有找到蓝牙设备");
			return;
		}

		Log.e("Tag", "找到蓝牙设备,准备扫描");

		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			Log.e("Tag", "启动蓝牙设备");
		} else {

			Log.e("Tag", "蓝牙设备已经打开了");
		}

	}

	@Override
	protected void onDestroy() {

		Intent daoLanIntent = new Intent();
		daoLanIntent.setAction("chinamobile.iot.andtravels.communication.BeaconService");
		daoLanIntent.setPackage(getPackageName());
		stopService(daoLanIntent);
		
		unregisterReceiver(recv);

		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	public boolean getUserIsLogin(){
		return mIsLogin;
	}
	
	public class UserLoginReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String intentAction = intent.getAction();
			if(intentAction.equals("chinamobile.iot.andtravels.SetLogin")){
				Log.i(LOG_TAG, "接收到广播：用户已经登录了！！！");
				mIsLogin = true;
			}else if(intentAction.equals(Constants.ACTION_CHINAMOBILE_IOT_ANDTRAVELS_BROADCAST) ){
				
			}else{
				
			}
			
		}
	}
	
	public void fetchImageFile(ArrayList<ImageView> imageViews){
	
		mImageViewList = imageViews;
		
		if(mImageUrlList.isEmpty()){
			fetchImageSource();
		}else{
			;
		}
			
	}
	
	private void downloadImageFile(){
		
		/*mImageUrlList.add("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg");
		mImageUrlList.add("http://img3.cache.netease.com/photo/0005/2015-09-15/B3HFSCD30ACR0005.jpg");
		mImageUrlList.add("http://img3.cache.netease.com/photo/0005/2015-09-15/900x600_B3HFRSGH0ACR0005.jpg");
		mImageUrlList.add("http://img2.cache.netease.com/photo/0008/2014-12-08/ACUUN0C85BD20008.jpg");*/
		Log.e(LOG_TAG, "首页准备从服务器上加载首页图片了！！！");
		if(mQueue==null){
			Log.e(LOG_TAG, "mQueue为空，需要在创建了");
			mQueue = Volley.newRequestQueue(this);
		}else{
			Log.e(LOG_TAG, "mQueue不为空，不需要在创建了");
		}
		
		if(mImageLoader==null){
			Log.e(LOG_TAG, "mImageLoader为空，需要在创建了");
			mImageLoader = new ImageLoader(mQueue, mImageCache);
		}else{
			Log.e(LOG_TAG, "mImageLoader不为空，不需要在创建了");
		}
			
		for(int i = 0; i < mImageUrlList.size();i++){
			ImageListener listener = null;
			listener = ImageLoader.getImageListener(mImageViewList.get(i),0, 0);

			if(listener != null){
				mImageLoader.get(mImageUrlList.get(i), listener); 
			}else{
				Log.e(LOG_TAG, "创建的ImageListener失败了");
				
			}
			
		}
	}
	public class ImageBitmapCache implements ImageCache {  
		  
	    private LruCache<String, Bitmap> mCache;  
	  
	    public ImageBitmapCache() {  
	        int maxSize = 2 * 1024 * 1024;  
	        mCache = new LruCache<String, Bitmap>(maxSize) {  
	            @Override  
	            protected int sizeOf(String key, Bitmap bitmap) {  
	                return bitmap.getRowBytes() * bitmap.getHeight();  
	            }  
	        };  
	    }  
	  
	    @Override  
	    public Bitmap getBitmap(String url) {  
	    	Log.e(LOG_TAG, "从缓存中获取图片资源！！！");
	    	if( mCache.get(url) == null ){
	    		Log.e(LOG_TAG, "从缓存中获取图片资源为空！！！！！");
	    	}else{
	    		Log.e(LOG_TAG, "从缓存中获取到了图片资源！！！！！");
	    	}
	        return mCache.get(url);  
	    }  
	  
	    @Override  
	    public void putBitmap(String url, Bitmap bitmap) { 
	    	Log.e(LOG_TAG, "往缓存中添加图片资源！！！");
	        mCache.put(url, bitmap);  
	    }  
	  
	} 
	
	/**
	 * 首页需要从服务器上获取最新推荐的景区图片
	 */
	private void fetchImageSource(){
		
		String url = "http://172.16.0.138:8080/AndTravel/content/introductory/";
		
		RequestQueue mQueue = Volley.newRequestQueue(this);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (response.getString("code").equals("1")) {
						Log.d(LOG_TAG, "首页从服务器获取到了相关的图片！！！！");
						//JSONObject message = response.getJSONObject("Value");
			        	JSONArray contentArray = response.getJSONArray("message");
			        	if(contentArray.length() > 0){
			        		for(int i = 0; i < contentArray.length(); i++){	
		        				String imageUrl = contentArray.get(i).toString();
		        				Log.e(LOG_TAG, "从服务器获取到图片的URL：" + imageUrl);
				        		mImageUrlList.add(imageUrl);			

			        		}
			        		
			        		handle.sendEmptyMessage(DOWN_LOAD_IMAGE);
	
						} else {
							Toast.makeText(StartActivity.this,"首页从服务器上获取的图片位空！！！", Toast.LENGTH_SHORT).show();
						}
					}
				} catch (Exception e) {
					Log.e(LOG_TAG, e.toString());
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(LOG_TAG, "error = " + error.toString());
				Log.e(LOG_TAG, "Login Activity 没有从服务器上获取到首页图片！！！！");
			}
		});

		mQueue.add(jsonObjectRequest);
			
	}
	
}

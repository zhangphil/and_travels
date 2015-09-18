package chinamobile.iot.andtravels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import chinamobile.iot.andtravels.SettingFragment.ListViewAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.LruCache;

public class StartMainFragment extends Fragment implements OnPageChangeListener{

	private static final String LOG_TAG = "StartMainFragment";
	private ViewPager viewPager;
	private RadioGroup viewDaoLanGroup;
	private ImageAdapter mImageAdapter;
	private Handler handler;
	private final int MESSAGE_WHAT_CHANGED = 100;
	private  CircleIndicatorView mCircleIndicatorView;
	private final int mViewPageNum = 4;

	private boolean mIsLogin = false;
	private final boolean mTest = true;
	private ArrayList<ImageView> mImageViews = new ArrayList<ImageView>();

	
	//增加缓存机制，用户存放从服务器上获取的图片
	ImageLoader mImageLoader;
	RequestQueue mQueue;
	private ImageBitmapCache mImageCache = new ImageBitmapCache();
	private ArrayList<String> mImageUrlList = new ArrayList<String>();
	ImageView mView1, mView2, mView3,mView4; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.start_main, null);
		
		viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		ImageView personView = (ImageView)view.findViewById(R.id.personImage);
		
		initImageSource(inflater);
		mImageAdapter = new ImageAdapter(mImageViews);
		viewPager.setAdapter(mImageAdapter);
		viewPager.setOnPageChangeListener(this);
		viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
	
		initDaoLanView(view);
		
	    mCircleIndicatorView =  (CircleIndicatorView) view.findViewById(R.id.circleIndicatorView);
	    
	    personView.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//进入我的界面去
				int pos = 3;
				Intent intent = new Intent(getActivity(), ContainerActivity.class);
				intent.putExtra("curViewPos", pos);
				getActivity().startActivity(intent);
			}
	    	
	    });

		return view;
	}
	
	private class ImageAdapter extends PagerAdapter {

		private ArrayList<ImageView> viewlist;

		public ImageAdapter(ArrayList<ImageView> viewlist) {
			this.viewlist = viewlist;
		}

		@Override
		public int getCount() {
			// 设置成最大，使用户看不到边界
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// Warning：不要在这里调用removeView
			((ViewPager)container).removeView(viewlist.get(position % (viewlist.size())));  
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 对ViewPager页号求模取出View列表中要显示的项
			position %= viewlist.size();
			if (position < 0) {
				position = viewlist.size() + position;
			}
			ImageView view = viewlist.get(position);
			
			mCircleIndicatorView.setCircleCount(mViewPageNum);
			mCircleIndicatorView.setCircleSelectedPosition(position);
			mCircleIndicatorView.drawCircleView();
			
			// 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
			ViewParent vp = view.getParent();
			if (vp != null) {
				ViewGroup parent = (ViewGroup) vp;
				parent.removeView(view);
			}
			container.addView(view);
			// add listeners here if necessary
			return view;
		}
	}

	private void initDaoLanView(View view) {
		viewDaoLanGroup = (RadioGroup) view.findViewById(R.id.kaiShiDaoLan);
		viewDaoLanGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.kaiShiDaoLanButtom:
					// 此处先检查客户端是否注册,先暂时不处理
					Log.i("DaoLanView", "On Click");

					if (!(boolean)((StartActivity) getActivity()).getUserIsLogin()) {
						AlertDialog dialog =new AlertDialog.Builder(getActivity()).setTitle("")
						.setMessage("您还没有加入我们的圈子！").setNegativeButton("返回", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub

							}

						}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

								// TODO Auto-generated method stub
								Intent intent = new Intent(getActivity(), LoginActivity.class);
								getActivity().startActivity(intent);
								
								dialog.dismiss();
							}

						}).show();
					} else {
						//提示用户需要付费才能使用
						int pos = 0;
						Intent intent = new Intent(getActivity(), ContainerActivity.class);
						intent.putExtra("curViewPos", pos);
						getActivity().startActivity(intent);

					}
					break;

				default:
					break;
				}

			}

		});

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void initImageSource(LayoutInflater inflater){
		
		mView1 = (ImageView) inflater.inflate(R.layout.viewpage_item, null).findViewById(R.id.imageViewPage);
		mView2 = (ImageView) inflater.inflate(R.layout.viewpage_item02, null).findViewById(R.id.imageViewPage02);
		mView3 = (ImageView) inflater.inflate(R.layout.viewpage_item03, null).findViewById(R.id.imageViewPage03);
		mView4 = (ImageView) inflater.inflate(R.layout.viewpage_item04, null).findViewById(R.id.imageViewPage04);
		
		if(mTest){
			mView1.setImageResource(R.drawable.start_view01);
			mView2.setImageResource(R.drawable.start_view02);
			mView3.setImageResource(R.drawable.start_view03);
			mView4.setImageResource(R.drawable.start_view04);

		}else{
			fetchImageSource();
		}
		
		mImageViews.add(mView1);
		mImageViews.add(mView2);
		mImageViews.add(mView3);
		mImageViews.add(mView4);
		
	}
	
	/**
	 * 首页需要从服务器上获取最新推荐的景区图片
	 */
	private void fetchImageSource(){
		
		String url = "http://172.16.0.138:8080/AndTravel/content/introductory/";
		
		if(mImageUrlList.isEmpty()){
			RequestQueue mQueue = Volley.newRequestQueue(getActivity());
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
					        		if(imageUrl.isEmpty()){
					        			Log.e(LOG_TAG, "从服务器获取到播放音频数据URL为空");
					        		}else{
					        			mImageUrlList.add(imageUrl);			
					        		}
				        			
				        		}
				        		
				        		fetchImageFile(mImageUrlList);
		
						} else {
							Toast.makeText(getActivity(), "首页从服务器上获取的图片位空！！！", Toast.LENGTH_SHORT).show();
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
		}else{
			fetchImageFile(mImageUrlList);
		}
		
	}
	
	private void  fetchImageFile(ArrayList<String> imageUrlList){
		
		if(mQueue==null){
			Log.e(LOG_TAG, "mQueue为空，需要在创建了");
			mQueue = Volley.newRequestQueue(getActivity());
		}else{
			Log.e(LOG_TAG, "mQueue不为空，不需要在创建了");
		}
		
		if(mImageLoader==null){
			Log.e(LOG_TAG, "mImageLoader为空，需要在创建了");
			mImageLoader = new ImageLoader(mQueue, mImageCache);
		}else{
			Log.e(LOG_TAG, "mImageLoader不为空，不需要在创建了");
		}
			
		for(int i = 0; i < imageUrlList.size();i++){
			ImageListener listener = null;
			if(i==0){
				listener = ImageLoader.getImageListener(mView1,0, 0);
			}else if(i==1){
				listener = ImageLoader.getImageListener(mView2,0, 0);
			}else if(i==2){
				listener = ImageLoader.getImageListener(mView3,0, 0);
			}else if(i==3){
				listener = ImageLoader.getImageListener(mView4,0, 0);
			}else{
				;
			}
			
			if(listener != null){
				mImageLoader.get(imageUrlList.get(i), listener); 
			}else{
				Log.e(LOG_TAG, "创建的ImageListener失败了");
				
			}
			
		}
		
  }
	
	public class ImageBitmapCache implements ImageCache {  
		  
	    private LruCache<String, Bitmap> mCache;  
	  
	    public ImageBitmapCache() {  
	        int maxSize = 10 * 1024 * 1024;  
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
	    	}
	        return mCache.get(url);  
	    }  
	  
	    @Override  
	    public void putBitmap(String url, Bitmap bitmap) { 
	    	Log.e(LOG_TAG, "往缓存中添加图片资源！！！");
	        mCache.put(url, bitmap);  
	    }  
	  
	}  
	
}
	
	
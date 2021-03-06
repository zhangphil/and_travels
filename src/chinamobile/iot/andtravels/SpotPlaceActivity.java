package chinamobile.iot.andtravels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bumptech.glide.Glide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ZoomControls;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import chinamobile.iot.andtravels.utils.Constants.WuHouCiGeoPoint;
import chinamobile.iot.andtravels.StartActivity.ImageBitmapCache;
import chinamobile.iot.andtravels.StartActivity.UserLoginReceiver;
import chinamobile.iot.andtravels.utils.Constants;
import chinamobile.iot.andtravels.utils.Utils;

public class SpotPlaceActivity extends FragmentActivity {

	private final String LOG_TAG = "SpotPlaceActivity";
	private PagerAdapter mPagerAdapter;
	private ViewPager mViewPager;

	private Handler handler;
	private final int MESSAGE_WHAT_CHANGED = 100;

	private GeoCoder mSearch = null;
	private BaiduMap mBaiduMap = null;
	private MapView mMapView = null;

	private View containerView;
	private boolean FULL_SCREEN = false;

	private final String CITY = "成都";
	private final String ADDRESS = "武侯祠";
	private String spotId = "";

	private boolean isPlaying = false;
	private RequestQueue mQueue;
	
	private String mTravelImageUrl = null;
	private ImageBitmapCache mImageCache = new ImageBitmapCache();
	private ImageLoader mImageLoader;
	
	public static final String strBroadcastMessage = "chinamobile.iot.andtravels.SetLogin";
	private UserLoginReceiver recv;
	private String mStrUserID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SDKInitializer.initialize(getApplicationContext());

		containerView = this.getLayoutInflater().inflate(R.layout.spot_place, null);
		setContentView(containerView);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		spotId = bundle.getString("placeId");
		
		IntentFilter userLoginfilter = new IntentFilter(strBroadcastMessage);
		recv = new UserLoginReceiver();
		registerReceiver(recv, userLoginfilter);
		

		mViewPager = (ViewPager) containerView.findViewById(R.id.viewpager_head);
		mPagerAdapter = new MyPagerAdapter(this);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				mViewPager.setCurrentItem(pos, true);
				handler.sendEmptyMessage(MESSAGE_WHAT_CHANGED);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		final CircleIndicatorView mCircleIndicatorView = (CircleIndicatorView) containerView
				.findViewById(R.id.circleIndicatorView);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case MESSAGE_WHAT_CHANGED:
					mCircleIndicatorView.setCircleCount(mPagerAdapter.getCount());
					mCircleIndicatorView.setCircleSelectedPosition(mViewPager.getCurrentItem());
					mCircleIndicatorView.drawCircleView();

					break;
				}
			};
		};

		// 初始化选择第一项
		if (mPagerAdapter.getCount() > 0) {
			mViewPager.setCurrentItem(0, true);
			handler.sendEmptyMessage(MESSAGE_WHAT_CHANGED);
		}

		daoYouImageView();
		backImageView();

		initMyBaiduMap();
		
		final	ImageView more=(ImageView) containerView.findViewById(R.id.more);
		
		final	Context context=this;
		RelativeLayout share_RelativeLayout=(RelativeLayout)containerView.findViewById(R.id.share_RelativeLayout);
		share_RelativeLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ShareDialog share=new ShareDialog(context);
				share.requestWindowFeature(Window.FEATURE_NO_TITLE);
				share.show(); 
			}
		});
		
		//增加收藏、评论、点赞的点击事件
		ImageView travelCollect = (ImageView) containerView.findViewById(R.id.travelCollect);
		travelCollect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		
		ImageView travelFavorite = (ImageView) containerView.findViewById(R.id.travelFavorite);
		travelFavorite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		
		ImageView travelComment = (ImageView) containerView.findViewById(R.id.travelComment);
		travelComment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		
		mQueue = Volley.newRequestQueue(this);
	}

	private void collectTravelHttpRequest(){
		String url = "http://172.16.0.138:8080/AndTravel/userinfo/upload/like";

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", mStrUserID);
		params.put("areaid", spotId);
		params.put("pictureurl", mTravelImageUrl);
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");       
		String time = sDateFormat.format(new java.util.Date());
		params.put("date",time );
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,url, new JSONObject(params), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.d(LOG_TAG, "httpRequest 服务器返回收藏结果！！！！");
				try {
					String message = response.getString("message");
					if (response.getString("code").equals("1")) {
						Toast.makeText(SpotPlaceActivity.this, message, Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(SpotPlaceActivity.this, message, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Log.e(LOG_TAG, e.toString());
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(LOG_TAG, "error = " + error.toString());
				Log.e(LOG_TAG, "httpRequest 客户收藏景点失败！！！！");
			}
		});

		mQueue.add(jsonObjectRequest);

	}
	
	private void backImageView() {
		ImageView backiv = (ImageView) containerView.findViewById(R.id.back_ImageView);
		backiv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (FULL_SCREEN) {
					setBaiduMapFullScreen(false);
					return;
				}

				if (!FULL_SCREEN) {
					Utils.onKeyEvent(KeyEvent.KEYCODE_BACK);
					return;
				}
			}
		});
	}

	private void setBaiduMapFullScreen(boolean show) {

		final FrameLayout headFrameLayout = (FrameLayout) containerView.findViewById(R.id.headFrameLayout);
		final FrameLayout daoyouFrameLayout = (FrameLayout) containerView.findViewById(R.id.daoyouFrameLayout);
		final LinearLayout centerLinearLayout = (LinearLayout) containerView.findViewById(R.id.centerLinearLayout);

		int visibility = 0;

		if (show)
			visibility = View.GONE;
		if (!show)
			visibility = View.VISIBLE;

		headFrameLayout.setVisibility(visibility);
		daoyouFrameLayout.setVisibility(visibility);
		centerLinearLayout.setVisibility(visibility);

		FULL_SCREEN = show;
		
		//当地图全屏时候放大整个地图和景点覆盖物。
		if(FULL_SCREEN){
			setBaidMapZoomLarge();
		}	

		startBluetooth();
	}
	
	private	void	setBaidMapZoomLarge(){
		MapStatusUpdate mapStatusUpdate=MapStatusUpdateFactory.zoomTo(17.5f);
		mBaiduMap.setMapStatus(mapStatusUpdate);
	}
	
	private	void	setBaidMapZoomSmall(){
		MapStatusUpdate mapStatusUpdate=MapStatusUpdateFactory.zoomTo(16.0f);
		mBaiduMap.setMapStatus(mapStatusUpdate);
	}

	private void startBluetooth() {
		// 点击导览后，启动后台蓝牙扫描
		Intent daoLanIntent = new Intent();
		daoLanIntent.setAction("chinamobile.iot.andtravels.communication.BeaconService");
		daoLanIntent.setPackage(getPackageName());
		startService(daoLanIntent);
	}

	private void daoYouImageView() {

		ImageView spotImageView = (ImageView) containerView.findViewById(R.id.daoyouImageView);
		spotImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setBaiduMapFullScreen(true);
			}
		});
	}

	private void initMyBaiduMap() {

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		// 隐藏百度的LOGO
		View child = mMapView.getChildAt(1);
		if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
			child.setVisibility(View.INVISIBLE);
		}

		// 不显示地图上比例尺
		mMapView.showScaleControl(false);

		// 不显示地图缩放控件（按钮控制栏）
		mMapView.showZoomControls(false);

		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

			@Override
			public void onGetGeoCodeResult(GeoCodeResult result) {
				baiduMapOnCreate(result);
			}

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {

			}
		});

		// 不晓得为啥，反正必须不能太快调用百度地图的定位搜索功能，
		// 需要先暂停一些时间才可以正常工作
		// 如果直接调用，则返回为空值。莫名其妙！
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mSearch.geocode(new GeoCodeOption().city(CITY).address(ADDRESS));
			}
		}, 100);
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		mSearch.destroy();
		super.onDestroy();
	}

	// @Override
	// public void onGetGeoCodeResult(GeoCodeResult result) {
	// if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	// Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
	// return;
	// }
	//
	// mBaiduMap.clear();
	//
	// mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
	// .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)));
	//
	// mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
	//
	// @Override
	// public boolean onMarkerClick(Marker marker) {
	// if (!FULL_SCREEN) {
	// setBaiduMapFullScreen(true);
	// }
	//
	// if (FULL_SCREEN) {
	// pop();
	// }
	//
	// return false;
	// }
	// });
	//
	// // newpop();
	//
	// MapStatusUpdate mMapStatusUpdate =
	// MapStatusUpdateFactory.newLatLngZoom(result.getLocation(), 17.0f);
	// mBaiduMap.setMapStatus(mMapStatusUpdate);
	// }

	/*
	 * private void newpop() {
	 * 
	 * mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
	 * 
	 * @Override public boolean onMarkerClick(Marker marker) {
	 * 
	 * View view =
	 * LayoutInflater.from(getApplicationContext()).inflate(R.layout.
	 * popupwindow, null);
	 * 
	 * String[] spots = { "宽巷子", "窄巷子", "井巷子", "其他" }; final int[] imgs = {
	 * R.drawable.homepage, R.drawable.qingyanggong, R.drawable.spotplace_home,
	 * R.drawable.ic_launcher }; final ImageView imageViewDisplay = (ImageView)
	 * view.findViewById(R.id.imageViewDisplay); ListView lv = (ListView)
	 * view.findViewById(R.id.listView); ArrayAdapter adapter = new
	 * MyArrayAdapter(getApplicationContext(), -1, spots);
	 * lv.setAdapter(adapter); lv.setOnItemClickListener(new
	 * OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> parent, View view, int
	 * position, long id) { imageViewDisplay.setImageResource(imgs[position]); }
	 * });
	 * 
	 * ImageView playImageView = (ImageView)
	 * view.findViewById(R.id.playerImageView);
	 * 
	 * final LatLng ll = marker.getPosition(); Point p =
	 * mBaiduMap.getProjection().toScreenLocation(ll); p.y -= 47; LatLng llInfo
	 * = mBaiduMap.getProjection().fromScreenLocation(p); InfoWindow mInfoWindow
	 * = new InfoWindow(view, llInfo, 100); MapStatusUpdate m =
	 * MapStatusUpdateFactory.newLatLng(ll); mBaiduMap.setMapStatus(m);
	 * mBaiduMap.showInfoWindow(mInfoWindow);
	 * 
	 * 
	 * if (!FULL_SCREEN) { setBaiduMapFullScreen(true); }
	 * 
	 * return false; } });
	 * 
	 * mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
	 * 
	 * @Override public void onMapStatusChangeStart(MapStatus arg0) {
	 * //mBaiduMap.hideInfoWindow(); }
	 * 
	 * @Override public void onMapStatusChangeFinish(MapStatus arg0) {
	 * 
	 * }
	 * 
	 * @Override public void onMapStatusChange(MapStatus arg0) {
	 * 
	 * } }); }
	 */

	/**
	 * 首页需要从服务器上获取最新推荐的景区图片
	 */
	private void fetchImageSource(final ArrayList<ImageView> mItems){
		
		String url = "http://172.16.0.138:8080/AndTravel/spot/getallspot/";
	
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (response.getString("code").equals("1")) {
			        	JSONArray contentArray = response.getJSONArray("message");
			        	if(contentArray.length() > 0){
			        		for(int i = 0; i < contentArray.length(); i++){	
		        				String imageUrl = contentArray.get(i).toString();
		        				Log.e(LOG_TAG, "从服务器获取到图片的URL：" + imageUrl);
		        				if(i==0){
		        					mTravelImageUrl = imageUrl;
		        				}else{
		        					/*把景区的第一张图存起来，用于存储评论、收藏景区图片*/
		        				}
		        				
		        				ImageView image = new ImageView(SpotPlaceActivity.this);
		        				image.setScaleType(ScaleType.FIT_XY);
		        				Glide.with(SpotPlaceActivity.this).load(imageUrl).placeholder(R.drawable.loading).crossFade(1000).centerCrop().into(image);  
		        				mItems.add(image);
		        				
			        		}
			        		
			        		//handle.sendEmptyMessage(DOWN_LOAD_IMAGE);
	
						} else {
							//Toast.makeText(StartActivity.this,"首页从服务器上获取的图片位空！！！", Toast.LENGTH_SHORT).show();
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
	
	private class MyPagerAdapter extends PagerAdapter {

		private ArrayList<ImageView> mItems = null;
		private Context context;

		public MyPagerAdapter(Context context) {
			this.context = context;

			//int[] res = { R.drawable.wuhouci1, R.drawable.wuhouci2, R.drawable.wuhouci3 };

			mItems = new ArrayList<ImageView>();
			fetchImageSource(mItems);
			
		}

		@Override
		public ImageView instantiateItem(View container, int position) {
			((ViewPager) container).addView(mItems.get(position));
			return mItems.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			handler.sendEmptyMessage(MESSAGE_WHAT_CHANGED);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
	
	private void pop() {
		int blank_w = 180, hight = 500;
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int w = displayMetrics.widthPixels;

		LayoutInflater inflater = (LayoutInflater) (this).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.popupwindow, null);
		v.setAlpha(128);// 半透明

		String[] spots = { "武侯祠1", "武侯祠2", "武侯祠3" };
		final int[] imgs = { R.drawable.wuhouci1, R.drawable.wuhouci2, R.drawable.wuhouci3 };
		final ImageView imageViewDisplay = (ImageView) v.findViewById(R.id.imageViewDisplay);
		ListView lv = (ListView) v.findViewById(R.id.listView);
		ArrayAdapter adapter = new MyArrayAdapter(this, -1, spots);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				imageViewDisplay.setImageResource(imgs[position]);
			}
		});

		final ImageView playImageView = (ImageView) v.findViewById(R.id.playerImageView);
		playImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPlaying) {
					stopAudio();
					playImageView.setImageResource(R.drawable.stop);
				} else {
					playAudio();
					playImageView.setImageResource(R.drawable.play);
				}
			}
		});

		PopupWindow popWindow = new PopupWindow(this);
		
		//popWindow.setBackgroundDrawable(this.getDrawable(R.drawable.round));
		//ColorDrawable dw = new ColorDrawable(0x757575);
		//popWindow.setBackgroundDrawable(dw);
		popWindow.setFocusable(true);
		popWindow.setTouchable(true);// PopupWindow可触摸
		popWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
		popWindow.setAnimationStyle(android.R.anim.fade_in); // -1 缺省
		popWindow.setWidth(w - blank_w);
		popWindow.setHeight(hight);

		popWindow.setContentView(v);

		popWindow.showAtLocation(getWindow().getDecorView(), Gravity.NO_GRAVITY, blank_w / 2, hight / 2);
	}

	protected void playAudio() {
		Intent intent = new Intent("chinamobile.iot.andtravels.BLEScanService.UserAction");
		intent.putExtra("usrAction", "start");
		sendBroadcast(intent);

		isPlaying = true;
	}

	protected void stopAudio() {
		Intent intent = new Intent("chinamobile.iot.andtravels.BLEScanService.UserAction");
		intent.putExtra("usrAction", "stop");
		sendBroadcast(intent);

		isPlaying = false;
	}

	private class MyArrayAdapter extends ArrayAdapter {

		private String[] data;
		private LayoutInflater mLayoutInflater;

		public MyArrayAdapter(Context context, int resource, String[] objects) {
			super(context, resource, objects);
			data = objects;
			mLayoutInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_1, null);
			TextView text = (TextView) convertView.findViewById(android.R.id.text1);
			text.setText((position + 1) + " " + getItem(position));
			text.setTextColor(Color.WHITE);

			return convertView;
		}

		@Override
		public String getItem(int pos) {
			return data[pos];
		}

		@Override
		public int getCount() {
			return data.length;
		}
	}

	private void baiduMapOnCreate(GeoCodeResult result) {

		// MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		// mBaiduMap.setMapStatus(msu);

		LatLng latlng = result.getLocation();

		Log.d("百度返回的武侯祠坐标", latlng.latitude + " " + latlng.longitude);

		LatLng newLL = new LatLng(WuHouCiGeoPoint.lat, WuHouCiGeoPoint.lng);

		addOverlay(newLL);

		// coords(latlng);

		// mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
		// public boolean onMarkerClick(Marker marker) {
		// Button button = new Button(getApplicationContext());
		// button.setBackgroundResource(R.drawable.popup);
		// OnInfoWindowClickListener listener = null;
		// if (marker == mMarkerA || marker == mMarkerD) {
		// button.setText("更改位置");
		// listener = new OnInfoWindowClickListener() {
		// public void onInfoWindowClick() {
		// LatLng ll = marker.getPosition();
		// LatLng llNew = new LatLng(ll.latitude + 0.005, ll.longitude + 0.005);
		// marker.setPosition(llNew);
		// mBaiduMap.hideInfoWindow();
		// }
		// };
		// LatLng ll = marker.getPosition();
		// mInfoWindow = new
		// InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47,
		// listener);
		// mBaiduMap.showInfoWindow(mInfoWindow);
		// } else if (marker == mMarkerB) {
		// button.setText("更改图标");
		// button.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// marker.setIcon(bd);
		// mBaiduMap.hideInfoWindow();
		// }
		// });
		// LatLng ll = marker.getPosition();
		// mInfoWindow = new InfoWindow(button, ll, -47);
		// mBaiduMap.showInfoWindow(mInfoWindow);
		// } else if (marker == mMarkerC) {
		// button.setText("删除");
		// button.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// marker.remove();
		// mBaiduMap.hideInfoWindow();
		// }
		// });
		// LatLng ll = marker.getPosition();
		// mInfoWindow = new InfoWindow(button, ll, -47);
		// mBaiduMap.showInfoWindow(mInfoWindow);
		// }
		// return true;
		// }
		// });
	}

	/*
	private void coords(LatLng latlng) {
		RequestQueue mQueue = Volley.newRequestQueue(this);

		String url = Utils.getBaiduCoordURL(5, 5, latlng.longitude, latlng.latitude);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Gson gson = new Gson();
				BaiduMapCoord coord = gson.fromJson(response + "", BaiduMapCoord.class);
				LatLng llNew = new LatLng(coord.result[0].y, coord.result[0].x);
				Log.d("调校状态:" + coord.status, coord.result[0].y + " " + coord.result[0].x);

				addOverlay(llNew);

				// MapStatusUpdate u = MapStatusUpdateFactory
				// .newLatLng(llNew);
				// mBaiduMap.setMapStatus(u);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		});

		mQueue.add(jsonObjectRequest);
	}
	*/

	public void addOverlay(LatLng ll) {

		// 添加气泡
		MarkerOptions markerOptions = new MarkerOptions().position(ll)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_mark));
		Marker marker = (Marker) (mBaiduMap.addOverlay(markerOptions));

		double lat = ll.latitude;
		double lng = ll.longitude;
		// double delta = 0.003;
		LatLng northeast = new LatLng(lat + 0.00252, lng + 0.002);
		LatLng southwest = new LatLng(lat - 0.0022, lng - 0.0028);
		LatLngBounds bounds = new LatLngBounds.Builder().include(northeast).include(southwest).build();

		Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.wuhouci);
		
		BitmapDescriptor bdGround = BitmapDescriptorFactory.fromBitmap(bmp);
		
		GroundOverlayOptions goGround = new GroundOverlayOptions();
		goGround.positionFromBounds(bounds);
		goGround.image(bdGround);
		//goGround.transparency(0.8f);

		mBaiduMap.addOverlay(goGround);

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				pop();
				setBaiduMapFullScreen(true);
				return false;
			}
		});

		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(ll, 16.0f);
		mBaiduMap.setMapStatus(mapStatusUpdate);

		// Log.d("武侯祠坐标 2", bounds.getCenter().latitude + " " +
		// bounds.getCenter().longitude);

		// coords(bounds.getCenter().latitude, bounds.getCenter().longitude);

		// mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
		// public void onMarkerDrag(Marker marker) {
		//
		// }
		//
		// public void onMarkerDragEnd(Marker marker) {
		// Toast.makeText(getApplication(),
		// "拖拽结束，新位置：" + marker.getPosition().latitude + ", " +
		// marker.getPosition().longitude,
		// Toast.LENGTH_LONG).show();
		// }
		//
		// public void onMarkerDragStart(Marker marker) {
		//
		// }
		// });
	}
	
	@Override
	public	boolean	onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK && FULL_SCREEN){
			this.setBaiduMapFullScreen(false);
			this.setBaidMapZoomSmall();
			return	false;
		}
		
		if(keyCode == KeyEvent.KEYCODE_BACK && !FULL_SCREEN){
			return	super.onKeyDown(keyCode, event);
		}
		
		return	false;
	}
	
	public class ImageBitmapCache implements ImageCache {  
		  
	    private LruCache<String, Bitmap> mCache;  
	  
	    public ImageBitmapCache() {  
	        int maxSize = 1 * 1024 * 1024;  
	        mCache = new LruCache<String, Bitmap>(maxSize) {  
	            @Override  
	            protected int sizeOf(String key, Bitmap bitmap) {  
	                return bitmap.getRowBytes() * bitmap.getHeight();  
	            }  
	        };  
	    }  
	  
	    @Override  
	    public Bitmap getBitmap(String url) {  
	        return mCache.get(url);  
	    }  
	  
	    @Override  
	    public void putBitmap(String url, Bitmap bitmap) { 
	        mCache.put(url, bitmap);  
	    }  
	  
	} 
	
	public class UserLoginReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String intentAction = intent.getAction();
			if(intentAction.equals("chinamobile.iot.andtravels.SetLogin")){
				Log.i(LOG_TAG, "接收到广播：用户已经登录了！！！");
				mStrUserID = getIntent().getStringExtra("UserID");
				
			}else if(intentAction.equals(Constants.ACTION_CHINAMOBILE_IOT_ANDTRAVELS_BROADCAST) ){
				
			}else{
				
			}
			
		}
	}
}

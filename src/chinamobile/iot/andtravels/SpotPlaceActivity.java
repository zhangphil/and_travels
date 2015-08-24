package chinamobile.iot.andtravels;

import java.util.ArrayList;
import java.util.HashMap;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.widget.ImageView.ScaleType;

import chinamobile.iot.andtravels.utils.Utils;

public class SpotPlaceActivity extends FragmentActivity implements OnGetGeoCoderResultListener {

	private MyFragmentPagerAdapter mPagerAdapter;
	private ViewPager mViewPager;

	private Handler handler;
	private final int MESSAGE_WHAT_CHANGED = 100;

	private ArrayList<HashMap<String, Object>> mArrayList = null;
	private final String FRAGMENT = "fragment_tag";

	private GeoCoder mSearch = null;
	private BaiduMap mBaiduMap = null;
	private MapView mMapView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SDKInitializer.initialize(getApplicationContext());

		View view = this.getLayoutInflater().inflate(R.layout.spot_place, null);
		setContentView(view);

		baiduMap();

		ImageView backImageView = (ImageView) view.findViewById(R.id.back_ImageView);
		backImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				back();
			}
		});

		mArrayList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 4; i++) {
			Fragment fragment = new ImageFragment();
			add(fragment);
		}

		mViewPager = (ViewPager) view.findViewById(R.id.viewpager_head);
		mPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				set(pos);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		final CircleIndicatorView mCircleIndicatorView = (CircleIndicatorView) view
				.findViewById(R.id.circleIndicatorView);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case MESSAGE_WHAT_CHANGED:
					mCircleIndicatorView.setCircleCount(mPagerAdapter.getCount());
					mCircleIndicatorView.setCircleSelectedPosition(mViewPager.getCurrentItem());
					// mCircleIndicatorView.setSelectedCircleRadius(7);
					mCircleIndicatorView.drawCircleView();

					break;
				}
			};
		};

		// 初始化选择第一项
		if (mPagerAdapter.getCount() > 0) {
			set(0);
		}
	}

	private void baiduMap() {

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

		// 不显示缩放按钮控制栏
		mMapView.showZoomControls(false);

		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);

		// 不晓得为啥，反正必须不能太快调用百度地图的定位搜索功能，
		// 需要先暂停几秒钟才可以正常工作
		// 如果直接调用，则返回为空值。
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				locationTo();
			}
		}, 3000);
	}

	private void locationTo() {
		mSearch.geocode(new GeoCodeOption().city("成都").address("宽窄巷子"));
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

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}

		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)));

		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(result.getLocation(), 17.0f);
		mBaiduMap.setMapStatus(mMapStatusUpdate);

		// mBaiduMap.animateMapStatus(mMapStatusUpdate,5000);
		// String strInfo = String.format("纬度：%f 经度：%f",
		// result.getLocation().latitude, result.getLocation().longitude);
		// Toast.makeText(GeoCoderDemo.this, strInfo, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {

	}

	private void back() {
		Utils.onKeyEvent(KeyEvent.KEYCODE_BACK);
	}

	private void add(Fragment fragment) {

		HashMap<String, Object> map = new HashMap<String, Object>();

		Bundle args = new Bundle();
		fragment.setArguments(args);
		map.put(FRAGMENT, fragment);

		mArrayList.add(map);
	}

	private void set(int pos) {
		mViewPager.setCurrentItem(pos, true);
		handler.sendEmptyMessage(MESSAGE_WHAT_CHANGED);
	}

	private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			return (Fragment) mArrayList.get(pos).get(FRAGMENT);
		}

		@Override
		public int getItemPosition(Object object) {
			return FragmentPagerAdapter.POSITION_NONE;
		}

		@Override
		public int getCount() {
			return mArrayList.size();
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			handler.sendEmptyMessage(MESSAGE_WHAT_CHANGED);
		}
	}

	private class ImageFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			ImageView iv = new ImageView(getContext());
			iv.setImageResource(R.drawable.spotplace_home);
			iv.setScaleType(ScaleType.CENTER_CROP);
			return iv;
		}
	}
}

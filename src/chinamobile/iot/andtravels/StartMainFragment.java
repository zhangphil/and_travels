package chinamobile.iot.andtravels;

import java.util.ArrayList;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class StartMainFragment extends Fragment implements OnPageChangeListener{

	private static final String LOG_TAG = "StartMainFragment";
	private ViewPager viewPager;
	private RadioGroup viewDaoLanGroup;
	private ImageAdapter mImageAdapter;
	private Handler handler;
	private final int MESSAGE_WHAT_CHANGED = 100;

	private boolean mIsLogin = false;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.start_main, null);
		
		viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		final CircleIndicatorView mCircleIndicatorView =  (CircleIndicatorView) view.findViewById(R.id.circleIndicatorView);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case MESSAGE_WHAT_CHANGED:
					mCircleIndicatorView.setCircleCount(mImageAdapter.getCount());
					mCircleIndicatorView.setCircleSelectedPosition(viewPager.getCurrentItem());
					mCircleIndicatorView.setCircleSelectedColor(Color.RED);
					mCircleIndicatorView.drawCircleView();
					

					break;
				}
			};
		};
		
		
		ImageView view1 = (ImageView) inflater.inflate(R.layout.viewpage_item, null).findViewById(R.id.imageViewPage);
		ImageView view2 = (ImageView) inflater.inflate(R.layout.viewpage_item02, null)
				.findViewById(R.id.imageViewPage02);
		ImageView view3 = (ImageView) inflater.inflate(R.layout.viewpage_item03, null)
				.findViewById(R.id.imageViewPage03);
		ImageView view4 = (ImageView) inflater.inflate(R.layout.viewpage_item04, null)
				.findViewById(R.id.imageViewPage04);

		view1.setImageResource(R.drawable.start_view01);
		view2.setImageResource(R.drawable.start_view01);
		view3.setImageResource(R.drawable.start_view01);
		view4.setImageResource(R.drawable.start_view01);

		ArrayList<ImageView> views = new ArrayList<ImageView>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);

		mImageAdapter = new ImageAdapter(views);
		viewPager.setAdapter(mImageAdapter);
		viewPager.setOnPageChangeListener(this);

		viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
	
		initDaoLanView(view);
		
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

					if (!mIsLogin) {
						Intent daoLanIntent = new Intent();
						daoLanIntent.setAction("chinamobile.iot.andtravels.communication.BeaconService");
						daoLanIntent.setPackage(getActivity().getPackageName());
						getActivity().startService(daoLanIntent);
					} else {
						//提示用户需要付费才能使用
						

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
		//setImageBackground(arg0 % mImageViews.length); 
	}



	/*
	 * //@Override public boolean onKeyDown(int keyCode, KeyEvent event) { //
	 * TODO Auto-generated method stub if(keyCode == KeyEvent.KEYCODE_BACK){
	 * ToQuitTheApp(); return false; } else{ return super.onKeyDown(keyCode,
	 * event); }
	 * 
	 * 
	 * }
	 */

	

}

package chinamobile.iot.andtravels;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import chinamobile.iot.andtravels.utils.Utils;

public class LvYouDaoLan extends Fragment {

	private MyFragmentPagerAdapter mPagerAdapter;
	private ViewPager mViewPager;

	private Handler handler;
	private final int MESSAGE_WHAT_CHANGED = 100;

	//装载若干张展示用的图片。
	//private ArrayList<HashMap<String, Object>> mArrayList = null;
	
	//private final String FRAGMENT = "fragment_tag";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
//	private void add(Fragment fragment) {
//
//		HashMap<String, Object> map = new HashMap<String, Object>();
//
//		Bundle args = new Bundle();
//		fragment.setArguments(args);
//		map.put(FRAGMENT, fragment);
//
//		mArrayList.add(map);
//	}

//	private class ImageFragment extends Fragment {
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//			ImageView iv = new ImageView(getContext());
//			iv.setImageResource(R.drawable.homepage);
//			iv.setScaleType(ScaleType.CENTER_CROP);
//			return iv;
//		}
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.lvyoudaohang, null);

		ImageView backImageView = (ImageView) view.findViewById(R.id.back_ImageView);
		backImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				back();
			}
		});

		mViewPager = (ViewPager) view.findViewById(R.id.viewpager_head);
		//mPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());
		mPagerAdapter = new MyFragmentPagerAdapter();
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
					mCircleIndicatorView.drawCircleView();

					break;
				}
			};
		};

		// 初始化选择第一项
		if (mPagerAdapter.getCount() > 0) {
			set(0);
		}

		ListView lv = (ListView) view.findViewById(R.id.listView);
		ArrayAdapter mArrayAdapter = new MyArrayAdapter(this.getContext(), -1);
		lv.setAdapter(mArrayAdapter);
		final Context mContext = this.getContext();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(mContext, SpotPlaceActivity.class);
				startActivity(intent);
			}
		});

		return view;
	}

	private void back() {
		Utils.onKeyEvent(KeyEvent.KEYCODE_BACK);
	}

	private void set(int pos) {
		mViewPager.setCurrentItem(pos, true);
		handler.sendEmptyMessage(MESSAGE_WHAT_CHANGED);
	}

	
	private class MyFragmentPagerAdapter extends	PagerAdapter {

		private ArrayList<ImageView> mItems = null;
		
		public	MyFragmentPagerAdapter(){
			mItems=new ArrayList<ImageView>();
			int[] res={R.drawable.cd1,R.drawable.cd2,R.drawable.cd3};
			for(int i=0;i<3;i++){
				ImageView image=new ImageView(getContext());
				image.setImageResource(res[i]);
				image.setScaleType(ScaleType.CENTER_CROP);
				mItems.add(image);
			}
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
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			handler.sendEmptyMessage(MESSAGE_WHAT_CHANGED);
		}
	}
	
	/*
	private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			return (Fragment) mArrayList.get(pos).get(FRAGMENT);
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
	*/

	private class MyArrayAdapter extends ArrayAdapter {

		private LayoutInflater mLayoutInflater;

		public MyArrayAdapter(Context context, int resource) {
			super(context, resource);

			mLayoutInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = mLayoutInflater.inflate(R.layout.lvyoudaolan_item, null);

			ImageView imageViewPlace = (ImageView) convertView.findViewById(R.id.imageViewPlace);
			imageViewPlace.setImageBitmap(Utils.getResize(getContext(), R.drawable.qingyanggong, 2));

			return convertView;
		}

		@Override
		public int getCount() {
			return 5;
		}
	}
}

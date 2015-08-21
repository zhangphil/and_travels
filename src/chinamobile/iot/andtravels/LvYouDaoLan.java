package chinamobile.iot.andtravels;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LvYouDaoLan extends Fragment {

	private MyFragmentPagerAdapter mPagerAdapter;
	private ViewPager mViewPager;

	private Handler handler;
	private final int MESSAGE_WHAT_CHANGED = 100;

	private ArrayList<HashMap<String, Object>> mArrayList = null;
	private final String FRAGMENT = "fragment_tag";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mArrayList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 10; i++) {
			Fragment fragment = new TestFragment();
			add(fragment);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.lvyoudaohang, null);

		mViewPager = (ViewPager) view.findViewById(R.id.viewpager_head);
		mPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());
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

		final TextView circleIndicatorView_TextView = (TextView) view.findViewById(R.id.circleIndicatorView_TextView);
		final CircleIndicatorView mCircleIndicatorView = (CircleIndicatorView) view
				.findViewById(R.id.circleIndicatorView);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case MESSAGE_WHAT_CHANGED:
					mCircleIndicatorView.setCircleCount(mPagerAdapter.getCount());
					mCircleIndicatorView.setCircleSelectedPosition(mViewPager.getCurrentItem());
					mCircleIndicatorView.setSelectedCircleRadius(7);
					mCircleIndicatorView.setCircleUnSelectedColor(Color.RED);
					mCircleIndicatorView.setCircleUnSelectedColor(Color.GREEN);
					mCircleIndicatorView.drawCircleView();

					String s= "<br>成都，一个来了不想走的城市</br>气候宜人，风景迷人 "+mViewPager.getCurrentItem();
					circleIndicatorView_TextView.setText(Html.fromHtml(s));

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

		return view;
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

	//
	// 仅仅用于测试的Fragment，在ViewPager中加载
	//
	public static class TestFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			TextView tv = new TextView(getActivity());

			String str = "";
			for (int i = 0; i < 500; i++)
				str = str + i + "--";

			tv.setTextColor(Color.LTGRAY);
			tv.setText(str);

			return tv;
		}
	}

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

			return convertView;
		}

		@Override
		public int getCount() {
			return 10;
		}
	}
}

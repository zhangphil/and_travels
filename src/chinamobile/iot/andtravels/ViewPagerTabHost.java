package chinamobile.iot.andtravels;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ViewPagerTabHost extends Fragment {

	private final String TAG_LOG = "ViewPagerTabHost";
	private MyFragmentPagerAdapter mPagerAdapter;
	private ViewPager mViewPager;
	private LinearLayout mLinearLayout;
	protected int mCurViewPos = 0;

	public ViewPagerTabHost(int pos){
		mCurViewPos = pos;
	}
	protected Fragment getFragmentAt(int pos) {
		return null;
	}

	protected View getIndicatorAt(int pos) {
		return null;
	}

	protected int getItemsCount() {
		return 0;
	}

	protected void onOnTabIndicatorSelected(View view, int pos) {
	}

	protected void onOnTabIndicatorUnSelected(View view, int pos) {
	}

	public void notifyDataSetChanged() {
		mPagerAdapter.notifyDataSetChanged();
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.viewpager_tabhost, null);

		mViewPager = (ViewPager) mView.findViewById(R.id.viewpager);

		mLinearLayout = (LinearLayout) mView.findViewById(R.id.indicator_LinearLayout);
		addIndicators();
		// 初始化，第一项被选中
		setIndicatorViewSelected(mCurViewPos);
		
		Log.e(TAG_LOG, "当前显示page is" + mCurViewPos);

		mPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(mCurViewPos);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				setIndicatorViewSelected(pos);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		//set(mCurViewPos);
		
		return mView;
	}

	private void addIndicators() {
		// 添加Tab选项卡
		for (int i = 0; i < getItemsCount(); i++) {
			View v = getIndicatorAt(i);
			addIndicatorItem(v, i);
		}
	}

	private void addIndicatorItem(View view, final int index) {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1);
		mLinearLayout.addView(view, index, params);
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				set(index);
			}
		});
	}

	// 在这里设置被选中时候选项卡变化的效果
	private void setIndicatorViewSelected(int pos) {
		// 某一个选项卡被选中时候的处理。

		for (int i = 0; i < mLinearLayout.getChildCount(); i++) {

			if (i == pos) {
				View v = mLinearLayout.getChildAt(i);
				onOnTabIndicatorSelected(v, i);
			} else {
				View v = mLinearLayout.getChildAt(i);
				onOnTabIndicatorUnSelected(v, i);
			}
		}
	}

	private void set(int pos) {
		mViewPager.setCurrentItem(pos, true);
		setIndicatorViewSelected(pos);
	}

	private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			return getFragmentAt(pos);
		}

		@Override
		public int getCount() {
			return getItemsCount();
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			// addIndicators();
		}
	}
}
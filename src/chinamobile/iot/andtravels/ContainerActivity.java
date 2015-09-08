package chinamobile.iot.andtravels;

import java.util.ArrayList;

import com.aprilbrother.aprilbrothersdk.BeaconManager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContainerActivity extends FragmentActivity {

	private String TAG_LOG = "ContainerActivity";
	private int  mCurViewPos = 0;
	private Fragment newFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment);
 
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();//("curViewPos", mCurViewPos);
		mCurViewPos = bundle.getInt("curViewPos");
		
		Log.e(TAG_LOG, "当前显示page is" + mCurViewPos);
		
		newFragment = new MyViewPagerTabHost(mCurViewPos);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment, newFragment);
		transaction.commit();
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	
	public static class MyViewPagerTabHost extends ViewPagerTabHost {

		public MyViewPagerTabHost(int pos) {
			super(pos);
			// TODO Auto-generated constructor stub
		}

		private ArrayList<Fragment> mArrayList;
		private String[] tab_cards;
		private LayoutInflater mLayoutInflater;

		private int[] icon_selected = { R.drawable.a_selected, R.drawable.b_selected,R.drawable.c_selected, R.drawable.d_selected };
		private int[] icon_unselected = { R.drawable.a_unselected, R.drawable.b_unselected,R.drawable.c_unselected, R.drawable.d_unselected, };

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mArrayList = new ArrayList<Fragment>();

			mArrayList.add(new LvYouDaoLan());
			mArrayList.add(new YouJiFragment());
			mArrayList.add(new PersonSettingFragment());
			mArrayList.add(new SettingFragment());

			Resources res = getResources();
			tab_cards = res.getStringArray(R.array.tab_cards);

			mLayoutInflater = LayoutInflater.from(getContext());
			
		}

		@Override
		protected Fragment getFragmentAt(int pos) {
			return mArrayList.get(pos);
		}

		@Override
		protected View getIndicatorAt(final int pos) {
			View v = mLayoutInflater.inflate(R.layout.tab_card, null);

			ImageView iv = (ImageView) v.findViewById(R.id.imageView);
			iv.setImageResource(icon_unselected[pos]);
			TextView text = (TextView) v.findViewById(R.id.textView);
			text.setText(tab_cards[pos]);

			return v;
		}

		@Override
		protected int getItemsCount() {
			return 4;
		}

		@Override
		public void onStart() {
			super.onStart();
			super.notifyDataSetChanged();
		}

		@Override
		public void onOnTabIndicatorSelected(View view, int pos) {
			ImageView iv = (ImageView) view.findViewById(R.id.imageView);
			iv.setImageResource(icon_selected[pos]);
		}

		@Override
		public void onOnTabIndicatorUnSelected(View view, int pos) {
			ImageView iv = (ImageView) view.findViewById(R.id.imageView);
			iv.setImageResource(icon_unselected[pos]);
		}
		
	}
}

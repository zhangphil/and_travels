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
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContainerActivity extends FragmentActivity {

<<<<<<< HEAD
	private String TAG_LOG = "ContainerActivity";
	private int  mCurViewPos = 0;
=======
	private static final int REQUEST_ENABLE_BT = 1234;
	private BeaconManager beaconManager = new BeaconManager(this);
>>>>>>> master

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment);
 
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();//("curViewPos", mCurViewPos);
		mCurViewPos = bundle.getInt("curViewPos");
		
		Log.e(TAG_LOG, "当前显示page is" + mCurViewPos);
		
		Fragment newFragment = new MyViewPagerTabHost(mCurViewPos);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment, newFragment);
		transaction.commit();
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
			Fragment fragment = null;
			if(mCurViewPos > 0 && mCurViewPos < mArrayList.size()){
				fragment = mArrayList.get(1);
			}else{
				fragment = mArrayList.get(0);
			}
			
			if(fragment == null){
				fragment = mArrayList.get(0);
			}
			
			return fragment;
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
<<<<<<< HEAD
		
		public void setCurViewPage(int pos){
			
		}

=======
>>>>>>> master
	}
}

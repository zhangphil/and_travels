package chinamobile.iot.andtravels;

import java.util.ArrayList;

import com.aprilbrother.aprilbrothersdk.BeaconManager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ContainerActivity extends FragmentActivity {

	private static final int REQUEST_ENABLE_BT = 1234;
	//private BluetoothAdapter mBluetoothAdapter;
	private BeaconManager beaconManager = new BeaconManager(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment);

		Fragment newFragment = new MyViewPagerTabHost();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment, newFragment);
		transaction.commit();
		
		//开启蓝牙
		startBle();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				Log.e("Tag", "蓝牙设备打开了,启动服务开始扫描");

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
	
	public static class MyViewPagerTabHost extends ViewPagerTabHost {

		private ArrayList<Fragment> mArrayList;
		private String[] tab_cards;
		private LayoutInflater mLayoutInflater;

		private int[] icon_selected = { R.drawable.a_selected,R.drawable.c_selected,R.drawable.d_selected };
		private int[] icon_unselected = { R.drawable.a_unselected,R.drawable.c_unselected,R.drawable.d_unselected, };

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mArrayList = new ArrayList<Fragment>();

			mArrayList.add(new LvYouDaoLan());
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
		protected View getIndicatorAt(final	int pos) {
			View v = mLayoutInflater.inflate(R.layout.tab_card, null);
			ImageView iv = (ImageView) v.findViewById(R.id.imageView);
			iv.setImageResource(icon_unselected[pos]);
			TextView text = (TextView) v.findViewById(R.id.textView);
			text.setText(tab_cards[pos]);
			
			return v;
		}

		@Override
		protected int getItemsCount() {
			return 3;
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

package chinamobile.iot.andtravels;

import com.aprilbrother.aprilbrothersdk.BeaconManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aprilbrother.aprilbrothersdk.BeaconManager;

import chinamobile.iot.andtravels.BLEScanService;
import chinamobile.iot.andtravels.ContainerActivity.MyViewPagerTabHost;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aprilbrother.aprilbrothersdk.Beacon;
import com.aprilbrother.aprilbrothersdk.BeaconManager.MonitoringListener;
import com.aprilbrother.aprilbrothersdk.BeaconManager.RangingListener;
import com.aprilbrother.aprilbrothersdk.Region;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class StartActivity extends FragmentActivity {

	private final String LOG_TAG = "StartActivity";
	
	private static final int REQUEST_ENABLE_BT = 1234;
	private BluetoothAdapter mBluetoothAdapter;
	private BeaconManager beaconManager = new BeaconManager(this);
	private Fragment newFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.start_main_fragment);
		
		newFragment = new StartMainFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment, newFragment);
		transaction.commit();
		
		startBle();
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.e(LOG_TAG, "onResume 重新绘制fragment");
		newFragment = new StartMainFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment, newFragment);
		transaction.commit();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				Log.e("Tag", "蓝牙设备打开了,开始扫描");

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

	@Override
	protected void onDestroy() {

		Intent daoLanIntent = new Intent();
		daoLanIntent.setAction("chinamobile.iot.andtravels.communication.BeaconService");
		daoLanIntent.setPackage(getPackageName());
		stopService(daoLanIntent);

		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}

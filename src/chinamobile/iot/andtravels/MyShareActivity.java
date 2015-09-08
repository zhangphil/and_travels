package chinamobile.iot.andtravels;

import com.aprilbrother.aprilbrothersdk.BeaconManager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class MyShareActivity extends FragmentActivity {

	private final String LOG_TAG = "MyShareActivity";
	
	private Fragment newFragment;
	private int mCurViewPos = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.my_share_activity);
		
		openFragment(mCurViewPos);
		
		
	}

	private void openFragment(int pos){
		
		switch(pos){
			case 0:{
				newFragment = new MyShareFragment();
				break;
			}
			case 1:{
				break;
			}
			case 2:{
				break;
			}
			case 3:{
				break;
			}
			case 4:{
				break;
			}
			default:{
				break;
			}
		}
		
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment, newFragment);
		transaction.commit();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}

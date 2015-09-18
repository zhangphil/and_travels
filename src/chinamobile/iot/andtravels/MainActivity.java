package chinamobile.iot.andtravels;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import chinamobile.iot.andtravels.utils.Constants;

public class MainActivity extends Activity {

	private LocationClient mLocationClient = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		ListView lv = (ListView) findViewById(R.id.listView);
		String[] city = this.getResources().getStringArray(R.array.city);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				android.R.id.text1, city);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView tv = (TextView) view;
				String s = tv.getText() + "";
				if (s.equals("成都")) {
					goToContainerActivity();
				}
			}
		});

		LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.chooseCity);
		mLinearLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goToContainerActivity();
			}
		});

		MyLocationListenner myListener = new MyLocationListenner();
		mLocationClient = new LocationClient(this);
		LocationClientOption option = new LocationClientOption();
		option.setIsNeedAddress(true);
		option.setAddrType("all");
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.start();
	}

	@Override
	public void onDestroy() {
		mLocationClient.stop();
		super.onDestroy();
	}

	private void goToContainerActivity() {
		Intent intent = new Intent(this, ContainerActivity.class);
		int pos = 0;
		intent.putExtra("curViewPos", pos);
		startActivity(intent);
	}

	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			try {
				String province = location.getProvince();
				String city = location.getCity();

				Intent intent = new Intent();
				intent.setAction(Constants.ACTION_CHINAMOBILE_IOT_ANDTRAVELS_BROADCAST);
				intent.putExtra(Constants.LOCATION_CITY, city);
				sendBroadcast(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

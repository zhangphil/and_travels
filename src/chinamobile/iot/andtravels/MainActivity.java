package chinamobile.iot.andtravels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.title);
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.and_travels_titlebar));

		ListView lv = (ListView) findViewById(R.id.listView);
		String[] city = this.getResources().getStringArray(R.array.city);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				android.R.id.text1, city);
		lv.setAdapter(adapter);

		final Activity mActivity = this;

		LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.chooseCity);
		mLinearLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, ContainerActivity.class);
				mActivity.startActivity(intent);
			}
		});
	}

}

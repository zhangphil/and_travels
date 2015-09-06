package chinamobile.iot.andtravels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView tv=(TextView)view;
				String s=tv.getText()+"";
				if(s.equals("成都")){
					goToContainerActivity();
				}
			}});

		//final Activity mActivity = this;

		LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.chooseCity);
		mLinearLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goToContainerActivity();
			}
		});
	}
	
	
	private	void	goToContainerActivity(){
		Intent intent = new Intent(this, ContainerActivity.class);
		startActivity(intent);
	}
}

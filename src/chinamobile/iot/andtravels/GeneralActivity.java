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
import android.widget.Toast;

import org.json.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public class GeneralActivity extends FragmentActivity {

	private final String LOG_TAG = "MyShareActivity";
	
	private Fragment newFragment;
	private String mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.my_share_activity);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mTitle = bundle.getString("title");
		openFragment();
		
		
	}

	private void openFragment(){
		if( mTitle.equalsIgnoreCase("我的评论") ){
			newFragment = new MyCommentFragment(mTitle);
		}else{
			newFragment = new GeneralFragment(mTitle);
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
	
	private void GetResource(){
		
		 String url = "http://172.16.0.11:8080/AndTravel/uservalidation/checkidentity/"; 
		 RequestQueue mQueue = Volley.newRequestQueue(this);
		 
		 JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,null, new Response.Listener<JSONObject>() {
		 
		 @Override 
		 public void onResponse(JSONObject response) {
		    Log.d(LOG_TAG, "服务器返回客户信息是否注册"); //此处只处理景点的声音
		    try{
		    	if(response.getString("code").equals("1")){
		    		//处理从服务器上获取的数据
		    		
		 
		           }else{
		        	   
		           }
		     }catch(Exception e){ Log.e(LOG_TAG, e.toString()); }
			 } }, new Response.ErrorListener() {
				 @Override 
				 public void onErrorResponse(VolleyError error) { 
					 Log.e(LOG_TAG,"Login Activity 没有从服务器上获取客户信息失败"); } });
				 
				  	 mQueue.add(jsonObjectRequest);
		 
	}

}

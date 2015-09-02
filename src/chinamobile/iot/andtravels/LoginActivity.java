package chinamobile.iot.andtravels;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private final String LOG_TAG = "LoginActivity";
	private String strPhoneNum;
	private String strPassword;
	private EditText firstEditText;
	private EditText secondEditText;
	private TextView submitTextView;
	private TextView regTextView;
	
	private final LoginActivity mActivity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login);

		firstEditText = (EditText) findViewById(R.id.phoneNum);
		secondEditText = (EditText) findViewById(R.id.Password);
		regTextView = (TextView) findViewById(R.id.login_register);
		submitTextView = (TextView) findViewById(R.id.login);
		
		submitTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 发送客户信息到服务器
				strPhoneNum = firstEditText.toString();
				strPassword = secondEditText.toString();
				if (strPhoneNum == null || strPassword == null) {
					Toast.makeText(LoginActivity.this, "手机号码或密码为空，请输入！", Toast.LENGTH_SHORT).show();
				} else {
					Login();
				}

			}
		});

		regTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mActivity, RegisterActivity.class);
				mActivity.startActivity(intent);

			}
		});

	}

	private void Login() {

		Intent intent = new Intent(mActivity, MainActivity.class);
		mActivity.startActivity(intent);

		/**
		 * String url =
		 * "http://172.16.0.11:8080/AndTravel/uservalidation/checkidentity/" +
		 * strPhoneNum + strPassword; RequestQueue mQueue =
		 * Volley.newRequestQueue(this);
		 * 
		 * JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
		 * null, new Response.Listener<JSONObject>() {
		 * 
		 * @Override public void onResponse(JSONObject response) {
		 *           Log.d(LOG_TAG, "服务器返回客户信息是否注册"); //此处只处理景点的声音 try{
		 *           if(response.getString("code").equals("1")){
		 * 
		 * 
		 *           Intent intent=new Intent(mActivity, MainActivity.class);
		 *           mActivity.startActivity(intent);
		 * 
		 * 
		 * 
		 *           } else{
		 * 
		 *           Toast.makeText(LoginActivity.this,
		 *           "用户名和密码不匹配，请更正！",Toast.LENGTH_SHORT).show(); } }
		 *           catch(Exception e){ Log.e(LOG_TAG, e.toString()); }
		 * 
		 *           } }, new Response.ErrorListener() {
		 * @Override public void onErrorResponse(VolleyError error) { // TODO
		 *           Auto-generated method stub Log.e(LOG_TAG,
		 *           "Login Activity 没有从服务器上获取客户信息失败"); } });
		 * 
		 *           mQueue.add(jsonObjectRequest);
		 * 
		 **/
	}
	
	@Override
	protected void onDestroy(){

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

package chinamobile.iot.andtravels;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
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
	//设置是否和服务器进行通信
	private boolean mTest = false;

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
				strPhoneNum = firstEditText.getText().toString();
				strPassword = secondEditText.getText().toString();
				if (strPhoneNum == null || strPassword == null) {
					Toast.makeText(LoginActivity.this, "手机号码或密码为空，请输入！", Toast.LENGTH_SHORT).show();
				} else {
					if(mTest){
						//广播用户已经登录了
						Intent broadcastIntent = new Intent("chinamobile.iot.andtravels.SetLogin");
						sendBroadcast(broadcastIntent);
						
						Intent intent = new Intent(mActivity, MainActivity.class);
						startActivity(intent);
					}else{
						login(strPhoneNum,strPassword);
					}
					
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


	private void login(String strNum,String strPassword){
		
		String url = "http://172.16.0.138:8080/AndTravel/uservalidation/validate/usingplaintext/";
		url = url + strNum + "/" + strPassword;
		
		Log.e(LOG_TAG, "客户注册的手机号码：" + strNum + "密码：" + strPassword);

		RequestQueue mQueue = Volley.newRequestQueue(this);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.d(LOG_TAG, "服务器返回客户信息验证结果");
				try {
					String message = response.getString("message");
					if (response.getString("code").equals("1")) {
						//广播用户已经登录了
						Intent broadcastIntent = new Intent("chinamobile.iot.andtravels.SetLogin");
						sendBroadcast(broadcastIntent);
						
						Intent intent = new Intent(mActivity, MainActivity.class);
						startActivity(intent);
						
					} else {
						Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Log.e(LOG_TAG, e.toString());
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(LOG_TAG, "error = " + error.toString());
				Log.e(LOG_TAG, "Login Activity 没有从服务器上获取客户信息失败");
			}
		});

		mQueue.add(jsonObjectRequest);

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

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
					try {
						login(strPhoneNum,strPassword);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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

private void login(String strNum,String strPassword) throws JSONException{
		
		String url = "http://172.16.0.138:8080/AndTravel/uservalidation/checkidentity/";
		url = url + strNum + "/" + strPassword;
		
		
	   	HttpGet httpGet = new HttpGet(url);
	   	HttpResponse httpResponse;
	   	try {
			httpResponse = new DefaultHttpClient().execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200)
			{
		        String result = EntityUtils.toString(httpResponse.getEntity());
		        JSONObject jsonObject = new JSONObject(result.toString());
		        
		        int resultCode = jsonObject.getInt("code");
		        String message = jsonObject.getString("message");
		        if(resultCode == -1 ){
		        	Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		        }else if(resultCode == 1){
		        	Intent intent = new Intent(mActivity, MainActivity.class);
					startActivity(intent);
		        }else{
		        	Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		        }
		        
		    }else{
		    	Log.e(LOG_TAG, "向服务器注册用户信息失败");
		    }
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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

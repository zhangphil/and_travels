package chinamobile.iot.andtravels;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
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
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SubmitRegisterActivity extends Activity {

	private final String LOG_TAG = "SubmitRegisterActivity";
	private String mStrPhoneNum, mStrIdentifyCode;
	private EditText mFirstEditText;
	private EditText mSecondEditText;
	private TextView mRegButton;
	private ImageView mBackView;

	private final SubmitRegisterActivity mActivity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_register);

		mFirstEditText = (EditText) findViewById(R.id.password);
		mSecondEditText = (EditText) findViewById(R.id.identifyCode);

		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		mStrPhoneNum = bundle.getString("PhoneNum");
		
		Log.e(LOG_TAG, "获取的手机号码：" + mStrPhoneNum + "验证码：" + mStrIdentifyCode);

		mRegButton = (TextView) findViewById(R.id.register);

		mBackView = (ImageView) findViewById(R.id.back);

		mRegButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				// 发送客户信息到服务器
				String strPassword = mFirstEditText.getText().toString();
				mStrIdentifyCode = mSecondEditText.getText().toString();
				if (strPassword == null || mStrIdentifyCode == null) {
					Toast.makeText(mActivity, "密码或验证码为空，请输入！", Toast.LENGTH_SHORT).show();
				} else{ 
					register(strPassword);
				} 
					
			}

		});

		mBackView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});

	}

	private void register(String strPassword) {
		String url;
		url = "http://172.16.0.138:8080/AndTravel/uservalidation/register";
		url = url + "/" + mStrPhoneNum + "/" + strPassword;
		RequestQueue mQueue = Volley.newRequestQueue(this);
		//Map<String, String> params = new HashMap<String, String>();
		//params.put(mStrPhoneNum, strPassword);
		//params.put("password", strPassword);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.d(LOG_TAG, "向服务器注册客户信息");
				// 此处只处理景点的声音
				try {
					if (response.getString("code").equals("1")) {
						//广播用户已经登录了
						Intent broadcastIntent = new Intent("chinamobile.iot.andtravels.SetLogin");
						sendBroadcast(broadcastIntent);
						
						Intent intent = new Intent(mActivity, MainActivity.class);
						mActivity.startActivity(intent);
							
					} else {
						//登录失败后，提示用户
						String message = response.getString("message");
						Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Log.e(LOG_TAG, e.toString());
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Log.e(LOG_TAG, "注册客户信息失败：" + error.toString());
			}
		});

		mQueue.add(jsonObjectRequest);
	}
	
	
	private void submitRegisterInfo(String strPassword) throws JSONException{
		
		String url;// = "http://172.16.0.138:8080/AndTravel/uservalidation/checkidentity/";
		boolean bTest = true;
		if(bTest){
			url = "http://172.16.0.138:8080/AndTravel/uservalidation/validate/usingplaintext/";
			url = url + mStrPhoneNum + "/" + strPassword;
		}else{
			url = "http://172.16.0.138:8080/AndTravel/uservalidation/checkidentity/";
			url = url + mStrPhoneNum + "/" + strPassword + "/" + mStrIdentifyCode;
		}
		
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
	
	private void registerForTest(String strPassword) {
		//用户注册时，先核对验证码后，再存入用户名密码
		//1 核对验证码
		try {
			if(checkSmsCode()){
				submitRegisterInfo(strPassword);
			}else{
				/*短信验证码核对失败了，就暂时不提交用户注册信息到服务器*/
				Toast.makeText(this, "短信验证码验证失败，请稍候重试", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean checkSmsCode() throws JSONException{
		boolean checkResult = false;
		String url = "http://172.16.0.138:8080/AndTravel/sms/checksmscode/";
		url = url + mStrPhoneNum + "/" + mStrIdentifyCode;
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
		        	checkResult = false;
		        }else{
		        	checkResult = true;
		        }
		        
		    }else{
		    	Log.e(LOG_TAG, "向服务器注册用户信息失败");
		    }
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			checkResult = false;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			checkResult = false;
		}
		
	   	return checkResult;
	}
	
}

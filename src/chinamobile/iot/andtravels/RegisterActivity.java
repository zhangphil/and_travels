package chinamobile.iot.andtravels;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import chinamobile.iot.andtravels.wxapi.WXEntryActivity;

public class RegisterActivity extends Activity {

	private final String LOG_TAG = "RegisterActivity";
	private EditText editText;
	private TextView regTextView;
	private String strPhoneNum;
	private final Activity mActivity = this;
	private ImageView backView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		editText = (EditText) findViewById(R.id.phoneNum);
		editText.setHint("请输入手机号码");
		editText.setSingleLine();
		editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });

		regTextView = (TextView) findViewById(R.id.register);
		backView = (ImageView) findViewById(R.id.back);

		regTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(RegisterActivity.this).setTitle("确认手机号码")

				.setMessage("确定使用该号码进行注册？").setNegativeButton("返回", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						// TODO Auto-generated method stub

					}

				}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						// TODO Auto-generated method stub
						// 获取号码，切换到输入密码界面
						strPhoneNum = editText.getText().toString();
						if( strPhoneNum.isEmpty() ){
							Toast.makeText(mActivity, "请输入手机号码", Toast.LENGTH_LONG).show();
						}else{
							try {
								submitRegister(strPhoneNum);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						

					}

				}).show();
			}

		});

		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});

	}
	/**
	 * 向服务器请求验证码
	 * @param strNum
	 * @throws JSONException 
	 */
	private void submitRegister(String strNum) throws JSONException{
		Log.e(LOG_TAG, "客户注册的手机号码：" + strNum);
		String url = "http://172.16.0.138:8080/AndTravel/sms/requestforcode/";
		url = url + strNum;
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
		        }else{
		        	Intent intent = new Intent(mActivity, SubmitRegisterActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("PhoneNum", strPhoneNum);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
		        }
		        
		    }else{
		    	Log.e(LOG_TAG, "从服务器获取验证码失败");
		    }
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

}

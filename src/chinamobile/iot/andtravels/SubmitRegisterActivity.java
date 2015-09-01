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
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SubmitRegisterActivity extends Activity {

	private final String LOG_TAG = "SubmitRegisterActivity";
	private String strPhoneNum;
	private EditText firstEditText;
	private EditText secondEditText;
	private TextView regButton;
	private ImageView backView;

	private final SubmitRegisterActivity mActivity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_password);

		firstEditText = (EditText) findViewById(R.id.password);
		secondEditText = (EditText) findViewById(R.id.secondPassword);

		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		strPhoneNum = bundle.getString("PhoneNum");

		regButton = (TextView) findViewById(R.id.registerButton);

		backView = (ImageView) findViewById(R.id.back);

		regButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				// 发送客户信息到服务器
				Register();

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

	private void Register() {
		String url = "http://172.16.0.11:8080/AndTravel/beacondatasearch/onekeyguide/";
		RequestQueue mQueue = Volley.newRequestQueue(this);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.d(LOG_TAG, "向服务器注册客户信息");
				// 此处只处理景点的声音
				try {
					if (response.getString("code").equals("1")) {
						Intent intent = new Intent(mActivity, MainActivity.class);
						mActivity.startActivity(intent);

					} else {
						/* 没有获取到数据就不处理了 */
					}
				} catch (Exception e) {
					Log.e(LOG_TAG, e.toString());
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Log.e(LOG_TAG, "注册客户信息失败");
			}
		});

		mQueue.add(jsonObjectRequest);
	}

}

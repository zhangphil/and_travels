package chinamobile.iot.andtravels;

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

public class ChangeNameActivity extends Activity {

	private final String LOG_TAG = "ChangeNameActivity";
	private String strName;
	private EditText editText;
	private TextView submitButton;
	private ImageView backView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_name);

		editText = (EditText) findViewById(R.id.name);
		submitButton = (TextView) findViewById(R.id.register);
		backView = (ImageView) findViewById(R.id.back);

		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strName = editText.toString();
				// 把修改信息提交服务器上去
				Submit();

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

	private void Submit() {
		String url = "http://172.16.0.11:8080/AndTravel/uservalidation/requestcipher";
		RequestQueue mQueue = Volley.newRequestQueue(this);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.d(LOG_TAG, "服务器返回客户修改信息是否成功");
				// 此处只处理景点的声音
				try {
					if (response.getString("code").equals("1")) {

						Intent intent = new Intent(ChangeNameActivity.this, MainActivity.class);
						startActivity(intent);

					} else {
						/* 用户名和密码错误 */
						Toast.makeText(ChangeNameActivity.this, "昵称更新失败", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Log.e(LOG_TAG, e.toString());
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Log.e(LOG_TAG, "ChangeNameActivity 没有从服务器上获取更新客户信息结果");
			}
		});

		mQueue.add(jsonObjectRequest);
	}

}

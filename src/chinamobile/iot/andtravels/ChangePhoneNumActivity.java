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

public class ChangePhoneNumActivity extends Activity {

	private final String LOG_TAG = "ChangePhoneNum";
	private String strPhoneNum;
	private EditText editTextLogin;
	private EditText editTextPhoneNum;
	private TextView submitButton;
	private ImageView backView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_phone_num);

		editTextLogin = (EditText) findViewById(R.id.editPassword);
		editTextPhoneNum = (EditText) findViewById(R.id.editNum);
		submitButton = (TextView) findViewById(R.id.submit);
		backView = (ImageView) findViewById(R.id.back);
		
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				new AlertDialog.Builder(ChangePhoneNumActivity.this).setTitle("确认手机号码")

				.setMessage("确定修改手机号码？").setNegativeButton("返回", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						// TODO Auto-generated method stub

					}

				}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						// TODO Auto-generated method stub
						// 获取号码，切换到输入密码界面
						strPhoneNum = editTextPhoneNum.toString();
						Intent intent = new Intent(ChangePhoneNumActivity.this, SubmitChangePhoneNumActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("PhoneNum", strPhoneNum);
						intent.putExtra("bundle", bundle);

						startActivity(intent);

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

}

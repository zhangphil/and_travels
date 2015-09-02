package chinamobile.iot.andtravels;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterActivity extends Activity {

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
						strPhoneNum = editText.toString();
						Intent intent = new Intent(mActivity, MainActivity.class);
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

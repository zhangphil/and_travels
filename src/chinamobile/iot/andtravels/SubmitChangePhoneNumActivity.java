package chinamobile.iot.andtravels;

import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubmitChangePhoneNumActivity extends Activity {

	private final String LOG_TAG ="SubmitChangePhoneNumActivity";
	private String strName;
	private EditText editTextLogin;
	private EditText editTextChangeNum;
	private Button submitButton;
	@Override  
	protected void onCreate(Bundle savedInstanceState) {  
	super.onCreate(savedInstanceState);  
	setContentView(R.layout.submit_password);
	
	//editTextLogin = (EditText)findViewById(R.id.newPassword);
	//editTextChangeNum = (EditText)findViewById(R.id.secondNewPassword);
	submitButton = (Button)findViewById(R.id.submit);
	
	
	submitButton.setOnClickListener( new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			strName = editTextChangeNum.toString();
			Submit();
   	    
		}
	});

	}
	
	private void Submit(){
		String url = "http://172.16.0.11:8080/AndTravel/uservalidation/requestcipher";
		RequestQueue mQueue = Volley.newRequestQueue(this);
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,  
		        new Response.Listener<JSONObject>() {  
		            @Override  
		            public void onResponse(JSONObject response) {  
		                Log.d(LOG_TAG, "服务器返回客户修改信息（手机号码）是否成功"); 
		                //此处只处理景点的声音
		                try{
		                	 if(response.getString("code").equals("1")){

		                		 Intent intent=new Intent(SubmitChangePhoneNumActivity.this, MainActivity.class);
		 		            	 startActivity(intent);
 
		    	                }
		    	                else{
		    	                	/* 用户名和密码错误 */
		    	                	Toast.makeText(SubmitChangePhoneNumActivity.this, "手机号码更新失败",Toast.LENGTH_SHORT).show();
		    	                }
		                }
		                catch(Exception e){
		                	Log.e(LOG_TAG, e.toString());
		                }
		               
		            }  
		        }, new Response.ErrorListener() {  
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Log.e(LOG_TAG, "ChangeNameActivity 没有从服务器上获取更新客户信息（手机号码）结果");
					}  
		        });
		
		mQueue.add(jsonObjectRequest);
	}


}

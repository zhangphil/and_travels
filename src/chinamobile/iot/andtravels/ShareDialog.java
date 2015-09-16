package chinamobile.iot.andtravels;

import java.util.HashMap;

import org.json.JSONObject;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;
import chinamobile.iot.andtravels.wxapi.WXEntryActivity;


public class ShareDialog extends Dialog {
	
	private String LOG_TAG = "ShareDialog";
	private RadioButton shareImage1, shareImage2, shareImage3, shareImage4;
	private Context mContext;
	public ShareDialog(Context context) {
		super(context);
		mContext = context;
	}
	private Intent intent;

	@Override
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_pop_window);
        
        shareImage1 = (RadioButton)findViewById(R.id.share1);  
		shareImage2 = (RadioButton)findViewById(R.id.share2); 
		shareImage3 = (RadioButton)findViewById(R.id.share3); 
		shareImage4 = (RadioButton)findViewById(R.id.share4); 
        
		
		shareImage1.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
            	//showShareApp(Wechat.NAME);  
            	intent = new Intent(mContext,WXEntryActivity.class);
            	intent.putExtra("shareIndex", 0);
            	mContext.startActivity(intent);
            	
            	dismiss();
            }  
        }); 
		
		shareImage2.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
            	intent = new Intent(mContext,WXEntryActivity.class);
            	intent.putExtra("shareIndex", 1);
            	mContext.startActivity(intent); 
            	
            	dismiss();
            }  
        }); 
		
		
		shareImage3.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) { 
            	//intent = new Intent(mContext,QQZoneShare.class);
            	//mContext.startActivity(intent); 
            	dismiss();
                 
            }  
        }); 
		
		shareImage4.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
            	
            	dismiss();
                 
            }  
        }); 
        
    }
	
	@Override
    public void dismiss() {
        super.dismiss();
    }
	
	
	
	
}

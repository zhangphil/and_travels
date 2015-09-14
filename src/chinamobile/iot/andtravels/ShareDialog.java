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
	
	private static Tencent mTencent;
	private String mShareQQZoneAPPId = "1104785615";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_pop_window);
        
        mTencent = Tencent.createInstance(mShareQQZoneAPPId, mContext);
        
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
            	showShareAppToQQZone();
            	dismiss();
                 
            }  
        }); 
		
		shareImage4.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
            	
            	dismiss();
                 
            }  
        }); 
        
    }
	/**
	 * 分享到QQ空间的回调函数
	 * @author JoeyZhang
	 *
	 */
	private class BaseUiListener implements IUiListener {

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			Log.e(LOG_TAG, "QQ空间分享时，用户选择了取消！");
		}

		@Override
		public void onComplete(Object arg0) {
			// TODO Auto-generated method stub
			Log.e(LOG_TAG, "QQ空间分享成功！！！");
		}

		@Override
		public void onError(UiError arg0) {
			// TODO Auto-generated method stub
			Log.e(LOG_TAG, "QQ空间分享失败，原因：" + arg0 );
		}
		
	}
	
	private void showShareAppToQQZone(){
		
		Log.e(LOG_TAG, "点击APP分享到QQ空间");
		//分享类型
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
	    params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "分享APP给你的朋友吧！！！");
	    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "");
	    params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://baidu.com");
	    mTencent.shareToQzone((Activity) mContext, params, new BaseUiListener());
	}
	
}

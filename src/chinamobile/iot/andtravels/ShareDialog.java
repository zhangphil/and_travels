package chinamobile.iot.andtravels;

import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

public class ShareDialog extends Dialog {
	
	private String LOG_TAG = "ShareDialog";
	private RadioButton shareImage1, shareImage2, shareImage3, shareImage4;
	private Context mContext;
	public ShareDialog(Context context) {
		super(context);
		mContext = context;
	}
	
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
            	showShareApp(SinaWeibo.NAME);   
            }  
        }); 
		
		shareImage2.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
                 
            }  
        }); 
		
		
		shareImage3.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
                 
            }  
        }); 
		
		shareImage4.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
                 
            }  
        }); 
        
    }
	
	private void showShareApp(String title){
		
		ShareSDK.initSDK(mContext, LOG_TAG);
		shareApp(title);
		
	}
	
	
	private void shareApp(String shareName){  
		
		 Platform.ShareParams sp;
		 Platform sharePlatform;
		if(shareName.equalsIgnoreCase(SinaWeibo.NAME)){
			 sp = new SinaWeibo.ShareParams();  
		     sharePlatform = ShareSDK.getPlatform(mContext, shareName); 
		}else if(shareName.equalsIgnoreCase(SinaWeibo.NAME)){
			sp = new SinaWeibo.ShareParams();  
		    sharePlatform = ShareSDK.getPlatform(mContext, shareName); 
		}else if(shareName.equalsIgnoreCase(SinaWeibo.NAME)){
			sp = new SinaWeibo.ShareParams();  
		    sharePlatform = ShareSDK.getPlatform(mContext, shareName); 
		}else{
			sp = new SinaWeibo.ShareParams();  
		    sharePlatform = ShareSDK.getPlatform(mContext, shareName); 
		}
        
  
        // 设置分享事件回调  
        sharePlatform.setPlatformActionListener(new PlatformActionListener() {  
  
            public void onError(Platform platform, int action, Throwable t) {  
               Toast.makeText(mContext, "分享失败", action);
                
            }  
  
            public void onComplete(Platform platform, int action,  
                    HashMap<String, Object> res) {  
            	Toast.makeText(mContext, "分享成功", action);
            }  
  
            public void onCancel(Platform platform, int action) {  
                // 操作取消的处理代码  
            }  
  
        });  
  
        sharePlatform.share(sp);  
    }  

	
}

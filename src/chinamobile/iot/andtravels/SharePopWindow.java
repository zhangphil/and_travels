package chinamobile.iot.andtravels;

import java.util.HashMap;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

public class SharePopWindow extends PopupWindow {
	
	private String LOG_TAG = "SharePopWindow";
	private View mPopView; 
	private ImageView shareImage1, shareImage2, shareImage3, shareImage4;
	private Context mContext;
	public SharePopWindow(Context context) {
		super(context);
		mContext = context;
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		mPopView = inflater.inflate(R.layout.share_pop_window, null);  
		shareImage1 = (ImageView) mPopView.findViewById(R.id.share1);  
		shareImage2 = (ImageView) mPopView.findViewById(R.id.share2); 
		shareImage3 = (ImageView) mPopView.findViewById(R.id.share3); 
		shareImage4 = (ImageView) mPopView.findViewById(R.id.share4); 
         
         
		shareImage1.setOnClickListener(new OnClickListener() {  
            public void onClick(View v) {  
            	showShareApp();   
            }  
        }); 
		
		shareImage2.setOnClickListener(new OnClickListener() {  
            public void onClick(View v) {  
                 
            }  
        }); 
		
		
		shareImage3.setOnClickListener(new OnClickListener() {  
            public void onClick(View v) {  
                 
            }  
        }); 
		
		shareImage4.setOnClickListener(new OnClickListener() {  
            public void onClick(View v) {  
                 
            }  
        }); 
		
        this.setContentView(mPopView); 
        this.setWidth(700); 
        this.setHeight(250);   
        this.setFocusable(true);  
        ColorDrawable dw = new ColorDrawable(0xb0000000);  
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);  
        mPopView.setOnTouchListener(new OnTouchListener() {  
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int height = 100; 
                int y=(int) event.getY();  
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y<height){  
                        dismiss();  
                    }  
                }                 
                return true;  
			}  
        });  
		
	}
	
	private void showShareApp(){
		
		ShareSDK.initSDK(mContext, LOG_TAG);
		shareApp(SinaWeibo.NAME);
		
	}
	
	
	private void shareApp(String sharename){  
			  
	        Platform.ShareParams sp = new SinaWeibo.ShareParams();  
	        
	        Platform sharePlatform = ShareSDK.getPlatform(mContext, sharename);  
	  
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
	  
	        // 执行图文分享  
	        sharePlatform.share(sp);  
	    }  

	
}

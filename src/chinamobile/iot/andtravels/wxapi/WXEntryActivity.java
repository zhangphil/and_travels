package chinamobile.iot.andtravels.wxapi;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;
import chinamobile.iot.andtravels.ContainerActivity;
import chinamobile.iot.andtravels.R;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

	private IWXAPI api;
	private final String APP_ID ="wxde8390c1b8021c18";
	private int index = 0;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
       
        Log.e("WXEntryActivity", "进入微信分享");
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);  
        api.registerApp(APP_ID); 
        
        api.handleIntent(getIntent(), (IWXAPIEventHandler) this); 
        
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        index = bundle.getInt("shareIndex");
        
    }
	
	@Override
    protected void onStart() { 
        super.onStart();
       
        Log.e("WXEntryActivity", "开始微信分享了哈");
        
        shareAPPToWeiXin(index);
        
    }
	
	@Override
    protected void onResume() { 
        super.onResume();
        this.finish();
    }
	
	
	private void shareAPPToWeiXin(int flag) {  
	    
	    if (!api.isWXAppInstalled()) {  
	        Toast.makeText(WXEntryActivity.this, "您还未安装微信客户端",  
	                Toast.LENGTH_SHORT).show();  
	        return;  
	    }  
	  
	    WXWebpageObject webpage = new WXWebpageObject();  
	    webpage.webpageUrl = "http://baidu.com";  
	    WXMediaMessage msg = new WXMediaMessage(webpage);  
	  
	    msg.title = "分享APP";  
	    msg.description ="分享APP给你的朋友吧！！！";  
	    Bitmap thumb = BitmapFactory.decodeResource(getResources(),  
	            R.drawable.a_selected);  
	    msg.setThumbImage(thumb);  
	    SendMessageToWX.Req req = new SendMessageToWX.Req();  
	    req.transaction = String.valueOf(System.currentTimeMillis());  
	    req.message = msg;  
	    req.scene = flag;  
	    api.sendReq(req);  
	}  
	
	@Override  
	public void onReq(BaseReq arg0) {  
	    // TODO Auto-generated method stub 
		this.finish();
	}  
	
	@Override  
	public void onResp(BaseResp resp) {  
	  
	    Log.v("WXEntryActivity", "微信分享APP结果");  
	    String result;  
	  
	    
	    switch (resp.errCode) {  
	    case BaseResp.ErrCode.ERR_OK:  
	        result = "分享成功";  
	        break;  
	    case BaseResp.ErrCode.ERR_USER_CANCEL:  
	    	result = "";
	        break;  
	    case BaseResp.ErrCode.ERR_AUTH_DENIED:
	    	result = "分享失败"; 
	        break;  
	    default:
	    	result = "分享失败"; 
	        break;  
	    }  
	  
	    if(!result.isEmpty()){
	    	Toast.makeText(this, result, Toast.LENGTH_LONG).show(); 
	    }
	    
	    /*int pos = 3;
		Intent intent = new Intent(this, ContainerActivity.class);
		intent.putExtra("curViewPos", pos);
		startActivity(intent);*/

		this.finish();
	  
	}  

}

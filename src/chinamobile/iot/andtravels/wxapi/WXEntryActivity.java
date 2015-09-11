package chinamobile.iot.andtravels.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import chinamobile.iot.andtravels.R;
import cn.sharesdk.sina.weibo.SinaWeibo;

public class WXEntryActivity extends Activity {

	//private IWXAPI api;
	private final String APP_ID ="wxde8390c1b8021c18";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
       
        //api = WXAPIFactory.createWXAPI(this, APP_ID, true);  
       // api.registerApp(APP_ID); 
        
    }

}

package chinamobile.iot.andtravels;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

public class QQZoneShare extends Activity {

	private final String LOG_TAG = "QQZoneShare";
	private Tencent mTencent;
	private String mShareQQZoneAPPId = "1104785615";
	
	public QQZoneShare() {
		// TODO Auto-generated constructor stub
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        mTencent = Tencent.createInstance(mShareQQZoneAPPId, this);

	}
	
	@Override
    protected void onStart() { 
        super.onStart();
        showShareAppToQQZone();

	}
	
	@Override
    protected void onDestroy() { 
        super.onDestroy();
        mTencent.releaseResource();

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
	    mTencent.shareToQzone(this, params, new BaseUiListener());
	}
	
	 
}

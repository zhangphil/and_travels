package chinamobile.iot.andtravels.utils;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

public class Utils {

	/**
	 * 模拟键盘事件方法
	 * 
	 * @param keyCode
	 */
	public static void actionKey(final int keyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(keyCode);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	
	public	static Bitmap getResize(Context context,int resId, int resize) {
		Bitmap sourceBitmap = BitmapFactory.decodeResource(context.getResources(), resId);

		// 获得原始bitmap的高和宽，下面将对原始Bitmap等比例缩放成缩略图加载。
		int h = sourceBitmap.getHeight();
		int w = sourceBitmap.getWidth();

		Bitmap bmp = ThumbnailUtils.extractThumbnail(sourceBitmap, w / resize, h / resize);
		return bmp;
	}
}

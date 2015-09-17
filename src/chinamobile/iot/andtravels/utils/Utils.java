package chinamobile.iot.andtravels.utils;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

public class Utils {

	public static String getBaiduCoordURL(int from, int to, double lng, double lat) {
		String url = "http://api.map.baidu.com/geoconv/v1/?coords=" + lng + "," + lat + "&from=" + from + "&to=" + to
				+ "&ak=" + Constants.BAIDUMAP_AK;
		return url;
	}

	/**
	 * 模拟系统按键。
	 * 
	 * @param keyCode
	 */
	public static void onKeyEvent(final int keyCode) {
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

	public static Bitmap getResize(Context context, int resId, int resize) {
		Bitmap sourceBitmap = BitmapFactory.decodeResource(context.getResources(), resId);

		// 获得原始bitmap的高和宽，下面将对原始Bitmap等比例缩放成缩略图加载。
		int h = sourceBitmap.getHeight();
		int w = sourceBitmap.getWidth();

		Bitmap bmp = ThumbnailUtils.extractThumbnail(sourceBitmap, w / resize, h / resize);

		if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
			sourceBitmap.recycle();
			sourceBitmap = null;
			System.gc();
		}

		return bmp;
	}

	// public static Bitmap getAssetImage(Context context, String filename) {
	// AssetManager assets = context.getResources().getAssets();
	// InputStream is = null;
	// try {
	// is = assets.open("drawable/" + filename + ".png");
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// BitmapFactory.Options options = new BitmapFactory.Options();
	// options.inPurgeable = true;
	// BufferedInputStream buffer = new BufferedInputStream(is);
	// Bitmap bitmap = BitmapFactory.decodeStream(buffer ,null, options);
	// return bitmap;
	// //return new BitmapDrawable(context.getResources(), bitmap);
	// }

	// public static Bitmap getBitmapNonOOM(Context context, int resId) {
	//
	// BitmapFactory.Options options = new BitmapFactory.Options();
	//
	// // 将原图缩小n倍
	// options.inSampleSize = 4;
	//
	// Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resId,
	// options);
	//
	// return bmp;
	// }
}

package chinamobile.iot.andtravels;

import com.sun.jna.platform.unix.X11.Drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView {
	 
    Path path;
    public PaintFlagsDrawFilter mPaintFlagsDrawFilter;// 毛边过滤
    Paint paint;
     
    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }
 
    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }
 
    public CircleImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }
    public void init(){
        mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0,
                Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setColor(Color.WHITE);
         
    }
     
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
    	android.graphics.drawable.Drawable drawable = getDrawable();    
        if (drawable == null) {  
            return;  
        }  
      
        if (getWidth() == 0 || getHeight() == 0) {  
            return;   
        }  
          
        Bitmap b =  ((BitmapDrawable)drawable).getBitmap();     
        if(null == b)  
        {  
            return;  
        }  
          
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);  
        int w = getWidth(), h = getHeight();  
       
        Bitmap roundBitmap =  getCroppedBitmap(bitmap, w);  
        canvas.drawBitmap(roundBitmap, 0,0, null); 
    }
    
    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {  
        Bitmap sbmp;  
        if(bmp.getWidth() != radius || bmp.getHeight() != radius)  
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);  
        else  
            sbmp = bmp;  
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),  
                sbmp.getHeight(), Config.ARGB_8888);  
        Canvas canvas = new Canvas(output);  
      
        final int color = 0xffa19774;  
        final Paint paint = new Paint();  
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());  
      
        paint.setAntiAlias(true);  
        paint.setFilterBitmap(true);  
        paint.setDither(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(Color.parseColor("#BAB399"));  
        canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.7f,  
                sbmp.getWidth() / 2+0.1f, paint);  
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        canvas.drawBitmap(sbmp, rect, rect, paint);  
      
      
                return output;  
    }  
}
package chinamobile.iot.andtravels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CircleIndicatorView extends View { 
	
    public CircleIndicatorView(Context context, AttributeSet attrs){   
    	       super(context, attrs);  
    }
    
    private	int	gap=20;//各个横向排列的小球间距
	public	void	setCirlceGap(int gap){
	    	this.gap=padding;
	}
	public	int	getCirlceGap(){
	    	return	gap;
	}
    
    private	int	padding=20;
    public	void	setPadding(int padding){
    	this.padding=padding;
    }
    public	int	getPadding(){
    	return	padding;
    }
    
    private	int	circle_normal_radius=5; //普通小球半径
    public	void	setNormalCircleRadius(int radius){
    	this.circle_normal_radius=radius;
    }
    public	int	getNormalCircleRadius(){
    	return	circle_normal_radius;
    }
    
    private	int	circle_selected_radius=5; //被选择的小球半径
    public	void setSelectedCircleRadius(int radius){
    	this.circle_selected_radius=radius;
    } 
    public	int	getSelectedCircleRadius(){
    	return	circle_selected_radius;
    }
    
    private	int	count=0;
    public	void	setCircleCount(int count){
    	this.count=count;
    }
    public	int	getCircleCount(){
    	return	count;
    }

    private	int	pos=0;
    public	void	setCircleSelectedPosition(int pos){
    	this.pos=pos;
    }
    public	int	getCircleSelectedPosition(){
    	return	pos;
    }
    
    public	void	drawCircleView(){
    	this.invalidate();
    }
    
    private	int	circleSelectedColor=Color.RED;
    public	void	setCircleSelectedColor(int color){
    	circleSelectedColor=color;
    }
    public	int	getCircleSelectedColor(){
    	return	circleSelectedColor;
    }
    
    private	int	circleUnSelectedColor=Color.LTGRAY;
    public	void	setCircleUnSelectedColor(int color){
    	circleUnSelectedColor=color;
    }
    public	int	getCircleUnSelectedColor(){
    	return	circleUnSelectedColor;
    }

    
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas); 
        
        Paint p = new Paint();  
        p.setAntiAlias(true);
        
        int w=this.getWidth();
        int h=this.getHeight();
        
        //因为是自右往左绘制小圆圈，需要转化pos的位置。
      //int translate_pos=getCircleCount()-getCircleSelectedPosition()-1;
       
      //如果居中绘制则使用start_x，但需要依次递加x坐标轴位置值。
      int start_x=(w-(getCirlceGap()*(getCircleCount()-1)))/2;
        
        for(int i=0;i<getCircleCount();i++){
        	int r=getNormalCircleRadius();
        	
        	if(i==getCircleSelectedPosition()){
        		r=getSelectedCircleRadius();
        		p.setColor(getCircleSelectedColor());
        	}
        	else{
        		r=getNormalCircleRadius();
        		p.setColor(getCircleUnSelectedColor()); 
        	}
        	
        	//自右向左绘制。从最右边往左边绘制小球
        	//如果从该自定制的View左边绘制，直接将 x=0即可。
        	
        	//从右边开始绘制w-i*getCirlceGap()-getPadding()
        	
        	canvas.drawCircle(start_x+i*getCirlceGap()+getPadding(), h/2, r, p);
        }
    } 
}  

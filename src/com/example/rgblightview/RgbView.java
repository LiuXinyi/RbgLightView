package com.example.rgblightview;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * @author LiuXinYi
 * @Date 2015年9月17日 上午10:00:15
 * @Description [正方形颜色选择界面]
 * @version 1.0.0
 */
public class RgbView extends ImageView {
    public RgbView(Context context) {
	this(context, null);
    }

    public RgbView(Context context, AttributeSet attrs) {
	this(context, attrs, 0);
    }

    public RgbView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
    }

    private boolean isOpen = true;

    public boolean isOpen() {
	return isOpen;
    }

    public void setOpen(boolean isOpen) {
	this.isOpen = isOpen;
	if (isOpen) {
	    setImageResource(R.drawable.rgb_image);
	} else {
	    setImageResource(R.drawable.rgb_close);
	}
    }

    private void colorPf(float x, float y) {
	if (x - (iconRadius) <= 0) {
	    x = iconRadius;
	}
	if (y - (iconRadius) <= 0) {
	    y = iconRadius;
	}
	if (x >= getWidth() - iconRadius) {
	    x = getWidth() - iconRadius;
	}
	if (y >= getHeight() - iconRadius) {
	    y = getHeight() - iconRadius;
	}
	iconPoint.x = x;
	iconPoint.y = y;

    }

    int color = 0;
    public int lastColor;
    ArrayList<Point> rects = new ArrayList<Point>();

    private Bitmap transparentPX(int color) {
	Bitmap bitmap = iconBmp.copy(iconBmp.getConfig(), true);
	int maxy = bitmap.getHeight();
	int maxx = bitmap.getWidth();
	for (Point point : rects) {
	    if (point.x > maxx || point.y >= maxy) {
		continue;
	    }
	    bitmap.setPixel(point.x, point.y, color);
	}
	lastColor = color;
	return bitmap;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
	if (!isOpen) {
	    return true;
	}
	float x = event.getX();
	float y = event.getY();
	int pixel;
	int r;
	int g;
	int b;
	switch (event.getAction()) {
	case MotionEvent.ACTION_DOWN:
	    colorPf(x, y);
	    pixel = getImagePixel(iconPoint.x, iconPoint.y);
	    color = pixel;
	    invalidate();
	    break;
	case MotionEvent.ACTION_MOVE:
	    colorPf(x, y);
	    pixel = getImagePixel(iconPoint.x, iconPoint.y);
	    color = pixel;
	    if (listener != null) {
		listener.onMoveColor(pixel);
	    }
	    invalidate();
	    break;
	case MotionEvent.ACTION_UP:
	    colorPf(x, y);
	    pixel = getImagePixel(iconPoint.x, iconPoint.y);
	    color = pixel;
	    if (listener != null) {
		listener.onColorChanged(pixel);
	    }
	    invalidate();
	    break;

	default:
	    break;
	}

	return true;
    }

    private Paint iconPaint;
    private Bitmap iconBmp;
    private PointF iconPoint;
    private float iconRadius;

    private void init() {
	if (isOpen) {
	    setImageResource(R.drawable.rgb_image);
	} else {
	    setImageResource(R.drawable.rgb_close);
	}
	iconPaint = new Paint();
	iconPoint = new PointF();
	iconBmp = BitmapFactory.decodeResource(getContext().getResources(),
		R.drawable.pickup);// 吸管的图片
	iconRadius = iconBmp.getWidth() / 2;
	iconPoint.x = getWidth() / 2;
	iconPoint.y = getHeight() / 2;
	Bitmap icon2 = BitmapFactory.decodeResource(
		getContext().getResources(), R.drawable.pickup2);// 吸管的图片
	for (int i = 0; i < icon2.getWidth(); i++) {
	    for (int j = 0; j < icon2.getHeight(); j++) {
		if (icon2.getPixel(i, j) == Color.BLACK) {
		    rects.add(new Point(i, j));// 获取透明颜色的坐标
		}
	    }
	}
	icon2.recycle();
	icon2 = null;
    }

    Bitmap iconTransp;

    @Override
    protected void onDraw(Canvas canvas) {
	// TODO Auto-generated method stub
	super.onDraw(canvas);
	if (iconBmp == null) {
	    init();
	}
	if (isOpen) {
	    if (color == lastColor && iconTransp != null) {
		canvas.drawBitmap(iconTransp, iconPoint.x - iconRadius,
			iconPoint.y - iconRadius, iconPaint);
	    } else {
		if (iconTransp != null) {
		    iconTransp.recycle();
		    iconTransp = null;
		    
		}
		Bitmap b = transparentPX(color);
		canvas.drawBitmap(b, iconPoint.x - iconRadius, iconPoint.y
			- iconRadius, iconPaint);
		iconTransp = b;
	    }
	    // canvas.drawBitmap(iconBmp, iconPoint.x - iconRadius, iconPoint.y
	    // - iconRadius, iconPaint);
	}
    }

    /**
     * 取颜色值
     * 
     * @param x
     * @param y
     * @return
     */
    public int getImagePixel(float x, float y) {
	if (x < 0)
	    x = 0;
	if (y < 0)
	    y = 0;

	if (y > getHeight())
	    y = getHeight();

	if (x > getWidth())
	    x = getWidth();
	// 换成比例处理
	double bx = x / getWidth();
	double by = y / getHeight();

	Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();

	// 为了防止越界
	double intX = ((double) bitmap.getWidth()) * bx;
	double intY = ((double) bitmap.getHeight()) * by;
	if (intX >= bitmap.getWidth()) {
	    intX = bitmap.getWidth() - 1;
	}
	if (intY >= bitmap.getHeight()) {
	    intY = bitmap.getHeight() - 1;
	}
	int pixel = bitmap.getPixel((int) intX, (int) intY);
	return pixel;

    }

    private OnColorChangedListener listener;

    public void setOnColorChangedListener(OnColorChangedListener listener) {
	this.listener = listener;
    }

    // 内部接口 回调颜色 rgb值
    public interface OnColorChangedListener {
	// 手指抬起，确定颜色回调
	void onColorChanged(int pixel);

	// 移动时颜色回调
	void onMoveColor(int pixel);
    }
}

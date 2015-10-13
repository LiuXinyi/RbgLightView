package com.example.rgblightview;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * @author LiuXinYi
 * @Date 2015�?�?9�?上午9:19:01
 * @Description [第一版]
 * @version 1.0.0
 */
public class MyRgbPickerView extends ImageView {


    private Context context;
    private Paint iconPaint;
    private Paint thumbPaint;
    private PointF iconPoint; // 颜色取色�?point
    private PointF thumbPoint; // 滑块取色�?point
    private Bitmap iconBmp;
    private Bitmap thumbBmp;
    private float iconRadius;// 取色器的半径
    private float colorRadius;// 里面圆的半径

    public float getColorRadius() {
	return colorRadius;
    }

    public void onDetach() {
	light_szie = 0;
    }

    private float peripheryRadius;// 外围圆的直径
    private float imageRadius;// 整个图片的半�?
    private float thumbWidth;
    private float thumbHeight;
    private boolean isOpen = true;

    public boolean isOpen() {
	return isOpen;
    }

    public void setOpen(boolean isOpen) {
	this.isOpen = isOpen;
	if (!isOpen) {
	    invalidate();
	}
    }

    public MyRgbPickerView(Context context) {
	this(context, null);
    }

    public MyRgbPickerView(Context context, AttributeSet attrs) {
	this(context, attrs, 0);
    }


    public MyRgbPickerView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	this.context = context;

	// init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// TODO Auto-generated method stub
	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// int measuredHeight = measureHeight(heightMeasureSpec);
	// int measuredWidth = measureWidth(widthMeasureSpec);
	// // setMeasuredDimension((int)measuredWidth+iconRadius,
	// // (int)measuredHeight+iconRadius);
	// setMeasuredDimension(measuredWidth, measuredHeight);
	// init();
    }

    float colorAndMidBlank;
    float peripheryMax;

    private void init() {
	iconPaint = new Paint();
	thumbPaint = new Paint();
	iconPoint = new PointF();
	thumbPoint = new PointF();
	iconBmp = BitmapFactory.decodeResource(context.getResources(),
		R.drawable.pickup);// 吸管的图�?
	thumbBmp = BitmapFactory.decodeResource(context.getResources(),
		R.drawable.thumb3);//
	thumbWidth = thumbBmp.getWidth() / 2;
	thumbHeight = thumbBmp.getHeight() / 2;
	iconRadius = iconBmp.getWidth() / 2;// 吸管的图片一�?
	imageRadius = getWidth() / 2;
	colorRadius = imageRadius * 0.6f + (iconRadius / 2);// 里面圆的半径�?.75�?
	colorAndMidBlank = imageRadius * 0.82f;
	peripheryMax = imageRadius * 1.1f;// * 0.97f;

	peripheryRadius = imageRadius - thumbBmp.getHeight() / 2 - 10;

	// // 初始化颜色取色器位置
	iconPoint.x = imageRadius;
	iconPoint.y = imageRadius;
	thumbPoint.x = imageRadius;
	thumbPoint.y = thumbHeight;
	thumbX = (float) (imageRadius / 2.5);
	thumbY = imageRadius * 2;
	thumbProof(thumbX, thumbY, getDistance(thumbX, thumbY));

	// setDegrees(240);
	color = ((BitmapDrawable) getDrawable()).getBitmap().getPixel(
		(int) iconPoint.x, (int) iconPoint.y);
	Bitmap icon2 = BitmapFactory.decodeResource(context.getResources(),
		R.drawable.pickup2);// 吸管的图�?
	for (int i = 0; i < icon2.getWidth(); i++) {
	    for (int j = 0; j < icon2.getHeight(); j++) {
		if (icon2.getPixel(i, j) == Color.BLACK) {
		    rects.add(new Point(i, j));// 获取透明颜色的坐�?
		}
	    }
	}
	icon2.recycle();
	icon2 = null;
    }

    int color = 0;
    public int lastColor;
    ArrayList<Point> rects = new ArrayList<Point>();

    /**
     * 把�?明的像素填充为当前颜�?
     * 
     * @param color
     * @return
     */
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

    /**
     * 获取圆角位图的方�?
     * 
     * @param bitmap
     *            �?��转化成圆角的位图
     * @param pixels
     *            圆角的度数，数�?越大，圆角越�?
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, float pixels) {
	Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
		bitmap.getHeight(), Config.ARGB_8888);
	Canvas canvas = new Canvas(output);
	int color = 0xff424242;
	Paint paint = new Paint();
	Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	RectF rectF = new RectF(rect);
	float roundPx = pixels;
	paint.setAntiAlias(true);
	canvas.drawARGB(0, 0, 0, 0);
	paint.setColor(color);
	canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	canvas.drawBitmap(bitmap, rect, rect, paint);
	bitmap.recycle();
	return output;
    }

    Bitmap iconTransp;

    /**
     * 总大�?450 <br>
     * 中间�?315 450*0.7 <br>
     * 
     * 中间空白 60 225*0.066<br>
     * 外围�?50450*0.055<br>
     * 外面留白 25 450*0.03<br>
     */
    @Override
    protected void onDraw(Canvas canvas) {
	super.onDraw(canvas);
	if (iconBmp == null) {
	    init();
	}
	if (isOpen) {
	    if (isTransparent) {
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

	    } else {
		canvas.drawBitmap(iconBmp, iconPoint.x - iconRadius,
			iconPoint.y - iconRadius, iconPaint);
	    }
	}
	if (isOpen) {
	    canvas.drawBitmap(thumbBmp, thumbPoint.x - thumbWidth, thumbPoint.y
		    - thumbHeight, thumbPaint);
	}
	// isTransparent = false;

	// canvas.rotate((float) degrees);
	// panRotate.setRotate((float)degrees, imageRadius, imageRadius);
	// thumbPaint.set。canvas
	// canvas.rotate((float)degrees);
	// canvas.rotate((float) degrees, imageRadius, imageRadius);

	// canvas.drawBitmap(thumbBmp, panRotate, null);
	// canvas.restore();// 恢复canvas状�?
    }

    private boolean isTransparent;// 判断取色图标是否超过�?

    //

    private void colorProof(float x, float y, float distance) {
	// float h = x - imageRadius; // 取xy点和圆点 的三角形�?
	// float w = y - imageRadius;// 取xy点和圆点 的三角形�?
	// float h2 = h * h;
	// float w2 = w * w;
	// float distance = (float) Math.sqrt((h2 + w2)); // 勾股定理�?斜边距离
	if (distance > colorRadius) { // 如果斜边距离大于半径，则取点和圆�?��的一个点为x,y
	    isTransparent = true;
	    float maxX = x - imageRadius;
	    float maxY = y - imageRadius;
	    x = ((colorRadius * maxX) / distance) + imageRadius; // 通过三角形一边平行原理求出x,y
	    y = ((colorRadius * maxY) / distance) + imageRadius;
	} else {
	    if (distance > (colorRadius - iconBmp.getHeight())) {
		// 如果distance 大于（圆的半�?icon的直径）
		// 则还是超过过圆，�?��填充颜色
		isTransparent = true;
	    } else {
		isTransparent = false;
	    }
	}
	iconPoint.x = x;
	iconPoint.y = y;
    }

    int right_h;
    int left_h;
    boolean isOn;
    boolean isOff;
    public double light_szie;
    private float thumbX = (float) (imageRadius / 2.5);
    private float thumbY = imageRadius * 2;


    double currentDegrees;

    private void thumbProof(float x, float y, float distance) {
	// float h = x - imageRadius; // 取xy点和圆点 的三角形�?
	// float w = y - imageRadius;// 取xy点和圆点 的三角形�?
	// float h2 = h * h;
	// float w2 = w * w;
	// float distance = (float) Math.sqrt((h2 + w2)); // 勾股定理�?斜边距离
	float maxX = x - imageRadius;
	float maxY = y - imageRadius;
	x = ((peripheryRadius * maxX) / distance) + imageRadius; // 通过三角形一边平行原理求出x,y
	y = ((peripheryRadius * maxY) / distance) + imageRadius;

	int rX = (int) (x - imageRadius);
	int rY = (int) (imageRadius - y);
	// Log.e(VIEW_LOG_TAG, "rX:" + rX + " rY:" + rY);
	double degrees = Math.toDegrees(Math.atan2(rY, rX));
	if (degrees < 0) {
	    degrees = degrees + 360; // 小于0�?60
	}
	// Log.e(VIEW_LOG_TAG, "角度:" + degrees);
	if (degrees >= 240 && degrees <= 300) {
	    // 这里是点击了下面空白区域
	} else {
	    currentDegrees = degrees;
	    thumbPoint.x = x;
	    thumbPoint.y = y;
	    if (degrees < 92 || degrees > 300) {
		left_h = LayoutParams.MATCH_PARENT;
		if (degrees > 250) {
		    right_h = (int) y;
		} else {
		    right_h = (int) y - 2;
		}
	    } else {
		right_h = 0;
		left_h = (int) (getHeight() - y);

	    }
	    double dszie = ((d - degrees) / (300));
	    if (dszie < 0) {
		dszie = (360 - degrees + d) / (300);
	    }
	    dszie = dszie * 100;
	    light_szie = dszie;
	    if (mChangedListener != null)
		mChangedListener.onHeightSzie(left_h, right_h);
	}
	invalidate();
    }


    // 计算�?亮度�?时的角度
    double d = (1.5 * Math.PI - Math.atan2(124.5, 239.5)) * 180 / Math.PI;

    //
    // double d = Math.atan2(-Math.sqrt(18.75), -2.5) * 180 / Math.PI;
    /**
     * 获取斜边�?
     * 
     * @param x
     * @param y
     * @return
     */
    private float getDistance(float x, float y) {
	float h = x - imageRadius; // 取xy点和圆点 的三角形�?
	float w = y - imageRadius;// 取xy点和圆点 的三角形�?
	float h2 = h * h;
	float w2 = w * w;
	float distance = (float) Math.sqrt((h2 + w2));

	return distance;
    }

    private static final int COLOR = 1; // 当前是点击的颜色
    private static final int THUMB = 3;// 当前点击是滑�?
    private int type = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
	if (!isOpen) {
	    return true;
	}
	float x = event.getX();
	float y = event.getY();
	float distance = 0;
	int pixel;
	int r;
	int g;
	int b;
	switch (event.getAction()) {
	case MotionEvent.ACTION_DOWN:
	    distance = getDistance(x, y); // 获取该点和圆心距�?
	    if (distance <= (colorRadius + iconRadius)) {
		type = COLOR;
		colorProof(x, y, distance);
	    } else if (distance >= colorAndMidBlank && distance <= peripheryMax) {
		type = THUMB;
		thumbProof(x, y, distance);
	    } else {

	    }
	    // Log.e(VIEW_LOG_TAG, "现在type:" + type);

	    break;
	case MotionEvent.ACTION_MOVE:
	    distance = getDistance(x, y); // 获取该点和圆心距�?
	    if (type == COLOR) {
		colorProof(x, y, distance);
		pixel = getImagePixel(iconPoint.x, iconPoint.y);
		color = pixel;
		invalidate();
		if (mChangedListener != null) {

		    // r = Color.red(pixel);
		    // g = Color.green(pixel);
		    // b = Color.blue(pixel);
		    mChangedListener.onMoveColor(pixel);
		}
	    } else if (type == THUMB) {
		thumbProof(x, y, distance);
		if (mChangedListener != null)
		    mChangedListener.onSyncLight(light_szie);
	    }

	    break;
	case MotionEvent.ACTION_UP:
	    distance = getDistance(x, y); // 获取该点和圆心距�?
	    switch (type) {
	    case COLOR:
		if (mChangedListener != null) {
		    pixel = getImagePixel(iconPoint.x, iconPoint.y);
		    color = pixel;
		    invalidate();
		    // r = Color.red(pixel);
		    // g = Color.green(pixel);
		    // b = Color.blue(pixel);
		    mChangedListener.onColorChanged(pixel);
		    mChangedListener.onXY(iconPoint.x
			    - (imageRadius - colorRadius), iconPoint.y);
		}
		break;
	    case THUMB:
		thumbProof(x, y, distance);
		if (mChangedListener != null)
		    mChangedListener.onLight(light_szie);
		break;
	    default:
		break;
	    }
	    type = 0;// 取消类型
	    break;

	default:
	    break;
	}
	return true;
    }

    /**
     * 取颜色�?
     * 
     * @param x
     * @param y
     * @return
     */
    public int getImagePixel(float x, float y) {

	Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
	// 为了防止越界
	int intX = (int) x;
	int intY = (int) y;
	if (intX < 0)
	    intX = 0;
	if (intY < 0)
	    intY = 0;
	if (intX >= bitmap.getWidth()) {
	    intX = bitmap.getWidth() - 1;
	}
	if (intY >= bitmap.getHeight()) {
	    intY = bitmap.getHeight() - 1;
	}
	int pixel = bitmap.getPixel(intX, intY);
	return pixel;

    }

    private int measureWidth(int measureSpec) {
	int result = 0;
	int specMode = MeasureSpec.getMode(measureSpec);
	int specSize = MeasureSpec.getSize(measureSpec);

	if (specMode == MeasureSpec.EXACTLY) {
	    // We were told how big to be
	    result = specSize;
	} else {
	    // Measure the text
	    // result = imageBmp.getWidth() + thumbBmp.getHeight();
	    if (specMode == MeasureSpec.AT_MOST) {
		// Respect AT_MOST value if that was what is called for by
		// measureSpec
		result = Math.min(result, specSize);// 60,480
	    } else {

	    }
	}

	return result;
    }

    public void setOnColorChangedListenner(OnColorChangedListener l) {
	this.mChangedListener = l;
    }

    private OnColorChangedListener mChangedListener;

    // 内部接口 回调颜色 rgb�?
    public interface OnColorChangedListener {
	// 手指抬起，确定颜色回�?
	void onColorChanged(int pixel);

	// 移动时颜色回�?
	void onMoveColor(int pixel);

	void onHeightSzie(int left, int right);

	void onLight(double szie);

	void onXY(float x, float y);
	void onSyncLight(double size);
    }

    private int measureHeight(int measureSpec) {
	int result = 0;
	int specMode = MeasureSpec.getMode(measureSpec);
	int specSize = MeasureSpec.getSize(measureSpec);

	if (specMode == MeasureSpec.EXACTLY) {
	    // We were told how big to be
	    result = specSize;
	} else {
	    // result = imageBmp.getHeight() + thumbBmp.getHeight();
	    if (specMode == MeasureSpec.AT_MOST) {
		// Respect AT_MOST value if that was what is called for by
		// measureSpec
		result = Math.min(result, specSize);
	    }
	}
	return result;
    }
}

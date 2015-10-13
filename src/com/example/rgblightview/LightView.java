package com.example.rgblightview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ScrollView;

/**
 * @author LiuXinYi
 * @Date 2015�?�?6�?下午2:52:34
 * @Description [新版灯控界面]
 * @version 1.0.0
 */
public class LightView extends ImageView {
    public LightView(Context context) {
	this(context, null);
    }

    public LightView(Context context, AttributeSet attrs) {
	this(context, attrs, 0);
    }

    public LightView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	// init();
    }

    private final int TYPE_BTN = 1;
    private final int TYPE_TEMP = 2;
    private final int TYPE_LIGHT = 3;
    private Paint tempPaint; // 色温滑块
    private Paint lightPaint; // 亮度滑块

    private PointF tempPoint; //
    private PointF lightPoint; //
    private Bitmap tempBmp;
    private Bitmap lightBmp;
    private float btnRadius;// 里面�?��按钮半径
    private float tempRadius;// 色温半径
    private float lightRadius;// 亮度半径
    private float lightWidth, tempWidth, imageRadius;
    private float btnRatio = (float) 117 / 367;
    private float tempRation = (float) 208 / 367;
    private float lightRation = (float) 288 / 367;
    private float diffLight, diffTemp;
    private OnLightListener listener;

    public void setOnLightListener(OnLightListener l) {
	this.listener = l;
    }

    private double lightSize = 0;
    private double tempSize = 0;
    boolean isInit = false;
    private boolean isOpen = true;
    private boolean isHaveTemp = true;

    public boolean isOpen() {
	return isOpen;
    }

    public void setOpen(boolean isOpen) {
	this.isOpen = isOpen;
	setImage();
	invalidate();
    }

    public boolean isHaveTemp() {
	return isHaveTemp;

    }

    public void setHaveTemp(boolean isHaveTemp) {
	this.isHaveTemp = isHaveTemp;
	setImage();
	invalidate();
    }

    private void setImage() {
	if (isOpen) {
	    if (isHaveTemp) {
		setImageResource(R.drawable.light_image);
	    } else {
		setImageResource(R.drawable.light_image2);
	    }
	} else {
	    if (isHaveTemp) {
		setImageResource(R.drawable.light_close);
	    } else {
		setImageResource(R.drawable.light_close2);
	    }

	}
    }

    // Bitmap imageBmp;

    private void init() {
	isInit = true;
	setAdjustViewBounds(true);
	textPaint = new Paint();
	tempPaint = new Paint();
	lightPaint = new Paint();
	tempPoint = new PointF();
	lightPoint = new PointF();
	setImage();

	tempBmp = BitmapFactory.decodeResource(getContext().getResources(),
		R.drawable.temp_thumb);//
	lightBmp = BitmapFactory.decodeResource(getContext().getResources(),
		R.drawable.light_thumb);//
	// imageBmp = BitmapFactory.decodeResource(getContext().getResources(),
	// R.drawable.light_image);
	//
	// this.setMaxHeight(imageBmp.getHeight());
	// this.setMaxWidth(imageBmp.getWidth());
	imageRadius = getWidth() / 2;
	lightWidth = lightBmp.getWidth() / 2;
	tempWidth = tempBmp.getHeight() / 2;
	btnRadius = imageRadius * btnRatio;
	tempRadius = imageRadius * tempRation;
	lightRadius = imageRadius * lightRation;
	diffLight = imageRadius - lightRadius;
	diffTemp = imageRadius - tempRadius;
	setLightSize(lightSize);
	setTempSize(tempSize);

	//
	// // // 初始化颜色取色器位置
	// iconPoint.x = imageRadius;
	// iconPoint.y = imageRadius;
	// thumbPoint.x = imageRadius;
	// thumbPoint.y = thumbHeight;
	// thumbX = (float) (imageRadius / 2.5);
	// thumbY = imageRadius * 2;
	// thumbProof(thumbX, thumbY, getDistance(thumbX, thumbY));
    }

    float textSize;

    @Override
    public void draw(Canvas canvas) {
	// TODO Auto-generated method stub
	super.draw(canvas);
	if (tempBmp == null) {
	    init();
	}
	if (isOpen) {
	    if (isHaveTemp) {
		canvas.drawBitmap(tempBmp, tempPoint.x - tempWidth, tempPoint.y
			- tempWidth, tempPaint);
	    }
	    canvas.drawBitmap(lightBmp, lightPoint.x - lightWidth, lightPoint.y
		    - lightWidth, lightPaint);
	    text = "开启";
	    textPaint.setColor(Color.parseColor("#FF9920"));
	} else {
	    textPaint.setColor(Color.parseColor("#BDBDBD"));
	    text = "关闭";
	}
	if (textSize == 0) {
	    textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
		    21, Resources.getSystem().getDisplayMetrics());
	}
	textPaint.setTextSize(textSize);
	canvas.drawText(text, imageRadius - textSize, lightRadius * 2
		+ diffLight - textSize / 2, textPaint);
    }

    private Paint textPaint;
    private String text = "开启";
    private int TOUCH_TYPE = 0;

    private void Log(String text) {
	android.util.Log.e(VIEW_LOG_TAG, text);
    }

    private void tempProof(float x, float y, float distance) {
	// float h = x - imageRadius; // 取xy点和圆点 的三角形�?
	// float w = y - imageRadius;// 取xy点和圆点 的三角形�?
	// float h2 = h * h;
	// float w2 = w * w;
	// float distance = (float) Math.sqrt((h2 + w2)); // 勾股定理�?斜边距离
	float maxX = x - imageRadius;
	float maxY = y - imageRadius;
	x = ((tempRadius * maxX) / distance) + imageRadius; // 通过三角形一边平行原理求出x,y
	y = ((tempRadius * maxY) / distance) + imageRadius;

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
	    tempPoint.x = x;
	    tempPoint.y = y;
	    invalidate();
	    double dszie = ((initDegrees - degrees) / (300));
	    if (dszie < 0) {
		dszie = (360 - degrees + initDegrees) / (300);
	    }
	    dszie = dszie * 100;
	    tempSize = dszie;

	}

    }

    private int left_h, right_h;
    // 计算�?亮度�?时的角度
    double initDegrees = 240;

    // (1.5 * Math.PI - Math.atan2(124.5, 239.5)) * 180
    // / Math.PI;

    private void lightProof(float x, float y, float distance) {
	// float h = x - imageRadius; // 取xy点和圆点 的三角形�?
	// float w = y - imageRadius;// 取xy点和圆点 的三角形�?
	// float h2 = h * h;
	// float w2 = w * w;
	// float distance = (float) Math.sqrt((h2 + w2)); // 勾股定理�?斜边距离
	float maxX = x - imageRadius;
	float maxY = y - imageRadius;
	x = ((lightRadius * maxX) / distance) + imageRadius; // 通过三角形一边平行原理求出x,y
	y = ((lightRadius * maxY) / distance) + imageRadius;
	int rX = (int) (x - imageRadius);
	int rY = (int) (imageRadius - y);
	double degrees = Math.toDegrees(Math.atan2(rY, rX));
	if (degrees < 0) {
	    degrees = degrees + 360; // 小于0�?60
	}
	// Log.e(VIEW_LOG_TAG, "角度:" + degrees);
	if (degrees >= 240 && degrees <= 305) {
	    // 这里是点击了下面空白区域
	} else {
	    lightPoint.x = x;
	    lightPoint.y = y;
	    invalidate();
	    if (degrees < 90 || degrees > 300) {
		left_h = getHeight();
		if (degrees > 250) {
		    right_h = (int) y;
		} else {
		    right_h = (int) y - 3;
		}
	    } else {
		right_h = 0;
		left_h = (int) (getHeight() - y) + 5;
	    }
	    double dszie = ((initDegrees - degrees) / (295));
	    if (dszie < 0) {
		dszie = (360 - degrees + initDegrees) / (295);
	    }
	    dszie = dszie * 100;
	    lightSize = dszie;
	    if (listener != null) {
		listener.onHeightSzie(left_h, right_h);
	    }
	}

    }

    public float lightSizeToDegrees(double size) {
	float rat = (float) size / 100;
	float d = 300 * rat;
	float x = 240 - d;
	if (x < 0) {
	    x = x + 360;
	}
	return x;
    }

    public void setLightSize(double size) {
	this.lightSize = size;
	if (!isInit) {
	    return;
	}
	lightDegreesToXY(lightSizeToDegrees(size));
    }

    public void setTempSize(double size) {
	this.tempSize = size;
	if (!isInit) {
	    return;
	}

	tempDegreesToXY(lightSizeToDegrees(size));
    }

    private void tempDegreesToXY(double degrees) {

	if (degrees >= 240 && degrees <= 300) {
	    // 这里是点击了下面空白区域
	    degrees = 240;
	}

	degrees = Math.PI * degrees / 180;
	double x, y;
	double a = Math.cos(degrees) * tempRadius;
	double b = Math.sin(degrees) * tempRadius;

	x = a + tempRadius + diffTemp;
	y = tempRadius - b + diffTemp;

	tempPoint.x = (float) x;
	tempPoint.y = (float) y;
	invalidate();
    }

    private void lightDegreesToXY(double degrees) {

	if (degrees >= 240 && degrees <= 300) {
	    // 这里是点击了下面空白区域
	    degrees = 240;
	}
	double radian = Math.PI * degrees / 180;
	double x, y;
	double a = Math.cos(radian) * lightRadius;
	double b = Math.sin(radian) * lightRadius;

	x = a + lightRadius + diffLight;
	y = lightRadius - b + diffLight;

	lightPoint.x = (float) x;
	lightPoint.y = (float) y;
	invalidate();
	if (degrees < 90 || degrees > 300) {
	    left_h = getHeight();
	    if (degrees > 250) {
		right_h = (int) y;
	    } else {
		right_h = (int) y - 3;
	    }
	} else {
	    right_h = 0;
	    left_h = (int) (getHeight() - y) + 5;
	}
	double dszie = ((initDegrees - degrees) / (295));
	if (dszie < 0) {
	    dszie = (360 - degrees + initDegrees) / (295);
	}
	dszie = dszie * 100;
	lightSize = dszie;
	if (listener != null) {
	    listener.onHeightSzie(left_h, right_h);
	}

    }

    ScrollView scrollView;

    // public void
    @Override
    public boolean onTouchEvent(MotionEvent event) {
	float x = event.getX();
	float y = event.getY();
	// 禁用
	float distance = 0;
	boolean touch = true;
	switch (event.getAction()) {
	case MotionEvent.ACTION_DOWN:
	    distance = getDistance(x, y); // 获取该点和圆心距�?
	    // Log.e(VIEW_LOG_TAG, "现在type:" + type);
	    if (distance <= btnRadius) {
		TOUCH_TYPE = TYPE_BTN;
	    } else if (distance > (tempRadius - tempWidth)
		    && distance < (tempRadius + tempWidth) && isHaveTemp) {
		TOUCH_TYPE = TYPE_TEMP;
		// if (scrollView != null) {
		// scrollView.setEnabledRoll(true);
		// }
		tempProof(x, y, distance);
	    } else if (distance > (lightRadius - lightWidth)
		    && distance < (lightRadius + lightWidth)) {
		// if (scrollView != null) {
		// scrollView.setEnabledRoll(true);
		// }
		TOUCH_TYPE = TYPE_LIGHT;
		lightProof(x, y, distance);
	    } else {
		touch = false;
		// if (scrollView != null) {
		// scrollView.setEnabledRoll(false);
		// }
		// 启动
		TOUCH_TYPE = -1;
		Log("其他动作:" + distance);
	    }
	    // if (TOUCH_TYPE == TYPE_TEMP) {
	    // if (!isHaveTemp) {
	    // TOUCH_TYPE = -1;
	    // }
	    // }
	    break;
	case MotionEvent.ACTION_MOVE:
	    distance = getDistance(x, y); // 获取该点和圆心距�?

	    switch (TOUCH_TYPE) {
	    case TYPE_BTN:

		break;
	    case TYPE_TEMP:
		tempProof(x, y, distance);
		if (listener != null) {
		    listener.onSyncTemp(tempSize);
		}
		break;
	    case TYPE_LIGHT:
		lightProof(x, y, distance);
		if (listener != null) {
		    listener.onSyncLight(lightSize);
		}
		break;
	    default:
		touch = false;
		break;
	    }

	    break;
	case MotionEvent.ACTION_UP:

	    distance = getDistance(x, y); // 获取该点和圆心距�?
	    switch (TOUCH_TYPE) {
	    case TYPE_BTN:
		if (distance <= btnRadius) {
		    // TOUCH_TYPE = TYPE_BTN;
		    Log("回调按钮点击");
		    if (listener != null) {
			listener.onBtnClick();
		    }
		}
		break;
	    case TYPE_TEMP:
		if (listener != null) {
		    listener.onTemp(tempSize);
		}
		break;
	    case TYPE_LIGHT:
		if (listener != null) {
		    listener.onLight(lightSize);
		}
		break;
	    default:
		touch = false;
		break;
	    }
	    // if (scrollView != null) {
	    // scrollView.setEnabledRoll(false);
	    // }
	    break;

	default:
	    break;
	}
	return touch;
    }

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

    public interface OnLightListener {
	void onBtnClick();

	void onSyncTemp(double temp);

	void onSyncLight(double light);

	void onLight(double light);

	void onTemp(double temp);

	void onHeightSzie(int left, int right);
    }
}

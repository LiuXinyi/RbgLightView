package com.example.rgblightview;

import com.example.rgblightview.LightView.OnLightListener;
import com.example.rgblightview.RgbView.OnColorChangedListener;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    // 背景，控制滑动条颜色
    private View left_view, right_view, left_view2, right_view2;

    // 新版灯控界面
    private LightView lightView;
    // 新版取色界面
    private RgbView rgb_view;

    private TextView rgb_text, temp_text, light_text;
    // 旧版灯控界面
    private MyRgbPickerView rgbPickerView;
    // 两个layout
    private View smart_light_layout, new_smart_light_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// requestWindowFeature(Window.FEATURE_NO_TITLE);
	super.onCreate(savedInstanceState);
	setContentView(R.layout.new_smart_layout);
	smart_light_layout = findViewById(R.id.smart_light_layout);
	new_smart_light_layout = findViewById(R.id.new_smart_light_layout);

	rgb_text = (TextView) findViewById(R.id.rgb);
	temp_text = (TextView) findViewById(R.id.temp);
	light_text = (TextView) findViewById(R.id.light);
	// System.out.println(this.getApplicationContext().ge + "");
	left_view = findViewById(R.id.left_view);
	rgb_view = (RgbView) findViewById(R.id.rgb_view);
	right_view = findViewById(R.id.right_view);
	lightView = (LightView) findViewById(R.id.light_view);

	rgb_view.setOnColorChangedListener(new OnColorChangedListener() {

	    @Override
	    public void onMoveColor(int pixel) {
		// TODO Auto-generated method stub
		colorChanged(pixel);// 拖动时颜色改变
	    }

	    @Override
	    public void onColorChanged(int pixel) {
		// TODO Auto-generated method stub
		colorChanged(pixel);// 手指抬起，最终确定的颜色
	    }
	});
	lightView.setOnLightListener(new OnLightListener() {

	    @Override
	    public void onTemp(double temp) {
		tempChanged(temp);// 里面圈圆的
	    }

	    @Override
	    public void onLight(double light) {
		lightChanged(light);
	    }

	    @Override
	    public void onHeightSzie(int left, int right) {
		// TODO Auto-generated method stub
		LayoutParams leftParams = left_view.getLayoutParams();
		leftParams.height = left;
		left_view.setLayoutParams(leftParams);
		LayoutParams rightParams = right_view.getLayoutParams();
		rightParams.height = right;
		right_view.setLayoutParams(rightParams);
	    }

	    @Override
	    public void onBtnClick() {
		// TODO Auto-generated method stub
		lightView.setOpen(!lightView.isOpen());
		// rgb_view.setOpen(!rgb_view.isOpen());
		if (lightView.isOpen()) {
		    left_view.setBackgroundColor(Color.WHITE);
		    right_view.setBackgroundColor(Color.WHITE);
		} else {
		    left_view.setBackgroundColor(getResources().getColor(
			    android.R.color.transparent));
		    right_view.setBackgroundColor(getResources().getColor(
			    android.R.color.transparent));
		}
	    }

	    @Override
	    public void onSyncTemp(double temp) {
		// TODO Auto-generated method stub
		tempChanged(temp);
	    }

	    @Override
	    public void onSyncLight(double light) {
		lightChanged(light);
	    }
	});
	initView2();
    }

    /**
     * 初始化旧版灯控界面
     */
    private void initView2() {
	left_view2 = findViewById(R.id.left_view2);
	right_view2 = findViewById(R.id.right_view2);
	// 设置动画时间
	alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
	alphaAnimation.setDuration(300);
	switch_btn = (ImageView) findViewById(R.id.switch_btn);

	switch_btn.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		if (rgbPickerView.isOpen()) {
		    rgbPickerView.setImageResource(R.drawable.rbg_image_gone);
		    rgbPickerView.setOpen(false);
		    switch_btn.setBackgroundColor(Color.WHITE);
		    switch_btn.setSelected(true);
		} else {
		    rgbPickerView.setImageResource(R.drawable.rgb_bg);
		    rgbPickerView.setOpen(true);
		    switch_btn.setSelected(false);
		}

	    }
	});

	rgbPickerView = (MyRgbPickerView) findViewById(R.id.rgb_view2);
	rgbPickerView
		.setOnColorChangedListenner(new com.example.rgblightview.MyRgbPickerView.OnColorChangedListener() {

		    @Override
		    public void onXY(float x, float y) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void onMoveColor(int pixel) {
			// TODO Auto-generated method stub
			colorChanged(pixel);
		    }

		    @Override
		    public void onLight(double szie) {
			lightChanged(szie);
		    }

		    @Override
		    public void onHeightSzie(int left, int right) {
			// TODO Auto-generated method stub
			LayoutParams leftParams = left_view2.getLayoutParams();
			leftParams.height = left;
			left_view2.setLayoutParams(leftParams);
			LayoutParams rightParams = right_view2
				.getLayoutParams();
			rightParams.height = right;
			right_view2.setLayoutParams(rightParams);
		    }

		    @Override
		    public void onColorChanged(int pixel) {
			// TODO Auto-generated method stub
			colorChanged(pixel);
		    }

		    @Override
		    public void onSyncLight(double size) {
			// TODO Auto-generated method stub
			lightChanged(size);
		    }
		});
    }

    ImageView switch_btn;
    Animation alphaAnimation;

    private void colorChanged(int pixel) {

	int r = Color.red(pixel);
	int g = Color.green(pixel);
	int b = Color.blue(pixel);
	rgb_text.setText("颜色值 r：" + r + " g:" + g + " b:" + b);
	rgb_text.setTextColor(pixel);
	switch_btn_Selected(pixel);
	left_view2.setBackgroundColor(pixel);
	right_view2.setBackgroundColor(pixel);
	right_view2.startAnimation(alphaAnimation);
	left_view2.startAnimation(alphaAnimation);
	switch_btn.startAnimation(alphaAnimation);
	switch_btn.setBackgroundColor(pixel);
    }

    private void lightChanged(double light) {
	if (light > 99.4) {
	    light = 100;
	}
	if (light < 0.6) {
	    light = 0;
	}
	light_text.setText("亮度：" + light);
    }

    private void tempChanged(double temp) {
	if (temp > 99.4) {
	    temp = 100;
	}
	if (temp < 0.6) {
	    temp = 0;
	}
	temp_text.setText("色温：" + temp);
    }

    private void switch_btn_Selected(int pixel) {
	int r = Color.red(pixel);
	int g = Color.green(pixel);
	int b = Color.blue(pixel);
	switch_btn.setImageResource(R.drawable.light_btn__select);
	if (r > 198 && g > 198 && b > 198) {// 如果颜色为高亮
	    switch_btn.setSelected(true);
	} else {
	    switch_btn.setSelected(false);
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.action_settings) {
	    if (new_smart_light_layout.getVisibility() == View.VISIBLE) {
		new_smart_light_layout.setVisibility(View.GONE);
		smart_light_layout.setVisibility(View.VISIBLE);
	    } else {
		new_smart_light_layout.setVisibility(View.VISIBLE);
		smart_light_layout.setVisibility(View.GONE);
	    }
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }
}

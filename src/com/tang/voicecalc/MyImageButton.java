package com.tang.voicecalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

public class MyImageButton extends ImageButton {
	static final int CLICK_DURATION = 600;
	static final int CLICK_INTERVAL = 10;
	int CLICK_COLOR;
	long mAnimStart = -1L;
	Paint mPaint;

	public MyImageButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyImageButton(Context context, AttributeSet attrs) {
		// super(context, attrs);
		// TODO Auto-generated constructor stub
		super(context, attrs);
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		this.CLICK_COLOR = getResources().getColor(R.color.green);
		this.mPaint = new Paint();
		this.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		this.mPaint.setStrokeWidth(2.0F);
		this.mAnimStart = -1L;

	}

	public MyImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		int i = 0;
		if (this.mAnimStart != -1L) {
			i = (int) (System.currentTimeMillis() - this.mAnimStart);
			if (i >= 600)
				this.mAnimStart = -1L;
		}
		while (!isPressed()) {
			drawRect(i, canvas);
			postInvalidateDelayed(10L);
			return;
		}
		drawRect(0, canvas);
	}

	private void drawRect(int paramInt, Canvas canvas) {
		// TODO Auto-generated method stub
		int i = 255 - paramInt * 255 / 600;
		int j = this.CLICK_COLOR | i << 24;
		this.mPaint.setColor(j);
		canvas.drawBitmap(Bitmap.createScaledBitmap(
				((BitmapDrawable) getResources().getDrawable(R.drawable.style1_darkorange))
						.getBitmap(), getWidth(), getHeight(), true), null,
				new RectF(0.0F, 0.0F, getWidth(), getHeight()), this.mPaint);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		// return super.onTouchEvent(event);
		boolean bool = super.onTouchEvent(event);
		switch (event.getAction()) {
		case 2:
		default:
			return bool;
		case 1:
			animateClick();
			return bool;
		case 0:
			vibrate();
		case 3:
		}
		invalidate();
		return bool;
	}

	private void animateClick() {
		// TODO Auto-generated method stub
		this.mAnimStart = System.currentTimeMillis();
		invalidate();
	}

	private void vibrate() {
		// TODO Auto-generated method stub
		SharedPreferences localSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getContext());
		Boolean localBoolean = Boolean.valueOf(localSharedPreferences
				.getBoolean("button_vibration_check_box", false));
		int i = Integer
				.valueOf(
						localSharedPreferences.getString(
								"vibration_length_list_preference", "30 ms")
								.split(" ")[0]).intValue();
		if (localBoolean.booleanValue())
			MainActivity.vibrator.vibrate(i);

	}

}

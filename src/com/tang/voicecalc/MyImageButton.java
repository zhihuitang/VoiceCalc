package com.tang.voicecalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageButton;

public class MyImageButton extends ImageButton {
	String TAG = "MyImageButtion";
	static final int CLICK_DURATION = 600;
	static final int CLICK_INTERVAL = 10;
	int CLICK_COLOR;
	long mAnimStart = -1L;
	Paint mPaint;
	  public boolean isSoundsLoaded;

	public final float[] BT_SELECTED=new float[]
			{ 2, 0, 0, 0, 2,
			0, 2, 0, 0, 2,
			0, 0, 2, 0, 2,
			0, 0, 0, 1, 0 };
			public final float[] BT_NOT_SELECTED=new float[]
			{ 1, 0, 0, 0, 0,
			0, 1, 0, 0, 0,
			0, 0, 1, 0, 0,
			0, 0, 0, 1, 0 };
			
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
//
//		int i = 0;
//		// Log.i(TAG, String.format("mAnimStart : %d", this.mAnimStart));
//		if (this.mAnimStart != -1L) {
//			i = (int) (System.currentTimeMillis() - this.mAnimStart);
//			Log.i(TAG, String.format("time gap: %d", i));
//			if (i >= 600)
//				this.mAnimStart = -1L;
//
//		}
//		if (isPressed()) {
//			Log.i(TAG, String.format("pressed: %d", i));
//			drawRect(i, canvas);
//			postInvalidateDelayed(1000L);
//			return;
//		}else{
//			//drawRect(i, canvas);
//		}
	}

	private void drawRect(int paramInt, Canvas canvas) {
		// TODO Auto-generated method stub
		int i = 255 - paramInt * 255 / 600;
		int j = this.CLICK_COLOR | i << 24;
		BitmapDrawable bitmap = (BitmapDrawable) getResources().getDrawable(
				R.drawable.style1_darkorange);
		this.mPaint.setColor(j);
		canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap.getBitmap(),
				getWidth(), getHeight(), true), null, new RectF(0.0F, 0.0F,
				getWidth(), getHeight()), this.mPaint);

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
		case MotionEvent.ACTION_UP:
			this.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
//			this.setBackgroundDrawable(this.getBackground());
//			animateClick();
			break;
		case MotionEvent.ACTION_DOWN:
			this.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
//			this.setBackgroundDrawable(this.getBackground());
			vibrate();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		default:
			return bool;
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

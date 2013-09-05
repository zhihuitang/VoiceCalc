package com.tang.voicecalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.ColorMatrixColorFilter;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

public class MyButton extends Button {
	
	String TAG = "MyButton";
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

	public MyButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public MyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.widget.TextView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
//		return super.onTouchEvent(event);
		boolean bool = super.onTouchEvent(event);
		Log.i(TAG, String.format("onTouchEvent: %d", event.getAction()));
		switch (event.getAction()) {
		case MotionEvent.ACTION_CANCEL:
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

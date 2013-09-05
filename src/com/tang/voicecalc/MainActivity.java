package com.tang.voicecalc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MainActivity extends SlidingActivity implements OnClickListener {

	String s = "0";
	EditText editTextExpression;
	static Vibrator vibrator;
	private int characterThis;
	private int characterLast;

	private final String TAG = "MainActivity";
	boolean isSoundsLoaded = false;
	SoundPool soundPool;
	int soundTap;
	SoundThread soundThread;
	int soundType; // -1-None, 0-English, 1-Swedish, 2-Chinese

	Map<String, int[]> soundMap = new HashMap<String, int[]>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * 初始化 SlidingActivity
		 */
        setContentView(R.layout.main);
		MyButton number[] = new MyButton[10];
		MyButton symbol_dot, symbol_bracket_left, symbol_bracket_right, symbol_equal;
		MyImageButton symbol_c, symbol_back;
		MyButton symbol_plus, symbol_substract, symbol_multiply, symbol_divide;
        
     // set the Behind View
        setBehindContentView(R.layout.frame_menu);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        SettingsPreferenceFragment sfp = new SettingsPreferenceFragment();
        fragmentTransaction.replace(R.id.menu, sfp);
        //fragmentTransaction.replace(R.id.content, new ContentFragment("Welcome"),"Welcome");
        fragmentTransaction.commit();

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidth(50);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffset(60);
        sm.setFadeDegree(0.35f);
        //设置slding menu的几种手势模式
        //TOUCHMODE_FULLSCREEN 全屏模式，在content页面中，滑动，可以打开sliding menu
        //TOUCHMODE_MARGIN 边缘模式，在content页面中，如果想打开slding ,你需要在屏幕边缘滑动才可以打开slding menu
        //TOUCHMODE_NONE 自然是不能通过手势打开啦
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        //使用左上方icon可点，这样在onOptionsItemSelected里面才可以监听到R.id.home
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        /*
         * 初始化 SlidingActivity
         */

		number[0] = (MyButton) findViewById(R.id.button0);
		number[1] = (MyButton) findViewById(R.id.button1);
		number[2] = (MyButton) findViewById(R.id.button2);
		number[3] = (MyButton) findViewById(R.id.button3);
		number[4] = (MyButton) findViewById(R.id.button4);
		number[5] = (MyButton) findViewById(R.id.button5);
		number[6] = (MyButton) findViewById(R.id.button6);
		number[7] = (MyButton) findViewById(R.id.button7);
		number[8] = (MyButton) findViewById(R.id.button8);
		number[9] = (MyButton) findViewById(R.id.button9);

		symbol_c = (MyImageButton) findViewById(R.id.button_c);
		symbol_back = (MyImageButton) findViewById(R.id.button_back);

		symbol_plus = (MyButton) findViewById(R.id.button_plus);
		symbol_multiply = (MyButton) findViewById(R.id.button_multiply);
		symbol_divide = (MyButton) findViewById(R.id.button_divide);
		symbol_substract = (MyButton) findViewById(R.id.button_substract);

		symbol_dot = (MyButton) findViewById(R.id.button_dot);
		symbol_bracket_left = (MyButton) findViewById(R.id.button_bracket_left);
		symbol_bracket_right = (MyButton) findViewById(R.id.button_bracket_right);
		symbol_equal = (MyButton) findViewById(R.id.button_equal);

		editTextExpression = (EditText) findViewById(R.id.textView1);
		editTextExpression.setText(s);

		for (int i = 0; i < 10; i++) {
			number[i].setOnClickListener(this);
		}
		symbol_c.setOnClickListener(this);
		symbol_back.setOnClickListener(this);
		symbol_plus.setOnClickListener(this);
		symbol_substract.setOnClickListener(this);
		symbol_multiply.setOnClickListener(this);
		symbol_divide.setOnClickListener(this);
		symbol_dot.setOnClickListener(this);
		symbol_bracket_left.setOnClickListener(this);
		symbol_bracket_right.setOnClickListener(this);
		symbol_equal.setOnClickListener(this);

		Init();
	}

	private void Init() {
		// TODO Auto-generated method stub
		new initSoundTask().execute("sound");
	}

	class initSoundTask extends AsyncTask<String, Integer, Integer> {
		public initSoundTask() {
		}

		protected Integer doInBackground(String... params) {
			try {
				MainActivity.this.initSound();
				return 0;
			} catch (Exception e) {
				Log.e(TAG, "initSoundTask error!");
				e.printStackTrace();
			}
			return 0;
		}

	}

	private void initSound() {
		// TODO Auto-generated method stub
		this.isSoundsLoaded = false;
		setVolumeControlStream(3);
		new Runnable() {
			public void run() {
				Date localDate = new Date();
				Log.i(TAG, "Audio loading ......  at " + localDate);
				soundPool = new SoundPool(1, 3, 5);

				soundTap = soundPool.load(MainActivity.this, R.raw.tap, 1); // English
				int[] soundSetC = new int[3];
				soundSetC[0] = soundPool.load(MainActivity.this, R.raw.clear, 1); // English
				soundSetC[1] = soundPool.load(MainActivity.this, R.raw.clear, 1); // Swedish
				soundSetC[2] = soundPool.load(MainActivity.this, R.raw.clear, 1); // Chinese
				soundMap.put("C", soundSetC);

				int[] soundSetB = new int[3]; // Backspace
				soundSetB[0] = soundPool.load(MainActivity.this, R.raw.tap, 1); // English
				soundSetB[1] = soundPool.load(MainActivity.this, R.raw.tap, 1); // Swedish
				soundSetB[2] = soundPool.load(MainActivity.this, R.raw.tap, 1); // Chinese
				soundMap.put("B", soundSetB);

				int[] soundSet0 = new int[3];
				soundSet0[0] = soundPool.load(MainActivity.this, R.raw.zero, 1); // English
				soundSet0[1] = soundPool.load(MainActivity.this, R.raw.zero_sv, 1); // Swedish
				soundSet0[2] = soundPool.load(MainActivity.this, R.raw.zero_cn, 1); // Chinese
				soundMap.put("0", soundSet0);

				int[] soundSet1 = new int[3];
				soundSet1[0] = soundPool.load(MainActivity.this, R.raw.one, 1); // English
				soundSet1[1] = soundPool.load(MainActivity.this, R.raw.one_sv, 1); // Swedish
				soundSet1[2] = soundPool.load(MainActivity.this, R.raw.one_cn, 1); // Chinese
				soundMap.put("1", soundSet1);

				int[] soundSet2 = new int[3];
				soundSet2[0] = soundPool.load(MainActivity.this, R.raw.two, 1); // English
				soundSet2[1] = soundPool.load(MainActivity.this, R.raw.two_sv, 1); // Swedish
				soundSet2[2] = soundPool.load(MainActivity.this, R.raw.two_cn, 1); // Chinese
				soundMap.put("2", soundSet2);

				int[] soundSet3 = new int[3];
				soundSet3[0] = soundPool.load(MainActivity.this, R.raw.three, 1); // English
				soundSet3[1] = soundPool.load(MainActivity.this, R.raw.three_sv, 1); // Swedish
				soundSet3[2] = soundPool.load(MainActivity.this, R.raw.three_cn, 1); // Chinese
				soundMap.put("3", soundSet3);

				int[] soundSet4 = new int[3];
				soundSet4[0] = soundPool.load(MainActivity.this, R.raw.four, 1); // English
				soundSet4[1] = soundPool.load(MainActivity.this, R.raw.four_sv, 1); // Swedish
				soundSet4[2] = soundPool.load(MainActivity.this, R.raw.four_cn, 1); // Chinese
				soundMap.put("4", soundSet4);

				int[] soundSet5 = new int[3];
				soundSet5[0] = soundPool.load(MainActivity.this, R.raw.five, 1); // English
				soundSet5[1] = soundPool.load(MainActivity.this, R.raw.five_sv, 1); // Swedish
				soundSet5[2] = soundPool.load(MainActivity.this, R.raw.five_cn, 1); // Chinese
				soundMap.put("5", soundSet5);

				int[] soundSet6 = new int[3];
				soundSet6[0] = soundPool.load(MainActivity.this, R.raw.six, 1); // English
				soundSet6[1] = soundPool.load(MainActivity.this, R.raw.six_sv, 1); // Swedish
				soundSet6[2] = soundPool.load(MainActivity.this, R.raw.six_cn, 1); // Chinese
				soundMap.put("6", soundSet6);

				int[] soundSet7 = new int[3];
				soundSet7[0] = soundPool.load(MainActivity.this, R.raw.seven, 1); // English
				soundSet7[1] = soundPool.load(MainActivity.this, R.raw.seven_sv, 1); // Swedish
				soundSet7[2] = soundPool.load(MainActivity.this, R.raw.seven_cn, 1); // Chinese
				soundMap.put("7", soundSet7);

				int[] soundSet8 = new int[3];
				soundSet8[0] = soundPool.load(MainActivity.this, R.raw.eight, 1); // English
				soundSet8[1] = soundPool.load(MainActivity.this, R.raw.eight_sv, 1); // Swedish
				soundSet8[2] = soundPool.load(MainActivity.this, R.raw.eight_cn, 1); // Chinese
				soundMap.put("8", soundSet8);

				int[] soundSet9 = new int[3];
				soundSet9[0] = soundPool.load(MainActivity.this, R.raw.nine, 1); // English
				soundSet9[1] = soundPool.load(MainActivity.this, R.raw.nine_sv, 1); // Swedish
				soundSet9[2] = soundPool.load(MainActivity.this, R.raw.nine_cn, 1); // Chinese
				soundMap.put("9", soundSet9);

				int[] soundSetPlus = new int[3];
				soundSetPlus[0] = soundPool.load(MainActivity.this, R.raw.plus, 1); // English
				soundSetPlus[1] = soundPool.load(MainActivity.this, R.raw.plus_sv, 1); // Swedish
				soundSetPlus[2] = soundPool.load(MainActivity.this, R.raw.plus_cn, 1); // Chinese
				soundMap.put("+", soundSetPlus);

				int[] soundSetMinus = new int[3];
				soundSetMinus[0] = soundPool.load(MainActivity.this, R.raw.minus, 1); // English
				soundSetMinus[1] = soundPool.load(MainActivity.this, R.raw.minus_sv, 1); // Swedish
				soundSetMinus[2] = soundPool.load(MainActivity.this, R.raw.minus_cn, 1); // Chinese
				soundMap.put("-", soundSetMinus);

				int[] soundSetMultiply = new int[3];
				soundSetMultiply[0] = soundPool.load(MainActivity.this, R.raw.multiply, 1); // English
				soundSetMultiply[1] = soundPool.load(MainActivity.this, R.raw.multiply_sv, 1); // Swedish
				soundSetMultiply[2] = soundPool.load(MainActivity.this, R.raw.multiply_cn, 1); // Chinese
				soundMap.put("*", soundSetMultiply);

				int[] soundSetDivide = new int[3];
				soundSetDivide[0] = soundPool.load(MainActivity.this, R.raw.divide, 1); // English
				soundSetDivide[1] = soundPool.load(MainActivity.this, R.raw.divide_sv, 1); // Swedish
				soundSetDivide[2] = soundPool.load(MainActivity.this, R.raw.divide_cn, 1); // Chinese
				soundMap.put("/", soundSetDivide);

				int[] soundSetEqual = new int[3];
				soundSetEqual[0] = soundPool.load(MainActivity.this, R.raw.equal, 1); // English
				soundSetEqual[1] = soundPool.load(MainActivity.this, R.raw.equal_sv, 1); // Swedish
				soundSetEqual[2] = soundPool.load(MainActivity.this, R.raw.equal_cn, 1); // Chinese
				soundMap.put("=", soundSetEqual);

				int[] soundSetPoint = new int[3];
				soundSetPoint[0] = soundPool.load(MainActivity.this, R.raw.point, 1); // English
				soundSetPoint[1] = soundPool.load(MainActivity.this, R.raw.point_sv, 1); // Swedish
				soundSetPoint[2] = soundPool.load(MainActivity.this, R.raw.point_cn, 1); // Chinese
				soundMap.put(".", soundSetPoint);

				int[] soundBacket = new int[3];
				soundBacket[0] = soundPool.load(MainActivity.this, R.raw.bracket, 1); // English
				soundBacket[1] = soundPool.load(MainActivity.this, R.raw.bracket_sv, 1); // Swedish
				soundBacket[2] = soundPool.load(MainActivity.this, R.raw.bracket_cn, 1); // Chinese
				soundMap.put("(", soundBacket);
				soundMap.put(")", soundBacket);

				int[] soundE = new int[3];
				soundE[0] = soundPool.load(MainActivity.this, R.raw.e, 1); // English
				soundE[1] = soundPool.load(MainActivity.this, R.raw.e, 1); // Swedish
				soundE[2] = soundPool.load(MainActivity.this, R.raw.e_cn, 1); // Chinese
				soundMap.put("E", soundE);

				Log.i(TAG, "Audio loaded at " + new Date());
				MainActivity.this.isSoundsLoaded = true;
			}
		}.run();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// Button btn = (Button) v;
		String ch = "0";

		s = editTextExpression.getText().toString();

		try {
			switch (v.getId()) {
			case R.id.button0:
				// c = '0';
				ch = "0";
				break;
			case R.id.button1:
				ch = "1";
				break;
			case R.id.button2:
				ch = "2";
				break;
			case R.id.button3:
				ch = "3";
				break;
			case R.id.button4:
				ch = "4";
				break;
			case R.id.button5:
				ch = "5";
				break;
			case R.id.button6:
				ch = "6";
				break;
			case R.id.button7:
				ch = "7";
				break;
			case R.id.button8:
				ch = "8";
				break;
			case R.id.button9:
				ch = "9";
				break;
			case R.id.button_c:
				s = "";
				ch = "C";
				// soundClear = GetSoundByChar(ch);
				break;

			case R.id.button_plus:
				ch = "+";
				break;
			case R.id.button_substract:
				ch = "-";
				break;
			case R.id.button_multiply:
				ch = "*";
				break;
			case R.id.button_divide:
				ch = "/";
				break;
			case R.id.button_dot:
				ch = ".";
				break;
			case R.id.button_bracket_left:
				ch = "(";
				break;
			case R.id.button_bracket_right:
				ch = ")";
				break;
			case R.id.button_equal:
				ch = "=";
				break;
			case R.id.button_back:
				ch = "B";
				// soundBackspace = GetSoundByChar(ch);
				break;
			default:
				ch = "#";
				break;
			}
			if (this.isSoundsLoaded && this.soundThread != null) {
				//Log.i(TAG, "soundThread interrupt");
				// Thread interrupted by MainActivity
				this.soundThread.interrupt();
				soundThread = null;
			}

			PlaySoundByChar(ch);
			ViberateOnTouch();

			if (ch.charAt(0) == '#') {
				return;
			}
			if (ch.charAt(0) == '=') {
				ExpressionCalculation();
				return;
			}

			characterThis = Element.GetElementType(ch);
			if (characterThis == Element.CLEAR) {
				s = "0";
				editTextExpression.setText(s);
				return;
			} else if (ch == "B") {
				// delete last character;
				if (s.length() == 1) {
					s = "0";
				} else if (s.length() > 1) {
					s = s.substring(0, s.length() - 1);
				}

			} else if (s.contains("=")) {
				// 上一次计算过
				s = ch;
			} else if (s.equals("0") && ch.equals("0")) {
				// 00- -> 0
				s = "0";
			} else if (s.equals("0") && Element.GetElementType(ch) == Element.NUMBER) {
				// 09 -> 9
				s = ch;
			} else if (s.equals("0") && Element.GetElementType(ch) == Element.DOT) {
				s = s + ch;
			} else if (s.equals("0") && ch.equals("(")) {
				// 0( -> (
				s = ch;
			} else if (characterThis == Element.DOT && characterLast == Element.DOT) {
				// ..
				ViberateOnError();
				return;
			} else if (characterThis == Element.OPERATOR && characterLast == Element.OPERATOR) {
				// ++
				ViberateOnError();
				return;
			} else if (characterThis == Element.BRACKET_LEFT && characterLast == Element.NUMBER) {
				// 1-9(
				ViberateOnError();
				return;
			} else if (characterThis == Element.BRACKET_RIGHT && characterLast == Element.OPERATOR) {
				// +)
				ViberateOnError();
				return;
			} else if (characterThis == Element.BRACKET_LEFT && characterLast == Element.BRACKET_RIGHT) {
				// )(
				ViberateOnError();
				return;
			} else if (characterThis == Element.BRACKET_RIGHT && characterLast == Element.BRACKET_LEFT) {
				// ()
				ViberateOnError();
				return;
			} else if (characterThis == Element.BRACKET_RIGHT && s.equals("0")) {
				// 0)
				ViberateOnError();
				return;
			} else {
				s = s + ch;
			}

			// not valid char pressed
			editTextExpression.setText(s);
			characterLast = characterThis;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "OnClick() Exception");
		}
		editTextExpression.setSelection(editTextExpression.length());

	}

	private int PlaySoundByChar(String ch) {
		// TODO Auto-generated method stub
		int ret = -1;
		int sound;
		
		// default is Svenska-1
		String soundSetup = PreferenceManager.getDefaultSharedPreferences(this).getString(
				"sound_setup_list_preference", "1");
		

		// Log.i(TAG, String.format("soundSetup: %s", soundSetup));

		soundType = Integer.parseInt(soundSetup);
		if (soundType < 0) {
			return ret;
		}

		if (this.isSoundsLoaded) {
			// Log.i(TAG, String.format("ch = %s", ch));
			if (soundMap.containsKey(ch)) {
				sound = soundMap.get(ch)[soundType];
				if (sound != 0) {
					ret = this.soundPool.play(sound, 1.0F, 1.0F, 1, 0, 1.0F);
				}
			} else {
				Log.e(TAG, String.format("Cannot find audio [%s]", ch));
			}
		}
		return ret;
	}

	private void ViberateOnTouch() {
		// TODO Auto-generated method stub

		String viberateSetup = PreferenceManager.getDefaultSharedPreferences(this).getString(
				"viberate_setup_list_preference", "2");
		//Log.i(TAG, String.format("viberatesetup:%s", viberateSetup));
		if (viberateSetup.equals("1")) {
			// viberate on touch
			vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			long[] pattern = { 40, 50 }; // 停止 开启 停止 开启
			vibrator.vibrate(pattern, -1); // 重复两次上面的pattern
		}
	}

	private void ViberateOnError() {
		// TODO Auto-generated method stub
		String viberateSetup = PreferenceManager.getDefaultSharedPreferences(this).getString(
				"viberate_setup_list_preference", "2");
		if (viberateSetup.equals("1") || viberateSetup.equals("2")) {
			// viberate on Error
			Log.e(TAG, "viberate on Error");
			vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			long[] pattern = { 10, 50, 50, 100 }; // 停止 开启 停止 开启
			vibrator.vibrate(pattern, -1); // 重复两次上面的pattern
		}
	}

	private void ExpressionCalculation() {
		// TODO Auto-generated method stub
		String expression = editTextExpression.getText().toString();

		// 去掉 = 右边的字符

		int n = expression.indexOf("=");
		if (n > -1) {
			expression = expression.substring(0, n).trim();
		}

		//		Log.i(TAG, expression);

		EvaluateExpression evaluateExpression = new EvaluateExpression(expression);

		String s = evaluateExpression.CalculateExpression();
		String res = String.format("%s=%s", expression, s);
		editTextExpression.setText(res);

		editTextExpression.setSelection(editTextExpression.length());

		soundThread = new SoundThread(s);
		soundThread.start();

		//		Log.i(TAG, res);
	}

	public class SoundThread extends Thread {
		String soundString;

		public SoundThread(String s) {
			this.soundString = s;
		}

		public void run() {
			try {
				for (int i = 0; i < soundString.length(); i++) {
					if( soundType == 0){
						// English
						sleep(600L);
					}else if( soundType == 1 ){
						// Swedish
						sleep(900L);
					}else if( soundType == 2)
					{	// Chinese
						sleep(500L);
					}
					PlaySoundByChar(soundString.substring(i, i + 1));
				}
			} catch (InterruptedException e) {
				//				e.printStackTrace();
				return;
			}
		}
	}

	public void playResultSound(int intValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// return super.onOptionsItemSelected(item);
		super.onOptionsItemSelected(item);
        toggle();
//
//		Intent intent = new Intent(this, SettingsActivity.class);
//		startActivity(intent);
		return false;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (this.isSoundsLoaded && this.soundThread != null) {
			//Log.i(TAG, "soundThread interrupt");
			// Thread interrupted by MainActivity
			this.soundThread.interrupt();
			soundThread = null;
		}

	}
	
}

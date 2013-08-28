package com.tang.voicecalc;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {

	String s = "0";
	EditText editTextExpression;
	static Vibrator vibrator;
	private int characterThis;
	private int characterLast;

	private final String TAG = "MainActivity";
	boolean isSoundsLoaded = false;
	SoundPool soundPool;
	int soundBackspace;
	int soundClear;
	int soundTap;
	SoundThread soundThread;

	Map<String, int[]> soundMap = new HashMap<String, int[]>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyButton number[] = new MyButton[10];
		MyButton symbol_minus, symbol_dot, symbol_bracket_left, symbol_bracket_right, symbol_equal;
		MyImageButton symbol_c, symbol_back;
		MyButton symbol_plus, symbol_substract, symbol_multiply, symbol_divide;

		setContentView(R.layout.main);

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

		symbol_minus = (MyButton) findViewById(R.id.button_minus);

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
		symbol_minus.setOnClickListener(this);
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
				Log.i(TAG, "initSoundTask error!");
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
				Log.i(TAG, "声音加载线程" + localDate);
				soundPool = new SoundPool(1, 3, 5);
				soundBackspace = soundPool
						.load(MainActivity.this, R.raw.tap, 1);
				soundClear = soundPool.load(MainActivity.this, R.raw.clear, 1);
				soundTap = soundBackspace;

				int[]	soundSet1 = new int[3];
				soundSet1[0]	= soundPool.load(MainActivity.this, R.raw.one, 1);		// English
				soundSet1[1]	= soundPool.load(MainActivity.this, R.raw.clear,1);	// Swedish
				soundSet1[2]	= soundPool.load(MainActivity.this, R.raw.one_cn,1);	// Chinese
				soundMap.put("1", soundSet1);

				int[]	soundSet2 = new int[3];
				soundSet2[0]	= soundPool.load(MainActivity.this, R.raw.two, 1);		// English
				soundSet2[1]	= soundPool.load(MainActivity.this, R.raw.clear,1);	// Swedish
				soundSet2[2]	= soundPool.load(MainActivity.this, R.raw.two_cn,1);	// Chinese
				soundMap.put("2", soundSet2);
				
				int[]	soundSet3 = new int[3];
				soundSet3[0]	= soundPool.load(MainActivity.this, R.raw.three, 1);		// English
				soundSet3[1]	= soundPool.load(MainActivity.this, R.raw.clear,1);	// Swedish
				soundSet3[2]	= soundPool.load(MainActivity.this, R.raw.three_cn,1);	// Chinese
				soundMap.put("3", soundSet3);

				int[]	soundSet4 = new int[3];
				soundSet4[0]	= soundPool.load(MainActivity.this, R.raw.four, 1);		// English
				soundSet4[1]	= soundPool.load(MainActivity.this, R.raw.clear,1);	// Swedish
				soundSet4[2]	= soundPool.load(MainActivity.this, R.raw.four_cn,1);	// Chinese
				soundMap.put("4", soundSet4);
				
				int[]	soundSet5 = new int[3];
				soundSet5[0]	= soundPool.load(MainActivity.this, R.raw.five, 1);		// English
				soundSet5[1]	= soundPool.load(MainActivity.this, R.raw.clear,1);	// Swedish
				soundSet5[2]	= soundPool.load(MainActivity.this, R.raw.five_cn,1);	// Chinese
				soundMap.put("5", soundSet5);
				
				int[]	soundSet6 = new int[3];
				soundSet6[0]	= soundPool.load(MainActivity.this, R.raw.six, 1);		// English
				soundSet6[1]	= soundPool.load(MainActivity.this, R.raw.clear,1);	// Swedish
				soundSet6[2]	= soundPool.load(MainActivity.this, R.raw.six_cn,1);	// Chinese
				soundMap.put("6", soundSet6);
				
				int[]	soundSet7 = new int[3];
				soundSet7[0]	= soundPool.load(MainActivity.this, R.raw.seven, 1);		// English
				soundSet7[1]	= soundPool.load(MainActivity.this, R.raw.clear,1);	// Swedish
				soundSet7[2]	= soundPool.load(MainActivity.this, R.raw.seven_cn,1);	// Chinese
				soundMap.put("7", soundSet7);
				
				int[]	soundSet8 = new int[3];
				soundSet8[0]	= soundPool.load(MainActivity.this, R.raw.eight, 1);		// English
				soundSet8[1]	= soundPool.load(MainActivity.this, R.raw.clear,1);	// Swedish
				soundSet8[2]	= soundPool.load(MainActivity.this, R.raw.eight_cn,1);	// Chinese
				soundMap.put("8", soundSet8);
				
				int[]	soundSet9 = new int[3];
				soundSet9[0]	= soundPool.load(MainActivity.this, R.raw.nine, 1);		// English
				soundSet9[1]	= soundPool.load(MainActivity.this, R.raw.clear,1);	// Swedish
				soundSet9[2]	= soundPool.load(MainActivity.this, R.raw.nine_cn,1);	// Chinese
				soundMap.put("9", soundSet9);
				
				Log.i(TAG, "声音加载线程结束" + new Date());
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
				ExpressionCalculation();
				return;
			case R.id.button_back:
				ch = "D";
				// soundBackspace = GetSoundByChar(ch);
				break;
			default:
				ch = "#";
				break;
			}
			PlaySoundByChar(ch);

			if (ch.charAt(0) == '#') {
				return;
			}

			characterThis = Element.GetElementType(ch);
			if (characterThis == Element.CLEAR) {
				s = "0";
				editTextExpression.setText(s);
				return;
			} else if (ch == "D") {
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
			} else if (s.equals("0") && !ch.equals("0")) {
				// 09 -> 9
				s = ch;
			} else if (s.equals("0") && ch.equals("(")) {
				// 0( -> (
				s = ch;
			} else if (characterThis == Element.DOT
					&& characterLast == Element.DOT) {
				// ..
				InputError();
				return;
			} else if (characterThis == Element.OPERATOR
					&& characterLast == Element.OPERATOR) {
				// ++
				InputError();
				return;
			} else if (characterThis == Element.BRACKET_LEFT
					&& characterLast == Element.NUMBER) {
				// 1-9(
				InputError();
				return;
			} else if (characterThis == Element.BRACKET_RIGHT
					&& characterLast == Element.OPERATOR) {
				// +)
				InputError();
				return;
			} else if (characterThis == Element.BRACKET_LEFT
					&& characterLast == Element.BRACKET_RIGHT) {
				// )(
				InputError();
				return;
			} else if (characterThis == Element.BRACKET_RIGHT
					&& characterLast == Element.BRACKET_LEFT) {
				// ()
				InputError();
				return;
			} else if (characterThis == Element.BRACKET_RIGHT && s.equals("0")) {
				// 0)
				InputError();
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

	}

	private void PlaySoundByChar(String ch) {
		// TODO Auto-generated method stub
		int ret = -1;
		int sound;
		int soundType;	// None, English, Swedish, Chinese
		String soundSetup = PreferenceManager.getDefaultSharedPreferences(this)
				.getString("sound_setup_list_preference", "0");
		Log.i(TAG, String.format("soundSetup: %s", soundSetup));

		soundType	= Integer.parseInt(soundSetup);
		if( soundType < 0){
			return;
		}
		
		if (this.isSoundsLoaded) {
			if (this.soundThread != null) {
				this.soundThread.interrupt();
			}
			Log.i(TAG, String.format("ch = %s", ch));
			if (soundMap.containsKey(ch)) {
				sound = soundMap.get(ch)[soundType];
				if (sound != 0) {
					ret = this.soundPool.play(sound, 1.0F, 1.0F, 1, 0, 1.0F);
				}
			} else {
				Log.e(TAG, String.format("Cannot find audio [%s]", ch));
			}
		}
	}

	private int GetSoundByChar_to_be_delete(String s) {
		// TODO Auto-generated method stub
		int ret;
		if (s.length() < 1) {
			return 0;
		}
		char ch = s.charAt(0);
		switch (ch) {
		case 'D':
			ret = Integer.valueOf(this.soundBackspace);
			break;
		default:
			ret = -1;

		}
		return ret;
	}

	private void PlaySound(int sound) {
		// TODO Auto-generated method stub
		int ret = -1;
		String soundSetup = PreferenceManager.getDefaultSharedPreferences(this)
				.getString("sound_setup_list_preference", "0");
		Log.i(TAG, String.format("soundSetup: %s", soundSetup));

		if (this.isSoundsLoaded) {
			if (this.soundThread != null) {
				this.soundThread.interrupt();
			}
			if (soundSetup.equals("0")) {
				// no sound
				ret = 0;
			} else if (soundSetup.equals("1")) {
				ret = this.soundPool.play(soundTap, 1.0F, 1.0F, 1, 0, 1.0F);
			} else if (soundSetup.equals("2")) {
				// Swedish
				ret = this.soundPool.play(sound, 1.0F, 1.0F, 1, 0, 1.0F);
			}
		}
		if (ret == 0) {
			Log.i(TAG, "Play fail");
		}
	}

	private void InputError() {
		// TODO Auto-generated method stub

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 40, 50 }; // 停止 开启 停止 开启
		vibrator.vibrate(pattern, -1); // 重复两次上面的pattern

	}

	private void ExpressionCalculation() {
		// TODO Auto-generated method stub
		String expression = editTextExpression.getText().toString();
		Log.i(TAG, expression);

		EvaluateExpression evaluateExpression = new EvaluateExpression(
				expression);

		String s = evaluateExpression.CalculateExpression();
		String res = String.format("%s = %s", expression, s);
		editTextExpression.setText(res);
		Log.i(TAG, res);
	}

	public class SoundThread extends Thread {
		List<Integer> soundList;

		public SoundThread(LinkedList localObject) {
			this.soundList = localObject;
		}

		public void run() {
			for (int i = 0;; i++) {
				try {
					if (i >= this.soundList.size())
						return;
					Integer localInteger = (Integer) this.soundList.get(i);
					if (localInteger == null)
						continue;
					MainActivity.this.playResultSound(localInteger.intValue());
					if (i == 0)
						sleep(600L);
				} catch (InterruptedException localInterruptedException) {
					localInterruptedException.printStackTrace();
					return;
				}
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
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
		return false;
	}
}

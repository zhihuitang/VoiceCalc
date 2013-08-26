package com.tang.voicecalc;

import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Button number[] = new Button[10];
		Button symbol_c, symbol_plus, symbol_substract, symbol_multiply, symbol_divide, symbol_dot, symbol_minus, symbol_bracket_left, symbol_bracket_right, symbol_equal;
		//MyImageButton	symbol_back;

		setContentView(R.layout.main);

		number[0] = (Button) findViewById(R.id.button0);
		number[1] = (Button) findViewById(R.id.button1);
		number[2] = (Button) findViewById(R.id.button2);
		number[3] = (Button) findViewById(R.id.button3);
		number[4] = (Button) findViewById(R.id.button4);
		number[5] = (Button) findViewById(R.id.button5);
		number[6] = (Button) findViewById(R.id.button6);
		number[7] = (Button) findViewById(R.id.button7);
		number[8] = (Button) findViewById(R.id.button8);
		number[9] = (Button) findViewById(R.id.button9);

		symbol_c = (Button) findViewById(R.id.button_c);
		//symbol_back = (MyImageButton) findViewById(R.id.button_back);
		symbol_plus = (Button) findViewById(R.id.button_plus);
		symbol_substract = (Button) findViewById(R.id.button_substract);
		symbol_multiply = (Button) findViewById(R.id.button_multiply);
		symbol_divide = (Button) findViewById(R.id.button_divide);
		symbol_dot = (Button) findViewById(R.id.button_dot);
		symbol_minus = (Button) findViewById(R.id.button_minus);
		symbol_bracket_left = (Button) findViewById(R.id.button_bracket_left);
		symbol_bracket_right = (Button) findViewById(R.id.button_bracket_right);
		symbol_equal = (Button) findViewById(R.id.button_equal);

		editTextExpression = (EditText) findViewById(R.id.textView1);
		editTextExpression.setText(s);

		for (int i = 0; i < 10; i++) {
			number[i].setOnClickListener(this);
		}
		symbol_c.setOnClickListener(this);
		//symbol_back.setOnClickListener(this);
		symbol_plus.setOnClickListener(this);
		symbol_substract.setOnClickListener(this);
		symbol_multiply.setOnClickListener(this);
		symbol_divide.setOnClickListener(this);
		symbol_dot.setOnClickListener(this);
		symbol_minus.setOnClickListener(this);
		symbol_bracket_left.setOnClickListener(this);
		symbol_bracket_right.setOnClickListener(this);
		symbol_equal.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Button btn = (Button) v;
		// char ch[];
		// c = '0';
		String ch = "0";

		s = editTextExpression.getText().toString();

		try {
			switch (btn.getId()) {
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
				break;
			default:
				ch = "#";
				break;
			}

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
}

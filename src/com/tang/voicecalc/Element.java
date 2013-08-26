package com.tang.voicecalc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class Element {
	static String TAG = "Element";
	static int UNKNOWN = -1;
	static int NUMBER = 0;
	static int OPERATOR = 1;
	static int BRACKET_LEFT = 2;
	static int BRACKET_RIGHT = 3;
	static int CLEAR = 4;
	static int DOT = 5;

	public static int GetElementType(String element) {
		// TODO Auto-generated method stub
		if (IsNumber(element)) {
			return NUMBER;
		} else if (IsOperator(element)) {
			return OPERATOR;
		} else if (IsBracketLeft(element)) {
			return BRACKET_LEFT;
		} else if (IsBracketRight(element)) {
			return BRACKET_RIGHT;
		} else if (IsClear(element)) {
			return CLEAR;
		} else if (IsDot(element)) {
			return DOT;
		} else {
			return UNKNOWN;
		}
	}

	private static boolean IsDot(String s) {
		// TODO Auto-generated method stub
		boolean ret = false;

		if (s.equals(".")) {
			ret = true;
		} else {
			ret = false;
		}
		return ret;
	}

	private static boolean IsClear(String s) {
		// TODO Auto-generated method stub
		boolean ret = false;

		if (s.equals("C")) {
			ret = true;
		} else {
			ret = false;
		}
		return ret;
	}

	private static boolean IsNumber(String s) {
		// TODO Auto-generated method stub
		boolean ret = false;
		try {
			/*
			 * 是否只包含数字，及最多一个 +, .
			 */
			Pattern patternFloat = Pattern.compile("^-?[0-9]+(\\.?)[0-9]+");
			Pattern patternInt = Pattern.compile("^-?[0-9]+");

			Matcher matcherFloat = patternFloat.matcher(s);
			Matcher matcherInt = patternInt.matcher(s);
			if (matcherFloat.find()) {
//				Log.i(TAG, String.format("%s is a float", s));
				ret = true;
			} else if (matcherInt.find()) {
//				Log.i(TAG, String.format("%s is a int", s));
				ret = true;
			} else {
//				Log.i(TAG, String.format("%s not a number", s));
				ret = false;
			}
		} catch (NumberFormatException e) {
			ret = false;
		}
		return ret;
	}

	private static boolean IsOperator(String s) {
		// TODO Auto-generated method stub
		boolean ret = false;
		if (s.length() != 1) {
			// 如果长度不等于1，则肯定不是操作符
			ret = false;
		} else {
			char c = s.charAt(0);
			switch (c) {
			case '+':
			case '-':
			case '*':
			case '/':
			case '#':
				ret = true;
				break;
			default:
				ret = false;
				break;
			}
		}
		return ret;
	}

	private static boolean IsBracketLeft(String s) {
		// TODO Auto-generated method stub
		boolean ret = false;
		if (s.length() != 1) {
			// 如果长度不等于1，则肯定不是操(
			ret = false;
		} else {
			char c = s.charAt(0);
			switch (c) {
			case '(':
				ret = true;
				break;
			default:
				ret = false;
				break;
			}
		}
		return ret;
	}

	private static boolean IsBracketRight(String s) {
		// TODO Auto-generated method stub
		boolean ret = false;
		if (s.length() != 1) {
			// 如果长度不等于1，则肯定不是操)
			ret = false;
		} else {
			char c = s.charAt(0);
			switch (c) {
			case ')':
				ret = true;
				break;
			default:
				ret = false;
				break;
			}
		}
		return ret;
	}
}

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
		} else if (IsOperator(element) >= 0) {
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

	public static boolean IsPrior(String s1, String s2) {
		/*
		 *  column, s1
		 *  row,    s2
		 *  
		 * 		#   +   -   *   /  
		 *  #   1   0   0   0   0
		 *  +   1   1   1   0   0
		 *  -   1   1   1   0   0 
		 *  *   1   1   1   1   1
		 *  /   1   1   1   1   1
		 */

		if (GetElementType(s1) == OPERATOR && GetElementType(s2) == OPERATOR) {
			int[][] precedeTable = { //  +   -   *   /
			{ 1, 0, 0, 0, 0 }, // #
					{ 1, 1, 1, 0, 0 }, // +
					{ 1, 1, 1, 0, 0 }, //  -
					{ 1, 1, 1, 1, 1 }, // * 
					{ 1, 1, 1, 1, 1 } // /
			};
			int n1 = GetOperatorIndex(s1);
			int n2 = GetOperatorIndex(s2);

			return (precedeTable[n1][n2] == 1 ? true : false);
		} else {
			Log.e(TAG, String.format("[%s][%s] not operator", s1, s2));
			return false;
		}
	}

	private static int GetOperatorIndex(String s) {
		// TODO Auto-generated method stub
		String operator = "#+-*/";
		return operator.indexOf(s);
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

	// 返回数参见IsPrior的表
	private static int IsOperator(String s) {
		// TODO Auto-generated method stub
		int ret = -1;
		if (s.length() != 1) {
			// 如果长度不等于1，则肯定不是操作符
			ret = -1;
		} else {
			char c = s.charAt(0);
			switch (c) {
			case '#':
				ret = 0;
				break;
			case '+':
				ret = 1;
				break;
			case '-':
				ret = 2;
				break;
			case '*':
				ret = 3;
				break;
			case '/':
				ret = 4;
				break;
			default:
				ret = -1;
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

package com.tang.voicecalc;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class EvaluateExpression {
	String TAG = "EvaluateExpression";
	Stack<String> postfixStack = new Stack<String>();

	public EvaluateExpression(String s) {
		// TODO Auto-generated constructor stub
		String expression = s;
		String c;
		List<String> expressionList = new ArrayList<String>();
		Stack<String> operatorStack = new Stack<String>();
		String element;

		// 计算器可接受的字符
		String separator = "[\\+\\-\\*\\/()]";
		Stack<String> postfixStack_tmp = new Stack<String>();

		String[] elements = s.split(separator);
		try{
		int j = 0;
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].length() > 0) {
				expressionList.add(elements[i]);
			}
			for (j = j + elements[i].length(); j < expression.length(); j++) {
				c = expression.substring(j, j + 1);
				if (isNumber(c)) {
					break;
				} else {
					// 运算符
					expressionList.add(c);
				}
			}
		}

		operatorStack.add("#");
		for (int i = 0; i < expressionList.size(); i++) {
			// 生成逆波兰表达式
//			Log.i(TAG, expressionList.get(i).toString());
			element = expressionList.get(i);
			if (Element.GetElementType(element) == Element.NUMBER) {
				// 如果是操作数，则入postfixStack栈
				postfixStack_tmp.push(element);
			} else if (Element.GetElementType(element) == Element.OPERATOR) {
				// 运算符，比较操作符栈顶的优先级
				for (; isOperator(operatorStack.peek());) {
					if (isGreaterThanPreviousOne(element, operatorStack.peek())) {
						operatorStack.push(element);
						break;
					} else {
						postfixStack_tmp.push(operatorStack.pop());
					}
				}
				if (Element.GetElementType(operatorStack.peek()) == Element.BRACKET_LEFT) {
					// 栈顶是左括号 (， 运算符直接压栈
					operatorStack.push(element);
				}
			} else if (Element.GetElementType(element) == Element.BRACKET_LEFT) {
				// ( 左括号，直接运算符压栈
				operatorStack.push(element);
			} else if (Element.GetElementType(element) == Element.BRACKET_RIGHT) {
				// )
				// 右括号，将运算符栈operatorStack顶的第一个左括号(上面的运算符全部弹出到postfixStack_tmp，再次丢弃左括号(
				for (; !operatorStack.isEmpty();) {
					if (operatorStack.peek().equals("(")) {
						operatorStack.pop();
						break;
					} else {
						postfixStack_tmp.push(operatorStack.pop());
					}
				}
			}
		}
		for (; !operatorStack.isEmpty();) {
			postfixStack_tmp.push(operatorStack.pop());
		}
		postfixStack.clear();
		for (; !postfixStack_tmp.isEmpty();) {
			// 得到逆波兰表达式
			String ss = postfixStack_tmp.pop();
			postfixStack.push(ss);
//			Log.i(TAG, ss);
		}
		}catch(Exception e){
//			e.printStackTrace();
			Log.e(TAG, "expression ERROR");
		}
	}

	private void GetElementType(String element2) {
		// TODO Auto-generated method stub

	}

	public String CalculateExpression() {
		Stack<String> numberStack = new Stack<String>();
		String s1, s2, s3;
		s3 = "ERROR";
		try {
			for (; !postfixStack.isEmpty();) {
				// 得到逆波兰表达式
				String ss = postfixStack.pop();
				if (isNumber(ss)) {
					numberStack.push(ss);
				} else if (isPoundSign(ss)) {
					// 逆波兰表达式栈为空，结束循环
					s3 = numberStack.pop();
					break;
				} else if (isOperator(ss)) {
					s2 = numberStack.pop();
					s1 = numberStack.pop();
					ss = Calculate(s1, s2, ss);
					numberStack.push(ss);
				}
			}
		} catch (Exception e) {
//			e.printStackTrace();
			Log.e(TAG, "expression ERROR");
		}
		return s3;
	}

	private boolean isPoundSign(String ss) {
		// TODO Auto-generated method stub

		if (ss.length() != 1) {
			return false;
		}

		char c = ss.charAt(0);

		if (c == '#') {
			return true;
		} else {
			return false;
		}
	}

	private String Calculate(String s1, String s2, String ss) {
		// TODO Auto-generated method stub
		float f1, f2, f3;
		char operator;

		operator = ss.charAt(0);

		f1 = Float.valueOf(s1);
		f2 = Float.valueOf(s2);

		switch (operator) {
		case '+':
			f3 = f1 + f2;
			break;
		case '-':
			f3 = f1 - f2;
			break;
		case '*':
			f3 = f1 * f2;
			break;
		case '/':
			f3 = f1 / f2;
			break;
		default:
			f3 = 0;
			Log.e(TAG, "Operator ERROR");
			break;

		}
		return String.valueOf(f3);
	}

	/*
	 * true: 是一个操作符，例如 + , _ *, / false 不是一个操作符，可能是一个数字
	 */
	private boolean isOperator(String s) {
		// TODO Auto-generated method stub
		boolean ret = false;
		if (s.length() != 1) {
			// 如果长度不等于1，则肯定不是操作符
			ret = false;
			return ret;
		}

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
		return ret;
	}

	/*
	 * 如果 e1 > e2, 则返回 true 如果 e1 < e2, 则返回 false
	 */
	private boolean isGreaterThanPreviousOne(String e1, String e2) {
		// TODO Auto-generated method stub
		String priority = "(+-*/";
		if (e1.length() != 1 || e2.length() != 1) {
			Log.e(TAG, "[isGreaterThanPreviousOne] parameter error");
			return false;
		}
		int n1 = priority.indexOf(e1);
		int n2 = priority.indexOf(e2);

		if (n1 > n2) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isNumber(String s) {
		// TODO Auto-generated method stub
		try {
			/*
			 * 是否只包含数字，及最多一个 +, .
			 */
			Pattern patternFloat = Pattern.compile("^-?[0-9]+(\\.?)[0-9]+");
			Pattern patternInt = Pattern.compile("^-?[0-9]+");

			Matcher matcherFloat = patternFloat.matcher(s);
			Matcher matcherInt = patternInt.matcher(s);
			if (matcherFloat.find()) {
				// Log.i(TAG, String.format("%s is a float", s));
				return true;
			} else if (matcherInt.find()) {
				// Log.i(TAG, String.format("%s is a int", s));
				return true;
			} else {
				// Log.i(TAG, String.format("%s not a number", s));
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}

}

package com.tang.voicecalc;

import java.math.BigDecimal;
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
		String separator = "[\\+\\-\\/\\*()]";
		Stack<String> postfixStack_tmp = new Stack<String>();

		String[] elements = s.split(separator);
		try {
			int j = 0;
			for (int i = 0; i < elements.length; i++) {
				if (elements[i].length() > 0) {
					expressionList.add(elements[i]);
				}
				for (j = j + elements[i].length(); j < expression.length(); j++) {
					c = expression.substring(j, j + 1);
					if (Element.GetElementType(c) == Element.NUMBER) {
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
				// Log.i(TAG, expressionList.get(i).toString());
				element = expressionList.get(i);
				if (Element.GetElementType(element) == Element.NUMBER) {
					// 如果是操作数，则入postfixStack栈
					postfixStack_tmp.push(element);
				} else if (Element.GetElementType(element) == Element.OPERATOR) {
					// 运算符，比较操作符栈顶的优先级
					for (; Element.GetElementType(operatorStack.peek()) == Element.OPERATOR;) {
						//						if (isGreaterThanPreviousOne(element,
						//								operatorStack.peek())) 
						if( Element.IsPrior(operatorStack.peek(), element) == false)
						{
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
				//Log.i(TAG, ss);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			Log.e(TAG, "expression ERROR");
		}
	}

	public String CalculateExpression() {
		Stack<String> numberStack = new Stack<String>();
		String s1, s2, s3;
		s3 = "ERROR";
		try {
			for (; !postfixStack.isEmpty();) {
				// 得到逆波兰表达式
				String ss = postfixStack.pop();
				if (Element.GetElementType(ss) == Element.NUMBER) {
					numberStack.push(ss);
				} else if (isPoundSign(ss)) {
					// 逆波兰表达式栈为空，结束循环
					s3 = numberStack.pop();
					break;
				} else if ( Element.GetElementType(ss) == Element.OPERATOR) {
					s2 = numberStack.pop();
					s1 = numberStack.pop();
					ss = Calculate(s1, s2, ss);
					numberStack.push(ss);
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
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
		char operator;
		String res = "";
		BigDecimal bd1, bd2;

		try {
			operator = ss.charAt(0);
			bd1 = new BigDecimal(s1);
			bd2 = new BigDecimal(s2);

			switch (operator) {
			case '+':
				res = bd1.add(bd2).toString();
				break;
			case '-':
				res = bd1.subtract(bd2).toString();
				break;
			case '*':
				res = bd1.multiply(bd2).toString();
				break;
			case '/':
				// res = bd1.divide(bd2).toString();
				BigDecimal bd0 = new BigDecimal(0);
				if (bd2.compareTo(bd0) == 0) {
					res = "∞";
				} else {
					try {
						res = bd1.divide(bd2).toString();
					} catch (ArithmeticException e) {
						//e.printStackTrace();
						res = bd1.divide(bd2, 6, BigDecimal.ROUND_UP)
								.toString();
					}
				}
				break;
			default:
				res = "0";
				Log.e(TAG, "Operator ERROR");
				break;

			}
		} catch (Exception e) {
			Log.e(TAG, String.format("[%s][%s][%s], BigDecimal Exception", s1, ss, s2));
			e.printStackTrace();
		}
		return res;
	}

	/*
	 * true: 是一个操作符，例如 + , _ *, / false 不是一个操作符，可能是一个数字
	 */
	private boolean isOperator__(String s) {
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
}

package com.spring.cloud.base.captcha;

import com.spring.cloud.base.utils.crypto.NumberUtil;
import com.spring.cloud.base.utils.str.StrUtil;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Stack;

/**
 * @Author: ls
 * @Description: 数学表达式计算工具类
 * @Date: 2023/4/17 15:00
 */
public class Calculator {

	private final Stack<String> postfixStack = new Stack<>();

	private final int[] operatPriority = new int[]{0, 3, 2, 1, -1, 1, 0, 2};

	/**
	 * 计算表达式的值
	 *
	 * @param expression 表达式
	 * @return 计算结果
	 */
	public static double conversion(String expression) {
		return (new Calculator()).calculate(expression);
	}

	/**
	 * 按照给定的表达式计算
	 *
	 * @param expression 要计算的表达式例如:5+12*(3+5)/7
	 * @return 计算结果
	 */
	public double calculate(String expression) {
		prepare(transform(expression));

		final Stack<String> resultStack = new Stack<>();
		Collections.reverse(postfixStack);
		String firstValue, secondValue, currentOp;
		while (!postfixStack.isEmpty()) {
			currentOp = postfixStack.pop();
			if (!isOperator(currentOp.charAt(0))) {
				currentOp = currentOp.replace("~", "-");
				resultStack.push(currentOp);
			} else {
				secondValue = resultStack.pop();
				firstValue = resultStack.pop();
				firstValue = firstValue.replace("~", "-");
				secondValue = secondValue.replace("~", "-");
				final BigDecimal tempResult = calculate(firstValue, secondValue, currentOp.charAt(0));
				resultStack.push(tempResult.toString());
			}
		}
		return NumberUtil.mul(resultStack.toArray(new String[0])).doubleValue();
	}

	/**
	 * 数据准备阶段将表达式转换成为后缀式栈
	 *
	 * @param expression 表达式
	 */
	private void prepare(String expression) {
		final Stack<Character> opStack = new Stack<>();
		opStack.push(',');
		final char[] arr = expression.toCharArray();
		int currentIndex = 0;
		int count = 0;
		char currentOp, peekOp;
		for (int i = 0; i < arr.length; i++) {
			currentOp = arr[i];
			if (isOperator(currentOp)) {
				if (count > 0) {
					postfixStack.push(new String(arr, currentIndex, count));
				}
				peekOp = opStack.peek();
				if (currentOp == ')') {
					while (opStack.peek() != '(') {
						postfixStack.push(String.valueOf(opStack.pop()));
					}
					opStack.pop();
				} else {
					while (currentOp != '(' && peekOp != ',' && compare(currentOp, peekOp)) {
						postfixStack.push(String.valueOf(opStack.pop()));
						peekOp = opStack.peek();
					}
					opStack.push(currentOp);
				}
				count = 0;
				currentIndex = i + 1;
			} else {
				count++;
			}
		}
		if (count > 1 || (count == 1 && !isOperator(arr[currentIndex]))) {
			postfixStack.push(new String(arr, currentIndex, count));
		}

		while (opStack.peek() != ',') {
			postfixStack.push(String.valueOf(opStack.pop()));
		}
	}

	/**
	 * 判断是否为算术符号
	 *
	 * @param c 字符
	 * @return 是否为算术符号
	 */
	private boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')' || c == '%';
	}

	/**
	 * 利用ASCII码-40做下标去算术符号优先级
	 *
	 * @param cur  下标
	 * @param peek peek
	 * @return 优先级，如果cur高或相等，返回true，否则false
	 */
	private boolean compare(char cur, char peek) {
		final int offset = 40;
		if (cur == '%') {
			
			cur = 47;
		}
		if (peek == '%') {
			
			peek = 47;
		}

		return operatPriority[peek - offset] >= operatPriority[cur - offset];
	}

	/**
	 * 按照给定的算术运算符做计算
	 *
	 * @param firstValue  第一个值
	 * @param secondValue 第二个值
	 * @param currentOp   算数符，只支持'+'、'-'、'*'、'/'、'%'
	 * @return 结果
	 */
	private BigDecimal calculate(String firstValue, String secondValue, char currentOp) {
		final BigDecimal result;
		switch (currentOp) {
			case '+':
				result = NumberUtil.add(firstValue, secondValue);
				break;
			case '-':
				result = NumberUtil.sub(firstValue, secondValue);
				break;
			case '*':
				result = NumberUtil.mul(firstValue, secondValue);
				break;
			case '/':
				result = NumberUtil.div(firstValue, secondValue);
				break;
			case '%':
				result = NumberUtil.toBigDecimal(firstValue).remainder(NumberUtil.toBigDecimal(secondValue));
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + currentOp);
		}
		return result;
	}

	/**
	 * 将表达式中负数的符号更改
	 *
	 * @param expression 例如-2+-1*(-3E-2)-(-1) 被转为 ~2+~1*(~3E~2)-(~1)
	 * @return 转换后的字符串
	 */
	private static String transform(String expression) {
		expression = StrUtil.cleanBlank(expression);
		expression = StrUtil.removeSuffix(expression, "=");
		final char[] arr = expression.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == '-') {
				if (i == 0) {
					arr[i] = '~';
				} else {
					char c = arr[i - 1];
					if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == 'E' || c == 'e') {
						arr[i] = '~';
					}
				}
			}
		}
		if (arr[0] == '~' && (arr.length > 1 && arr[1] == '(')) {
			arr[0] = '-';
			return "0" + new String(arr);
		} else {
			return new String(arr);
		}
	}
}

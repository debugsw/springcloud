package com.spring.cloud.base.jwt.json;

import com.spring.cloud.base.utils.utils.CharUtil;
import com.spring.cloud.base.utils.str.StrUtil;
/**
 * @Author: ls
 * @Description: JSON字符串格式化工具
 * @Date: 2023/4/25 13:36
 */
public class JSONStrFormatter {

	/**
	 * 单位缩进字符串。
	 */
	private static final String SPACE = "    ";
	/**
	 * 换行符
	 */
	private static final char NEW_LINE = StrUtil.C_LF;

	/**
	 * 返回格式化JSON字符串。
	 *
	 * @param json 未格式化的JSON字符串。
	 * @return 格式化的JSON字符串。
	 */
	public static String format(String json) {
		final StringBuilder result = new StringBuilder();
		Character wrapChar = null;
		boolean isEscapeMode = false;
		int length = json.length();
		int number = 0;
		char key;
		for (int i = 0; i < length; i++) {
			key = json.charAt(i);
			if (CharUtil.DOUBLE_QUOTES == key || CharUtil.SINGLE_QUOTE == key) {
				if (null == wrapChar) {
					wrapChar = key;
				} else if (isEscapeMode) {
					isEscapeMode = false;
				} else if (wrapChar.equals(key)) {
					wrapChar = null;
				}
				if ((i > 1) && (json.charAt(i - 1) == CharUtil.COLON)) {
					result.append(CharUtil.SPACE);
				}
				result.append(key);
				continue;
			}
			if (CharUtil.BACKSLASH == key) {
				if (null != wrapChar) {
					isEscapeMode = !isEscapeMode;
					result.append(key);
					continue;
				} else {
					result.append(key);
				}
			}
			if (null != wrapChar) {
				result.append(key);
				continue;
			}
			if ((key == CharUtil.BRACKET_START) || (key == CharUtil.DELIM_START)) {
				if ((i > 1) && (json.charAt(i - 1) == CharUtil.COLON)) {
					result.append(NEW_LINE);
					result.append(indent(number));
				}
				result.append(key);
				result.append(NEW_LINE);
				number++;
				result.append(indent(number));
				continue;
			}
			if ((key == CharUtil.BRACKET_END) || (key == CharUtil.DELIM_END)) {
				result.append(NEW_LINE);
				number--;
				result.append(indent(number));
				result.append(key);
				continue;
			}
			if ((key == ',')) {
				result.append(key);
				result.append(NEW_LINE);
				result.append(indent(number));
				continue;
			}
			if ((i > 1) && (json.charAt(i - 1) == CharUtil.COLON)) {
				result.append(CharUtil.SPACE);
			}
			result.append(key);
		}
		return result.toString();
	}

	/**
	 * 返回指定次数的缩进字符串。每一次缩进4个空格，即SPACE。
	 *
	 * @param number 缩进次数。
	 * @return 指定缩进次数的字符串。
	 */
	private static String indent(int number) {
		return StrUtil.repeat(SPACE, number);
	}
}

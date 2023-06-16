package com.spring.cloud.base.utils.str;

import com.spring.cloud.base.utils.utils.ArrayUtil;

import java.util.Map;

/**
 * @Author: ls
 * @Description: 字符串格式化工具
 * @Date: 2023/4/13 16:11
 */
public class StrFormatter {

	public static String format(String strPattern, Object... argArray) {
		return formatWith(strPattern, StrUtil.EMPTY_JSON, argArray);
	}

	public static String formatWith(String strPattern, String placeHolder, Object... argArray) {
		if (StrUtil.isBlank(strPattern) || StrUtil.isBlank(placeHolder) || ArrayUtil.isEmpty(argArray)) {
			return strPattern;
		}
		final int strPatternLength = strPattern.length();
		final int placeHolderLength = placeHolder.length();

		// 初始化定义好的长度以获得更好的性能
		final StringBuilder sbuf = new StringBuilder(strPatternLength + 50);

		int handledPosition = 0;
		// 记录已经处理到的位置
		int delimIndex;
		// 占位符所在位置
		for (int argIndex = 0; argIndex < argArray.length; argIndex++) {
			delimIndex = strPattern.indexOf(placeHolder, handledPosition);
			if (delimIndex == -1) {
				// 剩余部分无占位符
				if (handledPosition == 0) {
					// 不带占位符的模板直接返回
					return strPattern;
				}
				// 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
				sbuf.append(strPattern, handledPosition, strPatternLength);
				return sbuf.toString();
			}

			// 转义符
			if (delimIndex > 0 && strPattern.charAt(delimIndex - 1) == StrUtil.C_BACKSLASH) {
				// 转义符
				if (delimIndex > 1 && strPattern.charAt(delimIndex - 2) == StrUtil.C_BACKSLASH) {
					// 双转义符
					// 转义符之前还有一个转义符，占位符依旧有效
					sbuf.append(strPattern, handledPosition, delimIndex - 1);
					sbuf.append(StrUtil.utf8Str(argArray[argIndex]));
					handledPosition = delimIndex + placeHolderLength;
				} else {
					// 占位符被转义
					argIndex--;
					sbuf.append(strPattern, handledPosition, delimIndex - 1);
					sbuf.append(placeHolder.charAt(0));
					handledPosition = delimIndex + 1;
				}
			} else {
				// 正常占位符
				sbuf.append(strPattern, handledPosition, delimIndex);
				sbuf.append(StrUtil.utf8Str(argArray[argIndex]));
				handledPosition = delimIndex + placeHolderLength;
			}
		}
		// 加入最后一个占位符后所有的字符
		sbuf.append(strPattern, handledPosition, strPatternLength);
		return sbuf.toString();
	}

	public static String format(CharSequence template, Map<?, ?> map, boolean ignoreNull) {
		if (null == template) {
			return null;
		}
		if (null == map || map.isEmpty()) {
			return template.toString();
		}
		String template2 = template.toString();
		String value;
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			value = StrUtil.utf8Str(entry.getValue());
			if (null == value && ignoreNull) {
				continue;
			}
			template2 = StrUtil.replace(template2, "{" + entry.getKey() + "}", value);
		}
		return template2;
	}
}

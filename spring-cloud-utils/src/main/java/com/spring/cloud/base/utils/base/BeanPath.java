package com.spring.cloud.base.utils.base;

import com.spring.cloud.base.utils.utils.ArrayUtil;
import com.spring.cloud.base.utils.utils.CharUtil;
import com.spring.cloud.base.utils.utils.CollUtil;
import com.spring.cloud.base.utils.Convert;
import com.spring.cloud.base.utils.bean.BeanUtil;
import com.spring.cloud.base.utils.crypto.NumberUtil;
import com.spring.cloud.base.utils.list.ListUtil;
import com.spring.cloud.base.utils.map.MapUtil;
import com.spring.cloud.base.utils.str.StrUtil;

import java.io.Serializable;
import java.util.*;

/**
 * @Author: ls
 * @Description: Bean路径表达式
 * @Date: 2023/4/13 16:11
 */
public class BeanPath implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 表达式边界符号数组
	 */
	private static final char[] EXP_CHARS = {CharUtil.DOT, CharUtil.BRACKET_START, CharUtil.BRACKET_END};

	private boolean isStartWith = false;
	protected List<String> patternParts;

	/**
	 * 解析Bean路径表达式为Bean模式
	 *
	 * @param expression 表达式
	 * @return BeanPath
	 */
	public static BeanPath create(final String expression) {
		return new BeanPath(expression);
	}

	/**
	 * 构造
	 *
	 * @param expression 表达式
	 */
	public BeanPath(final String expression) {
		init(expression);
	}

	/**
	 * 获取表达式解析后的分段列表
	 *
	 * @return 表达式分段列表
	 */
	public List<String> getPatternParts() {
		return this.patternParts;
	}

	/**
	 * 获取Bean中对应表达式的值
	 *
	 * @param bean Bean对象或Map或List等
	 * @return 值，如果对应值不存在，则返回null
	 */
	public Object get(final Object bean) {
		return get(this.patternParts, bean, false);
	}

	/**
	 * 设置表达式指定位置（或filed对应）的值
	 *
	 * @param bean  Bean、Map或List
	 * @param value 值
	 */
	public void set(final Object bean, final Object value) {
		set(bean, this.patternParts, lastIsNumber(this.patternParts), value);
	}

	@Override
	public String toString() {
		return this.patternParts.toString();
	}

	/**
	 * 设置表达式指定位置（或filed对应）的值
	 *
	 * @param bean           Bean、Map或List
	 * @param patternParts   表达式块列表
	 * @param nextNumberPart 下一个值是否
	 * @param value          值
	 */
	private void set(Object bean, List<String> patternParts, boolean nextNumberPart, Object value) {
		Object subBean = this.get(patternParts, bean, true);
		if (null == subBean) {
			final List<String> parentParts = getParentParts(patternParts);
			this.set(bean, parentParts, lastIsNumber(parentParts), nextNumberPart ? new ArrayList<>() : new HashMap<>(16));
			subBean = this.get(patternParts, bean, true);
		}
		final Object newSubBean = BeanUtil.setFieldValue(subBean, patternParts.get(patternParts.size() - 1), value);
		if (newSubBean != subBean) {
			this.set(bean, getParentParts(patternParts), nextNumberPart, newSubBean);
		}
	}

	/**
	 * 判断path列表中末尾的标记是否为数字
	 *
	 * @param patternParts path列表
	 * @return 是否为数字
	 */
	private static boolean lastIsNumber(List<String> patternParts) {
		return NumberUtil.isInteger(patternParts.get(patternParts.size() - 1));
	}

	/**
	 * 获取父级路径列表
	 *
	 * @param patternParts 路径列表
	 * @return 父级路径列表
	 */
	private static List<String> getParentParts(List<String> patternParts) {
		return patternParts.subList(0, patternParts.size() - 1);
	}

	/**
	 * 获取Bean中对应表达式的值
	 *
	 * @param patternParts 表达式分段列表
	 * @param bean         Bean对象或Map或List等
	 * @param ignoreLast   是否忽略最后一个值，忽略最后一个值则用于set，否则用于read
	 * @return 值，如果对应值不存在，则返回null
	 */
	private Object get(final List<String> patternParts, final Object bean, final boolean ignoreLast) {
		int length = patternParts.size();
		if (ignoreLast) {
			length--;
		}
		Object subBean = bean;
		boolean isFirst = true;
		String patternPart;
		for (int i = 0; i < length; i++) {
			patternPart = patternParts.get(i);
			subBean = getFieldValue(subBean, patternPart);
			if (null == subBean) {
				if (isFirst && !this.isStartWith && BeanUtil.isMatchName(bean, patternPart, true)) {
					subBean = bean;
					isFirst = false;
				} else {
					return null;
				}
			}
		}
		return subBean;
	}

	@SuppressWarnings("unchecked")
	private static Object getFieldValue(final Object bean, final String expression) {
		if (StrUtil.isBlank(expression)) {
			return null;
		}
		if (StrUtil.contains(expression, ':')) {
			final List<String> parts = StrUtil.splitTrim(expression, ':');
			final int start = Integer.parseInt(parts.get(0));
			final int end = Integer.parseInt(parts.get(1));
			int step = 1;
			if (3 == parts.size()) {
				step = Integer.parseInt(parts.get(2));
			}
			if (bean instanceof Collection) {
				return CollUtil.sub((Collection<?>) bean, start, end, step);
			} else if (ArrayUtil.isArray(bean)) {
				return ArrayUtil.sub(bean, start, end, step);
			}
		} else if (StrUtil.contains(expression, ',')) {
			final List<String> keys = StrUtil.splitTrim(expression, ',');
			if (bean instanceof Collection) {
				return CollUtil.getAny((Collection<?>) bean, Convert.convert(int[].class, keys));
			} else if (ArrayUtil.isArray(bean)) {
				return ArrayUtil.getAny(bean, Convert.convert(int[].class, keys));
			} else {
				final String[] unWrappedKeys = new String[keys.size()];
				for (int i = 0; i < unWrappedKeys.length; i++) {
					unWrappedKeys[i] = StrUtil.unWrap(keys.get(i), '\'');
				}
				if (bean instanceof Map) {
					return MapUtil.getAny((Map<String, ?>) bean, unWrappedKeys);
				} else {
					final Map<String, Object> map = BeanUtil.beanToMap(bean);
					return MapUtil.getAny(map, unWrappedKeys);
				}
			}
		} else {
			return BeanUtil.getFieldValue(bean, expression);
		}
		return null;
	}

	/**
	 * 初始化
	 *
	 * @param expression 表达式
	 */
	private void init(final String expression) {
		final List<String> localPatternParts = new ArrayList<>();
		final int length = expression.length();
		final StringBuilder builder = new StringBuilder();
		char c;
		boolean isNumStart = false;
		boolean isInWrap = false;
		for (int i = 0; i < length; i++) {
			c = expression.charAt(i);
			if (0 == i && '$' == c) {
				isStartWith = true;
				continue;
			}
			if ('\'' == c) {
				isInWrap = (!isInWrap);
				continue;
			}
			if (!isInWrap && ArrayUtil.contains(EXP_CHARS, c)) {
				if (CharUtil.BRACKET_END == c) {
					if (!isNumStart) {
						throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find ']' but no '[' !", expression, i));
					}
					isNumStart = false;
				} else {
					if (isNumStart) {
						throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find '[' but no ']' !", expression, i));
					} else if (CharUtil.BRACKET_START == c) {
						isNumStart = true;
					}
				}
				if (builder.length() > 0) {
					localPatternParts.add(builder.toString());
				}
				builder.setLength(0);
			} else {
				builder.append(c);
			}
		}
		if (isNumStart) {
			throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find '[' but no ']' !", expression, length - 1));
		} else {
			if (builder.length() > 0) {
				localPatternParts.add(builder.toString());
			}
		}
		this.patternParts = ListUtil.unmodifiable(localPatternParts);
	}
}

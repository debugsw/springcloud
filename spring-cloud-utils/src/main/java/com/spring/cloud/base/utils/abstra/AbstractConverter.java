package com.spring.cloud.base.utils.abstra;

import com.spring.cloud.base.utils.utils.ArrayUtil;
import com.spring.cloud.base.utils.utils.CharUtil;
import com.spring.cloud.base.utils.interf.Converter;
import com.spring.cloud.base.utils.map.ClassUtil;
import com.spring.cloud.base.utils.str.StrUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: ls
 * @Description: 抽象转换器
 * @Date: 2023/4/13 16:11
 */
public abstract class AbstractConverter<T> implements Converter<T>, Serializable {

	private static final long serialVersionUID = -6718419049400037562L;

	/**
	 * 不抛异常转换<br>
	 * 当转换失败时返回默认值
	 *
	 * @param value        被转换的值
	 * @param defaultValue 默认值
	 * @return 转换后的值
	 */
	public T convertQuietly(Object value, T defaultValue) {
		try {
			return convert(value, defaultValue);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public T convert(Object value, T defaultValue) {
		Class<T> targetType = getTargetType();
		if (null == targetType && null == defaultValue) {
			throw new NullPointerException(StrUtil.format("[type] and [defaultValue] are both null for Converter [{}], we can not know what type to convert !", this.getClass().getName()));
		}
		if (null == targetType) {
			// 目标类型不确定时使用默认值的类型
			targetType = (Class<T>) defaultValue.getClass();
		}
		if (null == value) {
			return defaultValue;
		}

		if (null == defaultValue || targetType.isInstance(defaultValue)) {
			if (targetType.isInstance(value) && false == Map.class.isAssignableFrom(targetType)) {
				// 除Map外，已经是目标类型，不需要转换（Map类型涉及参数类型，需要单独转换）
				return targetType.cast(value);
			}
			final T result = convertInternal(value);
			return ((null == result) ? defaultValue : result);
		} else {
			throw new IllegalArgumentException(
					StrUtil.format("Default value [{}]({}) is not the instance of [{}]", defaultValue, defaultValue.getClass(), targetType));
		}
	}

	/**
	 * 内部转换器，被 {@link AbstractConverter#convert(Object, Object)} 调用，实现基本转换逻辑<br>
	 * 内部转换器转换后如果转换失败可以做如下操作，处理结果都为返回默认值：
	 *
	 * <pre>
	 * 1、返回{@code null}
	 * 2、抛出一个{@link RuntimeException}异常
	 * </pre>
	 *
	 * @param value 值
	 * @return 转换后的类型
	 */
	public abstract T convertInternal(Object value);

	/**
	 * 值转为String，用于内部转换中需要使用String中转的情况<br>
	 * 转换规则为：
	 *
	 * <pre>
	 * 1、字符串类型将被强转
	 * 2、数组将被转换为逗号分隔的字符串
	 * 3、其它类型将调用默认的toString()方法
	 * </pre>
	 *
	 * @param value 值
	 * @return String
	 */
	public String convertToStr(Object value) {
		if (null == value) {
			return null;
		}
		if (value instanceof CharSequence) {
			return value.toString();
		} else if (ArrayUtil.isArray(value)) {
			return ArrayUtil.toString(value);
		} else if (CharUtil.isChar(value)) {
			//对于ASCII字符使用缓存加速转换，减少空间创建
			return CharUtil.toString((char) value);
		}
		return value.toString();
	}

	/**
	 * 获得此类实现类的泛型类型
	 *
	 * @return 此类的泛型类型，可能为{@code null}
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getTargetType() {
		return (Class<T>) ClassUtil.getTypeArgument(getClass());
	}
}

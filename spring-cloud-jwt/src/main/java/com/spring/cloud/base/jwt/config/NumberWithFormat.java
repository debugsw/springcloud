package com.spring.cloud.base.jwt.config;

import com.spring.cloud.base.utils.Convert;
import com.spring.cloud.base.utils.interf.TypeConverter;

import java.lang.reflect.Type;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * @Author: ls
 * @Description: 包含格式的数字转换器
 * @Date: 2023/4/25 13:36
 */
public class NumberWithFormat extends Number implements TypeConverter {
	private static final long serialVersionUID = 1L;
	private final Number number;
	private final String format;

	/**
	 * 构造
	 * @param number 数字
	 * @param format 格式
	 */
	public NumberWithFormat(final Number number, final String format) {
		this.number = number;
		this.format = format;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object convert(Type targetType, Object value) {

		if (null != this.format && targetType instanceof Class) {
			final Class<?> clazz = (Class<?>) targetType;
			if (Date.class.isAssignableFrom(clazz)) {
				return new DateConverter((Class<? extends Date>) clazz, format).convert(this.number, null);
			} else if (TemporalAccessor.class.isAssignableFrom(clazz)) {
				return new TemporalAccessorConverter(clazz, format).convert(this.number, null);
			} else if(String.class == clazz){
				return toString();
			}
		}
		return Convert.convertWithCheck(targetType, this.number, null, false);
	}

	@Override
	public int intValue() {
		return this.number.intValue();
	}

	@Override
	public long longValue() {
		return this.number.longValue();
	}

	@Override
	public float floatValue() {
		return this.number.floatValue();
	}

	@Override
	public double doubleValue() {
		return this.number.doubleValue();
	}

	@Override
	public String toString() {
		return this.number.toString();
	}
}

package com.spring.cloud.base.utils.map;

import com.spring.cloud.base.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @Author: ls
 * @Description: 基本类型的getter接口抽象实现
 * @Date: 2023/4/13 16:11
 */
public interface OptNullBasicTypeFromObjectGetter<K> extends OptNullBasicTypeGetter<K> {
	@Override
	default String getStr(K key, String defaultValue) {
		final Object obj = getObj(key);
		if (null == obj) {
			return defaultValue;
		}
		return Convert.toStr(obj, defaultValue);
	}

	@Override
	default Integer getInt(K key, Integer defaultValue) {
		final Object obj = getObj(key);
		if (null == obj) {
			return defaultValue;
		}
		return Convert.toInt(obj, defaultValue);
	}

	@Override
	default Short getShort(K key, Short defaultValue) {
		final Object obj = getObj(key);
		if (null == obj) {
			return defaultValue;
		}
		return Convert.toShort(obj, defaultValue);
	}

	@Override
	default Boolean getBool(K key, Boolean defaultValue) {
		final Object obj = getObj(key);
		if (null == obj) {
			return defaultValue;
		}
		return Convert.toBool(obj, defaultValue);
	}

	@Override
	default Long getLong(K key, Long defaultValue) {
		final Object obj = getObj(key);
		if (null == obj) {
			return defaultValue;
		}
		return Convert.toLong(obj, defaultValue);
	}

	@Override
	default Character getChar(K key, Character defaultValue) {
		final Object obj = getObj(key);
		if (null == obj) {
			return defaultValue;
		}
		return Convert.toChar(obj, defaultValue);
	}

	@Override
	default Float getFloat(K key, Float defaultValue) {
		final Object obj = getObj(key);
		if (null == obj) {
			return defaultValue;
		}
		return Convert.toFloat(obj, defaultValue);
	}

	@Override
	default Double getDouble(K key, Double defaultValue) {
		final Object obj = getObj(key);
		if (null == obj) {
			return defaultValue;
		}
		return Convert.toDouble(obj, defaultValue);
	}

	@Override
	default Byte getByte(K key, Byte defaultValue) {
		final Object obj = getObj(key);
		if (null == obj) {
			return defaultValue;
		}
		return Convert.toByte(obj, defaultValue);
	}

	@Override
	default BigDecimal getBigDecimal(K key, BigDecimal defaultValue) {
		final Object obj = getObj(key);
		if (null == obj) {
			return defaultValue;
		}
		return Convert.toBigDecimal(obj, defaultValue);
	}

	@Override
	default BigInteger getBigInteger(K key, BigInteger defaultValue) {
		final Object obj = getObj(key);
		if (null == obj) {
			return defaultValue;
		}
		return Convert.toBigInteger(obj, defaultValue);
	}

	@Override
	default <E extends Enum<E>> E getEnum(Class<E> clazz, K key, E defaultValue) {
		final Object obj = getObj(key);
		if (null == obj) {
			return defaultValue;
		}
		return Convert.toEnum(clazz, obj, defaultValue);
	}

	@Override
	default Date getDate(K key, Date defaultValue) {
		final Object obj = getObj(key);
		if (null == obj) {
			return defaultValue;
		}
		return Convert.toDate(obj, defaultValue);
	}
}

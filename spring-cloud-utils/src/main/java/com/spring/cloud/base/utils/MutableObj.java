package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.crypto.ObjectUtil;

import java.io.Serializable;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/13 16:11
 */
public class MutableObj<T> implements Mutable<T>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 构建MutableObj
	 *
	 * @param value 被包装的值
	 * @param <T>   值类型
	 * @return MutableObj
	 * @since 5.8.0
	 */
	public static <T> MutableObj<T> of(T value) {
		return new MutableObj<>(value);
	}

	private T value;

	/**
	 * 构造，空值
	 */
	public MutableObj() {
	}

	/**
	 * 构造
	 *
	 * @param value 值
	 */
	public MutableObj(final T value) {
		this.value = value;
	}

	// -----------------------------------------------------------------------
	@Override
	public T get() {
		return this.value;
	}

	@Override
	public void set(final T value) {
		this.value = value;
	}

	// -----------------------------------------------------------------------
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (this.getClass() == obj.getClass()) {
			final MutableObj<?> that = (MutableObj<?>) obj;
			return ObjectUtil.equals(this.value, that.value);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return value == null ? 0 : value.hashCode();
	}

	// -----------------------------------------------------------------------
	@Override
	public String toString() {
		return value == null ? "null" : value.toString();
	}

}

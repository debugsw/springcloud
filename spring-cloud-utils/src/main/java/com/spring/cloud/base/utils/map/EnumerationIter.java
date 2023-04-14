package com.spring.cloud.base.utils.map;

import java.io.Serializable;
import java.util.Enumeration;

/**
 * @Author: ls
 * @Description: 对象转换
 * @Date: 2023/4/13 16:11
 */
public class EnumerationIter<E> implements IterableIter<E>, Serializable {
	private static final long serialVersionUID = 1L;

	private final Enumeration<E> e;

	/**
	 * 构造
	 *
	 * @param enumeration {@link Enumeration}对象
	 */
	public EnumerationIter(Enumeration<E> enumeration) {
		this.e = enumeration;
	}

	@Override
	public boolean hasNext() {
		return e.hasMoreElements();
	}

	@Override
	public E next() {
		return e.nextElement();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}

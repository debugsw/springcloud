package com.spring.cloud.base.utils;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/13 16:11
 */
public class IteratorEnumeration<E> implements Enumeration<E>, Serializable {
	private static final long serialVersionUID = 1L;

	private final Iterator<E> iterator;

	/**
	 * 构造
	 *
	 * @param iterator {@link Iterator}对象
	 */
	public IteratorEnumeration(Iterator<E> iterator) {
		this.iterator = iterator;
	}

	@Override
	public boolean hasMoreElements() {
		return iterator.hasNext();
	}

	@Override
	public E nextElement() {
		return iterator.next();
	}

}

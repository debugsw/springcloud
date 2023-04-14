package com.spring.cloud.base.utils;

import java.util.Iterator;

/**
 * @Author: ls
 * @Description: 支持重置的接口
 * @Date: 2023/4/13 16:11
 */
public interface ResettableIter<E> extends Iterator<E> {

	/**
	 * 重置，重置后可重新遍历
	 */
	void reset();
}

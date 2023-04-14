package com.spring.cloud.base.utils.map;

import java.util.Iterator;

/**
 * @Author: ls
 * @Description: 提供合成接口共同提供功能
 * @Date: 2023/4/13 16:11
 */
public interface IterableIter<T> extends Iterable<T>, Iterator<T> {

	@Override
	default Iterator<T> iterator() {
		return this;
	}
}

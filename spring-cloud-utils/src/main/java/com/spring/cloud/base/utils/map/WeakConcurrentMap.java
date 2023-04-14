package com.spring.cloud.base.utils.map;

import java.lang.ref.Reference;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: ls
 * @Description: 线程安全的WeakMap实现
 * @Date: 2023/4/13 16:11
 */
public class WeakConcurrentMap<K, V> extends ReferenceConcurrentMap<K, V> {

	/**
	 * 构造
	 */
	public WeakConcurrentMap() {
		this(new SafeConcurrentHashMap<>());
	}

	/**
	 * 构造
	 *
	 * @param raw {@link ConcurrentMap}实现
	 */
	public WeakConcurrentMap(ConcurrentMap<Reference<K>, V> raw) {
		super(raw, ReferenceUtil.ReferenceType.WEAK);
	}
}

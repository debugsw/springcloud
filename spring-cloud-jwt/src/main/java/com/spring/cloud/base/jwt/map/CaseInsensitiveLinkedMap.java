package com.spring.cloud.base.jwt.map;

import com.spring.cloud.base.utils.map.CaseInsensitiveMap;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: ls
 * @Description: 忽略大小写的LinkedHashMap
 * @Date: 2023/4/25 13:36
 */
public class CaseInsensitiveLinkedMap<K, V> extends CaseInsensitiveMap<K, V> {

	private static final long serialVersionUID = 4043263744224569870L;

	/**
	 * 构造
	 */
	public CaseInsensitiveLinkedMap() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 */
	public CaseInsensitiveLinkedMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * 构造
	 *
	 * @param m Map
	 */
	public CaseInsensitiveLinkedMap(Map<? extends K, ? extends V> m) {
		this(DEFAULT_LOAD_FACTOR, m);
	}

	/**
	 * 构造
	 *
	 * @param loadFactor 加载因子
	 * @param m          Map
	 */
	public CaseInsensitiveLinkedMap(float loadFactor, Map<? extends K, ? extends V> m) {
		this(m.size(), loadFactor);
		this.putAll(m);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 * @param loadFactor      加载因子
	 */
	public CaseInsensitiveLinkedMap(int initialCapacity, float loadFactor) {
		super(new LinkedHashMap<>(initialCapacity, loadFactor));
	}
}

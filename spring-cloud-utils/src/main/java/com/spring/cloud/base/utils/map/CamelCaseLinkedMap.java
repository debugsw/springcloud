package com.spring.cloud.base.utils.map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: ls
 * @Description: 驼峰Key风格的LinkedHashMap
 * @Date: 2023/4/13 16:11
 */
public class CamelCaseLinkedMap<K, V> extends CamelCaseMap<K, V> {

	private static final long serialVersionUID = 4043263744224569870L;

	/**
	 * 构造
	 */
	public CamelCaseLinkedMap() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 */
	public CamelCaseLinkedMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * 构造
	 *
	 * @param m Map
	 */
	public CamelCaseLinkedMap(Map<? extends K, ? extends V> m) {
		this(DEFAULT_LOAD_FACTOR, m);
	}

	/**
	 * 构造
	 *
	 * @param loadFactor 加载因子
	 * @param m          Map，数据会被默认拷贝到一个新的LinkedHashMap中
	 */
	public CamelCaseLinkedMap(float loadFactor, Map<? extends K, ? extends V> m) {
		this(m.size(), loadFactor);
		this.putAll(m);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 * @param loadFactor      加载因子
	 */
	public CamelCaseLinkedMap(int initialCapacity, float loadFactor) {
		super(new LinkedHashMap<>(initialCapacity, loadFactor));
	}
}

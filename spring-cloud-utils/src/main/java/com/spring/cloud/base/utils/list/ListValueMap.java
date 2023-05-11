package com.spring.cloud.base.utils.list;

import java.util.*;

/**
 * @Author: ls
 * @Description: 值作为集合List的Map实现
 * @Date: 2023/4/13 16:11
 */
public class ListValueMap<K, V> extends AbsCollValueMap<K, V, List<V>> {

	private static final long serialVersionUID = 2143995220439567244L;

	/**
	 * 构造
	 */
	public ListValueMap() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 */
	public ListValueMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * 构造
	 *
	 * @param m Map
	 */
	public ListValueMap(Map<? extends K, ? extends Collection<V>> m) {
		this(DEFAULT_LOAD_FACTOR, m);
	}

	/**
	 * 构造
	 *
	 * @param loadFactor 加载因子
	 * @param m          Map
	 */
	public ListValueMap(float loadFactor, Map<? extends K, ? extends Collection<V>> m) {
		this(m.size(), loadFactor);
		this.putAllValues(m);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 * @param loadFactor      加载因子
	 */
	public ListValueMap(int initialCapacity, float loadFactor) {
		super(new HashMap<>(initialCapacity, loadFactor));
	}

	@Override
	protected List<V> createCollection() {
		return new ArrayList<>(DEFAULT_COLLECTION_INITIAL_CAPACITY);
	}
}

package com.spring.cloud.base.utils.map;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @Author: ls
 * @Description: 忽略大小写的Map
 * @Date: 2023/4/13 16:11
 */
public class CaseInsensitiveMap<K, V> extends FuncKeyMap<K, V> {

	private static final long serialVersionUID = 4043263744224569870L;

	/**
	 * 构造
	 */
	public CaseInsensitiveMap() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 */
	public CaseInsensitiveMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * 构造
	 *
	 * @param m 被包装的自定义Map创建器
	 */
	public CaseInsensitiveMap(Map<? extends K, ? extends V> m) {
		this(DEFAULT_LOAD_FACTOR, m);
	}

	/**
	 * 构造
	 *
	 * @param loadFactor 加载因子
	 * @param m          Map
	 */
	public CaseInsensitiveMap(float loadFactor, Map<? extends K, ? extends V> m) {
		this(m.size(), loadFactor);
		this.putAll(m);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 * @param loadFactor      加载因子
	 */
	public CaseInsensitiveMap(int initialCapacity, float loadFactor) {
		this(MapBuilder.create(new HashMap<>(initialCapacity, loadFactor)));
	}

	/**
	 * 构造
	 *
	 * @param emptyMapBuilder 被包装的自定义Map创建器
	 */
	CaseInsensitiveMap(MapBuilder<K, V> emptyMapBuilder) {
		// issue#I5VRHW@Gitee 使Function可以被序列化
		super(emptyMapBuilder.build(), (Function<Object, K> & Serializable) (key) -> {
			if (key instanceof CharSequence) {
				key = key.toString().toLowerCase();
			}
			//noinspection unchecked
			return (K) key;
		});
	}
}

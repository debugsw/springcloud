package com.spring.cloud.base.utils.map;

import com.spring.cloud.base.utils.utils.JdkUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
/**
 * @Author: ls
 * @Description: 安全的ConcurrentHashMap实现
 * @Date: 2023/4/13 16:11
 */
public class SafeConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> {
	private static final long serialVersionUID = 1L;

	// region == 构造 ==

	/**
	 * 构造，默认初始大小（16）
	 */
	public SafeConcurrentHashMap() {
		super();
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 预估初始大小
	 */
	public SafeConcurrentHashMap(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * 构造
	 *
	 * @param m 初始键值对
	 */
	public SafeConcurrentHashMap(Map<? extends K, ? extends V> m) {
		super(m);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 * @param loadFactor      增长系数
	 */
	public SafeConcurrentHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity  初始容量
	 * @param loadFactor       增长系数
	 * @param concurrencyLevel 并发级别，即Segment的个数
	 */
	public SafeConcurrentHashMap(int initialCapacity,
                                 float loadFactor, int concurrencyLevel) {
		super(initialCapacity, loadFactor, concurrencyLevel);
	}
	// endregion == 构造 ==

	@Override
	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		if (JdkUtil.IS_JDK8) {
			return MapUtil.computeIfAbsentForJdk8(this, key, mappingFunction);
		} else {
			return super.computeIfAbsent(key, mappingFunction);
		}
	}
}

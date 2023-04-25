package com.spring.cloud.base.jwt.utils;

import com.spring.cloud.base.utils.base.Mutable;
import com.spring.cloud.base.utils.map.Pair;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/25 11:29
 */
public class MutablePair<K, V> extends Pair<K, V> implements Mutable<Pair<K, V>> {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 *
	 * @param key   键
	 * @param value 值
	 */
	public MutablePair(K key, V value) {
		super(key, value);
	}

	/**
	 * 设置键
	 *
	 * @param key 新键
	 * @return this
	 */
	public MutablePair<K, V> setKey(K key) {
		this.key = key;
		return this;
	}

	/**
	 * 设置值
	 *
	 * @param value 新值
	 * @return this
	 */
	public MutablePair<K, V> setValue(V value) {
		this.value = value;
		return this;
	}

	@Override
	public Pair<K, V> get() {
		return this;
	}

	@Override
	public void set(Pair<K, V> pair) {
		this.key = pair.getKey();
		this.value = pair.getValue();
	}
}

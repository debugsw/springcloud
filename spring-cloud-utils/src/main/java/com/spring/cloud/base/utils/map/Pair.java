package com.spring.cloud.base.utils.map;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: ls
 * @Description: 键值对对象，只能在构造时传入键值
 * @Date: 2023/4/13 16:11
 */
public class Pair<K, V> extends CloneSupport<Pair<K, V>> implements Serializable {
	private static final long serialVersionUID = 1L;

	protected K key;
	protected V value;

	/**
	 * 构建{@code Pair}对象
	 *
	 * @param <K>   键类型
	 * @param <V>   值类型
	 * @param key   键
	 * @param value 值
	 * @return {@code Pair}
	 */
	public static <K, V> Pair<K, V> of(K key, V value) {
		return new Pair<>(key, value);
	}

	/**
	 * 构造
	 *
	 * @param key   键
	 * @param value 值
	 */
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * 获取键
	 *
	 * @return 键
	 */
	public K getKey() {
		return this.key;
	}

	/**
	 * 获取值
	 *
	 * @return 值
	 */
	public V getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "Pair [key=" + key + ", value=" + value + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof Pair) {
			Pair<?, ?> pair = (Pair<?, ?>) o;
			return Objects.equals(getKey(), pair.getKey()) &&
					Objects.equals(getValue(), pair.getValue());
		}
		return false;
	}

	@Override
	public int hashCode() {
		//copy from 1.8 HashMap.Node
		return Objects.hashCode(key) ^ Objects.hashCode(value);
	}
}

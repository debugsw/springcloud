package com.spring.cloud.base.utils.map;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @Author: ls
 * @Description: 双向Map
 * @Date: 2023/4/13 16:11
 */
public class BiMap<K, V> extends MapWrapper<K, V> {

	private static final long serialVersionUID = 1L;

	private Map<V, K> inverse;

	/**
	 * 构造
	 *
	 * @param raw 被包装的Map
	 */
	public BiMap(Map<K, V> raw) {
		super(raw);
	}

	@Override
	public V put(K key, V value) {
		if (null != this.inverse) {
			this.inverse.put(value, key);
		}
		return super.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		super.putAll(m);
		if (null != this.inverse) {
			m.forEach((key, value) -> this.inverse.put(value, key));
		}
	}

	@Override
	public V remove(Object key) {
		final V v = super.remove(key);
		if (null != this.inverse && null != v) {
			this.inverse.remove(v);
		}
		return v;
	}

	@Override
	public boolean remove(Object key, Object value) {
		return super.remove(key, value) && null != this.inverse && this.inverse.remove(value, key);
	}

	@Override
	public void clear() {
		super.clear();
		this.inverse = null;
	}

	/**
	 * 获取反向Map
	 *
	 * @return 反向Map
	 */
	public Map<V, K> getInverse() {
		if (null == this.inverse) {
			inverse = MapUtil.inverse(getRaw());
		}
		return this.inverse;
	}

	/**
	 * 根据值获得键
	 *
	 * @param value 值
	 * @return 键
	 */
	public K getKey(V value) {
		return getInverse().get(value);
	}

	@Override
	public V putIfAbsent(K key, V value) {
		if (null != this.inverse) {
			this.inverse.putIfAbsent(value, key);
		}
		return super.putIfAbsent(key, value);
	}

	@Override
	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		final V result = super.computeIfAbsent(key, mappingFunction);
		resetInverseMap();
		return result;
	}

	@Override
	public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		final V result = super.computeIfPresent(key, remappingFunction);
		resetInverseMap();
		return result;
	}

	@Override
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		final V result = super.compute(key, remappingFunction);
		resetInverseMap();
		return result;
	}

	@Override
	public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		final V result = super.merge(key, value, remappingFunction);
		resetInverseMap();
		return result;
	}

	/**
	 * 重置反转的Map，如果反转map为空，则不操作。
	 */
	private void resetInverseMap() {
		if (null != this.inverse) {
			inverse = null;
		}
	}
}

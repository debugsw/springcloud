package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.map.MapBuilder;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

/**
 * @Author: ls
 * @Description: 唯一键的Set
 * @Date: 2023/4/13 16:11
 */
public class UniqueKeySet<K, V> extends AbstractSet<V> implements Serializable {
	private static final long serialVersionUID = 1L;

	private Map<K, V> map;
	private final Function<V, K> uniqueGenerator;

	//region 构造

	/**
	 * 构造
	 *
	 * @param uniqueGenerator 唯一键生成规则函数，用于生成对象对应的唯一键
	 */
	public UniqueKeySet(Function<V, K> uniqueGenerator) {
		this(false, uniqueGenerator);
	}

	/**
	 * 构造
	 *
	 * @param uniqueGenerator 唯一键生成规则函数，用于生成对象对应的唯一键
	 * @param c               初始化加入的集合
	 * @since 5.8.0
	 */
	public UniqueKeySet(Function<V, K> uniqueGenerator, Collection<? extends V> c) {
		this(false, uniqueGenerator, c);
	}

	/**
	 * 构造
	 *
	 * @param isLinked        是否保持加入顺序
	 * @param uniqueGenerator 唯一键生成规则函数，用于生成对象对应的唯一键
	 */
	public UniqueKeySet(boolean isLinked, Function<V, K> uniqueGenerator) {
		this(MapBuilder.create(isLinked), uniqueGenerator);
	}

	/**
	 * 构造
	 *
	 * @param isLinked        是否保持加入顺序
	 * @param uniqueGenerator 唯一键生成规则函数，用于生成对象对应的唯一键
	 * @param c               初始化加入的集合
	 * @since 5.8.0
	 */
	public UniqueKeySet(boolean isLinked, Function<V, K> uniqueGenerator, Collection<? extends V> c) {
		this(isLinked, uniqueGenerator);
		addAll(c);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 * @param loadFactor      增长因子
	 * @param uniqueGenerator 唯一键生成规则函数，用于生成对象对应的唯一键
	 */
	public UniqueKeySet(int initialCapacity, float loadFactor, Function<V, K> uniqueGenerator) {
		this(MapBuilder.create(new HashMap<>(initialCapacity, loadFactor)), uniqueGenerator);
	}

	/**
	 * 构造
	 *
	 * @param builder         初始Map，定义了Map类型
	 * @param uniqueGenerator 唯一键生成规则函数，用于生成对象对应的唯一键
	 */
	public UniqueKeySet(MapBuilder<K, V> builder, Function<V, K> uniqueGenerator) {
		this.map = builder.build();
		this.uniqueGenerator = uniqueGenerator;
	}

	//endregion

	@Override
	public Iterator<V> iterator() {
		return map.values().iterator();
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		//noinspection unchecked
		return map.containsKey(this.uniqueGenerator.apply((V) o));
	}

	@Override
	public boolean add(V v) {
		return null == map.put(this.uniqueGenerator.apply(v), v);
	}

	/**
	 * 加入值，如果值已经存在，则忽略之
	 *
	 * @param v 值
	 * @return 是否成功加入
	 */
	public boolean addIfAbsent(V v) {
		return null == map.putIfAbsent(this.uniqueGenerator.apply(v), v);
	}

	/**
	 * 加入集合中所有的值，如果值已经存在，则忽略之
	 *
	 * @param c 集合
	 * @return 是否有一个或多个被加入成功
	 */
	public boolean addAllIfAbsent(Collection<? extends V> c) {
		boolean modified = false;
		for (V v : c) {
			if (addIfAbsent(v)) {
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public boolean remove(Object o) {
		//noinspection unchecked
		return null != map.remove(this.uniqueGenerator.apply((V) o));
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	@SuppressWarnings("unchecked")
	public UniqueKeySet<K, V> clone() {
		try {
			UniqueKeySet<K, V> newSet = (UniqueKeySet<K, V>) super.clone();
			newSet.map = ObjectUtil.clone(this.map);
			return newSet;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

}

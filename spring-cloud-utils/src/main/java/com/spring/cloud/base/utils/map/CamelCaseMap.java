package com.spring.cloud.base.utils.map;

import com.spring.cloud.base.utils.str.StrUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @Author: ls
 * @Description: 驼峰Key风格的Map
 * @Date: 2023/4/13 16:11
 */
public class CamelCaseMap<K, V> extends FuncKeyMap<K, V> {
	private static final long serialVersionUID = 4043263744224569870L;

	/**
	 * 构造
	 */
	public CamelCaseMap() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 */
	public CamelCaseMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * 构造
	 *
	 * @param m Map
	 */
	public CamelCaseMap(Map<? extends K, ? extends V> m) {
		this(DEFAULT_LOAD_FACTOR, m);
	}

	/**
	 * 构造
	 *
	 * @param loadFactor 加载因子
	 * @param m          初始Map，数据会被默认拷贝到一个新的HashMap中
	 */
	public CamelCaseMap(float loadFactor, Map<? extends K, ? extends V> m) {
		this(m.size(), loadFactor);
		this.putAll(m);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始大小
	 * @param loadFactor      加载因子
	 */
	public CamelCaseMap(int initialCapacity, float loadFactor) {
		this(MapBuilder.create(new HashMap<>(initialCapacity, loadFactor)));
	}

	/**
	 * 构造<br>
	 * 注意此构造将传入的Map作为被包装的Map，针对任何修改，传入的Map都会被同样修改。
	 *
	 * @param emptyMapBuilder Map构造器，必须构造空的Map
	 */
	CamelCaseMap(MapBuilder<K, V> emptyMapBuilder) {
		// issue#I5VRHW@Gitee 使Function可以被序列化
		super(emptyMapBuilder.build(), (Function<Object, K> & Serializable) (key) -> {
			if (key instanceof CharSequence) {
				key = StrUtil.toCamelCase(key.toString());
			}
			return (K) key;
		});
	}
}

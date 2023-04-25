package com.spring.cloud.base.jwt.map;

import com.spring.cloud.base.utils.map.CaseInsensitiveMap;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @Author: ls
 * @Description: 忽略大小写的TreeMap
 * @Date: 2023/4/25 13:36
 */
public class CaseInsensitiveTreeMap<K, V> extends CaseInsensitiveMap<K, V> {

	private static final long serialVersionUID = 4043263744224569870L;

	/**
	 * 构造
	 */
	public CaseInsensitiveTreeMap() {
		this((Comparator<? super K>) null);
	}

	/**
	 * 构造
	 *
	 * @param m Map
	 */
	public CaseInsensitiveTreeMap(Map<? extends K, ? extends V> m) {
		this();
		this.putAll(m);
	}

	/**
	 * 构造
	 *
	 * @param m Map，初始Map，键值对会被复制到新的TreeMap中
	 */
	public CaseInsensitiveTreeMap(SortedMap<? extends K, ? extends V> m) {
		super(new TreeMap<K, V>(m));
	}

	/**
	 * 构造
	 *
	 * @param comparator 比较器，{@code null}表示使用默认比较器
	 */
	public CaseInsensitiveTreeMap(Comparator<? super K> comparator) {
		super(new TreeMap<>(comparator));
	}
}

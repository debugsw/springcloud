package com.spring.cloud.base.utils.map;

import java.util.Map;

/**
 * @Author: ls
 * @Description: 自定义键的Map默认HashMap实现
 * @Date: 2023/4/13 16:11
 */
public abstract class CustomKeyMap<K, V> extends TransMap<K, V> {
	private static final long serialVersionUID = 4043263744224569870L;

	/**
	 * 构造<br>
	 * 通过传入一个Map从而确定Map的类型，子类需创建一个空的Map，而非传入一个已有Map，否则值可能会被修改
	 *
	 * @param emptyMap Map 被包装的Map，必须为空Map，否则自定义key会无效
	 * @since 3.1.2
	 */
	public CustomKeyMap(Map<K, V> emptyMap) {
		super(emptyMap);
	}

	@Override
	protected V customValue(Object value) {
		//noinspection unchecked
		return (V) value;
	}
}

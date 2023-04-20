package com.spring.cloud.base.utils.base;

import com.spring.cloud.base.utils.map.Func0;
import com.spring.cloud.base.utils.map.WeakConcurrentMap;

/**
 * @Author: ls
 * @Description: Bean属性缓存
 * @Date: 2023/4/13 16:11
 */
public enum BeanDescCache {

	INSTANCE;

	private final WeakConcurrentMap<Class<?>, BeanDesc> bdCache = new WeakConcurrentMap<>();

	/**
	 * 获得属性名和{@link BeanDesc}Map映射
	 *
	 * @param beanClass Bean的类
	 * @param supplier  对象不存在时创建对象的函数
	 * @return 属性名和{@link BeanDesc}映射
	 */
	public BeanDesc getBeanDesc(Class<?> beanClass, Func0<BeanDesc> supplier) {
		return bdCache.computeIfAbsent(beanClass, (key) -> supplier.callWithRuntimeException());
	}

	/**
	 * 清空全局的Bean属性缓存
	 */
	public void clear() {
		this.bdCache.clear();
	}
}

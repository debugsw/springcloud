package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.list.FuncComparator;
/**
 * @Author: ls
 * @Description: Bean属性排序器
 * @Date: 2023/4/13 16:11
 */
public class PropertyComparator<T> extends FuncComparator<T> {
	private static final long serialVersionUID = 9157326766723846313L;

	/**
	 * 构造
	 *
	 * @param property 属性名
	 */
	public PropertyComparator(String property) {
		this(property, true);
	}

	/**
	 * 构造
	 *
	 * @param property 属性名
	 * @param isNullGreater null值是否排在后（从小到大排序）
	 */
	public PropertyComparator(String property, boolean isNullGreater) {
		super(isNullGreater, (bean)-> BeanUtil.getProperty(bean, property));
	}
}

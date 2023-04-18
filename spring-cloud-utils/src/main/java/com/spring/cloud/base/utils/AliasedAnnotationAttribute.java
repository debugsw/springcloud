package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.abstra.AbstractWrappedAnnotationAttribute;

/**
 * @Author: ls
 * @Description: 表示一个具有别名的属性
 * @Date: 2023/4/13 16:11
 */
public class AliasedAnnotationAttribute extends AbstractWrappedAnnotationAttribute {

	protected AliasedAnnotationAttribute(AnnotationAttribute origin, AnnotationAttribute linked) {
		super(origin, linked);
	}

	/**
	 * 若{@link #linked}为默认值，则返回{@link #original}的值，否则返回{@link #linked}的值
	 *
	 * @return 属性值
	 */
	@Override
	public Object getValue() {
		return linked.isValueEquivalentToDefaultValue() ? super.getValue() : linked.getValue();
	}

	/**
	 * 当{@link #original}与{@link #linked}都为默认值时返回{@code true}
	 *
	 * @return 是否
	 */
	@Override
	public boolean isValueEquivalentToDefaultValue() {
		return linked.isValueEquivalentToDefaultValue() && original.isValueEquivalentToDefaultValue();
	}
}

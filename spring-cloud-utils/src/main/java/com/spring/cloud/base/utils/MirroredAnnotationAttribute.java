package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.abstra.AbstractWrappedAnnotationAttribute;
import com.spring.cloud.base.utils.interf.AnnotationAttribute;

/**
 * @Author: ls
 * @Description: 表示存在对应镜像属性的注解属性
 * @Date: 2023/4/13 16:11
 */
public class MirroredAnnotationAttribute extends AbstractWrappedAnnotationAttribute {

	public MirroredAnnotationAttribute(AnnotationAttribute origin, AnnotationAttribute linked) {
		super(origin, linked);
	}

	@Override
	public Object getValue() {
		final boolean originIsDefault = original.isValueEquivalentToDefaultValue();
		final boolean targetIsDefault = linked.isValueEquivalentToDefaultValue();
		final Object originValue = original.getValue();
		final Object targetValue = linked.getValue();

		// 都为默认值，或都为非默认值时，两方法的返回值必须相等
		if (originIsDefault == targetIsDefault) {
			Assert.equals(
					originValue, targetValue,
					"the values of attributes [{}] and [{}] that mirror each other are different: [{}] <==> [{}]",
					original.getAttribute(), linked.getAttribute(), originValue, targetValue
			);
			return originValue;
		}

		// 两者有一者不为默认值时，优先返回非默认值
		return originIsDefault ? targetValue : originValue;
	}

	/**
	 * 当{@link #original}与{@link #linked}都为默认值时返回{@code true}
	 *
	 * @return 是否
	 */
	@Override
	public boolean isValueEquivalentToDefaultValue() {
		return original.isValueEquivalentToDefaultValue() && linked.isValueEquivalentToDefaultValue();
	}
}

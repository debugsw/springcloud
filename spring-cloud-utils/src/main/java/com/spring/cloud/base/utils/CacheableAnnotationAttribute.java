package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.base.ReflectUtil;
import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.interf.AnnotationAttribute;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/13 16:11
 */
public class CacheableAnnotationAttribute implements AnnotationAttribute {

	private boolean valueInvoked;
	private Object value;

	private boolean defaultValueInvoked;
	private Object defaultValue;

	private final Annotation annotation;
	private final Method attribute;

	public CacheableAnnotationAttribute(Annotation annotation, Method attribute) {
		Assert.notNull(annotation, "annotation must not null");
		Assert.notNull(attribute, "attribute must not null");
		this.annotation = annotation;
		this.attribute = attribute;
		this.valueInvoked = false;
		this.defaultValueInvoked = false;
	}

	@Override
	public Annotation getAnnotation() {
		return this.annotation;
	}

	@Override
	public Method getAttribute() {
		return this.attribute;
	}

	@Override
	public Object getValue() {
		if (!valueInvoked) {
			valueInvoked = true;
			value = ReflectUtil.invoke(annotation, attribute);
		}
		return value;
	}

	@Override
	public boolean isValueEquivalentToDefaultValue() {
		if (!defaultValueInvoked) {
			defaultValue = attribute.getDefaultValue();
			defaultValueInvoked = true;
		}
		return ObjectUtil.equals(getValue(), defaultValue);
	}

}

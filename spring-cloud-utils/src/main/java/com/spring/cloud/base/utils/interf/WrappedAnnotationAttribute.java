package com.spring.cloud.base.utils.interf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/13 16:11
 */
public interface WrappedAnnotationAttribute extends AnnotationAttribute {

	/**
	 * 获取被包装的{@link AnnotationAttribute}对象，该对象也可能是{@link AnnotationAttribute}
	 *
	 * @return 被包装的{@link AnnotationAttribute}对象
	 */
	AnnotationAttribute getOriginal();

	/**
	 * 获取最初的被包装的{@link AnnotationAttribute}
	 *
	 * @return 最初的被包装的{@link AnnotationAttribute}
	 */
	AnnotationAttribute getNonWrappedOriginal();

	/**
	 * 获取包装{@link #getOriginal()}的{@link AnnotationAttribute}对象，该对象也可能是{@link AnnotationAttribute}
	 *
	 * @return 包装对象
	 */
	AnnotationAttribute getLinked();

	/**
	 * 遍历以当前实例为根节点的树结构，获取所有未被包装的属性
	 *
	 * @return 叶子节点
	 */
	Collection<AnnotationAttribute> getAllLinkedNonWrappedAttributes();

	// =========================== 代理实现 ===========================

	/**
	 * 获取注解对象
	 *
	 * @return 注解对象
	 */
	@Override
	default Annotation getAnnotation() {
		return getOriginal().getAnnotation();
	}

	/**
	 * 获取注解属性对应的方法
	 *
	 * @return 注解属性对应的方法
	 */
	@Override
	default Method getAttribute() {
		return getOriginal().getAttribute();
	}

	/**
	 * 该注解属性的值是否等于默认值 <br>
	 * 默认仅当{@link #getOriginal()}与{@link #getLinked()}返回的注解属性
	 * 都为默认值时，才返回{@code true}
	 *
	 * @return 该注解属性的值是否等于默认值
	 */
	@Override
	boolean isValueEquivalentToDefaultValue();

	/**
	 * 获取属性类型
	 *
	 * @return 属性类型
	 */
	@Override
	default Class<?> getAttributeType() {
		return getOriginal().getAttributeType();
	}

	/**
	 * 获取属性上的注解
	 *
	 * @param annotationType 注解类型
	 * @return 注解对象
	 */
	@Override
	default <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		return getOriginal().getAnnotation(annotationType);
	}

	/**
	 * 当前注解属性是否已经被{@link WrappedAnnotationAttribute}包装
	 *
	 * @return boolean
	 */
	@Override
	default boolean isWrapped() {
		return true;
	}

}

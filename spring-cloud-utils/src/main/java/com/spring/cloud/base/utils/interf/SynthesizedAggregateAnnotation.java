package com.spring.cloud.base.utils.interf;

import java.lang.annotation.Annotation;

public interface SynthesizedAggregateAnnotation extends AggregateAnnotation, Hierarchical, AnnotationSynthesizer, AnnotationAttributeValueProvider {

	/**
	 * 距离{@link #getRoot()}返回值的垂直距离，
	 * 默认聚合注解即为根对象，因此返回0
	 *
	 * @return 距离{@link #getRoot()}返回值的水平距离，
	 */
	@Override
	default int getVerticalDistance() {
		return 0;
	}

	/**
	 * 距离{@link #getRoot()}返回值的水平距离，
	 * 默认聚合注解即为根对象，因此返回0
	 *
	 * @return 距离{@link #getRoot()}返回值的水平距离，
	 */
	@Override
	default int getHorizontalDistance() {
		return 0;
	}

	/**
	 * 获取在聚合中存在的指定注解对象
	 *
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	<T extends Annotation> T getAnnotation(Class<T> annotationType);

	/**
	 * 获取合成注解属性处理器
	 *
	 * @return 合成注解属性处理器
	 */
	SynthesizedAnnotationAttributeProcessor getAnnotationAttributeProcessor();

	/**
	 * 获取当前的注解类型
	 *
	 * @return 注解类型
	 */
	@Override
	default Class<? extends Annotation> annotationType() {
		return this.getClass();
	}

	/**
	 * 从聚合中获取指定类型的属性值
	 *
	 * @param attributeName 属性名称
	 * @param attributeType 属性类型
	 * @return 属性值
	 */
	@Override
	Object getAttributeValue(String attributeName, Class<?> attributeType);

}

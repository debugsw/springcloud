package com.spring.cloud.base.utils.interf;

import com.spring.cloud.base.utils.CollUtil;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.function.UnaryOperator;

public interface SynthesizedAnnotation extends Annotation, Hierarchical, AnnotationAttributeValueProvider {

	/**
	 * 获取被合成的注解对象
	 *
	 * @return 注解对象
	 */
	Annotation getAnnotation();

	/**
	 * 获取该合成注解与根对象的垂直距离。
	 * 默认情况下，该距离即为当前注解与根对象之间相隔的层级数。
	 *
	 * @return 合成注解与根对象的垂直距离
	 */
	@Override
	int getVerticalDistance();

	/**
	 * 获取该合成注解与根对象的水平距离。
	 * 默认情况下，该距离即为当前注解与根对象之间相隔的已经被扫描到的注解数。
	 *
	 * @return 合成注解与根对象的水平距离
	 */
	@Override
	int getHorizontalDistance();

	/**
	 * 注解是否存在该名称相同，且类型一致的属性
	 *
	 * @param attributeName 属性名
	 * @param returnType    返回值类型
	 * @return 是否存在该属性
	 */
	boolean hasAttribute(String attributeName, Class<?> returnType);

	/**
	 * 获取该注解的全部属性
	 *
	 * @return 注解属性
	 */
	Map<String, AnnotationAttribute> getAttributes();

	/**
	 * 设置该注解的全部属性
	 *
	 * @param attributes 注解属性
	 */
	default void setAttributes(Map<String, AnnotationAttribute> attributes) {
		if (CollUtil.isNotEmpty(attributes)) {
			attributes.forEach(this::setAttribute);
		}
	}

	/**
	 * 设置属性值
	 *
	 * @param attributeName 属性名称
	 * @param attribute     注解属性
	 */
	void setAttribute(String attributeName, AnnotationAttribute attribute);

	/**
	 * 替换属性值
	 *
	 * @param attributeName 属性名
	 * @param operator      替换操作
	 */
	void replaceAttribute(String attributeName, UnaryOperator<AnnotationAttribute> operator);

	/**
	 * 获取属性值
	 *
	 * @param attributeName 属性名
	 * @return 属性值
	 */
	Object getAttributeValue(String attributeName);

}

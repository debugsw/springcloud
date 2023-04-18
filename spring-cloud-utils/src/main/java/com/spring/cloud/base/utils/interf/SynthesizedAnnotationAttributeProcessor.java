package com.spring.cloud.base.utils.interf;

import java.util.Collection;

/**
 * @Author: ls
 * @Description: 合成注解属性选择器
 * @Date: 2023/4/13 16:11
 */
@FunctionalInterface
public interface SynthesizedAnnotationAttributeProcessor {

	/**
	 * 从一批被合成注解中，获取指定名称与类型的属性值
	 *
	 * @param attributeName          属性名称
	 * @param attributeType          属性类型
	 * @param synthesizedAnnotations 被合成的注解
	 * @param <R>                    属性类型
	 * @return 属性值
	 */
	<R> R getAttributeValue(String attributeName, Class<R> attributeType, Collection<? extends SynthesizedAnnotation> synthesizedAnnotations);

}

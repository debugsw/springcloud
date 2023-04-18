package com.spring.cloud.base.utils;

import java.lang.annotation.Annotation;

/**
 * @Author: ls
 * @Description: 表示一组被聚合在一起的注解对象
 * @Date: 2023/4/13 16:11
 */
public interface AggregateAnnotation extends Annotation {

	/**
	 * 在聚合中是否存在的指定类型注解对象
	 *
	 * @param annotationType 注解类型
	 * @return 是否
	 */
	boolean isAnnotationPresent(Class<? extends Annotation> annotationType);

	/**
	 * 获取聚合中的全部注解对象
	 *
	 * @return 注解对象
	 */
	Annotation[] getAnnotations();

}

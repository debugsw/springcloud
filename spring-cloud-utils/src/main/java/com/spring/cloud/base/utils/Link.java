package com.spring.cloud.base.utils;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Link {

	/**
	 * 产生关联的注解类型，当不指定时，默认指注释的属性所在的类
	 *
	 * @return 关联的注解类型
	 */
	Class<? extends Annotation> annotation() default Annotation.class;

	/**
	 * {@link #annotation()}指定注解中关联的属性
	 *
	 * @return 属性名
	 */
	String attribute() default "";

	/**
	 * {@link #attribute()}指定属性与当前注解的属性建的关联关系类型
	 *
	 * @return 关系类型
	 */
	RelationType type() default RelationType.MIRROR_FOR;

}

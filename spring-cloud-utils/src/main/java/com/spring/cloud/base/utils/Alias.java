package com.spring.cloud.base.utils;

import java.lang.annotation.*;

/**
 * @Author: ls
 * @Description: 别名注解
 * @Date: 2023/4/13 16:11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface Alias {

	/**
	 * 别名值，即使用此注解要替换成的别名名称
	 *
	 * @return 别名值
	 */
	String value();
}

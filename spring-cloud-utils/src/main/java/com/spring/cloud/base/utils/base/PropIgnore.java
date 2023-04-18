package com.spring.cloud.base.utils.base;

import java.lang.annotation.*;

/**
 * @Author: ls
 * @Description: 属性忽略注解
 * @Date: 2023/4/16 16:11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface PropIgnore {

}

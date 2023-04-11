package com.spring.cloud.base.common.condition;

import java.lang.annotation.*;

/**
 * @Author: ls
 * @Date: 2019/8/14
 * @Description: Condition 注解
 **/
@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Condition {

    /**
     * 数据库字段名
     * @return
     */
    String value();

    /**
     * 匹配类型
     * @return
     */
    MatchEnum match() default MatchEnum.EQUALS;

}

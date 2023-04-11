package com.spring.cloud.base.common.condition;

/**
 * @Author: ls
 * @Date: 2020-04-13 15:44:00
 * @Description: 表示这个是个查询条件类
 * 可结合QueryWrapperUtils进行自动封装QueryWrapper
 * QueryWrapper queryWrapper = QueryWrapperUtils.initQueryWrapper(conditionCo);
 * <p>
 * 注解的方式 @Condition(value = "数据库字段名",match = MatchEnum.GT);
 */
public interface ConditionCo {
}

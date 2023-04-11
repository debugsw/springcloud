package com.springcloud.base.core.condition;

/**
 * @Author: ls
 * @Date: 2019/8/14
 * @Description: Condition 匹配
 **/
public enum MatchEnum {
    // 等于 大于 小于 大于等于 小于等于 模糊匹配 IN查询 NOT IN 查询 左匹配 右匹配
    EQUALS, GT, LT, GTE, LTE, LIKE, IN, NOT_IN, LEFT_LIKE, RIGHT_LIKE
}

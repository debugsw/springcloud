package com.spring.cloud.base.utils;

import java.io.Serializable;

/**
 * @Author: ls
 * @Description: 建造者模式接口定义
 * @Date: 2023/4/13 16:11
 */
public interface Builder<T> extends Serializable {
    /**
     * 构建
     *
     * @return 被构建的对象
     */
    T build();
}
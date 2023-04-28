package com.spring.cloud.base.http;

import java.io.Serializable;

/**
 * @Author: ls
 * @Description: 建造者模式接口定义
 * @Date: 2023/4/26 15:00
 */
public interface Builder<T> extends Serializable {
    /**
     * 构建
     *
     * @return 被构建的对象
     */
    T build();
}
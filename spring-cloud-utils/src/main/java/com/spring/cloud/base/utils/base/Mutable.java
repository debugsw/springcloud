package com.spring.cloud.base.utils.base;

/**
 * @Author: ls
 * @Description: 提供可变值类型接口
 * @Date: 2023/4/13 16:11
 */
public interface Mutable<T> {

    /**
     * 获得原始值
     *
     * @return 原始值
     */
    T get();

    /**
     * 设置值
     *
     * @param value 值
     */
    void set(T value);

}
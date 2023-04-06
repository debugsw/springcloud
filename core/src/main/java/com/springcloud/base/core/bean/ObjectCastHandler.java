package com.springcloud.base.core.bean;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/6 11:34
 */
public interface ObjectCastHandler<S, T> {

    /**
     * 转换队形
     *
     * @param sourceObject 源对象
     * @param targetObject 转换对象
     * @return 转换后的对象
     */
    T cast(S sourceObject, T targetObject);
}

package com.spring.cloud.base.utils;

/**
 * @Author: ls
 * @Description: 过滤器接口
 * @Date: 2023/4/13 16:11
 */
@FunctionalInterface
public interface Filter<T> {
	/**
	 * 是否接受对象
	 *
	 * @param t 检查的对象
	 * @return 是否接受对象
	 */
	boolean accept(T t);
}
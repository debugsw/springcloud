package com.spring.cloud.base.utils.crypto;

/**
 * @Author: ls
 * @Description: 匹配接口
 * @Date: 2023/4/13 15:56
 */
@FunctionalInterface
public interface Matcher<T> {
	/**
	 * 给定对象是否匹配
	 *
	 * @param t 对象
	 * @return 是否匹配
	 */
	boolean match(T t);
}

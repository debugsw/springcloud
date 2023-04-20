package com.spring.cloud.base.utils.map;

import java.io.Serializable;

/**
 * @Author: ls
 * @Description: 函数对象
 * @Date: 2023/4/13 16:11
 */
@FunctionalInterface
public interface VoidFunc0 extends Serializable {

	/**
	 * 执行函数
	 *
	 * @throws Exception 自定义异常
	 */
	void call() throws Exception;

	/**
	 * 执行函数，异常包装为RuntimeException
	 */
	default void callWithRuntimeException() {
		try {
			call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

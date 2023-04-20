package com.spring.cloud.base.utils.crypto;

import java.io.Serializable;

/**
 * @Author: ls
 * @Description: wz
 * @Date: 2023/4/13 15:56
 */
@FunctionalInterface
public interface Func1<P, R> extends Serializable {

	/**
	 * 执行函数
	 *
	 * @param parameter 参数
	 * @return 函数执行结果
	 * @throws Exception 自定义异常
	 */
	R call(P parameter) throws Exception;

	/**
	 * 执行函数，异常包装为RuntimeException
	 *
	 * @param parameter 参数
	 * @return 函数执行结果
	 */
	default R callWithRuntimeException(P parameter) {
		try {
			return call(parameter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

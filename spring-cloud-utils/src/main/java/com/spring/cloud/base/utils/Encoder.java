package com.spring.cloud.base.utils;

/**
 * @Author: ls
 * @Description: 编码接口
 * @Date: 2023/4/13 16:11
 */
public interface Encoder<T, R> {

	/**
	 * 执行编码
	 *
	 * @param data 被编码的数据
	 * @return 编码后的数据
	 */
	R encode(T data);
}

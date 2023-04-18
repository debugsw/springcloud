package com.spring.cloud.base.utils.interf;

/**
 * @Author: ls
 * @Description: 解码接口
 * @Date: 2023/4/13 16:11
 */
public interface Decoder<T, R> {

	/**
	 * 执行解码
	 *
	 * @param encoded 被解码的数据
	 * @return 解码后的数据
	 */
	R decode(T encoded);
}

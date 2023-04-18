package com.spring.cloud.base.utils.interf;

/**
 * @Author: ls
 * @Description: 替换器
 * @Date: 2023/4/13 16:11
 */
@FunctionalInterface
public interface Replacer<T> {

	/**
	 * 替换指定类型为目标类型
	 *
	 * @param t 被替换的对象
	 * @return 替代后的对象
	 */
	T replace(T t);
}

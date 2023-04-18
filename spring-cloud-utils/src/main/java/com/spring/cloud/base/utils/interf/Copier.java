package com.spring.cloud.base.utils.interf;

/**
 * @Author: ls
 * @Description: 拷贝接口
 * @Date: 2023/4/13 16:11
 */
@FunctionalInterface
public interface Copier<T> {
	/**
	 * 执行拷贝
	 * @return 拷贝的目标
	 */
	T copy();
}

package com.spring.cloud.base.utils.map;

/**
 * @Author: ls
 * @Description: 克隆支持接口
 * @Date: 2023/4/13 16:11
 */
public interface Cloneable<T> extends java.lang.Cloneable {

	/**
	 * 克隆当前对象，浅复制
	 *
	 * @return 克隆后的对象
	 */
	T clone();
}

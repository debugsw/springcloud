package com.spring.cloud.base.utils.map;

/**
 * @Author: ls
 * @Description: 为了解决反射过程中需要传递null参数但是会丢失参数类型而设立的包装类
 * @Date: 2023/4/13 16:11
 */
public class NullWrapperBean<T> {

	private final Class<T> clazz;

	/**
	 * @param clazz null的类型
	 */
	public NullWrapperBean(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * 获取null值对应的类型
	 *
	 * @return 类型
	 */
	public Class<T> getWrappedClass() {
		return clazz;
	}
}

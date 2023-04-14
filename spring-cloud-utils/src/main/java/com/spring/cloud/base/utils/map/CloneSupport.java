package com.spring.cloud.base.utils.map;

import com.spring.cloud.base.utils.exception.CloneRuntimeException;

/**
 * @Author: ls
 * @Description: 提供默认的克隆方法
 * @Date: 2023/4/13 16:11
 */
public class CloneSupport<T> implements Cloneable<T>{

	@SuppressWarnings("unchecked")
	@Override
	public T clone() {
		try {
			return (T) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new CloneRuntimeException(e);
		}
	}

}

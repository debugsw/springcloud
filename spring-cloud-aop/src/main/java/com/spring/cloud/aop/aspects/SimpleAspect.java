package com.spring.cloud.aop.aspects;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @Author: ls
 * @Description: 简单切面类不做任何操作可以继承此类实现自己需要的方法即可
 * @Date: 2023/5/26 15:00
 */
public class SimpleAspect implements Aspect, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public boolean before(Object target, Method method, Object[] args) {
		//继承此类后实现此方法
		return true;
	}

	@Override
	public boolean after(Object target, Method method, Object[] args, Object returnVal) {
		//继承此类后实现此方法
		return true;
	}

	@Override
	public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
		//继承此类后实现此方法
		return true;
	}

}

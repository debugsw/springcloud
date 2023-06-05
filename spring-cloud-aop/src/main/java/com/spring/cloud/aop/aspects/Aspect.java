package com.spring.cloud.aop.aspects;

import java.lang.reflect.Method;

/**
 * @Author: ls
 * @Description: 切面接口
 * @Date: 2023/5/26 15:00
 */
public interface Aspect {

	boolean before(Object target, Method method, Object[] args);

	boolean after(Object target, Method method, Object[] args, Object returnVal);

	boolean afterException(Object target, Method method, Object[] args, Throwable e);
}

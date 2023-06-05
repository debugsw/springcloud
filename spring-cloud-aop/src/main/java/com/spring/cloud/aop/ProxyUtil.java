package com.spring.cloud.aop;

import com.spring.cloud.aop.aspects.Aspect;
import com.spring.cloud.aop.proxy.ProxyFactory;
import com.spring.cloud.base.utils.map.ClassUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @Author: ls
 * @Description: 代理工具类
 * @Date: 2023/5/26 15:00
 */
public final class ProxyUtil {

	public static <T> T proxy(T target, Class<? extends Aspect> aspectClass) {
		return ProxyFactory.createProxy(target, aspectClass);
	}

	public static <T> T proxy(T target, Aspect aspect) {
		return ProxyFactory.createProxy(target, aspect);
	}

	@SuppressWarnings("unchecked")
	public static <T> T newProxyInstance(ClassLoader classloader, InvocationHandler invocationHandler, Class<?>... interfaces) {
		return (T) Proxy.newProxyInstance(classloader, interfaces, invocationHandler);
	}

	public static <T> T newProxyInstance(InvocationHandler invocationHandler, Class<?>... interfaces) {
		return newProxyInstance(ClassUtil.getClassLoader(), invocationHandler, interfaces);
	}
}

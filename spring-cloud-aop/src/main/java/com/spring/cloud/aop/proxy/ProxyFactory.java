package com.spring.cloud.aop.proxy;

import com.spring.cloud.aop.aspects.Aspect;
import com.spring.cloud.base.utils.base.ReflectUtil;
import com.spring.cloud.base.utils.utils.ServiceLoaderUtil;

import java.io.Serializable;

/**
 * @Author: ls
 * @Description: 代理工厂 根据用户引入代理库的不同，产生不同的代理对象
 * @Date: 2023/5/26 15:00
 */
public abstract class ProxyFactory implements Serializable {
	private static final long serialVersionUID = 1L;

	public static <T> T createProxy(T target, Class<? extends Aspect> aspectClass) {
		return createProxy(target, ReflectUtil.newInstance(aspectClass));
	}

	public static <T> T createProxy(T target, Aspect aspect) {
		return create().proxy(target, aspect);
	}

	public static ProxyFactory create() {
		return ServiceLoaderUtil.loadFirstAvailable(ProxyFactory.class);
	}

	public <T> T proxy(T target, Class<? extends Aspect> aspectClass) {
		return proxy(target, ReflectUtil.newInstanceIfPossible(aspectClass));
	}

	public abstract <T> T proxy(T target, Aspect aspect);
}

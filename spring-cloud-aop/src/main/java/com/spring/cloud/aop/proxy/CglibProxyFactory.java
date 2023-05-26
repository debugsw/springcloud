package com.spring.cloud.aop.proxy;

import com.spring.cloud.aop.aspects.Aspect;
import com.spring.cloud.aop.interceptor.CglibInterceptor;
import net.sf.cglib.proxy.Enhancer;


/**
 * @Author: ls
 * @Description: 基于Cglib的切面代理工厂
 * @Date: 2023/5/26 15:00
 */
public class CglibProxyFactory extends ProxyFactory {
	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("unchecked")
	public <T> T proxy(T target, Aspect aspect) {
		final Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(target.getClass());
		enhancer.setCallback(new CglibInterceptor(target, aspect));
		return (T) enhancer.create();
	}

}

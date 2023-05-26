package com.spring.cloud.aop.proxy;

import com.spring.cloud.aop.ProxyUtil;
import com.spring.cloud.aop.aspects.Aspect;
import com.spring.cloud.aop.interceptor.JdkInterceptor;

/**
 * @Author: ls
 * @Description: JDK实现的切面代理
 * @Date: 2023/5/26 15:00
 */
public class JdkProxyFactory extends ProxyFactory {
	private static final long serialVersionUID = 1L;

	@Override
	public <T> T proxy(T target, Aspect aspect) {
		return ProxyUtil.newProxyInstance(
				target.getClass().getClassLoader(),
				new JdkInterceptor(target, aspect),
				target.getClass().getInterfaces());
	}
}

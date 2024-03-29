package com.spring.cloud.aop.interceptor;

import com.spring.cloud.aop.aspects.Aspect;
import com.spring.cloud.base.utils.base.ReflectUtil;
import com.spring.cloud.base.utils.map.ClassUtil;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: ls
 * @Description: JDK实现的动态代理切面
 * @Date: 2023/5/26 15:00
 */
public class JdkInterceptor implements InvocationHandler, Serializable {
	private static final long serialVersionUID = 1L;

	private final Object target;
	private final Aspect aspect;

	public JdkInterceptor(Object target, Aspect aspect) {
		this.target = target;
		this.aspect = aspect;
	}

	public Object getTarget() {
		return this.target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		final Object target = this.target;
		final Aspect aspect = this.aspect;
		Object result = null;
		// 开始前回调
		if (aspect.before(target, method, args)) {
			ReflectUtil.setAccessible(method);
			try {
				result = method.invoke(ClassUtil.isStatic(method) ? null : target, args);
			} catch (InvocationTargetException e) {
				// 异常回调（只捕获业务代码导致的异常，而非反射导致的异常）
				if (aspect.afterException(target, method, args, e.getTargetException())) {
					throw e;
				}
			}
			// 结束执行回调
			if (aspect.after(target, method, args, result)) {
				return result;
			}
		}
		return null;
	}

}

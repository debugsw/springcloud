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

	/**
	 * 根据用户引入Cglib与否自动创建代理对象
	 *
	 * @param <T>         切面对象类型
	 * @param target      目标对象
	 * @param aspectClass 切面对象类
	 * @return 代理对象
	 */
	public static <T> T createProxy(T target, Class<? extends Aspect> aspectClass) {
		return createProxy(target, ReflectUtil.newInstance(aspectClass));
	}

	/**
	 * 根据用户引入Cglib与否自动创建代理对象
	 *
	 * @param <T>    切面对象类型
	 * @param target 被代理对象
	 * @param aspect 切面实现
	 * @return 代理对象
	 */
	public static <T> T createProxy(T target, Aspect aspect) {
		return create().proxy(target, aspect);
	}

	/**
	 * 根据用户引入Cglib与否创建代理工厂
	 *
	 * @return 代理工厂
	 */
	public static ProxyFactory create() {
		return ServiceLoaderUtil.loadFirstAvailable(ProxyFactory.class);
	}

	/**
	 * 创建代理
	 *
	 * @param <T>         代理对象类型
	 * @param target      被代理对象
	 * @param aspectClass 切面实现类，自动实例化
	 * @return 代理对象
	 * @since 5.3.1
	 */
	public <T> T proxy(T target, Class<? extends Aspect> aspectClass) {
		return proxy(target, ReflectUtil.newInstanceIfPossible(aspectClass));
	}

	/**
	 * 创建代理
	 *
	 * @param <T>    代理对象类型
	 * @param target 被代理对象
	 * @param aspect 切面实现
	 * @return 代理对象
	 */
	public abstract <T> T proxy(T target, Aspect aspect);
}

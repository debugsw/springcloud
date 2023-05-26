package com.spring.cloud.aop.aspects;

import com.spring.cloud.base.utils.Console;

import java.lang.reflect.Method;

/**
 * @Author: ls
 * @Description: 通过日志打印方法的执行时间的切面
 * @Date: 2023/5/26 15:00
 */
public class TimeIntervalAspect extends SimpleAspect {
	private static final long serialVersionUID = 1L;

	private final TimeInterval interval = new TimeInterval();

	@Override
	public boolean before(Object target, Method method, Object[] args) {
		interval.start();
		return true;
	}

	@Override
	public boolean after(Object target, Method method, Object[] args, Object returnVal) {
		Console.log("Method [{}.{}] execute spend [{}]ms return value [{}]",
				target.getClass().getName(),
				method.getName(),
				interval.intervalMs(),
				returnVal);
		return true;
	}
}

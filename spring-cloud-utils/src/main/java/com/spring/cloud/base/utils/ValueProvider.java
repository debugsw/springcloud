package com.spring.cloud.base.utils;

import java.lang.reflect.Type;

/**
 * @Author: ls
 * @Description: 值提供者
 * @Date: 2023/4/13 16:11
 */
public interface ValueProvider<T>{

	/**
	 * 获取值<br>
	 * 返回值一般需要匹配被注入类型，如果不匹配会调用默认转换 Convert#convert(Type, Object)实现转换
	 *
	 * @param key Bean对象中参数名
	 * @param valueType 被注入的值的类型
	 * @return 对应参数名的值
	 */
	Object value(T key, Type valueType);

	/**
	 * 是否包含指定KEY，如果不包含则忽略注入<br>
	 * 此接口方法单独需要实现的意义在于：有些值提供者（比如Map）key是存在的，但是value为null，此时如果需要注入这个null，需要根据此方法判断
	 *
	 * @param key Bean对象中参数名
	 * @return 是否包含指定KEY
	 */
	boolean containsKey(T key);
}

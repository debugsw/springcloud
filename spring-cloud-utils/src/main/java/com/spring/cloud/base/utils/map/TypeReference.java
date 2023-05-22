package com.spring.cloud.base.utils.map;

import com.spring.cloud.base.utils.utils.TypeUtil;

import java.lang.reflect.Type;

/**
 * @Author: ls
 * @Description: Type类型参考
 * @Date: 2023/4/13 16:11
 */
public abstract class TypeReference<T> implements Type {

	/**
	 * 泛型参数
	 */
	private final Type type;

	/**
	 * 构造
	 */
	public TypeReference() {
		this.type = TypeUtil.getTypeArgument(getClass());
	}

	/**
	 * 获取用户定义的泛型参数
	 *
	 * @return 泛型参数
	 */
	public Type getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return this.type.toString();
	}
}

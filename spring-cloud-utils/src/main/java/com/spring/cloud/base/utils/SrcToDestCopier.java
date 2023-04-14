package com.spring.cloud.base.utils;

import java.io.Serializable;

/**
 * @Author: ls
 * @Description: 复制器抽象类
 * @Date: 2023/4/13 16:11
 */
public abstract class SrcToDestCopier<T, C extends SrcToDestCopier<T, C>> implements Copier<T>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 源
	 */
	protected T src;
	/**
	 * 目标
	 */
	protected T dest;
	/**
	 * 拷贝过滤器，可以过滤掉不需要拷贝的源
	 */
	protected Filter<T> copyFilter;

	//-------------------------------------------------------------------------------------------------------- Getters and Setters start

	/**
	 * 获取源
	 *
	 * @return 源
	 */
	public T getSrc() {
		return src;
	}

	/**
	 * 设置源
	 *
	 * @param src 源
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public C setSrc(T src) {
		this.src = src;
		return (C) this;
	}

	/**
	 * 获得目标
	 *
	 * @return 目标
	 */
	public T getDest() {
		return dest;
	}

	/**
	 * 设置目标
	 *
	 * @param dest 目标
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public C setDest(T dest) {
		this.dest = dest;
		return (C) this;
	}

	/**
	 * 获得过滤器
	 *
	 * @return 过滤器
	 */
	public Filter<T> getCopyFilter() {
		return copyFilter;
	}

	/**
	 * 设置过滤器
	 *
	 * @param copyFilter 过滤器
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public C setCopyFilter(Filter<T> copyFilter) {
		this.copyFilter = copyFilter;
		return (C) this;
	}
	//-------------------------------------------------------------------------------------------------------- Getters and Setters end
}

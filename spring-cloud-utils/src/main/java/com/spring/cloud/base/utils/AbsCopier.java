package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.crypto.ObjectUtil;

/**
 * @Author: ls
 * @Description: 抽象的对象拷贝封装
 * @Date: 2023/4/13 16:11
 */
public abstract class AbsCopier<S, T> implements Copier<T> {

	protected final S source;
	protected final T target;
	/**
	 * 拷贝选项
	 */
	protected final CopyOptions copyOptions;

	public AbsCopier(S source, T target, CopyOptions copyOptions) {
		this.source = source;
		this.target = target;
		this.copyOptions = ObjectUtil.defaultIfNull(copyOptions, CopyOptions::create);
	}
}

package com.spring.cloud.base.utils.list;

/**
 * @Author: ls
 * @Description: 片段默认实现
 * @Date: 2023/4/13 16:11
 */
public class DefaultSegment<T extends Number> implements Segment<T> {

	protected T startIndex;
	protected T endIndex;

	/**
	 * 构造
	 *
	 * @param startIndex 起始位置
	 * @param endIndex   结束位置
	 */
	public DefaultSegment(T startIndex, T endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	@Override
	public T getStartIndex() {
		return this.startIndex;
	}

	@Override
	public T getEndIndex() {
		return this.endIndex;
	}
}

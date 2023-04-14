package com.spring.cloud.base.utils.list;

import com.spring.cloud.base.utils.Assert;
import com.spring.cloud.base.utils.Convert;
import com.spring.cloud.base.utils.crypto.NumberUtil;

import java.lang.reflect.Type;

/**
 * @Author: ls
 * @Description: 片段表示，用于表示文本、集合等数据结构的一个区间
 * @Date: 2023/4/13 16:11
 */
public interface Segment<T extends Number> {

	/**
	 * 获取起始位置
	 *
	 * @return 起始位置
	 */
	T getStartIndex();

	/**
	 * 获取结束位置
	 *
	 * @return 结束位置
	 */
	T getEndIndex();

	/**
	 * 片段长度，默认计算方法为abs({@link #getEndIndex()} - {@link #getEndIndex()})
	 *
	 * @return 片段长度
	 */
	default T length() {
		final T start = Assert.notNull(getStartIndex(), "Start index must be not null!");
		final T end = Assert.notNull(getEndIndex(), "End index must be not null!");
		return Convert.convert((Type) start.getClass(), NumberUtil.sub(end, start).abs());
	}
}

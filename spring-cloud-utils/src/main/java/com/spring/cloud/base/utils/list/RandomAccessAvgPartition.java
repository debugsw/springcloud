package com.spring.cloud.base.utils.list;

import java.util.List;
import java.util.RandomAccess;

/**
 * @Author: ls
 * @Description: 列表分区或分段，可随机访问列表
 * @Date: 2023/4/13 16:11
 */
public class RandomAccessAvgPartition<T> extends AvgPartition<T> implements RandomAccess {

	/**
	 * 列表分区
	 *
	 * @param list  被分区的列表
	 * @param limit 分区个数
	 */
	public RandomAccessAvgPartition(List<T> list, int limit) {
		super(list, limit);
	}
}

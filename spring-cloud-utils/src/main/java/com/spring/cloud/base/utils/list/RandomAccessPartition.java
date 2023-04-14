package com.spring.cloud.base.utils.list;

import java.util.List;
import java.util.RandomAccess;

/**
 * @Author: ls
 * @Description: 列表分区或分段（可随机访问列表）
 * @Date: 2023/4/13 16:11
 */
public class RandomAccessPartition<T> extends Partition<T> implements RandomAccess {

	/**
	 * 构造
	 *
	 * @param list 被分区的列表，必须实现{@link RandomAccess}
	 * @param size 每个分区的长度
	 */
	public RandomAccessPartition(List<T> list, int size) {
		super(list, size);
	}
}

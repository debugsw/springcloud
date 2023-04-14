package com.spring.cloud.base.utils.list;

import com.spring.cloud.base.utils.Assert;

import java.util.AbstractList;
import java.util.List;

/**
 * @Author: ls
 * @Description: 列表分区或分段
 * @Date: 2023/4/13 16:11
 */
public class Partition<T> extends AbstractList<List<T>> {

	protected final List<T> list;
	protected final int size;

	/**
	 * 列表分区
	 *
	 * @param list 被分区的列表，非空
	 * @param size 每个分区的长度，必须&gt;0
	 */
	public Partition(List<T> list, int size) {
		this.list = Assert.notNull(list);
		this.size = Math.min(list.size(), size);
	}

	@Override
	public List<T> get(int index) {
		final int start = index * size;
		final int end = Math.min(start + size, list.size());
		return list.subList(start, end);
	}

	@Override
	public int size() {
		// 此处采用动态计算，以应对list变
		final int size = this.size;
		if (0 == size) {
			return 0;
		}

		final int total = list.size();
		// 类似于判断余数，当总数非整份size时，多余的数>=1，则相当于被除数多一个size，做到+1目的
		// 类似于：if(total % size > 0){length += 1;}
		return (total + size - 1) / size;
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}
}

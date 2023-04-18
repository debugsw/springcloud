package com.spring.cloud.base.utils.list;

import com.spring.cloud.base.utils.Assert;
import com.spring.cloud.base.utils.interf.Filter;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @Author: ls
 * @Description: 过滤元素输出
 * @Date: 2023/4/13 16:11
 */
public class FilterIter<E> implements Iterator<E> {

	private final Iterator<? extends E> iterator;
	private final Filter<? super E> filter;

	/**
	 * 下一个元素
	 */
	private E nextObject;
	/**
	 * 标记下一个元素是否被计算
	 */
	private boolean nextObjectSet = false;

	/**
	 * 构造
	 *
	 * @param iterator 被包装的{@link Iterator}
	 * @param filter   过滤函数，{@code null}表示不过滤
	 */
	public FilterIter(final Iterator<? extends E> iterator, final Filter<? super E> filter) {
		this.iterator = Assert.notNull(iterator);
		this.filter = filter;
	}

	@Override
	public boolean hasNext() {
		return nextObjectSet || setNextObject();
	}

	@Override
	public E next() {
		if (false == nextObjectSet && false == setNextObject()) {
			throw new NoSuchElementException();
		}
		nextObjectSet = false;
		return nextObject;
	}

	@Override
	public void remove() {
		if (nextObjectSet) {
			throw new IllegalStateException("remove() cannot be called");
		}
		iterator.remove();
	}

	/**
	 * 获取被包装的{@link Iterator}
	 *
	 * @return {@link Iterator}
	 */
	public Iterator<? extends E> getIterator() {
		return iterator;
	}

	/**
	 * 获取过滤函数
	 *
	 * @return 过滤函数，可能为{@code null}
	 */
	public Filter<? super E> getFilter() {
		return filter;
	}

	/**
	 * 设置下一个元素，如果存在返回{@code true}，否则{@code false}
	 */
	private boolean setNextObject() {
		while (iterator.hasNext()) {
			final E object = iterator.next();
			if (null == filter || filter.accept(object)) {
				nextObject = object;
				nextObjectSet = true;
				return true;
			}
		}
		return false;
	}

}

package com.spring.cloud.base.utils.crypto;

import com.spring.cloud.base.utils.Assert;

import java.util.Iterator;
import java.util.function.Function;

/**
 * @Author: ls
 * @Description: 使用给定的转换函数
 * @Date: 2023/4/13 15:56
 */
public class TransIter<F, T> implements Iterator<T> {

	private final Iterator<? extends F> backingIterator;
	private final Function<? super F, ? extends T> func;

	/**
	 * 构造
	 *
	 * @param backingIterator 源{@link Iterator}
	 * @param func            转换函数
	 */
	public TransIter(final Iterator<? extends F> backingIterator, final Function<? super F, ? extends T> func) {
		this.backingIterator = Assert.notNull(backingIterator);
		this.func = Assert.notNull(func);
	}

	@Override
	public final boolean hasNext() {
		return backingIterator.hasNext();
	}

	@Override
	public final T next() {
		return func.apply(backingIterator.next());
	}

	@Override
	public final void remove() {
		backingIterator.remove();
	}
}

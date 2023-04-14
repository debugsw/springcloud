package com.spring.cloud.base.utils.list;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/13 16:11
 */
public class NullComparator<T> implements Comparator<T>, Serializable {
	private static final long serialVersionUID = 1L;

	protected final boolean nullGreater;
	protected final Comparator<T> comparator;

	/**
	 * 构造
	 *
	 * @param nullGreater 是否{@code null}最大，排在最后
	 * @param comparator  实际比较器
	 */
	@SuppressWarnings("unchecked")
	public NullComparator(boolean nullGreater, Comparator<? super T> comparator) {
		this.nullGreater = nullGreater;
		this.comparator = (Comparator<T>) comparator;
	}

	@Override
	public int compare(T a, T b) {
		if (a == b) {
			return 0;
		}
		if (a == null) {
			return nullGreater ? 1 : -1;
		} else if (b == null) {
			return nullGreater ? -1 : 1;
		} else {
			return doCompare(a, b);
		}
	}

	@Override
	public Comparator<T> thenComparing(Comparator<? super T> other) {
		Objects.requireNonNull(other);
		return new NullComparator<>(nullGreater, comparator == null ? other : comparator.thenComparing(other));
	}

	/**
	 * 不检查{@code null}的比较方法<br>
	 * 用户可自行重写此方法自定义比较方式
	 *
	 * @param a A值
	 * @param b B值
	 * @return 比较结果，-1:a小于b，0:相等，1:a大于b
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	protected int doCompare(T a, T b) {
		if (null == comparator) {
			if (a instanceof Comparable && b instanceof Comparable) {
				return ((Comparable) a).compareTo(b);
			}
			return 0;
		}

		return comparator.compare(a, b);
	}
}

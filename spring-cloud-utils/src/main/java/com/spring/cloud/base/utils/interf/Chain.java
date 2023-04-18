package com.spring.cloud.base.utils.interf;

/**
 * @Author: ls
 * @Description: 责任链接口
 * @Date: 2023/4/13 16:11
 */
public interface Chain<E, T> extends Iterable<E> {
	/**
	 * 加入责任链
	 *
	 * @param element 责任链新的环节元素
	 * @return this
	 */
	T addChain(E element);
}

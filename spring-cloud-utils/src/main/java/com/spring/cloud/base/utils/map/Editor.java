package com.spring.cloud.base.utils.map;

/**
 * @Author: ls
 * @Description: 编辑器接口，常用于对于集合中的元素做统一编辑
 * @Date: 2023/4/13 16:11
 */
@FunctionalInterface
public interface Editor<T> {
	/**
	 * 修改过滤后的结果
	 *
	 * @param t 被过滤的对象
	 * @return 修改后的对象，如果被过滤返回{@code null}
	 */
	T edit(T t);
}

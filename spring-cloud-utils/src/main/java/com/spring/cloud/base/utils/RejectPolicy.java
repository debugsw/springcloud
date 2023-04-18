package com.spring.cloud.base.utils;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: ls
 * @Description: 线程拒绝策略枚举
 * @Date: 2023/4/13 16:11
 */
public enum RejectPolicy {

	/**
	 * 处理程序遭到拒绝将抛出RejectedExecutionException
	 */
	ABORT(new ThreadPoolExecutor.AbortPolicy()),
	/**
	 * 放弃当前任务
	 */
	DISCARD(new ThreadPoolExecutor.DiscardPolicy()),
	/**
	 * 如果执行程序尚未关闭，则位于工作队列头部的任务将被删除，然后重试执行程序（如果再次失败，则重复此过程）
	 */
	DISCARD_OLDEST(new ThreadPoolExecutor.DiscardOldestPolicy()),
	/**
	 * 由主线程来直接执行
	 */
	CALLER_RUNS(new ThreadPoolExecutor.CallerRunsPolicy());

	private final RejectedExecutionHandler value;

	RejectPolicy(RejectedExecutionHandler handler) {
		this.value = handler;
	}

	/**
	 * 获取RejectedExecutionHandler枚举值
	 *
	 * @return RejectedExecutionHandler
	 */
	public RejectedExecutionHandler getValue() {
		return this.value;
	}
}

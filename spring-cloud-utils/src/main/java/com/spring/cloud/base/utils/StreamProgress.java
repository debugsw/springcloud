package com.spring.cloud.base.utils;

/**
 * @Author: ls
 * @Description: Stream进度条
 * @Date: 2023/4/13 16:11
 */
public interface StreamProgress {

	/**
	 * 开始
	 */
	void start();

	/**
	 * 进行中
	 *
	 * @param total        总大小，如果未知为 -1或者{@link Long#MAX_VALUE}
	 * @param progressSize 已经进行的大小
	 */
	void progress(long total, long progressSize);

	/**
	 * 结束
	 */
	void finish();
}

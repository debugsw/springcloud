package com.spring.cloud.aop.aspects;

import com.spring.cloud.base.utils.str.StrUtil;

/**
 * @Author: ls
 * @Description: 计时器
 * @Date: 2023/5/26 15:00
 */
public class TimeInterval extends GroupTimeInterval {
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_ID = StrUtil.EMPTY;

	public TimeInterval() {
		this(false);
	}

	public TimeInterval(boolean isNano) {
		super(isNano);
		start();
	}

	public long start() {
		return start(DEFAULT_ID);
	}

	public long intervalRestart() {
		return intervalRestart(DEFAULT_ID);
	}

	public TimeInterval restart() {
		start(DEFAULT_ID);
		return this;
	}

	public long interval() {
		return interval(DEFAULT_ID);
	}

	public long intervalMs() {
		return intervalMs(DEFAULT_ID);
	}

	public long intervalSecond() {
		return intervalSecond(DEFAULT_ID);
	}

	public long intervalMinute() {
		return intervalMinute(DEFAULT_ID);
	}

	public long intervalHour() {
		return intervalHour(DEFAULT_ID);
	}

	public long intervalDay() {
		return intervalDay(DEFAULT_ID);
	}

	public long intervalWeek() {
		return intervalWeek(DEFAULT_ID);
	}
}

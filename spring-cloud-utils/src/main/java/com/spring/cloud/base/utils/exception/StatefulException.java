package com.spring.cloud.base.utils.exception;


import com.spring.cloud.base.utils.crypto.StrUtil;

/**
 * @Author: ls
 * @Description: 带有状态码的异常
 * @Date: 2023/4/13 16:11
 */
public class StatefulException extends RuntimeException {
	private static final long serialVersionUID = 6057602589533840889L;

	// 异常状态码
	private int status;

	public StatefulException() {
	}

	public StatefulException(String msg) {
		super(msg);
	}

	public StatefulException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public StatefulException(Throwable throwable) {
		super(throwable);
	}

	public StatefulException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public StatefulException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public StatefulException(int status, String msg) {
		super(msg);
		this.status = status;
	}

	public StatefulException(int status, Throwable throwable) {
		super(throwable);
		this.status = status;
	}

	public StatefulException(int status, String msg, Throwable throwable) {
		super(msg, throwable);
		this.status = status;
	}

	/**
	 * @return 获得异常状态码
	 */
	public int getStatus() {
		return status;
	}
}

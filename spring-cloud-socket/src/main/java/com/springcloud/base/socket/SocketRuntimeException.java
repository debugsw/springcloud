package com.springcloud.base.socket;

import com.spring.cloud.base.utils.exception.ExceptionUtil;
import com.spring.cloud.base.utils.str.StrUtil;

/**
 * @Author: ls
 * @Description: Socket异常
 * @Date: 2023/5/6 10:54
 */
public class SocketRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public SocketRuntimeException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public SocketRuntimeException(String message) {
		super(message);
	}
	public SocketRuntimeException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}
	public SocketRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}
	public SocketRuntimeException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}
	public SocketRuntimeException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}

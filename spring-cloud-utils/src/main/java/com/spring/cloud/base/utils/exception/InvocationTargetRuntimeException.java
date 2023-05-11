package com.spring.cloud.base.utils.exception;

/**
 * @Author: ls
 * @Description: InvocationTargetException的运行时异常
 * @Date: 2023/4/13 16:11
 */
public class InvocationTargetRuntimeException extends UtilException {

	private static final long serialVersionUID = 7956412770860499420L;

	public InvocationTargetRuntimeException(Throwable e) {
		super(e);
	}

	public InvocationTargetRuntimeException(String message) {
		super(message);
	}

	public InvocationTargetRuntimeException(String messageTemplate, Object... params) {
		super(messageTemplate, params);
	}

	public InvocationTargetRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public InvocationTargetRuntimeException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public InvocationTargetRuntimeException(Throwable throwable, String messageTemplate, Object... params) {
		super(throwable, messageTemplate, params);
	}
}

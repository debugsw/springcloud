package com.springcloud.base.log;

import com.spring.cloud.base.utils.str.StrUtil;
import com.springcloud.base.log.enums.Levels;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/25 10:02
 */
public class JdkLog extends AbstractLog {

	private static final long serialVersionUID = -1574848566294219932L;

	private final transient Logger logger;

	public JdkLog(Logger logger) {
		this.logger = logger;
	}

	public JdkLog(Class<?> clazz) {
		this((null == clazz) ? StrUtil.NULL : clazz.getName());
	}

	public JdkLog(String name) {
		this(Logger.getLogger(name));
	}

	@Override
	public String getName() {
		return logger.getName();
	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isLoggable(Level.FINEST);
	}

	@Override
	public void trace(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.FINEST, t, format, arguments);
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isLoggable(Level.FINE);
	}

	@Override
	public void debug(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.FINE, t, format, arguments);
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isLoggable(INFO);
	}

	@Override
	public void info(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, INFO, t, format, arguments);
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isLoggable(Level.WARNING);
	}

	@Override
	public void warn(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.WARNING, t, format, arguments);
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isLoggable(Level.SEVERE);
	}

	@Override
	public void error(String fqcn, Throwable t, String format, Object... arguments) {
		logIfEnabled(fqcn, Level.SEVERE, t, format, arguments);
	}

	@Override
	public void log(String fqcn, Levels level, Throwable t, String format, Object... arguments) {
		Level jdkLevel;
		switch (level) {
			case TRACE:
				jdkLevel = Level.FINEST;
				break;
			case DEBUG:
				jdkLevel = Level.FINE;
				break;
			case INFO:
				jdkLevel = INFO;
				break;
			case WARN:
				jdkLevel = Level.WARNING;
				break;
			case ERROR:
				jdkLevel = Level.SEVERE;
				break;
			default:
				throw new Error(StrUtil.format("Can not identify level: {}", level));
		}
		logIfEnabled(fqcn, jdkLevel, t, format, arguments);
	}

	/**
	 * 打印对应等级的日志
	 *
	 * @param callerFQCN 调用者的完全限定类名(Fully Qualified Class Name)
	 * @param level      等级
	 * @param throwable  异常对象
	 * @param format     消息模板
	 * @param arguments  参数
	 */
	private void logIfEnabled(String callerFQCN, Level level, Throwable throwable, String format, Object[] arguments) {
		if (logger.isLoggable(level)) {
			LogRecord record = new LogRecord(level, StrUtil.format(format, arguments));
			record.setLoggerName(getName());
			record.setThrown(throwable);
			fillCallerData(callerFQCN, record);
			logger.log(record);
		}
	}

	/**
	 * 传入调用日志类的信息
	 *
	 * @param callerFQCN 调用者全限定类名
	 * @param record     The record to update
	 */
	private static void fillCallerData(String callerFQCN, LogRecord record) {
		StackTraceElement[] steArray = Thread.currentThread().getStackTrace();

		int found = -1;
		String className;
		for (int i = steArray.length - 2; i > -1; i--) {
			className = steArray[i].getClassName();
			if (callerFQCN.equals(className)) {
				found = i;
				break;
			}
		}

		if (found > -1) {
			StackTraceElement ste = steArray[found + 1];
			record.setSourceClassName(ste.getClassName());
			record.setSourceMethodName(ste.getMethodName());
		}
	}
}

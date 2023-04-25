package com.springcloud.base.log;

import com.spring.cloud.base.utils.exception.ExceptionUtil;
import com.spring.cloud.base.utils.str.StrUtil;
import com.springcloud.base.log.enums.Levels;

import java.io.Serializable;

/**
 * @Author: ls
 * @Description: 抽象日志类
 * @Date: 2023/4/25 10:03
 */
public abstract class AbstractLog implements Log, Serializable {

	private static final String FQCN = AbstractLog.class.getName();

	private static final long serialVersionUID = -7248919862022232526L;

	@Override
	public boolean isEnabled(Levels levels) {
		switch (levels) {
			case TRACE:
				return isTraceEnabled();
			case DEBUG:
				return isDebugEnabled();
			case INFO:
				return isInfoEnabled();
			case WARN:
				return isWarnEnabled();
			case ERROR:
				return isErrorEnabled();
			default:
				throw new Error(StrUtil.format("Can not identify level: {}", levels));
		}
	}

	@Override
	public void trace(Throwable t) {
		trace(t, ExceptionUtil.getSimpleMessage(t));
	}

	@Override
	public void trace(String format, Object... arguments) {
		trace(null, format, arguments);
	}

	@Override
	public void trace(Throwable t, String format, Object... arguments) {
		trace(FQCN, t, format, arguments);
	}

	@Override
	public void debug(Throwable t) {
		debug(t, ExceptionUtil.getSimpleMessage(t));
	}

	@Override
	public void debug(String format, Object... arguments) {
		if (null != arguments && 1 == arguments.length && arguments[0] instanceof Throwable) {
			debug((Throwable) arguments[0], format);
		} else {
			debug(null, format, arguments);
		}
	}

	@Override
	public void debug(Throwable t, String format, Object... arguments) {
		debug(FQCN, t, format, arguments);
	}

	@Override
	public void info(Throwable t) {
		info(t, ExceptionUtil.getSimpleMessage(t));
	}

	@Override
	public void info(String format, Object... arguments) {
		if (null != arguments && 1 == arguments.length && arguments[0] instanceof Throwable) {
			info((Throwable) arguments[0], format);
		} else {
			info(null, format, arguments);
		}
	}

	@Override
	public void info(Throwable t, String format, Object... arguments) {
		info(FQCN, t, format, arguments);
	}

	@Override
	public void warn(Throwable t) {
		warn(t, ExceptionUtil.getSimpleMessage(t));
	}

	@Override
	public void warn(String format, Object... arguments) {
		if (null != arguments && 1 == arguments.length && arguments[0] instanceof Throwable) {
			warn((Throwable) arguments[0], format);
		} else {
			warn(null, format, arguments);
		}
	}

	@Override
	public void warn(Throwable t, String format, Object... arguments) {
		warn(FQCN, t, format, arguments);
	}

	@Override
	public void error(Throwable t) {
		this.error(t, ExceptionUtil.getSimpleMessage(t));
	}

	@Override
	public void error(String format, Object... arguments) {
		if (null != arguments && 1 == arguments.length && arguments[0] instanceof Throwable) {
			error((Throwable) arguments[0], format);
		} else {
			error(null, format, arguments);
		}
	}

	@Override
	public void error(Throwable t, String format, Object... arguments) {
		error(FQCN, t, format, arguments);
	}

	@Override
	public void log(Levels levels, String format, Object... arguments) {
		if (null != arguments && 1 == arguments.length && arguments[0] instanceof Throwable) {
			log(levels, (Throwable) arguments[0], format);
		} else {
			log(levels, null, format, arguments);
		}
	}

	@Override
	public void log(Levels levels, Throwable t, String format, Object... arguments) {
		this.log(FQCN, levels, t, format, arguments);
	}
}

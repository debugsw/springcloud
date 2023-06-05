package com.springcloud.base.log;


import com.spring.cloud.base.utils.Assert;
import com.spring.cloud.base.utils.Console;
import com.spring.cloud.base.utils.date.DateUtil;
import com.spring.cloud.base.utils.str.StrUtil;
import com.springcloud.base.log.enums.Levels;
import com.springcloud.base.log.utils.Dict;

/**
 * @Author: ls
 * @Description: 利用System.out.println()打印日志
 * @Date: 2023/4/25 10:03
 */
public class ConsoleLog extends AbstractLog {

	private static final long serialVersionUID = -6843151523380063975L;

	private static final String logFormat = "[{date}] [{level}] {name}: {msg}";
	private static Levels currentLevels = Levels.DEBUG;

	private final String name;

	/**
	 * 构造
	 *
	 * @param clazz 类
	 */
	public ConsoleLog(Class<?> clazz) {
		this.name = (null == clazz) ? StrUtil.NULL : clazz.getName();
	}

	/**
	 * 构造
	 *
	 * @param name 类名
	 */
	public ConsoleLog(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * 设置自定义的日志显示级别
	 *
	 * @param customLevels 自定义级别
	 */
	public static void setLevel(Levels customLevels) {
		Assert.notNull(customLevels);
		currentLevels = customLevels;
	}

	@Override
	public boolean isTraceEnabled() {
		return isEnabled(Levels.TRACE);
	}

	@Override
	public void trace(String fqcn, Throwable t, String format, Object... arguments) {
		log(fqcn, Levels.TRACE, t, format, arguments);
	}

	@Override
	public boolean isDebugEnabled() {
		return isEnabled(Levels.DEBUG);
	}

	@Override
	public void debug(String fqcn, Throwable t, String format, Object... arguments) {
		log(fqcn, Levels.DEBUG, t, format, arguments);
	}

	@Override
	public boolean isInfoEnabled() {
		return isEnabled(Levels.INFO);
	}

	@Override
	public void info(String fqcn, Throwable t, String format, Object... arguments) {
		log(fqcn, Levels.INFO, t, format, arguments);
	}

	@Override
	public boolean isWarnEnabled() {
		return isEnabled(Levels.WARN);
	}

	@Override
	public void warn(String fqcn, Throwable t, String format, Object... arguments) {
		log(fqcn, Levels.WARN, t, format, arguments);
	}

	@Override
	public boolean isErrorEnabled() {
		return isEnabled(Levels.ERROR);
	}

	@Override
	public void error(String fqcn, Throwable t, String format, Object... arguments) {
		log(fqcn, Levels.ERROR, t, format, arguments);
	}

	@Override
	public void log(String fqcn, Levels levels, Throwable t, String format, Object... arguments) {
		// fqcn 无效
		if (!isEnabled(levels)) {
			return;
		}


		final Dict dict = Dict.create()
				.set("date", DateUtil.now())
				.set("level", levels.toString())
				.set("name", this.name)
				.set("msg", StrUtil.format(format, arguments));

		final String logMsg = StrUtil.format(logFormat, dict);

		if (levels.ordinal() >= Levels.WARN.ordinal()) {
			Console.error(t, logMsg);
		} else {
			Console.log(t, logMsg);
		}
	}

	@Override
	public boolean isEnabled(Levels levels) {
		return currentLevels.compareTo(levels) <= 0;
	}
}

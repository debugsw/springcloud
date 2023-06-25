package com.springcloud.base.log;

import com.springcloud.base.log.enums.Levels;
import com.springcloud.base.log.levels.*;
import com.springcloud.base.log.utils.CallerUtil;

/**
 * @Author: ls
 * @Description: 日志统一接口
 * @Date: 2023/4/25 10:03
 */
public interface Log extends TraceLog, DebugLog, InfoLog, WarnLog, ErrorLog {

	/**
	 * 获得Log
	 *
	 * @param clazz 日志发出的类
	 * @return Log
	 */
	static Log get(Class<?> clazz) {
		return LogFactory.get(clazz);
	}

	/**
	 * 获得Log
	 *
	 * @param name 自定义的日志发出者名称
	 * @return Log
	 */
	static Log get(String name) {
		return LogFactory.get(name);
	}

	/**
	 * @return 获得日志，自动判定日志发出者
	 */
	static Log get() {
		return LogFactory.get(CallerUtil.getCallerCaller());
	}

	/**
	 * @return 日志对象的Name
	 */
	String getName();

	/**
	 * 是否开启指定日志
	 *
	 * @param levels 日志级别
	 * @return 是否开启指定级别
	 */
	boolean isEnabled(Levels levels);

	/**
	 * 打印指定级别的日志
	 *
	 * @param levels     级别
	 * @param format    消息模板
	 * @param arguments 参数
	 */
	void log(Levels levels, String format, Object... arguments);

	/**
	 * 打印 指定级别的日志
	 *
	 * @param levels     级别
	 * @param t         错误对象
	 * @param format    消息模板
	 * @param arguments 参数
	 */
	void log(Levels levels, Throwable t, String format, Object... arguments);

	/**
	 * 打印 ERROR 等级的日志
	 *
	 * @param fqcn      完全限定类名(Fully Qualified Class Name)，用于定位日志位置
	 * @param levels     级别
	 * @param t         错误对象
	 * @param format    消息模板
	 * @param arguments 参数
	 */
	void log(String fqcn, Levels levels, Throwable t, String format, Object... arguments);
}

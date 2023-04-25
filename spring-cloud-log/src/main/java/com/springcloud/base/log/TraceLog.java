package com.springcloud.base.log;

/**
 * @Author: ls
 * @Description: TRACE级别日志接口
 * @Date: 2023/4/25 10:03
 */
public interface TraceLog {
	/**
	 * 等级是否开启
	 *
	 * @return TRACE
	 */
	boolean isTraceEnabled();

	/**
	 * 打印 TRACE 等级的日志
	 *
	 * @param t 错误对象
	 */
	void trace(Throwable t);

	/**
	 * 打印 TRACE 等级的日志
	 *
	 * @param format    消息模板
	 * @param arguments 参数
	 */
	void trace(String format, Object... arguments);

	/**
	 * 打印 TRACE 等级的日志
	 *
	 * @param t         错误对象
	 * @param format    消息模板
	 * @param arguments 参数
	 */
	void trace(Throwable t, String format, Object... arguments);

	/**
	 * 打印 TRACE 等级的日志
	 *
	 * @param fqcn      完全限定类名(Fully Qualified Class Name)，用于定位日志位置
	 * @param t         错误对象
	 * @param format    消息模板
	 * @param arguments 参数
	 */
	void trace(String fqcn, Throwable t, String format, Object... arguments);
}

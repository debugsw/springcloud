package com.springcloud.base.log.levels;

/**
 * @Author: ls
 * @Description: DEBUG级别日志接口
 * @Date: 2023/4/25 10:03
 */
public interface DebugLog {
	/**
	 * 等级是否开启
	 *
	 * @return DEBUG
	 */
	boolean isDebugEnabled();

	/**
	 * 打印 DEBUG 等级的日志
	 *
	 * @param t 错误对象
	 */
	void debug(Throwable t);

	/**
	 * 打印 DEBUG 等级的日志
	 *
	 * @param format    消息模板
	 * @param arguments 参数
	 */
	void debug(String format, Object... arguments);

	/**
	 * 打印 DEBUG 等级的日志
	 *
	 * @param t         错误对象
	 * @param format    消息模板
	 * @param arguments 参数
	 */
	void debug(Throwable t, String format, Object... arguments);

	/**
	 * 打印 DEBUG 等级的日志
	 *
	 * @param fqcn      完全限定类名(Fully Qualified Class Name)，用于定位日志位置
	 * @param t         错误对象
	 * @param format    消息模板
	 * @param arguments 参数
	 */
	void debug(String fqcn, Throwable t, String format, Object... arguments);
}

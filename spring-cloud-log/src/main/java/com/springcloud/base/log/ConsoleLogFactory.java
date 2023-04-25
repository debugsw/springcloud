package com.springcloud.base.log;

/**
 * @Author: ls
 * @Description: 利用System.out.println()打印日志
 * @Date: 2023/4/25 10:03
 */
public class ConsoleLogFactory extends LogFactory {

	public ConsoleLogFactory() {
		super("SpringCloud Console Logging");
	}

	@Override
	public Log createLog(String name) {
		return new ConsoleLog(name);
	}

	@Override
	public Log createLog(Class<?> clazz) {
		return new ConsoleLog(clazz);
	}

}

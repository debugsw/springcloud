package com.springcloud.base.log.log4j2;

import com.springcloud.base.log.Log;
import com.springcloud.base.log.LogFactory;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/25 10:03
 */
public class Log4j2LogFactory extends LogFactory {

	public Log4j2LogFactory() {
		super("Log4j2");
		checkLogExist(org.apache.logging.log4j.LogManager.class);
	}

	@Override
	public Log createLog(String name) {
		return new Log4j2Log(name);
	}

	@Override
	public Log createLog(Class<?> clazz) {
		return new Log4j2Log(clazz);
	}

}

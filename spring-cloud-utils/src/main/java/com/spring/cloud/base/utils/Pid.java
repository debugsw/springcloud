package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.exception.UtilException;
import com.spring.cloud.base.utils.str.StrUtil;

import java.lang.management.ManagementFactory;

/**
 * @Author: ls
 * @Description: 进程ID单例封装
 * @Date: 2023/4/13 16:11
 */
public enum Pid {
	INSTANCE;

	private final int pid;

	Pid() {
		this.pid = getPid();
	}

	/**
	 * 获取PID值
	 *
	 * @return pid
	 */
	public int get() {
		return this.pid;
	}

	/**
	 * 获取当前进程ID，首先获取进程名称，读取@前的ID值，如果不存在，则读取进程名的hash值
	 *
	 * @return 进程ID
	 * @throws UtilException 进程名称为空
	 */
	private static int getPid() throws UtilException {
		final String processName = ManagementFactory.getRuntimeMXBean().getName();
		if (StrUtil.isBlank(processName)) {
			throw new UtilException("Process name is blank!");
		}
		final int atIndex = processName.indexOf('@');
		if (atIndex > 0) {
			return Integer.parseInt(processName.substring(0, atIndex));
		} else {
			return processName.hashCode();
		}
	}
}

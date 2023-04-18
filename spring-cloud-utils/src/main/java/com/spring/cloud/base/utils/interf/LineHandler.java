package com.spring.cloud.base.utils.interf;

/**
 * @Author: ls
 * @Description: 行处理器
 * @Date: 2023/4/13 16:11
 */
@FunctionalInterface
public interface LineHandler {
	/**
	 * 处理一行数据，可以编辑后存入指定地方
	 * @param line 行
	 */
	void handle(String line);
}

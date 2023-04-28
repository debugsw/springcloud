package com.spring.cloud.base.http;

import java.io.IOException;

/**
 * @Author: ls
 * @Description: 请求处理接口
 * @Date: 2023/4/26 15:00
 */
@FunctionalInterface
public interface Action {

	/**
	 * 处理请求
	 *
	 * @param request  请求对象
	 * @param response 响应对象
	 * @throws IOException IO异常
	 */
	void doAction(HttpServerRequest request, HttpServerResponse response) throws IOException;
}

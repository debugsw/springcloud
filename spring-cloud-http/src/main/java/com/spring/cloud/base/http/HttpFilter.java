package com.spring.cloud.base.http;

import com.sun.net.httpserver.Filter;

import java.io.IOException;

/**
 * @Author: ls
 * @Description: 过滤器接口
 * @Date: 2023/4/26 15:00
 */
@FunctionalInterface
public interface HttpFilter {

	/**
	 * 执行过滤
	 *
	 * @param req   {@link HttpServerRequest} 请求对象，用于获取请求内容
	 * @param res   {@link HttpServerResponse} 响应对象，用于写出内容
	 * @param chain {@link Filter.Chain}
	 * @throws IOException IO异常
	 */
	void doFilter(HttpServerRequest req, HttpServerResponse res, Filter.Chain chain) throws IOException;
}

package com.spring.cloud.base.http;

import com.spring.cloud.base.utils.utils.CharsetUtil;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;

import java.io.Closeable;
import java.nio.charset.Charset;

/**
 * @Author: ls
 * @Description: HttpServer公用对象
 * @Date: 2023/4/26 15:00
 */
public class HttpServerBase implements Closeable {

	final static Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;

	final HttpExchange httpExchange;

	/**
	 * 构造
	 *
	 * @param httpExchange {@link HttpExchange}
	 */
	public HttpServerBase(HttpExchange httpExchange) {
		this.httpExchange = httpExchange;
	}

	/**
	 * 获取{@link HttpExchange}对象
	 *
	 * @return {@link HttpExchange}对象
	 */
	public HttpExchange getHttpExchange() {
		return this.httpExchange;
	}

	/**
	 * 获取{@link HttpContext}
	 *
	 * @return {@link HttpContext}
	 */
	public HttpContext getHttpContext() {
		return getHttpExchange().getHttpContext();
	}

	/**
	 * 调用{@link HttpExchange#close()}，关闭请求流和响应流
	 */
	@Override
	public void close() {
		this.httpExchange.close();
	}
}

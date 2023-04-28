package com.spring.cloud.base.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * @Author: ls
 * @Description: Action处理器
 * @Date: 2023/4/26 15:00
 */
public class ActionHandler implements HttpHandler {

	private final Action action;

	/**
	 * 构造
	 *
	 * @param action Action
	 */
	public ActionHandler(Action action) {
		this.action = action;
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		action.doAction(
				new HttpServerRequest(httpExchange),
				new HttpServerResponse(httpExchange)
		);
		httpExchange.close();
	}
}

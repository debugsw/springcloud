package com.springcloud.base.socket.aio;

import com.springcloud.base.socket.SocketRuntimeException;

import java.nio.channels.CompletionHandler;

/**
 * @Author: ls
 * @Description: 数据读取完成回调调用Session中相应方法处理消息
 * @Date: 2023/5/6 10:54
 */
public class ReadHandler implements CompletionHandler<Integer, AioSession> {

	@Override
	public void completed(Integer result, AioSession session) {
		session.callbackRead();
	}

	@Override
	public void failed(Throwable exc, AioSession session) {
		throw new SocketRuntimeException(exc);
	}

}

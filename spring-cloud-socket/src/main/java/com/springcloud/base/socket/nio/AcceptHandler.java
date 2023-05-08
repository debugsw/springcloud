package com.springcloud.base.socket.nio;

import com.spring.cloud.base.utils.exception.IORuntimeException;
import com.springcloud.base.log.StaticLog;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author: ls
 * @Description: 接入完成回调单例使用
 * @Date: 2023/5/6 10:54
 */
public class AcceptHandler implements CompletionHandler<ServerSocketChannel, NioServer> {

	@Override
	public void completed(ServerSocketChannel serverSocketChannel, NioServer nioServer) {
		SocketChannel socketChannel;
		try {
			// 获取连接到此服务器的客户端通道
			socketChannel = serverSocketChannel.accept();
			StaticLog.debug("Client [{}] accepted.", socketChannel.getRemoteAddress());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}

		// SocketChannel通道的可读事件注册到Selector中
		NioUtil.registerChannel(nioServer.getSelector(), socketChannel, Operation.READ);
	}

	@Override
	public void failed(Throwable exc, NioServer nioServer) {
		StaticLog.error(exc);
	}

}

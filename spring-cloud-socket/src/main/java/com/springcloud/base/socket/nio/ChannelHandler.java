package com.springcloud.base.socket.nio;

import java.nio.channels.SocketChannel;

/**
 * @Author: ls
 * @Description: NIO数据处理接口通过实现此接口
 * @Date: 2023/5/6 10:54
 */
@FunctionalInterface
public interface ChannelHandler {

	/**
	 * 处理NIO数据
	 *
	 * @param socketChannel {@link SocketChannel}
	 * @throws Exception 可能的处理异常
	 */
	void handle(SocketChannel socketChannel) throws Exception;
}

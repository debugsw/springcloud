package com.springcloud.base.socket.nio;

import com.spring.cloud.base.utils.exception.IORuntimeException;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;

/**
 * @Author: ls
 * @Description: NIO工具类
 * @Date: 2023/5/6 10:54
 */
public class NioUtil {

	/**
	 * 注册通道的指定操作到指定Selector上
	 *
	 * @param selector Selector
	 * @param channel  通道
	 * @param ops      注册的通道监听（操作）类型
	 */
	public static void registerChannel(Selector selector, SelectableChannel channel, Operation ops) {
		if (channel == null) {
			return;
		}

		try {
			channel.configureBlocking(false);
			// 注册通道
			//noinspection MagicConstant
			channel.register(selector, ops.getValue());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
}

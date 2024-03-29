package com.springcloud.base.socket;

import com.spring.cloud.base.utils.exception.IORuntimeException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ClosedChannelException;

/**
 * @Author: ls
 * @Description: Socket相关工具类
 * @Date: 2023/5/6 10:54
 */
public class SocketUtil {

	/**
	 * 获取远程端的地址信息，包括host和端口
	 * null表示channel为null或者远程主机未连接
	 *
	 * @param channel
	 * @return 远程端的地址信息，包括host和端口，null表示channel为null或者远程主机未连接
	 * @throws IORuntimeException IO异常
	 */
	public static SocketAddress getRemoteAddress(AsynchronousSocketChannel channel) throws IORuntimeException {
		try {
			return (null == channel) ? null : channel.getRemoteAddress();
		} catch (ClosedChannelException e) {
			return null;
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 远程主机是否处于连接状态<br>
	 * 通过判断远程地址获取成功与否判断
	 *
	 * @param channel {@link AsynchronousSocketChannel}
	 * @return 远程主机是否处于连接状态
	 * @throws IORuntimeException IO异常
	 */
	public static boolean isConnected(AsynchronousSocketChannel channel) throws IORuntimeException {
		return null != getRemoteAddress(channel);
	}

	/**
	 * 创建Socket并连接到指定地址的服务器
	 *
	 * @param hostname 地址
	 * @param port     端口
	 * @return {@link Socket}
	 * @throws IORuntimeException IO异常
	 */
	public static Socket connect(String hostname, int port) throws IORuntimeException {
		return connect(hostname, port, -1);
	}

	/**
	 * 创建Socket并连接到指定地址的服务器
	 *
	 * @param hostname          地址
	 * @param port              端口
	 * @param connectionTimeout 连接超时
	 * @return {@link Socket}
	 * @throws IORuntimeException IO异常
	 */
	public static Socket connect(final String hostname, int port, int connectionTimeout) throws IORuntimeException {
		return connect(new InetSocketAddress(hostname, port), connectionTimeout);
	}

	/**
	 * 创建Socket并连接到指定地址的服务器
	 *
	 * @param address           地址
	 * @param connectionTimeout 连接超时
	 * @return {@link Socket}
	 * @throws IORuntimeException IO异常
	 */
	public static Socket connect(InetSocketAddress address, int connectionTimeout) throws IORuntimeException {
		final Socket socket = new Socket();
		try {
			if (connectionTimeout <= 0) {
				socket.connect(address);
			} else {
				socket.connect(address, connectionTimeout);
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return socket;
	}
}

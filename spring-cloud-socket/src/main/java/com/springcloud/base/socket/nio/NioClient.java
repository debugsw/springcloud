package com.springcloud.base.socket.nio;

import com.spring.cloud.base.utils.utils.IoUtil;
import com.spring.cloud.base.utils.exception.IORuntimeException;
import com.springcloud.base.log.Log;
import com.springcloud.base.socket.SocketRuntimeException;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Author: ls
 * @Description: NIO客户端
 * @Date: 2023/5/6 10:54
 */
public class NioClient implements Closeable {

	private static final Log log = Log.get();

	private Selector selector;
	private SocketChannel channel;
	private ChannelHandler handler;

	/**
	 * 构造
	 *
	 * @param host 服务器地址
	 * @param port 端口
	 */
	public NioClient(String host, int port) {
		init(new InetSocketAddress(host, port));
	}

	/**
	 * 构造
	 *
	 * @param address 服务器地址
	 */
	public NioClient(InetSocketAddress address) {
		init(address);
	}

	/**
	 * 初始化
	 *
	 * @param address 地址和端口
	 * @return this
	 */
	public NioClient init(InetSocketAddress address) {
		try {

			this.channel = SocketChannel.open();
			channel.configureBlocking(false);
			channel.connect(address);


			this.selector = Selector.open();
			channel.register(this.selector, SelectionKey.OP_READ);


			while (false == channel.finishConnect()) {
			}
		} catch (IOException e) {
			close();
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 设置NIO数据处理器
	 *
	 * @param handler {@link ChannelHandler}
	 * @return this
	 */
	public NioClient setChannelHandler(ChannelHandler handler) {
		this.handler = handler;
		return this;
	}

	/**
	 * 开始监听
	 *
	 * @throws IOException IO异常
	 */
	private void doListen() throws IOException {
		while (this.selector.isOpen() && 0 != this.selector.select()) {

			final Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
			while (keyIter.hasNext()) {
				handle(keyIter.next());
				keyIter.remove();
			}
		}
	}

	/**
	 * 处理SelectionKey
	 *
	 * @param key SelectionKey
	 */
	private void handle(SelectionKey key) {

		if (key.isReadable()) {
			final SocketChannel socketChannel = (SocketChannel) key.channel();
			try {
				handler.handle(socketChannel);
			} catch (Exception e) {
				throw new SocketRuntimeException(e);
			}
		}
	}

	/**
	 * 实现写逻辑<br>
	 * 当收到写出准备就绪的信号后，回调此方法，用户可向客户端发送消息
	 *
	 * @param datas 发送的数据
	 * @return this
	 */
	public NioClient write(ByteBuffer... datas) {
		try {
			this.channel.write(datas);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 获取SocketChannel
	 *
	 * @return SocketChannel
	 */
	public SocketChannel getChannel() {
		return this.channel;
	}

	@Override
	public void close() {
		IoUtil.close(this.selector);
		IoUtil.close(this.channel);
	}
}

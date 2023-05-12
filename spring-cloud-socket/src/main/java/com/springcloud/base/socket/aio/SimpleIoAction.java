package com.springcloud.base.socket.aio;

import com.springcloud.base.log.StaticLog;

import java.nio.ByteBuffer;

/**
 * @Author: ls
 * @Description: 简易IO信息处理类
 * @Date: 2023/5/6 10:54
 */
public abstract class SimpleIoAction implements IoAction<ByteBuffer> {

	@Override
	public void accept(AioSession session) {
	}
	@Override
	public void failed(Throwable exc, AioSession session) {
		StaticLog.error(exc);
	}
}

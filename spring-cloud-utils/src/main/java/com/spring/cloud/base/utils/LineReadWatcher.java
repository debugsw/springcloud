package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.exception.IORuntimeException;
import com.spring.cloud.base.utils.interf.LineHandler;
import com.spring.cloud.base.utils.utils.FileUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * @Author: ls
 * @Description: 行处理的Watcher实现
 * @Date: 2023/4/13 16:11
 */
public class LineReadWatcher extends IgnoreWatcher implements Runnable {

	private final RandomAccessFile randomAccessFile;
	private final Charset charset;
	private final LineHandler lineHandler;

	/**
	 * 构造
	 *
	 * @param randomAccessFile {@link RandomAccessFile}
	 * @param charset          编码
	 * @param lineHandler      行处理器{@link LineHandler}实现
	 */
	public LineReadWatcher(RandomAccessFile randomAccessFile, Charset charset, LineHandler lineHandler) {
		this.randomAccessFile = randomAccessFile;
		this.charset = charset;
		this.lineHandler = lineHandler;
	}

	@Override
	public void run() {
		onModify(null, null);
	}

	@Override
	public void onModify(WatchEvent<?> event, Path currentPath) {
		final RandomAccessFile randomAccessFile = this.randomAccessFile;
		final Charset charset = this.charset;
		final LineHandler lineHandler = this.lineHandler;

		try {
			final long currentLength = randomAccessFile.length();
			final long position = randomAccessFile.getFilePointer();
			if (position == currentLength) {
				// 内容长度不变时忽略此次事件
				return;
			} else if (currentLength < position) {
				// 如果内容变短或变0，说明文件做了删改或清空，回到内容末尾或0
				randomAccessFile.seek(currentLength);
				return;
			}

			// 读取行
			FileUtil.readLines(randomAccessFile, charset, lineHandler);

			// 记录当前读到的位置
			randomAccessFile.seek(currentLength);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
}

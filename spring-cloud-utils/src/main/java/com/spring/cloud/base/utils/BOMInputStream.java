package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.exception.IORuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * @Author: ls
 * @Description: 读取带BOM头的流内容
 * @Date: 2023/4/13 16:11
 */
public class BOMInputStream extends InputStream {

	private final PushbackInputStream in;
	private boolean isInited = false;
	private final String defaultCharset;
	private String charset;

	private static final int BOM_SIZE = 4;

	// ----------------------------------------------------------------- Constructor start

	/**
	 * 构造
	 *
	 * @param in 流
	 */
	public BOMInputStream(InputStream in) {
		this(in, CharsetUtil.UTF_8);
	}

	/**
	 * 构造
	 *
	 * @param in             流
	 * @param defaultCharset 默认编码
	 */
	public BOMInputStream(InputStream in, String defaultCharset) {
		this.in = new PushbackInputStream(in, BOM_SIZE);
		this.defaultCharset = defaultCharset;
	}
	// ----------------------------------------------------------------- Constructor end

	/**
	 * 获取默认编码
	 *
	 * @return 默认编码
	 */
	public String getDefaultCharset() {
		return defaultCharset;
	}

	/**
	 * 获取BOM头中的编码
	 *
	 * @return 编码
	 */
	public String getCharset() {
		if (false == isInited) {
			try {
				init();
			} catch (IOException ex) {
				throw new IORuntimeException(ex);
			}
		}
		return charset;
	}

	@Override
	public void close() throws IOException {
		isInited = true;
		in.close();
	}

	@Override
	public int read() throws IOException {
		isInited = true;
		return in.read();
	}

	/**
	 * Read-ahead four bytes and check for BOM marks. <br>
	 * Extra bytes are unread back to the stream, only BOM bytes are skipped.
	 *
	 * @throws IOException 读取引起的异常
	 */
	protected void init() throws IOException {
		if (isInited) {
			return;
		}

		byte[] bom = new byte[BOM_SIZE];
		int n, unread;
		n = in.read(bom, 0, bom.length);

		if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00) && (bom[2] == (byte) 0xFE) && (bom[3] == (byte) 0xFF)) {
			charset = "UTF-32BE";
			unread = n - 4;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE) && (bom[2] == (byte) 0x00) && (bom[3] == (byte) 0x00)) {
			charset = "UTF-32LE";
			unread = n - 4;
		} else if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB) && (bom[2] == (byte) 0xBF)) {
			charset = "UTF-8";
			unread = n - 3;
		} else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF)) {
			charset = "UTF-16BE";
			unread = n - 2;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)) {
			charset = "UTF-16LE";
			unread = n - 2;
		} else {
			// Unicode BOM mark not found, unread all bytes
			charset = defaultCharset;
			unread = n;
		}

		if (unread > 0) {
			in.unread(bom, (n - unread), unread);
		}

		isInited = true;
	}
}

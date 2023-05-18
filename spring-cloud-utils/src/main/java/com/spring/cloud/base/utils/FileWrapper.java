package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.utils.CharsetUtil;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Author: ls
 * @Description: 文件包装器，扩展文件对象
 * @Date: 2023/4/13 16:11
 */
public class FileWrapper implements Serializable{
	private static final long serialVersionUID = 1L;

	protected File file;
	protected Charset charset;

	/** 默认编码：UTF-8 */
	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	/**
	 * 构造
	 * @param file 文件
	 * @param charset 编码，使用 {@link CharsetUtil}
	 */
	public FileWrapper(File file, Charset charset) {
		this.file = file;
		this.charset = charset;
	}

	/**
	 * 获得文件
	 * @return 文件
	 */
	public File getFile() {
		return file;
	}

	/**
	 * 设置文件
	 * @param file 文件
	 * @return 自身
	 */
	public FileWrapper setFile(File file) {
		this.file = file;
		return this;
	}

	/**
	 * 获得字符集编码
	 * @return 编码
	 */
	public Charset getCharset() {
		return charset;
	}

	/**
	 * 设置字符集编码
	 * @param charset 编码
	 * @return 自身
	 */
	public FileWrapper setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * 可读的文件大小
	 * @return 大小
	 */
	public String readableFileSize() {
		return FileUtil.readableFileSize(file.length());
	}
}

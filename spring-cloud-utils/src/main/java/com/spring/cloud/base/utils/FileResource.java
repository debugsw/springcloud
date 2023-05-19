package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.exception.NoResourceException;
import com.spring.cloud.base.utils.map.Resource;
import com.spring.cloud.base.utils.utils.FileUtil;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Path;

/**
 * @Author: ls
 * @Description: 文件资源访问对象
 * @Date: 2023/4/13 16:11
 */
public class FileResource implements Resource, Serializable {
	private static final long serialVersionUID = 1L;

	private final File file;
	private final long lastModified;
	private final String name;

	// ----------------------------------------------------------------------- Constructor start

	/**
	 * 构造
	 *
	 * @param path 文件绝对路径或相对ClassPath路径，但是这个路径不能指向一个jar包中的文件
	 */
	public FileResource(String path) {
		this(FileUtil.file(path));
	}

	/**
	 * 构造，文件名使用文件本身的名字，带扩展名
	 *
	 * @param path 文件
	 */
	public FileResource(Path path) {
		this(path.toFile());
	}

	/**
	 * 构造，文件名使用文件本身的名字，带扩展名
	 *
	 * @param file 文件
	 */
	public FileResource(File file) {
		this(file, null);
	}

	/**
	 * 构造
	 *
	 * @param file     文件
	 * @param fileName 文件名，带扩展名，如果为null获取文件本身的文件名
	 */
	public FileResource(File file, String fileName) {
		Assert.notNull(file, "File must be not null !");
		this.file = file;
		this.lastModified = file.lastModified();
		this.name = ObjectUtil.defaultIfNull(fileName, file::getName);
	}

	// ----------------------------------------------------------------------- Constructor end

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public URL getUrl() {
		return URLUtil.getURL(this.file);
	}

	@Override
	public InputStream getStream() throws NoResourceException {
		return FileUtil.getInputStream(this.file);
	}

	/**
	 * 获取文件
	 *
	 * @return 文件
	 */
	public File getFile() {
		return this.file;
	}

	@Override
	public boolean isModified() {
		return this.lastModified != file.lastModified();
	}

	/**
	 * 返回路径
	 *
	 * @return 返回URL路径
	 */
	@Override
	public String toString() {
		return this.file.toString();
	}
}

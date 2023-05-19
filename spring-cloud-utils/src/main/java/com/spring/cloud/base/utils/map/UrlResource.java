package com.spring.cloud.base.utils.map;

import com.spring.cloud.base.utils.utils.FileUtil;
import com.spring.cloud.base.utils.URLUtil;
import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.exception.NoResourceException;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;

/**
 * @Author: ls
 * @Description: URL资源访问类
 * @Date: 2023/4/13 16:11
 */
public class UrlResource implements Resource, Serializable {
	private static final long serialVersionUID = 1L;

	protected URL url;
	private long lastModified = 0;
	protected String name;


	/**
	 * 构造
	 *
	 * @param uri URI
	 */
	public UrlResource(URI uri) {
		this(URLUtil.url(uri), null);
	}

	/**
	 * 构造
	 *
	 * @param url URL
	 */
	public UrlResource(URL url) {
		this(url, null);
	}

	/**
	 * 构造
	 *
	 * @param url  URL，允许为空
	 * @param name 资源名称
	 */
	public UrlResource(URL url, String name) {
		this.url = url;
		if (null != url && URLUtil.URL_PROTOCOL_FILE.equals(url.getProtocol())) {
			this.lastModified = FileUtil.file(url).lastModified();
		}
		this.name = ObjectUtil.defaultIfNull(name, () -> (null != url ? FileUtil.getName(url.getPath()) : null));
	}

	/**
	 * 构造
	 *
	 * @param file 文件路径
	 * @deprecated Please use
	 */
	@Deprecated
	public UrlResource(File file) {
		this.url = URLUtil.getURL(file);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public URL getUrl() {
		return this.url;
	}

	@Override
	public InputStream getStream() throws NoResourceException {
		if (null == this.url) {
			throw new NoResourceException("Resource URL is null!");
		}
		return URLUtil.getStream(url);
	}

	@Override
	public boolean isModified() {
		// lastModified == 0表示此资源非文件资源
		return (0 != this.lastModified) && this.lastModified != getFile().lastModified();
	}

	/**
	 * 获得File
	 *
	 * @return {@link File}
	 */
	public File getFile() {
		return FileUtil.file(this.url);
	}

	/**
	 * 返回路径
	 *
	 * @return 返回URL路径
	 */
	@Override
	public String toString() {
		return (null == this.url) ? "null" : this.url.toString();
	}
}

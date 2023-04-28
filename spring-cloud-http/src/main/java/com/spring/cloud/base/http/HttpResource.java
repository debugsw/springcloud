package com.spring.cloud.base.http;

import com.spring.cloud.base.utils.Assert;
import com.spring.cloud.base.utils.map.Resource;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

/**
 * @Author: ls
 * @Description: HTTP资源
 * @Date: 2023/4/26 15:00
 */
public class HttpResource implements Resource, Serializable {
	private static final long serialVersionUID = 1L;

	private final Resource resource;
	private final String contentType;

	/**
	 * 构造
	 *
	 * @param resource    资源，非空
	 * @param contentType Content-Type类型，{@code null}表示不设置
	 */
	public HttpResource(Resource resource, String contentType) {
		this.resource = Assert.notNull(resource, "Resource must be not null !");
		this.contentType = contentType;
	}

	@Override
	public String getName() {
		return resource.getName();
	}

	@Override
	public URL getUrl() {
		return resource.getUrl();
	}

	@Override
	public InputStream getStream() {
		return resource.getStream();
	}

	/**
	 * 获取自定义Content-Type类型
	 *
	 * @return Content-Type类型
	 */
	public String getContentType() {
		return this.contentType;
	}
}

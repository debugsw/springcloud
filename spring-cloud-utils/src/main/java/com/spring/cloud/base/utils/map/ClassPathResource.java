package com.spring.cloud.base.utils.map;

import com.spring.cloud.base.utils.Assert;
import com.spring.cloud.base.utils.FileUtil;
import com.spring.cloud.base.utils.URLUtil;
import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.str.StrUtil;
import com.spring.cloud.base.utils.exception.NoResourceException;

import java.net.URL;
/**
 * @Author: ls
 * @Description: ClassPath单一资源访问类
 * @Date: 2023/4/13 16:11
 */
public class ClassPathResource extends UrlResource {
	private static final long serialVersionUID = 1L;

	private final String path;
	private final ClassLoader classLoader;
	private final Class<?> clazz;

	// -------------------------------------------------------------------------------------- Constructor start

	/**
	 * 构造
	 *
	 * @param path 相对于ClassPath的路径
	 */
	public ClassPathResource(String path) {
		this(path, null, null);
	}

	/**
	 * 构造
	 *
	 * @param path        相对于ClassPath的路径
	 * @param classLoader {@link ClassLoader}
	 */
	public ClassPathResource(String path, ClassLoader classLoader) {
		this(path, classLoader, null);
	}

	/**
	 * 构造
	 *
	 * @param path  相对于给定Class的路径
	 * @param clazz {@link Class} 用于定位路径
	 */
	public ClassPathResource(String path, Class<?> clazz) {
		this(path, null, clazz);
	}

	/**
	 * 构造
	 *
	 * @param pathBaseClassLoader 相对路径
	 * @param classLoader         {@link ClassLoader}
	 * @param clazz               {@link Class} 用于定位路径
	 */
	public ClassPathResource(String pathBaseClassLoader, ClassLoader classLoader, Class<?> clazz) {
		super((URL) null);
		Assert.notNull(pathBaseClassLoader, "Path must not be null");

		final String path = normalizePath(pathBaseClassLoader);
		this.path = path;
		this.name = StrUtil.isBlank(path) ? null : FileUtil.getName(path);

		this.classLoader = ObjectUtil.defaultIfNull(classLoader, ClassUtil::getClassLoader);
		this.clazz = clazz;
		initUrl();
	}
	// -------------------------------------------------------------------------------------- Constructor end

	/**
	 * 获得Path
	 *
	 * @return path
	 */
	public final String getPath() {
		return this.path;
	}

	/**
	 * 获得绝对路径Path<br>
	 * 对于不存在的资源，返回拼接后的绝对路径
	 *
	 * @return 绝对路径path
	 */
	public final String getAbsolutePath() {
		if (FileUtil.isAbsolutePath(this.path)) {
			return this.path;
		}
		// url在初始化的时候已经断言，此处始终不为null
		return FileUtil.normalize(URLUtil.getDecodedPath(this.url));
	}

	/**
	 * 获得 {@link ClassLoader}
	 *
	 * @return {@link ClassLoader}
	 */
	public final ClassLoader getClassLoader() {
		return this.classLoader;
	}

	/**
	 * 根据给定资源初始化URL
	 */
	private void initUrl() {
		if (null != this.clazz) {
			super.url = this.clazz.getResource(this.path);
		} else if (null != this.classLoader) {
			super.url = this.classLoader.getResource(this.path);
		} else {
			super.url = ClassLoader.getSystemResource(this.path);
		}
		if (null == super.url) {
			throw new NoResourceException("Resource of path [{}] not exist!", this.path);
		}
	}

	@Override
	public String toString() {
		return (null == this.path) ? super.toString() : "classpath:" + this.path;
	}

	/**
	 * 标准化Path格式
	 *
	 * @param path Path
	 * @return 标准化后的path
	 */
	private String normalizePath(String path) {
		// 标准化路径
		path = FileUtil.normalize(path);
		path = StrUtil.removePrefix(path, StrUtil.SLASH);

		Assert.isFalse(FileUtil.isAbsolutePath(path), "Path [{}] must be a relative path !", path);
		return path;
	}
}

package com.spring.cloud.base.utils.map;

import com.spring.cloud.base.utils.FileUtil;
import com.spring.cloud.base.utils.URLUtil;
import com.spring.cloud.base.utils.base.ReflectUtil;
import com.spring.cloud.base.utils.exception.UtilException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * @Author: ls
 * @Description: 外部Jar的类加载器
 * @Date: 2023/4/13 16:11
 */
public class JarClassLoader extends URLClassLoader {

	/**
	 * 加载Jar到ClassPath
	 *
	 * @param dir jar文件或所在目录
	 * @return JarClassLoader
	 */
	public static JarClassLoader load(File dir) {
		final JarClassLoader loader = new JarClassLoader();
		loader.addJar(dir);
		loader.addURL(dir);
		return loader;
	}

	/**
	 * 加载Jar到ClassPath
	 *
	 * @param jarFile jar文件或所在目录
	 * @return JarClassLoader
	 */
	public static JarClassLoader loadJar(File jarFile) {
		final JarClassLoader loader = new JarClassLoader();
		loader.addJar(jarFile);
		return loader;
	}

	/**
	 * 加载Jar文件到指定loader中
	 *
	 * @param loader  {@link URLClassLoader}
	 * @param jarFile 被加载的jar
	 * @throws UtilException IO异常包装和执行异常
	 */
	public static void loadJar(URLClassLoader loader, File jarFile) throws UtilException {
		try {
			final Method method = ClassUtil.getDeclaredMethod(URLClassLoader.class, "addURL", URL.class);
			if (null != method) {
				method.setAccessible(true);
				final List<File> jars = loopJar(jarFile);
				for (File jar : jars) {
					ReflectUtil.invoke(loader, method, jar.toURI().toURL());
				}
			}
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 加载Jar文件到System ClassLoader中
	 *
	 * @param jarFile 被加载的jar
	 * @return System ClassLoader
	 */
	public static URLClassLoader loadJarToSystemClassLoader(File jarFile) {
		URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		loadJar(urlClassLoader, jarFile);
		return urlClassLoader;
	}

	/**
	 * 构造
	 */
	public JarClassLoader() {
		this(new URL[]{});
	}

	/**
	 * 构造
	 *
	 * @param urls 被加载的URL
	 */
	public JarClassLoader(URL[] urls) {
		super(urls, ClassUtil.getClassLoader());
	}

	/**
	 * 构造
	 *
	 * @param urls        被加载的URL
	 * @param classLoader 类加载器
	 */
	public JarClassLoader(URL[] urls, ClassLoader classLoader) {
		super(urls, classLoader);
	}

	/**
	 * 加载Jar文件，或者加载目录
	 *
	 * @param jarFileOrDir jar文件或者jar文件所在目录
	 * @return this
	 */
	public JarClassLoader addJar(File jarFileOrDir) {
		if (isJarFile(jarFileOrDir)) {
			return addURL(jarFileOrDir);
		}
		final List<File> jars = loopJar(jarFileOrDir);
		for (File jar : jars) {
			addURL(jar);
		}
		return this;
	}

	@Override
	public void addURL(URL url) {
		super.addURL(url);
	}

	/**
	 * 增加class所在目录或文件<br>
	 * 如果为目录，此目录用于搜索class文件，如果为文件，需为jar文件
	 *
	 * @param dir 目录
	 * @return this
	 */
	public JarClassLoader addURL(File dir) {
		super.addURL(URLUtil.getURL(dir));
		return this;
	}

	/**
	 * 递归获得Jar文件
	 *
	 * @param file jar文件或者包含jar文件的目录
	 * @return jar文件列表
	 */
	private static List<File> loopJar(File file) {
		return FileUtil.loopFiles(file, JarClassLoader::isJarFile);
	}

	/**
	 * 是否为jar文件
	 *
	 * @param file 文件
	 * @return 是否为jar文件
	 * 
	 */
	private static boolean isJarFile(File file) {
		if (false == FileUtil.isFile(file)) {
			return false;
		}
		return file.getPath().toLowerCase().endsWith(".jar");
	}
}

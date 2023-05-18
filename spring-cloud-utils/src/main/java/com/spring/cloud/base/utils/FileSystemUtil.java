package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.str.StrUtil;
import com.spring.cloud.base.utils.exception.IORuntimeException;
import com.spring.cloud.base.utils.map.MapUtil;
import com.spring.cloud.base.utils.utils.CharsetUtil;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @Author: ls
 * @Description: 相关工具类封装
 * @Date: 2023/4/13 16:11
 */
public class FileSystemUtil {

	/**
	 * 创建 {@link FileSystem}
	 *
	 * @param path 文件路径，可以是目录或Zip文件等
	 * @return {@link FileSystem}
	 */
	public static FileSystem create(String path) {
		try {
			return FileSystems.newFileSystem(
					Paths.get(path).toUri(),
					MapUtil.of("create", "true"));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 创建 Zip的{@link FileSystem}，默认UTF-8编码
	 *
	 * @param path 文件路径，可以是目录或Zip文件等
	 * @return {@link FileSystem}
	 */
	public static FileSystem createZip(String path) {
		return createZip(path, null);
	}

	/**
	 * 创建 Zip的{@link FileSystem}
	 *
	 * @param path    文件路径，可以是目录或Zip文件等
	 * @param charset 编码
	 * @return {@link FileSystem}
	 */
	public static FileSystem createZip(String path, Charset charset) {
		if (null == charset) {
			charset = CharsetUtil.CHARSET_UTF_8;
		}
		final HashMap<String, String> env = new HashMap<>();
		env.put("create", "true");
		env.put("encoding", charset.name());

		try {
			return FileSystems.newFileSystem(
					URI.create("jar:" + Paths.get(path).toUri()), env);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取目录的根路径，或Zip文件中的根路径
	 *
	 * @param fileSystem {@link FileSystem}
	 * @return 根 {@link Path}
	 */
	public static Path getRoot(FileSystem fileSystem) {
		return fileSystem.getPath(StrUtil.SLASH);
	}
}

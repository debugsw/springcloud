package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.utils.PathUtil;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @Author: ls
 * @Description: 文件拷贝的FileVisitor实现
 * @Date: 2023/4/13 16:11
 */
public class CopyVisitor extends SimpleFileVisitor<Path> {

	/**
	 * 源Path，或基准路径，用于计算被拷贝文件的相对路径
	 */
	private final Path source;
	private final Path target;
	private final CopyOption[] copyOptions;

	/**
	 * 标记目标目录是否创建，省略每次判断目标是否存在
	 */
	private boolean isTargetCreated;

	/**
	 * 构造
	 *
	 * @param source      源Path，或基准路径，用于计算被拷贝文件的相对路径
	 * @param target      目标Path
	 * @param copyOptions 拷贝选项，如跳过已存在等
	 */
	public CopyVisitor(Path source, Path target, CopyOption... copyOptions) {
		if (PathUtil.exists(target, false) && false == PathUtil.isDirectory(target)) {
			throw new IllegalArgumentException("Target must be a directory");
		}
		this.source = source;
		this.target = target;
		this.copyOptions = copyOptions;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		initTargetDir();
		// 将当前目录相对于源路径转换为相对于目标路径
		final Path targetDir = resolveTarget(dir);

		// 在目录不存在的情况下，copy方法会创建新目录
		try {
			Files.copy(dir, targetDir, copyOptions);
		} catch (FileAlreadyExistsException e) {
			if (false == Files.isDirectory(targetDir)) {
				// 目标文件存在抛出异常，目录忽略
				throw e;
			}
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		initTargetDir();
		// 如果目标存在，无论目录还是文件都抛出FileAlreadyExistsException异常，此处不做特别处理
		Files.copy(file, resolveTarget(file), copyOptions);
		return FileVisitResult.CONTINUE;
	}

	private Path resolveTarget(Path file) {
		return target.resolve(source.relativize(file));
	}

	/**
	 * 初始化目标文件或目录
	 */
	private void initTargetDir() {
		if (false == this.isTargetCreated) {
			PathUtil.mkdir(this.target);
			this.isTargetCreated = true;
		}
	}
}

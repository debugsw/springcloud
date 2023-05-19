package com.springcloud.base.setting;

import com.spring.cloud.base.utils.utils.ArrayUtil;
import com.spring.cloud.base.utils.utils.IoUtil;
import com.spring.cloud.base.utils.interf.Filter;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ls
 * @Description: 文件监听服务
 * @Date: 2023/5/6 10:54
 */
public class WatchServer extends Thread implements Closeable, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 监听服务
	 */
	private WatchService watchService;
	/**
	 * 监听事件列表
	 */
	protected WatchEvent.Kind<?>[] events;
	/**
	 * 监听选项，例如监听频率等
	 */
	private WatchEvent.Modifier[] modifiers;
	/**
	 * 监听是否已经关闭
	 */
	protected boolean isClosed;
	/**
	 * WatchKey 和 Path的对应表
	 */
	private final Map<WatchKey, Path> watchKeyPathMap = new HashMap<>();

	/**
	 * 初始化
	 *
	 * @throws WatchException 监听异常，IO异常时抛出此异常
	 */
	public void init() throws WatchException {
		//初始化监听
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			throw new WatchException(e);
		}

		isClosed = false;
	}

	/**
	 * 设置监听选项
	 *
	 * @param modifiers 监听选项，例如监听频率等
	 */
	public void setModifiers(WatchEvent.Modifier[] modifiers) {
		this.modifiers = modifiers;
	}

	/**
	 * 将指定路径加入到监听中
	 *
	 * @param path     路径
	 * @param maxDepth 递归下层目录的最大深度
	 */
	public void registerPath(Path path, int maxDepth) {
		final WatchEvent.Kind<?>[] kinds = ArrayUtil.defaultIfEmpty(this.events, WatchKind.ALL);

		try {
			final WatchKey key;
			if (ArrayUtil.isEmpty(this.modifiers)) {
				key = path.register(this.watchService, kinds);
			} else {
				key = path.register(this.watchService, kinds, this.modifiers);
			}
			watchKeyPathMap.put(key, path);
			// 递归注册下一层层级的目录
			if (maxDepth > 1) {
				//遍历所有子目录并加入监听
				Files.walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), maxDepth, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						registerPath(dir, 0);//继续添加目录
						return super.postVisitDirectory(dir, exc);
					}
				});
			}
		} catch (IOException e) {
			if (!(e instanceof AccessDeniedException)) {
				throw new WatchException(e);
			}
		}
	}

	/**
	 * 执行事件获取并处理
	 *
	 * @param action      监听回调函数，实现此函数接口用于处理WatchEvent事件
	 * @param watchFilter 监听过滤接口，通过实现此接口过滤掉不需要监听的情况，null表示不过滤
	 * @since 5.4.0
	 */
	public void watch(WatchAction action, Filter<WatchEvent<?>> watchFilter) {
		WatchKey wk;
		try {
			wk = watchService.take();
		} catch (InterruptedException | ClosedWatchServiceException e) {
			// 用户中断
			close();
			return;
		}

		final Path currentPath = watchKeyPathMap.get(wk);

		for (WatchEvent<?> event : wk.pollEvents()) {
			// 如果监听文件，检查当前事件是否与所监听文件关联
			if (null != watchFilter && !watchFilter.accept(event)) {
				continue;
			}

			action.doAction(event, currentPath);
		}

		wk.reset();
	}

	/**
	 * 执行事件获取并处理
	 *
	 * @param watcher     {@link Watcher}
	 * @param watchFilter 监听过滤接口，通过实现此接口过滤掉不需要监听的情况，null表示不过滤
	 */
	public void watch(Watcher watcher, Filter<WatchEvent<?>> watchFilter) {
		watch((event, currentPath) -> {
			final WatchEvent.Kind<?> kind = event.kind();
			if (kind == WatchKind.CREATE.getValue()) {
				watcher.onCreate(event, currentPath);
			} else if (kind == WatchKind.MODIFY.getValue()) {
				watcher.onModify(event, currentPath);
			} else if (kind == WatchKind.DELETE.getValue()) {
				watcher.onDelete(event, currentPath);
			} else if (kind == WatchKind.OVERFLOW.getValue()) {
				watcher.onOverflow(event, currentPath);
			}
		}, watchFilter);
	}

	/**
	 * 关闭监听
	 */
	@Override
	public void close() {
		isClosed = true;
		IoUtil.close(watchService);
	}
}

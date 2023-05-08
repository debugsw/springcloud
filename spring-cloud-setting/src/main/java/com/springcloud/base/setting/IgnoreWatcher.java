package com.springcloud.base.setting;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * @Author: ls
 * @Description: 跳过所有事件处理Watcher
 * @Date: 2023/5/6 10:54
 */
public class IgnoreWatcher implements Watcher {

	@Override
	public void onCreate(WatchEvent<?> event, Path currentPath) {
	}

	@Override
	public void onModify(WatchEvent<?> event, Path currentPath) {
	}

	@Override
	public void onDelete(WatchEvent<?> event, Path currentPath) {
	}

	@Override
	public void onOverflow(WatchEvent<?> event, Path currentPath) {
	}
}

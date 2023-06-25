package com.springcloud.base.setting.watch;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * @Author: ls
 * @Description: 监听事件处理函数接口
 * @Date: 2023/5/6 10:54
 */
@FunctionalInterface
public interface WatchAction {

	/**
	 * 事件处理，通过实现此方法处理各种事件
	 *
	 * @param event       事件
	 * @param currentPath 事件发生的当前Path路径
	 */
	void doAction(WatchEvent<?> event, Path currentPath);
}

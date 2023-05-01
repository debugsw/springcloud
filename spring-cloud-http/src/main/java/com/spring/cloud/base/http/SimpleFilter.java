package com.spring.cloud.base.http;

import com.sun.net.httpserver.Filter;

/**
 * @Author: ls
 * @Description: 匿名简单过滤器，跳过了描述
 * @Date: 2023/4/26 15:00
 */
public abstract class SimpleFilter extends Filter {

	@Override
	public String description() {
		return "Anonymous Filter";
	}
}

package com.spring.cloud.base.http;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: ls
 * @Description: Http拦截器接口，通过实现此接口，完成请求发起前或结束后对请求的编辑工作
 * @Date: 2023/4/26 15:00
 */
@FunctionalInterface
public interface HttpInterceptor<T extends HttpBase<T>> {

	/**
	 * 处理请求
	 *
	 * @param httpObj 请求或响应对象
	 */
	void process(T httpObj);

	/**
	 * 拦截器链
	 *
	 * @param <T> 过滤参数类型，HttpRequest或者HttpResponse
	 * @author looly
	 * @since 5.7.16
	 */
	class Chain<T extends HttpBase<T>> implements Chain<HttpInterceptor<T>, Chain<T>> {
		private final List<HttpInterceptor<T>> interceptors = new LinkedList<>();

		@Override
		public Chain<T> addChain(HttpInterceptor<T> element) {
			interceptors.add(element);
			return this;
		}

		@Override
		public Iterator<HttpInterceptor<T>> iterator() {
			return interceptors.iterator();
		}

		/**
		 * 清空
		 *
		 * @return this
		 * @since 5.8.0
		 */
		public Chain<T> clear() {
			interceptors.clear();
			return this;
		}
	}
}

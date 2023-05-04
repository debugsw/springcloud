package com.spring.cloud.base.http;

import com.spring.cloud.base.utils.RFC3986;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/5/4 15:00
 */
public class FormUrlencoded {

	/**
	 * query中的value，默认除"-", "_", ".", "*"外都编码<br>
	 * 这个类似于JDK提供的{@link java.net.URLEncoder}
	 */
	public static final PercentCodec ALL = PercentCodec.of(RFC3986.UNRESERVED)
			.removeSafe('~').addSafe('*').setEncodeSpaceAsPlus(true);
}

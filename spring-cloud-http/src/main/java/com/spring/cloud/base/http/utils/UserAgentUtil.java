package com.spring.cloud.base.http.utils;

import com.spring.cloud.base.http.UserAgent;

/**
 * @Author: ls
 * @Description: User-Agent工具类
 * @Date: 2023/4/26 15:00
 */
public class UserAgentUtil {

	/**
	 * 解析User-Agent
	 *
	 * @param userAgentString User-Agent字符串
	 * @return {@link UserAgent}
	 */
	public static UserAgent parse(String userAgentString) {
		return UserAgentParser.parse(userAgentString);
	}

}

package com.spring.cloud.base.http;

import com.spring.cloud.base.utils.PercentCodec;
import com.spring.cloud.base.utils.RFC3986;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/5/4 15:00
 */
public class FormUrlencoded {

	public static final PercentCodec ALL = PercentCodec.of(RFC3986.UNRESERVED).removeSafe('~').addSafe('*').setEncodeSpaceAsPlus(true);
}

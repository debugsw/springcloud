package com.spring.cloud.base.http.factory;

import com.spring.cloud.base.http.SSLProtocols;
import com.spring.cloud.base.utils.exception.IORuntimeException;

/**
 * @Author: ls
 * @Description: 兼容android低版本SSL连接
 * @Date: 2023/4/26 15:00
 */
public class AndroidSupportSSLFactory extends CustomProtocolsSSLFactory {

	private static final String[] protocols = {
			SSLProtocols.SSLv3, SSLProtocols.TLSv1, SSLProtocols.TLSv11, SSLProtocols.TLSv12};

	public AndroidSupportSSLFactory() throws IORuntimeException {
		super(protocols);
	}

}

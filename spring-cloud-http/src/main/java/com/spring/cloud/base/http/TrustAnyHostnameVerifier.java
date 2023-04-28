package com.spring.cloud.base.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @Author: ls
 * @Description: https 域名校验
 * @Date: 2023/4/26 15:00
 */
public class TrustAnyHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}

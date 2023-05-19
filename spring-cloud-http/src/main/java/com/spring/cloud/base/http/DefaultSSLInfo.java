package com.spring.cloud.base.http;

import com.spring.cloud.base.http.factory.AndroidSupportSSLFactory;
import com.spring.cloud.base.http.factory.DefaultSSLFactory;
import com.spring.cloud.base.utils.utils.JdkUtil;

import javax.net.ssl.SSLSocketFactory;

/**
 * @Author: ls
 * @Description: 默认的全局SSL配置
 * @Date: 2023/4/26 15:00
 */
public class DefaultSSLInfo {
    /**
     * 默认信任全部的域名校验器
     */
    public static final TrustAnyHostnameVerifier TRUST_ANY_HOSTNAME_VERIFIER;
    /**
     * 默认的SSLSocketFactory，区分安卓
     */
    public static final SSLSocketFactory DEFAULT_SSF;

    static {
        TRUST_ANY_HOSTNAME_VERIFIER = new TrustAnyHostnameVerifier();
        if (JdkUtil.IS_ANDROID) {
            // 兼容android低版本SSL连接
            DEFAULT_SSF = new AndroidSupportSSLFactory();
        } else {
            DEFAULT_SSF = new DefaultSSLFactory();
        }
    }
}

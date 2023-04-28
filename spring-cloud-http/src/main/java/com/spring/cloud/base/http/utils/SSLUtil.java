package com.spring.cloud.base.http.utils;

import com.spring.cloud.base.http.SSLContextBuilder;
import com.spring.cloud.base.utils.exception.IORuntimeException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * @Author: ls
 * @Description: 安全套接字协议相关工具封装
 * @Date: 2023/4/26 15:00
 */
public class SSLUtil {

    /**
     * 创建{@link SSLContext}，默认新人全部
     *
     * @param protocol SSL协议，例如TLS等
     * @return {@link SSLContext}
     * @throws IORuntimeException 包装 GeneralSecurityException异常
     */
    public static SSLContext createSSLContext(String protocol) throws IORuntimeException {
        return SSLContextBuilder.create().setProtocol(protocol).build();
    }

    /**
     * 创建{@link SSLContext}
     *
     * @param protocol     SSL协议，例如TLS等
     * @param keyManager   密钥管理器,{@code null}表示无
     * @param trustManager 信任管理器, {@code null}表示无
     * @return {@link SSLContext}
     * @throws IORuntimeException 包装 GeneralSecurityException异常
     */
    public static SSLContext createSSLContext(String protocol, KeyManager keyManager, TrustManager trustManager) throws IORuntimeException {
        return createSSLContext(protocol, keyManager == null ? null : new KeyManager[]{keyManager}, trustManager == null ? null : new TrustManager[]{trustManager});
    }

    /**
     * 创建和初始化{@link SSLContext}
     *
     * @param protocol      SSL协议，例如TLS等
     * @param keyManagers   密钥管理器,{@code null}表示无
     * @param trustManagers 信任管理器, {@code null}表示无
     * @return {@link SSLContext}
     * @throws IORuntimeException 包装 GeneralSecurityException异常
     */
    public static SSLContext createSSLContext(String protocol, KeyManager[] keyManagers, TrustManager[] trustManagers) throws IORuntimeException {
        return SSLContextBuilder.create().setProtocol(protocol).setKeyManagers(keyManagers).setTrustManagers(trustManagers).build();
    }
}

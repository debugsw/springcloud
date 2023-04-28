package com.spring.cloud.base.http;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.cert.X509Certificate;

/**
 * @Author: ls
 * @Description: 默认信任管理器，默认信任所有客户端和服务端证书
 * @Date: 2023/4/26 15:00
 */
public class DefaultTrustManager extends X509ExtendedTrustManager {

    /**
     * 默认的全局单例默认信任管理器，默认信任所有客户端和服务端证书
     *
     * @since 5.7.8
     */
    public static DefaultTrustManager INSTANCE = new DefaultTrustManager();

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) {
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) {
    }
}

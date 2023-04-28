package com.spring.cloud.base.http.factory;

import com.spring.cloud.base.http.utils.SSLUtil;
import com.spring.cloud.base.utils.ArrayUtil;
import com.spring.cloud.base.utils.exception.IORuntimeException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @Author: ls
 * @Description: 自定义支持协议类型的SSLSocketFactory
 * @Date: 2023/4/26 15:00
 */
public class CustomProtocolsSSLFactory extends SSLSocketFactory {

    private final String[] protocols;
    private final SSLSocketFactory base;

    /**
     * 构造
     *
     * @param protocols 支持协议列表
     * @throws IORuntimeException IO异常
     */
    public CustomProtocolsSSLFactory(String... protocols) throws IORuntimeException {
        this.protocols = protocols;
        this.base = SSLUtil.createSSLContext(null).getSocketFactory();
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return base.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return base.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket() throws IOException {
        final SSLSocket sslSocket = (SSLSocket) base.createSocket();
        resetProtocols(sslSocket);
        return sslSocket;
    }

    @Override
    public SSLSocket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        final SSLSocket socket = (SSLSocket) base.createSocket(s, host, port, autoClose);
        resetProtocols(socket);
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        final SSLSocket socket = (SSLSocket) base.createSocket(host, port);
        resetProtocols(socket);
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        final SSLSocket socket = (SSLSocket) base.createSocket(host, port, localHost, localPort);
        resetProtocols(socket);
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        final SSLSocket socket = (SSLSocket) base.createSocket(host, port);
        resetProtocols(socket);
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        final SSLSocket socket = (SSLSocket) base.createSocket(address, port, localAddress, localPort);
        resetProtocols(socket);
        return socket;
    }

    /**
     * 重置可用策略
     *
     * @param socket SSLSocket
     */
    private void resetProtocols(SSLSocket socket) {
        if (ArrayUtil.isNotEmpty(this.protocols)) {
            socket.setEnabledProtocols(this.protocols);
        }
    }

}

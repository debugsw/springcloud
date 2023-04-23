package com.spring.cloud.base.monitor.sleuth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.function.Predicate;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/23 09:47
 */
@Slf4j
public class LogIpUtil {
    private static final String UNKNOWN = "NONE";
    private static String hostIp = UNKNOWN;
    private static String hostName = UNKNOWN;

    public static String localhostName;

    public static String getLocalHostIp() {
        try {
            if (hostIp.equals( UNKNOWN )) {
                hostIp = getLocalhostStr();
            }
        } catch (Exception e) {
        }
        return hostIp;
    }

    public static String getHostName() {
        try {
            if (hostName.equals( UNKNOWN )) {
                hostName = getLocalHostName();
            }
        } catch (Exception e) {
        }
        return hostName;
    }

    private static String getLocalhostStr() {
        InetAddress localhost = getLocalhost();
        if (null != localhost) {
            return localhost.getHostAddress();
        }
        return null;
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip;
        try {
            ip = request.getHeader( "x-forwarded-for" );
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip )) {
                ip = request.getHeader( "Proxy-Client-IP" );
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip )) {
                ip = request.getHeader( "WL-Proxy-Client-IP" );
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip )) {
                ip = request.getHeader( "HTTP_CLIENT_IP" );
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip )) {
                ip = request.getHeader( "HTTP_X_FORWARDED_FOR" );
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip )) {
                ip = request.getRemoteAddr();
                if ("0:0:0:0:0:0:0:1".equalsIgnoreCase( ip )
                        || "127.0.0.1".equalsIgnoreCase( ip )) {
                    ip = getLocalhostStr();
                }
            }
        } catch (Exception e) {
            ip = "未知IP";
        }
        return ip;
    }

    private static InetAddress getLocalhost() {
        final LinkedHashSet<InetAddress> localAddressList = localAddressList( address -> {
            // 非loopback地址，指127.*.*.*的地址
            return !address.isLoopbackAddress()
                    // 需为IPV4地址
                    && address instanceof Inet4Address;
        } );

        if (!CollectionUtils.isEmpty( localAddressList )) {
            InetAddress address2 = null;
            for (InetAddress inetAddress : localAddressList) {
                if (!inetAddress.isSiteLocalAddress()) {
                    // 非地区本地地址，指10.0.0.0 ~ 10.255.255.255、172.16.0.0 ~ 172.31.255.255、192.168.0.0 ~ 192.168.255.255
                    return inetAddress;
                } else if (null == address2) {
                    address2 = inetAddress;
                }
            }

            if (null != address2) {
                return address2;
            }
        }

        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
        }

        return null;
    }

    private static LinkedHashSet<InetAddress> localAddressList(Predicate<InetAddress> addressFilter) {
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RuntimeException( e );
        }

        if (networkInterfaces == null) {
            throw new RuntimeException( "Get network interface error!" );
        }

        final LinkedHashSet<InetAddress> ipSet = new LinkedHashSet<>();

        while (networkInterfaces.hasMoreElements()) {
            final NetworkInterface networkInterface = networkInterfaces.nextElement();
            final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                final InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress != null && (null == addressFilter || addressFilter.test( inetAddress ))) {
                    ipSet.add( inetAddress );
                }
            }
        }

        return ipSet;
    }

    private static String getLocalHostName() {
        if (StringUtils.hasText( localhostName )) {
            return localhostName;
        }
        final InetAddress localhost = getLocalhost();
        if (null != localhost) {
            String name = localhost.getHostName();
            if (StringUtils.isEmpty( name )) {
                name = localhost.getHostAddress();
            }
            localhostName = name;
        }

        return localhostName;
    }
}

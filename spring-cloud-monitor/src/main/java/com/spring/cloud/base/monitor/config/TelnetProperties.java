package com.spring.cloud.base.monitor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/21 15:51
 */
@ConfigurationProperties(prefix = "management.health.telnet")
@Setter
@Getter
public class TelnetProperties {

    /**
     * 排除包含的key(采用String.contains进行验证)
     */
    private List<String> containsKey;
    /**
     * telnet链接超时时间(默认值2s)
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration connectionTimeOut = Duration.ofSeconds(2);
    /**
     * telnet缓存开启，主要防止容器内心跳验证时间短。telnet开销大的问题
     * 通过缓存解决每次心跳都需要telnet的情况
     */
    private Boolean telnetCacheStatus = Boolean.TRUE;
    /**
     * 自定义url检查地址（改属性不做定向判断，由全局判断统一处理）
     */
    private List<String> customUrl;
    /**
     * 是否打印日志异常堆栈
     */
    private boolean logExceptionStack = false;
}

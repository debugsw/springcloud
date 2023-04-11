package com.spring.cloud.base.common.xxljob;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: ls
 * @Date: 2023/1/30
 * @Description: xxl-job配置
 **/
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.xxl-job")
public class XxlJobProperties {
    /**
     * 调度中心地址
     */
    private String adminAddresses;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 多网卡需要配置
     */
    private String ip;

    /**
     * 端口
     */
    private int port;

    /**
     * 安全需要的密钥
     */
    private String accessToken;

    /**
     * 日志地址
     */
    private String logPath;

    /**
     * 日志保留的时间
     */
    private int logRetentionDays = 30;
}

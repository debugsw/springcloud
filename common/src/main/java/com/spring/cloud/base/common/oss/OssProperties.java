package com.spring.cloud.base.common.oss;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: ls
 * @Date: 2023/1/28
 * @Description:
 **/
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "common.oss")
public class OssProperties {
    /**
     * 上传的accessId
     */
    private String accessId;

    /**
     * 上传的accessKey
     */
    private String accessKey;

    /**
     * 上传的bucket
     */
    private String bucket;

    /**
     * 上传的endpoint
     */
    private String endpoint;

    /**
     * 上传的目录
     */
    private String dir;

    /**
     * cdn地址
     */
    private String cdnHost;

    /**
     * 签名有效时间默认60秒
     */
    private Integer expireTime = 60;
}

package com.springcloud.base.core.config.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: ls
 * @Date: 2023/1/9
 * @Description: 是否开启的配置
 **/
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "common.web.exception")
public class WebExceptionProperties {
    /**
     * enable exception handle
     * 是否开启异常处理
     * 默认true开启
     */
    private boolean enable = true;

}

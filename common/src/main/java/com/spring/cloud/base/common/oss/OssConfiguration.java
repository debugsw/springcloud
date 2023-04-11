package com.spring.cloud.base.common.oss;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

/**
 * @Author: ls
 * @Date: 2023/1/28
 * @Description:
 **/
@Configuration
@Import({OssProperties.class})
public class OssConfiguration {

    @Resource
    private OssProperties ossProperties;

    @Bean
    public OssTemplate ossTemplate() {
        return new OssTemplate(ossProperties);
    }
}

package com.spring.cloud.base.monitor.config;

import com.spring.cloud.base.monitor.enums.MiddlewareEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/21 10:54
 */
@Configuration
public class HealthIndicatorConfig {

    @Bean
    @ConditionalOnClass(name = "org.springframework.boot.actuate.redis.RedisHealthIndicator")
    public MiddlewareEnum redisMiddlewareEnum() {
        return MiddlewareEnum.REDIS;
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.boot.actuate.mongo.MongoHealthIndicator")
    public MiddlewareEnum mongoMiddlewareEnum() {
        return MiddlewareEnum.MONGODB;
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator")
    public MiddlewareEnum dataSourceMiddlewareEnum() {
        return MiddlewareEnum.DATASOURCE;
    }

    @Bean
    @ConditionalOnClass(name = "com.alibaba.cloud.nacos.discovery.actuate.health.NacosDiscoveryHealthIndicator")
    public MiddlewareEnum nacosMiddlewareEnum() {
        return MiddlewareEnum.NACOS;
    }
}

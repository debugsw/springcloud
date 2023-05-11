package com.spring.cloud.base.monitor.handler;

import com.spring.cloud.base.monitor.config.TelnetProperties;
import feign.Feign;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/21 15:50
 */
@ConditionalOnWebApplication
@Component
@Slf4j
@ConditionalOnClass({Feign.class})
public class FeignMonitorHandler {

    @Getter
    private final MultiValueMap<String, String> urlMap = new LinkedMultiValueMap<>();

    @Resource
    private NacosMonitorHandler nacosHandler;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private TelnetProperties telnetProperties;

    @SneakyThrows
    @PostConstruct
    private void init() {
        final Map<String, String> feignMap = listFeignInfo();
        handleUrl( feignMap );
    }

    /**
     * 获取feign对象中的链接
     *
     * @param feignMap
     */
    @SneakyThrows
    private void handleUrl(Map<String, String> feignMap) {
        /**
         *  获取所有实力链接信息
         */
        if (!CollectionUtils.isEmpty( feignMap )) {
            feignMap.forEach( (key, value) -> {
                //feign url地址为空的情况，根据name获取nacos中实例信息
                if (!StringUtils.hasText( value )) {
                    try {
                        urlMap.addAll( nacosHandler.getNacosUrl( key ) );
                    } catch (Exception e) {
                        if (telnetProperties.isLogExceptionStack()) {
                            log.error( "--->in naocs not found instance {}", key, e );
                        } else {
                            log.error( "--->in naocs not found instance {}", key );
                        }
                    }
                } else {
                    urlMap.add( key, value );
                }
            } );
        }
    }

    /**
     * 获取所有feign对象的配置链接信息
     *
     * @return
     */
    private Map<String, String> listFeignInfo() {
        /**
         * 获取所有的feign对象信息
         */
        return Arrays.stream( applicationContext.getBeanNamesForAnnotation( FeignClient.class ) )
                .map( beanName -> {
                    try {
                        return AnnotationUtils.findAnnotation( ClassUtils.getClass( beanName ), FeignClient.class );
                    } catch (ClassNotFoundException e) {
                        log.warn( "--->not found feign object,so jump over feign telnet check!!!<---" );
                    }
                    return null;
                } )
                .filter( Objects::nonNull )
                .collect( Collectors.toMap( FeignClient::name, FeignClient::url, (v1, v2) -> v2 ) );
    }
}

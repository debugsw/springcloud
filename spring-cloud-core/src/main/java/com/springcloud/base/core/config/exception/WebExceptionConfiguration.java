package com.springcloud.base.core.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author: ls
 * @Date: 2019/8/9
 * @Description: web相关配置
 **/
@Slf4j
@Configuration
@Import({WebExceptionProperties.class, WebApiExceptionHandler.class})
public class WebExceptionConfiguration {

}

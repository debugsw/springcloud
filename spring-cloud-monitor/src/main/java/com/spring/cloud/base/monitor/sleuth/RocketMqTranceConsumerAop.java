package com.spring.cloud.base.monitor.sleuth;

import com.spring.cloud.base.monitor.config.TelnetProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;

import javax.annotation.Resource;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/23 09:47
 */
@Aspect
@Slf4j
//sleuth 3.0.0后支持
//@ConditionalOnSleuthWeb
public class RocketMqTranceConsumerAop {

    @Resource
    private TelnetProperties telnetProperties;

    @Before("execution(* org.apache.rocketmq.spring.core.RocketMQListener.onMessage(..))")
    public void addTraceId(JoinPoint joinPoint) {
        try {
            Message message = (Message) joinPoint.getArgs()[0];
            String tranceId = message.getProperties().get("traceId");
            MDC.put("traceId", tranceId);
            String sourceIp = message.getProperties().get("sourceIp");
            MDC.put("sourceIp", sourceIp);
        } catch (Exception e) {
            if (telnetProperties.isLogExceptionStack()) {
                log.error("RocketMQ consume tranceId init fail..", e);
            } else {
                log.error("RocketMQ consume tranceId init fail..");
            }
        }
    }
}

package com.spring.cloud.base.monitor.sleuth;

import brave.Span;
import brave.Tracer;
import com.spring.cloud.base.monitor.config.TelnetProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/23 09:47
 */
@Aspect
@Slf4j
//@ConditionalOnSleuthWeb
public class RocketMqTranceProducerAop {
    @Resource
    private Tracer tracer;

    @Resource
    private TelnetProperties telnetProperties;

    @Before("execution(* org.apache.rocketmq.client.producer.DefaultMQProducer.send*(..))")
    public void addTraceId(JoinPoint joinPoint) {
        try {
            Message message = (Message) joinPoint.getArgs()[0];
            final Span span = tracer.currentSpan();
            if (Objects.nonNull(span)) {
                message.getProperties().put("traceId", span.context().traceIdString());
            } else {
                String tranceId = MDC.get("traceId");
                message.getProperties().put("traceId", tranceId);
            }
            message.getProperties().put("sourceIp", MDC.get("sourceIp"));
        } catch (Exception exception) {
            if (telnetProperties.isLogExceptionStack()) {
                log.error("RocketMQ producer tranceId init fail..", exception);
            } else {
                log.error("RocketMQ producer tranceId init fail..");
            }
        }
    }
}

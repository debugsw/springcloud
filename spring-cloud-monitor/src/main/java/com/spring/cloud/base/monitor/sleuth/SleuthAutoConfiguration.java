package com.spring.cloud.base.monitor.sleuth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/23 09:47
 */
@Configuration
public class SleuthAutoConfiguration {

    @Bean
    @ConditionalOnClass(name = {"org.apache.rocketmq.spring.core.RocketMQListener"})
    public RocketMqTranceConsumerAop rocketMqTranceConsumerAop() {
        return new RocketMqTranceConsumerAop();
    }

    @Bean
    @ConditionalOnClass(name = {"org.apache.rocketmq.client.producer.DefaultMQProducer"})
    public RocketMqTranceProducerAop rocketMqTranceProducerAop() {
        return new RocketMqTranceProducerAop();
    }


}

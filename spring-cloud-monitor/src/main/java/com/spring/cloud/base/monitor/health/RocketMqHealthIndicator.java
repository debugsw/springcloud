package com.spring.cloud.base.monitor.health;

import com.spring.cloud.base.monitor.handler.RocketActuatorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/23 09:40
 */
@Slf4j
public class RocketMqHealthIndicator extends AbstractHealthIndicator {

    private final RocketActuatorHandler rocketActuateHandler;

    public RocketMqHealthIndicator(RocketActuatorHandler rocketActuateHandler) {
        super("RocketMq health check failed");
        this.rocketActuateHandler = rocketActuateHandler;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        final Serializable serializable = rocketActuateHandler.collectClusterNum();
        if (serializable instanceof String) {
            
            builder.status(Status.DOWN);
            builder.withDetail("exception", serializable);
        } else if (serializable instanceof HashMap) {
            builder.status(Status.UP);
        } else {
            builder.status(Status.UNKNOWN);
        }
    }

}

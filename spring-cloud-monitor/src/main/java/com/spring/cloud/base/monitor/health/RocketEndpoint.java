package com.spring.cloud.base.monitor.health;

import com.spring.cloud.base.monitor.handler.RocketActuatorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.health.Status;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/23 09:39
 */
@Endpoint(id = "rocket-console")
@Slf4j
public class RocketEndpoint {

    private final RocketActuatorHandler rocketActuateHandler;

    public RocketEndpoint(RocketActuatorHandler rocketActuateHandler) {
        this.rocketActuateHandler = rocketActuateHandler;
    }

    @ReadOperation
    public Map<String, Object> rocketConsole() throws Exception {
        Map<String, Object> result = new HashMap<>(2);
        final Serializable serializable = rocketActuateHandler.collectClusterNum();
        if (serializable instanceof HashMap) {
            result.put("RocketClusterInfo", serializable);
            result.put("RocketTPS", rocketActuateHandler.collectClusterTps());
        } else {
            result.put("RocketClusterInfo", Status.DOWN);
        }
        return result;
    }
}

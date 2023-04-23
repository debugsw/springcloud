package com.spring.cloud.base.monitor.health;

import com.spring.cloud.base.monitor.config.TelnetProperties;
import com.spring.cloud.base.monitor.handler.FeignMonitorHandler;
import com.spring.cloud.base.monitor.handler.RocketActuatorHandler;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointProperties;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/23 09:33
 */
@Configuration
@EnableConfigurationProperties({TelnetProperties.class})
@ConditionalOnWebApplication
@Slf4j
@ConditionalOnClass({AbstractHealthIndicator.class})
public class CustomHealthAutoConfig {

    @Value("classpath:telnet-log.txt")
    private Resource resource;

    @Bean
    @ConditionalOnEnabledHealthIndicator("telnet")
    @ConditionalOnClass({ConfigurableEnvironment.class, HealthEndpointProperties.class})
    public TelnetHealthIndicator telnet(ConfigurableEnvironment environment
            , TelnetProperties telnetProperties
            , @Nullable FeignMonitorHandler feignHandler) {
        printLogoInConsole();
        log.info( ".......beginning network address telenet check......." );
        log.info( ".......telnet cache status [{}].......", telnetProperties.getTelnetCacheStatus() ? "OPEN" : "CLOSE" );
        return new TelnetHealthIndicator( environment, telnetProperties, feignHandler );
    }

    @Bean
    @ConditionalOnEnabledHealthIndicator("rocketMQ")
    @ConditionalOnBean(RocketActuatorHandler.class)
    @ConditionalOnMissingBean
    public RocketMqHealthIndicator rocketMQ(RocketActuatorHandler rocketActuateHandler) {
        return new RocketMqHealthIndicator( rocketActuateHandler );
    }

    @Bean
    @ConditionalOnBean({RocketActuatorHandler.class})
    @ConditionalOnClass(Endpoint.class)
    @ConditionalOnMissingBean
    public RocketEndpoint rocketEndpoint(
            RocketActuatorHandler rocketActuateHandler) {
        return new RocketEndpoint( rocketActuateHandler );
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    @SneakyThrows
    private void printLogoInConsole() {
        @Cleanup InputStream is = resource.getInputStream();
        @Cleanup InputStreamReader isr = new InputStreamReader( is );
        @Cleanup BufferedReader br = new BufferedReader( isr );
        String springBootVersion = SpringBootVersion.getVersion();
        String springCloudVersion = "NONE";
        try {
            final Class<?> aClass = Class.forName( "org.springframework.cloud.client.discovery.EnableDiscoveryClient" );
            springCloudVersion = aClass.getPackage().getImplementationVersion();
        } catch (ClassNotFoundException e) {
        }
        String data;
        while ((data = br.readLine()) != null) {
            if (data.contains( "Boot" )) {
                print( data, springBootVersion );
            } else {
                print( data, springCloudVersion );
            }
        }
    }

    private void print(String data, String version) {
        if (Pattern.matches( "[a-zA-Z:\\d.%\\- ]+", data )) {
            System.out.printf( ANSI_GREEN + (data) + "%n" + ANSI_RESET, ANSI_RED + version + ANSI_RESET );
        } else {
            System.out.printf( (data) + "%n", version );
        }
    }
}

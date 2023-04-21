package com.spring.cloud.base.monitor.handler;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.spring.util.PropertySourcesUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/21 15:53
 */
@ConditionalOnBean(NacosDiscoveryProperties.class)
@Component
@Slf4j
public class NacosMonitorHandler {

    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    private NamingService namingService;

    @Resource
    private Environment environment;
    private static final Pattern PATTERN = Pattern.compile("-(\\w)");

    @SneakyThrows
    @PostConstruct
    private void init() {
        try {
            namingService = NacosFactory.createNamingService(getNacosProperties());
        } catch (Exception var2) {
            log.error("Nacos error:create naming service error!properties={},e=,", this, var2);
        }
    }

    private Properties getNacosProperties() {
        Properties properties = new Properties();
        properties.put("serverAddr", nacosDiscoveryProperties.getServerAddr());
        properties.put("username", Objects.toString(nacosDiscoveryProperties.getUsername(), ""));
        properties.put("password", Objects.toString(nacosDiscoveryProperties.getPassword(), ""));
        properties.put("namespace", nacosDiscoveryProperties.getNamespace());
        properties.put("com.alibaba.nacos.naming.log.filename", nacosDiscoveryProperties.getLogName());
        String endpoint = nacosDiscoveryProperties.getEndpoint();
        if (endpoint.contains(":")) {
            int index = endpoint.indexOf(":");
            properties.put("endpoint", endpoint.substring(0, index));
            properties.put("endpointPort", endpoint.substring(index + 1));
        } else {
            properties.put("endpoint", endpoint);
        }

        properties.put("accessKey", nacosDiscoveryProperties.getAccessKey());
        properties.put("secretKey", nacosDiscoveryProperties.getSecretKey());
        properties.put("clusterName", nacosDiscoveryProperties.getClusterName());
        properties.put("namingLoadCacheAtStart", nacosDiscoveryProperties.getNamingLoadCacheAtStart());
        this.enrichNacosDiscoveryProperties(properties);
        return properties;
    }

    private void enrichNacosDiscoveryProperties(Properties nacosDiscoveryProperties) {
        Map<String, Object> properties = PropertySourcesUtils.getSubProperties((ConfigurableEnvironment) this.environment, "spring.cloud.nacos.discovery");
        properties.forEach((k, v) -> {
            nacosDiscoveryProperties.putIfAbsent(this.resolveKey(k), String.valueOf(v));
        });
    }

    private String resolveKey(String key) {
        Matcher matcher = PATTERN.matcher(key);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获取nacos注册服务详细信息
     *
     * @param serverName
     * @return
     * @throws NacosException
     */
    public MultiValueMap<String, String> getNacosUrl(String serverName) throws NacosException {
        final MultiValueMap<String, String> urlMap = new LinkedMultiValueMap<>();
        namingService.selectInstances(serverName, Boolean.TRUE).forEach(instance -> {
            urlMap.add(instance.getServiceName(), "http://" + instance.getIp() + ":" + instance.getPort());
        });
        return urlMap;
    }
}

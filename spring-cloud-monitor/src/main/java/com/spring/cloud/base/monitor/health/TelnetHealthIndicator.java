package com.spring.cloud.base.monitor.health;

import com.spring.cloud.base.monitor.config.TelnetProperties;
import com.spring.cloud.base.monitor.exception.TelnetException;
import com.spring.cloud.base.monitor.handler.FeignMonitorHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.util.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/23 09:25
 */
@Slf4j
public class TelnetHealthIndicator extends AbstractHealthIndicator {

    private static final Pattern URL_PATTERN = Pattern.compile("[ht|f]*?tp[s]*?://[\\w.]+[:\\d]+");
    private static final String FEIGN_CONFIG_URL_PATTERN = ".*[$]\\{.*}.*";
    private final ConfigurableEnvironment environment;
    private final TelnetProperties telnetProperties;
    private final FeignMonitorHandler feignHandler;
    private Pair<Status, Map<String, List<String>>> telnetResult;
    private boolean isBeginning = true;
    private final CopyOnWriteArrayList<String> commandArgs = new CopyOnWriteArrayList<>();

    public TelnetHealthIndicator(ConfigurableEnvironment environment
            , TelnetProperties telnetProperties
            , FeignMonitorHandler feignHandler) {
        super("Telnet health check failed");
        this.environment = environment;
        this.telnetProperties = telnetProperties;
        this.feignHandler = feignHandler;
    }

    /**
     * 遍历String集合中查询是否包含target
     *
     * @param target
     * @param source
     * @return
     */
    private boolean colStrIndexOfIgnoreCase(String target, Collection<String> source) {
        return Optional
                .ofNullable(source)
                .orElse(new ArrayList<>(1))
                .stream()
                .anyMatch(target::contains);
    }


    @Override
    protected void doHealthCheck(Health.Builder builder) {
        if (telnetProperties.getTelnetCacheStatus() && !isBeginning) {
            builder.withDetails(telnetResult.getRight()).status(telnetResult.getLeft());
        } else {
            log.info("telnet exclud url list: {}", telnetProperties.getContainsKey());
            MultiValueMap<Status, String> checkMap = checkByConfig();
            checkMap.addAll(checkByFeign());
            Map<String, List<String>> result = new HashMap<>(2);
            List<String> upList = checkMap.get(Status.UP);
            result.put(Status.UP.toString(), null != upList ? upList : Collections.emptyList());
            List<String> downList = checkMap.get(Status.DOWN);
            result.put(Status.DOWN.toString(), null != downList ? downList : Collections.emptyList());
            Status status = CollectionUtils.isEmpty(downList) ? Status.UP : Status.DOWN;
            telnetResult = Pair.of(status, result);
            builder.withDetails(result).status(status);
            isBeginning = false;
        }
    }

    /**
     * feign 链接检查
     *
     * @return
     */
    private MultiValueMap<Status, String> checkByFeign() {
        MultiValueMap<Status, String> multiValueMap = new LinkedMultiValueMap<>();
        Optional.ofNullable(feignHandler)
                .ifPresent(feignNacosHandler -> feignNacosHandler.getUrlMap()
                        .forEach((key, value) -> value.forEach(val -> {
                            if (StringUtils.hasText(val)
                                    && !val.matches(FEIGN_CONFIG_URL_PATTERN)
                                    && !colStrIndexOfIgnoreCase(val
                                    , telnetProperties.getContainsKey())) {
                                log.info("<<FeignClient interceptor 【{}】 start check>>", key);
                                final boolean telnet = telnet(URI.create(val));
                                log.info("<<FeignClient interceptor【{}】 end check status 【{}】>>", key, telnet);
                                multiValueMap.add(telnet ? Status.UP : Status.DOWN, val);
                            }
                        })));
        return multiValueMap;
    }

    /**
     * 包含加载的properties类型
     */
    private static final String[] INCLOUD_PROPERTIES_SOURCE = {
            "org.springframework.cloud.bootstrap.config.BootstrapPropertySource"
            //fix 仅使用了nacos config 功能的情况
            , "com.alibaba.nacos.spring.core.env.NacosPropertySource",};

    private boolean filterSource(PropertySource<?> propertySource) {
        return Arrays.stream(INCLOUD_PROPERTIES_SOURCE)
                .anyMatch(source -> ClassUtils.isPresent(source, this.getClass().getClassLoader())
                        && propertySource.getClass().getName().equals(source));
    }

    /**
     * 检查配置文件中的链接信息
     */
    private MultiValueMap<Status, String> checkByConfig() {
        MutablePropertySources propSrcs = environment.getPropertySources();
        MultiValueMap<Status, String> multiValueMap = new LinkedMultiValueMap<>();
        //此处为了解决spring core版本问题，5.2+以后版本 MutablePropertySources存在stream方法，但是之前的版本无该方法只能采用迭代器方式
        Stream<PropertySource<?>> propertySourceStream = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        propSrcs.iterator(),
                        Spliterator.ORDERED)
                , false);
        propertySourceStream
                .filter(propertySource -> propertySource instanceof OriginTrackedMapPropertySource
                        | propertySource instanceof SimpleCommandLinePropertySource
                        //fix 部分版本BootstrapPropertySource对象非public情况
                        | filterSource(propertySource))
                .forEach(properties -> {
                    //fix 启动参数中包含有url的情况
                    if (properties instanceof SimpleCommandLinePropertySource) {
                        SimpleCommandLinePropertySource simpleCommandLinePropertySource = (SimpleCommandLinePropertySource) properties;
                        telnetCommandArgsContainsUrl(multiValueMap, simpleCommandLinePropertySource);
                    } else {
                        final Set<Map.Entry<String, Object>> entries = ((Map<String, Object>) properties.getSource()).entrySet();
                        telnetCommonSourceConstainsUrl(multiValueMap, entries);
                    }
                });
        return multiValueMap;
    }

    /**
     * tenlent 通用配置文件中包含有url的情况
     *
     * @param multiValueMap
     * @param propertySourceEntry
     */
    private void telnetCommonSourceConstainsUrl(MultiValueMap<Status, String> multiValueMap
            , Set<Map.Entry<String, Object>> propertySourceEntry) {
        propertySourceEntry
                .stream()
                .filter(val -> !colStrIndexOfIgnoreCase(val.getKey(), telnetProperties.getContainsKey()))
                //fix 此处排除命令参数包含的key，遵循命令传参优先级最高原则
                .filter(val -> !commandArgs.contains(val.getKey()))
                .map(this::getUrlByJdk)
                .filter(Objects::nonNull)
                //telnet 验证
                .forEach(url -> {
                    boolean telnet = telnet(URI.create(url));
                    multiValueMap.add(telnet ? Status.UP : Status.DOWN, url);
                });
    }

    /**
     * telnet启动参数中包含有url的情况，
     *
     * @param multiValueMap
     */
    private void telnetCommandArgsContainsUrl(MultiValueMap<Status, String> multiValueMap
            , SimpleCommandLinePropertySource simpleCommandLinePropertySource) {
        Stream.of(simpleCommandLinePropertySource.getPropertyNames())
                .map(key -> {
                    final String val = simpleCommandLinePropertySource.getProperty(key);
                    DefaultMapEntry defaultMapEntry = new DefaultMapEntry(key, val);
                    String url = getUrlByJdk(defaultMapEntry);
                    return StringUtils.hasText(url) ? Pair.of(key, url) : null;
                })
                .filter(Objects::nonNull)
                //telnet 验证
                .forEach(url -> {
                    boolean telnet = telnet(URI.create(url.getRight()));
                    if (telnet) {
                        commandArgs.addIfAbsent(url.getLeft());
                    }
                    multiValueMap.add(telnet ? Status.UP : Status.DOWN, url.getRight());
                });
    }

    /**
     * 正则解析url
     *
     * @param entry
     * @return
     */
    @Deprecated
    private String getUrl(Map.Entry<String, Object> entry) {
        Objects.requireNonNull(entry.getValue(), entry.getKey() + " must not is null");
        Matcher matcher = URL_PATTERN.matcher(entry.getValue() + "");
        return matcher.find() ? matcher.group(0) : null;
    }

    /**
     * 通过net.URL进行url获取
     *
     * @param entry
     * @return
     */
    private String getUrlByJdk(Map.Entry<String, Object> entry) {
        try {
            if (Objects.nonNull(entry.getValue())) {
                String addr = entry.getValue().toString().trim();
                URL url = new URL(addr);
                return HttpMethod.covertURL2Str(url);
            }
        } catch (MalformedURLException e) {
            log.debug("{} telnet check fail,{} not url", entry.getKey(), entry.getValue());
        }
        return null;
    }

    /**
     * telnet链接地址是否正常
     *
     * @param uri
     */
    private boolean telnet(URI uri) {
        try {
            TelnetClient telnetClient = new TelnetClient("vt200");
            telnetClient.setConnectTimeout(Math.toIntExact(telnetProperties.getConnectionTimeOut().toMillis()));
            int port = uri.getPort();
            if (HttpMethod.HTTP.name().equalsIgnoreCase(uri.getScheme())) {
                telnetClient.connect(uri.getHost(), port < 0 ? 80 : port);
            } else if (HttpMethod.HTTPS.name().equalsIgnoreCase(uri.getScheme())) {
                telnetClient.connect(uri.getHost(), port < 0 ? 443 : port);
            } else {
                throw new IllegalArgumentException("not found HttpMethod, please check url scheme");
            }
            Assert.isTrue(telnetClient.isConnected(), uri + "telnet fail");
            telnetClient.disconnect();
            log.info("url [{}] telnet success", uri);
        } catch (Exception e) {
            log.error("url:[{}] connection fail,please check network way", uri.toString());
            if (telnetProperties.isLogExceptionStack()) {
                log.error(e.getMessage(), e);
            }
            throw new TelnetException(uri.toString());
        }
        return true;
    }

    /**
     * http Scheme 类别
     */
    @AllArgsConstructor
    @Getter
    private enum HttpMethod {
        /**
         * http 请求
         */
        HTTP(80),
        /**
         * https 请求
         */
        HTTPS(443);
        /**
         * 默认端口
         */
        private final int defaultPort;

        private static Optional<HttpMethod> getHttpMethod(URL url) {
            return Arrays.stream(values())
                    .filter(httpMethod -> httpMethod.name().equalsIgnoreCase(url.getProtocol()))
                    .findFirst();
        }

        /**
         * URL转换为string
         *
         * @param url
         * @return
         */
        public static String covertURL2Str(URL url) {
            return getHttpMethod(url).map(httpMethod -> url.getProtocol() + "://"
                            + url.getHost() + ":"
                            + (url.getPort() < 0 ? httpMethod.getDefaultPort() : url.getPort()))
                    .orElse(null);
        }
    }

    public static class DefaultMapEntry implements Map.Entry<String, Object> {
        private String key;
        private Object value;

        DefaultMapEntry(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public Object getValue() {
            return this.value;
        }

        @Override
        public Object setValue(Object value) {
            return value;
        }

    }
}

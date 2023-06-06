package com.springcloud.base.core.list;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/10 11:20
 */
public class ListToMapConverter<T, K, V> {

    public Map<K, V> convert(List<T> list, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return list.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public static Map<String, String> convert(List<String> list) {
        return list.stream().collect(Collectors.toMap(s -> s, s -> s));
    }
}

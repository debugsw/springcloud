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

    /**
     * 三个类型参数：T表示List中元素的类型，K表示Map的键类型，V表示Map的值类型。该类有一个convert方法，该方法接受一个List，以及两个用于生
     * 成Map键和值的函数keyMapper和valueMapper。在方法内部，我们使用Java 8的流API将List转换为Map
     *
     * @param list
     * @param keyMapper
     * @param valueMapper
     * @return
     */
    public Map<K, V> convert(List<T> list, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return list.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * 静态方法convert，该方法接受一个List<String>，并将其转换为Map<String, String>。在方法内部，我们使用Java 8的流API将List转换为
     * Map，使用lambda表达式s -> s作为键选择器函数，使用s -> s作为值选择器函数。这两个lambda表达式的含义是将List中的每个字符串都作为Map
     * 的键和值
     *
     * @param list
     * @return
     */
    public static Map<String, String> convert(List<String> list) {
        return list.stream().collect(Collectors.toMap(s -> s, s -> s));
    }
}

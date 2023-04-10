package com.springcloud.base.core.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/10 11:31
 */
public class MapToListConverter {

    /**
     * 该方法接受一个Map<K, V>，并将其转换为List<Map.Entry<K, V>>。在方法内部，我们使用Map.entrySet()方法将Map中的每个键值对转换为一
     * 个Map.Entry<K, V>对象，并使用ArrayList构造函数将这些对象放入一个新的List中。最后，我们返回生成的List。
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> List<Map.Entry<K, V>> convert(Map<K, V> map) {
        return new ArrayList<>(map.entrySet());
    }
}

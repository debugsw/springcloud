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

    public static <K, V> List<Map.Entry<K, V>> convert(Map<K, V> map) {
        return new ArrayList<>(map.entrySet());
    }
}

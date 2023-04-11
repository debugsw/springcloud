package com.springcloud.base.core.map;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/1/28 10:57
 */
public class ParamMap<K, V> extends HashMap<K, V> {

    public String getAsString(String key) {
        return MapUtils.getAsString(this, key);
    }

    public Integer getAsInteger(String key) {
        return MapUtils.getAsInteger(this, key);
    }

    public Long getAsLong(String key) {
        return MapUtils.getAsLong(this, key);
    }

    public Double getAsDouble(String key) {
        return MapUtils.getAsDouble(this, key);
    }

    public Float getAsFloat(String key) {
        return MapUtils.getAsFloat(this, key);
    }

    public List getAsList(String key) {
        return MapUtils.getAsList(this, key);
    }

    public Boolean getAsBoolean(String key) {
        return MapUtils.getAsBoolean(this, key);
    }

    public static ParamMap<Object, Object> builder() {
        return new ParamMap<>();
    }

    /**
     * 追加
     *
     * @param key
     * @param value
     * @return
     */
    public ParamMap<K, V> append(K key, V value) {
        this.put(key, value);
        return this;
    }
}

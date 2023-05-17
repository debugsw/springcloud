package com.spring.cloud.base.jwt.config;

import com.spring.cloud.base.jwt.json.JSONArray;

/**
 * @Author: ls
 * @Description: JSON列表的序列化接口
 * @Date: 2023/4/25 13:36
 */
@FunctionalInterface
public interface JSONArraySerializer<V> extends JSONSerializer<JSONArray, V>{}

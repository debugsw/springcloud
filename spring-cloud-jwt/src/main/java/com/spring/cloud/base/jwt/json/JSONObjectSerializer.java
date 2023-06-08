package com.spring.cloud.base.jwt.json;

/**
 * @Author: ls
 * @Description: 对象的序列化接口，用于将特定对象序列化为JSONObject
 * @Date: 2023/4/25 13:36
 */
@FunctionalInterface
public interface JSONObjectSerializer<V> extends JSONSerializer<JSONObject, V> {}

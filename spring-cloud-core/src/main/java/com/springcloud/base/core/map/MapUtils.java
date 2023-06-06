package com.springcloud.base.core.map;

import com.springcloud.base.core.bean.BeanWrapper;
import com.springcloud.base.core.exception.DefaultException;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/1/28 10:58
 */
public class MapUtils {

    public static String getAsString(Map map, String key) {
        Object value = map.get(key);
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    public static Integer getAsInteger(Map map, String key) {
        Object value = map.get(key);
        if (value != null) {
            return Double.valueOf(value.toString()).intValue();
        }
        return null;
    }

    public static Long getAsLong(Map map, String key) {
        Object value = map.get(key);
        if (value != null) {
            BigDecimal bigDecimal = new BigDecimal(value.toString());
            return bigDecimal.longValue();
        }
        return null;
    }

    public static Double getAsDouble(Map map, String key) {
        Object value = map.get(key);
        if (value != null) {
            return Double.parseDouble(value.toString());
        }
        return null;
    }

    public static Float getAsFloat(Map map, String key) {
        Object value = map.get(key);
        if (value != null) {
            return Float.parseFloat(value.toString());
        }
        return null;
    }

    public static List getAsList(Map map, String key) {
        Object value = map.get(key);
        if (value != null) {
            if (value instanceof List<?>) {
                return (List<?>) value;
            } else {
                throw DefaultException.defaultException("对象非数组");
            }
        } else {
            return new ArrayList<>();
        }
    }

    public static String[] getAsArray(Map map, String key) {
        Object value = map.get(key);
        if (value != null) {
            if (value instanceof String[]) {
                return (String[]) value;
            } else {
                throw DefaultException.defaultException("对象非数组");
            }
        } else {
            String[] arr = {};
            return arr;
        }
    }

    public static Boolean getAsBoolean(Map map, String key) {
        Object value = map.get(key);
        if (value != null) {
            return Boolean.valueOf(value.toString());
        }
        return null;
    }

    @SneakyThrows
    public static <T> T getAsClass(Map map, String key, Class<T> tClass) {
        Object value = map.get(key);
        if (value != null) {
            T t = tClass.newInstance();
            BeanWrapper.copyProperties(value, t);
            return t;
        }
        return null;
    }
}

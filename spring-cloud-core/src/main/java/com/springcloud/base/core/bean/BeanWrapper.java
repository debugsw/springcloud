package com.springcloud.base.core.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcloud.base.core.exception.DefaultException;
import com.springcloud.base.core.json.JsonConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;

import java.io.IOException;
import java.util.*;

/**
 * @Author: ls
 * @Description: 类构造器
 * @Date: 2023/1/28 10:52
 */
@Slf4j
public class BeanWrapper {

    /**
     * 忽略在JSON字符串中存在，而在Java中不存在的属性
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        JsonConfiguration.initConfiguration(BeanWrapper.OBJECT_MAPPER);
    }

    /**
     * 复制非空属性
     *
     * @param source 源对象
     * @param blank  需要填充的对象
     * @param <T>    泛型
     * @return 复制之后的对象
     */
    public static <S, T> T copyNotNullProperties(S source, T blank) {
        String[] nullPropertiesName = getNullPropertyNames(source);
        BeanUtils.copyProperties(source, blank, nullPropertiesName);
        return blank;
    }

    /**
     * 复制属性
     *
     * @param source 源对象
     * @param blank  需要填充的对象
     * @param <T>    泛型
     * @return 复制之后的对象
     */
    public static <S, T> T copyProperties(S source, T blank) {
        BeanUtils.copyProperties(source, blank);
        return blank;
    }

    /**
     * 强制转化成对象
     *
     * @param source 源对象
     * @param tClass 需要转换之后的对象类型
     * @param <S>    泛型对象
     * @return 转换后的对象
     */
    public static <S, T> T castTo(S source, Class<T> tClass) {
        String sourceJson = writeValueAsString(source);
        return readValueAsString(sourceJson, tClass);
    }

    /**
     * 批量转换
     *
     * @param sourceList 源对象列表
     * @param tClass     目标对象类型
     * @param <S>        源对象泛型
     * @param <T>        目标对象泛型
     * @return 转换之后的列表
     */
    public static <S, T> List<T> arrCastTo(Collection<S> sourceList, Class<T> tClass) {
        List<T> tList = new ArrayList<>();
        for (S s : sourceList) {
            tList.add(castTo(s, tClass));
        }
        return tList;
    }

    /**
     * 自定义转换
     *
     * @param source            源对象列表
     * @param tClass            目标对象类型
     * @param objectCastHandler 转换处理器
     * @param <S>               源对象泛型
     * @param <T>               目标对象泛型
     * @return 转换之后的列表
     */
    public static <S, T> T castTo(S source, Class<T> tClass, ObjectCastHandler<S, T> objectCastHandler) {
        try {
            T targetObject = tClass.newInstance();
            return objectCastHandler.cast(source, targetObject);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getLocalizedMessage());
            throw DefaultException.defaultException("类型转换异常");
        }
    }

    /**
     * 批量自定义转换
     *
     * @param sourceList        源对象列表
     * @param tClass            目标对象类型
     * @param objectCastHandler 转换处理器
     * @param <S>               源对象泛型
     * @param <T>               目标对象泛型
     * @return 转换之后的列表
     */
    public static <S, T> List<T> arrCastTo(Collection<S> sourceList, Class<T> tClass, ObjectCastHandler<S, T> objectCastHandler) {
        List<T> tList = new ArrayList<>();
        for (S s : sourceList) {
            tList.add(castTo(s, tClass, objectCastHandler));
        }
        return tList;
    }

    /**
     * 转换JSON
     *
     * @param object json对象
     * @return 返回值
     */
    private static String writeValueAsString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
            throw jsonException();
        }
    }

    /**
     * 读取JSON
     *
     * @param json   json
     * @param tClass 泛型对象Class
     * @param <T>    泛型对象
     * @return 泛型对象
     */
    private static <T> T readValueAsString(String json, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(json, tClass);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw jsonException();
        }
    }


    private static DefaultException jsonException() {
        return DefaultException.defaultException("对象转换异常");
    }

    /**
     * 获取对象为空的属性
     *
     * @param source 获取值为空的属性
     * @return 返回一个数组
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapperImpl src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}

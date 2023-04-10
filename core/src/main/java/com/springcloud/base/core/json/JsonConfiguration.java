package com.springcloud.base.core.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: ls
 * @Description: json配置
 * @Date: 2023/1/28 10:52
 */
public class JsonConfiguration {

    private static final DateTimeFormatter DATETIME_FORMAT_MATCHER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter DATE_FORMAT_MATCHER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter TIME_FORMAT_MATCHER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void initConfiguration(ObjectMapper objectMapper) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule simpleModule = new SimpleModule();

        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        simpleModule.addSerializer(new LocalDateTimeSerializer(DATETIME_FORMAT_MATCHER));
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATETIME_FORMAT_MATCHER));

        simpleModule.addSerializer(new LocalTimeSerializer(TIME_FORMAT_MATCHER));
        simpleModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(TIME_FORMAT_MATCHER));

        simpleModule.addSerializer(new LocalDateSerializer(DATE_FORMAT_MATCHER));
        simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMAT_MATCHER));

        objectMapper.registerModule(simpleModule);
    }
}


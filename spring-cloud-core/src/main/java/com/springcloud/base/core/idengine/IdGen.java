package com.springcloud.base.core.idengine;

import cn.hutool.core.util.RandomUtil;

/**
 * @Author: ls
 * @Description: 获取唯一ID
 * @Date: 2023/1/28 11:10
 */
public class IdGen {

    private static final SnowFlowerIdGenerator SNOW_FLOWER_ID_GENERATOR;

    static {
        // 默认设置雪花算法的workerId为0
        SNOW_FLOWER_ID_GENERATOR = new SnowFlowerIdGenerator();
        SNOW_FLOWER_ID_GENERATOR.initialize(RandomUtil.randomInt(0, 1024));
    }

    public static Long nextId() {
        return SNOW_FLOWER_ID_GENERATOR.nextId();
    }

    public static SnowFlowerIdGenerator getIdEngine() {
        return SNOW_FLOWER_ID_GENERATOR;
    }

}

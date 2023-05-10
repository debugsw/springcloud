package com.springcloud.base.core.random;

import java.util.UUID;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/10 14:41
 */
public class RandomUtils {

    /**
     * 随机生成指定长度的 随机数字 不能超过int 大小
     *
     * @param length
     * @return
     */
    public static String random(int length) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < length; j++) {
            
            int intValN = (int) (Math.random() * 10 + 48);
            sb.append((char) intValN);
        }
        return sb.toString();
    }

    /**
     * 随机生成指定长度的 随机字符串
     *
     * @param length
     * @return
     */
    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < length; j++) {
            
            int intValL = (int) (Math.random() * 26 + 97);
            
            int intValU = (int) (Math.random() * 26 + 65);
            
            int intValN = (int) (Math.random() * 10 + 48);

            int intVal = 0;
            int r = (int) (Math.random() * 3);

            if (r == 0) {
                intVal = intValL;
            } else if (r == 1) {
                intVal = intValU;
            } else {
                intVal = intValN;
            }

            sb.append((char) intVal);
        }
        return sb.toString();
    }

    /**
     * 大写不带-的UUID
     *
     * @return
     */
    public static String randomUpCaseUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}

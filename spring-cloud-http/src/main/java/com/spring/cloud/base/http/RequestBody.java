package com.spring.cloud.base.http;

import com.spring.cloud.base.utils.utils.IoUtil;

import java.io.OutputStream;

/**
 * @Author: ls
 * @Description: 定义请求体接口
 * @Date: 2023/4/26 15:00
 */
public interface RequestBody {

    /**
     * 写出数据，不关闭流
     *
     * @param out out流
     */
    void write(OutputStream out);

    /**
     * 写出并关闭{@link OutputStream}
     *
     * @param out {@link OutputStream}
     */
    default void writeClose(OutputStream out) {
        try {
            write(out);
        } finally {
            IoUtil.close(out);
        }
    }
}

package com.spring.cloud.base.http;

import com.spring.cloud.base.utils.IoUtil;

import java.io.OutputStream;

/**
 * @Author: ls
 * @Description: bytes类型的Http
 * @Date: 2023/4/26 15:00
 */
public class BytesBody implements RequestBody {

    private final byte[] content;

    /**
     * 创建 Http request body
     *
     * @param content body内容，编码后
     * @return BytesBody
     */
    public static BytesBody create(byte[] content) {
        return new BytesBody(content);
    }

    /**
     * 构造
     *
     * @param content Body内容，编码后
     */
    public BytesBody(byte[] content) {
        this.content = content;
    }

    @Override
    public void write(OutputStream out) {
        IoUtil.write(out, false, content);
    }
}

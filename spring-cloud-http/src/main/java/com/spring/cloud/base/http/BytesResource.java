package com.spring.cloud.base.http;

import com.spring.cloud.base.utils.exception.IORuntimeException;
import com.spring.cloud.base.utils.map.Resource;
import com.spring.cloud.base.utils.str.StrUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/26 15:00
 */
public class BytesResource implements Resource, Serializable {
    private static final long serialVersionUID = 1L;

    private final byte[] bytes;
    private final String name;

    /**
     * 构造
     *
     * @param bytes 字节数组
     */
    public BytesResource(byte[] bytes) {
        this(bytes, null);
    }

    /**
     * 构造
     *
     * @param bytes 字节数组
     * @param name  资源名称
     */
    public BytesResource(byte[] bytes, String name) {
        this.bytes = bytes;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public InputStream getStream() {
        return new ByteArrayInputStream(this.bytes);
    }

    @Override
    public String readStr(Charset charset) throws IORuntimeException {
        return StrUtil.str(this.bytes, charset);
    }

    @Override
    public byte[] readBytes() throws IORuntimeException {
        return this.bytes;
    }

}

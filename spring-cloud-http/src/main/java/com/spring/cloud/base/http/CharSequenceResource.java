package com.spring.cloud.base.http;

import com.spring.cloud.base.utils.utils.CharsetUtil;
import com.spring.cloud.base.utils.utils.IoUtil;
import com.spring.cloud.base.utils.exception.IORuntimeException;
import com.spring.cloud.base.utils.map.Resource;
import com.spring.cloud.base.utils.str.StrUtil;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/26 15:00
 */
public class CharSequenceResource implements Resource, Serializable {
    private static final long serialVersionUID = 1L;

    private final CharSequence data;
    private final CharSequence name;
    private final Charset charset;

    /**
     * 构造，使用UTF8编码
     *
     * @param data 资源数据
     */
    public CharSequenceResource(CharSequence data) {
        this(data, null);
    }

    /**
     * 构造，使用UTF8编码
     *
     * @param data 资源数据
     * @param name 资源名称
     */
    public CharSequenceResource(CharSequence data, String name) {
        this(data, name, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 构造
     *
     * @param data    资源数据
     * @param name    资源名称
     * @param charset 编码
     */
    public CharSequenceResource(CharSequence data, CharSequence name, Charset charset) {
        this.data = data;
        this.name = name;
        this.charset = charset;
    }

    @Override
    public String getName() {
        return StrUtil.str(this.name);
    }

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public InputStream getStream() {
        return new ByteArrayInputStream(readBytes());
    }

    @Override
    public BufferedReader getReader(Charset charset) {
        return IoUtil.getReader(new StringReader(this.data.toString()));
    }

    @Override
    public String readStr(Charset charset) throws IORuntimeException {
        return this.data.toString();
    }

    @Override
    public byte[] readBytes() throws IORuntimeException {
        return this.data.toString().getBytes(this.charset);
    }

}

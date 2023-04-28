package com.spring.cloud.base.http;

import com.spring.cloud.base.utils.map.Resource;

import java.io.OutputStream;

/**
 * @Author: ls
 * @Description: 主要发送编码后的表单数据或rest
 * @Date: 2023/4/26 15:00
 */
public class ResourceBody implements RequestBody {

    private final Resource resource;

    /**
     * 创建 Http request body
     *
     * @param resource body内容，编码后
     * @return BytesBody
     */
    public static ResourceBody create(Resource resource) {
        return new ResourceBody(resource);
    }

    /**
     * 构造
     *
     * @param resource Body内容，编码后
     */
    public ResourceBody(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void write(OutputStream out) {
        if (null != this.resource) {
            this.resource.writeTo(out);
        }
    }
}

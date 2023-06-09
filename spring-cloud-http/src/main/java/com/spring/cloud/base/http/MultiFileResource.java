package com.spring.cloud.base.http;

import com.spring.cloud.base.utils.file.FileResource;
import com.spring.cloud.base.utils.map.Resource;

import java.io.File;
import java.util.Collection;

/**
 * @Author: ls
 * @Description: 多资源组合资源
 * @Date: 2023/4/26 15:00
 */
public class MultiFileResource extends MultiResource {
    private static final long serialVersionUID = 1L;

    /**
     * 构造
     *
     * @param files 文件资源列表
     */
    public MultiFileResource(Collection<File> files) {
        add(files);
    }

    /**
     * 构造
     *
     * @param files 文件资源列表
     */
    public MultiFileResource(File... files) {
        add(files);
    }

    /**
     * 增加文件资源
     *
     * @param files 文件资源
     * @return this
     */
    public MultiFileResource add(File... files) {
        for (File file : files) {
            this.add(new FileResource(file));
        }
        return this;
    }

    /**
     * 增加文件资源
     *
     * @param files 文件资源
     * @return this
     */
    public MultiFileResource add(Collection<File> files) {
        for (File file : files) {
            this.add(new FileResource(file));
        }
        return this;
    }

    @Override
    public MultiFileResource add(Resource resource) {
        return (MultiFileResource) super.add(resource);
    }
}

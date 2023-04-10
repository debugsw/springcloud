package com.springcloud.base.core.tree;

import java.io.Serializable;
import java.util.Collection;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/1/28 11:04
 */
public interface TreeModel<T extends TreeModel<?>> {

    /**
     * 获取ID
     *
     * @return id
     */
    Serializable getId();

    /**
     * 获取父级ID
     *
     * @return parentId
     */
    Serializable getParentId();

    /**
     * 获取子类
     *
     * @return child
     */
    Collection<T> getChildren();

    /**
     * 设置子类
     *
     * @param modelCollection child
     */
    void setChildren(Collection<T> modelCollection);
}

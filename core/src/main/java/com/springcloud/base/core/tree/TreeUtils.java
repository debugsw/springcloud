package com.springcloud.base.core.tree;

import com.springcloud.base.core.bean.BeanWrapper;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: ls
 * @Description: 树形结构工具类
 * @Date: 2023/1/28 11:04
 */
public class TreeUtils {

    /**
     * 两层循环实现建树
     *
     * @param treeNodes 传入的树节点列表
     * @return 树形结构
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <PK extends Serializable, MODEL extends TreeModel<MODEL>> List<MODEL> build(Collection<MODEL> treeNodes, PK root) {

        List<MODEL> trees = new ArrayList<>();
        List<MODEL> caches = new ArrayList<>();

        for (MODEL treeNode : treeNodes) {
            MODEL cacheModel = (MODEL) BeanWrapper.castTo(treeNode, treeNode.getClass());
            caches.add(cacheModel);
        }

        for (MODEL treeNode : caches) {

            if (root.equals(treeNode.getParentId())) {
                trees.add(treeNode);
            }

            for (MODEL it : caches) {
                if (it.getParentId().equals(treeNode.getId())) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<>());
                    }
                    treeNode.getChildren().add(it);
                }
            }
        }
        return trees;
    }
}

package com.springcloud.base.core.auth;

import com.springcloud.base.core.tree.TreeModel;
import lombok.Data;

import java.util.Collection;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/10 15:05
 */
@Data
public class OrgInfoModel implements TreeModel<OrgInfoModel> {

    //ID
    private String id;

    //编码
    private String code;

    //父级ID，顶层为0，只有type=系统时候，parentId才能为0
    private String parentId;

    //资源名称
    private String name;

    //子集
    private Collection<OrgInfoModel> children;
}

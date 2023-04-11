package com.springcloud.base.core.auth;

import com.springcloud.base.core.tree.TreeModel;
import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/10 15:03
 */
@Data
public class ResourceInfoModel  implements TreeModel<ResourceInfoModel> {

    //id
    private String id;

    //编码
    private String code;

    //最顶级的Code
    private String systemCode;

    //父级ID，顶层为0，只有type=系统时候，parentId才能为0
    private String parentId;

    //资源名称
    private String name;

    //当type = 页面/api，api路径
    private List<String> apiList;

    //资源类型,0 系统 1 分组 2 页面 3 按钮权限 4 api 权限
    private Integer type;

    //图标标识
    private String icon;

    //单页面内嵌多个页面资源是否展示 0 不展示 1 展示
    private Integer showFlag;

    //子集
    private Collection<ResourceInfoModel> children;

    @Override
    public void setChildren(Collection<ResourceInfoModel> modelCollection) {

    }
}

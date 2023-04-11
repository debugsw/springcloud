package com.spring.cloud.base.common.auth;

import lombok.Data;

import java.util.List;

/**
 * @Author: ls
 * @Description: 角色基础信息
 * @Date: 2023/4/10 15:01
 */
@Data
public class RoleInfoModel {
    //ID
    private String id;

    //角色编码
    private String code;

    //角色名称
    private String name;

    //角色拥有的资源
    private List<ResourceInfoModel> resourceInfo;

    //角色拥有的组织机构
    private List<OrgInfoModel> orgInfo;
}

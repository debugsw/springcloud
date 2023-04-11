package com.springcloud.base.core.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/10 14:53
 */
@Data
public class PermissionsModel implements Serializable {

    //登录凭证SessionId
    private String sessionId;

    //用户详情
    private UserInfoModel userInfo;

    //组织机构详情
    private DeptInfoModel deptInfo;

    //角色详情
    private Collection<RoleInfoModel> roleInfoList;

    //所有的资源
    private Collection<ResourceInfoModel> allResourcesList;

    //所有的组织
    private Collection<OrgInfoModel> allOrgList;

}

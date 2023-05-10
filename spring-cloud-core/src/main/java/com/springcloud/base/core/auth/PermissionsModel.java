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

    
    private String sessionId;

    
    private UserInfoModel userInfo;

    
    private DeptInfoModel deptInfo;

    
    private Collection<RoleInfoModel> roleInfoList;

    
    private Collection<ResourceInfoModel> allResourcesList;

    
    private Collection<OrgInfoModel> allOrgList;

}

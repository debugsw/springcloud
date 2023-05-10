package com.springcloud.base.core.auth;

import lombok.Data;

import java.util.List;

/**
 * @Author: ls
 * @Description: 角色基础信息
 * @Date: 2023/4/10 15:01
 */
@Data
public class RoleInfoModel {
    
    private String id;

    
    private String code;

    
    private String name;

    
    private List<ResourceInfoModel> resourceInfo;

    
    private List<OrgInfoModel> orgInfo;
}

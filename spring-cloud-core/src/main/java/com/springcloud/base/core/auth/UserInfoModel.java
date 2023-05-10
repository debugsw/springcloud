package com.springcloud.base.core.auth;

import lombok.Data;

/**
 * @Author: ls
 * @Description: 用户基础信息
 * @Date: 2023/4/10 14:59
 */
@Data
public class UserInfoModel {

    
    private String id;

    
    private String username;

    
    private String nickname;

    
    private String mobile;

    
    private Integer gender;

    
    private String deptId;

    
    private DeptInfoModel deptInfo;

}

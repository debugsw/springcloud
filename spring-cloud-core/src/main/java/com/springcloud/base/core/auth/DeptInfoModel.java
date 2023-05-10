package com.springcloud.base.core.auth;

import lombok.Data;

/**
 * @Author: ls
 * @Description: 部门机构基础信息
 * @Date: 2023/4/10 15:00
 */
@Data
public class DeptInfoModel {

    
    private String id;

    
    private String code;

    
    private String name;

    
    private String parentId;

}

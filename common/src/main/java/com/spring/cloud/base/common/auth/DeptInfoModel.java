package com.spring.cloud.base.common.auth;

import lombok.Data;

/**
 * @Author: ls
 * @Description: 部门机构基础信息
 * @Date: 2023/4/10 15:00
 */
@Data
public class DeptInfoModel {

    //ID
    private String id;

    //编号
    private String code;

    //组织名称
    private String name;

    //父级ID，默认O就是顶层
    private String parentId;

}

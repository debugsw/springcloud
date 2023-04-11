package com.springcloud.base.core.auth;

import lombok.Data;

/**
 * @Author: ls
 * @Description: 用户基础信息
 * @Date: 2023/4/10 14:59
 */
@Data
public class UserInfoModel {

    //用户ID
    private String id;

    //帐号
    private String username;

    //昵称
    private String nickname;

    //绑定手机号
    private String mobile;

    //性别 1 男 2 女
    private Integer gender;

    //组织机构ID
    private String deptId;

    //组织机构Code
    private DeptInfoModel deptInfo;

}

package com.springcloud.base.core.auth;

import lombok.Data;

/**
 * @Author: ls
 * @Description: 后台JWT模型对象
 * @Date: 2023/4/10 15:09
 */
@Data
public class JwtInfoModel {
    private String token;
    private String userId;
    private String name;
}

package com.spring.cloud.base.utils;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;

import java.util.Date;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/12 15:30
 */
@Table("tb_account")
public class Account {

    @Id(keyType = KeyType.Auto)
    private Long id;
    private String userName;
    private Integer age;
    private Date birthday;
}

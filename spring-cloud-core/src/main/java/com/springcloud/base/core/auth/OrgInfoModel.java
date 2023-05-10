package com.springcloud.base.core.auth;

import com.springcloud.base.core.tree.TreeModel;
import lombok.Data;

import java.util.Collection;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/10 15:05
 */
@Data
public class OrgInfoModel implements TreeModel<OrgInfoModel> {

    
    private String id;

    
    private String code;

    
    private String parentId;

    
    private String name;

    
    private Collection<OrgInfoModel> children;
}

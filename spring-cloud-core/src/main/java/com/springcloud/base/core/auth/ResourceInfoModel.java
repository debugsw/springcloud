package com.springcloud.base.core.auth;

import com.springcloud.base.core.tree.TreeModel;
import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/10 15:03
 */
@Data
public class ResourceInfoModel  implements TreeModel<ResourceInfoModel> {

    
    private String id;

    
    private String code;

    
    private String systemCode;

    
    private String parentId;

    
    private String name;

    
    private List<String> apiList;

    
    private Integer type;

    
    private String icon;

    
    private Integer showFlag;

    
    private Collection<ResourceInfoModel> children;

    @Override
    public void setChildren(Collection<ResourceInfoModel> modelCollection) {

    }
}

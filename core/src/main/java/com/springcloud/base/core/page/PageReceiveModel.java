package com.springcloud.base.core.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/1/28 11:00
 */
@ApiModel("分页默认参数对象")
public class PageReceiveModel {

    @ApiModelProperty("开始页")
    private Integer pageStart;

    @ApiModelProperty("每页展示条数")
    private Integer pageLimit;

    public Integer getPageStart() {
        return pageStart != null ? pageStart : 1;
    }

    public void setPageStart(Integer pageStart) {
        this.pageStart = pageStart;
    }

    public Integer getPageLimit() {
        return pageLimit != null ? pageLimit : 15;
    }

    public void setPageLimit(Integer pageLimit) {
        this.pageLimit = pageLimit;
    }
}

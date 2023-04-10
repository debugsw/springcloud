package com.springcloud.base.core.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ls
 * @Description: 分页返回模型
 * @Date: 2023/1/28 11:01
 */
@Data
@ApiModel("分页默认返回值对象")
public class PageResultModel<T> implements Serializable {

    @ApiModelProperty("数据总条数")
    private long total;

    @ApiModelProperty("每页显示条数")
    private long size;

    @ApiModelProperty("当前页")
    private long current;

    @ApiModelProperty("数据")
    private List<T> data;

    public PageResultModel() {
    }

    public PageResultModel(PageReceiveModel pageReceiveModelVo) {
        this.setCurrent(pageReceiveModelVo.getPageStart());
        this.setSize(pageReceiveModelVo.getPageLimit());
    }

    public PageResultModel(PageReceiveModel pageReceiveModelVo, long total, List<T> data) {
        this.current = pageReceiveModelVo.getPageStart();
        this.size = pageReceiveModelVo.getPageLimit();
        this.total = total;
        this.data = data;
    }

    /**
     * 将当前pageResultModel 转换成对应的子类对象
     * 有可能是dto 对象
     * 或者是vo对象
     *
     * @param prmClass 需要转换的对象类型
     * @param <PRM>    对象类型反省
     * @return 返回对象
     */
    @SneakyThrows
    public <PRM extends PageResultModel<T>> PRM wrapper(Class<PRM> prmClass) {
        PRM prm = prmClass.newInstance();
        prm.setTotal(this.getTotal());
        prm.setSize(this.getSize());
        prm.setData(this.getData());
        prm.setCurrent(this.current);
        return prm;
    }
}

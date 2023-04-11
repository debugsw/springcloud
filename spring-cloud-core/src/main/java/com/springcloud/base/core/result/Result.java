package com.springcloud.base.core.result;

import com.springcloud.base.core.exception.AssertUtils;
import com.springcloud.base.core.exception.DefaultException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: ls
 * @Date: 2023/1/17 17:50
 * @Description: 统一返回结构
 **/
@Getter
@Setter
@ApiModel("系统统一返回格式")
public class Result<T> {

    /**
     * 操作结果编码 详见 ResultCode.java
     */
    @ApiModelProperty("操作码（正常条件下为 '1'）")
    private String code;

    /**
     * 操作结果提示
     */
    @ApiModelProperty("操作提示")
    private String msg;

    /**
     * 错误简略信息
     */
    @ApiModelProperty("简要错误提示")
    private String err;

    /**
     * 主体信息
     */
    private T data;

    /**
     * 默认返回的模型
     *
     * @return
     */
    public static Result<?> ok() {
        Result<?> result = new Result<>();
        result.setCode(ResultCode.DEFAULT_SUCCESS);
        result.setMsg("操作成功");
        return result;
    }

    /**
     * 返回带内容的模型
     *
     * @param data 内容
     * @param <T>  内容类型
     * @return Result
     */
    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.DEFAULT_SUCCESS);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 返回默认失败的模型
     *
     * @param msg 提示语
     * @return Result
     */
    public static Result<?> error(String msg) {
        Result<?> result = new Result<>();
        result.setCode(ResultCode.DEFAULT_FAIL);
        result.setMsg(msg);
        return result;
    }

    /**
     * 根据异常信息返回默认的失败模型
     *
     * @param defaultException 异常
     * @return Result
     */
    public static Result<?> error(DefaultException defaultException) {
        Result<Object> result = new Result<>();
        result.setCode(defaultException.getCode());
        result.setMsg(defaultException.getMessage());
        result.setErr(defaultException.getErr());
        result.setData(defaultException.getData());
        return result;
    }

    /**
     * 验证code并返回data
     *
     * @return
     */
    public T detection() {
        AssertUtils.isTrue(this.getCode().equals(ResultCode.DEFAULT_SUCCESS), this.msg);
        return this.data;
    }

    /**
     * 验证code 如果code != 1
     * 返回result自判断处理进行总结出来
     *
     * @param resultFallback
     */
    public T detection(ResultFallback<T> resultFallback) {
        return resultFallback.callback(this);
    }

}

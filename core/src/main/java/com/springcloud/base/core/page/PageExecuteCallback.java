package com.springcloud.base.core.page;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/1/28 11:01
 */
public interface PageExecuteCallback<T> {

    /**
     * page 对象执行回调
     *
     * @param page page对象
     * @return 返回Page对象
     */
    IPage<T> execute(IPage<T> page);
}

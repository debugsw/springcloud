package com.springcloud.base.core.vo.page;

import com.springcloud.base.core.page.PageResultModel;
import com.springcloud.base.core.vo.Vo;

/**
 * @Author: ls
 * @Date: 2019/6/18
 * @Description: 分页默认返回值Vo对象
 **/
public class PageResultVo<T> extends PageResultModel<T> implements Vo {

    private static final long serialVersionUID = -7021302852971165750L;

    /**
     * 将 PageResultModel 转换为 PageResultVo;
     *
     * @param pageResultModel pageResultModel
     * @param <S>             泛型对象
     * @return PageResultVo
     */
    public static <S> PageResultVo<S> newInstance(PageResultModel<S> pageResultModel) {
        PageResultVo<S> pageResultVo = new PageResultVo<>();
        pageResultVo.setData(pageResultModel.getData());
        pageResultVo.setCurrent(pageResultModel.getCurrent());
        pageResultVo.setSize(pageResultModel.getSize());
        pageResultVo.setTotal(pageResultModel.getTotal());
        return pageResultVo;
    }
}

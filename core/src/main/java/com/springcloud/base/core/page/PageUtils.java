package com.springcloud.base.core.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springcloud.base.core.bean.BeanWrapper;
import com.springcloud.base.core.bean.ObjectCastHandler;

import java.util.List;

/**
 * @Author: ls
 * @Description: 分页工具类
 * @Date: 2023/1/28 10:59
 */
public class PageUtils {

    /**
     * 默认转化
     *
     * @param page   从数据库查询出来的原始数据
     * @param tClass 需要转换成的目标类型
     * @param <S>    原始类型
     * @param <T>    目标类型
     * @return page
     */
    public static <S, T> PageResultModel<T> conversion(IPage<S> page, Class<T> tClass) {
        PageResultModel<T> pageResultDto = new PageResultModel<>();
        pageResultDto.setCurrent(page.getCurrent());
        pageResultDto.setSize(page.getSize());
        pageResultDto.setTotal(page.getTotal());
        List<T> tList = BeanWrapper.arrCastTo(page.getRecords(), tClass);
        pageResultDto.setData(tList);
        return pageResultDto;
    }

    /**
     * 自定义转换
     *
     * @param page   从数据库查询出来的原始数据
     * @param tClass 需要转换成的目标类型
     * @param <S>    原始类型
     * @param <T>    目标类型
     * @return page
     */
    public static <S, T> PageResultModel<T> conversion(IPage<S> page, Class<T> tClass, ObjectCastHandler<S, T> objectCastHandler) {
        PageResultModel<T> pageResult = new PageResultModel<>();
        pageResult.setCurrent(page.getCurrent());
        pageResult.setSize(page.getSize());
        pageResult.setTotal(page.getTotal());

        List<T> tList = BeanWrapper.arrCastTo(page.getRecords(), tClass, objectCastHandler);
        pageResult.setData(tList);

        return pageResult;
    }

    /**
     * 默认转化
     *
     * @param page   从数据库查询出来的原始数据
     * @param tClass 需要转换成的目标类型
     * @param <S>    原始类型
     * @param <T>    目标类型
     * @return page
     */
    public static <S, T> PageResultModel<T> conversion(PageResultModel<S> page, Class<T> tClass) {
        PageResultModel<T> pageResult = new PageResultModel<>();
        pageResult.setCurrent(page.getCurrent());
        pageResult.setSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        List<T> tList = BeanWrapper.arrCastTo(page.getData(), tClass);
        pageResult.setData(tList);
        return pageResult;
    }

    /**
     * 自定义转换
     *
     * @param page   从数据库查询出来的原始数据
     * @param tClass 需要转换成的目标类型
     * @param <S>    原始类型
     * @param <T>    目标类型
     * @return page
     */
    public static <S, T> PageResultModel<T> conversion(PageResultModel<S> page, Class<T> tClass, ObjectCastHandler<S, T> objectCastHandler) {
        PageResultModel<T> pageResult = new PageResultModel<>();
        pageResult.setCurrent(page.getCurrent());
        pageResult.setSize(page.getSize());
        pageResult.setTotal(page.getTotal());

        List<T> tList = BeanWrapper.arrCastTo(page.getData(), tClass, objectCastHandler);
        pageResult.setData(tList);

        return pageResult;
    }

    /**
     * 直接封装SQL执行过程
     *
     * @param start           start
     * @param limit           limit
     * @param executeCallback 执行回调
     * @param <S>             泛型类
     * @return 封装好的model
     */
    public static <S> PageResultModel<S> execute(Long start, Long limit, PageExecuteCallback<S> executeCallback) {
        Page<S> page = new Page<>();

        page.setCurrent(start != null ? start : 1);
        page.setSize(limit != null ? limit : 15);

        IPage<S> iPage = executeCallback.execute(page);

        PageResultModel<S> pageResult = new PageResultModel<>();
        pageResult.setCurrent(page.getCurrent());
        pageResult.setSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        pageResult.setData(iPage.getRecords());

        return pageResult;
    }

    /**
     * 直接封装SQL执行过程
     *
     * @param pageReceiveModel 分页对象
     * @param executeCallback  回调函数
     * @param <S>              泛型对象
     * @return 封装好的分页对象
     */
    public static <S> PageResultModel<S> execute(PageReceiveModel pageReceiveModel, PageExecuteCallback<S> executeCallback) {
        return execute(pageReceiveModel != null ? pageReceiveModel.getPageStart() : 1L, pageReceiveModel != null ? pageReceiveModel.getPageLimit() : 15L, executeCallback);
    }
}

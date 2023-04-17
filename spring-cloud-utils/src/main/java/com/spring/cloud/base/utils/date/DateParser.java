package com.spring.cloud.base.utils.date;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: ls
 * @Description: 日期解析接口用于解析日期字符串为
 * @Date: 2023/4/13 16:11
 */
public interface DateParser extends DateBasic {


	/**
	 * 将日期字符串解析并转换为Date对象
	 *
	 * @param source 日期字符串
	 * @return 结果
	 * @throws ParseException
	 */
	Date parse(String source) throws ParseException;

	/**
	 * 将日期字符串解析并转换为Date对象
	 *
	 * @param source 日期字符串
	 * @param pos    格式化
	 * @return 结果
	 */
	Date parse(String source, ParsePosition pos);

	/**
	 * 根据给定格式更新
	 *
	 * @param source   被转换的日期字符串
	 * @param pos      定义开始转换的位置,转换结束后更新转换到的位置
	 * @param calendar 对象
	 * @return 结果
	 */
	boolean parse(String source, ParsePosition pos, Calendar calendar);

}

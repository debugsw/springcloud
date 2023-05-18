package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.crypto.Validator;
import com.spring.cloud.base.utils.date.DatePattern;
import com.spring.cloud.base.utils.date.DateTime;
import com.spring.cloud.base.utils.date.DateUtil;
import com.spring.cloud.base.utils.str.StrUtil;
import com.spring.cloud.base.utils.utils.CharUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: ls
 * @Description: 身份证相关工具类
 * @Date: 2023/4/13 16:11
 */
public class IdCardUtil {

	/**
	 * 中国公民身份证号码最小长度。
	 */
	private static final int CHINA_ID_MIN_LENGTH = 15;
	/**
	 * 中国公民身份证号码最大长度。
	 */
	private static final int CHINA_ID_MAX_LENGTH = 18;
	/**
	 * 每位加权因子
	 */
	private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
	/**
	 * 省市代码表
	 */
	private static final Map<String, String> CITY_CODES = new HashMap<>();
	/**
	 * 台湾身份首字母对应数字
	 */
	private static final Map<Character, Integer> TW_FIRST_CODE = new HashMap<>();

	static {
		CITY_CODES.put("11", "北京");
		CITY_CODES.put("12", "天津");
		CITY_CODES.put("13", "河北");
		CITY_CODES.put("14", "山西");
		CITY_CODES.put("15", "内蒙古");
		CITY_CODES.put("21", "辽宁");
		CITY_CODES.put("22", "吉林");
		CITY_CODES.put("23", "黑龙江");
		CITY_CODES.put("31", "上海");
		CITY_CODES.put("32", "江苏");
		CITY_CODES.put("33", "浙江");
		CITY_CODES.put("34", "安徽");
		CITY_CODES.put("35", "福建");
		CITY_CODES.put("36", "江西");
		CITY_CODES.put("37", "山东");
		CITY_CODES.put("41", "河南");
		CITY_CODES.put("42", "湖北");
		CITY_CODES.put("43", "湖南");
		CITY_CODES.put("44", "广东");
		CITY_CODES.put("45", "广西");
		CITY_CODES.put("46", "海南");
		CITY_CODES.put("50", "重庆");
		CITY_CODES.put("51", "四川");
		CITY_CODES.put("52", "贵州");
		CITY_CODES.put("53", "云南");
		CITY_CODES.put("54", "西藏");
		CITY_CODES.put("61", "陕西");
		CITY_CODES.put("62", "甘肃");
		CITY_CODES.put("63", "青海");
		CITY_CODES.put("64", "宁夏");
		CITY_CODES.put("65", "新疆");
		CITY_CODES.put("71", "台湾");
		CITY_CODES.put("81", "香港");
		CITY_CODES.put("82", "澳门");

		CITY_CODES.put("83", "台湾");
		CITY_CODES.put("91", "国外");

		TW_FIRST_CODE.put('A', 10);
		TW_FIRST_CODE.put('B', 11);
		TW_FIRST_CODE.put('C', 12);
		TW_FIRST_CODE.put('D', 13);
		TW_FIRST_CODE.put('E', 14);
		TW_FIRST_CODE.put('F', 15);
		TW_FIRST_CODE.put('G', 16);
		TW_FIRST_CODE.put('H', 17);
		TW_FIRST_CODE.put('J', 18);
		TW_FIRST_CODE.put('K', 19);
		TW_FIRST_CODE.put('L', 20);
		TW_FIRST_CODE.put('M', 21);
		TW_FIRST_CODE.put('N', 22);
		TW_FIRST_CODE.put('P', 23);
		TW_FIRST_CODE.put('Q', 24);
		TW_FIRST_CODE.put('R', 25);
		TW_FIRST_CODE.put('S', 26);
		TW_FIRST_CODE.put('T', 27);
		TW_FIRST_CODE.put('U', 28);
		TW_FIRST_CODE.put('V', 29);
		TW_FIRST_CODE.put('X', 30);
		TW_FIRST_CODE.put('Y', 31);
		TW_FIRST_CODE.put('W', 32);
		TW_FIRST_CODE.put('Z', 33);
		TW_FIRST_CODE.put('I', 34);
		TW_FIRST_CODE.put('O', 35);
	}

	/**
	 * 将15位身份证号码转换为18位
	 *
	 * @param idCard 15位身份编码
	 * @return 18位身份编码
	 */
	public static String convert15To18(String idCard) {
		StringBuilder idCard18;
		if (idCard.length() != CHINA_ID_MIN_LENGTH) {
			return null;
		}
		if (ReUtil.isMatch(PatternPool.NUMBERS, idCard)) {

			String birthday = idCard.substring(6, 12);
			Date birthDate = DateUtil.parse(birthday, "yyMMdd");

			int sYear = DateUtil.year(birthDate);
			if (sYear > 2000) {

				sYear -= 100;
			}
			idCard18 = StrUtil.builder().append(idCard, 0, 6).append(sYear).append(idCard.substring(8));

			char sVal = getCheckCode18(idCard18.toString());
			idCard18.append(sVal);
		} else {
			return null;
		}
		return idCard18.toString();
	}

	/**
	 * 将18位身份证号码转换为15位
	 *
	 * @param  idCard 18位身份编码
	 * @return 15位身份编码
	 */
	public static String convert18To15(String idCard) {
		if (StrUtil.isNotBlank(idCard) && IdCardUtil.isValidCard18(idCard)) {
			return idCard.substring(0, 6) + idCard.substring(8, idCard.length() - 1);
		}
		return idCard;
	}

	/**
	 * 是否有效身份证号，忽略X的大小写<br>
	 * 如果身份证号码中含有空格始终返回{@code false}
	 *
	 * @param idCard 身份证号，支持18位、15位和港澳台的10位
	 * @return 是否有效
	 */
	public static boolean isValidCard(String idCard) {
		if (StrUtil.isBlank(idCard)) {
			return false;
		}


		int length = idCard.length();
		switch (length) {
			case 18:
				return isValidCard18(idCard);
			case 15:
				return isValidCard15(idCard);
			case 10: {
				String[] cardVal = isValidCard10(idCard);
				return null != cardVal && "true".equals(cardVal[2]);
			}
			default:
				return false;
		}
	}

	/**
	 * <p>
	 * 判断18位身份证的合法性
	 * </p>
	 * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。<br>
	 * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
	 * <p>
	 * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
	 * </p>
	 * <ol>
	 * <li>第1、2位数字表示：所在省份的代码</li>
	 * <li>第3、4位数字表示：所在城市的代码</li>
	 * <li>第5、6位数字表示：所在区县的代码</li>
	 * <li>第7~14位数字表示：出生年、月、日</li>
	 * <li>第15、16位数字表示：所在地的派出所的代码</li>
	 * <li>第17位数字表示性别：奇数表示男性，偶数表示女性</li>
	 * <li>第18位数字是校检码，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示</li>
	 * </ol>
	 * <p>
	 * 第十八位数字(校验码)的计算方法为：
	 * <ol>
	 * <li>将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2</li>
	 * <li>将这17位数字和系数相乘的结果相加</li>
	 * <li>用加出来和除以11，看余数是多少</li>
	 * <li>余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2</li>
	 * <li>通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2</li>
	 * </ol>
	 * <ol>
	 * 	     <li>香港人在大陆的身份证，【810000】开头；同样可以直接获取到 性别、出生日期</li>
	 * 	     <li>81000019980902013X: 文绎循 男 1998-09-02</li>
	 * 	     <li>810000201011210153: 辛烨 男 2010-11-21</li>
	 * 	 </ol>
	 * 	 <ol>
	 *       <li>澳门人在大陆的身份证，【820000】开头；同样可以直接获取到 性别、出生日期</li>
	 *       <li>820000200009100032: 黄敬杰 男 2000-09-10</li>
	 *  </ol>
	 *  <ol>
	 *     <li>台湾人在大陆的身份证，【830000】开头；同样可以直接获取到 性别、出生日期</li>
	 *     <li>830000200209060065: 王宜妃 女 2002-09-06</li>
	 *     <li>830000194609150010: 苏建文 男 1946-09-14</li>
	 *     <li>83000019810715006X: 刁婉琇 女 1981-07-15</li>
	 * </ol>
	 *
	 * @param idCard 待验证的身份证
	 * @return 是否有效的18位身份证，忽略x的大小写
	 */
	public static boolean isValidCard18(String idCard) {
		return isValidCard18(idCard, true);
	}

	/**
	 * <p>
	 * 判断18位身份证的合法性
	 * </p>
	 * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。<br>
	 * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
	 * <p>
	 * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
	 * </p>
	 * <ol>
	 * <li>第1、2位数字表示：所在省份的代码</li>
	 * <li>第3、4位数字表示：所在城市的代码</li>
	 * <li>第5、6位数字表示：所在区县的代码</li>
	 * <li>第7~14位数字表示：出生年、月、日</li>
	 * <li>第15、16位数字表示：所在地的派出所的代码</li>
	 * <li>第17位数字表示性别：奇数表示男性，偶数表示女性</li>
	 * <li>第18位数字是校检码，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示</li>
	 * </ol>
	 * <p>
	 * 第十八位数字(校验码)的计算方法为：
	 * <ol>
	 * <li>将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2</li>
	 * <li>将这17位数字和系数相乘的结果相加</li>
	 * <li>用加出来和除以11，看余数是多少</li>
	 * <li>余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2</li>
	 * <li>通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2</li>
	 * </ol>
	 *
	 * @param idCard     待验证的身份证
	 * @param ignoreCase 是否忽略大小写。{@code true}则忽略X大小写，否则严格匹配大写。
	 * @return 是否有效的18位身份证
	 *
	 */
	public static boolean isValidCard18(String idCard, boolean ignoreCase) {
		if (CHINA_ID_MAX_LENGTH != idCard.length()) {
			return false;
		}


		final String proCode = idCard.substring(0, 2);
		if (null == CITY_CODES.get(proCode)) {
			return false;
		}


		if (!Validator.isBirthday(idCard.substring(6, 14))) {
			return false;
		}


		final String code17 = idCard.substring(0, 17);
		if (ReUtil.isMatch(PatternPool.NUMBERS, code17)) {

			char val = getCheckCode18(code17);

			return CharUtil.equals(val, idCard.charAt(17), ignoreCase);
		}
		return false;
	}

	/**
	 * 验证15位身份编码是否合法
	 *
	 * @param idCard 身份编码
	 * @return 是否合法
	 */
	public static boolean isValidCard15(String idCard) {
		if (CHINA_ID_MIN_LENGTH != idCard.length()) {
			return false;
		}
		if (ReUtil.isMatch(PatternPool.NUMBERS, idCard)) {

			String proCode = idCard.substring(0, 2);
			if (null == CITY_CODES.get(proCode)) {
				return false;
			}


			return Validator.isBirthday("19" + idCard.substring(6, 12));
		} else {
			return false;
		}
	}

	/**
	 * 验证10位身份编码是否合法
	 *
	 * @param idCard 身份编码
	 * @return 身份证信息数组
	 * <p>
	 * [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false) 若不是身份证件号码则返回null
	 * </p>
	 */
	public static String[] isValidCard10(String idCard) {
		if (StrUtil.isBlank(idCard)) {
			return null;
		}
		String[] info = new String[3];
		String card = idCard.replaceAll("[()]", "");
		if (card.length() != 8 && card.length() != 9 && idCard.length() != 10) {
			return null;
		}
		if (idCard.matches("^[a-zA-Z][0-9]{9}$")) {
			info[0] = "台湾";
			char char2 = idCard.charAt(1);
			if ('1' == char2) {
				info[1] = "M";
			} else if ('2' == char2) {
				info[1] = "F";
			} else {
				info[1] = "N";
				info[2] = "false";
				return info;
			}
			info[2] = isValidTWCard(idCard) ? "true" : "false";
		} else if (idCard.matches("^[157][0-9]{6}\\(?[0-9A-Z]\\)?$")) {
			info[0] = "澳门";
			info[1] = "N";
			info[2] = "true";
		} else if (idCard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) {
			info[0] = "香港";
			info[1] = "N";
			info[2] = isValidHKCard(idCard) ? "true" : "false";
		} else {
			return null;
		}
		return info;
	}

	/**
	 * 验证台湾身份证号码
	 *
	 * @param idCard 身份证号码
	 * @return 验证码是否符合
	 */
	public static boolean isValidTWCard(String idCard) {
		if (null == idCard || idCard.length() != 10) {
			return false;
		}
		final Integer iStart = TW_FIRST_CODE.get(idCard.charAt(0));
		if (null == iStart) {
			return false;
		}
		int sum = iStart / 10 + (iStart % 10) * 9;

		final String mid = idCard.substring(1, 9);
		final char[] chars = mid.toCharArray();
		int iflag = 8;
		for (char c : chars) {
			sum += Integer.parseInt(String.valueOf(c)) * iflag;
			iflag--;
		}

		final String end = idCard.substring(9, 10);
		return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.parseInt(end);
	}

	/**
	 * 验证香港身份证号码
	 *
	 * @param idCard 身份证号码
	 * @return 验证码是否符合
	 */
	public static boolean isValidHKCard(String idCard) {
		String card = idCard.replaceAll("[()]", "");
		int sum;
		if (card.length() == 9) {
			sum = (Character.toUpperCase(card.charAt(0)) - 55) * 9 + (Character.toUpperCase(card.charAt(1)) - 55) * 8;
			card = card.substring(1, 9);
		} else {
			sum = 522 + (Character.toUpperCase(card.charAt(0)) - 55) * 8;
		}


		String mid = card.substring(1, 7);
		String end = card.substring(7, 8);
		char[] chars = mid.toCharArray();
		int iflag = 7;
		for (char c : chars) {
			sum = sum + Integer.parseInt(String.valueOf(c)) * iflag;
			iflag--;
		}
		if ("A".equalsIgnoreCase(end)) {
			sum += 10;
		} else {
			sum += Integer.parseInt(end);
		}
		return sum % 11 == 0;
	}

	/**
	 * 根据身份编号获取生日，只支持15或18位身份证号码
	 *
	 * @param idCard 身份编号
	 * @return 生日(yyyyMMdd)
	 * @see #getBirth(String)
	 */
	public static String getBirthByIdCard(String idCard) {
		return getBirth(idCard);
	}

	/**
	 * 根据身份编号获取生日，只支持15或18位身份证号码
	 *
	 * @param idCard 身份编号
	 * @return 生日(yyyyMMdd)
	 */
	public static String getBirth(String idCard) {
		Assert.notBlank(idCard, "id card must be not blank!");
		final int len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = convert15To18(idCard);
		}

		return Objects.requireNonNull(idCard).substring(6, 14);
	}

	/**
	 * 从身份证号码中获取生日日期，只支持15或18位身份证号码
	 *
	 * @param idCard 身份证号码
	 * @return 日期
	 */
	public static DateTime getBirthDate(String idCard) {
		final String birthByIdCard = getBirthByIdCard(idCard);
		return null == birthByIdCard ? null : DateUtil.parse(birthByIdCard, DatePattern.PURE_DATE_FORMAT);
	}

	/**
	 * 根据身份编号获取年龄，只支持15或18位身份证号码
	 *
	 * @param idCard 身份编号
	 * @return 年龄
	 */
	public static int getAgeByIdCard(String idCard) {
		return getAgeByIdCard(idCard, DateUtil.date());
	}

	/**
	 * 根据身份编号获取指定日期当时的年龄年龄，只支持15或18位身份证号码
	 *
	 * @param idCard        身份编号
	 * @param dateToCompare 以此日期为界，计算年龄。
	 * @return 年龄
	 */
	public static int getAgeByIdCard(String idCard, Date dateToCompare) {
		String birth = getBirthByIdCard(idCard);
		return DateUtil.age(DateUtil.parse(birth, "yyyyMMdd"), dateToCompare);
	}

	/**
	 * 根据身份编号获取生日年，只支持15或18位身份证号码
	 *
	 * @param idCard 身份编号
	 * @return 生日(yyyy)
	 */
	public static Short getYearByIdCard(String idCard) {
		final int len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = convert15To18(idCard);
		}
		return Short.valueOf(Objects.requireNonNull(idCard).substring(6, 10));
	}

	/**
	 * 根据身份编号获取生日月，只支持15或18位身份证号码
	 *
	 * @param idCard 身份编号
	 * @return 生日(MM)
	 */
	public static Short getMonthByIdCard(String idCard) {
		final int len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = convert15To18(idCard);
		}
		return Short.valueOf(Objects.requireNonNull(idCard).substring(10, 12));
	}

	/**
	 * 根据身份编号获取生日天，只支持15或18位身份证号码
	 *
	 * @param idCard 身份编号
	 * @return 生日(dd)
	 */
	public static Short getDayByIdCard(String idCard) {
		final int len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = convert15To18(idCard);
		}
		return Short.valueOf(Objects.requireNonNull(idCard).substring(12, 14));
	}

	/**
	 * 根据身份编号获取性别，只支持15或18位身份证号码
	 *
	 * @param idCard 身份编号
	 * @return 性别(1 : 男 ， 0 : 女)
	 */
	public static int getGenderByIdCard(String idCard) {
		Assert.notBlank(idCard);
		final int len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			throw new IllegalArgumentException("ID Card length must be 15 or 18");
		}

		if (len == CHINA_ID_MIN_LENGTH) {
			idCard = convert15To18(idCard);
		}
		char sCardChar = Objects.requireNonNull(idCard).charAt(16);
		return (sCardChar % 2 != 0) ? 1 : 0;
	}

	/**
	 * 根据身份编号获取户籍省份编码，只支持15或18位身份证号码
	 *
	 * @param idCard 身份编码
	 * @return 省份编码
	 */
	public static String getProvinceCodeByIdCard(String idCard) {
		int len = idCard.length();
		if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
			return idCard.substring(0, 2);
		}
		return null;
	}

	/**
	 * 根据身份编号获取户籍省份，只支持15或18位身份证号码
	 *
	 * @param idCard 身份编码
	 * @return 省份名称。
	 */
	public static String getProvinceByIdCard(String idCard) {
		final String code = getProvinceCodeByIdCard(idCard);
		if (StrUtil.isNotBlank(code)) {
			return CITY_CODES.get(code);
		}
		return null;
	}

	/**
	 * 根据身份编号获取地市级编码，只支持15或18位身份证号码<br>
	 * 获取编码为4位
	 *
	 * @param idCard 身份编码
	 * @return 地市级编码
	 */
	public static String getCityCodeByIdCard(String idCard) {
		int len = idCard.length();
		if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
			return idCard.substring(0, 4);
		}
		return null;
	}

	/**
	 * 根据身份编号获取区县级编码，只支持15或18位身份证号码<br>
	 * 获取编码为6位
	 *
	 * @param idCard 身份编码
	 * @return 地市级编码
	 */
	public static String getDistrictCodeByIdCard(String idCard) {
		int len = idCard.length();
		if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
			return idCard.substring(0, 6);
		}
		return null;
	}

	/**
	 * 隐藏指定位置的几个身份证号数字为“*”
	 *
	 * @param idCard       身份证号
	 * @param startInclude 开始位置（包含）
	 * @param endExclude   结束位置（不包含）
	 * @return 隐藏后的身份证号码
	 * @see StrUtil#hide(CharSequence, int, int)
	 *
	 */
	public static String hide(String idCard, int startInclude, int endExclude) {
		return StrUtil.hide(idCard, startInclude, endExclude);
	}

	/**
	 * 获取身份证信息，包括身份、城市代码、生日、性别等
	 *
	 * @param idCard 15或18位身份证
	 * @return idCard
	 */
	public static IdCard getIdCardInfo(String idCard) {
		return new IdCard(idCard);
	}


	/**
	 * 获得18位身份证校验码
	 *
	 * @param code17 18位身份证号中的前17位
	 * @return 第18位
	 */
	private static char getCheckCode18(String code17) {
		int sum = getPowerSum(code17.toCharArray());
		return getCheckCode18(sum);
	}

	/**
	 * 将power和值与11取模获得余数进行校验码判断
	 *
	 * @param iSum 加权和
	 * @return 校验位
	 */
	private static char getCheckCode18(int iSum) {
		switch (iSum % 11) {
			case 10:
				return '2';
			case 9:
				return '3';
			case 8:
				return '4';
			case 7:
				return '5';
			case 6:
				return '6';
			case 5:
				return '7';
			case 4:
				return '8';
			case 3:
				return '9';
			case 2:
				return 'X';
			case 1:
				return '0';
			case 0:
				return '1';
			default:
				return StrUtil.C_SPACE;
		}
	}

	/**
	 * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
	 *
	 * @param iArr 身份证号码的数组
	 * @return 身份证编码
	 */
	private static int getPowerSum(char[] iArr) {
		int iSum = 0;
		if (POWER.length == iArr.length) {
			for (int i = 0; i < iArr.length; i++) {
				iSum += Integer.parseInt(String.valueOf(iArr[i])) * POWER[i];
			}
		}
		return iSum;
	}


	/**
	 * 身份证信息，包括身份、城市代码、生日、性别等
	 *
	 * @author looly
	 */
	public static class IdCard implements Serializable {
		private static final long serialVersionUID = 1L;

		private final String provinceCode;
		private final String cityCode;
		private final DateTime birthDate;
		private final Integer gender;
		private final int age;

		/**
		 * 构造
		 *
		 * @param idCard 身份证号码
		 */
		public IdCard(String idCard) {
			this.provinceCode = IdCardUtil.getProvinceCodeByIdCard(idCard);
			this.cityCode = IdCardUtil.getCityCodeByIdCard(idCard);
			this.birthDate = IdCardUtil.getBirthDate(idCard);
			this.gender = IdCardUtil.getGenderByIdCard(idCard);
			this.age = IdCardUtil.getAgeByIdCard(idCard);
		}

		/**
		 * 获取省份代码
		 *
		 * @return 省份代码
		 */
		public String getProvinceCode() {
			return this.provinceCode;
		}

		/**
		 * 获取省份名称
		 *
		 * @return 省份代码
		 */
		public String getProvince() {
			return CITY_CODES.get(this.provinceCode);
		}

		/**
		 * 获取市级编码
		 *
		 * @return 市级编码
		 */
		public String getCityCode() {
			return this.cityCode;
		}

		/**
		 * 获得生日日期
		 *
		 * @return 生日日期
		 */
		public DateTime getBirthDate() {
			return this.birthDate;
		}

		/**
		 * 获取性别代号，性别(1 : 男 ， 0 : 女)
		 *
		 * @return 性别(1 : 男 ， 0 : 女)
		 */
		public Integer getGender() {
			return this.gender;
		}

		/**
		 * 获取年龄
		 *
		 * @return 年龄
		 */
		public int getAge() {
			return age;
		}

		@Override
		public String toString() {
			return "IdCard{" +
					"provinceCode='" + provinceCode + '\'' +
					", cityCode='" + cityCode + '\'' +
					", birthDate=" + birthDate +
					", gender=" + gender +
					", age=" + age +
					'}';
		}
	}
}

package com.spring.cloud.base.utils.date;

import com.spring.cloud.base.utils.FastDateParser;
import com.spring.cloud.base.utils.FastDatePrinter;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/13 16:11
 */
public class FastDateFormat extends Format implements DateParser, DatePrinter {

	private static final FormatCache<FastDateFormat> CACHE = new FormatCache<FastDateFormat>() {
		@Override
		protected FastDateFormat createInstance(final String pattern, final TimeZone timeZone, final Locale locale) {
			return new FastDateFormat(pattern, timeZone, locale);
		}
	};
	private static final long serialVersionUID = -4489822869119953562L;

	private final FastDatePrinter printer;

	private final FastDateParser parser;

	// -----------------------------------------------------------------------

	/**
	 * 获得 FastDateFormat实例，使用默认格式和地区
	 *
	 * @return FastDateFormat
	 */
	public static FastDateFormat getInstance() {
		return CACHE.getInstance();
	}

	/**
	 * 获得 FastDateFormat 实例，使用默认地区<br>
	 * 支持缓存
	 *
	 * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @return FastDateFormat
	 * @throws IllegalArgumentException 日期格式问题
	 */
	public static FastDateFormat getInstance(final String pattern) {
		return CACHE.getInstance(pattern, null, null);
	}

	/**
	 * 获得 FastDateFormat 实例<br>
	 * 支持缓存
	 *
	 * @param pattern  使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @param timeZone 时区{@link TimeZone}
	 * @return FastDateFormat
	 * @throws IllegalArgumentException 日期格式问题
	 */
	public static FastDateFormat getInstance(final String pattern, final TimeZone timeZone) {
		return CACHE.getInstance(pattern, timeZone, null);
	}

	/**
	 * 获得 FastDateFormat 实例<br>
	 * 支持缓存
	 *
	 * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @param locale  {@link Locale} 日期地理位置
	 * @return FastDateFormat
	 * @throws IllegalArgumentException 日期格式问题
	 */
	public static FastDateFormat getInstance(final String pattern, final Locale locale) {
		return CACHE.getInstance(pattern, null, locale);
	}

	/**
	 * 获得 FastDateFormat 实例<br>
	 * 支持缓存
	 *
	 * @param pattern  使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @param timeZone 时区{@link TimeZone}
	 * @param locale   {@link Locale} 日期地理位置
	 * @return FastDateFormat
	 * @throws IllegalArgumentException 日期格式问题
	 */
	public static FastDateFormat getInstance(final String pattern, final TimeZone timeZone, final Locale locale) {
		return CACHE.getInstance(pattern, timeZone, locale);
	}

	/**
	 * 构造
	 *
	 * @param pattern  使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @param timeZone 非空时区 {@link TimeZone}
	 * @param locale   {@link Locale} 日期地理位置
	 * @throws NullPointerException if pattern, timeZone, or locale is null.
	 */
	protected FastDateFormat(final String pattern, final TimeZone timeZone, final Locale locale) {
		this(pattern, timeZone, locale, null);
	}

	/**
	 * 构造
	 *
	 * @param pattern      使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @param timeZone     非空时区 {@link TimeZone}
	 * @param locale       {@link Locale} 日期地理位置
	 * @param centuryStart The start of the 100 year period to use as the "default century" for 2 digit year parsing. If centuryStart is null, defaults to now - 80 years
	 * @throws NullPointerException if pattern, timeZone, or locale is null.
	 */
	protected FastDateFormat(final String pattern, final TimeZone timeZone, final Locale locale, final Date centuryStart) {
		printer = new FastDatePrinter(pattern, timeZone, locale);
		parser = new FastDateParser(pattern, timeZone, locale, centuryStart);
	}

	@Override
	public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
		return toAppendTo.append(printer.format(obj));
	}

	@Override
	public String format(final long millis) {
		return printer.format(millis);
	}

	@Override
	public String format(final Date date) {
		return printer.format(date);
	}

	@Override
	public String format(final Calendar calendar) {
		return printer.format(calendar);
	}

	@Override
	public <B extends Appendable> B format(final long millis, final B buf) {
		return printer.format(millis, buf);
	}

	@Override
	public <B extends Appendable> B format(final Date date, final B buf) {
		return printer.format(date, buf);
	}

	@Override
	public <B extends Appendable> B format(final Calendar calendar, final B buf) {
		return printer.format(calendar, buf);
	}

	// ----------------------------------------------------------------------- Parsing
	@Override
	public Date parse(final String source) throws ParseException {
		return parser.parse(source);
	}

	@Override
	public Date parse(final String source, final ParsePosition pos) {
		return parser.parse(source, pos);
	}

	@Override
	public boolean parse(final String source, final ParsePosition pos, final Calendar calendar) {
		return parser.parse(source, pos, calendar);
	}

	@Override
	public Object parseObject(final String source, final ParsePosition pos) {
		return parser.parseObject(source, pos);
	}
	@Override
	public String getPattern() {
		return printer.getPattern();
	}

	@Override
	public TimeZone getTimeZone() {
		return printer.getTimeZone();
	}

	@Override
	public Locale getLocale() {
		return printer.getLocale();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof FastDateFormat == false) {
			return false;
		}
		final FastDateFormat other = (FastDateFormat) obj;
		// no need to check parser, as it has same invariants as printer
		return printer.equals(other.printer);
	}

	@Override
	public int hashCode() {
		return printer.hashCode();
	}

	@Override
	public String toString() {
		return "FastDateFormat[" + printer.getPattern() + "," + printer.getLocale() + "," + printer.getTimeZone().getID() + "]";
	}
}

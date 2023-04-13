package com.spring.cloud.base.common.money;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;


/**
 * <p>
 * 货币类中封装了货币金额和币种。目前金额在内部是long类型表示，
 * 单位是所属币种的最小货币单位（如对人民币是分）。
 *
 * <p>
 * 目前，货币实现了以下主要功能：<br>
 * <ul>
 *   <li>支持货币对象与double(float)/long(int)/String/BigDecimal之间相互转换。
 *   <li>货币类在运算中提供与JDK中的BigDecimal类似的运算接口，
 *       BigDecimal的运算接口支持任意指定精度的运算功能，能够支持各种
 *       可能的财务规则。
 *   <li>货币类在运算中也提供一组简单运算接口，使用这组运算接口，则在
 *       精度处理上使用缺省的处理规则。
 *   <li>推荐使用Money，不建议直接使用BigDecimal的原因之一在于，
 *       使用BigDecimal，同样金额和币种的货币使用BigDecimal存在多种可能
 *       的表示，例如：new BigDecimal("10.5")与new BigDecimal("10.50")
 *       不相等，因为scale不等。使得Money类，同样金额和币种的货币只有
 *       一种表示方式，new Money("10.5")和new Money("10.50")应该是相等的。
 *   <li>不推荐直接使用BigDecimal的另一原因在于， BigDecimal是Immutable，
 *       一旦创建就不可更改，对BigDecimal进行任意运算都会生成一个新的
 *       BigDecimal对象，因此对于大批量统计的性能不够满意。Money类是
 *       mutable的，对大批量统计提供较好的支持。
 *   <li>提供基本的格式化功能。
 *   <li>Money类中不包含与业务相关的统计功能和格式化功能。业务相关的功能
 *       建议使用utility类来实现。
 *   <li>Money类实现了Serializable接口，支持作为远程调用的参数和返回值。
 *   <li>Money类实现了equals和hashCode方法。
 * </ul>
 *
 * @author ls
 * @author lijian 处理了运算中的溢出情形
 * @since 2022/02/13
 */
public class MultiCurrencyMoney implements Serializable, Comparable<MultiCurrencyMoney>, Cloneable {


    /**
     * 缺省的币种代码，为CNY（人民币）。
     */
    public static final String DEFAULT_CURRENCY_CODE = CurrencyEnum.CNY.getCurrencyCode();

    /**
     * 缺省的取整模式，为<code>BigDecimal.ROUND_HALF_EVEN
     * （四舍五入，当小数为0.5时，则取最近的偶数）。
     */
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

    /**
     * 一组可能的元/分换算比例。
     *
     * <p>
     * 此处，“分”是指货币的最小单位，“元”是货币的最常用单位，
     * 不同的币种有不同的元/分换算比例，如人民币是100，而日元为1。
     */
    private static final int[] CENT_FACTORS = new int[]{1, 10, 100, 1000, 10000,
            100000};

    /**
     * 金额，以分为单位。
     */
    private long cent;

    /**
     * 币种。
     */
    private Currency currency;

    /**
     * 系统内一般存储币种值（156/840/...）
     */
    private String currencyValue;

    // 构造器 ====================================================

    /**
     * 缺省构造器。
     *
     * <p>
     * 创建一个具有缺省金额（0）和缺省币种的货币对象。
     */
    public MultiCurrencyMoney() {
        this(0);
    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有金额<code>yuan</code>元<code>cent</cent>分和缺省币种的货币对象。
     *
     * @param yuan 金额元数。
     * @param cent 金额分数。
     */
    public MultiCurrencyMoney(long yuan, long cent) {
        this(yuan, cent, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有金额<code>yuan</code>元<code>cent</code>分和指定币种的货币对象。
     *
     * @param yuan     金额元数。
     * @param cent     金额分数。
     * @param currency 币种
     */
    public MultiCurrencyMoney(long yuan, long cent, Currency currency) {
        this.setCurrency(currency);
        this.cent = CheckOverflow.longCheckedAdd(CheckOverflow.longCheckedMultiply(yuan, getCentFactor()), cent);
    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有金额<code>cent</code>分和指定币种的货币对象。
     *
     * @param cent     金额分数。
     * @param currency 币种
     */
    public MultiCurrencyMoney(long cent, Currency currency) {
        this.setCurrency(currency);
        this.cent = cent;
    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有金额<code>cent</code>分和指定币种的货币对象。
     *
     * @param cent          金额分数。
     * @param currencyValue 币种value(数字，例如156)
     */
    public MultiCurrencyMoney(long cent, String currencyValue) {
        CurrencyEnum currencyEnum = CurrencyEnum.getByCurrencyValue(currencyValue);

        if (currencyEnum == null) {
            throw new RuntimeException("not support currencyValue : " + currencyValue);
        }

        this.setCurrency(Currency.getInstance(currencyEnum.getCurrencyCode()));
        this.cent = cent;
    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有金额<code>amount</code>元和缺省币种的货币对象。
     *
     * @param amount 金额，以元为单位。
     */
    public MultiCurrencyMoney(String amount) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有金额<code>amount</code>元和指定币种<code>currency</code>的货币对象。
     *
     * @param amount   金额，以元为单位。
     * @param currency 币种。
     */
    public MultiCurrencyMoney(String amount, Currency currency) {
        this(new BigDecimal(amount), currency);
    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有金额<code>amount</code>元和指定币种<code>currency</code>的货币对象。
     * 如果金额不能转换为整数分，则使用指定的取整模式<code>roundingMode</code>取整。
     *
     * @param amount       金额，以元为单位。
     * @param currency     币种。
     * @param roundingMode 取整模式。
     */
    public MultiCurrencyMoney(String amount, Currency currency, RoundingMode roundingMode) {
        this(new BigDecimal(amount), currency, roundingMode);
    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有参数<code>amount</code>指定金额和缺省币种的货币对象。
     * 如果金额不能转换为整数分，则使用四舍五入方式取整。
     *
     * <p>
     * 注意：由于double类型运算中存在误差，使用四舍五入方式取整的
     * 结果并不确定，因此，应尽量避免使用double类型创建货币类型。
     * 例：
     * <code>
     * assertEquals(999, Math.round(9.995 * 100));
     * assertEquals(1000, Math.round(999.5));
     * money = new Money((9.995));
     * assertEquals(999, money.getCent());
     * money = new Money(10.005);
     * assertEquals(1001, money.getCent());
     * </code>
     *
     * @param amount 金额，以元为单位。
     */
    public MultiCurrencyMoney(double amount) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有金额<code>amount</code>和指定币种的货币对象。
     * 如果金额不能转换为整数分，则使用四舍五入方式取整。
     *
     * <p>
     * 注意：由于double类型运算中存在误差，使用四舍五入方式取整的
     * 结果并不确定，因此，应尽量避免使用double类型创建货币类型。
     * 例：
     * <code>
     * assertEquals(999, Math.round(9.995 * 100));
     * assertEquals(1000, Math.round(999.5));
     * money = new Money((9.995));
     * assertEquals(999, money.getCent());
     * money = new Money(10.005);
     * assertEquals(1001, money.getCent());
     * </code>
     *
     * @param amount   金额，以元为单位。
     * @param currency 币种。
     */
    public MultiCurrencyMoney(double amount, Currency currency) {
        this.setCurrency(currency);
        //      this.cent = Math.round(amount * getCentFactor());        
        this.cent = Math.round(CheckOverflow.doubleCheckedMultiply(amount, getCentFactor()));
    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有金额<code>amount</code>和指定币种值的对象。
     * 如果金额不能转换为整数分，则使用缺省的取整模式<code>DEFAULT_ROUNDING_MODE</code>进行取整。
     *
     * @param amount        金额，以元为单位。
     * @param currencyValue 币种值，使用的是国内一般存储的840，156等值
     */
    public MultiCurrencyMoney(double amount, String currencyValue) {
        this.setCurrencyValue(currencyValue);
        //       this.cent = Math.round(amount * getCentFactor());
        this.cent = Math.round(CheckOverflow.doubleCheckedMultiply(amount, getCentFactor()));

    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有金额<code>amount</code>和指定币种值的对象。
     * 如果金额不能转换为整数分，则使用缺省的取整模式<code>DEFAULT_ROUNDING_MODE</code>进行取整。
     *
     * @param amount        金额，以元为单位。
     * @param currencyValue 币种值，使用的是国内一般存储的840，156等值
     */
    public MultiCurrencyMoney(BigDecimal amount, String currencyValue) {
        this.setCurrencyValue(currencyValue);

        this.cent = rounding(amount.movePointRight(currency.getDefaultFractionDigits()),
                DEFAULT_ROUNDING_MODE);
    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有金额<code>amount</code>和指定币种值的对象。
     * 如果金额不能转换为整数分，则使用缺省的取整模式<code>DEFAULT_ROUNDING_MODE</code>进行取整。
     *
     * @param amount        金额，以元为单位。
     * @param currencyValue 币种值，使用的是国内一般存储的840，156等值
     */
    public MultiCurrencyMoney(String amount, String currencyValue) {
        this.setCurrencyValue(currencyValue);
        this.cent = rounding(
                new BigDecimal(amount).movePointRight(currency.getDefaultFractionDigits()),
                DEFAULT_ROUNDING_MODE);
    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有金额<code>amount</code>和缺省币种的货币对象。
     * 如果金额不能转换为整数分，则使用缺省取整模式<code>DEFAULT_ROUNDING_MODE</code>取整。
     *
     * @param amount 金额，以元为单位。
     */
    public MultiCurrencyMoney(BigDecimal amount) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE));
    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有参数<code>amount</code>指定金额和缺省币种的货币对象。
     * 如果金额不能转换为整数分，则使用指定的取整模式<code>roundingMode</code>取整。
     *
     * @param amount       金额，以元为单位。
     * @param roundingMode 取整模式
     */
    public MultiCurrencyMoney(BigDecimal amount, RoundingMode roundingMode) {
        this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE), roundingMode);
    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有金额<code>amount</code>和指定币种的货币对象。
     * 如果金额不能转换为整数分，则使用缺省的取整模式<code>DEFAULT_ROUNDING_MODE</code>进行取整。
     *
     * @param amount   金额，以元为单位。
     * @param currency 币种
     */
    public MultiCurrencyMoney(BigDecimal amount, Currency currency) {
        this(amount, currency, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 构造器。
     *
     * <p>
     * 创建一个具有金额<code>amount</code>和指定币种的货币对象。
     * 如果金额不能转换为整数分，则使用指定的取整模式<code>roundingMode</code>取整。
     *
     * @param amount       金额，以元为单位。
     * @param currency     币种。
     * @param roundingMode 取整模式。
     */
    public MultiCurrencyMoney(BigDecimal amount, Currency currency, RoundingMode roundingMode) {
        this.setCurrency(currency);
        this.cent = rounding(amount.movePointRight(currency.getDefaultFractionDigits()),
                roundingMode);
    }

    // Bean方法 ====================================================

    /**
     * 获取本货币对象代表的金额数。
     *
     * @return 金额数，以元为单位。
     */
    public BigDecimal getAmount() {
        return BigDecimal.valueOf(cent, currency.getDefaultFractionDigits());
    }

    /**
     * <font color='red'><b>
     * <p>慎用！
     * <p>历史上发生过多次将分当成元来使用的资损事件。
     * <p>请再次确认获取分的必要性！
     * </b></font>
     *
     * <p>获取本货币对象代表的金额数。
     *
     * @return 金额数，以分为单位。
     */
    public long getCent() {
        return cent;
    }

    /**
     * 获取本货币对象代表的币种。
     *
     * @return 本货币对象所代表的币种。
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * 获取本货币币种的元/分换算比率。
     * VND在jdk6中是最小单位是分，而在jdk8中最小单位是元，需要针对VND默认精度去除对jdk底层依赖，自定义为“元”
     *
     * @return 本货币币种的元/分换算比率。
     */
    public final int getCentFactor() {
        if (CurrencyEnum.VND.getCurrencyCode().equals(currency.getCurrencyCode())) {
            return CENT_FACTORS[0];
        }
        return CENT_FACTORS[currency.getDefaultFractionDigits()];
    }

    // 基本对象方法 ===================================================

    /**
     * 判断本货币对象与另一对象是否相等。
     *
     * <p>
     * 本货币对象与另一对象相等的充分必要条件是：<br>
     * <ul>
     *  <li>另一对象也属货币对象类。
     *  <li>金额相同。
     *  <li>币种相同。
     * </ul>
     *
     * @param other 待比较的另一对象。
     * @return <code>true</code>表示相等，<code>false</code>表示不相等。
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof MultiCurrencyMoney) && equals((MultiCurrencyMoney) other);
    }

    /**
     * 判断本货币对象与另一货币对象是否相等。
     *
     * <p>
     * 本货币对象与另一货币对象相等的充分必要条件是：<br>
     * <ul>
     *  <li>金额相同。
     *  <li>币种相同。
     * </ul>
     *
     * @param other 待比较的另一货币对象。
     * @return <code>true</code>表示相等，<code>false</code>表示不相等。
     */
    public boolean equals(MultiCurrencyMoney other) {
        return currency.equals(other.currency) && (cent == other.cent);
    }

    /**
     * 计算本货币对象的杂凑值。
     *
     * @return 本货币对象的杂凑值。
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (int) (cent ^ (cent >>> 32));
    }

    /**
     * 克隆一个本货币对象的副本。
     *
     * @see Object#clone()
     */
    @Override
    public MultiCurrencyMoney clone() {

        return new MultiCurrencyMoney(this.getAmount(), this.getCurrencyValue());
    }

    // Comparable接口 ========================================

    /**
     * 货币比较。
     *
     * <p>
     * 比较本货币对象与另一货币对象的大小。
     * 如果待比较的两个货币对象的币种不同，则抛出<code>java.lang.IllegalArgumentException</code>。
     * 如果本货币对象的金额少于待比较货币对象，则返回-1。
     * 如果本货币对象的金额等于待比较货币对象，则返回0。
     * 如果本货币对象的金额大于待比较货币对象，则返回1。
     *
     * @param other 另一对象。
     * @return -1表示小于，0表示等于，1表示大于。
     * @throws IllegalArgumentException 待比较货币对象与本货币对象的币种不同。
     */
    @Override
    public int compareTo(MultiCurrencyMoney other) {
        assertSameCurrencyAs(other);

        if (cent < other.cent) {
            return -1;
        } else if (cent == other.cent) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 货币比较。
     *
     * <p>
     * 判断本货币对象是否大于另一货币对象。
     * 如果待比较的两个货币对象的币种不同，则抛出<code>java.lang.IllegalArgumentException</code>。
     * 如果本货币对象的金额大于待比较货币对象，则返回true，否则返回false。
     *
     * @param other 另一对象。
     * @return true表示大于，false表示不大于（小于等于）。
     * @throws IllegalArgumentException 待比较货币对象与本货币对象的币种不同。
     */
    public boolean greaterThan(MultiCurrencyMoney other) {
        return compareTo(other) > 0;
    }

    // 货币算术 ==========================================

    /**
     * 货币加法。
     *
     * <p>
     * 如果两货币币种相同，则返回一个新的相同币种的货币对象，其金额为
     * 两货币对象金额之和，本货币对象的值不变。
     * 如果两货币对象币种不同，抛出<code>java.lang.IllegalArgumentException</code>。
     *
     * @param other 作为加数的货币对象。
     * @return 相加后的结果。
     * @throws IllegalArgumentException 如果本货币对象与另一货币对象币种不同。
     */
    public MultiCurrencyMoney add(MultiCurrencyMoney other) {
        assertSameCurrencyAs(other);

        //        return newMoneyWithSameCurrency(  cent + other.cent);
        return newMoneyWithSameCurrency(CheckOverflow.longCheckedAdd(cent, other.cent));
    }

    /**
     * 货币累加。
     *
     * <p>
     * 如果两货币币种相同，则本货币对象的金额等于两货币对象金额之和，并返回本货币对象的引用。
     * 如果两货币对象币种不同，抛出<code>java.lang.IllegalArgumentException</code>。
     *
     * @param other 作为加数的货币对象。
     * @return 累加后的本货币对象。
     * @throws IllegalArgumentException 如果本货币对象与另一货币对象币种不同。
     */
    public MultiCurrencyMoney addTo(MultiCurrencyMoney other) {
        assertSameCurrencyAs(other);

        this.cent = CheckOverflow.longCheckedAdd(this.cent, other.cent);
        return this;
    }

    /**
     * 货币减法。
     *
     * <p>
     * 如果两货币币种相同，则返回一个新的相同币种的货币对象，其金额为
     * 本货币对象的金额减去参数货币对象的金额。本货币对象的值不变。
     * 如果两货币币种不同，抛出<code>java.lang.IllegalArgumentException</code>。
     *
     * @param other 作为减数的货币对象。
     * @return 相减后的结果。
     * @throws IllegalArgumentException 如果本货币对象与另一货币对象币种不同。
     */
    public MultiCurrencyMoney subtract(MultiCurrencyMoney other) {
        assertSameCurrencyAs(other);

        //       return newMoneyWithSameCurrency(cent - other.cent);
        return newMoneyWithSameCurrency(CheckOverflow.longCheckedSubtract(cent, other.cent));
    }

    /**
     * 货币累减。
     *
     * <p>
     * 如果两货币币种相同，则本货币对象的金额等于两货币对象金额之差，并返回本货币对象的引用。
     * 如果两货币币种不同，抛出<code>java.lang.IllegalArgumentException</code>。
     *
     * @param other 作为减数的货币对象。
     * @return 累减后的本货币对象。
     * @throws IllegalArgumentException 如果本货币对象与另一货币对象币种不同。
     */
    public MultiCurrencyMoney subtractFrom(MultiCurrencyMoney other) {
        assertSameCurrencyAs(other);

        this.cent = CheckOverflow.longCheckedSubtract(this.cent, other.cent);

        return this;
    }

    /**
     * 货币乘法。
     *
     * <p>
     * 返回一个新的货币对象，币种与本货币对象相同，金额为本货币对象的金额乘以乘数。
     * 本货币对象的值不变。
     *
     * @param val 乘数
     * @return 乘法后的结果。
     */
    public MultiCurrencyMoney multiply(long val) {
        return newMoneyWithSameCurrency(CheckOverflow.longCheckedMultiply(cent, val));
    }

    /**
     * 货币累乘。
     *
     * <p>
     * 本货币对象金额乘以乘数，并返回本货币对象。
     *
     * @param val 乘数
     * @return 累乘后的本货币对象。
     */
    public MultiCurrencyMoney multiplyBy(long val) {
        this.cent = CheckOverflow.longCheckedMultiply(cent, val);
        return this;
    }

    /**
     * 货币乘法。
     *
     * <p>
     * 返回一个新的货币对象，币种与本货币对象相同，金额为本货币对象的金额乘以乘数。
     * 本货币对象的值不变。如果相乘后的金额不能转换为整数分，则四舍五入。
     *
     * @param val 乘数
     * @return 相乘后的结果。
     */
    public MultiCurrencyMoney multiply(double val) {
        return newMoneyWithSameCurrency(Math.round(CheckOverflow.doubleCheckedMultiply(val, cent)));
    }

    /**
     * 货币累乘。
     *
     * <p>
     * 本货币对象金额乘以乘数，并返回本货币对象。
     * 如果相乘后的金额不能转换为整数分，则使用四舍五入。
     *
     * @param val 乘数
     * @return 累乘后的本货币对象。
     */
    public MultiCurrencyMoney multiplyBy(double val) {
        this.cent = Math.round(CheckOverflow.doubleCheckedMultiply(val, cent));
        return this;
    }

    /**
     * 货币乘法。
     *
     * <p>
     * 返回一个新的货币对象，币种与本货币对象相同，金额为本货币对象的金额乘以乘数。
     * 本货币对象的值不变。如果相乘后的金额不能转换为整数分，使用缺省的取整模式
     * <code>DEFUALT_ROUNDING_MODE</code>进行取整。
     *
     * @param val 乘数
     * @return 相乘后的结果。
     */
    public MultiCurrencyMoney multiply(BigDecimal val) {
        return multiply(val, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 货币累乘。
     *
     * <p>
     * 本货币对象金额乘以乘数，并返回本货币对象。
     * 如果相乘后的金额不能转换为整数分，使用缺省的取整方式
     * <code>DEFUALT_ROUNDING_MODE</code>进行取整。
     *
     * @param val 乘数
     * @return 累乘后的结果。
     */
    public MultiCurrencyMoney multiplyBy(BigDecimal val) {
        return multiplyBy(val, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 货币乘法。
     *
     * <p>
     * 返回一个新的货币对象，币种与本货币对象相同，金额为本货币对象的金额乘以乘数。
     * 本货币对象的值不变。如果相乘后的金额不能转换为整数分，使用指定的取整方式
     * <code>roundingMode</code>进行取整。
     *
     * @param val          乘数
     * @param roundingMode 取整方式
     * @return 相乘后的结果。
     */
    public MultiCurrencyMoney multiply(BigDecimal val, RoundingMode roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(cent).multiply(val);

        return newMoneyWithSameCurrency(rounding(newCent, roundingMode));
    }

    /**
     * 货币累乘。
     *
     * <p>
     * 本货币对象金额乘以乘数，并返回本货币对象。
     * 如果相乘后的金额不能转换为整数分，使用指定的取整方式
     * <code>roundingMode</code>进行取整。
     *
     * @param val          乘数
     * @param roundingMode 取整方式
     * @return 累乘后的结果。
     */
    public MultiCurrencyMoney multiplyBy(BigDecimal val, RoundingMode roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(cent).multiply(val);

        this.cent = rounding(newCent, roundingMode);

        return this;
    }

    /**
     * 货币除法。
     *
     * <p>
     * 返回一个新的货币对象，币种与本货币对象相同，金额为本货币对象的金额除以除数。
     * 本货币对象的值不变。如果相除后的金额不能转换为整数分，使用四舍五入方式取整。
     *
     * @param val 除数
     * @return 相除后的结果。
     */
    public MultiCurrencyMoney divide(double val) {
        //  return newMoneyWithSameCurrency(Math.round(cent / val));
        return this.divide(new BigDecimal(val));
    }

    /**
     * 货币累除。
     *
     * <p>
     * 本货币对象金额除以除数，并返回本货币对象。
     * 如果相除后的金额不能转换为整数分，使用四舍五入方式取整。
     *
     * @param val 除数
     * @return 累除后的结果。
     */
    public MultiCurrencyMoney divideBy(double val) {
        //   this.cent = Math.round(this.cent / val);
        return this.divideBy(new BigDecimal(val));
    }

    /**
     * 货币除法。
     *
     * <p>
     * 返回一个新的货币对象，币种与本货币对象相同，金额为本货币对象的金额除以除数。
     * 本货币对象的值不变。如果相除后的金额不能转换为整数分，使用缺省的取整模式
     * <code>DEFAULT_ROUNDING_MODE</code>进行取整。
     *
     * @param val 除数
     * @return 相除后的结果。
     */
    public MultiCurrencyMoney divide(BigDecimal val) {
        return divide(val, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 货币除法。
     *
     * <p>
     * 返回一个新的货币对象，币种与本货币对象相同，金额为本货币对象的金额除以除数。
     * 本货币对象的值不变。如果相除后的金额不能转换为整数分，使用指定的取整模式
     * <code>roundingMode</code>进行取整。
     *
     * @param val          除数
     * @param roundingMode 取整
     * @return 相除后的结果。
     */
    public MultiCurrencyMoney divide(BigDecimal val, RoundingMode roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(cent).divide(val, roundingMode);

        return newMoneyWithSameCurrency(CheckOverflow.bigDecimalChecked(newCent));
    }

    /**
     * 货币累除。
     *
     * <p>
     * 本货币对象金额除以除数，并返回本货币对象。
     * 如果相除后的金额不能转换为整数分，使用缺省的取整模式
     * <code>DEFAULT_ROUNDING_MODE</code>进行取整。
     *
     * @param val 除数
     * @return 累除后的结果。
     */
    public MultiCurrencyMoney divideBy(BigDecimal val) {
        return divideBy(val, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 货币累除。
     *
     * <p>
     * 本货币对象金额除以除数，并返回本货币对象。
     * 如果相除后的金额不能转换为整数分，使用指定的取整模式
     * <code>roundingMode</code>进行取整。
     *
     * @param val          除数
     * @param roundingMode 取整模式
     * @return 累除后的结果。
     */
    public MultiCurrencyMoney divideBy(BigDecimal val, RoundingMode roundingMode) {
        BigDecimal newCent = BigDecimal.valueOf(cent).divide(val, roundingMode);

        this.cent = CheckOverflow.bigDecimalChecked(newCent);

        return this;
    }

    /**
     * 货币分配<br>
     * <strong>本方法返回值只做只读操作</strong>。
     *
     * <p>
     * 将本货币对象尽可能平均分配成<code>targets</code>份。
     * 如果不能平均分配尽，则将零头放到开始的若干份中。分配
     * 运算能够确保不会丢失金额零头。
     *
     * @param targets 待分配的份数
     * @return 货币对象数组，数组的长度与分配份数相同，数组元素
     * 从大到小排列，所有货币对象的金额最多只相差1分。
     */
    public MultiCurrencyMoney[] allocate(int targets) {
        MultiCurrencyMoney[] results = new MultiCurrencyMoney[targets];

        MultiCurrencyMoney lowResult = newMoneyWithSameCurrency(cent / targets);
        MultiCurrencyMoney highResult = newMoneyWithSameCurrency(lowResult.cent + 1);

        int remainder = (int) (cent % targets);
        for (int i = 0; i < remainder; i++) {
            results[i] = highResult;
        }

        for (int i = remainder; i < targets; i++) {
            results[i] = lowResult;
        }

        return results;
    }

    /**
     * 货币分配。
     *
     * <p>
     * 将本货币对象按照规定的比例分配成若干份。分配所剩的零头
     * 从第一份开始顺序分配。分配运算确保不会丢失金额零头。
     *
     * @param ratios 分配比例数组，每一个比例是一个长整型，代表
     *               相对于总数的相对数。
     * @return 货币对象数组，数组的长度与分配比例数组的长度相同。
     */
    public MultiCurrencyMoney[] allocate(long[] ratios) {
        MultiCurrencyMoney[] results = new MultiCurrencyMoney[ratios.length];

        long total = 0;

        for (int i = 0; i < ratios.length; i++) {
            total += ratios[i];
        }

        long remainder = cent;

        for (int i = 0; i < results.length; i++) {
            results[i] = newMoneyWithSameCurrency((cent * ratios[i]) / total);
            remainder -= results[i].cent;
        }

        for (int i = 0; i < remainder; i++) {
            results[i].cent++;
        }

        return results;
    }

    // 格式化方法 =================================================

    /**
     * 生成本对象的缺省字符串表示。
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "MultiCurrencyMoney [cent=" + cent + ", currency=" + currency + ", currencyValue="
                + currencyValue + "]";
    }

    /**
     * 设置货币的分值。
     *
     * @param l 分值
     */
    public void setCent(long l) {
        cent = l;
    }

    /**
     * 设置本货币对象币种。
     *
     * @param currency 币种。
     */
    public void setCurrency(Currency currency) {

        //所支持的币种类
        CurrencyEnum supportCurrency = CurrencyEnum.getByCurrencyCode(currency.getCurrencyCode());

        if (null == supportCurrency) {
            throw new RuntimeException();
        }

        this.currencyValue = supportCurrency.getCurrencyValue();
        this.currency = currency;

    }

    /**
     * 返回本货币对象币种值。
     *
     * @return 币种值。
     */
    public String getCurrencyValue() {
        return currencyValue;
    }

    /**
     * 设置本货币对象币种值。
     *
     * @param currencyValue 币种值
     */
    public void setCurrencyValue(String currencyValue) {

        CurrencyEnum supportCurrency = null;

        //防止数据库存储为空的数据错误创建对象
        if (StringUtils.isBlank(currencyValue)) {
            this.currencyValue = null;
            this.currency = null;
        } else {

            //默认：根据币种值转换【999，156】
            supportCurrency = CurrencyEnum.getByCurrencyValue(currencyValue);

            //兼容老支付系统数据格式【999，CNY】
            if (null == supportCurrency) {
                supportCurrency = CurrencyEnum.getByCurrencyCode(currencyValue);
            }

            if (null == supportCurrency) {
                throw new RuntimeException();
            }

            this.currencyValue = supportCurrency.getCurrencyValue();

            this.setCurrency(Currency.getInstance(supportCurrency.getCurrencyCode()));
        }
    }

    /**
     * 设置本货币对象币种代码。
     *
     * @param currencyCode 币种代码
     */
    public void setCurrencyCode(String currencyCode) {

        this.setCurrency(Currency.getInstance(currencyCode));
    }

    /**
     * 取得本货币对象的币种代码。
     *
     * @return 币种代码
     */
    public String getCurrencyCode() {
        return this.currency.getCurrencyCode();
    }

    // 内部方法 ===================================================

    /**
     * 断言本货币对象与另一货币对象是否具有相同的币种。
     *
     * <p>
     * 如果本货币对象与另一货币对象具有相同的币种，则方法返回。
     * 否则抛出运行时异常<code>java.lang.IllegalArgumentException</code>。
     *
     * @param other 另一货币对象
     * @throws IllegalArgumentException 如果本货币对象与另一货币对象币种不同。
     */
    protected void assertSameCurrencyAs(MultiCurrencyMoney other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Money math currency mismatch.");
        }
    }

    /**
     * 对BigDecimal型的值按指定取整方式取整。
     *
     * @param val          待取整的BigDecimal值
     * @param roundingMode 取整方式
     * @return 取整后的long型值
     */
    protected long rounding(BigDecimal val, RoundingMode roundingMode) {
        BigDecimal newVal = val.setScale(0, roundingMode);
        return CheckOverflow.bigDecimalChecked(newVal);
    }

    /**
     * 创建一个币种相同，具有指定金额的货币对象。
     *
     * @param cent 金额，以分为单位
     * @return 一个新建的币种相同，具有指定金额的货币对象
     */
    protected MultiCurrencyMoney newMoneyWithSameCurrency(long cent) {
        MultiCurrencyMoney money = new MultiCurrencyMoney(0, currency);

        money.cent = cent;

        return money;
    }

    // 调试方式 ==================================================

    /**
     * 生成本对象内部变量的字符串表示，用于调试。
     *
     * @return 本对象内部变量的字符串表示。
     */
    public String dump() {
        String lineSeparator = System.getProperty("line.separator");

        StringBuffer sb = new StringBuffer();

        sb.append("cent = ").append(cent).append(lineSeparator);
        sb.append("currency = ").append(currency);

        return sb.toString();
    }

}

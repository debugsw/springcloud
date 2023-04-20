package com.spring.cloud.base.common.money;

/**
 * 专为多币种提供的中文展示描述枚举。
 *
 * @author ls
 */
public enum CurrencyCHNShowEnum {

    /**
     * 人民币 [￥]
     */
    CNY(CurrencyEnum.CNY, "人民币"),

    /**
     * 美元 [US $]
     */
    USD(CurrencyEnum.USD, "美元"),

    /**
     * 港元 [HK$]
     */
    HKD(CurrencyEnum.HKD, "港元"),

    /**
     * 台币 [NT$]
     */
    TWD(CurrencyEnum.TWD, "台币"),

    /**
     * 欧元 [€]
     */
    EUR(CurrencyEnum.EUR, "欧元"),

    /**
     * 英镑 [￡]
     */
    GBP(CurrencyEnum.GBP, "英镑"),

    /**
     * 日元 [￥]
     */
    JPY(CurrencyEnum.JPY, "日元"),

    /**
     * 巴西雷亚尔 [R$]
     */
    BRL(CurrencyEnum.BRL, "巴西雷亚尔"),

    /**
     * 卢布 [руб.]
     */
    RUB(CurrencyEnum.RUB, "卢布"),

    /**
     * 澳元 [AU $]
     */
    AUD(CurrencyEnum.AUD, "澳元"),

    /**
     * 加元 [C$]
     */
    CAD(CurrencyEnum.CAD, "加元"),

    /**
     * 印度卢比 [Rs.]
     */
    INR(CurrencyEnum.INR, "印度卢比"),

    /**
     * 乌克兰里夫纳 [грн.]
     */
    UAH(CurrencyEnum.UAH, "乌克兰里夫纳"),

    /**
     * 墨西哥比索 [MXN$]
     */
    MXN(CurrencyEnum.MXN, "墨西哥比索"),

    /**
     * 瑞士法郎 [CHF]
     */
    CHF(CurrencyEnum.CHF, "瑞士法郎"),

    /**
     * 新加坡元 [S$]
     */
    SGD(CurrencyEnum.SGD, "新加坡元"),

    /**
     * 波兰兹罗提 [PLN]
     */
    PLN(CurrencyEnum.PLN, "波兰兹罗提"),

    /*-------------------XXX 没有确认币种符号--------------------------*/

    /**
     * 马来西亚林吉特 [RM]
     */
    MYR(CurrencyEnum.MYR, "马来西亚林吉特"),

    /**
     * 新西兰元 [$]
     */
    NZD(CurrencyEnum.NZD, "新西兰元"),

    /**
     * 泰铢 [฿]
     */
    THB(CurrencyEnum.THB, "泰铢"),

    /**
     * 匈牙利福林 [Ft]
     */
    HUF(CurrencyEnum.HUF, "匈牙利福林"),

    /**
     * 阿联酋迪拉姆 [AED]
     */
    AED(CurrencyEnum.AED, "阿联酋迪拉姆"),

    /**
     * 南非兰特 [R]
     */
    ZAR(CurrencyEnum.ZAR, "南非兰特"),

    /**
     * 菲律宾比索 [₱]
     */
    PHP(CurrencyEnum.PHP, "菲律宾比索"),

    /**
     * 瑞典克朗 [kr]
     */
    SEK(CurrencyEnum.SEK, "瑞典克朗"),

    /**
     * 印度尼西亚卢比 [Rp]
     */
    IDR(CurrencyEnum.IDR, "印度尼西亚卢比"),

    /**
     * 沙特里亚尔 [﷼]
     */
    SAR(CurrencyEnum.SAR, "沙特里亚尔"),

    /**
     * 土耳其里拉 [TRY]
     */
    TRY(CurrencyEnum.TRY, "土耳其里拉"),

    /**
     * 肯尼亚先令 [KES]
     */
    KES(CurrencyEnum.KES, "肯尼亚先令"),

    /**
     * 韩元 [₩]
     */
    KRW(CurrencyEnum.KRW, "韩元"),

    /**
     * 埃及镑 [£]
     */
    EGP(CurrencyEnum.EGP, "埃及镑"),

    /**
     * 伊拉克第纳尔 [IQD]
     */
    IQD(CurrencyEnum.IQD, "伊拉克第纳尔"),

    /**
     * 挪威克朗 [kr]
     */
    NOK(CurrencyEnum.NOK, "挪威克朗"),

    /**
     * 科威特第纳尔 [KWD]
     */
    KWD(CurrencyEnum.KWD, "科威特第纳尔"),

    /**
     * 丹麦克朗 [kr]
     */
    DKK(CurrencyEnum.DKK, "丹麦克朗"),

    /**
     * 巴基斯坦卢比 [₨]
     */
    PKR(CurrencyEnum.PKR, "巴基斯坦卢比"),

    /**
     * 以色列谢克尔 [₪]
     */
    ILS(CurrencyEnum.ILS, "以色列谢克尔"),

    /**
     * 卡塔尔里亚尔 [﷼]
     */
    QAR(CurrencyEnum.QAR, "卡塔尔里亚尔"),

    /**
     * 金（盎司） [XAU]
     */
    XAU(CurrencyEnum.XAU, "金（盎司）"),

    /**
     * 阿曼里亚尔 [﷼]
     */
    OMR(CurrencyEnum.OMR, "阿曼里亚尔"),

    /**
     * 哥伦比亚比索 [$]
     */
    COP(CurrencyEnum.COP, "哥伦比亚比索"),

    /**
     * 智利比索 [$]
     */
    CLP(CurrencyEnum.CLP, "智利比索"),

    /**
     * 阿根廷比索 [$]
     */
    ARS(CurrencyEnum.ARS, "阿根廷比索"),

    /**
     * 捷克克朗 [Kč]
     */
    CZK(CurrencyEnum.CZK, "捷克克朗"),

    /**
     * 越南盾 [₫]
     */
    VND(CurrencyEnum.VND, "越南盾"),

    /**
     * 摩洛哥迪拉姆 [MAD]
     */
    MAD(CurrencyEnum.MAD, "摩洛哥迪拉姆"),

    /**
     * 约旦第纳尔 [JOD]
     */
    JOD(CurrencyEnum.JOD, "约旦第纳尔"),

    /**
     * 巴林第纳尔 [BHD]
     */
    BHD(CurrencyEnum.BHD, "巴林第纳尔"),

    /**
     * CFA 法郎 [XOF]
     */
    XOF(CurrencyEnum.XOF, "CFA 法郎"),

    /**
     * 斯里兰卡卢比 [₨]
     */
    LKR(CurrencyEnum.LKR, "斯里兰卡卢比"),

    /**
     * 尼日利亚奈拉 [₦]
     */
    NGN(CurrencyEnum.NGN, "尼日利亚奈拉"),

    /**
     * 突尼斯第纳尔 [TND]
     */
    TND(CurrencyEnum.TND, "突尼斯第纳尔"),

    /**
     * 乌干达先令 [UGX]
     */
    UGX(CurrencyEnum.UGX, "乌干达先令"),

    /**
     * 罗马尼亚新列伊 [lei]
     */
    RON(CurrencyEnum.RON, "罗马尼亚新列伊"),

    /**
     * 孟加拉国塔卡 [BDT]
     */
    BDT(CurrencyEnum.BDT, "孟加拉国塔卡"),

    /**
     * 秘鲁新索尔 [S/.]
     */
    PEN(CurrencyEnum.PEN, "秘鲁新索尔"),

    /**
     * 格鲁吉亚拉里 [GEL]
     */
    GEL(CurrencyEnum.GEL, "格鲁吉亚拉里"),

    /**
     * 中非金融合作法郎 [XAF]
     */
    XAF(CurrencyEnum.XAF, "中非金融合作法郎"),

    /**
     * 斐济元 [$]
     */
    FJD(CurrencyEnum.FJD, "斐济元"),

    /**
     * 委内瑞拉玻利瓦尔 [Bs]
     */
    VEF(CurrencyEnum.VEF, "委内瑞拉玻利瓦尔"),

    /**
     * 白俄罗斯卢布 [p.]
     */
    BYR(CurrencyEnum.BYR, "白俄罗斯卢布"),

    /**
     * 克罗地亚库纳 [kn]
     */
    HRK(CurrencyEnum.HRK, "克罗地亚库纳"),

    /**
     * 乌兹别克斯坦索姆 [лв]
     */
    UZS(CurrencyEnum.UZS, "乌兹别克斯坦索姆"),

    /**
     * 保加利亚列弗 [лв]
     */
    BGN(CurrencyEnum.BGN, "保加利亚列弗"),

    /**
     * 阿尔及利亚第纳尔 [DZD]
     */
    DZD(CurrencyEnum.DZD, "阿尔及利亚第纳尔"),

    /**
     * 伊朗里亚尔 [﷼]
     */
    IRR(CurrencyEnum.IRR, "伊朗里亚尔"),

    /**
     * 多米尼加比索 [RD$]
     */
    DOP(CurrencyEnum.DOP, "多米尼加比索"),

    /**
     * 冰岛克朗 [kr]
     */
    ISK(CurrencyEnum.ISK, "冰岛克朗"),

    /**
     * 银（盎司） [XAG]
     */
    XAG(CurrencyEnum.XAG, "银（盎司）"),

    /**
     * 哥斯达黎加科朗 [₡]
     */
    CRC(CurrencyEnum.CRC, "哥斯达黎加科朗"),

    /**
     * 叙利亚镑 [£]
     */
    SYP(CurrencyEnum.SYP, "叙利亚镑"),

    /**
     * 利比亚第纳尔 [LYD]
     */
    LYD(CurrencyEnum.LYD, "利比亚第纳尔"),

    /**
     * 牙买加元 [J$]
     */
    JMD(CurrencyEnum.JMD, "牙买加元"),

    /**
     * 毛里塔尼亚卢比 [₨]
     */
    MUR(CurrencyEnum.MUR, "毛里塔尼亚卢比"),

    /**
     * 加纳塞地 [GHS]
     */
    GHS(CurrencyEnum.GHS, "加纳塞地"),

    /**
     * 安哥拉宽扎 [AOA]
     */
    AOA(CurrencyEnum.AOA, "安哥拉宽扎"),

    /**
     * 乌拉圭比索 [$U]
     */
    UYU(CurrencyEnum.UYU, "乌拉圭比索"),

    /**
     * 阿富汗尼 [؋]
     */
    AFN(CurrencyEnum.AFN, "阿富汗尼"),

    /**
     * 黎巴嫩镑 [£]
     */
    LBP(CurrencyEnum.LBP, "黎巴嫩镑"),

    /**
     * CFP 法郎 [XPF]
     */
    XPF(CurrencyEnum.XPF, "CFP 法郎"),

    /**
     * 特立尼达元 [TT$]
     */
    TTD(CurrencyEnum.TTD, "特立尼达元"),

    /**
     * 坦桑尼亚先令 [TZS]
     */
    TZS(CurrencyEnum.TZS, "坦桑尼亚先令"),

    /**
     * 阿尔巴尼列克 [Lek]
     */
    ALL(CurrencyEnum.ALL, "阿尔巴尼列克"),

    /**
     * 东加勒比元 [$]
     */
    XCD(CurrencyEnum.XCD, "东加勒比元"),

    /**
     * 危地马拉格查尔 [Q]
     */
    GTQ(CurrencyEnum.GTQ, "危地马拉格查尔"),

    /**
     * 尼泊尔卢比 [₨]
     */
    NPR(CurrencyEnum.NPR, "尼泊尔卢比"),

    /**
     * 玻利维亚诺 [$b]
     */
    BOB(CurrencyEnum.BOB, "玻利维亚诺"),

    /**
     * 巴巴多斯元 [$]
     */
    BBD(CurrencyEnum.BBD, "巴巴多斯元"),

    /**
     * 古巴可兑换比索 [CUC]
     */
    CUC(CurrencyEnum.CUC, "古巴可兑换比索"),

    /**
     * 老挝基普 [₭]
     */
    LAK(CurrencyEnum.LAK, "老挝基普"),

    /**
     * 文莱元 [$]
     */
    BND(CurrencyEnum.BND, "文莱元"),

    /**
     * 博茨瓦纳普拉 [P]
     */
    BWP(CurrencyEnum.BWP, "博茨瓦纳普拉"),

    /**
     * 洪都拉斯伦皮拉 [L]
     */
    HNL(CurrencyEnum.HNL, "洪都拉斯伦皮拉"),

    /**
     * 巴拉圭瓜拉尼 [Gs]
     */
    PYG(CurrencyEnum.PYG, "巴拉圭瓜拉尼"),

    /**
     * 埃塞俄比亚比尔 [ETB]
     */
    ETB(CurrencyEnum.ETB, "埃塞俄比亚比尔"),

    /**
     * 纳米比亚元 [$]
     */
    NAD(CurrencyEnum.NAD, "纳米比亚元"),

    /**
     * 巴布亚新几内亚基那 [PGK]
     */
    PGK(CurrencyEnum.PGK, "巴布亚新几内亚基那"),

    /**
     * 苏丹镑 [SDG]
     */
    SDG(CurrencyEnum.SDG, "苏丹镑"),

    /**
     * 澳门元 [MOP]
     */
    MOP(CurrencyEnum.MOP, "澳门元"),

    /**
     * 尼加拉瓜科多巴 [C$]
     */
    NIO(CurrencyEnum.NIO, "尼加拉瓜科多巴"),

    /**
     * 百慕大元 [$]
     */
    BMD(CurrencyEnum.BMD, "百慕大元"),

    /**
     * 哈萨克斯坦坚戈 [лв]
     */
    KZT(CurrencyEnum.KZT, "哈萨克斯坦坚戈"),

    /**
     * 巴拿马巴波亚 [B/.]
     */
    PAB(CurrencyEnum.PAB, "巴拿马巴波亚"),

    /**
     * 波斯尼亚可兑换马尔卡 [KM]
     */
    BAM(CurrencyEnum.BAM, "波斯尼亚可兑换马尔卡"),

    /**
     * 圭亚那元 [$]
     */
    GYD(CurrencyEnum.GYD, "圭亚那元"),

    /**
     * 也门里亚尔 [﷼]
     */
    YER(CurrencyEnum.YER, "也门里亚尔"),

    /**
     * 马尔加什阿里亚 [MGA]
     */
    MGA(CurrencyEnum.MGA, "马尔加什阿里亚"),

    /**
     * 开曼元 [$]
     */
    KYD(CurrencyEnum.KYD, "开曼元"),

    /**
     * 莫桑比克梅蒂卡尔 [MT]
     */
    MZN(CurrencyEnum.MZN, "莫桑比克梅蒂卡尔"),

    /**
     * 塞尔维亚第纳尔 [Дин.]
     */
    RSD(CurrencyEnum.RSD, "塞尔维亚第纳尔"),

    /**
     * 塞舌尔卢比 [₨]
     */
    SCR(CurrencyEnum.SCR, "塞舌尔卢比"),

    /**
     * 亚美尼亚德拉姆 [AMD]
     */
    AMD(CurrencyEnum.AMD, "亚美尼亚德拉姆"),

    /**
     * 所罗门群岛元 [$]
     */
    SBD(CurrencyEnum.SBD, "所罗门群岛元"),

    /**
     * 阿塞拜疆新马纳特 [ман]
     */
    AZN(CurrencyEnum.AZN, "阿塞拜疆新马纳特"),

    /**
     * 塞拉利昂利昂 [SLL]
     */
    SLL(CurrencyEnum.SLL, "塞拉利昂利昂"),

    /**
     * 汤加潘加 [TOP]
     */
    TOP(CurrencyEnum.TOP, "汤加潘加"),

    /**
     * 伯利兹元 [BZ$]
     */
    BZD(CurrencyEnum.BZD, "伯利兹元"),

    /**
     * 马拉维克瓦查 [MWK]
     */
    MWK(CurrencyEnum.MWK, "马拉维克瓦查"),

    /**
     * 冈比亚达拉西 [GMD]
     */
    GMD(CurrencyEnum.GMD, "冈比亚达拉西"),

    /**
     * 布隆迪法郎 [BIF]
     */
    BIF(CurrencyEnum.BIF, "布隆迪法郎"),

    /**
     * 索马里先令 [S]
     */
    SOS(CurrencyEnum.SOS, "索马里先令"),

    /**
     * 海地古德 [HTG]
     */
    HTG(CurrencyEnum.HTG, "海地古德"),

    /**
     * 几内亚法郎 [GNF]
     */
    GNF(CurrencyEnum.GNF, "几内亚法郎"),

    /**
     * 马尔代夫拉菲亚 [MVR]
     */
    MVR(CurrencyEnum.MVR, "马尔代夫拉菲亚"),

    /**
     * 蒙古图格里克 [₮]
     */
    MNT(CurrencyEnum.MNT, "蒙古图格里克"),

    /**
     * 刚果法郎 [CDF]
     */
    CDF(CurrencyEnum.CDF, "刚果法郎"),

    /**
     * 圣多美多布拉 [STD]
     */
    STD(CurrencyEnum.STD, "圣多美多布拉"),

    /**
     * 塔吉克斯坦索莫尼 [TJS]
     */
    TJS(CurrencyEnum.TJS, "塔吉克斯坦索莫尼"),

    /**
     * 朝鲜元 [₩]
     */
    KPW(CurrencyEnum.KPW, "朝鲜元"),

    /**
     * 缅元 [MMK]
     */
    MMK(CurrencyEnum.MMK, "缅元"),

    /**
     * 巴索托洛蒂 [LSL]
     */
    LSL(CurrencyEnum.LSL, "巴索托洛蒂"),

    /**
     * 利比里亚元 [$]
     */
    LRD(CurrencyEnum.LRD, "利比里亚元"),

    /**
     * 吉尔吉斯斯坦索姆 [лв]
     */
    KGS(CurrencyEnum.KGS, "吉尔吉斯斯坦索姆"),

    /**
     * 直布罗陀镑 [£]
     */
    GIP(CurrencyEnum.GIP, "直布罗陀镑"),

    /**
     * 铂（盎司） [XPT]
     */
    XPT(CurrencyEnum.XPT, "铂（盎司）"),

    /**
     * 摩尔多瓦列伊 [MDL]
     */
    MDL(CurrencyEnum.MDL, "摩尔多瓦列伊"),

    /**
     * 古巴比索 [₱]
     */
    CUP(CurrencyEnum.CUP, "古巴比索"),

    /**
     * 柬埔寨瑞尔 [៛]
     */
    KHR(CurrencyEnum.KHR, "柬埔寨瑞尔"),

    /**
     * 马其顿第纳尔 [ден]
     */
    MKD(CurrencyEnum.MKD, "马其顿第纳尔"),

    /**
     * 瓦努阿图瓦图 [VUV]
     */
    VUV(CurrencyEnum.VUV, "瓦努阿图瓦图"),

    /**
     * 毛里塔尼亚乌吉亚 [MRO]
     */
    MRO(CurrencyEnum.MRO, "毛里塔尼亚乌吉亚"),

    /**
     * 荷兰盾 [ƒ]
     */
    ANG(CurrencyEnum.ANG, "荷兰盾"),

    /**
     * 斯威士兰里兰吉尼 [SZL]
     */
    SZL(CurrencyEnum.SZL, "斯威士兰里兰吉尼"),

    /**
     * 佛得角埃斯库多 [CVE]
     */
    CVE(CurrencyEnum.CVE, "佛得角埃斯库多"),

    /**
     * 苏里南元 [$]
     */
    SRD(CurrencyEnum.SRD, "苏里南元"),

    /**
     * 钯（盎司） [XPD]
     */
    XPD(CurrencyEnum.XPD, "钯（盎司）"),

    /**
     * 巴哈马元 [$]
     */
    BSD(CurrencyEnum.BSD, "巴哈马元"),

    /**
     * 国际货币基金组织特别提款权 [XDR]
     */
    XDR(CurrencyEnum.XDR, "国际货币基金组织特别提款权"),

    /**
     * 卢旺达法郎 [RWF]
     */
    RWF(CurrencyEnum.RWF, "卢旺达法郎"),

    /**
     * 阿鲁巴或荷兰盾 [ƒ]
     */
    AWG(CurrencyEnum.AWG, "阿鲁巴或荷兰盾"),

    /**
     * 吉布提法郎 [DJF]
     */
    DJF(CurrencyEnum.DJF, "吉布提法郎"),

    /**
     * 不丹努尔特鲁姆 [BTN]
     */
    BTN(CurrencyEnum.BTN, "不丹努尔特鲁姆"),

    /**
     * 科摩罗法郎 [KMF]
     */
    KMF(CurrencyEnum.KMF, "科摩罗法郎"),

    /**
     * 萨摩亚塔拉 [WST]
     */
    WST(CurrencyEnum.WST, "萨摩亚塔拉"),

    /**
     * 厄立特里亚纳克法 [ERN]
     */
    ERN(CurrencyEnum.ERN, "厄立特里亚纳克法"),

    /**
     * 福克兰群岛镑 [£]
     */
    FKP(CurrencyEnum.FKP, "福克兰群岛镑"),

    /**
     * 圣赫勒拿镑 [£]
     */
    SHP(CurrencyEnum.SHP, "圣赫勒拿镑"),

    /**
     * 土库曼斯坦马纳特 [TMT]
     */
    TMT(CurrencyEnum.TMT, "土库曼斯坦马纳特");

    /**
     * 币种
     */
    private final CurrencyEnum currencyEnum;

    /**
     * 币种中文描述
     */
    private final String currencyChnShowName;

    /**
     * 私有构造函数。
     *
     * @param currencyEnum        币种
     * @param currencyChnShowName 币种值中文描述
     */
    private CurrencyCHNShowEnum(CurrencyEnum currencyEnum, String currencyChnShowName) {
        this.currencyEnum = currencyEnum;
        this.currencyChnShowName = currencyChnShowName;
    }

    /**
     * Getter method for property <tt>currencyEnum</tt>.
     *
     * @return property value of currencyEnum
     */
    public CurrencyEnum getCurrencyEnum() {
        return currencyEnum;
    }

    /**
     * Getter method for property <tt>currencyCHNShowName</tt>.
     *
     * @return property value of currencyCHNShowName
     */
    public String getCurrencyCHNShowName() {
        return currencyChnShowName;
    }

    /**
     * 通过枚举<code>currencyCode</code>获得枚举。
     *
     * @param currencyEnum 币种
     * @return 币种枚举
     */
    public static CurrencyCHNShowEnum getByCurrencyEnum(CurrencyEnum currencyEnum) {
        for (CurrencyCHNShowEnum each : values()) {
            if (each.getCurrencyEnum().equals(currencyEnum)) {
                return each;
            }
        }
        return null;
    }
}

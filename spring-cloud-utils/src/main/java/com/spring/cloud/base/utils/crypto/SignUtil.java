package com.spring.cloud.base.utils.crypto;

import com.spring.cloud.base.utils.map.MapUtil;
import com.spring.cloud.base.utils.str.StrUtil;

import java.util.Map;

/**
 * @Author: ls
 * @Description: 签名工具类
 * @Date: 2023/4/13 15:56
 */
public class SignUtil {

	/**
	 * 创建签名算法对象<br>
	 * 生成新的私钥公钥对
	 *
	 * @param algorithm 签名算法
	 * @return {@link Sign}
	 */
	public static Sign sign(SignAlgorithm algorithm) {
		return new Sign(algorithm);
	}

	/**
	 * 创建签名算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 *
	 * @param algorithm        签名算法
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64  公钥Base64
	 * @return {@link Sign}
	 */
	public static Sign sign(SignAlgorithm algorithm, String privateKeyBase64, String publicKeyBase64) {
		return new Sign(algorithm, privateKeyBase64, publicKeyBase64);
	}

	/**
	 * 创建Sign算法对象<br>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
	 *
	 * @param algorithm  算法枚举
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 * @return {@link Sign}
	 * 
	 */
	public static Sign sign(SignAlgorithm algorithm, byte[] privateKey, byte[] publicKey) {
		return new Sign(algorithm, privateKey, publicKey);
	}

	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 *
	 * @param crypto      对称加密算法
	 * @param params      参数
	 * @param otherParams 其它附加参数字符串（例如密钥）
	 * @return 签名
	 * 
	 */
	public static String signParams(SymmetricCrypto crypto, Map<?, ?> params, String... otherParams) {
		return signParams(crypto, params, StrUtil.EMPTY, StrUtil.EMPTY, true, otherParams);
	}

	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串
	 *
	 * @param crypto            对称加密算法
	 * @param params            参数
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param isIgnoreNull      是否忽略null的键和值
	 * @param otherParams       其它附加参数字符串（例如密钥）
	 * @return 签名
	 * 
	 */
	public static String signParams(SymmetricCrypto crypto, Map<?, ?> params, String separator,
									String keyValueSeparator, boolean isIgnoreNull, String... otherParams) {
		return crypto.encryptHex(MapUtil.sortJoin(params, separator, keyValueSeparator, isIgnoreNull, otherParams));
	}

	/**
	 * 对参数做md5签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 *
	 * @param params      参数
	 * @param otherParams 其它附加参数字符串（例如密钥）
	 * @return 签名
	 * 
	 */
	public static String signParamsMd5(Map<?, ?> params, String... otherParams) {
		return signParams(DigestAlgorithm.MD5, params, otherParams);
	}

	/**
	 * 对参数做Sha1签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 *
	 * @param params      参数
	 * @param otherParams 其它附加参数字符串（例如密钥）
	 * @return 签名
	 * 
	 */
	public static String signParamsSha1(Map<?, ?> params, String... otherParams) {
		return signParams(DigestAlgorithm.SHA1, params, otherParams);
	}

	/**
	 * 对参数做Sha256签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 *
	 * @param params      参数
	 * @param otherParams 其它附加参数字符串（例如密钥）
	 * @return 签名
	 * 
	 */
	public static String signParamsSha256(Map<?, ?> params, String... otherParams) {
		return signParams(DigestAlgorithm.SHA256, params, otherParams);
	}

	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
	 * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
	 *
	 * @param digestAlgorithm 摘要算法
	 * @param params          参数
	 * @param otherParams     其它附加参数字符串（例如密钥）
	 * @return 签名
	 * 
	 */
	public static String signParams(DigestAlgorithm digestAlgorithm, Map<?, ?> params, String... otherParams) {
		return signParams(digestAlgorithm, params, StrUtil.EMPTY, StrUtil.EMPTY, true, otherParams);
	}

	/**
	 * 对参数做签名<br>
	 * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串
	 *
	 * @param digestAlgorithm   摘要算法
	 * @param params            参数
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param isIgnoreNull      是否忽略null的键和值
	 * @param otherParams       其它附加参数字符串（例如密钥）
	 * @return 签名
	 * 
	 */
	public static String signParams(DigestAlgorithm digestAlgorithm, Map<?, ?> params, String separator,
									String keyValueSeparator, boolean isIgnoreNull, String... otherParams) {
		return new Digester(digestAlgorithm).digestHex(MapUtil.sortJoin(params, separator, keyValueSeparator, isIgnoreNull, otherParams));
	}
}

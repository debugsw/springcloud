package com.spring.cloud.base.utils.crypto;

import com.spring.cloud.base.utils.utils.ArrayUtil;
import com.spring.cloud.base.utils.utils.HexUtil;
import com.spring.cloud.base.utils.base.Base64;
import com.spring.cloud.base.utils.exception.CryptoException;
import com.spring.cloud.base.utils.str.StrUtil;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.InputStream;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Map;

/**
 * @Author: ls
 * @Description: 安全相关工具类
 * @Date: 2023/4/13 16:05
 */
public class SecureUtil {

    /**
     * 默认密钥字节数
     *
     * <pre>
     * RSA/DSA
     * Default Keysize 1024
     * Keysize must be a multiple of 64, ranging from 512 to 1024 (inclusive).
     * </pre>
     */
    public static final int DEFAULT_KEY_SIZE = KeyUtil.DEFAULT_KEY_SIZE;

    /**
     * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
     *
     * @param algorithm 算法，支持PBE算法
     * @return {@link SecretKey}
     */
    public static SecretKey generateKey(String algorithm) {
        return KeyUtil.generateKey(algorithm);
    }

    /**
     * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
     *
     * @param algorithm 算法，支持PBE算法
     * @param keySize   密钥长度
     * @return {@link SecretKey}
     */
    public static SecretKey generateKey(String algorithm, int keySize) {
        return KeyUtil.generateKey(algorithm, keySize);
    }

    /**
     * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
     *
     * @param algorithm 算法
     * @param key       密钥，如果为{@code null} 自动生成随机密钥
     * @return {@link SecretKey}
     */
    public static SecretKey generateKey(String algorithm, byte[] key) {
        return KeyUtil.generateKey(algorithm, key);
    }

    /**
     * 生成 {@link SecretKey}
     *
     * @param algorithm DES算法，包括DES、DESede等
     * @param key       密钥
     * @return {@link SecretKey}
     */
    public static SecretKey generateDESKey(String algorithm, byte[] key) {
        return KeyUtil.generateDESKey(algorithm, key);
    }

    /**
     * 生成PBE {@link SecretKey}
     *
     * @param algorithm PBE算法，包括：PBEWithMD5AndDES、PBEWithSHA1AndDESede、PBEWithSHA1AndRC2_40等
     * @param key       密钥
     * @return {@link SecretKey}
     */
    public static SecretKey generatePBEKey(String algorithm, char[] key) {
        return KeyUtil.generatePBEKey(algorithm, key);
    }

    /**
     * 生成 {@link SecretKey}，仅用于对称加密和摘要算法
     *
     * @param algorithm 算法
     * @param keySpec   {@link KeySpec}
     * @return {@link SecretKey}
     */
    public static SecretKey generateKey(String algorithm, KeySpec keySpec) {
        return KeyUtil.generateKey(algorithm, keySpec);
    }

    /**
     * 生成私钥，仅用于非对称加密<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param algorithm 算法
     * @param key       密钥
     * @return 私钥 {@link PrivateKey}
     */
    public static PrivateKey generatePrivateKey(String algorithm, byte[] key) {
        return KeyUtil.generatePrivateKey(algorithm, key);
    }

    /**
     * 生成私钥，仅用于非对称加密<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param algorithm 算法
     * @param keySpec   {@link KeySpec}
     * @return 私钥 {@link PrivateKey}
     *
     */
    public static PrivateKey generatePrivateKey(String algorithm, KeySpec keySpec) {
        return KeyUtil.generatePrivateKey(algorithm, keySpec);
    }

    /**
     * 生成私钥，仅用于非对称加密
     *
     * @param keyStore {@link KeyStore}
     * @param alias    别名
     * @param password 密码
     * @return 私钥 {@link PrivateKey}
     */
    public static PrivateKey generatePrivateKey(KeyStore keyStore, String alias, char[] password) {
        return KeyUtil.generatePrivateKey(keyStore, alias, password);
    }

    /**
     * 生成公钥，仅用于非对称加密<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param algorithm 算法
     * @param key       密钥
     * @return 公钥 {@link PublicKey}
     */
    public static PublicKey generatePublicKey(String algorithm, byte[] key) {
        return KeyUtil.generatePublicKey(algorithm, key);
    }

    /**
     * 生成公钥，仅用于非对称加密<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param algorithm 算法
     * @param keySpec   {@link KeySpec}
     * @return 公钥 {@link PublicKey}
     *
     */
    public static PublicKey generatePublicKey(String algorithm, KeySpec keySpec) {
        return KeyUtil.generatePublicKey(algorithm, keySpec);
    }

    /**
     * 生成用于非对称加密的公钥和私钥，仅用于非对称加密<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param algorithm 非对称加密算法
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(String algorithm) {
        return KeyUtil.generateKeyPair(algorithm);
    }

    /**
     * 生成用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param algorithm 非对称加密算法
     * @param keySize   密钥模（modulus ）长度
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(String algorithm, int keySize) {
        return KeyUtil.generateKeyPair(algorithm, keySize);
    }

    /**
     * 生成用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param algorithm 非对称加密算法
     * @param keySize   密钥模（modulus ）长度
     * @param seed      种子
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(String algorithm, int keySize, byte[] seed) {
        return KeyUtil.generateKeyPair(algorithm, keySize, seed);
    }

    /**
     * 生成用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param algorithm 非对称加密算法
     * @param params    {@link AlgorithmParameterSpec}
     * @return {@link KeyPair}
     *
     */
    public static KeyPair generateKeyPair(String algorithm, AlgorithmParameterSpec params) {
        return KeyUtil.generateKeyPair(algorithm, params);
    }

    /**
     * 生成用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param algorithm 非对称加密算法
     * @param seed      种子
     * @param params    {@link AlgorithmParameterSpec}
     * @return {@link KeyPair}
     *
     */
    public static KeyPair generateKeyPair(String algorithm, byte[] seed, AlgorithmParameterSpec params) {
        return KeyUtil.generateKeyPair(algorithm, seed, params);
    }

    /**
     * 获取用于密钥生成的算法<br>
     * 获取XXXwithXXX算法的后半部分算法，如果为ECDSA或SM2，返回算法为EC
     *
     * @param algorithm XXXwithXXX算法
     * @return 算法
     */
    public static String getAlgorithmAfterWith(String algorithm) {
        return KeyUtil.getAlgorithmAfterWith(algorithm);
    }

    /**
     * 生成算法，格式为XXXwithXXX
     *
     * @param asymmetricAlgorithm 非对称算法
     * @param digestAlgorithm     摘要算法
     * @return 算法
     *
     */
    public static String generateAlgorithm(AsymmetricAlgorithm asymmetricAlgorithm, DigestAlgorithm digestAlgorithm) {
        final String digestPart = (null == digestAlgorithm) ? "NONE" : digestAlgorithm.name();
        return StrUtil.format("{}with{}", digestPart, asymmetricAlgorithm.getValue());
    }

    /**
     * 生成签名对象，仅用于非对称加密
     *
     * @param asymmetricAlgorithm {@link AsymmetricAlgorithm} 非对称加密算法
     * @param digestAlgorithm     {@link DigestAlgorithm} 摘要算法
     * @return {@link Signature}
     */
    public static Signature generateSignature(AsymmetricAlgorithm asymmetricAlgorithm, DigestAlgorithm digestAlgorithm) {
        try {
            return Signature.getInstance(generateAlgorithm(asymmetricAlgorithm, digestAlgorithm));
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    // ------------------------------------------------------------------- 对称加密算法

    /**
     * AES加密，生成随机KEY。注意解密时必须使用相同 {@link AES}对象或者使用相同KEY<br>
     * 例：
     *
     * <pre>
     * AES加密：aes().encrypt(data)
     * AES解密：aes().decrypt(data)
     * </pre>
     *
     * @return {@link AES}
     */
    public static AES aes() {
        return new AES();
    }

    /**
     * AES加密<br>
     * 例：
     *
     * <pre>
     * AES加密：aes(key).encrypt(data)
     * AES解密：aes(key).decrypt(data)
     * </pre>
     *
     * @param key 密钥
     * @return {@link SymmetricCrypto}
     */
    public static AES aes(byte[] key) {
        return new AES(key);
    }

    /**
     * DES加密，生成随机KEY。注意解密时必须使用相同 {@link DES}对象或者使用相同KEY<br>
     * 例：
     *
     * <pre>
     * DES加密：des().encrypt(data)
     * DES解密：des().decrypt(data)
     * </pre>
     *
     * @return {@link DES}
     */
    public static DES des() {
        return new DES();
    }

    /**
     * DES加密<br>
     * 例：
     *
     * <pre>
     * DES加密：des(key).encrypt(data)
     * DES解密：des(key).decrypt(data)
     * </pre>
     *
     * @param key 密钥
     * @return {@link DES}
     */
    public static DES des(byte[] key) {
        return new DES(key);
    }

    /**
     * DESede加密（又名3DES、TripleDES），生成随机KEY。注意解密时必须使用相同 {@link DESede}对象或者使用相同KEY<br>
     * Java中默认实现为：DESede/ECB/PKCS5Padding<br>
     * 例：
     *
     * <pre>
     * DESede加密：desede().encrypt(data)
     * DESede解密：desede().decrypt(data)
     * </pre>
     *
     * @return {@link DESede}
     *
     */
    public static DESede desede() {
        return new DESede();
    }

    /**
     * DESede加密（又名3DES、TripleDES）<br>
     * Java中默认实现为：DESede/ECB/PKCS5Padding<br>
     * 例：
     *
     * <pre>
     * DESede加密：desede(key).encrypt(data)
     * DESede解密：desede(key).decrypt(data)
     * </pre>
     *
     * @param key 密钥
     * @return {@link DESede}
     *
     */
    public static DESede desede(byte[] key) {
        return new DESede(key);
    }

    // ------------------------------------------------------------------- 摘要算法

    /**
     * MD5加密<br>
     * 例：
     *
     * <pre>
     * MD5加密：md5().digest(data)
     * MD5加密并转为16进制字符串：md5().digestHex(data)
     * </pre>
     *
     * @return {@link Digester}
     */
    public static MD5 md5() {
        return new MD5();
    }

    /**
     * MD5加密，生成16进制MD5字符串<br>
     *
     * @param data 数据
     * @return MD5字符串
     */
    public static String md5(String data) {
        return new MD5().digestHex(data);
    }

    /**
     * MD5加密，生成16进制MD5字符串<br>
     *
     * @param data 数据
     * @return MD5字符串
     */
    public static String md5(InputStream data) {
        return new MD5().digestHex(data);
    }

    /**
     * MD5加密文件，生成16进制MD5字符串<br>
     *
     * @param dataFile 被加密文件
     * @return MD5字符串
     */
    public static String md5(File dataFile) {
        return new MD5().digestHex(dataFile);
    }

    /**
     * SHA1加密<br>
     * 例：<br>
     * SHA1加密：sha1().digest(data)<br>
     * SHA1加密并转为16进制字符串：sha1().digestHex(data)<br>
     *
     * @return {@link Digester}
     */
    public static Digester sha1() {
        return new Digester(DigestAlgorithm.SHA1);
    }

    /**
     * SHA1加密，生成16进制SHA1字符串<br>
     *
     * @param data 数据
     * @return SHA1字符串
     */
    public static String sha1(String data) {
        return new Digester(DigestAlgorithm.SHA1).digestHex(data);
    }

    /**
     * SHA1加密，生成16进制SHA1字符串<br>
     *
     * @param data 数据
     * @return SHA1字符串
     */
    public static String sha1(InputStream data) {
        return new Digester(DigestAlgorithm.SHA1).digestHex(data);
    }

    /**
     * SHA1加密文件，生成16进制SHA1字符串<br>
     *
     * @param dataFile 被加密文件
     * @return SHA1字符串
     */
    public static String sha1(File dataFile) {
        return new Digester(DigestAlgorithm.SHA1).digestHex(dataFile);
    }

    /**
     * SHA256加密<br>
     * 例：<br>
     * SHA256加密：sha256().digest(data)<br>
     * SHA256加密并转为16进制字符串：sha256().digestHex(data)<br>
     *
     * @return {@link Digester}
     *
     */
    public static Digester sha256() {
        return new Digester(DigestAlgorithm.SHA256);
    }

    /**
     * SHA256加密，生成16进制SHA256字符串<br>
     *
     * @param data 数据
     * @return SHA256字符串
     *
     */
    public static String sha256(String data) {
        return new Digester(DigestAlgorithm.SHA256).digestHex(data);
    }

    /**
     * SHA256加密，生成16进制SHA256字符串<br>
     *
     * @param data 数据
     * @return SHA1字符串
     *
     */
    public static String sha256(InputStream data) {
        return new Digester(DigestAlgorithm.SHA256).digestHex(data);
    }

    /**
     * SHA256加密文件，生成16进制SHA256字符串<br>
     *
     * @param dataFile 被加密文件
     * @return SHA256字符串
     *
     */
    public static String sha256(File dataFile) {
        return new Digester(DigestAlgorithm.SHA256).digestHex(dataFile);
    }

    /**
     * 创建HMac对象，调用digest方法可获得hmac值
     *
     * @param algorithm {@link HmacAlgorithm}
     * @param key       密钥，如果为{@code null}生成随机密钥
     * @return {@link HMac}
     *
     */
    public static HMac hmac(HmacAlgorithm algorithm, String key) {
        return hmac(algorithm, StrUtil.isNotEmpty(key)? StrUtil.utf8Bytes(key): null);
    }

    /**
     * 创建HMac对象，调用digest方法可获得hmac值
     *
     * @param algorithm {@link HmacAlgorithm}
     * @param key       密钥，如果为{@code null}生成随机密钥
     * @return {@link HMac}
     *
     */
    public static HMac hmac(HmacAlgorithm algorithm, byte[] key) {
        if (ArrayUtil.isEmpty(key)) {
            key = generateKey(algorithm.getValue()).getEncoded();
        }
        return new HMac(algorithm, key);
    }

    /**
     * 创建HMac对象，调用digest方法可获得hmac值
     *
     * @param algorithm {@link HmacAlgorithm}
     * @param key       密钥{@link SecretKey}，如果为{@code null}生成随机密钥
     * @return {@link HMac}
     *
     */
    public static HMac hmac(HmacAlgorithm algorithm, SecretKey key) {
        if (ObjectUtil.isNull(key)) {
            key = generateKey(algorithm.getValue());
        }
        return new HMac(algorithm, key);
    }

    /**
     * HmacMD5加密器<br>
     * 例：<br>
     * HmacMD5加密：hmacMd5(key).digest(data)<br>
     * HmacMD5加密并转为16进制字符串：hmacMd5(key).digestHex(data)<br>
     *
     * @param key 加密密钥，如果为{@code null}生成随机密钥
     * @return {@link HMac}
     *
     */
    public static HMac hmacMd5(String key) {
        return hmacMd5(StrUtil.isNotEmpty(key)? StrUtil.utf8Bytes(key): null);
    }

    /**
     * HmacMD5加密器<br>
     * 例：<br>
     * HmacMD5加密：hmacMd5(key).digest(data)<br>
     * HmacMD5加密并转为16进制字符串：hmacMd5(key).digestHex(data)<br>
     *
     * @param key 加密密钥，如果为{@code null}生成随机密钥
     * @return {@link HMac}
     */
    public static HMac hmacMd5(byte[] key) {
        if (ArrayUtil.isEmpty(key)) {
            key = generateKey(HmacAlgorithm.HmacMD5.getValue()).getEncoded();
        }
        return new HMac(HmacAlgorithm.HmacMD5, key);
    }

    /**
     * HmacMD5加密器，生成随机KEY<br>
     * 例：<br>
     * HmacMD5加密：hmacMd5().digest(data)<br>
     * HmacMD5加密并转为16进制字符串：hmacMd5().digestHex(data)<br>
     *
     * @return {@link HMac}
     */
    public static HMac hmacMd5() {
        return new HMac(HmacAlgorithm.HmacMD5);
    }

    /**
     * HmacSHA1加密器<br>
     * 例：<br>
     * HmacSHA1加密：hmacSha1(key).digest(data)<br>
     * HmacSHA1加密并转为16进制字符串：hmacSha1(key).digestHex(data)<br>
     *
     * @param key 加密密钥，如果为{@code null}生成随机密钥
     * @return {@link HMac}
     *
     */
    public static HMac hmacSha1(String key) {
        return hmacSha1(StrUtil.isNotEmpty(key)? StrUtil.utf8Bytes(key): null);
    }

    /**
     * HmacSHA1加密器<br>
     * 例：<br>
     * HmacSHA1加密：hmacSha1(key).digest(data)<br>
     * HmacSHA1加密并转为16进制字符串：hmacSha1(key).digestHex(data)<br>
     *
     * @param key 加密密钥，如果为{@code null}生成随机密钥
     * @return {@link HMac}
     */
    public static HMac hmacSha1(byte[] key) {
        if (ArrayUtil.isEmpty(key)) {
            key = generateKey(HmacAlgorithm.HmacMD5.getValue()).getEncoded();
        }
        return new HMac(HmacAlgorithm.HmacSHA1, key);
    }

    /**
     * HmacSHA1加密器，生成随机KEY<br>
     * 例：<br>
     * HmacSHA1加密：hmacSha1().digest(data)<br>
     * HmacSHA1加密并转为16进制字符串：hmacSha1().digestHex(data)<br>
     *
     * @return {@link HMac}
     */
    public static HMac hmacSha1() {
        return new HMac(HmacAlgorithm.HmacSHA1);
    }

    /**
     * HmacSHA256加密器<br>
     * 例：<br>
     * HmacSHA256加密：hmacSha256(key).digest(data)<br>
     * HmacSHA256加密并转为16进制字符串：hmacSha256(key).digestHex(data)<br>
     *
     * @param key 加密密钥，如果为{@code null}生成随机密钥
     * @return {@link HMac}
     *
     */
    public static HMac hmacSha256(String key) {
        return hmacSha256(StrUtil.isNotEmpty(key)? StrUtil.utf8Bytes(key): null);
    }

    /**
     * HmacSHA256加密器<br>
     * 例：<br>
     * HmacSHA256加密：hmacSha256(key).digest(data)<br>
     * HmacSHA256加密并转为16进制字符串：hmacSha256(key).digestHex(data)<br>
     *
     * @param key 加密密钥，如果为{@code null}生成随机密钥
     * @return {@link HMac}
     *
     */
    public static HMac hmacSha256(byte[] key) {
        if (ArrayUtil.isEmpty(key)) {
            key = generateKey(HmacAlgorithm.HmacMD5.getValue()).getEncoded();
        }
        return new HMac(HmacAlgorithm.HmacSHA256, key);
    }

    /**
     * HmacSHA256加密器，生成随机KEY<br>
     * 例：<br>
     * HmacSHA256加密：hmacSha256().digest(data)<br>
     * HmacSHA256加密并转为16进制字符串：hmacSha256().digestHex(data)<br>
     *
     * @return {@link HMac}
     *
     */
    public static HMac hmacSha256() {
        return new HMac(HmacAlgorithm.HmacSHA256);
    }

    // ------------------------------------------------------------------- 非称加密算法

    /**
     * 创建RSA算法对象<br>
     * 生成新的私钥公钥对
     *
     * @return {@link RSA}
     *
     */
    public static RSA rsa() {
        return new RSA();
    }

    /**
     * 创建RSA算法对象<br>
     * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param privateKeyBase64 私钥Base64
     * @param publicKeyBase64  公钥Base64
     * @return {@link RSA}
     *
     */
    public static RSA rsa(String privateKeyBase64, String publicKeyBase64) {
        return new RSA(privateKeyBase64, publicKeyBase64);
    }

    /**
     * 创建RSA算法对象<br>
     * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param privateKey 私钥
     * @param publicKey  公钥
     * @return {@link RSA}
     */
    public static RSA rsa(byte[] privateKey, byte[] publicKey) {
        return new RSA(privateKey, publicKey);
    }

    /**
     * 创建签名算法对象<br>
     * 生成新的私钥公钥对
     *
     * @param algorithm 签名算法
     * @return {@link Sign}
     *
     */
    public static Sign sign(SignAlgorithm algorithm) {
        return SignUtil.sign(algorithm);
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
     *
     */
    public static Sign sign(SignAlgorithm algorithm, String privateKeyBase64, String publicKeyBase64) {
        return SignUtil.sign(algorithm, privateKeyBase64, publicKeyBase64);
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
        return SignUtil.sign(algorithm, privateKey, publicKey);
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
        return SignUtil.signParams(crypto, params, otherParams);
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
        return SignUtil.signParams(crypto, params, separator, keyValueSeparator, isIgnoreNull, otherParams);
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
        return SignUtil.signParamsMd5(params, otherParams);
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
        return SignUtil.signParamsSha1(params, otherParams);
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
        return SignUtil.signParamsSha256(params, otherParams);
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
        return SignUtil.signParams(digestAlgorithm, params, otherParams);
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
        return SignUtil.signParams(digestAlgorithm, params, separator, keyValueSeparator, isIgnoreNull, otherParams);
    }

    /**
     * 增加加密解密的算法提供者，默认优先使用，例如：
     *
     * <pre>
     * addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
     * </pre>
     *
     * @param provider 算法提供者
     *
     */
    public static void addProvider(Provider provider) {
        Security.insertProviderAt(provider, 0);
    }

    /**
     * 解码字符串密钥，可支持的编码如下：
     *
     * <pre>
     * 1. Hex（16进制）编码
     * 1. Base64编码
     * </pre>
     *
     * @param key 被解码的密钥字符串
     * @return 密钥
     *
     */
    public static byte[] decode(String key) {
        return Validator.isHex(key) ? HexUtil.decodeHex(key) : Base64.decode(key);
    }

    /**
     * 创建{@link Cipher}
     *
     * @param algorithm 算法
     * @return {@link Cipher}
     *
     */
    public static Cipher createCipher(String algorithm) {
        final Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();

        Cipher cipher;
        try {
            cipher = (null == provider) ? Cipher.getInstance(algorithm) : Cipher.getInstance(algorithm, provider);
        } catch (Exception e) {
            throw new CryptoException(e);
        }

        return cipher;
    }

    /**
     * 创建{@link MessageDigest}
     *
     * @param algorithm 算法
     * @return {@link MessageDigest}
     *
     */
    public static MessageDigest createMessageDigest(String algorithm) {
        final Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();

        MessageDigest messageDigest;
        try {
            messageDigest = (null == provider) ? MessageDigest.getInstance(algorithm) : MessageDigest.getInstance(algorithm, provider);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }

        return messageDigest;
    }

    /**
     * 创建{@link Mac}
     *
     * @param algorithm 算法
     * @return {@link Mac}
     *
     */
    public static Mac createMac(String algorithm) {
        final Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();

        Mac mac;
        try {
            mac = (null == provider) ? Mac.getInstance(algorithm) : Mac.getInstance(algorithm, provider);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }

        return mac;
    }

    /**
     * 创建{@link Signature}
     *
     * @param algorithm 算法
     * @return {@link Signature}
     *
     */
    public static Signature createSignature(String algorithm) {
        final Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();

        Signature signature;
        try {
            signature = (null == provider) ? Signature.getInstance(algorithm) : Signature.getInstance(algorithm, provider);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }

        return signature;
    }
}

package com.spring.cloud.base.jwt;

import com.spring.cloud.base.jwt.utils.NoneJWTSigner;
import com.spring.cloud.base.utils.date.DateUtil;
import com.spring.cloud.base.utils.exception.ValidateException;
import com.spring.cloud.base.utils.str.StrUtil;

import java.util.Date;

/**
 * @Author: ls
 * @Description: JWT数据校验器
 * @Date: 2023/4/25 11:29
 */
public class JWTValidator {

	private final JWT jwt;

	/**
	 * 创建JWT验证器
	 *
	 * @param token JWT Token
	 * @return JWTValidator
	 */
	public static JWTValidator of(String token) {
		return new JWTValidator(JWT.of(token));
	}

	/**
	 * 创建JWT验证器
	 *
	 * @param jwt JWT对象
	 * @return JWTValidator
	 */
	public static JWTValidator of(JWT jwt) {
		return new JWTValidator(jwt);
	}

	/**
	 * 构造
	 *
	 * @param jwt JWT对象
	 */
	public JWTValidator(JWT jwt) {
		this.jwt = jwt;
	}

	/**
	 * 验证算法，使用JWT对象自带的{@link JWTSigner}
	 *
	 * @return this
	 * @throws ValidateException 验证失败的异常
	 */
	public JWTValidator validateAlgorithm() throws ValidateException {
		return validateAlgorithm(null);
	}

	/**
	 * 验证算法，使用自定义的{@link JWTSigner}
	 *
	 * @param signer 用于验证算法的签名器
	 * @return this
	 * @throws ValidateException 验证失败的异常
	 */
	public JWTValidator validateAlgorithm(JWTSigner signer) throws ValidateException {
		validateAlgorithm(this.jwt, signer);
		return this;
	}

	/**
	 * 检查JWT的以下三两个时间：
	 *
	 * <ul>
	 *     <li>{@link JWTPayload#NOT_BEFORE}：被检查时间必须晚于生效时间</li>
	 *     <li>{@link JWTPayload#EXPIRES_AT}：被检查时间必须早于失效时间</li>
	 *     <li>{@link JWTPayload#ISSUED_AT}：签发时间必须早于失效时间</li>
	 * </ul>
	 * <p>
	 * 如果某个时间没有设置，则不检查（表示无限制）
	 *
	 * @return this
	 * @throws ValidateException 验证失败的异常
	 */
	public JWTValidator validateDate() throws ValidateException {
		return validateDate(DateUtil.beginOfSecond(DateUtil.date()));
	}

	/**
	 * 检查JWT的以下三两个时间：
	 *
	 * <ul>
	 *     <li>{@link JWTPayload#NOT_BEFORE}：生效时间不能晚于当前时间</li>
	 *     <li>{@link JWTPayload#EXPIRES_AT}：失效时间不能早于当前时间</li>
	 *     <li>{@link JWTPayload#ISSUED_AT}： 签发时间不能晚于当前时间</li>
	 * </ul>
	 * <p>
	 * 如果某个时间没有设置，则不检查（表示无限制）
	 *
	 * @param dateToCheck 被检查的时间，一般为当前时间
	 * @return this
	 * @throws ValidateException 验证失败的异常
	 */
	public JWTValidator validateDate(Date dateToCheck) throws ValidateException {
		validateDate(this.jwt.getPayload(), dateToCheck, 0L);
		return this;
	}

	/**
	 * 检查JWT的以下三两个时间：
	 *
	 * <ul>
	 *     <li>{@link JWTPayload#NOT_BEFORE}：生效时间不能晚于当前时间</li>
	 *     <li>{@link JWTPayload#EXPIRES_AT}：失效时间不能早于当前时间</li>
	 *     <li>{@link JWTPayload#ISSUED_AT}： 签发时间不能晚于当前时间</li>
	 * </ul>
	 * <p>
	 * 如果某个时间没有设置，则不检查（表示无限制）
	 *
	 * @param dateToCheck 被检查的时间，一般为当前时间
	 * @param leeway      容忍空间，单位：秒。当不能晚于当前时间时，向后容忍；不能早于向前容忍。
	 * @return this
	 * @throws ValidateException 验证失败的异常
	 */
	public JWTValidator validateDate(Date dateToCheck, long leeway) throws ValidateException {
		validateDate(this.jwt.getPayload(), dateToCheck, leeway);
		return this;
	}

	/**
	 * 验证算法
	 *
	 * @param jwt    {@link JWT}对象
	 * @param signer 用于验证的签名器
	 * @throws ValidateException 验证异常
	 */
	private static void validateAlgorithm(JWT jwt, JWTSigner signer) throws ValidateException {
		final String algorithmId = jwt.getAlgorithm();
		if (null == signer) {
			signer = jwt.getSigner();
		}

		if (StrUtil.isEmpty(algorithmId)) {
			
			if (null == signer || signer instanceof NoneJWTSigner) {
				return;
			}
			throw new ValidateException("No algorithm defined in header!");
		}

		if (null == signer) {
			throw new IllegalArgumentException("No Signer for validate algorithm!");
		}

		final String algorithmIdInSigner = signer.getAlgorithmId();
		if (false == StrUtil.equals(algorithmId, algorithmIdInSigner)) {
			throw new ValidateException("Algorithm [{}] defined in header doesn't match to [{}]!"
					, algorithmId, algorithmIdInSigner);
		}

		
		if (false == jwt.verify(signer)) {
			throw new ValidateException("Signature verification failed!");
		}
	}

	/**
	 * 检查JWT的以下三两个时间：
	 *
	 * <ul>
	 *     <li>{@link JWTPayload#NOT_BEFORE}：生效时间不能晚于当前时间</li>
	 *     <li>{@link JWTPayload#EXPIRES_AT}：失效时间不能早于当前时间</li>
	 *     <li>{@link JWTPayload#ISSUED_AT}： 签发时间不能晚于当前时间</li>
	 * </ul>
	 * <p>
	 * 如果某个时间没有设置，则不检查（表示无限制）
	 *
	 * @param payload {@link JWTPayload}
	 * @param now     当前时间
	 * @param leeway  容忍空间，单位：秒。当不能晚于当前时间时，向后容忍；不能早于向前容忍。
	 * @throws ValidateException 验证异常
	 */
	private static void validateDate(JWTPayload payload, Date now, long leeway) throws ValidateException {
		if (null == now) {
			
			now = DateUtil.date();
			
			now.setTime(now.getTime() / 1000 * 1000);
		}

		
		final Date notBefore = payload.getClaimsJson().getDate(JWTPayload.NOT_BEFORE);
		validateNotAfter(JWTPayload.NOT_BEFORE, notBefore, now, leeway);

		
		final Date expiresAt = payload.getClaimsJson().getDate(JWTPayload.EXPIRES_AT);
		validateNotBefore(JWTPayload.EXPIRES_AT, expiresAt, now, leeway);

		
		final Date issueAt = payload.getClaimsJson().getDate(JWTPayload.ISSUED_AT);
		validateNotAfter(JWTPayload.ISSUED_AT, issueAt, now, leeway);
	}

	/**
	 * 验证指定字段的时间不能晚于当前时间
	 * 被检查的日期不存在则跳过
	 *
	 * @param fieldName   字段名
	 * @param dateToCheck 被检查的字段日期
	 * @param now         当前时间
	 * @param leeway      容忍空间，单位：秒。向后容忍
	 * @throws ValidateException 验证异常
	 */
	private static void validateNotAfter(String fieldName, Date dateToCheck, Date now, long leeway) throws ValidateException {
		if (null == dateToCheck) {
			return;
		}
		if (leeway > 0) {
			now = DateUtil.date(now.getTime() + leeway * 1000);
		}
		if (dateToCheck.after(now)) {
			throw new ValidateException("'{}':[{}] is after now:[{}]",
					fieldName, DateUtil.date(dateToCheck), DateUtil.date(now));
		}
	}

	/**
	 * 验证指定字段的时间不能早于当前时间
	 * 被检查的日期不存在则跳过
	 *
	 * @param fieldName   字段名
	 * @param dateToCheck 被检查的字段日期
	 * @param now         当前时间
	 * @param leeway      容忍空间，单位：秒。。向前容忍
	 * @throws ValidateException 验证异常
	 */
	@SuppressWarnings("SameParameterValue")
	private static void validateNotBefore(String fieldName, Date dateToCheck, Date now, long leeway) throws ValidateException {
		if (null == dateToCheck) {
			return;
		}
		if (leeway > 0) {
			now = DateUtil.date(now.getTime() - leeway * 1000);
		}
		if (dateToCheck.before(now)) {
			throw new ValidateException("'{}':[{}] is before now:[{}]",
					fieldName, DateUtil.date(dateToCheck), DateUtil.date(now));
		}
	}
}

package com.spring.cloud.base.captcha;

import com.spring.cloud.base.captcha.utils.GraphicsUtil;
import com.spring.cloud.base.captcha.utils.ImgUtil;
import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.crypto.RandomUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author: ls
 * @Description: 圆圈干扰验证码
 * @Date: 2023/4/17 15:00
 */
public class CircleCaptcha extends AbstractCaptcha {
	private static final long serialVersionUID = -7096627300356535494L;

	/**
	 * 构造
	 *
	 * @param width  图片宽
	 * @param height 图片高
	 */
	public CircleCaptcha(int width, int height) {
		this(width, height, 5);
	}

	/**
	 * 构造
	 *
	 * @param width     图片宽
	 * @param height    图片高
	 * @param codeCount 字符个数
	 */
	public CircleCaptcha(int width, int height, int codeCount) {
		this(width, height, codeCount, 15);
	}

	/**
	 * 构造
	 *
	 * @param width          图片宽
	 * @param height         图片高
	 * @param codeCount      字符个数
	 * @param interfereCount 验证码干扰元素个数
	 */
	public CircleCaptcha(int width, int height, int codeCount, int interfereCount) {
		super(width, height, codeCount, interfereCount);
	}

	@Override
	public Image createImage(String code) {
		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g = ImgUtil.createGraphics(image, ObjectUtil.defaultIfNull(this.background, Color.WHITE));


		drawInterfere(g);


		drawString(g, code);

		return image;
	}

	/**
	 * 绘制字符串
	 *
	 * @param g    {@link Graphics2D}画笔
	 * @param code 验证码
	 */
	private void drawString(Graphics2D g, String code) {

		if (null != this.textAlpha) {
			g.setComposite(this.textAlpha);
		}
		GraphicsUtil.drawStringColourful(g, code, this.font, this.width, this.height);
	}

	/**
	 * 画随机干扰
	 *
	 * @param g {@link Graphics2D}
	 */
	private void drawInterfere(Graphics2D g) {
		final ThreadLocalRandom random = RandomUtil.getRandom();

		for (int i = 0; i < this.interfereCount; i++) {
			g.setColor(ImgUtil.randomColor(random));
			g.drawOval(random.nextInt(width), random.nextInt(height), random.nextInt(height >> 1), random.nextInt(height >> 1));
		}
	}
}

package com.spring.cloud.base.captcha;

import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.crypto.RandomUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * @Author: ls
 * @Description: Gif验证码类
 * @Date: 2023/4/17 15:00
 */
public class GifCaptcha extends AbstractCaptcha {

	private static final long serialVersionUID = 7091627304326538464L;

	
	private int quality = 10;
	
	private int repeat = 0;
	
	private int minColor = 0;
	
	private int maxColor = 255;


	/**
	 * 可以设置验证码宽度，高度的构造函数
	 *
	 * @param width  验证码宽度
	 * @param height 验证码高度
	 */
	public GifCaptcha(int width, int height) {
		this(width, height, 5);
	}

	/**
	 * @param width     验证码宽度
	 * @param height    验证码高度
	 * @param codeCount 验证码个数
	 */
	public GifCaptcha(int width, int height, int codeCount) {
		super(width, height, codeCount, 10);
	}

	/**
	 * 设置图像的颜色量化(转换质量 由GIF规范允许的最大256种颜色)。
	 * 低的值(最小值= 1)产生更好的颜色,但处理显著缓慢。
	 * 10是默认,并产生良好的颜色而且有以合理的速度。
	 * 值更大(大于20)不产生显著的改善速度
	 *
	 * @param quality 大于1
	 * @return this
	 */
	public GifCaptcha setQuality(int quality) {
		if (quality < 1) {
			quality = 1;
		}
		this.quality = quality;
		return this;
	}

	/**
	 * 设置GIF帧应该播放的次数。
	 * 默认是 0; 0意味着无限循环。
	 * 必须在添加的第一个图像之前被调用。
	 *
	 * @param repeat 必须大于等于0
	 * @return this
	 */
	public GifCaptcha setRepeat(int repeat) {
		if (repeat >= 0) {
			this.repeat = repeat;
		}
		return this;
	}

	/**
	 * 设置验证码字符颜色
	 *
	 * @param maxColor 颜色
	 * @return this
	 */
	public GifCaptcha setMaxColor(int maxColor) {
		this.maxColor = maxColor;
		return this;
	}

	/**
	 * 设置验证码字符颜色
	 *
	 * @param minColor 颜色
	 * @return this
	 */
	public GifCaptcha setMinColor(int minColor) {
		this.minColor = minColor;
		return this;
	}

	@Override
	public void createCode() {
		generateCode();
		final ByteArrayOutputStream out = new ByteArrayOutputStream();

		AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
		
		gifEncoder.start(out);
		gifEncoder.setQuality(quality);
		
		int delay = 100;
		gifEncoder.setDelay(delay);
		gifEncoder.setRepeat(repeat);
		BufferedImage frame;
		char[] chars = code.toCharArray();
		Color[] fontColor = new Color[chars.length];
		for (int i = 0; i < chars.length; i++) {
			fontColor[i] = getRandomColor(minColor, maxColor);
			frame = graphicsImage(chars, fontColor, chars, i);
			gifEncoder.addFrame(frame);
			frame.flush();
		}
		gifEncoder.finish();
		this.imageBytes = out.toByteArray();
	}

	@Override
	protected Image createImage(String code) {
		return null;
	}

	/**
	 * 画随机码图
	 *
	 * @param fontColor 随机字体颜色
	 * @param words     字符数组
	 * @param flag      透明度使用
	 * @return BufferedImage
	 */
	private BufferedImage graphicsImage(char[] chars, Color[] fontColor, char[] words, int flag) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g2d = image.createGraphics();
		
		g2d.setColor(ObjectUtil.defaultIfNull(this.background, Color.WHITE));
		g2d.fillRect(0, 0, width, height);
		AlphaComposite ac;
		
		float y = (height >> 1) + (font.getSize() >> 1);
		float m = 1.0f * (width - (chars.length * font.getSize())) / chars.length;
		
		float x = Math.max(m / 2.0f, 2);
		g2d.setFont(font);
		
		if (null != this.textAlpha) {
			g2d.setComposite(this.textAlpha);
		}
		for (int i = 0; i < chars.length; i++) {
			ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha(chars.length, flag, i));
			g2d.setComposite(ac);
			g2d.setColor(fontColor[i]);
			g2d.drawOval(
					RandomUtil.randomInt(width),
					RandomUtil.randomInt(height),
					RandomUtil.randomInt(5, 30), 5 + RandomUtil.randomInt(5, 30)
			);
			g2d.drawString(words[i] + "", x + (font.getSize() + m) * i, y);
		}
		g2d.dispose();
		return image;
	}

	/**
	 * 获取透明度,从0到1,自动计算步长
	 *
	 * @return float 透明度
	 */
	private float getAlpha(int v, int i, int j) {
		int num = i + j;
		float r = (float) 1 / v;
		float s = (v + 1) * r;
		return num > v ? (num * r - s) : num * r;
	}

	/**
	 * 通过给定范围获得随机的颜色
	 *
	 * @return Color 获得随机的颜色
	 */
	private Color getRandomColor(int min, int max) {
		if (min > 255) {
			min = 255;
		}
		if (max > 255) {
			max = 255;
		}
		if (min < 0) {
			min = 0;
		}
		if (max < 0) {
			max = 0;
		}
		if (min > max) {
			min = 0;
			max = 255;
		}
		return new Color(
				RandomUtil.randomInt(min, max),
				RandomUtil.randomInt(min, max),
				RandomUtil.randomInt(min, max));
	}
}

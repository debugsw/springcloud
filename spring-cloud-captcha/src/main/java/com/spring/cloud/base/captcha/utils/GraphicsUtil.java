package com.spring.cloud.base.captcha.utils;

import com.spring.cloud.base.captcha.utils.FontUtil;
import com.spring.cloud.base.captcha.utils.ImgUtil;
import com.spring.cloud.base.utils.crypto.ObjectUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @Author: ls
 * @Description: Graphics相关工具类
 * @Date: 2023/4/17 15:00
 */
public class GraphicsUtil {

	/**
	 * 创建{@link Graphics2D}
	 *
	 * @param image {@link BufferedImage}
	 * @param color {@link Color}背景颜色以及当前画笔颜色，{@code null}表示不设置背景色
	 * @return {@link Graphics2D}
	 */
	public static Graphics2D createGraphics(BufferedImage image, Color color) {
		final Graphics2D g = image.createGraphics();
		if (null != color) {
			
			g.setColor(color);
			g.fillRect(0, 0, image.getWidth(), image.getHeight());
		}
		return g;
	}

	/**
	 * 获取文字居中高度的Y坐标（距离上边距距离）<br>
	 * 此方法依赖FontMetrics，如果获取失败，默认为背景高度的1/3
	 *
	 * @param g                {@link Graphics2D}画笔
	 * @param backgroundHeight 背景高度
	 * @return 最小高度，-1表示无法获取
	 */
	public static int getCenterY(Graphics g, int backgroundHeight) {
		
		FontMetrics metrics = null;
		try {
			metrics = g.getFontMetrics();
		} catch (Exception e) {
			System.err.println(e);
		}
		int y;
		if (null != metrics) {
			y = (backgroundHeight - metrics.getHeight()) / 2 + metrics.getAscent();
		} else {
			y = backgroundHeight / 3;
		}
		return y;
	}

	/**
	 * 绘制字符串，使用随机颜色，默认抗锯齿
	 *
	 * @param g      {@link Graphics}画笔
	 * @param str    字符串
	 * @param font   字体
	 * @param width  字符串总宽度
	 * @param height 字符串背景高度
	 * @return 画笔对象
	 */
	public static Graphics drawStringColourful(Graphics g, String str, Font font, int width, int height) {
		return drawString(g, str, font, null, width, height);
	}

	/**
	 * 绘制字符串，默认抗锯齿
	 *
	 * @param g      {@link Graphics}画笔
	 * @param str    字符串
	 * @param font   字体
	 * @param color  字体颜色，{@code null} 表示使用随机颜色（每个字符单独随机）
	 * @param width  字符串背景的宽度
	 * @param height 字符串背景的高度
	 * @return 画笔对象
	 */
	public static Graphics drawString(Graphics g, String str, Font font, Color color, int width, int height) {
		
		if (g instanceof Graphics2D) {
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		g.setFont(font);

		
		int midY = getCenterY(g, height);
		if (null != color) {
			g.setColor(color);
		}

		final int len = str.length();
		int charWidth = width / len;
		for (int i = 0; i < len; i++) {
			if (null == color) {
				
				g.setColor(ImgUtil.randomColor());
			}
			g.drawString(String.valueOf(str.charAt(i)), i * charWidth, midY);
		}
		return g;
	}

	/**
	 * 绘制字符串，默认抗锯齿。<br>
	 * 此方法定义一个矩形区域和坐标，文字基于这个区域中间偏移x,y绘制。
	 *
	 * @param g         {@link Graphics}画笔
	 * @param str       字符串
	 * @param font      字体，字体大小决定了在背景中绘制的大小
	 * @param color     字体颜色，{@code null} 表示使用黑色
	 * @param rectangle 字符串绘制坐标和大小，此对象定义了绘制字符串的区域大小和偏移位置
	 * @return 画笔对象
	 */
	public static Graphics drawString(Graphics g, String str, Font font, Color color, Rectangle rectangle) {
		
		final int backgroundWidth = rectangle.width;
		final int backgroundHeight = rectangle.height;

		
		Dimension dimension;
		try {
			dimension = FontUtil.getDimension(g.getFontMetrics(font), str);
		} catch (Exception e) {
			
			dimension = new Dimension(backgroundWidth / 3, backgroundHeight / 3);
		}

		rectangle.setSize(dimension.width, dimension.height);
		final Point point = ImgUtil.getPointBaseCentre(rectangle, backgroundWidth, backgroundHeight);

		return drawString(g, str, font, color, point);
	}

	/**
	 * 绘制字符串，默认抗锯齿
	 *
	 * @param g     {@link Graphics}画笔
	 * @param str   字符串
	 * @param font  字体，字体大小决定了在背景中绘制的大小
	 * @param color 字体颜色，{@code null} 表示使用黑色
	 * @param point 绘制字符串的位置坐标
	 * @return 画笔对象
	 */
	public static Graphics drawString(Graphics g, String str, Font font, Color color, Point point) {
		
		if (g instanceof Graphics2D) {
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}

		g.setFont(font);
		g.setColor(ObjectUtil.defaultIfNull(color, Color.BLACK));
		g.drawString(str, point.x, point.y);

		return g;
	}

	/**
	 * 绘制图片
	 *
	 * @param g     画笔
	 * @param img   要绘制的图片
	 * @param point 绘制的位置，基于左上角
	 * @return 画笔对象
	 */
	public static Graphics drawImg(Graphics g, Image img, Point point) {
		return drawImg(g, img,
				new Rectangle(point.x, point.y, img.getWidth(null), img.getHeight(null)));
	}

	/**
	 * 绘制图片
	 *
	 * @param g         画笔
	 * @param img       要绘制的图片
	 * @param rectangle 矩形对象，表示矩形区域的x，y，width，height,，基于左上角
	 * @return 画笔对象
	 */
	public static Graphics drawImg(Graphics g, Image img, Rectangle rectangle) {
		g.drawImage(img, rectangle.x, rectangle.y, rectangle.width, rectangle.height, null);
		return g;
	}

	/**
	 * 设置画笔透明度
	 *
	 * @param g     画笔
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @return 画笔
	 */
	public static Graphics2D setAlpha(Graphics2D g, float alpha) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		return g;
	}
}

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
 * @Description: 使用干扰线方式生成的图形验证码
 * @Date: 2023/4/17 15:00
 */
public class LineCaptcha extends AbstractCaptcha {

    private static final long serialVersionUID = 8691294460763091089L;

    /**
     * 构造，默认5位验证码，150条干扰线
     *
     * @param width  图片宽
     * @param height 图片高
     */
    public LineCaptcha(int width, int height) {
        this(width, height, 5, 150);
    }

    /**
     * 构造
     *
     * @param width     图片宽
     * @param height    图片高
     * @param codeCount 字符个数
     * @param lineCount 干扰线条数
     */
    public LineCaptcha(int width, int height, int codeCount, int lineCount) {
        super(width, height, codeCount, lineCount);
    }

    @Override
    public Image createImage(String code) {
        
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = GraphicsUtil.createGraphics(image, ObjectUtil.defaultIfNull(this.background, Color.WHITE));

        
        drawInterfere(g);

        
        drawString(g, code);

        return image;
    }

    /**
     * 绘制字符串
     *
     * @param g    {@link Graphics}画笔
     * @param code 验证码
     */
    private void drawString(Graphics2D g, String code) {
        
        if (null != this.textAlpha) {
            g.setComposite(this.textAlpha);
        }
        GraphicsUtil.drawStringColourful(g, code, this.font, this.width, this.height);
    }

    /**
     * 绘制干扰线
     *
     * @param g {@link Graphics2D}画笔
     */
    private void drawInterfere(Graphics2D g) {
        final ThreadLocalRandom random = RandomUtil.getRandom();
        
        for (int i = 0; i < this.interfereCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width / 8);
            int ye = ys + random.nextInt(height / 8);
            g.setColor(ImgUtil.randomColor(random));
            g.drawLine(xs, ys, xe, ye);
        }
    }
}
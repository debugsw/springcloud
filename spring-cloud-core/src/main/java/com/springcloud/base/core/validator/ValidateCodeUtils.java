package com.springcloud.base.core.validator;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.UUID;

/**
 * @Author: ls
 * @Description: 验证码生成器
 * @Date: 2023/1/28 11:05
 */
@Slf4j
public class ValidateCodeUtils {

    private static final String BASE_NUM_LETTER = "123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";

    /**
     * 绘制验证码
     *
     * @param widthPx  宽度
     * @param heightPx 敢赌
     * @return 返回对象
     */
    public static ValidateCodeResultVo drawValidateCode(int widthPx, int heightPx) {
        
        widthPx = widthPx <= 0 ? 200 : widthPx;
        heightPx = heightPx <= 0 ? 40 : heightPx;

        
        int fontSize = (int) Math.max(Math.min((heightPx * 0.8), (widthPx * 0.2)), 1);

        BufferedImage verifyImg = new BufferedImage(widthPx, heightPx, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = (Graphics2D) verifyImg.getGraphics();
        
        graphics.setColor(Color.WHITE);
        
        graphics.fillRect(0, 0, widthPx, heightPx);
        graphics.setFont(new Font("微软雅黑", Font.BOLD, fontSize));

        StringBuilder sBuffer = new StringBuilder();
        int x = (int) ((widthPx - (fontSize * 4)) * 0.5);
        int y = (int) (heightPx - ((heightPx - fontSize) * 0.4));
        String ch = "";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            graphics.setColor(getRandomColor());
            
            int degree = random.nextInt() % 30;
            int dot = random.nextInt(BASE_NUM_LETTER.length());
            ch = BASE_NUM_LETTER.charAt(dot) + "";
            sBuffer.append(ch);
            
            graphics.rotate(degree * Math.PI / 180, x, y);
            graphics.drawString(ch, x, y);
            
            graphics.rotate(-degree * Math.PI / 180, x, y);
            x += widthPx * 0.2;
        }

        
        for (int i = 0; i < 6; i++) {
            
            graphics.setColor(getRandomColor());
            
            graphics.drawLine(random.nextInt(widthPx), random.nextInt(heightPx),
                    random.nextInt(widthPx), random.nextInt(heightPx));
        }

        
        for (int i = 0; i < 30; i++) {
            int x1 = random.nextInt(widthPx);
            int y1 = random.nextInt(widthPx);
            graphics.setColor(getRandomColor());
            graphics.fillRect(x1, y1, 2, 2);
        }

        ValidateCodeResultVo validateCodeResultVo = new ValidateCodeResultVo();
        validateCodeResultVo.setCode(sBuffer.toString());
        validateCodeResultVo.setUuid(UUID.randomUUID().toString());
        validateCodeResultVo.setValidateCode(verifyImg);

        return validateCodeResultVo;
    }

    /**
     * 将内容输出到 httpResponse
     *
     * @param httpServletResponse  httpResponse
     * @param validateCodeResultVo 获取的验证码对象
     */
    public static void resultResponse(HttpServletResponse httpServletResponse, ValidateCodeResultVo validateCodeResultVo) {
        httpServletResponse.setContentType("image/png");
        httpServletResponse.addHeader("_validateUid", validateCodeResultVo.getUuid());
        try {
            
            OutputStream os = httpServletResponse.getOutputStream();
            
            ImageIO.write(validateCodeResultVo.getValidateCode(), "png", os);
            os.flush();
            os.close();
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }


    /**
     * 随机取色
     */
    private static Color getRandomColor() {
        Random ran = new Random();
        return new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
    }

    @Getter
    @Setter
    public static class ValidateCodeResultVo {
        private String uuid;
        private BufferedImage validateCode;
        private String code;

        public ValidateCodeResultVo clearCode() {
            this.code = null;
            return this;
        }
    }
}

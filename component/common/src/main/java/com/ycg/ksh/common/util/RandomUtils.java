package com.ycg.ksh.common.util;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/13
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 随机
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/13
 */
public class RandomUtils extends org.apache.commons.lang.math.RandomUtils {

    private static char[] CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static long random(long n, long m){
        return Math.round(Math.random() * (m - n) + n);
    }

    public static String numberString(int count){
        return String.valueOf(random(min(count), max(count)));
    }
    public static String string(int count){
        char[] chars = new char[count];
        for (int i = 0; i < count; i++) {
            chars[i] = CHARS[(int) random(0, CHARS.length - 1)];
        }
        return new String(chars);
    }

    private static long max(int count) {
        long value = 0;
        for (int i = 0; i < count; i++) {
            value += 9 * (Math.pow(10, i));
        }
        return value;
    }
    private static long min(int count) {
        return (long) Math.pow(10, count - 1);
    }

    public static BufferedImage createCode(String codeString, int width, int height){
        return createCode(codeString, width, height, 20);
    }

    public static BufferedImage createCode(String codeString, int width, int height, int lineCount) {
        int x = 0, fontHeight = 0, codeY = 0;
        int red = 0, green = 0, blue = 0;
        String[] codes = codeString.split("");

        x = width / (codes.length);//每个字符的宽度(左右各空出一个字符)
        fontHeight = height - 2;//字体的高度
        codeY = height - 4;

        // 图像buffer
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 生成随机数
        Random random = new Random();
        // 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        // 创建字体,可以修改为其它的
        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
        //Font font = new Font("Times New Roman", Font.ROMAN_BASELINE, fontHeight);
        g.setFont(font);

        for (int i = 0; i < lineCount; i++) {
            // 设置随机开始和结束坐标
            int xs = random.nextInt(width);//x坐标开始
            int ys = random.nextInt(height);//y坐标开始
            int xe = xs + random.nextInt(width / 8);//x坐标结束
            int ye = ys + random.nextInt(height / 8);//y坐标结束

            // 产生随机的颜色值，让输出的每个干扰线的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawLine(xs, ys, xe, ye);
        }
        // randomCode记录随机产生的验证码
        for (int i = 0; i < codes.length; i++) {
            // 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawString(codes[i], i * x, codeY);
        }
        return buffImg;
    }

}

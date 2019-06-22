package com.ycg.ksh.common.util;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.system.Globallys;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SealBuilder {

    private static final int S = 400;//图片宽高

    private static final int S_W = 5;//线条的宽度

    private static final Font CN_FONT = new Font("宋体", Font.BOLD, 40);//企业名称字体
    private static final Font SN_FONT = new Font("楷体", Font.BOLD, 30);//公章的名称字体
    private static final Font SCN_FONT = new Font("楷体", Font.PLAIN, 30);//防伪码字体

    public static FileEntity saveImage(FileEntity fileEntity, BufferedImage bufferedImage) throws IOException {
        fileEntity.setSuffix("png");
        fileEntity.setFileName(FileUtils.appendSuffix(Globallys.next(), fileEntity.getSuffix()));
        ImageIO.write(bufferedImage, "PNG", new File(fileEntity.getPath()));
        return fileEntity;
    }


    private static BufferedImage createBufferedImage(int w, int h, int t){
        return new BufferedImage(w, h, t);
    }
    private static BufferedImage createBufferedImage(int w, int h){
        return createBufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
    }
    private static Graphics2D createGraphics2D(BufferedImage buffered){
        Graphics2D g2d = buffered.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        return g2d;
    }

    /**
     * 企业圆章
     * @param companyName 企业名称
     * @param sealName  公章的名称
     * @param securityCode  防伪码 最多13为数字
     * @param needStar  公章中间是否需要 五角星
     * @return
     */
    public static BufferedImage circleSeal(String companyName, String sealName, String securityCode, boolean needStar){
        BufferedImage buffered = createBufferedImage(S, S);
        Graphics2D g2d = createGraphics2D(buffered);

        g2d.setPaint(Color.red);//画笔颜色
        g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));//设置画笔的粗度
        //画圆
        g2d.drawOval(S_W, S_W, S - 2 * S_W, S - 2 * S_W);
        g2d.translate(S / 2, S / 2);
        float sealNameOffset = 0;
        if(needStar){
            float radius = S / 4;
            if(sealName != null && sealName.length() > 0){
                radius = radius * 0.6F;
            }
            drawFiveStar(g2d, radius);
            sealNameOffset = radius + 30;
        }
        if(sealName != null && sealName.length() > 0){
            //画公章名称
            g2d.setFont(SN_FONT);
            drawHorizontalText(g2d, sealName, sealNameOffset);
        }

        //画企业名称
        g2d.setFont(CN_FONT);
        drawAngleText(g2d, companyName.toCharArray(), 210F, true);
        //画底部的防伪码
        g2d.setFont(SCN_FONT);
        drawAngleText(g2d, securityCode.toCharArray(), 90F, false);

        return buffered;
    }

    private static void drawAngleText(Graphics2D g2d, char[] chars, float useAngle, boolean clockwise){
        int r = S / 2;// 半径
        FontMetrics fm = g2d.getFontMetrics();
        int h = fm.getHeight();// 字高度
        //r = r - S_W;
        int count = chars.length;// 字数

        float angle = useAngle / (count - 1);// 字间角度
        float start = 90 + (clockwise ? (360 - useAngle) : useAngle) / 2;// 以x轴正向为0,顺时针旋转
        double vr = Math.toRadians(clockwise ? 90 : -90);// 垂直旋转弧度
        for (int i = 0; i < count; i++) {
            char c = chars[i];// 需要绘制的字符
            int cw = fm.charWidth(c);// 此字符宽度
            float a = start + angle * i * (clockwise ? 1 : -1);// 现在角度
            double radians = Math.toRadians(a);
            g2d.rotate(radians);// 旋转坐标系,让要绘制的字符处于x正轴
            float x = r - h;// 绘制字符的x坐标为半径减去字高度
            // g2d.drawLine(0, 0, (int) x, 0);// debug
            g2d.translate(x, 0);// 移动到此位置,此时字和x轴垂直
            g2d.rotate(vr);// 旋转90度,让字平行于x轴
            g2d.scale(1.0F, 1);// 缩放字体宽度
            g2d.drawString(String.valueOf(c), -cw / 2, clockwise ? 0 : h / 2);//此点为字的中心点
            // 将所有设置还原,等待绘制下一个
            g2d.scale(1 / 1.0F, 1);
            g2d.rotate(-vr);
            g2d.translate(-x, 0);
            g2d.rotate(-radians);
        }
    }


    private static void drawFiveStar(Graphics2D g2d, float radius) {
        if (radius <= 0) return;
        float lradius = radius * 0.381966f;// 根据radius求内圆半径
        double halfpi = Math.PI / 180f;
        Polygon polygon = new Polygon();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 1){
                polygon.addPoint((int) (Math.sin(halfpi * 36 * i) * radius), (int) (Math.cos(halfpi * 36 * i) * radius));
            } else {
                polygon.addPoint((int) (Math.sin(halfpi * 36 * i) * lradius), (int) (Math.cos(halfpi * 36 * i) * lradius));
            }
        }
        g2d.setColor(Color.RED);
        g2d.fill(polygon);
    }

    private static void drawHorizontalText(Graphics2D g2d, String textString, float offset) {
        FontMetrics fm = g2d.getFontMetrics();
        int w = fm.stringWidth(textString);// 名称宽度
        int h = fm.getHeight();// 名称高度
        int y = fm.getAscent() - h / 2;// 求得中心线经过字体的高度的一半时的字体的起绘点
        g2d.drawString(textString, -w / 2, y + offset);
    }


    public static void main(String[] args) {
        BufferedImage image = SealBuilder.circleSeal("上海远成储运有限公司", "电子签收专用章", "123244365476583", true);

        try
        {
            ImageIO.write(image, "PNG", new File("X:\\backup\\temp\\"+ System.nanoTime() +".png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}

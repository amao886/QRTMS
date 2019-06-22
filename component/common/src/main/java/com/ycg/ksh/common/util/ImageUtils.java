package com.ycg.ksh.common.util;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/20
 */

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * 图片工具类
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/20
 */
public class ImageUtils {

    public static String encode(InputStream in) {
        byte[] data = null;
        try {
            data = new byte[in.available()];
            in.read(data);
            return new String(Base64.encodeBase64(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream decode(String base64String) {
        byte[] bytes = Base64.decodeBase64(base64String);
        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] < 0) {//调整异常数据
                bytes[i] += 256;
            }
        }
        return new ByteArrayInputStream(bytes);
    }

    public static byte[] decodeBytes(String base64String) {
        byte[] bytes = Base64.decodeBase64(base64String);
        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] < 0) {//调整异常数据
                bytes[i] += 256;
            }
        }
        return bytes;
    }


    public static BufferedImage createCode(String codeString, int width, int height) {
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


    public static BufferedImage createSeal(String head, String center , String foot, int canvasWidth, int canvasHeight){
        BufferedImage bi = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        //设置画笔
        //g2d.setPaint(Color.WHITE);
        //g2d.fillRect(0, 0, canvasWidth, canvasWidth);

        int circleRadius = Math.min(canvasWidth - 10, canvasHeight - 10) / 2;
        int mx = canvasWidth / 2 - circleRadius, my = canvasHeight / 2 - circleRadius;
        /***********draw circle*************/
        g2d.setPaint(Color.red);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));//设置画笔的粗度
        Shape circle = new Arc2D.Double(mx, my, circleRadius * 2 , circleRadius * 2, 0 , 360 , Arc2D.OPEN);
        g2d.draw(circle);
        /************************************/

        /***************draw string head**************/
        int fontSize = 40;
        Font f = new Font("黑体", Font.BOLD, fontSize);
        FontRenderContext context = g2d.getFontRenderContext();
        Rectangle2D bounds = f.getStringBounds(head,context);

        double msgWidth = bounds.getWidth();
        int countOfMsg = head.length();
        double interval = msgWidth/(countOfMsg-1);//计算间距


        double newRadius = circleRadius + bounds.getY() - 5;//bounds.getY()是负数，这样可以将弧形文字固定在圆内了。-5目的是离圆环稍远一点
        double radianPerInterval = 2 * Math.asin(interval / (2 * newRadius));//每个间距对应的角度

        //第一个元素的角度
        double firstAngle;
        if(countOfMsg % 2 == 1){//奇数
            firstAngle = (countOfMsg - 1) * radianPerInterval / 2.0 + Math.PI / 2 + 0.11;
        }else{//偶数
            firstAngle = (countOfMsg / 2.0 - 1) * radianPerInterval + radianPerInterval / 2.0 + Math.PI / 2 + 0.11;
        }

        for(int i = 0;i< countOfMsg;i++){
            double aa = firstAngle - i * radianPerInterval;
            double ax = newRadius * Math.sin(Math.PI/2 - aa);//小小的trick，将【0，pi】区间变换到[pi/2,-pi/2]区间
            double ay = newRadius * Math.cos(aa-Math.PI/2);//同上类似，这样处理就不必再考虑正负的问题了
            AffineTransform transform = AffineTransform .getRotateInstance(Math.PI/2 - aa);// ,x0 + ax, y0 + ay);
            Font f2 = f.deriveFont(transform);
            g2d.setFont(f2);
            g2d.drawString(head.substring(i,i+1), (float) (circleRadius+ax) + mx,  (float) (circleRadius - ay) + my);
        }

        if(center != null && center.length() > 0){
            fontSize = 100;
            f = new Font("楷体", Font.BOLD, fontSize);
            context = g2d.getFontRenderContext();
            bounds = f.getStringBounds(center, context);

            System.out.println("X:"+bounds.getX());
            System.out.println("Y:"+bounds.getY());
            System.out.println("height:"+bounds.getHeight());
            System.out.println("center y:"+bounds.getCenterY());


            /***************draw line*******************/
            //float halfHeight = circleRadius * (Math.cos(lineArc));
            //double halfWidth = circleRadius * (Math.sin(lineArc));

            //g2d.drawLine((int)(circleRadius-halfWidth),(int)(circleRadius-halfHeight),(int)(circleRadius+halfWidth),(int)(circleRadius-halfHeight));//
            //g2d.drawLine((int)(circleRadius-halfWidth),(int)(circleRadius+halfHeight),(int)(circleRadius+halfWidth),(int)(circleRadius+halfHeight));//


            /***********************END********************/

            /*****************draw string******************/
            g2d.setFont(f);
            g2d.drawString(center, (float) (circleRadius - bounds.getCenterX()) + mx, (float) (circleRadius - bounds.getCenterY()) + my);

            /********************END*********************/

        }


        /*****************draw foot*******************/
        if(foot != null && foot.length() > 0){

            fontSize = 30;
            f = new Font("黑体",Font.BOLD,fontSize);
            context = g2d.getFontRenderContext();
            bounds = f.getStringBounds(foot, context);
            g2d.setFont(f);
            double msgHeight = bounds.getHeight();
            msgWidth = bounds.getWidth();
            countOfMsg = foot.length();
            interval = msgWidth / (countOfMsg - 1);//计算间距


            newRadius = circleRadius + bounds.getY() - 5;//bounds.getY()是负数，这样可以将弧形文字固定在圆内了。-5目的是离圆环稍远一点
            radianPerInterval = 2 * Math.asin(interval / (2 * newRadius));//每个间距对应的角度

            //第一个元素的角度
            if(countOfMsg % 2 == 1){//奇数
                firstAngle = (countOfMsg - 1) * radianPerInterval / 2.0 + Math.PI / 2 + 0.11;
            }else{//偶数
                firstAngle = (countOfMsg / 2.0 - 1) * radianPerInterval + radianPerInterval / 2.0 + Math.PI / 2 + 0.11;
            }
            firstAngle = firstAngle;
            for(int i = 0;i< countOfMsg;i++){
                double aa = firstAngle + i * radianPerInterval;
                double ax = newRadius * Math.sin(Math.PI / 2 - aa);//小小的trick，将【0，pi】区间变换到[pi/2,-pi/2]区间
                double ay = newRadius * Math.cos(aa - Math.PI / 2);//同上类似，这样处理就不必再考虑正负的问题了
                AffineTransform transform = AffineTransform.getRotateInstance(Math.PI * 2 - aa);// ,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g2d.setFont(f2);
                g2d.drawString(foot.substring(i, i + 1), (float) (circleRadius + ax),  (float) ((circleRadius - ay) + msgHeight));
            }

        }

        g2d.dispose();//销毁资源
        return bi;
    }


    public static void main(String[] args) throws Exception {
        //String ssss = ImageUtils.encode(new FileInputStream("C:\\Users\\baymax\\Desktop\\图层0.png"));
        //System.out.print(ssss);
        //ImageUtils.decode(ssss);

        BufferedImage image = ImageUtils.createSeal("远成集团有限公司杭州分公司", "☆", "123456789", 400, 400);
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


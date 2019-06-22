package com.ycg.ksh.service.support.assist;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/29
 */
public class DrawSeal {

    private final static int WIDTH = 400;

    private final static int HEIGHT = 400;


    public static void main(String[] args) {
        //drawCircularChapter("测试测试", "上海远成储运有限公司", "123456789");

        BufferedImage image = DrawSeal.rectangle("学锋学锋",  "楷体");
        try
        {
            ImageIO.write(image, "PNG", new File("X:\\backup\\temp\\"+ System.nanoTime() +".png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static FileEntity saveImage(FileEntity fileEntity, BufferedImage bufferedImage) throws IOException {
        fileEntity.setSuffix("png");
        fileEntity.setFileName(FileUtils.appendSuffix(Globallys.next(), fileEntity.getSuffix()));
        ImageIO.write(bufferedImage, "PNG", new File(fileEntity.getPath()));
        return fileEntity;
    }

    private static BufferedImage createBufferedImage(int width, int height){
        return new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    }

    private static BufferedImage createBufferedImage(){
        return new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
    }
    private static Graphics2D createGraphics2D(BufferedImage bufferedImage){
        Graphics2D gs = bufferedImage.createGraphics();
        //bufferedImage = gs.getDeviceConfiguration().createCompatibleImage(bufferedImage.getWidth(), bufferedImage.getHeight(), Transparency.TRANSLUCENT);
        //gs.dispose();
        //gs = bufferedImage.createGraphics();
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        return gs;
    }

    public static BufferedImage rectangle(String name, String fontName){
        int w = WIDTH, h = (int) (WIDTH * 0.5), p = 10, m = 20;
        //创建缓存
        BufferedImage buffered = createBufferedImage(w + 2*p, h + 2*p);
        //获得画布
        Graphics2D gs = createGraphics2D(buffered);
        gs.setColor(Color.RED);
        //设置画笔宽度
        gs.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        gs.draw(new Rectangle2D.Double(p , p, w, h));
        String[] chars = name.split("");
        if(chars.length == 2){
            gs.setFont(new Font(fontName, Font.BOLD, 120));
        }
        if(chars.length == 3){
            gs.setFont(new Font(fontName, Font.BOLD, 100));
        }
        if(chars.length == 4){
            gs.setFont(new Font(fontName, Font.BOLD, 80));
        }

        Font font = gs.getFont();
        Rectangle2D bounds = font.getStringBounds(name, gs.getFontRenderContext());
        // 字符宽度＝字符串长度/字符数
        double char_interval = (bounds.getWidth() / chars.length) - 1;

        double y = (h + bounds.getHeight()) / 2 - p;
        System.out.println();
        double x = p + (w - bounds.getWidth()) / 2;
        for (int i = 0; i < chars.length; i++) {
            gs.drawString(chars[i], (float) x , (float) y);
            x = x + char_interval;
        }
        gs.dispose();
        return buffered;
    }

    /**
     * 画圆形章
     * @param name1 章的名字
     * @param name2 公司的名字
     * @param numCode 公司的数字编码
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage drawCircularChapter(String upper, String center, String numCode){

        //创建缓存
        BufferedImage bufImg = createBufferedImage();

        //获得画布
        Graphics2D gs = createGraphics2D(bufImg);

        //设置颜色
        //gs.setColor(Color.WHITE);
        //填充区域
        //gs.fillRect(0, 0, WIDTH, HEIGHT);
        //设置画笔颜色
        gs.setColor(Color.RED);
        //画五角星
        drawFiveStar(gs, WIDTH/2, HEIGHT/2, HEIGHT/6 - 3);
        //设置画笔宽度
        gs.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        //画圆形边框
        gs.drawOval(2, 2, WIDTH-4, HEIGHT-4);
        //设置字体
        gs.setFont(new Font("宋体", Font.BOLD, 40));
        //画数据(合同专用章)
        if(StringUtils.isNotBlank(center)){
            drawCenterMessage(gs, center, WIDTH/2 + 2, HEIGHT * 4 / 5);
        }
        if (StringUtils.isNotEmpty(upper) && upper.length() > 11) {
            int len = upper.length();
            int fontSize = 18;
            switch (len) {
                case 12:
                    fontSize = 20;
                    break;
                case 13:
                    fontSize = 19;
                    break;
                case 14:
                    fontSize = 19;
                    break;
                case 15:
                    fontSize = 18;
                    break;
                case 16:
                    fontSize = 17;
                    break;
                case 17:
                    fontSize = 16;
                    break;
                case 18:
                    fontSize = 15;
                    break;
                case 19:
                    fontSize = 14;
                    break;
                case 20:
                    fontSize = 14;
                    break;
                case 21:
                    fontSize = 12;
                    break;
                case 22:
                    fontSize = 12;
                    break;
                case 23:
                    fontSize = 10;
                    break;
                case 24:
                    fontSize = 10;
                    break;
                case 25:
                    fontSize = 9;
                    break;
                default :
                    fontSize = 18;
                    break;
            }
            //设置字体
            gs.setFont(new Font("宋体", Font.PLAIN, 40));
        }
        //画上方的环形字
        drawUpperMessage(gs, upper, WIDTH/2, HEIGHT/2, WIDTH/2-10);
        //设置字体
        gs.setFont(new Font("宋体",Font.PLAIN, 30));
        //画下方的环形数字

        if(StringUtils.isNotBlank(numCode)){
            drawBelowNumber(gs, numCode, WIDTH/2, HEIGHT/2, HEIGHT/2);
        }
        //释放此图形的上下文以及它使用的所有系统资源
        gs.dispose();
        return bufImg;
    }

    public static void drawCenterMessage(Graphics2D gs, String message, int CENTERX, int CENTERY){
        if (message != null) {
            // 根据输入字符串得到字符数组
            String[] messages2 = message.split("", 0);
            String[] messages = new String[messages2.length];
            System.arraycopy(messages2, 0, messages, 0, messages2.length);
            // 输入的字数
            int ilength = messages.length;
            Font f = gs.getFont();
            FontRenderContext context = gs.getFontRenderContext();
            Rectangle2D bounds = f.getStringBounds(message, context);
            // 字符宽度＝字符串长度/字符数
            double char_interval = (bounds.getWidth() / ilength) - 1;

            int first = 0, second = 0;
            boolean odd = false;
            if (ilength % 2 == 1) {
                first = (ilength - 1) / 2;
                odd = true;
            } else {
                first = (ilength) / 2 - 1;
                second = (ilength) / 2;
                odd = false;
            }
            if (odd) {
                gs.drawString(messages[first],
                        (float)(CENTERX - char_interval / 2 - char_interval / 10),
                        (float) CENTERY);
                // 中心点的右边
                for (int i = first + 1; i < ilength; i++) {
                    double x = CENTERX + (i - first - 0.6) * char_interval;
                    gs.drawString(messages[i],(float) x,
                            (float) CENTERY);
                }
                // 中心点的左边
                for (int i = first - 1; i > -1; i--) {
                    double x = CENTERX - (first - i + 0.6) * char_interval;
                    gs.drawString(messages[i],(float) x,
                            (float) CENTERY);
                }
            } else {
                // 中心点的右边
                for (int i = second; i < ilength; i++) {
                    double x = CENTERX + (i - second - 0.1) * char_interval;
                    gs.drawString(messages[i],(float) x,
                            (float) CENTERY);
                }
                // 中心点的左边
                for (int i = first; i > -1; i--) {
                    double x = CENTERX - (first + 1 - i + 0.1) * char_interval;
                    gs.drawString(messages[i],(float) x,
                            (float) CENTERY);
                }

            }
        }
    }

    /**
     * 画上方圆弧的汉字
     *
     * @param gs
     * @param message
     * @param CENTERX
     * @param CENTERY
     * @param r
     */
    public static void drawUpperMessage(Graphics2D gs, String message, int CENTERX, int CENTERY, int r) {
        if (message != null) {
            // 根据输入字符串得到字符数组
            String[] messages2 = message.split("", 0);
            String[] messages = new String[messages2.length];
            System.arraycopy(messages2, 0, messages, 0, messages2.length);
            // 输入的字数
            int ilength = messages.length;
            Font f = gs.getFont();
            FontRenderContext context = gs.getFontRenderContext();
            Rectangle2D bounds = f.getStringBounds(message, context);
            // 字符宽度＝字符串长度/字符数
            double char_interval = (bounds.getWidth() / ilength) - 1;
            // 上坡度
            double ascent = -bounds.getY()-2;
            int first = 0, second = 0;
            boolean odd = false;
            if (ilength % 2 == 1) {
                first = (ilength - 1) / 2;
                odd = true;
            } else {
                first = (ilength) / 2 - 1;
                second = (ilength) / 2;
                odd = false;
            }
            double r2 = r - ascent;
            double x0 = CENTERX;
            double y0 = CENTERY - r + ascent;
            // 旋转角度
            double a = 2 * Math.asin(char_interval / (2 * r2));

            if (odd) {
                gs.drawString(messages[first],(float)(x0 - char_interval / 2), (float) y0);
                // 中心点的右边
                for (int i = first + 1; i < ilength; i++) {
                    double aa = (i - first) * a;
                    double ax = r2 * Math.sin(aa);
                    double ay = r2 - r2 * Math.cos(aa);
                    AffineTransform transform = AffineTransform.getRotateInstance(aa);
                    Font f2 = f.deriveFont(transform);
                    gs.setFont(f2);
                    gs.drawString(messages[i],(float) (x0 + ax - char_interval / 2 * Math.cos(aa)),
                            (float) (y0 + ay - char_interval / 2 * Math.sin(aa)));
                }
                // 中心点的左边
                for (int i = first - 1; i > -1; i--) {
                    double aa = (first - i) * a;
                    double ax = r2 * Math.sin(aa);
                    double ay = r2 - r2 * Math.cos(aa);
                    AffineTransform transform = AffineTransform.getRotateInstance(-aa);
                    Font f2 = f.deriveFont(transform);
                    gs.setFont(f2);
                    gs.drawString(messages[i],
                            (float) (x0 - ax - char_interval / 2 * Math.cos(aa)),
                            (float) (y0 + ay + char_interval / 2 * Math.sin(aa)));
                }
            } else {
                // 中心点的右边
                for (int i = second; i < ilength; i++) {
                    double aa = (i - second + 0.3) * a;
                    double ax = r2 * Math.sin(aa);
                    double ay = r2 - r2 * Math.cos(aa);
                    AffineTransform transform = AffineTransform.getRotateInstance(aa);
                    Font f2 = f.deriveFont(transform);
                    gs.setFont(f2);
                    gs.drawString(messages[i],
                            (float) (x0 + ax - char_interval / 2 * Math.cos(aa)),
                            (float) (y0 + ay - char_interval / 2 * Math.sin(aa)));
                }
                // 中心点的左边
                for (int i = first; i > -1; i--) {
                    double aa = (first - i + 0.7) * a;
                    double ax = r2 * Math.sin(aa);
                    double ay = r2 - r2 * Math.cos(aa);
                    AffineTransform transform = AffineTransform.getRotateInstance(-aa);
                    Font f2 = f.deriveFont(transform);
                    gs.setFont(f2);
                    gs.drawString(messages[i],
                            (float) (x0 - ax - char_interval / 2 * Math.cos(aa)),
                            (float) (y0 + ay + char_interval / 2* Math.sin(aa)));
                }
            }
        }
    }

    /**
     * 画下方圆弧数字编码
     *
     * @param gs
     * @param message
     * @param CENTERX
     * @param CENTERY
     * @param r
     */
    public static void drawBelowNumber(Graphics2D gs, String message, int CENTERX, int CENTERY, int r) {
        if (message != null) {
            // 根据输入字符串得到字符数组
            String[] messages2 = message.split("", 0);
            String[] messages = new String[messages2.length - 1];
            System.arraycopy(messages2, 1, messages, 0, messages2.length - 1);
            // 输入的字数
            int ilength = messages.length;
            Font f = gs.getFont();
            FontRenderContext context = gs.getFontRenderContext();
            Rectangle2D bounds = f.getStringBounds(message, context);
            // 字符宽度＝字符串长度/字符数
            double char_interval = (bounds.getWidth() / ilength) + 4;
            // 上坡度
            double ascent = 8;
            int first = 0, second = 0;
            boolean odd = false;
            if (ilength % 2 == 1) {
                first = (ilength - 1) / 2;
                odd = true;
            } else {
                first = (ilength) / 2 - 1;
                second = (ilength) / 2;
                odd = false;
            }
            double r2 = r - ascent;
            double x0 = CENTERX;
            double y0 = CENTERY - r + ascent;
            // 旋转角度
            double a = 2 * Math.asin(char_interval / (2 * r2));
            if (odd) {
                // 中心点的右边
                for (int i = first + 1; i < ilength; i++) {
                    double aa = (i - first) * a;
                    double ax = r2 * Math.sin(aa);
                    double ay = r2 - r2 * Math.cos(aa);
                    AffineTransform transform = AffineTransform.getRotateInstance(2*Math.PI - aa);
                    Font f2 = f.deriveFont(transform);
                    gs.setFont(f2);
                    gs.drawString(messages[i],
                            (float) (x0 + ax),
                            2*CENTERY - (float) (y0 + ay));
                }
                // 中心点的左边
                for (int i = first; i > -1; i--) {
                    double aa = (first - i) * a;
                    double ax = r2 * Math.sin(aa);
                    double ay = r2 - r2 * Math.cos(aa);
                    AffineTransform transform = AffineTransform.getRotateInstance(2*Math.PI + aa);
                    Font f2 = f.deriveFont(transform);
                    gs.setFont(f2);
                    gs.drawString(messages[i],
                            (float) (x0 - ax),
                            2*CENTERY - (float) (y0 + ay));
                }
            } else {
                // 中心点的右边
                for (int i = second; i < ilength; i++) {
                    double aa = (i - second) * a;
                    double ax = r2 * Math.sin(aa);
                    double ay = r2 - r2 * Math.cos(aa);
                    AffineTransform transform = AffineTransform.getRotateInstance(2*Math.PI - aa);
                    Font f2 = f.deriveFont(transform);
                    gs.setFont(f2);
                    gs.drawString(messages[i],
                            (float) (x0 + ax),
                            2*CENTERY - (float) (y0 + ay));
                }
                // 中心点的左边
                for (int i = first; i > -1; i--) {
                    double aa = (first - i) * a;
                    double ax = r2 * Math.sin(aa);
                    double ay = r2 - r2 * Math.cos(aa);
                    AffineTransform transform = AffineTransform.getRotateInstance(2*Math.PI + aa);
                    Font f2 = f.deriveFont(transform);
                    gs.setFont(f2);
                    gs.drawString(messages[i],
                            (float) (x0 - ax),
                            2*CENTERY - (float) (y0 + ay));
                }
            }
        }
    }

    /**
     * 画章中间的五角星
     *
     * @param g
     * @param x
     * @param y
     * @param r
     */
    public static void drawFiveStar(Graphics g, int x, int y, int r) {
        double ch=72*Math.PI/180;
        int x1=x,
                x2=(int)(x-Math.sin(ch)*r),
                x3=(int)(x+Math.sin(ch)*r),
                x4=(int)(x-Math.sin(ch/2)*r),
                x5=(int)(x+Math.sin(ch/2)*r);
        int y1=y-r,
                y2=(int)(y-Math.cos(ch)*r),
                y3=y2,
                y4=(int)(y+Math.cos(ch/2)*r),
                y5=y4;
        int bx=(int)(x+Math.cos(ch)*Math.tan(ch/2)*r);
        int by=y2;

        Polygon a=new Polygon();
        Polygon b=new Polygon();
        a.addPoint(x2,y2);
        a.addPoint(x5,y5);
        a.addPoint(bx,by);
        b.addPoint(x1,y1);
        b.addPoint(bx,by);
        b.addPoint(x3,y3);
        b.addPoint(x4,y4);

        g.fillPolygon(a);
        g.fillPolygon(b);
    }
}

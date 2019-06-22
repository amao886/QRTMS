package com.ycg.ksh.service.support.assist;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.extend.QRCode;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/29
 */
public class AssociateCustomerQrCode {

    private final static int WIDTH = 400;

    private final static int HEIGHT = 500;


    public static void main(String[] args) throws Exception {
        //drawCircularChapter("测试测试", "上海远成储运有限公司", "123456789");
        byte[] data = new QRCode(400, "png").bytes("上海远成储运有限公司上海远成储运有限公司");
        BufferedImage image = AssociateCustomerQrCode.rectangle("成都亿佳兴业商贸有限公司",  "2018/07/23 12:30过期", data);
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

    public static BufferedImage rectangle(String name, String eeeeee, byte[] bytes) throws IOException {
        int w = WIDTH, h = HEIGHT, p = 10, m = 20;
        //创建缓存
        BufferedImage buffered = new BufferedImage(w + 2*p, h + 2*p, BufferedImage.TYPE_INT_RGB);
        //获得画布
        Graphics2D gs = createGraphics2D(buffered);
        gs.setBackground(Color.WHITE);//设置背景色
        //g2.setBackground(Color.WHITE);//设置背景色
        gs.clearRect(0, 0, w + 2*p, h + 2*p);//通过使用当前绘图表面的背景色进行填充来清除指定的矩形。

        gs.setColor(Color.BLACK);

        //设置画笔宽度
        gs.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        gs.setFont(new Font("楷体", Font.BOLD, 20));
        Font font = gs.getFont();
        Rectangle2D bounds = font.getStringBounds(name, gs.getFontRenderContext());
        String[] chars = name.split("");
        // 字符宽度＝字符串长度/字符数
        double char_interval = (bounds.getWidth() / chars.length) - 1;
        double y = 20 + p + bounds.getHeight() / 2;
        double x = p + (w - bounds.getWidth()) / 2;

        gs.drawString(name, (float) x , (float) y);

        gs.setFont(new Font("楷体", Font.PLAIN, 15));
        font = gs.getFont();
        bounds = font.getStringBounds(eeeeee, gs.getFontRenderContext());
        chars = eeeeee.split("");
        // 字符宽度＝字符串长度/字符数
        char_interval = (bounds.getWidth() / chars.length) - 1;
        y = y + 20 + bounds.getHeight() / 2;
        x = p + (w - bounds.getWidth()) / 2;

        gs.drawString(eeeeee, (float) x , (float) y);


        y = y + 20;
        x = p;

        //Image image = Toolkit.getDefaultToolkit().createImage(bytes, 0, bytes.length);
        BufferedImage bi = ImageIO.read(new ByteArrayInputStream(bytes));

        gs.drawImage(bi, (int)x, (int)y, null);

        gs.dispose();
        return buffered;
    }
}

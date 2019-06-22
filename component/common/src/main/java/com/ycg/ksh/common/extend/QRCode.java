/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-11 11:14:44
 */
package com.ycg.ksh.common.extend;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;

/**
 * 二维码
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-11 11:14:44
 */
public class QRCode {

	private static final int BLACK = 0xFF000000;//黑色
    private static final int WHITE = 0xFFFFFFFF;//白色
    
	private int size;//大小
	private int logoScale = 20;//logo缩放比例
	private String format = "png";
	
	private byte[] logoBytes;
	
    private QRCodeWriter codeWriter;
    
    private Hashtable<EncodeHintType, Object> encodeHintTypes;

	public QRCode(int size, String format, File logoFile) {
		this(size, format);
		modifyLogoFile(logoFile);
	}

	public QRCode(int size, String format) {
		super();
		this.size = size;
		this.format = format;
		this.codeWriter = new QRCodeWriter();
		this.encodeHintTypes = new Hashtable<EncodeHintType, Object>(1);
		setEncodeHintType(EncodeHintType.CHARACTER_SET, "utf-8");
		setEncodeHintType(EncodeHintType.MARGIN, 1);
		setEncodeHintType(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
	}

	public void setEncodeHintType(EncodeHintType encodeHintType, Object value) {
		encodeHintTypes.put(encodeHintType, value);
	}
	
	private BitMatrix bitMatrix(String contents) throws WriterException {
		return codeWriter.encode(contents, BarcodeFormat.QR_CODE, size, size, encodeHintTypes);
	}

	private BufferedImage bufferedImage(String contents, boolean logo) throws Exception {    
		BitMatrix bitMatrix = bitMatrix(contents);
		int width = bitMatrix.getWidth();    
        int height = bitMatrix.getHeight();    
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);    
        for (int x = 0; x < width; x++) {    
            for (int y = 0; y < height; y++) {    
            	bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? BLACK : WHITE);    
            }    
        } 
        if(logo && logoBytes != null && logoBytes.length > 0) {
        	 Image src = ImageIO.read(new ByteArrayInputStream(logoBytes));
             int w = src.getWidth(null), _w = Math.round(width * logoScale / 100);
             int h = src.getHeight(null), _h = Math.round(height * logoScale / 100);
             if (_w <= w || _h <= h) { // 压缩LOGO
            	 src = src.getScaledInstance(_w, _h, Image.SCALE_SMOOTH);
            	 w = _w;
            	 h = _h;
             }
             // 插入LOGO
             Graphics2D graph = bufferedImage.createGraphics();
             int x = (width - w) / 2;
             int y = (height - h) / 2;
             graph.drawImage(src, x, y, w, h, null);
            
             graph.drawRoundRect(x, y, w, h, 15 ,15);  
             //logo边框大小  
             graph.setStroke(new BasicStroke(1));  
             //logo边框颜色  
             graph.setColor(Color.BLACK);  
             graph.drawRect(x, y, w, h); 
             graph.dispose();
             src.flush();  
             bufferedImage.flush();  
        }
        return bufferedImage;    
    } 
	
	/**
	 * 生成二维码然后转成字节数组
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-11 13:49:53
	 * @param contents
	 * @return
	 * @throws Exception
	 */
	public byte[] bytes(String contents) throws Exception {    
		ByteArrayOutputStream baout = null;
		byte[] bytes = null;
		try {
			baout = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage(contents, true), format, baout);
			bytes = baout.toByteArray();
		} finally {
			if(baout != null) {
				try {
					baout.flush();
					baout.close();
				} catch (Exception e) { }
			}
		}
		return bytes;
    }   
	
    /**
     * 生成二维码然后输出到文件
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-11 13:50:14
     * @param contents
     * @param output
     * @throws Exception
     */
    public void write(String contents, File output) throws Exception {    
        if(!ImageIO.write(bufferedImage(contents, true), format, output)) {
        	 throw new IOException("Could not write an image of format " + format + "to "+ output);   
        }
    }    
    
	/**
	 * 生成二维码然后输出到流
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-11 13:50:32
	 * @param contents
	 * @param stream
	 * @throws Exception
	 */
	public void write(String contents, OutputStream stream) throws Exception {
		if (!ImageIO.write(bufferedImage(contents, true), format, stream)) {
			throw new IOException("Could not write an image of format " + format);
		}
	}
	
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}

	public void modifyLogoFile(File logoFile) {
		if (logoFile != null && logoFile.exists() && logoFile.isFile()) {
			InputStream ins = null;
			try {
				ins = new FileInputStream(logoFile);
				byte[] buffer = new byte[(int) logoFile.length()];
				int offset = 0, numRead = 0;
				while ((numRead = ins.read(buffer, offset, buffer.length - offset)) > 0) {
					offset += numRead;
				}
				logoBytes = buffer;
			} catch (Exception e) {
				try {
					if (ins != null) {
						ins.close();
					}
				} catch (IOException ee) { }
			}
		} else {
			this.logoBytes = null;
		}
	}
	
	public void modifyLogoBytes(byte[] bytes) {
		logoBytes = bytes;
	}
}

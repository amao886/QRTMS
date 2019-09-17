/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-11 16:58:13
 */
package com.ycg.ksh.common.barcode;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.ycg.ksh.common.extend.QRCode;
import com.ycg.ksh.common.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 二维码生成PDF文件
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-11 16:58:13
 */
public class PDFBuilder {

	private static final String QRCODE_FORMAT = "png";
	private static final int QRCODE_SIZE = 280;
	private static final int PDF_WIDTH = 600;
	private static final int PDF_HEIGHT = 400;
	
	private static final String NAME = "合同物流管理平台";
	
	private Document document;
	private PdfWriter writer;
	private File file;
	private Font font;
	
	private QRCode qrCode;
	
	public PDFBuilder(File file) {
		super();
		this.file = file;
	}
	
	public void ready() throws Exception {
		document = new Document(new Rectangle(PDF_WIDTH, PDF_HEIGHT), 0, 0, 10, 10);
        writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        font = new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED), 24, Font.NORMAL);
        //打开文件
        document.open();
        document.addTitle("合同物流管理平台二维码");//标题
        document.addAuthor("http://www.lehan-tech.com");//作者
        document.addSubject("合同物流管理平台二维码");//主题
        document.addCreationDate();//创建时间
        document.addCreator("www.lehan-tech.com"); //应用程序
        
        qrCode = new QRCode(QRCODE_SIZE, QRCODE_FORMAT);
	}
	
	public void insert(String barcode,String deliveryNumber, String qrcodeContext) throws Exception {
	    Paragraph bp = new Paragraph(barcode, font);
	    bp.setAlignment(Element.ALIGN_CENTER);
        document.add(bp);
        Image image = Image.getInstance(qrCode.bytes(qrcodeContext));
        image.setAlignment(Element.ALIGN_CENTER);
        document.add(image);
        Paragraph text = null;
        if(StringUtils.isEmpty(deliveryNumber))
            text = new Paragraph(NAME, font);
        else
            text = new Paragraph(deliveryNumber, font);
        text.setAlignment(Element.ALIGN_CENTER);
        document.add(text);
	}
	
	public void close() {
        if(writer != null) {
        	writer.flush();
            if(!writer.isCloseStream()) {
            	writer.close();
            }
        }
        if(document != null) {
        	document.close();
        }
	}
	
	public static void main(String[] args) throws Exception {
		PDFBuilder builder = new PDFBuilder(new File("C:\\Users\\baymax\\Desktop\\test2.pdf"));
		builder.ready();
		for (int i = 0; i < 1000; i++) {
			builder.insert("barcode_"+i, "" ,"http://www.ycgwl.com");
		}
		builder.close();
	}
}

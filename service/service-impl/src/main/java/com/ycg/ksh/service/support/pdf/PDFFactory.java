package com.ycg.ksh.service.support.pdf;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/16
 */

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.system.SystemKey;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.encrypt.MD5;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * PDF创建类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/16
 */
public class PDFFactory {

    protected final Logger logger = LoggerFactory.getLogger(PDFFactory.class);

    private static final String AUTHOR = "ksh.ycgwl.com";
    private static final String CREATOR = "ksh.ycgwl.com";
    private static final String SUFFIX = "pdf";

    private static final String PRE_TEMPLATE_KEY = "TEMPLATE_KEY#";
    private static final String ENCODING = "UTF-8";


    private static final FontProvider provider = new XMLWorkerFontProvider(){
        public Font getFont(final String fontName, String encoding, float size, final int style) {
            if (size == 0) {
                size = 4;
            }
            return super.getFont(Optional.ofNullable(fontName).orElse("微软雅黑"), encoding, size, style);
        }
    };


    public static PDFFactory getInstance(){
        return new PDFFactory();
    }

    public String loadByTemplate(Serializable uniqueName, String templateString, Object data) throws Exception {
        Configuration cfg = new Configuration(new Version("2.3.28"));
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate(PRE_TEMPLATE_KEY + uniqueName, templateString);
        cfg.setTemplateLoader(stringLoader);

        Template template = cfg.getTemplate(PRE_TEMPLATE_KEY + uniqueName, ENCODING);
        StringWriter writer = new StringWriter();
        template.process(data, writer);
        return writer.toString();
    }

    public FileEntity createByTemplate(String fileName, Collection<? extends Object> objects, String htmlTemplate, String cssContent) throws Exception {
        return createByTemplate(fileName, pageHtml(MD5.encrypt(fileName), objects, htmlTemplate), cssContent);
    }


    public FileEntity createByTemplate(String fileName, String content, String cssContent) throws Exception {
        FileEntity entity = new FileEntity(SystemUtils.fileRootPath(), Directory.ERECEIPT, fileName, SUFFIX);
        Document document = null;
        try{
            File file = FileUtils.file(entity.getPath());
            document = new Document(PageSize.A4, 20, 20, 50, 50);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            document.newPage();
            document.addAuthor(AUTHOR);
            document.addCreator(CREATOR);
            document.addCreationDate();
            document.addTitle("电子回单");
            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            InputStream htmlStream = new ByteArrayInputStream(content.getBytes());
            InputStream cssStream = new ByteArrayInputStream(cssContent.getBytes());
            worker.parseXHtml(pdfWriter, document, htmlStream, cssStream, Charset.forName("UTF-8"), provider);
        }finally {
            if(document != null){
                document.close();
            }
        }
        return entity;
    }

    public void signature(final File file, Collection<SealObject> sealObjects) throws BusinessException {
        PdfReader pdfReader = null;
        PdfStamper stamper = null;
        File tempFile = null;
        try {
            PdfSealPosition sealPosition = getSealPosition(file, new PdfSealPosition(sealObjects));
            if(sealPosition != null && !sealPosition.isEmpty()){
                tempFile = FileUtils.file(file.getParent(), FileUtils.appendSuffix(String.valueOf(System.nanoTime()), "pdf"));
                FileUtils.copyFile(file, tempFile);
                pdfReader = new PdfReader(new FileInputStream(tempFile));
                stamper = new PdfStamper(pdfReader, new FileOutputStream(file));
                if(sealPosition.getCoordinates() != null){
                    for (Map.Entry<SealObject, Collection<PdfCoordinate>> entry : sealPosition.getCoordinates().entrySet()) {
                        Image image = Image.getInstance(entry.getKey().getImage());
                        float h = image.getHeight() / (image.getWidth() / 100);
                        image.scaleAbsolute(100, h);
                        for (PdfCoordinate coordinate : entry.getValue()) {
                            PdfContentByte pdfContent = stamper.getOverContent(coordinate.page);
                            image.setAbsolutePosition(coordinate.x + coordinate.w, coordinate.y - (h / 2));
                            pdfContent.addImage(image);
                        }
                    }
                }
                if(sealPosition.getPageSeals() != null){
                    int i = 0;
                    for (SealObject sealObject : sealPosition.getSealObjects()) {
                        Image image = Image.getInstance(sealObject.getImage());
                        float h = image.getHeight() / (image.getWidth() / 100);
                        image.scaleAbsolute(100, h);
                        for (PdfCoordinate coordinate : sealPosition.getPageSeals()) {
                            float x = coordinate.x + (i++) * (coordinate.w - 140F) + 20F;
                            PdfContentByte pdfContent = stamper.getOverContent(coordinate.page);
                            image.setAbsolutePosition(x, coordinate.h - coordinate.y + 30F);
                            pdfContent.addImage(image);
                        }
                    }
                }
                stamper.close();
                pdfReader.close();
            }
        } catch (Exception e) {
            logger.error("PDF文件签署异常 -> {}", file, e);
            throw new BusinessException("PDF文件签署异常");
        }finally {
            if(stamper != null){
                try{
                    stamper.close();
                }catch (Exception e){e.printStackTrace();}
            }
            if(pdfReader != null){ pdfReader.close(); }
            if(tempFile != null){
                try{
                    FileUtils.forceDelete(tempFile);
                }catch (Exception e){e.printStackTrace();}
            }
        }
    }

    public PdfSealPosition getSealPosition(final File file, PdfSealPosition sealPosition) throws BusinessException {
        PdfReader pdfReader = null;
        try {
            pdfReader = new PdfReader(new FileInputStream(file));
            if(pdfReader.getNumberOfPages() > 0){
                PdfReaderContentParser contentParser = new PdfReaderContentParser(pdfReader);
                for (int page = 1; page <= pdfReader.getNumberOfPages(); page++) {
                    Rectangle rectangle = pdfReader.getPageSize(page);
                    PdfRenderListener listener = contentParser.processContent(page, new PdfRenderListener(page, sealPosition));
                    if(listener.getSize() <= 0){
                        sealPosition.addPageSeal(new PdfCoordinate(rectangle.getLeft(), rectangle.getHeight() - rectangle.getBottom(), rectangle.getWidth(), rectangle.getHeight(), page));
                    }
                }
            }
            return sealPosition;
        } catch (Exception e) {
            logger.error("PDF文件关键字定位异常 -> {}", file, e);
            throw new BusinessException("PDF文件关键字定位异常");
        }finally {
            if(pdfReader != null){
                pdfReader.close();
            }
        }
    }

    private String pageHtml(Serializable uniqueName, Collection<? extends Object> objects, String htmlTemplate) throws Exception {
        StringBuilder builder = new StringBuilder();
        int index = 1;
        for (Object object : objects) {
            builder.append("<div style='page-break-after:always'>");
            builder.append(loadByTemplate(uniqueName +"#"+ (index++), htmlTemplate, object));
            builder.append("</div>");
        }
        return builder.toString();
    }


    public static void main(String[] args) throws Exception {
        SystemUtils.put(SystemKey.SYSTEM_FILE_ROOT, "D:/workspace/backup/temp");

        PDFFactory factory = PDFFactory.getInstance();

        String template = "<div class=\"pdf-div\">\n" +
                "<div class=\"title-h4\">产品配送单</div>\n" +
                "<table class=\"table-left\">\n" +
                "    <tbody>\n" +
                "        <tr>\n" +
                "            <td width=\"110\">配送单号:</td>\n" +
                "            <td width=\"390\">${deliveryNo}</td>\n" +
                "            <td width=\"110\">日期:</td>\n" +
                "            <td width=\"110\">${deliveryTime?string(\"yyyy-MM-dd\")}</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>订单客户:</td>\n" +
                "            <td><#if receive??>${receive.companyName}</#if></td>\n" +
                "            <td>订单编号:</td>\n" +
                "            <td>${orderNo}</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>收货客户:</td>\n" +
                "            <td><#if receive??>${receive.companyName}</#if></td>\n" +
                "            <td>收货人员:</td>\n" +
                "            <td>${receiverName}</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>物流商名称:</td>\n" +
                "            <td><#if convey??>${convey.companyName}</#if></td>\n" +
                "            <td>联系方式:</td>\n" +
                "            <td>${receiverContact}</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>收货地址:</td>\n" +
                "            <td colspan=\"3\">${receiveAddress}</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>备注:</td>\n" +
                "            <td colspan=\"3\">${remark}</td>\n" +
                "        </tr>\n" +
                "    </tbody>\n" +
                "</table>\n" +
                "<div>订单明细：</div>\n" +
                "    <table class=\"table-border\">\n" +
                "        <thead>\n" +
                "            <tr>\n" +
                "                <th width=\"100\">物料编号</th>\n" +
                "                <th width=\"100\">物料描述</th>\n" +
                "                <th width=\"80\">产品型号</th>\n" +
                "                <th width=\"80\">单位</th>\n" +
                "                <th width=\"80\">数量</th>\n" +
                "                <th width=\"80\">箱数</th>\n" +
                "                <th width=\"80\">体积</th>\n" +
                "                <th width=\"80\">重量</th>\n" +
                "            </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n" +
                "        <#list commodities as commodity>\n" +
                "        <tr>\n" +
                "                <td><#if commodity.commodityNo??>${commodity.commodityNo}</#if></td>\n" +
                "                <td><#if commodity.commodityName??>${commodity.commodityName}</#if></td>\n" +
                "                <td><#if commodity.commodityType??>${commodity.commodityType}</#if></td>\n" +
                "                <td><#if commodity.commodityUnit??>${commodity.commodityUnit}</#if></td>\n" +
                "                <td>${commodity.quantity}</td>\n" +
                "                <td>${commodity.boxCount}</td>\n" +
                "                <td>${commodity.volume}</td>\n" +
                "                <td>${commodity.weight}</td>\n" +
                "            </tr>\n" +
                "        </#list>\n" +
                "        </tbody>\n" +
                "        <tfoot>\n" +
                "            <tr>\n" +
                "                <td colspan=\"4\">合计</td>\n" +
                "                <td>${quantity}</td>\n" +
                "                <td>${boxCount}</td>\n" +
                "                <td>${volume}</td>\n" +
                "                <td>${weight}</td>\n" +
                "            </tr>\n" +
                "        </tfoot>\n" +
                "    </table>\n" +
                "<table>\n" +
                "    <tbody>\n" +
                "        <tr>\n" +
                "            <td><#if exception??>异常:<span>${exception.content}</span></#if></td>\n" +
                "        </tr>\n" +
                "    </tbody>\n" +
                "</table>\n" +
                "<table class=\"table-border table-left\">\n" +
                "    <tbody>\n" +
                "    <tr>\n" +
                "        <td width=\"360\">物流商签字</td>\n" +
                "        <td width=\"360\">物流商盖章</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td colspan=\"2\">\n" +
                "            <p>温馨提示：物流配送责任以签收单据为主要依据，故请您严格按照收货标准验收签字、盖章确认。如有异常情况，请及时致电九阳物流</p>\n" +
                "            <p>\n" +
                "                <span>杭州RDC:0571-12345678</span>\n" +
                "                <span>济南RDC:0531-12345678</span>\n" +
                "                <span>广州RDC:0757-12345678</span>\n" +
                "            </p>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>实收件数(大写)</td>\n" +
                "        <td>${realNumber}</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td colspan=\"2\">\n" +
                "            <p>我已确认此配送单信息与实际接收货物数量、型号、配送标识(防窜货码)一致</p>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>收货人签字</td>\n" +
                "        <td>收货单位业务章</td>\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "</table>\n" +
                "</div>";
        String cssString =".pdf-div{background:#ffffff; width:740px;}" +
                ".title-h4{text-align:center; font-size:20px;font-weight:bold;}\n" +
                "table{width:100%;margin-top:10px;border-collapse: collapse; border-spacing: 0;}\n" +
                "table tr td{text-align:center; font-weight:normal; height:30px; padding:2px;}\n" +
                "table tr th{text-align:center; font-weight:normal; height:30px; padding:2px;}\n" +
                ".table-border{border-left:#000000 solid 1px;border-top:#000000 solid 1px;}\n" +
                ".table-border tr th{border-right:#000000 solid 1px;border-bottom:#000000 solid 1px; background-color: #CCCCCC; }\n" +
                ".table-border tr td{border-right:#000000 solid 1px;border-bottom:#000000 solid 1px; }\n" +
                ".table-left tr td{text-align:left;  }\n" +
                ".tips{ font-size: 10px; color: ##4F4F4F; } \n" +
                ".title{padding-top: 10px;} ";
        //System.out.println(Base64.encodeBase64String(template.getBytes()));
        //System.out.println(Base64.encodeBase64String(cssString.getBytes()));


        //FileEntity file = factory.createByTemplate("1234567890", factory.loadByTemplate("123", template, data), cssString);
        //System.out.println(file);

        //factory.signature(new File("D:/workspace/backup/temp/pdf/receipt/1234567890.pdf"), "物流商签字盖章", "OrderConcise:\\Users\\baymax\\Desktop\\图层0.png" );
        //byte[] bytes = ImageUtils.decodeBytes("");
        //Collection<SealObject> sealObjects = new ArrayList<SealObject>();
        //sealObjects.add(new SealObject("收货单位业务章", "D:\\storage\\upload\\seal\\20180420161116.png"));
        //sealObjects.add(new SealObject("物流商签字盖章", "D:\\storage\\upload\\seal\\11111111111.png"));
        //factory.signature(new File(file.getPath()), sealObjects);

        String cccc = "PGRpdiBjbGFzcz0icGRmLWRpdiI+CjxkaXYgY2xhc3M9InRpdGxlLWg0Ij7kuqflk4HphY3pgIHljZU8L2Rpdj4KPHRhYmxlIGNsYXNzPSJ0YWJsZS1sZWZ0Ij4KICAgIDx0Ym9keT4KICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZCB3aWR0aD0iMTEwIj7phY3pgIHljZXlj7c6PC90ZD4KICAgICAgICAgICAgPHRkIHdpZHRoPSIzOTAiPiR7ZGVsaXZlcnlOb308L3RkPgogICAgICAgICAgICA8dGQgd2lkdGg9IjExMCI+5pel5pyfOjwvdGQ+CiAgICAgICAgICAgIDx0ZCB3aWR0aD0iMTEwIj4ke2RlbGl2ZXJ5VGltZT9zdHJpbmcoInl5eXktTU0tZGQiKX08L3RkPgogICAgICAgIDwvdHI+CiAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQ+6K6i5Y2V5a6i5oi3OjwvdGQ+CiAgICAgICAgICAgIDx0ZD48I2lmIHJlY2VpdmU/Pz4ke3JlY2VpdmUuY29tcGFueU5hbWV9PC8jaWY+PC90ZD4KICAgICAgICAgICAgPHRkPuiuouWNlee8luWPtzo8L3RkPgogICAgICAgICAgICA8dGQ+JHtvcmRlck5vfTwvdGQ+CiAgICAgICAgPC90cj4KICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZD7mlLbotKflrqLmiLc6PC90ZD4KICAgICAgICAgICAgPHRkPjwjaWYgcmVjZWl2ZT8/PiR7cmVjZWl2ZS5jb21wYW55TmFtZX08LyNpZj48L3RkPgogICAgICAgICAgICA8dGQ+5pS26LSn5Lq65ZGYOjwvdGQ+CiAgICAgICAgICAgIDx0ZD4ke3JlY2VpdmVyTmFtZX08L3RkPgogICAgICAgIDwvdHI+CiAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQ+54mp5rWB5ZWG5ZCN56ewOjwvdGQ+CiAgICAgICAgICAgIDx0ZD48I2lmIGNvbnZleT8/PiR7Y29udmV5LmNvbXBhbnlOYW1lfTwvI2lmPjwvdGQ+CiAgICAgICAgICAgIDx0ZD7ogZTns7vmlrnlvI86PC90ZD4KICAgICAgICAgICAgPHRkPiR7cmVjZWl2ZXJDb250YWN0fTwvdGQ+CiAgICAgICAgPC90cj4KICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZD7mlLbotKflnLDlnYA6PC90ZD4KICAgICAgICAgICAgPHRkIGNvbHNwYW49IjMiPiR7cmVjZWl2ZUFkZHJlc3N9PC90ZD4KICAgICAgICA8L3RyPgogICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkPuWkh+azqDo8L3RkPgogICAgICAgICAgICA8dGQgY29sc3Bhbj0iMyI+JHtyZW1hcmt9PC90ZD4KICAgICAgICA8L3RyPgogICAgPC90Ym9keT4KPC90YWJsZT4KPGRpdj7orqLljZXmmI7nu4bvvJo8L2Rpdj4KICAgIDx0YWJsZSBjbGFzcz0idGFibGUtYm9yZGVyIj4KICAgICAgICA8dGhlYWQ+CiAgICAgICAgICAgIDx0cj4KICAgICAgICAgICAgICAgIDx0aCB3aWR0aD0iMTAwIj7nianmlpnnvJblj7c8L3RoPgogICAgICAgICAgICAgICAgPHRoIHdpZHRoPSIxMDAiPueJqeaWmeaPj+i/sDwvdGg+CiAgICAgICAgICAgICAgICA8dGggd2lkdGg9IjgwIj7kuqflk4Hlnovlj7c8L3RoPgogICAgICAgICAgICAgICAgPHRoIHdpZHRoPSI4MCI+5Y2V5L2NPC90aD4KICAgICAgICAgICAgICAgIDx0aCB3aWR0aD0iODAiPuaVsOmHjzwvdGg+CiAgICAgICAgICAgICAgICA8dGggd2lkdGg9IjgwIj7nrrHmlbA8L3RoPgogICAgICAgICAgICAgICAgPHRoIHdpZHRoPSI4MCI+5L2T56evPC90aD4KICAgICAgICAgICAgICAgIDx0aCB3aWR0aD0iODAiPumHjemHjzwvdGg+CiAgICAgICAgICAgIDwvdHI+CiAgICAgICAgPC90aGVhZD4KICAgICAgICA8dGJvZHk+CiAgICAgICAgPCNsaXN0IGNvbW1vZGl0aWVzIGFzIGNvbW1vZGl0eT4KICAgICAgICA8dHI+CiAgICAgICAgICAgICAgICA8dGQ+PCNpZiBjb21tb2RpdHkuY29tbW9kaXR5Tm8/Pz4ke2NvbW1vZGl0eS5jb21tb2RpdHlOb308LyNpZj48L3RkPgogICAgICAgICAgICAgICAgPHRkPjwjaWYgY29tbW9kaXR5LmNvbW1vZGl0eU5hbWU/Pz4ke2NvbW1vZGl0eS5jb21tb2RpdHlOYW1lfTwvI2lmPjwvdGQ+CiAgICAgICAgICAgICAgICA8dGQ+PCNpZiBjb21tb2RpdHkuY29tbW9kaXR5VHlwZT8/PiR7Y29tbW9kaXR5LmNvbW1vZGl0eVR5cGV9PC8jaWY+PC90ZD4KICAgICAgICAgICAgICAgIDx0ZD48I2lmIGNvbW1vZGl0eS5jb21tb2RpdHlVbml0Pz8+JHtjb21tb2RpdHkuY29tbW9kaXR5VW5pdH08LyNpZj48L3RkPgogICAgICAgICAgICAgICAgPHRkPiR7Y29tbW9kaXR5LnF1YW50aXR5fTwvdGQ+CiAgICAgICAgICAgICAgICA8dGQ+JHtjb21tb2RpdHkuYm94Q291bnR9PC90ZD4KICAgICAgICAgICAgICAgIDx0ZD4ke2NvbW1vZGl0eS52b2x1bWV9PC90ZD4KICAgICAgICAgICAgICAgIDx0ZD4ke2NvbW1vZGl0eS53ZWlnaHR9PC90ZD4KICAgICAgICAgICAgPC90cj4KICAgICAgICA8LyNsaXN0PgogICAgICAgIDwvdGJvZHk+CiAgICAgICAgPHRmb290PgogICAgICAgICAgICA8dHI+CiAgICAgICAgICAgICAgICA8dGQgY29sc3Bhbj0iNCI+5ZCI6K6hPC90ZD4KICAgICAgICAgICAgICAgIDx0ZD4ke3F1YW50aXR5fTwvdGQ+CiAgICAgICAgICAgICAgICA8dGQ+JHtib3hDb3VudH08L3RkPgogICAgICAgICAgICAgICAgPHRkPiR7dm9sdW1lfTwvdGQ+CiAgICAgICAgICAgICAgICA8dGQ+JHt3ZWlnaHR9PC90ZD4KICAgICAgICAgICAgPC90cj4KICAgICAgICA8L3Rmb290PgogICAgPC90YWJsZT4KPHRhYmxlPgogICAgPHRib2R5PgogICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkPjwjaWYgZXhjZXB0aW9uPz8+5byC5bi4OjxzcGFuPiR7ZXhjZXB0aW9uLmNvbnRlbnR9PC9zcGFuPjwvI2lmPjwvdGQ+CiAgICAgICAgPC90cj4KICAgIDwvdGJvZHk+CjwvdGFibGU+Cjx0YWJsZSBjbGFzcz0idGFibGUtYm9yZGVyIHRhYmxlLWxlZnQiPgogICAgPHRib2R5PgogICAgPHRyPgogICAgICAgIDx0ZCB3aWR0aD0iMzYwIj7nianmtYHllYbnrb7lrZc8L3RkPgogICAgICAgIDx0ZCB3aWR0aD0iMzYwIj7nianmtYHllYbnm5bnq6A8L3RkPgogICAgPC90cj4KICAgIDx0cj4KICAgICAgICA8dGQgY29sc3Bhbj0iMiI+CiAgICAgICAgICAgIDxwPua4qemmqOaPkOekuu+8mueJqea1gemFjemAgei0o+S7u+S7peetvuaUtuWNleaNruS4uuS4u+imgeS+neaNru+8jOaVheivt+aCqOS4peagvOaMieeFp+aUtui0p+agh+WHhumqjOaUtuetvuWtl+OAgeeblueroOehruiupOOAguWmguacieW8guW4uOaDheWGte+8jOivt+WPiuaXtuiHtOeUteS5nemYs+eJqea1gTwvcD4KICAgICAgICAgICAgPHA+CiAgICAgICAgICAgICAgICA8c3Bhbj7mna3lt55SREM6MDU3MS0xMjM0NTY3ODwvc3Bhbj4KICAgICAgICAgICAgICAgIDxzcGFuPua1juWNl1JEQzowNTMxLTEyMzQ1Njc4PC9zcGFuPgogICAgICAgICAgICAgICAgPHNwYW4+5bm/5beeUkRDOjA3NTctMTIzNDU2Nzg8L3NwYW4+CiAgICAgICAgICAgIDwvcD4KICAgICAgICA8L3RkPgogICAgPC90cj4KICAgIDx0cj4KICAgICAgICA8dGQ+5a6e5pS25Lu25pWwKOWkp+WGmSk8L3RkPgogICAgICAgIDx0ZD4ke3JlYWxOdW1iZXJ9PC90ZD4KICAgIDwvdHI+CiAgICA8dHI+CiAgICAgICAgPHRkIGNvbHNwYW49IjIiPgogICAgICAgICAgICA8cD7miJHlt7Lnoa7orqTmraTphY3pgIHljZXkv6Hmga/kuI7lrp7pmYXmjqXmlLbotKfnianmlbDph4/jgIHlnovlj7fjgIHphY3pgIHmoIfor4Yo6Ziy56qc6LSn56CBKeS4gOiHtDwvcD4KICAgICAgICA8L3RkPgogICAgPC90cj4KICAgIDx0cj4KICAgICAgICA8dGQ+5pS26LSn5Lq6562+5a2XPC90ZD4KICAgICAgICA8dGQ+5pS26LSn5Y2V5L2N5Lia5Yqh56ugPC90ZD4KICAgIDwvdHI+CiAgICA8L3Rib2R5Pgo8L3RhYmxlPgo8L2Rpdj4=";
        System.out.println(new String(Base64.decodeBase64(cccc)));

    }
}

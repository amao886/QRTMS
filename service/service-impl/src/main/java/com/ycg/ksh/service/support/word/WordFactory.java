package com.ycg.ksh.service.support.word;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/16
 */

import com.ycg.ksh.common.system.SystemKey;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.util.encrypt.MD5;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.jaxb.Context;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * WORD创建类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/16
 */
public class WordFactory {

    protected final Logger logger = LoggerFactory.getLogger(WordFactory.class);

    private static final String SUFFIX = "doc";
    private static final String PRE_TEMPLATE_KEY = "TEMPLATE_KEY_WORD#";

    public static WordFactory getInstance(){
        return new WordFactory();
    }

    public String loadByTemplate(Serializable uniqueName, String templateString, final Object object) throws Exception {
        Object dataObject = object;
        if(object instanceof Collection || object instanceof Set){
            dataObject = new HashMap<String, Object>(){{
                put("results", object);
            }};
        }
        Configuration cfg = new Configuration(new Version("2.3.28"));
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate(PRE_TEMPLATE_KEY + uniqueName, templateString);
        cfg.setTemplateLoader(stringLoader);

        Template template = cfg.getTemplate(PRE_TEMPLATE_KEY + uniqueName, "utf-8");
        StringWriter writer = new StringWriter();
        template.process(dataObject, writer);
        return writer.toString();
    }

    public File createByTemplate(String fileName, Collection<? extends Object> objects, String htmlTemplate, String cssContent) throws Exception {
        try {
            int pages = objects.size(), page = 0;
            File file = FileUtils.file(fileName);
            ObjectFactory factory = Context.getWmlObjectFactory();
            WordprocessingMLPackage wp = WordprocessingMLPackage.createPackage();
            XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wp);
            MainDocumentPart mdp = wp.getMainDocumentPart();
            //String top, String left, String bottom, String right
            setDocMarginSpace(wp, factory, "1440", "600", "1440", "600");
            //mdp.getContent().addAll(XHTMLImporter.convert(html("", cssContent), null));
            for (Object object : objects) {
                if(pages > 1 && page ++ > 0){
                    addPageBreak(mdp, factory);
                }
                String htmlContext = html(loadByTemplate(MD5.encrypt(fileName)+"-"+ page, htmlTemplate, object), cssContent);
                //System.out.println(htmlContext);
                mdp.getContent().addAll(new XHTMLImporterImpl(wp).convert(htmlContext, null));
            }
            List<Object> contents = mdp.getContent();
            wp.save(new java.io.File(fileName));
            /*
            //创建 POIFSFileSystem 对象
            try(InputStream is = new ByteArrayInputStream(bytes);
                OutputStream os = new FileOutputStream(file);
                POIFSFileSystem poifs = new POIFSFileSystem()) {
                //创建文档,1.格式,2.HTML文件输入流
                poifs.createDocument(is,"WordDocument");
                //写入
                poifs.writeFilesystem(os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String html(String htmlContent, String cssContent){
        StringBuilder builder = new StringBuilder("<!DOCTYPE html>");
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");
        builder.append("<style>" + cssContent + "</style>");
        builder.append("</head>");
        builder.append("<body>" + htmlContent + "</body>");
        builder.append("</html>");
        return builder.toString();
    }

    /*------------------------------------Word 相关---------------------------------------------------  */
    /**
     * @Description: 设置分节符 nextPage:下一页 continuous:连续 evenPage:偶数页 oddPage:奇数页
     */
    public void setDocSectionBreak(WordprocessingMLPackage wordPackage, String sectValType) {
        if (StringUtils.isNotBlank(sectValType)) {
            SectPr sectPr = getDocSectPr(wordPackage);
            SectPr.Type sectType = sectPr.getType();
            if (sectType == null) {
                sectType = new SectPr.Type();
                sectPr.setType(sectType);
            }
            sectType.setVal(sectValType);
        }
    }

    /**
     * @Description: 设置页面背景色
     */
    public void setDocumentBackGround(WordprocessingMLPackage wordPackage, ObjectFactory factory, String color) throws Exception {
        MainDocumentPart mdp = wordPackage.getMainDocumentPart();
        CTBackground bkground = mdp.getContents().getBackground();
        if (StringUtils.isNotBlank(color)) {
            if (bkground == null) {
                bkground = factory.createCTBackground();
                bkground.setColor(color);
            }
            mdp.getContents().setBackground(bkground);
        }
    }

    /**
     * @Description: 设置页面边框
     */
    public void setDocumentBorders(WordprocessingMLPackage wordPackage, ObjectFactory factory, CTBorder top, CTBorder right, CTBorder bottom, CTBorder left) {
        SectPr sectPr = getDocSectPr(wordPackage);
        SectPr.PgBorders pgBorders = sectPr.getPgBorders();
        if (pgBorders == null) {
            pgBorders = factory.createSectPrPgBorders();
            sectPr.setPgBorders(pgBorders);
        }
        if (top != null) {
            pgBorders.setTop(top);
        }
        if (right != null) {
            pgBorders.setRight(right);
        }
        if (bottom != null) {
            pgBorders.setBottom(bottom);
        }
        if (left != null) {
            pgBorders.setLeft(left);
        }
    }

    /**
     * @Description: 设置页面大小及纸张方向 landscape横向
     */
    public void setDocumentSize(WordprocessingMLPackage wordPackage, ObjectFactory factory, String width, String height, STPageOrientation stValue) {
        SectPr sectPr = getDocSectPr(wordPackage);
        SectPr.PgSz pgSz = sectPr.getPgSz();
        if (pgSz == null) {
            pgSz = factory.createSectPrPgSz();
            sectPr.setPgSz(pgSz);
        }
        if (StringUtils.isNotBlank(width)) {
            pgSz.setW(new BigInteger(width));
        }
        if (StringUtils.isNotBlank(height)) {
            pgSz.setH(new BigInteger(height));
        }
        if (stValue != null) {
            pgSz.setOrient(stValue);
        }
    }

    public SectPr getDocSectPr(WordprocessingMLPackage wordPackage) {
        List<SectionWrapper> sectionWrappers = wordPackage.getDocumentModel().getSections();
        if(sectionWrappers != null && sectionWrappers.size() > 0){
            return sectionWrappers.get(0).getSectPr();
        }
        return null;
    }

    /**
     * @Description：设置页边距
     */
    public void setDocMarginSpace(WordprocessingMLPackage wordPackage, ObjectFactory factory, String top, String left, String bottom, String right) {
        SectPr sectPr = getDocSectPr(wordPackage);
        SectPr.PgMar pg = sectPr.getPgMar();
        if (pg == null) {
            pg = factory.createSectPrPgMar();
            sectPr.setPgMar(pg);
        }
        if (StringUtils.isNotBlank(top)) {
            pg.setTop(new BigInteger(top));
        }
        if (StringUtils.isNotBlank(bottom)) {
            pg.setBottom(new BigInteger(bottom));
        }
        if (StringUtils.isNotBlank(left)) {
            pg.setLeft(new BigInteger(left));
        }
        if (StringUtils.isNotBlank(right)) {
            pg.setRight(new BigInteger(right));
        }
    }

    /**
     * @Description: 设置行号
     * @param distance
     *            :距正文距离 1厘米=567
     * @param start
     *            :起始编号(0开始)
     * @param countBy
     *            :行号间隔
     * @param restartType
     *            :STLineNumberRestart.CONTINUOUS(continuous连续编号)<br/>
     *            STLineNumberRestart.NEW_PAGE(每页重新编号)<br/>
     *            STLineNumberRestart.NEW_SECTION(每节重新编号)
     */
    public void setDocInNumType(WordprocessingMLPackage wordPackage, String countBy, String distance, String start, STLineNumberRestart restartType) {
        SectPr sectPr = getDocSectPr(wordPackage);
        CTLineNumber lnNumType = sectPr.getLnNumType();
        if (lnNumType == null) {
            lnNumType = new CTLineNumber();
            sectPr.setLnNumType(lnNumType);
        }
        if (StringUtils.isNotBlank(countBy)) {
            lnNumType.setCountBy(new BigInteger(countBy));
        }
        if (StringUtils.isNotBlank(distance)) {
            lnNumType.setDistance(new BigInteger(distance));
        }
        if (StringUtils.isNotBlank(start)) {
            lnNumType.setStart(new BigInteger(start));
        }
        if (restartType != null) {
            lnNumType.setRestart(restartType);
        }
    }

    /**
     * @Description：设置文字方向 tbRl 垂直
     */
    public void setDocTextDirection(WordprocessingMLPackage wordPackage, String textDirection) {
        if (StringUtils.isNotBlank(textDirection)) {
            SectPr sectPr = getDocSectPr(wordPackage);
            TextDirection textDir = sectPr.getTextDirection();
            if (textDir == null) {
                textDir = new TextDirection();
                sectPr.setTextDirection(textDir);
            }
            textDir.setVal(textDirection);
        }
    }

    /**
     * @Description：设置word 垂直对齐方式(Word默认方式都是"顶端对齐")
     */
    public void setDocVAlign(WordprocessingMLPackage wordPackage, STVerticalJc valignType) {
        if (valignType != null) {
            SectPr sectPr = getDocSectPr(wordPackage);
            CTVerticalJc valign = sectPr.getVAlign();
            if (valign == null) {
                valign = new CTVerticalJc();
                sectPr.setVAlign(valign);
            }
            valign.setVal(valignType);
        }
    }

    /**
     * @Description：获取文档的可用宽度
     */
    public int getWritableWidth(WordprocessingMLPackage wordPackage) throws Exception {
        return wordPackage.getDocumentModel().getSections().get(0).getPageDimensions().getWritableWidthTwips();
    }


    /**
     * 增加分页，从当前行直接跳转到下页
     * @param mainPart 文档主体
     * @param contents 文档内容
     */
    public static void addPageBreak(MainDocumentPart mainPart, ObjectFactory factory) {
        Br br = new Br();//换行
        br.setType(STBrType.PAGE);//换页方式

        P paragraph = factory.createP();//段落
        paragraph.getContent().add(br);

        mainPart.addObject(paragraph);
    }

    public static void main(String[] args) throws Exception {
        SystemUtils.put(SystemKey.SYSTEM_FILE_ROOT, "D:/workspace/backup/temp");
        WordFactory wfactory = WordFactory.getInstance();
        String template =
                        "<div class=\"print-item\">" +
                        "    <table cellpadding=\"0\" cellspacing=\"0\">" +
                        "<tbody>" +
                        "        <tr>" +
                        "            <td colspan=\"2\" rowspan=\"2\">发货仓库</td>" +
                        "            <td colspan=\"2\" rowspan=\"2\">${depot}</td>" +
                        "            <td colspan=\"2\">收货单位</td>" +
                        "            <td colspan=\"6\" class=\"tl-pl\">${customerName}</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td colspan=\"2\">收货地址</td>" +
                        "            <td colspan=\"6\" class=\"tl-pl\">${detailaddress}(${contact})</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td colspan=\"2\" rowspan=\"2\">调拨单号</td>" +
                        "            <td colspan=\"2\" rowspan=\"2\">客户编码</td>" +
                        "            <td colspan=\"6\" rowspan=\"2\">品种规格</td>" +
                        "            <td colspan=\"2\">数量</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td colspan=\"1\">瓶数</td>" +
                        "            <td colspan=\"1\">件数</td>" +
                        "        </tr>" +
                        "        <#list list as cc>" +
                        "        <tr>" +
                        "            <td colspan=\"2\" height=\"25px\"><div class=\"goods-td\">${cc.scheduleNo}</div></td>" +
                        "            <td colspan=\"2\" height=\"25px\"><div class=\"goods-td\">${cc.customerNo}</div></td>" +
                        "            <td colspan=\"6\" class=\"tl-pl\" height=\"25px\"><div class=\"goods-td\">${cc.specification}</div></td>" +
                        "            <td colspan=\"1\" height=\"25px\"><div class=\"goods-td\">${cc.bottles!\" \"}</div></td>" +
                        "            <td colspan=\"1\" height=\"25px\"><div class=\"goods-td\">${cc.quantity!\" \"}</div></td>" +
                        "        </tr>" +
                        "        </#list>" +
                        "        <tr>" +
                        "            <td colspan=\"2\">铅封锁</td>" +
                        "            <td colspan=\"8\" class=\"tl-pl\">需要(  )铅封锁号:</td>" +
                        "            <td colspan=\"2\">不需要(√)</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td colspan=\"12\" class=\"tl-pl\">酒类流通随附单号(有客户编码时必填):</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td colspan=\"5\" rowspan=\"3\">企业盖章(未盖章无效)</td>" +
                        "            <td colspan=\"7\" class=\"tl-pl\">承运单位:<span>${conveyName}</span></td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td colspan=\"7\" class=\"tl-pl\">承运单号:</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td colspan=\"7\" class=\"tl-pl\">取单人签字:</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td colspan=\"5\" class=\"tl-pl\">制 单 人:<span class=\"nameStyle\">${username}</span></td>" +
                        "            <td colspan=\"7\" class=\"tl-pl\">取单人身份证:${takeIdcare}</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td colspan=\"5\" class=\"tl-pl\">通知日期:${year}年${month}月${day}日</td>" +
                        "            <td colspan=\"7\" class=\"tl-pl\">领单日期:${year}年${month}月${day}日</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td colspan=\"1\" rowspan=\"6\">收<br />货<br />单<br />位<br />验<br />收<br />签<br />章</td>" +
                        "            <td colspan=\"6\">正常</td>" +
                        "            <td colspan=\"3\">异常</td>" +
                        "            <td colspan=\"2\" rowspan=\"2\" class=\"p9\">第三方物流服<br />务评价</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td colspan=\"6\" rowspan=\"3\" class=\"borderNone\"></td>" +
                        "            <td colspan=\"3\" rowspan=\"5\"></td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td colspan=\"2\" class=\"p17\">优( )</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td colspan=\"2\" class=\"p17\">良( )</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td colspan=\"6\" style=\"border: 0\">(章)</td>" +
                        "            <td colspan=\"2\" class=\"p17\">中( )</td>" +
                        "        </tr>" +
                        "        <tr>" +
                        "            <td colspan=\"6\" class=\"borderTopNone\">签收日期:${year}年  月  日</td>" +
                        "            <td colspan=\"2\" class=\"p17\">差( )</td>" +
                        "        </tr>" +
                        "       </tbody>" +
                        "    </table>" +
                        "</div>";
        String cssString =
                ".print-item{ width:142mm; background:#fff; color:#333; font-family: '等线';}\n" +
                        ".print-item table{ border-collapse: collapse; width: 100%; text-align: center; table-layout:fixed;}\n" +
                        ".print-item table th{width:8.333%;}\n" +
                        ".print-item table td{ width:8.333%; font-size: 14px; border: 1px solid #666;word-wrap: break-word;padding:2px 0;}\n" +
                        ".tl-pl{ text-align: left; padding-left:10px !important;}\n" +
                        ".nameStyle{font-size:14px;letter-spacing: 12px;padding-left:12px;}\n" +
                        ".p9{padding:5px 0 !important;}\n" +
                        ".p17{padding:5px 0 !important;}\n" +
                        ".borderNone{border-top:none !important;border-bottom: none !important;}\n" +
                        ".borderTopNone{border-top:none !important;}\n" +
                        ".pwidth{padding:0 15px;}\n" +
                        ".widthShort{display: inline-block;width:30px;text-align: center;}";
        System.out.println(org.apache.commons.codec.binary.Base64.encodeBase64String(template.getBytes()));
        System.out.println(org.apache.commons.codec.binary.Base64.encodeBase64String(cssString.getBytes()));


        /*
        String xhtml= "<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width:100%;\"><tbody><tr><td>test</td><td>test</td></tr><tr><td>test</td><td>test</td></tr><tr><td>test</td><td>test</td></tr></tbody></table>";

        // To docx, with content controls
        WordprocessingMLPackage wp = WordprocessingMLPackage.createPackage();

        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wp);

        List<Object> objects = XHTMLImporter.convert( xhtml, null);

        MainDocumentPart mdp = wp.getMainDocumentPart();

        ObjectFactory factory = Context.getWmlObjectFactory();

        Br br = new Br();//换行
        br.setType(STBrType.PAGE);//换页方式

        P paragraph = factory.createP();//段落
        paragraph.getContent().add(br);

        mdp.getContent().addAll(objects);
        mdp.getContent().add(paragraph);
        mdp.getContent().addAll(objects);
        wp.save(new java.io.File("X:\\backup\\test.docx"));
        */
    }
}

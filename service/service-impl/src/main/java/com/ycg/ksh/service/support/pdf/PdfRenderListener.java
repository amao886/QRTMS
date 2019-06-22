package com.ycg.ksh.service.support.pdf;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/26
 */

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

/**
 * PDF渲染监听
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/26
 */
public class PdfRenderListener implements RenderListener {

    private int size;
    private int page;

    private PdfSealPosition sealPosition;

    public PdfRenderListener(int page, PdfSealPosition sealPosition) {
        this.page = page;
        this.sealPosition = sealPosition;
    }

    @Override
    public void beginTextBlock() {

    }

    @Override
    public void renderText(TextRenderInfo textRenderInfo) {
        String text = textRenderInfo.getText();
        if (null != text) {
            Rectangle2D.Float rectange = textRenderInfo.getBaseline().getBoundingRectange();
            for (SealObject sealObject : sealPosition.getSealObjects()) {
                if(text.contains(sealObject.getKeyWord())){
                    sealPosition.addCoordinate(sealObject, new PdfCoordinate(rectange.x, rectange.y, rectange.width, rectange.height, page));
                    size++;
                }
            }
        }
    }

    @Override
    public void endTextBlock() {

    }

    @Override
    public void renderImage(ImageRenderInfo imageRenderInfo) {

    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public PdfSealPosition getSealPosition() {
        return sealPosition;
    }

    public void setSealPosition(PdfSealPosition sealPosition) {
        this.sealPosition = sealPosition;
    }
}

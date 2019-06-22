package com.ycg.ksh.service.support.pdf;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/19
 */

/**
 * PDF坐标
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/19
 */
public class PdfCoordinate {

    float x;
    float y;
    float w;
    float h;
    int page;

    public PdfCoordinate(float x, float y, float w, float h, int page) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.page = page;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}

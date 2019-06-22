/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:22:11
 */
package com.ycg.ksh.entity.adapter.baidu;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 图片识别文字，文字信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:22:11
 */
public class WordInfo extends BaseEntity {

    private static final long serialVersionUID = 4212556266913081780L;
    
    private String words;//收货地址
    private Integer top;//表示定位位置的长方形左上顶点的垂直坐标
    private Integer left;//表示定位位置的长方形左上顶点的水平坐标
    private Integer width;//表示定位位置的长方形的宽度
    private Integer height;//表示定位位置的长方形的高度
    /**
     * getter method for words
     * @return the words
     */
    public String getWords() {
        return words;
    }
    /**
     * setter method for words
     * @param words the words to set
     */
    public void setWords(String words) {
        this.words = words;
    }
    /**
     * getter method for top
     * @return the top
     */
    public Integer getTop() {
        return top;
    }
    /**
     * setter method for top
     * @param top the top to set
     */
    public void setTop(Integer top) {
        this.top = top;
    }
    /**
     * getter method for left
     * @return the left
     */
    public Integer getLeft() {
        return left;
    }
    /**
     * setter method for left
     * @param left the left to set
     */
    public void setLeft(Integer left) {
        this.left = left;
    }
    /**
     * getter method for width
     * @return the width
     */
    public Integer getWidth() {
        return width;
    }
    /**
     * setter method for width
     * @param width the width to set
     */
    public void setWidth(Integer width) {
        this.width = width;
    }
    /**
     * getter method for height
     * @return the height
     */
    public Integer getHeight() {
        return height;
    }
    /**
     * setter method for height
     * @param height the height to set
     */
    public void setHeight(Integer height) {
        this.height = height;
    }
    
}

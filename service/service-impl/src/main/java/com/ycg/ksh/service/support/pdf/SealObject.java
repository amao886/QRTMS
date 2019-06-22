package com.ycg.ksh.service.support.pdf;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/26
 */

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 公章对象
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/26
 */
public class SealObject extends BaseEntity {

    private String keyWord;
    private String image;

    public SealObject(String keyWord, String image) {
        this.keyWord = keyWord;
        this.image = image;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

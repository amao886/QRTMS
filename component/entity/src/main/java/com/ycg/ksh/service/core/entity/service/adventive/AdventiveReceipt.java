package com.ycg.ksh.service.core.entity.service.adventive;

import com.ycg.ksh.common.entity.BaseEntity;

public class AdventiveReceipt extends BaseEntity {

    private String uploadTime;//上传时间
    private String imgUrl;//图片地址

    public AdventiveReceipt() {
    }

    public AdventiveReceipt(String uploadTime, String imgUrl) {
        this.uploadTime = uploadTime;
        this.imgUrl = imgUrl;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

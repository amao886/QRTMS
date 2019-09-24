package com.ycg.ksh.common.constant;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/20
 */

import com.ycg.ksh.common.util.FileUtils;

/**
 * 文件目录枚举
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/20
 */
public enum Directory {
    //
    UPLOAD("upload"),//上传文件目录
    DOWN("down"),//下载文件目录
    TEMP("temp"),//临时目录
    PDF("pdf"),//pdf文件目录
    TEMPLATE("template"),//模板目录
    DELIVERY("upload/delivery"),//上传绑单图片目录
    RECEIPT("upload/receipt"),//上传回单图片目录
    EXCEPTION("upload/exception"),//上传异常图片目录
    ERECEIPT("pdf/receipt"),//电子回单生成存放目录
    LICENSE("upload/license"),//上传的公司营业执照
    SEAL("upload/seal"),//上传的公司印章图片
    PERSONALAUTH("upload/personalauth"),//上传企业授权书


    DOWN_REPORT("report"),//下载报表目录

    UPLOAD_DELIVERY("delivery"),//上传绑单图片目录
    UPLOAD_RECEIPT("receipt"),//上传回单图片目录
    UPLOAD_EXCEPTION("exception");//上传异常图片目录


    private String dir;

    Directory(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }

    public String sub(String subPath) {
        return FileUtils.path(dir, subPath);
    }
}

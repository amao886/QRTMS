package com.ycg.ksh.common.constant;

import com.ycg.ksh.common.util.DateUtils;

import java.util.Date;

public interface FileConstant {
	String CurrentTime = DateUtils.formatDateToSimple(new Date());
	String SIVE_UPLOAD_PATH = "/opt/storage/upload/"+CurrentTime+"/";//文件按时间存放
	String SERCH_UPLOAD_PATH = "/"+CurrentTime+"/";//数据库保存文件夹日期
	String SAVE_UPLOAD_PATH = "/opt/storage/upload/";//文件根路径
	String TEMP_DOWNLOAD_PATH = "/opt/storage/temp/";//图片文件临时路径
	
	//文件类型
	String IMAGE_UPLOAD_ZIP = "zip";//zip类型
	String IMAGE_UPLOAD_JPEG = "jpg";//jpeg类型
	
	String UPLOAD_TXT = "txt";//jpeg类型
	String UPLOAD_EXCEL_XLS = "xls";//xls类型
	
}

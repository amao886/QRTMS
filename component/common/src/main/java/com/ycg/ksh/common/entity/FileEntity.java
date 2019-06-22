/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-27 15:42:17
 */
package com.ycg.ksh.common.entity;

import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;

import java.io.File;

/**
 * 文件下载实体类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-27 15:42:17
 */
public class FileEntity extends BaseEntity {


	private static final long serialVersionUID = 3947590724285291494L;

	/**
	 * 文件后缀
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-18 19:23:31
	 */
	private String suffix;
	/**
	 * 文件名称
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-18 16:30:42
	 */
	private String fileName;
	/**
	 * 子目录
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-18 16:30:42
	 */
	private String subDir;

	/**
	 * 根目录
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-18 17:06:41
	 */
	private String directory;
	/**
	 * 文件路径
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-27 14:58:33
	 */
	private String path;
	/**
	 * 文件个数
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-27 14:58:45
	 */
	private Integer count;
	/**
	 * 文件大小
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-27 14:58:45
	 */
	private double size;

	private String url;

	private String aliasName;


	private Integer successCount = 0;
	private Integer failureCount = 0;


	public FileEntity() { }

	public FileEntity(String directory, Directory subDir, String fileName, String suffix) {
		setDirectory(directory);
		setSubDir(subDir.getDir());
		setSuffix(suffix);
		setFileName(FileUtils.appendSuffix(fileName, suffix));
	}

	public FileEntity(File file) {
		setSuffix(FileUtils.suffix(file.getName()));
		setDirectory(file.getParent());
		setFileName(file.getName());
		setSize(FileUtils.size(file.length(), FileUtils.ONE_MB));
		setPath(file.getPath());
	}

	public FileEntity(File file, Integer count) {
		this(file);
		this.count = count;
	}

	public void modify(Integer successCount, Integer failureCount){
		this.successCount = successCount;
		this.failureCount = failureCount;
	}

	public String persistence(){
		if(StringUtils.isNotBlank(subDir)){
			return FileUtils.path(subDir, fileName);
		}
		return fileName;
	}
    /**
	 * getter method for fileName
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * setter method for fileName
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * getter method for path
	 * @return the path
	 */
	public String getPath() {
		if(StringUtils.isNotBlank(directory)) {
			path = FileUtils.path(directory, persistence());
		}
	    return path;
	}
	/**
	 * setter method for path
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * getter method for count
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}
	/**
	 * setter method for count
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
	/**
	 * getter method for size
	 * @return the size
	 */
	public double getSize() {
		return size;
	}
	/**
	 * setter method for size
	 * @param size the size to set
	 */
	public void setSize(double size) {
		this.size = size;
	}
	/**
	 * getter method for directory
	 * @return the directory
	 */
	public String getDirectory() {
		return directory;
	}
	/**
	 * setter method for directory
	 * @param directory the directory to set
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	/**
	 * getter method for suffix
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}
	/**
	 * setter method for suffix
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public Integer getFailureCount() {
		return failureCount;
	}

	public String getSubDir() {
		return subDir;
	}

	public void setSubDir(String subDir) {
		this.subDir = subDir;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
}

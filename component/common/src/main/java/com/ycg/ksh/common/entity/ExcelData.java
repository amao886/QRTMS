package com.ycg.ksh.common.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-16 10:59:36
 */
public class ExcelData implements Serializable{

	private static final long serialVersionUID = -6107225011400631435L;

	private List<RowData> rows;
	
	private String fileName;

	public List<RowData> getRows() {
		return rows;
	}

	public void setRows(List<RowData> rows) {
		this.rows = rows;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}

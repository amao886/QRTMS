package com.ycg.ksh.api.common.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class PageTag extends SimpleTagSupport {

	protected final Logger logger = LoggerFactory.getLogger(PageTag.class);

	private Integer pageNum;
	private Integer pageSize;
	private Long total;
	private Integer pages;
	private Integer prePage;
	private Integer nextPage;
	private Boolean firstPage;
	private Boolean lastPage;
	private Integer startRow;
	private Integer endRow;
	
	@Override
	public void doTag() throws JspException, IOException {
		try {
			completeSomething();
			StringBuilder builder = new StringBuilder();
	    	if(pages > 1) {
	    		builder.append("<div id=\"layui_div_page\" class=\"col-sm-12 center-block\" >");
	        	builder.append("<ul class=\"pagination\">");
	        	builder.append("<li class=\"disabled\"><span><span aria-hidden=\"true\">共"+ total +"条,每页"+ pageSize +"条</span></span></li>");
	        	if(!firstPage) {
	        		builder.append("<li><a href=\"javascript:;\" num=\"1\">首页</a></li>");
	            	builder.append("<li><a href=\"javascript:;\" num=\""+ prePage +"\">上一页</a></li>");
	        	}
	        	if(startRow > 0 || endRow > startRow) {
	        		for (int p = startRow; p <= endRow; p++) {
	        			if(p == pageNum) {
	        				builder.append("<li class=\"active\"><span>"+ p +"</span></li>");
	        			}else {
	                    	builder.append("<li><a href=\"javascript:;\" num=\""+ p +"\">"+ p +"</a></li>");
	        			}
					}
	        	}
	        	if(!lastPage) {
	            	builder.append("<li><a href=\"javascript:;\" num=\""+ nextPage +"\">下一页</a></li>");
	            	builder.append("<li><a href=\"javascript:;\" num=\""+ pages +"\">尾页</a></li>");
	        	}
	        	builder.append("<li class=\"disabled\"><span><span>共"+ pages +"页</span></span></li>");
	        	builder.append("</ul>");
	        	builder.append("</div>");
	    	}else {
	    		builder.append("<div style=\"height: 10px;\"></div>");
	    	}
			getJspContext().getOut().write(builder.toString());
		} catch (IOException e) {
			logger.error("自定义标签解析异常  pageNum {} pageSize {} total {} pages {}", pageNum, pageSize , total, pages, e);
		}
	}
	
	private void completeSomething() {
		if(pageNum == 0) {
			pageNum = 1;
		}
		if(pages == null) {
			pages = 0;
		}
		if(total == null) {
			total = 0l;
		}
		firstPage = (pageNum - 1 == 0);
		prePage = Math.max(1, pageNum - 1);
		nextPage = Math.min(pages, pageNum + 1);
		lastPage = (pageNum - pages == 0);
		
		startRow = Math.max(1, pageNum - 4);
		endRow = Math.min(pageNum + 4, pages);
	}
	
	public void setPageNum(Integer pageNum) {
		if(pageNum == null) {
			this.pageNum = 0;
		}else {
			this.pageNum = pageNum;
		}
	}

	public void setPageSize(Integer pageSize) {
		if(pageSize == null) {
			this.pageSize = 0;
		}else {
			this.pageSize = pageSize;
		}
	}

	public void setTotal(Long total) {
		if(total == null) {
			this.total = 0l;
		}else {
			this.total = total;
		}
	}

	public void setPages(Integer pages) {
		if(pages == null) {
			this.pages = 0;
		}else {
			this.pages = pages;
		}
	}
}

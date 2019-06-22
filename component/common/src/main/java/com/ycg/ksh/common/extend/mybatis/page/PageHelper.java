package com.ycg.ksh.common.extend.mybatis.page;

import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis分页插件
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-06 12:35:59
 */
public class PageHelper extends com.github.pagehelper.PageHelper {

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object afterPage(List pageList, Object parameterObject, RowBounds rowBounds) {
		Object object = super.afterPage(pageList, parameterObject, rowBounds);
		if(com.github.pagehelper.Page.class.isAssignableFrom(object.getClass())){
			com.github.pagehelper.Page<?> page = (com.github.pagehelper.Page<?>) object;
			return new CustomPage(page.getPageNum(), page.getPageSize(), page.getTotal(), new ArrayList<Object>(page));
		}
		return object;
	}

}

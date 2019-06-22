package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.Route;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Collection;

public class MergeRoute extends Route {

	/**
	 * serialVersionUID : TODO Add description
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-24 11:03:04
	 */
	private static final long serialVersionUID = 5328793692060781421L;
	public MergeRoute(){
		super();
	}
	
	public MergeRoute(Route route) throws Exception{
		super();
		BeanUtils.copyProperties(this, route);
	}
	
	private Collection<MergeRouteLine> routeLines;
	
	public Collection<MergeRouteLine> getRouteLines() {
		return routeLines;
	}

	public void setRouteLines(Collection<MergeRouteLine> routeLines) {
		this.routeLines = routeLines;
	}
}

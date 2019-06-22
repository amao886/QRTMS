package com.ycg.ksh.common.extend.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CORSFilter implements Filter {

	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException { }

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) servletResponse; 
		response.setHeader("Access-Control-Allow-Origin", "*"); 
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE"); 
		response.setHeader("Access-Control-Max-Age", "3600"); 
		response.setHeader("Access-Control-Allow-Headers", "token,Accept,Origin,XRequestedWith,Content-Type,LastModified,X-Request-Type,X-Requested-With"); 
		response.setHeader("Access-Control-Allow-Credentials","true"); 
		chain.doFilter(servletRequest, response);
	}

	@Override
	public void destroy() { }
}

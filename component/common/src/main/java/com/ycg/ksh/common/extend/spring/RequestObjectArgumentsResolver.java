/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-28 12:44:54
 */
package com.ycg.ksh.common.extend.spring;

import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-28 12:44:54
 */
public class RequestObjectArgumentsResolver implements HandlerMethodArgumentResolver {

	/**
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#supportsParameter(org.springframework.core.MethodParameter)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-28 12:44:54
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return RequestObject.class.isAssignableFrom(parameter.getParameterType());
	}

	/**
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-28 12:44:54
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		RequestObject requestObject = new RequestObject();
		for (Map.Entry<String, String[]> entry : webRequest.getParameterMap().entrySet()) {
			if(entry.getValue() == null) {
				continue;
			}
			if(entry.getValue().length == 0) {
				requestObject.put(entry.getKey(), entry.getValue()[0]);
			}else {
				requestObject.put(entry.getKey(), StringUtils.join(entry.getValue(), ","));
			}
			//RequestParamMapMethodArgumentResolver
		}
		return requestObject;
	}
}

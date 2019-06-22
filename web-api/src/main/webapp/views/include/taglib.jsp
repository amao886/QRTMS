<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="someprefix" uri="/WEB-INF/sometag.tld" %>
<c:set var="times" value="<%=System.currentTimeMillis()%>"/>
<c:set var="scheme" value="<%=request.getScheme()%>"/>
<c:set var="server_name" value="<%=request.getServerName()%>"/>
<c:set var="server_port" value="<%=request.getServerPort()%>"/>
<c:set var="context_path" value="<%=request.getContextPath()%>"/>
<c:set var="basePath" value="${scheme}://${server_name }"/>
<c:set var="path_host" value="${server_name }"/>
<c:if test="${server_port != null && server_port != 80 }">
	<c:set var="basePath" value="${basePath}:${server_port }"/>
	<c:set var="path_host" value="${path_host}:${server_port }"/>
</c:if>
<c:if test="${context_path != null}">
	<c:set var="basePath" value="${basePath}${context_path}"/>
	<c:set var="path_host" value="${path_host}${context_path}"/>
</c:if>
<c:set var="baseStatic" value="/static/"/>

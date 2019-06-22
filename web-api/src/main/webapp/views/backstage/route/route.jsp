<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>常用设置-路由管理</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/page.css?times=${times}" />
<link rel="stylesheet" href="${baseStatic}css/route.css?times=${times}" />
<style type="text/css">


</style>
</head>
<body>
	<div class="custom-content clearfix">
	    <form id="searchRouteForm" action="${basePath}/backstage/route/list" method="post">
	        <div class="searchInput">
	            <input type="hidden" name="pageNum" value="${search.pageNum }" />
	            <input type="hidden" name="pageSize" value="${search.pageSize }" />
	            <input type="text" name="routeName" placeholder="路由名称" value="${search.routeName }" />
	            <button id="search" type="submit"></button>
	        </div>
	    </form>	 
	       
		<div class="routeCon">	
		    <p><a class="btn btn-default mr20" href="${basePath}/backstage/route/addView">新建路由</a>当前共${page.total}条常用路由</p>
		   
		    <!-- 循环体 st -->
		    <c:forEach var="mergeRoute" items="${page.collection }">
			    <div class="routeBox">
			        <h3>${mergeRoute.routeName }</h3>		        
			        <div class="routeInfo clearfix">
			            <c:forEach items="${mergeRoute.routeLines }" var="line">
							<!-- 起点 -->
							<c:if test="${line.lineType == 1 }">
								<div class="route first">
									<div class="routeTop clearfix">
										<span class="circle">起</span>
										<div class="line"></div>
									</div>
									<div class="routeBottom clear">
										<c:if test="${line.user != null}">
											<c:choose>
												<c:when test="${line.user.type == 2}">
													<p>承运商:${line.user.company }</p>
													<p>司机姓名:${line.user.unamezn }</p>
													<p>联系方式:${line.user.mobilephone }</p>
												</c:when>
												<c:otherwise>
													<p>司机姓名:${line.user.unamezn }</p>
													<p>联系方式:${line.user.mobilephone }</p>
												</c:otherwise>
											</c:choose>
										</c:if>
									</div>
								</div>
							</c:if>
							<!-- 中转点 -->
							<c:if test="${line.lineType == 2 }">
								<div class="route">
									<div class="routeTop clearfix">
										<span class="circle">转</span>
										<div class="line">${line.simpleStation}</div>
									</div>
									<div class="routeBottom clear">
										<c:if test="${line.user != null}">
											<c:choose>
												<c:when test="${line.user.type == 2}">
													<p>承运商:${line.user.company }</p>
													<p>司机姓名:${line.user.unamezn }</p>
													<p>联系方式:${line.user.mobilephone }</p>
												</c:when>
												<c:otherwise>
													<p>司机姓名:${line.user.unamezn }</p>
													<p>联系方式:${line.user.mobilephone }</p>
												</c:otherwise>
											</c:choose>
										</c:if>
									</div>
								</div>
							</c:if>
							<!-- 终点 -->
							<c:if test="${line.lineType == 3 }">
								<div class="route last">
									<div class="routeTop clearfix">
										<span class="circle">终</span>
										<div class="line"></div>
									</div>
									<!--
									<div class="routeBottom clear">
										<p>联系人：${line.userNick }</p>
										<p>联系方式：${line.mobile }</p>
									</div>
									-->
								</div>
							</c:if>
						</c:forEach>
			        </div>
			        <span class="btn btn-default del" data-id="${mergeRoute.id }">删除</span>	
			        <a class="btn btn-default edit" data-id="${mergeRoute.id }" href="${basePath}/backstage/route/editView?id=${mergeRoute.id}">编辑</a>	        
			    </div>
		    </c:forEach>  
		    <!-- 循环体 ed -->
		</div>
		<c:if test="${page.collection != null && fn:length(page.collection) > 0}">
             <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}"  pages="${page.pages}" total="${page.total}"/>
        </c:if>						
	</div>
	
</body>
<%@ include file="/views/include/floor.jsp"%>
<script type="text/javascript" src='//webapi.amap.com/maps?v=1.4.0&key=a4e0b28c9ca0d166b8872fc3823dbb8a&plugin=AMap.ToolBar,AMap.DistrictSearch,AMap.Geocoder'></script>
<!-- UI组件库 1.0 -->
<script src="//webapi.amap.com/ui/1.0/main.js?v=1.0.11"></script>
<script>
var searchBody;//新增修改弹出框页面对象
$(document).ready(function(){
	//提交查询事件
	$("#search").on("click", function(){
		$("#searchRouteForm").submit();
	});
	//翻页事件
	$(".pagination a").each(function(i){
		$(this).on('click', function(){
			$("form input[name='pageNum']").val($(this).attr("num"));
			$("#searchRouteForm").submit();
		});   
	});
    $(".del").on("click",function(){
        var id = $(this).attr("data-id");
        $.util.confirm("提示", "确定要删除该路由信息吗?", function(){
            $.util.json(base_url+'/backstage/route/delete/'+ id, {}, function(data){
                if(data.success){
                    $.util.alert('操作提示', data.message, function(){
                        location.href = base_url + "/backstage/route/list";
                    });
                }else{
                    $.util.error(data.message);
                }
            });
        });
    })
});
</script>

</html>
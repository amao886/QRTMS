<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>物流跟踪-电子回单管理-待处理回单-详情</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/viewer.css" />
<link rel="stylesheet" href="${baseStatic}css/billDetails.css?times=${times}" />
<style type="text/css">
body,html,#container{
  height: 100%;
  margin: 0px;
}
#container {
	width:100%; 
	height: 380px; 
	border: dashed 1px #000000; 
} 
.track{
	display: none
}
.infoTitle{
	font-size: 13px; 
	line-height:1.8em;
}
.infoBody{
	font-size: 12px; 
	line-height:1.8em;
}
.dcontent-part h2 {
    font-size: 16px;
    font-weight: 400;
    padding: 10px 0px 10px;
}
.docs-pictures img{
	width: 100px !important;
	height: 68px;
}
.status{
	font-size: 12px; 
	line-height:1.8em;
	font-weight: bold;
	font-family: "微软雅黑";
}
.navigation_bar{
	font-size: 12px;
	padding-bottom: 10px;
	padding-top: 10px;
}
</style>
</head>
<body>
	<div class="detail-content" id="main">
		<div class="navigation_bar">
			当前位置：电子回单管理>><a href="${basePath}/backstage/receipt/wait/search">待处理回单</a>>>查看详情
		</div>
		<div style="padding-bottom: 10px; padding-top: 10px;">
			<h3>待处理类型：
				<c:choose>
				   <c:when test="${waybill.receiptCount == 0}">  
				       	未上传     
				   </c:when>
				   <c:when test="${waybill.receiptCount > 0 && waybill.receiptVerifyCount == 0}">  
				       	已上传-全部未审核    
				   </c:when>
				   <c:when test="${waybill.receiptVerifyCount >= 1 && waybill.receiptCount > waybill.receiptVerifyCount}">  
				       	已上传-部分未审核 
				   </c:when>
				   <c:otherwise> 
				     	已审核-有不合格
				   </c:otherwise>
				</c:choose>
			</h3>
		</div>
		<!-- 任务单详情 -->
		<div class="dcontent-part">
			<h2>任务单详情</h2>
			<div class="track-con track-con-bg">
				<div class="clearfix">
					<div class="fl">
						<span class="spa_n">送货单号:</span>
						<p class="text_p">${waybill.deliveryNumber }</p>
					</div>
					<div class="fl">
						<span class="spa_n">任务摘要:</span>
						<p class="text_p">${waybill.orderSummary }</p>
					</div>
				</div>
				<div class="clearfix">
					<div class="fl">
						<span class="spa_n">任务单号:</span>
						<p class="text_p">${waybill.barcode }</p>
					</div>
					<div class="fl">
						<span class="spa_n">联系人:</span>
						<p class="text_p">${waybill.contactName }</p>
					</div>
				</div>
				<div class="clearfix">
					<div class="fl">
						<span class="spa_n">收货客户:</span>
						<p class="text_p">${waybill.receiverName }</p>
					</div>
					<div class="fl">
						<span class="spa_n">收货地址:</span>
						<p class="text_p">${waybill.receiveAddress }</p>
					</div>
				</div>
				<div class="clearfix">
					<div class="fl">
						<span class="spa_n">联系电话:</span>
						<p class="text_p">${waybill.contactPhone }</p>
					</div>
					<div class="fl">
						<span class="spa_n">座机号:</span>
						<p class="text_p">
						${waybill.receiverTel }
						</p>
					</div>
				</div>
				<div class="clearfix">
					<div class="fl">
						<span class="spa_n">数量:</span>
						<p class="text_p">${waybill.number }</p>
					</div>
					<div class="fl">
						<span class="spa_n">要求到货时间:</span>
						<p class="text_p">
						<c:if test="${waybill.arrivaltime != null}">
							<someprefix:dateTime date="${waybill.arrivaltime}" pattern="yyyy-MM-dd HH:mm"/>
						</c:if>
						</p>
					</div>
				</div>
				<div class="clearfix">
					<div class="fl">
						<span class="spa_n">重量:</span>
						<p class="text_p">${waybill.weight }</p>
					</div>
					<div class="fl">
						<span class="spa_n">发货时间:</span>
						<p class="text_p">
						<c:if test="${waybill.createtime != null}">
							<someprefix:dateTime date="${waybill.createtime}" pattern="yyyy-MM-dd HH:mm"/>
						</c:if>
						</p>
					</div>
				</div>
				<div class="clearfix">
					<div class="fl">
						<span class="spa_n">体积:</span>
						<p class="text_p">${waybill.volume }</p>
					</div>
					<div class="fl">
						<span class="spa_n">到货时间:</span>
						<p class="text_p">
						<c:if test="${waybill.actualArrivalTime != null}">
							<someprefix:dateTime date="${waybill.actualArrivalTime}" pattern="yyyy-MM-dd HH:mm"/>
						</c:if>
						</p>
					</div>
				</div>
			</div>
		</div>
		<!-- 异常情况 -->
		<div class="dcontent-part">
			<h2>异常情况</h2>
			<div class="track-con">
				<c:if test="${waybill.exceptions == null || fn:length(waybill.exceptions) <= 0}">
				 	<div class="track-sub-part track-con-bg">没有异常信息</div>
				</c:if>
				<c:forEach items="${waybill.exceptions}" var="exception">
					<div class="track-sub-part track-con-bg">
						<div class="clearfix">
							<div class="fl">
								<span class="spa_n">异常情况:</span>
								<p class="text_p">${exception.content }</p>
							</div>
							<div class="fl">
								<span class="spa_n">任务摘要:</span>
								<p class="text_p">${waybill.orderSummary }</p>
							</div>
						</div>
						<div class="clearfix">
							<div class="fl">
								<span class="spa_n">上报时间:</span>
								<p class="text_p">
									<c:if test="${exception.createtime != null}">
										<someprefix:dateTime date="${exception.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</c:if>
								</p>
							</div>
							<div class="fl">
								<span class="spa_n">上报人:</span>
								<p class="text_p">
									<c:if test="${exception.user != null}">
										${exception.user.mobilephone}
									</c:if>
								</p>
							</div>
						</div>
						<div class="bill_test">
							<ul class="clearfix paddin_g docs-pictures">
								<c:forEach items="${exception.images}" var="image">
									<li>
										<img src="${imagePath}${image.path }" alt="异常上传图片">
									</li>	
								</c:forEach>
							</ul>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
		<!-- 回单信息 -->
		<div class="dcontent-part">
			<h2>回单信息</h2>
			<div class="track-con">
				<c:if test="${waybill.receipts == null || fn:length(waybill.receipts) <= 0}">
				 	<div class="track-sub-part track-con-bg">暂无回单信息</div>
				</c:if>
				<c:forEach items="${waybill.receipts}" var="receipt">
					<div class="track-sub-part track-con-bg">
						<div class="clearfix">
							<div class="fl">
								<span class="spa_n">上传时间:</span>
								<p class="text_p">
									<c:if test="${receipt.createtime != null}">
										<someprefix:dateTime date="${receipt.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</c:if>
								</p>
							</div>
							<div class="fl">
								<span class="spa_n">上传人:</span>
								<p class="text_p">
									<c:if test="${receipt.user != null }">
										${receipt.user.mobilephone }
									</c:if>
								</p>
							</div>
						</div>
						<div class="bill_test">
							<ul class="clearfix paddin_g docs-pictures">
								<c:forEach items="${receipt.images}" var="image">
									<li>
										<img src="${imagePath}${image.path}" alt="回单上传图片" title="${image.verifyRemark}">
										<c:if test="${image.verifyStatus != null && image.verifyStatus == 1}">
											<span class="img-status">合格</span>
										</c:if>
										<c:if test="${image.verifyStatus != null && image.verifyStatus == 0}">
											<span class="img-status img-unqualified">不合格</span>
										</c:if>
									</li>	
								</c:forEach>
							</ul>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
		<div class="track" >
			<img class="map_marker_icon" src="${baseStatic}images/newcar.png">
			<c:forEach items="${waybill.tracks }" var="track">
				<li class="marker-tip" latitude="${track.latitude }" longitude="${track.longitude }">
					<div class="infoTitle">&nbsp;&nbsp;&nbsp;</div>
					<div class="infoBody">
						<div class="marker-tip-body">
							<b>地址:</b>${track.locations }
						</div>
						<div class="marker-tip-body">
							<b>日期:</b><someprefix:dateTime date="${track.createtime}" pattern="yyyy年MM月dd日 HH:mm:ss" />
						</div>
						<div class="marker-tip-body">
							<b>联系人:</b>${track.user.mobilephone }
						</div>
					</div>
				</li>			
			</c:forEach>
		</div>
	</div>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script type="text/javascript" src="${baseStatic}js/viewer.min.js"></script>
<script type="text/javascript" src="${baseStatic}js/date.extend.js"></script>
<script>
$(document).ready(function(){
	$(".docs-pictures").viewer({
	    fullscreen: false
	});
	$(".img-status").click(function(){
		$(this).siblings('img').trigger('click');
	});
});
</script>
</html>
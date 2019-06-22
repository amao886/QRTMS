<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>物流跟踪-纸质回单-送交客户</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
    <link rel="stylesheet" href="${baseStatic}css/sendcustomer.css?times=${times}"/>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchWaybillForm" action="${basePath}/backstage/coordination/get/searchExceptionPage" method="post">
        <input type="hidden" name="num" value="${search.num}">
        <input type="hidden" name="pageSize" value="20">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">上报日期</label>
                    <div class="input-append date startDate" id="datetimeStart0">
                        <input type="text" class="tex_t z_index" id="deliverStartTime" name="firstTime"
                               value="${search.firstTime}" readonly>
                        <span class="add-on">
								<i class="icon-th"></i>
							</span>
                    </div>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">至</label>
                    <div class="input-append date endDate" id="datetimeEnd0">
                        <input type="text" class="tex_t z_index" id="deliverEndTime" name="secondTime"
                               value="${search.secondTime}" readonly>
                        <span class="add-on">
								<i class="icon-th"></i>
							</span>
                    </div>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">承运人:</label>
                    <input type="hidden" name="assignKey" value="${search.assignKey}">
                    <input type="text" class="tex_t z_index" name="assignName" value="${search.assignName}" readonly>
                    <span class="close-x"><i class="layui-icon">&#x1007;</i></span>
                </div>
                <button type="button" class="fr btn btn-default content-search-btn">查询</button>
            </div>
        </div>
    </form>
    <div class="table-style">
        <table>
            <thead>
            <tr>
                <th width="10%">时间</th>
                <th width="10%">送货单号</th>
                <th width="10%">任务单号</th>
                <th width="10%">承运人</th>
                <th width="10%">发站</th>
                <th width="10%">到站</th>
                <th>异常说明</th>
                <th width="5%">异常图片</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.collection }" var="exception">
                <tr>
                    <td><fmt:formatDate value="${exception.createtime}" pattern="yyyy-MM-dd"/></td>
                    <td>${exception.deliveryNumber}</td>
                    <td><a class="aBtn" href="${basePath}/backstage/trace/findById/${exception.waybillid}">${exception.barcode}</a></td>
                    <td>${exception.assignName}</td>
                    <td>${exception.startStation}</td>
                    <td>${exception.endStation}</td>
                    <td>${exception.content}</td>
                    <td>
                        <c:choose>
                            <c:when test="${exception.images != null && fn:length(exception.images) > 0}">
                                <a href="javascript:;" class="show_images">查看</a>
                                <div style="display: none;">
                                    <div>
                                        <c:forEach items="${exception.images}" var="img">
                                            <img src="${imagePath}${img.path}" style="margin: 10px; max-width: 680px;">
                                        </c:forEach>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <span>&nbsp;&nbsp;</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="page-item">
            <c:if test="${page.collection != null && fn:length(page.collection) > 0}">
                <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}" pages="${page.pages}" total="${page.total}"/>
            </c:if>
        </div>
    </div>
</div>
<input type="hidden" class="startTxt" readonly/>
<input type="hidden" class="endTxt" readonly/>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/fileDownload.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker1.js"></script>
<script type="text/javascript" src="${baseStatic}js/Date.js?time=${times}"></script>
<script src="${baseStatic}js/select.resource.js?times=${times}" ></script>
<script>
    $(document).ready(function () {
        //查询按钮事件
        $(".content-search-btn").on("click", function () {
            $("#searchWaybillForm").submit();
            // init();
        });
        $("input[name='assignName']").on('click', function(){
            var target = $(this), targetKey = $("input[name='assignKey']");
            target.selectUser({
                selectClose: true,
                selectKey: targetKey.val(),
                selectFun: function(model){
                    targetKey.val(model.friendKey);
                    if($.trim(model.remarkName)){
                        target.val(model.remarkName);
                    }else{
                        target.val(model.userName);
                    }
                }
            });
        });
        $('.show_images').on('click', function(){
            var htmls = $(this).next('div').html();
            $.dialog({content: htmls, closeIcon:true, title:'异常图片', boxWidth: 745, useBootstrap:false});
        });
    });
    
    //清空承运人信息
    $('.close-x').on('click',function(){
    	$("input[name='assignName']").val('').attr('placeholder','选择承运人').prev().val('');
    })

    //翻页事件
    $(".pagination a").each(function (i) {
        $(this).on('click', function () {
            $("form input[name='num']").val($(this).attr("num"));
            $("#searchWaybillForm").submit();
        });
    });
</script>
</html>
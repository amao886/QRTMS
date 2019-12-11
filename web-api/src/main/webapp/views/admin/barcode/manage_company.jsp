<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-条码分配</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/inventory.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}bootstrap/bootstrap-select.min.css"/>
    <style>
        .jconfirm .jconfirm-box div.jconfirm-content-pane {
            position: static;
        }

        .jconfirm .jconfirm-box {
            overflow: visible;
        }

        .combo-input.text-input-user {
            padding: 4px 0;
        }
        .tex_t{
            /* height: 26px; */
            line-height: 34px;
        }
        .btn-default{
            color: #333 !important;
            background-color: #fff !important;
            border: 1px #ccc solid !important;
        }
        .btn-primary{
            background: #31acfa !important;
            border: 0 !important;
        }

    </style>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchWaybillForm" class="form-inline" action="${basePath}/admin/barcode/allocated/search" method="post" style="margin-top: 10px; margin-bottom: 10px;">
        <input type="hidden" name="num" value="${results.pageNum}">
        <input type="hidden" name="size" value="10">
        <div class="form-group">
            <select id="company" name="companyId" data-live-search="true" placeholder="企业名称">
                <option value="0" ${empty search.companyId ? "selected='selected'":'' } >企业名称</option>
                <c:forEach items="${companys}" var="company">
                    <option value="${company.id}" ${company.id == search.companyId ? "selected='selected'":'' } >${company.companyName }</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <input type="text" class="form-control" name="startNum" placeholder="二维码开始编号" value="${search.startNum}">
            <input type="text" class="form-control" name="endNum" placeholder="二维码结束编号" value="${search.endNum}">
        </div>
        <button type="button" class="btn btn-primary content-search-btn">查询</button>
    </form>
    <div class="table-style">
        <table class="dtable" id="trackTable">
            <thead>
            <tr>
                <th width="15%">企业名称</th>
                <th width="5%">申请数量</th>
                <th width="5%">已使用</th>
                <th width="23%">二维码开始编号</th>
                <th width="23%">二维码结束编号</th>
                <th width="10%">操作人</th>
                <th width="15%">操作时间</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${results.collection}" var="obj">
                <tr>
                    <td>${obj.companyName}</td>
                    <td>${obj.total}</td>
                    <td>${obj.useNumber}</td>
                    <td>${obj.startNum}</td>
                    <td>${obj.endNum}</td>
                    <td>${obj.unamezn}</td>
                    <td><fmt:formatDate value="${obj.createtime}" pattern="yyyy-MM-dd hh:mm"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${results.collection != null && fn:length(results.collection) > 0}">
            <someprefix:page pageNum="${results.pageNum}" pageSize="${results.pageSize}" pages="${results.pages}"
                             total="${results.total}"/>
        </c:if>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}bootstrap/bootstrap-select.min.js"></script>
<script>
    $(document).ready(function () {
        $('select').selectpicker();
			$(".content-search-btn").on("click",function(){
				 $("input[name='num']").val(1);
				 $("#searchWaybillForm").submit();
			})
            //翻页事件
            $(".pagination a").each(function (i) {
                $(this).on('click', function () {
                    $("form input[name='num']").val($(this).attr("num"));
                    $("#searchWaybillForm").submit();
                });
            });
        }
    )
    ;
</script>
</html>
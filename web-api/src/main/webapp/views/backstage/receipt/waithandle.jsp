<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>物流跟踪-电子回单管理-待处理回单</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/page.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}css/track.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
    <style type="text/css">
        #layui_div_page {
            padding-left: 0;
        }

        .th_first {
            width: 110px;
        }

        .bottom-div {
            border-bottom: 1px solid #e1e5ed;
        }

        .clamp-div {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            text-align: left;
        }

        .clamps-div {
            position: relative;
            line-height: 20px;
            max-height: 45px;
            overflow: hidden;
        }

        .clamps-div::after {
            content: "...";
            position: absolute;
            bottom: 0;
            right: 0;
            padding-left: 5px;
            background: -webkit-linear-gradient(left, transparent, #fff 55%);
            background: -o-linear-gradient(right, transparent, #fff 55%);
            background: -moz-linear-gradient(right, transparent, #fff 55%);
            background: linear-gradient(to right, transparent, #fff 55%);
        }

        .td-div {
            text-align: left;
        }
    </style>
</head>
<body>
<div class="track-content clearfix">
    <form id="search_form" action="${basePath}/backstage/receipt/wait/search" method="post">
        <input type="hidden" name="num" value="${search.num }">
        <input type="hidden" name="size" value="${search.size }">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">查询内容:</label>
                    <input type="text" class="tex_t" name="likeString" placeholder="运单号/送货单号/收货客户/客户地址"
                           value="${search.likeString }" maxlength="30">
                </div>
                <div class="fl col-min input-append date">
                    <label class="labe_l">绑定时间:</label>
                    <div class="input-append date" id="datetimeStart">
                        <input type="text" class="tex_t z_index" name="startTime" value="${search.startTime }" readonly>
                        <span class="add-on">
								<i class="icon-th"></i>
							</span>
                    </div>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">至</label>
                    <div class="input-append date" id="datetimeEnd">
                        <input type="text" class="tex_t z_index" name="endTime" value="${search.endTime }" readonly>
                        <span class="add-on">
								<i class="icon-th"></i>
							</span>
                    </div>
                </div>
            </div>
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">状态:</label>
                    <select class="tex_t selec_t" name="waitFettle">
                        <c:if test="${1 == search.waitFettle}">
                            <option value="0">全部</option>
                            <option value="1" selected>未上传</option>
                            <option value="2">已上传-全部未审核</option>
                            <option value="3">已上传-部分未审核</option>
                            <option value="4">已审核-有不合格</option>
                        </c:if>
                        <c:if test="${2 == search.waitFettle}">
                            <option value="0">全部</option>
                            <option value="1">未上传</option>
                            <option value="2" selected>已上传-全部未审核</option>
                            <option value="3">已上传-部分未审核</option>
                            <option value="4">已审核-有不合格</option>
                        </c:if>
                        <c:if test="${3 == search.waitFettle}">
                            <option value="0">全部</option>
                            <option value="1">未上传</option>
                            <option value="2">已上传-全部未审核</option>
                            <option value="3" selected>已上传-部分未审核</option>
                            <option value="4">已审核-有不合格</option>
                        </c:if>
                        <c:if test="${4 == search.waitFettle}">
                            <option value="0">全部</option>
                            <option value="1">未上传</option>
                            <option value="2">已上传-全部未审核</option>
                            <option value="3">已上传-部分未审核</option>
                            <option value="4" selected>已审核-有不合格</option>
                        </c:if>
                        <c:if test="${search.waitFettle == null || search.waitFettle == 0}">
                            <option value="0" selected>全部</option>
                            <option value="1">未上传</option>
                            <option value="2">已上传-全部未审核</option>
                            <option value="3">已上传-部分未审核</option>
                            <option value="4">已审核-有不合格</option>
                        </c:if>
                    </select>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">项目组:</label>
                    <select class="tex_t selec_t" name="groupid">
                        <option value="0" selected>其他</option>
                        <c:forEach items="${groups}" var="group">
                            <option value="${ group.id}" <c:if test="${group.id == search.groupid}"> selected</c:if>>${group.groupName }</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>
    </form>
    <div class="content_btn">
        <button type="button" class="btn btn-default content-search-btn">查询</button>
    </div>
    <div class="table-style">
        <table>
            <thead>
            <tr>
                <th class="th_first">(任务|送货)单号</th>
                <th width="200px">收货客户</th>
                <th width="200px">收货地址</th>
                <th width="150px">联系人</th>
                <th>要求到货时间</th>
                <th>发货时间</th>
                <th>回单<font size="1">(已审/总数)</font></th>
                <th>待处理类型</th>
                <th>描述</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.collection }" var="waybill">
                <input type="hidden" name="waybill.userid" value="${waybill.userid }">
                <tr>
                    <td>
                        <div class="bottom-div"><a
                                href="${basePath}/backstage/trace/findById/${waybill.id}">${waybill.barcode}</a></div>
                        <div>${waybill.deliveryNumber }</div>
                    </td>
                    <td>${waybill.receiverName}</td>
                    <td>${waybill.receiveAddress}</td>
                    <td>
                        <div class="td-div">${waybill.contactName }
                            <c:if test="${waybill.contactPhone != null && waybill.contactPhone != ''}">
                                (${waybill.contactPhone })
                            </c:if>
                        </div>
                    </td>
                    <td><fmt:formatDate value="${waybill.arrivaltime}" pattern="yyyy-MM-dd HH:mm"/></td>
                    <td><fmt:formatDate value="${waybill.createtime}" pattern="yyyy-MM-dd HH:mm"/></td>
                    <td>${waybill.receiptVerifyCount}/${waybill.receiptCount}</td>
                    <td>
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
                    </td>
                    <td>
                        <c:if test="${waybill.receiptVerifyCount >= 1 && waybill.receiptCount > waybill.receiptVerifyCount}">
                            ${waybill.receiptCount - waybill.receiptVerifyCount}张未审核
                        </c:if>
                        <c:if test="${waybill.receiptVerifyCount >= waybill.receiptCount && waybill.receiptUnqualifyCount > 0}">
                            ${waybill.receiptUnqualifyCount}张不合格
                        </c:if>
                    </td>
                    <td>
                        <a href="${basePath}/backstage/receipt/wait/detail?wkey=${waybill.id}" class="a_link">查看</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${page.collection != null && fn:length(page.collection) > 0}">
            <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}" pages="${page.pages}"
                             total="${page.total}"/>
        </c:if>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="${baseStatic}plugin/js/jquery.mloading.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker.js"></script>
<script>
    $(document).ready(function () {
        //提交查询事件
        $(".content-search-btn").on("click", function () {
            $("#search_form").submit();
        });
        //翻页事件
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                $("form input[name='num']").val($(this).attr("num"));
                $("#search_form").submit();
            });
        });
    });
</script>
</html>
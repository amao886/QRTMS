<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-运单查询</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/js/cityselect/chooseAddress/city.css"/>
    <link rel="stylesheet" href="${baseStatic}css/conveyance.css"/>
</head>
<body>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<div class="track-content">
    <!-- 搜索条件部分 -->
    <div class="check-wrapper">
        <form id="searchConveyanceForm" action="${basePath}/backstage/conveyance/search" method="post">
            <input type="hidden" name="num" value="${search.num }">
            <input type="hidden" name="size" value="${search.size }">
            <div class="selectCondition">
                <select name="groupKey" val="${search.groupKey}">
                    <c:forEach items="${groups}" var="group">
                        <option value="${ group.id}">${group.groupName }</option>
                    </c:forEach>
                    <option value="0">其他</option>
                </select>
                <select name="timeType" val="${search.timeType}">
                    <option value="3">近三天发货任务</option>
                    <option value="7">近一周发货任务</option>
                    <option value="30">近一个月发货任务</option>
                    <option value="0">全部日期</option>
                </select>
                <select name="fettle" val="${search.fettle}">
                    <option value="" selected>正常</option>
                    <option value="0">取消</option>
                </select>
                <ul class="tabSelect">
                    <li>
                        <label class="transport-status active">
                            <input type="radio" name="assignFettle" value="" val="${search.assignFettle}"/>全部
                        </label>
                    </li>
                    <li>
                        <label class="transport-status">
                            <input type="radio" name="assignFettle" value="1" val="${search.assignFettle}"/>已指派
                        </label>
                    </li>
                    <li>
                        <label class="transport-status">
                            <input type="radio" name="assignFettle" value="0" val="${search.assignFettle}"/>未指派
                        </label>
                    </li>
                </ul>
                <span class="more" id="moreCon"><span>更多搜索条件</span><i class="icon"></i></span>
            </div>
            <div class="layui-fluid moreCondition" style="display:none;">
                <div class="layui-row layui-col-space10">
                    <div class="layui-col-md4 layui-col-lg2">
                        <div class="line-part">
                            <input type="text" class="dblock" name="likeString" value="${search.likeString}"
                                   placeholder="任务单号/运单号/送货单号"/>
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg2">
                        <div class="line-part">
                            <input type="text" class="dblock" name="shipperName" value="${search.shipperName}"
                                   placeholder="收货方"/>
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg2">
                        <div class="line-part">
                            <input type="text" class="dblock" name="transporter" value="${search.transporter}"
                                   placeholder="承运人"/>
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg2">
                        <div class="line-part manage-wrapper">
                            <input type="text" class="dblock dizhi_city" name="startStation"
                                   value="${search.startStation}" placeholder="始发地"/>
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg2">
                        <div class="line-part manage-wrapper">
                            <input type="text" class="dblock dizhi_city" name="endStation" value="${search.endStation}"
                                   placeholder="目的地"/>
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg2">
                        <div class="line-part line-btn">
                            <button class="layui-btn layui-btn-normal submit" type="button">查询</button>
                        </div>
                    </div>

                </div>
            </div>
        </form>
    </div>
    <!-- 表格部分 -->
    <div class="table-style">
        <div class="handle-box clearfix">
            <div class="handle-left fl">
                <span>运输规划：</span>
                <button class="layui-btn layui-btn-normal assign-whole">总包指派</button>
                <c:forEach items="${routes}" var="r">
                    <button class="layui-btn layui-btn-primary route-btn" id="${r.id}">${r.routeName}</button>
                </c:forEach>
                <button class="layui-btn bg-green add-route">
                    <i class="layui-icon">&#xe608;</i>更多路由
                </button>
            </div>
            <div class="handle-right fr">
                <button class="layui-btn bg-green share-group">分享到项目组</button>
                <button class="layui-btn bg-red cancle-assign">取消指派</button>
            </div>
        </div>
        <div class="table-style">
            <table class="conveyance-table">
                <thead>
                <tr>
                    <th width="6%"><label class="check_all"><input type="checkbox" class="check_box"/>全选</label></th>
                    <th width="10%">送货单号</th>
                    <th width="10%">运单号</th>
                    <th width="10%">任务单号</th>
                    <th width="7%">发站</th>
                    <th width="7%">到站</th>
                    <th width="8%">发货日期</th>
                    <th width="10%">要求到货时间</th>
                    <th width="8%">运单状态</th>
                    <th width="8%">承运人</th>
                    <th width="8%">指派状态</th>
                    <th width="8%">操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.collection}" var="c">
                    <c:if test="${c.conveyanceFettle == 0}">
                        <tr>
                            <td style="background-color: #D9D9D9"><input type="checkbox" class="check_box" disabled
                                                                         value="${c.id}"><i
                                    class="layui-icon">&#xe622;</i></td> <!-- &#xe622; -->
                            <td style="background-color: #D9D9D9">${c.waybill.deliveryNumber}</td>
                            <td style="background-color: #D9D9D9">${c.conveyanceNumber}</td>
                            <td style="background-color: #D9D9D9"><a class="aBtn"
                                                                     href="${basePath}/backstage/trace/findById/${c.waybill.id}">${c.barcode}</a>
                            </td>
                            <td style="background-color: #D9D9D9">${c.simpleStartStation}</td>
                            <td style="background-color: #D9D9D9">${c.simpleEndStation}</td>
                            <td style="background-color: #D9D9D9">
                                <c:if test="${c.waybill.deliveryTime != null}">
                                    <someprefix:dateTime date="${c.waybill.deliveryTime}" pattern="yyyy-MM-dd"/>
                                </c:if>
                                <c:if test="${c.waybill.deliveryTime == null}">
                                    未发货
                                </c:if>
                            </td>
                            <td style="background-color: #D9D9D9">
                                <c:if test="${c.waybill.arrivaltime != null}">
                                    <someprefix:dateTime date="${c.waybill.arrivaltime}" pattern="yyyy-MM-dd HH:mm"/>
                                </c:if>
                                <c:if test="${c.waybill.arrivaltime == null}">
                                    无要求
                                </c:if>
                            </td>
                            <c:choose>
                                <c:when test="${c.conveyanceFettle == 10}">
                                    <td>未发货</td>
                                </c:when>
                                <c:when test="${c.conveyanceFettle == 20}">
                                    <td>待运输</td>
                                </c:when>
                                <c:when test="${c.conveyanceFettle == 30}">
                                    <td>运输中</td>
                                </c:when>
                                <c:when test="${c.conveyanceFettle == 35}">
                                    <td>已送达</td>
                                </c:when>
                            </c:choose>

                            <td style="background-color: #D9D9D9">
                                <c:if test="${c.contactName != null}">
                                    ${c.contactName}
                                </c:if>
                            </td>
                            <td style="background-color: #D9D9D9">
                                <c:choose>
                                    <c:when test="${c.assignFettle == 0}">未指派</c:when>
                                    <c:otherwise>已指派</c:otherwise>
                                </c:choose>
                            </td>
                            <td style="background-color: #D9D9D9">
                                已取消
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${c.conveyanceFettle > 0}">
                        <tr>
                            <td>
                                <input type="checkbox" class="check_box check_box_item" value="${c.id}">
                                <i class="layui-icon">&#xe622;</i>
                            </td> <!-- &#xe622; -->
                            <td>${c.waybill.deliveryNumber}</td>
                            <td class="in_block">
                                <input type="text" class="input_txt" value="${c.conveyanceNumber}" cid="${c.id}" val="${c.conveyanceNumber}" readonly/>
                                <i class="layui-icon edit-number">&#xe642;</i>
                            </td>
                            <td class="blue_color">
                                <a class="aBtn" href="${basePath}/backstage/trace/findById/${c.waybill.id}">${c.barcode}</a>
                            </td>
                            <td>${c.simpleStartStation}</td>
                            <td>${c.simpleEndStation}</td>
                            <td>
                                <c:if test="${c.waybill.deliveryTime != null}">
                                    <someprefix:dateTime date="${c.waybill.deliveryTime}" pattern="yyyy-MM-dd"/>
                                </c:if>
                                <c:if test="${c.waybill.deliveryTime == null}">
                                    未发货
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${c.waybill.arrivaltime != null}">
                                    <someprefix:dateTime date="${c.waybill.arrivaltime}" pattern="yyyy-MM-dd HH:mm"/>
                                </c:if>
                                <c:if test="${c.waybill.arrivaltime == null}">
                                    无要求
                                </c:if>
                            </td>
                            <c:choose>
                                <c:when test="${c.conveyanceFettle == 10}">
                                    <td>未发货</td>
                                </c:when>
                                <c:when test="${c.conveyanceFettle == 20}">
                                    <td>待运输</td>
                                </c:when>
                                <c:when test="${c.conveyanceFettle == 30}">
                                    <td>运输中</td>
                                </c:when>
                                <c:when test="${c.conveyanceFettle == 35}">
                                    <td>已送达</td>
                                </c:when>
                            </c:choose>
                            <td class="in_block">
                                <c:if test="${c.owner != null}">
                                    <input type="text" class="input_txt" data-id="${c.id}" data-ukey="${c.ownerKey}" value="${c.owner.unamezn}" readonly/>
                                </c:if>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${c.assignFettle == 0}">未指派</c:when>
                                    <c:otherwise>已指派</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${c.assignFettle > 0}">
                                        <c:if test="${c.conveyanceFettle < 35}">
                                            <a href="javascript:;" id="${c.id}" class="aBtn blue_color cancle-assign-single">取消指派</a>
                                        </c:if>
                                        <c:if test="${c.conveyanceFettle >= 35}">
                                            已到货
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="javascript:;" id="${c.id}" class="aBtn blue_color assign-single">指派</a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:if>
                    <c:forEach items="${c.children}" var="cl">
                        <!-- 这个上一个tr的子运单 -->
                        <c:if test="${cl.conveyanceFettle == 0}">
                            <tr class="sub_tr">
                                <td></td>
                                <td style="background-color: #D9D9D9">${cl.waybill.deliveryNumber}</td>
                                <td style="background-color: #D9D9D9">${cl.conveyanceNumber}</td>
                                <td style="background-color: #D9D9D9">
                                    <a class="aBtn" href="${basePath}/backstage/trace/findById/${cl.waybill.id}">${cl.barcode}</a>
                                </td>
                                <td style="background-color: #D9D9D9">${cl.simpleStartStation}</td>
                                <td style="background-color: #D9D9D9">${cl.simpleEndStation}</td>
                                <td style="background-color: #D9D9D9">
                                    <c:if test="${cl.waybill.deliveryTime != null}">
                                        <someprefix:dateTime date="${cl.waybill.deliveryTime}" pattern="yyyy-MM-dd"/>
                                    </c:if>
                                    <c:if test="${cl.waybill.deliveryTime == null}">
                                        未发货
                                    </c:if>
                                </td>
                                <td style="background-color: #D9D9D9">
                                    <c:if test="${cl.waybill.arrivaltime != null}">
                                        <someprefix:dateTime date="${cl.waybill.arrivaltime}" pattern="yyyy-MM-dd HH:mm"/>
                                    </c:if>
                                    <c:if test="${cl.waybill.arrivaltime == null}">
                                        无要求
                                    </c:if>
                                </td>
                                <c:choose>
                                    <c:when test="${cl.conveyanceFettle == 10}">
                                        <td>未发货</td>
                                    </c:when>
                                    <c:when test="${cl.conveyanceFettle == 20}">
                                        <td>待运输</td>
                                    </c:when>
                                    <c:when test="${cl.conveyanceFettle == 30}">
                                        <td>运输中</td>
                                    </c:when>
                                    <c:when test="${cl.conveyanceFettle == 35}">
                                        <td>已送达</td>
                                    </c:when>
                                </c:choose>
                                <td style="background-color: #D9D9D9">
                                    <c:if test="${cl.contactName != null}">
                                        ${cl.contactName}
                                    </c:if>
                                </td>
                                <td style="background-color: #D9D9D9">已指派</td>
                                <td style="background-color: #D9D9D9">
                                    已取消
                                </td>
                            </tr>
                        </c:if>
                        <c:if test="${cl.conveyanceFettle > 0}">
                            <tr class="sub_tr">
                                <td></td>
                                <td>${cl.waybill.deliveryNumber}</td>
                                <td class="in_block">
                                    <input type="text" class="input_txt" value="${cl.conveyanceNumber}" cid="${cl.id}" val="${cl.conveyanceNumber}" readonly/>
                                    <i class="layui-icon edit-number">&#xe642;</i>
                                </td>
                                <td class="blue_color">
                                    <a class="aBtn" href="${basePath}/backstage/trace/findById/${cl.waybill.id}">${cl.barcode}</a>
                                </td>
                                <td>${cl.simpleStartStation}</td>
                                <td>${cl.simpleEndStation}</td>
                                <td>
                                    <c:if test="${cl.waybill.deliveryTime != null}">
                                        <someprefix:dateTime date="${cl.waybill.deliveryTime}" pattern="yyyy-MM-dd"/>
                                    </c:if>
                                    <c:if test="${cl.waybill.deliveryTime == null}">
                                        未发货
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${cl.waybill.arrivaltime != null}">
                                        <someprefix:dateTime date="${cl.waybill.arrivaltime}" pattern="yyyy-MM-dd HH:mm"/>
                                    </c:if>
                                    <c:if test="${cl.waybill.arrivaltime == null}">
                                        无要求
                                    </c:if>
                                </td>
                                <c:choose>
                                    <c:when test="${cl.conveyanceFettle == 10}">
                                        <td>未发货</td>
                                    </c:when>
                                    <c:when test="${c.conveyanceFettle == 20}">
                                        <td>待运输</td>
                                    </c:when>
                                    <c:when test="${c.conveyanceFettle == 30}">
                                        <td>运输中</td>
                                    </c:when>
                                    <c:when test="${c.conveyanceFettle == 35}">
                                        <td>已送达</td>
                                    </c:when>
                                </c:choose>
                                <td class="in_block">
                                    <c:if test="${cl.owner != null}">
                                        <input type="text" class="input_txt" data-id="${cl.id}" data-ukey="${cl.ownerKey}" value="${cl.owner.unamezn}" readonly/>
                                    </c:if>
                                    <c:if test="${cl.conveyanceFettle <= 20 && cl.assignFettle <= 0}">
                                        <i class="layui-icon click_edit_carrier" style="float: right">&#xe642;</i>
                                    </c:if>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${cl.assignFettle == 0}">未指派</c:when>
                                        <c:otherwise>已指派</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <!--
									<c:if test="${cl.conveyanceFettle < 35}">
										<c:if test="${cl.ownerKey - c.ownerKey != 0}">
											<a href="javascript:;" id="${cl.id}" class="aBtn blue_color cancle-assign-single">取消指派</a>
										</c:if>
										<c:if test="${cl.ownerKey - c.ownerKey == 0}">
											<a href="javascript:;" id="${cl.id}" class="aBtn blue_color assign-single">指派</a>
										</c:if>
									</c:if>
									-->
                                    <c:if test="${cl.conveyanceFettle >= 35}">
                                        已到货
                                    </c:if>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="page-item">
            <c:if test="${page.collection != null && fn:length(page.collection) > 0}">
                <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}" pages="${page.pages}" total="${page.total}"/>
            </c:if>
        </div>
    </div>

    <!-- 整包指派弹框部分开始 -->
    <div class="model_box whole-assign" style="width:540px;display: none;">
        <h1 class="title-h1">指派承运商</h1>
        <input type="hidden" name="assignKey"/>
        <div class="carrier_box">
            <div class="line-part large-tag">
                <span class="float-tag">承运商名称：</span>
                <div class="text-tag">
                    <input type="text" class="sub-tag c-company" disabled/>
                </div>
            </div>
            <div class="line-part large-tag">
                <span class="float-tag">联系人：</span>
                <div class="text-tag"><input type="text" class="sub-tag c-name" disabled/></div>
            </div>
            <div class="line-part large-tag">
                <span class="float-tag">联系方式：</span>
                <div class="text-tag"><input type="text" class="sub-tag c-mobile" disabled/></div>
            </div>
        </div>
        <div class="driver_box change-show" style="display:none;">
            <div class="line-part large-tag">
                <span class="float-tag">司机姓名：</span>
                <div class="text-tag">
                    <input type="text" class="sub-tag d-name" disabled/>
                </div>
            </div>
            <div class="line-part large-tag">
                <span class="float-tag">联系方式：</span>
                <div class="text-tag"><input type="text" class="sub-tag d-mobile" disabled/></div>
            </div>
        </div>
    </div>
    <!-- 整包指派弹框部分结束 -->

    <!-- 运单指派确认弹框部分开始 -->
    <div class="model_box waybill_box route-assign" style="width:540px;display:none;">
        <h1>运单指派确认</h1>
        <p class="route_tips">已选择<span style="color: #e60000;font-weight: bold;">10</span>张运单,指派路由如下:</p>
        <div class="box_content">
            <div class="route_box">
                <!-- 起点 -->
                <div class="route_start">
                    <div class="top clearfix">
                        <span class="route_circle fl">起</span>
                        <p>宜春市</p>
                    </div>
                    <div class="bottom">
                        <div class="line-part">
                            <span class="float-tag">司机姓名：</span>
                            <div class="text-tag">老吴吴无</div>
                        </div>
                    </div>
                </div>
                <!-- 中转点 -->
                <div class="route_context_middle"></div>
                <!-- 终点 -->
                <div class="route_end">
                    <div class="top">
                        <span class="route_circle fl">终</span>
                        <p class="station">金山寺</p>
                    </div>
                </div>
            </div>
        </div>
        <p class="route_warn"><i class="layui-icon">&#xe60b;</i>请仔细核实以上信息，确认提交后，运单将指派给下游承运商或司机 </p>
    </div>
    <!-- 运单指派确认弹框部分结束 -->

    <!-- 修改承运人 -->
    <!-- 整包指派弹框部分开始 -->
    <div class="model_box edit-assign-box" style="width:540px;display: none;">
        <div class="carrier_box">
            <input type="hidden" name="id">
            <input type="hidden" name="ownerKey">
            <div class="line-part org">
                <span class="float-tag">承运商:</span>
                <div class="text-tag">
                    <input type="text" name="organization" class="sub-tag" readonly/>
                </div>
            </div>
            <div class="line-part">
                <span class="float-tag">联系人:</span>
                <div class="text-tag"><input type="text" name="contactName" class="sub-tag" readonly/></div>
            </div>
            <div class="line-part">
                <span class="float-tag">联系电话:</span>
                <div class="text-tag"><input type="text" name="contactPhone" class="sub-tag" readonly/></div>
            </div>
        </div>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}js/select.resource.js?times=${times}"></script>
<script src="${baseStatic}plugin/js/cityselect/chooseAddress/area.js"></script>
<script src="${baseStatic}plugin/js/cityselect/chooseAddress/provinceSelect.js"></script>
<script src="${baseStatic}plugin/js/jquery.mloading.js"></script>
<script src="${baseStatic}plugin/js/jquery.cookie.js"></script>
<script>
    function selects() {
        var conveyanceKeys = [];
        $("td").find('input:checkbox:checked').each(function () {
            conveyanceKeys.push($(this).val());
        });
        if (!conveyanceKeys || conveyanceKeys.length <= 0) {
            $.util.warning("至少选择一个运单");
        }
        return conveyanceKeys;
    }

    function itemDiv(u, r) {
        if (!u) {
            return "<div class=\"line-part large-tag\"><button class=\"layui-btn layui-btn-danger add-user\" id=\"" + r.id + "\"><i class=\"layui-icon\">&#xe608;</i>选择司机/承运商</button></div>";
        }
        if (u.type === 2) {//0:普通 1：货主 2：承运商 3：司机
            return "<div class=\"line-part\"><span class=\"float-tag\">承运商:</span><div class=\"text-tag\">" + u.company + "</div></div><div class=\"line-part\"><span class=\"float-tag\">司机姓名:</span><div class=\"text-tag\">" + u.unamezn + "</div></div><div class=\"line-part\"><span class=\"float-tag\">联系方式:</span><div class=\"text-tag\">" + u.mobilephone + "</div></div>";
        } else {
            return "<div class=\"line-part\"><span class=\"float-tag\">司机姓名:</span><div class=\"text-tag name\">" + u.unamezn + "</div></div><div class=\"line-part\"><span class=\"float-tag\">联系方式:</span><div class=\"text-tag\">" + u.mobilephone + "</div></div>";
        }
    }

    function buildRouteDiv(body, station, v) {

        if (v.lineType === 1) {//起点
            var c = body.find(".route_start");
            c.find(".top p").text(station.simpleStartStation);
            c.find(".bottom").html(itemDiv(v.user, v));
        }
        if (v.lineType === 2) {//中转点
            body.find(".route_context_middle").append("<div class=\"route_middle\"><div class=\"top\"><span class=\"route_circle fl\">转</span><p class=\"station\">" + v.simpleStation + "</p></div><div class=\"bottom\">" + itemDiv(v.user, v) + "</div></div>");
        }
        if (v.lineType === 3) {//终点
            body.find(".route_end .top p").text(station.simpleEndStation);
        }
    }

    var submit = function (num) {
        $("form input[name='num']").val(num);
        $("#searchConveyanceForm").submit();
    };
    $(document).ready(function () {
        $("#searchConveyanceForm").find("select").on('change', function () {
            submit(1);
        });
        $("#searchConveyanceForm").find(":radio").click(function () {
            submit(1);
        });
        $("#searchConveyanceForm").find(".submit").click(function () {
            submit(1);
        });
        //翻页事件
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                submit($(this).attr("num"));
            });
        });
        $('select, :radio').each(function () {
            var target = $(this), val = target.attr("val");
            if (val && target.is('select')) {
                target.val(val);
            }
            if (val && target.is(':radio')) {
                if (target.val() == val) {
                    target.attr("checked", true);
                    target.parent().addClass('active')
                } else {
                    target.attr("checked", false);
                    target.parent().removeClass('active');
                }
            }
        });
        $('.check_all .check_box').on('click', function () {
            if (this.checked) {
                $(".check_box_item").each(function () {
                    this.checked = true;
                });
            } else {
                $(".check_box_item").each(function () {
                    this.checked = false;
                });
            }
        });

        $('.click_edit_carrier').on('click', function () {
            var target = $(this).prev();
            target.selectUser({
                selectClose: true,
                selectKey: target.data('ukey'),
                selectFun: function (item) {
                    var html = $(".edit-assign-box").clone().show();
                    $.util.form('修改承运人', html, function (model) {
                        var editBody = this.$body;
                        editBody.find("input[name='id']").val(target.data('id'));
                        editBody.find("input[name='ownerKey']").val(item.friendKey);
                        var $organization = editBody.find('.org');
                        if (item.company) {
                            $organization.show().find("input[name='organization']").val(item.company);
                        } else {
                            $organization.hide();
                        }
                        editBody.find("input[name='contactName']").val(item.remarkName);
                        editBody.find("input[name='contactPhone']").val(item.mobile);
                    }, function () {
                        var $editBody = this.$body;
                        var parmas = {};
                        $editBody.find('input').each(function () {
                            parmas[this.name] = this.value;
                        });
                        $.util.json(base_url + "/backstage/conveyance/edit/assign", parmas, function (data) {
                            if (!data.success) {//处理返回结果
                                $.util.danger('修改异常', data.message);
                            } else {
                                $.util.success('修改成功');
                                console.log(target);
                                target.val(item.remarkName);
                            }
                        });
                    });
                }
            });
        });
        //input框失去焦点时
        $('.input_txt').on('blur', function () {
            $(this).css({
                'color': '#636363',
                'readonly': 'readonly'
            })
        });

        //tab切换
        $('.transport-status').click(function () {
            $('.transport-status').removeClass('active');
            $(this).addClass('active');
        });

        //判断搜索条件是否展示
        if ($.cookie('openhook') == 1) {
            $('.moreCondition').show();
            $('#moreCon').addClass('open-hook').children('span').text('收起更多').next().addClass('up');
        }

        //更多搜索条件显示
        $('#moreCon').on('click', function () {
            if ($(this).hasClass('open-hook')) {
                $('.moreCondition').hide();
                $(this).removeClass('open-hook').children('span').text('更多搜索条件').next().removeClass('up');
                $.cookie('openhook', '2');
            } else {
                $('.moreCondition').show();
                $(this).addClass('open-hook').children('span').text('收起更多').next().addClass('up');
                $.cookie('openhook', '1');
            }

        });
        var wholeAssign = function (conveyanceKeys) {
            $('.assign-whole').selectUser({
                selectClose: true, selectFun: function (item) {
                    var html = $(".whole-assign").clone().show();
                    $.util.form('整包指派', html, function (model) {
                        var editBody = this.$body;
                        editBody.find("input:hidden[name='assignKey']").val(item.friendKey);
                        if (item.type == 2) {//承运商
                            editBody.find('.title-h1').text('指派承运商');
                            editBody.find('.driver_box').hide();
                            editBody.find('.carrier_box').show();
                            editBody.find('.carrier_box .c-company').val(item.company);
                            editBody.find('.carrier_box .c-name').val(item.remarkName);
                            editBody.find('.carrier_box .c-mobile').val(item.remarkMobile);
                        } else {
                            editBody.find('.title-h1').text('指派承运人');
                            editBody.find('.driver_box').show();
                            editBody.find('.carrier_box').hide();
                            editBody.find('.driver_box .d-name').val(item.remarkName);
                            editBody.find('.driver_box .d-mobile').val(item.remarkMobile);
                        }
                    }, function () {
                        var editBody = this.$body;
                        var parmas = {
                            'assignKey': editBody.find("input:hidden[name='assignKey']").val(),
                            'conveyanceKeys': conveyanceKeys.join(',')
                        }
                        if (parmas.assignKey == null || parmas.assignKey <= 0) {
                            $.util.warning("请选择一位承运人");
                            return;
                        }
                        $.util.json(base_url + "/backstage/conveyance/assign/whole", parmas, function (data) {
                            if (data.success) {//处理返回结果
                                $.util.alert("操作提示", data.message, function () {
                                    submit(1);
                                });
                            } else {
                                $.util.error(data.message);
                            }
                        }, 'json');
                        return false;
                    });
                }
            });
        }
        var cancelAssign = function (conveyanceKeys) {
            var parmas = {'conveyanceKeys': conveyanceKeys.join(',')};
            $.util.confirm('取消指派', '确定要取消指派么?', function () {
                $.util.json(base_url + "/backstage/conveyance/cancel/assign", parmas, function (data) {
                    if (data.success) {//处理返回结果
                        $.util.success(data.message, function () {
                            submit(1);
                        });
                    } else {
                        $.util.error(data.message);
                    }
                });
            });
        };
        var selectAssign = function (conveyanceKeys, routeKey) {
            var parmas = {'routeKey': routeKey, 'conveyanceKeys': conveyanceKeys.join(',')};
            $.util.json(base_url + "/backstage/conveyance/assign/pre", parmas, function (data) {
                if (data.success) {//处理返回结果
                    var html = $(".route-assign").clone().show();
                    $.util.form($(this).text(), html, function (model) {
                        var editBody = this.$body;
                        editBody.find("h1").text('运单指派确认-' + data.line.routeName);
                        editBody.find(".route_tips span").text(conveyanceKeys.length);
                        $.each(data.line.routeLines, function (i, v) {
                            console.log(v);
                            buildRouteDiv(editBody, data.station, v);
                        });
                        editBody.find(".add-user").on('click', function () {
                            var target = $(this);
                            $(this).selectUser({
                                selectClose: true, selectFun: function (item) {
                                    var parmas = {nodeKey: target.attr("id"), userKey: item.friendKey};
                                    $.util.json(base_url + "/backstage/route/update/user", parmas, function (data) {
                                        if (data.success) {//处理返回结果
                                            target.parents('.bottom').html(itemDiv({
                                                mobilephone: item.mobile,
                                                company: item.company,
                                                type: item.type,
                                                unamezn: item.userName
                                            }));
                                        } else {
                                            $.util.error(data.message);
                                        }
                                    });
                                }
                            });
                        });
                    }, function () {
                        var editBody = this.$body;
                        $.util.json(base_url + "/backstage/conveyance/assign/save", parmas, function (data) {
                            if (data.success) {//处理返回结果
                                $.util.alert("操作提示", data.message, function () {
                                    submit(1);
                                });
                            } else {
                                $.util.error(data.message);
                            }
                        });
                        return false;
                    });
                } else {
                    $.util.error(data.message);
                }
            });
        };

        $('.assign-whole').on('click', function () {
            var conveyanceKeys = selects();
            if (conveyanceKeys.length > 0) {
                wholeAssign(conveyanceKeys);
            }
        });
        $('.assign-single').on('click', function () {
            var conveyanceKeys = [$(this).attr('id')];
            if (conveyanceKeys.length > 0) {
                wholeAssign(conveyanceKeys);
            }
        });

        $('.route-btn').on('click', function () {
            var conveyanceKeys = selects();
            if (conveyanceKeys.length > 0) {
                selectAssign(conveyanceKeys, $(this).attr("id"));
            }
        });
        $('.share-group').on('click', function () {
            var conveyanceKeys = selects();
            if (conveyanceKeys.length <= 0) {
                return;
            }
            var cloneGroup = $("select[name='groupKey']").clone();
            cloneGroup.find("option[value='0']").remove();
            console.log(cloneGroup.size());
            if (cloneGroup.size() <= 0) {
                $.util.warning("请先加入一个项目组");
                return;
            }
            var html = "<div class=\"line-part\"><span class=\"float-tag\">项目组:</span><div class=\"text-tag\"></div></div>";
            $.util.form('分享到项目组', html, function (model) {
                var editBody = this.$body;
                editBody.find(".text-tag").append(cloneGroup);
            }, function () {
                var editBody = this.$body;
                var groupId = editBody.find("select[name='groupKey']").val();
                if (!groupId || groupId <= 0) {
                    $.util.warning("请选择一个项目组");
                    return false;
                }
                var parmas = {'groupKey': groupId, 'conveyanceKeys': conveyanceKeys.join(',')};
                $.util.json(base_url + "/backstage/conveyance/share/group", parmas, function (data) {
                    if (data.success) {//处理返回结果
                        $.util.success("操作提示", function () {
                            submit(1);
                        }, 3000);
                    } else {
                        $.util.error(data.message);
                    }
                });
            });
        });
        $('.edit-number').on('click', function () {
            var target = $(this).prev();
            var html = "<form action=\"\" style=\"width: 350px;\"><div class=\"form-group\"><input type=\"text\" placeholder=\"运单号\" class=\"name form-control\" value=\"" + target.val() + "\"/></div></form>";
            $.util.form('修改运单号', html, function () {
                var editbody = this.$body;
                editbody.find('input').val(target.val());
            }, function () {
                var editbody = this.$body;
                var val = editbody.find('.name').val();
                if (!val || target.val() === val) {
                    return false;
                }
                var parmas = {'number': val, 'conveyanceKey': target.attr('cid')};
                $.util.json(base_url + "/backstage/conveyance/edit/number", parmas, function (data) {
                    if (!data.success) {//处理返回结果
                        $.util.danger('修改异常', data.message);
                    } else {
                        $.util.success("操作提示", null, 3000);
                        target.val(val);
                    }
                });
            });
        });
        $('.add-route').on('click', function () {
            $(this).selectRoute({
                selectClose: true, selectFun: function (item) {
                    var conveyanceKeys = selects();
                    if (conveyanceKeys.length > 0) {
                        selectAssign(conveyanceKeys, item.id);
                    }
                }
            });
        });
        $('.cancle-assign').on('click', function () {
            var conveyanceKeys = selects();
            if (conveyanceKeys.length > 0) {
                cancelAssign(conveyanceKeys);
            }
        });
        $('.cancle-assign-single').on('click', function () {
            var conveyanceKeys = [$(this).attr('id')];
            if (conveyanceKeys.length > 0) {
                cancelAssign(conveyanceKeys);
            }
        });
    });

    //获得焦点
    function setFocus(obj) {
        var t = $(obj).val();
        $(obj).val("").focus().val(t);
    };
</script>
</html>
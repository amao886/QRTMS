<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-任务单管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
    <link rel="stylesheet" href="${baseStatic}css/addtask.css?times=${times}"/>
    <style type="text/css">

    </style>
</head>
<body>
<div class="track-content">
    <form id="searchWaybillForm" action="${basePath}/backstage/trace/search" method="post">
        <div class="content-search" id="myDataFrom">
            <input type="hidden" value="${waybill.id}" id="waybillId"/>
            <div class="clearfix">
                <span class="fl"><strong>发货人信息</strong></span>
                <div class="inputRight">
                    <div class="fl col-mid">
                        <label class="labe_l"><em>*</em>发货人:</label>
                        <div class="inputBox">
                            <input type="text" class="tex_t" name="shipperName" placeholder="请输入发货人"
                                   value="${waybill.shipperName}" maxlength="15">
                        </div>
                    </div>
                    <div class="fl col-mid">
                        <label class="labe_l">项目组:</label>
                        <div class="inputBox">
                            <select id="select_group" class="tex_t selec_t" name="groupId">
                                <option value="">--请选择项目组--</option>
                                <c:forEach items="${groups}" var="group">
                                    <c:if test="${group.id == waybill.groupid}">
                                        <option value="${group.id}" selected>${group.groupName}</option>
                                    </c:if>
                                    <c:if test="${group.id != waybill.groupid}">
                                        <option value="${group.id}">${group.groupName }</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="fl col-mid">
                        <label class="labe_l">送货单号</label>
                        <div class="inputBox">
                            <input type="text" class="tex_t" name="deliveryNumber" placeholder="请输入送货单号" maxlength="20" value="${waybill.deliveryNumber}">
                        </div>
                    </div>
                </div>
            </div>
            <div class="clearfix topLine">
                <span class="fl"><strong>收货客户</strong></span>
                <div class="inputRight" id="customFormBox">
                    <input type="hidden" id="customerId"/>
                    <div class="clearfix">
                        <div class="fl col-mid">
                            <label class="labe_l"><em>*</em>收货客户:</label>
                            <div class="inputBox">
                                <input type="text" id="receiverName" class="tex_t" name="receiverName"
                                       placeholder="请输入收货客户名称"
                                       maxlength="15" value="${waybill.receiverName}">
                            </div>
                        </div>
                        <div class="fl col-mid">
                            <label class="labe_l"><em>*</em>联系人:</label>
                            <div class="inputBox">
                                <input type="text" id="contactName" class="tex_t" name="contactName"
                                       placeholder="请输入联系人姓名"
                                       maxlength="15" value="${waybill.contactName}">
                            </div>
                        </div>
                        <div class="fl col-mid">
                            <button type="button" class="btn btn-default" id="chooseCustomer">选择客户</button>
                        </div>
                    </div>
                    <div class="clearfix">
                        <div class="fl col-mid">
                            <label class="labe_l"><em>*</em>联系电话:</label>
                            <div class="inputBox">
                                <input type="text" id="contactPhone" class="tex_t" name="contactPhone"
                                       placeholder="请输入联系人电话"
                                       maxlength="15" value="${waybill.contactPhone}">
                            </div>
                        </div>
                        <div class="fl col-big">
                            <label class="labe_l"><em>*</em>收货地址:</label>
                            <div class="inputBox">
                                <input type="text" id="addrDtl" class="tex_t" name="addrDtl" placeholder="详细地址"
                                       maxlength="25"
                                       style="width:70%;" value="${waybill.receiveAddress}">
                            </div>
                        </div>
                    </div>
                    <!--<div class="fl col-mid">
                        <input type="text" class="tex_t" name="customer" placeholder="详细地址"  maxlength="15">
                    </div>-->
                    <div class="clearfix">
                        <div class="fl col-mid input-append date">
                            <label class="labe_l">要求到货时间:</label>
                            <div class="inputBox">
                                <select id="arriveDay" name="arriveDay" class="tex_t" style="width:48.5%;">
                                    <option value="" <c:if test="${waybill.arriveDay < 0}">selected</c:if>>
                                        请选择第几天
                                    </option>
                                    <option value="0" <c:if test="${waybill.arriveDay == 0}">selected</c:if>>
                                        当天
                                    </option>
                                    <option value="1" <c:if test="${waybill.arriveDay == 1}">selected</c:if>>
                                        第1天
                                    </option>
                                    <option value="2" <c:if test="${waybill.arriveDay == 2}">selected</c:if>>
                                        第2天
                                    </option>
                                    <option value="3" <c:if test="${waybill.arriveDay == 3}">selected</c:if>>
                                        第3天
                                    </option>
                                    <option value="4" <c:if test="${waybill.arriveDay == 4}">selected</c:if>>
                                        第4天
                                    </option>
                                    <option value="5" <c:if test="${waybill.arriveDay == 5}">selected</c:if>>
                                        第5天
                                    </option>
                                    <option value="6" <c:if test="${waybill.arriveDay == 6}">selected</c:if>>
                                        第6天
                                    </option>
                                    <option value="7" <c:if test="${waybill.arriveDay == 7}">selected</c:if>>
                                        第7天
                                    </option>
                                    <option value="8" <c:if test="${waybill.arriveDay == 8}">selected</c:if>>
                                        第8天
                                    </option>
                                    <option value="9" <c:if test="${waybill.arriveDay == 9}">selected</c:if>>
                                        第9天
                                    </option>
                                    <option value="10" <c:if test="${waybill.arriveDay == 10}">selected</c:if>>
                                        第10天
                                    </option>
                                    <option value="11" <c:if test="${waybill.arriveDay == 11}">selected</c:if>>
                                        第11天
                                    </option>
                                    <option value="12" <c:if test="${waybill.arriveDay == 12}">selected</c:if>>
                                        第12天
                                    </option>
                                    <option value="13" <c:if test="${waybill.arriveDay == 13}">selected</c:if>>
                                        第13天
                                    </option>
                                    <option value="14" <c:if test="${waybill.arriveDay == 14}">selected</c:if>>
                                        第14天
                                    </option>
                                    <option value="15" <c:if test="${waybill.arriveDay == 15}">selected</c:if>>
                                        第15天
                                    </option>
                                    <option value="16" <c:if test="${waybill.arriveDay == 16}">selected</c:if>>
                                        第16天
                                    </option>
                                    <option value="17" <c:if test="${waybill.arriveDay == 17}">selected</c:if>>
                                        第17天
                                    </option>
                                    <option value="18" <c:if test="${waybill.arriveDay == 18}">selected</c:if>>
                                        第18天
                                    </option>
                                    <option value="19" <c:if test="${waybill.arriveDay == 19}">selected</c:if>>
                                        第19天
                                    </option>
                                    <option value="20" <c:if test="${waybill.arriveDay == 20}">selected</c:if>>
                                        第20天
                                    </option>
                                    <option value="21" <c:if test="${waybill.arriveDay == 21}">selected</c:if>>
                                        第21天
                                    </option>
                                    <option value="22" <c:if test="${waybill.arriveDay == 22}">selected</c:if>>
                                        第22天
                                    </option>
                                    <option value="23" <c:if test="${waybill.arriveDay == 23}">selected</c:if>>
                                        第23天
                                    </option>
                                    <option value="24" <c:if test="${waybill.arriveDay == 24}">selected</c:if>>
                                        第24天
                                    </option>
                                    <option value="25" <c:if test="${waybill.arriveDay == 25}">selected</c:if>>
                                        第25天
                                    </option>
                                    <option value="26" <c:if test="${waybill.arriveDay == 26}">selected</c:if>>
                                        第26天
                                    </option>
                                    <option value="27" <c:if test="${waybill.arriveDay == 27}">selected</c:if>>
                                        第27天
                                    </option>
                                    <option value="28" <c:if test="${waybill.arriveDay == 28}">selected</c:if>>
                                        第28天
                                    </option>
                                    <option value="29" <c:if test="${waybill.arriveDay == 29}">selected</c:if>>
                                        第29天
                                    </option>
                                    <option value="30" <c:if test="${waybill.arriveDay == 30}">selected</c:if>>
                                        第30天
                                    </option>
                                    <option value="31" <c:if test="${waybill.arriveDay == 31}">selected</c:if>>
                                        第31天
                                    </option>
                                </select>
                                <select id="arriveHour" name="arriveHour" class="tex_t" style="width:48.5%;">
                                    <option value="" <c:if test="${waybill.arriveHour < 0}">selected</c:if>>请选择时间点
                                    </option>
                                    <option value="1" <c:if test="${waybill.arriveHour == 1}">selected</c:if>>
                                        凌晨1点
                                    </option>
                                    <option value="2" <c:if test="${waybill.arriveHour == 2}">selected</c:if>>
                                        2点
                                    </option>
                                    <option value="3" <c:if test="${waybill.arriveHour == 3}">selected</c:if>>
                                        3点
                                    </option>
                                    <option value="4" <c:if test="${waybill.arriveHour == 4}">selected</c:if>>
                                        4点
                                    </option>
                                    <option value="5" <c:if test="${waybill.arriveHour == 5}">selected</c:if>>
                                        5点
                                    </option>
                                    <option value="6" <c:if test="${waybill.arriveHour == 6}">selected</c:if>>
                                        6点
                                    </option>
                                    <option value="7" <c:if test="${waybill.arriveHour == 7}">selected</c:if>>
                                        7点
                                    </option>
                                    <option value="8" <c:if test="${waybill.arriveHour == 8}">selected</c:if>>
                                        8点
                                    </option>
                                    <option value="9" <c:if test="${waybill.arriveHour == 9}">selected</c:if>>
                                        9点
                                    </option>
                                    <option value="10" <c:if test="${waybill.arriveHour == 10}">selected</c:if>>
                                        10点
                                    </option>
                                    <option value="11" <c:if test="${waybill.arriveHour == 11}">selected</c:if>>
                                        11点
                                    </option>
                                    <option value="12" <c:if test="${waybill.arriveHour == 12}">selected</c:if>>
                                        12点
                                    </option>
                                    <option value="13" <c:if test="${waybill.arriveHour == 13}">selected</c:if>>
                                        13点
                                    </option>
                                    <option value="14" <c:if test="${waybill.arriveHour == 14}">selected</c:if>>
                                        14点
                                    </option>
                                    <option value="15" <c:if test="${waybill.arriveHour == 15}">selected</c:if>>
                                        15点
                                    </option>
                                    <option value="16" <c:if test="${waybill.arriveHour == 16}">selected</c:if>>
                                        16点
                                    </option>
                                    <option value="17" <c:if test="${waybill.arriveHour == 17}">selected</c:if>>
                                        17点
                                    </option>
                                    <option value="18" <c:if test="${waybill.arriveHour == 18}">selected</c:if>>
                                        18点
                                    </option>
                                    <option value="19" <c:if test="${waybill.arriveHour == 19}">selected</c:if>>
                                        19点
                                    </option>
                                    <option value="20" <c:if test="${waybill.arriveHour == 20}">selected</c:if>>
                                        20点
                                    </option>
                                    <option value="21" <c:if test="${waybill.arriveHour == 21}">selected</c:if>>
                                        21点
                                    </option>
                                    <option value="22" <c:if test="${waybill.arriveHour == 22}">selected</c:if>>
                                        22点
                                    </option>
                                    <option value="23" <c:if test="${waybill.arriveHour == 23}">selected</c:if>>
                                        23点
                                    </option>
                                    <option value="24" <c:if test="${waybill.arriveHour == 24}">selected</c:if>>
                                        24点
                                    </option>
                                </select>
                            </div>
                        </div>
                        <div class="fl col-big">
                            <label class="labe_l">&nbsp;&nbsp;&nbsp;订单摘要:</label>
                            <div class="inputBox">
                                <input type="text" id="" class="tex_t" name="orderSummary"
                                       style="width:70%;" placeholder="请输入订单摘要" value="${waybill.orderSummary}">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="table-style">
            <table class="customTB">
                <thead>
                <tr>
                    <th>客户料号</th>
                    <th>物料名称</th>
                    <th>重量（kg）</th>
                    <th>体积（m<sup>3</sup>）</th>
                    <th>数量（件）</th>
                    <th colspan="2">货物摘要</th>
                </tr>
                </thead>
                <tbody id="appendTB">
                <c:choose>
                    <c:when test="${waybill.goods != null && fn:length(waybill.goods) > 0}">
                        <c:forEach var="goods" items="${waybill.goods}" varStatus="statu">
                            <tr>
                                <input  type="hidden" name="waybillid" value="${goods.waybillid}"/>
                                <input  type="hidden" name="createTime" value="<fmt:formatDate value="${goods.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <td style="width:12%"><input type="text" name="goodsType" maxlength="10"
                                                             value="${goods.goodsType}"></td>
                                <td style="width:12%"><input type="text" name="goodsName" maxlength="20"
                                                             value="${goods.goodsName}"></td>
                                <td style="width:12%"><input type="text" name="goodsWeight"
                                                             value="${goods.goodsWeight}"
                                                             onkeyup="this.value=this.value.replace(/[^\-?\d.]/g,'') "
                                                             onafterpaste="this.value=this.value.replace(/[^\-?\d.]/g,'') ">
                                </td>
                                <td style="width:12%"><input type="text" name="goodsVolume"
                                                             value="${goods.goodsVolume}"
                                                             onkeyup="this.value=this.value.replace(/[^\-?\d.]/g,'') "
                                                             onafterpaste="this.value=this.value.replace(/[^\-?\d.]/g,'') ">
                                </td>
                                <td style="width:12%"><input type="text" name="goodsQuantity"
                                                             value="${goods.goodsQuantity}"
                                                             onkeyup="this.value=this.value.replace(/[^\d\.]/g,'') "
                                                             onafterpaste="this.value=this.value.replace(/[^\d\.]/g,'') ">
                                </td>
                                <td style="width:28%"><input type="text" name="summary" value="${goods.summary}"
                                                             maxlength="15"/></td>
                                <c:if test="${statu.index < 1}">
                                    <td style="width:100px">
                                        <input type="hidden" value="${goods.id}" name="id"/>
                                        <a class="addBtn btnStyle">+</a>
                                    </td>
                                </c:if>
                                <c:if test="${statu.index > 0}">
                                    <td style="width:100px">
                                        <input type="hidden" value="${goods.id}" name="id" class="editGoodsId"/>
                                        <a class="minusBtn btnStyle">-</a>
                                    </td>
                                </c:if>
                            </tr>

                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td style="width:12%"><input type="text" name="goodsType" maxlength="10"></td>
                            <td style="width:12%"><input type="text" name="goodsName" maxlength="20"></td>
                            <td style="width:12%"><input type="text" name="goodsWeight"
                                                         onkeyup="this.value=this.value.replace(/[^\-?\d.]/g,'') "
                                                         onafterpaste="this.value=this.value.replace(/[^\-?\d.]/g,'') ">
                            </td>
                            <td style="width:12%"><input type="text" name="goodsVolume"
                                                         onkeyup="this.value=this.value.replace(/[^\-?\d.]/g,'') "
                                                         onafterpaste="this.value=this.value.replace(/[^\-?\d.]/g,'') ">
                            </td>
                            <td style="width:12%"><input type="text" name="goodsQuantity"
                                                         onkeyup="this.value=this.value.replace(/[^\d]/g,'') "
                                                         onafterpaste="this.value=this.value.replace(/[^\d]/g,'') ">
                            </td>
                            <td style="width:28%"><input type="text" name="summary" maxlength="15"></td>
                            <td style="width:100px"><a class="addBtn btnStyle">+</a></td>
                        </tr>
                    </c:otherwise>
                </c:choose>

                </tbody>
            </table>
            <!-- 模板 -->
            <table class="hide">
                <tbody id="templeteTB">
                <tr>
                    <td style="width:12%"><input type="text" name="goodsType" maxlength="10"></td>
                    <td style="width:12%"><input type="text" name="goodsName" maxlength="20"></td>
                    <td style="width:12%"><input type="text" name="goodsWeight"
                                                 onkeyup="this.value=this.value.replace(/[^\-?\d.]/g,'') "
                                                 onafterpaste="this.value=this.value.replace(/[^\-?\d.]/g,'') "></td>
                    <td style="width:12%"><input type="text" name="goodsVolume"
                                                 onkeyup="this.value=this.value.replace(/[^\-?\d.]/g,'') "
                                                 onafterpaste="this.value=this.value.replace(/[^\-?\d.]/g,'') "></td>
                    <td style="width:12%"><input type="text" name="goodsQuantity"
                                                 onkeyup="this.value=this.value.replace(/[^\d]/g,'') "
                                                 onafterpaste="this.value=this.value.replace(/[^\d]/g,'') "></td>
                    <td style="width:28%"><input type="text" name="summary" maxlength="15"></td>
                    <td style="width:100px"><a class="minusBtn btnStyle">-</a></td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="textC mt30">
            <a class="btn btn-default submitBtn" id="submitBtn">确定</a>
            <a id="cancelBtn" onClick="history.back(-1)">取消</a>
        </div>
        <div class="textC mt10" id="msgTips"></div>
    </form>
</div>

<!-- 客户信息弹出框 -->
<div class="bind_customer_panel" style="display: none">
    <div class="model-base-box bind_customer" style="width: 700px;">
        <div class="search-content">
            <div class="clearfix">
                <div class="fl inline-field">
                    <label class="labe_l">收货客户:</label>
                    <input type="text" class="tex_t" name="companyName">
                </div>
                <div class="fl inline-field">
                    <label class="labe_l">联系人:</label>
                    <input type="text" class="tex_t" name="contacts">
                </div>
                <div class="fr inline-field">
                    <input type="hidden" class="tex_t" name="groupId">
                    <button type="button" class="btn btn-default search_btn" id="search_customer">查询</button>
                </div>
            </div>
        </div>
        <!--收货客户维护  -->
        <div class="table-style">
            <table class="wtable">
                <thead>
                <tr>
                    <th width="15%">收货客户</th>
                    <th width="15%">联系人</th>
                    <th width="18%">联系电话</th>
                    <th>联系地址</th>
                    <th width="12%">操作</th>
                </tr>
                </thead>
                <tbody>
                <!-- 客户列表 -->
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/fileDownload.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<%-- <script src="${baseStatic}plugin/js/jquery-hcheckbox.js"></script> --%>
<script src="${baseStatic}plugin/js/jquery.mloading.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/cityselect/city.js"></script>
<script src="${baseStatic}plugin/js/cityselect/select.js"></script>
<script>

    var deleteGoods = [];

    //新增行
    $(".addBtn").on("click", function () {
        var _html = $("#templeteTB").html();
        $("#appendTB").append(_html);
    });
    //删除行
    $("#appendTB").on("click", ".minusBtn", function () {
        deleteGoods.push($(this).siblings("input").val());
        $(this).parents("tr").remove();
    });


    function searchCustomer(editBody) {
        var parmas = {};
        parmas.companyName = editBody.find("input[name='companyName']").val();
        parmas.contacts = editBody.find("input[name='contacts']").val();
        parmas.groupId = $("select[name='groupId']").val();
        $.util.json(base_url + "/backstage/trace/customerByGroupId", parmas, function (data) {
            if (data.success) {
                var tbody = editBody.find('table tbody');
                tbody.empty();
                $.each(data.customers, function (i, item) {
                    var tr = "<tr>"
                        + "<td class='receiverName'>" + item.companyName + "</td>"
                        + "<td class='contactName'>" + (item.contacts != null ? item.contacts : "") + "</td>"
                        + "<td class='contactPhone'>" + (item.contactNumber != null ? item.contactNumber : "") + "</td>"
                        + "<td>" + (item.fullAddress != null ? item.fullAddress : "")
                        + "<span class='addrDtl'>" + item.province + item.city + item.district + item.address + "</span>"
                        + "<span class='arriveDay'>" + item.arriveDay + "</span>"
                        + "<span class='arriveHour'>" + item.arriveHour + "</span>"
                        + "<span class='customerId'>" + item.id + "</span>"
                        + "</td>"
                        + "<td><a class='selectCustom' data-id='" + item.id + "'>选择客户</a></td>"
                        + "</tr>";
                    tbody.append(tr);
                });
            }
        }, 'json');
    }

    $(document).ready(function () {
        var parmas = {};
        //提交查询事件
        $("#submitBtn").on("click", function () {
            if ($("input[name='shipperName']").val() == "") {
                $("#msgTips").text("发货人不能为空")
                return false;
            }
            if ($("input[name='receiverName']").val() == "") {
                $("#msgTips").text("收货客户不能为空")
                return false;
            }
            if ($("input[name='contactName']").val() == "") {
                $("#msgTips").text("联系人不能为空")
                return false;
            }
            if ($("input[name='contactPhone']").val() == "") {
                $("#msgTips").text("联系电话不能为空")
                return false;
            } else if (!(/^1[\d]{10}$/.test($("input[name='contactPhone']").val())) && !(/^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/.test($("input[name='contactPhone']").val()))) {
                $("#msgTips").text("联系电话格式错误")
                return false;
            }
            if ($("input[name='addrDtl']").val() == "") {
                $("#msgTips").text("联系地址不能为空")
                return false;
            }
            //获取货品数组
            var goods = new Array();         //先声明一维
            $("#appendTB tr").each(function (i) {
                var obj = {};//在声明二维
                $(this).find("input").each(function () {
                    obj[this.name] = this.value;
                });
                goods[i] = obj;
            });
            console.log(goods);
            if ($("#select_group").val() != "") {
                parmas.groupid = $("select[name='groupId']").val();
            } else {
                $.util.warning("请先选择项目组");
                return;
            }
            var arriveDay = $("select[name='arriveDay']").val();
            var arriveHour = $("select[name='arriveHour']").val()
            //需要删除的货物ID
            parmas.deleteGoods = JSON.stringify(deleteGoods);
            parmas.groupid = $("select[name='groupId']").val();
            parmas.waybillId = $("#waybillId").val();
            parmas.customerid = $("#customerId").val();
            parmas.contactName = $("input[name='contactName']").val();
            parmas.receiverName = $("input[name='receiverName']").val();
            parmas.shipperName = $("input[name='shipperName']").val();
            parmas.contactPhone = $("input[name='contactPhone']").val();
            parmas.receiveAddress = $("input[name='addrDtl']").val();
            parmas.deliveryNumber = $("input[name='deliveryNumber']").val();
            parmas.arriveDay = arriveDay;
            parmas.arriveHour = arriveHour;
            parmas.orderSummary = $("input[name='orderSummary']").val();
            parmas.goodString = JSON.stringify(goods);
            $.util.json(base_url + '/backstage/trace/update', parmas, function (data) {
                if (data.success) {
                    $.util.alert('操作提示', data.message, function () {
                        window.location.href = base_url + "/backstage/trace/search";
                    });
                } else {
                    $.util.error(data.message);
                }
            });
        });

        //省市区联动
        if ($("#hideProvince").text() != "") {
            var _province = $("#hideProvince").text();
            var _city = $("#hideCity").text();
            var _district = $("#hideDistrict").text();
            $("#province").val(_province).change()
                .next().val(_city).change()
                .next().val(_district);
        }

    });

    //选择客户信息——弹窗
    $("#chooseCustomer").on("click", function () {
        if ($("#select_group").val() != "") {
            $.util.form('客户信息', $(".bind_customer_panel").html(), function (model) {
                var editBody = this.$body;
                searchCustomer(editBody);
                editBody.find("button").on('click', function () {
                    searchCustomer(editBody);
                });
            }, function () {
            });
        } else {
            $.util.warning("请先选择项目组");
        }

    });


    //选择客户
    $("body").on("click", ".selectCustom", function () {
        var _target = $(this).parents("tr");
        _target.addClass("backBlue").siblings("tr").removeClass("backBlue");
        _target.find("td").each(function (index) {
            var $this = $(this);
            if (index < 3) {
                var _id = $this.attr("class"),
                    _val = $this.text();
                $("#customFormBox").find("#" + _id).val(_val);
            } else if (index < 4) {
                $(this).find("span").each(function (i) {
                    var _id = $(this).attr("class"),
                        _val = $(this).text();
                    if (_id == "province" || _id == "city" || _id == "addrDtl") {
                        $("#customFormBox").find("#" + _id).val(_val).change();
                    } else {
                        $("#customFormBox").find("#" + _id).val(_val);
                    }
                });
            }
        })
    });
</script>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>任务单绑定</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${baseStatic}css/viewer.css?times=${times}"/>
    <link rel="stylesheet" type="text/css" href="${baseStatic}css/bindWaybill.css?times=${times}"/>
</head>
<body>
<div class="track-content">
    <c:choose>
        <c:when test="${driver != null && fn:length(driver) > 0}">
            <div class="barcode-con clearfix">
                <div class="div-left">
                    <div class="barcode-text">
                        <div class="line-group">
                            <div class="line-part line-min clearfix">
                                <span class="float-tag">上报人：</span>
                                <p id="loadName"
                                   class="text-tag">${!empty result.loader.unamezn ? result.loader.unamezn : null }</p>
                            </div>
                            <div class="line-part line-max clearfox">
                                <span class="float-tag">手机号：</span>
                                <p id="mobilePhone"
                                   class="text-tag">${!empty result.loader.mobilephone ? result.loader.mobilephone : null }</p>
                            </div>
                        </div>
                        <div class="line-group">
                            <div class="line-part line-min clearfix">
                                <span class="float-tag">上报时间：</span>
                                <p id="loadTime" class="text-tag"><fmt:formatDate value="${result.loadTime}"
                                                                                  pattern="yyyy-MM-dd HH:ss"/></p>
                            </div>
                            <div class="line-part line-max clearfix">
                                <span class="float-tag">上报位置：</span>
                                <p id="loadPosition"
                                   class="text-tag">${not empty result.tracks ? result.tracks.get(0).reportLoaction : null }</p>
                            </div>
                        </div>
                    </div>
                    <div class="barcode-img">
                        <div class="ul-box">
                            <ul id="barcodeImg">
                                <li>
                                    <c:if test="${result.images != null && fn:length(result.images) > 0}">
                                        <img id="loadImg" src="${baseImg}${result.images[0].storagePath}">
                                    </c:if>
                                    <c:if test="${result.images == null || fn:length(result.images) <= 0}">
                                        <img id="loadImg">
                                    </c:if>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="div-right">
                    <div class="search-con clearfix">
                        <form id="searchForm" action="${basePath}/backstage/driver/search">
                            <div class="search-part">
                                <input type="text" name="likeString" class="search-input" placeholder="输入二维码号"/>
                                <button class="search-btn">搜索</button>
                                <span class="search-icon"></span>
                            </div>
                        </form>
                        <div class="refresh">刷新</div>
                    </div>
                    <div class="barcode-list">
                        <p class="list-num">共<span id="count">${ not empty fn:length(driver) ? fn:length(driver) : 0}</span>条待处理任务
                        </p>
                        <div class="table-style">
                            <table>
                                <thead>
                                <tr>
                                    <th>二维码号</th>
                                    <th>上传时间</th>
                                </tr>
                                </thead>
                                <tbody id="fristTBody">
                                <c:forEach items="${driver}" var="driverContainer" varStatus="status">
                                    <tr onclick="unBindListClick(this)" data-id="${driverContainer.barcode }">
                                        <td>${driverContainer.barcode }</td>
                                        <td><fmt:formatDate value="${driverContainer.loadTime }" pattern="yyyy-MM-dd HH:ss"/></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <c:if test="${not empty waybill }">
                <div class="waybill-con clearfix">
                    <div class="div-left">
                        <h2>任务单详情</h2>
                        <div class="handle-btn">
                            <a href="javascript:bindBarcode();" class="aBind" id="aBind">绑码</a>
                            <a href="javascript:;" class="aEdit" id="aEdit">编辑</a>
                        </div>
                        <div class="detail-con">
                            <div class="line-group">
                                <input id="waybillId" name="waybillId" type="hidden"/>
                                <div class="line-part line-middle clearfix">
                                    <span class="float-tag">送货单号：</span>
                                    <div class="text-tag">
                                        <input id="deliveryNumber" name="deliveryNumber" class="input-tag" value="" readonly/>
                                    </div>
                                </div>
                                <!-- <div class="line-part line-middle clearfox">
                                    <span class="float-tag">任务单号：</span>
                                    <div class="text-tag">
                                        <input id="barcode" name="barcode" class="input-tag" value="" readonly/>
                                    </div>
                                </div> -->
                                <div class="line-part wl line-middle clearfox">
                                    <span class="float-tag">要求到货时间：</span>
                                    <div class="text-tag">
                                        <p class="show-tag"><span id="arriveDaySpan"></span><span id="arriveHourSpan"></span>
                                        </p>
                                        <div class="hide-tag" style="display:none;">
                                            <select id="arriveDay" name="arriveDay" class="">
                                                <option value="0">当天</option>
                                                <option value="1">第1天</option>
                                                <option value="2">第2天</option>
                                                <option value="3">第3天</option>
                                                <option value="4">第4天</option>
                                                <option value="5">第5天</option>
                                                <option value="6">第6天</option>
                                                <option value="7">第7天</option>
                                                <option value="8">第8天</option>
                                                <option value="9">第9天</option>
                                                <option value="10">第10天</option>
                                                <option value="11">第11天</option>
                                                <option value="12">第12天</option>
                                                <option value="13">第13天</option>
                                                <option value="14">第14天</option>
                                                <option value="15">第15天</option>
                                                <option value="16">第16天</option>
                                                <option value="17">第17天</option>
                                                <option value="18">第18天</option>
                                                <option value="19">第19天</option>
                                                <option value="20">第20天</option>
                                                <option value="21">第21天</option>
                                                <option value="22">第22天</option>
                                                <option value="23">第23天</option>
                                                <option value="24">第24天</option>
                                                <option value="25">第25天</option>
                                                <option value="26">第26天</option>
                                                <option value="27">第27天</option>
                                                <option value="28">第28天</option>
                                                <option value="29">第29天</option>
                                                <option value="30">第30天</option>
                                                <option value="31">第31天</option>
                                            </select>
                                            <select id="arriveHour" name="arriveHour" class="">
                                                <option value="1">凌晨1点</option>
                                                <option value="2">2点</option>
                                                <option value="3">3点</option>
                                                <option value="4">4点</option>
                                                <option value="5">5点</option>
                                                <option value="6">6点</option>
                                                <option value="7">7点</option>
                                                <option value="8">8点</option>
                                                <option value="9">9点</option>
                                                <option value="10">10点</option>
                                                <option value="11">11点</option>
                                                <option value="12">12点</option>
                                                <option value="13">13点</option>
                                                <option value="14">14点</option>
                                                <option value="15">15点</option>
                                                <option value="16">16点</option>
                                                <option value="17">17点</option>
                                                <option value="18">18点</option>
                                                <option value="19">19点</option>
                                                <option value="20">20点</option>
                                                <option value="21">21点</option>
                                                <option value="22">22点</option>
                                                <option value="23">23点</option>
                                                <option value="24">24点</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="line-part line-middle clearfix">
                                    <span class="float-tag">发货人：</span>
                                    <div class="text-tag">
                                        <input id="shipperName" name="shipperName" class="input-tag" value="" readonly/>
                                    </div>
                                </div>
                            </div>
                            <!-- 第二行 -->
                            <div class="line-group">
                                <div class="line-part line-middle clearfox">
                                    <span class="float-tag">任务摘要：</span>
                                    <div class="text-tag">
                                        <p id="orderSummary_p" class="show-tag"></p>
                                        <div class="hide-tag" style="display:none;">
                                            <input id="orderSummary" name="orderSummary" class="input-tag" type="text"
                                                   value=""/>
                                        </div>
                                    </div>
                                </div>
                                <div class="line-part wl line-middle clearfox">
                                    <span class="float-tag">收货地址：</span>
                                    <div class="text-tag">
                                        <p id="receiveAddressSpan" class="show-tag"></p>
                                        <div class="hide-tag" style="display:none;">
                                            <input id="receiveAddress" name="receiveAddress" class="input-tag" type="text"
                                                   value=""/>
                                        </div>
                                    </div>
                                </div>
                                <div class="line-part line-middle clearfix">
                                    <span class="float-tag">收货客户：</span>
                                    <div class="text-tag">
                                        <input id="receiverName" name="receiverName" class="input-tag" value="" readonly/>
                                    </div>
                                </div>
                            </div>
                            <!-- 第三行 -->
                            <div class="line-group">
                                <div class="line-part line-middle clearfox">
                                    <span class="float-tag">联系人：</span>
                                    <div class="text-tag">
                                        <input id="contactName" name="contactName" class="input-tag" value="" readonly/>
                                    </div>
                                </div>
                                <div class="line-part wl line-middle clearfox">
                                    <span class="float-tag">联系电话：</span>
                                    <div class="text-tag">
                                        <input id="contactPhone" name="contactPhone" class="input-tag" value="" readonly/>
                                    </div>
                                </div>
                                <div class="line-part line-middle clearfix">
                                    <span class="float-tag">座机号：</span>
                                    <div class="text-tag">
                                        <input id="receiverTel" name="receiverTel" class="input-tag" value="" readonly/>
                                    </div>
                                </div>
                            </div>
                            <!-- 物料内容 -->
                            <div class="table-style">
                                <table>
                                    <thead>
                                    <tr>
                                        <th>客户料号</th>
                                        <th>物料名称</th>
                                        <th width="10%">重量/kg</th>
                                        <th width="10%">体积/m³</th>
                                        <th width="10%">数量/件</th>
                                        <th>货物摘要</th>
                                        <th width="7%" class="bn"></th>
                                    </tr>
                                    </thead>
                                    <tbody id="goods_list">
                                        <%-- <c:forEach items="${ not empty waybill.goods ? waybill.goods : null }" var="goods" varStatus="status">
                                            <tr>
                                                <td><input type="text" class="input-tag" value="${goods.goodsType}" readonly/></td>
                                                <td><input type="text" class="input-tag" value="${goods.goodsName}" readonly/></td>
                                                <td><input type="text" class="input-tag" value="${goods.goodsWeight}" readonly/></td>
                                                <td><input type="text" class="input-tag" value="${goods.goodsVolume}" readonly/></td>
                                                <td><input type="text" class="input-tag" value="${goods.goodsQuantity}" readonly/></td>
                                                <td><input type="text" class="input-tag" value="${goods.summary}" readonly/></td>
                                                <td class="bn"><span class="addBtn btn-style">+</span></td>
                                            </tr>
                                        </c:forEach> --%>
                                    </tbody>
                                </table>
                            </div>
                            <!-- 确定取消按钮 -->
                            <div class="detail-btn-box" style="display:none;">
                                <a href="javascript:;" class="detail-btn detail-cancel">取消</a>
                                <a href="javascript:;" class="detail-btn detail-sure">确定</a>
                            </div>

                        </div>
                    </div>
                    <div class="div-right">
                        <h2>请选择任务绑码</h2>
                        <div class="table-style">
                            <table>
                                <thead>
                                <tr>
                                    <th width="15%">发货人</th>
                                    <th>收货客户</th>
                                    <th width="15%">联系人</th>
                                    <th>联系电话</th>
                                    <th>要求到货时间</th>
                                </tr>
                                </thead>
                                <tbody id="waybillList">
                                <c:forEach items="${waybill }" var="waybill">
                                    <tr onclick="unWaybillListClick(this)" data-id="${waybill.id }">
                                        <td>${waybill.shipperName }</td>
                                        <td>${waybill.receiverName }</td>
                                        <td>${waybill.contactName }</td>
                                        <td>${waybill.contactPhone }</td>
                                        <td><fmt:formatDate value="${waybill.arrivaltime }" pattern="yyyy-MM-dd HH:ss"/></td>
                                    </tr>
                                </c:forEach>
                                </tbody>

                            </table>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:when>
        <c:otherwise>
            <div class="container vertical-center">
                <div class="row">
                    <div class="bs-callout bs-callout-info">
                        <div class="tips-context">没有待绑定的任务单</div>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}plugin/js/viewer-jquery.min.js"></script>
<script src="${baseStatic}plugin/js/jquery.mloading.js"></script>
<script src="${baseStatic}js/Date.js"></script>
<script>
    var deleteCommodityKeys = [];

    function bindBarcode() {
        var arriveDay = $("select[name='arriveDay']").val();
        var arriveHour = $("select[name='arriveHour']").val()
        var barcode = $("#fristTBody").children("tr.active").attr("data-id");
        var waybillId = $("#waybillList").children("tr.active").attr("data-id");
        var deliveryNumber = $("#deliveryNumber").val();
        if (!barcode) {
            $.util.warning("绑定条码不能为空");
            return false;
        }
        if (!waybillId) {
            $.util.warning("绑定任务单不能为空");
            return false;
        }
        if (!deliveryNumber) {
            $.util.warning("送货单号不能为空");
            return false;
        }
        var parmas = {'barcode': barcode, 'id': waybillId, "arriveday": arriveDay, "arrivehour": arriveHour};
        $.util.json(base_url + "/backstage/driver/bindBarcode", parmas, function (data) {
            if (data.success) {
                $.util.alert('操作提示', data.message, function () {
                    $("#searchForm").submit();
                });
            } else {
                $.util.error(data.message);
            }
        })
    }

    //未绑定条码列表点击事件
    function unBindListClick(_this) {
        $("#fristTBody").children('tr').removeClass("active");
        var barcode = $(_this).attr("data-id");
        var parmas = {'barcode': barcode};
        $.post(base_url + "/backstage/driver/search/reportInfo", parmas, function (data) {
            if (data.success) {
                if (data.reportinfo) {//处理返回结果
                    if (data.reportinfo.loader) {
                        $("#loadName").val(data.reportinfo.loader.unamezn);
                        $("#mobilePhone").val(data.reportinfo.loader.mobilephone);
                    }
                    $("#loadTime").html(DateTimeToString(data.reportinfo.loadTime));
                    if (data.reportinfo.tracks && data.reportinfo.tracks.length) {
                        $("#loadPosition").val(data.reportinfo.tracks[0].reportLoaction);
                    }
                    if (data.reportinfo.images && data.reportinfo.images.length) {
                        $("#loadImg").attr("src", base_img + data.reportinfo.images[0].storagePath);
                    }else{
                        $("#loadImg").attr("src", "");
                    }
                    $(_this).addClass("active");
                }
                if (data.waybill) {
                    $("#waybillList").html("");
                    $.each(data.waybill, function (index, waybill) {
                        var _html = "<tr onclick='unWaybillListClick(this)' data-id='" + waybill.id + "'>"
                            + "<td>" + waybill.shipperName + "</td>"
                            + "<td>" + waybill.receiverName + "</td>"
                            + "<td>" + waybill.contactName + "</td>"
                            + "<td>" + waybill.contactPhone + "</td>"
                            + "<td>" + DateTimeToString(waybill.arrivaltime) + "</td>";
                        $("#waybillList").append(_html);
                    })
                    var firstTr = $("#waybillList").children("tr:first-child");
                    firstTr.addClass("active");
                    firstTr.trigger("click");
                }
            } else {
                $.util.error(data.message);
            }
        }, 'json');
    }

    //为绑定任务单列表点击事件
    function unWaybillListClick(_this) {
        deleteCommodityKeys = [];
        $("#waybillList").children('tr').removeClass("active");
        var waybillId = $(_this).attr("data-id");
        var parmas = {'waybillId': waybillId};
        $.post(base_url + "/backstage/driver/searchWaybillInfo", parmas, function (data) {
            if (data.waybill) {//处理返回结果
                $("#waybillId").val(data.waybill.id);
                $("#receiverName").val(data.waybill.receiverName);
                $("#receiverTel").val(data.waybill.receiverTel != null ? data.waybill.receiverTel : "");
                $("#receiveAddress").val(data.waybill.receiveAddress);
                $("#receiveAddressSpan").text(data.waybill.receiveAddress);
                $("#contactName").val(data.waybill.contactName);
                $("#contactPhone").val(data.waybill.contactPhone);
                $("#barcode").val(data.waybill.barcode);
                $("#deliveryNumber").val(data.waybill.deliveryNumber != null ? data.waybill.deliveryNumber : "");
                if (data.waybill.arriveDay >= 0) {
                    var optionDay = "option[value='" + data.waybill.arriveDay + "']";
                    $("#arriveDay").find(optionDay).attr("selected", true);
                    $("#arriveDaySpan").text($("#arriveDay").find("option:selected").text());
                } else {
                    $("#arriveDaySpan").text("");
                }
                if (data.waybill.arriveHour >= 0) {
                    var optionHour = "option[value='" + data.waybill.arriveHour + "']";
                    $("#arriveHour").find(optionHour).attr("selected", true);
                    $("#arriveHourSpan").text($("#arriveHour").find("option:selected").text());
                } else {
                    $("#arriveHourSpan").text("");
                }
                $("#shipperName").val(data.waybill.shipperName);
                $("#orderSummary").val(data.waybill.orderSummary != null ? data.waybill.orderSummary : "");
                $("#orderSummary_p").text(data.waybill.orderSummary != null ? data.waybill.orderSummary : "");
                $("#goods_list").children('tr').remove();
                if (data.waybill.goods && data.waybill.goods.length) {
                    $.each(data.waybill.goods, function (index, good) {
                        var htmlStr = "<tr><td style='display:none;'><input name='waybillid' type='hidden' value='" + good.waybillid + "'/>" +
                            "<input type='hidden' name='createTime' value='" + DateTimeToString(good.createTime) + "'/><input name='id' type='text' value='" + good.id + "'/></td>"
                            + "<td><input name='goodsType' type='text' class='input-tag' value='" + (good.goodsType != null ? good.goodsType : "") + "' readonly/></td>"
                            + "<td><input name='goodsName' type='text' class='input-tag' value='" + (good.goodsName != null ? good.goodsName : "") + "' readonly/></td>"
                            + "<td><input name='goodsWeight' type='text' class='input-tag' value='" + (good.goodsWeight != null ? good.goodsWeight : "-") + "' readonly/></td>"
                            + "<td><input name='goodsVolume' type='text' class='input-tag' value='" + (good.goodsVolume != null ? good.goodsVolume : "-" ) + "' readonly/></td>"
                            + "<td><input name='goodsQuantity' type='text' class='input-tag' value='" + (good.goodsQuantity != null ? good.goodsQuantity : "-") + "' readonly/></td>"
                            + "<td><input name='summary' type='text' class='input-tag' value='" + (good.summary != null ? good.summary : "") + "' readonly/></td>";
                        if (index == 0) {
                            htmlStr += "<td class='bn'><span onclick='addTr()' class='addBtn btn-style'>+</span></td></tr>";
                        } else {
                            htmlStr += "<td class='bn'><span onclick='removeTr(this)' class='addBtn btn-style'>-</span></td></tr>";
                        }
                        $("#goods_list").append(htmlStr);
                    })
                }
                $(_this).addClass("active");
            } else {
                $.util.error(data.message);
            }
        }, 'json');
    }

    $(document).ready(function () {
        if ($("#waybillList").children("tr")) {
            var firstTr = $("#waybillList").children("tr:first-child");
            firstTr.addClass("active");
            firstTr.trigger("click");
        }
        //点编辑显示编辑内容
        $('#aEdit').on('click', function () {
            $('.input-tag').addClass('edit').css('background', '#fff').removeAttr('readonly');
            $('.show-tag').hide().next().show();
            $('.btn-style').css('display', 'block');
            //隐藏绑码编辑按钮
            $('.handle-btn').hide();
            //显示确定取消按钮
            $('.detail-btn-box').show();
        })

        //编辑取消
        $('.detail-cancel').on('click', function () {
            $('.input-tag').removeClass('edit').css('background', 'transparent').attr('readonly', 'readonly');
            $('.show-tag').val($('.hide-tag>input').val()).show().next().hide();
            $('.btn-style').hide();

            //显示绑码编辑按钮
            $('.handle-btn').show();
            //隐藏确定取消
            $('.detail-btn-box').hide();
            $("#waybillList").children("tr.active").trigger('click');
        })
        //编辑确定
        $('.detail-sure').on('click', function () {
            var sender = {
                companyName : $("input[name='shipperName']").val(),
                contacts
                contactNumber
                tel
                address
            };
            if ($("input[name='shipperName']").val() == "") {
                $.util.warning("发货人不能为空")
                return false;
            }
            if ($("input[name='receiverName']").val() == "") {
                $.util.warning("收货客户不能为空")
                return false;
            }
            if ($("input[name='contactName']").val() == "") {
                $.util.warning("联系人不能为空")
                return false;
            }
            if ($("input[name='contactPhone']").val() == "") {
                $.util.warning("联系电话不能为空")
                return false;
            } else if (!(/^1[\d]{10}$/.test($("input[name='contactPhone']").val()))) {
                $.util.warning("联系电话格式错误")
                return false;
            }
            if ($("input[name='receiveAddress']").val() == "") {
                $.util.warning("联系地址不能为空")
                return false;
            }
            if ($("input[name='deliveryNumber']").val() == "") {
                $.util.warning("送货单号不能为空")
                return false;
            }
            //获取货品数组
            var commodities = new Array();         //先声明一维
            $("#goods_list tr").each(function (i) {
                var obj = {};//在声明二维
                $(this).find("input").each(function () {
                    obj[this.name] = this.value;
                });
                commodities[i] = obj;
            });
            var arriveDay = $("select[name='arriveDay']").val();
            var arriveHour = $("select[name='arriveHour']").val()
            var parmas = {};
            //需要删除的货物ID
            parmas.deleteCommodityKeys = JSON.stringify(deleteCommodityKeys);
            parmas.waybillId = $("input[name='waybillId']").val();//$("#waybillList").children("tr.active").attr("data-id");
            //parmas.customerid = $("#customerId").val();
            parmas.contactName = $("input[name='contactName']").val();
            parmas.receiverName = $("input[name='receiverName']").val();
            parmas.shipperName = $("input[name='shipperName']").val();
            parmas.contactPhone = $("input[name='contactPhone']").val();
            parmas.receiveAddress = $("input[name='receiveAddress']").val();
            parmas.barcode = $("input[name='barcode']").val();
            parmas.receiverTel = $("input[name='receiverTel']").val();
            parmas.deliveryNumber = $("input[name='deliveryNumber']").val();
            parmas.arriveDay = arriveDay;
            parmas.arriveHour = arriveHour;
            parmas.orderSummary = $("input[name='orderSummary']").val();
            parmas.commodities = JSON.stringify(commodities);
            $.util.json(base_url + '/backstage/trace/update', parmas, function (data) {
                if (data.success) {
                    $("#waybillList").children("tr.active").trigger('click');
                    $.util.alert('操作提示', data.message, null);
                } else {
                    $.util.error(data.message);
                }
            });
            $('.input-tag').removeClass('edit').css('background', 'transparent').attr('readonly', 'readonly');
            $('.show-tag').val($('.hide-tag>input').val()).show().next().hide();
            $('.btn-style').hide();

            //显示绑码编辑按钮
            $('.handle-btn').show();
            //隐藏确定取消
            $('.detail-btn-box').hide();
        })

        $('.search-btn').on('click', function () {
            $('#searchForm').submit();
        })

        $('.refresh').on('click', function () {
            location.href = base_url + '/backstage/driver/search';
        })
        //初始化图片
        $('#barcodeImg').viewer({
            'navbar': false
        });
        $('#fristTBody').children("tr:first-child").addClass("active");//默认选择第一个
        $("#waybillList").children("tr:first-child").addClass("active");//默认选择第一个
    });

    //添加货物行
    function addTr() {
        var htmlStr = "<tr><td>" +
            "<input id='goodsType' name='goodsType' type='text' class='input-tag edit' value=''/></td>"
            + "<td><input id='goodsName' name='goodsName' type='text' class='input-tag edit' value=''/></td>"
            + "<td><input id='goodsWeight' name='goodsWeight' type='text' class='input-tag edit' value=''/></td>"
            + "<td><input id='goodsVolume' name='goodsVolume' type='text' class='input-tag edit' value=''/></td>"
            + "<td><input id='goodsQuantity' name='goodsQuantity' type='text' class='input-tag edit' value=''/></td>"
            + "<td><input id='summary' name='summary' type='text' class='input-tag edit' value=''/></td>"
            + "<td class='bn'><span onclick='removeTr(this)' class='addBtn btn-style show'>-</span></td></tr>";
        $("#goods_list").append(htmlStr);
    }

    //删除货物行
    function removeTr(_this) {
        var id = $(_this).parent().parent().find("input[name='id']").val();
        if(id){
            deleteCommodityKeys.push($(_this).parent().parent().find("input[name='id']").val());
        }
        $(_this).parent().parent().remove();
    }
</script>
</html>
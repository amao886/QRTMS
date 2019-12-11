<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>客户资料-常用地址</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/page.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}css/customInfo.css?times=${times}"/>
    <style type="text/css">
        .amap-sug-result {
            z-index: 99999
        }

        .jconfirm {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            z-index: 100;
            font-family: inherit;
            overflow: hidden
        }

        .searchBtn {
            color: rgb(255, 255, 255);
            font-size: 14px;
            padding: 3px 20px;
            border-radius: 3px;
            background: rgb(49, 172, 250);
        }

        ::-webkit-scrollbar {
            width: 14px;
        }

        ::-webkit-scrollbar-track {
            background-color: #bee1eb;
        }

        ::-webkit-scrollbar-thumb {
            background-color: #00aff0;
        }

        ::-webkit-scrollbar-thumb:hover {
            background-color: #9c3
        }

        ::-webkit-scrollbar-thumb:active {
            background-color: #00aff0
        }
    </style>
</head>
<body>
<div class="custom-content clearfix">
    <form id="searchCustomerForm" action="${basePath}/backstage/customer/search" method="post">
        <input type="hidden" id="num" name="num" value="${ search.num}">
        <input type="hidden" id="type" name="type" value="${ search.type}">
        <input type="hidden" id="size" name="size" value="${ search.size}">
        <div class="tabBtnCon">
            <span id="shipAddress" class="tabBtn curr" data-type="2">发货地址</span>
            <span id="receiveAddress" class="tabBtn" data-type="1">收货地址</span>
        </div>
        <div class="content-search">
	        <span>客户名称：</span><input type="text" class="tex_t" id="companyName" name="companyName" value="${ search.companyName}"/>
	        <span>联系电话：</span><input type="text" class="tex_t" id="contactNumber" name="contactNumber" value="${ search.contactNumber}"/>
	        <span>客户编码：</span><input type="text" class="tex_t" id="customerCode" name="customerCode" value="${ search.customerCode}"/>
        	<a id="search" class="btn btn-default search">查询</a>
        </div>
    </form>
    <div class="topInfo clearfix">
        <span>当前共${page.currSize}条</span>
        <a id="newAddr" class="btn btn-default newAddr">新增地址</a>
    </div>
    <div class="table-style">
        <table>
            <thead>
            <tr>
                <th>地址所属</th>
                <th>组名称</th>
                <th id="c_name">收货客户</th>
                <th>联系人</th>
                <th>联系电话</th>
                <th>固定电话</th>
                <th id="address_th">收货地址</th>
                <th>详细地址</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.collection }" var="customer">
                <tr>
                    <td>${not empty customer.group ? "项目组":"个人"}</td>
                    <td>${not empty customer.group ? customer.group.groupName:""}</td>
                    <td>${customer.companyName}</td>
                    <td>${customer.contacts}</td>
                    <td>${customer.contactNumber}</td>
                    <td>${customer.tel}</td>
                    <td>${customer.region}</td>
                    <td>${customer.address}</td>
                    <td>
                        <button type="button" data-id="${customer.id}" class="btn btn-link edit_row">编辑</button>
                        <button type="button" data-id="${customer.id}" data-groupid="${customer.groupId}"
                                class="btn btn-link delete-row">删除
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <c:if test="${page.collection != null && fn:length(page.collection) > 0}">
        <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}" pages="${page.pages}"
                         total="${page.total}"/>
    </c:if>

</div>

<!--新增/编辑弹出框-->
<div class="edit_customer_panel" style="display:none;">
    <div class="model-base-box edit_customer" style="width:500px;height: auto;">
        <div class="model-form-field">
            <label>地址所属:</label>
            <label><input type="radio" id="radioGroup" name="belongType" value="1" style="width:auto;">项目组:</label>
            <label><input type="radio" checked="checked" id="radioPerson" name="belongType" value="2"
                          style="width:auto;">个人</label>
        </div>
        <div class="model-form-field select_group" style="display:none;">
            <label for="">项目组:</label>
            <select class="tex_t" name="groupId" style="width:61%">
                <option value="">请选择项目组</option>
                <c:forEach items="${groups}" var="group">
                    <option value="${group.id}">${group.groupName }</option>
                </c:forEach>
            </select>
        </div>
        <div class="model-form-field">
            <label for="">地址类型:</label>
            <label><input type="radio" id="shipAddressRadio" name="type" value="2" style="width:auto;">发货地址</label>
            <label><input type="radio" id="receiveAddressRadio" name="type" checked="checked" value="1"
                          style="width:auto;">收货地址</label>
        </div>
        <div class="model-form-field">
            <label for="" id="customerCode">客户编码:</label>
            <input type="text" name="customerCode" style="width:61%"/>
        </div>
        <div class="model-form-field">
            <label for="" id="companyName">收货客户:</label>
            <input type="text" name="companyName" style="width:61%"/>
        </div>
        <div class="model-form-field">
            <label for="">联系人:</label>
            <input type="text" name="contacts" style="width:61%"/>
        </div>
        <div class="model-form-field">
            <label for="">手机号码:</label>
            <input type="tel" name="contactNumber" style="width:61%"/>
        </div>
        <div class="model-form-field">
            <label for="">固定电话:</label>
            <input type="tel" name="tel" id="tel" placeholder="021-65896369" style="width:61%;"/>
        </div>
        <div class="model-form-field">
            <label class="labe_l">发货地区:</label>
            <div class="inputBox model-form-field" style="display: inline; ">
                <select id="province" name="province" class="tex_t" style="width:20%;">
                    <option value="">--请选省份--</option>
                </select>
                <select id="city" name="city" class="tex_t" style="width:20%;">
                    <option value="">--请选城市--</option>
                </select>
                <select id="district" name="district" class="tex_t" style="width:20%;">
                    <option value="">--请选区县--</option>
                </select>
            </div>
        </div>
        <div class="model-form-field">
            <label class="labe_l">详细地址:</label>
            <input type="text" id="addrDtl" class="tex_t" name="address" placeholder="详细地址" maxlength="25"
                   style="width:61%;">
        </div>
        <div class="model-form-field p-relative arrive_time">
            <label for="">要求时间:</label>
            <div class="relative-box">
                <span>发货后</span>
                <select id="selectDay" name="arrivalDay">
                    <option value="">选择第几天</option>
                    <c:forEach begin="0" end="31" var="day">
                        <c:if test="${day == 0 }">
                            <option value="${day}">当天</option>
                        </c:if>
                        <c:if test="${day != 0 }">
                            <option value="${day}">第${day}天</option>
                        </c:if>
                    </c:forEach>
                </select>
                <select id="selectHour" name="arrivalHour">
                    <option value="">选择时间点</option>
                    <c:forEach begin="1" end="24" var="hour">
                        <c:if test="${hour != 0 }">
                            <option value="${hour}">${hour}点</option>
                        </c:if>
                    </c:forEach>
                </select>
                <span>之前</span>
            </div>
        </div>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/cityselect/city.js"></script>
<script src="${baseStatic}plugin/js/cityselect/select.js?time=${times}"></script>
<script>
    $(document).ready(function () {
        //发货地址
        //var type = $(".curr").attr("data-type")
        //$("#type").val(type);
        $("#shipAddress").on("click", function () {
            $("#type").val($(this).attr("data-type"));
            $("#searchCustomerForm").submit();
        });
        //收货地址
        $("#receiveAddress").on("click", function () {
            $("#type").val($(this).attr("data-type"));
            $("#searchCustomerForm").submit();
        });
        //翻页事件
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                $("form input[name='num']").val($(this).attr("num"));
                $("#searchCustomerForm").submit();
            });
        });
        //条件查询
        $("#search").on('click', function () {
            $("#searchCustomerForm").submit();
        });   

        /*编辑*/
        $(".edit_row").on('click', function () {
            var id = $(this).attr("data-id");
            $.post(base_url + '/backstage/customer/findById/' + id, {}, function (data) {
                if (data.success) {//处理返回结果
                    var customer = data.customer;
                    $.util.form('编辑客户', $(".edit_customer_panel").html(), function (model) {
                        var editBody = this.$body;
                        editBody.find(":input[name='customerCode']").val(customer.customerCode);
                        editBody.find(":input[name='companyName']").val(customer.companyName);
                        editBody.find(":input[name='contacts']").val(customer.contacts);
                        editBody.find(":input[name='contactNumber']").val(customer.contactNumber);
                        editBody.find(":input[name='address']").val(customer.address);
                        editBody.find(":input[name='tel']").val(customer.tel);
                        var selectGroup = editBody.find("select[name='groupId']");
                        if (customer.province) {
                            province = editBody.find("#province");
                            city = editBody.find("#city");
                            town = editBody.find("#district");
                            province.on("change", function () {
                                selectName('province', province);
                            });
                            city.on("change", function () {
                                selectName("city", city);
                            });
                            province.val(customer.province).change().next().val(customer.city).change().next().val(customer.district);
                        }

                        editBody.find("#radioGroup").on('change', function () {
                            editBody.find(".select_group").show();
                        });
                        editBody.find("#radioPerson").on('change', function () {
                            editBody.find(".select_group").hide().find('select').val("");
                        });
                        if (customer.groupId > 0) {
                            editBody.find("select[name='groupId']").val(customer.groupId);
                            editBody.find(".select_group").show();
                            editBody.find("#radioGroup").attr("checked", "checked");
                        } else {
                            editBody.find("#radioPerson").attr("checked", "checked");
                            editBody.find(".select_group").hide();
                        }
                        var shipAddressRadio = editBody.find("#shipAddressRadio");
                        var receiveAddressRadio = editBody.find("#receiveAddressRadio");
                        $(shipAddressRadio).on('change', function () {
                            $(this).attr("checked", "checked");
                            $(receiveAddressRadio).removeAttr("checked");
                            editBody.find("#companyName").text("货主名称");
                            editBody.find('.arrive_time').hide().find('select').val("");
                        })
                        $(receiveAddressRadio).on('change', function () {
                            $(this).attr("checked", "checked");
                            $(shipAddressRadio).removeAttr("checked");
                            editBody.find("#companyName").text("收货客户");
                            var arrive = editBody.find('.arrive_time').show();
                            arrive.find("select[name='arrivalDay']").val(customer.arrivalDay);
                            arrive.find("select[name='arrivalHour']").val(customer.arrivalHour);
                        })
                        if (customer.type == 2) {
                            $(shipAddressRadio).trigger("change");
                        } else {
                            $(receiveAddressRadio).trigger("change");
                        }
                    }, function () {
                        var parmas = {}, editBody = this.$body;
                        editBody.find("input, select, textarea").each(function (index, item) {
                            if (item.name == "belongType" || item.name == "type") {
                                if (item.checked) {
                                    parmas[item.name] = item.value;
                                }
                            } else {
                                parmas[item.name] = item.value;
                            }
                        });
                        if (vlidated(parmas)) {
                            parmas["id"] = customer.id;
                            if (parmas['belongType'] === "2") {
                                parmas['groupId'] = 0;
                            }
                            console.log(parmas);
                            $.util.json(base_url + '/backstage/customer/edit', parmas, function (data) {
                                if (data.success) {
                                    $.util.alert('操作提示', data.message, function () {
                                        $("#searchCustomerForm").submit();
                                    });
                                } else {
                                    $.util.error(data.message);
                                }
                            });
                        }
                        return false;
                    });
                } else {
                    $.util.error(data.message);
                }
            }, 'json');
        });

        $(".newAddr").on('click', function () {
            $.util.form('新增客户', $(".edit_customer_panel").html(), function () {
                var editBody = this.$body;
                var shipAddressRadio = editBody.find("#shipAddressRadio");
                var receiveAddressRadio = editBody.find("#receiveAddressRadio");
                var radioGroup = editBody.find("#radioGroup");
                var radioPerson = editBody.find("#radioPerson");
                province = editBody.find("#province");
                city = editBody.find("#city");
                town = editBody.find("#district");
                province.on("change", function () {
                    selectName('province', province);
                });
                city.on("change", function () {
                    selectName("city", city);
                });
                $(radioGroup).on('change', function () {
                    editBody.find(".select_group").show();
                })
                $(radioPerson).on('change', function () {
                    editBody.find(".select_group").hide().find('select').val("");
                })
                $(shipAddressRadio).on('change', function () {
                    $(this).attr("checked", "checked");
                    $(receiveAddressRadio).removeAttr("checked");
                    editBody.find("#companyName").text("货主名称");
                    editBody.find('.arrive_time').hide().find('select').val("");
                })
                $(receiveAddressRadio).on('change', function () {
                    $(this).attr("checked", "checked");
                    $(shipAddressRadio).removeAttr("checked");
                    editBody.find("#companyName").text("收货客户");
                    editBody.find('.arrive_time').show();
                })
            }, function () {
                var parmas = {}, editBody = this.$body;
                editBody.find("input, select, textarea").each(function (index, item) {
                    if (item.name == "belongType" || item.name == "type") {
                        if (item.checked) {
                            parmas[item.name] = item.value;
                        }
                    } else {
                        parmas[item.name] = item.value;
                    }
                });
                if (vlidated(parmas)) {
                    if (parmas['belongType'] === "2") {
                        parmas['groupId'] = 0;
                    }
                    $.util.json(base_url + '/backstage/customer/save', parmas, function (data) {
                        if (data.success) {
                            $.util.alert('操作提示', data.message, function () {
                                $("#searchCustomerForm").submit();
                            });
                        } else {
                            $.util.error(data.message);
                        }
                    });
                }
                return false;
            });
        });

        if ($("#type").val() == "1") {
            $("#shipAddress").removeClass("curr");
            $("#receiveAddress").addClass("curr");
            $("#c_name").html("收货客户");
            $("#address_th").html("收货地址");
        } else {
            $("#receiveAddress").removeClass("curr");
            $("#shipAddress").addClass("curr");
            $("#c_name").html("货主名称");
            $("#address_th").html("发货地址");
        }
    });

    $(".delete-row").on("click", function () {
        var id = $(this).attr("data-id");
        var groupId = $(this).attr("data-groupid");
        $.util.confirm("提示", "确定要删除该条地址信息吗?", function () {
            var parmas = {"customerId": id, "groupId": groupId};
            $.util.json(base_url + '/backstage/customer/delete', parmas, function (data) {
                if (data.success) {
                    $.util.alert('操作提示', data.message, function () {
                        $("#searchCustomerForm").submit();
                    });
                } else {
                    $.util.error(data.message);
                }
            });
        });
    })

    /**
     * 数据校验
     */
    function vlidated(parmas) {
        var result = false;
        var belongType = parmas['belongType'];
        var groupId = parmas['groupId'];
        var contactNumber = parmas['contactNumber'];
        var compayName = parmas['companyName'];
        var contacts = parmas['contacts'];
        var type = parmas['type'];
        var province = parmas['province'];
        var city = parmas['city'];
        var district = parmas['district'];
        var address = parmas['address'];
        if (belongType == '1' && (!groupId || groupId <= 0)) {
            $.util.error("请先选择一个项目组");
        } else if (!compayName || compayName == '') {
            $.util.error("收货客户不能为空");
        } else if (!contacts || contacts == '') {
            $.util.error("联系人不能为空");
        } else if (!province || province == '') {
            $.util.error("请选择省份");
        } else if (!city || city == '') {
            $.util.error("请选择城市");
        } else if (!address || address == '') {
            $.util.error("详细地址不能为空");
        } else if (contactNumber && !(/^1[\d]{10}$/.test(contactNumber)) && !(/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(contactNumber))) {
            $.util.error("手机号码格式错误！");
        } else if (parmas['tel'] && !(/\d{3,4}-\d{8}|\d{4}-\{7,8}/.test(parmas['tel']))) {
            $.util.error("固定电话格式错误！");
        } else {
            result = true;
        }
        return result;
    }

</script>

</html>
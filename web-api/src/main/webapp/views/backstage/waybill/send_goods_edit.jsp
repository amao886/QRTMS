<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-我的任务</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/js/cityselect/chooseAddress/city.css"/>
    <link rel="stylesheet" href="${baseStatic}css/send_goods.css"/>
</head>
<body>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<div class="track-content">
    <input type="hidden" value="${waybill.id}" name="waybillId"/>
    <div class="project-wrapper">
        <div class="line-part large-tag">
            <span class="float-tag">分享到项目组</span>
				<div class="text-tag">
					<select name="groupId" id="select_group" disabled="disabled" class="select-tag width-auto">
						<option value="">请选择项目组</option>
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
    </div>
    <div class="part-wrapper">
        <div class="title">
            <h2>发货信息</h2>
            <span class="search-input-two">选择常用地址</span>
        </div>
        <div class="part-item layui-fluid">
            <div class="layui-row layui-col-space10 sendCustomer">
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag"><span class="require-icon">*</span>货主名称</span>
                        <div class="text-tag"><input type="text" class="input-tag"  placeholder="请填写货主名称" name="companyName" value="${waybill.shipperName}"/></div>
                    </div>
                </div>
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag"><span class="require-icon">*</span>联系人</span>
                        <div class="text-tag"><input type="text" class="input-tag" placeholder="请填写发货联系人" name="contacts" value="${waybill.shipperContactName}"/></div>
                    </div>
                </div>
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag"><span class="require-icon">*</span>联系电话</span>
                        <div class="text-tag"><input type="text" class="input-tag" placeholder="请填写发货联系电话" name="contactNumber" value="${waybill.shipperContactTel}"/></div>
                    </div>
                </div>
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag">固定电话</span>
                        <div class="text-tag"><input type="text" class="input-tag" placeholder="请填写发货固定电话" name="tel" value="${waybill.shipperTel}"/></div>
                    </div>
                </div>
                <div class="layui-col-md12 layui-col-lg12">
                    <div class="line-part address-box">
                        <span class="float-tag"><span class="require-icon">*</span>发货地址</span>
                        <div class="text-tag manage-wrapper">
                            <input type="text" placeholder="请选择发货地区" class="dizhi_city sendgoods_addr"
                                   name="selectcity" value="${waybill.startStation}" readonly="readonly"/>
                            <input type="text" class="ml2" placeholder="请输入发货详细地址" name="address" value="${waybill.shipperAddress}"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="part-wrapper">
        <div class="title">
            <h2>收货信息</h2>
            <span class="search-input-one">选择常用地址</span>
        </div>
        <div class="part-item layui-fluid">
            <div class="layui-row layui-col-space10 receiveCustomer">
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag"><span class="require-icon">*</span>收货客户</span>
                        <div class="text-tag"><input type="text" class="input-tag" placeholder="请填写收货客户"  name="companyName" value="${waybill.receiverName}"/></div>
                    </div>
                </div>
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag"><span class="require-icon">*</span>联系人</span>
                        <div class="text-tag"><input type="text" class="input-tag" placeholder="请填写收货联系人" name="contacts" value="${waybill.contactName}"/></div>
                    </div>
                </div>
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag"><span class="require-icon">*</span>联系电话</span>
                        <div class="text-tag"><input type="text" class="input-tag" placeholder="请填写收货联系电话" name="contactNumber" value="${waybill.contactPhone}"/></div>
                    </div>
                </div>
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag">固定电话</span>
                        <div class="text-tag"><input type="text" class="input-tag" placeholder="请填写收货固定电话" name="tel" value="${waybill.receiverTel}"/></div>
                    </div>
                </div>
                <div class="layui-col-md12 layui-col-lg12">
                    <div class="line-part address-box">
                        <span class="float-tag"><span class="require-icon">*</span>收货地址</span>
                        <div class="text-tag manage-wrapper">
                            <input type="text" class="dizhi_city receive_goods" placeholder="请选择收货地区" name="selectcity"
                                   value="${waybill.endStation}" readonly="readonly"/>
                            <input type="text" class="ml2" placeholder="请输入收货详细地址" name="address" value="${waybill.receiveAddress}"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="part-wrapper other-info">
        <div class="title">
            <h2>其他信息</h2>
        </div>
        <div class="part-item layui-fluid">
            <div class="layui-row layui-col-space10">
                <div class="layui-col-md6 layui-col-lg6">
                    <div class="line-part">
                        <span class="float-tag"><span class="require-icon">*</span>运输路线</span>
                        <div class="text-tag inline_block">
                            <div class="manage-wrapper">
                                <input type="text" class="input-tag dizhi_city" placeholder="始发地" name="startStation" value="${waybill.startStation}" disabled/>
                            </div>
                            <span class="under_line"></span>
                            <div class="manage-wrapper">
                                <input type="text" class="input-tag dizhi_city" placeholder="目的地" name="endStation" value="${waybill.endStation}" disabled/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-col-md-offset1 layui-col-md5  layui-col-lg5  layui-col-lg-offset1">
                    <div class="line-part large-tag">
                        <span class="float-tag">要求到货时间</span>
                        <div class="text-tag ml_more">
                            <span class="wrap-word">发货后</span>
                            <select name="arriveDay" id="arriveDay" class="select-tag width-auto">
                                <option value="" <c:if test="${waybill.arriveDay < 0}">selected</c:if>>
                                    请选择天数
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
                            <select name="arriveHour" id="arriveHour" class="select-tag width-auto">
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
                            <span class="wrap-word">之前</span>
                        </div>
                    </div>
                </div>
                <div class="layui-col-md12 layui-col-lg12">
                    <div class="line-part">
                        <span class="float-tag">订单摘要</span>
                        <div class="text-tag">
                            <textarea class="txtarea" name="orderSummary" rows="3" cols="" >${waybill.orderSummary}</textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="table-style">
        <table class="table-handle">
            <thead>
            <tr>
                <th>客户料号</th>
                <th>物体名称</th>
                <th width="10%">重量/kg</th>
                <th width="10%">体积/m³</th>
                <th width="10%">数量/件</th>
                <th>货物摘要</th>
                <th width="10%"></th>
            </tr>
            </thead>
            <tbody class="t-body" id="appendTB">

            <c:choose>
                <c:when test="${waybill.goods != null && fn:length(waybill.goods) > 0}">
                    <c:forEach var="goods" items="${waybill.goods}" varStatus="statu">
                        <tr>
                            <input  type="hidden" name="waybillid" value="${goods.waybillid}"/>
                            <input  type="hidden" name="createTime" value="<fmt:formatDate value="${goods.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                            <td><input type="text" name="goodsType" class="td-input" value="${goods.goodsType}"/></td>
                            <td><input type="text" name="goodsName" class="td-input" value="${goods.goodsName}"/></td>
                            <td><input type="text" name="goodsWeight"
                                       onkeyup="this.value=this.value.replace(/[^\-?\d.]/g,'') "
                                       onafterpaste="this.value=this.value.replace(/[^\-?\d.]/g,'') " value="${goods.goodsWeight}" class="td-input"/></td>
                            <td><input type="text" name="goodsVolume"
                                       onkeyup="this.value=this.value.replace(/[^\-?\d.]/g,'') "
                                       onafterpaste="this.value=this.value.replace(/[^\-?\d.]/g,'') " value="${goods.goodsVolume}" class="td-input"/></td>
                            <td><input type="text" name="goodsQuantity"
                                       onkeyup="this.value=this.value.replace(/[^\d]/g,'') "
                                       onafterpaste="this.value=this.value.replace(/[^\d]/g,'') " value="${goods.goodsQuantity}" class="td-input"/></td>
                            <td><input type="text" name="summary" class="td-input" value="${goods.summary}"/></td>
                            <c:if test="${statu.index < 1}">
                                <td style="width:100px">
                                    <input type="hidden" value="${goods.id}" name="id"/>
                                    <span class="handle-icon add-ico">+</span>
                                </td>
                            </c:if>
                            <c:if test="${statu.index > 0}">
                                <td style="width:100px">
                                    <input type="hidden" value="${goods.id}" name="id" class="editGoodsId"/>
                                    <span class="handle-icon minus-ico" onclick="removeTr(this,${goods.id})">-</span>
                                </td>
                            </c:if>
                        </tr>

                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td><input type="text" name="goodsType" class="td-input"/></td>
                        <td><input type="text" name="goodsName" class="td-input"/></td>
                        <td><input type="text" name="goodsWeight"
                                   onkeyup="this.value=this.value.replace(/[^\-?\d.]/g,'') "
                                   onafterpaste="this.value=this.value.replace(/[^\-?\d.]/g,'') " class="td-input"/></td>
                        <td><input type="text" name="goodsVolume"
                                   onkeyup="this.value=this.value.replace(/[^\-?\d.]/g,'') "
                                   onafterpaste="this.value=this.value.replace(/[^\-?\d.]/g,'') " class="td-input"/></td>
                        <td><input type="text" name="goodsQuantity"
                                   onkeyup="this.value=this.value.replace(/[^\d]/g,'') "
                                   onafterpaste="this.value=this.value.replace(/[^\d]/g,'') " class="td-input"/></td>
                        <td><input type="text" name="summary" class="td-input"/></td>
                        <td><span class="handle-icon add-ico">+</span></td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <div class="btnBox">
        <button class="layui-btn layui-btn-normal" id="submitBtn">确定</button>
        <button class="layui-btn layui-btn-primary" onclick="window.history.go(-1)">取消</button>
    </div>
    <!-- 弹出框部分 -->
    <div class="model-box" style="display:none;">
        <h2>常用发货地址</h2>
        <div class="line-wrapper">
            <div class="line-part" style="width:40%;">
                <span class="float-tag">货主名称</span>
                <div class="text-tag"><input type="text" class="input-tag"/></div>
            </div>
            <div class="line-part" style="width:40%;">
                <span class="float-tag">联系人</span>
                <div class="text-tag"><input type="text" class="input-tag"/></div>
            </div>
            <div class="line-part" style="width:16%;">
                <button class="layui-btn layui-btn-sm layui-btn-normal">查询</button>
            </div>
        </div>
        <div class="table-style">
            <table>
                <thead>
                <tr>
                    <th>货主名称</th>
                    <th>联系人</th>
                    <th>联系电话</th>
                    <th>固定电话</th>
                    <th>发货地区</th>
                    <th>详细地址</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>
                        <button class="layui-btn layui-btn-primary layui-btn-sm">选择</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="page-box">这里放分页</div>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/jquery.mloading.js"></script>
<script src="${baseStatic}plugin/js/cityselect/chooseAddress/area.js"></script>
<script src="${baseStatic}plugin/js/cityselect/chooseAddress/provinceSelect.js"></script>
<script src="${baseStatic}js/select.resource.js?times=${times}"></script>

<script>
    var deleteCommodityKeys = [];
    function removeTr(obj , id) {
        if(id){
            deleteCommodityKeys.push(id);
        }
        $(obj).parent().parent().remove();
    }

    $(".sendgoods_addr").on("click", function () {
        $("input[name='startStation']").val(this.value);
    })
    $(".receive_goods").on("click", function () {
        $("input[name='endStation']").val(this.value);
    })

    $(document).ready(function () {

        //点击添加表单行
        var str = '<tr>' +
            '<td><input type="text" class="td-input" name="goodsType"/></td>' +
            '<td><input type="text" class="td-input" name="goodsName"/></td>' +
            '<td><input type="text" class="td-input" name="goodsWeight"/></td>' +
            '<td><input type="text" class="td-input" name="goodsVolume"/></td>' +
            '<td><input type="text" class="td-input" name="goodsQuantity"/></td>' +
            '<td><input type="text" class="td-input" name="summary"/></td>' +
            '<td><span class="handle-icon minus-ico" onclick="removeTr(this)">-</span></td>' +
            '</tr>';
        $('.add-ico').click(function () {
            $('.t-body').append(str);
        })

        //点击删除表单行
        $('.table-handle').delegate('.minus-ico', 'click', function () {
            $(this).parent().parent().remove();
        })
        function validateRequire(item){
            var $item = $(item);
            if(!$item.val() && $item.attr('type') == 'text'){
                var $p = $item.parents('.line-part');
                if($p.is('div')){
                    return $p.find('.require-icon').length > 0;
                }
            }
            return false;
        }
        //提交查询事件
        $("#submitBtn").on("click", function () {
            var parmas = {}, msgs = [];
            //收货客户
            parmas.receiver = {type:1};
            $.each($('.receiveCustomer').find('input'), function(k, v){
                if(validateRequire(v)){
                    msgs.push(v.placeholder);
                }else{
                    parmas.receiver[v.name] = v.value;
                }
            });
            //发货客户
            parmas.sender = {type:2};
            $.each($('.sendCustomer').find('input'), function(k, v){
                if(validateRequire(v)){
                    msgs.push(v.placeholder);
                }else{
                    parmas.sender[v.name] = v.value;
                }
            });
            $.each([parmas.sender, parmas.receiver], function(i, c){
                if(c.selectcity){
                    var items = c.selectcity.split('-');
                    c['province'] = items[0];
                    c['city'] = items[1];
                    c['district'] = items[2];
                }
                if(c.contactNumber && !(/^1[\d]{10}$/.test(c.contactNumber))){
                    msgs.push((c.type == 1 ? "收货":"发货") + "联系电话格式错误");
                }
            });
            //获取货品数组
            parmas.commodities = new Array();         //先声明一维
            $(".t-body tr").each(function (i) {
                var obj = {}, v = false;//在声明二维
                $(this).find("input").each(function () {
                    obj[this.name] = this.value;
                    if(this.value){ v = true; }
                });
                if(v){
                    parmas.commodities[i] = obj;
                }
            });
            if(parmas.commodities.length <= 0){
                msgs.push("请至少添加一条货物信息");
            }
            if(msgs && msgs.length > 0){
                $.util.error(msgs.join("<br/>"));
                return false;
            }
            parmas.groupid = $("select[name='groupId']").val();
            if(!parmas.groupid){
                parmas.groupid = 0;
            }
            parmas.id = $("input[name='waybillId']").val();
            parmas.arriveDay = $("select[name='arriveDay']").val();
            parmas.arriveHour = $("select[name='arriveHour']").val();
            parmas.orderSummary = $("textarea[name='orderSummary']").val();
            parmas.sender = JSON.stringify(parmas.sender);
            parmas.receiver = JSON.stringify(parmas.receiver);
            parmas.commodities = JSON.stringify(parmas.commodities);
            parmas.deleteCommodityKeys = JSON.stringify(deleteCommodityKeys);
            console.log(parmas);
            $.util.json(base_url + '/backstage/trace/update', parmas, function (data) {
                if (data.success) {
                    $.util.alert('操作提示', data.message, function () {
                        window.location.href = base_url + "/backstage/trace/search?groupId="+ parmas.groupid;
                    });
                } else {
                    $.util.error(data.message);
                }
            });

        });

        //发货
        $('.search-input-two').on("click", function () {//1：收货地址，2：发货地址
            $(this).selectCustomer({selectClose:true, selectType: 2, selectGroup: $("select[name='groupId']").val(),
                selectFun: function (item) {
                    var selecter = $('.sendCustomer');
                    $.each(item, function(k, v){
                        selecter.find("input[name='"+ k +"']").val(v);
                    });
                    var pcdaddress = item.province;
                    if(item.city){ pcdaddress += '-'+ item.city; }
                    if(item.district){ pcdaddress += '-'+ item.district; }
                    if(pcdaddress == null || pcdaddress == 'null'){
                        pcdaddress = '';
                    }
                    selecter.find("input[name='selectcity']").val(pcdaddress);
                    $("input[name='startStation']").val(pcdaddress);
                }
            });
        });
        //收货
        $('.search-input-one').on("click", function () {//1：收货地址，2：发货地址
            $(this).selectCustomer({selectClose:true, selectType: 1, selectGroup: $("select[name='groupId']").val(),
                selectFun: function (item) {
                    var selecter = $('.receiveCustomer');
                    $.each(item, function(k, v){
                        selecter.find("input[name='"+ k +"']").val(v);
                    });
                    var pcdaddress = item.province;
                    if(item.city){ pcdaddress += '-'+ item.city; }
                    if(item.district){ pcdaddress += '-'+ item.district; }
                    if(pcdaddress == null || pcdaddress == 'null'){
                        pcdaddress = '';
                    }
                    console.log(item);

                    selecter.find("input[name='selectcity']").val(pcdaddress);
                    $("input[name='endStation']").val(pcdaddress);
                    $("select[name='arriveDay']").val(item.arrivalDay);
                    $("select[name='arriveHour']").val(item.arrivalHour);
                }
            });
        });
    });
</script>
</html>
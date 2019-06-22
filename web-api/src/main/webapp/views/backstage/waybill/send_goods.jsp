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
    <div class="project-wrapper">
        <div class="line-part large-tag">
            <span class="float-tag">分享到项目组</span>
            <div class="text-tag">
                <select name="groupId" id="select_group" class="select-tag width-auto">
                    <option value="">请选择项目组</option>
                    <c:forEach items="${groups}" var="group">
                        <option value="${group.id}">${group.groupName }</option>
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
            <div class="layui-row layui-col-space10 address sendCustomer">
                <input name="id" type="hidden">
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag"><span class="require-icon">*</span>货主名称</span>
                        <div class="text-tag"><input type="text" class="input-tag" placeholder="请填写货主名称" name="companyName"/></div>
                    </div>
                </div>
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag"><span class="require-icon">*</span>联系人</span>
                        <div class="text-tag"><input type="text" class="input-tag" placeholder="请填写发货联系人" name="contacts"/></div>
                    </div>
                </div>
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag"><span class="require-icon">*</span>联系电话</span>
                        <div class="text-tag"><input type="text" class="input-tag" placeholder="请填写发货联系电话" name="contactNumber"/></div>
                    </div>
                </div>
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag">固定电话</span>
                        <div class="text-tag"><input type="text" class="input-tag" placeholder="请填写发货固定电话" name="tel"/></div>
                    </div>
                </div>
                <div class="layui-col-md12 layui-col-lg12">
                    <div class="line-part address-box">
                        <span class="float-tag"><span class="require-icon">*</span>发货地址</span>
                        <div class="text-tag manage-wrapper">
                            <input type="text" placeholder="请选择发货地区" class="dizhi_city sendgoods_addr" name="selectcity"  readonly="readonly"/>
                            <input type="text" class="ml2" placeholder="请输入发货详细地址" name="address"/>
                            <label class="ml2 saveAddress">
                                <input type="checkbox" style="vertical-align: -3px;margin-right:4px;" value="1" name="saveSend"/>保存到常用发货地址
                            </label>
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
            <div class="layui-row layui-col-space10 address receiveCustomer">
                <input name="id" type="hidden">
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag"><span class="require-icon">*</span>收货客户</span>
                        <div class="text-tag"><input type="text" class="input-tag" placeholder="请填写收货客户"  name="companyName"/></div>
                    </div>
                </div>
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag"><span class="require-icon">*</span>联系人</span>
                        <div class="text-tag"><input type="text" class="input-tag" placeholder="请填写收货联系人" name="contacts"/></div>
                    </div>
                </div>
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag"><span class="require-icon">*</span>联系电话</span>
                        <div class="text-tag"><input type="text" class="input-tag" placeholder="请填写收货联系电话" name="contactNumber"/></div>
                    </div>
                </div>
                <div class="layui-col-md3 layui-col-lg3">
                    <div class="line-part">
                        <span class="float-tag">固定电话</span>
                        <div class="text-tag"><input type="text" class="input-tag" placeholder="请填写收货固定电话" name="tel"/></div>
                    </div>
                </div>
                <div class="layui-col-md12 layui-col-lg12">
                    <div class="line-part address-box">
                        <span class="float-tag"><span class="require-icon">*</span>收货地址</span>
                        <div class="text-tag manage-wrapper">
                            <input type="text" class="dizhi_city receive_goods" placeholder="请选择收货地区" name="selectcity" readonly="readonly"/>
                            <input type="text" class="ml2" placeholder="请输入收货详细地址" name="address"/>
                            <label class="ml2 saveAddress">
                                <input type="checkbox" style="vertical-align:-3px;margin-right:4px;" value="2" name="saveReceive"/>保存到常用收货地址
                            </label>
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
            	<div class="layui-col-md12  layui-col-lg12">
                    <div class="line-part large-tag">
                        <span class="float-tag">要求到货时间</span>
                        <div class="text-tag ml_more">
                            <span class="wrap-word">发货后</span>
                            <select name="arriveDay" id="arriveDay" class="select-tag width-auto">
                                <option value="">请选择第几天</option>
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
                            <select name="arriveHour" id="arriveHour" class="select-tag width-auto">
                                <option value="">时间</option>
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
                            <span class="wrap-word">之前</span>
                        </div>
                    </div>
                </div>
                
                <div class="layui-col-md6 layui-col-lg6">
                    <div class="line-part">
                        <span class="float-tag"><span class="require-icon">*</span>运输路线</span>
                        <div class="text-tag inline_block">
                            <div class="manage-wrapper">
                                <input type="text" class="input-tag dizhi_city" placeholder="始发地" name="startStation" disabled/>
                            </div>
                            <span class="under_line"></span>
                            <div class="manage-wrapper">
                                <input type="text" class="input-tag dizhi_city" placeholder="目的地" name="endStation" disabled/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-col-md12 layui-col-lg12">
                    <div class="line-part">
                        <span class="float-tag">订单摘要</span>
                        <div class="text-tag">
                            <textarea class="txtarea" name="orderSummary" rows="3" cols=""></textarea>
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
            </tbody>
        </table>
    </div>
    <div class="btnBox">
        <button class="layui-btn layui-btn-normal" id="submitBtn">确定</button>
        <button class="layui-btn layui-btn-primary" onclick="window.history.back();">取消</button>
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

    function removeTr(obj) {
        $(obj).parent().parent().remove();
    }

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

        $(".sendgoods_addr").on("click", function () {
            $("input[name='startStation']").val(this.value);
        })
        $(".receive_goods").on("click", function () {
            $("input[name='endStation']").val(this.value);
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
                    if('saveReceive' === v.name){
                        parmas.saveReceive = v.checked;
                    }else{
                        parmas.receiver[v.name] = v.value;
                    }
                }
            });
            //发货客户
            parmas.sender = {type:2};
            $.each($('.sendCustomer').find('input'), function(k, v){
                if(validateRequire(v)){
                    msgs.push(v.placeholder);
                }else{
                    if('saveSend' === v.name){
                        parmas.saveSend = v.checked;
                    }else{
                        parmas.sender[v.name] = v.value;
                    }
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
            parmas.arriveDay = $("select[name='arriveDay']").val();
            parmas.arriveHour = $("select[name='arriveHour']").val();
            parmas.orderSummary = $("textarea[name='orderSummary']").val();

            parmas.commodities = JSON.stringify(parmas.commodities);
            parmas.sender = JSON.stringify(parmas.sender);
            parmas.receiver = JSON.stringify(parmas.receiver);
            console.log(parmas);
            $.util.json(base_url + '/backstage/trace/send/goods/save', parmas, function (data) {
                if (data.success) {
                    $.util.alert('操作提示', data.message, function () {
                        window.location.href = base_url + "/backstage/trace/search?groupId="+ parmas.groupid;
                    });
                } else {
                    $.util.error(data.message);
                }
            });
        });
        //发货信息
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
        //收货信息
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
                    selecter.find("input[name='selectcity']").val(pcdaddress);
                    $("input[name='endStation']").val(pcdaddress);
                    $("select[name='arriveDay']").val(item.arrivalDay);
                    $("select[name='arriveHour']").val(item.arrivalHour);
                }
            });
        });
        
        $('#select_group').change(function(){
        	$('.address').find(":input").each(function(index,item){
        		$(item).val('');
        	});
        })
    });
</script>
</html>
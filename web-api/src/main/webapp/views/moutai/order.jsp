<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>茅台打印-发货单管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
    <link rel="stylesheet" href="${baseStatic}css/inventory.css?times=${times}"/>
    <style type="text/css">
        .downloadBtn{color:#31acfa;text-decoration: underline !important;}
        .order_delete{background:#ff7f00 !important}
        #layui_div_page > #pageSize{float: left;margin: 20px 5px 0 0;height: 34px;border: #ddd solid 1px;border-radius: 3px;}
        .file_box>a.file{position: absolute;top: 0px;width: 80px;left: auto;right: 1px;height: 30px;z-index: 22;overflow: hidden;}
        .file_box>a.file input{width: 100%;height: 100%;}
        .keep>a{text-decoration: underline !important;color:#31acfa;}
        .search-con{position:relative;}
        .search-con>.btn{position:absolute;right:0;top:21px;line-height:32px;}
        .detail_order{margin-right:0;padding:0 10px;}
        .detail_order .field-item {width:47%;position:relative;}
        .detail_order .one-line{position:relative;width:100%;}
        .detail_order .field-item label,.detail_order .one-line label{position:absolute;top:0;left:0;line-height:28px;overflow:hidden;text-align:center;width:4em;}
        .detail_order .field-item label>span,.detail_order .one-line label>span{display: block;}
        .detail_order .w2{letter-spacing: 2em;margin-right:-2em;}
        .detail_order .w3{letter-spacing: 0.5em;margin-right:-0.5em;}
        .detail_order .one-line label{width:auto;text-align:left;}
        .detail_order .item-input{margin-left:75px;}
        .detail_order .item-input>input,
        .detail_order .item-input>select{
            width:100%;
            border: 1px solid #ddd;
            border-radius: 3px;
            height: 28px;
            text-indent: 6px;
        }
        .detail_order .one-line,.detail_order .field-item{position:relative;}
        .detail_order .one-line:before,.detail_order .field-item:before{
            content:':';
            position:absolute;
            top:7px;
            left:60px;
            font-weight:700;
        }
        .cityChecked{font-size:0;}
        .cityChecked .tex_t,.cityChecked input{
            display:inline-block;
            vertical-align:top;
            height: 28px;
            font-size:14px;
            border: 1px solid #ddd;
            border-radius: 3px;
        }
        .cityChecked .tex_t{width:18%;}
        .cityChecked .tex_t + .tex_t{margin:0 2%;}
        .cityChecked input{width:60%;}
    </style>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchDeliveryForm" action="${basePath}/backstage/moutai/queryDeliveryList" method="post">
      <input type="hidden" name="pageNum" value="${search.pageNum}">
       <input type="hidden" name="pageSize" value="10">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">调拨单号:</label>
                    <input type="text" class="tex_t" name="scheduleNo" value="${search.scheduleNo}" >
                </div>
                <div class="fl col-min">
                    <label class="labe_l">承运单位:</label>
                    <select  class="tex_t selec_t" name="conveyId" value="${search.conveyId}" >
                        <option value ="">全部</option>
                        <c:forEach items="${conveyList}" var="convey">
                            <option value="${convey.id}" <c:if test="${convey.id == search.conveyId}">selected</c:if> >${convey.conveyName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">发货仓库:</label>
                    <select class="tex_t selec_t depot" name="depotId" value="${search.depotId}">
                        <option value ="">全部</option>
                        <option value="1"  <c:if test="${1 == search.depotId}">selected</c:if>  >茅台库</option>
                        <option value="2"  <c:if test="${2 == search.depotId}">selected</c:if> >贵阳库</option>
                        <option value="3"  <c:if test="${3 == search.depotId}">selected</c:if> >郑州库</option>
                        <option value="4"  <c:if test="${4 == search.depotId}">selected</c:if> >沈阳库</option>
                        <option value="5"  <c:if test="${5 == search.depotId}">selected</c:if> >北京库</option>
                        <option value="6"  <c:if test="${6 == search.depotId}">selected</c:if> >上海库</option>
                        <option value="7"  <c:if test="${7 == search.depotId}">selected</c:if> >杭州库</option>
                        <option value="8"  <c:if test="${8 == search.depotId}">selected</c:if> >西安库</option>
                        <option value="9"  <c:if test="${9 == search.depotId}">selected</c:if> >兰州库</option>
                        <option value="10" <c:if test="${10 == search.depotId}">selected</c:if> >珠海库</option>
                    </select>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">客户编码:</label>
                    <input type="text" class="tex_t" name="customerId"  value="${search.customerId}" >
                </div>
            </div>
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">购货单位:</label>
                    <input type="text" class="tex_t" name="buyerUnit" value="${search.buyerUnit}" >
                </div>

                <div class="fl col-min">
                    <label class="labe_l">导入日期:</label>
                    <div class="input-append date startDate" id="startTime">
                        <input type="text" class="tex_t z_index" name="startTime" value="<fmt:formatDate value="${search.startTime}" pattern="yyyy-MM-dd"/>" readonly>
                        <span class="add-on">
                            <i class="icon-th"></i>
                        </span>
                    </div>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">至</label>
                    <div class="input-append date endDate" id="secondTime">
                        <input type="text" class="tex_t z_index" name="endTime"  value="<fmt:formatDate value="${search.endTime}" pattern="yyyy-MM-dd"/>" readonly>
                        <span class="add-on">
                            <i class="icon-th"></i>
                        </span>
                    </div>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">打印状态:</label>
                    <select  class="tex_t selec_t" name="printSign" value="${search.printSign}" >
                        <option value ="">全部</option>
                        <option value="1" <c:if test="${search.printSign == 1}">selected</c:if> >已打印</option>
                        <option value="0" <c:if test="${search.printSign == 0}">selected</c:if> >未打印</option>
                    </select>
                </div>
            </div>
            <div class="search-con">
                <button type="button" class="btn btn-default content-search-btn">查询</button>
            </div>
        </div>
    </form>
    <div class="btnBox">
        <button type="button" class="btn btn-default order-leading">发货单导入</button>
        <button type="button" class="btn btn-default order-print-skip">配送单打印</button>
        <button type="button" class="btn btn-default depot-leading" >发货仓库编辑</button>
        <button type="button" class="btn btn-default order_delete">删除</button>
    </div>
    <div class="table-style" >
        <table class="dtable" style="table-layout: auto;">
            <thead>
            <tr>
                <th width="60" style="text-align:left;text-indent:10px;"><input type="checkbox" id="checkAll">全选</th>
                <th width="90">发货单号</th>
                <th width="80">调拨单号</th>
                <th width="80">承运单位</th>
                <th width="65">发货仓库</th>
                <th width="65">客户编码</th>
                <th width="150">购货单位</th>
                <th width="70">联系人员</th>
                <th width="80">联系电话</th>
                <th width="200">收货地址</th>
                <th width="80">导入日期</th>
                <th width="120">规格</th>
                <th width="100">销售量|瓶数</th>
                <th width="125">销售量|折合件数</th>
                <th width="80">是否打印过</th>
                <th width="70">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.collection}" var="order">
                <tr ondblclick="td_popBox(event,${order.id}, 2, this )">
                    <td style="text-align:left;text-indent:10px;"><input class="keep" type="checkbox" name="checksingle" value="${order.id}"></td>
                    <td>${order.id}</td>
                    <td>${order.scheduleNo}</td>
                    <td class ="check-conveyId" data="${order.conveyId}">
                        <c:forEach items="${conveyList}" var="convey">
                            <c:if test="${convey.id == order.conveyId}">${convey.conveyName}</c:if>
                        </c:forEach>
                    </td>
                    <td class ="check-storeId" data="${order.storeId}">
                        <c:if test="${order.storeId ==1 }">茅台库</c:if>
                        <c:if test="${order.storeId ==2 }">贵阳库 </c:if>
                        <c:if test="${order.storeId ==3 }">郑州库</c:if>
                        <c:if test="${order.storeId ==4 }">沈阳库</c:if>
                        <c:if test="${order.storeId ==5 }">北京库 </c:if>
                        <c:if test="${order.storeId ==6 }">上海库 </c:if>
                        <c:if test="${order.storeId ==7 }">杭州库 </c:if>
                        <c:if test="${order.storeId ==8 }">西安库 </c:if>
                        <c:if test="${order.storeId ==9 }">兰州库 </c:if>
                        <c:if test="${order.storeId ==10 }">珠海库</c:if>
                    </td>
                    <c:set var="contains" value="false" />
                    <c:forEach items="${customerKeyList}" var="key">
                        <c:if test="${order.customerNo eq key}">
                            <c:set var="contains" value="true" />
                        </c:if>
                    </c:forEach>
                  <%--  <td <c:if test="${!contains}">class="keep" style="background: #5FB878" onclick="add_customer('${order.customerNo}')"</c:if>>
                            ${order.customerNo}
                    </td>--%>
                    <c:if test="${!contains}">
                        <td class="keep" onclick="add_customer('${order.customerNo}')">
                            <a href="javascript:;" onfocus="this.blur()">${order.customerNo}</a>
                        </td>
                    </c:if>
                    <c:if test="${contains}">
                        <td>
                                ${order.customerNo}
                        </td>

                    </c:if>

                    <td style="text-align:left;"> <c:out value="${order.buyOrg}"/></td>

                    <td>${order.contactName}</td>
                    <td>${order.contactTel}</td>
                    <td>${( order.province eq 'undefined'|| order.province eq 'null') ? "" : order.province} ${(order.city eq 'undefined'|| order.city eq 'null') ? "" : order.city} ${order.address}  </td>

                    <td><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd"/> </td>
                    <td style="text-align:left;"><c:out value="${order.specification}"/></td>
                    <td>${order.bottles}</td>
                    <td>${order.quantity}</td>
                    <td>
                        <c:if test="${order.printSign ==1 }">已打印</c:if>
                        <c:if test="${order.printSign ==0 }">未打印</c:if>
                    </td>
                    <td>
                    <a class="aBtn viewMoreBtn keep" onclick="popBox(${order.id} , 1)">查看</a>
                    <a class="aBtn editBtn keep" onclick="popBox(${order.id} , 2)">编辑</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${page.collection != null && fn:length(page.collection) > 0}">
	            <select id="pageSize" onchange="commonSubmit()"  <c:if test="${page.total * 1.0 <= search.pageSize * 1.0}" >hidden</c:if>
                        class="tex_t selec_t" name="_pageSize" value="${search.pageSize}">
	                <option value="10"  <c:if test="${10 == search.pageSize}">selected</c:if>  >10</option>
	                <option value="50"  <c:if test="${50 == search.pageSize}">selected</c:if>  >50</option>
	                <option value="100"  <c:if test="${100 == search.pageSize}">selected</c:if> >100</option>
	            </select>
	            <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}" pages="${page.pages}" total="${page.total}"/>
        </c:if>
    </div>
</div>

<!-- 发货单导入 -->
<div class="import_task_panel" style="display: none">
    <div class="model-base-box import_task" style="width: 480px;">
        <form id="importForm" action="${basePath}/" enctype="multipart/form-data"  method="post">
            <div class="model-form-field">
            	<label>导入模版:</label>
                <a href="javascript:void(0)" class="downloadBtn" id="download-order-template" url="${template}">发货单导入模板下载.xls</a>
                </select>
            </div>
            <div class="model-form-field">
                <label>承运单位:</label>
                <select id="select_group" class="tex_t selec_t" name="conveyId" style="width:81%">
                    <option value="-1">--请选择承运单位--</option>
                    <c:forEach items="${conveyList}" var="convey">
                            <option value="${convey.id}" >${convey.conveyName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="model-form-field file_box">
                <label>选择文件:</label>
                <input type="text" name="fileName" id="fileName" readonly  style="width:63%"/>
                <span class="span-txt">请选择文件</span>
                <a href="javascript:;" class="file">
                    <input name="file" type="file" accept=".xls,.xlsx" id="uploadFile"/>
                </a>
            </div>
        </form>
    </div>
</div>
<!-- 仓库弹出框 -->
<div class="import_depot_panel" style="display: none">
    <div class="model-form-field">
        <label>仓库:</label>
        <select id="depotId" class="tex_t selec_t depot" name="depotId">
            <option value="1" >茅台库</option>
            <option value="2" >贵阳库</option>
            <option value="3" >郑州库</option>
            <option value="4" >沈阳库</option>
            <option value="5" >北京库</option>
            <option value="6" >上海库</option>
            <option value="7" >杭州库</option>
            <option value="8" >西安库</option>
            <option value="9" >兰州库</option>
            <option value="10" >珠海库</option>
        </select>
    </div>
</div>


<!--查看详情弹出框-->
<div class="view_more_panel" style="display:none;">
        <div class="model-base-box view_more detail_order" style="width:650px;height: auto;">
            <div class="model-form-field clearfix">
                <div class="field-item fl">
                    <label><span>发货单号</span></label>
                    <div class="item-input"><input type="text" name="id" id="form_id" readonly/></div>
                </div>
                <div class="field-item fr">
                    <label><span>调拨单号</span></label>
                    <div class="item-input"><input type="text" name="scheduleNo" id="form_scheduleNo"/></div>
                </div>
            </div>
            <div class="model-form-field clearfix">
                <div class="field-item fl">
                    <label><span>客户编码</span></label>
                    <div class="item-input"><input type="tel" name="customerNo" id="form_customerNo"/></div>
                </div>
                <div class="field-item fr">
                    <label><span>购货单位</span></label>
                    <div class="item-input"><input type="tel" name="buyOrg" id="form_buyOrg"/></div>
                </div>
            </div>

            <div class="model-form-field clearfix">
                <div class="field-item fl">
                    <label><span>联系人员</span></label>
                    <div class="item-input"><input type="tel" name="contactName" id="form_contactName"/></div>
                </div>
                <div class="field-item fr">
                    <label><span>联系电话</span></label>
                    <div class="item-input"><input type="tel" name="contactTel" id="form_contactTel"/></div>
                </div>
            </div>

            <div class="model-form-field clearfix">
                <label class="fl" style="width:4em;line-height:28px;">省市</label>
                <div class="inputBox model-form-field" style="margin-left:75px;">
                    <div class="cityChecked">
                        <select id="province1" name="currentprovince" class="tex_t">
                            <option value="">--请选省份--</option>
                        </select>
                        <select id="city1" name="currentcity" class="tex_t">
                            <option value="">--请选城市--</option>
                        </select>
                    <input name="currentaddress" id="form_currentaddress" placeholder="详细地址">
                    </div>
                </div>
            </div>

            <div class="model-form-field clearfix">
                <div class="field-item fl">
                    <label class="labe_l"><span>发货仓库</span></label>
                    <div class="item-input">
                        <select name="storeId" id="form_storeId" class="tex_t storeId">
                            <option value="0" >--请选择仓库--</option>
                            <option value="1" >茅台库</option>
                            <option value="2" >贵阳库</option>
                            <option value="3" >郑州库</option>
                            <option value="4" >沈阳库</option>
                            <option value="5" >北京库</option>
                            <option value="6" >上海库</option>
                            <option value="7" >杭州库</option>
                            <option value="8" >西安库</option>
                            <option value="9" >兰州库</option>
                            <option value="10" >珠海库</option>
                        </select>
                    </div>
                </div>
                <div class="field-item fr">
                    <label class="labe_l"><span>承运单位</span></label>
                    <div class="item-input">
                        <select  name="conveyId" id="form_conveyId" class="tex_t">
                            <option value="-1" >--请选择承运单位--</option>
                            <c:forEach items="${conveyList}" var="convey">
                                <option value="${convey.id}" >${convey.conveyName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <div class="model-form-field one-line">
                <label><span class="w2">规格</span></label>
                <div class="item-input"><input type="tel" name="specification" id="form_specification"/></div>
            </div>
            <div class="model-form-field clearfix">
                <div class="field-item fl">
                    <label><span class="w2">瓶数</span></label>
                    <div class="item-input">
                        <input type="tel" name="bottles" id="form_bottles" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')}"/>
                    </div>
                </div>
                <div class="field-item fr">
                    <label><span class="w2">件数</span></label>
                    <div class="item-input">
                        <input type="tel" name="quantity"id="form_quantity"  onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')}"/>
                    </div>
                </div>
            </div>
        </div>
</div>
<!--当客户编码有问题的时候,增加客户编码-->
<div class="view_customer_panel" style="display:none;">
    <div class="model-base-box view_more" style="width:800px !important;height: auto;">
        <div class="model-form-field">
            <label>客户编码:</label>
            <input type="text" name="customerNo" id="form-customerNo" style="width:73%"/>
        </div>
        <div class="model-form-field">
            <label >客户名称:</label>
            <input type="tel" name="customerName" id="form-customerName" style="width:73%;"/>
        </div>
        <div class="model-form-field lineWidth">
            <label class="labe_l">省市:</label>
            <div class="inputBox model-form-field" style="display: inline; ">
                <select id="province" name="province" class="tex_t" style="width:18%;">
                    <option value="">--请选省份--</option>
                </select>
                <select id="city" name="city" class="tex_t" style="width:18%;margin-right: 4%;">
                    <option value="">--请选城市--</option>
                </select>
                <input type="tel" name="address" id="form-address" placeholder="详细地址" style="width:32%;"/>
            </div>
        </div>
        <div class="model-form-field">
            <label>联系人员:</label>
            <input type="tel" name="contactName" id="form-contactName" style="width:73%;"/>
        </div>
        <div class="model-form-field">
            <label>联系电话:</label>
            <input type="tel" name="contactPhone" id="form-contactPhone" style="width:73%;"/>
        </div>
        <div class="model-form-field">
            <label>传真:</label>
            <input type="tel" name="fax" id="form-fax" style="width:73%;"/>
        </div>
    </div>
</div>
<input type="hidden" class="startTxt" readonly/>
<input type="hidden" class="endTxt" readonly/>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker1.js"></script>
<script src="${baseStatic}plugin/js/cityselect/city.js"></script>
<script src="${baseStatic}plugin/js/cityselect/selectNew.js"></script>
<script>
    $(document).ready(function () {
        //全选事件
        $('#checkAll').on("click", function () {
            $("input[name='checksingle']").prop('checked', this.checked);
        });
        //单选事件
        $("input[name='checksingle']").on("click", function () {
            /*获取当前选中的个数，判断是否跟全部复选框的个数是否相等*/
            var all = $("input[name='checksingle']").length;
            var selects = $("input[name='checksingle']:checked").length;
            $('#checkAll').prop('checked', (all - selects == 0));
        });
        //翻页事件

        if($("#pageSize").length){
        	$("#pageSize").insertBefore(".pagination")
        }
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                $("form input[name='pageNum']").val($(this).attr("num"));
                commonSubmit();
            });
        });
        //刪除
        $(".order_delete").on("click", function () {
            var selects = $("input[name='checksingle']:checked").length;
            if(selects){
                var ids = new Array();
                $.each($("input[name='checksingle']:checked"), function (idx, value) {
                    ids.push($(value).val());
                });
                var paramData = {"ids":JSON.stringify(ids)};
                var url = base_url + "/backstage/moutai/order/delete";
                $.util.json(url, paramData, function (data) {
                    if (data.code == 'SUCCESS') {
                        $.util.success("操作成功", function () {
                            commonSubmit();
                        }, 2000)
                    }else{
                        $.util.warning(data.message);
                    }
                });
            }else{
                $.util.warning("请选择一条发货单");
            }
            });
        });
       //任务单打印事件
        $(".order-print-skip").on('click', function(){
            var ids = new Array;
            var flag = true;
            $.each($("input[name='checksingle']:checked"), function (idx, value) {
                ids.push($(value).val());
                var convey = $(value).parent().siblings(".check-conveyId");
                var conveyId = convey.html().trim();
                var storeId = convey.next().html().trim();
                if (typeof conveyId == "undefined" || conveyId == null || conveyId == "") {
                    flag = false;
                }
                if (typeof storeId == "undefined" || storeId == null || storeId == "") {
                    flag = false;
                }
            });
            if(!flag){
                $.util.error("请先选择仓库和承运单位后再进行打印操作!");
                return false;
            }
            if(ids == null || ids.length <= 0){
                $.util.error("请至少选择一条数据!");
                return false;
            }
            var url =  base_url + '/backstage/moutai/order/checkPrintData'
            var paramData = {"ids" : JSON.stringify(ids)};
              $.util.json(url, paramData, function (data) {
                   if (data.code == 'SUCCESS') {
                        location.href = base_url + '/backstage/moutai/order/toPrint?ids='+ids.join(',');
                    }else{
                        $.util.warning(data.message);
                    }
                });

        });
        //发货单导入
        $(".order-leading").on("click", function () {
            $.util.form('发货单导入', $(".import_task_panel").html(), function (model) {
                var $editBody = this.$body;
                $editBody.find("input:file").on('change', function () {
                    var fileName = this.value;
                    //校验文件格式
                    var fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (fileType != "xlsx" && fileType != "xls") {
                        $.util.error("选择的文件格式不支持！");
                        return false;
                    }
                    $(this).parent().parent().find("input[name='fileName']").val(fileName);
                })
                $editBody.find('#select_group').on('change',function(){
                    if($(this).val() == 17){
                        $editBody.find('.select-sender').show();
                    }else{
                        $editBody.find('.select-sender').hide();
                    }
                    $editBody.find('.close-x').trigger('click');
                })
                $editBody.find('#download-order-template').on('click', function(){
                    $.util.download($(this).attr('url'));
                })

            }, function () {
                var editBody = this.$body, formData = new FormData();
                var conveyId = editBody.find("select").val()
                if(conveyId < 0){
                    $.util.error("请选择需要导入的承运单位！");
                    return false;
                }
                formData.append("conveyKey",conveyId);
                var file = editBody.find("input:file");
                if (!file.val()) {
                    $.util.error("请选择需要导入的excel文件！");
                    return false;
                } else {
                    formData.append("file", file[0].files[0]);
                }
                $.ajax({
                    url: base_url + "/backstage/moutai/order/import/excel",
                    type: 'POST',
                    data: formData,
                    processData: false, // 告诉jQuery不要去处理发送的数据
                    contentType: false,// 告诉jQuery不要去设置Content-Type请求头
                    success: function (response) {
                        if (response.success) {
                            $.util.success(response.message, function () {
                                commonSubmit();
                            });
                        } else {
                            if (response.result.url) {
                                $.util.danger('有异常数据', response.message, function () {
                                    $.util.download(response.result.url);
                                    setTimeout(function(){
                                        submit(1);
                                    }, 1000);
                                });
                            } else {
                                $.util.error(response.message);
                            }
                        }
                    }
                });
            });
        });
    //仓库弹框
    $(".depot-leading").on("click", function () {
        var selects = $("input[name='checksingle']:checked").length;
        if(selects){
            $.util.form('发货仓库批量编辑', $(".import_depot_panel").html(), function () {
            },function () {
                var ids = new Array()
                var editBody = this.$body;
                var depot = editBody.find("#depotId").val();
                $.each($("input[name='checksingle']:checked"), function (idx, value) {
                    ids.push($(value).val());
                });
                var paramData = {"depot": depot, "ids": JSON.stringify(ids)};
                var url = base_url + "/backstage/moutai/add/depot";
                $.util.json(url, paramData, function (data) {
                    if (data.code == 'SUCCESS') {
                        $.util.success("操作成功", function () {
                            commonSubmit();
                        }, 2000)
                    }else{
                        $.util.warning(data.message);
                    }
                });
            });

        }else{
            $.util.warning("请选择一条发货单");
        }

    });
    //查询按钮事件
    $(".content-search-btn").on("click", function () {
        $("form input[name='pageSize']").val(10);
        $("form input[name='pageNum']").val(1);
        $("#searchDeliveryForm").submit();
    });

    var commonSubmit = function(){
        $("form input[name='pageSize']").val($("#pageSize").val());
        $("#searchDeliveryForm").submit();
    }
    var submit = function (num) {
        $("form input[name='pageNum']").val(num);
        commonSubmit();
    };

    function td_popBox(event, id, type, obj){
        var event = event || window.event,flag = true, keeps = $(obj).find('.keep');
        for (var i = 0; i < keeps.length ; i++) {
            console.log(event.target);
            console.log(keeps[i]);
            if (event.target === keeps[i]) {
                flag = false;
            }
        }
        console.log(flag);
        if (flag) {
            popBox(id , type);
        }

    }
    function popBox(id , type){
        $.util.form('发货单详情', $(".view_more_panel").html(),function () {
            //do something...
            var editBody = this.$body;
            if(type == 1){
                editBody.find("input").each(function () {
                    var $this = $(this);
                    $this.attr("readonly","readonly")
                })
                editBody.find("select").each(function () {
                    var $this = $(this);
                    $this.attr("disabled","ture")
                })
            }
            //初始化插件
            var selectObj = buildSelect(editBody.find("#province1"), editBody.find("#city1"));
            selectObj.init();
            $.util.json(base_url+'/backstage/moutai/editOrder/'+id, null, function(data){
                if(data.success){
                    editBody.find("input[name=id]").val(data.order.id)
                    editBody.find("input[name=scheduleNo]").val(data.order.scheduleNo)
                    editBody.find("input[name=customerNo]").val(data.order.customerNo)
                    editBody.find("input[name=buyOrg]").val(data.order.buyOrg)

                    editBody.find("input[name=contactName]").val(data.order.contactName)
                    editBody.find("input[name=contactTel]").val(data.order.contactTel)
                    console.log(selectObj);

                    selectObj.setValue(data.order.province, data.order.city);
                    // setTimeout(function(){
                    //     console.log('延迟后...')
                    //     console.log(editBody.find("select[name=currentprovince]")[0]);
                    //     editBody.find("select[name=currentprovince]").val(data.order.province);
                    //     editBody.find("select[name=currentcity]").val(data.order.city);
                    //
                    //     //province = editBody.find("select[name=currentprovince]");
                    //     //city = editBody.find("select[name=currentcity]");
                    //     // province.on("change", function () {
                    //     //     selectName('province', province);
                    //     // });
                    //     // city.on("change", function () {
                    //     //     selectName("city", city);
                    //     // });
                    //     //province.val(data.order.province).change().next().val(data.order.city);
                    // },5000)
                    editBody.find("input[name=currentaddress]").val(data.order.address);
                    editBody.find("select[name=storeId] option[value='"+data.order.storeId +"']").attr("selected",true);
                    editBody.find("select[name=conveyId] option[value='"+data.order.conveyId +"']").attr("selected",true);
                    editBody.find("input[name=specification]").val(data.order.specification)
                    editBody.find("input[name=bottles]").val(data.order.bottles)
                    editBody.find("input[name=quantity]").val(data.order.quantity)
                }else{
                    $.util.error(data.message);
                }
            });
        },function () {
            if(type == 2){
                var action_url = base_url + '/backstage/moutai/order/update';
                var editBody = this.$body, form = new FormData();
                form.append("id",editBody.find("#form_id").val());
                form.append("scheduleNo",editBody.find("#form_scheduleNo").val());
                form.append("customerNo",editBody.find("#form_customerNo").val());
                form.append("buyOrg",editBody.find("#form_buyOrg").val());

                form.append("contactName",editBody.find("#form_contactName").val());
                form.append("contactTel",editBody.find("#form_contactTel").val());
                form.append("province",editBody.find("#province1").val());
                form.append("city",editBody.find("#city1").val());
                form.append("address",editBody.find("#form_currentaddress").val());

                form.append("storeId",editBody.find("#form_storeId").val());
                form.append("conveyId",editBody.find("#form_conveyId").val());
                form.append("specification",editBody.find("#form_specification").val());
                form.append("bottles",editBody.find("#form_bottles").val());
                form.append("quantity",editBody.find("#form_quantity").val());
                $.ajax({
                    url: base_url + "/backstage/moutai/order/update",
                    type: 'POST',
                    data: form,
                    processData: false, // 告诉jQuery不要去处理发送的数据
                    contentType: false,// 告诉jQuery不要去设置Content-Type请求头
                    success: function (data) {
                        if (data.code == 'SUCCESS') {
                            $.util.success("操作成功", function () {
                                commonSubmit();
                            }, 2000)
                        }else{
                            $.util.warning(data.message);
                        }
                    }
                });
            }else{
                //nothing to do
            }
        });
    }

    function add_customer(customerNo) {
        $.util.form('新增收货客户', $(".view_customer_panel").html(),function () {
            var editBody = this.$body;

            buildSelect(editBody.find("#province"), editBody.find("#city")).init();

            editBody.find("#form-customerNo").attr("value",customerNo);
            editBody.find("#form-customerNo").attr("readonly","readonly");
        },function () {
            var $editBody = this.$body;
            var customerNo = $editBody.find("#form-customerNo").val();
            var customerName = $editBody.find("#form-customerName").val();
            var contactName = $editBody.find("#form-contactName").val();
            var contactPhone = $editBody.find("#form-contactPhone").val();
            var province =  $editBody.find("#province").val();
            var city = $editBody.find("#city").val();
            var address = $editBody.find("#form-address").val();
            if(!customerNo){
                $.util.error("请输入收货客户编号!");
                return false;
            }
            if (!customerName) {
                $.util.error("请输入收货客户名称!");
                return false;
            }
            if(!province){
                $.util.error("请选择省份!");
                return false;
            }
            if(!city){
                $.util.error("请选择市区!");
                return false;
            }
            if(!address){
                $.util.error("请输入地址!");
                return false;
            }
            var paramData = {
                "customerNo": customerNo,
                "province":  province,
                "city":  city,
                "customerName": customerName,
                "address":  address,
                "contactName":  contactName,
                "contactPhone":  contactPhone,
                "fax":  $editBody.find("#form-fax").val(),
            };
            var url = base_url + "/backstage/moutai/customer/add";
            $.util.json(url, paramData, function (data) {
                if (data.code == 'SUCCESS') {
                    $.util.success("操作成功", function () {
                        $("#searchDeliveryForm").submit();
                    }, 2000)
                }else{
                    $.util.warning(data.message);
                    return false;
                }
            });
        });
    }
</script>
</html>
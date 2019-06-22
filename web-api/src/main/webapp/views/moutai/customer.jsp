<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>茅台打印-收货客户管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
    <link rel="stylesheet" href="${baseStatic}css/inventory.css?times=${times}"/>
    <style type="text/css">
    .content-search .col-min{width:20%;}
    .content-search .col-min1{width:10%;}
    .customer-delete{background:#ff7f00 !important;}
    .view_more>.model-form-field{width:50%;float:left;}
    .view_more>.lineWidth{width:100%;}

    .downloadBtn{color:#31acfa;text-decoration: underline !important;}
    </style>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchCustomerForm" action="${basePath}/backstage/moutai/queryCustomerList" method="post">
        <input type="hidden" name="pageNum" value="${search.pageNum}">
        <input type="hidden" name="pageSize" value="10">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">客户编号:</label>
                    <input type="text" class="tex_t" name="customerNo"  value="${search.customerNo}" >
                </div>
                <div class="fl col-min">
                    <label class="labe_l">客户名称:</label>
                    <input type="text" class="tex_t" name="customerName" value="${search.customerName}" >
                </div>
                <div class="fl col-min">
                    <label class="labe_l">联系人员:</label>
                    <input type="text" class="tex_t" name="contactName" value="${search.contactName}" >
                </div>
                <div class="fl col-min">
                    <label class="labe_l">联系电话:</label>
                    <input type="text" class="tex_t" name="contactPhone" value="${search.contactPhone}" >
                </div>
                <div class="fl col-min1">
                    <button type="button" class="btn btn-default content-search-btn">查询</button>
                </div>
            </div>
        </div>
    </form>
    <div class="btnBox">
        <button type="button" class="btn btn-default customer-add">添加</button>
        <button type="button" class="btn btn-default customer-leading">导入</button>
        <button type="button" class="btn btn-default customer-delete">删除</button>
    </div>
    <div class="table-style">
        <table class="dtable" id="trackTable">
            <thead>
            <tr>
                <th width="50" style="text-align:left;text-indent:10px;"><input type="checkbox" id="checkAll">全选</th>
                <th width="100">客户编码</th>
                <th width="150">客户名称</th>
                <th width="90">省份</th>
                <th width="90">城市</th>
                <th width="200">注册地址</th>
                <th width="100">联系人员</th>
                <th width="90">联系电话</th>
                <th width="90">传真</th>
                <th width="80">操作</th>
            </tr>
            </thead>
            <tbody>
                <c:forEach items="${page.collection}" var="customer">
                <tr>
                    <td style="text-align:left;text-indent:10px;"><input  type="checkbox" name="checksingle" value="${customer.customerNo}"></td>
                    <td>${customer.customerNo}</td>
                    <td <%--style="text-align:left;"--%>>
                        <c:out value="${customer.customerName}"/>
                    </td>
                    <td>${customer.province eq 'null' ? "":customer.province}</td>
                    <td>${customer.city eq 'null' ? "":customer.city} </td>
                    <td style="text-align:left;">
                        <c:out value="${customer.address}"/>
                    </td>
                    <td>
                        <c:out value="${customer.contactName}"/>
                    </td>
                    <td>${customer.contactPhone}</td>
                    <td>${customer.fax}</td>
                    <td>
                        <a class="aBtn viewMoreBtn" onclick="_popBox('${customer.customerNo}' , 1)">查看</a>
                        <a class="aBtn editBtn" onclick="_popBox('${customer.customerNo}' , 2)">编辑</a>
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


<!-- 收货客户导入 -->
<div class="import_task_panel" style="display: none">
    <div class="model-base-box import_task" style="width: 480px;">
        <form id="importForm" action="${basePath}/" enctype="multipart/form-data"  method="post">
            <div class="model-form-field">
            	<label>导入模版:</label>
                <a href="javascript:void(0)" class="downloadBtn" id="download-customer-template" url="${template}">收货客户导入模板下载.xls</a>
                </select>
            </div>
            <div class="model-form-field file_box">
                <label>选择文件:</label>
                <input type="text" name="fileName" id="fileName" readonly/>
                <span class="span-txt">请选择文件</span>
                <a href="javascript:;" class="file">
                    <input name="file" type="file" accept=".xls,.xlsx" id="uploadFile"/>
                </a>
            </div>
        </form>
    </div>
</div>
<!--查看详情弹出框-->
<div class="view_more_panel" style="display:none;">
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
                <input type="tel" name="address" id="form-address" placeholder="详细地址" style="width:45.5%;"/>
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
<script src="${baseStatic}plugin/js/cityselect/select.js"></script>
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
        //查询按钮事件
        $(".content-search-btn").on("click", function () {
            $("#searchCustomerForm").submit();
        });
        //翻页事件
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                $("form input[name='pageNum']").val($(this).attr("num"));
                $("#searchCustomerForm").submit();
            });
        });

        //收货客户导入
        $(".customer-leading").on("click", function () {
            $.util.form('收货客户导入', $(".import_task_panel").html(), function (model) {
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
                $editBody.find('#download-customer-template').on('click', function(){
                    $.util.download($(this).attr('url'));
                })
            }, function () {
                var editBody = this.$body, formData = new FormData();
                var file = editBody.find("input:file");
                if (!file.val()) {
                    $.util.error("请选择需要导入的excel文件！");
                    return false;
                } else {
                    formData.append("file", file[0].files[0]);
                }
                $.ajax({
                    url: base_url + "/backstage/moutai/customer/import/excel",
                    type: 'POST',
                    data: formData,
                    processData: false, // 告诉jQuery不要去处理发送的数据
                    contentType: false,// 告诉jQuery不要去设置Content-Type请求头
                    success: function (response) {
                        if (response.success) {
                            $("#searchCustomerForm").submit();
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
        //刪除
        $(".customer-delete").on("click", function () {
            var selects = $("input[name='checksingle']:checked").length;
            if(selects){
                var customerNos = new Array();
                $.each($("input[name='checksingle']:checked"), function (idx, value) {
                    customerNos.push($(value).val());
                });
                var paramData = {"customerNos":JSON.stringify(customerNos)};
                var url = base_url + "/backstage/moutai/customer/delete";
                $.util.json(url, paramData, function (data) {
                    if (data.code == 'SUCCESS') {
                        $.util.success("操作成功", function () {
                            $("#searchCustomerForm").submit();
                        }, 2000)
                    }else{
                        $.util.warning(data.message);
                    }
                });
            }else{
                $.util.warning("请选择一条收货客户信息");
            }

        });
    });

    $(".customer-add").on("click", function () {
        $.util.form('新增收货客户', $(".view_more_panel").html(),function () {
            var editBody = this.$body;
            province = editBody.find("#province");
            city = editBody.find("#city");
            province.on("change", function () {
                selectName('province', province);
            });
            city.on("change", function () {
                selectName("city", city);
            });
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
          /*  if(!customerName){
                $.util.error("请输入收货客户名称!");
                return false;
            }*/
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
            /*  if(!contactName){
                $.util.error("请输入收货联系人!");
                return false;
            }
            if(!contactPhone){
                $.util.error("请输入收货联系电话!");
                return false;
            }
            if(!(/^1[\d]{10}$/.test(contactPhone))){
                $.util.error("收货联系人电话格式有误!");
                return false;
            }*/
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
                        $("#searchCustomerForm").submit();
                    }, 2000)
                }else{
                    $.util.warning(data.message);
                    return false;
                }
            });
        });
    });
    var submit = function (num) {
        $("form input[name='num']").val(num);
        $("#searchCustomerForm").submit();
    };
    function _popBox(customerNo , type){
        $.util.form('收货客户详情', $(".view_more_panel").html(),function () {
            var editBody = this.$body;

            editBody.find(".customer-add-page").hide();
            if(type == 1){
                editBody.find("input").each(function () {
                    var $this = $(this);
                    $this.attr("readonly","readonly")
                })
                editBody.find("select").each(function () {
                    var $this = $(this);
                    $this.attr("disabled","disabled")
                })
            }else{
                editBody.find("#form-customerNo").attr("readonly","readonly");
            }
            $.util.json(base_url+'/backstage/moutai/editCustomer/'+customerNo, null, function(data){
                if(data.success){
                    var customer = data.customer;
                    editBody.find("input[name=customerNo]").val(customer.customerNo);
                    editBody.find("input[name=customerName]").val(data.customer.customerName)
                    editBody.find("input[name=address]").val(customer.address);
                    editBody.find("input[name=contactName]").val(customer.contactName);
                    editBody.find("input[name=contactPhone]").val(customer.contactPhone);
                    editBody.find("input[name=fax]").val(customer.fax);
                    editBody.find("select[name=province]").val(customer.province);
                    province = editBody.find("#province");
                    city = editBody.find("#city");
                    province.on("change", function () {
                        selectName('province', province);
                    });
                    city.on("change", function () {
                        selectName("city", city);
                    });
                    province.val(customer.province).change().next().val(customer.city);
                }else{
                    $.util.error(data.message);
                }
            });
        },function () {
            if(type == 2){
                var editBody = this.$body, form = new FormData();
                var contactPhone = editBody.find("#form-contactPhone").val();
               /* if(!contactPhone){
                    $.util.error("请输入收货联系电话!");
                    return false;
                }
                if(!(/^1[\d]{10}$/.test(contactPhone))){
                    $.util.error("收货联系人电话格式有误!");
                    return false;
                }*/
                var action_url = base_url + '/backstage/moutai/customer/update';
                form.append("customerNo",editBody.find("#form-customerNo").val());
                form.append("province",editBody.find("#province").val());
                form.append("city",editBody.find("#city").val());
                form.append("customerName",editBody.find("#form-customerName").val());
                form.append("address",editBody.find("#form-address").val());
                form.append("contactName",editBody.find("#form-contactName").val());
                form.append("contactPhone",contactPhone);
                form.append("fax",editBody.find("#form-fax").val());
                $.ajax({
                    url: base_url + "/backstage/moutai/customer/update",
                    type: 'POST',
                    data: form,
                    processData: false, // 告诉jQuery不要去处理发送的数据
                    contentType: false,// 告诉jQuery不要去设置Content-Type请求头
                    success: function (data) {
                        if (data.code == 'SUCCESS') {
                            $.util.success("操作成功", function () {
                                $("#searchCustomerForm").submit();
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

</script>
</html>
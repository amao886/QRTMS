<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-我的任务</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/taskList.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
    
    <link rel="stylesheet" href="${baseStatic}css/sendcustomer.css?times=${times}"/>
    
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
        <form id="searchWaybillForm" action="${basePath}/backstage/trace/search" method="post">
            <input type="hidden" name="num" value="${search.num }">
            <input type="hidden" name="size" value="14">
            <div class="selectCondition">
                <select name="groupid" id="groupSelect" val="${search.groupid}">
                    <c:forEach items="${groups}" var="group">
                        <option value="${ group.id}">${group.groupName }</option>
                    </c:forEach>
                    <option value="0">其他</option>
                </select>
                <select name="TimeSearch" id="TimeSearch" val="${search.TimeSearch}">
                    <option value="3">近三天发货任务</option>
                    <option value="7">近一周发货任务</option>
                    <option value="30">近一个月发货任务</option>
                    <option value="0">全部日期</option>
                </select>
                <ul class="tabSelect">
                    <li>
                        <label class="transport-status active"><input type="radio" name="waybillFettles" value="" val="${search.waybillFettles}"/>全部</label>
                    </li>
                    <li>
                        <label class="transport-status"><input type="radio" name="waybillFettles" value="10" val="${search.waybillFettles}"/>待绑定</label>
                    </li>
                    <li>
                        <label class="transport-status"><input type="radio" name="waybillFettles" value="20" val="${search.waybillFettles}"/>已绑定</label>
                    </li>
                    <li>
                        <label class="transport-status"><input type="radio" name="waybillFettles" value="30" val="${search.waybillFettles}"/>运输中</label>
                    </li>
                    <li>
                        <label class="transport-status"><input type="radio" name="waybillFettles" value="35" val="${search.waybillFettles}"/>已送达</label>
                    </li>
                    <li>
                        <label class="transport-status"><input type="radio" name="waybillFettles" value="40" val="${search.waybillFettles}"/>确认到货</label>
                    </li>
                </ul>
                <span class="more" id="moreCon"><span>更多搜索条件</span><i class="icon"></i></span>
            </div>
            <div class="layui-fluid moreCondition" style="display:none;">
                <div class="layui-row layui-col-space10">
                    <div class="layui-col-md4 layui-col-lg2">
                        <div class="line-part">
                            <span class="float-tag">搜索条件</span>
                            <div class="text-tag"><input id="likeString" type="text" class="sub-tag" name="likeString"
                                                         placeholder="任务单号/送货单号/收货客户" value="${search.likeString}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg2">
                        <div class="line-part">
                            <span class="float-tag">始发地</span>
                            <div class="text-tag"><input type="text" class="sub-tag" id="startStation" name="startStation"
                                                         value="${search.startStation}"/></div>
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg2">
                        <div class="line-part">
                            <span class="float-tag">目的地</span>
                            <div class="text-tag"><input type="text" class="sub-tag" id="endStation" name="endStation"
                                                         value="${search.endStation}"/></div>
                        </div>
                    </div>
                    <div class="layui-col-md4 layui-col-lg2">
                        <div class="line-part line-btn">
                            <button class="layui-btn layui-btn-normal submit" type="button">查询</button>
                            <a class="layui-btn layui-btn-normal" onclick="remove()">重置</a>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
    <!-- 表格部分 -->
    <div class="table-style">
        <div class="handle-box">
            <button class="layui-btn layui-btn-normal" onclick="window.location.href='${basePath}/backstage/trace/send/goods'">我要发货</button>
            <button class="layui-btn layui-btn-normal btn-print">任务单打印</button>
            <button class="layui-btn layui-btn-normal btn_task_import">批量发货</button>
            <button class="layui-btn layui-btn-normal" url="${template}" id="down_template">模板下载</button>
            <button class="layui-btn layui-btn-warm link_sure_batch">确认到货</button>
            <button class="layui-btn layui-btn-danger link_delete">删除</button>
            <button class="layui-btn layui-btn-normal" id="batch_bind">批量綁定</button>
            <button class="layui-btn layui-btn-normal" id="batch_down">已綁定任务单下载</button>
            <!--<i class="layui-icon pos_right delete-icon link_delete">&#xe640;</i>-->
        </div>
        <table>
            <thead>
            <tr>
                <th width="30">
                    <input type="checkbox" id="checkAll"/>
                </th>
                <th>任务单号</th>
                <th>送货单号</th>
                <th>项目组</th>
                <th>始发地</th>
                <th>目的地</th>
                <th>发货日期</th>
                <th>要求到货时间</th>
                <th width="20%">收货客户</th>
                <th width="65">状态</th>
                <th width="134">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.collection }" var="waybill">
                <tr>
                    <td><input type="checkbox" data-barcode="${waybill.barcode}" name="waybillId" value="${waybill.id}"/></td>
                    <td><a class="aBtn" href="${basePath}/backstage/trace/findById/${waybill.id}" target="_blank">${waybill.barcode}</a>
                    </td>
                    <td>${waybill.deliveryNumber}</td>
                    <td>
                        <c:if test="${waybill.group != null}">
                            ${waybill.group.groupName}
                        </c:if>
                        <c:if test="${waybill.group == null}">
                            个人
                        </c:if>
                    </td>
                    <td>${waybill.simpleStartStation}</td>
                    <td>${waybill.simpleEndStation}</td>
                    <td>
                        <c:if test="${waybill.deliveryTime != null}">
                            <fmt:formatDate value="${waybill.deliveryTime}" pattern="yyyy-MM-dd"/>
                        </c:if>
                        <c:if test="${waybill.deliveryTime == null}">
                            未发货
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${waybill.arrivaltime == null}">无要求</c:if>
                        <c:if test="${waybill.arrivaltime != null}">
                            <c:if test="${waybill.waybillStatus == 10}">
                                发货后${waybill.arriveDay}天${waybill.arriveHour}点前
                            </c:if>
                            <c:if test="${waybill.waybillStatus > 10}">
                                <fmt:formatDate value="${waybill.arrivaltime}" pattern="yyyy-MM-dd HH:mm"/>
                            </c:if>
                        </c:if>
                    </td>
                    <td>${waybill.receiverName}</td>
                    <td><c:if test="${waybill.waybillStatus== 10 }">未绑定</c:if>
                        <c:if test="${waybill.waybillStatus== 20 }">已绑定</c:if>
                        <c:if test="${waybill.waybillStatus== 30 }">运输中</c:if>
                        <c:if test="${waybill.waybillStatus== 35 }">已送达</c:if>
                        <c:if test="${waybill.waybillStatus== 40 }">确认到货</c:if>
                    </td>
                    <td>
                        <a class="aBtn" href="${basePath}/backstage/trace/findById/${waybill.id}">查看</a>
                        <a class="aBtn" href="${basePath}/backstage/trace/eidtview/${waybill.id}">编辑</a>
                        <span class="others_val" waybillid="${waybill.id}" groupid="${waybill.groupid }"
                              weight="${waybill.weight }" volume="${waybill.volume }" number="${waybill.number }"
                              verifyTotal="${waybill.receiptCount}" verifyCount="${waybill.receiptVerifyCount}"></span>
                        <c:if test="${waybill.waybillStatus > 10 && waybill.waybillStatus < 40}">
                            <a href="javascript:;" class="aBtn link_sure">确认到货</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="page-item">
            <c:if test="${page.collection != null && fn:length(page.collection) > 0}">
                <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}" pages="${page.pages}"
                                 total="${page.total}"/>
            </c:if>
        </div>
    </div>
</div>
<!-- 上传导入 -->
<div class="import_task_panel" style="display: none">
    <div class="model-base-box import_task" style="width: 480px;">
        <form id="importForm" action="${basePath}/backstage/trace/import/excel" enctype="multipart/form-data"
              method="post">
            <div class="model-form-field">
                <label>项目组:</label>
                <select id="select_group" class="tex_t selec_t" name="groupid">
                    <option value="0">--请选择项目组--</option>
                    <c:forEach items="${groups}" var="group">
                        <option value="${group.id}">${group.groupName }</option>
                    </c:forEach>
                </select>
            </div>
            <div class="model-form-field select-sender" style="display: none;">
                <label>发货方:</label>
                <input type="hidden"name="senderKey">
                <input type="text" class="tex_t" name="senderName" readonly>
                <span class="close-x"><i class="layui-icon">&#x1007;</i></span>
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
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker1.js"></script>
<%-- <script src="${baseStatic}plugin/js/jquery-hcheckbox.js"></script> --%>
<script src="${baseStatic}plugin/js/jquery.mloading.js"></script>
<script src="${baseStatic}plugin/js/jquery.cookie.js"></script>
<script src="${baseStatic}js/datetimepicker.js"></script>
<script src="${baseStatic}js/select.resource.js?times=${times}"></script>
<script>

    function remove() {
        $("input[name='likeString']").val("");
        $("input[name='startStation']").val("");
        $("input[name='endStation']").val("");
    }

    $(document).ready(function () {
        var submit = function (num) {
            $("form input[name='num']").val(num);
            $("#searchWaybillForm").submit();
        }
        $('select, :radio').each(function(){
            var target = $(this), val = target.attr("val");
            if(val && target.is('select')){
                target.val(val);
            }
            if(val && target.is(':radio')){
                if(target.val() == val){
                    target.attr("checked", true);
                    target.parent().addClass('active')
                }else{
                    target.attr("checked", false);
                    target.parent().removeClass('active');
                }
            }
        });


        //翻页事件
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                submit($(this).attr("num"));
            });
        });
        //全选事件
        $('#checkAll').on("click", function () {
            $("input[name='waybillId']").prop('checked', this.checked);
        });

        $("select#groupSelect").change(function () {
            submit(1);
        });
        $("select#TimeSearch").change(function () {
            submit(1);
        });
        $(":radio").click(function () {
            submit(1);
        })
        $(".submit").click(function () {
            submit(1);
        });
        //任务单模板下载
        $("#down_template").click(function(){
            $.util.download($(this).attr("url"));
        });
        //批量上传
        $(".btn_task_import").on("click", function () {
            $.util.form('批量上传', $(".import_task_panel").html(), function (model) {
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
                $editBody.find("input[name='senderName']").on('click', function(){
                    var target = $(this), targetKey = target.prev();
                    target.selectCustomer({
                        selectType: 2,
                        selectClose: true,
                        selectKey: targetKey.val(),
                        selectGroup: $editBody.find('#select_group').val(),
                        selectFun: function(model){
                            targetKey.val(model.id);
                            target.val(model.companyName);
                        }
                    });
                });
                //清空发货方信息
                $editBody.find('.close-x').on('click',function(){
                    $(this).prev().val('').attr('placeholder','点击选择发货方').prev().val('');
                })
            }, function () {
                var editBody = this.$body, formData = new FormData();
                var groupId = editBody.find("select").val()
                if (!groupId) {
                    formData.append("groupid", 0);
                }else{
                    formData.append("groupid", groupId);
                }
                formData.append("senderKey", editBody.find("input[name='senderKey']").val());
                var file = editBody.find("input:file");
                if (!file.val()) {
                    $.util.error("请选择需要导入的excel文件！");
                    return false;
                } else {
                    formData.append("file", file[0].files[0]);
                }
                $.ajax({
                    url: base_url + "/backstage/trace/import/excel",
                    type: 'POST',
                    data: formData,
                    processData: false, // 告诉jQuery不要去处理发送的数据
                    contentType: false,// 告诉jQuery不要去设置Content-Type请求头
                    success: function (response) {
                        if (response.success) {
                            $.util.success(response.message, function () {
                                window.location.href = base_url + response.url;
                            });
                        } else {
                            if (response.url) {
                                $.util.danger('有异常数据', response.message, function () {
                                    $.util.download(response.url);
                                    $("select[name='waybillFettles']").val(10);
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

        //删除事件
        $(".link_delete").on("click", function () {
            var chk_value = [], parmas = {};
            $('input[name="waybillId"]:checked').each(function () {
                chk_value.push($(this).val());
            });
            if (chk_value == null || chk_value == "") {
                $.util.error("请至少选择一条数据");
                return false;
            }
            $.util.confirm('删除确认', '是否确认删除选中的数据？', function () {
                $.util.json(base_url + '/backstage/trace/delete', {waybillIds: chk_value.join(',')}, function (data) {
                    if (data.success) {//处理返回结果
                        submit(1);
                    } else {
                        $.util.error(data.message);
                    }
                });
            });
        });
        //批量确认到货事件
        $(".link_sure_batch").on("click", function () {
            var chk_value = [];
            $('input[name="waybillId"]:checked').each(function () {
                chk_value.push($(this).val());
            });
            if (chk_value == null || chk_value == "") {
                $.util.error("请至少选择一条任务单");
                return false;
            }
            $.util.confirm('到货确认', "您选择了"+ chk_value.length +"条任务单,确定要继续操作吗?", function () {
                $.util.json(base_url + '/backstage/trace/confirm/receive', {waybillIds: chk_value.join(',')}, function (data) {
                    if (data.success) {//处理返回结果
                        submit(1);
                    } else {
                        $.util.error(data.message);
                    }
                });
            });
        });
        //确认到货事件
        $(".link_sure").on("click", function () {
            var my = $(this);
            var obj = $(my).parents("td").find(".others_val");
            var waybillid = obj.attr("waybillid"), total = obj.attr("verifyTotal"), count = obj.attr("verifyCount");
            var cal_btn, msg;
            if (total) {
                msg = '是否确认到货?';
                if (Number(total) > Number(count)) {
                    msg = '有未审核回单,确认后回单自动设置为合格,是否确认到货?'
                    cal_btn = {
                        text: '前往回单审核', action: function () {
                            location.href = base_url + '/backstage/receipt/search?waybill.id=' + waybillid;
                        }
                    };
                }
            } else {
                msg = '还未上传回单,是否确认到货?';
            }
            $.util.confirm('到货确认', msg, function () {
                $.util.json(base_url + '/backstage/trace/confirm/receive', {waybillIds: waybillid}, function (data) {
                    if (data.success) {//处理返回结果
                        submit(1);
                    } else {
                        $.util.error(data.message);
                    }
                });
            }, cal_btn);
        });
        //任务单打印事件
        $(".btn-print").on('click', function(){
            var waybillKeys = [];
            var groupId = $("#groupSelect option:selected").val();
            $('input[name="waybillId"]:checked').each(function () {
                waybillKeys.push($(this).val());
            });
            if (waybillKeys == null || waybillKeys.length <= 0) {
                $.util.error("请至少选择一条数据");
                return false;
            }
            location.href = base_url + '/backstage/trace/print/page?waybillKeys=' + waybillKeys.join(',') +"&groupId=" + groupId;
        });

        //tab切换
        $('.transport-status').click(function () {
            $('.transport-status').removeClass('active');
            $(this).addClass('active');
        })

        //判断更多搜索条件是否展示
        if ($.cookie('open') == 1) {
            $('.moreCondition').show();
            $('#moreCon').addClass('open-hook').children('span').text('收起更多').next().addClass('up');
        }

        //更多搜索条件显示
        $('#moreCon').on('click', function () {
            if ($(this).hasClass('open-hook')) {
                $('.moreCondition').hide();
                $(this).removeClass('open-hook').children('span').text('更多搜索条件').next().removeClass('up');
                $.cookie('open', '2');
            } else {
                $('.moreCondition').show();
                $(this).addClass('open-hook').children('span').text('收起更多').next().addClass('up');
                $.cookie('open', '1');
            }
        })
        
        
        //批量绑定
        $("#batch_bind").on("click", function () {
            var chk_value = [], parmas = {};
            $('input[name="waybillId"]:checked').each(function () {
                chk_value.push($(this).val());
            });
            if (chk_value && chk_value != "") {
            	parmas["waybillIds"] = chk_value.join(',')
            }
            var groupId = $("#groupSelect option:selected").val();
            parmas["groupId"] = groupId;
            if (!groupId) {
                $.util.error("请至少选择项目组");
                return false;
            }
            var deliverStartTime = $("#deliverStartTime").val();
            if(deliverStartTime && deliverStartTime !=""){
            	parmas["deliverStartTime"] = deliverStartTime;
            }
            
            var deliverEndTime = $("#deliverEndTime").val();
            if(deliverEndTime && deliverEndTime != ""){
            	parmas["deliverEndTime"] = deliverEndTime;
            }
            // 请求
            $.util.json(base_url + '/backstage/trace/batchBind', parmas, function (data) {
                if (data.success) {//处理返回结果
                	submit(1);
                } else {
                    $.util.error(data.message);
                }
            });
        });
        
        //批量下载
        $(".batch_down").on("click", function () {
            var chk_value = [], parmas = {};
            $('input[name="waybillId"]:checked').each(function () {
                chk_value.push($(this).val());
            });
            if (chk_value == null || chk_value == "") {
                $.util.error("请至少选择一条数据");
                return false;
            }
            /* $.util.json(base_url + '/backstage/trace/downBind' + resid, null, function (response) {
                if (response.success) {
                	if(response.url){
                		var msg = "二维码PDF文件下载,数量 : <font color='red'>" + response.count + "</font>个,大小 : <font color='red'>" + response.size + "</font>MB";
							$.util.success(msg, function(){
								$.fileDownload(response.url);
								$("#"+ resid +"printStatus").html("已下载");
							}, 3000);
						}else{
							$.util.error(response.message);
						} 
                } else {
                    $.util.error(response.message);
                }
            }); */
        });
    });
</script>
</html>
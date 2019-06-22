<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>茅台打印-取单人管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
    <link rel="stylesheet" href="${baseStatic}css/inventory.css?times=${times}"/>
</head>
<body>
<div class="history-content clearfix">
    <div class="btnBox" style="margin-top:15px;">
        <button type="button" class="btn btn-default taker-add">添加</button>
    </div>
    <div class="table-style">
        <table class="dtable" id="trackTable">
            <thead>
            <tr>
                <th width="50" style="text-align:left;text-indent:10px;"><input type="checkbox" id="checkAll">全选</th>
                <th width="100">承运单位</th>
                <th width="100">取单人员</th>
                <th width="150">联系方式</th>
                <th width="200">身份证号码</th>
                <th width="100">添加日期</th>
                <th width="100">使用状态</th>
                <th width="100">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.collection}" var="taker">
            <tr>
                <td style="text-align:left;text-indent:10px;"><input  type="checkbox" name="checksingle" value="${taker.id}"></td>
                <td>
                    <c:forEach items="${conveyList}" var="convey">
                        <c:if test="${convey.id == taker.conveyId}">${convey.conveyName}</c:if>
                    </c:forEach>
                </td>
                <td>
                    <c:out value="${taker.takeName}"/>
                </td>
                <td>${taker.takePhone}</td>
                <td>${taker.takeIdcare}</td>
                <td><fmt:formatDate value="${taker.createTime}" pattern="yyyy-MM-dd"/> </td>
                <td>
                    <c:if test="${taker.fettle == 0 }">禁用</c:if>
                    <c:if test="${taker.fettle == 1 }">启用</c:if>
                </td>
                <td>
                    <a class="aBtn viewMoreBtn" onclick="popBox(${taker.id} , 1)">查看</a>
                    <a class="aBtn editBtn" onclick="popBox(${taker.id} , 2)">编辑</a>
                </td>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${page.collection != null && fn:length(page.collection) > 0}">
            <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}" pages="${page.pages}"
                             total="${page.total}"/>
        </c:if>
    </div>
</div>

<form id="searchTakerForm" action="${basePath}/backstage/moutai/queryTakerList" method="post">
    <input type="hidden" name="pageNum" value="${search.pageNum}">
    <input type="hidden" name="pageSize" value="10">
</form>
<!-- 增加承运单位 -->
<div class="import_taker_panel" style="display: none">
    <div class="model-base-box" style="width:500px;height: auto;">
        <input type="hidden" id="form-id" name="id">
        <div class="model-form-field">
            <label class="labe_l">承运单位:</label>
            <select id="form-conveyId" name="conveyId"  class="tex_t selec_t" style="width:78%" >
                <c:forEach items="${conveyList}" var="convey">
                    <option value="${convey.id}" >${convey.conveyName}</option>
                </c:forEach>
            </select>
        </div>
        <div class="model-form-field">
            <label class="labe_l">制单人员:</label>
            <input type="text" class="tex_t" id="form-takeName" name="takeName" style="width:78%" >
        </div>
        <div class="model-form-field">
            <label class="labe_l">联系方式:</label>
            <input type="text" class="tex_t" id="form-takePhone" name="takePhone" style="width:78%">
        </div>
        <div class="model-form-field">
            <label class="labe_l">身份证号:</label>
            <input type="text" class="tex_t" id="form-takeIdcare" name="takeIdcare" style="width:78%">
        </div>
        <div class="model-form-field">
            <label class="labe_l">使用状态:</label>
            <select id="form-fettle" class="tex_t selec_t" name="fettle" style="width:78%">
                <option value="0">--禁用--</option>
                <option value="1">--启用--</option>
            </select>
        </div>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker1.js"></script>
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
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                $("form input[name='pageNum']").val($(this).attr("num"));
                $("#searchTakerForm").submit();
            });
        });
    });
    //新增弹出框
    $(".taker-add").on("click", function () {
        $.util.form('新增取单人', $(".import_taker_panel").html(),function() {
        //do nothing
        },function () {
            var $editBody = this.$body;
            var takeName = $editBody.find("#form-takeName").val();
            var takePhone = $editBody.find("#form-takePhone").val();
            var takeIdcare = $editBody.find("#form-takeIdcare").val();
            if(!takeName){
                $.util.error("请输入联系人姓名!");
                return false;
            }
            if(!takePhone){
                $.util.error("请输入联系人电话!");
                return false;
            }
            if(!(/^1[\d]{10}$/.test(takePhone))){
                $.util.error("联系人电话格式有误!");
                return false;
            }
            if(!takeIdcare){
                $.util.error("请输入身份证号码!");
                return false;
            }
            if(takeIdcare.length < 15 || takeIdcare.length > 18){
                $.util.error("身份证位数不准确,请重新输入!");
                return false;
            }
            var paramData = {
                "conveyId":  $editBody.find("#form-conveyId").val(),
                "takeName": takeName,
                "takePhone": takePhone,
                "takeIdcare": takeIdcare,
                "fettle": $editBody.find("#form-fettle").val()
            };
            var url = base_url + "/backstage/moutai/taker/add";
            $.util.json(url, paramData, function (data) {
                if (data.code == 'SUCCESS') {
                    $.util.success("操作成功", function () {
                        $("#searchTakerForm").submit();
                    }, 2000)
                }else{
                    $.util.warning(data.message);
                }
            });

        });
    });


    function popBox(id , type){
        $.util.form('取货人详情', $(".import_taker_panel").html(),function () {
            var editBody = this.$body;
            editBody.find(".taker-add-field").hide();
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
            $.util.json(base_url+'/backstage/moutai/editTaker/'+id, null, function(data){
                if(data.success){
                    editBody.find("input[name=id]").val(data.taker.id)
                    editBody.find("select[name=conveyId] option[value='"+data.taker.conveyId +"']").attr("selected",true);
                    editBody.find("input[name=takeName]").val(data.taker.takeName)
                    editBody.find("input[name=takePhone]").val(data.taker.takePhone)
                    editBody.find("input[name=takeIdcare]").val(data.taker.takeIdcare)
                    editBody.find("select[name=fettle] option[value='"+data.taker.fettle +"']").attr("selected",true);
                }else{
                    $.util.error(data.message);
                }
            });
        },function () {
            if(type == 2){
                var editBody = this.$body, form = new FormData();
                var takePhone = editBody.find("#form-takePhone").val();
                if(!takePhone){
                    $.util.error("请输入联系人电话!");
                    return false;
                }
                if(!(/^1[\d]{10}$/.test(takePhone))){
                    $.util.error("联系人电话格式有误!");
                    return false;
                }
                var takeIdcare = editBody.find("#form-takeIdcare").val();
                if(takeIdcare.length < 15 || takeIdcare.length > 18){
                    $.util.error("身份证位数不准确,请重新输入!");
                    return false;
                }
                var action_url = base_url + '/backstage/moutai/taker/update';
                form.append("id",editBody.find("#form-id").val());
                form.append("conveyId",editBody.find("#form-conveyId").val());
                form.append("takeName",editBody.find("#form-takeName").val());
                form.append("takePhone",takePhone);
                form.append("takeIdcare",takeIdcare);
                form.append("fettle",editBody.find("#form-fettle").val());
                $.ajax({
                    url: base_url + "/backstage/moutai/taker/update",
                    type: 'POST',
                    data: form,
                    processData: false, // 告诉jQuery不要去处理发送的数据
                    contentType: false,// 告诉jQuery不要去设置Content-Type请求头
                    success: function (data) {
                        if (data.code == 'SUCCESS') {
                            $.util.success("操作成功", function () {
                                $("#searchTakerForm").submit();
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
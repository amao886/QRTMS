<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>物流跟踪-登录</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${baseStatic}css/userManagement.css?times=${times}"/>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchManageForm" action="${basePath}/admin/user/userManagement" method="post">
        <input type="hidden" name="num" value="${search.num }">
        <input type="hidden" name="size" value="${search.size }">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">用户名:</label>
                    <input type="text" class="tex_t" name="username" value="${search.username}">
                </div>
                <div class="fl col-min">
                    <label class="labe_l">真实姓名:</label>
                    <input type="text" class="tex_t" name="realName" value="${search.realName}">
                </div>
                <div class="fl col-min">
                    <button type="button" class="btn btn-default content-search-btn">查询</button>
                </div>
            </div>
        </div>
    </form>
    <c:if test="${SESSION_USER_INFO.admin}">
        <div class="btnBox">
            <button type="button" class="btn btn-default addManage">添加管理员</button>
        </div>
    </c:if>
    <div class="table-style">
        <table class="dtable" id="trackTable">
            <thead>
            <tr>
                <th>用户名</th>
                <th>真实姓名</th>
                <th>角色</th>
                <th>上传登录IP</th>
                <th>上次登录时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.collection}" var="obj" varStatus="s">
                <tr>
                    <td>${obj.username}</td>
                    <td>${obj.realName}</td>
                    <td>
                        <c:if test="${obj.role.roleCategory == 1}">普通用户</c:if>
                        <c:if test="${obj.role.roleCategory == 101}">茅台用户</c:if>
                        <c:if test="${obj.role.roleCategory == 102}">九阳用户</c:if>
                        <c:if test="${obj.role.roleCategory == 9999}">超级管理员</c:if>
                    </td>
                    <td>${obj.lastLoginIp}</td>
                    <td><fmt:formatDate value="${obj.lastLoginTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>
                        <c:if test="${SESSION_USER_INFO.id - obj.userId == 0 || SESSION_USER_INFO.admin}">
                            <button type="button" data-id="${obj.id}" data-rkey="${obj.role.id}" class="btn btn-link editBtn">修改</button>
                            <button type="button" data-id="${obj.id}" class="btn btn-link resetBtn">重置</button>
                        </c:if>
                        <c:if test="${SESSION_USER_INFO.id - obj.userId != 0 && SESSION_USER_INFO.admin}">
                            <button type="button" data-id="${obj.id}" class="btn btn-link delBtn">删除</button>
                        </c:if>
                    </td>
                </tr>

            </c:forEach>

            </tbody>
        </table>
        <c:if test="${page.collection != null && fn:length(page.collection) > 0}">
            <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}" pages="${page.pages}" total="${page.total}"/>
        </c:if>
    </div>
</div>
<!-- 弹框——新增 -->
<div class="add_manage_panel" style="display: none">
    <div class="model-base-box add_manage" style="width: 470px;">
        <div class="model-form-field">
            <label for="">用户名 :</label>
            <input class="userName" name="userName" type="text" placeholder='4-15位字母、数字或符号"_"组成'>
        </div>
        <div class="model-form-field">
            <label for="">密码 :</label>
            <input class="password" name="password" type="password" placeholder="6-15位字母、数字或符号组成">
        </div>
        <div class="model-form-field">
            <label for="">重复密码 :</label>
            <input class="repeatPassword" name="repeatPassword" type="password" placeholder="请重复输入管理员登录后台的密码">
        </div>
        <div class="model-form-field">
            <label for="">真实姓名:</label>
            <input class="trulyName" name="trulyName" type="text" placeholder="请输入真实姓名">
        </div>
        <div class="model-form-field">
            <label for="">角色:</label>
            <select  class="tex_t selec_t" name="userType">
                <c:forEach items="${roles}" var="r">
                    <option value="${r.id}">${r.roleName}</option>
                </c:forEach>
            </select>
        </div>
    </div>
</div>

<!-- 弹框——编辑 -->
<div class="edit_manage_panel" style="display: none">
    <div class="model-base-box edit_manage" style="width: 470px;">
        <div class="model-form-field">
            <label for="">用户名 :</label>
            <input class="userName" name="userName" type="text" disabled="disabled">
        </div>
        <div class="model-form-field">
            <label for="">真实姓名 :</label>
            <input class="trulyName" name="trulyName" type="text">
        </div>
        <div class="model-form-field">
            <label for="">角色:</label>
            <select  class="tex_t selec_t" name="userType">
                <c:forEach items="${roles}" var="r">
                    <option value="${r.id}">${r.roleName}</option>
                </c:forEach>
            </select>
        </div>
    </div>
</div>

<!-- 弹框——重置密码 -->
<div class="reset_manage_panel" style="display: none">
    <div class="model-base-box reset_manage" style="width: 470px;">
        <div class="model-form-field">
            <label for="">新密码 :</label>
            <input class="userName" name="password" type="password">
        </div>
        <div class="model-form-field">
            <label for="">确认密码 :</label>
            <input class="trulyName" name="confirmPassword" type="password">
        </div>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script>
    $(document).ready(function () {
        //翻页事件
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                $("form input[name='num']").val($(this).attr("num"));
                $("#searchManageForm").submit();
            });
        });
    })
    $(".content-search-btn").on('click', function () {
    	$("input[name='num']").val(1);
        $("#searchManageForm").submit();
    })

    $(".addManage").on('click', function () {
        $.util.form('添加账户', $(".add_manage_panel").html(), function () {
            var editBody = this.$body;
            editBody.find("select[name='userType']").val(0);
        }, function () {
            var parmas = {}, editBody = this.$body;
            parmas.userType = editBody.find("select[name='userType']").val();
            parmas.username = editBody.find("input[name='userName']").val();
            parmas.password = editBody.find("input[name='password']").val();
            parmas.repeatPassword = editBody.find("input[name='repeatPassword']").val();
            parmas.realName = editBody.find("input[name='trulyName']").val()
            if (parmas.username == "" || parmas.username == null) {
                $.util.error("用户名不能为空");
                return false;
            } else if (!(/^(?!_)(?!.*?_$)[a-zA-Z0-9_]{4,15}$/.test(parmas.username))) {
                $.util.error("用户名格式错误");
                return false;
            }
            if (parmas.password == "" || parmas.password == null) {
                $.util.error("密码不能为空");
                return false;
            } else if (!(/^[a-zA-Z0-9_-]{6,15}$/.test(parmas.password))) {
                $.util.error("密码格式错误");
                return false;
            }
            if (parmas.repeatPassword == "" || parmas.repeatPassword == null) {
                $.util.error("确认密码不能为空");
                return false;
            } else if (parmas.password !== parmas.repeatPassword) {
                $.util.error("确认密码需与密码保持一致");
                return false;
            }
            if (parmas.realName == "" || parmas.realName == null) {
                $.util.error("真实姓名不能为空");
                return false;
            }
            $.util.json(base_url + '/admin/user/saveManagingUsers', parmas, function (data) {
                if (data.success) {
                    $.util.alert('操作提示', data.message, function () {
                        $("#searchManageForm").submit();
                    });
                } else {
                    $.util.error(data.message);
                }
            });
        });
    });

    $(".editBtn").on('click', function () {
        var $this = $(this),
            _user = $this.parents("tr").find("td:eq(0)").text(),
            _id = $this.attr("data-id"),
            _roleKey = $this.attr("data-rkey"),
            _name = $this.parents("tr").find("td:eq(1)").text();
        $.util.form('修改账户', $(".edit_manage_panel").html(), function () {
            var editBody = this.$body;
            editBody.find("input[name='userName']").val(_user);
            editBody.find("input[name='trulyName']").val(_name);
            editBody.find("select[name='userType']").val(_roleKey);
        }, function () {
            var parmas = {}, editBody = this.$body;
            parmas.realName = editBody.find("input[name='trulyName']").val();
            if (parmas.realName == "" || parmas.realName == null) {
                $.util.error("姓名不能为空");
                return false;
            }
            parmas.userType = editBody.find("select[name='userType']").val();
            parmas.id = _id;
            $.util.json(base_url + "/admin/user/updateUserInfo", parmas, function (data) {
                if (data.success) {
                    $.util.alert('操作提示', data.message, function () {
                        $("#searchManageForm").submit();
                    });
                } else {
                    $.util.error(data.message);
                }
            });
        });
    });

    $(".delBtn").on("click", function () {
        var $this = $(this),
            _id = $this.attr("data-id");
        $.util.confirm("删除提示", "是否确认删除？", function () {
            $.util.json(base_url + "/admin/user/delManagingUsers/" + _id, null, function (data) {
                if (data.success) {
                    $.util.alert('操作提示', data.message, function () {
                        $("#searchManageForm").submit();
                    });
                } else {
                    $.util.error(data.message);
                }
            });
        });
    });

    $(".resetBtn").on("click", function () {
        var $this = $(this),
            _id = $this.attr("data-id");
        $.util.form('修改密码', $(".reset_manage_panel").html(), function () {
            var editBody = this.$body;
        }, function () {
            var parmas = {}, editBody = this.$body;
            console.log(parmas);
            parmas.password = editBody.find("input[name='password']").val();
            parmas.confirmPassword = editBody.find("input[name='confirmPassword']").val();
            if (parmas.password == "" || parmas.password == null) {
                $.util.error("密码不能为空");
                return false;
            } else if (!(/^[a-zA-Z0-9_-]{6,15}$/.test(parmas.password))) {
                $.util.error("密码格式错误");
                return false;
            }

            if (parmas.confirmPassword == "" || parmas.confirmPassword == null) {
                $.util.error("确认密码不能为空");
                return false;
            } else if (parmas.password !== parmas.confirmPassword) {
                $.util.error("确认密码需与密码保持一致");
                return false;
            }
            parmas.id = _id;
            $.util.json(base_url + "/admin/user/updateUserInfo", parmas, function (data) {
                if (data.success) {
                    $.util.alert('操作提示', data.message, function () {
                        $("#searchManageForm").submit();
                    });
                } else {
                    $.util.error(data.message);
                }
            });
        });
    });

</script>
</html>
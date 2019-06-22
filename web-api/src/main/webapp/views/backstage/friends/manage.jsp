<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-我的好友</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/page.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}css/track.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}css/friends.css?times=${times}"/>
</head>
<body>
<div class="track-content clearfix">
    <form id="serchFriendsForm" action="${basePath}/backstage/friend/search" method="post">
        <input type="hidden" name="num" value="${search.num }">
        <input type="hidden" name="size" value="${search.size }">
        <div class="content-search">
            <div class="clearfix">
                <div class="search-con">
                    <input type="text" class="search-input" name="likeString" value="${search.likeString }"
                           maxlength="30" placeholder="姓名/手机号"/>
                    <input type="hidden"/>
                    <button class="search-btn">查询</button>
                </div>
                <div class="btnBox" style="float: right;margin-right: 72px;">
                    <button type="button" class="btn btn-default add_something">添加好友</button>
                    <button type="button" class="btn btn-default delete_something">批量删除</button>
                </div>
            </div>
        </div>
    </form>
    <div class="table-style">
        <table class="dtable" id="trackTable">
            <thead>
            <tr>
                <th width="8%"><label><input type="checkbox" style="margin:0 10px 0 0;vertical-align:middle;"
                                             id="checkAll"/>全选</label>
                </th>
                <th>姓名</th>
                <th>手机号</th>
                <th>是否注册</th>
                <th>所属公司</th>
                <th>创建日期</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.collection}" var="friends">
                <tr>
                    <td><input type="checkbox" style="margin:0 20px;vertical-align:middle;" name="checksingle"
                               value="${friends.id}"/></td>
                    <td>
                        <c:choose>
	                        <c:when test="${friends.friendsType == 1}">
	                            <span class="role-classify owner">货</span>
	                        </c:when>
	                        <c:when test="${friends.friendsType == 2}">
	                            <span class="role-classify carrier">承</span>
	                        </c:when>
	                        
	                        <c:when test="${friends.friendsType == 3}">
	                            <span class="role-classify driver">司</span>
	                        </c:when>
	                        <c:otherwise >
	                        	<span class="role-classify"></span>
	                    	</c:otherwise>
                    	</c:choose>
                    	<span>${friends.fullName}</span>
                    </td>
                    <td>${friends.mobilePhone }</td>
                    <c:choose>
                        <c:when test="${friends.pid > 0}">
                            <td>已注册</td>
                        </c:when>
                        <c:otherwise>
                            <td>未注册</td>
                        </c:otherwise>
                    </c:choose>
                    <td>${friends.company }</td>
                    <td><fmt:formatDate value="${friends.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>
                        <input type="hidden" value="${friends.id}" name="friendsId">
                        <input type="hidden" value="${friends.userid}" name="userId">
                        <a href="javascript:;" class="aBtn btn_send_msg edit_something">编辑</a>
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

<div class="editfriends" style="display: none">
    <div class="model-base-box edit_customer" style="width: 470px;">
        <input name="id" type="hidden">
        <div class="model-form-field">
            <label for="">姓名 :</label>
            <input name="fullName" type="text" min="0" max="50">
        </div>
        <div class="model-form-field">
            <label for="">手机号 :</label>
            <input name="mobilePhone" type="text" min="0" max="11" id="mobilePhone">
        </div>
        <div class="model-form-field">
            <label for="">所属公司 :</label>
            <input name="company" type="text" min="0" max="100">
        </div>
        <div class="model-form-field">
            <label for="">好友类型 :</label>
            <div class="inline_block">
                <label class="label-style"><input type="radio" name="friendsType" value="1">货主</label>
                <label class="label-style"><input type="radio" name="friendsType" value="2">承运商</label>
                <label class="label-style"><input type="radio" name="friendsType" value="3">司机</label>
            </div>
        </div>
    </div>
</div>

</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/fileDownload.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker.js"></script>
<script>
    $(document).ready(function () {
        //提交查询事件
        $(".content-search-btn").on("click", function () {
            $("#serchFriendsForm").submit();
        });
        $('#checkAll').on("click", function () {
            $("input[name='checksingle']").prop('checked', this.checked);
        });
        //翻页事件
        $(".pagination a").each(function (i) {
            $(this).on('click', function () {
                $("form input[name='num']").val($(this).attr("num"));
                $("#serchFriendsForm").submit();
            });
        });
        $(".edit_something").on('click', function () {
            var friendsId = $(this).parent().find(":hidden[name='friendsId']").val();
            $.post(base_url + "/backstage/friend/getByFriendsId/" + friendsId, {}, function (data) {
                $.util.form('编辑好友', $(".editfriends").html(), function () {
                    var editBody = this.$body;
                    if (data.friends.pid > 0) {
                        editBody.find("input[name='mobilePhone']").attr("disabled", true);
                        ;
                    }
                    editBody.find("input[name='fullName']").val(data.friends.fullName);
                    editBody.find("input[name='mobilePhone']").val(data.friends.mobilePhone);
                    editBody.find("input[name='company']").val(data.friends.company);
                    editBody.find(":radio[name='friendsType'][value='" + data.friends.friendsType + "']").prop("checked", "checked").parent().addClass('active');
                    //给label添加点击事件
                    editBody.find($('.label-style')).on('click', function () {
                        $(this).addClass('active').siblings().removeClass('active');
                    })
                }, function () {
                    var parmas = {}, editBody = this.$body;
                    console.log(parmas);
                    parmas.id = friendsId;
                    parmas.fullName = editBody.find("input[name='fullName']").val();
                    parmas.mobilePhone = editBody.find("input[name='mobilePhone']").val();
                    parmas.company = editBody.find("input[name='company']").val();
                    parmas.friendsType = editBody.find("input[name='friendsType']:checked").val();
                    parmas.userid = editBody.find("input[name='userId']").val();
                    if (parmas.fullName == "" || parmas.fullName == null) {
                        $.util.error("好友姓名不能为空");
                        return false;
                    }

                    if (parmas.mobilePhone == "" || parmas.mobilePhone == null) {
                        $.util.error("好友手机号码不能为空");
                        return false;
                    } else if (!(/^1[\d]{10}$/.test(parmas.mobilePhone))) {
                        $.util.error("好友手机号码格式错误");
                        return false;
                    }


                    $.util.json(base_url + '/backstage/friend/updateFriends/', parmas, function (data) {
                        if (data.success) {
                            $.util.alert('操作提示', data.message, function () {
                                $("#serchFriendsForm").submit();
                            });
                        } else {
                            $.util.error(data.message);
                        }
                    });
                    return false;
                });
            })
        });
        $(".add_something").on('click', function () {
            $.util.form('添加好友', $(".editfriends").html(), function () {
                var editBody = this.$body;
                //给label添加点击事件
                editBody.find($('.label-style')).on('click', function () {
                    $(this).addClass('active').siblings().removeClass('active');
                })

            }, function () {
                var parmas = {}, editBody = this.$body;
                console.log(parmas);
                parmas.fullName = editBody.find("input[name='fullName']").val();
                parmas.mobilePhone = editBody.find("input[name='mobilePhone']").val();
                parmas.company = editBody.find("input[name='company']").val();
                parmas.friendsType = editBody.find("input[name='friendsType']:checked").val()
                if (parmas.fullName == "" || parmas.fullName == null) {
                    $.util.error("好友姓名不能为空");
                    return false;
                }
                if (parmas.mobilePhone == "" || parmas.mobilePhone == null) {
                    $.util.error("好友手机号码不能为空");
                    return false;
                } else if (!(/^1[\d]{10}$/.test(parmas.mobilePhone))) {
                    $.util.error("好友手机号码格式错误");
                    return false;
                }
                $.util.json(base_url + '/backstage/friend/addFriends/', parmas, function (data) {
                    if (data.success) {
                        $.util.alert('操作提示', data.message, function () {
                            $("#serchFriendsForm").submit();
                        });
                    } else {
                        $.util.error(data.message);
                    }
                });
            });
        });
        $(".delete_something").on('click', function () {

            var chk_value = [], parmas = {};
            $('input[name="checksingle"]:checked').each(function () {
                chk_value.push($(this).val());
            });

            if (chk_value == null || chk_value == "") {
                $.util.error("请至少选择一条数据");
                return false;
            }

            parmas.deleIds = JSON.stringify(chk_value);
            $.util.json(base_url + '/backstage/friend/deleFriends/', parmas, function (data) {
                if (data.success) {
                    $.util.alert('操作提示', data.message, function () {
                        $("#serchFriendsForm").submit();
                    });
                } else {
                    $.util.error(data.message);
                }
            });

        });

        $(".show_detail").on('click', function () {
            $(this).parent().parent().next('tr').toggle();
        });
    });
</script>
</html>
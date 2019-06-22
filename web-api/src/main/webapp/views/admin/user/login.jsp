<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>物流跟踪-登录</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${baseStatic}manage/css/login.css?times=${times}"/>
</head>
<body>
<div class="bg">
    <div class="logo">
        <img src="${baseStatic}images/login_logo.png">
        <h2>合同物流管理平台后台管理系统</h2>
    </div>
    <input type="hidden" name="debug" value="${debug}">
    <div class="loginBox">
        <ul class="loginList loginCon">
            <li>
                <label>用户名:</label>
                <input type="text" name="username" id="username">
            </li>
            <li>
                <label>密码:</label>
                <input type="text" name="password" id="password">
            </li>
            <li>
                <label>验证码:</label>
                <div>
                    <input type="text" class="w130" name="captcha" id="captcha">
                    <img src="${basePath}/backstage/admin/user/captcha" id="codeImg"/>
                </div>
            </li>
            <li>
                <button type="button" class="loginBtn">登录</button>
            </li>
        </ul>

        <div class="loginCon hide" id="login_user">
            <input type="hidden" name="redirect_uri" value="${redirect_uri }">
            <input type="hidden" name="appId" value="${appId }">
            <input type="hidden" name="scope" value="${scope }">
            <input type="hidden" name="state" value="${state }">
            <img src="${baseStatic}manage/images/qrCode.png"/>
        </div>
        <div class="changeBox"></div>
    </div>
</div>

<!-- 弹出层 -->
<div class="qrCode-pannel" style="display:none;">
    <div class="pannelBox" style="width:250px; margin:0 auto">
        <img src="${baseStatic}manage/images/qrCode.png"/>
    </div>
</div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}js/weixin_login.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $(".changeBox").on("click", function () {
            if ($(this).hasClass("first")) {
                $(".loginCon").eq(0).removeClass("hide").next(".loginCon").addClass("hide");
                $(this).removeClass("first");
            } else {
                $(".loginCon").eq(0).addClass("hide").next(".loginCon").removeClass("hide");
                $(this).addClass("first");
            }

        });

        $('#codeImg').click(function () {
            $(this).attr('src', '${basePath}/backstage/admin/user/captcha?' + Math.floor(Math.random() * 100)).fadeIn();
        })

        $(".loginBtn").on('click', function () {
            var parmas = {};
            parmas.username = $("#username").val();
            parmas.password = $("#password").val();
            parmas.verificationCode = $("#captcha").val();
            $.util.json(base_url + "/backstage/admin/user/userLogin/", parmas, function (data) {
                console.log(binUser(data))
                if (data.status == 'false') {
                    $.util.pictures($(".qrCode-pannel").html());
                } else if (data.success) {
                    alert('登陆成功');
                } else {
                    $.util.error(data.message);
                }
            });
        })

        if (window != window.parent) {
            window.parent.location.reload(true);
        }

        /*   new WxLogin({
               id: "login_user",
               appid: data.appId,
               scope: data.scope,
               redirect_uri: data.redirect_uri,
               state: data.state,
               style: "white",
               href: ""
           });
   */

        function binUser(data) {
            new WxLogin({
                id: "login_user",
                appid: data.appId,
                scope: data.scope,
                redirect_uri: data.redirect_uri,
                state: data.state,
                style: "white",
                href: ""
            });
        }

    });
</script>
</html>
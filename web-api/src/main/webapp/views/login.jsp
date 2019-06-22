<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<title>合同物流管理平台-登录</title>
	<%@ include file="/views/include/head.jsp" %>
	<link rel="stylesheet" type="text/css" href="${baseStatic}manage/css/login.css?times=${times}"/>
	<style type="text/css">
		#codeImg{
			vertical-align: top !important;
			border: 1px solid #000000;
		}
	</style>
</head>
<body>
<div class="bg">
	<div class="logo">
		<img src="${baseStatic}images/login_logo.png">
		<h2>合同物流管理平台</h2>
	</div>
	<input type="hidden" name="debug" value="${debug}">
	<div class="loginBox">
		<div style="padding: 20px;" class="loginCon hide">
			<ul class="loginList">
				<li>
					<label>用户名:</label>
					<input type="text" name="username" id="username">
				</li>
				<li>
					<label>密码:</label>
					<input type="password" name="password" id="password">
				</li>
				<li>
					<label>验证码:</label>
					<div>
						<input type="text" class="w130" name="captcha" id="captcha">
						<img src="${basePath}/special/image/code?w=68&h=25" id="codeImg"/>
					</div>
				</li>
				<li>
					<button type="button" class="loginBtn">登录</button>
				</li>
			</ul>
		</div>
		<c:if test="${debug != null && debug == true }">
			<div class="loginCon" id="login_container">
				<div style="width: 400px; margin-bottom: 20px;">
					<form action="${redirect_uri}" method="post" id="debug_input_login">
						<div style="width: 400px;">
							<input name="openid" id="debug_input_openid" style="width:360px;border: #ddd solid 1px;">
							<button type="button" id="debug_input_submit">登陆</button>
						</div>
					</form>
				</div>
				<div>
					<form action="${redirect_uri}" method="post" id="debug_select_login">
						<select name="openid" style="width: 400px;">
							<option value="">请选择用户登陆</option>
							<c:forEach items="${users}" var="u">
								<option value="${u.openid }">${u.unamezn }(${u.openid })</option>
							</c:forEach>
						</select>
					</form>
				</div>
			</div>
		</c:if>
		<c:if test="${debug == null || debug == false }">
			<div class="loginCon" id="login_container">
				<div style="display: none;" id="wx_login">
					<input type="hidden" name="redirect_uri" value="${redirect_uri}">
					<input type="hidden" name="appId" value="${appId }">
					<input type="hidden" name="scope" value="${scope }">
					<input type="hidden" name="state" value="${state }">
				</div>
			</div>
		</c:if>
		<div class="changeBox first"></div>
	</div>
</div>

<!-- 弹出层 -->
<div class="qrCode-pannel" style="display:none;background-color: #328ac1;" id="tc_qcode">
	<div class="pannelBox" style="width:250px; margin:0 auto">
	</div>
</div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}js/weixin_login.js"></script>
<script type="text/javascript">
    if (window != window.parent) {
        window.parent.location.reload(true);
    }
    $(document).ready(function () {
        $(".changeBox").on("click", function () {
            if ($(this).hasClass("first")) {
                $(".loginCon").eq(0).removeClass("hide").siblings(".loginCon").addClass("hide");
                $(this).removeClass("first");
            } else {
                $(".loginCon").eq(0).addClass("hide").siblings(".loginCon").removeClass("hide");
                $(this).addClass("first");
            }
        });

        $('#codeImg').click(function () {
            $(this).attr('src', base_url + '/special/image/code?w=68&h=25&t=' + new Date().getTime()).fadeIn();
        })

        $(".loginBtn").on('click', function () {
            var parmas = {};
            parmas.username = $("#username").val();
            parmas.password = $("#password").val();
            parmas.verificationCode = $("#captcha").val();
            if (parmas.username == null || parmas.username == "") {
                $.util.error("请输入用户名");
                return false;
            }
            if (parmas.password == null || parmas.password == "") {
                $.util.error("请输入密码");
                return false;
            }
            if (parmas.verificationCode == null || parmas.verificationCode == "") {
                $.util.error("请输入验证码");
                return false;
            }
            parmas.callBackUrl = window.location.href;
            $.util.json(base_url + "/special/wechat/login/admin", parmas, function (data) {
                if(data.success){
                    if(!data.status){
                        tcSetVal(data);
                        $.util.pictures("微信扫码绑定账号", $(".qrCode-pannel").html());
					}else{
                        window.location.href = base_url;
                    }
				}else {
                    $.util.error(data.message);
				}
            });
        })

        function tcSetVal(data) {
            new WxLogin({
                id: "tc_qcode",
                appid: data.appId,
                scope: data.scope,
                redirect_uri: data.redirect_uri,
                state: data.state,
                href: ""
            });
        }

        var debug = $(":hidden[name='debug']").val();
        if (debug && (debug == true || debug == 'true')) {
            $("#debug_input_submit").on('click', function () {
                if ($("#debug_input_openid").val()) {
                    $("#debug_input_login").submit();
                }
            });
            $("select[name='openid']").on("change", function () {
                if ($(this).val()) {
                    $("#debug_select_login").submit();
                }
            });
        } else {
            new WxLogin({
                id: "login_container",
                appid: $(":hidden[name='appId']").val(),
                scope: $(":hidden[name='scope']").val(),
                redirect_uri: $(":hidden[name='redirect_uri']").val(),
                state: $(":hidden[name='state']").val(),
                href: ""
            });
        }
    });
</script>
</html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>物流跟踪-绑定手机号</title>
<%@ include file="/views/include/head.jsp"%>
	<link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
</head>
<body class="layui-layout-body">
<el-container>
	<el-header class="layui-layout layui-layout-admin">
		<div class="layui-header">
			<div class="layui-logo">
				<h1>合同物流管理平台</h1>
			</div>
			<!-- 头部区域（可配合layui已有的水平导航） -->
			<ul class="layui-nav layui-layout-right layout_right">
				<li class="layui-nav-item admin">
					<span id="user_id" uid="${SESSION_USER_INFO.id}"></span>
					<div class="welcome">
						<c:if test="${SESSION_USER_INFO.headImg != null}">
							<img src="${SESSION_USER_INFO.headImg}" width="28px" height="28px">
						</c:if>
						<c:if test="${SESSION_USER_INFO.headImg == null}">
							<i>欢迎您,</i>
						</c:if>
					</div>
				</li>
				<li class="layui-nav-item admin">
					<div style="margin-left: 10px;">${SESSION_USER_INFO.unamezn }</div>
				</li>
				<li class="layui-nav-item"><a href="javascript:void(0);" style="color:white;" id="logout">退出登录</a></li>
			</ul>
		</div>
	</el-header>
	<el-main>
		<div class="context" id="validate-sms-box" style="margin-top: 40px;">
			<el-row :gutter="20">
				<el-col :span="10" :offset="6">
					<el-form ref="ruleForm" :model="ruleForm" label-width="100px" :rules="rules">
						<el-form-item label="手机号" prop="phone">
							<el-input v-model="ruleForm.phone"></el-input>
						</el-form-item>
						<el-form-item label="真实姓名" prop="name">
							<el-input v-model="ruleForm.name"></el-input>
						</el-form-item>
						<el-form-item label="短息随机码" prop="code">
							<el-input v-model="ruleForm.code">
								<el-button slot="append" style="width: 185px;" :disabled="time > 0" @click="sendsmscode">{{smscodetext}}</el-button>
							</el-input>
						</el-form-item>
						<el-form-item>
							<el-button type="primary" @click="submitForm">立即绑定</el-button>
						</el-form-item>
					</el-form>
				</el-col>
			</el-row>

		</div>
	</el-main>
</el-container>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	if(window != window.parent){  
	    window.parent.location.reload(true);  
	}

    $("#logout").on('click', function(){
        $.util.confirm('安全退出', '确定要退出登陆么?', function () {
            $.util.json(base_url + "/special/logout", null, function (data) {
                if (data.success) {//处理返回结果
                    window.location.reload();
                } else {
                    $.util.error(data.message);
                }
            });
        });
    });
	var vm = new Vue({
		el:'#validate-sms-box',
		data:{
		    time:0,
            smscodetext:'发送短信随机码',
		    imageCdoeUrl:'',
            ruleForm:{ phone: null, name: null, code: null},
            rules: {
                phone: [
                    { required: true, message: '请输入要绑定的手机号', trigger: 'blur' }
                ],
                name: [
                    { required: true, message: '请输入您的真实姓名', trigger: 'blur' }
                ],
                smscode: [
                    { required: true, message: '请输入短息随机码', trigger: 'blur' }
                ]
            }
		},
		methods: {
            submitForm:function() {
                var _this = this;
                this.$refs['ruleForm'].validate(function(valid){
                    if (valid) {
                        var parmas = _this.ruleForm;
                        if(!parmas.phone || !/1\d{10}$/.test(parmas.phone)){
                            $.util.error("请输入正确的手机号");
                            return false;
                        }
                        if(!parmas.name){
                            $.util.error("请输入真实姓名");
                            return false;
                        }
                        if(!parmas.code){
                            $.util.error("请输入验证码");
                            return false;
                        }
                        $.util.json(base_url + '/bind/phone', parmas, function(res){
                            if(res.success){
                                $.util.success("绑定成功", function(){
                                    window.location.href = base_url;
                                }, 6000);
                            }else{
                                $.util.error(res.message);
                            }
                        });
                    } else {
                		return false;
            		}
            	});
            },
			sendsmscode:function(){
                var _this = this;
                if(!_this.ruleForm.phone || !/1\d{10}$/.test(_this.ruleForm.phone)){
                    $.util.error("请输入正确的手机号");
                    return false;
                }
                $.util.json(base_url + "/special/smsCode/"+ _this.ruleForm.phone, null, function(res){
                    if(res.success){
                        var time = 60;
                        var interval = window.setInterval(function(){
                            time = time - 1;
                            if(time <= 0){
                                window.clearInterval(interval);
                                _this.smscodetext = '点击发送短息随机码';
                            }else{
                                _this.smscodetext = time+'秒后重新发送';
                            }
                        }, 1000);
                    }else{
                        $.util.error(res.message);
                    }
                });
			}
		}
	});
});
</script>
</html>



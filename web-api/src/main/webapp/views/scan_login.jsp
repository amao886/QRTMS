<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<title>合同物流管理平台-登录</title>
	<link rel="shortcut icon" href="/favicon.ico">
	<%@ include file="/views/include/head.jsp" %>
	<link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
	<link rel="stylesheet" type="text/css" href="${baseStatic}manage/css/login.css?times=${times}"/>
	<style type="text/css">
		#codeImg{
			vertical-align: top !important;
			border: 1px solid #000000;
		}
		.bbbb{
			display: inline-block;
			padding: 2px;
			color: #fff;
			background: #31acfa;
			border: 1px solid #000000;
		}
	</style>
</head>
<body data-debug="${debug}" data-redirect="${redirect}" data-scanurl="${scanurl}" >
<div class="bg">
	<div class="logo">
		<img style="width: 25%;" src="/static/images/login_logo.png">
		<h2>合同物流管理平台</h2>
	</div>
	<div class="loginBox" id="login-content">
		<template>
		<div style="padding: 20px; width: 350px;" class="loginCon"  v-show="!scancode" >
			<el-form :model="form" label-width="80px">
				<el-form-item label="用户名">
					<el-input v-model="form.username"></el-input>
				</el-form-item>
				<el-form-item label="密码">
					<el-input v-model="form.password" type="password"></el-input>
				</el-form-item>
				<el-form-item label="验证码">
					<el-input placeholder="请输入内容" v-model="form.captcha">
						<template slot="append">
							<img :src="captchaUrl" @click="reloadCaptcha" width="68px" height="25px"/>
						</template>
					</el-input>
				</el-form-item>
				<el-button type="primary" @click="login(3)">登陆</el-button>
			</el-form>
		</div>
		<div class="loginCon" v-show="scancode" style="width: 250px;">
			<div id="login_container">
				<span>{{tips}}</span>
			</div>
			<div style="margin-top: 20px;" v-if="debug">
				<div style="margin-bottom: 20px;">
					<div style="width: 100%;">
						<el-input placeholder="请输入OPENID登陆" v-model="form.enterKey">
							<el-button slot="append" @click="login(1)">登陆</el-button>
						</el-input>
					</div>
				</div>
				<div style="margin-bottom: 20px; width: 100%;">
					<el-select placeholder="请选择用户登陆" v-model="form.selectKey" style=" width: 100%;" @change="login(2)" filterable>
						<el-option v-for="item in users" :key="item.id" :label="item.unamezn" :value="item.openid">
							<span style="float: left">{{ item.unamezn }}</span>
							<span style="float: right; color: #8492a6; font-size: 13px">{{ item.openid }}</span>
						</el-option>
					</el-select>
				</div>
			</div>
		</div>
		<div class="changeBox" :class="{first : scancode}" @click="scancode = !scancode"></div>
		</template>
	</div>
</div>

<!-- 弹出层 -->
<div class="qrCode-pannel" style="display:none;background-color: #328ac1;">
	<div class="pannelBox" style="width:250px; margin:0 auto" id="tc_qcode"></div>
</div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/socket/swfobject.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/socket/web_socket.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/socket/sockjs.min.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/socket/websocket.js?times=${times}"></script>
<script type="text/javascript" src="${baseStatic}plugin/js/qrcode.min.js"></script>
<script type="text/javascript">
    if (window !== window.parent) {
        window.parent.location.reload(true);
    }
    $(document).ready(function () {
        new Vue({
            el: '#login-content',
            data: {
                openid:null,
                scancode: true,
                tips:'网络环境安全监测中...',
                debug : $('body').data('debug'),
				redirect: $('body').data('redirect'),
				scanurl: $('body').data('scanurl'),
                users: [],
                socket:null,
                form:{
                    username:'',
                    password:'',
                    captcha:'',
                    selectKey:null,
					enterKey:null
				},
                captchaUrl: '/special/image/code?w=68&h=25'
            },
			mounted:function(){
				var _this= this ;
				_this.contactSocket();
				if(_this.debug){
				    _this.loadUsers();
				}
			},
            methods: {
                contactSocket:function(){
                    var _this = this;
                    _this.socket = new CustomSocket(base_host + "/special", "websocket/login_pc", "sockjs/websocket/login_pc", base_static);
                    _this.socket.on('open', function () {
                        new QRCode("login_container", {
                            text: _this.scanurl,
                            width: 250,
                            height: 250,
                            colorDark: "#000000",
                            colorLight: "#ffffff",
                            correctLevel: QRCode.CorrectLevel.M
                        });
                        _this.tips = '';
                    });
                    _this.socket.on('scan_login', function (context) {
                        window.location.href = context + "/group";
                    });
                    _this.socket.contact();
                },
                reloadCaptcha:function(){
                    this.captchaUrl = '/special/image/code?w=68&h=25&t=' + new Date().getTime();
				},
				login:function(type){
                    var _this = this;
                    if(type === 1){
                        if(!_this.form.enterKey){
                            _this.$message({ message: '请输入微信OPENID', type: 'warning' });
                            return false;
						}
                        window.location.href = '/special/wechat/auth?openid=' + _this.form.enterKey;
                    }else if(type === 2){
                        if(!_this.form.selectKey){
                            _this.$message({ message: '请选择一个用户登陆', type: 'warning' });
                            return false;
                        }
                        window.location.href = '/special/wechat/auth?openid=' + _this.form.selectKey;
					}else if(type === 3){
                        var parmas = {
                            username: _this.form.username,
                            password: _this.form.password,
                            verificationCode: _this.form.captcha,
						};
                        if (!parmas.username) {
                            _this.$message({ message: '请输入用户名', type: 'warning' });
                            return false;
                        }
                        if (!parmas.password) {
                            _this.$message({ message: '请输入密码', type: 'warning' });
                            return false;
                        }
                        if (!parmas.verificationCode) {
                            _this.$message({ message: '请输入验证码', type: 'warning' });
                            return false;
                        }
                        parmas.callBackUrl = window.location.href;
                        $.util.json(base_url + "/special/wechat/scan/admin", parmas, function (data) {
                            if(data.success){
                                if(!data.status){
                                    new QRCode("tc_qcode", {
                                        text: data.results,
                                        width: 260,
                                        height: 260,
                                        colorDark: "#000000",
                                        colorLight: "#ffffff",
                                        correctLevel: QRCode.CorrectLevel.L
                                    });
                                    setTimeout(function () {
                                        $.util.pictures("微信扫码绑定账号", $(".qrCode-pannel").html());
                                    }, 500)
                                }else{
                                    window.location.href = base_url + "/group";
                                }
                            }else {
                                _this.$message({ message: data.message, type: 'error' });
                            }
                        });
					}
				},
                loadUsers:function(){
                    var _this = this;
                    $.util.json('/special/wechat/users', {}, function (data) {
                        if (data.success) {
                            if(data.users && data.users.length > 0){
                                _this.users = data.users;
                            }
                        } else {
                            _this.$message({ message: data.message, type: 'error' });
                        }
                    });
                }
			}
        });
    });
</script>
</html>
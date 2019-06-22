<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

	<head>
		<title>签署管理-批量签署</title>
		<%@ include file="/views/include/head.jsp" %>
		<link rel="stylesheet" href="${baseStatic}css/receiptmanage.css?times=${times}" />
		<style type="text/css">
			.tripInfo {
				color: #636363;
				padding-top: 30px;
				padding-bottom: 10px;
			}
			
			.title {
				color: #636363;
				margin-top: 30px;
				margin-bottom: 20px;
			}
			
			.pancelCon {
				background: #fafafa;
				padding-left: 10px;
				padding-right: 10px;
			}
			
			.content {
				width: 100%;
			}
			
			.signPos {
				float: left;
				width: 440px;
				min-height: 260px;
				background: #fafafa;
				padding: 30px 30px 30px 30px;
			}
			
			.signPos .titleLine {
				padding-bottom: 30px;
			}
			
			.signPos .contentLine {
				padding-bottom: 20px;
			}
			
			.line {
				float: left;
				width: 120px;
				height: 17px;
				margin-left: 10px;
				margin-right: 15px;
				background-color: #D5D5D5;
				overflow: hidden;
			}
			
			.smsVailCode {
				padding-left: 10px;
				padding-right: 10px;
				background-color: #FAFAFA;
			}
			
			.getVailCodeText {
				color: #31ACFA;
				background: transparent;
			}
			
			.personSignatureList {
				width: 325px;
			}
			
			.personSignatureItem {
				float: left;
				margin-top: 20px;
				margin-right: 40px;
				cursor: pointer;
			}
			
			.radio-box-person {
				width: 0;
				height: 0;
				border: 10px solid transparent;
				border-left: 10px solid transparent;
				border-top: 10px solid transparent;
				float: right;
				margin-top: -20px;
				margin-left: 102px;
				position: absolute;
			}
			
			.radio-box-person input {
				opacity: 0;
				position: absolute;
			}
			
			.selectspan {
				width: 0;
				height: 0;
				border: 10px solid #31ACFA;
				border-left: 10px solid transparent;
				border-top: 10px solid transparent;
				float: right;
				margin-top: -10px;
				margin-right: -10px;
			}
			
			.radio-box-person input[type="radio"]+span {
				opacity: 0;
			}
			
			.radio-box-person input[type="radio"]:checked+span {
				opacity: 1;
			}
			
			.companySignatureList {
				width: 400px;
			}
			
			.companySignatureItem {
				float: left;
				margin-bottom: 20px;
				margin-right: 40px;
				cursor: pointer;
			}
			
			.radio-box-company {
				width: 160px;
				height: 160px;
				border: 1px solid #CCCCCC;
				text-align: center;
				padding: 5px;
			}
			
			.radio-box-company input {
				opacity: 0;
				position: absolute;
			}
			
			.radio-box-company span {
				width: 0;
				height: 0;
				border: 10px solid #31ACFA;
				border-left: 10px solid transparent;
				border-top: 10px solid transparent;
				float: right;
				margin-top: -5px;
				margin-right: -1px;
			}
			
			.companySignatureSelected {
				border: 1px solid #31ACFA;
			}
			
			.radio-box-company input[type="radio"]+span {
				opacity: 0;
			}
			
			.radio-box-company input[type="radio"]:checked+span {
				opacity: 1;
			}
			
			.companySignatureNickName {
				color: #333333;
				padding-top: 6px;
			}
			
			.submit {
				width: 300px;
				margin-top: 40px;
				margin-bottom: 40px;
			}
		</style>
	</head>

	<body keys="${orderKeys}" roletype="${rt}">
		<div class="track-content" id="vue-content">
			<!-- 表格部分 -->
			<div class="table-style" id="list-manage">
				<div class="tripInfo">批量签署的回单信息如下（选中回单中已签署的回单系统已自动跳过）：</div>
				<table>
					<thead>
						<tr>
							<th style="width: 28px;">送货单号</th>
							<th width="35">订单编号</th>
							<th width="45">发货日期</th>
							<th width="45">发货方</th>
							<th width="45">收货客户</th>
							<th width="18">收货人</th>
							<th width="27">联系方式</th>
							<th width="55">收货地址</th>
							<th width="32">
								<span v-if="roleType == 2">收货方签署状态</span>
								<span v-else-if="roleType == 3">物流商签署状态</span>
							</th>
							<th width="24">签收状态</th>
							<th width="50">到货时间</th>
						</tr>
					</thead>
					<tbody>
						<tr v-for="o in orders">
							<td>{{o.deliveryNo}}</td>
							<td>{{o.orderNo}}</td>
							<td>{{o.deliveryTime | date}}</td>
							<td><span v-if="o.shipper != null">{{o.shipper .companyName}}</span></td>
							<td><span v-if="o.receive != null">{{o.receive .companyName}}</span></td>
							<td>{{o.receiverName}}</td>
							<td>{{o.receiverContact}}</td>
							<td>{{o.receiveAddress}}</td>
							<td>
								<%--收货方签署状态--%>
								<span v-if="roleType == 2 && (o.receiptFettle == 2 || o.receiptFettle == 4)">已签</span>
								<%--物流商签署状态--%>
								<span v-else-if="roleType == 3 && o.receiptFettle >= 3">已签</span>
								<span v-else>未签</span>
							</td>
							<td>
								<span class="abnormalSts" v-if="o.signFettle == 2">异常签收</span>
								<span class="normalSts" v-else-if="o.signFettle == 1">完好签收</span>
								<span v-else>未签收</span>
							</td>
							<td>
								<span v-if="o.receiveTime != null">{{o.receiveTime | datetime}}</span>
								<span v-if="o.receiveTime == null">未收货</span>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="content clearfix">
				<div class="title">批量签署：</div>
				<div class="tableCon signPos">
					<p class="fb titleLine">第一步：请输入您收到的签收验证码</p>
					<p class="contentLine">签收人：<span>{{certified.name}}</span></p>
					<p class="contentLine">手机号码：<span>{{certified.mobilePhone}}</span></p>
					<p class="contentLine" style="float: left;">短信验证码
						<div class="line"><input type="text" name="code" v-model="code.value" style="background-color: #f9f9f9;"></div>
						<button class="getVailCodeText" :class="{ active: code.time != 0}" :disabled="code.time != 0" @click="loadCode">{{code.text}}</button>
					</p>
				</div>
				<div class="tableCon signPos" v-if="certified.personals != null && certified.personals.length > 0">
					<p class="fb titleLine" style="padding-bottom: 10px;">第二步：请选择使用的个人签章（单选）</p>
					<div class="personSignatureList clearfix">
						<ul>
							<li class="personSignatureItem" v-for="p in certified.personals" @click="params.psealKey = p.id">
								<img :src="baseurl +'/'+ p.sealImgPath" width="122" height="51" />
								<div class="radio-box-person"><span :class="{selectspan : p.id == params.psealKey}"></span></div>
							</li>
						</ul>
					</div>
				</div>
				<div class="tableCon signPos">
					<p class="fb titleLine">第三步：请选择使用的企业章（单选）</p>
					<div class="companySignatureList clearfix" v-if="certified.enterprises != null && certified.enterprises.length > 0">
						<ul>
							<li class="companySignatureItem" v-for="e in certified.enterprises" @click="params.csealKey = e.id">
								<div class="radio-box-company" :class="{companySignatureSelected : e.id == params.csealKey}">
									<img :src="baseurl +'/'+ e.sealData" width="150" height="150" />
									<div class="companySignatureNickName">{{e.copyWriting}}</div>
								</div>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div style="text-align: center;">
				<button class="layui-btn layui-btn-normal submit" @click="submit">确认签署</button>
			</div>
		</div>
	</body>
	<%@ include file="/views/include/floor.jsp" %>
	<script>
        $(document).ready(function () {
            var roleType = $("body").attr('roletype'), keys = $("body").attr('keys');
            var v = new Vue({
                el: '#vue-content',
                data: {
                    baseurl: base_static,
                    roleType: roleType,
                    orders: [],
                    certified:{},
                    params:{keys : [], psealKey: 0, csealKey:0},//要提交的数据
                    code: {text:'获取验证码', time : 0, value: ''} //验证码
                },
                created: function () {
                    loadOrders(this);
                    loadPersonal(this)
                },
                methods:{
                    loadCode:function(){
                        loadSmsCode(this);
                    },
                    submit:function(){
                        submitData(this);
                    },
                    select:function(key){
                        console.log(key);
                    }
                }
            });
            function loadOrders(vm){
                var params = {orderKeys: keys};
                console.log(params);
                $.util.json(base_url + '/enterprise/sign/receipt/selects', params, function (data) {
                    if (data.success) {//处理返回结果
                        vm.orders = data.results;
                        if(data.keys == null || data.keys.length <= 0){
							$.util.warning("没有可以签署回单");
						}else{
                            vm.params.keys = data.keys.join(',');
						}
                    } else {
                        $.util.error(data.message);
                    }
                });
            }
            function loadPersonal(vm){
                $.util.json(base_url + '/enterprise/order/userCertified/search', null, function (data) {
                    if (data.success) {//处理返回结果
                        vm.certified = data.reslut
                        if(data.pathPrefix){
                            vm.baseurl = data.pathPrefix;
                        }
                    } else {
                        $.util.error(data.message);
                    }
                });
            }
            function loadSmsCode(vm){
                if(!vm.params.keys){
                    $.util.warning("没有可以签署回单");
					return;
                }
                var parmas = {orderKey : vm.params.keys}
                $.util.json(base_url + '/enterprise/sign/send/code', parmas, function (data) {
                    if (data.success) {//处理返回结果
                        vm.code.time = 60;
                        var interval = window.setInterval(function(){
                            vm.code.time = vm.code.time - 1;
                            vm.code.text = vm.code.time + "秒";
                            if(vm.code.time <= 0){
                                vm.code.text = '获取验证码';
                                window.clearInterval(interval);
                            }
                        }, 1000);
                    } else {
                        $.util.error(data.message);
                    }
                });
            }
            function submitData(vm){
                if(!vm.params.keys){
                    $.util.warning("没有可以签署回单");
                    return;
                }
                var params = $.extend({}, vm.params, {code: vm.code.value});
                console.log(params);
                if (!params.code) {
                    $.util.error("签署短信校验码不能为空");
                }
                /*
                else if (!params.psealKey) {
                    $.util.error("请选择个人印章");
                }
                */
                else if (!params.csealKey) {
                    $.util.error("请选择企业印章");
                }else{
                    $.util.json(base_url + '/enterprise/sign/signature/multiple', params, function (data) {
                        if (data.success) {//处理返回结果
                            $.util.success(data.message, function(){
                                window.location.href = base_url + '/enterprise/order/receipt/' + (roleType == 2 ? 'conveyer' : 'receiver');
                            }, 3000);
                        } else {
                            $.util.error(data.message);
                            loadOrders(vm);
                        }
                    });
                }
            }
        });
	</script>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

	<head>
		<title>签署管理-单个签署</title>
		<%@ include file="/views/include/head.jsp" %>
		<link rel="stylesheet" href="${baseStatic}css/receiptmanage.css?times=${times}" />
		<style type="text/css">
			.pancelCon {
				background: #fafafa;
				padding-left: 10px;
				padding-right: 10px;
			}
			
			.rightContent {
				padding-top: 30px;
				padding-left: 50px;
				width: 33%;
				float: left;
				position: relative;
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
				margin-top: 20px;
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
				width: 290px;
				margin-top: 40px;
				margin-bottom: 40px;
			}
		</style>
	</head>
	<body id="${orderKey}">
		<div class="track-content" id="vue-content">
			<template>
			<div class="creat_receipt_panel" style="float: left;width:67%;">
				<div class="model-base-box creat_receipt" style="width:100%;">
					<div class="pancelCon clearfix">
						<div class="h4">产品配送单</div>
						<div class="infoNo">
							<p>订单编号：{{order.orderNo}}</p>
							<p>日期：{{order.deliveryTime | date}}</p>
						</div>
						<ul class="pancelUl clearfix">
							<li><label>配送单号：</label><span>{{order.deliveryNo}}</span></li>
							<li><label>订单客户：</label><span v-if="order.receive != null">{{order.receive.companyName}}</span></li>
							<li><label>收货客户：</label><span v-if="order.receive != null">{{order.receive.companyName}}</span></li>
							<li><label>收货人员：</label><span>{{order.receiverName}}</span></li>
							<li><label>物流商名称：</label><span v-if="order.convey != null">{{order.convey.companyName}}</span></li>
							<li><label>联系方式：</label><span>{{order.receiverContact}}</span></li>
							<li><label>收货地址：</label><span>{{order.receiveAddress}}</span></li>
							<li><label>订单备注：</label><span>{{order.remark}}</span></li>
							<li>
								<label>订单明细：</label>
								<div class="tableCon">
									<table>
										<thead>
											<tr>
												<th width="90">物料编号</th>
												<th width="190">物料描述/物料名称</th>
												<th width="80">单位</th>
												<th width="80">数量</th>
												<th width="80">箱数</th>
												<th width="80">体积</th>
												<th width="80">重量</th>
											</tr>
										</thead>
										<tbody>
											<tr v-for="c in order.commodities">
												<td>{{c.commodityNo}}</td>
												<td>{{c.commodityName}}</td>
												<td>{{c.commodityUnit}}</td>
												<td>{{c.quantity}}</td>
												<td>{{c.boxCount}}</td>
												<td>{{c.volume}}</td>
												<td>{{c.weight}}</td>
											</tr>
										</tbody>
										<tfoot>
											<tr>
												<td colspan="3">合计</td>
												<td>{{order.quantity}}</td>
												<td>{{order.boxCount}}</td>
												<td>{{order.volume | numberFixed}}</td>
												<td>{{order.weight | numberFixed}}</td>
											</tr>
										</tfoot>
									</table>
								</div>
							</li>
						</ul>
						<div class="clearfix">
							<div class="abnormalSign">
							</div>
							<div class="tableCon signPos clearfix">
								<p class="fb" style="float: left;">物流商签字：
									<img v-if="order.receive && order.convey.userSealPath" :src="baseurl +'/'+ order.convey.userSealPath" width="122" height="51" />
								</p>
								<p class="fb" style="float: left;margin-left: 40%;">物流商盖章：
									<img v-if="order.convey && order.convey.sealDate" :src="baseurl +'/'+ order.convey.sealDate" width="150" height="150"/>
								</p>
							</div>
							<div class="tableCon signPos">
								<p style="text-align:left;padding-bottom: 20px;">
									温馨提示：物流配送责任以签收单据为主要依据，故请您严格按照收货标准验收签字、盖章确认。如有异常情况，请及时致电九阳物流：</p>
								<p class="col-3">
									<span>杭州RDC：0571-12345678</span>
									<span>济南RDC：0531-12345678</span>
									<span>广州RDC：0757-12345678</span>
								</p>
								<p class="fb">实收件数(大写)：{{order.realNumber}}</p>
								<p>我已确认此配送单信息与实际接收货物数量、型号、配送标识（防窜货码）一致。</p>
							</div>
							<div class="tableCon signPos">
								<p class="fb" style="float: left;">收货单位签字：
									<img v-if="order.receive && order.receive.userSealPath" :src="baseurl +'/'+ order.receive.userSealPath" width="122" height="51" />
								</p>
								<p class="fb" style="float: left;margin-left: 39%;">收货单位业务章：
									<img v-if="order.receive && order.receive.sealDate" :src="baseurl +'/'+ order.receive.sealDate"  width="150" height="150" />
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="rightContent" v-if="certified != null && !order.sign">
				<div class="tableCon signPos">
					<p class="fb titleLine">第一步：请输入您收到的签收验证码</p>
					<p class="contentLine">签收人：<span>{{certified.name}}</span></p>
					<p class="contentLine">手机号码：<span>{{certified.mobilePhone}}</span></p>
					<p class="contentLine" style="float: left;">短信验证码
						<div class="line"><input type="text" name="code" v-model="code.value" style="background-color: #f9f9f9;" ></div>
						<button class="getVailCodeText" :class="{ active: code.time != 0}" :disabled="code.time != 0" @click="loadCode">{{code.text}}</button>
					</p>
				</div>
				<div class="tableCon signPos" style="margin-top: 65px;" v-if="certified.personals != null && certified.personals.length > 0">
					<p class="fb titleLine" style="padding-bottom: 10px;">第二步：请选择使用的个人签章（单选）</p>
					<div class="personSignatureList clearfix">
						<ul>
							<li class="personSignatureItem" v-for="p in certified.personals" @click="params.personalSeal = p.id">
                                <img :src="baseurl +'/'+ p.sealImgPath" width="122" height="51" />
								<div class="radio-box-person"><span :class="{selectspan : p.id == params.personalSeal}"></span></div>
							</li>
						</ul>
					</div>
				</div>
				<div class="tableCon signPos" style="margin-top: 85px;">
					<p class="fb titleLine">第三步：请选择使用的企业章（单选）</p>
					<div class="companySignatureList clearfix" v-if="certified.enterprises != null && certified.enterprises.length > 0">
						<ul>
							<li class="companySignatureItem" v-for="e in certified.enterprises" @click="params.sealId = e.id">
								<div class="radio-box-company" :class="{companySignatureSelected : e.id == params.sealId}">
									<img :src="baseurl +'/'+ e.sealData" width="150" height="150" />
									<div class="companySignatureNickName">{{e.copyWriting}}</div>
								</div>
							</li>
						</ul>
					</div>
				</div>
				<button class="layui-btn layui-btn-normal submit" @click="submit">确认签署</button>
			</div>
			</template>
		</div>
	</body>
	<%@ include file="/views/include/floor.jsp" %>
	<script>
      $(document).ready(function () {
        var v = new Vue({
          el: '#vue-content',
          data: {
            baseurl: base_static,
            order: {},
            certified:{},
            params:{orderId : 0, personalSeal: 0, sealId:0},//要提交的数据
            code: {text:'获取验证码', time : 0, value: ''} //验证码
          },
          created: function () {
            loadOrder(this);
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
	  });
      function loadOrder(vm){
        var orderKey = $('body').attr('id');
        console.log('orderKey--------------'+ orderKey);
        $.util.json(base_url + '/enterprise/order/orderDetail/'+ orderKey, null, function (data) {
          if (data.success) {//处理返回结果
            vm.order = data.reslut;
            vm.params.orderId = data.reslut.id;
            if(data.pathPrefix){
              vm.baseurl = data.pathPrefix;
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
          } else {
            $.util.error(data.message);
          }
        });
      }
      function loadSmsCode(vm){
        var parmas = {orderKey : vm.params.orderId}
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
        var params = $.extend({}, vm.params, {code: vm.code.value});
        console.log(params);
        if (!params.code) {
          $.util.error("签署短信校验码不能为空");
        }
        /*
        else if (!params.personalSeal) {
          $.util.error("请选择个人印章");
        }
        */
        else if (!params.sealId) {
          $.util.error("请选择企业印章");
        }else{
          $.util.json(base_url + '/enterprise/order/Sign', params, function (data) {
            if (data.success) {//处理返回结果
              $.util.success(data.message, function(){
                loadOrder(vm);
              }, 3000);
            } else {
              $.util.error(data.message);
            }
          });
        }
      }
	</script>
</html>
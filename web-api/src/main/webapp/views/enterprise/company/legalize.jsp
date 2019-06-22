<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/css/select2.min.css" rel="stylesheet" />
<head>
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
	<title>物流跟踪-企业管理-企业认证</title>
	<%@ include file="/views/include/head.jsp" %>
	<link rel="stylesheet" href="${baseStatic}css/legalize.css?times=${times}"/>
</head>
<body>
<div class="qlm-content">
	<div class="legalize-con" id="legalize-con">
		<template>
			<div class="confirm-progress" :class="{half:fettle == 2, done: fettle == 3}">
				<i></i>
				<span class="people">个人认证</span>
				<span class="company">企业信息校验</span>
				<span class="public">对公账户信息校验</span>
			</div>
			<%--个人认证--%>
			<div class="people-confirm" v-if="fettle == 1">
				<div class="line-part">
					<span class="float-tag"><span class="need">*</span>姓名：</span>
					<div class="text-tag"><input type="text" v-model="legalize.name" class="input-tag"></div>
				</div>
				<div class="line-part">
					<span class="float-tag"><span class="need">*</span>身份证号：</span>
					<div class="text-tag"><input type="text" v-model="legalize.idCardNo" class="input-tag"></div>
				</div>
				<div class="line-part">
					<span class="float-tag"><span class="need">*</span>银行卡号：</span>
					<div class="text-tag"><input type="text" v-model="legalize.brankCardNo" class="input-tag"></div>
				</div>
				<div class="line-part">
					<span class="float-tag"><span class="need">*</span>银行预留手机号：</span>
					<div class="text-tag"><input type="text" v-model="legalize.mobilePhone" class="input-tag"></div>
				</div>
				<div class="line-part get-code">
					<input type="text" name="code" v-model="code.value">
					<button class="get-code-btn" :class="{ active: code.time != 0}" :disabled="code.time != 0" @click="loadCode">{{code.text}}</button>
				</div>
				<div class="save-btn">
					<a href="javascript:;" class="layui-btn layui-btn-normal toCompanyBtn" @click="postLegalize">下一步</a>
				</div>
			</div>
			<%--企业信息--%>
			<div class="company-confirm" v-if="fettle == 2">
				<div class="line-part">
					<span class="float-tag"><span class="need">*</span>企业名称：</span>
					<div class="text-tag"><input type="text" class="input-tag" v-model="company.companyName"></div>
				</div>
				<div class="line-part">
					<span class="float-tag"><span class="need"></span>组织机构代码：</span>
					<div class="text-tag"><input type="text" class="input-tag" v-model="company.codeOrg"></div>
				</div>
				<div class="line-part">
					<span class="float-tag"><span class="need"></span>社会统一信用代码：</span>
					<div class="text-tag"><input type="text" class="input-tag" v-model="company.codeUsc"></div>
				</div>
				<div class="line-part">
					<span class="float-tag"><span class="need">*</span>法人姓名：</span>
					<div class="text-tag"><input type="text" class="input-tag" v-model="company.legalName"></div>
				</div>
				<div class="line-part">
					<span class="float-tag"><span class="need"></span>法人身份证号：</span>
					<div class="text-tag"><input type="text" class="input-tag" v-model="company.legalIdNo"></div>
				</div>
				<div class="line-part">
					<p class="tips">组织机构代码和社会统一代码至少填一项</p>
				</div>
				<div class="save-btn">
					<a href="javascript:;" class="layui-btn layui-btn-normal toPublicBtn" @click="postCompany">下一步</a>
				</div>
			</div>
			<%--对公账户信息--%>
			<div class="public-confirm" v-if="fettle == 3">
				<div class="line-part" v-if="bankVerify.errorMsg != null">
					<div style="color: #e60000; text-align: center;">{{bankVerify.errorMsg}}</div>
				</div>
				<div class="line-part">
					<span class="float-tag"><span class="need">*</span>对公账户名：</span>
					<div class="text-tag"><input type="text" name="name" class="input-tag" v-model="bankVerify.name"></div>
				</div>
				<div class="line-part">
					<span class="float-tag"><span class="need">*</span>企业对公银行账号：</span>
					<div class="text-tag"><input type="text" name="cardNo" class="input-tag" v-model="bankVerify.cardNo"></div>
				</div>
				<div class="line-part">
					<span class="float-tag"><span class="need">*</span>企业银行账号开户行所在省市：</span>
					<div class="text-tag cityBox">
						<select class="select-tag" v-model="bankVerify.provice" @change="selectChange(1)">
							<option value="">--请选省份--</option>
							<option v-for="p in provinces" :value="p">{{p}}</option>
						</select>
						<select class="select-tag" v-model="bankVerify.city" @change="selectChange(2)">
							<option value="">--请选城市--</option>
							<option v-for="c in cityList" :value="c">{{c}}</option>
						</select>
					</div>
				</div>
				<div class="line-part">
					<span class="float-tag"><span class="need">*</span>企业银行账号开户行名称：</span>
					<div class="text-tag">
							<select id="bank" class="select-tag select2" v-model="bankVerify.bank"  @change="selectChange(3)">
								<option value="">--请选开户行名称--</option>
								<option v-for="b in branks" :value="b">{{b}}</option>
							</select>
					</div>
				</div>
				<div class="line-part">
					<span class="float-tag"><span class="need">*</span>企业银行账号开户行支行全称：</span>
					<div class="text-tag">
						<select id="branch" class="select-tag select2" v-model="bankVerify.prcptcd">
							<option value="">--请选开户行支行全称--</option>
							<option v-for="b in branchs" :value="b.brankCode">{{b.branchName}}</option>
						</select>
					</div>
				</div>
				<div class="line-part agreeLabel">
					<input type="checkbox" id="checkbox_agreement"  v-model="protocol">
					<label for="checkbox_agreement">
						我已阅读并同意 <a class="" href="javascript:;">《合同物流管理平台平台电子签收服务协议》</a>
					</label>
				</div>
				<div class="save-btn">
					<a href="javascript:;" class="layui-btn layui-btn-normal submitBtn" :class="{ active: protocol}" :disabled="protocol"  @click="postVerify">提交</a>
				</div>
			</div>
		</template>
	</div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/js/select2.min.js"></script>
<script src="${baseStatic}plugin/js/cityselect/city.js"></script>
<script src="${baseStatic}plugin/js/cityselect/selectNew.js"></script>
<script  type="text/javascript">
    $(document).ready(function () {
        var v = new Vue({
            el: '#legalize-con',
            data: {
                legalize: {}, //个人认证信息
                company: {}, //企业基础信息
                bankVerify: {provice:'', prcptcd:'', city:'', brank:''},//打款信息
                provinces: [],//省份
                citys: [],//城市
                branks: [],//银行
                branchs:[],//支行信息
                code: {text:'获取验证码', time : 0, value: ''}, //验证码
                fettle:1,   //状态
                protocol: false  //服务协议是否选中
            },
            created: function () { loadEmployees(this); },
            methods:{
                selectChange: function(t){
                    var $this = this;
                    if(t == 1){
                        this.bankVerify.city = '';
                    }
                    this.bankVerify.prcptcd = '';
                    loadBranch(this);
                    $this.initSelect2();
                },
                loadCode:function(){
                    loadSmsCode(this);
                },
                postLegalize: function(){
                    submitLegalize(this);
                },
                postCompany: function(){
                    submitCompany(this);
                },
                postVerify: function(){
                    submitVerify(this);
                },
                initSelect2:function() {
                    var $this = this;
                    $this.$nextTick(function(){
                        $('.select2').select2();
                        $('#bank').on("change", function(e) {
                            $this.bankVerify.bank = $('#bank').val();
                            $this.bankVerify.prcptcd = "";
                            $this.branchs = "";
                            loadBranch($this);
                            $('.select2').select2();
                        })
                        $('#branch').on("change", function(e) {
                            $this.bankVerify.prcptcd = $('#branch').val();
                            loadBranch($this);
                            $('.select2').select2();
                        })
                    });
      			}
            },
            computed:{
                cityList : function(){
                    if(this.bankVerify && this.bankVerify.provice){
                        return this.citys[this.bankVerify.provice];
                    }
                    return [];
                }
            },
			mounted: function(){
                this.initSelect2();
            }
        });
    })
    function loadEmployees(vm){
        $.util.json(base_url + '/enterprise/company/verify/manage', null, function (data) {
            if (data.success) {//处理返回结果
                if(data.company){
                    vm.company = data.company;
                }
                if(data.legalize){
                    vm.legalize = data.legalize;
                }
                if(data.brankData){
                    vm.provinces = data.brankData.provinces;
                    vm.citys = data.brankData.citys;
                    vm.branks = data.brankData.branks;
                }
                if(data.bankVerify){
                    vm.bankVerify = data.bankVerify;
                }
                vm.fettle = data.fettle;
                if(vm.fettle == 3){
                    loadBranch(vm);
                    vm.initSelect2();
                }
            } else {
                $.util.error(data.message);
            }
        });
    }
    function loadBranch(vm){
        var parmas = { brank: vm.bankVerify.bank, province: vm.bankVerify.provice, city: vm.bankVerify.city };
        console.log(parmas);
        if(parmas.brank && parmas.province && parmas.city){
            $.util.json(base_url + '/special/support/brank/branch', parmas, function (data) {
                if (data.success) {//处理返回结果
                    vm.branchs = data.results;
                } else {
                    $.util.error(data.message);
                }
            });
        }else{
            vm.branchs = [];//支行信息
        }
    }

    function loadSmsCode(vm){
        if($.validate.mobile(vm.legalize.mobilePhone)){
            $.util.json(base_url + '/special/smsCode/'+ vm.legalize.mobilePhone, null, function (data) {
                if (data.success) {//处理返回结果
                    //$.util.successfuc(data.message);
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
        }else{
            $.util.error("手机号格式错误");
        }
    }

    function submitLegalize(vm){
        var legalize = vm.legalize;
        if (!legalize.name) {
            $.util.error("姓名不能为空");
        }else if (!$.validate.idCard(legalize.idCardNo)) {
            $.util.error("身份证号格式错误");
        }else if (!legalize.brankCardNo) {
            $.util.error("银行卡号不能为空");
        }else if (!$.validate.mobile(legalize.mobilePhone)) {
            $.util.error("预留手机号不正确");
        }else if (!vm.code.value) {
            $.util.error("验证码不能为空");
        } else {
            var params = {code: vm.code.value, name:legalize.name, idCardNo:legalize.idCardNo, brankCardNo: legalize.brankCardNo, mobilePhone:legalize.mobilePhone};
            $.util.json(base_url + '/enterprise/company/personalAuth', params, function (data) {
                if (data.success) {//处理返回结果
                    $.util.success(data.message, function(){
                        loadEmployees(vm);
                    }, 3000);
                } else {
                    $.util.error(data.message);
                }
            });
        }
    }
    function submitCompany(vm){
        var company = vm.company;
        if (!company.companyName) {
            $.util.error("企业名称不能为空");
        }else if (!company.codeOrg && !company.codeUsc) {
            $.util.error("组织机构代码和社会统一代码至少填一项");
        }else if (!company.legalName) {
            $.util.error("法人名称不能为空");
        }else{
            var params = {companyName: company.companyName, codeOrg:company.codeOrg, codeUsc:company.codeUsc, legalName:company.legalName, legalIdNo:company.legalIdNo, id:company.id};
            $.util.json(base_url + '/enterprise/backstage/authentication', params, function (data) {
                if (data.success) {//处理返回结果
                    $.util.success(data.message, function(){
                        loadEmployees(vm);
                    }, 3000);
                } else {
                    $.util.error(data.message);
                }
            });
        }
    }
    function submitVerify(vm){
        var verify = vm.bankVerify;
        if (!verify.name) {
            $.util.error("对公账户名不能为空");
        } else if (!verify.cardNo) {
            $.util.error("企业对公银行账号不能为空");
        } else if (!verify.provice) {
            $.util.error("请选择开户行所在省市");
        } else if (!verify.city) {
            $.util.error("请选择开户行所在省市");
        } else if (!verify.prcptcd) {
            $.util.error("请选择开户行支行全称");
        } else if (!verify.bank) {
            $.util.error("请选择开户行名称");
        } else {
            var params = {name: verify.name, cardNo:verify.cardNo, provice:verify.provice, city:verify.city, prcptcd:verify.prcptcd, bank:verify.bank, companyId: vm.company.id};
            $.util.json(base_url + '/enterprise/company/add/company/bank', params, function (data) {
                if (data.success) {//处理返回结果
                    $.util.success("您的对公账户将于2个工作日内收到一笔打款信息，收到款项后请在平台提交打款金额数值进行校验，验证无误后即开通电子签收功能", function(){
                        window.location.href= base_url + "/enterprise/company/view/manage";
                    }, 0);
                } else {
                    $.util.error(data.message);
                }
            });
        }
    }
</script>
</html>
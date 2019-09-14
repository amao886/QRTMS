$("#navBarIn").load("../common/navBarIn.html", function() {
	$(".weui-tabbar__item:first").addClass("weui-bar__item--on");

	$('.a-hook').on('click', function() {
		$('.a-hook').removeClass('active');
		$(this).addClass('active');
	})

	//跳转到首页
	$('#myIndex').on('click', function() {
		window.location.href = _common.version('../../home.html?jstatus=' + 0);
	})

	//跳转到扫一扫
	$('#swapCircle').on('click', function() {
		scanCode();
	})

	//跳转到通讯录
	$('#conList').on('click', function() {
		window.location.href = _common.version('../partener/contactList.html?jstatus=' + 1);
	})

	//跳转到个人中心
	$('#peopleCenter').on('click', function() {
		window.location.href = _common.version('../user/peopleCenter.html?jstatus=' + 2 + '&identity=' + _common.getUrlParam('identity'));
	})

	var jstatus = _common.getUrlParam('jstatus');
	console.log('jstatus:' + jstatus);
	if(jstatus == '0') {
		//首页
	} else if(jstatus == '1') {
		//通讯录
		$('.a-hook').removeClass('active');
		$('#conList').addClass('active');
	} else if(jstatus == '2') {
		//个人中心
		$('.a-hook').removeClass('active');
		$('#peopleCenter').addClass('active');
	}
});

var vm = new Vue({
	el: '#vue-common-function',
	data: {
		collection: [],
		identity: _common.getUrlParam('identity'),
		authoritys: []
	},
	methods: {
		listCode: function(num) {
			var _this = this;

			EasyAjax.ajax_Post_Json({
				url: 'admin/user/commonly'
			}, function(res) {
				var results = res.commonlies;
				if(res.success) {
					_this.collection = results;
				}
				console.info(_this.collection);

				_this.getMenuList();
			});
		},
		jumpLink: function(num) {
			goPage(num);
		},
		getMenuList: function() {
			var _this = this;

			EasyAjax.ajax_Post_Json({
				url: 'admin/user/authoritys'
			}, function(res) {
				if(res.success) {
					console.info(res);
					_this.authoritys = res.authoritys;
				}
			});
		},
		searchCode: function(code) {
			var _this = this;

			var result = false;
			for(var i = 0; i < _this.authoritys.length; i++) {
				if(_this.authoritys[i] == code) {
					result = true;
					break;
				}
			}

			return result;
		},
		allFunction: function() {
			var _this = this;

			window.location.href = _common.version('functionAll.html?identity=' + _this.identity);
		},
		enterSetPage: function() {
			var _this = this;

			window.location.href = _common.version('functionSet.html?identity=' + _this.identity);
		}
	},
	computed: {},
	mounted: function() {
		var _this = this;

		_this.listCode();
	}
})

//点击跳转链接
$(".contentBox").on("click", "a.funcLink", function() {
	var _num = $(this).attr("data-num")
	goPage(_num);
});

//页面跳转
var goPage = function(num) {
	var _url = "";
	switch(num) {
		case "104":
			_url = "../../view/customer/customerManage.html";
			break;
		case "105":
			_url = "../../view/partener/waybillEntryOne.html";
			break;
		case "106":
			_url = "../../view/deliveryplan/shipper/deliveryPlan.html";
			break;
		case "107":
			_url = "../../view/deliveryplan/carrier/deliveryPlan.html";
			break;

		case "201":
			$("#swapCircle").trigger("click");
			break; //扫一扫
		case "203":
			_url = "../../view/partener/loadGoods.html?index=index";
			break;
		case "204":
			switch(vm.identity) {
				case "2":
					_url = "../../view/partener/elecReceiptManage.html?receiptSts=shipper";
					break;
				case "3":
					_url = "../../view/partener/elecReceiptManage.html?receiptSts=convey";
					break;
				case "4":
					_url = "../../view/partener/elecReceiptManage.html?receiptSts=receive";
					break;
				default:
					_url = "../../view/partener/electronicManage.html";
					break;
			}
			break;
		case "205":
			_url = "../../view/sitemanagement/siteManageList.html";
			break;

		case "301":
			_url = "../../view/partener/projectTeamTask.html?index=index";
			break;
		case "302":
			_url = "../../view/partener/checkStock.html?index=index";
			break;
		case "303":
			$("#swapCircle").trigger("click");
			break; //扫一扫
		case "304":
			_url = "../../view/partener/loadGoods.html?index=index";
			break;

		case "401":
			_url = "../../view/driver/uploadReceipt.html?showClose=false&waybillId=" + "" + "&index=index";
			break;
		case "403":
			_url = "../../view/partener/transportManageList.html?transportSts=receive";
			break;
		case "404":
			_url = "../../view/complain/complainRecordList.html";
			break;
		case "405":
			_url = "../../view/complain/customerComplainList.html"
			break;

		case "502":
			_url = "../../view/partener/applyResource.html";
			break;
		case "505":
			switch(vm.identity) {
				case "2":
					_url = "../../view/partener/transportManageList.html?transportSts=shipper";
					break;
				case "3":
					_url = "../../view/partener/transportManageList.html?transportSts=covey";
					break;
				case "4":
					_url = "../../view/partener/transportManageList.html?transportSts=receive";
					break;
				default:
					_url = "../../view/partener/transportManage.html";
					break;
			}
			break;
		case "506":
			_url = "../../view/driver/transportRecord.html";
			break;
		case "507":
			$.toast('该功能暂未上线，敬请期待', 'text');
			break;

		case "602":
			_url = "../../view/partener/address.html";
			break;
	}

	if(_url) {
		window.location.href = _common.version(_url);
	}
}
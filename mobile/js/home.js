/*底部 导航部分*/
loadImg();
$("#navBar").load("view/common/navBar.html", function() {

	$('.a-hook').on('click', function() {
		$('.a-hook').removeClass('active');
		$(this).addClass('active');
	})

	//跳转到扫一扫
	$('#swapCircle').on('click', function() {
		scanCode();
	})

	//跳转到通讯录
	$('#conList').on('click', function() {
		window.location.href = _common.version('view/partener/contactList.html?jstatus=' + 1);
	})

	//跳转到个人中心
	$('#peopleCenter').on('click', function() {
		window.location.href = _common.version('view/user/peopleCenter.html?jstatus=' + 2 + '&identity=' + vm.commonFunc.identity);
	})

	//选中状态
	var jstatus = _common.getUrlParam('jstatus');
	console.log('jstatus首页:' + jstatus);
	if(jstatus == '1') {
		//通讯录
		$('.a-hook').removeClass('active');
		$('#conList').addClass('active');
	} else if(jstatus == '2') {
		//个人中心
		$('.a-hook').removeClass('active');
		$('#peopleCenter').addClass('active');
	}
});

if($('.my-task').children().length == 1) {
	$('.my-task').addClass('marBottom');
}

var dLoading = false,
	dNum = 1,
	pageSize = 3;
var vm = {
	commonFunc: new Vue({
		el: '#vue-common-function',
		data: {
			commonlies: [],
			orders: [],
			identity: null
		},
		methods: {
			listCode: function(num) {
				EasyAjax.ajax_Post_Json({
					url: 'admin/user/commonly'
				}, function(res) {
					if(res.success) {
						vm.commonFunc.commonlies = res.commonlies;
						vm.commonFunc.orders = res.orders;
						vm.commonFunc.identity = res.identity;
					}
					console.info(vm.commonFunc.commonlies);
				});
			},
			jumpLink: function(num) {
				var _url = "";
				switch(num) {
					case "104":
						_url = "view/customer/customerManage.html";
						break;
					case "105":
						_url = "view/partener/waybillEntryOne.html?token=" + api_token;
						break;
					case "106":
						_url = "view/deliveryplan/shipper/deliveryPlan.html";
						break;
					case "107":
						_url = "view/deliveryplan/carrier/deliveryPlan.html";
						break;

					case "201":
						$("#swapCircle").trigger("click");
						break; //扫一扫
					case "203":
						_url = "view/partener/loadGoods.html?index=index";
						break;
					case "204":
						switch(vm.commonFunc.identity) {
							case 2:
								_url = "view/partener/elecReceiptManage.html?receiptSts=shipper";
								break;
							case 3:
								_url = "view/partener/elecReceiptManage.html?receiptSts=convey";
								break;
							case 4:
								_url = "view/partener/elecReceiptManage.html?receiptSts=receive";
								break;
							default:
								_url = "view/partener/electronicManage.html";
								break;
						}
						break;
					case "205":
						_url = "view/sitemanagement/siteManageList.html";
						break;

					case "301":
						_url = "view/partener/projectTeamTask.html?index=index";
						break;
					case "302":
						_url = "view/partener/checkStock.html?index=index";
						break;
					case "303":
						$("#swapCircle").trigger("click");
						break; //扫一扫
					case "304":
						_url = "view/partener/loadGoods.html?index=index";
						break;

					case "401":
						_url = "view/driver/uploadReceipt.html?showClose=false&waybillId=" + "" + "&index=index";
						break;
					case "403":
						_url = "view/partener/transportManageList.html?transportSts=receive";
						break;
					case "404":
						_url = "view/complain/complainRecordList.html"
						break;
					case "405":
						_url = "view/complain/customerComplainList.html"
						break;

					case "502":
						_url = "view/partener/applyResource.html";
						break;
					case "505":
						switch(vm.commonFunc.identity) {
							case 2:
								_url = "view/partener/transportManageList.html?transportSts=shipper";
								break;
							case 3:
								_url = "view/partener/transportManageList.html?transportSts=covey";
								break;
							case 4:
								_url = "view/partener/transportManageList.html?transportSts=receive";
								break;
							default:
								_url = "view/partener/transportManage.html";
								break;
						}
						break;
					case "506":
						_url = "view/driver/transportRecord.html";
						break;
					case "507":
						$.toast('该功能暂未上线，敬请期待', 'text');
						break;

					case "602":
						_url = "view/partener/address.html";
						break;
				}

				if(_url) {
					window.location.href = _common.version(_url);
				}
			},
			toDetail: function(id) {
				window.location.href = _common.version("view/partener/transportManageDetail.html?id=" + id + "&fromHomePage=true&fromComplainPage=false");
			},
			toAll: function() {
				window.location.href = _common.version('view/menu/function.html?identity=' + vm.commonFunc.identity);
			}
		},
		mounted: function() {
			this.listCode();
		}
	})
}
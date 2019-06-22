var receiptWrapper = new Vue({
	el: '#receiptWrapper',
	data: {
		showMore: false,
		needId: _common.getUrlParam('id'),
		listReslut: null,
		pathUrl: '',
		abnText: '',
		abnStatus: 0,
		ifSubmit: 0
	},
	filters: {
		toFix: function(value) {
			return value.toFixed(2);
		},
		addUrl: function(value) {
			return receiptWrapper.pathUrl + '/' + value;
		}
	},
	mounted: function() {
		this.getListData();
	},
	methods: {
		//显示隐藏
		showMoreFun: function() {
			this.showMore = !this.showMore;
		},
		//获取数据
		getListData: function() {
			var _this = this;
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/order/orderDetail/' + _this.needId
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");

				} else {
					_this.listReslut = res.reslut;
					_this.pathUrl = res.pathPrefix;
				}
			});
		},
		//签署
		signChecked: function(orderid, signrole) {
			if(signrole == 2 || (signrole == 3 && this.abnStatus != 1)) {
				EasyAjax.ajax_Post_Json({
					url: '/enterprise/order/userCertified/search',
				}, function(res) {
					console.log(res);
					var personals = res.reslut.personals;
					var enterprises = res.reslut.enterprises;
					var name = res.reslut.name;
					var phone = res.reslut.mobilePhone;
					if((personals != null && personals.length > 0) && (enterprises != null && enterprises.length > 0)) {
						window.location.href = _common.version('./signOne.html?id=' + orderid + '&name=' + name + '&phone=' + phone);
					} else {
						if(enterprises == null || enterprises.length <= 0) {
							$.alert("您未被授权签署企业章，请联系管理员进行授权后再来签署~", "");
						} else if(personals == null || personals.length <= 0) {
							$.modal({
								title: "",
								text: "您尚未设置个人签章，无法完成签署，请完成签章设置。",
								buttons: [{
										text: "去设置签章",
										onClick: function() {
											if(res.fettle == 0) {
												window.location.href = _common.version("./personRealNameAuthentication.html?type=signature");
											} else {
												window.location.href = _common.version("./choosePersonSignature.html");
											}
										}
									},
									{
										text: "取消",
										className: "default",
										onClick: function() {
											$.closeModal();
										}
									},
								]
							});
						}
					}
				});
				return;
			}
			if(this.abnStatus == 1 && !this.abnText) {
				$.toast('异常信息必填', 'text');
				return;
			}
			var _this = this;
			var baseValue = {
				orderId: orderid,
				signRole: signrole,
				exception: this.abnStatus,
				content: this.abnText
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/order/Sign',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(res.success) {
					if(signrole == 2) {
						$.toast('物流商签署完成');

					} else if(signrole == 3 && _this.abnStatus == 1) {
						$.toast('异常信息已提交');
						_this.ifSubmit = 1;
					} else if(signrole == 3) {
						$.toast('完好签收');
					}

					_this.getListData();
				}
			});
		},
		// 查看物料明细
		checkDetail: function() {
			window.location.href = _common.version("checkGoodsDetail.html");
			sessionStorage.setItem("orderKey", _common.getUrlParam('id'))
		},
		changeData: function() {
			this.abnStatus = 1;
		}
	}
})
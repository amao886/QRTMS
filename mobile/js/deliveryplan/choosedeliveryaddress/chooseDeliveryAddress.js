var addressList = new Vue({
	el: '#addressList',
	data: {
		companyName: '',
		addresss: []
	},
	created: function() {
		var _this = this;

		_this.getAddressListData();
	},
	methods: {
		getAddressListData: function() {
			var _this = this;

			var baseValue = {
				companyName: _this.companyName
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/customer/address/list',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					_this.addresss = res.addresss;

					setTimeout(function() {
						$('.weui-cell_swiped').swipeout();
					}, 200)
				}
			});
		},
		search: function() {
			var _this = this;

			_this.closeSwipe();

			_this.getAddressListData();
		},
		clear: function() {
			var _this = this;

			_this.closeSwipe();

			_this.likeName = '';
		},
		addAddress: function() {
			window.location.href = _common.version('./addDeliveryAddress.html?role=' + _common.getUrlParam('role') + '&type=' + _common.getUrlParam("type") + '&planKey=' + _common.getUrlParam('planKey') + '&isInputPlanPage=' + _common.getUrlParam('isInputPlanPage'));
		},
		editAddress: function(addressObj) {
			window.location.href = _common.version('./editDeliveryAddress.html?info=' + JSON.stringify(addressObj) + '&role=' + _common.getUrlParam('role') + '&type=' + _common.getUrlParam("type") + '&planKey=' + _common.getUrlParam('planKey') + '&isInputPlanPage=' + _common.getUrlParam('isInputPlanPage'));
		},
		deleteAddress: function(id) {
			$.confirm("确定要删除这条地址吗？", function() {
				//点击确认后的回调函数
				var baseValue = {
					ids: id
				}
				EasyAjax.ajax_Post_Json({
					url: 'enterprise/customer/address/delete',
					data: JSON.stringify(baseValue)
				}, function(res) {
					if(res.success) {
						$.toast(res.message, function() {
							window.location.reload();
						});
					}
				});
			});
		},
		chooseAddress: function(index, addressObj) {
			var _this = this;

			var transform = $($('.weui-cell__bd')[index]).css('transform');
			var transformX = transform.slice(7, transform.length - 1).split(', ')[4];
			console.log(transformX);

			if(transformX < 0) {
				return;
			}

			if(_common.getUrlParam('isInputPlanPage') == 'true') {
				window.location.href = _common.version('../' + _common.getUrlParam('role') + '/inputDeliveryPlanOne.html?isChooseType=2' + '&info=' + JSON.stringify(addressObj));
			} else {
				window.location.href = _common.version('../' + _common.getUrlParam('role') + '/generateInvoiceOne.html?isChooseType=2' + '&info=' + JSON.stringify(addressObj) + '&planKey=' + _common.getUrlParam('planKey'));
			}
		},
		closeSwipe: function() {
			$('.weui-cell_swiped').swipeout('close') //关闭
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});
var goodsDetailEdit = new Vue({
	el: '#goodsDetailEdit',
	data: {
		goodsList: [],
		deleteStatus: false, //显示删除内容
		allFlag: false, //全选判断
		checkFlag: false, //toggle判断
		deleteItem: [], //删除集合
		idItem: [],
		id: 0,
		order: {},
		extra: {},
		commodities: [],
		customDatas: []
	},
	created: function() {
		var _this = this;

		_this.getGoodsListData();
	},
	methods: {
		getGoodsListData: function() {
			var _this = this;

			if(sessionStorage.getItem('generateInvoicePageStore')) {
				_this.order = JSON.parse(sessionStorage.getItem('generateInvoicePageStore')).order;
				_this.extra = JSON.parse(sessionStorage.getItem('generateInvoicePageStore')).extra;
				_this.commodities = JSON.parse(sessionStorage.getItem('generateInvoicePageStore')).commodities;
				_this.customDatas = JSON.parse(sessionStorage.getItem('generateInvoicePageStore')).customDatas;

				if(_this.commodities != null && _this.commodities.length > 0) {
					for(var i = 0; i < _this.commodities.length; i++) {
						var commoditie = {};
						commoditie["id"] = this.goodsList.length;
						commoditie["goodsType"] = _this.commodities[i].commodityNo;
						commoditie["goodsWeight"] = _this.commodities[i].weight;
						commoditie["goodsVolume"] = _this.commodities[i].volume;
						commoditie["goodsQuantity"] = _this.commodities[i].quantity;
						commoditie["box"] = _this.commodities[i].boxCount;
						commoditie["unit"] = _this.commodities[i].commodityUnit;
						commoditie["summary"] = _this.commodities[i].commodityName;
						commoditie["remark"] = _this.commodities[i].remark;
						this.goodsList.push(commoditie);
					}
				}
			}

			setTimeout(function() {
				_this.initScroll();
			}, 20);
		},
		//初始化better-scroll插件
		initScroll: function() {
			var _this = this;
			if(!_this.scroll) {
				_this.scroll = new BScroll(_this.$refs.wrapper, {
					scrollX: true,
					scrollY: false,
					momentum: false,
					click: true,
					bounce: false
				});
			} else {
				_this.scroll.refresh();
			}
		},
		processAddressData: function() {
			var _this = this;

			if(_this.order.receiveAddress != null) {
				var address = _this.order.receiveAddress.split(" ");
				_this.order.receiveAddress = address[0].replace(/\,+/g, "") + address[1];
			}

			if(_this.extra != null) {
				if(_this.extra.distributeAddress != null) {
					var address = _this.extra.distributeAddress.split(" ");
					_this.extra.distributeAddress = address[0].replace(/\,+/g, "") + address[1];
				}
				if(_this.extra.originStation != null) {
					var address = _this.extra.originStation.split(" ");
					_this.extra.originStation = address[0].replace(/\,+/g, "") + address[1];
				}
				if(_this.extra.arrivalStation != null) {
					var address = _this.extra.arrivalStation.split(" ");
					_this.extra.arrivalStation = address[0].replace(/\,+/g, "") + address[1];
				}
			}
		},
		//保存发货计划
		saveDeliveryPlan: function() {
			var _this = this;

			if(_this.goodsList.length < 1) {
				$.toast('至少有一行物料信息', 'text');
				return;
			}
			for(var i = 0; i < _this.goodsList.length; i++) {
				if(!_this.goodsList[i].goodsType && !_this.goodsList[i].goodsWeight && !_this.goodsList[i].goodsVolume && !_this.goodsList[i].goodsQuantity && !_this.goodsList[i].box && !_this.goodsList[i].unit && !_this.goodsList[i].summary && !_this.goodsList[i].remark) {
					$.toast('第' + (i + 1) + '行至少添加一个物料信息', 'text');
					return;
				}
			}

			_this.processAddressData();

			var commodities = [];
			var id = ['commodityNo', 'weight', 'volume', 'quantity', 'boxCount', 'commodityUnit', 'commodityName', 'remark'];
			for(var i = 0; i < this.goodsList.length; i++) {
				var commoditie = {};
				commoditie[id[0]] = $.trim(this.goodsList[i].goodsType);
				commoditie[id[1]] = $.trim(this.goodsList[i].goodsWeight);
				commoditie[id[2]] = $.trim(this.goodsList[i].goodsVolume);
				commoditie[id[3]] = $.trim(this.goodsList[i].goodsQuantity);
				commoditie[id[4]] = $.trim(this.goodsList[i].box);
				commoditie[id[5]] = $.trim(this.goodsList[i].unit);
				commoditie[id[6]] = $.trim(this.goodsList[i].summary);
				commoditie[id[7]] = $.trim(this.goodsList[i].remark);
				commodities.push(commoditie);
			}

			var baseValue = {
				planKey: _common.getUrlParam('planKey'),
				order: JSON.stringify(_this.order),
				extra: JSON.stringify(_this.extra),
				commodities: JSON.stringify(commodities),
				customDatas: JSON.stringify(_this.customDatas)
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/plan/shipper/generate/order',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(res.success) {
					sessionStorage.removeItem('generateInvoicePageStore');
					$.toast(res.message, function() {
						window.location.href = _common.version("./deliveryPlan.html");
					});
				}
			});
		},
		//新增物料
		addGoods: function() {
			var commoditie = {};
			commoditie["id"] = this.goodsList.length;
			commoditie["goodsType"] = '';
			commoditie["goodsWeight"] = '';
			commoditie["goodsVolume"] = '';
			commoditie["goodsQuantity"] = '';
			commoditie["box"] = '';
			commoditie["unit"] = '';
			commoditie["summary"] = '';
			commoditie["remark"] = '';
			this.goodsList.push(commoditie);
			console.log(this.goodsList);
			this.id++;
		},
		//删除物料
		deleteGoods: function() {
			if(this.deleteItem.length <= 0) {
				$.toast("请先选中删除项", "text");
				return;
			}
			var _this = this;
			$.confirm("确定删除吗", '', function() {
				//点击确认后的回调函数
				var temps = [];
				for(var i = 0; i < _this.goodsList.length; i++) {
					var flag = true;
					for(var j = 0; j < _this.deleteItem.length; j++) {
						if(i == _this.deleteItem[j]) {
							flag = false;
							break;
						}
					}
					if(flag) {
						temps.push(_this.goodsList[i]);
					}
				}
				_this.goodsList = temps;
				_this.deleteItem = [];
				//删除总的物料
				if(_this.idItem.length > 0) {
					_this.deleteStatus = false;
					_this.idItem = [];
					this.id = 0;
					_this.checkFlag = false;
				}
			}, function() {
				//点击取消后的回调函数
			});
		},
		//单选
		checkOne: function(value, num) {
			if(typeof value.checked == 'undefined') {
				this.$set(value, 'checked', true);
			} else {
				value.checked = !value.checked;
			}
			if(value.checked) {
				//选中
				console.log('选中');
				this.deleteItem.push(num);
				if(this.goodsList[num].id) {
					this.idItem.push(this.goodsList[num].id);
				}
				if(this.deleteItem.length == this.goodsList.length) {
					this.allFlag = true;
				}
				console.log('选中idItem:' + this.idItem);
			} else {
				//没有选中
				console.log('没有选中');
				var nowIndex = this.deleteItem.indexOf(num);
				this.deleteItem.splice(nowIndex, 1);
				console.log(this.idItem);
				if(this.goodsList[num].id) {
					var nowId = this.idItem.indexOf(this.goodsList[num].id);
					this.idItem.splice(nowId, 1);
				}
				console.log('没有选中idItem:' + this.idItem);
			}
			var _this = this;
			this.goodsList.forEach(function(val, index) {
				if(val.checked == false) {
					_this.checkFlag = false;
					_this.allFlag = false; //取消全选
					return;
				}
			});

		},
		//全选
		checkAll: function(flag) {
			this.allFlag = flag;
			var _this = this;
			this.goodsList.forEach(function(val, index) {
				_this.deleteItem.push(val.id);
				_this.idItem.push(val.id)
				if(typeof val.checked == 'undefined') {
					_this.$set(val, 'checked', flag);
				} else {
					val.checked = flag;
				}
			});
			if(!flag) {
				_this.deleteItem.splice(0, _this.deleteItem.length);
				_this.idItem.push(0, _this.idItem.length)
			}
			console.log(_this.deleteItem)
		},
		//切换全选
		toggleCheck: function() {
			if(this.checkFlag) {
				this.checkAll(false);
				this.checkFlag = false;
			} else {
				//第一次点击
				this.checkAll(true);
				this.checkFlag = true;
			}
		},
		//取消全选
		cancelCheckAll: function() {
			this.deleteStatus = false;
			this.checkAll(false);
			this.checkFlag = false;
		},
		//点击删除按钮
		showDeleteBox: function() {
			this.deleteStatus = true;
			this.allFlag = false;
		},
		//重量，体积，数量，箱数验证
		weightValidate: function(value) {
			value.goodsWeight = value.goodsWeight.replace(/[^\d\.]/g, '');
			value.goodsWeight = value.goodsWeight.replace(/^\./g, ""); //验证第一个字符是数字
			value.goodsWeight = value.goodsWeight.replace(/\.{2,}/g, "."); //只保留第一个, 清除多余的
			value.goodsWeight = value.goodsWeight.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
			value.goodsWeight = value.goodsWeight.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
			console.log(value.goodsWeight);
		},
		volumeValidate: function(value) {
			value.goodsVolume = value.goodsVolume.replace(/[^\d\.]/g, '');
			value.goodsVolume = value.goodsVolume.replace(/^\./g, ""); //验证第一个字符是数字
			value.goodsVolume = value.goodsVolume.replace(/\.{2,}/g, "."); //只保留第一个, 清除多余的
			value.goodsVolume = value.goodsVolume.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
			value.goodsVolume = value.goodsVolume.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
		},
		quantityValidate: function(value) {
			value.goodsQuantity = value.goodsQuantity.replace(/\D/g, '');
			//			value.goodsVolume = value.goodsVolume.replace(/[^\d\.]/g, '');
			//			value.goodsQuantity = value.goodsQuantity.replace(/^\./g, ""); //验证第一个字符是数字
			//			value.goodsQuantity = value.goodsQuantity.replace(/\.{2,}/g, "."); //只保留第一个, 清除多余的
			//			value.goodsQuantity = value.goodsQuantity.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
			//			value.goodsQuantity = value.goodsQuantity.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
		},
		boxValidate: function(value) {
			value.box = value.box.replace(/\D/g, '');
			//			value.goodsVolume = value.goodsVolume.replace(/[^\d\.]/g, '');
			//			value.box = value.box.replace(/^\./g, ""); //验证第一个字符是数字
			//			value.box = value.box.replace(/\.{2,}/g, "."); //只保留第一个, 清除多余的
			//			value.box = value.box.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
			//			value.box = value.box.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
		}
	}
});
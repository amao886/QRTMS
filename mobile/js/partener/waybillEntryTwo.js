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
		required: [],
		templateKey: _common.getUrlParam('modelNo'),
		waybillInfo: []
	},
	created: function() {
		var _this = this;
		if(JSON.parse(sessionStorage.getItem('sendGoodsPageStore')).data) {
			_this.waybillInfo = JSON.parse(sessionStorage.getItem('sendGoodsPageStore')).data;
		}
		_this.getModelFiledNameListData();
	},
	methods: {
		getModelFiledNameListData: function() {
			var _this = this;
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/template/templateInfo/' + _this.templateKey,
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					_this.required = res.required;
					var rightTabWidth = 0;
					if(_this.required.weight != null) {
						rightTabWidth = rightTabWidth + 85;
					}
					if(_this.required.volume != null) {
						rightTabWidth = rightTabWidth + 85;
					}
					if(_this.required.quantity != null) {
						rightTabWidth = rightTabWidth + 85;
					}
					if(_this.required.boxCount != null) {
						rightTabWidth = rightTabWidth + 85;
					}
					if(_this.required.commodityUnit != null) {
						rightTabWidth = rightTabWidth + 85;
					}
					if(_this.required.commodityName != null) {
						rightTabWidth = rightTabWidth + 220;
					}
					if(_this.required.remark != null) {
						rightTabWidth = rightTabWidth + 120;
					}
					$(".listRight .content").width(rightTabWidth);
					setTimeout(function() {
						_this.initScroll();
					}, 20);
				}
			});
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
		//保存发货数据
		confirmReleaseGoods: function() {
			if(this.goodsList.length < 1) {
				$.toast('至少有一行物料信息', 'text');
				return;
			}
			for(var i = 0; i < this.goodsList.length; i++) {
				if(!this.goodsList[i].goodsType && !this.goodsList[i].goodsWeight && !this.goodsList[i].goodsVolume && !this.goodsList[i].goodsQuantity && !this.goodsList[i].box && !this.goodsList[i].unit && !this.goodsList[i].summary && !this.goodsList[i].remark) {
					$.toast('第' + (i + 1) + '行至少添加一个物料信息', 'text');
					return;
				}
			}

			var commodities = [];
			var id = [this.required.commodityNo, this.required.weight, this.required.volume, this.required.quantity, this.required.boxCount, this.required.commodityUnit, this.required.commodityName, this.required.remark];
			for(var i = 0; i < this.goodsList.length; i++) {
				var commoditie = {};
				if(this.required.commodityNo != null) {
					commoditie[id[0]] = $.trim(this.goodsList[i].goodsType);
				}
				if(this.required.weight != null) {
					commoditie[id[1]] = $.trim(this.goodsList[i].goodsWeight);
				}
				if(this.required.volume != null) {
					commoditie[id[2]] = $.trim(this.goodsList[i].goodsVolume);
				}
				if(this.required.quantity != null) {
					commoditie[id[3]] = $.trim(this.goodsList[i].goodsQuantity);
				}
				if(this.required.boxCount != null) {
					commoditie[id[4]] = $.trim(this.goodsList[i].box);
				}
				if(this.required.commodityUnit != null) {
					commoditie[id[5]] = $.trim(this.goodsList[i].unit);
				}
				if(this.required.commodityName != null) {
					commoditie[id[6]] = $.trim(this.goodsList[i].summary);
				}
				if(this.required.remark != null) {
					commoditie[id[7]] = $.trim(this.goodsList[i].remark);
				}
				commodities.push(commoditie);
			}

			var baseValue = {
				templateKey: this.templateKey,
				waybillInfo: JSON.stringify(this.waybillInfo),
				commodities: JSON.stringify(commodities)
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/template/entrance/conveyer',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(res.success) {
					sessionStorage.removeItem('sendGoodsPageStore');
					$.toast(res.message, function() {
						window.location.href = _common.version("../../home.html");
					});
				}
			});
		},
		//新增物料
		addGoods: function() {
			var commoditie = {};
			commoditie["id"] = this.goodsList.length;
			if(this.required.commodityNo != null) {
				commoditie["goodsType"] = '';
			}
			if(this.required.weight != null) {
				commoditie["goodsWeight"] = '';
			}
			if(this.required.volume != null) {
				commoditie["goodsVolume"] = '';
			}
			if(this.required.quantity != null) {
				commoditie["goodsQuantity"] = '';
			}
			if(this.required.boxCount != null) {
				commoditie["box"] = '';
			}
			if(this.required.commodityUnit != null) {
				commoditie["unit"] = '';
			}
			if(this.required.commodityName != null) {
				commoditie["summary"] = '';
			}
			if(this.required.remark != null) {
				commoditie["remark"] = '';
			}
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
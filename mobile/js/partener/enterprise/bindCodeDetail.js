var codeDetail = new Vue({
	el: '#bindCodeDetail',
	data: {
		orderKey:_common.getUrlParam('orderKey'),
		deliveryNo: "",
		listResult: null,
		commodities: [],
		fettle:'',
		convey: {},
		createYourself: false
	},
	created: function() {
		this.getListData();
	},
	mounted: function() {

	},
	methods: {
		submit:function(){
			var _this = this;
			
			 var parmas = {
			 	orderKey:_this.orderKey,
			 	deliveryNo:_this.deliveryNo
			 }
//			 var reg = /^[0-9a-zA-Z]+$/;
//			  if(!reg.test(parmas.deliveryNo)){
//			  	$.toast('只能输入数字和英文', "cancel");
//			  	   return false;
//			  }
			 EasyAjax.ajax_Post_Json({
				url: 'enterprise/order/update/deliveryNo', 
				data:JSON.stringify(parmas)
			}, function(res) {
			 window.location.reload()
				
			});
			
		},
		editSave:function(event){
			$('#deliveryNo').focus()
		},
		getListData: function() {
			var _this = this;
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/order/orderDetail/' + _common.getUrlParam('orderKey')
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					_this.listResult = res.reslut;
					if(_this.listResult.deliveryNo != null) {
						_this.deliveryNo = _this.listResult.deliveryNo;
					}
					if(_this.listResult.convey != null) {
						_this.convey = _this.listResult.convey;
					}
					_this.fettle = _this.listResult.fettle;
					_this.createYourself = _this.listResult.createYourself;
					console.log(_this.listResult)
					if(res.reslut) {
						
						_this.commodities = res.reslut.commodities;
						
					}

					_this.$nextTick(function() {
						_this.initScroll();
						_this.calcHeight();
					})
				}
			});
		},
		bindCode: function(orderKey) {
			var _this = this;
			var baseValue = {
				code: sessionStorage.getItem('qrcode'),
				orderKey: orderKey
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/order/code/bind',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");

				} else {
					$.toast("二维码绑定成功", function() {
						codeDetail.getListData();
						scanCode(); //调扫一扫接口
						CloseWebPage();
					});

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
		//计算高度
		calcHeight: function() {
			var _this = this;
			var titleEle = $('.listLeft .goodsName');
			titleEle.each(function(i) {
				var $this = $(this);
				var height = $(".line_" + i).height();
				$this.css({
					'height': height + 'px'
				});
			});
		},
		//关闭窗口
		CloseWebPage: function() {
			if(navigator.userAgent.indexOf("MSIE") > 0) {
				if(navigator.userAgent.indexOf("MSIE 6.0") > 0) {
					window.opener = null;
					window.close();
				} else {
					window.open('', '_top');
				}
			} else if(navigator.userAgent.indexOf("Firefox") > 0) {
				window.location.href = 'about:blank ';
			} else {
				window.opener = null;
				window.open('', '_self', '');
				window.close();
			}
		},
		confirmArrivalGoods: function(orderid) {
			//获取地理位置
			wx.ready(function() {
				console.log('获取地理位置');
				wx.getLocation({
					type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
					success: function(res) {
						var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
						var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
						geocoder(latitude, longitude, orderid);

					},
					fail: function(res) {
						requestArrivalGoods(orderid);
					}
				});
			});
		},
		isEditDeliverCode: function() {
			var _this = this;

			if(_this.isNull(_this.deliveryNo) || (!_this.isNull(_this.deliveryNo) && _this.createYourself)) {
				return true;
			}

			return false;
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
});

//反向地理编码
function geocoder(latitude, longitude, id) {
	var wgs84togcj02 = coordtransform.wgs84togcj02(longitude, latitude);
	longitude = wgs84togcj02[0];
	latitude = wgs84togcj02[1];
	AMap.service('AMap.Geocoder', function() { //回调函数
		//逆地理编码
		var lnglatXY = [longitude, latitude]; //地图上所标点的坐标
		var geocoder = new AMap.Geocoder();
		geocoder.getAddress(lnglatXY, function(status, result) {
			if(status === 'complete' && result.info === 'OK') {
				//定位完成后自动上报位置
				var addressTxt = result.regeocode.formattedAddress+"("+result.regeocode.addressComponent.district+result.regeocode.addressComponent.township+result.regeocode.addressComponent.street+result.regeocode.addressComponent.streetNumber+")";
				if(addressTxt == "") {
					requestArrivalGoods(id);
				} else {
					uploadPosition({
						orderKey: id,
						latitude: latitude,
						longitude: longitude,
						location: addressTxt
					});
				}
			} else {
				//获取地址失败
				requestArrivalGoods(id);
			}
		});
	});
}

//ajax上报经纬度
function uploadPosition(options) {
	var baseValue = {
		orderKey: options.orderKey,
		longitude: options.longitude,
		latitude: options.latitude,
		location: options.location
	}
	EasyAjax.ajax_Post_Json({
		url: 'enterprise/order/loaction',
		data: JSON.stringify(baseValue)
	}, function(res) {
		requestArrivalGoods(options.orderKey);
	})
}

function requestArrivalGoods(orderid) {
	EasyAjax.ajax_Post_Json({
		url: 'enterprise/order/arrivel/' + orderid,
	}, function(res) {
		if(!res.success) {
			$.toast("加载失败，请稍后重试", "text");
		} else {
			$.toast("收货成功", function() {
				codeDetail.getListData();
				scanCode(); //调扫一扫接口
				CloseWebPage();
			});
		}
	});
}
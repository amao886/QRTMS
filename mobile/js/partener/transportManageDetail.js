var goodsDetail = new Vue({
	el: '#transportDetailsCon',
	data: {
		orderKey: '',
		quantity: 0,
		volume: 0,
		boxCount: 0,
		weight: 0,
		fettle: 1,
		deliveryNo: "",
		bindCode: "--",
		conveyName: "",
		receiveName: "",
		receiveAddress: "",
		receiverName: "",
		receiverContact: "",
		arrivedTime: "",
		receiveTime: "",
		signFettle: 0,
		exception: {},
		imagePath: "",
		commodities: [],
		operates: [],
		imageStorages: [],
		userType: 0, // 1:发货方  2:承运方  3:收货方
		insertType: 0, // 1:发货方  2:收货方  3:承运方
		complaint: {},
		complainantContent: null,
		fromHomePage: _common.getUrlParam("fromHomePage"), //true:首页列表进入  false:其他列表进入
		fromComplainPage: _common.getUrlParam("fromComplainPage") //true:客户投诉列表进入  false:其他列表进入
	},
	mounted: function() {
		var _this = this;
		_this.getListData();
		_this.orderKey = _common.getUrlParam('id');

	},
	filters: {
		formatDate: function(time) {
			var date = new Date(time);
			return dateFormatCommon(date, 3);
		},
		formatTime: function(time) {
			var date = new Date(time);
			return dateFormatCommon(date, 4);
		}
	},
	methods: {
		submit: function() {
			var _this = this;
			$('#deliveryNo').prop('readonly', true)
			var parmas = {
				orderKey: _this.orderKey,
				deliveryNo: _this.deliveryNo
			}
			var reg = /^[0-9a-zA-Z]+$/;
			if(!reg.test(parmas.deliveryNo)) {
				$.toast('只能输入数字和英文', "cancel");
				return false;
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/order/update/deliveryNo',
				data: JSON.stringify(parmas)
			}, function(res) {
				window.location.reload()

			});
		},
		deleteOrder: function(roleType) {
			var _this = this;

			$.confirm({
				text: '确定要删除吗?',
				onOK: function() {
					var roleSts = '';
					if(roleType == 1) {
						roleSts = 'shipper';
					} else if(roleType == 2) {
						roleSts = 'conveyer';
					}

					var idArray = [_this.orderKey];
					var parmas = {
						ids: JSON.stringify(idArray)
					}
					EasyAjax.ajax_Post_Json({
						url: 'enterprise/order/' + roleSts + '/manage/delete',
						data: JSON.stringify(parmas)
					}, function(res) {
						if(res.success) {
							$.toast(res.message, function() {
								var roleSts = '';
								if(roleType == 1) {
									roleSts = 'shipper';
								} else if(roleType == 2) {
									roleSts = 'convey';
								}
								if(_this.fromHomePage == 'true') {
									window.location.href = _common.version("../../home.html");
								} else {
									window.location.href = _common.version("../../view/partener/transportManageList.html?transportSts=" + roleSts);
								}
							});
						}
					});
				}
			})
		},
		editOrder: function(roleType) {
			var _this = this;

			window.location.href = _common.version("./editOrderOne.html?id=" + _this.orderKey + "&type=" + roleType + "&fromHomePage=" + _this.fromHomePage);
		},
		editSave: function(event) {

			$('#deliveryNo').prop('readonly', false);
			$('#deliveryNo').focus();
		},
		evaluate: function(evaluationKey) { //evaluationKey-- 0:差评  1:好评 
			var _this = this;

			var tripContent = '您选择的是好评，确定提交吗？';
			if(evaluationKey == 0) {
				tripContent = '您选择的是差评，确定提交吗？';
			}
			$.confirm(tripContent, function() {
				var baseValue = {
					orderKey: _this.orderKey,
					evaluation: evaluationKey
				}
				EasyAjax.ajax_Post_Json({
					url: 'enterprise/order/evaluation',
					data: JSON.stringify(baseValue)
				}, function(res) {
					if(res.success) {
						$.toast(res.message, function() {
							window.location.href = _common.version("./receiveGoodFinish.html?fromReceiveGoodNormalPage=true");
						});
					} else {
						$.toast(res.message);
					}
				});
			});
		},
		abnormalSign: function() {
			var _this = this;

			window.location.href = _common.version("./receiveGoodAbnormal.html?id=" + _this.orderKey);
		},
		complain: function() {
			var _this = this;

			window.location.href = _common.version("../../view/complain/complain.html?id=" + _this.orderKey + "&fromHomePage=" + _this.fromHomePage + "&fromComplainPage=" + _this.fromComplainPage);
		},
		getListData: function() {
			var _this = this;
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/order/orderDetail/' + _common.getUrlParam('id')
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					console.log(res);
					_this.quantity = res.reslut.quantity;
					_this.volume = res.reslut.volume;
					_this.boxCount = res.reslut.boxCount;
					_this.weight = res.reslut.weight;
					_this.fettle = res.reslut.fettle;
					_this.deliveryNo = res.reslut.deliveryNo;
					_this.bindCode = _this.formatNullString(res.reslut.bindCode);
					_this.conveyName = res.reslut.convey.companyName;
					_this.receiveName = res.reslut.receive.companyName;
					_this.receiveAddress = res.reslut.receiveAddress;
					_this.receiverName = res.reslut.receiverName;
					_this.receiverContact = res.reslut.receiverContact;
					_this.arrivedTime = res.reslut.arrivedTime;
					_this.receiveTime = res.reslut.receiveTime;
					_this.signFettle = res.reslut.signFettle;
					_this.exception = res.reslut.exception;
					_this.commodities = res.reslut.commodities;
					_this.operates = res.operates;
					_this.imageStorages = res.reslut.imageStorages;
					_this.imagePath = res.imagePath;
					_this.userType = res.reslut.isUserType;
					_this.insertType = res.reslut.insertType;
					_this.complaint = res.reslut.complaint;
					if(_this.complaint != null) {
						_this.complainantContent = res.reslut.complaint.complainantContent;
					}
					_this.$nextTick(function() {
						_this.initScroll();
						_this.calcHeight();
					})
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
			var contentEle = $('.listRight .table-body');
			var heightArray = new Array();
			titleEle.each(function(i) {
				var height = $(".line_" + i).height();
				heightArray[i] = height;
			});
			var height = Math.max.apply(null, heightArray);
			titleEle.each(function(i) {
				var $this = $(this);
				$this.css({
					'height': height + 'px'
				});
			});
			contentEle.each(function(i) {
				var $this = $(this);
				$this.css({
					'height': height + 'px'
				});
			});
		},
		lookBigPic: function(imgUrl) {
			$('#gallery').show().children('.weui-gallery__img').css('background-image', 'url(' + imgUrl + ')').click(function() {
				$('#gallery').hide();
			})
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		},
		formatNullString: function(text) {
			var result = "--";
			if(this.isNull(text)) {
				return result;
			}
			return text;
		}
	}
});

function dateFormatCommon(msg, type) {
	if(msg != null && msg != "") {
		var time = new Date(msg);
		var year = time.getFullYear();
		var month = time.getMonth() + 1;
		var day = time.getDate();
		var hour = time.getHours();
		var minute = time.getMinutes();
		var second = time.getSeconds();
		month = month < 10 ? '0' + month : month;
		day = day < 10 ? '0' + day : day;
		hour = hour < 10 ? '0' + hour : hour;
		minute = minute < 10 ? '0' + minute : minute;
		second = second < 10 ? '0' + second : second;
		switch(type) {
			case 1:
				return year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;
			case 2:
				return year + '-' + month + '-' + day + ' ' + hour + ':' + minute;
			case 3:
				return year + '-' + month + '-' + day;
			case 4:
				return hour + ':' + minute;
			default:
				return year + '年' + month + '月' + day + '日' + ' ' + hour + ':' + minute;
		}
	} else {
		return "--"
	}
}

$(function() {
	var $actionsheet = $('#actionsheet');
	var $mask = $('#mask');

	function hideActionSheet() {
		$("#actionsheetWidget").css("display", "none");
		$actionsheet.removeClass('weui-actionsheet_toggle');
		$mask.fadeOut(200);
		$.toast('完好收货成功');
	}

	$mask.on('click', hideActionSheet);
	$('#actionsheetCancel').on('click', hideActionSheet);
	$("#normalSign").on("click", function() {
		var orderIdAry = [goodsDetail.orderKey];

		var baseValue = {
			orderIds: JSON.stringify(orderIdAry)
		}
		EasyAjax.ajax_Post_Json({
			url: 'enterprise/order/arrivel',
			data: JSON.stringify(baseValue)
		}, function(res) {
			if(res.success) {
				goodsDetail.getListData();
				$("#actionsheetWidget").css("display", "block");
				$actionsheet.addClass('weui-actionsheet_toggle');
				$mask.fadeIn(200);
			} else {
				$.toast(res.message);
			}
		});
	});
});
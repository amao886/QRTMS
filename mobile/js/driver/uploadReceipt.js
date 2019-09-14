console.log(localStorage.getItem("clickTag"));

var recordList = new Vue({
	el: '#uploadReceiptWrapper',
	data: {
		uploadId: _common.getUrlParam('waybillId') || '',
		showClose: _common.getUrlParam('showClose'),
		result: [],
		shipper: []
	},
	mounted: function() {
		var _this = this;
		_this.initPage();
	},
	methods: {
		initPage: function() {
			var _this = this;
			if(_this.uploadId == '') { //从首页进入，没有任务单id
				$('#upload-task-title').show();
			} else {
				_this.getTaskDetail();
			}
		},
		getTaskDetail: function() {
			var _this = this;
			EasyAjax.ajax_Get_Json({
				url: 'enterprise/order/detail/code/' + _this.uploadId
			}, function(res) {
				$('.upload-task').addClass('cancelPadding');
				if(_this.showClose == 'true') {
					$('.showTaskList').show();
					$('#uploadTaskClose').show();
				} else {
					$('#uploadTaskClose').hide();
				}

				_this.result = res.reslut;
				_this.shipper = res.reslut.shipper;
			});
		},
		uploadScanCode: function() {
			var _this = this;
			wx.scanQRCode({
				needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
				scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
				success: function(res) {
					var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
					var code = '';
					//alert(result);
					//要判断是新条码还是旧条码
					if(result.indexOf('scan') > -1) { //是新条码
						var start = result.lastIndexOf('/') + 1;
						var end = result.lastIndexOf('?');
						code = result.substring(start, end);
						_this.getScanCode(code);

					} else if(result.indexOf('code') > -1) { //旧条码	
						code = _this.getWXParam('code', result);
						_this.getScanCode(code);
					} else {
						$.toast("无效条码", "cancel");
					}

				}
			});
		},
		chooseList: function() {
			window.location.href = './chooseTransportTask.html';
		},
		getWXParam: function(name, url) {
			var num = url.indexOf('?');
			var str = url.substring(num + 1);
			var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
			var result = str.match(reg);
			return result ? decodeURIComponent(result[2]) : null;
		},
		getScanCode: function(code) {
			var _this = this;
			EasyAjax.ajax_Get({
				url: 'enterprise/order/detail/code/' + code
			}, function(res) {
				$('.upload-task').addClass('cancelPadding');
				$('#upload-task-title').hide(); //扫码成功后隐藏再次扫码
				$('.showTaskList').show(); //显示内容框
				$('#show-task-list').show();
				$('#uploadTaskClose').show(); //删除显示

				_this.result = res.reslut;
				_this.shipper = res.reslut.shipper;
			})
		},
		addImage: function() {
			var _this = this;

			if($(".showTaskList").is(":hidden")) {
				$.toast('请先选择任务单', 'text');
			} else if(!localStorage.getItem('clickTag')) {
				//第一次点击
				$('.upload,.demos-content-padded').hide();
				$('.img_tips').show();
			} else {
				chooseImage(images.localId);
			}
		},
		upload: function() {
			var _this = this;
			//获得任务单id
			var wayBillId = parseInt($('#chooseId').attr('uid'));

			images.serverId = [];
			var len = images.localId.length;
			if(len == 0) { //若是没有上传图片这个步骤，直接点确定上传，是没有反应的，wx.uploadImage这个方法是不执行的
				$.toast('请上传回单照片', 'text');
			} else {
				_this.uploadImage(0, len, wayBillId);
			}
		},
		uploadImage: function(i, len, wayBillId) {
			var _this = this;
			setTimeout(function() {
				wx.uploadImage({
					localId: images.localId[i], // 需要上传的图片的本地ID，由chooseImage接口获得
					success: function(res) {
						images.serverId.push(res.serverId);
						if(++i < len) {
							uploadImage(i);
						} else {
							var serverIds = images.serverId.join(',');
							var baseValue = {
								"serverId": serverIds,
								"orderKey": wayBillId
							}
							EasyAjax.ajax_Post_Json({
								url: '/mobile/wayBill/uploadReceipt',
								data: JSON.stringify(baseValue)
							}, function(res) {
								$.toast(res.message, function() {
									window.location.replace("./uploadReceipt.html");
								});
							});
						}
					}
				});
			}, 100);
		},
		understandRequirement: function() {
			var _this = this;
			$('.upload,.demos-content-padded').show();
			$('.img_tips').hide();
			localStorage.setItem("clickTag", "1");
			_this.addImage();
		},
		reselection: function() {
			//显示二维码扫码接口
			$('.upload-task').removeClass('cancelPadding');
			$('#upload-task-title').show();
			$('#show-task-list').hide();
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
var ordrId = _common.getUrlParam('id');
var valiCode = _common.getUrlParam('code');
var name = _common.getUrlParam('name');
var phone = _common.getUrlParam('phone');

var codeDetail = new Vue({
	el: '#signatureTwo',
	data: {
		listResult: null,
		pathPrefix: "",
		personals: [],
		enterprises: [],
		sealId: "",
		personalSeal: ""
	},
	created: function() {
		this.getListData();
	},
	mounted: function() {

	},
	methods: {
		getListData: function() {
			var _this = this;
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/order/userCertified/search'
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					_this.listResult = res.reslut;
					if(res.reslut) {
						_this.personals = res.reslut.personals;
						_this.enterprises = res.reslut.enterprises;
						_this.pathPrefix = res.pathPrefix;
					}
				}
			});
		},
		choosePersonSignature: function(id, clickPosition) {
			if(clickPosition == 0) {
				$($(".personSignatureChoosedPic")[0]).css("display", "block");
				$($(".personSignatureChoosedPic")[1]).css("display", "none");
				$($(".personSignatureChoosedPic")[2]).css("display", "none");
			} else if(clickPosition == 1) {
				$($(".personSignatureChoosedPic")[0]).css("display", "none");
				$($(".personSignatureChoosedPic")[1]).css("display", "block");
				$($(".personSignatureChoosedPic")[2]).css("display", "none");
			} else {
				$($(".personSignatureChoosedPic")[0]).css("display", "none");
				$($(".personSignatureChoosedPic")[1]).css("display", "none");
				$($(".personSignatureChoosedPic")[2]).css("display", "block");
			}
			this.personalSeal = id;
		},
		chooseCompanySignature: function(id, clickPosition) {
			if(clickPosition == 0) {
				$($(".companySignatureChoosedPic")[0]).css("display", "block");
				$($(".companySignatureChoosedPic")[1]).css("display", "none");
				$($(".companySignatureChoosedPic")[2]).css("display", "none");
			} else if(clickPosition == 1) {
				$($(".companySignatureChoosedPic")[0]).css("display", "none");
				$($(".companySignatureChoosedPic")[1]).css("display", "block");
				$($(".companySignatureChoosedPic")[2]).css("display", "none");
			} else {
				$($(".companySignatureChoosedPic")[0]).css("display", "none");
				$($(".companySignatureChoosedPic")[1]).css("display", "none");
				$($(".companySignatureChoosedPic")[2]).css("display", "block");
			}
			this.sealId = id;
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		},
		submitInfo: function() {
			if(this.isNull(this.personalSeal)) {
				$.toptip('请先选择使用的个人章');
				return;
			}

			if(this.isNull(this.sealId)) {
				$.toptip('请先选择使用的企业章');
				return;
			}

			var baseValue = {
				orderId: _common.getUrlParam('id'),
				sealId: this.sealId,
				personalSeal: this.personalSeal,
				code: _common.getUrlParam('code')
			}
			EasyAjax.ajax_Post_Json({
				url: 'enterprise/sign/signature/single',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					$.toast("签署成功", function() {
						window.location.href = _common.version('./electronicReceipt.html?id=' + ordrId);
					});
				}
			}, function(error) {
				var code = error.code;
				if(code == "SMS_CODE_ERROR") {
					window.location.href = _common.version('./signOne.html?id=' + ordrId + '&name=' + name + '&phone=' + phone);
				}
			});
		}
	}
});
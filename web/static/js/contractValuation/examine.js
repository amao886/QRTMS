const vm = new Vue({
	el: '#content',
	data: {
		contractKey: _common.getUrlParam('contractKey'),
		page: [{
			pageNum: 1, //页数
			pageSize: 10, //每一页数量
			total: 0, //总数量
			currentPage: 1,
		}],
		fileList: [],
		currentPage: 1, //当前页
		customerList: [], //客户名称列表
		//基本信息
		baseInformation: {
			contractType: '', //合同类型
			contractNo: '', //合同编号
			contractStartDate: '', //合同开始日期
			contractEndDate: '', //合同结束日期
			customerName: '', //客户名称
			customerCode: '', //客户编码
			industryType: '', //行业类别
			freightMode: 1, //运费方式：1 零担运输 2：整车运输
			list: [], //计算方法数据
		},
		//客户信息
		customer: {
			contacts: '', //联系人姓名
			landlineNumber: '', //座机号码
			phone: '', //手机号码
			province: '', //省
			city: '', //市
			district: '', //区/县
			fullAddress: '', //详细地址
		},
		tableDataList: [
			//{
			//			goodsNature: '0', //货物性质
			//			goodsCalculMode: '1', //计价方式
			//			goodsUnit: '0', //重量单位
			//			useStartDate: '', //开始使用日期
			//			useEndDate: '', //结束使用日期
			//			//区间数据
			//			intervals: [{
			//				startNum: 0,
			//				endNum: ''
			//			}],

			//	}
		],
		//审核数据
		auditResultList: [],
		//区间数组
		weightIntervalList: [{
			weightInterval: ['重量区间']
		}],
		checked: true, //零担运输,
		auditFailReason: '', //审核不通过原因
		dialogVisible: false,
	},
	mounted: function() {
		var _this = this;
		_this.render()
	},
	methods: {
		handleClose: function(done) {
			//      this.$confirm('确认关闭？')
			done();

		},
		//每页数量改变
		handleSizeChange: function(val, index) {
			console.log('每页 ' + val + '条');
			var _this = this;
			_this.page[index].pageSize = val;
		},
		// 当前页改变时触发事件
		handleCurrentChange: function(val, index) {
			console.log('当前页:' + val);
			var _this = this;
			console.log(_this.page[index].currentPage)
			_this.page[index].currentPage = val;
		},

		//请求详情页面数据
		render: function() {
			var _this = this;
			_this.page = []
			_this.weightIntervalList = []
			EasyAjax.ajax_Post_Json({
				url: '/contract/get/' + _this.contractKey
			}, function(res) {
				console.log(res);
				if(res.success) {
					_this.baseInformation.contractNo = res.result.contractNo;
					_this.baseInformation.contractStartDate = res.result.contractStartDate;
					_this.baseInformation.contractEndDate = res.result.contractEndDate;
					_this.baseInformation.customerName = res.result.contactName;
					_this.baseInformation.customerCode = res.result.customerCode;
					_this.baseInformation.industryType = res.result.industryType;
					_this.customer.landlineNumber = res.result.telephoneNumber
					_this.customer.contacts = res.result.contactName;
					_this.customer.phone = res.result.mobileNumber;
					_this.customer.province = res.result.customerProvince;
					_this.customer.city = res.result.customerCity;
					_this.customer.district = res.result.customerDistrict;
					_this.customer.fullAddress = res.result.customerFullAddress;
					_this.baseInformation.customerCode = res.result.customerCode;
					_this.baseInformation.customerName = res.result.customerName;
					_this.auditResultList = res.result.auditResultList;
					console.log(_this.auditResultList)
					for(var i = 0; i < res.result.list.length; i++) {
						_this.page.push({
								pageNum: 1, //页数
								pageSize: 10, //每一页数量
								total: res.result.list[i].freightList.length, //总数量
								currentPage: 1,
							}),
							_this.weightIntervalList.push({
								weightInterval: []
							})
						res.result.list[i].goodsUnit = res.result.list[i].goodsUnit + '';
						res.result.list[i].goodsNature = res.result.list[i].goodsNature + '';
						res.result.list[i].goodsCalculMode = res.result.list[i].goodsCalculMode + '';
						for(var k = 0; k < res.result.list[i].freightList.length; k++) {
							res.result.list[i].freightList[k].cityLevel = res.result.list[i].freightList[k].cityLevel + '';
						}
						for(var j = 0; j < res.result.list[i].intervals.length; j++) {
							if(res.result.list[i].goodsCalculMode == 1) {
								_this.weightIntervalList[i].weightInterval.push('重量区间')
							} else if(res.result.list[i].goodsCalculMode == 2) {
								_this.weightIntervalList[i].weightInterval.push('体积区间')
							} else {
								_this.weightIntervalList[i].weightInterval.push('数量区间')
							}
						}
					}
					_this.tableDataList = res.result.list;
				}
			})

		},

		//审核不通过
		auditFail: function() {
			var _this = this;
			_this.dialogVisible = true;
		},
		//审核结果提交
		auditSub: function(type) {
			//type  1 审核不通过 2 审核通过
			var _this = this;
			var parmas ={
				verifyDto:{
					verifyStatus: type,
				    reason: _this.auditFailReason,
				}
			}
			if(type == 1) {
				if(!_this.auditFailReason) {
					_common.toast(3, '请填写审核不通过原因')
				}
			}
						EasyAjax.ajax_Post_Json({
							url: 'contract/verify/'+_this.contractKey,
							data: parmas
						}, function(res) {
							console.log(res);
							if(res.success) {
			                   _this.dialogVisible = false;
								window.location.href = '../../../views/contractValuationManagement/manageList.html'
							} else {
								_common.toast(3, res.message)
							}
						})
		},

	},

})
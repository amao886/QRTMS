const vm = new Vue({
	el: '#siteManageList',
	data: {
		form: {
			likeString: '', //送货单号/收货客户/物流商
			deliveryDate: '', //要求提货日期
			arrivalStatus: '', //到车状态
			remarks: '', //备注
		},
		pageNum: 1, //页数
		pageSize: 10, //每一页数量
		total: 0, //总数量
		list: [], //列表数据
		dialogVisible: false,
		radio: '',
		isShow: {
			one: false,
			two: false
		},
		driverInformation: {
			name: '', //司机姓名
			license:'', //车牌号
			phone:'', //司机电话
		},
		arrivalType: '', //到达类型
		dispatchingInformation: {}, //派车信息
		deliveryNo:'',//送货单号
	},
	created: function() {
		var _this = this;
		_this.render();
	},
	methods: {
		//			//复选框变化时
		//			handleSelectChange: function(selects) {
		//				console.log(selects)
		//			},
		//当前页发生改变
		currentChange: function(val) {
			console.log(val)
			var _this = this;
			_this.pageNum = val
			_this.render();
		},
		//弹窗关闭
		handleClose: function(done) {
			done();

		},
		toggle: function(val) {
			var _this = this;
			_this.driverInformation.license = '';
			_this.driverInformation.name = ''
			_this.driverInformation.phone = ''
			_this.arrivalType = val
			if(val == 1) {
				_this.isShow.one = false;
				_this.isShow.two = false;
			} else if(val == 2) {
				_this.isShow.one = true;
				_this.isShow.two = false;
			} else {
				_this.isShow.one = false;
				_this.isShow.two = true;
			}
			_this.subFlag = val;
		},
		//筛选
		search: function() {
			var _this = this;
			_this.render()
		},
		//列表渲染方法
		render: function() {
			var _this = this;
			var parmas = {
				status: _this.form.arrivalStatus,
				likeString: _this.form.likeString,
				pickTime: _this.form.deliveryDate,
				num: _this.pageNum,
				size: _this.pageSize,
				type: _this.form.remarks
			}
			EasyAjax.ajax_Post_Json({
				url: 'scene/search/list',
				data: parmas
			}, function(res) {
				console.log(res);
				if(res.success) {
					_this.list = res.page.results;
					_this.total = res.page.total;
				}

			})
		},
		//到车登记
		vehicles: function(deliveryNo) {
			var _this = this;
		   if(deliveryNo){
		   	_this.deliveryNo = deliveryNo
		   	_this.dialogVisible = true;
			_this.dispatchingInformation ={};
		   		EasyAjax.ajax_Post_Json({
				url: 'scene/queryDeliveryDetails/' + deliveryNo,
			}, function(res) {
				console.log(res);
				if(res.success) {
					if(res.result.deliveryCarDto!= null) {
						_this.dispatchingInformation = res.result.deliveryCarDto;
					}
					_this.dispatchingInformation.orderKey = res.result.orderKey
				}
			})
		   } else {
		   	_this.$message({
					showClose: true,
					message: '送货单号为空，不能操作',
					type: 'error'
				})
		   }
		},
		//取消
		cancel: function() {
			var _this = this;
			_this.dialogVisible = false
			_this.driverInformation.license = '';
			_this.driverInformation.name = ''
			_this.driverInformation.phone = ''
			_this.radio = '';
		},
		//到车登记提交
		vehiclesSubmit: function() {
			var _this = this;
			var parmas = {
				orderKey: _this.dispatchingInformation.orderKey,
				arrivalsCarDto:{
					inDriverName: _this.driverInformation.name,
					inDriverContact: _this.driverInformation.phone,
					inLicense: _this.driverInformation.license,
					arrivalType: _this.arrivalType
				},
				deliveryCarDto: {
					deliveryNo: _this.deliveryNo, // 配送单号
					driverName: _this.dispatchingInformation.driverName, //司机名称
					driverContact: _this.dispatchingInformation.driverContact, // 司机电话
					license: _this.dispatchingInformation.careNo, //车牌号
				}
			}
			 if(!parmas.arrivalsCarDto.arrivalType){
			 		_this.$message({
					showClose: true,
					message: '请选择备注类型',
					type: 'error'
				})
				return false;
			 }
			if(parmas.arrivalsCarDto.arrivalType != 1){
					if(_this.isNull(parmas.arrivalsCarDto.inLicense)) {
				_this.$message({
					showClose: true,
					message: '车牌号不能为空',
					type: 'error'
				})
				return false;
			}
			if(_this.isNull(parmas.arrivalsCarDto.inDriverName)) {
				_this.$message({
					showClose: true,
					message: '司机姓名不能为空',
					type: 'error'
				})
				return false;
			}
			if(_this.isNull(parmas.arrivalsCarDto.inDriverContact)) {
				_this.$message({
					showClose: true,
					message: '手机号码不能为空',
					type: 'error'
				})
				return false;
			}
			if(!/1\d{10}$/.test(parmas.arrivalsCarDto.inDriverContact)) {
				_this.$message({
					showClose: true,
					message: '请输入正确的手机号码',
					type: 'error'
				})
				return false;
			}
			}
			console.log(parmas)
			EasyAjax.ajax_Post_Json({
				url: 'scene/vehicle/save',
				data: parmas
			}, function(res) {
				console.log(res);
				if(res.success) {
					_this.$message({
						showClose: true,
						message: res.message,
						type: 'success'
					})
					_this.dialogVisible = false;
					_this.driverInformation.license = '';
					_this.driverInformation.name = ''
					_this.driverInformation.phone = ''
					
					_this.render()
				}

			})
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	}
})
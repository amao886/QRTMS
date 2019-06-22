const vm = new Vue({
	el: '#content',
	data: {
		page: [{
			pageNum: 1, //页数
			pageSize: 10, //每一页数量
			total: 0, //总数量
			currentPage: 1,
		}],
		flag: false,
		fileList: [],
		currentPage: 1, //当前页
		customerList: [], //客户名称列表
		//基本信息
		baseInformation: {
			contractType: _common.getUrlParam('contractType'), //合同类型
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
		tableDataList: [{
			goodsNature: '0', //货物性质
			goodsCalculMode: '1', //计价方式
			goodsUnit: '0', //重量单位
			useStartDate: '', //开始使用日期
			useEndDate: '', //结束使用日期
			freightList: [],
			//区间数据
			intervals: [{
				startNum: 0,
				endNum: ''
			}],

		}],

		//区间数组
		weightIntervalList: [{
			weightInterval: ['重量区间']
		}],
		provinces: [], //省
		citys: [], //市
		countys: [], //区县
		province: '',
		city: '',
		county: '',
		checked: true, //零担运输,
		dialogVisible: false

	},
	mounted: function() {
		var _this = this;
		_this.customersList()
	},
	methods: {
		//将单价转换成数值型
		conversionType: function(v, index, i) {
			var _this = this;
			_this.tableDataList[v].freightList[index].num[i] = parseInt(_this.tableDataList[v].freightList[index].num[i])

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
		//客户名称列表
		customersList: function() {
			var _this = this;
			//			console.log(_this.contractType)
			if(_this.baseInformation.contractType == '1') {
				var parmas = {
					type: 1,
					reg:1
				}
			} else {
				var parmas = {
					type: 3,
					reg:1
				}
			}
			EasyAjax.ajax_Post_Json({
				url: '/enterprise/customer/list',
				data: parmas
			}, function(res) {
				console.log(res);
				if(res.success) {
					_this.customerList = res.customers
				}
			})
		},
		//客户详情
		customerDetail: function() {
			var _this = this;
			          _this.customer.contacts = ''
						_this.customer.phone = ''
						_this.customer.province = ''
						_this.customer.city = ''
						_this.customer.district = ''
						_this.customer.fullAddress =''
						_this.baseInformation.customerCode = ''
			console.log(_this.baseInformation.customerName)
			    for(var i=0;i<_this.customerList.length;i++){
			    	   if(_this.customerList[i].name==_this.baseInformation.customerName){
			    	   	_this.baseInformation.customerCode = _this.customerList[i].key
			    	   }
			    }
			EasyAjax.ajax_Post_Json({
				url: '/enterprise/customer/address/list',
				data: {
					customerId: _this.baseInformation.customerName
				}

			}, function(res) {
				console.log(res);
				if(res.success) {
					if(res.addresss.length > 0) {
						_this.customer.contacts = res.addresss[0].contacts
						_this.customer.phone = res.addresss[0].contactNumber
						_this.customer.province = res.addresss[0].province
						_this.customer.city = res.addresss[0].city
						_this.customer.district = res.addresss[0].district
						_this.customer.fullAddress = res.addresss[0].address
						_this.baseInformation.customerCode = res.addresss[0].customerId;
						_this.baseInformation.customerName = res.addresss[0].customerName
					}
				}
			})
		},
		//下载模板 
		downLoad: function() {
			var str = api_host + 'special/download/101'
			_common.download(str)

		},
		//导入数据
		fileUpload: function(index, e) {
			var _this = this;
			var files = e.target.files;
			var fileReader = new FileReader()
			fileReader.onload = function(ev) {
				try {
					var data = ev.target.result,
						workbook = XLSX.read(data, {
							type: 'binary'
						}), // 以二进制流方式读取得到整份excel表格对象
						persons = []; // 存储获取到的数据
				} catch(e) {
					console.log('文件类型不正确');
					return;
				}
				// 表格的表格范围，可用于判断表头是否数量是否正确
				var fromTo = '';
				// 遍历每张表读取
				for(var sheet in workbook.Sheets) {
					if(workbook.Sheets.hasOwnProperty(sheet)) {
						fromTo = workbook.Sheets[sheet]['!ref'];
						persons = persons.concat(XLSX.utils.sheet_to_json(workbook.Sheets[sheet]));
						break; // 如果只取第一张表，就取消注释这行
					}
				}
				console.log(persons);
				_this.page[index].total = persons.length - 2
				//获取区间列数
				//获取区间列数
				_this.weightIntervalList[index].weightInterval = [];
				_this.tableDataList[index].intervals = [];

				for(var key in persons[0]) {
					if(_this.tableDataList[index].goodsCalculMode == 1) {
						_this.weightIntervalList[index].weightInterval.push('重量区间')
					} else if(_this.tableDataList[index].goodsCalculMode == 2) {
						_this.weightIntervalList[index].weightInterval.push('体积区间')
					} else {
						_this.weightIntervalList[index].weightInterval.push('数量区间')
					}
					_this.tableDataList[index].intervals.push({
						startNum: persons[0][key],
						endNum: ''
					})
				}

				//给表头每个区间赋值
				for(var i = 0; i < _this.weightIntervalList[index].weightInterval.length; i++) {
					var val = _this.weightIntervalList[index].weightInterval[i].substring(2, _this.weightIntervalList[index].weightInterval[i].length)
					if(i == 0) {
						_this.tableDataList[index].intervals[i].endNum = persons[1][val]
					} else {
						_this.tableDataList[index].intervals[i].endNum = persons[1][(val + "_" + i)]
					}

				}
				_this.tableDataList[index].freightList = []
				//  遍历数组，给每一行的数据赋值
				for(var i = 2; i < persons.length; i++) {
					var str = persons[i].终点城市
//					console.log(str)
					var arr = []
					for(j = 0; j < _this.weightIntervalList[index].weightInterval.length; j++) {
						var val = _this.weightIntervalList[index].weightInterval[j].substring(2, _this.weightIntervalList[index].weightInterval[j].length)  
					if(j == 0) {
						val = val
					} else {
						val = val + "_" + j
					}
						arr.push(parseInt(persons[i][val]))
					}
//					console.log(arr)
//					var pattern = /([^省]+自治区|上海[市]?|天津[市]?|重庆[市]?|北京[市]?|.*?省|.*?行政区|河北|山西|内蒙古|辽宁|吉林|黑龙江|江苏|浙江|安徽|福建|江西|山东|河南|湖北|湖南|广东|广西|广西壮族|海南|四川|贵州|云南|西藏|陕西|甘肃|青海|宁夏|宁夏回族|新疆)([^市]+自治州|.*?地区|.*?行政单位|.*?行政区划|.+盟|市辖区|.*?市|.*?县|.+?区)([^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?([^区]+?镇|.+区)?(.*)?/
					var areas = str.split('-')
//					console.log(areas)
					if(areas) {
						_this.tableDataList[index].freightList.push({
							area: {
								province: areas[0],
								city: areas[1],
								county: areas[2]
							},
							num: arr,
							cityLevel: persons[i].城市级别, //城市级别
						})
					}
				}
//				console.log(_this.tableDataList[index].freightList)
//				console.log(_this.tableDataList[index].intervals)
//				console.log(_this.weightIntervalList[index].weightInterval)
			};
			// 以二进制方式打开文件
			fileReader.readAsBinaryString(files[0]);
			EasyAjax.ajax_Post_Json({
				url: '/special/support/area',
				data: {
					parentid: 0
				}
			}, function(res) {
				console.log(res);
				if(res.success) {
					_this.provinces = res.result;
				}
			})

		},
		//添加列区间
		addColumn: function(index) {
			var _this = this;
			if(_this.tableDataList[index].goodsCalculMode == 1) {

				_this.weightIntervalList[index].weightInterval.push('重量区间')
			} else if(_this.tableDataList[index].goodsCalculMode == 2) {
				_this.weightIntervalList[index].weightInterval = ['体积区间']
				_this.tableDataList[index].weightInterval.push('体积区间')
			} else {
				_this.weightIntervalList[index].weightInterval = ['数量区间']
				_this.tableDataList[index].weightInterval.push('数量区间')
			}
			console.log(_this.weightIntervalList[index].weightInterval)
			//						_this.tableDataList[index].tableData[0].num.push(0);
			var val = _this.tableDataList[index].intervals[_this.tableDataList[index].intervals.length - 1]
			val = val.endNum
			_this.tableDataList[index].intervals.push({
				startNum: val,
				endNum: ''
			})

		},
		//添加城市
		addCity: function(index) {
			var _this = this;
			_this.dialogVisible = true;
			_this.province = ''
			_this.city = '';
			_this.county = '';
           _this.provinces = [];
           _this.citys = [];
           _this.countys = [];
			EasyAjax.ajax_Post_Json({
				url: '/special/support/area',
				data: {
					parentid: 0
				}
			}, function(res) {
				console.log(res);
				if(res.success) {
					_this.provinces = res.result;
				}
			})
			console.log(index)

		},
		//删除行
		deleteRow: function(index, rows) {
			rows.splice(index, 1);
			this.page[index].total = this.tableDataList[index].freightList.length
			console.log(this.freightList)
		},
		//删除列
		deleteColumn: function(index) {
			var _this = this;
			if(_this.weightIntervalList[index].weightInterval.length > 1) {
				_this.weightIntervalList[index].weightInterval.pop();
				_this.tableDataList[index].intervals.pop()
			} else {
				toast(1, '至少保留一列区间')
			}

		},
		//添加计算方法
		addMethods: function() {
			var _this = this;
			_this.tableDataList.push({
				goodsNature: '0', //货物性质
				goodsCalculMode: '1', //计价方式
				goodsUnit: '0', //重量单位
				startUseDate: '', //开始使用日期
				endUseDate: '', //结束使用日期
				freightList: [],
				//重量区间
				intervals: [{
					startNum: 0,
					endNum: ''
				}],
			})
			_this.page.push({
				pageNum: 1, //页数
				pageSize: 10, //每一页数量
				total: 0, //总数量
			})
			_this.weightIntervalList.push({
				weightInterval: ['重量区间']
			})
		},
		//计价方式切换
		toggle: function(index) {
			var _this = this;
			if(_this.tableDataList[index].goodsCalculMode == 1) {
				_this.weightIntervalList[index].weightInterval = ['重量区间']
				_this.tableDataList[index].goodsUnit = '0';
			} else if(_this.tableDataList[index].goodsCalculMode == 2) {
				_this.weightIntervalList[index].weightInterval = ['体积区间']
				_this.tableDataList[index].goodsUnit = '2';
			} else if(_this.tableDataList[index].goodsCalculMode == 3) {
				_this.weightIntervalList[index].weightInterval = ['数量 区间']
				_this.tableDataList[index].goodsUnit = '3';
			}
			console.log(_this.tableDataList[index].goodsCalculMode)
			console.log(index)
		},

		//选择城市
		selectAddress: function(index) {
			var _this = this;
			var list = {
				area: {
					county: _this.county,
					city: _this.city,
					province: _this.province
				},
				num: [0],
				cityLevel: '0', //城市级别
			}
			console.log(list)
			if(list.area.county == '' || list.area.city == '' || list.area.province == '') {
				_common.toast(3, '请选择完整的省市区')
				return false
			}
			console.log(_this.tableDataList[index].freightList)
			_this.tableDataList[index].freightList.push(list)
			_this.page[index].total = _this.tableDataList[index].freightList.length;
			_this.dialogVisible = false
			_this.page[index].currentPage = Math.ceil(_this.page[index].total / _this.page[index].pageSize)
			//			console.log(Math.ceil( _this.page[index].total/_this.page[index].pageSize))        		
		},
		//新增合同保存
		saveContract: function() {
			var _this = this;
			var parmas = {
				contractType: _this.baseInformation.contractType,
				contractNo: _this.baseInformation.contractNo,
				contractStartDate: _this.baseInformation.contractStartDate,
				contractEndDate: _this.baseInformation.contractEndDate,
				customerCode: _this.baseInformation.customerCode,
				customerName: _this.baseInformation.customerName,
				industryType: _this.baseInformation.industryType,
				list: _this.tableDataList,
				contactName: _this.customer.contacts,
				telephoneNumber: _this.customer.landlineNumber,
				mobileNumber: _this.customer.phone,
				customerProvince: _this.customer.province,
				customerCity: _this.customer.city,
				customerDistrict: _this.customer.district,
				customerAddress: _this.customer.fullAddress
			}
			
			if(parmas.contractNo == '' || parmas.contractNo == null) {
				_common.toast(3, '合同编号不能为空')
				return false
			}
			var reg = /[^\u4e00-\u9fa5]+/
			if(!reg.test(parmas.contractNo)) {
				_common.toast(3, '合同编号为数字,字母或者特殊符号')
				return false
			}
			if(parmas.customerName == '' || parmas.customerName == null) {
				_common.toast(3, '客户名称不能为空,请先选择客户')
				return false
			}
			if(parmas.contractStartDate == '' || parmas.contractStartDate == null) {
				_common.toast(3, '合同开始日期不能为空')
				return false
			}
			if(parmas.contractEndDate == '' || parmas.contractEndDate == null) {
				_common.toast(3, '合同结束日期不能为空')
				return false
			}
			var startTime = new Date(Date.parse(parmas.contractStartDate));
			var endTime = new Date(Date.parse(parmas.contractEndDate))
			if(endTime < startTime) {
				_common.toast(3, '合同结束日期不能小于合同开始日期')
				return false
			}
			for(var i = 0; i < parmas.list.length; i++) {

				if(parmas.list[i].useStartDate == '' || parmas.list[i].useStartDate == null) {
					_common.toast(3, '开始使用日期不能为空')
					return false
				}
				if(parmas.list[i].useEndDate == '' || parmas.list[i].useEndDate == null) {
					_common.toast(3, '结束使用日期不能为空')
					return false
				}
				var useStartTime = new Date(Date.parse(parmas.list[i].useStartDate));
				var useEndTime = new Date(Date.parse(parmas.list[i].useEndDate))
				if(useEndTime < useStartTime) {
					_common.toast(3, '结束使用日期不能小于开始使用日期')
					return false
				}
			}
			console.log(parmas)
			EasyAjax.ajax_Post_Json({
				url: 'contract/save',
				data: parmas
			}, function(res) {
				console.log(res);
				if(res.success) {
					window.location.href = '../../../views/contractValuation/contractValuationManagement/manageList.html'
				}
			})
		},
		selectChange: function(type) {
			//type: 1-省份 2-城市 3-区县
			var _this = this;
			if(type == 1) {
				if(_this.province=='不限(省)'){
					return false ;
				}
				_this.loadCity(function() {});
			} else if(type == 2) {
				if(_this.city=='不限(市)'){
					return false ;
				}
				console.log(_this.city)
				_this.loadCounty(function() {});
			} else if(type == 3) {
				if(_this.county=='不限(区/县)'){
					return false ;
				}
				_this.county = _this.countys[_this.county].name
			}
		},
		//加载城市
		loadCity: function(callback) {
			const _this = this;
			var p = _this.provinces[_this.province]
			console.log(p)
			_this.province = p.name
			EasyAjax.ajax_Post_Json({
				url: '/special/support/area',
				data: {
					parentid: p.id
				}
			}, function(res) {
				console.log(res);
				if(res.success) {
					_this.citys = res.result;
				}
			})
		},
		//加载区县
		loadCounty: function(callback) {
			const _this = this;
			var c = _this.citys[_this.city]
			_this.city = c.name
			
			EasyAjax.ajax_Post_Json({
				url: '/special/support/area',
				data: {
					parentid: c.id
				}
			}, function(res) {
				console.log(res);
				if(res.success) {
					_this.countys = res.result;
					if(callback) {
						callback.call(_this);
					}
				}
			})

		}
	},

})
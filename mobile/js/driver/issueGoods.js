
const  vm = new Vue({
	el:'#issueGoods',
	data:{
		areaVersion: 0,
	    driver:{
			driverKey: '', //司机id
			name: '', //司机姓名
			phone: '' ,//手机号
			routes: [{
				type: '', //路线类型
				starts: '', //路线起点
				ends: '', //路线终点
			}],
			cars: [{
				type: '', //车型
				length: '', //车长
				loadValue: '', //载重
				license: '', //车牌号
			}],	
		},
		flag:true
	},
	created:function(){
	var _this = this;
	if(window.sessionStorage) {
		_this.areaVersion = sessionStorage.getItem('areaVersion') | 0 ;
	}
	_this.getAreasData(_this.areaVersion);
		EasyAjax.ajax_Post_Json({
			url:'driver/personal/info',
		}, function(res) {
			console.log(res)
			_this.driver=res.driver;
			if(res.driver.routes[0].starts!=null){
				$("#waitingAddress").val(res.driver.routes[0].starts.join(' '))
			}
			if(res.driver.cars[0].license==null){
				$.toast("司机信息不完善，请先去完善司机资料", "text");
				_this.flag=false;
			} else {
				_this.flag=true;
			}
		})
	},
	methods:{
		getAreasData: function(version) {
			var _this = this;

			var baseValue = {
				version: version
			}
			EasyAjax.ajax_Post_Json({
				url: 'special/support/area/all',
				data: JSON.stringify(baseValue)
			}, function(res) {
				if(!res.success) {
					$.toast("加载失败，请稍后重试", "text");
				} else {
					console.log(res)
					
					if(window.sessionStorage) {
						if(_this.areaVersion < res.version) {
							sessionStorage.removeItem('areaData');
							sessionStorage.setItem('areaVersion', res.version);
							sessionStorage.setItem('areaData', JSON.stringify(res.result));
							loadData(res.result);
						} else {
							sessionStorage.setItem('areaVersion', res.version);
							var areaData = JSON.parse(sessionStorage.getItem('areaData'));
							if(areaData) {
								loadData(areaData);
							}
						}
					} else {
						loadData(res.result);
					}
					
					setTimeout(function(){
						$("#waitingAddress").cityPicker({
						title: "请选择发货地址省市区/县"
					});
					$("#waitingDate").mobiscroll($.extend(opt['date'], opt['default']));
					},200)
					
				}
			});
		},
		//发布
		release:function(){
			var _this = this;
			if(!$("#waitingAddress").val()){
				$.toast("等货地点不能为空", "text");
				return false;
			}
		 var parmas = {
		 	startTime:$("#waitingDate").val(),//等货时间
		 	routeType:_this.driver.routes[0].type,
		 	carType:_this.driver.cars[0].type,
		 	length:_this.driver.cars[0].length,
		 	license:_this.driver.cars[0].license,
		 	loadValue:_this.driver.cars[0].loadValue
		 }
		 if(_this.driver.routes[0].starts==null){
		 	parmas.start = null;
		 }else if(_this.driver.routes[0].starts.join(',')==$("#waitingAddress").val().split(' ').join(',')){
		 	parmas.start = _this.driver.routes[0].starts.join()
		 }else {
		 	parmas.start = $("#waitingAddress").val().split(' ').join(',')
		 }
		  if(_this.driver.routes[0].ends==null){
		 	parmas.end = null;
		 } else {
		 	parmas.end = _this.driver.routes[0].starts.join()
		 }
		 console.log(parmas)
		 console.log($("#waitingAddress").val().split(' '))
		 	if(_this.flag){
		 		EasyAjax.ajax_Post_Json({
				url: 'driver/personal/releaseWaitInfo',
				data: JSON.stringify(parmas)
			}, function(res) {
				console.log(res)
				if(res.success){
					window.location.href = '../../view/driver/lookingGoods.html'
				}
			});
		 	} else {
		 		$.toast("司机信息不完善，请先去完善司机资料", "text");
		 	}
		},
	}
})
//初始化日期插件
var currYear = (new Date()).getFullYear();
var opt = {};
opt.date = {
	preset: 'date'
};
opt.datetime = {
	preset: 'datetime'
};
opt.time = {
	preset: 'time'
};
opt.default = {
	theme: 'android-ics light', //皮肤样式
	display: 'modal', //显示方式 
	mode: 'scroller', //日期选择模式
	dateFormat: 'yy-mm-dd',
	lang: 'zh',
	showNow: true,
	nowText: "今天",
	startYear: currYear - 10, //开始年份
	endYear: currYear + 10 //结束年份
};
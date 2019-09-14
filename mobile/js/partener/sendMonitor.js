var monitor = {
	monitorObj: new Vue({
		el: '#monitorWrapper',
		data:{
			dataList: [],
			dateVal: ''
		},
		computed:{},
		mounted: function(){
			this.$nextTick(function () {
			    this.dateVal = this.getTodayDate();
			})
		},
		methods:{
			toMyTaskPage:function(groupId,sts){
				groupId = groupId == null ? 0 : groupId;
				var sendMon,bindSts,delaySts;
				
				switch(sts){
					case "20": //待发货
						bindSts = '20';
						delaySts = ''
						break;					
					case "30": //在途
						bindSts = '30';
						delaySts = ''
						break;					
					case "true": //延迟
						bindSts = '';
						delaySts = 'true';
						break;
				}
				sendMon = {
					groupid: groupId,
					bindStatus:bindSts,
					delayStatus:delaySts,
					moreTag: "-2",
					sendTime: this.dateVal
				}
				sessionStorage.setItem('monitorPageStore',JSON.stringify(sendMon));
				
				window.location.href="projectTeamTask.html?monitorParam=1";
				console.log('++++');
				console.log(JSON.parse(sessionStorage.getItem('monitorPageStore')));
				console.log('++++');
			},
			//获取当天日期
			getTodayDate: function(){
				var nowDate = getNowDate();
				return nowDate;
			},
			//跳到发货统计页
			toDailyTotalPage: function(){
				window.location.href = 'dailyTotal.html';
			}
		}
	})
}

$("#todayDate").calendar({
	onChange: function (p, values, displayValues) {
        console.log(values[0]);
        monitor.monitorObj.dateVal = values[0];
        getMonitorData(monitor.monitorObj.dateVal);
    }
});

getMonitorData(getNowDate());

//获取数据
function getMonitorData(time) {
	var baseValue = {
        selectTime: time,
    }
    EasyAjax.ajax_Post_Json({
        url: 'mobile/summary/waybill',
        data: JSON.stringify(baseValue)
    }, function (res) {
		monitor.monitorObj.dataList = res.summarys;
		console.log(monitor.monitorObj.dataList);
    });
}

//获取今天日期
function getNowDate() {
	var mDate = new Date(),
	m = (mDate.getMonth()+1) < 10 ? '0'+ (mDate.getMonth()+1) : (mDate.getMonth()+1),
	d = mDate.getDate() < 10 ? '0'+ mDate.getDate() : mDate.getDate();	
	var todayDate = mDate.getFullYear()+ '-' + m + '-' + d;

	return todayDate;
}

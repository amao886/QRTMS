<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>物流跟踪-每日统计-月统计图</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/monthchart.css?times=${times}" />
</head>
<body>
<div class="body-content">
	<div class="content-search">
		<div class="clearfix">
			<c:if test="${userType == 0}">
				<div class="fl col-middle">
					<label class="labe_l">项目组名:</label>
					<select class="tex_t selec_t" name="groupid" id="selectGroup">
						<c:forEach items="${groups}" var="group">
							<c:if test="${groupid == group.id }">
								<option value="${ group.id}" selected>${group.groupName }</option>
							</c:if>
							<c:if test="${groupid != group.id }">
								<option value="${ group.id}">${group.groupName }</option>
							</c:if>
						</c:forEach>
					</select>
				</div>
			</c:if>
			<div class="fl col-middle">
				<label class="labe_l">选择年份:</label>
				<select class="tex_t selec_t" name="selectYear">
					<c:forEach begin="${cyear - 3}" end="${cyear}" step="1" var="y">
						<c:if test="${cyear == y }">
							<option value="${y}" selected>${y}年</option>
						</c:if>
						<c:if test="${cyear != y }">
							<option value="${y}">${y}年</option>
						</c:if>
					</c:forEach>
				</select>
			</div>
			<div class="fl col-middle">
				<label class="labe_l">选择月份:</label>
				<select class="tex_t selec_t" name="selectMonth">
					<c:forEach begin="1" end="12" step="1" var="m">
						<c:if test="${cmonth == m }">
							<option value="${m}" selected>${m}月</option>
						</c:if>
						<c:if test="${cmonth != m }">
							<option value="${m}">${m}月</option>
						</c:if>
					</c:forEach>
				</select>
			</div>
            <div class="fl col-min">
                <a href="javascript:;" class="content-search-btn">查询</a>
            </div>
		</div>
	</div>
	<div class="echart-box">
		<div id="taskBox" style="width:100%;height:280px;"></div>
		<div id="rateBox" style="width:100%;height:280px;margin-top:20px;"></div>
	</div>
</div>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script src="${baseStatic}plugin/js/echarts.common.min.js"></script>
<script>
$(document).ready(function(){
	// 基于准备好的dom，初始化echarts实例
    var myChartTask = echarts.init(document.getElementById('taskBox'));
    var myChartRate = echarts.init(document.getElementById('rateBox'));
    
  	//任务数、发货率配置项
    var optionTask = {
    	    title: {
    	        text: '任务数统计'
    	    },
    	    tooltip: {
    	        trigger: 'axis',
    	        axisPointer: {
    	            label: {
    	                formatter: function (params) {
					    	    // 给x轴的日期后面添加一个单位"号"
					    	    return echarts.format.formatTime(params.value)+'号';
					    }
    	            }
    	        }
    	    },
    	    legend: {
    	        data:['发货任务数','应到任务数','已送达任务数','按时送达任务数']
    	    },
    	    grid: {
    	        left: '3%',
    	        right: '4%',
    	        bottom: '3%',
    	        containLabel: true
    	    },
    	    xAxis: {
    	        type: 'category',
    	        boundaryGap: true,
    	        data: []
    	    },
    	    yAxis: {
    	        type: 'value'
    	    },
    	    series: []
    	}
    var optionRate = {
    	    title: {
    	        text: '按时送达率'
    	    },
    	    tooltip: {
    	        trigger: 'axis',
    	        axisPointer: {
    	            label: {
    	                formatter: function (params) {
					    	    // 给x轴的日期后面添加一个单位"号"
					    	    return echarts.format.formatTime(params.value)+'号';
					    }
    	            }
    	        }
    	    },
    	    legend: {
    	        data:['按时送达率']
    	    },
    	    grid: {
    	        left: '3%',
    	        right: '4%',
    	        bottom: '3%',
    	        containLabel: true
    	    },
    	    xAxis: {
    	        type: 'category',
    	        boundaryGap: true,
    	        data: []
    	    },
    	    yAxis: {
    	        type: 'value'
    	    },
    	    series: [{
	            type:'line',
	            data: []
	        }]
    	}
      
    //显示图表
    myChartTask.setOption(optionTask);
    myChartRate.setOption(optionRate);
	
	$(".content-search-btn").on("click", function(){
		//获取查询参数
		var pamars = {};
		var xDate = [];
		$("select").each(function(){
			pamars[this.name] = this.value;
		});
		
		var xDateL = getLastDay(pamars.selectYear,pamars.selectMonth);
		for(var i=1;i <= xDateL;i++){
			xDate.push(i);
		}
		
		console.log('数组：'+xDate);
		console.log(JSON.stringify(pamars))
		//ajax异步请求数据加载图表
		var url = base_url+ "/backstage/dailyTotal/monthchart/search";
		$.util.json(url, pamars, function(data){        
			if(data.success){
				if(data.results.length == 0){
					$.util.success('暂无数据');
				}
				var result = data.results;
				var dateTask = [],
					allCount = [], //发货任务总数
					toCount = [], //应到任务数
					sendCount = [], //已送达任务数
					timeCount = [], //按时送达任务数
					sendRate = []; //按时送达率
				for(var i=0;i<result.length;i++){
					allCount.push(result[i].allCount);
					toCount.push(result[i].toCount);
					sendCount.push(result[i].sendCount);
					timeCount.push(result[i].timeCount);
					sendRate.push(toPoint(result[i].sendRate));
				}
			/* 	allCount.reverse();
				toCount.reverse();
				sendCount.reverse();
				timeCount.reverse();
				sendRate.reverse(); */
				
				dateTask.push({
		            name:'发货任务数',
		            type:'line',
		            data: allCount
		        },
		        {
		            name:'应到任务数',
		            type:'line',
		            data: toCount
		        },
		        {
		            name:'已送达任务数',
		            type:'line',
		            data: sendCount
		        },
		        {
		            name:'按时送达任务数',
		            type:'line',
		            data: timeCount
		        })
		        console.log('dateTask:'+JSON.stringify(dateTask));
				console.log('dateRate:'+JSON.stringify(sendRate));
		        
		        //更新数据
		        if(dateTask.length > 0){
		        	myChartTask.setOption({
				        xAxis: {
				            data: xDate
				        },
				        series: dateTask
				    });
				    myChartRate.setOption({
				        xAxis: {
				            data: xDate
				        },
				        series: {		                    
		                    name: '按时送达率', // 根据名字对应到相应的系列
		                    data: sendRate
		                }
				    });
		        }
				
				
			}else{
				$.util.error(data.message)  //失败提示
			}
		})
	});
	//页面加载完立马请求一次数据
	$(".content-search-btn").trigger('click');
	
	//根据年月计算月天数
    function getLastDay(myyear,mymonth){
        var new_date=new Date(myyear,mymonth,0);
        return new_date.getDate();
    }
	
	//将百分数转成小数
	function toPoint(percent){
	    var str=percent.replace("%","");
	    str= str/100;
	    return str;
	}
});
</script>
</html>
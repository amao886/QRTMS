/**
 * 时间控件
 */
 $(function(){
	 $(".startDate").click(function(){
		 var _index = $(".startDate").index($(this))
		 $(".startTxt").val(_index);
	 })
	 
	 $(".endDate").click(function(){
		 var _index = $(".endDate").index($(this));
		 $(".endTxt").val(_index);
	 })
	 
	 
            	//日期插件初始化
         		$('.startDate').datetimepicker({
         			language : 'zh-CN',
         			format : 'yyyy-mm-dd',
         			weekStart : 1, /*以星期一为一星期开始*/
         			todayBtn : 1,
         			autoclose : 1,
         			minView : 2, /*精确到天*/
         			pickerPosition : "bottom-left"
         		}).on(
         				"changeDate",
         				function(ev) { //值改变事件
         					//选择的日期不能大于第二个日期控件的日期
         					var i = $(".startTxt").val();   
         					if (ev.date) {
         						$("#datetimeEnd" + i).datetimepicker('setStartDate',
         								new Date(ev.date.valueOf()));
         					} else {
         						$("#datetimeEnd" + i).datetimepicker('setStartDate', null);
         					}
         		});
	 
         		$('.endDate').datetimepicker({
         			language : 'zh-CN',
         			format : 'yyyy-mm-dd',
         			weekStart : 1, /*以星期一为一星期开始*/
         			todayBtn : 1,
         			autoclose : 1,
         			minView : 2, /*精确到天*/
         			pickerPosition : "bottom-left"
         		}).on(
    				"changeDate",
    				function(ev) {
    					//选择的日期不能小于第一个日期控件的日期
    					var i = $(".endTxt").val();    					
    					if (ev.date) {
    						$("#datetimeStart" + i).datetimepicker('setEndDate',
    								new Date(ev.date.valueOf()));
    					} else {
    						$("#datetimeStart" + i).datetimepicker('setEndDate',
    								new Date());
    					}
    			});
 });

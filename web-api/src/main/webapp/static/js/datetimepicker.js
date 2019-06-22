/**
 * 时间控件
 */
 $(function(){
            	//日期插件初始化
         		$('#datetimeStart').datetimepicker({
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
         					if (ev.date) {
         						$("#datetimeEnd").datetimepicker('setStartDate',
         								new Date(ev.date.valueOf()));
         					} else {
         						$("#datetimeEnd").datetimepicker('setStartDate', null);
         					}
         				});
         		$('#datetimeEnd').datetimepicker({
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
    					if (ev.date) {
    						$("#datetimeStart").datetimepicker('setEndDate',
    								new Date(ev.date.valueOf()));
    					} else {
    						$("#datetimeStart").datetimepicker('setEndDate',
    								new Date());
    					}
    			});
 });

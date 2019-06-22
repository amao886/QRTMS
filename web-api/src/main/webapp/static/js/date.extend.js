/**
 * 日期相关
 */
//日期格式转换
Date.prototype.Format = function (fmt) {  
    var o = {  
        "M+": this.getMonth() + 1, //月份   
        "d+": this.getDate(), //日   
        "h+": this.getHours(), //小时   
        "m+": this.getMinutes(), //分   
        "s+": this.getSeconds(), //秒   
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度   
        "S": this.getMilliseconds() //毫秒   
    };  
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));  
    for (var k in o)  
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));  
    return fmt;  
}

//获取当天日期
function days(){  
    var date = new Date();//获取当前时间  
     var time = date.Format("yyyy-MM-dd"); 
     return time;
  }

//获取当天日期减1
function daysJian(){  
    var date = new Date();//获取当前时间  
    date.setDate(date.getDate()-1);//设置天数 -1 天  
     var time = date.Format("yyyy-MM-dd"); 
     return time;
  }
//指定日期减1
function dateJian(orgdate){
	var date =new Date(orgdate.replace(/-/g, '/'));
    date.setDate(date.getDate()-1);//设置天数 -1 天  
     var time = date.Format("yyyy-MM-dd");
     return time;
  }


//timestamp转换成datetime
function timeStamp2String(time){
    var datetime = new Date();
     datetime.setTime(time);
     var year = datetime.getFullYear();
     var month = datetime.getMonth() + 1;
     var date = datetime.getDate();
     return year + "年" + month + "月" + date + "日";
};

//格式化日期
function FormatDate(strTime) {
    var date = new Date(strTime);
    return date.getFullYear()+"年"+(date.getMonth()+1)+"月"+date.getDate()+"日";
}

//日期转换字符串格式 
function DateTimeToString(value) {
    if (value == null || value == '') {
        return '';
    }
       var dt;
	if (value instanceof Date) {
	    dt = value;
	}else {
	    dt = new Date(value);
	    if (isNaN(dt)) {
	        value = value.replace(/\/Date\((-?\d+)\)\//, '$1'); 
	        dt = new Date();
	        dt.setTime(value);
	    }
	}
	return dt.Format("yyyy-MM-dd hh:mm:ss");
}

//日期转换指定字符串格式 
function DateToStrfarmat(value,fmt) {
  if (value == null || value == '') {
      return '';
  }
     var dt;
	if (value instanceof Date) {
	    dt = value;
	}else {
	    dt = new Date(value);
	    if (isNaN(dt)) {
	        value = value.replace(/\/Date\((-?\d+)\)\//, '$1'); 
	        dt = new Date();
	        dt.setTime(value);
	    }
	}
	return dt.Format(fmt);
}
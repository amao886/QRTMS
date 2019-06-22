$(document).ready(function(){
	$("html,body").scrollTop(0);
});

//设置文档高度
var docH = $(document).height(),
	winH = $(window).height(),
	lastH = $(".targetBox").eq($(".targetBox").length-1).outerHeight(true),
	finalH = docH + winH -lastH;
$("body").height(finalH-70+"px");

//获取每个盒子相对于文档的位置
var topArr=[];
$(".targetBox").each(function(){
	var _top = $(this).offset();
	topArr.push(_top.top-44);
});

//点击跳转到对应的位置
$(".tabBox").on("click",".kinds",function(){
    var _index = $(".tabBox .kinds").index(this),
        pos = topArr[_index];
	//$(this).addClass("active").siblings().removeClass("active");

	$("html,body").stop().animate({scrollTop:pos},250);
});
$(document).scroll(function() {
	var top = getScrollTop(),
	    _index;
	for(var i=0 ; i < topArr.length ; i++){
		if(top > 0){
			if(i != topArr.length-1){
				if(topArr[i] <= top && topArr[i+1]>top){		
					_index = i;			
					break;
				}
			}else{
				_index = i;	
				break;
			}
		}					
	}	
    $(".tabBox .kinds").eq(i).addClass("active").siblings().removeClass("active");
});


/**
 *获取scrollTop的值，兼容所有浏览器 
 */
function getScrollTop() {
	var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
	return scrollTop;
}

/**
 *设置scrollTop的值，兼容所有浏览器 
 */
function setScrollTop(scroll_top) {
	document.documentElement.scrollTop = scroll_top;
	window.pageYOffset = scroll_top;
	document.body.scrollTop = scroll_top;
}

//点击图标跳转
$(".funcLink").on("click",function(){
	var _id = $(this).attr("data-num")
	jumpLink(_id);
});
//页面跳转
var jumpLink = function(num){				
	var _url = "";
	switch(num){
		case "101": _url = "sendGoods.html?index=index&token="+api_token; break;
		case "102": _url = "projectTeamTask.html?index=index"; break;
		case "103": _url = "sendMonitor.html"; break;
		case "104": _url = "customList.html"; break;
		
		case "201": $("#swapCircle").trigger("click"); break;//扫一扫
		case "202": break;//无
		case "203": _url = "loadGoods.html?index=index"; break;
		
		case "301": _url = "projectTeamTask.html?index=index"; break;
		case "302": _url = "checkStock.html?index=index"; break;
		case "303": $("#swapCircle").trigger("click"); break;//扫一扫
		case "304": _url = "loadGoods.html?index=index"; break;
		
		case "401": _url = "upload.html?showClose=10&waybillId="+""+"&index=index"; break;
        case "402": _url = "electronicManage.html"; break;//电子回单
		//case "402": _url = "electronicSearch.html"; break;//电子回单
		
		case "501": _url = "myProjectTeam.html"; break;
		case "502": _url = "applyResource.html"; break;
		case "503": break;//无
		case "504": _url = "myTask.html"; break;//无
		case "505": _url = "transportManage.html"; break;
		
		case "601": break;//同APP，暂无
		case "602": _url = "address.html"; break;
	}

	if(_url){
		window.location.href =  _common.version(_url);
	}
}

//小黑点跳转
$("#moreTouch").click(function(){
	window.location.href = _common.version("functionAll.html");
}); 



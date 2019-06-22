<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>常用路由</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}css/page.css?times=${times}" />
<link rel="stylesheet" href="${baseStatic}css/editRoute.css?times=${times}" />
<link rel="stylesheet" href="${baseStatic}plugin/js/city/pick-pcc.min.1.0.1.css?times=${times}" />
<style type="text/css">


</style>
</head>
<body>
	<div class="custom-content clearfix">
	   <div class="appendInput">
	        <label>路由名称</label>
	        <input type="text" id="routeName" class="routeInput" placeholder="自定义路由名称" />
	        <button type="button" class="btn btn-default" id="insertPoint">插入中转点</button>
	   </div>    
	       
		<div class="routeCon">			   
		    <!-- 循环体 st -->    
		    <div class="routeBox">
		        <!-- 起点 st -->
		        <div class="route clear first">
                    <div class="routeLeft clearfix">
                    	<input type="hidden" name="lineType" class="userType" value="1"/>
                    	<input type="hidden" name="address" class="userType" value=""/>
                        <span class="circle">起</span>
                        <div class="line"></div>                                                    
                    </div>
                    <div class="routeRight clearfix">
                        <p class="routeTips">该路段暂未指派，您可以<a class="assignBtn" onclick="friendBox(this)">指派</a></p>              
                    </div>                      
                </div>
                <!-- 起点 ed -->                                
                
                <!-- 终点 st -->
                <div class="route clear last">
                    <div class="routeLeft clearfix">
                    	<input type="hidden" name="lineType" class="userType" value="3"/>
                    	<input type="hidden" name="userId" class="userType" value="0"/>
                        <span class="circle">终</span>                                                   
                    </div>
                </div>
                <!-- 终点 ed -->
		    </div>
		    <!-- 循环体 ed -->
		    <!-- 提交按钮 -->
		    <div class="clear btn-div" style="margin-top:100px;">
	            <a class="btn btn-default submitBtn" id="submitBtn">保存</a>
	            <a id="cancelBtn" onClick="history.back(-1)">取消</a>
	        </div>
		</div>						
	</div>
	
	<!-- 中转点模版 -->
    <div id="template" class="hide">
        <div class="route routePoint clear">
            <div class="routeLeft clearfix">
            	<input type="hidden" name="lineType" class="userType" value="2"/>
                <span class="circle" title="删除" onclick="delPoint(this)">转</span>
                <div class="line"></div>                                                    
            </div>
            <div class="routeRight clearfix">
                <div class="manage-wrapper">
                    <a href="javascript:void(0)" class="pick-area"></a>
                    <!-- <input type="text" name="address" value="" class="hide"/> -->
                </div>
                <p class="routeTips">该路段暂未指派，您可以<a class="assignBtn" onclick="friendBox(this)">指派</a></p>             
            </div>                      
        </div>
    </div>
    
    <!-- 隐藏编辑框 -->
    <div id="editBoxCon" class="hide">
	    <div class="editBox">
	        <input type="hidden" name="userId" class="userType" value="_userId"/>
	        <ul class="formUl">	            
	            <li>
	                <a>联系人：</a>
	                <span class="contactName">_contactName</span>
	            </li>
	            <li>
	                <a>联系方式：</a>
	                <span class="contactPhone">_contactPhone</span>
	            </li>
	        </ul>
	        <a class="closeBtn" onclick="closeBox(this)"></a>
	        <a class="btn btn-default changeBtn" onclick="changefriends(this)">更改</a>
	    </div> 
    </div>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script src="${baseStatic}plugin/js/city/pick-pcc.min.1.0.1.js"></script>
<script src="${baseStatic}js/select.resource.js?times=${times}" ></script>
<script>
String.prototype.replaceAll = function(s1,s2) { 
    return this.replace(new RegExp(s1,"gm"),s2); 
};

//显示选择好友弹窗
function friendBox(obj){
	$obj = $(obj);
	$obj.selectUser({selectClose:true, selectFun: function(item){
        //item为选择好友用户信息
        var _target = $obj.parent(".routeTips");       
		var _html = $("#editBoxCon").html()
		_html = _html.replaceAll("_userId",item.friendKey)
		             .replaceAll("_contactName",item.remarkName ? item.remarkName : item.userName)
		             .replaceAll("_contactPhone",item.mobile)
		
		if(_target.next().length){
			_target.next().remove();
		}
		$obj.parent(".routeTips").addClass("hide").parent(".routeRight").append(_html);	
    }});
}

//更改好友
function changefriends(obj){
	$obj = $(obj);
	$obj.selectUser({selectClose:true, selectFun: function(item){
        //item为选择好友用户信息
        var _target = $obj.parent(".editBox");       
        _target.find(".userType").val(item.friendKey);
        _target.find(".contactName").html(item.remarkName ? item.remarkName : item.userName);
        _target.find(".contactPhone").html(item.mobile);		
    }});
}

$(".closeBtn").click(function(obj){
	closeBox(obj);
})
$(".routeBox").on("click",".closeBtn",function(obj){
	closeBox(obj);
});

function closeBox(obj){
	var $this = $(obj);
	$this.parent().prev().removeClass("hide").next().remove();
    /*$this.parent().find("input").each(function(index){   
        var $obj = $(this);
        if(index > 0){          
            $obj.val("");
        }else{
            $obj.val("1");
            $obj.prev("span").removeClass("curr").prev("span").addClass("curr");
        }        
    })*/
    
}

//选择承运商或者司机
/*$(".routeBox").on("click",".chooseSingle span",function(){
	var $this = $(this),
	    _data = $this.attr("data-type");
	$this.addClass("curr").siblings("span").removeClass("curr").siblings("input").val(_data);
});*/


var cNum = 0;
$("#insertPoint").on("click",function(){
	var _html = $("#template").find(".routePoint").clone();
	$(_html).insertBefore(".route.last");
	$(".routeBox").find(".routePoint:eq("+ cNum +")").find(".pick-area").pickArea({
        "width":"300",
        "getVal":function(){
            //console.log($(".pick-area-hidden").val())
            //console.log($(".pick-area-dom").val())
            //var thisdom = $("."+$(".pick-area-dom").val());
            //thisdom.next().val($(".pick-area-hidden").val());
        }
    });
	cNum ++;
	return cNum;
});

//删除节点
function delPoint(obj){
	$.util.confirm("提示", "是否删除中转点", function(){
		var $this = $(obj);
		$this.parents(".routePoint").remove();
		cNum --;
		$(".jconfirm-closeIcon").trigger("click");
		return cNum;
	});
}


//选择客户信息——弹窗
/*$("#chooseCustomer").on("click", function () {
    if ($("#select_group").val() != "") {
        $.util.form('客户信息', $(".bind_customer_panel").html(), function (model) {
            var editBody = this.$body;
            searchCustomer(editBody);
            editBody.find("button").on('click', function () {
                searchCustomer(editBody);
            });
        }, function () {
        });
    } else {
        $.util.warning("请先选择项目组");
    }

});*/
//
$("#submitBtn").on("click", function(){
	var routeName = $("#routeName").val();
	var validateError="";
	if(!routeName){
		$.util.error("路由名称不能为空");
		return false;
	}
	$(".routeBox").find("input.pick-area,p.routeTips").each(function(){
		var $this = $(this);
		if($this.attr("class")=="routeTips"){
			if($this.is(":visible")){
				validateError = "userId";
			}
		}else if($this.attr("class")=="pick-area"){
			$this.attr("name","address")
			if(!$this.val()){
				validateError = "address";
			}
		}
	})
	//获取参数并封装
	//var length = $(".routeBox").find("input").length;
	var parmas = {"routeName":routeName};
	var routeLines = [];
	var lastUserId = null;
	var length = $(".routeBox .route").length;
	$(".routeBox .route").each(function(index,item) {
		var routeLine = {};
		var $this = $(item);
		if(index == length-2){
			lastUserId = $this.find("input[name='userId']").val();
		}
		if(index == length-1){
			$this.find("input[name='userId']").val(lastUserId==null?"":lastUserId);
		}
		$(item).find(":input").each(function(_index,_item){
			routeLine[_item.name] = _item.value;
		})
		routeLines[index] = routeLine;
	})
	/* $(".routeBox").find(":input").each(function(index,item) {
		routeLine[item.name] = item.value;
		if(index == length-3){
			lastUserId = item.value;
		}
		if((index+1)%3==0){
			routeLines.push(routeLine);
			routeLine = {};
		}
		if(length == (index+1)){
			routeLine[item.name] = lastUserId;
			routeLines.push(routeLine);
		}
	}) */
	parmas["routeLines"] = JSON.stringify(routeLines);
	//console.log(JSON.stringify(parmas));
	if(validate(validateError)){
		$.util.json(base_url+'/backstage/route/save', parmas, function(data){
	        if(data.success){
	            $.util.alert('操作提示', data.message, function(){
	            	location.href = base_url + "/backstage/route/list";
	            });
	        }else{
	            $.util.error(data.message);
	        }
	     });
	}
})
function validate(validateError){
	if(validateError=="userId"){
		$.util.error("请选择司机或承运商");
		return false;
	}else if(validateError=="address"){
		$.util.error("请选择地址");
		return false;
	}else{
		return true;
	}
}
</script>

</html>
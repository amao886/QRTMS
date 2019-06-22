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
	        <input type="text" id="route-info" data-id="${mergeRoute.id }" class="routeInput" value="${mergeRoute.routeName }" placeholder="自定义路由名称" />
	        <button type="button" class="btn btn-default" id="insertPoint">插入中转点</button>
	   </div>    
	       
		<div class="routeCon">			   
		    <!-- 循环体 st -->    
		    <div class="routeBox">
		    	<c:forEach items="${mergeRoute.routeLines }" var="line">
			        <c:if test="${line.lineType == 1}">
				        <!-- 起点 st -->
				        <div data-id="${line.id}" data-pid="${line.pid }" class="route clear first">
		                    <div class="routeLeft clearfix">
		                    	<input type="hidden" name="lineType" class="userType" value="1"/>
		                        <span class="circle">起</span>
		                        <div class="line"></div>                                                    
		                    </div>
		                    <div class="routeRight clearfix">
		                        <c:if test="${line.user == null}">
									<p class="routeTips">该路段暂未指派，您可以<a class="assignBtn" onclick="friendBox(this)">指派</a></p>
								</c:if>
								<c:if test="${line.user != null}">
									<p class="routeTips hide">该路段暂未指派，您可以<a class="assignBtn" onclick="friendBox(this)">指派</a></p>
									<div class="editBox">
										<input type="hidden" name="userId" class="userType" value="${line.user.id }"/>
										<ul class="formUl">
											<li>
												<a>联系人：</a>
												<span class="contactName">${line.user.unamezn }</span>
											</li>
											<li>
												<a>联系方式：</a>
												<span class="contactPhone">${line.user.mobilephone } </span>
											</li>
										</ul>
										<a class="closeBtn" onclick="closeBox(this)"></a>
										<a class="btn btn-default changeBtn" onclick="changefriends(this)">更改</a>
									</div>
								</c:if>
		                    </div>                      
		                </div>
		                <!-- 起点 ed -->                                
			        </c:if>
			        <c:if test="${line.lineType == 2}">
		                <!-- 中转点 st -->
		                <div data-id="${line.id }" data-pid="${line.pid }" class="route routePoint clear">
				            <div class="routeLeft clearfix">
				            	<input type="hidden" name="lineType" class="userType" value="2"/>
				                <span class="circle delete-point" title="删除">转</span>
				                <div class="line"></div>                                                    
				            </div>
				            <div class="routeRight clearfix">
				                <div class="manage-wrapper">
				                    <a href="javascript:void(0)" class="pick-area" name="${line.address }"></a>
				                    <!-- <input type="text" name="address" value="" class="hide"/> -->
				                </div>
								<c:if test="${line.user == null}">
									<p class="routeTips">该路段暂未指派，您可以<a class="assignBtn" onclick="friendBox(this)">指派</a></p>
								</c:if>
								<c:if test="${line.user != null}">
									<p class="routeTips hide">该路段暂未指派，您可以<a class="assignBtn" onclick="friendBox(this)">指派</a></p>
									<div class="editBox">
										<input type="hidden" name="userId" class="userType" value="${line.user.id }"/>
										<ul class="formUl">
											<li>
												<a>联系人：</a>
												<span class="contactName">${line.user.unamezn }</span>
											</li>
											<li>
												<a>联系方式：</a>
												<span class="contactPhone">${line.user.mobilephone } </span>
											</li>
										</ul>
										<a class="closeBtn" onclick="closeBox(this)"></a>
										<a class="btn btn-default changeBtn" onclick="changefriends(this)">更改</a>
									</div>
								</c:if>
				            </div>                      
				        </div>             
		                <!-- 中转点 ed -->  
			        </c:if>
	                <c:if test="${line.lineType == 3}">
		                <!-- 终点 st -->
		                <div data-id="${line.id }" data-pid="${line.pid }" class="route clear last">
		                    <div class="routeLeft clearfix">
		                    	<input type="hidden" name="lineType" class="userType" value="3"/>
		                    	<input type="hidden" name="userId" class="userType" value="0"/>
		                        <span class="circle">终</span>                                                   
		                    </div>
		                </div>
		                <!-- 终点 ed -->
	                </c:if>
                </c:forEach>
		    </div>
		    <!-- 循环体 ed -->
		    <!-- 提交按钮 -->
		    <div class="clear" style="margin-top:10px;">
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
                <span class="circle delete-point" title="删除">转</span>
                <div class="line"></div>                                                    
            </div>
            <div class="routeRight clearfix">
                <div class="manage-wrapper">
                    <a href="javascript:void(0)" class="pick-area"></a>
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
//初始化地址插件
$(".routeBox .pick-area").pickArea({
        "width":"300",
        "getVal":function(){}
});
$("input.pick-area").each(function(){
	var _val = $(this).parents(".pick-area").attr("name")
	$(this).val(_val);
});

//显示选择好友弹窗
function friendBox(obj){
	$obj = $(obj);
	$obj.selectUser({selectClose:true, selectFun: function(item){//选择好友
        //item为选择好友用户信息
        var _target = $obj.parent(".routeTips");
		if(_target.next().length){
			_target.next().remove();
		}
		var _html = $("#editBoxCon").html();
		_html = _html.replaceAll("_userId",item.friendKey)
		             .replaceAll("_contactName",item.remarkName ? item.remarkName : item.userName)
		             .replaceAll("_contactPhone",item.mobile);
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
$('.delete-point').on('click', function(){
    var $this = $(this);
    $.util.confirm("提示", "是否删除中转点", function(){
        $this.parents(".routePoint").remove();
    });
});

$("#insertPoint").on("click",function(){
    var cNum = $(".routeBox .routePoint").length;
	$(".route.last").before($("#template").find(".routePoint").clone());
	var $current = $(".routeBox").find(".routePoint:eq("+ cNum +")");
    $current.find('.delete-point').on('click', function(){
        var $this = $(this);
        $.util.confirm("提示", "是否删除中转点", function(){
            $this.parents(".routePoint").remove();
        });
    });
    $current.find(".pick-area").pickArea({ "width":"300" });
});

$(".submitBtn").on("click", function(){
    var $route = $('#route-info');
    var parmas = {
        id:$route.data('id'),
		routeName:$route.val()
    };
	if(!parmas.routeName){
        $.util.error("路由名称不能为空");
        return false;
	}
	var lines = [], points = [];
    $(".routeBox").find(".route").each(function(){
        var obj = {id: $(this).data('id')};
        $(this).find('input').each(function(){
            var name = $(this).attr('name');
            if(!name){
				name = 'address';
			}
            obj[name] = $(this).val();
		});
        if(Number(obj.lineType) == 2){
            points.push(obj);
		}
        lines.push(obj);
	});
    if(points == null || points.length <= 0){
        $.util.error("至少输入一个中转点");
        return false;
	}
    for(var i in points){
        if(!$.trim(points[i].address)){
            $.util.error("请选择中转点区域");
            return false;
        }
    }
	parmas["lines"] = JSON.stringify(lines);
	console.log(parmas);
    $.util.json(base_url+'/backstage/route/update', parmas, function(data){
        if(data.success){
            $.util.alert('操作提示', data.message, function(){
                location.href = base_url + "/backstage/route/list";
            });
        }else{
            $.util.error(data.message);
        }
    });
})
</script>

</html>
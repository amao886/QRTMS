<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-权限列表</title>
    <%@ include file="/views/include/head.jsp" %>
</head>
<body>
	<div class="track-content clearfix">
		<div  class="clearfix">
			<button type="button" class="btn btn-default btn_add_permission">新增</button>
		</div>
		<div class="table-style">
			<table class="dtable" id="trackTable">
				<thead>
					<tr>
						<th>权限名称</th>
						<th>权限编号</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${!empty list }">
						<c:forEach items="${list }" var="permission">
							<tr>
								<td>${permission.permissionname }</td>
								<td>${permission.permissioncode }</td>
								<td>
									<button type="button" data-id="${permission.id}" class="btn btn-link edit_something" >编辑</button>
									<button type="button" data-id="${permission.id}" class="btn btn-link del" >删除</button>
								</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>
	<div class="edit_permission_panel" style="display: none">
		<div class="model-base-box edit_permission" style="width: 470px;">
			<div class="model-form-field">
				<label for="">权限名称 :</label> 
				<input id="permissionname" name="permissionname" type="text">
			</div>
			<div class="model-form-field">
				<label for="">权限编号 :</label>
				<input id="permissioncode" name="permissioncode" type="text">
			</div>
		</div>
	</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script>
    $(document).ready(function () {
       //initList();//权限列表
       //新增权限
	   $(".btn_add_permission").click(function () {
		   $.util.form("权限新增",$(".edit_permission_panel").html(),function(){
				},function(){
					var parmas = {}, editBody = this.$body;
					editBody.find("input").each(function(index, item){
	    				parmas[item.name] = item.value;
					});
					if(checkValue(parmas)){
						$.util.json(base_url + '/admin/permission/add', parmas, function(data){
							if(data.success){
					 			$.util.alert('操作提示', data.message, function(){
					 			location.reload();
							});
						}else{
							$.util.error(data.message);
						}
					});
				}
				return false;
			})			
	    })
	    //修改权限
	    $(".edit_something").click(function () {
	    	var id = $(this).attr("data-id");
	    	$.post(base_url + '/admin/permission/info/'+ id, {} , function(data) {
	    	   if(data.success){
	    		   $.util.form("权限新增",$(".edit_permission_panel").html(),function(){
	    			   var editBody = this.$body
	    			   editBody.find(":input[name='permissionname']").val(data.permission.permissionname);
	    			   editBody.find(":input[name='permissioncode']").val(data.permission.permissioncode);
					},function(){
					var parmas = {}, editBody = this.$body;
					editBody.find("input").each(function(index, item){
		    			parmas[item.name] = item.value;
					});
					if(checkValue(parmas)){
						parmas['id'] = id;
						$.util.json(base_url + '/admin/permission/update', parmas, function(data){
							if(data.success){
						 		$.util.alert('操作提示', data.message, function(){
						 			location.reload();
								});
							}else{
								$.util.error(data.message);
							}
						});
					}
					return false;
					})  
	    	   }else{
	    		   $.util.error(data.message);
	    	   }	
		    }) 
		})
		//删除权限
		$(".del").click(function(){
			var id = $(this).attr('data-id');
	    	$.post(base_url+"/admin/permission/delete/" + id, {} , function(data){
	    		if(data.success){
	    			location.reload();
	    		}else{
	    			$.util.error(data.message);
	    		}
	    	})
		})
    });
    //查询所有权限
    function initList(){
    	$.post(base_url+"/admin/permission/list",{},function(data){
    		if(data.success){
    			$(document).find('tbody').html("");
    			$.each(data.result, function(index,item){
						var _html = "<tr><td>" + item.permissionname + "</td>"
								+ "<td>"+ item.permissioncode + "</td>"
								+ "<td><button type='button' data-id='" 
								+ item.id + "' class='btn btn-link edit_something'>编辑</button>"
								+ "<button type='button' data-id='" 
								+ item.id + "' class='btn btn-link del'>删除</button></td></tr>";
					 	$('tbody').prepend(_html);
				 })
    		}else{
    			$.util.error(data.message);	
    		}
    	})
    }
    //数据校验
    function checkValue(parmas){
    	var permissionName = parmas['permissionname'];
    	var permissionCode = parmas['permissioncode'];
    	if(!permissionName){
    		alert("权限名称不能为空！");
    		return false;
    	}
    	if(permissionCode==null||permissionCode==''){
    		alert("权限编号不能为空！");
    		return false;
    	}
    	return true;
    } 
</script>
</html>
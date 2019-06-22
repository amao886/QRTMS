<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-权限管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <style type="text/css">
    	ul{
    		margin-left:80px;
    	}
    	li{
    		display:inline;
    		margin:10px;
    	}
    </style>
</head>
<body>
	<div class="track-content clearfix">
		<div  class="clearfix">
			<button type="button" class="btn btn-default btn_add_role">新增角色</button>
	        <button type="button" class="btn btn-default btn_add_permission">新增权限</button>
		</div>
		<div class="table-style">
			<table class="dtable" id="trackTable">
				<thead>
					<tr>
						<th width="10%">角色名称</th>
						<th width="10%">角色代号</th>
						<th width="15%">创建时间</th>
						<th>所含权限</th>
						<th width="10%">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${!empty list }">
						<c:forEach items="${list }" var="roleAndPerssion">
							<tr>
								<td>${roleAndPerssion.roleName }</td>
								<td>${roleAndPerssion.roleCode }</td>
								<td><fmt:formatDate value="${roleAndPerssion.createtime }" pattern="yyyy-MM-dd HH:mm"/></td>
								<td>
									<c:forEach items="${roleAndPerssion.projectGroupPermissions }" var="permission" varStatus="status">
										${permission.permissionname }
										<c:if test="${fn:length(roleAndPerssion.projectGroupPermissions) != status.index+1}">,</c:if>
									</c:forEach>
								</td>
								<td>
									<button type="button" data-roleid="${roleAndPerssion.roleid }" data-id="${roleAndPerssion.id}" class="btn btn-link edit_something" >编辑</button>
									<button type="button" data-roleid="${roleAndPerssion.roleid }" data-id="${roleAndPerssion.id}" class="btn btn-link del" >删除</button>
								</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>
	<div class="edit_role_panel" style="display: none">
		<div class="model-base-box edit_customer" style="width: 470px;">
			<!-- <input name="" type="hidden"> -->
			<div class="model-form-field">
				<label for="">角色名称 :</label> 
				<input id="rolename" name="roleName" type="text">
			</div>
			<div class="model-form-field">
				<label for="">角色编号 :</label>
				<input id="rolecode" name="roleCode" type="text">
			</div>
			<div class="model-form-field">
				<label for="">角色权限 :</label> 
				<ul id="permission_ul">
				</ul>
			</div>
		</div>
	</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script>
    $(document).ready(function () {
	    $(".btn_add_role").click(function () {
	    	$.post(base_url+"/admin/permission/list",{},function(data){
	    		if(data.success){
	    			$.util.form("角色新增",$(".edit_role_panel").html(),function(model){
	    				 var editBody = this.$body;
	    				 $.each(data.result, function(index,item){
	    					 var str = "<li style='width: 80px;'><input type='checkbox' name='permission"+item.id +"' value='";
	    						 editBody.find('ul').prepend(str + item.id +"'/>"+ item.permissionname + "</li>");
	    					 if((index+1) % 2 == 0){
		    					 editBody.find('ul').prepend("<br/>");
	    					 }
	    				 })
	    			},function(){
	    				var parmas = {},arr = new Array(); editBody = this.$body;
	    				editBody.find("input").each(function(index, item){
	    					var permissionIds = {};
	    					if(item.checked){
	    						permissionIds['id'] = item.value
	    						arr.push(permissionIds);
	    					}else{
	    						if(item.type != 'checkbox'){
			    					parmas[item.name] = item.value;
	    						}
	    					}
	    				});
	    				if(arr.length>0){
		    				parmas['projectGroupPermissions'] =  arr;
	    				}
	    				if(checkValue(parmas)){
	    					$.util.json(base_url + '/admin/permission/role/add', parmas, function(data){
	    						if(data.success){
	    							$.util.alert('操作提示', data.message, function(){
	    								location.href= base_url + '/admin/permission/search';
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
		//权限修改
		$(".edit_something").click(function () {
			var id = $(this).attr('data-id'), roleid = $(this).attr('data-roleid');
			$.post(base_url+"/admin/permission/rolePermission/" + id, {}, function(data){
	    		if(data.success){
	    			var rolePermission = data.result;
	    			var checkPermission = data.checkPermission;
	    			var permissions = data.permissions;//所有权限集合
	    			$.util.form("角色修改",$(".edit_role_panel").html(),function(model){
	    				 var editBody = this.$body;
	    				 editBody.find(":input[name='roleName']").val(rolePermission.roleName);
	    				 editBody.find(":input[name='roleCode']").val(rolePermission.roleCode);
	    				 $.each(permissions, function(index, item){
	    					 var str = "<li style='width: 80px;'><input type='checkbox' name='permission"+item.id +"' value='";
	    					 if(item.permissioncode === checkPermission[item.id]){
		    					 editBody.find('ul').prepend(str + item.id +"' checked='true'/>"+ item.permissionname + "</li>");
	    					 }else{
	    						 editBody.find('ul').prepend(str + item.id +"'/>"+ item.permissionname + "</li>");
	    					 }
	    					 if((index+1) % 2 == 0){
		    					 editBody.find('ul').prepend("<br/>");
	    					 }
	    				 })
	    			},function(){
	    				var parmas = {},arr = new Array(); editBody = this.$body;
	    				editBody.find("input").each(function(index, item){
	    					var permissionIds = {};
	    					if(item.checked){
	    						permissionIds['id'] = item.value
	    						arr.push(permissionIds);
	    					}else{
	    						if(item.type != 'checkbox'){
			    					parmas[item.name] = item.value;
	    						}
	    					}
	    				});
	    				if(arr.length>0){
		    				parmas['projectGroupPermissions'] =  arr;
	    				}
	    				if(checkValue(parmas)){
	    					parmas['roleid'] = roleid;
	    					$.util.json(base_url + '/admin/permission/rolePermission/update', parmas, function(data){
	    						if(data.success){
	    							$.util.alert('操作提示', data.message, function(){
	    								location.href= base_url + '/admin/permission/search';
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
		
		//新增权限跳转
		$(".btn_add_permission").click(function () {
			location.href= base_url + '/admin/permission/goto/permission';
		})
		//角色列表删除
		$(".del").click(function(){
			var id = $(this).attr('data-id') , roleId = $(this).attr('data-roleid');
	    	$.post(base_url+"/admin/permission/role/del/" + id +"/" + roleId, {} , function(data){
	    		if(data.success){
	    			location.href= base_url + '/admin/permission/search';
	    		}else{
	    			$.util.error(data.message);
	    		}
	    	})
		})
    });
    //新增角色校验
    function checkValue(parmas){
    	var roleName = parmas['roleName'];
    	var roleCode = parmas['roleCode'];
    	var checkBox = parmas['projectGroupPermissions'];
    	if(roleName==null||roleName==''){
    		alert("角色名称不能为空！");
    		return false;
    	}
    	if(roleCode==null||roleCode==''){
    		alert("角色代号不能为空！");
    		return false;
    	}
    	if(!checkBox || checkBox.length==0){
    	    alert("对不起，权限至少要选一项！");
    	    return false
    	}
    	return true;
    } 
</script>
</html>
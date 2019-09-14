

const vm = new Vue({
	el:'#app',
	data:{
		list:[]
	},
	mounted:function(){
		var _this = this;
		//发送请求，获取员工列表数据渲染到页面
		 EasyAjax.ajax_Post_Json({
				url: 'enterprise/company/employee/list',
				data:JSON.stringify({})
			}, function (res) {
		      if(res.success){
		      	console.log(res)
		      	_this.list = res.employees;
		      }
							
	   })
	},
	methods:{
	allocationPermissions:function(employeeId){
		console.log(employeeId)
		//点击分配权限，跳转到分配权限页面
		window.location.href = 'allocatPermission.html?employeeId='+employeeId
	}
	}
})

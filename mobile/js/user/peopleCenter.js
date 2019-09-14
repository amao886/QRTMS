//页面初始化
$("#navBarIn").load("../common/navBarIn.html",function () {
    $(".weui-tabbar__item:first").addClass("weui-bar__item--on");
    
	$('.a-hook').on('click',function(){
    	$('.a-hook').removeClass('active');
    	$(this).addClass('active');     	
    })
    
	//跳转到首页
    $('#myIndex').on('click',function(){
    	window.location.href =_common.version('../../home.html?jstatus='+0);
    })
    
    //跳转到扫一扫
    $('#swapCircle').on('click',function(){
    	scanCode();
    })
    
    //跳转到通讯录
    $('#conList').on('click',function(){
    	window.location.href = _common.version('../partener/contactList.html?jstatus='+1);
    })
    
    //跳转到个人中心
    $('#peopleCenter').on('click',function(){
    	window.location.href = _common.version('peopleCenter.html?jstatus='+2 + '&identity=' + _common.getUrlParam('identity'));
    })
    
    var jstatus = _common.getUrlParam('jstatus');
    console.log('jstatus:'+jstatus);
    if(jstatus == '0'){
    	//首页
    }else if(jstatus == '1'){
    	//通讯录
    	$('.a-hook').removeClass('active');
    	$('#conList').addClass('active');
    }else if(jstatus == '2'){
    	//个人中心
    	$('.a-hook').removeClass('active');
    	$('#peopleCenter').addClass('active');
    }
});

$("#content").height($(window).height() - 52);

const vm = new Vue({
	el:'#content',
	data:{
		list:[],
		money:0.00
	},
	methods:{
		//修改企业
		EditCompany:function(){
			console.log(this.list[0])
			  // allowModify 为true 才有权限 修改企业名称
			  //如果没有企业则允许添加企业
			if(this.list[0].allowModify){
             		window.location.href =_common.version('./companyNameEdit.html?companyName='+this.list[0].companyName);
             }else if(this.list[0].companyName==null||this.list[0].companyName==''){
				window.location.href =_common.version('./addCompany.html');
			}else {
				$.toast('无操作权限！','cancel')
			}
		},
		//企业员工
		// 只有allowAdd为true才表示有权限添加员工
		myEmployee:function(){
//			
			if(this.list[0].allowAdd){
				window.location.href = "../employee/myEmployee.html"
			}else {
				$.toast('无操作权限！','cancel')
			}
		},
		//实名认证
		Authentication:function(){
			if(this.list[0].fettle>0){
				window.location.href = _common.version('./personRealNameAuthenticationInfo.html');
			}else {
			window.location.href = _common.version('./personRealNameAuthentication.html?type=');
			}
		},
		//切换身份
		changeRole:function(identity,headImg){
			var _this = this;

			var headUrl = '';

			if (!_this.isNull(headImg)) {
				headUrl = headImg;
			}
			window.location.href = "exchangeRole.html?identify="+identity+'&headImg='+headUrl;
		},
		//司机资料查看
		applicationDriver:function(){
			window.location.href='../../view/driver/applyOrEditDriverInformation.html'
		},
		//司机资料查看
		dirverInformation:function(){
			window.location.href='../../view/driver/personalDirverInformation.html'
		},
		isNull: function(text) {
			return !text && text !== 0 && typeof text !== "boolean" ? true : false;
		}
	},
	mounted:function(){
		var _this = this;
			EasyAjax.ajax_Post_Json({
							url: 'admin/user/info',
						}, function (res) {
							console.log(res)
						  _this.list.push(res) 
						  _this.money =Math.floor(res.money * 100) / 100;
						})
	}
})

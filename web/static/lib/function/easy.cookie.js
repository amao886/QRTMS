/**
 * easyCookie
 * 用于处理数据缓存
 */
var easyCookie = {
	/**
	 * 存储用户信息
	 */
	set_user:function(data){
		$.cookie("user",JSON.stringify(data),{path:"/"});
	},
    /**
     * 删除用户信息
     */
	del_user:function(){
		$.cookie("user","",{path:"/"});
	},
    /**
     * 获取用户昵称
     */
    get_userName:function(){
		var user=$.cookie("user");
		if (user) {
			return JSON.parse(user).userName;
		}else{
			return null;
		}
	},
    /**
     * 存储用户token
     */
    set_token : function (data) {
       //$.cookie("token",JSON.stringify(data),{path:"/"});
       $.cookie("token",data,{path:"/"});

    },
    /**
     * 获取用户token
     */
    get_token : function () {
		var token = $.cookie("token");
		return token;
	}
};
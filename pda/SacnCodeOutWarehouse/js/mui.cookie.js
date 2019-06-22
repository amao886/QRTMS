/**
 * muiCookie
 * 用于处理数据缓存
 */
var muiCookie = {
    /**
     * 存储用户token
     */
    set_token : function (data) {
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
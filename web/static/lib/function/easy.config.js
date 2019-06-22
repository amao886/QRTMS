/**
 * 系统路径
 */
var api_header = "www.wq5000.xyz/", //fty.3w.dkys.org   开发环境：kshdev.ycgwl.com  测试环境：kshtest.ycgwl.com
	project = "",
	api_host = "http://" + api_header + "/" + project;

//api_token 从url参数获取
var api_token = _common.getUrlParam('token');
if(api_token) { //如果api_token不为空或者部位
	easyCookie.set_token(api_token);
	console.log("我是原始token " + api_token)
} else {
	api_token = easyCookie.get_token();
	console.log("我是cookie" + api_token)
}
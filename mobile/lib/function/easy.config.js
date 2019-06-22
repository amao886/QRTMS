/**
 * 系统路径
 */
var api_header="www.wq5000.xyz", //fty.3w.dkys.org   开发环境：kshdev.ycgwl.com  测试环境：kshtest.ycgwl.com
    project="",
    api_host="http://"+api_header+"/"+project;

//var api_token = '2D10F513778D27520013CF7326F9218B46C85D0862DD654DD7F7E120B8087A8548105981A5FA96450707357A32528BFFE64D2347E3D222BD3478DC2FF72F4B35B0311F92EC4D9F8A8CB1D96A796F785091B453DF223BF54FFC45E50243E02E5CFAE3515F796BE7DE';

//api_token 从url参数获取
var api_token = _common.getUrlParam('token');
if(api_token){//如果api_token不为空或者部位
	easyCookie.set_token(api_token);
	console.log("我是原始token "+api_token)
}else{
	api_token = easyCookie.get_token();
	console.log("我是cookie"+api_token)
}

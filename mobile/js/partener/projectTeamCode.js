'use strict';
var groupId = _common.getUrlParam('groupId');
console.log(groupId);
$(function(){
    $("#qrcodeimg").qrcode({ 
      render: "canvas",
      //width: 200, //宽度 
      //height:200, //高度 
      typeNumber  : -1,      //计算模式    
      correctLevel: 0,//纠错等级    
      background: "#fff",//背景颜色    
      foreground: "#000", //前景颜色   
      //添加组员  
      text: api_host+'mobile/wechat/group/'+ groupId +'?state=GROUP'
    });
});

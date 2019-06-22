<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>运单地图</title>
<%@ include file="/views/include/head.jsp"%>
<link rel="stylesheet" href="${baseStatic}plugin/css/jquery.Jcrop.min.css" />
<style type="text/css">
.preimg{
	align-content: space-between;
}
.preimg img{
	margin: 2px;
}
</style>
</head>
<body>
<div>
  <div align="left" id="imgDiv" class="preimg"></div> 
  <div align="left" id="imgDiv2" class="preimg2"></div>
  <input type="file" value="选择上传文件">
  <input type="button" class="upload" value="上传">
  <input type="button" class="crop" value="裁剪">
</div>
</body>
<%@ include file="/views/include/floor.jsp"%>
<script type="text/javascript" src="${baseStatic}js/image.extend.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/js/jquery.Jcrop.min.js"></script>
<script>

$(document).ready(function(){
	var jcrop_api;
	$("input:file").on("change", function(obj){
		for (var i = 0; i < this.files.length; i++) {
			var reader = new FileReader();
            reader.onload = function(e) {
            	var imgelement = document.createElement("img");
    			imgelement.setAttribute("src", e.target.result);
    			$("#imgDiv").append(imgelement);
    			var jcrop_api;
    			$(imgelement).Jcrop({},function(){
   			        var self = this;
   			    	$(".crop").on("click", function(){
	   			        var c = self.tellSelect();
	   			        var canvas = document.createElement('canvas');
	   			     	canvas.width = c.w;
	   			     	canvas.height=c.h;
	   			        var context = canvas.getContext("2d")
	   			        //img.crossOrigin="*"; //关键
	   			        
	   			        console.log(c.x, c.y, c.x2, c.y2, c.w, c.h)
	   			        
	   			        context.drawImage(imgelement, c.x, c.y, c.w, c.h, 0, 0, c.w, c.h);
	   			        //alert(canvas.toDataURL("image/png"));
	   			        var cropImg = document.createElement("img");
	   			        cropImg.src = canvas.toDataURL("image/png");
	   			        $("#imgDiv2").append(cropImg);
   			       });
   			    });
            }
            reader.readAsDataURL(this.files[i]);
		}
	})
	$(".upload").on("click", function(){
		var images = [];
		$("img").each(function(){
			var img = imageExt.compress(this, 30, 'jpg');
			var imgelement = document.createElement("img");
			imgelement.setAttribute("src", img.src);
			$("#imgDiv2").append(imgelement);
			images.push(img.src);
		});
		imageExt.multiple(base_url+"/upload/image", images, {
			loadstart:function(e){console.log(e.target.responseText)}, 
			loadend:function(e){console.log(e.target.responseText)}, 
			progress:function(e){console.log(e.target.responseText)}, 
			load:function(e){console.log(e.target.responseText)}, 
			error:function(e){console.log(e.target.responseText)}, 
			abort:function(e){console.log(e.target.responseText)}
		})
	});
});
</script>
</html>
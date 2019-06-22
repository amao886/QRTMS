// JavaScript Document


;(function($){
	$.fn.viewImg=function(options){
		var $this=$(this);
		var defaults = {
			'sizeImg':'3',  //图片大小
			'upImg':'',     //显示的图片,
			'upFile':$this,    //上传图片的file
			'callback':function(){}       //选择图片成功后回调函数
		};
		var opt=$.extend(defaults,options);
		return this.each(function(){
			var isIE = (navigator.userAgent.indexOf('MSIE') >= 0)&&(navigator.userAgent.indexOf('Opera') < 0);
			var fileSize = 0; 
			var filepath = $this.val(); 
			var filemaxsize = 1024*opt.sizeImg; //2M 
			var state=true;
			if(filepath){ 
				var isnext = false; 	
				var str  = filepath.split('.');
				var jpg=str[str.length-1].toLowerCase().indexOf("jpg")>-1,
					gif=str[str.length-1].toLowerCase().indexOf("gif")>-1,
					png=str[str.length-1].toLowerCase().indexOf("png")>-1,
					jpeg=str[str.length-1].toLowerCase().indexOf("jpeg")>-1,
					bmp=str[str.length-1].toLowerCase().indexOf("bmp")>-1;
				if(jpg||gif||png||jpeg||bmp){
					isnext = true; 
				}
				if(!isnext){
					$(opt.tip).text("不接受此文件类型！")
					$(this).val("");
					$(opt.upImg).attr("src","");
					if(isIE){
						this.select();	
						$(opt.upImg).attr("filter","");
						$(opt.upImg).attr("src","");	
						$(opt.upImg).parent().addClass("Validform_error");
						$this.val("");
					}
					$(opt.upImg).parent().addClass("Validform_error"); 
					state=false; 
					return false; 
				} 
			}else{ 
			    state=false; 
				return false; 
			} 
			if (isIE && !this.files) { 
				try{
					var fileSystem = new ActiveXObject("Scripting.FileSystemObject"); 
					if(!fileSystem.FileExists(filePath)){ 
						$(opt.tip).text("图片不存在，请重新选择！"); 
						return false; 
					} 
					var file = fileSystem.GetFile (filePath); 
					fileSize = file.Size; 
				}catch(e){}
			} else { 
				fileSize = this.files[0].size; 
			} 
			
			var size= fileSize / 1024; 
			if(size>filemaxsize){ 
				$(opt.tip).text("图片大小不能大于"+filemaxsize/1024+"M！");
				$this.val("");
				$(opt.upImg).attr("src","");
				$(opt.upImg).parent().addClass("Validform_error"); 
				state=false; 
				return false; 
			}  
			if(size<=0 && !isIE){
				$(opt.tip).text("图片大小不能为0M！");
				$this.val(""); 
				$(opt.upImg).attr("src","");
				$(opt.upImg).parent().addClass("Validform_error"); 
				state=false;
				return false; 
			}	 
			//图片预览ie
			if(state){
				if (isIE && !this.files){//!this.files 排除ie10，ie10不兼容filters
					$this.select();
					$(opt.upImg).attr("src","");
					$this.blur();
					var imgSrc = document.selection.createRange().text;
					$(opt.upImg)[0].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod = scale)";
				    $(opt.upImg)[0].filters("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc; 
					$(opt.upImg).attr("src",imgSrc);
					$(opt.upImg).parent().removeClass("Validform_error");
					$(opt.tip).text("");	
				}else{
					/*
					var file=$this[0].files[0];
					var reader = new FileReader();
					var img=document.getElementById(opt.upImg);  
					reader.readAsDataURL(file);
					reader.onload=function(e){  
						//显示文件	
						$(opt.upImg).attr("src",this.result);
						$(opt.tip).text("");
						$(opt.upImg).parent().removeClass("Validform_error");	
					}
					*/
					$(opt.upImg)[0].src = window.URL.createObjectURL($this[0].files[0]);
		            $(opt.tip).text("");
	                $(opt.upImg).removeClass("Validform_error");   
				}
				opt.callback();
			}
			return state;	  
	    });	 
	}
})(jQuery);
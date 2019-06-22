var uploadId = _common.getUrlParam('waybillId') || '';
console.log(uploadId);

//点击添加图片
$('#uploaderRecipt').click(function(){
	if(!localStorage.getItem('bindClickTag')){
		//第一次
		$('.topImg,.upload,.demos-content-padded').hide();
		$('.img_tips').show();
		localStorage.setItem("bindClickTag","1");
	}else{
		chooseOneImage(images.localId,1);
	}
	
})

//点击明白图片要求按钮
$('.imgTipsBtn').on('click',function(){
	$('.img_tips').hide();
	$('.topImg,.upload,.demos-content-padded').show();
	chooseOneImage(images.localId,1);
})


//定义多选后数据路径保存上
//选择图片
function chooseOneImage(arr,len){	
	wx.chooseImage({
		count : len, // 默认9
		sizeType : ['compressed' ], // 可以指定是原图还是压缩图，默认二者都有
		sourceType : [ 'album', 'camera' ], // 可以指定来源是相册还是相机，默认二者都有
		success : function(res) {
			var localIds = res.localIds;
			var imagestr="";
			for(var i=0;i<localIds.length;i++){
				arr.push(localIds[i]);
				imagestr += '<li class="weui-uploader__file z_photo" imgSrc="'+localIds[i]+'">'+
							'<img src="'+localIds[i]+'"/>'+
							'<span class="close-img"></span>';
			}
			$('#uploaderFiles').append(imagestr);
			$("#uploaderRecipt,.upload-tip-text").hide();
			
			//点击删除图片
			$('#uploaderFiles').delegate('.close-img','click',function(event){
				var $parentLi = $(this).parent('li');
				$.confirm({
				  	title: '确认删除图片？',
				  	text: '',
				  	onOK: function () {
				    	var imgSrc = $parentLi.attr('imgSrc');
			          	$parentLi.remove();
			          	
			          	for(var i=0;i<arr.length;i++){
			          		if(imgSrc == arr[i]){
			          			arr.splice(i,1);
			          		}
			          	}
			          	$("#uploaderRecipt").show();
				  	}
				});	
				event.stopPropagation(); //阻止事件冒泡
			})
			//点击放大图片
			$('#uploaderFiles').delegate('.z_photo','click',function(){
				var $src = $(this).attr('imgSrc');
				$('#gallery').show().children('#galleryImg').attr('src',$src);			
			})
			//点击关闭图片
			$('#gallery').click(function(){
				$(this).hide();
			})
		},
		cancel:function(){

		}
	});
}

//点击上传图片
$('#uploadConfirm').click(function(){		
	//获得任务单id
	var barcode = _common.getUrlParam('barcode');
	
	var i=0,len=images.localId.length;	
	if(len == 0){  //若是没有上传图片这个步骤，直接点确定上传，是没有反应的，wx.uploadImage这个方法是不执行的
		$.toast('请上传回单照片','text');
	}else{
		uploadImg();
	}
	function uploadImg(){
		wx.uploadImage({
		    localId: images.localId[i], // 需要上传的图片的本地ID，由chooseImage接口获得
		    success: function (res) {
		        //var serverId = res.serverId; // 返回图片的服务器端ID,是单个id
		        i++;
		        if(i<len){
		        	uploadImg();
		        }else{
					var baseValue = {
						"serverIds"  : res.serverId,
						"barcode"    : barcode
					}
					EasyAjax.ajax_Post_Json({
						url:'/mobile/driver/image/delivery',
						data:JSON.stringify(baseValue)
					},function(res){
						$.toast(res.message,function(){
							window.location.href = '../../view/partener/loadGoods.html';
						});						
					});
	
		        }
		    }
		});
	}
})




$(function(){
	var checktype = _common.getUrlParam('checktype'); 
	
	//初始化数据
	if(checktype){
		getAddrData(checktype);
	}
	
	//编辑
	$('#content').delegate('.edit','click',function(){
		var $parent = $(this).parents('.content-item');
		var $text = $parent.find('p').html();
		$parent.find('p').hide();
		$parent.find('.input-item').val($text).show();
	})
	
	//编辑失去焦点时
	$('#content').delegate('.input-item','blur',function(){
		var $p = $(this).siblings('p');
		$p.html($(this).val()).show();
		$(this).hide();
	})	
	
	//删除
	$('#content').delegate('.delete','click',function(){
		var $this = $(this);
		$.confirm({
			title: '',
			text: '确定删除吗',
			onOK: function () {
				$this.parents('.content-item').remove();
				var len = $('#content').children().length;
				$('#addr-num').html(len);
			}
		});
		
	});
	
	//保存地址
	$('#saveAddr').on('click',function(){
		var addressArr = getInputVal(checktype);
		console.log(addressArr);
		var len = $('#content').children().length;
		if(len){
			//提交数据
			$('.address-load').show();
			saveAddress(addressArr,checktype);
		}else{
			$.toast('暂无地址保存','text');
		}
	})
	
	//重新拍照
	$('#add-address').on('click',function(){
		wx.chooseImage({
			count : 1, // 默认9
			sizeType : ['compressed' ], // 可以指定是原图还是压缩图，默认二者都有
			sourceType : [ 'album', 'camera' ], // 可以指定来源是相册还是相机，默认二者都有
			success : function(res) {
				$('.address-load').show();
				var localIds = res.localIds[0];
				wx.getLocalImgData({
				    localId: localIds, // 图片的localID
				    success: function (res) {
				        var localData = res.localData; // localData是图片的base64数据，可以用img标签显示
				        localData = localData.replace('jgp', 'jpeg');//iOS 系统里面得到的数据，类型为 image/jgp,因此需要替换一下
				        ajaxUploadImg(localData,checktype);
				    }
				});
			}
		});
	})
})



//获取地址数据
function getAddrData(type){
	EasyAjax.ajax_Post_Json({
		url:'mobile/dispatch/recognize/address/'+type
	},function(res){
		//导航显示
		$('.address-bottom').show();
		var result = res.collection;
		var str = '';
		if(result == null || result.length == 0){
			$('.content').html('<p style="text-align:center;font-size:15px;padding:10px 0;">暂无数据。。。</p>');
		}else{
			for(var i=0;i<result.length;i++){
				str += 	'<div class="weui-flex content-item">'+
						  	'<div class="weui-flex__item">'+
						  		'<p>'+result[i]+'</p>'+
						  		'<textarea class="input-item" rows="2">'+result[i]+'</textarea>'+
						  	'</div>'+
						  	'<div class="fixed-width">'+
						  		'<i class="fa fa-pencil edit color-orange" aria-hidden="true"></i>'+
						  		'<i class="fa fa-trash-o delete color-red" aria-hidden="true"></i>'+
						  	'</div>'+
						'</div>';
			}
			$('#addr-num').html(result.length);
			$('#content').html(str);
		}
	});
}

//保存地址
function saveAddress(data,type){
	EasyAjax.ajax_Post_Json({
		url:'mobile/dispatch/save/address',
		data:JSON.stringify(data)
	},function(res){
		$('.address-load').hide();
		$.toast(res.message);
		$('#content').html('');
		$('#addr-num').html('0');
		window.location.href = _common.version('../../view/partener/address.html?type='+type);
	},function(){
		$('.address-load').hide();
	})
}

//图片上传
function ajaxUploadImg(data,type){
	EasyAjax.ajax_Post_Json({
		url:'/mobile/dispatch/base64/image/'+type,
		data:JSON.stringify(data)
	},function(res){
		$('.address-load').hide();
		window.location.href = _common.version('../../view/partener/addressCheck.html?checktype='+type);
	},function(){
		$('.address-load').hide();
	});
}

//获取input的值
function getInputVal(type){
	var arr = [];
	$('.input-item').each(function(index,ele){
		arr.push({
			"address": ele.value,
			"addressType": type
		})
	})
	return arr;
}

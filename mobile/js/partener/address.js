$(function(){
	var $id; //保存当前地址id,判断是新增还是编辑
	var addressType = _common.getUrlParam('type'); //判断保存地址跳过来的时候页面
	//页面初始化，获取数据,先判断是否有cookie值，没有就是第一次进入，还有从别的页面进入时的状态判断
	if(addressType){
		//从别的页面进入，重置一下cookie值
		$.cookie("type",addressType);
		if($.cookie("type") == 2){//常用地址
			showUsedAddr();
			getAddress('',2);	
		}else{ //普通地址
			console.log('普通地址');
			getAddress('',1);
		}
	}else{
		//入口进入，不能把cookie值写死
		if(!$.cookie('type')){
			//首次进入没有点击tab,此时没有cookie值
			$.cookie('type',1)
			getAddress('',1);
		}else if($.cookie('type') == 1){
			//点击了我的地址后刷新
			getAddress('',1);
		}else if($.cookie('type') == 2){
			//点击了常用地址后刷新
			showUsedAddr();
			getAddress('',2);
		}
	}
	console.log('刷新时的cookie:'+$.cookie('type'));
	
	//选择图片或拍照
	$('#add-address').on('click',function(){
		wx.ready(function(){
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
					        ajaxUploadImg(localData,$.cookie('type'));
					    }
					});
					
				}
			});
		});
		
	})
	
	//点击我的地址
	$('#myAddress').on('click',function(){
		$.cookie("type",1);
		console.log('我的地址:'+$.cookie("type"))
		getAddress('',1);
	})
	
	//点击常用地址
	$('#usedAddress').on('click',function(){
		$.cookie("type",2);
		console.log('常用地址:'+$.cookie("type"))
		getAddress('',2);
	})
	
	//点击我的地址删除按钮
	$('#content-item').delegate('.address-delete','click',function(event){
		event.stopPropagation();
		var obj = $(this).parent();
		var checkboxValue = getCheckValue(obj);
		console.log('删除val:'+checkboxValue);
		if(checkboxValue.length == 0){
			$.toast('请先选中地址','text');
		}else{
			$.confirm({
				title: '',
				text: '确定删除吗',
				onOK: function () {
					deleteAddress(checkboxValue);	
				}
			});
		}
	})
	
	//点击常用地址删除按钮
	$('#used-address').delegate('.handle-delete','click',function(){
		var arr = []
		$id = $(this).attr('uid');
		arr.push($id);
		$.confirm({
			title: '',
			text: '确定删除吗',
			onOK: function () {
				deleteAddress(arr);	
			}
		});
	})
	
	
	//点击我的地址编辑按钮
	$('#content-item').delegate('.address-edit','click',function(event){
		event.stopPropagation();
		$id = $(this).attr('uid');
		var $div = $(this).siblings('.address-con').find('.address-show');
		$('.add-edit-head > p').html('编辑地址');
		$('#txtItem').val($div.children('p').html());
		$('#remarkItem').show().val($div.find('.span-remark-con').html());
		$('#addEditAddress').fadeIn(250);
			
	})
	
	//点击常用地址编辑按钮
	$('#used-address').delegate('.handle-edit','click',function(event){
		event.stopPropagation();
		$id = $(this).attr('uid');
		var $inputHtml = $(this).parent().prev().find('p').html();
		$('.add-edit-head > p').html('编辑地址');
		$('#txtItem').val($inputHtml);
		$('#remarkItem').hide();
		$('#addEditAddress').fadeIn(250);	
	})
	
	//点击地址保存按钮(通用)
	$('#addrSave').on('click',function(){
		if($id){
			//此时是编辑
			console.log('编辑地址');
			saveAddress({
				id: $id,
	    		address: $.trim($('#txtItem').val()),
	    		remark: $.trim($('#remarkItem').val())
			})
			$('#addEditAddress').fadeOut(250);
		}else{
			//此时是新建
			console.log('新建地址');	
			console.log('addressType:'+$.cookie("type"));
			if($.trim($('#txtItem').val())){
				addNewAddr({
					addressType: $.cookie("type"),
					address: $.trim($('#txtItem').val()),
		    		remark: $.trim($('#remarkItem').val())
				})	
				$('#addEditAddress').fadeOut(250);
			}else{
				$.toast('请先输入收货地址','text')
			}
		}		
	})
	
	//点击新建地址(通用)
	$('#add-addr').on('click',function(){
		$id = '';
		if($.cookie("type") == 1){
			// 普通地址
			$('.txt-item').val('');
			$('.remark-item').show().val('');
			$('#addEditAddress').fadeIn(250);
		}else{
			//常用地址
			$('.txt-item').val('');
			$('.remark-item').hide();
			$('#addEditAddress').fadeIn(250);
		}
		
	})
	
	/*搜索功能部分*/
	//搜索功能-点击搜索图标
	$('.address-search').on('click',function(){
		$(this).parent().hide();
		$(this).parent().next().show();
		//出现搜索结果标签
		$('.content').addClass('show-search-list');
	})
	
	//搜索功能-点击返回icon
	$('#search-cancel').on('click',function(){
		$(this).parents('.search-box').hide();
		$(this).parents('.search-box').prev().show();
		//隐藏搜索结果标签
		$('.content').removeClass('show-search-list');

		getAddress('',1);
	})
	
	//搜索功能-点击搜索按钮
	$('#search-btn').on('click',function(){
		var $val = $.trim($('#searchData').val());
		if($val){
			getAddress($val,1);
		}else{
			$.toast('请先输入搜索内容','text')
		}
	})
	
	//搜索功能-enter键
	$('#searchData').keyup(function(ev){
		var $val = $.trim($('#searchData').val());
		if(ev.keyCode == 13){
			if($val){
				getAddress($val,1);
			}else{
				$.toast('请先输入搜索内容','text')
			}
		}
	})
	
	//搜索功能-点击删除icon
	$('#searchClear').on('click',function(){
		$(this).prev().val('');
		getAddress('',1);
	})
	
	//点击更改包裹数
	$('#used-address').delegate('.minus','click',function(){
		changeBaoNum(0,this);
	})
	$('#used-address').delegate('.plus','click',function(){
		changeBaoNum(1,this);
	})
	
	//点击分享功能
	$('#address-share').on('click',function(){
		var checkVal = getCheckValue();
		if(checkVal.length == 0){
			$.toast('请先选中需要分享的地址','text')
		}else{
			shareAddress(checkVal.join(','));
			$("#weixin-tip").show();
			setTimeout(function(){
				$("#weixin-tip").hide();
			},3000)
			$("#weixin-tip").click(function(){
				$("#weixin-tip").hide();
			})
		}
	})
	
	//不点击分享按钮，直接点分享
	wx.ready(function(){
		shareAddress();
	});
	
	//新建编辑框-点击取消隐藏
	$('#addrCancel').on('click',function(){
		$('#addEditAddress').fadeOut(250);
	})
	
	//点击我的地址中日期选中地址
	$('#content-item').delegate('.title-con','click',function(){
		var curCheck = $(this).find('input[type=checkbox]');
		var $label = $(this).parent().siblings('.label-address');
		var $checkbox = $label.find('input[type=checkbox]');
		var currentI = $(this).find('.checkedbox');
		var $i = $label.find('.checkedbox');
		
		curCheck[0].checked = !curCheck[0].checked;
		
		if(curCheck.is(':checked')){
			$checkbox.prop('checked',true);
			currentI.removeClass('fa-square-o').addClass('fa-check-square');
			$i.removeClass('fa-square-o').addClass('fa-check-square');
		}else{
			$checkbox.prop('checked',false);
			currentI.removeClass('fa-check-square').addClass('fa-square-o');
			$i.removeClass('fa-check-square').addClass('fa-square-o');
		}
		shareAddress();
	})
	
	$('#content-item').delegate('.address-con','click',function(){
		var currCheck = $(this).find('input[type=checkbox]');
		var title = $(this).parent().siblings('.label-title');
		var titleCheck = title.find('input[type=checkbox]')[0];
		var curI = $(this).find('.checkedbox');
		var titleI = title.find('.checkedbox');

		currCheck[0].checked = !currCheck[0].checked;
		
		if(!currCheck.is(':checked')){
			//有一个没选中
			titleCheck.checked = false;
			curI.removeClass('fa-check-square').addClass('fa-square-o'); //当前灰
			titleI.removeClass('fa-check-square').addClass('fa-square-o'); //全选灰
		}else{
			curI.removeClass('fa-square-o').addClass('fa-check-square'); //当前蓝
		}
		shareAddress();
	})
	
	//常用地址页选中样式
	$('#used-address').delegate('.used-label-address','click',function(event){
		var currCheck = $(this).find('input[type=checkbox]');
		var curI = $(this).find('.checkedbox');
		currCheck[0].checked = !currCheck[0].checked;
		if(!currCheck.is(':checked')){
			curI.removeClass('fa-check-square').addClass('fa-square-o');
		}else{
			curI.removeClass('fa-square-o').addClass('fa-check-square');
		}
		shareAddress();
	})
});

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

//获取数据
function getAddress(searchData,type){
	if(!searchData || searchData == null){ searchData = ""; }
	EasyAjax.ajax_Post_Json({
		url:'mobile/dispatch/list',
		data:JSON.stringify({'type': type, 'bodyData': searchData })
	},function(res){
		var result = res.address;
		if(type == 1){
			//普通地址查询
			var len = 0;
			$('.content-item').html(''); //每次重新获取下数据，都要先将内容清空
			if(result !== null || result.length !== 0){
				for(var item in result){
					len += result[item].length;
					/*console.log('len里面：'+len);
					console.log('日期：'+result[item]);*/
					var addressTitle = 	'<div class="weui-cell weui-check__label label-title" >'+
					  						'<div class="label-con label-flex title-con">'+
						  						'<div class="weui-cell__hd">'+
						  							'<input type="checkbox" class="weui-check" name="checkbox1">'+
						  							'<i class="fa fa-square-o checkedbox"></i>'+
						  						'</div>'+
						  						'<div class="weui-cell__bd"><p>'+item+'</p></div>'+
					  						'</div>'+
					  						'<div class="address-delete">'+
					  							'<i class="fa fa-trash-o"></i>'+
					  						'</div>'+
					  					'</div>';
					var addressData = $('#addressData').render(result[item]);
					var addressHtml = '<div class="weui-cells">'+addressTitle+addressData+'</div>';
					$('#content-item').append(addressHtml);			
				}
				/*console.log('len:'+len);*/
				$('#address-num').html(len);
				//根据class判断是否显示搜索结果p标签
				if($('.content').hasClass('show-search-list')){
					$('.search-list').show().find('span').html(len);
				}else{
					$('.search-list').hide();
				}	
			}
		}else{
			//常用地址查询
			$('#used-address').html('');
			if(result === null || result.length === 0){
				$('#used-address').html('<p style="text-align:center;color:#999;">暂无常用地址，请添加</p>');
			}else{
				for(var item in result){
					var uesedAddrHtml = $('#usedAddr').render(result[item]);
					$('#used-address').append(uesedAddrHtml);
				}
			}
		}
		
		//点击跳转到地图规划页面
		$('#mapLink').on('click',function(){
			var allVal = getCheckValue();
			console.log('选中的值：'+getCheckValue())
			var len = 0;
			if($.cookie('type') == 2){
				//常用地址
				len = $('#used-address').children().length;
			}else{
				len = $('.content-item').children().length;
			}
			if(len){
				window.location.href = _common.version('../../view/partener/addrMap.html?val='+allVal+"&type="+type);
			}else{
				$.toast('请先增加地址','text');
			}	
		})
		
	});
}

//删除地址接口
function deleteAddress(data){
	var baseValue = data;
	EasyAjax.ajax_Post_Json({
		url:'mobile/dispatch/delete',
		data:JSON.stringify(baseValue)
	},function(res){
		$.toast(res.message);
		window.location.reload();
	});
}

//修改单个地址接口
function saveAddress(options){
	var baseValue = {
	    id: options.id,
	    address: options.address,
	    remark: options.remark
	};
	EasyAjax.ajax_Post_Json({
		url:'mobile/dispatch/update/single',
		data:JSON.stringify(baseValue)
	},function(res){
		$.toast(res.message);
		window.location.reload();
	});
}

//新建地址接口
function addNewAddr(options){
	var baseValue = {
		addressType: options.addressType,
	    address:     options.address,
	    remark:      options.remark
	};
	EasyAjax.ajax_Post_Json({
		url:'mobile/dispatch/save/single',
		data:JSON.stringify(options)
	},function(res){
		$.toast(res.message);
		window.location.reload();
	});
}

//更改包裹数量
function changeBaoNum(status,obj){
	var id = $(obj).parent().attr('uid');
	var count = $(obj).siblings('.changeVal').val() - 0;
	if(status === 0){ //减
		count --;
		if(count<0){
			count = 0;
			return false;
		}
		console.log('减：'+count);
	}else{
		count ++;
		console.log('加：'+count);
	}
	//将值赋给input
	$(obj).siblings('.changeVal').val(count);
	EasyAjax.ajax_Post_Json({
		url:'mobile/dispatch/package/count',
		data:JSON.stringify({
								id:    id,
							    count: count
							})
	},function(res){
		//$.toast(res.message);
		//window.location.reload();
	});
}

//获取所有被选中的checkbox的value值
function getCheckValue(obj){
	clearTabVal();
	var arr = [];
	var $checkedEle;
	if(obj){
		$checkedEle = $(obj).siblings('.label-address').find('input[type=checkbox]');
	}else{
		$checkedEle = $('#addr-navBar-hook').find('input[name=checkbox2]');
	}
	
	$checkedEle.each(function(index,ele){
		if($(this).is(':checked')){
			arr.push($(this).val());
		}
	})
	console.log(arr.join(','));
	return arr;
}

//显示常用地址样式
function showUsedAddr(){
	$('#usedAddress').addClass('weui-bar__item--on').siblings('#myAddress').removeClass('weui-bar__item--on');
	$('#addr-tab2').addClass('weui-tab__bd-item--active').siblings('#addr-tab1').removeClass('weui-tab__bd-item--active');
}

//清空tab中的value值
function clearTabVal(){
	if($.cookie('type') == 1){
		//清空常用地址value
		$('#used-address input[type=checkbox]').each(function(index,el){
			el.checked = false;
		})
	}else{
		//清空普通地址value
		$('#content-item input[type=checkbox]').each(function(index,el){
			el.checked = false;
		})
	}
}

//分享功能
function shareAddress(ids){
	//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPath = curWwwPath.substring(0, pos);
    //获取带"/"的项目名，如：/uimcardprj
    //var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	
	if(!ids){
		ids = getCheckValue().join(',');
	}
	
	//分享给朋友
	wx.onMenuShareAppMessage({
		title : '任务分享', //分享标题
		desc : '任务摘要：派送地址分享', // 分享描述
		link : localhostPath+'/mobile/wechat/share/address?keys='+ids, //分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
		imgUrl : localhostPath+'/mobile/images/logo.png', // 分享图标
		type : 'link', // 分享类型,music、video或link，不填默认为link
//		dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
		success : function() {
			// 用户确认分享后执行的回调函数
			$.toast('分享成功','info');
		},
		cancel : function() {
			// 用户取消分享后执行的回调函数
		}
	});
	
	//分享给朋友圈
	wx.onMenuShareTimeline({
        title: '任务分享', // 分享标题
	    link: localhostPath+'/mobile/wechat/share/address?keys='+ids, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
	    imgUrl: localhostPath+'/mobile/images/logo.png', // 分享图标
	    success: function () { 
	        // 用户确认分享后执行的回调函数
	        $.toast('分享成功','info');
	    },
	    cancel: function () { 
	        // 用户取消分享后执行的回调函数
	    }
    });

}
		


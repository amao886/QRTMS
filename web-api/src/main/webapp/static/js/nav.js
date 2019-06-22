$(function(){
	//列表
	pageWHeight();
	wrapWidth();
	$(window).resize(function(){
		pageWHeight();
		wrapWidth();
	});

	
	//切换页面
	/*
	$('.menu_a').click(function(){
		$('#iframe-main').attr('src',$(this).attr('link'));
		$('.menu_a').parent().removeClass('active');
		$(this).parent().addClass('active');
	})
	*/
	
	//切换菜单
	$('.submenu>a').click(function(){
		var $li = $(this).parent('li');
		var $ul = $(this).siblings('ul');
		var allLi = $('#navigation .submenu');
		var allUl = $('#navigation .submenu > ul');
		if($li.hasClass('open')){
			$ul.slideUp(400);
			$li.removeClass('open');
			$(this).children('.after-icon').css('background','pink');
		}else{
			allUl.slideUp(400);
			$ul.slideDown(400);
			allLi.removeClass('open');
			$li.addClass('open');
			$(this).children('.after-icon').css('background','yellow');			
		}
	})
	
	//跟踪页面，点击添加备注，弹出框
	$('.track-content .add-info').on('click',modelShow);
	
	//客户信息页面，点击按钮，出弹出框
	$('.custom-content .add-edit').on('click',function(){
		modelShow();
		divCenter('model-box');

	});
	$('.custom-content .add-info').on('click',modelShow);
	
	//点击X关闭弹框
	$(".model-box .close").on('click',modelClose);
	
	//点击空白处关闭弹框
	$('.model').on('click',function(event){
		var $target = $(event.target);
		if($target.is('.model')){
			modelClose();
		}
	})

	//form表单宽

  
})

function pageWHeight(){
    var Wdocument=$(document).width();
    var Hdocument=$(document).height()-80;
    $('#navigation').height(Hdocument);
}
function modelClose(){
	$('.model').css('display','none');

}
function modelShow(){
	$('.model').css('display','block');
}
function wrapWidth(){
	if($("#myDataFrom").length==0){
		var num = $('.content-search .labe_l').width();
	    //console.log(num);
		var min = $('.col-min').outerWidth()-num;
		var middle = $('.col-middle').outerWidth()-num;
		var max = $('.col-max').outerWidth()-num;
		$('.col-min .tex_t').css('width',min);
		$('.col-middle .tex_t').css('width',middle);
		$('.col-max .tex_t').css('width',max);
	}   
}
function divCenter(obj){
    var obj = $('.'+obj);
    var width = obj.outerWidth();
    var height = obj.outerHeight(true);
    //console.log(height);
    obj.css({
        'position':'absolute',
        'top':'50%',
        'left':'50%',
        'margin-top':-(height/2),
        'margin-left':-(width/2)
    })
}


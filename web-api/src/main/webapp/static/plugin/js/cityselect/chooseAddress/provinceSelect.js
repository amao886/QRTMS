// JavaScript Document
$(document).ready(function(){
	 $(".dizhi_city").click(function(){
		 $(this).siblings().find(".sheng").click();
	 });
    function AdrInputPopDiv(){
	    this.init=function(inpuId) {
			var dizhiDiv = null;
			var initInput = null;
			$(".manage-wrapper").append($(provinceObjectDiv).html());
			var dizhiDiv = $(".manage-wrapper").children(".dizhi_t");
			$(".dizhi_city").bind("click",function(){
					var flag = $(this).siblings(".dizhi_t").css("display");
					if (flag == "none") {
						$(".dizhi_t").hide();
						$(this).siblings(".dizhi_t").css("display", "block");
						/*$(".dizhi_t").mouseleave(function(){
							 $(this).fadeOut();
						});*/ 
					}
					else {
						$(this).siblings(".dizhi_t").css("display", "none"); 
						var arr = $(this).val().split("-");
						/*if(arr.length <3){					
							$(this).val("请选择城市");
						}*/	 
					}
					initInput = $(this);
			});
			
			$(document).click(function(){
				$(".dizhi_t").hide();
			});
			
			$('.dizhi_city,.dizhi_t').bind('click',function(e){ 
				e.stopPropagation();
			}); 
			
			
			dizhiDiv.on('click', '.dizhi_t_1 p', function(e){
				  var idx = $(this).attr("idx");
				  $(this).parent().find("p").removeClass("hover");
				  dizhiDiv.find(".province").css("display", "none");
				  dizhiDiv.find(".city").css("display", "none");
				  dizhiDiv.find(".area").css("display", "none");
				  if (idx == 1){
					$(this).addClass("hover");
					dizhiDiv.find(".province").css("display", "block");
				  }else if(idx == 2){
					$(this).addClass("hover");
					dizhiDiv.find(".city").css("display", "block");
				  }else if(idx == 3){
					$(this).addClass("hover");
					dizhiDiv.find(".area").css("display", "block");
				  }else if(idx == 4){
					 $(this).addClass("hover");
					 //initInput.val("请选择城市");
					 // initInput.attr("请选择城市");
					  initInput.trigger("click");
					  dizhiDiv.find('.dizhi_t_1 p[idx=1]').trigger("click");
				  }
			});
			
			var selPro = "";
			var selCity = "";
			var proFullName = {"北京":"北京市", "天津":"天津市", "内蒙古":"内蒙古自治区", "上海":"上海市"
			  , "广西":"广西壮族自治区", "重庆":"重庆市", "西藏":"西藏自治区", "宁夏":"宁夏回族自治区"
			  , "新疆":"新疆维吾尔自治区", "香港":"香港特别行政区", "澳门":"澳门特别行政区"};
			dizhiDiv.on('click', '.province .dizhi_t_2_2 p', function(e){
				  var pro = $(this).text();
				  if (proFullName[pro]){
					pro = proFullName[pro];
				  }else{
					pro += "省";
				  }
				  selPro = pro;
				  var city = "";
				  $.each(provinceObject[pro], function(v){
					city += "<p style='width:auto;padding-right: 6px;'>" + v + "</p>";
				  });
				  var cityDiv = dizhiDiv.find(".city .dizhi_t_2 .dizhi_t_2_2");
				  cityDiv.html("");
				  $(city).appendTo(cityDiv);
				  dizhiDiv.find('.dizhi_t_1 p[idx=2]').trigger("click");
				  initInput.val(selPro);
				  initInput.attr("title", initInput.val());
				  /*
				  if($(".dizhi_t_2").is(":hidden")){
					  $(".dizhi_city").val("请选择城市");
				  }
				  */

			});
			
			dizhiDiv.on('click', '.city .dizhi_t_2_2 p', function(e){
				  var city = $(this).text();
				  selCity = city;
				  var area = "";
				  $.each(provinceObject[selPro][city], function(idx, v){
					 area += "<p style='width:auto;padding-right: 6px;'>" + v + "</p>";
				  });
				  var areaDiv = dizhiDiv.find(".area .dizhi_t_2 .dizhi_t_2_2");
				  areaDiv.html("");
				  $(area).appendTo(areaDiv);
				  if(selCity !="全市"){
					  dizhiDiv.find('.dizhi_t_1 p[idx=3]').trigger("click");  
				  }else{
					  initInput.trigger("click");
				  }
				  
				  initInput.val(selPro + "-" + selCity);
				  initInput.attr("title", initInput.val());
			});
			
			dizhiDiv.on('click', '.area .dizhi_t_2_2 p', function(e){
				  var area = $(this).text();
				  initInput.val(selPro + "-" + selCity + "-" + area);
				  initInput.attr("title", initInput.val());
				  initInput.trigger("click");
			});
			$(".wrapper").off("keypress", ".dizhi_city_detail").on('keypress', ".dizhi_city_detail" , function(e){
				  var allowedReg = new RegExp('[,]');
				  var charCode = typeof e.charCode != 'undefined' ? e.charCode : e.keyCode; 
				  var keyChar = String.fromCharCode(charCode);
				  if(allowedReg.test(keyChar)){
					e.preventDefault();
					return;
				  };
			}).off("blur", ".dizhi_city_detail").on('blur', ".dizhi_city_detail", function(){
				  var val = $(this).val();
				  if (val.indexOf(",") != -1){
					$(this).val(val.replace(/,/g,''));
				  }
			});
	  };
  };
  new AdrInputPopDiv().init("",true);
});

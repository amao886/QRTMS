var province=$("#province"),city=$("#city"),town=$("#district"),cityFlag=false;
for(var i=0;i<provinceList.length;i++){	
    addEle(province,provinceList[i].name);
    cityFlag = true;
}
function addEle(ele,value){
    var optionStr="";
    optionStr="<option value="+value+">"+value+"</option>";
    ele.append(optionStr);
}
function removeEle(ele,str){
    ele.find("option").remove();
    var optionStar="<option value=''>"+"--请选"+str+"--"+"</option>";
    ele.append(optionStar);
}
var provinceText,cityText,cityItem;
province.on("change",function(){
	selectName('province');
});
city.on("change",function(){
	selectName('city');
});
var cityItem;
function selectName(status,objTarger){	
	var provinceText = objTarger.val();
	$.each(provinceList,function(i,item){
        if(provinceText == item.name){
            cityItem = i;
            return cityItem;
        }
    });
	if(status === 'province'){ //省
		removeEle(city,'城市');
    	removeEle(town,'区县');
	    $.each(provinceList[cityItem].childs,function(i,item){
	        addEle(city,item.name)
	    })
	}else if(status === 'city'){  //市
		var cityText = objTarger.val();
		removeEle(town,'区县');
		$.each(provinceList[cityItem].childs,function(i,item){
	        if(cityText == item.name){
	            for(var n=0;n<item.childs.length;n++){
	                addEle(town,item.childs[n].name)
	            }
	        }
	    });
	}
	
}

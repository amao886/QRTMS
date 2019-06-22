var buildSelect = function (_province, _city, _district) {
    var obj = {
        province: _province,
        city: _city,
        district: _district,
        cityFlag:false
    };
    obj.addEle = function(ele, value){
        var optionStr="";
        optionStr="<option value="+value+">"+value+"</option>";
        ele.append(optionStr);
    }
    obj.removeEle = function (ele, str){
        ele.find("option").remove();
        var optionStar="<option value=''>"+"--请选"+str+"--"+"</option>";
        ele.append(optionStar);
    }
    obj.init= function(){
        for(var i=0;i<provinceList.length;i++){
            obj.addEle(obj.province, provinceList[i].name);
            obj.cityFlag = true;
        }
        obj.province.on("change",function(){
            obj.selectName('province', $(this));
        });
        obj.city.on("change",function(){
            obj.selectName('city', $(this));
        });
    }
    obj.setValue= function(p, c, d){
        obj.province.val(p);
        obj.selectName('province', $(obj.province));

        obj.city.val(c);
        obj.selectName('city', $(obj.city));

        if(obj.district){
            obj.district.val(d);
        }
    }
    obj.selectName = function(status, objTarger){
        var provinceText = objTarger.val(), index = 0;
        $.each(provinceList, function(i,item){
            if(provinceText == item.name){
                index = i;
                return index;
            }
        });
        if(status === 'province'){ //省
            obj.removeEle(obj.city,'城市');
            if(obj.district){
                obj.removeEle(obj.district,'区县');
            }
            $.each(provinceList[index].childs,function(i,item){
                obj.addEle(obj.city, item.name)
            })
        }else if(status === 'city'){  //市
            var cityText = objTarger.val();
            if(obj.district){
                obj.removeEle(obj.district,'区县');
                $.each(provinceList[index].childs,function(i,item){
                    if(cityText == item.name){
                        for(var n=0;n<item.childs.length;n++){
                            obj.addEle(obj.district, item.childs[n].name)
                        }
                    }
                });
            }
        }

    }
    return obj;
}


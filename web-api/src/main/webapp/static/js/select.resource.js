(function($){
    if($.ex == null){ $.ex = {}; }
    $.ex.util ={
        createTable : function(cells){
            var table = $("<table class=\"dtable\"></table>");
            var tr = $("<tr></tr>");
            for(var j = 0; j < cells.length; j++) {
                var cell = cells[j];
                if(cell.w){
                    tr.append($("<th width='"+ cell.w +"'>"+ cell.n +"</th>"));
                }else{
                    tr.append($("<th>"+ cell.n +"</th>"));
                }
            }
            //table.css({'border-collapse':'collapse','width':'100%','text-align':'center'});
            return table.append($("<thead></thead>").append(tr)).append("<tbody></tbody>");
        },
        changeBackgroundTr : function(selecter){
            selecter.removeClass('.default').addClass('.select').siblings().removeClass('.select').addClass('.default');
        },
        createSearch :function(inputs, buttons){
            var form = $('<form class="form-inline"></form>');
            for(var i = 0; i < inputs.length; i++){
                form.append("<div class='form-group'><input type='text' class='form-control' name='"+ inputs[i].name +"' placeholder='"+ inputs[i].text  +"'></div>");
            }
            if(buttons && buttons.length > 0){
                form.append("<button class='btn btn-default search' type='button'>查询</button>");
                for(var i = 0; i < buttons.length; i++){
                    form.append("<button class='btn btn-default "+ buttons[i].class +"' type='button' style='float:right; margin-left: 5px;'>"+ buttons[i].text +"</button>");
                }
            }else{
                form.append("<button class='btn btn-default search' type='button' style='float:right'>查询</button>");
            }
            return form;
        },
        createPage : function(content, pages){
            content.find('.sr-page').html('');
            var startRow = Math.max(1, pages.pageNum - 3);
            var endRow = Math.min(pages.pageNum + 3, pages.pages);
            var ul = $("<ul class=\"pagination pagination-sm\" style='margin: 0'></ul>");
            if(startRow > 0 && endRow > startRow) {
                for (var p = startRow; p <= endRow; p++) {
                    if(p == pages.pageNum) {
                        ul.append("<li class=\"active\"><span>"+ p +"</span></li>");
                    }else {
                        ul.append("<li><a href=\"javascript:;\" num=\""+ p +"\">"+ p +"</a></li>");
                    }
                }
                content.find('.sr-page').append($("<div></div>").append(ul));
            }
        },
        load : function(panel, settins, url, parmas, buildCallback, selectCallback, completeCallback){
            var body = panel.$body, content = panel.$content;
            $.util.json(base_url + url, parmas, function (data) {
                var tbody = content.find('tbody'), thead = content.find('thead');
                tbody.html('');
                if (!data.success) {
                    tbody.append("<tr><td colspan='"+ thead.find("th").length +"'  class='colspan'>"+ data.message +"</td></tr>");
                    return;
                }
                if(data.result.collection && data.result.collection.length > 0){
                    settins.dataMap = new Map(); // 空Map m.set('Adam', 67);
                    $.each(data.result.collection, function (i, item) {
                        if(buildCallback){
                            tbody.append(buildCallback.call(panel, item));
                        }
                    });
                    tbody.find("tr a").on("click", function () {
                        var tr = $(this).parents("tr");
                        settins.selectKey = Number(tr.attr("id"));
                        if(settins.dataMap && settins.dataMap.has(settins.selectKey)){
                            if(selectCallback){
                                var item = settins.dataMap.get(settins.selectKey);
                                try{
                                    $.util.json(base_url + '/backstage/modify/hotspot', {'type':settins.hotspotType, 'key': item.hotspotKey});
                                } catch (Exception){}
                                selectCallback.call(settins.target, settins.dataMap.get(settins.selectKey));
                            }
                            if(settins.selectClose){
                                panel.close();
                            }else{
                                $.ex.util.changeBackgroundTr(tr);
                            }
                        }
                    });
                    $.ex.util.createPage(content, data.result);
                }else{
                    var message = '暂无数据';
                    if(settins.emptyMessage){
                        message = settins.emptyMessage;
                    }
                    tbody.append("<tr><td class='colspan' colspan='"+ thead.find("th").length +"'>"+ message +"</td></tr>");
                }
                if(!settins.boxWidth || settins.boxWidth <= 0){
                    panel.setBoxWidth( Math.min(700, content.find("table").outerWidth(true)) + 35 + 'px' ) ;
                }
                content.find('.sr-page a').on('click', function(){
                    settins.pageNum = Number($(this).attr("num"));
                    content.find('.sr-search').find('.search').trigger('click');
                });
                if(completeCallback){
                    completeCallback.call(panel, content);
                }
            });
        },
        modifyHotspot : function(type, key){
            $.util.json(base_url + '/backstage/modify/hotspot', {'type':type, 'key':key});
        }
    };
    $.fn.extend({
        /**
         * 选择用户信息
         * @param option (selectKey:已经选择的用户ID, selectFun:为选择一项触发的事件,当前选择的用户信息为参数)
         *  {
         *       "ownerKey": 当前用户ID,
         *       "remarkName": 好友备注名称,
         *       "remarkMobile": 备注手机号,
         *       "company": 备注公司,
         *       "remark": 备注,
         *       "type": 好友类型,
         *       "friendKey": 好友的用户ID,
         *       "mobile": 好友手机号,
         *       "userName": 好友名称,
         *       "createtime": 好友注册日期
         *  }
         * @returns {selectUser}
         */
        selectUser:function(option){
            var target = this;
            var options = {'title':'选择指派对象-请从好友列表中选择指派对象', 'useBootstrap': false, 'type':'blue', 'closeIcon':true, 'animation':'scale', 'closeAnimation':'scale' };
            var setting = $.extend(options, option);
            setting.hotspotType = 1;
            setting.target = this;
            setting['onOpenBefore'] = function () {
                var self = this, editBody = this.$body, content = this.$content;
                content.find('.sr-search').append($.ex.util.createSearch([{name:'likeString', text:'姓名/手机号'}],[{class:'add-friend', text:'添加好友'}]));
                content.find('.sr-table').append($.ex.util.createTable([{n:"姓名", w:150}, {n:"手机号", w:100}, {n:"是否注册",w:80}, {n:"备注公司", w:150}, {n:"操作", w:50}]));
                setting.emptyMessage = '暂未添加好友';
                content.find('.sr-search').find('.search').on('click', function(){
                    var parmas = content.find('form').serializeObject();
                    var _num = 1;
                    if(setting.pageNum){ _num = setting.pageNum; }
                    $.ex.util.load(self, setting, "/backstage/friend/select", $.extend(parmas, {size:10, num: _num}), function(item){
                        item['hotspotKey'] = item.fkey;
                        setting.dataMap.set(item.friendKey, item);
                        //0:普通 1：货主 2：承运商 3：司机
                        var ftype = "<span class='role-classify'></span>";
                        if(item.type == 1){
                            ftype = "<span class='role-classify owner'>货</span>";
                        }
                        if(item.type == 2){
                            ftype = "<span class='role-classify carrier'>承</span>";
                        }
                        if(item.type == 3){
                            ftype = "<span class='role-classify driver'>司</span>";
                        }
                        var tr = null;
                        if(item.friendKey && item.friendKey > 0){
                            tr = $("<tr id='"+ item.friendKey +"'><td>"+ ftype +"<span>"+ item.remarkName + "<span></td><td>" + item.mobile + "</td><td>已注册</td><td>" + item.company + "</td><td><a class='select-btn' href='javascript:;'>选择</a></td></tr>");
                        }else{
                            tr = $("<tr class='disabled'><td>"+ ftype +"<span>"+ item.remarkName + "<span></td><td>" + item.mobile + "</td><td>未注册</td><td>" + item.company + "</td><td><a class='select-btn' href='javascript:;'>选择</a></td></tr>");
                        }
                        if(setting.selectKey && setting.selectKey - item.friendKey == 0){
                            $.ex.util.changeBackgroundTr(tr);
                        }
                        return tr;
                    }, setting.selectFun);
                });
                content.find('.sr-search').find('.add-friend').on('click', function(){
                    var htmls = [];
                    htmls.push("<div class=\"model-from edit_customer\" style=\"width: 470px;\">");
                        htmls.push("<input name=\"id\" type=\"hidden\">");
                        htmls.push("<div class=\"model-form-field\"><div class=\"model-form-field\"><label for=\"\">姓名 :</label><input name=\"fullName\" type=\"text\" min=\"0\" max=\"50\"></div>");
                        htmls.push("<div class=\"model-form-field\"><label for=\"\">手机号 :</label><input name=\"mobilePhone\" type=\"text\" min=\"0\" max=\"11\" id=\"mobilePhone\"></div>");
                        htmls.push("<div class=\"model-form-field\"><label for=\"\">所属公司 :</label><input name=\"company\" type=\"text\" min=\"0\" max=\"100\"></div>");
                        htmls.push("<div class=\"model-form-field\">");
                            htmls.push("<label for=\"\">好友类型 :</label>");
                            htmls.push("<div class=\"inline_block\">");
                                htmls.push("<label class=\"label-style\"><input type=\"radio\" name=\"friendsType\" value=\"1\">货主</label>");
                                htmls.push("<label class=\"label-style\"><input type=\"radio\" name=\"friendsType\" value=\"2\">承运商</label>");
                                htmls.push("<label class=\"label-style\"><input type=\"radio\" name=\"friendsType\" value=\"3\">司机</label>");
                            htmls.push("</div>");
                        htmls.push("</div>");
                    htmls.push("</div>");
                    $.util.form('新增好友', htmls.join(''), function(){
                        var editBody = this.$body;
                        //给label添加点击事件
                        editBody.find($('.label-style')).on('click', function () {
                            $(this).addClass('active').siblings().removeClass('active');
                        })
                    }, function(){
                        var parmas = {}, editBody = this.$body;
                        parmas.fullName = editBody.find("input[name='fullName']").val();
                        parmas.mobilePhone = editBody.find("input[name='mobilePhone']").val();
                        parmas.company = editBody.find("input[name='company']").val();
                        parmas.friendsType = editBody.find("input[name='friendsType']:checked").val()
                        if (parmas.fullName == "" || parmas.fullName == null) {
                            $.util.error("好友姓名不能为空");
                            return false;
                        }
                        if (parmas.mobilePhone == "" || parmas.mobilePhone == null) {
                            $.util.error("好友手机号码不能为空");
                            return false;
                        } else if (!(/^1[\d]{10}$/.test(parmas.mobilePhone))) {
                            $.util.error("好友手机号码格式错误");
                            return false;
                        }
                        $.util.json(base_url + '/backstage/friend/addFriends/', parmas, function (data) {
                            if (data.success) {
                                $.util.alert('操作提示', data.message, function () {
                                    content.find('.sr-search').find('.search')[0].click();
                                });
                            } else {
                                $.util.error(data.message);
                            }
                        });
                    });
                });
                content.find('.sr-search').find('.search').trigger('click');
            };
            setting['content']="<div class='sr-search'></div><div class='sr-table''></div><div>温馨提示：运单不能指派给未注册的好友</div></div><div class='sr-page'></div>";
            setting['panel'] = $.dialog(setting);
            return this;
        },
        /**
         * 选择常用地址
         * @param option (selectType:为地址类型[1:收货地址，2:发货地址], selectFun:为选择一项触发的事件,当前选择的地址信息为参数)
         * @returns {selectCustomer}
         */
        selectCustomer:function(option){
            var target = this;
            var options = {'title':'选择客户', 'useBootstrap': false, 'type':'blue', 'closeIcon':true, 'animation':'scale', 'closeAnimation':'scale', 'boxWidth':820 };
            var setting = $.extend(options, option);
            if(!setting.selectType || setting.selectType <= 0){
                setting.selectType = 1;
            }
            setting.hotspotType = 2;
            setting.target = this;
            var selectType = setting.selectType;//地址类型，1：收货地址，2：发货地址
            var selectGroup = setting.selectGroup;//项目组ID
            setting['onOpenBefore'] = function () {
                var self = this, editBody = this.$body, content = this.$content;
                content.find('.sr-search').append($.ex.util.createSearch([{name:'companyName', text:'名称'},{name:'contacts', text:'联系人'}]));
                content.find('.sr-table').append($.ex.util.createTable([{n:"货主名称", w:200}, {n:"联系人", w:150}, {n:"发货地区", w:150}, {n:"详细地址", w:250}, {n:"操作", w:50}]));
                content.find('.sr-search').find('.search').on('click', function(){
                    var parmas = content.find('form').serializeObject();
                    var _num = 1;
                    if(setting.pageNum){ _num = setting.pageNum; }
                    setting.emptyMessage = '没有添加常用地址数据';
                    $.ex.util.load(self, setting, "/backstage/customer/select", $.extend(parmas, {size:5, num: _num, type: selectType, groupId:selectGroup}), function(item){
                        item['hotspotKey'] = item.id;
                        setting.dataMap.set(item.id, item);
                        item.region = item.province;
                        if(item.city){ item.region += item.city; }
                        if(item.district){ item.region += item.district; }
                        if(item.region == null || item.region == 'null'){
                            item.region = '';
                        }
                        var tr = $("<tr id='"+ item.id +"'><td>" + item.companyName + "</td><td>" + item.contacts + "</td><td>" + item.region + "</td><td>" + item.address + "</td><td><a class='select-btn' href='javascript:;'>选择</a></td></tr>");
                        if(setting.selectKey && setting.selectKey - item.id == 0){
                            $.ex.util.changeBackgroundTr(tr);
                        }
                        return tr;
                    }, setting.selectFun);
                });
                content.find('.sr-search').find('.search').trigger('click');
            };
            setting['content']="<div class='sr-search'></div><div class='sr-table''></div><div class='sr-page'></div>";
            setting['panel'] = $.dialog(setting);
            return this;
        },
        selectRoute:function(option){
            var target = this;
            var options = {'title':'选择路由', 'useBootstrap': false, 'type':'blue', 'closeIcon':true, 'animation':'scale', 'closeAnimation':'scale', 'boxWidth':700 };
            var setting = $.extend(options, option);
            setting.hotspotType = 3;
            setting.target = this;
            setting['onOpenBefore'] = function () {
                var self = this, editBody = this.$body, content = this.$content;
                content.find('.sr-search').append($.ex.util.createSearch([{name:'routeName', text:'路由名称'}],[{class:'add-route', text:'新增路由'}]));
                content.find('.sr-table').append($.ex.util.createTable([{n:"路由名称"}, {n:"路由路线"},{n:"操作", w:60}]));
                content.find('.sr-search').find('.search').on('click', function(){
                    var parmas = content.find('form').serializeObject();
                    var _num = 1;
                    if(setting.pageNum){ _num = setting.pageNum; }
                    setting.emptyMessage = '没有添加路由数据';
                    $.ex.util.load(self, setting, "/backstage/route/select", $.extend(parmas, {size:5, num: _num}), function(item){
                        item['hotspotKey'] = item.id;
                        setting.dataMap.set(item.id, item);
                        var line = "";
                        if(item.routeLines){
                            $.each(item.routeLines, function(i, v){
                                if(v.lineType == 1){ line += "起点"; }
                                if(v.lineType == 2){ line += " -> "+ v.simpleStation; }
                                if(v.lineType == 3){ line += " -> "+ "终点"; }
                            });
                        }
                        var tr = $("<tr id='"+ item.id +"'><td>" + item.routeName +"</td><td>" + line +"</td><td><a class='select-btn' href='javascript:;'>选择</a></td></tr>");
                        if(setting.selectKey && setting.selectKey - item.id == 0){
                            $.ex.util.changeBackgroundTr(tr);
                        }
                        return tr;
                    }, setting.selectFun);
                });
                content.find('.sr-search').find('.add-route').on('click', function(){
                    $.util.iframe(base_url +"/backstage/route/addView",
                        {title:'新增路由', boxWidth:900, initfun:function(iframe, ibody){
                                ibody.find('.btn-div').hide();
                            }, ok_obj:function(){
                                var $submit = this.$ibody.find('#submitBtn');
                                if($submit[0]){
                                    $submit[0].click();
                                    return false;
                                }else{
                                    content.find('.sr-search').find('.search')[0].click();
                                }
                            }, cancel_obj:function(){
                                content.find('.sr-search').find('.search')[0].click();
                            }
                        }
                    );
                });
                content.find('.sr-search').find('.search').trigger('click');
            };
            setting['content']="<div class='sr-search'></div><div class='sr-table''></div><div class='sr-page'></div>";
            setting['panel'] = $.dialog(setting);
            return this;
        }
    });
}(jQuery));
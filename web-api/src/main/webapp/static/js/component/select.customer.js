(function($){
    if($.select == null){ $.select = {}; }
    $.select.util = {
        modifyHotspot : function(keys){
            if(keys && keys.length){
                keys = keys.join(',');
            }
            $.util.json(base_url + '/enterprise/customer/selects', {'selects':keys.join(",")});
        },
        select_company_customer_html: "<div id='vue-box-12348754621' class=\"table-style\">" +
        "    <div>" +
        "        <form class=\"form-inline\">" +
        "            <div class=\"form-group\">" +
        "                <select class=\"form-control\" v-model=\"customerType\">" +
        "                    <option value=\"\">全部</option>" +
        "                    <option value=\"1\">货主</option>" +
        "                    <option value=\"2\">收货客户</option>" +
        "                    <option value=\"3\">承运商</option>" +
        "                </select>" +
        "            </div>" +
        "            <div class=\"form-group\">" +
        "                <input type=\"text\" class=\"form-control\" placeholder=\"输入企业名称查询\" v-model=\"likeName\">" +
        "            </div>" +
        "            <button type=\"button\" class=\"btn btn-default\" @click=\"loadCustomers\">查询</button>" +
        "            <div class=\"form-group\" style=\"float: right;\">" +
        "                <span style='font-size: 10px; vertical-align: bottom;'>已找到{{count}}条客户信息</span>" +
        "            </div>" +
        "        </form>" +
        "    </div>" +
        "    <div>" +
        "        <table>" +
        "            <thead>" +
        "            <tr>" +
        "                <th width='50%'>客户名称</th>" +
        "                <th width='20%'>客户类型</th>" +
        "                <th width='20%'>注册状态</th>" +
        "                <th width='10%'>选择</th>" +
        "            </tr>" +
        "            </thead>" +
        "            <tbody>" +
        "            <tr v-if=\"customers == null || customers.length <= 0\">" +
        "                <td colspan=\"4\">没有满足条件的客户信息</td>" +
        "            </tr>" +
        "            <tr v-for=\"(c, index) in customers\">" +
        "                <td style='text-align: left; padding-left: 10px;'>{{c.name}}<span v-if=\"c.commonly\" class=\"label label-success\" style='margin-left: 10px;'>常用</span></td>" +
        "                <td>{{c.typeName}}</td>" +
        "                <td>{{c.regName}}</td>" +
        "                <td>" +
        "                   <div :class=\"single ? 'el-radio el-radio-green' : 'el-checkbox el-checkbox-green'\">" +
        "                       <input :type=\"single ? 'radio' : 'checkbox'\" :id=\"c.key\" :value=\"index\" v-model=\"selects\">" +
        "                       <label :class=\"single ? 'el-radio-style' : 'el-checkbox-style'\" :for=\"c.key\"></label>" +
        "                   </div>" +
        "                </td>" +
        "            </tr>" +
        "            </tbody>" +
        "        </table>" +
        "    </div>" +
        "    <div>&nbsp;*&nbsp;未在合同物流管理平台注册的企业不能被分享,已选择的系统会自动过滤掉</div>" +
        "</div>",

        select_customer_address_html: "<div id='vue-box-9876543212131' class=\"table-style\">" +
        "    <div>" +
        "        <form class=\"form-inline\">" +
        "            <div class=\"form-group\">" +
        "                <select class=\"form-control\" v-model=\"customerType\">" +
        "                    <option value=\"\">全部</option>" +
        "                    <option value=\"1\">货主</option>" +
        "                    <option value=\"2\">收货客户</option>" +
        "                    <option value=\"3\">承运商</option>" +
        "                </select>" +
        "            </div>" +
        "            <div class=\"form-group\">" +
        "                <input type=\"text\" class=\"form-control\" placeholder=\"输入客户名称查询\" v-model=\"companyName\">" +
        "            </div>" +
        "            <button type=\"button\" class=\"btn btn-default\" @click=\"loadAddresss\">查询</button>" +
        "            <div class=\"form-group\" style=\"float: right;\">" +
        "                <span style='font-size: 10px; vertical-align: bottom;'>已找到{{count}}条地址信息</span>" +
        "            </div>" +
        "        </form>" +
        "    </div>" +
        "    <div>" +
        "        <table>" +
        "            <thead>" +
        "            <tr>" +
        "                <th width='30%'>客户名称</th>" +
        "                <th width='50%'>地址</th>" +
        "                <th width='15%'>联系人</th>" +
        "                <th width='5%'>选择</th>" +
        "            </tr>" +
        "            </thead>" +
        "            <tbody>" +
        "            <tr v-if=\"addresss == null || addresss.length <= 0\">" +
        "                <td colspan=\"4\">没有满足条件的信息</td>" +
        "            </tr>" +
        "            <tr v-for=\"(a, index) in addresss\">" +
        "                <td style='text-align: left; padding-left: 10px;'>{{a.customerName}}</td>" +
        "                <td>{{a.fullAddress}}</td>" +
        "                <td>{{a.contacts}}</td>" +
        "                <td>" +
        "                   <div :class=\"single ? 'el-radio el-radio-green' : 'el-checkbox el-checkbox-green'\">" +
        "                       <input :type=\"single ? 'radio' : 'checkbox'\" :id=\"a.id\" :value=\"index\" v-model=\"selects\">" +
        "                       <label :class=\"single ? 'el-radio-style' : 'el-checkbox-style'\" :for=\"a.id\"></label>" +
        "                   </div>" +
        "               </td>" +
        "            </tr>" +
        "            </tbody>" +
        "        </table>" +
        "    </div>" +
        "</div>"
    };
    $.fn.extend({
        /**
         * 选择企业客户信息
         * @param options
         *  {
         *       "title": 标题,
         *       "likeName": 模糊查询,
         *       "customerType": 客户类型,
         *       "selects": 已经选择的客户编号,
         *       "reg": 1:已注册的企业,0:未注册的企业,
         *       "userHost": 是否使用用户热点,
         *       "single": true:单选,false:多选,
         *       "selectClose": true:确认选择后关闭,false:关闭
         *  }
         * @param callback 确认选择后的回调函数,参数未当前选择的客户信息
         * @returns {selectUser}
         */
        selectCompanyCustomer:function(options, callback){
            var target = this, vm = null;
            var settings = $.extend({'title': '选择客户', 'useBootstrap': false, 'type':'blue', 'closeIcon': true, 'animation':'scale', 'closeAnimation':'scale', hotspotType: 4, single: false, customerType:'', likeName:'', selectClose: false}, options);
            if(!settings.selects){ settings.selects = []; }
            var panel = $.util.panel(settings.title , $.select.util.select_company_customer_html, 700);
            panel.initialize = function(self){
                var $editBody = this.$body;
                vm = new Vue({
                        el: $editBody.find('#vue-box-12348754621')[0],
                        data:{customers:[], likeName: settings.likeName, customerType:settings.customerType, count: 0, selects: settings.selects, single: settings.single},
                        created:function(){
                            this.loadCustomers();
                        },
                        methods:{
                            loadCustomers:function(){
                                var _this = this;
                                if(!settings.parameters){
                                    settings.parameters = {};
                                }
                                settings.parameters['likeName'] = _this.likeName;
                                settings.parameters['type'] = _this.customerType;
                                $.util.json(base_url + '/enterprise/customer/list', settings.parameters, function (data) {
                                    if (data.success) {//处理返回结果
                                        $.each(data.customers,function(index, c){
                                            c['typeName'] = c.type == 1 ? '货主' : (c.type == 2 ? '收货客户' : '承运商');
                                            c['regName'] = (c.companyKey != null && c.companyKey > 0) ? '已注册' : '未注册';
                                        });
                                        _this.customers = data.customers;
                                        if(data.customers != null){
                                            _this.count = data.customers.length;
                                        }
                                        _this.selects = [];
                                    } else {
                                        $.util.error(data.message);
                                    }
                                });
                            }
                        }
                });
            };
            panel.addButton(function() {
                if(vm){
                    if(vm.single){
                        var item = vm.customers[vm.selects];
                        if(item){
                            callback.call(vm, item, item.key);
                        }
                        $.select.util.modifyHotspot(item.key);
                    }else{
                        var items = vm.selects.map(function (i) {
                            return vm.customers[i];
                        });
                        var keys = items.map(function (item) {
                            return item.key;
                        });
                        callback.call(vm, items, keys);
                        $.select.util.modifyHotspot(keys);
                    }
                }
                return settings.selectClose;
            });
            panel.open();
            return panel;
        },
        /**
         * 选择客户地址信息
         * @param options
         *  {
         *       "title": 标题,
         *       "companyName": 模糊查询,
         *       "customerType": 客户类型,
         *       "single": true:单选,false:多选,
         *       "selectClose": true:确认选择后关闭,false:关闭
         *  }
         * @param callback 确认选择后的回调函数,参数未当前选择的客户地址信息
         * @returns {selectUser}
         */
        selectCustomerAddress:function(options, callback){
            var target = this, vm = null;
            var settings = $.extend({'title': '选择客户地址', 'useBootstrap': false, 'type':'blue', 'closeIcon': true, 'animation':'scale', 'closeAnimation':'scale', single: false, customerType:'', companyName:'', selectClose: false}, options);
            if(!settings.selects){ settings.selects = []; }
            var panel = $.util.panel(settings.title , $.select.util.select_customer_address_html, 800);
            panel.initialize = function(self){
                var $editBody = this.$body;
                vm = new Vue({
                    el: $editBody.find('#vue-box-9876543212131')[0],
                    data:{addresss:[], companyName: settings.companyName, customerType:settings.customerType, count: 0, selects: settings.selects, single: settings.single},
                    created:function(){
                        this.loadAddresss();
                    },
                    methods:{
                        loadAddresss:function(){
                            var _this = this;
                            if(!settings.parameters){
                                settings.parameters = {};
                            }
                            settings.parameters['companyName'] = _this.companyName;
                            settings.parameters['customerType'] = _this.customerType;
                            $.util.json(base_url + '/enterprise/customer/address/list', settings.parameters, function (data) {
                                if (data.success) {//处理返回结果
                                    _this.addresss = data.addresss;
                                    if(data.addresss != null){
                                        _this.count = data.addresss.length;
                                    }
                                    _this.selects = [];
                                } else {
                                    $.util.error(data.message);
                                }
                            });
                        }
                    }
                });
            };
            panel.addButton(function() {
                if(vm){
                    //$.select.util.modifyHotspot(vm.selects);
                    if(callback){
                        if(vm.single){
                            var item = vm.addresss[vm.selects];
                            if(item){
                                callback.call(vm, item, item.id);
                            }
                        }else{
                            var items = vm.selects.map(function (i) {
                                return vm.addresss[i];
                            });
                            var keys = items.map(function (item) {
                                return item.id;
                            });
                            callback.call(vm, items, keys);
                        }
                    }
                }
                return settings.selectClose;
            });
            panel.open();
            return target;
        }
    });
}(jQuery));
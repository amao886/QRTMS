<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <title>合同物流管理平台-客户管理-地址管理</title>
    <%@ include file="/views/include/head.jsp" %>

    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <link rel="stylesheet" href="${baseStatic}css/addressManage.css?times=${times}"/>
</head>

<body>
<div id="customer-address-content" class="clearfix">
        <div class="topInfo clearfix">

            <el-button type="primary" class="add" @click="add">新增地址</el-button>
        </div>
        <table>
            <thead>
            <tr>
                <th style="width: 25%;">收货客户</th>
                <th style="width: 10%;">联系人</th>
                <th style="width: 15%;">联系方式</th>
                <th style="width: 40%;">收货地址</th>
                <th style="width: 10%;">操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="item in tableList" :key="item.id" :data-id="item.id" >
                <td v-text="item.companyName"></td>
                <td v-text="item.contacts"></td>
                <td v-text="item.contactNumber"></td>
                <td v-text="item.fullAddress"></td>
                <td>
                    <span class="edit" @click="edit(item.id)">编辑</span>
                    <span class="del"  @click="del(item.id)">删除</span>
                </td>
            </tr>

            </tbody>
        </table>
     <%--分页 --%>
    <el-pagination
            background
            layout="total,prev, pager, next"
            next-text="下一页"
            prev-text="上一页"
            :total="totalCount"
            @prev-click="PrevClick"
            @next-click="NextClick"
            @current-change="currentChange"
            >
    </el-pagination>


        <!--  对话框 -->
        <el-dialog
                title="新增/编辑地址"
                :visible.sync="dialogVisible"
                width="535px"
                :before-close="handleClose">
            <div class="box">
            <el-form label-width="80px" >
                <el-form-item label="收货客户:" style="font-weight: 700">
                    <el-select v-model="customer.companyCustomerId" placeholder="请选择客户" ref="text" filterable>
                        <el-option  :value="item.key" v-for="item in customers" :key="item.id" :label="item.name"></el-option>

                    </el-select>
                </el-form-item>
                <el-form-item label="　收货人:">
                    <el-input v-model="customer.contacts" ></el-input>
                </el-form-item>
                <el-form-item label="联系方式:">
                    <el-input v-model="customer.contactNumber"></el-input>
                </el-form-item>

                <el-form-item label=" 收货地址:">
                    <!-- 省 -->
                    <select id="province" v-model="area.province" @change="selectChange(1)">
                        <option  value="" >--请选省份</option>
                        <option :value="index" v-for="(item, index) in provinces" :key="item.id" >
                            {{item.name}}
                        </option>
                    </select>

                    <!-- 市 -->
                    <select  id="city" v-model="area.city" @change="selectChange(2)">
                        <option value="" >--请选城市</option>
                        <option :value="index" v-for="(item, index) in citys" :key="item.id" >
                            {{item.name}}
                        </option>
                    </select>
                    <!-- 区县 -->
                    <select  id="county" v-model = 'area.county'  @change="selectChange(3)">
                        <option value="" >--请选区县</option>
                        <option :value="index" v-for="(item, index) in countys" :key="item.id" >
                            {{item.name}}
                        </option>
                    </select>
                </el-form-item>
                <el-form-item >
                    <el-input v-model="customer.address" placeholder="详细地址" ></el-input>
                </el-form-item>
            </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                            <el-button type="primary" @click="addAddress();updateAddress()">确 定</el-button>
            </span>
        </el-dialog>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<%--<script type="text/javascript" src="${baseStatic}js/jquery-1.11.1.js"></script>
<script type="text/javascript" src="${baseStatic}vue/vue.js"></script>--%>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script type="text/javascript" src="${baseStatic}vue/vue-resource.js"></script>
<%--<script type="text/javascript" src="${baseStatic}plugin/js/jquery-confirm.min.js"></script>--%>

<script>
    const vm = new Vue({

        el:'#customer-address-content',
        data:{
            address:[],
            flag:'',
            id:'',//客户id
            totalCount:0,
            pageNum:1,
            pageSize:10,
            tableList:[],
            provinces:[],
            citys:[],
            countys:[],
            customers:[],
            dialogVisible: false,
            area: {
                county:'',
                city:'',
                province:''
            },
            customer:{}
        },
        mounted(){
            const _this = this;
            _this.$http.post(base_url + '/special/support/area', {parentid:0}, {emulateJSON: false}).then(function (responce) {
                _this.provinces = responce.body.result

            })
            _this.loadAddress(1);
        },
        methods: {
            // 显示总页数事件
            handleSizeChange(val) {
                console.log(`每页 ${val} 条`);
            },
            //点击上一页事件
            PrevClick(val) {
                    console.log('上一页')
                 if (this.pageNum < 1){
                     return
                 }else {
                     this.pageNum--;
                     this.loadAddress();
                 }
            },
            //点击下一页事件
            NextClick(val) {
                console.log('下一页');
                if (this.pageNum>Math.ceil(this.totalCount/this.pageSize)) {
                    return
                }else {
                    this.pageNum++;
                    //渲染数据到页面上
                    this.loadAddress();
                }
            },

            // 当前页改变时触发事件
            currentChange(val){
                this.pageNum = val;
                //渲染数据到页面上
                this.loadAddress();
            },
            //对话框关闭确认事件
            handleClose(done) {
                done();
                /*this.$confirm('确认关闭？')
                    .then(_ => {
                    })
                    .catch(_ => {});*/
            },
            indexOfName:function(arr, name){
                for(j = 0; j < arr.length; j++) {
                    if(arr[j].name.indexOf(name) >= 0){
                        return j;
                    }
                }
                return null;
            },
            loadAddress:function(page){
                var _this = this;
                var  parmas = {
                    pageNum: page | _this.pageNum,
                    pageSize:_this.pageSize
                }
                //渲染数据到页面上
                _this.$http.post(base_url+"/enterprise/customer/address/search", parmas ,{emulateJSON: false})
                    .then(
                        (response)=>{
                            console.log(response)
                            if( response.body.page.total){
                                _this.totalCount = response.body.page.total
                            } else {
                                _this.totalCount=0;
                            }

                            _this.tableList = response.body.page.collection

                        },
                        (error)=>{
                            console.log(error);
                        }
                    )
            },
            //查询单个客户
            loadCustomer(id){
                const _this = this;
                _this.$http.post(base_url+"/enterprise/customer/address/detail/"+id,{},{emulateJSON: false})
                    .then(
                        (response)=>{
                            var c = response.body.customer;
                            // console.log(c)
                            _this.customer = {
                                id: c.id,
                                companyCustomerId: c.companyCustomerId,
                                contacts: c.contacts,
                                contactNumber: c.contactNumber,
                                address: c.address,
                                province: c.province,
                                city: c.city,
                                district: c.district
                            };
                            console.log(_this.customer)
                            if(_this.customer != null && _this.customer.province){
                                _this.area.province = _this.indexOfName(_this.provinces, _this.customer.province);
                                _this.loadCity(function(){
                                    console.log("加载完成");
                                });
                            }
                        },
                        (error)=>{
                            console.log(error);
                        }
                    )

            },
            //删除
            del(id){
                const  _this = this;
                $.util.confirm('删除地址', '确定将此记录删除?', function () {
                    _this.$http.post(base_url+"/enterprise/customer/address/delete",{ids:id},{emulateJSON: false})
                        .then(
                            (response)=>{
                                _this.loadAddress();
                            },
                            (error)=>{
                                console.log(error);
                            }
                        )
                });
            },
            //修改flag
             edit(id){
                  //如果flag为false，那么默认需要对地址进行修改
                 this.dialogVisible = true;
                 this.flag = false;
                 this.loadCustomerList();
                 this.loadCustomer(id);

             },
               add(){
                //如果flag为true，那么就默认需要新增地址
                    this.dialogVisible = true;
                    this.flag = true;
                    this.customer = {};
                   this.area.county = '';
                   this.area.city = '';
                   this.area.province = '';
                    this.loadCustomerList();
               },
            //修改地址
            updateAddress(){
                const _this = this;
                console.log(_this.customer)
                if(!_this.customer.companyCustomerId){
                    _this.$message.error('请选择客户');
                    return false;
                }else if (!_this.customer.contacts){
                    _this.$message.error('收货人名称不能为空');
                    return false;
                }else if (!_this.customer.contactNumber){
                    _this.$message.error('收货人联系方式不能为空');
                    return false;
                } else if (!(/^1[34578]\d{9}$/.test(_this.customer.contactNumber))){
                    _this.$message.error('请输入正确的手机号码');
                    return false;
                } else if (!_this.customer.province){
                    _this.$message.error('收货人省份不能为空');
                    return false;
                } else if(!_this.customer.city){
                    _this.$message.error('收货人市不能为空');
                    return false;
                }else if(!_this.customer.address){
                    _this.$message.error('收货人详细地址不能为空');
                    return false;
                }

                  if (_this.flag == false) {
                      _this.$http.post(base_url+"/enterprise/customer/address/edit", _this.customer, {emulateJSON: false})
                          .then(
                              (response)=>{
                                  console.log(response);
                                  if (response.body.success){
                                      _this.dialogVisible = false;
                                      _this.$message({
                                          message: response.body.message,
                                          type: 'success',
                                          duration:1000,
                                          center:true
                                      })
                                  }else {
                                      this.$message.error(response.body.message);
                                  }
                                  this.loadAddress();
                              },
                              (error)=>{
                                  console.log(error);
                              }
                          )
                  }

            },
            //查询客户列表
            loadCustomerList(){
                this.$http.post(base_url+"/enterprise/customer/list",{},{emulateJSON: false})
                    .then(
                        (response)=>{
                            console.log(response);
                            this.customers = response.body.customers
                        },
                        (error)=>{
                            console.log(error);
                        }
                    )
            },
            //新增地址
            addAddress(){
                const _this = this;
                console.log(_this.customer)

                if(!_this.customer.companyCustomerId){
                    _this.$message.error('请选择客户');
                    return;
                }else if (!_this.customer.contacts){
                    _this.$message.error('收货人名称不能为空');
                    return;
                }else if (!_this.customer.contactNumber){
                    _this.$message.error('收货人联系方式不能为空');
                    return;
                } else if (!_this.customer.province){
                    _this.$message.error('收货人省份不能为空');
                    return;
                } else if(!_this.customer.city){
                    _this.$message.error('收货人市不能为空');
                    return;
                }else if(!_this.customer.address){
                    _this.$message.error('收货人详细地址不能为空');
                    return;
                }else if (!(/^1[34578]\d{9}$/.test(_this.customer.contactNumber))) {
                    _this.$message.error('请输入正确的手机号码');
                    return;
                }
                if(_this.flag == true){

                    _this.$http.post(base_url+"/enterprise/customer/address/save",_this.customer,{emulateJSON: false})
                        .then(
                            (response)=>{
                                console.log(response);
                                if (response.body.success){
                                    _this.dialogVisible = false;
                                    _this.$message({
                                        message: response.body.message,
                                        type: 'success',
                                        duration:1000,
                                        center:true
                                    })
                                }else {
                                    this.$message.error(response.body.message);
                                }
                                this.loadAddress();
                            },
                            (error)=>{
                                console.log(error);
                            }
                        )
                }

            },
            selectChange:function(type){
                //type: 1-省份 2-城市 3-区县
                var _this = this;
                if(type == 1 && !isNaN(_this.area.province)){
                    _this.area.city='';
                    _this.area.county='';
                    var province = _this.provinces[_this.area.province];
                    _this.customer.province = province.name;
                    _this.loadCity(function () {
                        this.area.city = '';
                    });
                }else if(type == 2 && !isNaN(_this.area.city)){
                    _this.area.county='';
                    var city = _this.citys[_this.area.city];
                    _this.customer.city = city.name;
                    _this.loadCounty(function () {
                        this.area.county = '';
                    });
                }else if(type == 3 && !isNaN(_this.area.county)){
                    //_this.area = {county:''};
                    var county = _this.countys[_this.area.county];
                    _this.customer.district = county.name;
                }
            },
            loadCity: function(callback){
                const _this = this;
                if(_this.area.province || _this.area.province >= 0){
                    var province = _this.provinces[_this.area.province];
                    _this.$http.post(base_url + '/special/support/area', {parentid:province.id}, {emulateJSON: false}).then(function (responce) {
                        _this.citys = responce.body.result;
                        _this.area.city = _this.indexOfName(_this.citys, _this.customer.city);
                        if(_this.area.city != null){
                            _this.loadCounty(callback);
                        }else{
                            if(callback){
                                callback.call(_this);
                            }
                        }
                    })
                }
            },
            loadCounty: function(callback){
                const _this = this;
                if(_this.area.city || _this.area.city >= 0){
                    var city = _this.citys[_this.area.city];
                    _this.$http.post(base_url + '/special/support/area', {parentid:city.id}, {emulateJSON: false}).then(function (responce) {
                        _this.countys = responce.body.result;
                        _this.area.county = _this.indexOfName(_this.countys, _this.customer.district);
                        if(callback){
                            callback.call(_this);
                        }
                    })
                }
            }
        }

    })
</script>
</html>
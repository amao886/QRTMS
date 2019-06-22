<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-车辆管理-要车录入</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <style type="text/css">
        .title {
            height: 40px;
            background-color: #F2F2F2;
            line-height: 40px;
            padding-left: 5px;
            margin-bottom: 15px;
        }

        .el-input {
            width: 300px;
        }

        .el-form-item__content {
            margin-left: 130px !important;
        }
        .unit {
            position: absolute;
            top: 0;
            left: 265px;
        }
    </style>
</head>
<body>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->

<div class="car-management" id="car-management">
    <template>

        <el-form   label-width="80px"  >
            <div class="part-wrapper">
                <div class="title">要车单信息</div>
                <el-form-item label="要车对象" >
                    <el-select v-model="designate.conveyId" filterable placeholder="选择物流商">
                        <el-option
                                v-for="item in customerList"
                                :key="item.companyKey"
                                :label="item.name"
                                :value="item.companyKey">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="联系人" >
                    <el-input v-model="designate.contact " placeholder="选填"></el-input>
                </el-form-item>
                <el-form-item label="联系电话" >
                    <el-input v-model="designate.contactNumber" placeholder="选填" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></el-input>
                </el-form-item>
            </div>
            <div class="part-wrapper">
                <div class="title">要车信息</div>
                <el-form-item label="车型" >
                    <el-select v-model="vehicle.carModel" placeholder="请选择车型 " filterable>
                        <el-option
                                v-for="item in carModelList"
                                :key="item.key"
                                :label="item.type"
                                :value="item.type">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="车长" >
                    <el-input v-model="vehicle.carLength" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"></el-input>
                    <span class="unit">(m)</span>
                </el-form-item>
                <el-form-item label="载重" >
                    <el-input v-model="vehicle.vehicleLoad" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"></el-input>
                    <span class="unit">(kg)</span>
                </el-form-item>
                <el-form-item label="体积" >
                    <el-input v-model="vehicle.volume" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"></el-input>
                    <span class="unit">(m³)</span>
                </el-form-item>
            </div>
            <div class="part-wrapper">
                <div class="title">货物信息</div>
                <el-form-item label="总载重" >
                    <el-input v-model="vehicle.totalLoad" placeholder="请输入大于零的数字" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')">
                    </el-input>
                    <span class="unit">(kg)</span>
                </el-form-item>
                <el-form-item label="总体积">
                    <el-input v-model="vehicle.totalVolume" placeholder="请输入大于零的数字" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"></el-input>
                    <span class="unit">(m³)</span>
                </el-form-item>
            </div>
            <el-form-item>
                <el-button type="primary" @click="entrySubmit">提交</el-button>
            </el-form-item>
        </el-form>
    </template>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script type="text/javascript" src="${baseStatic}vue/vue-resource.js"></script>
<script>
    $(document).ready(function () {
        const vm = new Vue({
            el: '#car-management',
            data: {
                customerList:[],//物流商列表
                vehicle: {
                    carModel: "",//车型
                    carLength: "",//车长
                    vehicleLoad: "",//载重
                    volume: "",//体积
                    totalLoad: "",//总载重
                    totalVolume: "",//总体积
                },
                designate:{
                    conveyId: '',//物流商
                    contact: '',//联系人
                    contactNumber: "",//联系电话
                },
                //车型列表
                carModelList:[
                    {type:'厢式车',key:1},
                    {type:'平板车',key:2},
                    {type:'高栏车',key:3},
                    {type:'中栏车',key:4},
                    {type:'半封闭',key:5},
                    {type:'灌装车',key:6},
                    {type:'冷藏车',key:7},
                    {type:'集装车',key:8},
                    {type:'自卸车',key:9},
                    {type:'危险品车',key:10},
                    {type:'挂车',key:11},
                    {type:'其他',key:12}
                ],


            },
            mounted:function(){
                var _this = this ;
                _this.request('/enterprise/customer/list',{reg:1, status: 1},function (data) {
                    console.log(data)
                    if (data.success){
                        _this.customerList = data.customers
                    }
                })
            },
            methods: {
                //要车录入提交
                entrySubmit() {
                   var _this =this;
                    if (_this.designate.conveyId=='') {
                        _this.$message({ message: '请选择物流商', type: 'error' })
                        return false
                    }
                     if (_this.vehicle.totalLoad == '') {
                         _this.$message({ message: '总载重不能为空', type: 'error' })
                         return false
                     }
                    if (_this.vehicle.totalLoad <=0) {
                        _this.$message({ message: '请输入大于零的数字', type: 'error' })
                        return false
                    }
                    if (_this.vehicle.totalVolume =='') {
                        _this.$message({ message: '总体积不能为空', type: 'error' })
                        return false
                    }
                    if (_this.vehicle.totalVolume <=0) {
                        _this.$message({ message: '请输入大于零的数字', type: 'error' })
                        return false
                    }

                     if (_this.designate.contactNumber!=''){
                         if (!(/^1\d{10}$/.test(_this.designate.contactNumber))){
                             console.log(_this.designate.contactNumber)
                             _this.$message({
                                 message:'请输入正确的手机号码',
                                 type: 'error'
                             });
                             return false;
                         }
                     }
                    var parmas = {
                        vehicle:JSON.stringify(_this.vehicle),
                        designate:JSON.stringify(_this.designate)
                    }
                    console.log(parmas)
                    _this.request('/enterprise/vehicle/car/save',parmas,function (data) {
                        console.log(data)
                        if (data.success){
                            if(parent.layer){
                                parent.layer.close(parent.layer.getFrameIndex(window.name));
                            }
                        }
                    })
                },
                request:function(url, parmas, callback){
                    var _this = this;
                    _this.$http.post(url, parmas, {emulateJSON: false})
                        .then(
                            (response) => {
                                if (response.body.success) {
                                    if(callback){

                                        callback.call(_this, response.body);
                                    }
                                }else{
                                    _this.$message({ message: response.body.message, type: 'error' })
                                }
                            },
                            (error) => {
                                _this.$message({ message: '数据请求异常', type: 'error' })
                            }
                        )
                }
            }
        })
    });
</script>
</html>
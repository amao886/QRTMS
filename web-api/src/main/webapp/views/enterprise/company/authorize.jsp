<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>物流跟踪-员工管理-开通授权</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/signaturemanage.css?times=${times}"/>
    <style type="text/css">
        .selectspan {
            width: 0;
            height: 0;
            border: 10px solid #31ACFA;
            border-left: 10px solid transparent;
            border-top: 10px solid transparent;
            float: right;
            margin-top: -10px;
            margin-right: -10px;
        }
    </style>
</head>

<body key="${ukey}">
<div class="signature-content" id="authorizecontent">
    <div class="company">
        <div class="row">
            <div class="col-md-12">
                <div class="left" v-if="legalize != null">
                    请选择授权给<span style="font-weight: bold">{{legalize.name}}</span>的企业章（可多选，至少选择一个）:
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <ul v-if="companySeals != null && companySeals.length > 0">
                    <li class="signatureList" v-for="s in companySeals">
                        <div>
                            <img class="signaturePic" :src="imgPath +'/'+ s.sealData" width="150" height="150"/>
                        </div>
                        <div class="signatureName">{{s.companyName}}</div>
                        <div class="" style="float: right;position:relative">
                            <input type="checkbox"  :value="s.id" v-model="selects"/>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="saveBtn" style="text-align: center; padding-top: 20px;">
                    <a href="javascript:;" class="layui-btn layui-btn-normal" @click="authorize">确定将选择的公章授权使用</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script>
    var _ukey = $('body').attr('key');
    $(document).ready(function () {
        new Vue({
            el: '#authorizecontent',
            data: {
                companySeals: [],
                legalize: {},
                company: {},
                selects: [],
                uKey: _ukey
            },
            created: function () {
                loadSomething(this);
            },
            methods: {
                authorize: function () {
                    var parmas = {employeeKey: this.uKey, seals: this.selects.join(',')};
                    $.util.json(base_url + "/enterprise/company/signature/auth", parmas, function (data) {
                        if (data.success) {//处理返回结果
                            $.util.success(data.message, function () {
                                window.location.href = base_url + "/enterprise/company/view/employee";
                            }, 3000);
                        } else {
                            $.util.error(data.message);
                        }
                    });
                }
            }
        });
        function loadSomething(vm) {
            $.util.json(base_url + "/enterprise/company/authorize/"+ vm.uKey, null, function (data) {
                if (data.success) {//处理返回结果
                    vm.companySeals = data.companySeals;
                    vm.company = data.company;
                    vm.legalize = data.legalize;
                    if(data.selects != null){
                        vm.selects = data.selects;
                    }else{
                        vm.selects = [];
                    }
                    vm.imgPath = data.imagePath;
                } else {
                    $.util.error(data.message);
                }
            });
        }
    });
</script>

</html>
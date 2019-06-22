<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>物流跟踪-企业管理-签署方管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/signaturemanage.css?times=${times}"/>
    <style type="text/css">

    </style>
</head>

<body>
<div class="signature-content" id="signatureContent">
    <div class="chooseRole clearfix">
        <div class="role-company" @click="selectTab = 1">
            <button id="company" class="radio-box-chooserole" :class="{selected : selectTab == 1}">企业章</button>
        </div>
        <div class="role-person" @click="selectTab = 2">
            <button id="person" class="radio-box-chooserole" :class="{selected : selectTab == 2}">个人章</button>
        </div>

    </div>
    <div class="company" v-show="selectTab == 1">
        <div class="signature">
            <div class="signature-title">
                <div class="left">
                    <div class="input-group">
                        <input type="text" v-model="clikeString" placeholder="请输入企业章名称" class="form-control"
                               style="height: 32px;">
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-default" @click="searchCompanys">查询</button>
                        </div>
                    </div>
                </div>
                <div class="right">
                    <a href="javascript:;" onclick="showAdd();">新增企业章</a>
                </div>
            </div>
        </div>
        <div class="signature-content">
            <input type="hidden" :value="company.companyName" id="companyName"/>
            <div class="content">
                <ul>
                    <li class="signatureList" v-for="o in companySeals"
                        v-if="companySeals != null && companySeals.length > 0">
                        <div>
                            <img class="signaturePic" :src="imgPath +'/'+ o.sealData" width="150" height="150"/>
                        </div>
                        <div class="signatureName">{{o.companyName}}</div>
                        <div class="deleteIcon" v-on:click="deleteSeal(o.id)">
                            <img src="/static/images/deleteicon.png"/>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="person" v-show="selectTab == 2">
        <div class="signature-content" style="padding-left: 0;">
            <div class="input-group" style="width: 30.2%;margin-top: 40px;">
                <input type="text" v-model="plikeString" placeholder="请输入员工姓名" id="likeStringP" class="form-control"
                       style="height: 32px;">
                <div class="input-group-btn">
                    <button type="button" class="btn btn-default" @click="searchPersonals">查询</button>
                </div>
            </div>
            <div class="content-left">
                <div class="content">
                    <h3>{{company.companyName}} <span v-if="employees != null">（{{employees.length}}）</span></h3>
                    <ul>
                        <li class="personList" :class="{active : employeeKey == o.employeeId}" v-for="o in employees"
                            v-if="employees != null && employees.length > 0">
                            <span class="head_img"></span>
                            <p @click="employeeKey = o.employeeId">{{o.employeeName}}</p>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="content-right" v-if="employee != null">
            <div class="signature-info">
                <img v-if="employee.user != null && employee.user.headImg != null" :src="employee.user.headImg">
                <img v-else src="/static/images/head_img.png">
                <div class="text">
                    <div class="name">
                        <span class="span-text" id="personName">{{employee.employeeName}}<b
                                v-if="employee.legalize != null"> ({{employee.legalize.name}})</b></span>
                    </div>
                    <div class="phone" v-if="employee.user != null">
                        <span class="span-text" id="personPhone">{{employee.user.mobilephone}}</span>
                    </div>
                </div>
            </div>
            <div class="menu-content" v-if="employee != null && employee.seals != null">
                <div class="handWrite" v-for="o in employee.seals"
                     v-if="employee.seals != null && employee.seals.length > 0" style="margin-left: 40px">
                    <img class="showSignaturePic" :src="imgPath +'/'+ o.sealImgPath" width="122" height="51"/>
                </div>
                <div class="handWrite" v-if="employee.seals == null || employee.seals.length <= 0"
                     style="margin-left: 40px">
                    当前员工没有印章
                </div>
            </div>
        </div>
    </div>
</div>
<div class="addContent" style="display: none;">
    <div class="addBox" style="width:950px;">
        <div class="left">
            <div class="line-wrapper">
                <div class="line-part">
                    <span class="float-tag"><em>*</em>企业章简称</span>
                    <div class="text-tag"><input type="text" class="input-tag" name="signatureAbbreviation"
                                                 maxlength="10"></div>
                    <div class="tripText">不得超过十个字</div>
                </div>
                <div class="line-part">
                    <span class="float-tag">企业章样式</span>
                    <div class="style" style="padding-top: 5px;" onclick="chooseStyle(0)">
                        <div class="radio-box">
                            <input type="radio" checked="checked" name="signatureStyle" id="0" value="F1"/><span></span>
                        </div>
                        <div class="radioText">常规圆章</div>
                    </div>
                    <!--
                    <div class="style" onclick="chooseStyle(1)">
                        <div class="radio-box">
                            <input type="radio" name="signatureStyle" id="1" value="F2"/><span></span>
                        </div>
                        <div class="radioText">椭圆章</div>
                    </div>
                    -->
                    <div class="diagram">
                        <img class="round" src="../../../static/images/signaturerounddiagram.png"/>
                        <!--<img class="oval" src="../../../static/images/signaturepic.png" style="display: none;"/>-->
                    </div>
                </div>
                <div class="line-part">
                    <span class="float-tag">横向文文案</span>
                    <div class="text-tag"><input type="text" class="input-tag" name="signatureTransversalCase"
                                                 maxlength="10"
                                                 onkeyup="this.value=this.value.replace(/[^\u4E00-\u9FA5]/g,'')"
                                                 onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\u4E00-\u9FA5]/g,''))">
                    </div>
                    <div class="tripText">不得超过十个汉字，避免英文字母</div>
                </div>
                <div class="line-part">
                    <span class="float-tag">防伪码信息</span>
                    <div class="text-tag"><input type="text" class="input-tag" name="signatureSecurityCode"
                                                 maxlength="13" onkeyup="this.value=this.value.replace(/\D/g,'')"
                                                 onafterpaste="this.value=this.value.replace(/\D/g,'')"></div>
                    <div class="tripText">13位纯数字</div>
                </div>
                <div class="input-group-btn">
                    <button type="button" class="btn btn-default previewGeneration">预览生成结果</button>
                </div>
            </div>
        </div>
        <div class="right">
            <div class="spliteLine"></div>
            <div class="exampleText">生成结果预览</div>
            <canvas class="canvasContent" width="200px" height="200px"></canvas>
        </div>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}js/drawseal.js"></script>
<script>
    var companyId;
    $(document).ready(function () {
        var vm = new Vue({
            el: '#signatureContent',
            data: {
                companySeals: [],
                employees: [],//
                employee: null, //当前选中员工的信息
                count: 0,
                company: {},
                imgPath: null,
                clikeString: null,
                plikeString: null,
                employeeKey: null, //当前选中员工的编号
                selectTab: 1 //企业章
            },
            created: function () {
                selectTab = 1;
                loadCompanySeals(this);
            },
            methods: {
                searchCompanys: function () {
                    loadCompanySeals(this);
                },
                searchPersonals: function () {
                    loadEmployees(this);
                },
                deleteSeal: function (id) {
                    var vm = this;
                    $.util.confirm('删除企业章', '删除后将不可再选用此章进行签署,已被授权的员工将会自动取消授权,确定要删除吗？', function () {
                        $.post(base_url + '/enterprise/company/del/seal/' + id, {}, function (data) {
                            if (data.success) {//处理返回结果
                                $.util.success(data.message, function(){
                                    vm.searchCompanys();
                                }, 3000);
                            } else {
                                $.util.error(data.message);
                            }
                        }, 'json');
                    });
                },
                selectEmployee: function () {
                    selectEmployee();
                }
            },
            watch: {
                employeeKey: function (val, oldVal) {
                    if (val != oldVal && val > 0) {
                        selectEmployee(vm);
                    }
                },
                selectTab: function (val, oldVal) {
                    if (val == 1) {
                        loadCompanySeals(this);
                    } else {
                        loadEmployees(this);
                    }
                }
            }
        });

        var selectEmployee = function (vm) {
            $.util.json(base_url + "/enterprise/company/employee/seal/" + vm.employeeKey, null, function (data) {
                if (data.success) {//处理返回结果
                    vm.employee = $.extend(data.employee, {
                        user: data.user,
                        seals: data.seals,
                        legalize: data.legalize
                    });
                    vm.imgPath = data.imagePath;
                } else {
                    $.util.error(data.message);
                }
            });
        }

        function loadEmployees(vm) {
            var parmas = {employeeName: vm.plikeString};
            $.util.json(base_url + "/enterprise/company/personal/seals", parmas, function (data) {
                if (data.success) {//处理返回结果
                    vm.employees = data.employees;
                    vm.company = data.company;
                    vm.imgPath = data.imagePath;
                    if (vm.employees != null && vm.employees.length > 0) {
                        vm.employeeKey = vm.employees[0].employeeId;
                    } else {
                        vm.employee = null;
                        vm.employeeKey = -1;
                    }
                } else {
                    $.util.error(data.message);
                }
            });
        }

        function loadCompanySeals(vm) {
            var parmas = {companyName: vm.clikeString};
            $.util.json(base_url + "/enterprise/company/company/seals", parmas, function (data) {
                if (data.success) {//处理返回结果
                    vm.companySeals = data.companySeals;
                    vm.company = data.company;
                    vm.imgPath = data.imagePath;
                } else {
                    $.util.error(data.message);
                }
            });
        }
    });

    function chooseRole(index) {
        if (index == 0) {
            $("input[name='likeStringP']").val("");
            $("#person").removeClass("selected");
            $("#company").addClass("selected");
            $(".person").hide();
            $(".company").show();
        } else {
            $("input[name='likeStringC']").val("");
            $("#company").removeClass("selected");
            $("#person").addClass("selected");
            $(".company").hide();
            $(".person").show();
        }
    }

    function showAdd() {
        addSignature();
    }

    function addSignature() {
        var panel = $.util.panel('新增企业章', $(".addContent").html());
        panel.initialize = function () {
            var ebody = this.$body;
            ebody.find(".previewGeneration").on('click', function () {
                var radii = '100';
                var canvas = ebody.find(".canvasContent")[0];
                var securityCode = ebody.find("input[name='signatureSecurityCode']").val();
                var transversalCase = ebody.find("input[name='signatureTransversalCase']").val();
                generateSignature(canvas, $("#companyName").val(), transversalCase, securityCode, radii);
            });
        }
        panel.addButton({
            text: '确定',
            action: function () {
                var ebody = this.$body, parmas = {};
                parmas.companyName = ebody.find("input[name='signatureAbbreviation']").val();
                parmas.copyWriting = ebody.find("input[name='signatureTransversalCase']").val();
                parmas.securityCode = ebody.find("input[name='signatureSecurityCode']").val();
                parmas.sealType = ebody.find('input:radio[name="signatureStyle"]:checked').val();
                parmas.companyId = companyId;
                if (null == parmas.companyName && "" == $.trim(parmas.companyName)) {
                    $.util.error('企业章简称不能为空');
                    return;
                }
                /*
                if (null == parmas.copyWriting && "" == parmas.copyWriting) {
                    $.util.error('横向文文案不能为空');
                    return;
                }
                */
                $.util.json(base_url + "/enterprise/company/save/seal", parmas, function (data) {
                    if (data.success) {//处理返回结果
                        $.util.alert("操作提示", data.message, function () {
                            window.location.reload();
                        });
                    } else {
                        $.util.error(data.message);
                    }
                });
            }
        });
        var confirm = panel.open();
    }

    function search(type) {
        if (type == 0) {
            alert($("input[name='likeStringC']").val());
        } else {
            alert($("input[name='likeStringP']").val());
        }
    }

    function chooseStyle(clickPosition) {
        var list = document.getElementsByName("signatureStyle");
        for (var i = 0; i < list.length; i++) {
            if (list[i].id == clickPosition) {
                list[i].checked = "checked";
            }
        }
        if (clickPosition == 0) {
            $('.round').show();
            $('.oval').hide();
        } else {
            $('.round').hide();
            $('.oval').show();
        }
    }


    function isCanvasBlank(canvas) {
        var blank = document.createElement('canvas');
        blank.width = canvas.width;
        blank.height = canvas.height;

        return canvas.toDataURL() == blank.toDataURL();
    }

    function generateSignature(ecanvas, companyName, transversalCase, securityCode, radii) {
        DrawSeal(ecanvas, companyName, securityCode, transversalCase, parseInt(radii));
    }

    /*function getPersonInfo(clickPosition) {
        var nameArray = ["张三疯子", "张疯子"];
        var phoneArray = ["18946001234", "18546004321"];
        var list = document.getElementsByClassName("personList");
        for (var i = 0; i < list.length; i++) {
            if (list[i].id == clickPosition) {
                $(list[i]).addClass("active");
            } else {
                $(list[i]).removeClass("active");
            }
        }

        $("#personName").html(nameArray[clickPosition]);
        $("#personPhone").html(phoneArray[clickPosition]);
    }*/


</script>

</html>
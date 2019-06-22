<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>企业管理-企业列表</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/page.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}css/businessmanage.css?times=${times}"/>
    <style type="text/css">
        [v-cloak]{display: none !important;}
    </style>
</head>
<body>
<div class="custom-content clearfix">
    <h2 class="mt10">企业管理</h2>
    <div  id="companyCon" v-cloak>
        <div class="noList" v-if="!company"><a href="javascript:;" @click="addCompany">+添加企业信息</a></div>
        <div class="manageCon" v-else>
            <%--<a class="btn btn-default mt20" href="javascript:;" @click="addCompany">添加企业</a>--%>
            <div class="company">
                <div class="titCon">
                    <h3 class="companyName">{{company.companyName}}</h3>
                    <span v-if="employee.employeeType == 1 && company.signFettle != 2">
                        <a href="javascript:;" class="blue openElec" @click="openElec">开通电子签收</a>
                    </span>
                </div>
                <p v-if="employee.employeeType == 1">
                    <a href="javascript:;" onFocus="this.blur()" class="addPerson" @click="_addPersion">添加企业人员</a>
                </p>
                <div id="memberList">
                    <ul class="memberList clearfix" v-if="results">
                        <li v-for="item in results.collection">
                            <div class="listItem">
                                <div class="top clearfix">
                                    <p class="cName fl">{{item.employeeName}}</p>
                                    <p class="fr">{{item.mobilePhone}}</p>
                                </div>
                                <div class="bottom clearfix">
                                    <span v-if="employee.employeeType == 1">
                                    <a class="revoke signature fl" @click="_authorizeFun(item.employeeId,item.companyId)" v-if="!item.authorizeFile && !item.authorizeSealId && !item.authorizeTime">签章授权</a>
                                    <a class="revoke cancelSign fl" @click="_cancelAuthorize(item.employeeName,item.employeeId)" v-else>取消签章授权</a>
                                    <a class="delete fr" v-if="item.employeeId != item.userId"  @click="_deletePeople(item.employeeId)">删除</a>
                                    </span>
                                </div>
                            </div>
                        </li>
                    </ul>
                    <%--分页--%>
                    <div class="page-item clearfix" v-if="results">
                        <div id="layui_div_page" class="col-sm-12 center-block" v-if="results.pages > 1">
                            <ul class="pagination" style="margin-bottom: 0;">
                                <li class="disabled"><span><span aria-hidden="true">共{{results.total}}条,每页{{results.pageSize}}条</span></span>
                                </li>
                                <li v-if="!results.isFirstPage"><a @click="flip(1)" class="cur_pointer">首页</a></li>
                                <li v-if="!results.isFirstPage"><a @click="flip(results.prePage)" class="cur_pointer">上一页</a></li>
                                <li v-for="p in results.navigatepageNums">
                                    <span v-if="p == results.pageNum" class="cactive">{{p}}</span>
                                    <a v-if="p != results.pageNum" @click="flip(p)" class="cur_pointer">{{p}}</a>
                                </li>
                                <li v-if="!results.isLastPage"><a @click="flip(results.nextPage)" class="cur_pointer">下一页</a></li>
                                <li v-if="!results.isLastPage"><a @click="flip(results.pages)" class="cur_pointer">尾页</a></li>
                                <li class="disabled"><span><span>共{{results.pages}}页</span></span></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 添加企业人员  弹窗 -->
<div class="add_member_panel" style="display:none;">
    <div class="model-base-box add_member" style="width:500px;height: auto;">
        <div class="model-form-field">
            <label>姓名:</label>
            <input type="text" name="name" style="width:61%"/>
        </div>
        <div class="model-form-field">
            <label>手机号码:</label>
            <input type="tel" name="mobile" style="width:61%"/>
        </div>
    </div>
</div>

<!-- 签章授权  弹窗 -->
<div class="signature_panel" style="display:none;">
    <div class="model-base-box add_member" style="width:500px;height: auto;">
        <div class="model-form-field file_box">
            <label>选择文件:</label>
            <input type="text" name="fileName" placeholder="请上传企业授权书">
            <span class="span-txt">上传</span>
            <a href="javascript:;" class="file">
                <input name="file" type="file" accept="">
            </a>
        </div>
        <div class="model-form-field">
            <label>模板下载:</label>
            <a href="javascript:javascript:;" class="templetDownload" url="">下载授权书模板</a>
        </div>
    </div>
</div>

</body>
<%@ include file="/views/include/floor.jsp" %>

<script>
    $(document).ready(function () {
        var vm = new Vue({
            el: '#companyCon',
            data: {num: 1, size: 12, results: null, company: null, employee: null},
            created: function () {
                this._getMemberList(1);
            },
            methods: {
                //获取数据
                _getMemberList: function (num) {
                    var _this = this;
                    var parmas = {
                        num: num,
                        size: this.size
                    };
                    $.util.json(base_url + '/enterprise/company/queryPersonnel', parmas, function (data) {
                        if (data.success) {//处理返回结果
                            _this.results = data.page;
                            _this.company = data.company;
                            _this.employee = data.employee;
                        } else {
                            $.util.error(data.message);
                        }
                    });
                },
                //添加企业
                addCompany: function () {
                    window.location.href = base_url + '/enterprise/company/view/add';
                },
                //开通电子签收
                openElec: function () {
                    window.location.href = base_url + '/enterprise/company/view/legalize';
                },
                //翻页
                flip: function (num) {
                    this._getMemberList(num);
                },
                //添加企业人员
                _addPersion: function () {
                    addPersion();
                },
                //签章授权
                _authorizeFun: function (eId, cId) {
                    authorizeFun(eId, cId);
                },
                //取消签章授权
                _cancelAuthorize: function (name, id) {
                    cancelAuthorize(name, id);
                },
                //删除人员
                _deletePeople: function (id) {
                    deletePeople(id)
                }
            }
        });

        //添加企业人员
        function addPersion() {
            $.util.form('添加企业人员', $(".add_member_panel").html(), function () {

            }, function () {
                var editBody = this.$body,
                    _name = editBody.find("input[name=name]").val(),
                    _mobile = editBody.find("input[name=mobile]").val();
                if ($.trim(_name) == "") {
                    $.util.error("姓名不能为空");
                    return false;
                }
                if ($.trim(_mobile) == "") {
                    $.util.error("手机号码不能为空");
                    return false;
                }
                if (!/^1[\d]{10}$/.test(_mobile)) {
                    $.util.error("手机号码格式不正确！");
                    return false;
                }
                var parmas = {
                    employeeName: _name,
                    mobilePhone: _mobile
                };
                $.util.json(base_url + '/enterprise/company/addPersonnel', parmas, function (data) {
                    if (data.success) {
                        $.util.alert('操作提示', data.message, function () {
                            vm._getMemberList(1);
                        });
                    } else {
                        $.util.error(data.message);
                    }
                });
                return false;
            });
        }

        //签章授权
        function authorizeFun(eId, cId) {
            $.util.form('签章授权', $(".signature_panel").html(), function () {
                var editBody = this.$body;
                var file = editBody.find('input[name=file]')[0];
                var inputFile = editBody.find('input[name=fileName]')[0];
                //选择文件
                file.addEventListener('change', function () {
                    var fileName = this.files[0].name;
                    inputFile.value = fileName;
                }, false);

                //下载授权书模板
                editBody.find('.templetDownload').on('click', function () {
                    $.util.download($(this).attr('url'));
                })

            }, {
                text: '确定并通知人员进行实名认证',
                btnClass: 'btn-blue',
                action: function () {
                    var editBody = this.$body;
                    var file = editBody.find('input[name=file]')[0].files[0];
                    if (!file) {
                        $.util.error("授权书不能为空");
                        return false;
                    }
                    var formData = new FormData();
                    formData.append("employeeId", eId);
                    formData.append("companyId", cId);
                    formData.append("file", file);
                    $.ajax({
                        url: base_url + "/enterprise/company/signatureAuth",
                        type: 'POST',
                        data: formData,
                        processData: false, // 告诉jQuery不要去处理发送的数据
                        contentType: false,// 告诉jQuery不要去设置Content-Type请求头
                        success: function (response) {
                            if (response.success) {//处理返回结果
                                $.util.success(response.message);
                                vm._getMemberList(1);
                            } else {
                                $.util.error(response.message);
                            }
                        }
                    });
                    return false;
                }

            });
        }

        //取消签章授权
        function cancelAuthorize(name, id) {
            $.util.confirm("取消签章授权", '确定取消' + name + '的签章授权?<br>取消后Ta将不可再进行电子签收。', function () {
                $.util.json(base_url + '/enterprise/company/uSignatureAuth/' + id, null, function (data) {
                    if (data.success) {
                        $.util.alert('操作提示', data.message, function () {
                            vm._getMemberList(1);
                        });
                    } else {
                        $.util.error(data.message);
                    }
                });
            });
        }

        //删除员工
        function deletePeople(id) {
            $.util.confirm("删除员工", '是否确认删除该员工？', function () {
                $.util.json(base_url + '/enterprise/company/delPersonnel/' + id, null, function (data) {
                    if (data.success) {
                        $.util.alert('操作提示', data.message, function () {
                            vm._getMemberList(1);
                        });
                    } else {
                        $.util.error(data.message);
                    }
                });
            });
        }
    })
</script>

</html>
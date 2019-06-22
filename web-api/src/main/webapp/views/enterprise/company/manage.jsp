<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>物流跟踪-企业管理-企业信息</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/legalize.css?times=${times}"/>
    <style>
        .position {
            margin-top: 20px;
            font-size: 16px;
        }

        .validate-info {
            vertical-align: top !important;
        }

        .company-validate .company-title img {
            width: 90px !important;
        }
    </style>
</head>
<body>
<button onclick="test()"> test</button>
<div class="qlm-content">
    <%--物流跟踪-企业管理-企业信息--%>
    <%--<c:if test="${company == null}">--%>
    <%--<div>没有企业</div>--%>
    <%--</c:if>--%>
    <%--<c:if test="${company != null}">--%>
    <%--<div>编号:${company.id}</div>--%>
    <%--<div>企业名称:${company.companyName}</div>--%>
    <%--<div>状态(1:注册未认证,2:已认证,3:禁用) :${company.fettle}</div>--%>
    <%--<div>电子签收开通状态(0:未开通,1:已开通):${company.signFettle}</div>--%>
    <%--<div>创建人:${company.userId}</div>--%>
    <%--<div>创建时间:${company.createTime}</div>--%>
    <%--<div>更新时间:${company.updateTime}</div>--%>
    <%--<div>组织机构代码:${company.codeOrg}</div>--%>
    <%--<div>社会统一信用代码:${company.codeUsc}</div>--%>
    <%--<div>法人姓名:${company.legalName}</div>--%>
    <%--<div>法人身份证号码:${company.legalIdNo}</div>--%>
    <%--</c:if>--%>
    <div data-id="${company.id }" class="company-validate">
        <div class="company-title">
            <img src="../../../static/images/companylogonew.png" alt="">
            <div class="company-name">
                <h3>${company.companyName }</h3>
                <c:if test="${company.fettle == 1 }">
                    <span class="status">未认证</span>
                </c:if>
                <c:if test="${company.fettle == 2 || company.fettle == 3 }">
                    <span class="status ing">认证中</span>
                </c:if>
                <c:if test="${company.fettle == 5 }">
                    <span class="status error">认证失败</span>
                </c:if>
                <c:if test="${company.fettle == 4 }">
                    <span class="status done">已认证</span>
                </c:if>
                <p class="position">
                    ${company.address }
                </p>
            </div>


            <div class="validate-info">
                <%--认证中-未打款--%>
                <c:if test="${company.fettle == 1}">
                    <span>请提交企业基本信息,完成认证<a href="${basePath}/enterprise/company/view/legalize">去认证</a></span>
                </c:if>
                <c:if test="${company.fettle == 2}">
                    <span>企业基本信息已认证,请填写对公打款信息,完成认证<a href="${basePath}/enterprise/company/view/legalize">继续认证</a></span>
                </c:if>
                <c:if test="${company.fettle == 3 && companyBankVerify.fettle == 1}">
                    <span>企业对公打款信息提交完成,打款中</span>
                </c:if>
                <c:if test="${company.fettle == 3 && companyBankVerify.fettle == 3}">
                    <span>企业对公打款信息提交完成,请填写收到的金额,完成认证 <a id="checkId" href="javasript:;">去验证</a></span>
                </c:if>
                <c:if test="${company.fettle == 5 }">
                    <span>企业认证信息有误,请 <a href="javasript:;">重新认证</a></span>
                </c:if>
                <c:if test="${company.fettle == 4 }">
                    <span>已成功开通电子签收功能</span>
                </c:if>
            </div>

        </div>
        <div class="company-text">
            <%--未认证--%>
            <c:if test="${company.fettle == 0 }">
                <div class="un-validate">企业认证后，可使用电子签收功能 <a href="${basePath}/enterprise/company/view/legalize">去认证</a>
                </div>
            </c:if>
            <%--认证--%>
            <c:if test="${not empty companyBankVerify }">
                <div class="validate">
                    <div class="line-part">
                        <span class="float-tag">组织机构代码：</span>
                        <div class="text-tag"><p class="p-tag">${company.codeOrg }</p></div>
                    </div>
                    <div class="line-part large-tag">
                        <span class="float-tag">社会统一信用代码：</span>
                        <div class="text-tag"><p class="p-tag">${company.codeUsc }</p></div>
                    </div>
                    <div class="line-part short-tag">
                        <span class="float-tag">法人姓名：</span>
                        <div class="text-tag"><p class="p-tag">${company.legalName }</p></div>
                    </div>
                    <div class="line-part">
                        <span class="float-tag">法人身份证号：</span>
                        <div class="text-tag"><p class="p-tag">${company.legalIdNo }</p></div>
                    </div>
                    <div class="line-part">
                        <span class="float-tag">对公账户户名：</span>
                        <div class="text-tag"><p class="p-tag">${companyBankVerify.name }</p></div>
                    </div>
                    <div class="line-part large-tag">
                        <span class="float-tag">企业对公银行账号：</span>
                        <div class="text-tag"><p class="p-tag">${companyBankVerify.cardNo }</p></div>
                    </div>
                    <p>*如需更新企业信息，请联系系统管理员</p>
                </div>
            </c:if>
        </div>
    </div>
</div>
<!--  -->
<div class="operateBtn">
    <el-button type="primary" @click='importFile()'>导入</el-button>
</div>

<el-dialog title="上传文件导入" :visible.sync="dialog.import" :close-on-click-modal="false" :close-on-press-escape="false"
           width="40%">
    <el-row>
        <el-col>
            <el-button type="text" @click="downloadTemplate">下载模板</el-button>
        </el-col>
    </el-row>
    <el-row>
        <el-col :span="24">
            <el-upload drag :action="uploadUrl" accept=".xlsx,.xls" :data="fileData" style="margin-bottom: 10px;"
                       :on-error="importFile" :on-success="importFile" ref="upload" :auto-upload='false'>
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处,或<em>点击上传</em></div>
                <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件,请确保文件格式的正确性</div>
            </el-upload>
            <el-button class="btnUpload" size="small" @click="submitUpload">上传</el-button>
        </el-col>
    </el-row>
</el-dialog>

<div class="editCheck" style="display: none">
    <div class="model-base-box edit_customer" style="width: 470px;">
        <input id="id" name="id" type="hidden">
        <div class="model-form-field">
            <label for="">法人姓名 :</label>
            <!-- <input name="fullName" type="text" min="0" max="50"> -->
            <span id="name" class="float-tag"></span>
        </div>
        <div class="model-form-field">
            <label for="">对公卡号 :</label>
            <span id="cardNo" class="float-tag"></span>
            <!-- <input name="mobilePhone" type="text" min="0" max="11" id="mobilePhone"> -->
        </div>
        <div class="model-form-field">
            <label for="">开户名 :</label>
            <span id="bank" class="float-tag"></span>
        </div>
        <div class="model-form-field">
            <label for="">打款金额 :</label>
            <input name="checkAmount" type="number" id="checkAmount">
            <p style="text-align: center;">银行打款金额0.01~0.99之间
            <p>
        </div>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script>


    function test() {
        var parmas = {
            list: JSON.stringify([
                {
                    systemId: "399718306078720",// 系统单号
                    transportReceivable: "500.00", // 运输应收
                    otherTotalReceivable: "20.00", // 其他应收合计
                    otherReceivableRemark: "卸货" // 其他应收备注
                },
                {
                    systemId: "13",
                    transportReceivable: "600.00",
                    otherTotalReceivable: "25.00",
                    otherReceivableRemark: "装货"
                }
            ])
        };

        $.util.json(base_url + '/contract/singleReceivable', parmas, function (data) {
        });
    }

    $(document).ready(function () {


        $("#checkId").on('click', function () {
            $.util.form('银行打款金额校验', $(".editCheck").html(), function () {
                var editBody = this.$body;
                var companyId = $(".company-validate").attr("data-id");
                $.util.json(base_url + '/enterprise/company/query/companyKey/' + companyId, null, function (data) {
                    if (data.success) {
                        editBody.find("#id").val(data.results.id);
                        editBody.find("#name").html(data.results.name);
                        editBody.find("#cardNo").html(data.results.cardNo);
                        editBody.find("#bank").html(data.results.bank);
                    } else {
                        $.util.error(data.message);
                    }
                });
            }, function () {
                var parmas = {}, editBody = this.$body;
                editBody.find("input").each(function (index, item) {
                    parmas[item.name] = $.trim(item.value);
                });
                console.log(parmas);
                if (!parmas.id) {
                    $.util.error("编号不能为空");
                    return false;
                }
                if (!parmas.checkAmount) {
                    $.util.error("打款金额不能为空");
                    return false;
                } else if (parmas.checkAmount < 0.01 || parmas.checkAmount > 0.99) {
                    $.util.error("打款金额应在0.01~0.09元之间");
                    return false;
                }
                $.util.json(base_url + '/enterprise/company/check/amount', parmas, function (data) {
                    if (data.success) {
                        $.util.success(data.message, function () {
                            window.location.reload();
                        }, 2000);
                    } else {
                        $.util.danger("操作提示", data.message, function () {
                            window.location.reload();
                        });
                    }
                });
            });
        });
    })
</script>
</html>
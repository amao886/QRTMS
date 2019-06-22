<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>运营后台-企业管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/history.css?times=${times}"/>
    <style>
    .oprateBtn{color:#31acfa;cursor: pointer;}
    .model-base-box .model-form-field>label{width:90px;}
    </style>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchDailyForm" action="" method="get">

        <input type="hidden" id="num" name="pageNum" value="${param.pageNum}">
        <input type="hidden" id="size" name="pageSize" value="10">
        <div class="content-search">
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">企业名称:</label>
                    <input type="text" class="tex_t" placeholder="任务单号/送货单号" name="orderNumber" v-model="companyName">
                </div>
                <div class="fl col-min">
                    <label class="labe_l">企业名称:</label>
                    <select class="tex_t selec_t" name="companyName" id="companyName" v-model="fettle">
                    	<option value="">是否认证</option>
                    	<option value="1">是</option>
                    	<option value="0">否</option>
                    </select>
                </div>
                <div class="fl col-min">
                    <label class="labe_l">认证权限:</label>
                    <select class="tex_t selec_t" name="permission" id="permission" v-model="signFettle">
                    	<option value="">是否开通认证权限</option>
                    	<option value="1">是</option>
                    	<option value="0">否</option>
                    </select>
                </div>
                <div class="fl col-min">
                	<a href="javascript:;" class="content-search-btn search-btn" @click="searchList()">查询</a>
                </div>
            </div>
        </div>
    </form>
    <div class="table-style">
        <template>
        <table class="dtable" id="signNumTable">
            <thead>
            <tr>
                <th>企业名称</th>
                <th>是否认证</th>
                <th>是否分配电子签收认证权限</th>
                <th>注册时间</th>
                <th>认证时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>

                <tr v-for="(item, index) in results.collection">
                    <td>{{item.companyName}}</td>
                    <td>
                        <span v-if="item.fettle == 1">是</span>
                        <span v-else>否</span>
                    </td>
                    <td>
                        <span v-if="item.signFettle == 1">是</span>
                        <span v-else>否</span>
                    </td>
                    <td>{{item.createTime | datetime}}</td>
                    <td>{{item.updateTime | datetime}}</td>
                    <td>
                        <a class="oprateBtn" v-if="item.signFettle != 1">分配电子签收认证权限</a>
                    	<a class="oprateBtn viewBtn">查看</a>
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="page-item">
            <div id="layui_div_page" class="col-sm-12 center-block" v-if="results.pages > 1">
                <ul class="pagination" style="margin-bottom: 0;">
                    <li class="disabled"><span><span aria-hidden="true">共{{results.total}}条,每页{{results.pageSize}}条</span></span>
                    </li>
                    <li v-if="!results.isFirstPage"><a v-on:click="flip(1)">首页</a></li>
                    <li v-if="!results.isFirstPage"><a v-on:click="flip(results.prePage)">上一页</a></li>
                    <li v-for="p in results.navigatepageNums">
                        <span v-if="p == results.pageNum" class="cactive">{{p}}</span>
                        <a v-if="p != results.pageNum" v-on:click="flip(p)">{{p}}</a>
                    </li>
                    <li v-if="!results.isLastPage"><a v-on:click="flip(results.nextPage)">下一页</a></li>
                    <li v-if="!results.isLastPage"><a v-on:click="flip(results.pages)">尾页</a></li>
                    <li class="disabled"><span><span>共{{results.pages}}页</span></span></li>
                </ul>
            </div>
        </div>
        </template>
    </div>
</div>

<!-- 赠送可签署份数  弹窗 -->
<div class="assign_num_panel" style="display:none;">
    <div class="model-base-box assign_num" style="width:400px;height: auto;">      
        <div class="model-form-field">
            <label>累积赠送次数:</label>
            <span class="totalNum"></span>次
        </div>
        <div class="model-form-field">
            <label>赠送次数:</label>
            <input type="text" name="mobile" style="width:60%"/>
            <span>份</span>
        </div>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script>
    $(document).ready(function () {
        //提交查询事件
        $(".search-btn").on("click", function () {
            $("form input[name='pageNo']").val(1);
            $("#searchDailyForm").submit();
        });


        var vm = new Vue({
            el: ".history-content",
            data :{
                "companyName":"",
                "fettle":"",
                "signFettle":"",
                "pageNum":num || 1 ,
                "pageSize":"10",
                "results":{}
            },
            methods : {
                flip : function(num){//翻页
                    this.getList(num);
                },
                searchList : function(){//搜索
                    this.getList();
                },
                getList: function(num){//获取列表
                    var $this = this;

                    var opctions = {
                        "companyName" : $this.companyName,
                        "fettle" : $this.fettle,
                        "signFettle" : $this.signFettle,
                        "pageNum":num || 1 ,
                        "pageSize":"2"
                    };
                    $.util.json(base_url + '/enterprise/backstage/search', opctions, function (data) {
                        if (data.success) {//处理返回结果
                            $this.results = data.results;
                        } else {
                            $.util.error(data.message);
                        }
                    });
                }
            },
            computed: {

            },
            watch : {

            },
            mounted: function(){
                this.getList(1);
            }
        });

        $("#signNumTable").on("click",".assignBtn",function(){
        	var $this = $(this),
        		_total = $this.attr("data-total");
        	$.util.form('赠送可签署份数', $(".assign_num_panel").html(), function () {
        		var editBody = this.$body
        		editBody.find(".totalNum").text(_total);
            }, function () {
            	var editBody = this.$body,
            		_name = editBody.find("input[name=name]").val(),
            		_mobile = editBody.find("input[name=mobile]").val()
            	if($.trim(_name) == ""){
            		return false;
            	}
            	if(!/^1[\d]{10}$/.test(_mobile)){
            		return false;
            	}
            	alert(111);
            });
        });
    });



</script>
</html>
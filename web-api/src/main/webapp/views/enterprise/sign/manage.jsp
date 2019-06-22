<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>签署管理-签署管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}plugin/css/bootstrap-datetimepicker.css"/>
    <link rel="stylesheet" href="${baseStatic}css/inventory.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <style type="text/css">
    .content-search .col-min{width:20%;}
    .content-search .col-min1{width:10%;}
    .customer-delete{background:#ff7f00 !important;}
    .view_more>.model-form-field{width:50%;float:left;}
    .view_more>.lineWidth{width:100%;}

    .downloadBtn{color:#31acfa;text-decoration: underline !important;}
    </style>
</head>
<body>
<div class="history-content clearfix">
    <form id="searchSignRelationForm" action="${basePath}/enterprise/sign/search/manage" method="post">
        <div class="content-search">
            <div class="clearfix">

                <div class="fl col-min">
                    <label class="labe_l"></label>
                    <input type="text" class="tex_t" name="likeString" placeholder="企业名称/编号" v-model="likeString" >
                </div>
                <div class="fl col-min">
                    <label class="labe_l"></label>
                    <input type="text" class="tex_t" name="likeString" placeholder="企业名称/编号" v-model="likeString" >
                </div>
                <div class="fl col-min">
                    <label class="labe_l"><%--约定使用--%>电子签收开始时间:</label>
                    <div class="input-append date startDate">
                        <input type="text" class="tex_t z_index" id="startBeginTime" name="startBeginTime" readonly>
                        <span class="add-on">
                            <i class="icon-th"></i>
                        </span>
                    </div>
                </div>
                <div class="fl col-min">
                    <label class="labe_l"></label>
                    <div class="input-append date startDate" >
                        <input type="text" class="tex_t z_index" id="startEndTime" name="startEndTime"readonly>
                        <span class="add-on">
                            <i class="icon-th"></i>
                        </span>
                    </div>
                </div>
            </div>
            <div class="clearfix">
                <div class="fl col-min">
                    <label class="labe_l">到期状态:</label>
                    <select  class="tex_t selec_t" name="status" v-model ="status" >
                        <option value ="">全部</option>
                        <option value="1" >未到期</option>
                        <option value="2" >已到期</option>
                    </select>
                </div>
                <div class="fl col-min">
                    <label class="labe_l"><%--约定使用--%>电子签收结束时间:</label>
                    <div class="input-append date startDate">
                        <input type="text" class="tex_t z_index" id="endStartTime" name="endStartTime" readonly>
                        <span class="add-on">
                            <i class="icon-th"></i>
                        </span>
                    </div>
                </div>
                <div class="fl col-min">
                    <label class="labe_l"></label>
                    <div class="input-append date startDate">
                        <input type="text" class="tex_t z_index"  id="endEndTime" name="endEndTime" readonly>
                        <span class="add-on">
                            <i class="icon-th"></i>
                        </span>
                    </div>
                </div>
                <div class="fl col-min1">
                    <button type="button" class="btn btn-default search-btn" @click="searchList()">查询</button>
                </div>
            </div>
        </div>
    </form>
    <div class="btnBox">
        <button type="button" class="btn btn-default customer-add">新增签署方</button>
    </div>
    <div class="table-style">
        <table class="dtable" id="trackTable">
            <thead>
            <tr>
                <th width="150">企业编号</th>
                <th width="150">企业名称</th>
                <th width="150">约定使用电子签收开始时间</th>
                <th width="150">约定使用电子签收结束时间</th>
                <th width="150">到期状态</th>
                <th width="150">操作</th>
            </tr>
            </thead>
            <tbody>
                <tr v-for="(item, index) in results.collection">
                    <td>{{item.companyId}}</td>
                    <td>{{item.companyName}}</td>
                    <td>{{item.starttime | datetime}}</td>
                    <td>{{item.endtime | datetime}}</td>
                    <td>{{item.status ? "未到期":"已到期"}}</td>
                    <td>
                        <a class="aBtn editBtn" >查看</a>
                        <a class="aBtn editBtn" >编辑</a>
                        <a class="aBtn editBtn" >删除</a>
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
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.js"></script>
<script src="${baseStatic}plugin/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${baseStatic}js/datetimepicker1.js"></script>
<script src="${baseStatic}plugin/js/cityselect/city.js"></script>
<script src="${baseStatic}plugin/js/cityselect/select.js"></script>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script>
    $(document).ready(function () {
        var vm = new Vue({
            el: ".history-content",
            data: {
                "likeString": "",
                "startBeginTime": "",
                "startEndTime": "",
                "pageNum": "1",
                "pageSize": "10",
                "status": "",
                "endStartTime": "",
                "endEndTime": "",

                "results": {}
            },
            methods: {
                flip: function (num) {//翻页
                    this.getList(num);
                },
                searchList: function () {//搜索
                    this.getList();
                },
                getList: function (num) {//获取列表
                    var $this = this;

                    var opctions = {
                        "likeString": $this.likeString,
                        "startBeginTime": $("#startBeginTime").val(),
                        "startEndTime": $("#startEndTime").val(),
                        "endStartTime": $("#endStartTime").val(),
                        "endEndTime": $("#endEndTime").val(),
                        "status": $this.status,
                        "pageNum": num || 1,
                        "pageSize": 10
                    };
                    $.util.json(base_url + '/enterprise/sign/search/manage', opctions, function (data) {
                        if (data.success) {//处理返回结果
                            $this.results = data.page;
                        } else {
                            $.util.error(data.message);
                        }
                    });
                }
            },
            computed: {},
            watch: {},
            mounted: function () {
                this.getList(1);
            }
        });
    })
</script>
</html>
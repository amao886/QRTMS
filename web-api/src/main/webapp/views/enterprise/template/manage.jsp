<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>运输管理-发货配置</title>
    <%@ include file="/views/include/head.jsp" %>
    <%--<link rel="stylesheet" href="${baseStatic}css/deliverymanagement.css?times=${times}">--%>
    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <style type="text/css">

        .el-table td, .el-table th {
            padding: 5px 0;
        }
    </style>
</head>
<body>
<div class="wrapper" id="vue-wrapper">
    <template>
        <!--<div class="deliveryConfig">发货配置</div>-->
        <div style="margin-bottom: 20px;">
            <button class="layui-btn layui-btn-normal" @click="addTemplate">新增导入模板</button>
        </div>

        <el-table :data="templates" border style="width: 99%;">
            <el-table-column prop="name" label="模板名称">
                <template slot-scope="scope">
                    <span style="margin-right: 20px;">{{scope.row.name}}</span>
                    <el-tag type="success" size="small" v-if="scope.row.fettle == 2">常用</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="300">
                <template slot-scope="scope">
                    <el-button @click="settingTemplate(scope.row.key)"  type="text" size="small" v-if="scope.row.fettle == 1">设为常用模板</el-button>
                    <el-button @click="settingTemplate(scope.row.key)" type="text" size="small" v-if="scope.row.fettle == 2">取消常用模板</el-button>
                    <el-button @click="exportTemplate(scope.row.key)" type="text" size="small">导出</el-button>
                    <el-button @click="editTemplate(scope.row.key)" type="text" size="small">编辑</el-button>
                    <el-button @click="deleteTemplate(scope.row.key)" type="text" size="small">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <%--<table>
            <tr>
                <td>模板名称</td>
                <td>操作</td>
            </tr>
            <tr v-for="t in templates">
                <td>{{t.name}}<span class="comUse" v-if="t.fettle == 2">常用</span></td>
                <td>
                    <span class="setOption" v-if="t.fettle == 1"><a href="javascript:;" @click="settingTemplate(t.key)">设为常用模板</a></span>
                    <span class="setOption" v-if="t.fettle == 2"><a href="javascript:;" @click="settingTemplate(t.key)">取消常用模板</a></span>
                    <span class="setOption"><a href="javascript:;" class="tempDel" @click="exportTemplate(t.key)">导出</a></span>
                    <span class="setOption"><a href="javascript:;" @click="editTemplate(t.key)">编辑</a></span>
                    <span class="setOption"><a href="javascript:;" class="tempDel" @click="deleteTemplate(t.key)">删除</a></span>
                </td>
            </tr>
        </table>--%>
    </template>
</div>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script>
    $(document).ready(function () {
        //使用vue加载数据
        var v = new Vue({
            el: '#vue-wrapper',
            data: {
                templates: [],
            },
            created: function () {
                this.loadTemplates();
            },
            methods:{
                addTemplate:function(){
                    window.location.href = base_url +"/enterprise/template/view/add";
                },
                editTemplate:function(templateKey){
                    window.location.href = base_url +"/enterprise/template/view/edit?key="+templateKey;
                },
                exportTemplate:function(templateKey){
                    $.util.json(base_url + "/enterprise/template/download/"+ templateKey, null, function (res) {
                        if (res.success && res.url) {
                            $.util.download(res.url);
                        } else {
                            $.util.error(res.message);
                        }
                    });
                },
                deleteTemplate:function(templateKey){
                    var vm = this;
                    $.util.confirm('删除发货模板', '确定要删除该模板吗？', function () {
                        $.util.json(base_url + "/enterprise/template/delete/"+ templateKey, null, function (data) {
                            if (data.success) {//处理返回结果
                                $.util.success(data.message, function(){
                                    vm.loadTemplates();//成功后刷新列表
                                }, 2000);
                            } else {
                                $.util.error(data.message);
                            }
                        });
                    });
                },
                settingTemplate:function(templateKey){
                    var vm = this;
                    $.util.json(base_url +"/enterprise/template/setting/"+ templateKey, null, function(data){
                        if(data.success){
                            $.util.success(data.message, function(){
                                vm.loadTemplates();//成功后刷新列表
                            }, 2000);
                        }else{
                            $.util.error(data.message);
                        }
                    })
                },
                loadTemplates: function(){
                    var vm = this;
                    $.util.json(base_url +"/enterprise/template/manage", null, function(data){
                        if(data.success){
                            vm.templates = data.templates;
                        }else{
                            $.util.error(data.message);
                        }
                    })
                }
            }
        });

        /*
        $(".modal_box").hide();
        $(".tempDel").click(function () {
            $(".modal_box").show();
        });
        $(".modalClose").click(function () {
            $(".modal_box").hide();
        });
        */
    })
</script>
</html>
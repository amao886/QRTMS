<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-考核管理-承运方考核统计 </title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <style type="text/css">
        .search ,.content {
            padding-top: 30px;
        }
        .el-select {
            margin-right: 20px;
        }
        .el-button--small {
            margin-left: 20px;
            padding: 8px 15px;
        }
        .el-input__inner {
            height: 30px;
            line-height: 30px;
        }
        .el-input__icon {
            line-height: 30px;
        }
        .content {
            margin-top: 50px;
        }
        .content ul li {
            list-style: none;
            float: left;
            width: 20%;
            margin-right: 50px;
            background-color: #F2F2F2;
            height: 200px;
            margin-bottom: 50px;
            padding:20px;
            box-sizing: border-box;
            position: relative;
        }
        ul {
            overflow: hidden;
            margin: 0;
            padding: 0;
        }
        .content ul li span{
            position: absolute;
            width: 50px;
            height: 50px;
            bottom: 50px;
            right: 90px;
            color:#409EFF;
            font-size: 24px;
            font-weight: 700;
            text-align: center;
            line-height: 50px;
        }
    </style>
</head>
<body>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->

<div class="conveystatistics" id="conveystatistics">
    <template>
        <div class="search">
            <el-select v-model="search.otherSideKey" filterable placeholder="己方企业名称">
                <el-option  label="己方企业" value="0"> </el-option>
                <el-option  label="全部下线" value=""> </el-option>
            </el-select>
            <el-date-picker v-model="search.firstTime" type="date" placeholder="开始时间" value-format="yyyy-MM-dd">
            </el-date-picker>
            --
            <el-date-picker v-model="search.secondTime" type="date" placeholder="结束时间" value-format="yyyy-MM-dd">
            </el-date-picker>
            <el-button type="primary" size='small' @click="searchCollect">查询</el-button>
        </div>
        <div class="content">
            <ul >
                <li v-for="item in list" >{{item.name}}<span>{{item.formatValue}}</span></li>
            </ul>
        </div>
    </template>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script type="text/javascript" src="${baseStatic}vue/vue-resource.js"></script>
<script>
    $(document).ready(function () {
        const vm = new Vue({
            el: '#conveystatistics',
            data: {
                search: {
                    otherSideKey: null,//己方企业名称
                    firstTime: new Date(new Date().getTime() - 30 * 24*60*60*1000),//开始时间
                    secondTime: new Date(),//结束时间
                },
                list:[],
            },
            mounted:function(){
                var _this =this;
              _this.render()
            },
            methods: {
                //搜索
                searchCollect:function(){
                    var _this =this;
                    _this.render()
                },
                render:function(){
                    var _this = this;
                    var parmas = {
                        reportKey:1,//报表编号
                        summaryTime: moment(_this.search.firstTime).format('YYYY-MM-DD')+','+ moment(_this.search.secondTime).format('YYYY-MM-DD') ,//开始时间
                        otherSideKey:_this.search.otherSideKey
                    }
                    console.log(parmas)
                    _this.request("/report/sum/1", parmas, function(data){
                        console.log(data)
                        _this.list = data.results;

                    });
                    // $.util.json(base_url + '/report/sum/1', parmas, function (data) {
                    //     if (data.success) {//处理返回结果
                    //         _this.list = data.results;
                    //     } else {
                    //         _this.$message({ message: data.message, type: 'error' })
                    //     }
                    // });
                },
                //发起请求
                request:function(url, parmas, callback){
                    var _this = this;
                    _this.$http.post(url, parmas, {emulateJSON: true})
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

                },

            }
        })
    });
</script>
</html>
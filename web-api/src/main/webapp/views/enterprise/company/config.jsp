<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>物流跟踪-企业管理-企业配置</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
    <style>
        .title {
            height: 50px;
            line-height: 50px;
            background-color: #F2F2F2;
            text-indent: 10px;
        }
        .qlm-content {
            padding-top: 15px;
            position: relative;
        }
        .notice {
            display: inline-block;
            margin-right: 100px;
        }
        .qlm-content-item {
           padding-top: 30px;
            padding-left: 15px;


        }
        .explain {
            margin-top: 20px;
            color: #999;
            font-size: 12px;
        }
        .save {
            outline: none;
            height: 40px;
            width: 190px;
            line-height: 40px;
            color: #fff;
            background-color: #169BD5;
            border-radius: 5px;
            text-align: center;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%,-50%);
        }
    </style>
</head>
<body>
<div class="qlm-content" id="qlm-content">
      <h4 class="title">发货通知配置</h4>

      <div class="qlm-content-item">
          <h5 class="notice">开通发货通知</h5>
          <el-switch  v-model="value.value1" active-color="#13ce66" inactive-color="#999"
                      active-text="开"
                     inactive-text="关" >
          </el-switch>
          <p class="explain">
              说明：开通发货通知后，收货客户可在发货后即刻收到发货通知消息。当前为功能体验期，免费开通哦~~
          </p>

         <button class="save" @click="submit">保存设置</button>
      </div>

</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
<script type="text/javascript" src="${baseStatic}vue/vue-resource.js"></script>
<script>
    $(document).ready(function () {

        const vm = new Vue({
            el: '#qlm-content',
            data: {
               value:{
                   value1:'',
               },
              obj:{},//存储后台返回的对象
              list:[],//配置项数据
            },
            mounted:function(){
                var _this = this;
                _this.$http.post(base_url + "/enterprise/company/list/config",null, {emulateJSON: false})
                    .then(
                        (response) => {
                            console.log(response);
                            if (response.body.success) {
                                _this.obj = response.body.configs
                              if ( _this.obj.send_notice==0) {
                                    _this.value.value1 = false;
                              }else {
                                  _this.value.value1 = true;
                              }
                            }
                        },
                        (error) => {
                            console.log(error);
                        }
                    )

                },
            methods: {
                submit:function () {
                    var _this = this;
                var temArr = []; //声明一个临时的数组存储当前的value对象里面的值
                    _this.list = [];
                    for (let key in _this.obj) {
                            _this.list.push({configKey:key,configValue:''})
                    }
                     // console.log(_this.list)
                    for (let i in _this.value) {
                       temArr.push(_this.value[i])
                    }
                    // console.log(temArr)
                     for(var i = 0;i<temArr.length;i++){
                          if (temArr[i]) {
                              _this.list[i].configValue = 1
                          } else {
                              _this.list[i].configValue = 0
                          }
                     }
                     console.log(_this.list)
                    var parmas = {
                        configs:JSON.stringify(_this.list)
                    }
                    console.log(parmas)
                    _this.$http.post(base_url + "/enterprise/company/modify/config",parmas, {emulateJSON: false})
                        .then(
                            (response) => {
                                console.log(response);
                                    if(response.body.success){
                                        _this.$message({
                                            message: response.body.message,
                                            type: 'success'
                                        });
                                    }
                            },
                            (error) => {
                                console.log(error);
                            }
                        )
                }
            }

        })
    })
</script>
</html>
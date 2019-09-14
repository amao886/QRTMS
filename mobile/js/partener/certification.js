var cert = new Vue({
    el: '#signature',
    data: {
        certName: '',
        certPhone: '',
        idCard: '',
        brankCardNo: '',
        code: '',
        countdown: 60
    },
    methods: {
        //验证码
        sendcode: function(){
            if(this.isPhone()){
                this.sendMsg()
                this.settime()
            }
        },
        //倒计时
        settime: function(){
            var _this = this;
            if (this.countdown == 0) {
                this.countdown = 60;
                return;
            } else {
                this.countdown--;
            }
            setTimeout(function() {
                _this.settime();
            },1000)
        },
        //调用短信接口
        sendMsg: function(){
            EasyAjax.ajax_Post_Json({
                url:'/mobile/mine/smsCode/'+this.certPhone
            },function(res){
                if(res.success){
                    $.toast('验证码发送成功','text');
                }
            });
        },
        //实名认证接口
        personalValidate: function(){
            //验证姓名
            var regName =/^[\u4e00-\u9fa5]{2,4}$/;
            if(!this.certName || !regName.test(this.certName)){
                $.toptip('请输入正确的姓名');
                return false;
            }
            //验证银行卡
            if(!this.brankCardNo){
                $.toptip('请输入银行卡号');
                return false;
            }
            //验证手机号
            var regPhone =/^1(3|4|5|7|8|9)\d{9}$/;
            if(!this.certPhone || !regPhone.test(this.certPhone)){
                $.toptip('请输入正确的手机号');
                return false;
            }

            //验证身份证号
            var regId = /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}[0-9Xx]$)/;
            if(!/^\d{17}(\d|x)$/i.test(this.idCard)){
                $.toptip("身份证长度或格式错误");
                return false;
            }
            if(!(regId.test(this.idCard))){
                $.toptip('身份证格式有误');
                return false;
            }

            //验证验证码
            var regCode = /^\d{6}$/;
            if(!this.code || !regCode.test(this.code)){
                $.toptip('请输入正确的验证码');
                return false;
            }

            var baseValue = {
                employeeName: this.certName,
                mobilePhone: this.certPhone,
                idCardNo: this.idCard,
                brankCardNo: this.brankCardNo,
                code: this.code
            }
            EasyAjax.ajax_Post_Json({
                url:'enterprise/company/personalAuth',
                data:JSON.stringify(baseValue)
            },function(res){
                if(res.success){
                    $.toast('认证信息提交成功')
                    window.location.href = _common.version("./choosePersonSignature.html");
                    //window.location.href = _common.version('../../index.html');
                }
            });

        },
        //验证手机号
        isPhone: function(){
            var regPhone =/^1(3|4|5|7|8|9)\d{9}$/;
            if(!this.certPhone || !regPhone.test(this.certPhone)){
                $.toptip('请输入正确的手机号');
                return false;
            }
            return true;
        }
    }
});




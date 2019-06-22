



const vm = new Vue({
    el:'#app',
    data:{
         companyName:''
    },
    methods:{
       Save:function(){
           const _this = this;
           var parmas = {
           	companyName:_this.companyName
           }
           console.log(parmas)
           EasyAjax.ajax_Post_Json({
							url: '/enterprise/company/save',
							data:JSON.stringify(parmas)
						}, function (res) {
							console.log(res)
							if(res.success){
								$.toast(res.message,function(){
									window.location.href = './peopleCenter.html'
								})
							}
						})
       }
    }
})
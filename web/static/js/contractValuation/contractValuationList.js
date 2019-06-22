const vm = new Vue({
	el: '#content',
	data: {
		activeName: 'first',
		first: {
			likeString: '',
			list: [], //表格数据
			pageNum: 1, //页数
			pageSize: 10, //每一页数量
			total: 0, //总数量
		},
		second: {
			likeString: '',
			list: [], //表格数据
			pageNum: 1, //页数
			pageSize: 10, //每一页数量
			total: 0, //总数量
		},
		contractType:'',//合同类型
	},
	created: function() {
     this.render()
	},
	computed: {
		current: {
			get: function() {
				return this.activeName === 'first' ? this.first : this.second;
			}
		}
	},
	methods: {
		handleClick: function(tab, event) {
			console.log(tab, event);
		},
		// 当前页改变时触发事件
		currentChange: function(val) {
			this.current.num = val;
			console.log(this.current.num)
		},
		render:function(){
			var _this  =this ;
			if(_this.activeName=='first'){
				_this.contractType=1;
				var parmas = {
					likeString:_this.current.likeString,
					contractType:_this.contractType,
					num:_this.current.pageNum,
					size:_this.current.pageSize
				}
			} else {
				_this.contractType=2;
			}
			
			EasyAjax.ajax_Post_Json({
				url: 'contract/pricing/search',
				data:parmas
			}, function(res) {
				console.log(res);
				if(res.success) {
					_this.current.total = res.result.total
					_this.current.list = res.result.results
				}
			})
		},
		//y页面跳转 
		jumpLink: function(type,id) {
			var _this = this;
			if(type == 1) {
					window.location.href = '../../../views/contractValuation/contractValuationManagement/add.html?contractType='+_this.contractType;
			} else if(type == 2) {
				window.location.href = '../../../views/contractValuation/contractValuationManagement/detail.html?contractKey='+id;
			} else if(type == 3) {
				window.location.href = '../../../views/contractValuation/contractValuationManagement/edit.html?contractKey='+id;
			} else if(type == 4) {
				window.location.href = '../../../views/contractValuation/contractValuationManagement/examine.html?contractKey='+id;
			} else {
				window.location.href = '../../../views/contractValuation/materialInfoConfig/materialConfigList.html?id='+id;
			}

		}

	}
})
var content = new Vue({
	el: '#content',
	data: {
		id: _common.getUrlParam('id'), //编号
		form: {
			searchText: '', //物料编号/物料名称
		},
		currentPage: 1, //当前页
		pageSize: 10, //每页显示数据总数
		total: 0, //总数量
		list: [], //列表数据
		dialog: {
			import: false
		},
		fileData: {},
		header:{
			token: api_token
		},
		uploadUrl: api_host + 'contract/import/excel'
	},
	created: function() {
		var _this = this;

		_this.fileData.id = _this.id;

		_this.search();
	},
	methods: {
		//列表渲染方法
		render: function() {
			var _this = this;

			var parmas = {
				id: _this.id,
				searchText: _this.form.searchText,
				num: _this.currentPage,
				size: _this.pageSize
			};
			EasyAjax.ajax_Post_Json({
				url: 'contract/commodity/search',
				data: parmas
			}, function(res) {
				console.log(res);

				if(res.success) {
					_this.list = res.result.results;
					_this.total = res.result.pages;
				}
			});
		},
		//查询
		search: function() {
			var _this = this;

			_this.render();
		},
		//下载模板
		downloadTemplate: function() {
			var _this = this;

			_common.download(api_host + 'special/download/102');
		},
		//导入
		importFile: function(res, f, fs) {
			var _this = this;

			if(_this.dialog.import) {
				if(res) {
					if(res.success) {
						_common.toast(0, res.message);
						_this.dialog.import = false;
						_this.render();
					} else {
						f.status = 'failure';
						if(res.result && res.result.fileUrl) {
							_common.toast(1, '有异常数据');
							$.util.download(res.result.fileUrl);
							_this.dialog.import = false;
							_this.render();
						} else {
							_common.toast(3, res.message);
						}
					}
					_this.$refs.upload.clearFiles();
				}
			} else {
				_this.dialog.import = true;
			}
		},
		//文件上传提交
		submitUpload: function() {
			var _this = this;

			_this.$refs.upload.submit();
		},
		//当前页发生改变
		currentChange: function(val) {
			console.log(val);

			var _this = this;

			_this.currentPage = val;
			_this.render();
		}
	}
})
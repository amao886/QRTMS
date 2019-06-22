var vm = new Vue({
	el: '#vue-common-function',
	data: {
		collection: [],
		identity: _common.getUrlParam('identity'),
		authoritys: []
	},
	methods: {
		listCode: function(num) {
			var _this = this;

			EasyAjax.ajax_Post_Json({
				url: 'admin/user/commonly'
			}, function(res) {
				var results = res.commonlies;
				if(res.success) {
					_this.collection = results;

					setTimeout(function() {
						//step 渲染小图标
						$(".contentBox").find(".funcLink").each(function(index, obj) {
							var _num = $(obj).attr("data-num");
							$('<i class="addBtn" data-num="' + _num + '"></i>').insertAfter(obj);
							$.each(_this.collection, function(i, item) {
								//console.log(item)
								if(_num == item) {
									$(obj).next(".addBtn").addClass("nowBtn").removeClass("addBtn")
									return false;
								}
							});
						});
					}, 200);
				}
			});
			_this.getMenuList();
		},
		delFunc: function(num, i, obj) {
			setFun.del(num, i, obj);
		},
		jumpLink: function(num) {
			goPage(num)
		},
		getMenuList: function() {
			var _this = this;

			EasyAjax.ajax_Post_Json({
				url: 'admin/user/authoritys'
			}, function(res) {
				if(res.success) {
					console.info(res);
					_this.authoritys = res.authoritys;
				}
			});
		},
		searchCode: function(code) {
			var _this = this;

			var result = false;

			for(var i = 0; i < _this.authoritys.length; i++) {
				if(_this.authoritys[i] == code) {
					result = true;
					break;
				}
			}

			return result;
		},
		allFunction: function() {
			var _this = this;

			window.location.href = _common.version('function.html?identity=' + _this.identity);
		},
		save: function() {
			var _strArr = this.collection.join(",");
			console.log(_strArr)
			setFun.getData({
				code: _strArr
			})
		}
	},
	computed: {},
	created: function() {
		var _this = this;

		_this.listCode();
	}
})

//点击"+"号
$(".contentBox").on("click", ".addBtn", function(e) {
	e.stopPropagation();
	var $this = $(this),
		_num = $this.attr("data-num");
	setFun.add(_num, $this)
});
$(".contentBox li").on("click", ".funcLink", function() {
	//alert($(this).attr("data-num"))
	$(this).next(".addBtn").trigger("click")
});

//常用图标：新增、删除、请求后台
var setFun = {
	add: function(num, obj) {
		var len = vm.collection.length;

		if(len >= 7) {
			$.toast("抱歉，已达上限", "text");
		} else {
			var arr = vm.collection;
			arr.push(num);
			obj.addClass("nowBtn").removeClass("addBtn");
		}
	},
	del: function(num, i, obj) {
		var arr = vm.collection;
		arr.splice(i, 1);
		vm.collection = arr;
		$(".contentBox").find("a.funcLink[data-num=" + num + "]").next().addClass("addBtn").removeClass("nowBtn");
	},
	getData: function(options) {
		var baseValue = {
			"code": options.code
		}
		EasyAjax.ajax_Post_Json({
			url: 'admin/user/commonly/modify',
			data: JSON.stringify(baseValue)
		}, function(res) {
			if(res.success) {
				$.toast("保存成功", function() {
					window.location.href = _common.version('functionAll.html?identity=' + vm.identity)
				});
			} else {
				$.toast("保存失败，请稍后重试", "cancel");
			}
		});
	}
}

$("#navBarIn").load("../common/navBarIn.html", function() {
	$(".weui-tabbar__item:first").addClass("weui-bar__item--on");

	$('.a-hook').on('click', function() {
		$('.a-hook').removeClass('active');
		$(this).addClass('active');
	})

	//跳转到首页
	$('#myIndex').on('click', function() {
		window.location.href = _common.version('../../home.html?jstatus=' + 0);
	})

	//跳转到扫一扫
	$('#swapCircle').on('click', function() {
		scanCode();
	})

	//跳转到通讯录
	$('#conList').on('click', function() {
		window.location.href = _common.version('../partener/contactList.html?jstatus=' + 1);
	})

	//跳转到个人中心
	$('#peopleCenter').on('click', function() {
		window.location.href = _common.version('../user/peopleCenter.html?jstatus=' + 2 + '&identity=' + _common.getUrlParam('identity'));
	})

	var jstatus = _common.getUrlParam('jstatus');
	console.log('jstatus:' + jstatus);
	if(jstatus == '0') {
		//首页
	} else if(jstatus == '1') {
		//通讯录
		$('.a-hook').removeClass('active');
		$('#conList').addClass('active');
	} else if(jstatus == '2') {
		//个人中心
		$('.a-hook').removeClass('active');
		$('#peopleCenter').addClass('active');
	}
});
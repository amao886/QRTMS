var _common = {
	loading: null, //加载中的框变量
	//获取url的参数
	getUrlParam: function(name) {
		var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
		var result = window.location.search.substr(1).match(reg);
		return result ? decodeURIComponent(result[2]) : null;
	},
	//给url添加时间戳，清缓存
	timeStamp: function(url) {
		var getTimestamp = new Date().getTime();
		if(url.indexOf("?") > -1) {
			url = url + "&timestamp=" + getTimestamp;
		} else {
			url = url + "?timestamp=" + getTimestamp;
		}
		return url;
	},
	//给url添加版本号，清缓存
	version: function(url) {
		return _common.timeStamp(url);
	},
	//取得项目路径
	getRootPath: function() {
		//取得当前URL
		var path = window.document.location.href;
		//取得主机地址后的目录
		var pathName = window.document.location.pathname;
		var post = path.indexOf(pathName);
		//取得主机地址
		var hostPath = path.substring(0, post);
		//取得项目名
		var name = pathName.substring(0, pathName.substr(1).indexOf("/") + 1);
		return hostPath + name + "/";
	},
	//动态加载CSS和JS文件
	dynamicLoading: {
		meta: function(content) {
			document.write(content);
		},
		css: function(path) {
			if(!path || path.length === 0) {
				throw new Error('argument "path" is required!');
			}
			document.write('<link rel="stylesheet" href="' + path + '">');
		},
		js: function(path, charset) {
			if(!path || path.length === 0) {
				throw new Error('argument "path" is required!');
			}
			document.write('<script type="text/javascript" charset="' + charset + '" src="' + path + '"></script>');
		}
	},
	//下载文件
	download: function(url) {
		$('<form action="' + url + '" method="post"></form>').appendTo('body').submit().remove();
	},
	//显示loading
	showLoading: function() {
		Vue.prototype.$message.closeAll();
		this.loading = Vue.prototype.$loading({
			lock: true,
			text: '拼命加载中',
			spinner: 'el-icon-loading',
			background: 'rgba(0, 0, 0, 0.7)'
		});
	},
	//隐藏loading
	hideLoading: function() {
		this.loading.close();
	},
	/*
	 * 信息提示
	 * type: type为number型值；0：成功，1：警告，2：消息，3：错误；成功：success 警告：warning 消息：info 错误：error
	 * message: 提示内容
	 * duration：持续显示时间，默认5000ms
	 * showClose: 是否显示X按钮，默认true显示
	 */
	toast: function(type, message, duration, showClose) {
		if(type == undefined) {
			throw new Error('type is not defined');
			return;
		} else if(this.isNull(type) || (type != 0 && type != 1 && type != 2 && type != 3)) {
			throw new Error('type value is not valid');
			return;
		}
		if(message == undefined) {
			throw new Error('message is not defined');
			return;
		} else if(this.isNull(message)) {
			throw new Error('message value is not valid');
			return;
		}
		var typeString = null;
		switch(type) {
			case 0:
				typeString = 'success';
				break;
			case 1:
				typeString = 'warning';
				break;
			case 2:
				typeString = 'info';
				break;
			case 3:
				typeString = 'error';
				break;
		}
		if(duration == undefined || this.isNull(duration) || duration == 0 || typeof duration !== "number") {
			duration = 5000;
		}
		if(showClose == undefined || this.isNull(showClose) || typeof showClose !== "boolean") {
			showClose = true;
		}
		Vue.prototype.$message({
			type: typeString,
			message: message,
			duration: duration,
			showClose: showClose
		});
	},
	/*
	 * 是否确定操作提示弹框
	 * content:提示内容
	 * callBack：回调方法
	 */
	confirmMessageBox: function(content, callBack) {
		if(content == undefined) {
			throw new Error('content is not defined');
			return;
		} else if(this.isNull(content)) {
			throw new Error('content value is not valid');
			return;
		}
		Vue.prototype.$confirm(content, '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning',
			closeOnClickModal: false,
			closeOnPressEscape: false,
		}).then(function(action) {
			(callBack && typeof(callBack) === "function") && callBack(action);
		}).catch(function(action) {
			(callBack && typeof(callBack) === "function") && callBack(action);
		});
	},
	//判断是否为空
	isNull: function(text) {
		return !text && text !== 0 && typeof text !== "boolean" ? true : false;
	}
};

var rootPath = _common.getRootPath(); //项目路径

var _indexpage = _common.getUrlParam('index');
if(_indexpage) {
	sessionStorage.clear();
}

_common.timeStamp(window.location.href);
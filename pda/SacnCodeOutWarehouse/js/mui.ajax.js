/**
 * mui.Ajax 1.0
 * User: nicolas
 * Date: 2018-9-10
 * 该框架依赖mui,请先引入mui3.5+最新版本。
 * 使用方法：
 */
;
(function (window, $) {
    var MuiAjax = {};
    var _ajaxType = ['get', 'post'];
    var _ajaxDataType = ['json', 'xml', 'html', 'script'];
    /**
     * 标准AJAX方法
     * @param config Ajax需要参数
     * @param callback ajax结束回掉函数
     */
    MuiAjax.ajax = function (config, callback) {
        _ajax(config, callback);
    };
    /**
     * Ajax Get请求
     * @param config Ajax需要参数
     * @param callback ajax结束回掉函数
     */
    MuiAjax.ajax_Get = function (config, callback) {
        config.type = _ajaxType[0];
        _ajax(config, callback);
    };
    /**
     * Ajax Post请求
     * @param config Ajax需要参数
     * @param callback ajax结束回掉函数
     */
    MuiAjax.ajax_Post = function (config, callback) {
        config.type = _ajaxType[1];
        _ajax(config, callback);
    };
    /**
     * Ajax Get请求数据格式是JSON
     * @param config Ajax需要参数可只配URL
     * @param callback ajax结束回掉函数
     */
    MuiAjax.ajax_Get_Json = function (config, callback) {
        config.type = _ajaxType[0];
        config.dataType = _ajaxDataType[0];
        _ajax(config, callback);
    };
    /**
     * Ajax post请求数据格式是JSON
     * @param config Ajax需要参数可只配URL
     * @param callback ajax结束回掉函数
     */
    MuiAjax.ajax_Post_Json = function (config, callback,callbackError) {
        config.type = _ajaxType[1];
        config.dataType = _ajaxDataType[0];
        _ajax_post(config, callback,callbackError);
    };
    /**
     * 文件上传Ajax
     * @param config
     * @param callback
     */
    MuiAjax.ajax_Upload_File = function (config, callback) {
        config.type = _ajaxType[1];
        config.dataType = _ajaxDataType[0];
        config.cache = false;
        config.contentType = false;
        config.processData = false;
        _ajax(config, callback);
    };
    /**
     * 普通的post请求,传输的数据不是json格式
     * @param config
     * @param callback
     */
    MuiAjax.common_post = function (config, callback) {
        config.type = _ajaxType[1];
        config.cache = false;
        if(!config.headers){
    		config.headers = { "token": api_token }
    	}
    	_request(api_host + "" + config.url, config, callback, null);
    };
    
    function _ajax_post(config, callback, callbackError){
    	if(!config.headers){
    		config.headers = {
				'Content-Type': 'application/json;charset=UTF-8',
			    "token": api_token	
    		}
    	}
    	_request(api_host + "" + config.url, config, callback, callbackError);
    }
    
    function _ajax(config, callback) {
    	_ajax_post(config, callback, null);
    }

    function _request(url, config, callback, callbackError){
    	$.ajax(url,{
            data: config.data,
            dataType: config.dataType,
            type: config.type,
            timeout: config.timeout,
            headers:config.headers,
            success: function (_resultData) {
                // 成功或者强制回掉才执行回掉函数
                if(_resultData.success==true || config.mustCallback){
                    (callback && typeof(callback) === "function") && callback(_resultData);
                } else{
                   	$.toast(_resultData.message);
                   	(callbackError && typeof(callbackError) === "function") && callbackError(_resultData);
                }
            },
            error: function (XMLHttpRequest, type, errorThrown) {
                var defCallback = config.defCallback;
                if(defCallback){
                    (defCallback && typeof(defCallback) === "function") && defCallback();
                }else{
                	if(XMLHttpRequest.status != 200){
                		//alert(XMLHttpRequest.responseText);
                		mui(".mui-btn").button('reset');
                		_handleStatus(XMLHttpRequest.status);
                	}

                }
            }
        });
    }
    
    
    function _handleStatus(status) {
        switch (status) {
            case 404:
            	$.toast('请求资源不存在：#' + status);
                break;
            case 400:
            	$.toast('请求参数有误：#' + status);
                break;
            case 500:
            	$.toast('服务器错误：#' + status);
                break;
            case 504:
            	$.toast('链接超时：#' + status);
                break;
            default:
            	$.toast('未知错误：#' + status);
                break;
        }
    }

    window.MuiAjax = MuiAjax;
})(window, mui);
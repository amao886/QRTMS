var _common = {
    //获取url的参数
    getUrlParam: function (name) {
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
        var result = window.location.search.substr(1).match(reg);
        return result ? decodeURIComponent(result[2]) : null;
    },
    //给url添加时间戳，清缓存
    timeStamp: function (url) {
        var getTimestamp = new Date().getTime();
        if (url.indexOf("?") > -1) {
            url = url + "&timestamp=" + getTimestamp;
        } else {
            url = url + "?timestamp=" + getTimestamp;
        }
        return url;
    },
    //给url添加版本号，清缓存
    version: function (url) {
        /*
        if(url.indexOf("?") > -1){
            url = url + "&version=version";
          }else{
            url = url + "?version=version";
          }
          */
        return _common.timeStamp(url);
    },
    showLoad: function (obj, type) {
        if (type == 1) {//显示load
            $(obj).addClass('disabled');
            obj.setAttribute('disabled', true);

        } else { //去除load
            $(obj).removeClass('disabled');
            obj.removeAttribute('disabled');
        }
    }
};

var _indexpage = _common.getUrlParam('index');
if (_indexpage) {
    sessionStorage.clear();
}

_common.timeStamp(window.location.href);
//console.log(_common.timeStamp(window.location.href));

//loading 加载。。。
$(".weui-btn.weui-btn_primary").on("click", function () {
    $(document).ajaxStart(function () {
        loadImg();
    });
});

/*window.onload = function () {
    removeLoad()
};*/

$(document).ajaxStop(function () {
    removeLoad()
});

function loadImg() {
    var _html = '<div class="loadingBox" id="loadingBox">'
        + '<div class="weui-loadmore">'
        + '<i class="weui-loading"></i>'
        + '</div>'
        + '</div>'
    $("body").append(_html);
}

function removeLoad() {
    var $target = $(document).find(".loadingBox");
    if ($target) {
        $target.remove();
    }
}

if (!$("body").hasClass("vue-page")) {
    //处理后台返回的时间
    $.views.helpers({
        dateFormatCommon: function (msg, type) {
            if (msg != null && msg != "") {
                var time = new Date(msg);
                var year = time.getFullYear();
                var month = time.getMonth() + 1;
                var day = time.getDate();
                var hour = time.getHours();
                var minute = time.getMinutes();
                var second = time.getSeconds();
                month = month < 10 ? '0' + month : month;
                day = day < 10 ? '0' + day : day;
                hour = hour < 10 ? '0' + hour : hour;
                minute = minute < 10 ? '0' + minute : minute;
                second = second < 10 ? '0' + second : second;
                switch (type) {
                    case 1:
                        return year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;
                    case 2:
                        return year + '-' + month + '-' + day + ' ' + hour + ':' + minute;
                    case 3:
                        return year + '-' + month + '-' + day;
                    default:
                        return year + '年' + month + '月' + day + '日' + ' ' + hour + ':' + minute;
                }
            } else {
                return "--"
            }
        },
        ifNull: function (str, type) {
            if (str == "" || str == null) {
                switch (type) {
                    case 1:
                        return "";
                    default:
                        return "--";
                }
            } else {
                return str;
            }
        },
        address: function (str) {
            if (str) {
                var strArr = str.split("-"),
                    _str = "";
                for (var i = 0; i < strArr.length; i++) {
                    _str += strArr[i]
                }
                return _str;
            } else {
                return "";
            }

        }
    });
}

//判断是否是在微信页
function isWeixin() {
    var ua = navigator.userAgent.toLowerCase();
    var isWeixin = ua.indexOf('micromessenger') != -1;
    if (isWeixin) {
        //alert('微信页');
        return true;
    } else {
        //alert('浏览器');
        return false;
    }
}
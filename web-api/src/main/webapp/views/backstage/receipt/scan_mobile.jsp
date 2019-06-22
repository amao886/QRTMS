<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>扫描</title>
    <%@ include file="/views/include/head.jsp" %>
    <style type="text/css">
        .vertical-center {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }

        .row {
            padding-bottom: 20px;
        }

        .bs-callout-info {
            border-left-color: #1b809e !important;
            border-right-color: #1b809e !important;
        }

        .bs-callout {
            padding: 20px;
            margin: 20px 0;
            border: 1px solid #eee;
            border-left-width: 5px;
            border-right-width: 5px;
            border-radius: 3px;
        }

        .tips-title {
            text-align: center;
            font-size: 16px;
            color: gray;
        }

        .tips-context {
            text-align: center;
            font-size: 36px;
        }

        .hide {
            display: none;
        }

        #goTips {
            width: 100%;
            height: 100%;
            text-align: center;
            line-height: 200px;
        }
    </style>
    <script type="text/javascript">

    </script>
</head>
<body onbeforeunload="return exit()">
<div id="mainBox" class="hide">
		<span id="something" appId="${appId}" nonceStr="${nonceStr}"
              timestamp="${timestamp}" signature="${signature}" userId="${userId}"
              status="${status}"/>
    <div class="container">
        <div class="row">
            <div class="col-md-10 col-md-offset-1">
                <div class="bs-callout bs-callout-info">
                    <c:if test="${status == 1}">
                        <div class="tips-context">回单回收</div>
                    </c:if>
                    <c:if test="${status == 2}">
                        <div class="tips-context">送交客户</div>
                    </c:if>
                    <c:if test="${status == 3}">
                        <div class="tips-context">退供应商</div>
                    </c:if>
                    <c:if test="${status == 4}">
                        <div class="tips-context">客户退回</div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
    <div class="container" style="margin-top:20%;">
        <div class="row">
            <div class="col-md-10 col-md-offset-1">
                <button type="button" id="open_camera" class="btn btn-success btn-lg btn-block">扫码</button>
            </div>
        </div>
        <div class="row">
            <div class="col-md-10 col-md-offset-1">
                <button type="button" id="exit" class="btn btn-danger btn-lg btn-block">退出</button>
            </div>
        </div>
    </div>
</div>
<div id="goTips">跳转中，请稍候...</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}plugin/socket/swfobject.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/socket/web_socket.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/socket/sockjs.min.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/socket/websocket.js?times=${times}"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script>
    var socket;

    function exit() {
        if (socket) {
            socket.close();
        }
    }

    window.onunload = function (e) {
        if (socket) {
            socket.close();
        }
        return '';
    }
    var util = {
        json: function (url, data, callback) {
            return jQuery.ajax({
                'type': 'POST',
                'url': url,
                'contentType': 'application/json',
                'data': JSON.stringify(data),
                'dataType': 'json',
                'success': callback
            });
        },
        tips: function (message) {
            return $.dialog({
                closeIcon: false, content: message, title: ''
            });
        },
        success: function (message, fun, time) {
            var _autoClose = 'success';
            if (time) {
                _autoClose = 'success|' + Number(time);
            }
            var _buttons = null;
            if (fun) {
                _buttons = {success: {text: '确定', btnClass: 'btn-blue', action: fun}};
            } else {
                _buttons = {success: {text: '确定', btnClass: 'btn-blue'}};
            }
            return $.confirm({
                title: '扫码提示', content: message, animation: 'left', closeAnimation: 'right', type: 'blue',
                buttons: _buttons,
                autoClose: _autoClose
            });
        },
        getparam: function (url, name) {
            var num = url.indexOf('?');
            var str = url.substring(num + 1);
            var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
            var result = str.match(reg);
            return result ? decodeURIComponent(result[2]) : null;
        },
        getCode: function (result) {
            if (result.indexOf('scan') >= 0) {  //是新条码
                var s = result.lastIndexOf('/') + 1;
                var e = result.indexOf('?');
                if (e > 0) {
                    return result.substring(s, e);
                } else {
                    return result.substring(s);
                }
            } else if (result.indexOf('code') >= 0) {//旧条码
                return util.getparam(result, 'code');
            }
            return null;
        }
    };

    $(document).ready(function () {
        var something = $("#something");
        wx.config({
            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: something.attr("appId"), // 必填，企业号的唯一标识，此处填写企业号corpid
            timestamp: Number(something.attr("timestamp")), // 必填，生成签名的时间戳
            nonceStr: something.attr("nonceStr"), // 必填，生成签名的随机串
            signature: something.attr("signature"),// 必填，签名，见附录1
            jsApiList: ['checkJsApi', "scanQRCode"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
        wx.ready(function () {
            // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
            //打开扫一扫
            openCamera();
            setTimeout(function () {
                $("#goTips").addClass("hide").prev().removeClass("hide");
            }, 2000);
        });
        wx.error(function (res) {
            alert(JSON.stringify(res));
        });
        socket = new CustomSocket(base_host + "/backstage", "websocket/mobile", "sockjs/websocket/mobile", base_static).contact();
        socket.listener('close', 'scan_close', function (type) {
            if ('scan_close' == type) {
                try {
                    if (wx) {
                        wx.closeWindow();
                    }
                } catch (e) {
                    window.opener = null;
                    window.open('', '_self', '');
                    window.close();
                }
            }
        });

        function openCamera() {
            wx.scanQRCode({
                needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
                success: function (res) {
                    try {
                        doSomething(res.resultStr);// 当needResult 为 1 时，扫码返回的结果
                    } catch (e) {
                        alert(e.name + " : " + e.message)
                    }
                }
            });
        }

        function doSomething(result) {
            if (result.indexOf('backstage') >= 0) {
                location.href = result;
                return;
            }
            var code = util.getCode(result);
            if (!code) {
                util.success("无效二维码");
                return;
            }
            var data = {
                'userId': something.attr("userId"),
                'status': something.attr("status"),
                'code': code
            };
            var model = util.tips('扫码处理中...');
            util.json(base_url + "/backstage/receipt/scan/scan", data, function (data) {
                if (model) {
                    model.close();
                }
                if (!data.success) {
                    util.success(data.message, openCamera)

                } else {
                    util.success('扫码成功', openCamera, 2000)
                }
            });
        }

        $("#open_camera").on("click", function () {
            openCamera();
        })
        $("#exit").on("click", function () {
            try {
                if (socket) {
                    socket.close();
                }
                if (wx) {
                    wx.closeWindow();
                }
            } catch (e) {
                window.opener = null;
                window.open('', '_self', '');
                window.close();
            }
        })
    });
</script>
</html>
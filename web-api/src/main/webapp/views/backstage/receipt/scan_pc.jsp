<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-任务单管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css"
          href="${baseStatic}css/scan_pc.css?times=${times}"/>
    <link rel="stylesheet" type="text/css"
          href="${baseStatic}plugin/css/pgwslideshow.css">
    <link rel="stylesheet" type="text/css"
          href="${baseStatic}css/viewer.css?times=${times}"/>

    <style>
        .viewer-open {
            overflow: visible;
        }

        .pic-item {
            position: relative;
            width: 70%;
            margin: 0 auto;
        }

        .pgwSlideshow .ps-current .ps-prev,
        .pgwSlideshow .ps-current .ps-next {
            z-index: 2016
        }

        .viewer-fixed {
            position: absolute;
        }

        .pgwSlideshow .ps-current > ul > li img {
            opacity: 0;
            filter: alpha(opacity=0);
        }

    </style>
    <script>
        function checkFlag(val) {
            var parms = {};
            if(val == 1){
                parms.flag = true;
            }else{
                parms.flag = $("#flag").is(':checked')
            }
            $.ajax({
                type: 'POST',
                url: base_url + "/backstage/receipt/checkScanFlag",
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify(parms),
                success: function (data) { // 返回的RequestResult的json对象
                   // console.log(parms.flag);
                },
            });
        }
    </script>
</head>
<body>
<span id="something" url="${url}"></span>
<div id="show_qrcode">
    <div class="content-wrapper">
        <div class="radio-wrapper">
            <div class="radio-item active">
                <label class="text" for="scan1">回单回收</label>
                <input type="radio" name="scan_status" id="scan1" value="1" checked="checked">
            </div>
            <div class="radio-item">
                <label class="text" for="scan2">送交客户</label>
                <input type="radio" name="scan_status" id="scan2" value="2">
            </div>
            <div class="radio-item">
                <label class="text" for="scan3">退供应商</label>
                <input type="radio" name="scan_status" id="scan3" value="3">
            </div>
            <div class="radio-item">
                <label class="text" for="scan4">客户退回</label>
                <input type="radio" name="scan_status" id="scan4" value="4">
            </div>
        </div>
        <div class="code-wrapper">
            <div class="qr-item">
                <div id="qrcode_show" class="qr-img"></div>
                <p>微信扫码同步手机端回单扫描</p>
            </div>
        </div>
        <div style="display: none;">
            <div class="bs-callout bs-callout-info">
                <dl class="dl-horizontal">
                    <dt>回收下游回单</dt>
                    <dd>只能在什么状态下才能操作....</dd>
                    <dt>送审上游回单</dt>
                    <dd>只能在什么状态下才能操作....</dd>
                    <dt>退给下游回单</dt>
                    <dd>只能在什么状态下才能操作....</dd>
                    <dt>上游退回回单</dt>
                    <dd>只能在什么状态下才能操作....</dd>
                </dl>
            </div>
        </div>
    </div>
</div>
<div id="show_tips" style="display: none">
    <div class="container vertical-center">
        <div class="row">
            <div class="col-md-6 col-md-offset-3">
                <div class="tips-title">请使用手机扫描回单二维码</div>
            </div>
        </div>
        <div class="row receiptcheck">
            <div class="bs-callout bs-callout-info">
                <div class="tips-context"></div>
            </div>
        </div>
    </div>
</div>
<div id="show_details" style="display: none">
    <div id="div_details" class="div_details">
        <div class="content-info">
            <div class="info-title">
                <div class="info-title-left">
                    <h1>基本信息</h1>
                </div>

                <span id="batchNumber" style="font-size: 18px;"></span>
            </div>
            <div class="layout-line">
                <div class="line-part">
                    <span class="part-title">任务单号:</span>
                    <span class="part-text wordwrap" id="barcodeBarcode"></span>
                </div>
                <div class="line-part">
                    <span class="part-title">送货单号:</span>
                    <span class="part-text wordwrap" id="waybillDeliveryNumber"></span>
                </div>
                <div class="line-part">
                    <span class="part-title">客户名称:</span>
                    <span class="part-text" id="customerCompanyName"></span>
                </div>
            </div>
            <div class="layout-line">
                <div class="line-part">
                    <span class="part-title">联系人员:</span>
                    <span class="part-text" id="customerContacts"></span>
                </div>
                <div class="line-part">
                    <span class="part-title">手机号码:</span>
                    <span class="part-text" id="customerContactNumber"></span>
                </div>
                <div class="line-part">
                    <span class="part-title">座机号码:</span>
                    <span class="part-text" id="customerTel"></span>
                </div>
            </div>
            <div class="layout-line">
                <div class="line-part">
                    <span class="part-title">重量(kg):</span>
                    <span class="part-text" id="waybillWeight"></span>
                </div>
                <div class="line-part">
                    <span class="part-title">数&nbsp;&nbsp;&nbsp;量:</span> <span
                        class="part-text" id="waybillNumber"></span>
                </div>
                <div class="line-part">
                    <span class="part-title">体&nbsp;&nbsp;&nbsp;积:</span> <span
                        class="part-text" id="waybillVolume"></span>
                </div>
            </div>
            <div class="layout-line">
                <div class="line-part long-width">
                    <span class="part-title">收货地址:</span>
                    <span class="part-text wordwrap" id="customerFullAddress"></span>
                </div>
            </div>
        </div>
        <div class="content-pic">
            <div class="pic-title">
                <div class="pic-title-left">
                    <h1>回单信息</h1>
                </div>
                <div class="pic-title-right">
                    <div class="ptr-text">
                        <div class="ptr-text-part">
                            <span class="part-title">上传时间:</span>
                            <span class="part-text" id="receiptUploadTime"></span>
                        </div>
                        <div class="ptr-text-part">
                            <span class="part-title">上传人:</span>
                            <span class="part-text" id="receiptUploadName"></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="pic-item">
                <ul id="pgwSlideshow" class="pgwSlideshow"></ul>
                <!--  放大缩小旋转 -->
                <div class="pic-handle">
                    <span id="zoomBig" class="zoomBig"></span>
                    <span id="zoomSmall" class="zoomSmall"></span>
                    <span id="rotates" class="rotates"></span>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}plugin/socket/swfobject.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/socket/web_socket.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/socket/sockjs.min.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/socket/websocket.js?times=${times}"></script>
<script type="text/javascript" src="${baseStatic}js/Date.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/js/qrcode.min.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/js/viewer.min.js"></script>
<!-- <script type="text/javascript" src="${baseStatic}plugin/js/viewer-jquery.min.js"></script> -->
<script type="text/javascript" src="${baseStatic}plugin/js/pgwslideshow.js"></script>
<script>
    var pgwSlideshow;
    var picView; //图片放大实例化对象
    var divs = ['show_qrcode', 'show_details', 'show_tips'];

    function changeDiv(show_id) {
        for (var i = 0; i < divs.length; i++) {
            if (divs[i] == show_id) {
                $("#" + divs[i]).show();
            } else {
                $("#" + divs[i]).hide();
            }
        }
    }

    function addbig(param, obj) {
        if (param == "unqualified") {
            $(obj).children('.qualified').hide();
            $(obj).children('.unqualified').show();
            $(obj).children('.unqualified').addClass("selected").attr("flag", 0);
            $(obj).children('.qualified').removeClass("selected").removeAttr("flag");
        } else {
            $(obj).children('.qualified').show();
            $(obj).children('.unqualified').hide();
            $(obj).children('.qualified').addClass("selected").attr("flag", 1);
            $(obj).children('.unqualified').removeClass("selected").removeAttr("flag");
        }
    }


    $(document).ready(function () {
        var flag;
        var socket = new CustomSocket(base_host + "/backstage", "websocket/pc", "sockjs/websocket/pc", base_static).contact();
        socket.listener('receive', 'scan_open', function (context, type) {
            flag = parseInt(context);
            changeDiv('show_tips');
            var htmlstr = "<div style=\"text-align:center\">\n" +
                "<input type=\"checkbox\" id=\"flag\" name=\"flag\" onclick=\"checkFlag(0)\" checked>回收并送交给客户\n" +
                "</div>";
            if (flag == 1) {
                $(".receiptcheck").append("");
                $(".receiptcheck").append(htmlstr);
            }
        });
        socket.listener('receive', 'scan_reset', function (type) {
            changeDiv('show_qrcode');
        });
        socket.listener('receive', 'exception', function (context, type) {
            if ('exception' == type) {
                changeDiv('show_tips');
                var element = $('.tips-context');
                element.html('');
                /*element.append("<p>"+ context.code +"</p>");*/
                element.append("<p>" + context.exception + "</p>");
            }
        });
        socket.listener('receive', 'scan', function (context, type) {
            changeDiv('show_details');
            spanTextW();
            $("#barcodeBarcode").text(context.waybill.barcode);
            $("#waybillDeliveryNumber").text(context.waybill.deliveryNumber);
            $("#customerCompanyName").text(context.waybill.receiverName);
            $("#customerContacts").text(context.waybill.contactName);
            $("#customerContactNumber").text(context.waybill.contactPhone);
            $("#customerTel").text(context.waybill.receiverTel);
            $("#waybillWeight").text(context.waybill.weight);
            $("#waybillNumber").text(context.waybill.number);
            $("#waybillVolume").text(context.waybill.volume);
            $("#customerFullAddress").text(context.waybill.receiveAddress);
            $("#batchNumber").text(" 批次编号：" + context.batch.batchNumber);
            var div_pgwSlideshow = $("#pgwSlideshow");
            div_pgwSlideshow.html('');
            if (context.receiptList && context.receiptList.length > 0) {
                $.each(context.receiptList, function (e, node) {
                    $.each(node.images, function (i, item) {
                        //记录图片总数
                        //记录图片已审核数
                        var verifyStatus = item.verifyStatus;
                        div_pgwSlideshow.append("<li><img id='" + item.id + "' status='" + verifyStatus + "' uname='" + node.user.unamezn + "' createtime='" + item.createtime + "' src='" + context.path + item.path + "' data-large-src='" + context.path + item.path + "' alt='' data-description=''><i class='qualified'>合格</i><i class='unqualified'>不合格</i></li>");
                    });
                });

                if (!pgwSlideshow) {
                    pgwSlideshow = $('.pgwSlideshow').pgwSlideshow({
                        transitionEffect: 'fading',
                        autoSlide: false,
                        maxHeight: 500,
                        beforeSlide: function () {
                            if (picView) {
                                picView.destroy();
                            }
                        },
                        afterSlide: function (id) {
                            var picItemWidth = $('.pic-item').width();
                            var picWidth, picLeft;
                            picView = new Viewer(document.querySelector('.docs-pictures'), {
                                button: false,
                                title: false,
                                navbar: false,
                                toolbar: false,
                                transition: false,
                                viewed: function () {
                                    $('.viewer-canvas>img').css('height', '404px');
                                    picWidth = $('.viewer-canvas>img').width();
                                    picLeft = (picItemWidth - picWidth) / 2;
                                    picView.moveTo(picLeft, 0);
                                }
                            });
                            $('.docs-pictures>li>img').eq(id - 1).trigger('click');
                        }
                    });
                } else {
                    pgwSlideshow.reload({ //使用新的配置参数来重新加载轮播图插件
                        transitionEffect: 'fading',
                        autoSlide: false,
                        maxHeight: 500,
                        beforeSlide: function () {
                            if (picView) {
                                picView.destroy();
                            }
                        },
                        afterSlide: function (id) {
                            var picItemWidth = $('.pic-item').width();
                            var picWidth, picLeft;
                            picView = new Viewer(document.querySelector('.docs-pictures'), {
                                button: false,
                                title: false,
                                navbar: false,
                                toolbar: false,
                                transition: false,
                                viewed: function () {
                                    $('.viewer-canvas>img').css('height', '404px');
                                    picWidth = $('.viewer-canvas>img').width();
                                    picLeft = (picItemWidth - picWidth) / 2;
                                    picView.moveTo(picLeft, 0);
                                }
                            });
                            $('.docs-pictures>li>img').eq(id - 1).trigger('click');
                        }
                    });
                }
            }
        });
        var qrcode = null;
        $("input:radio").on("click", function () {
            var qrcode_text = $("#something").attr("url") + "/" + $(this).val();
            if (qrcode) {
                qrcode.makeCode(qrcode_text);
            } else {
                qrcode = new QRCode("qrcode_show", {
                    text: qrcode_text,
                    width: 260,
                    height: 260,
                    colorDark: "#000000",
                    colorLight: "#ffffff",
                    correctLevel: QRCode.CorrectLevel.H
                });
            }

            checkFlag(1)
            $(".tips-context").html($(this).parent().find("label").html());
        })
        $("input:radio:checked").trigger("click");

        /*
        $(window).unload(function () {
            if (socket) {
                socket.close();
                socket = null;
            }
        });
        */
        $(window).resize(function () {
            spanTextW();
        })
        //样式初始化
        spanTextW();

        $('.radio-item').click(function () {
            $(this).addClass('active').siblings().removeClass('active');
        })

        //图片放大
        document.getElementById('zoomBig').onclick = function () {
            picView.zoom(0.1);
        }
        //图片缩小
        document.getElementById('zoomSmall').onclick = function () {
            picView.zoom(-0.1);
        }
        //图片旋转
        document.getElementById('rotates').onclick = function () {
            picView.rotate(90);
        }

        //span的宽度
        function spanTextW() {
            var textWidth = $('.line-part').width() - 80,
                specialWidth = $('.line-part').outerWidth() - 110,
                longWidth = $('.long-width').outerWidth() - 90;
            $('.line-part').each(function (index, el) {
                if ($(this).hasClass('special')) {
                    $(this).children('.part-text').css('width', specialWidth);
                } else if ($(this).hasClass('long-width')) {
                    $(this).children('.part-text').css('width', longWidth);
                } else {
                    $(this).children('.part-text').css('width', textWidth);
                }
            })
        }
    });
</script>
</html>
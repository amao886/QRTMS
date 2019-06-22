<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-回单审核管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${baseStatic}plugin/css/prettify.css">
    <link rel="stylesheet" type="text/css" href="${baseStatic}plugin/css/pgwslideshow.css">
    <link rel="stylesheet" type="text/css" href="${baseStatic}css/viewer.css?times=${times}"/>
    <link rel="stylesheet" type="text/css" href="${baseStatic}css/control.css?times=${times}"/>
    <style type="text/css">
        .selectedtr {background: #e5f5ff;}
    </style>
</head>
<body>
<c:choose>
    <c:when test="${page.collection != null && fn:length(page.collection) > 0}">
    <div class="content-wrapper clearfix" style="overflow-x: auto;min-width: 1350px !important;">
        <div class="content-right">
            <div class="search-title">
                <form id="searchReceiptForm" action="${basePath}/backstage/receipt/search" method="post">
                    <div class="search-input">
                        <input type="text" class="deliveryNumber" name="likeString" value="${search.likeString}" placeholder="送货单号/客户名称">
                        <i class="close-icon"></i>
                    </div>
                    <div class="search-btn">
                        <a href="javascript:;" class="aBtn bg-blue">查询</a>
                        <a href="javascript:;" class="aBtn bg-blue">刷新</a>
                    </div>
                </form>
            </div>
            <div class="search-list table-style" id="searchList">
                <table id="billTB">
                    <thead>
                    <tr>
                        <th width="110">送货单号<span class="showTdBtn close"></span></th>
                        <th class="displayTd">客户名称</th>
                        <th width="120" class="displayTd">上传时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${page.collection}" var="waybillve">
                        <tr id="${waybillve.id}">
                            <td style="word-break:break-all">${waybillve.deliveryNumber}</td>
                            <td class="displayTd">${waybillve.receiverName }</td>
                            <td class="displayTd"><someprefix:dateTime date="${waybillve.actualArrivalTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <%-- <c:if test="${page.list != null && fn:length(page.list) > 0}">
                        <someprefix:page pageNum="${page.pageNum}" pageSize="${page.pageSize}" pages="${page.pages}" total="${page.total}"/>
                    </c:if> --%>
            </div>
        </div>
        <div class="content-left">
            <!-- 图片展示 -->
            <div class="content-pic">
                <div class="pic-title">
                    <div class="pic-title-left">
                        <h1>待审回单</h1>
                    </div>
                    <div class="pic-title-right">
                        <div class="ptr-text">
                            <div class="ptr-text-part">
                                <span class="part-title">上传时间:</span> <span class="part-text"
                                                                            id="receiptUploadTime"></span>
                            </div>
                            <div class="ptr-text-part">
                                <span class="part-title">上传人:</span> <span class="part-text"
                                                                           id="receiptUploadName"></span>
                            </div>
                        </div>
                        <div class="ptr-btn">
                            <a href="javascript:;" class="aBtn-small bg-red" id="unqualyBtn"
                               data-toggle="modal" data-target=".bs-example-modal-sm">不合格</a>
                            <a href="javascript:;" class="aBtn-small bg-green" id="qualyBtn">合格</a>
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
            <!-- 基本信息 -->
            <div class="content-info">
                <div class="info-title">
                    <div class="info-title-left">
                        <h1>基本信息</h1>
                    </div>
                    <div class="info-title-right">
                        <a href="javascript:;" class="aBtn bg-orange">确认到货</a>
                    </div>
                </div>
                <div class="layout-line">
                    <div class="line-part">
                        <span class="part-title">任务单号:</span> <span
                            class="part-text wordwrap" id="barcodeBarcode"></span>
                    </div>
                    <div class="line-part">
                        <span class="part-title">送货单号:</span> <span
                            class="part-text wordwrap" id="waybillDeliveryNumber"></span>
                    </div>
                    <div class="line-part">
                        <span class="part-title">客户名称:</span> <span class="part-text"
                                                                    id="customerCompanyName"></span>
                    </div>
                </div>
                <div class="layout-line">
                    <div class="line-part">
                        <span class="part-title">联系人员:</span> <span class="part-text"
                                                                    id="customerContacts"></span>
                    </div>
                    <div class="line-part">
                        <span class="part-title">手机号码:</span> <span class="part-text"
                                                                    id="customerContactNumber"></span>
                    </div>
                    <div class="line-part">
                        <span class="part-title">座机号码:</span> <span class="part-text"
                                                                    id="customerTel"></span>
                    </div>
                </div>
                <div class="layout-line">
                    <div class="line-part">
                        <span class="part-title">重量(kg):</span> <span class="part-text"
                                                                      id="waybillWeight"></span>
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
                    <div class="line-part">
                        <span class="part-title">任务摘要:</span> <span class="part-text"
                                                                    id="waybillOrderSummary"></span>
                    </div>
                    <div class="line-part special">
                        <span class="part-title">要求到货时间:</span> <span class="part-text"
                                                                      id="waybillArrivaltime"></span>
                    </div>
                    <div class="line-part">
                        <span class="part-title">送达时间:</span> <span class="part-text"
                                                                    id="waybillActualArrivalTime"></span>
                    </div>
                </div>
                <div class="layout-line">
                    <div class="line-part">
                        <span class="part-title">收货地址:</span> <span class="part-text" style="word-break:break-all;"
                                                                    id="customerFullAddress"></span>
                    </div>
                    <div class="line-part long-width">
                        <span class="part-title">最新位置:</span> <span class="part-text"
                                                                    style="color: #31acfa;word-break:break-all;"
                                                                    id="waybillAddress"></span>
                    </div>
                </div>
            </div>
            
            
            
            
        </div>
    </div>
    <!-- Modal -->
    <div class="modal fade bs-example-modal-sm" id="myModal" tabindex="-1" role="dialog"
         aria-labelledby="mySmallModalLabel">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-header" style="border-bottom:none;">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="mySmallModalLabel">请输入不合格原因</h4>
                </div>
                <div class="modal-body">
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" class="model-checkbox" value="不清晰">不清晰
                        </label>
                    </div>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" class="model-checkbox" value="照片不全">照片不全
                        </label>
                    </div>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" class="model-checkbox" value="上传回单照片和系统不对应">上传回单照片和系统不对应
                        </label>
                    </div>
                    <textarea class="form-control model-text" style="resize: none;" placeholder="请输入其他不合格原因"></textarea>
                </div>
                <div class="modal-footer" style="border-top:none;">
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        取消
                    </button>
                    <button type="button" id="btn-primary" class="btn btn-primary"
                            data-dismiss="modal">
                        确定
                    </button>
                </div>
            </div>
        </div>
    </div>
    </c:when>
    <c:otherwise>
        <div class="container vertical-center">
            <div class="row">
                <div class="bs-callout bs-callout-info">
                    <div class="tips-context">暂无要审核的回单</div>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>
<%@ include file="/views/include/floor.jsp" %>
<script type="text/javascript" src="${baseStatic}js/Date.js"></script>
<!--<script type="text/javascript" src="${baseStatic}js/main.js"></script>-->
<!-- <script type="text/javascript" src="${baseStatic}plugin/js/viewer-jquery.min.js"></script> -->
<script type="text/javascript" src="${baseStatic}plugin/js/viewer.min.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/js/pgwslideshow.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/js/prettify.js"></script>
<script type="text/javascript" src="${baseStatic}plugin/js/jquery.slimscroll.js"></script>
<script>
    var pgwSlideshow;
    var picView; //图片放大实例化对象
    $(function () {
        //初始化宽度
        spanTextW();
        //tableHeight();

        $(window).resize(function () {
            spanTextW();
            //tableHeight();
        });
        //点击合格按钮
        $('#qualyBtn').on('click', function () {
            if (waybillid == 0) { //无数据时不允许操作
                return;
            }
            var addressId = $(".ps-selected").children("img").attr("id");
            var param = {};
            param.status = 1;
            param.wayBillId = waybillid;
            param.addressId = addressId;
            verify(param, "qualified");
        })
        //点击不合格按钮
        $('#btn-primary').on('click', function () {
            if (waybillid == 0) { //无数据时不允许操作
                return;
            }
            var remark = "";
            $("input:checked").each(function () {
                remark += $(this).val() + ",";
            });
            remark += $(".model-text").val();
            if ($.trim(remark) == "") {
                $.util.success("请填写不合格原因！");
                return;
            }
            var addressId = $(".ps-selected").children("img").attr("id");
            var param = {};
            param.status = 0;
            param.wayBillId = waybillid;
            param.addressId = addressId;
            param.remark = remark;
            verify(param, "unqualified");
        })
        //清除弹出框选中的checkbox
        $('#myModal').on('show.bs.modal', function () {
            $('.model-checkbox').prop('checked', false);
            $('.model-text').val('');
        })

        //回单审核
        function verify(param, org) {
            $.util.json(base_url + '/backstage/receipt/verify', param, function (data) {
                if (data.success) { //处理返回结果
                    $.util.success(data.message);
                    addTag(org);
                } else {
                    $.util.success(data.message);
                    return false;
                }
            });
        }

        function addTag(obj) {
            var index = pgwSlideshow.getCurrentSlide();
            //获取操作前属性
            var flag = $.util.getvalue($('.ps-item').eq(index - 1).children(".selected").attr("flag"));
            if (obj == "unqualified") {
                if (flag == "") {
                    ++noverCount;
                    ++verCount;
                }
                if (flag != "" && Number(flag) == 1) {
                    ++noverCount;
                }
                $('.ps-item').eq(index - 1).children('.qualified').hide();
                $('.ps-item').eq(index - 1).children('.' + obj).show();
                $('.ps-item').eq(index - 1).children('.' + obj).addClass("selected").attr("flag", 0);
                $('.ps-item').eq(index - 1).children('.qualified').removeClass("selected").removeAttr("flag");
                $('.imgselected').children('.qualified-big').hide();
                $('.imgselected').children('.' + obj + '-big').show();
            } else {
                if (flag == "") {
                    ++verCount;
                }
                if (flag != "" && Number(flag) == 0) {
                    --noverCount;
                }
                $('.ps-item').eq(index - 1).children('.unqualified').hide();
                $('.ps-item').eq(index - 1).children('.' + obj).show();
                $('.ps-item').eq(index - 1).children('.' + obj).addClass("selected").attr("flag", 1);
                $('.ps-item').eq(index - 1).children('.unqualified').removeClass("selected").removeAttr("flag");
                $('.imgselected').children('.unqualified-big').hide();
                $('.imgselected').children('.' + obj + '-big').show();
            }
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

        //右边表格高度
        function tableHeight() {
            var tableHeight = $(window).height() - 71;
            $('#searchList').height(tableHeight);
            $('#searchList').slimscroll({
                height: tableHeight
            });
        }

        //当前条数小于等于23条且页码总数等于1时不显示下一页按钮
        if (endRow <= 23 && pages <= 1) {
            $(".next-btn").hide();
        }
        if (endRow == 0) {
            $.util.success("无待审核回单！");
        }

        //初始化选中数据
        $("table tbody tr:first").trigger("click");
    })

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

    var allCount = 0; //总图片张数
    var verCount = 0; //已审核张数
    var waybillid = 0; //任务单id
    var noverCount = 0;//不合格张数
    var confirmCount = 0;//记录确认到货条数
    var endRow = ${page.endRow};//回单审核当前页最后一天记录数
    var pages = ${page.pages};//总页码数
    //确认到货操作
    $(".bg-orange").on('click', function () {
        if (waybillid == null || waybillid == 0) { //无数据时不允许操作
            return;
        }
        if (allCount == verCount) {//审核完
            if (noverCount > 0) {//不合格
                $.util.confirm("提示", "任务单中有审核不合格的回单，是否确认？", confirmTask);
            } else {
                $.util.confirm("提示", "是否确认到货操作", confirmTask);
            }
        } else {
            if (noverCount > 0) {
                $.util.confirmWarning("提示", "任务单中有审核不合格和未审核的回单，是否确定该任务单？", confirmTask);

            } else {
                $.util.confirmWarning("提示", "任务单中有未审核的回单，是否确认该任务单且回单都合格？", confirmTask);
            }
        }


    });

    function confirmTask() {
        $.util.json(base_url + '/backstage/trace/confirm/receive', { waybillIds: waybillid}, function (data) {
            if (data.success) { //处理返回结果
                var _index = parseInt($("#billTB tbody").find("tr").index($("#" + waybillid))),
                    _len = $("#billTB tbody").find("tr").length;
                $("#" + waybillid).remove();
                if (_len > 1) {
                    if (_index != (_len - 1)) {//不是最后一个
                        $("#billTB tbody").find("tr").eq(_index).trigger("click");
                    } else {
                        $("#billTB tbody").find("tr").eq(0).trigger("click");
                    }
                }
                confirmCount++;
                if (confirmCount > 10) { //确认到货10次后跟新列表
                    $.util.successfuc(data.message, function () {
                        $("#searchReceiptForm").submit();
                    });
                } else {
                    $.util.success(data.message);
                }
            } else {
                $.util.error(data.message);
            }
        });
    }

    $(".bg-blue").on("click", function () {
        $("#searchReceiptForm").submit();
    });
    $(".close-icon").on("click", function () {
        $(".deliveryNumber").val('');
    });

    $("tbody tr").on("click", function () {
        $(this).children().addClass("selectedtr").parent().siblings().children().removeClass("selectedtr");
        findById($(this).attr("id"));
    });
    
    //图片放大
    document.getElementById('zoomBig').onclick = function() {
    	picView.zoom(0.1);
	}    
    //图片缩小
    document.getElementById('zoomSmall').onclick = function() {
    	picView.zoom(-0.1);
	}   
    //图片旋转
    document.getElementById('rotates').onclick = function() {
    	picView.rotate(90);
	}

    //任务详情
    function findById(wayBillId) {
        cleanParam();
        //	$("#pgwSlideshow").html('');
        $.util.json(base_url + '/backstage/receipt/queryInfo/' + wayBillId, {}, function (data) {
            if (data.success) { //处理返回结果
                waybillid = data.waybill.id;
                $("#barcodeBarcode").text($.util.getvalue(data.waybill.barcode));
                $("#waybillDeliveryNumber").text($.util.getvalue(data.waybill.deliveryNumber));
                $("#customerCompanyName").text($.util.getvalue(data.waybill.receiverName));
                $("#customerContacts").text($.util.getvalue(data.waybill.contactName));
                $("#customerContactNumber").text($.util.getvalue(data.waybill.contactPhone));
                $("#customerTel").text($.util.getvalue(data.waybill.customerTel));
                $("#waybillWeight").text($.util.getvalue(data.waybill.weight));
                $("#waybillNumber").text($.util.getvalue(data.waybill.number));
                $("#waybillVolume").text($.util.getvalue(data.waybill.volume));
                $("#waybillOrderSummary").text($.util.getvalue(data.waybill.orderSummary));
                $("#waybillArrivaltime").text(DateTimeToString($.util.getvalue(data.waybill.arrivaltime)));
                $("#waybillActualArrivalTime").text(DateTimeToString($.util.getvalue(data.waybill.actualArrivalTime)));
                $("#customerFullAddress").text($.util.getvalue(data.waybill.receiveAddress));
                var address = $.util.getvalue(data.waybill.address);
                if(address){
                    $("#waybillAddress").text(address);
                }else{
                    $("#waybillAddress").text("");
                }
                //加载图片数据
                var div_pgwSlideshow = $("#pgwSlideshow");
                div_pgwSlideshow.html('');
                if(data.results && data.results.length > 0){
                    $.each(data.results, function (e, node) {
                        $.each(node.images, function (i, item) {
                            //记录图片总数
                            ++allCount;
                            //记录图片已审核数
                            var verifyStatus = $.util.getvalue(item.verifyStatus);
                            if (verifyStatus != "" || verifyStatus == "0") {
                                ++verCount;
                                if (Number(verifyStatus) != 1) {
                                    ++noverCount;
                                }
                            }
                            div_pgwSlideshow.append("<li><img id='" + item.id + "' status='" + verifyStatus + "' uname='" + $.util.getvalue(node.user.mobilephone) + "' createtime='" + item.createtime + "' src='" + data.path + item.path + "' data-large-src='" + data.path + item.path + "' alt='' data-description=''><i class='qualified'>合格</i><i class='unqualified'>不合格</i></li>");
                        });
                    });
                    if (!pgwSlideshow) {
                        pgwSlideshow = $('.pgwSlideshow').pgwSlideshow({
                            transitionEffect: 'fading',
                            autoSlide: false,
                            maxHeight: 500,
                            beforeSlide:function(){
                                if(picView){
                                    picView.destroy();
                                }
                            },
                            afterSlide: function (id) {
                                var picItemWidth = $('.pic-item').width();
                                var picWidth,picLeft;
                                picView = new Viewer(document.querySelector('.docs-pictures'),{
                                    button:false,
                                    title:false,
                                    navbar:false,
                                    toolbar:false,
                                    transition:false,
                                    viewed:function(){
                                        $('.viewer-canvas>img').css('height','404px');
                                        picWidth = $('.viewer-canvas>img').width();
                                        picLeft = (picItemWidth-picWidth)/2;
                                        picView.moveTo(picLeft,0);
                                    }
                                });
                                $('.docs-pictures>li>img').eq(id-1).trigger('click');
                                
                            }
                        });
                        //初始化图片查看器插件
                        //$('.docs-pictures').viewer();
                    } else {
                        pgwSlideshow.reload({ //使用新的配置参数来重新加载轮播图插件
                            transitionEffect: 'fading',
                            autoSlide: false,
                            maxHeight: 500,
                            beforeSlide:function(){
                                if(picView){
                                    picView.destroy();
                                }
                            },
                            afterSlide: function (id) {
                                var picItemWidth = $('.pic-item').width();
                                var picWidth,picLeft;
                                picView = new Viewer(document.querySelector('.docs-pictures'),{
                                    button:false,
                                    title:false,
                                    navbar:false,
                                    toolbar:false,
                                    transition:false,
                                    viewed:function(){
                                        $('.viewer-canvas>img').css('height','404px');
                                        picWidth = $('.viewer-canvas>img').width();
                                        picLeft = (picItemWidth-picWidth)/2;
                                        picView.moveTo(picLeft,0);
                                    }
                                });
                                $('.docs-pictures>li>img').eq(id-1).trigger('click');
                                
                            }
                        });
                        //$('.docs-pictures').viewer("destroy");
                        //$('.docs-pictures').viewer();
                    }
                }
            } else { //处理返回结果
                $.util.error(data.message);
            }
        });
    }

    function cleanParam() {
        allCount = 0; //总图片张数
        verCount = 0; //已审核张数
        waybillid = 0; //任务单id
        noverCount = 0;//不合格张数
    }
    
    $(".showTdBtn").click(function(){
    	if($(this).hasClass("open")){
    		$(this).removeClass("open").addClass("close");	
    		$(".displayTd").show();
    	}else {
    		$(this).removeClass("close").addClass("open");
    		$(".displayTd").hide();
    	}   	
    });
</script>
</body>
</html>
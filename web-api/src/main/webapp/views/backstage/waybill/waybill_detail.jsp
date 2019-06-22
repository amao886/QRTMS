<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>物流跟踪-任务单管理</title>
    <%@ include file="/views/include/head.jsp" %>
    <link rel="stylesheet" href="${baseStatic}css/viewer.css"/>
    <link rel="stylesheet" href="${baseStatic}css/billDetails.css?times=${times}"/>
    <link rel="stylesheet" href="${baseStatic}css/waybill_detail.css?times=${times}"/>
</head>
<body>
<div class="detail-content" id="main">
    <h2 class="title-style">任务单详情</h2>
    <div class="arrive-style" style="position:absolute;top:10px;right: 40px;">
    	<span>
            <c:if test="${waybill.waybillStatus== 10 }">未绑定</c:if>
            <c:if test="${waybill.waybillStatus== 20 }">已绑定</c:if>
            <c:if test="${waybill.waybillStatus== 30 }">运输中</c:if>
            <c:if test="${waybill.waybillStatus== 35 }">已送达</c:if>
            <c:if test="${waybill.waybillStatus== 40 }">确认到货</c:if>
        </span>
        <c:if test="${waybill.waybillStatus > 10 && waybill.waybillStatus < 40}">
            <button class="layui-btn layui-btn-normal daohuoBt">
                <span data-waybillid="${waybill.id}" data-total="${waybill.receiptCount}" data-count="${waybill.receiptVerifyCount + waybill.receiptUnqualifyCount}">确认到货</span>
            </button>
        </c:if>
    </div>
    <!-- 发货信息 -->
    <div class="sendGoods">
        <div class="line-wrapper">
            <div class="line-part" style="width:220px;">
                <span class="float-tag">任务单号：</span>
                <div class="text-tag"><p class="p-tag">${waybill.barcode}</p></div>
            </div>
            <div class="line-part" style="width:220px;margin-left:40px;">
                <span class="float-tag">送货单号：</span>
                <div class="text-tag"><p class="p-tag">${waybill.deliveryNumber}</p></div>
            </div>
        </div>
        <ul class="ul-wrapper">
            <li>
                <h2>发货信息</h2>
                <div class="line-part">
                    <span class="float-tag">货主名称：</span>
                    <div class="text-tag"><p class="p-tag">${waybill.shipperName}</p></div>
                </div>
                <div class="line-part">
                    <span class="float-tag">联系人：</span>
                    <div class="text-tag"><p class="p-tag">${waybill.shipperContactName }</p></div>
                </div>
                <div class="line-part">
                    <span class="float-tag">联系电话：</span>
                    <div class="text-tag"><p class="p-tag">${waybill.shipperContactTel}</p></div>
                </div>
                <div class="line-part">
                    <span class="float-tag">固定电话：</span>
                    <div class="text-tag"><p class="p-tag">${waybill.shipperTel}</p></div>
                </div>
                <div class="line-part">
                    <span class="float-tag">发货地址：</span>
                    <div class="text-tag"><p class="p-tag">${fn:replace(waybill.startStation, '-', '')}${waybill.shipperAddress}</p></div>
                </div>
            </li>
            <li>
                <h2>收货信息</h2>
                <div class="line-part">
                    <span class="float-tag">收货客户：</span>
                    <div class="text-tag"><p class="p-tag">${waybill.receiverName}</p></div>
                </div>
                <div class="line-part">
                    <span class="float-tag">联系人：</span>
                    <div class="text-tag"><p class="p-tag">${waybill.contactName}</p></div>
                </div>
                <div class="line-part">
                    <span class="float-tag">联系电话：</span>
                    <div class="text-tag"><p class="p-tag">${waybill.contactPhone}</p></div>
                </div>
                <div class="line-part">
                    <span class="float-tag">固定电话：</span>
                    <div class="text-tag"><p class="p-tag">${waybill.receiverTel}</p></div>
                </div>
                <div class="line-part">
                    <span class="float-tag">收货地址：</span>
                    <div class="text-tag"><p class="p-tag">${fn:replace(waybill.endStation, '-', '')}${waybill.receiveAddress}</p></div>
                </div>
            </li>
            <li>
                <h2>发货要求</h2>
                <div class="line-part">
                    <span class="float-tag">发货时间：</span>
                    <div class="text-tag"><p class="p-tag"><c:if test="${waybill.deliveryTime != null}">
                        <someprefix:dateTime date="${waybill.deliveryTime}" pattern="yyyy-MM-dd HH:mm"/>
                    </c:if></p></div>
                </div>
                <div class="line-part large-tag">
                    <span class="float-tag">要求到货时间：</span>
                    <div class="text-tag">
                        <p class="p-tag">
                            <c:if test="${waybill.arrivaltime != null}">
                                <c:if test="${waybill.waybillStatus == 10}">
                                    发货后${waybill.arriveDay}天${waybill.arriveHour}点前
                                </c:if>
                                <c:if test="${waybill.waybillStatus > 10}">
                                    <someprefix:dateTime date="${waybill.arrivaltime}" pattern="yyyy-MM-dd HH:mm"/>
                                </c:if>
                            </c:if>
                        </p>
                    </div>
                </div>
                <div class="line-part">
                    <span class="float-tag">订单摘要：</span>
                    <div class="text-tag"><p class="p-tag">${waybill.orderSummary}</p></div>
                </div>
            </li>
            <li>
                <h2>其他信息</h2>
                <div class="line-part">
                    <span class="float-tag">创建时间：</span>
                    <div class="text-tag"><p class="p-tag"><c:if test="${waybill.createtime != null}">
                        <someprefix:dateTime date="${waybill.createtime}" pattern="yyyy-MM-dd HH:mm"/>
                    </c:if></p></div>
                </div>
                <c:if test="${waybill.group != null}">
                    <div class="line-part">
                        <span class="float-tag">项目组：</span>
                        <div class="text-tag"><p class="p-tag">${waybill.group.groupName}</p></div>
                    </div>
                </c:if>
                <div class="line-part">
                    <c:if test="${waybill.startStation != null && waybill.startStation != ''}">
                        <span class="float-tag">始发地：</span>
                        <div class="text-tag"><p class="p-tag">${waybill.startStation}</p></div>
                    </c:if>
                    <c:if test="${waybill.endStation != null && waybill.endStation != ''}">
                        <span class="float-tag">目的地：</span>
                        <div class="text-tag"><p class="p-tag">${waybill.endStation}</p></div>
                    </c:if>
                </div>
            </li>
        </ul>
    </div>


    <!-- 货物信息 -->
    <div class="dcontent-part">
        <h2>货物信息</h2>
        <div class="table-style">
            <table>
                <thead>
                <tr>
                    <th>客户料号</th>
                    <th>物料名称</th>
                    <th>重量/kg</th>
                    <th>体积/m³</th>
                    <th>数量/件</th>
                    <th>货物摘要</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${waybill.goods == null || fn:length(waybill.goods) <= 0}">
                    <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                </c:if>
                <c:forEach items="${waybill.goods}" var="goods">
                    <tr>
                        <td>${goods.goodsType}</td>
                        <td>${goods.goodsName}</td>
                        <td>${goods.goodsWeight}</td>
                        <td>${goods.goodsVolume}</td>
                        <td>${goods.goodsQuantity}</td>
                        <td>${goods.summary}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <!-- 回单信息 -->
    <div class="dcontent-part">
        <h2>回单信息</h2>
        <div class="track-con">
            <c:if test="${waybill.receipts == null || fn:length(waybill.receipts) <= 0}">
                <div class="track-sub-part track-con-bg">暂无回单信息</div>
            </c:if>
            <c:forEach items="${waybill.receipts}" var="receipt">
                <div class="track-sub-part track-con-bg">
                    <div class="clearfix">
                        <div class="fl">
                            <span class="spa_n">上传时间:</span>
                            <p class="text_p">
                                <c:if test="${receipt.createtime != null}">
                                    <someprefix:dateTime date="${receipt.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </c:if>
                            </p>
                        </div>
                        <div class="fl">
                            <span class="spa_n">上传人:</span>
                            <p class="text_p">
                                <c:if test="${receipt.user != null }">
                                    ${receipt.user.mobilephone }
                                </c:if>
                            </p>
                        </div>
                    </div>
                    <div class="bill_test">
                        <ul class="clearfix paddin_g docs-pictures">
                            <c:forEach items="${receipt.images}" var="image">
                                <li>
                                    <img src="${imagePath}${image.path}" alt="回单上传图片" title="${image.verifyRemark}">
                                    <c:if test="${image.verifyStatus != null && image.verifyStatus == 1}">
                                        <span class="img-status">合格</span>
                                    </c:if>
                                    <c:if test="${image.verifyStatus != null && image.verifyStatus == 0}">
                                        <span class="img-status img-unqualified">不合格</span>
                                    </c:if>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- 异常情况 -->
    <div class="dcontent-part">
        <h2>异常情况</h2>
        <div class="track-con">
            <c:if test="${waybill.exceptions == null || fn:length(waybill.exceptions) <= 0}">
                <div class="track-sub-part track-con-bg">没有异常信息</div>
            </c:if>
            <c:forEach items="${waybill.exceptions}" var="exception">
                <div class="track-sub-part track-con-bg">
                    <div class="clearfix">
                        <div class="fl">
                            <span class="spa_n">异常情况:</span>
                            <p class="text_p">${exception.content }</p>
                        </div>
                        <div class="fl">
                            <span class="spa_n">任务摘要:</span>
                            <p class="text_p">${waybill.orderSummary }</p>
                        </div>
                    </div>
                    <div class="clearfix">
                        <div class="fl">
                            <span class="spa_n">上报时间:</span>
                            <p class="text_p">
                                <c:if test="${exception.createtime != null}">
                                    <someprefix:dateTime date="${exception.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </c:if>
                            </p>
                        </div>
                        <div class="fl">
                            <span class="spa_n">上报人:</span>
                            <p class="text_p">
                                <c:if test="${exception.user != null}">
                                    ${exception.user.mobilephone}
                                </c:if>
                            </p>
                        </div>
                    </div>
                    <div class="bill_test">
                        <ul class="clearfix paddin_g docs-pictures">
                            <c:forEach items="${exception.images}" var="image">
                                <li><img src="${imagePath}${image.path }" alt="异常上传图片"></li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
</body>
<%@ include file="/views/include/floor.jsp" %>
<!-- <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.4.0&key=a4e0b28c9ca0d166b8872fc3823dbb8a"></script>-->
<!-- <script src="http://webapi.amap.com/ui/1.0/main.js?v=1.0.11"></script>-->
<script type="text/javascript" src="${baseStatic}js/viewer.min.js"></script>
<script type="text/javascript" src="${baseStatic}js/date.extend.js"></script>
<script>
    $(document).ready(function () {
        /*
        var map = new AMap.Map('container', {resizeEnable: true, zoom: 15});
        AMapUI.loadUI(['overlay/SimpleInfoWindow', 'misc/PathSimplifier'], function (SimpleInfoWindow, PathSimplifier) {
            map.plugin(['AMap.ToolBar', 'AMap.Scale', 'AMap.OverView', 'AMap.MapType'], function () {
                map.addControl(new AMap.ToolBar());//集成了缩放、平移、定位等功能按钮在内的组合控件
                map.addControl(new AMap.Scale());//展示地图在当前层级和纬度下的比例尺
                map.addControl(new AMap.OverView({isOpen: true}));//在地图右下角显示地图的缩略图
            });
            var offset = new AMap.Pixel(-12, -12);
            var infoWindow = new SimpleInfoWindow({
                offset: new AMap.Pixel(0, -12)
            });
            var items = $(".track li"), positions = [];
            if (items && items.length > 0) {
                var carIcon = new AMap.Icon({
                    'size': new AMap.Size(70, 32), //图标尺寸
                    'image': $(".map_marker_icon").attr("src") //	图标的取图地址。默认为蓝色图钉图片
                });
                $(items).each(function (index, element) {
                    var position = [$(element).attr("longitude"), $(element).attr("latitude")];
                    var marker = new AMap.Marker({'offset': offset, 'map': map, 'position': position});
                    marker.setContent("<div class='map_icon_blue'></div>");
                    marker.setExtData(element);
                    marker.on('click', function () {
                        var e = this.getExtData();
                        infoWindow.setInfoTitle($(e).find('.infoTitle').clone());
                        infoWindow.setInfoBody($(e).find('.infoBody').clone());
                        infoWindow.open(map, this.getPosition());
                    });
                    positions.unshift(position);
                    if (index == 0) {
                        map.setCenter(position);// 圆心位置
                        infoWindow.setInfoTitle($(element).find('.infoTitle').clone());
                        infoWindow.setInfoBody($(element).find('.infoBody').clone());
                        infoWindow.open(map, marker.getPosition());
                    }
                });
            }
            var citem = $("#customer_info");
            var position = [$(citem).attr("longitude"), $(citem).attr("latitude")];
            if (position && position[0] && position[1]) {
                var position = [$(citem).attr("longitude"), $(citem).attr("latitude")];
                var customer = new AMap.Marker({'offset': offset, 'position': position, 'map': map});
                customer.setContent("<div class='map_icon_red'></div>");
                customer.setExtData(citem);
                customer.on('click', function () {
                    var e = this.getExtData();
                    infoWindow.setInfoTitle($(e).find('.infoTitle').clone());
                    infoWindow.setInfoBody($(e).find('.infoBody').clone());
                    infoWindow.open(map, this.getPosition());
                });
                positions.push(position);
                var fenceStatus = $(citem).attr("fenceStatus");
                if (fenceStatus && fenceStatus == 1) {
                    var circle = new AMap.Circle({
                        radius: $(citem).attr("fenceRadius") * 1000, //半径
                        strokeColor: "#3366FF", //线颜色
                        strokeOpacity: 0.8, //线透明度
                        strokeWeight: 1, //线粗细度
                        fillColor: "#1791fc", //填充颜色
                        fillOpacity: 0.2//填充透明度
                    });
                    circle.setCenter(position);// 圆心位置
                    circle.setMap(map);
                    circle.show();
                }
            }
            if (positions.length > 1) {//轨迹数量大于1
                if (PathSimplifier.supportCanvas) {
                    var pathSimplifierIns = new PathSimplifier({
                        zIndex: 100,
                        autoSetFitView: true,
                        map: map, //所属的地图实例
                        getPath: function (pathData, pathIndex) {
                            return pathData.path;
                        },
                        renderOptions: {
                            getPathStyle: function (item, zoom) {
                                var color = '#00CD00';
                                return {
                                    pathLineStyle: {strokeStyle: color},
                                    pathNavigatorStyle: {
                                        fillStyle: color,
                                        strokeStyle: color,
                                        lineWidth: 1,
                                        pathLinePassedStyle: {strokeStyle: color}
                                    }
                                }
                            }
                        },
                        data: [{name: '运输轨迹', path: positions.slice(positions.length - 2)}]
                    });
                    //对第一条线路（即索引 0）创建一个巡航器
                    var navigator = pathSimplifierIns.createPathNavigator(0, {
                        loop: true, //循环播放
                        speed: 500 //巡航速度，单位千米/小时
                    });
                    navigator.start();
                }
                if (positions.length > 2) {
                    var polyline = new AMap.Polyline({
                        'strokeColor': "#3366FF", //线颜色
                        'strokeOpacity': 1,       //线透明度
                        'strokeWeight': 3,        //线宽
                        'strokeDasharray': [10, 5], //补充线样式
                        'lineJoin': 'round' //折线拐点的绘制样式
                    });
                    polyline.setPath(positions.slice(0, positions.length - 1));//设置线覆盖物路径
                    polyline.setMap(map);
                }
            }
            map.setFitView();
        });
        */

        //确认到货事件
        $(".daohuoBt").on("click", function () {
            var my = $(this);
            var span = my.find("span");
            var waybillid = span.data("waybillid"), total = span.data("total"), count = span.data("count");
            var cal_btn, msg;
            if (total && Number(total) > 0) {
                msg = '是否确认到货?';
                if (Number(total) > Number(count)) {
                    msg = '有'+ (Number(total) - Number(count)) +'张回单未审核,确认后回单自动设置为合格,是否确认到货?'
                    cal_btn = {
                        text: '前往回单审核', action: function () {
                            location.href = base_url + '/backstage/receipt/search?waybill.id=' + waybillid;
                        }
                    };
                }
            } else {
                msg = '还未上传回单,是否确认到货?';
            }
            $.util.confirm('到货确认', msg, function () {
                $.util.json(base_url + '/backstage/trace/confirm/receive', {waybillIds: waybillid}, function (data) {
                    if (data.success) {//处理返回结果
                        $(my).parent().html('确认到货');
                        $("#span_status").html("确认到货");
                    } else {
                        $.util.error(data.message);
                    }
                });
            }, cal_btn);
        });
        $(".docs-pictures").viewer({
            fullscreen: false
        });
        
        $('.ul-wrapper>li').height(maxValue());        
        //动态给li赋值
        function maxValue(){
        	var arr = [];
        	$('.ul-wrapper>li').each(function(index,el){
                arr.push($(this).height());
            })
        	arr.sort(function(n1,n2){return n2-n1;});
        	return arr[0];
        }
    });
</script>
</html>
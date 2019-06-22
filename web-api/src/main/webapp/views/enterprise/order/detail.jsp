<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">

	<head>
		<title>物流跟踪-订单管理-订单详情</title>
		<%@ include file="/views/include/head.jsp" %>
		<link rel="stylesheet" href="${baseStatic}element-ui/lib/theme-chalk/index.css"/>
		<link rel="stylesheet" href="${baseStatic}css/shipperDetail.css?times=${times}" />
         <style>
			 .el-collapse {
				 padding-left: 15px;
			 }

			 .el-collapse-item__arrow {
				 float: left;
				 margin-right: 15px;
			 }

			 .date {
				 margin-left: 10px;
				 margin-right: 30px;
			 }

			 .status {
				 display: inline-block;
				 width: 120px;
			 }

			.positionInfo {
				padding-left: 330px;
			}
			 .concats {

				 text-align: center;
				 display: inline-block;
				 width: 100px;
			 }
			 .line-detail .travelPosition {
				 top: 15px;
				 right: 210px;
				 font-size: 18px;
			 }
		 </style>
	</head>

	<body>
		<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
		<!--[if lt IE 9]>
<script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
		<%--物流跟踪-订单管理-订单详情--%>
		<%--订单ID - <span>${orderKey}</span>--%>
		<input type="hidden" value="${orderKey}" id="orderKey" data-share="${share}">
		<div class="orderWrapper" id="orderWrapper">
			<template>
			<div class="line-wrapper line-detail">
				<div class="line-part">
					<span class="float-tag">系统单号：</span>
					<div class="text-tag">
						<p class="p-tag" v-if="order.bindCode != null">{{order.bindCode}}</p>
						<p class="p-tag" v-else v-else style="color: #adadad;">未绑码</p>
					</div>
				</div>
				<div class="line-part">
					<span class="float-tag">送货单号：</span>
					<div class="text-tag">
						<p class="p-tag">
							{{order.deliveryNo}}
							<%--<span class="deliveryNo">{{order.deliveryNo}}</span>--%>
							<%--<a href="javascript:;" @click="editDeliveryNo(order.deliveryNo,order.id)" class="edit"> 编辑</a>--%>
						</p>
						<%--对话框--%>
						<el-dialog
								title="送货单号"
								:visible.sync="dialogVisible"
								width="30%"
								:before-close="handleClose">
							<input type="text" v-model="deliveryNo" :orderKey= 'orderKey'>
							<span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="submitDeliveryNo">确 定</el-button>
              </span>
						</el-dialog>
					</div>
				</div>

				<%--<div class="line-part">
             <span class="float-tag">订单编号：</span>
             <div class="text-tag"><p class="p-tag">{{order.orderNo}}</p></div>
         </div>--%>
				<div class="line-part">
					<span class="float-tag">送货日期：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.deliveryTime | dateFormat}}</p>
					</div>
				</div>
				<span class="travelPosition" v-if="order.fettle==3">已到货</span>
				<span class="travelPosition" v-if="order.fettle==4">已签收</span>
				<span class="travelPosition" v-else>运输中</span>
			</div>
			<div class="part-wrapper">
				<div class="title">位置跟踪</div>
				<%--<ul class="trace-wrap" v-if="operates != null && operates.length > 0">--%>
					<%--<li v-for="o in operates">--%>
						<%--<span class="time">{{o.createTime | datetime}}</span>--%>
						<%--<span class="location">{{o.logContext}} &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>--%>
						<%--<span class="location" v-if="o.user != null">{{o.user.unamezn}} &nbsp;&nbsp;&nbsp;</span>--%>
						<%--<span class="location" v-if="o.user != null">{{o.user.mobilephone}} </span>--%>
					<%--</li>--%>
				<%--</ul>--%>
				<el-collapse accordion v-if="operates != null && operates.length > 0">
					<el-collapse-item v-for="o in operates">
						<template slot="title">
							<span class="date">{{o.createTime | datetime}}</span>
							<span class="status" v-if="o.logType==1">录入发货单</span>
                            <span class="status" v-else-if="o.logType==12">导入发货单</span>
                            <span class="status" v-else-if="o.logType==13">生成发货单</span>
                            <span class="status" v-else-if="o.logType==14">系统传输发货单</span>
                            <span class="status" v-else-if="o.logType==2">扫码绑单</span>
                            <span class="status" v-else-if="o.logType==3">上报位置</span>
                            <span class="status" v-else-if="o.logType==4">上传回单</span>
                            <span class="status" v-else-if="o.logType==9">已签收</span>
                            <span class="status" v-else>装车完成</span>
							<span class="position">{{o.area}}</span>
						</template>
						<div class="positionInfo">
							<span class="txt">{{o.logContext}}</span>
							<span class="concats">{{o.user.unamezn}}</span>
							<span class="concatsNumber">{{o.user.mobilephone}}</span>
						</div>

					</el-collapse-item>

				</el-collapse>
			</div>
			<div class="part-wrapper" v-if="convey != null">
				<div class="title">承运信息</div>
				<div class="line-part large-tag">
					<span class="float-tag">承运商名称：</span>
					<div class="text-tag">
						<p class="p-tag">{{convey.companyName}}</p>
					</div>
				</div>
				<!--额外数据-->
				<div class="line-part large-tag" v-if="order.extra != null && order.extra.conveyerName != null && order.extra.conveyerName != ''">
					<span class="float-tag" v-if="order.extra.conveyerName != null">联系人：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.extra.conveyerName}}</p>
					</div>
				</div>
				<div class="line-part large-tag" v-if="order.extra != null && order.extra.conveyerContact != null && order.extra.conveyerContact != ''">
					<span class="float-tag" v-if="order.extra.conveyerContact != null">联系电话：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.extra.conveyerContact}}</p>
					</div>
				</div>
				<div class="line-part large-tag" v-if="order.extra != null && order.extra.careNo != null && order.extra.careNo != ''">
					<span class="float-tag" v-if="order.extra.careNo != null">车牌号：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.extra.careNo}}</p>
					</div>
				</div>
				<div class="line-part large-tag" v-if="order.extra != null && order.extra.driverName != null && order.extra.driverName != ''">
					<span class="float-tag" v-if="order.extra.driverName != null">司机姓名：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.extra.driverName}}</p>
					</div>
				</div>
				<div class="line-part large-tag" v-if="order.extra != null && order.extra.driverContact != null && order.extra.driverContact != ''">
					<span class="float-tag" v-if="order.extra.driverContact != null">司机电话：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.extra.driverContact}}</p>
					</div>
				</div>
			</div>
			<div class="part-wrapper">
				<div class="title">收货信息</div>
				<div class="line-part">
					<span class="float-tag">收货客户：</span>
					<div class="text-tag">
						<p class="p-tag">{{receive.companyName}}</p>
					</div>
				</div>
				<div class="line-part">
					<span class="float-tag">联系人：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.receiverName}}</p>
					</div>
				</div>
				<div class="line-part">
					<span class="float-tag">联系方式：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.receiverContact}}</p>
					</div>
				</div>
				<div class="line-part">
					<span class="float-tag">收货地址：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.receiveAddress}}</p>
					</div>
				</div>
			</div>
			<div class="part-wrapper">
				<div class="title">货物信息</div>
				<div class="table-style">
					<table>
						<thead>
							<tr>
								<th>物料编号</th>
								<th width="20%">产品描述</th>
								<th>单位</th>
								<th>数量</th>
								<th>箱数</th>
								<th>体积</th>
								<th>重量</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody v-if="commodities != null && commodities.length > 0">
							<tr v-for="h in commodities">
								<td>{{h.commodityNo}}</td>
								<td>{{h.commodityName}}</td>
								<td>{{h.commodityUnit}}</td>
								<td>{{h.quantity}}</td>
								<td>{{h.boxCount}}</td>
								<td>{{h.volume | numberFixed}}</td>
								<td>{{h.weight | numberFixed}}</td>
								<td>{{h.remark}}</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="part-wrapper">
				<div class="title">其他</div>
				<!--订单备注-->
				<div class="line-part large-tag" v-if="order.remark != null">
					<span class="float-tag">备注：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.remark}}</p>
					</div>
				</div>
				<!--订单额外数据-->
				<div class="line-part large-tag" v-if="order.extra != null && order.extra.distributeAddress != null && order.extra.distributeAddress !=''">
					<span class="float-tag">发货地址：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.extra.distributeAddress}}</p>
					</div>
				</div>
				<div class="line-part large-tag" v-if="order.extra != null && order.extra.originStation != null && order.extra.originStation !=''">
					<span class="float-tag">始发地：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.extra.originStation}}</p>
					</div>
				</div>
				<div class="line-part large-tag" v-if="order.extra != null && order.extra.arrivalStation != null && order.extra.arrivalStation !=''">
					<span class="float-tag">目的地：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.extra.arrivalStation}}</p>
					</div>
				</div>
				<div class="line-part large-tag" v-if="order.extra != null && order.extra.income != null">
					<span class="float-tag">收入：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.extra.income}}</p>
					</div>
				</div>
				<div class="line-part large-tag" v-if="order.extra != null && order.extra.expenditure != null">
					<span class="float-tag" v-if="order.extra.expenditure != null">支出：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.extra.expenditure}}</p>
					</div>
				</div>
				<!--自定义数据-->
				<div class="line-part large-tag" v-for="c in customDatas" v-if="c.customValue != null && c.customValue !=''">
					<span class="float-tag">{{c.customName}}：</span>
					<div class="text-tag">
						<p class="p-tag">{{c.customValue}}</p>
					</div>
				</div>

				<!--分享数据-->
				<div class="line-part large-tag" v-if="order.shareName != null">
					<span class="float-tag">{{数据来源}}：</span>
					<div class="text-tag">
						<p class="p-tag">{{order.shareName}}</p>
					</div>
				</div>
			</div>
			<div class="part-wrapper">
				<div class="title">回单</div>
				<div class="bill_test">
					<ul class="imgList clearfix" v-if="imgList != null && imgList.length > 0" >
						<li v-for="(image, index) in imgList" :key="image">
							<img :src="imagePath + image.storagePath" @click="lookBigPic(imagePath + image.storagePath)" <%--图片放大单击事件 @click="bigImg(index)" --%> >
						</li>
					</ul>
					<div v-if="imgList.length <= 0"> 无回单上传 </div>
				</div>
			</div>
				<div class="part-wrapper">
					<div class="title">反馈信息</div>
					<div class="line-part large-tag" v-if="">
						<span class="float-tag" >到货时间：</span>
						<div class="text-tag">
							<p class="p-tag">{{order.arrivedTime|datetime}}</p>
						</div>
					</div>
					<div class="line-part large-tag" >
						<span class="float-tag" >签收时间：</span>
						<div class="text-tag">
							<p class="p-tag">{{order.receiveTime|datetime}}</p>
						</div>
					</div>
					<div class="line-part large-tag clearfix" >
						<span class="float-tag" >收货结果：</span>
						<div class="text-tag clearfix">
							<p class="p-tag" v-if="order.signFettle>0&&order.signFettle==1">完好签收</p>
							<p class="p-tag" v-if="order.signFettle>0&&order.signFettle==0">未签收</p>
							<p class="p-tag" v-if="order.signFettle>0&&order.signFettle==2">异常签收</p>
							<p class="p-tag" v-if="order.signFettle>0&&order.signFettle==2"> {{exception.content}}</p>
						</div>
						<%--<div class="text-tag" v-if="order.signFettle>0&&order.signFettle==1">--%>
							<%--<p class="p-tag" v-if="exception.type==1">货物破损</p>--%>
							<%--<p class="p-tag" v-if="exception.type==2">货物数量不对</p>--%>
							<%--<p class="p-tag"> {{exception.content}}</p>--%>
						<%--</div>--%>
					</div>
					<div class="line-part large-tag clearfix" >
						<span class="float-tag" >投诉:</span>
						<div class="text-tag" v-if="complaint!=null">
							<p class="p-tag"> {{complaint.complainantContent}}</p>
						</div>
					</div>

				</div>
			<!--img放大-->
			<div class="pcui-gallery" style="display: none" id="gallery">
				<div class="trip">提示：点击图片进行图片旋转</div>
				<span class="pcui-gallery__img"></span>
				<div class="btnClose">
					<img src="../../../static/images/close-icon.png" @click="close()" />
				</div>
			</div>

			<%--   图片放大的弹框
        <div class="imgMask" v-if="showBigImg" @click.stop="showBigImg=!showBigImg">
            <img class="prev"  @click.stop="prev" src="http://picm.photophoto.cn/015/037/010/0370100072.jpg">
            <div class="showImg" >
                <img class="bigImg" :src="imagePath + imgList[num].storagePath">
            </div>
            <img class="next"  @click.stop="next" src="http://img.sucai.redocn.com/attachments/images/201206/20120629/Redocn_2012062907172617.jpg">
        </div>
        --%>

			</template>

		</div>

	</body>
	<%@ include file="/views/include/floor.jsp" %>
	<script type="text/javascript" src="${baseStatic}element-ui/lib/index.js"></script>
	<script>
		$(document).ready(function() {
			var orderKey = $("#orderKey").val(), share = $("#orderKey").data('share');
			new Vue({
				el: '#orderWrapper',
				data: {
                    orderKey:'',
                    deliveryNo:'',
                    dialogVisible: false,
					num: 0,
					showBigImg: false,
					operates: [],
					convey: {},
					receive: {},
					shipper: {},
					order: {},
					commodities: [],
                    customDatas: [],
					imgList: [],
                    complaint:{},//投诉
                    exception:{},//异常
				},
				created: function() {
					queryDetail(this);
				},
				methods: {
                    //对话框确认关闭事件
                    handleClose(done) {
                        this.$confirm('确认关闭？')
                            .then(_ => {
                                done();
                            })
                            .catch(_ => {});
                    },
                    //送货单号编辑
                    editDeliveryNo:function(deliveryNo,orderKey){
                        this.dialogVisible =true;
                        this.deliveryNo = deliveryNo;
                        this.orderKey = orderKey
                    },
                    //提交修改后的单号
                    submitDeliveryNo:function(){
                        var _this = this
                        var parmas = {
                            orderKey:_this.orderKey,
                            deliveryNo:_this.deliveryNo
                        }
                        $.util.json(base_url + '/enterprise/order/order/update/deliveryNo', parmas, function (data) {
                            console.log(data)
                            if (data.success) {//处理返回结果
                                _this.dialogVisible = false;
                                _this.$message({
                                    message: data.message,
                                    type: 'success',
                                    duration:'500'
                                });
                             _this.order.deliveryNo = _this.deliveryNo

                            } else {
                                $.util.error(data.message);
                            }
                        })
                    },
					lookBigPic: function(imgUrl) {
						var rotate = 0;
						$('#gallery').show().children('.pcui-gallery__img').css('background-image', 'url(' + imgUrl + ')').click(function() {
							rotate += 90;
							$('#gallery').show().children('.pcui-gallery__img').css('transform', 'rotate(' + rotate + 'deg)');
						})
					},
					close: function() {
						$('#gallery').hide();
					}
				}
			})

			function queryDetail(vm) {
				if(null != orderKey && orderKey > 0) {
					var url = share ? '/enterprise/order/share/detail/' : '/enterprise/order/orderDetail/';
					console.log(orderKey)
				    $.util.json(base_url + url + orderKey, null, function(data) {
				        console.log(data)
						if(data.success) { //处理返回结果
							if(data.operates) {
								vm.operates = data.operates;
							}
							if(data.reslut.shipper) {
								vm.order = data.reslut;
								if(data.reslut.convey) {
									vm.convey = data.reslut.convey;
								}
								if(data.reslut.receive) {
									vm.receive = data.reslut.receive;
								}
								if(data.reslut.shipper) {
									vm.shipper = data.reslut.shipper;
								}
								if(data.reslut.commodities) {
									vm.commodities = data.reslut.commodities;
								}
								if(data.reslut.imageStorages) {
									vm.imgList = data.reslut.imageStorages;
								}
                                if(data.reslut.customDatas) {
                                    vm.customDatas = data.reslut.customDatas;
                                }
                                if(data.reslut.complaint) {
                                    vm.complaint = data.reslut.complaint;
                                }
                                if(data.reslut.exception) {
                                    vm.exception = data.reslut.exception;
                                }
							}
							if(data.imagePath) {
								vm.imagePath = data.imagePath;
							}
						} else {
							$.util.error(data.message);
						}
					});
				}
			}
		});
	</script>

</html>
-- ----------------------------
-- 新增回单扫描记录表
-- ----------------------------
DROP TABLE IF EXISTS `waybill_receipt_status`;
CREATE TABLE `waybill_receipt_status` (
  `waybill_id` int(11) NOT NULL COMMENT '运单主键',
  `receipt_status` int(11) NOT NULL COMMENT '回单状态(0:未回收,1:已回收,2:已送客户,3:已退供应商,4:客户退回)',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `user_id` int(11) NOT NULL COMMENT '操作人编号',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- ----------------------------
-- 运单表新增回单状态字段
-- ----------------------------
ALTER TABLE waybill_tab ADD receipt_status int(11) DEFAULT 0 COMMENT '回单状态(0:未回收,1:已回收,2:已送客户,3:已退供应商,4:客户退回)';

-- ----------------------------
-- 新增电子围栏表
-- ----------------------------
DROP TABLE IF EXISTS `grate_tab`;
CREATE TABLE `grate_tab` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customerid` int(11) NOT NULL COMMENT '客户表id',
  `longitude` double DEFAULT NULL COMMENT '经度',
  `latitude` double DEFAULT NULL COMMENT '纬度',
  `createtime` datetime NOT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '修改时间',
  `create_userid` int(11) NOT NULL COMMENT '创建人',
  `update_userid` int(11) DEFAULT NULL COMMENT '修改人',
  `uploadtime` datetime DEFAULT NULL COMMENT '上报时间',
  `upload_userid` int(11) DEFAULT NULL COMMENT '上报人',
  `radius` double DEFAULT NULL COMMENT '半径（单位公里）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='电子围栏表';
-- ----------------------------
-- 收货客户表新增围栏状态字段
-- ----------------------------
ALTER TABLE customer_tab ADD grateSatus int(1) DEFAULT 0 COMMENT '围栏开关0关1开';
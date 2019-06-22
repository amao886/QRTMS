
-- 计划新增派车状态
ALTER TABLE `t_plan_order` ADD COLUMN `CAR_STATUS` INT(11)  DEFAULT 0 COMMENT '派车状态 0 未派车  1 已派车';

-- ----------------------------
-- Table structure for t_vehicle
-- ----------------------------
DROP TABLE IF EXISTS `t_vehicle`;
CREATE TABLE `t_vehicle` (
  `KEY` bigint(20) NOT NULL,
  `CAR_MODEL` varchar(50) DEFAULT NULL COMMENT '车型',
  `CAR_LENGTH` double DEFAULT NULL COMMENT '车长',
  `VEHICLE_LOAD` double DEFAULT NULL COMMENT '车辆载重',
  `VOLUME` double DEFAULT NULL COMMENT '体积',
  `TOTAL_LOAD` double DEFAULT NULL COMMENT '总载重',
  `TOTAL_VOLUME` double DEFAULT NULL COMMENT '总体积',
  `CREATE_TIME` datetime DEFAULT NULL,
  `CAR_NO` varchar(20) DEFAULT NULL COMMENT '车牌号',
  `DRIVER_NAME` varchar(50) DEFAULT NULL COMMENT '司机姓名',
  `DRIVER_NUMBER` varchar(20) DEFAULT NULL,
  `CAR_STATUS` int(11) DEFAULT '0' COMMENT '派车状态：0未派车 1已派车 2派车确认',
  `CREATE_USER` int(11) DEFAULT NULL COMMENT '创建人',
  `SHIPPER_ID` bigint(20) DEFAULT NULL COMMENT '发货ID',
  PRIMARY KEY (`KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_vehicle_designate
-- ----------------------------
DROP TABLE IF EXISTS `t_vehicle_designate`;
CREATE TABLE `t_vehicle_designate` (
  `KEY` bigint(20) NOT NULL,
  `CONTACT` varchar(50) DEFAULT NULL COMMENT '联系人',
  `CONTACT_NUMBER` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `CONVEY_ID` bigint(20) DEFAULT NULL COMMENT '物流商ID',
  `LAST_COMPANY_KEY` bigint(20) DEFAULT '0' COMMENT '上级企业ID',
  `V_ID` bigint(20) DEFAULT NULL COMMENT '要车单ID',
  `CREATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

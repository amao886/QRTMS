-- 菜单修改
INSERT INTO `T_SYS_MENU` (`ID`, `P_ID`, `MENU_NAME`, `MENU_URL`, `MENU_FETTLE`, `MENU_ICON`, `MENU_TYPE`, `CODE`) VALUES ('67', '0', '计划管理', '', '1', 'orderManage', '1', NULL);
INSERT INTO `T_SYS_MENU` (`ID`, `P_ID`, `MENU_NAME`, `MENU_URL`, `MENU_FETTLE`, `MENU_ICON`, `MENU_TYPE`, `CODE`) VALUES ('68', '67', '发货方计划', 'enterprise/plan/manage/shipper', '1', NULL, '1', NULL);
INSERT INTO `T_SYS_MENU` (`ID`, `P_ID`, `MENU_NAME`, `MENU_URL`, `MENU_FETTLE`, `MENU_ICON`, `MENU_TYPE`, `CODE`) VALUES ('69', '67', '物流商计划', 'enterprise/plan/manage/convey', '1', NULL, '1', NULL);
INSERT INTO `T_SYS_MENU` (`ID`, `P_ID`, `MENU_NAME`, `MENU_URL`, `MENU_FETTLE`, `MENU_ICON`, `MENU_TYPE`, `CODE`) VALUES ('70', '0', '模板管理', '', '1', 'orderManage', '1', NULL);
INSERT INTO `T_SYS_MENU` (`ID`, `P_ID`, `MENU_NAME`, `MENU_URL`, `MENU_FETTLE`, `MENU_ICON`, `MENU_TYPE`, `CODE`) VALUES ('71', '70', '计划模板', 'enterprise/template/manage/plan', '1', NULL, '1', NULL);
INSERT INTO `T_SYS_MENU` (`ID`, `P_ID`, `MENU_NAME`, `MENU_URL`, `MENU_FETTLE`, `MENU_ICON`, `MENU_TYPE`, `CODE`) VALUES ('72', '35', '企业配置', 'enterprise/company/view/config', '1', NULL, '1', NULL);

-- 角色权限修改
INSERT INTO `T_SYS_ROLE_MENU` (`R_ID`, `M_ID`) VALUES ('4', '67');
INSERT INTO `T_SYS_ROLE_MENU` (`R_ID`, `M_ID`) VALUES ('4', '68');
INSERT INTO `T_SYS_ROLE_MENU` (`R_ID`, `M_ID`) VALUES ('4', '69');
INSERT INTO `T_SYS_ROLE_MENU` (`R_ID`, `M_ID`) VALUES ('4', '70');
INSERT INTO `T_SYS_ROLE_MENU` (`R_ID`, `M_ID`) VALUES ('4', '71');
INSERT INTO `T_SYS_ROLE_MENU` (`R_ID`, `M_ID`) VALUES ('4', '72');

UPDATE T_SYS_MENU SET P_ID=70 , MENU_NAME='发货模板', MENU_URL='enterprise/template/manage/ship' WHERE ID=55;

-- 新增模板类别
ALTER TABLE `T_IMPORT_TEMPLATE` ADD COLUMN `CATEGORY` INT(11) DEFAULT 1 COMMENT '类别(1:发货模板,2:计划模板)';
ALTER TABLE `T_TEMPLATE_PROPERTY` ADD COLUMN `CATEGORY` INT(11) DEFAULT 1 COMMENT '类别(1:发货模板,2:计划模板)';

-- 计划订单
DROP TABLE IF EXISTS `T_PLAN_ORDER`;
CREATE TABLE `T_PLAN_ORDER` (
  `ID` bigint(20) NOT NULL COMMENT '订单编号',
  `PLAN_NO` varchar(50) NOT NULL COMMENT '计划单号',
  `COMPANY_KEY` bigint(20) NOT NULL COMMENT '所属企业编号',
  `SHIPPER_ID` bigint(20) DEFAULT '0' COMMENT '发货方客户编号',
  `RECEIVE_ID` bigint(20) NOT NULL COMMENT '收货方客户编号',
  `RECEIVER_NAME` varchar(30) NOT NULL COMMENT '收货人',
  `RECEIVER_CONTACT` varchar(100) DEFAULT '1' COMMENT '联系方式',
  `RECEIVE_ADDRESS` varchar(255) NOT NULL COMMENT '收货地址',
  `TRANSPORT_ROUTE` varchar(100) DEFAULT NULL COMMENT '运输路线',
  `FETTLE` int(4) DEFAULT '0' COMMENT '订单状态(待定)',
  `REMARK` varchar(500) DEFAULT '' COMMENT '备注',
  `ORDER_NO` varchar(50) DEFAULT NULL COMMENT '客户订单编号',
  `DELIVERY_TIME` datetime DEFAULT NULL COMMENT '发货时间',
  `ARRIVAL_TIME` datetime DEFAULT NULL COMMENT '要求到货时间',
  `COLLECT_TIME` datetime DEFAULT NULL COMMENT '要求提货时间',
  `CREATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `ORDER_KEY` bigint(20) DEFAULT '0' COMMENT '生成的发货订单编号',
  `USER_KEY` int(11) DEFAULT '0' COMMENT '创建人用户编号',
  PRIMARY KEY (`ID`),
  KEY `INDEX_COMPANY_KEY` (`COMPANY_KEY`),
  KEY `INDEX_SHIPPER_ID` (`SHIPPER_ID`),
  KEY `INDEX_RECEIVE_ID` (`RECEIVE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 计划明细
DROP TABLE IF EXISTS `T_PLAN_COMMODITY`;
CREATE TABLE `T_PLAN_COMMODITY` (
  `ID` bigint(20) NOT NULL COMMENT '编号',
  `PLAN_ID` bigint(20) NOT NULL COMMENT '计划编号',
  `COMMODITY_NO` varchar(30) DEFAULT NULL COMMENT '物料编号',
  `COMMODITY_NAME` varchar(100) DEFAULT NULL COMMENT '物料名称',
  `QUANTITY` int(4) DEFAULT '0' COMMENT '数量(件)',
  `BOX_COUNT` int(4) DEFAULT '0' COMMENT '箱数(箱)',
  `VOLUME` double DEFAULT '0' COMMENT '体积(立方米)',
  `WEIGHT` double DEFAULT '0' COMMENT '重量(千克)',
  `REMARK` varchar(200) DEFAULT NULL COMMENT '备注',
  `CREATE_USER_ID` int(11) NOT NULL COMMENT '创建人',
  `CREATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `COMMODITY_TYPE` varchar(100) DEFAULT NULL COMMENT '物料型号',
  `COMMODITY_UNIT` varchar(20) DEFAULT NULL COMMENT '物料单位',
  PRIMARY KEY (`ID`),
  KEY `INDEX_PLAN_ID` (`PLAN_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 计划指派(分配物流商)
DROP TABLE IF EXISTS `T_PLAN_DESIGNATE`;
CREATE TABLE `T_PLAN_DESIGNATE` (
  `ID` bigint(20) NOT NULL COMMENT '编号',
  `PLAN_ID` bigint(20) NOT NULL COMMENT '计划编号',
  `COMPANY_KEY` bigint(20) DEFAULT '0' COMMENT '被指派的物流商企业编号',
  `LAST_COMPANY_KEY` bigint(20) DEFAULT '0' COMMENT '上游企业编号',
  `UPDATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `FETTLE` int(4) DEFAULT '0' COMMENT '指派状态(0:未接单,1:已接单,2:下级已接单)',
  `ALLOCATE` int(4) DEFAULT 1 COMMENT '分配状态(1:已分配,2:下级已分配)',
  PRIMARY KEY (`ID`),
  KEY `INDEX_PLAN_ID` (`PLAN_ID`),
  KEY `INDEX_COMPANY_KEY` (`COMPANY_KEY`),
  KEY `INDEX_LAST_COMPANY_KEY` (`LAST_COMPANY_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `T_ORDER_EXTRA` ADD COLUMN `INCOME` double DEFAULT '0' COMMENT '收入';
ALTER TABLE `T_ORDER_EXTRA` ADD COLUMN `EXPENDITURE` double DEFAULT '0' COMMENT '支出';
ALTER TABLE `T_ORDER_EXTRA` ADD COLUMN `CARE_NO` varchar(20) DEFAULT '' COMMENT '车牌号';
ALTER TABLE `T_ORDER_EXTRA` ADD COLUMN `DRIVER_NAME` varchar(30) DEFAULT '' COMMENT '司机姓名';
ALTER TABLE `T_ORDER_EXTRA` ADD COLUMN `DRIVER_CONTACT` varchar(50) DEFAULT '' COMMENT '司机联系方式';

UPDATE T_ORDER_EXTRA e, T_FINANCE f SET e.INCOME=f.INCOME, e.EXPENDITURE = f.EXPENDITURE WHERE e.`KEY` = f.`OBJECT_KEY` AND f.FINANCE_TYPE = 1;
INSERT INTO T_ORDER_EXTRA (`KEY`, INCOME, EXPENDITURE) SELECT f.OBJECT_KEY AS `KEY`, f.INCOME, f.EXPENDITURE from T_FINANCE f left join T_ORDER_EXTRA e on e.`KEY` = f.`OBJECT_KEY` AND f.FINANCE_TYPE = 1 WHERE (f.INCOME >0 OR f.EXPENDITURE>0 ) AND e.`KEY` IS NULL;
UPDATE T_ORDER_EXTRA e, T_ORDER_CONVEY c SET e.CARE_NO=c.CARE_NO, e.DRIVER_NAME=c.DRIVER_NAME, e.DRIVER_CONTACT=c.DRIVER_CONTACT WHERE e.`KEY` = c.`KEY`;
INSERT INTO T_ORDER_EXTRA (`KEY`, CARE_NO, DRIVER_NAME, DRIVER_CONTACT) SELECT f.`KEY`, f.CARE_NO, f.DRIVER_NAME, f.DRIVER_CONTACT from T_ORDER_CONVEY f left join T_ORDER_EXTRA e on e.`KEY` = f.`KEY` WHERE e.`KEY` IS NULL;

ALTER TABLE `T_ORDER_EXTRA` ADD COLUMN `DATA_TYPE` INT(4) DEFAULT '1' COMMENT '额外数据类型(1:发货订单,2:发货计划)';
ALTER TABLE `T_ORDER_EXTRA` ADD COLUMN `DATA_KEY` BIGINT(20) DEFAULT '0' COMMENT '额外数据关联主键';
UPDATE `T_ORDER_EXTRA` SET DATA_KEY = `KEY` WHERE DATA_TYPE=1;

UPDATE `T_TEMPLATE_PROPERTY` SET `CLASS_NAME`='OrderExtra' WHERE `CLASS_NAME` IN ('OrderConvey', 'Finance');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('201', '发货方', NULL, 'com.ycg.ksh.service.core.entity.service.plan.PlanTemplate', 'shipperName', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('202', '计划单号', '发货计划的编号', 'com.ycg.ksh.service.core.entity.service.plan.PlanTemplate', 'planNo', '1', '1', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('203', '发货日期', NULL, 'com.ycg.ksh.service.core.entity.service.plan.PlanTemplate', 'deliveryTime', '4', '1', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('204', '收货客户', NULL, 'com.ycg.ksh.service.core.entity.service.plan.PlanTemplate', 'receiveName', '1', '1', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('205', '收货人', NULL, 'com.ycg.ksh.service.core.entity.service.plan.PlanTemplate', 'receiverName', '1', '1', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('206', '联系方式', '收货人的联系电话', 'com.ycg.ksh.service.core.entity.service.plan.PlanTemplate', 'receiverContact', '1', '1', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('207', '收货地址', NULL, 'com.ycg.ksh.service.core.entity.service.plan.PlanTemplate', 'receiveAddress', '1', '1', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('208', '产品描述', NULL, 'PlanCommodity', 'commodityName', '1', '1', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('209', '单位', NULL, 'PlanCommodity', 'commodityUnit', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('210', '数量', NULL, 'PlanCommodity', 'quantity', '2', '1', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('211', '箱数', NULL, 'PlanCommodity', 'boxCount', '2', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('212', '体积', NULL, 'PlanCommodity', 'volume', '3', '1', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('213', '重量', NULL, 'PlanCommodity', 'weight', '3', '1', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('214', '物流商', '发货方的承运物流商名称', 'com.ycg.ksh.service.core.entity.service.plan.PlanTemplate', 'conveyName', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('215', '联系人', '物流商的联系人', 'OrderExtra', 'conveyerName', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('216', '联系电话', '物流商联系人的联系电话', 'OrderExtra', 'conveyerContact', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('217', '订单编号', '购单客户给发货方下单后生产的订单的编号', 'com.ycg.ksh.service.core.entity.service.plan.PlanTemplate', 'orderNo', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('218', '始发地', NULL, 'OrderExtra', 'originStation', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('219', '目的地', NULL, 'OrderExtra', 'arrivalStation', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('220', '物料编号', '发货方的物料的编号', 'PlanCommodity', 'commodityNo', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('221', '物料备注', NULL, 'PlanCommodity', 'remark', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('222', '车牌号', NULL, 'OrderExtra', 'careNo', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('223', '司机姓名', NULL, 'OrderExtra', 'driverName', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('224', '司机电话', NULL, 'OrderExtra', 'driverContact', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('225', '发货地址', NULL, 'OrderExtra', 'distributeAddress', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('226', '收入', NULL, 'OrderExtra', 'income', '3', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('227', '运输成本', NULL, 'OrderExtra', 'expenditure', '3', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('228', '发站', NULL, 'OrderExtra', 'startStation', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('229', '到站', NULL, 'OrderExtra', 'endStation', '1', '2', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('230', '到货时间', '要求到货时间', 'com.ycg.ksh.service.core.entity.service.plan.PlanTemplate', 'arrivalTime', '4', '1', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('231', '提货时间', '要求提货时间', 'com.ycg.ksh.service.core.entity.service.plan.PlanTemplate', 'collectTime', '4', '1', '2');
INSERT INTO `T_TEMPLATE_PROPERTY` (`KEY`, `NAME`, `DESCRIPTION`, `CLASS_NAME`, `DATA_KEY`, `DATA_TYPE`, `TYPE`, `CATEGORY`) VALUES ('232', '运输路线', '运输路线', 'com.ycg.ksh.service.core.entity.service.plan.PlanTemplate', 'transportRoute', '1', '1', '2');

-- 企业配置
DROP TABLE IF EXISTS `T_COMPANY_CONFIG`;
CREATE TABLE `T_COMPANY_CONFIG` (
  `COMPANY_KEY` bigint(20) DEFAULT '0' COMMENT '企业编号',
  `CONFIG_KEY` varchar(50) DEFAULT '' COMMENT '配置编号',
  `CONFIG_VALUE` varchar(100) DEFAULT '' COMMENT '配置值',
  `UPDATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`COMPANY_KEY`, `CONFIG_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 对外推送记录
DROP TABLE IF EXISTS `T_ADVENTIVE_PUSH`;
CREATE TABLE `T_ADVENTIVE_PUSH` (
  `ID` BIGINT(20) NOT NULL COMMENT '编号' ,
  `ADVENTIVE_ID` BIGINT(20) NOT NULL COMMENT '推送对象' ,
  `INCLUDES`  VARCHAR(500) DEFAULT NULL COMMENT '要推送的配置(S发货方编号#R收货方编号#C承运方编号)' ,
  `EXCLUDES`  VARCHAR(500) DEFAULT NULL COMMENT '不推送的配置(S发货方编号#R收货方编号#C承运方编号)' ,
  `DATA_TYPES`  VARCHAR(500) DEFAULT NULL COMMENT '关注的数据类型',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



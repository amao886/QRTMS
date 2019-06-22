-- service
use track;

UPDATE t_order SET SIGN_FETTLE = 1 WHERE FETTLE > 1;
UPDATE t_order SET SIGN_FETTLE = 0 WHERE SIGN_FETTLE IS NULL;
UPDATE t_order SET POSITIONING_CHECK = 100 WHERE POSITIONING_CHECK > 0;

-- 计划新增派车时间
ALTER TABLE `t_plan_order` ADD COLUMN `CAR_TIME` DATETIME  DEFAULT NULL COMMENT '派车时间';
-- 订单新增到达时间
ALTER TABLE `t_order` ADD COLUMN `ARRIVED_TIME` DATETIME  DEFAULT NULL COMMENT '到达时间';
ALTER TABLE `t_order` ADD COLUMN `EVALUATION` INT(1)  DEFAULT NULL COMMENT '评价 0 差评 1好评';
ALTER TABLE `t_order` ADD COLUMN `IS_COMPLAINT` tinyint(1)  DEFAULT 0 COMMENT '是否投诉';
-- 订单异常表，新增异常类型
ALTER TABLE `t_order_exception` ADD COLUMN `TYPE` INT(1)  DEFAULT NULL COMMENT '异常类型 1 货物破损 2 货物数量不对';


UPDATE `t_order` SET IS_COMPLAINT = 0;

UPDATE `t_order` SET ARRIVED_TIME = RECEIVE_TIME WHERE  ARRIVED_TIME IS NULL;
UPDATE `t_order` SET EVALUATION = 1;

ALTER TABLE `t_order_invalid` ADD COLUMN `ARRIVED_TIME` DATETIME  DEFAULT NULL COMMENT '到达时间';
UPDATE `t_order_invalid` set ARRIVED_TIME = RECEIVE_TIME WHERE  ARRIVED_TIME IS NULL;

-- 作废表删除字段
ALTER TABLE `t_order_invalid` DROP COLUMN `DELAY_WARNING`,DROP COLUMN `PICK_UP_WARNING`,DROP COLUMN `POSITIONING_CHECK`;

-- 系统用户
INSERT INTO `user_tab` (
	`id`,
	`username`,
	`password`,
	`openid`,
	`mobilephone`,
	`uname`,
	`createtime`,
	`updatetime`,
	`remark`,
	`head_img`
)
VALUES
	(
		'0',
		'000000000000000000000000000000',
		'1',
		'000000000000000000000000000000',
		'0000000000',
		'57O757uf',
		'2017-09-06 14:54:08',
		'2018-10-10 10:26:40',
		NULL,
		NULL
	);

UPDATE t_template_property SET CLASS_NAME ='com.ycg.ksh.entity.persistent.enterprise.OrderExtra' WHERE CLASS_NAME='com.ycg.ksh.service.core.entity.persistent.enterprise.OrderExtra';
UPDATE t_template_property SET CLASS_NAME ='com.ycg.ksh.entity.persistent.OrderCommodity' WHERE CLASS_NAME='com.ycg.ksh.service.core.entity.persistent.OrderCommodity';
UPDATE t_template_property SET CLASS_NAME ='com.ycg.ksh.entity.persistent.plan.PlanCommodity' WHERE CLASS_NAME='com.ycg.ksh.service.core.entity.persistent.plan.PlanCommodity ';
UPDATE t_template_property SET CLASS_NAME ='com.ycg.ksh.entity.service.enterprise.OrderTemplate' WHERE CLASS_NAME='com.ycg.ksh.service.core.entity.service.enterprise.OrderTemplate';
UPDATE t_template_property SET CLASS_NAME ='com.ycg.ksh.entity.service.plan.PlanTemplate' WHERE CLASS_NAME='com.ycg.ksh.service.core.entity.service.plan.PlanTemplate';

-- 创建投诉表
DROP TABLE IF EXISTS `t_complaint`;
CREATE TABLE `t_complaint` (
  `KEY` bigint(20) NOT NULL,
  `COMPLAINANT` varchar(50) DEFAULT NULL COMMENT '投诉人',
  `COMPLAINANT_NUMBER` varchar(20) DEFAULT NULL COMMENT '投诉人电话',
  `CREATE_TIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `COMPLAINANT_CONTENT` varchar(500) DEFAULT NULL COMMENT '投诉内容',
  `ORDER_ID` bigint(20) DEFAULT NULL COMMENT '订单编号',
  `RECEIVE_ID` bigint(20) DEFAULT NULL COMMENT '收货ID',
  `SHIPPER_ID` bigint(20) DEFAULT NULL COMMENT '发货ID',
  `CONVEY_ID` bigint(20) DEFAULT NULL COMMENT '承运商ID',
  `RECEIVE_NAME` varchar(50) DEFAULT NULL COMMENT '收货方名字',
  `SHIPPER_NAME` varchar(50) DEFAULT NULL COMMENT '发货方名字',
  `CONVEY_NAME` varchar(50) DEFAULT NULL COMMENT '承运商名字',
  PRIMARY KEY (`KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;









-- collect 库
CREATE DATABASE IF NOT EXISTS qlm_collect DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use qlm_collect;
-- ----------------------------
-- Table structure for sys_day_summary
-- ----------------------------
DROP TABLE IF EXISTS `sys_day_summary`;
CREATE TABLE `sys_day_summary` (
  `summary_no` varchar(150) NOT NULL,
  `last_time` datetime NOT NULL,
  `least_time` datetime DEFAULT NULL,
  `ddl_string` text,
  `table_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`summary_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_day_summary
-- ----------------------------
INSERT INTO `sys_day_summary` VALUES ('com.ycg.ksh.entity.collecter.SummaryOrderAssess', '2018-11-06 10:53:00', '2018-05-01 00:00:00', 'create table %s ( summary_no varchar(50) NOT NULL, summary_time date NOT NULL, company_key bigint(20) NOT NULL, partner_type int(4) NOT NULL, other_side_key bigint(20) NOT NULL,total_count bigint(20) NULL DEFAULT 0, track_count bigint(20) NULL DEFAULT 0, send_car_count bigint(20) NULL DEFAULT 0, arrival_count bigint(20) NULL DEFAULT 0, evaluate_count bigint(20) NULL DEFAULT 0, complaint_count bigint(20) NULL DEFAULT 0, delay_count bigint(20) NULL DEFAULT 0, pickup_count bigint(20) NULL DEFAULT 0, sign_count bigint(20) NULL DEFAULT 0, PRIMARY KEY (summary_no), UNIQUE INDEX uk_time_key_type (summary_time, company_key, partner_type) ) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci', 'summary_order_assess_%s');
INSERT INTO `sys_day_summary` VALUES ('com.ycg.ksh.entity.collecter.SummaryOrderTimeout', '2018-11-06 10:53:00', '2018-05-01 00:00:00', null, null);
INSERT INTO `sys_day_summary` VALUES ('com.ycg.ksh.entity.collecter.SummaryOrderTrack', '2018-11-06 10:53:00', '2018-05-01 00:00:00', null, null);

-- ----------------------------
-- Table structure for sys_report_column
-- ----------------------------
DROP TABLE IF EXISTS `sys_report_column`;
CREATE TABLE `sys_report_column` (
  `id` int(11) NOT NULL COMMENT '编号',
  `report_id` int(11) NOT NULL COMMENT '报表编号',
  `column_name` varchar(20) NOT NULL COMMENT '列名',
  `column_key` varchar(20) NOT NULL,
  `column_type` int(4) NOT NULL DEFAULT '1' COMMENT '数据类型(1:字符串,2:整数,3:小数,4:百分比,5:日期)',
  `column_format` varchar(100) DEFAULT NULL COMMENT '格式化字符串',
  `sql_text` varchar(100) DEFAULT NULL COMMENT 'sql文本',
  `script_text` varchar(255) DEFAULT NULL,
  `link_url` varchar(255) DEFAULT NULL COMMENT '超链接地址',
  `calculate` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_report_column
-- ----------------------------
INSERT INTO `sys_report_column` VALUES ('1', '1', '发货单总数', 'total', '2', null, 'sum(total_count)', null, null, '1');
INSERT INTO `sys_report_column` VALUES ('2', '1', '跟踪及时率', 'track', '4', '0.00%', 'sum(track_count)', '{track}/{total}', '/report/{total}', '1');
INSERT INTO `sys_report_column` VALUES ('3', '1', '派车及时率', 'send_car', '4', '0.00%', 'sum(send_car_count)', '{send_car}/{total}', null, '1');
INSERT INTO `sys_report_column` VALUES ('4', '1', '到货及时率', 'arrival', '4', '0.00%', 'sum(arrival_count)', '{arrival}/{total}', null, '1');
INSERT INTO `sys_report_column` VALUES ('5', '1', '客户投诉率', 'evaluate', '4', '0.00%', 'sum(evaluate_count)', '{evaluate}/{total}', null, '1');
INSERT INTO `sys_report_column` VALUES ('6', '1', '客户好评率', 'complaint', '4', '0.00%', 'sum(complaint_count)', '{complaint}/{total}', null, '1');
INSERT INTO `sys_report_column` VALUES ('7', '1', '延迟率', 'delay', '4', '0.00%', 'sum(delay_count)', '{delay}/{total}', null, '1');
INSERT INTO `sys_report_column` VALUES ('8', '1', '提货及时率', 'pickup', '4', '0.00%', 'sum(pickup_count)', '{pickup}/{total}', null, '1');
INSERT INTO `sys_report_column` VALUES ('9', '1', '完好签收率', 'sign', '4', '0.00%', 'sum(sign_count)', '{sign}/{total}', null, '1');

-- ----------------------------
-- Table structure for sys_report_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_report_config`;
CREATE TABLE `sys_report_config` (
  `id` int(11) NOT NULL,
  `title` varchar(50) NOT NULL,
  `sql_text` tinytext NOT NULL,
  `table_name_suffix` int(4) DEFAULT '0' COMMENT '表名后缀(0:无，11:按月，待定)',
  `category` int(4) NOT NULL DEFAULT '1' COMMENT '报表类别(1:列表,2:统计,3:待定)',
  `suffix_query_key` int(11) DEFAULT NULL COMMENT '表名后缀参数',
  `need_sum` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_report_config
-- ----------------------------
INSERT INTO `sys_report_config` VALUES ('1', '考核', 'SELECT %s FROM summary_order_assess_%s where %s', '21', '2', '1', null);

-- ----------------------------
-- Table structure for sys_report_query
-- ----------------------------
DROP TABLE IF EXISTS `sys_report_query`;
CREATE TABLE `sys_report_query` (
  `id` int(11) NOT NULL,
  `report_id` int(11) NOT NULL COMMENT '所属报表编号',
  `show_label` varchar(20) NOT NULL COMMENT '展示标签',
  `show_type` int(4) NOT NULL DEFAULT '1' COMMENT '展示类型(1:输入框,2:单选框,3:复选框,4:下拉列表,5:日期,6:时间段)',
  `alias_key` varchar(30) NOT NULL,
  `required` tinyint(1) DEFAULT '0' COMMENT '是否必填',
  `sqlText` varchar(50) NOT NULL,
  `json_data` tinytext COMMENT '静态json数据',
  `source_type` int(4) NOT NULL DEFAULT '0' COMMENT '数据来源(0:不需要,1:静态json数据)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_report_query
-- ----------------------------
INSERT INTO `sys_report_query` VALUES ('1', '1', '所属企业', '1', 'companyKey', '0', 'company_key=%s', '', '0');
INSERT INTO `sys_report_query` VALUES ('2', '1', '当前角色', '1', 'partnerType', '0', 'partner_type=%s', '', '0');
INSERT INTO `sys_report_query` VALUES ('3', '1', '统计时间', '6', 'summaryTime', '0', 'summary_time>=\'%s\' and summary_time<=\'%s\'', null, '0');
INSERT INTO `sys_report_query` VALUES ('4', '1', '物流商', '1', 'otherSideKey', '0', 'other_side_key=%s', null, '0');


DROP TABLE IF EXISTS `sys_generate_barcode`;
CREATE TABLE `sys_generate_barcode` (
  `day_string` bigint(20) NOT NULL COMMENT '天数',
  `max_code` bigint(20) NOT NULL DEFAULT '0' COMMENT '最大序号',
  `total_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '当天生成总数',
  PRIMARY KEY (`day_string`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- qlm_core 库

CREATE DATABASE IF NOT EXISTS qlm_core DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use qlm_core;

-- ----------------------------
-- Table structure for t_company_driver
-- ----------------------------
DROP TABLE IF EXISTS `t_company_driver`;
CREATE TABLE `t_company_driver` (
  `id` bigint(20) NOT NULL,
  `compayn_key` bigint(20) DEFAULT NULL,
  `driver_key` bigint(20) DEFAULT NULL,
  `driver_name` varchar(255) DEFAULT NULL,
  `driver_phone` varchar(255) DEFAULT NULL,
  `relation_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ukey_compayny_driver` (`compayn_key`, `driver_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_company_driver_car
-- ----------------------------
DROP TABLE IF EXISTS `t_company_driver_car`;
CREATE TABLE `t_company_driver_car` (
  `id` varchar(255) NOT NULL,
  `car_type` int(11) DEFAULT NULL,
  `car_length` float DEFAULT NULL,
  `car_load_value` float DEFAULT NULL,
  `car_license` varchar(255) DEFAULT NULL,
  `driver_key` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_company_driver_route
-- ----------------------------
DROP TABLE IF EXISTS `t_company_driver_route`;
CREATE TABLE `t_company_driver_route` (
  `id` varchar(255) NOT NULL,
  `route_type` int(11) DEFAULT NULL,
  `route_start` varchar(255) DEFAULT NULL,
  `route_end` varchar(255) DEFAULT NULL,
  `driver_key` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_driver
-- ----------------------------
DROP TABLE IF EXISTS `t_driver`;
CREATE TABLE `t_driver` (
  `driver_key` bigint(20) NOT NULL,
  `driver_name` varchar(255) DEFAULT NULL,
  `driver_phone` varchar(255) DEFAULT NULL,
  `driver_register_time` datetime DEFAULT NULL,
  PRIMARY KEY (`driver_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_driver_await_info
-- ----------------------------
DROP TABLE IF EXISTS `t_driver_await_info`;
CREATE TABLE `t_driver_await_info` (
  `wait_key` bigint(20) NOT NULL,
  `driver_key` bigint(20) DEFAULT NULL,
  `driver_name` varchar(255) DEFAULT NULL,
  `driver_phone` varchar(255) DEFAULT NULL,
  `wait_address` varchar(255) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `release_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `car_type` int(11) DEFAULT NULL,
  `car_length` float DEFAULT NULL,
  `car_load_value` float DEFAULT NULL,
  `car_license` varchar(255) DEFAULT NULL,
  `route_type` int(11) DEFAULT NULL,
  `route_start` varchar(255) DEFAULT NULL,
  `route_end` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`wait_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_driver_car
-- ----------------------------
DROP TABLE IF EXISTS `t_driver_car`;
CREATE TABLE `t_driver_car` (
  `id` varchar(255) NOT NULL,
  `car_type` int(11) DEFAULT NULL,
  `car_length` float DEFAULT NULL,
  `car_load_value` float DEFAULT NULL,
  `car_license` varchar(255) DEFAULT NULL,
  `driver_key` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_driver_invite_ask
-- ----------------------------
DROP TABLE IF EXISTS `t_driver_invite_ask`;
CREATE TABLE `t_driver_invite_ask` (
  `invite_key` bigint(20) NOT NULL,
  `company_key` bigint(20) DEFAULT NULL,
  `invite_user_key` int(11) DEFAULT NULL,
  `invite_user_name` varchar(255) DEFAULT NULL,
  `driver_key` bigint(20) DEFAULT NULL,
  `driver_name` varchar(255) DEFAULT NULL,
  `driver_phone` varchar(255) DEFAULT NULL,
  `invite_time` datetime DEFAULT NULL,
  `handle_time` datetime DEFAULT NULL,
  `handle_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`invite_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_driver_route
-- ----------------------------
DROP TABLE IF EXISTS `t_driver_route`;
CREATE TABLE `t_driver_route` (
  `id` varchar(255) NOT NULL,
  `route_type` int(11) DEFAULT NULL,
  `route_start` varchar(255) DEFAULT NULL,
  `route_end` varchar(255) DEFAULT NULL,
  `driver_key` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
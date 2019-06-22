-- 2.0.1版本修改运单表
ALTER TABLE `conveyance_tab` ADD `waybill_key` int(11) NOT NULL COMMENT '任务单编号';
ALTER TABLE `conveyance_tab` ADD `delivery_number` varchar(255) DEFAULT '' COMMENT '送货单号';
ALTER TABLE `conveyance_tab` MODIFY COLUMN `barcode` VARCHAR(20) DEFAULT '' COMMENT '任务单号';

ALTER TABLE `reminders_things_tab` DROP column `barcode`;


UPDATE
  conveyance_tab c, waybill_tab w
SET
  c.waybill_key = w.id, c.delivery_number = w.delivery_number
WHERE
  c.barcode = w.barcode;


ALTER TABLE `customer_tab` ADD `arrival_day` int(11) DEFAULT NULL COMMENT '要求到时间天数';
ALTER TABLE `customer_tab` ADD `arrival_hour` int(11) DEFAULT NULL COMMENT '要求到时间小时数';


ALTER TABLE `user_tab` ADD `user_type` int(11) DEFAULT 0 COMMENT '用户类型(0:普通用户,1:管理员)';

DROP TABLE IF EXISTS `todo_log`;
CREATE TABLE `todo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `todo_key` int(11) NOT NULL COMMENT '待办消息主键',
  `log_type` int(11) NOT NULL COMMENT '待办日志类型',
  `log_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
  `user_key` int(11) NOT NULL COMMENT '操作人编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `behavior_differentiation`;
CREATE TABLE `behavior_differentiation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uri_key` varchar(255) DEFAULT NULL COMMENT 'uri标识key',
  `app_type` varchar(30) DEFAULT NULL COMMENT '所属平台 backstage:WEB端  api:微信端',
  `function_point` varchar(255) DEFAULT NULL COMMENT '功能点注释',
  `subordinate_module` varchar(255) DEFAULT NULL COMMENT '所属模块',
  PRIMARY KEY (`id`),
  KEY `key` (`uri_key`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

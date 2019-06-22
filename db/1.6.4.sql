
-- ALTER TABLE `user_tab` ADD unique(`username`);
-- ALTER TABLE `user_tab` ADD unique(`openid`);

ALTER TABLE `waybill_tab` ADD `barcode` VARCHAR(100) DEFAULT NULL COMMENT '条码编号';

ALTER TABLE `waybill_tab` ADD `receiver_name` VARCHAR(100) DEFAULT NULL COMMENT '收货客户名称/公司名称';
ALTER TABLE `waybill_tab` ADD `receiver_tel` VARCHAR(20) DEFAULT NULL COMMENT '收获客户座机';
ALTER TABLE `waybill_tab` ADD `receive_address` VARCHAR(255) DEFAULT NULL COMMENT '收货地址';
ALTER TABLE `waybill_tab` ADD `contact_name` VARCHAR(50) DEFAULT NULL COMMENT '收货联系人姓名';
ALTER TABLE `waybill_tab` ADD `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '收货联系人电话';
ALTER TABLE `waybill_tab` ADD `longitude` VARCHAR(20) DEFAULT NULL COMMENT '经度';
ALTER TABLE `waybill_tab` ADD `latitude` VARCHAR(20) DEFAULT NULL COMMENT '纬度';
ALTER TABLE `waybill_tab` ADD `fence_status` INT(11) DEFAULT 0 COMMENT '电子围栏开关1开0关';
ALTER TABLE `waybill_tab` ADD `fence_radius` DOUBLE DEFAULT 5 COMMENT '电子围栏半径,默认5公里';

ALTER TABLE `waybill_tab` ADD `position_count` INT(11) DEFAULT 0 COMMENT '定位次数';
ALTER TABLE `waybill_tab` ADD `receipt_count` INT(11) DEFAULT 0 COMMENT '回单数';
ALTER TABLE `waybill_tab` ADD `receipt_verify_count` INT(11) DEFAULT 0 COMMENT '回单审核数';


ALTER TABLE `waybill_tab` ADD `delay` INT(11) DEFAULT 999 COMMENT '是否延迟(999:未处理,0:未到已超时,1:延迟,2:未延迟)';

-- 修改纸质回单状态receipt_status为papery_receipt_status
ALTER TABLE `waybill_tab` CHANGE `receipt_status` `papery_receipt_status` INT(11) DEFAULT 0 COMMENT '纸质回单状态(0：未回收，1:已回收,2:已送客户,3:已退供应商,4:客户退回)';
-- 新增回单审核状态字段
ALTER TABLE `waybill_tab` ADD `receipt_verify_status` INT(11) DEFAULT 1 COMMENT '回单审核状态(1:待审核,2:审核中,3:已审核)';
-- 新增任务单状态字段
ALTER TABLE `waybill_tab` ADD `waybill_status` INT(11) DEFAULT 20 COMMENT '回单审核状态(20:发货,30:运输中,35:送达,40:收货)';

ALTER TABLE `waybill_tab` DROP column `source_type`; 
ALTER TABLE `waybill_tab` ADD `confirm_delivery_way` INT(11) DEFAULT 1 COMMENT '确认送达状态修改的方式(1:后台操作，2:上传回单，3:电子围栏)';

ALTER TABLE `sys_request_serial` ADD `agent_string` VARCHAR(255) DEFAULT NULL COMMENT 'user-agent';

-- 修复旧的客户信息数据
UPDATE 
	waybill_tab w, customer_tab c 
SET
	w.receiver_name = c.company_name,
	w.receive_address = c.fullAddress,
	w.receiver_tel = c.tel,
	w.contact_name = c.contacts,
	w.contact_phone = c.contact_number,
	w.fence_status = c.grateSatus,
	w.latitude = c.latitude,
	w.longitude = c.longitude
WHERE 
	w.customerid = c.id;

-- 修复回单审核数据
UPDATE 
	waybill_tab w, waybill_receipt_verify_view v
SET
	w.receipt_count = v.verify_total,
  	w.receipt_verify_count = v.verify_count,
	w.receipt_verify_status = CASE WHEN v.verify_count = 0 THEN 1 WHEN v.verify_count < v.verify_total THEN 2 ELSE 3 END
WHERE 
	w.id = v.waybillid;

-- 修复定位次数的数据
UPDATE 
	waybill_tab w, (SELECT COUNT(0) AS cccc, waybillid FROM track_tab GROUP BY waybillid) t
SET 
	w.position_count = t.cccc
WHERE 
	w.id = t.waybillid;

-- 修复条码数据
UPDATE 
	waybill_tab w, barcode_tab b
SET
	w.barcode = b.barcode,
	w.waybill_status = b.bindstatus
WHERE 
	w.barcodeid = b.id;

-- 新建发货计划表
DROP TABLE IF EXISTS `waybill_schedule`;
CREATE TABLE `waybill_schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `groupid` int(11) DEFAULT NULL COMMENT 'group主键',
  `userid` int(11) DEFAULT NULL COMMENT 'user主键',
  `createtime` datetime DEFAULT NULL COMMENT '导入时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `weight` double(16,1) DEFAULT '0.0' COMMENT '重量',
  `volume` double(16,1) DEFAULT '0.0' COMMENT '体积',
  `number` int(11) DEFAULT '0' COMMENT '数量',
  `arrive_day` int(11) DEFAULT NULL COMMENT '要求到货时间天数',
  `arrive_hour` int(11) DEFAULT NULL COMMENT '要求到货时间小时数',
  `receiver_name` varchar(100) DEFAULT NULL COMMENT '收货客户名称/公司名称',
  `receiver_tel` varchar(20) DEFAULT NULL COMMENT '收获客户座机',
  `receive_address` varchar(255) DEFAULT NULL COMMENT '收货地址',
  `contact_name` varchar(50) DEFAULT NULL COMMENT '收货联系人姓名',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '收货联系人电话',
  `longitude` varchar(20) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(20) DEFAULT NULL COMMENT '纬度',
  `fence_status` int(11) DEFAULT '0' COMMENT '电子围栏开关1开0关',
  `schedule_status` int(11) DEFAULT '0' COMMENT '计划状态(0:正常,1:已绑定,99:作废)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='发货计划表';


CREATE OR REPLACE VIEW waybill_receipt_view AS SELECT
	`w`.`id` AS `waybill_id`,
	max(`wr`.`modify_time`) AS `modify_time`,
	`w`.`delivery_number` AS `delivery_number`,
	`w`.`createtime` AS `createtime`,
	`w`.`arrivaltime` AS `arrivaltime`,
	`w`.`actual_arrival_time` AS `actual_arrival_time`,
	`w`.`papery_receipt_status` AS `receipt_status`,
	`w`.`groupid` AS `groupid`,
	`w`.`barcodeid` AS `barcodeid`,
	`w`.`weight` AS `weight`,
	`w`.`barcode` AS `barcode`,
	`g`.`group_name` AS `group_name`,
	`w`.`receiver_name` AS `company_name`,
	`w`.`contact_name` AS `contacts`,
	`w`.`contact_phone` AS `contact_number`,
	max(`rsb`.`batch_number`) AS `batch_number`,
	`w`.`receive_address` AS `address`,
	`w`.`number` AS `number`,
	`w`.`volume` AS `volume`
FROM
	(
		(
			(
				`waybill_receipt_status` `wr`
				JOIN `waybill_tab` `w` ON (
					(
						(`wr`.`waybill_id` = `w`.`id`)
						AND (
							`wr`.`receipt_status` = `w`.`papery_receipt_status`
						)
					)
				)
			)
			LEFT JOIN `group_tab` `g` ON ((`w`.`groupid` = `g`.`id`))
		)
		LEFT JOIN `receipt_scan_batch` `rsb` ON (
			(
				`rsb`.`waybill_id` = `wr`.`waybill_id`
			)
		)
	)
GROUP BY
	`wr`.`waybill_id`;





CREATE OR REPLACE VIEW waybill_Total_view AS (select date_format((`w`.`createtime` - interval if((`t`.`endGoodsTime` = '00:00'),0,cast(date_format(str_to_date(`t`.`endGoodsTime`,'%h'),'%h') as signed)) hour),'%Y-%m-%d') AS `createTime`,count(date_format((`w`.`createtime` - interval if((`t`.`endGoodsTime` = '00:00'),0,cast(date_format(str_to_date(`t`.`endGoodsTime`,'%h'),'%h') as signed)) hour),'%Y-%m-%d')) AS `allCount`,sum(if((`w`.`arrivaltime` <= now()),1,0)) AS `toCount`,sum(if((`w`.`actual_arrival_time` is not null),1,0)) AS `sendCount`,sum(if(((`w`.`actual_arrival_time` <= `w`.`arrivaltime`) and (`w`.`arrivaltime` <= now())),1,0)) AS `timeCount`,`t`.`id` AS `groupid`,`b`.`company_name` AS `company_name` from ((`waybill_tab` `w` join `group_tab` `t` on((`w`.`groupid` = `t`.`id`))) left join `customer_tab` `b` on((`w`.`customerid` = `b`.`id`))) group by `createTime`,`t`.`id`)



CREATE OR REPLACE VIEW waybill_Total_view AS SELECT
    DATE_FORMAT(DATE_SUB(`w`.`createtime`, INTERVAL CONVERT(substring(`t`.`endGoodsTime`, 1, 2), SIGNED) HOUR), '%Y-%m-%d') AS `createTime`,
    COUNT(`w`.`id`) AS `allCount`,
    SUM( IF((`w`.`arrivaltime` <= now()), 1, 0)) AS `toCount`,
    SUM( IF((`w`.`actual_arrival_time` IS NOT NULL), 1, 0)) AS `sendCount`,
    SUM( IF(((`w`.`actual_arrival_time` <= `w`.`arrivaltime`) AND (`w`.`arrivaltime` <= now())), 1, 0 ) ) AS `timeCount`,
    `w`.`groupid`,
    `w`.`receiver_name` AS `company_name`
FROM
	waybill_tab w LEFT JOIN group_tab t ON w.groupid = t.id
GROUP BY `createTime`, `w`.`groupid`;

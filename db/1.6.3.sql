-- ----------------------------
-- 新增回单扫描批次表
-- ----------------------------

DROP TABLE IF EXISTS `receipt_scan_batch`;
CREATE TABLE `receipt_scan_batch` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`batch_number`  varchar(255) DEFAULT NULL COMMENT '批次号' ,
`waybill_id`  int(11)DEFAULT NULL COMMENT '运单ID' ,
`createTime`  datetime DEFAULT NULL COMMENT '创建时间' ,
`user_id`  int(11) DEFAULT NULL COMMENT '用户ID' ,
`group_id`  int(11) DEFAULT NULL COMMENT '项目组ID' ,
`receipt_status`  int(11) DEFAULT NULL COMMENT '操作状态' ,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- ----------------------------
-- 运单表新增确认送达状态修改的方式字段
-- ----------------------------
ALTER TABLE waybill_tab ADD source_type varchar(50) DEFAULT 1 COMMENT '确认送达状态修改的方式(后台操作,上传回单,电子围栏)';


-- ----------------------------
-- 新增地图地址表
-- ----------------------------

DROP TABLE IF EXISTS `map_address`;
CREATE TABLE `map_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `address` varchar(300) NOT NULL COMMENT '地址',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  `user_id` int(11) DEFAULT NULL COMMENT '用户编号',
  `modify_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` int(11) DEFAULT '1' COMMENT '状态(9:删除,1:没完成,2:已完成)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- 创建统计视图
-- ----------------------------
CREATE OR REPLACE VIEW waybill_Total_view AS SELECT
	date_format((`w`.`createtime` - INTERVAL IF((`t`.`endGoodsTime` = '00:00'),0,cast(date_format(str_to_date(`t`.`endGoodsTime`, '%h'),'%h') AS signed )) HOUR ), '%Y-%m-%d') AS `createTime`,
	count( date_format((`w`.`createtime` - INTERVAL IF ((`t`.`endGoodsTime` = '00:00'), 0,cast(date_format(str_to_date(`t`.`endGoodsTime`, '%h'), '%h') AS signed)) HOUR ),'%Y-%m-%d')) AS `allCount`,
	sum(IF((`w`.`arrivaltime` <= now()),1,0) ) AS `toCount`,
	sum(IF((`w`.`actual_arrival_time` IS NOT NULL),1,0) ) AS `sendCount`,
	sum(IF(((`w`.`actual_arrival_time` <= `w`.`arrivaltime`) AND (`w`.`arrivaltime` <= now())), 1, 0)) AS `timeCount`,
	`t`.`id` AS `groupid`,
	`b`.`company_name` AS `company_name`
FROM
	(( `waybill_tab` `w` JOIN `group_tab` `t` ON ((`w`.`groupid` = `t`.`id`))) LEFT JOIN `customer_tab` `b` ON ((`w`.`customerid` = `b`.`id`)) )
GROUP BY
	`createTime`, `t`.`id`;


-- ----------------------------
-- 创建纸质回单视图
-- ----------------------------
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
	`b`.`barcode` AS `barcode`,
	`g`.`group_name` AS `group_name`,
	`c`.`company_name` AS `company_name`,
	`c`.`contacts` AS `contacts`,
	`c`.`contact_number` AS `contact_number`,
	`c`.`fullAddress` AS `fullAddress`,
	max(`rsb`.`batch_number`) AS `batch_number`,
	concat( `c`.`province`, `c`.`city`, `c`.`district` ) AS `address`,
	`w`.`number` AS `number`
FROM
	(
		(
			(
				(
					(
						`waybill_receipt_status` `wr` JOIN `waybill_tab` `w` ON (
							(
								(`wr`.`waybill_id` = `w`.`id`) AND ( `wr`.`receipt_status` = `w`.`papery_receipt_status` )
							)
						)
					)
					LEFT JOIN `barcode_tab` `b` ON ((`w`.`barcodeid` = `b`.`id`))
				)
				LEFT JOIN `group_tab` `g` ON ((`w`.`groupid` = `g`.`id`))
			)
			LEFT JOIN `customer_tab` `c` ON (
				(`w`.`customerid` = `c`.`id`)
			)
		)
		LEFT JOIN `receipt_scan_batch` `rsb` ON (
			(
				( `wr`.`waybill_id` = `rsb`.`waybill_id` ) AND ( `wr`.`receipt_status` = `rsb`.`receipt_status` )
			)
		)
	)
GROUP BY
	`wr`.`waybill_id`
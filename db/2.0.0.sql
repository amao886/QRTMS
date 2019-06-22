
-- ALTER TABLE waybill_tab CHANGE t_name t_name_new varchar(20)
-- 将用户表名称未加密的加密方便统一处理
UPDATE user_tab SET uname = TO_BASE64(uname) WHERE id < 1002;

-- 2.0.0版本修改任务单表
ALTER TABLE `waybill_tab` ADD `start_station` varchar(100) DEFAULT '' COMMENT '始发地';
ALTER TABLE `waybill_tab` ADD `simple_start_station` varchar(20) DEFAULT '' COMMENT '始发地简称';
ALTER TABLE `waybill_tab` ADD `end_station` varchar(100) DEFAULT '' COMMENT '目的地';
ALTER TABLE `waybill_tab` ADD `simple_end_station` varchar(20) DEFAULT '' COMMENT '目的地简称';
ALTER TABLE `waybill_tab` ADD `shipper_address` varchar(255) DEFAULT '' COMMENT '发货方地址';
ALTER TABLE `waybill_tab` ADD `shipper_tel` varchar(255) DEFAULT '' COMMENT '发货方固定电话';
ALTER TABLE `waybill_tab` ADD `shipper_contact_name` varchar(50) DEFAULT '' COMMENT '发货方联系人';
ALTER TABLE `waybill_tab` ADD `shipper_contact_tel` varchar(20) DEFAULT '' COMMENT '发货方联系人电话';
ALTER TABLE `waybill_tab` ADD `bind_time` datetime DEFAULT NULL COMMENT '绑定时间';
ALTER TABLE `waybill_tab` ADD `delivery_time` datetime DEFAULT NULL COMMENT '发货运输开始时间';

UPDATE waybill_tab SET bind_time = createtime, delivery_time = createtime WHERE createtime IS NOT NULL AND waybill_status > 10;

-- 好友通讯录新增好友类型
ALTER TABLE `friends_tab` ADD `friends_type` int(1) DEFAULT 0 COMMENT '0:普通 1：货主 2：承运商 3：司机';


-- 运单表
DROP TABLE IF EXISTS `conveyance_tab`;
CREATE TABLE `conveyance_tab` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `parent_key` bigint(20) DEFAULT 0 COMMENT '父运单编号',
  `barcode` VARCHAR(20) NOT NULL COMMENT '任务单号(条码号),关联运单表',
  `conveyance_number` VARCHAR(50) DEFAULT '' COMMENT '运单号,用户输入,系统不生成',
  `owner_key` INT (11) NOT NULL COMMENT '创建人用户ID',
  `owner_group_key` INT (11) DEFAULT 0 COMMENT '创建者分享到的项目组ID',
  `create_time` datetime DEFAULT NOW() COMMENT '生成时间',
  `assign_fettle` INT (11) DEFAULT 0 COMMENT '指派状态(0:初始状态,1:创建指派,2:承运人指派)',
  `contact_name` VARCHAR(100) DEFAULT '' COMMENT '联系人名称',
  `contact_phone` VARCHAR(20) DEFAULT '' COMMENT '联系人电话',
  `organization` VARCHAR(100) DEFAULT '' COMMENT '联系人所属组织',
  `assign_fettle_time` datetime DEFAULT NOW() COMMENT '指派状态变更时间',
  `assign_key` INT (11) DEFAULT 0 COMMENT '被指派人用户ID',
  `start_station` VARCHAR(100) DEFAULT '' COMMENT '发站',
  `simple_start_station` VARCHAR(100) DEFAULT '' COMMENT '发站简称',
  `end_station` VARCHAR(100)DEFAULT '' COMMENT '到站',
  `simple_end_station` VARCHAR(100)DEFAULT '' COMMENT '到站简称',
  `conveyance_fettle` INT (11) DEFAULT 20 COMMENT '运单状态(0:取消, 20:待运输, 30:运输中,35:送达)',
  `conveyance_fettle_time` datetime DEFAULT NOW() COMMENT '运单状态变更时间',
  `have_child` BIT(1) DEFAULT 0 COMMENT '是否有子节点',
  `next_key` BIGINT (20) DEFAULT 0 COMMENT '下一个节点ID',
  `node_level` INT (11) DEFAULT 1 COMMENT '节点层级',
  PRIMARY KEY (`id`),
  KEY `index_conveyance_barcode_key` (`barcode`),
  KEY `index_conveyance_parent_key` (`parent_key`),
  KEY `index_conveyance_owner_key` (`owner_key`),
  KEY `index_conveyance_owner_group_key` (`owner_group_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运单表';

-- ALTER TABLE `conveyance_tab` DROP `serial_number`;
-- ALTER TABLE `conveyance_tab` ADD `node_level` INT (11) DEFAULT 1 COMMENT '节点层级';

-- ALTER TABLE `conveyance_tab` DROP `assign_key`;

-- ALTER TABLE `conveyance_tab` ADD `contact_name` VARCHAR(100) DEFAULT '' COMMENT '联系人名称';
-- ALTER TABLE `conveyance_tab` ADD `contact_phone` VARCHAR(20) DEFAULT '' COMMENT '联系人电话';
-- ALTER TABLE `conveyance_tab` ADD `organization` VARCHAR(100) DEFAULT '' COMMENT '联系人所属组织';
-- 异常表新增运单ID字段
ALTER TABLE `exception_repor_tab` ADD `conveyance_id` bigint(20) DEFAULT 0 COMMENT '运单ID';

-- 常用地址表删除列(remark、arriveDay、arriveHour、grateSatus、radius)
ALTER TABLE `customer_tab` DROP `remark`, DROP `arriveDay`, DROP `arriveHour`, DROP `grateSatus`, DROP `radius`;

-- 常用地址表添加地址类型
ALTER TABLE `customer_tab` ADD `type` INT DEFAULT '1' COMMENT '地址类型，1：收货地址，2：发货地址';

-- 路由表
DROP TABLE IF EXISTS `route_tab`;
CREATE TABLE `route_tab` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户主键',
  `route_name` varchar(30) NOT NULL COMMENT '路由名称',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `index_route_userid_key` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='路由主表';

-- 路由线路表
DROP TABLE IF EXISTS `route_line_tab`;
CREATE TABLE `route_line_tab` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `route_id` int(11) DEFAULT '0' COMMENT '路由主键',
  `user_id` int(11) DEFAULT '0' COMMENT '当前用户的好友Id',
  `pid` int(11) DEFAULT '0' COMMENT '父节点',
  `line_type` int(11) DEFAULT NULL COMMENT '线路类型(1:起点，2：中转，3：终点)',
  `province` varchar(30) DEFAULT NULL COMMENT '省份',
  `city` varchar(30) DEFAULT NULL COMMENT '城市',
  `district` varchar(30) DEFAULT NULL COMMENT '区/县',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_route_line_route_id_key` (`route_id`),
  KEY `index_route_line_user_id_key` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8 COMMENT='路由线路表';

-- 用户热点表
DROP TABLE IF EXISTS `user_hotspot_tab`;
CREATE TABLE `user_hotspot_tab` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) DEFAULT '0' COMMENT '用户主键',
  `associate_type` int(11) NOT NULL COMMENT '热点类型(1:好友，2:常用地址，3:常用路由)',
  `associate_key` varchar(50) NOT NULL COMMENT '热点关联key',
  `hotspot_count` BIGINT(20) DEFAULT 0 COMMENT '热点数',
  PRIMARY KEY (`id`),
  KEY `index_associate_key` (`user_id`,`associate_type`,`associate_key`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='用户热点表';

-- 催办事件表
DROP TABLE IF EXISTS `reminders_things_tab`;
CREATE TABLE `reminders_things_tab` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `conveyance_id` bigint(20) NOT NULL COMMENT '运单主键',
  `msg_type` int(11) DEFAULT '1' COMMENT '消息类型（1：位置消息、2：回单消息）',
  `processing_status` int(11) DEFAULT '0' COMMENT '处理状态（0：未读、1：已读、2:转催、3：已处理、4：已完成、）',
  `sendkey` int(11) DEFAULT NULL COMMENT '催办人（当前用户）',
  `parent_news_id` int(11) NOT NULL COMMENT '父消息ID',
  `createtime` datetime DEFAULT NOW() COMMENT '创建时间',
  `msg_remark` varchar(100) DEFAULT NULL COMMENT '消息备注',
  `barcode` varchar(50) DEFAULT NULL COMMENT '任务单号',
  `update_time` datetime DEFAULT NOW() COMMENT '状态更新时间',
  PRIMARY KEY (`id`),
  KEY ` reference_conveyance_id` (`conveyance_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='催办事件表';

-- 库存回单下载、送交客户下载 新增体积
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


-- ----------------------------
-- 创建统计视图
-- ----------------------------
CREATE OR REPLACE VIEW waybill_Total_view AS select date_format((`w`.`createtime` - interval cast(substr(`t`.`endGoodsTime`,1,2) as signed) hour),'%Y-%m-%d') AS `createTime`,count(`w`.`id`) AS `allCount`,sum(if((date_format(`w`.`arrivaltime`,'%Y-%m-%d') <= date_format(now(),'%Y-%m-%d')),1,0)) AS `toCount`,sum(if((`w`.`actual_arrival_time` is not null),1,0)) AS `sendCount`,sum(if(((`w`.`actual_arrival_time` <= `w`.`arrivaltime`) and (date_format(`w`.`arrivaltime`,'%Y-%m-%d') <= date_format(now(),'%Y-%m-%d'))),1,0)) AS `timeCount`,`w`.`groupid` AS `groupid`,`w`.`receiver_name` AS `company_name` from (`waybill_tab` `w` join `group_tab` `t` on((`w`.`groupid` = `t`.`id`))) where (`w`.`waybill_status` <> 10) group by `createTime`,`w`.`groupid`;
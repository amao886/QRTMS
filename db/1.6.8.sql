UPDATE resourcse_app_tab SET print_status = 3 WHERE print_status = 1;

ALTER TABLE sys_request_serial MODIFY COLUMN agent_string VARCHAR(500);

DROP TABLE IF EXISTS `driver_container_tab`;
CREATE TABLE `driver_container_tab` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `barcode` varchar(20) DEFAULT NULL COMMENT '二维码',
  `load_id` int(11) DEFAULT NULL COMMENT '装车人',
  `load_time` datetime DEFAULT NOW() COMMENT '装车时间',
  `unload_id` int(11) DEFAULT NULL COMMENT '卸货人',
  `unload_time` datetime DEFAULT NULL COMMENT '卸货时间',
  `delivery_number` varchar(50) DEFAULT '' COMMENT '送货单号',
  `contact_name` varchar(50) DEFAULT '' COMMENT '收货联系人',
  `contact_number` varchar(50) DEFAULT '' COMMENT '收货联系人电话',
  `receive_address` varchar(255) DEFAULT NULL COMMENT '收货地址',
  `longitude` varchar(50) NOT NULL COMMENT '经度',
  `latitude` varchar(50) NOT NULL COMMENT '纬度',
  `bind_status` int(11) DEFAULT 0 COMMENT '10:未绑定,20:已经绑定',
  `unload` bit(1) DEFAULT 0 COMMENT '0:未卸车,1:卸车',
  PRIMARY KEY (`id`),
  KEY `barcode_key` (`barcode`),
  KEY `load_id_key` (`load_id`),
  KEY `unload_id_key` (`unload_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='司机装车';


DROP TABLE IF EXISTS `driver_track_tab`;
CREATE TABLE `driver_track_tab` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '上报人用户ID',
  `report_time` datetime DEFAULT NOW() COMMENT '上报时间',
  `report_loaction` varchar(255) NOT NULL COMMENT '上报地址',
  `longitude` varchar(50) NOT NULL COMMENT '经度',
  `latitude` varchar(50) NOT NULL COMMENT '纬度',
  PRIMARY KEY (`id`),
  KEY `user_id_key` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='司机轨迹';


DROP TABLE IF EXISTS `transition_track_tab`;
CREATE TABLE `transition_track_tab` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `barcode` varchar(20) NOT NULL COMMENT '条码号',
	`driver_track_id` bigint(20) NOT NULL COMMENT '司机轨迹',
  PRIMARY KEY (`id`),
  KEY `barcode_key` (`barcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='过渡期轨迹';



DROP TABLE IF EXISTS `transition_receipt_tab`;
CREATE TABLE `transition_receipt_tab` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `barcode` varchar(20) DEFAULT NULL COMMENT '二维码',
  `report_time` datetime DEFAULT NOW() COMMENT '上报时间',
  `user_id` int(11) NOT NULL COMMENT '上报人用户ID',
  PRIMARY KEY (`id`),
  KEY `user_id_key` (`user_id`),
  KEY `user_barcode_key` (`barcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='过渡期回单';


DROP TABLE IF EXISTS `image_storage_tab`;
CREATE TABLE `image_storage_tab` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `image_type` int(11) NOT NULL COMMENT '图片类型(1:送货单图片,2:过渡期回单,...)',
  `associate_key` varchar(50) NOT NULL COMMENT '关联',
  `storage_time` datetime DEFAULT NOW() COMMENT '图片存储时间',
  `storage_path` varchar(100) NOT NULL COMMENT '图片存储地址',
  PRIMARY KEY (`id`),
  KEY `user_associate_key` (`associate_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图片存储信息';

-- 删除多余的任务单
delete from waybill_tab
where barcode in (select a.barcode from ( select barcode from waybill_tab group by barcode having count(*) > 1) a)
and id not in (select b.minid from (select min(id) as minid from waybill_tab group by barcode having count(*) > 1) b);

-- 添加唯一约束
alter table waybill_tab add unique key `barcode_uq_key` (barcode);


DROP TABLE IF EXISTS `transition_exception_tab`;
CREATE TABLE `transition_exception_tab` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `barcode` varchar(20) DEFAULT NULL COMMENT '二维码',
  `report_time` datetime DEFAULT NOW() COMMENT '上报时间',
  `content` varchar(255) DEFAULT '' COMMENT '异常内容',
  `user_id` int(11) NOT NULL COMMENT '上报人用户ID',
  PRIMARY KEY (`id`),
  KEY `user_id_key` (`user_id`),
  KEY `user_barcode_key` (`barcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='过渡期异常';

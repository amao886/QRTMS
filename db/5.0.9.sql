-- 运单表添加字段 --
ALTER TABLE `waybill_tab` ADD COLUMN `load_no` VARCHAR(20) DEFAULT ""  COMMENT '装运号'; 
ALTER TABLE `waybill_tab` ADD COLUMN `car_type` VARCHAR(150) DEFAULT ""  COMMENT '车型'; 
ALTER TABLE `waybill_tab` ADD COLUMN `distance` DECIMAL(10,2) DEFAULT 0.00  COMMENT '最新位置与目的地的距离';
ALTER TABLE `waybill_tab` ADD COLUMN `load_time` VARCHAR(50) DEFAULT ""  COMMENT '装运日期';
ALTER TABLE `waybill_tab` ADD COLUMN `pre_distance` DECIMAL(10,2) DEFAULT 0.00 COMMENT '上一次位置与目的地的距离';
ALTER TABLE `driver_track_tab` ADD COLUMN `barcode` varchar(20) DEFAULT '' COMMENT '条码编号',
ALTER TABLE `track_tab` ADD COLUMN `describe` varchar(1000) DEFAULT '' COMMENT '节点描述',
ALTER TABLE `customer_tab` ADD COLUMN `customer_code` varchar(30) DEFAULT '' COMMENT '客户编码',
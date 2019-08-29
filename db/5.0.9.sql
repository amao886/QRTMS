-- 运单表添加字段 --
ALTER TABLE `waybill_tab` ADD COLUMN `load_no` VARCHAR(20) DEFAULT ""  COMMENT '装运号'; 
ALTER TABLE `waybill_tab` ADD COLUMN `car_type` VARCHAR(150) DEFAULT ""  COMMENT '车型'; 
ALTER TABLE `waybill_tab` ADD COLUMN `distance` DECIMAL(10,2) DEFAULT 0.00  COMMENT '最新位置与目的地的距离';
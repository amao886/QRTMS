
-- 系统菜单新增用户身份标识
ALTER TABLE `t_sys_menu` ADD COLUMN `ID_KEY` VARCHAR(100) DEFAULT '' COMMENT '身份标识(1:司机,2:发货发,3:承运方,4:收货方)';

-- 回单上传增加位置记录
ALTER TABLE `t_paper_receipt` ADD COLUMN `LONGITUDE` DOUBLE DEFAULT '' COMMENT '回单上传-经度';
ALTER TABLE `t_paper_receipt` ADD COLUMN `LATITUDE` DOUBLE DEFAULT '' COMMENT '回单上传-纬度';
ALTER TABLE `t_paper_receipt` ADD COLUMN `LOCATION` VARCHAR(300) DEFAULT '' COMMENT '回单上传-位置地址';

-- 企业新增企业地址字段
ALTER TABLE `t_company` ADD COLUMN `ADDRESS` VARCHAR(300) DEFAULT '' COMMENT '企业地址';
-- 企业客户新增扫码手机号字段
ALTER TABLE `t_company_customer` ADD COLUMN `SCAN_PHONE` VARCHAR(20) DEFAULT '' COMMENT '扫码手机号';

-- 订单表新增
ALTER TABLE `T_ORDER` ADD COLUMN `PICK_UP_WARNING` INT(1) DEFAULT 0 COMMENT '提货预警  1：正常提货 2：延迟提货';
ALTER TABLE `T_ORDER` ADD COLUMN `DELAY_WARNING` INT(1) DEFAULT 0 COMMENT '延迟预警1：正常运输 2：可能延迟 3：已延迟';
ALTER TABLE `T_ORDER` ADD COLUMN `POSITIONING_CHECK` INT(1) DEFAULT 0 COMMENT '定位检查1，已达标 2未达标，0为检查';

-- 订单作废表新增
ALTER TABLE `T_ORDER_INVALID` ADD COLUMN `PICK_UP_WARNING` INT(1) DEFAULT 0 COMMENT '提货预警  1：正常提货 2：延迟提货';
ALTER TABLE `T_ORDER_INVALID` ADD COLUMN `DELAY_WARNING` INT(1) DEFAULT 0 COMMENT '延迟预警1：正常运输 2：可能延迟 3：已延迟';
ALTER TABLE `T_ORDER_INVALID` ADD COLUMN `POSITIONING_CHECK` INT(1) DEFAULT 0 COMMENT '定位检查1，已达标 2未达标，0为检查';
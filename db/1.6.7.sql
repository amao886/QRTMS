DELETE FROM group_members_tab WHERE status = 20;

UPDATE resourcse_app_tab SET print_status = 1 WHERE print_status = 0;

ALTER TABLE `customer_tab` ADD `radius` INT(11) DEFAULT 5 COMMENT '电子围栏半径,公里';
ALTER TABLE `customer_tab` ADD `group_id` INT(11) DEFAULT 0 COMMENT '所属项目组编号';

UPDATE 
  customer_tab c, customer_group_tab cg
SET
  c.group_id = cg.groupid
WHERE 
  c.id = cg.customerid;


ALTER TABLE `resourcse_app_tab` ADD `start_num` varchar(30) DEFAULT NULL COMMENT '开始条码号';
ALTER TABLE `resourcse_app_tab` ADD `end_num` varchar(30) DEFAULT NULL COMMENT '结束条码号';

ALTER TABLE `resourcse_app_tab` drop COLUMN `address`;
ALTER TABLE `resourcse_app_tab` drop COLUMN `contacts`;
ALTER TABLE `resourcse_app_tab` drop COLUMN `contact_number`;

UPDATE resourcse_app_tab rat, (SELECT MIN(barcode) AS mincode, MAX(barcode) AS maxcode, resourceid FROM barcode_tab GROUP BY resourceid) mc
SET rat.start_num = mc.mincode ,rat.end_num = mc.maxcode
WHERE rat.id = mc.resourceid;


ALTER TABLE `barcode_tab` ADD `userid` INT DEFAULT 0 COMMENT '条码所属用户ID';

UPDATE 
  barcode_tab b, resourcse_app_tab r
SET
  b.userid = r.userid
WHERE 
  b.resourceid = r.id;



ALTER TABLE goods MODIFY COLUMN goods_quantity INT DEFAULT 0 COMMENT '货物数量';
ALTER TABLE goods MODIFY COLUMN goods_weight DOUBLE DEFAULT 0.0 COMMENT '货物重量';
ALTER TABLE goods MODIFY COLUMN goods_volume DOUBLE DEFAULT 0.0 COMMENT '货物体积';

UPDATE goods SET goods_quantity = 0 WHERE goods_quantity IS NULL;
UPDATE goods SET goods_weight = 0 WHERE goods_weight IS NULL;
UPDATE goods SET goods_volume = 0 WHERE goods_volume IS NULL;


UPDATE 
  waybill_tab w, (SELECT SUM(goods_quantity) AS quantity, SUM(goods_weight) AS weight, SUM(goods_volume) AS volume, waybillid FROM goods GROUP BY waybillid) cg
SET
  w.number = cg.quantity,
  w.weight = cg.weight,
  w.volume = cg.volume
WHERE 
  w.id = cg.waybillid;



DROP TABLE IF EXISTS `smsinfo_tab`;
CREATE TABLE `smsinfo_tab` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `userid` int(11) DEFAULT NULL COMMENT '发送人',
  `sendtime` datetime DEFAULT NULL COMMENT '发送时间',
  `mobile_phone` varchar(20) DEFAULT NULL COMMENT '接收手机号',
  `context` varchar(500) DEFAULT NULL COMMENT '发送内容',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='短息发送表';

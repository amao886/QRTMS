
ALTER TABLE `waybill_tab` ADD `receipt_unqualify_count` INT(11) DEFAULT 0 COMMENT '回单审核不合格数量';


CREATE OR REPLACE VIEW waybill_receipt_verify_view AS SELECT
	count(0) AS `verify_total`,
	sum( IF ( ( ifnull(`address`.`verify_status` , -(1) ) >= 0 ), 1, 0 ) ) AS `verify_count`,
	sum( IF ( `address`.`verify_status` = 0 , 1, 0 ) ) AS `unqualify_count`,
	min(`receipt`.`createtime`) AS `updatetime`,
	max(`address`.`verify_date`) AS `verify_date`,
	`receipt`.`waybillid` AS `waybillid`
FROM
	(
		`receipt_tab` `receipt` LEFT JOIN `address_tab` `address` ON ( ( `address`.`receiptid` = `receipt`.`id` ) )
	)
GROUP BY
	`receipt`.`waybillid`;


-- 修复回单审核数据
UPDATE waybill_tab w,
 waybill_receipt_verify_view v
SET w.receipt_count = v.verify_total,
 w.receipt_verify_count = v.verify_count,
 w.receipt_unqualify_count = v.unqualify_count,
 w.receipt_verify_status = CASE
	WHEN v.verify_count = 0 THEN
		1
	WHEN v.verify_count <= 0 AND v.verify_total > 0 THEN
		2
	WHEN v.verify_count > 0 AND v.verify_count < v.verify_total THEN
		3
	ELSE
		4
	END
WHERE
	w.id = v.waybillid;

-- ----------------------------
-- 通讯录好友表
-- ----------------------------
DROP TABLE IF EXISTS `friends_tab`;
CREATE TABLE `friends_tab` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `userid` int(11) NOT NULL DEFAULT '0' COMMENT '当前用户Id',
  `pid` int(11) DEFAULT NULL COMMENT '对应用户表中Id',
  `mobile_phone` varchar(11) NOT NULL COMMENT '好友手机号',
  `full_name` varchar(100) NOT NULL COMMENT '好友姓名',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `company` varchar(100) DEFAULT NULL COMMENT '所属公司',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8;

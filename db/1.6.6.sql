ALTER TABLE `waybill_tab` ADD `shipper_name` VARCHAR(100) DEFAULT NULL COMMENT '货主名称';

-- ----------------------------
-- 货物信息表
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `waybillid` int(11) NOT NULL COMMENT '任务单id',
  `goods_name` varchar(150) DEFAULT NULL COMMENT '物料名称',
  `goods_type` varchar(20) DEFAULT NULL COMMENT '客户料号',
  `goods_quantity` int(11) DEFAULT NULL COMMENT '货物数量',
  `goods_weight` double DEFAULT NULL COMMENT '货物重量',
  `goods_volume` double DEFAULT NULL COMMENT '货物体积',
  `summary` varchar(255) DEFAULT NULL COMMENT '摘要',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='货物明细表';


INSERT INTO goods (waybillid, goods_quantity, goods_weight, goods_volume, create_time, update_time) SELECT id, number,weight, volume, NOW(), NOW() FROM waybill_tab WHERE number > 0 OR weight>0 OR volume > 0;


-- ----------------------------
-- 创建统计视图
-- ----------------------------
CREATE OR REPLACE VIEW waybill_Total_view AS SELECT
    DATE_FORMAT(DATE_SUB(`w`.`createtime`, INTERVAL CONVERT(substring(`t`.`endGoodsTime`, 1, 2), SIGNED) HOUR), '%Y-%m-%d') AS `createTime`,
    COUNT(`w`.`id`) AS `allCount`,
    SUM( IF((`w`.`arrivaltime` <= now()), 1, 0)) AS `toCount`,
    SUM( IF((`w`.`actual_arrival_time` IS NOT NULL), 1, 0)) AS `sendCount`,
    SUM( IF(((`w`.`actual_arrival_time` <= `w`.`arrivaltime`) AND (`w`.`arrivaltime` <= now())), 1, 0 ) ) AS `timeCount`,
    `w`.`groupid`,
    `w`.`receiver_name` AS `company_name`
FROM
	waybill_tab `w` JOIN group_tab `t` ON `w`.groupid = `t`.`id`
WHERE `w`.`waybill_status` <> 10
GROUP BY `createTime`, `w`.`groupid`;


ALTER TABLE `waybill_tab` ADD `loaction_time` DATETIME DEFAULT NULL COMMENT '最新位置上报时间';

UPDATE waybill_tab w,
 (
  SELECT * FROM track_tab WHERE id IN (SELECT MAX(id) AS id FROM track_tab WHERE locations IS NOT NULL GROUP BY waybillid)
) t
SET w.address = t.locations,
 w.loaction_time = t.createtime
WHERE
  w.waybill_status <> 10;
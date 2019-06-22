/*
Navicat MySQL Data Transfer

Source Server         : 千里马测试库
Source Server Version : 50631
Source Host           : 172.16.36.80:3306
Source Database       : track

Target Server Type    : MYSQL
Target Server Version : 50631
File Encoding         : 65001

Date: 2017-11-11 16:14:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for map_address
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
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;

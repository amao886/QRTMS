-- ----------------------------
-- Table structure for t_contract
-- ----------------------------
DROP TABLE IF EXISTS `t_contract`;
CREATE TABLE `t_contract` (
  `id` bigint(20) NOT NULL,
  `contract_no` varchar(255) DEFAULT NULL,
  `first_time` datetime DEFAULT NULL,
  `second_time` datetime DEFAULT NULL,
  `contract_type` int(11) DEFAULT NULL,
  `company_key` bigint(20) DEFAULT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `verify_status` int(11) DEFAULT NULL,
  `opposite_key` bigint(20) DEFAULT NULL,
  `opposite_name` varchar(255) DEFAULT NULL,
  `industry_category` int(11) DEFAULT NULL,
  `contact_name` varchar(255) DEFAULT NULL,
  `telephone_number` varchar(255) DEFAULT NULL,
  `mobile_number` varchar(255) DEFAULT NULL,
  `customer_address` varchar(255) DEFAULT NULL,
  `opposite_province` varchar(255) DEFAULT NULL,
  `opposite_city` varchar(255) DEFAULT NULL,
  `opposite_district` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_contract_commodity_config
-- ----------------------------
DROP TABLE IF EXISTS `t_contract_commodity_config`;
CREATE TABLE `t_contract_commodity_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `commodity_code` varchar(255) DEFAULT NULL,
  `commodity_name` varchar(255) DEFAULT NULL,
  `unit_weight` double DEFAULT NULL,
  `unit_volume` double DEFAULT NULL,
  `fare_type` int(11) DEFAULT NULL,
  `contract_key` bigint(20) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4owroe7788e3f41n5be367vp2` (`contract_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_contract_commodity_verify
-- ----------------------------
DROP TABLE IF EXISTS `t_contract_commodity_verify`;
CREATE TABLE `t_contract_commodity_verify` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `verify_status` int(11) DEFAULT NULL,
  `verify_time` datetime DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `contract_key` bigint(20) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKawecd3x31cjj4rpjn4tiu3w14` (`contract_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_contract_fragmentary_unitprice
-- ----------------------------
DROP TABLE IF EXISTS `t_contract_fragmentary_unitprice`;
CREATE TABLE `t_contract_fragmentary_unitprice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `origin` varchar(255) DEFAULT NULL,
  `destination` varchar(255) DEFAULT NULL,
  `city_lv` int(11) DEFAULT NULL,
  `fare_category` int(11) DEFAULT NULL,
  `valuation_key` bigint(20) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  `price_src` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKft4ool8iedac1y5skyxiqcw5` (`valuation_key`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_contract_fragmentary_valuation
-- ----------------------------
DROP TABLE IF EXISTS `t_contract_fragmentary_valuation`;
CREATE TABLE `t_contract_fragmentary_valuation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_time` datetime DEFAULT NULL,
  `second_time` datetime DEFAULT NULL,
  `commodity_category` int(11) DEFAULT NULL,
  `commodity_unit` int(11) DEFAULT NULL,
  `fare_type` int(11) DEFAULT NULL,
  `contract_key` bigint(20) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  `interval_str` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8ax3vv5kbs34x288ey7u89ofg` (`contract_key`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

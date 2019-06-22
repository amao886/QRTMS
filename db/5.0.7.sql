-- service
use track;

-- 订单新增要求提货时间
ALTER TABLE `t_order` ADD COLUMN `COLLECT_TIME` DATETIME  DEFAULT NULL COMMENT '要求提货时间';


DROP TABLE IF EXISTS `t_scene_vehicle_registration`;
CREATE TABLE `t_scene_vehicle_registration` (
  `order_key` bigint(20) NOT NULL,
  `arrival_time` datetime DEFAULT NULL,
  `delivery_no` varchar(255) DEFAULT NULL,
  `driver_name` varchar(255) DEFAULT NULL,
  `driver_contact` varchar(255) DEFAULT NULL,
  `license` varchar(255) DEFAULT NULL,
  `in_driverName` varchar(255) DEFAULT NULL,
  `in_driverContact` varchar(255) DEFAULT NULL,
  `in_license` varchar(255) DEFAULT NULL,
  `arrival_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`order_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



INSERT INTO `t_sys_menu` (`ID`, `P_ID`, `MENU_NAME`, `MENU_URL`, `MENU_FETTLE`, `MENU_ICON`, `MENU_TYPE`, `CODE`, `ID_KEY`) VALUES ('75', '0', '现场管理', '', '1', 'orderManage', '1', NULL, '2');
INSERT INTO `t_sys_menu` (`ID`, `P_ID`, `MENU_NAME`, `MENU_URL`, `MENU_FETTLE`, `MENU_ICON`, `MENU_TYPE`, `CODE`, `ID_KEY`) VALUES ('76', '75', '现场管理', 'web/views/siteManagement/siteManagementList.html', '1', '', '1', '205', '2');
INSERT INTO `t_sys_role_menu` (`R_ID`, `M_ID`) VALUES ('4', '75');
INSERT INTO `t_sys_role_menu` (`R_ID`, `M_ID`) VALUES ('4', '76');

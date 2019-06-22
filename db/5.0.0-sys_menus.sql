
-- ----------------------------
-- Table structure for T_SYS_MENU
-- ----------------------------
DROP TABLE IF EXISTS `T_SYS_MENU`;
CREATE TABLE `T_SYS_MENU` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `P_ID` int(11) NOT NULL DEFAULT '0' COMMENT '父级主键',
  `MENU_NAME` varchar(100) NOT NULL COMMENT '菜单名称',
  `MENU_URL` varchar(200) DEFAULT '' COMMENT '菜单地址',
  `MENU_FETTLE` int(11) DEFAULT '1' COMMENT '菜单状态(1:启用，0:禁用)',
  `MENU_ICON` varchar(100) DEFAULT NULL COMMENT '菜单图标',
  `MENU_TYPE` int(11) DEFAULT NULL COMMENT '菜单分类 1：PC    2:  微信',
  `CODE` varchar(20) DEFAULT NULL COMMENT '菜单编号',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;


CREATE TABLE `t_sys_menu` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `P_ID` int(11) NOT NULL DEFAULT '0' COMMENT '父级主键',
  `MENU_NAME` varchar(100) NOT NULL COMMENT '菜单名称',
  `MENU_URL` varchar(200) DEFAULT '' COMMENT '菜单地址',
  `MENU_FETTLE` int(11) DEFAULT '1' COMMENT '菜单状态(1:启用，0:禁用)',
  `MENU_ICON` varchar(100) DEFAULT NULL COMMENT '菜单图标',
  `MENU_TYPE` int(11) DEFAULT NULL COMMENT '菜单分类 1：PC    2:  微信',
  `CODE` varchar(20) DEFAULT NULL COMMENT '菜单编号',
  `ID_KEY` varchar(100) DEFAULT '' COMMENT '身份标识(1:司机,2:发货发,3:承运方,4:收货方)',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------
-- Records of T_SYS_MENU
-- ----------------------------
INSERT INTO `T_SYS_MENU` VALUES ('1', '0', '数据统计', '', '1', 'dataCount', '3', null);
INSERT INTO `T_SYS_MENU` VALUES ('2', '1', '后台首页', 'admin/business/loadBehaviorTotal', '1', null, '3', null);
INSERT INTO `T_SYS_MENU` VALUES ('3', '1', '用户行为', 'admin/business/queryUserbehavior', '1', null, '3', null);
INSERT INTO `T_SYS_MENU` VALUES ('4', '0', '系统管理', '', '1', 'systemManage', '3', null);
INSERT INTO `T_SYS_MENU` VALUES ('5', '4', '权限管理', 'admin/permission/search', '1', null, '3', null);
INSERT INTO `T_SYS_MENU` VALUES ('6', '4', '条码管理', 'admin/barcode/all/search', '1', null, '3', null);
INSERT INTO `T_SYS_MENU` VALUES ('7', '4', '条码分配', 'admin/barcode/search', '1', null, '3', null);
INSERT INTO `T_SYS_MENU` VALUES ('8', '0', '每日统计', '', '1', 'daily-total', '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('9', '8', '日统计表', 'backstage/dailyTotal/searchDayCount', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('10', '8', '月统计图', 'backstage/dailyTotal/monthchart', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('11', '0', '发货管理', '', '1', 'trace', '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('12', '11', '我要发货', 'backstage/trace/send/goods', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('13', '11', '我的任务', 'backstage/trace/search', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('14', '11', '待绑码任务', 'backstage/driver/search', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('15', '0', '运单调度', '', '1', 'conveyance', '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('16', '15', '运单指派', 'backstage/conveyance/search', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('17', '0', '工作协同', '', '1', 'cooridination', '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('18', '17', '日常工作', 'backstage/coordination/search', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('19', '17', '异常记录', 'backstage/coordination/get/searchExceptionPage', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('20', '0', '任务监控', '', '1', 'follow-into', '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('21', '20', '任务监控', 'backstage/trace/follow/into', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('22', '0', '电子回单管理', '', '1', 'receipt', '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('23', '22', '回单审核', 'backstage/receipt/search', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('24', '22', '待处理回单', 'backstage/receipt/wait/search', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('25', '0', '纸质回单管理', '', '1', 'page-bill', '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('26', '25', '回单列表', 'backstage/receipt/queryReceiptList', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('27', '25', '回单扫描', 'backstage/receipt/scan/enter', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('28', '25', '统计分析', 'backstage/receipt/analyze', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('29', '0', '常用设置', '', '1', 'resize', '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('30', '29', '地址管理', 'backstage/customer/search?type=2', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('31', '29', '好友管理', 'backstage/friend/search', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('32', '29', '路由管理', 'backstage/route/list', '1', null, '2', null);
INSERT INTO `T_SYS_MENU` VALUES ('33', '0', '运输管理', '', '1', 'orderManage', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('34', '33', '发货管理', 'enterprise/order/traffic/shipper', '1', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('35', '0', '企业管理', '', '1', 'companyManage', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('36', '35', '企业管理', 'enterprise/company/view/manage', '1', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('37', '35', '企业添加', 'enterprise/company/view/add', '0', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('38', '35', '企业认证', 'enterprise/company/view/legalize', '0', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('39', '0', '运营后台', '', '1', 'backstageTab', '3', null);
INSERT INTO `T_SYS_MENU` VALUES ('40', '39', '企业管理', 'enterprise/backstage/view/companymanage', '1', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('41', '39', '签收管理', 'enterprise/backstage/view/signandmanage', '1', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('42', '0', '茅台打印', '', '1', 'printTab', '4', null);
INSERT INTO `T_SYS_MENU` VALUES ('43', '42', '发货单', 'backstage/moutai/view/order', '1', null, '4', null);
INSERT INTO `T_SYS_MENU` VALUES ('44', '42', '收货客户', 'backstage/moutai/view/customer', '1', null, '4', null);
INSERT INTO `T_SYS_MENU` VALUES ('45', '42', '取单人', 'backstage/moutai/view/person', '1', null, '4', null);
INSERT INTO `T_SYS_MENU` VALUES ('46', '49', '作废回单', 'enterprise/order/traffic/invalid', '1', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('47', '33', '承运管理', 'enterprise/order/traffic/conveyer', '1', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('48', '33', '收货管理', 'enterprise/order/traffic/receiver', '1', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('49', '0', '电子回单管理', '', '1', 'orderManage', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('50', '49', '发货回单', 'enterprise/order/receipt/shipper', '1', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('51', '49', '承运回单', 'enterprise/order/receipt/conveyer', '1', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('52', '49', '收货回单', 'enterprise/order/receipt/receiver', '1', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('53', '35', '员工管理', 'enterprise/company/view/employee', '1', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('54', '35', '签章管理', 'enterprise/company/view/signaturemanage', '1', '', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('55', '33', '发货配置', 'enterprise/template/view/manage', '1', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('56', '39', '发货管理', 'enterprise/backstage/view/shippermanage', '1', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('57', '0', '客户管理', '', '1', 'orderManage', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('58', '57', '客户管理', 'enterprise/customer/view/manage', '1', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('59', '57', '地址管理', 'enterprise/customer/view/address', '1', null, '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('60', '4', '企业条码管理', 'admin/barcode/allocated/search', '1', null, '3', null);
INSERT INTO `T_SYS_MENU` VALUES ('61', '4', '用户管理', 'admin/user/userManagement', '1', null, '3', null);

-- ----------------------------
-- Table structure for T_SYS_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `T_SYS_ROLE`;
CREATE TABLE `T_SYS_ROLE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ROLE_NAME` varchar(100) NOT NULL COMMENT '角色名称',
  `ROLE_FETTLE` int(11) DEFAULT '1' COMMENT '角色状态(1:启用，0:禁用)',
  `ROLE_CATEGORY` int(11) NOT NULL COMMENT '角色类别(1:普通角色，9999:超级管理员)',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of T_SYS_ROLE
-- ----------------------------
INSERT INTO `T_SYS_ROLE` VALUES ('1', '超级管理员', '1', '9999');
INSERT INTO `T_SYS_ROLE` VALUES ('2', '普通角色', '1', '1');
INSERT INTO `T_SYS_ROLE` VALUES ('3', '茅台角色', '1', '101');
INSERT INTO `T_SYS_ROLE` VALUES ('4', '企业角色', '1', '102');

-- ----------------------------
-- Table structure for T_SYS_ROLE_MENU
-- ----------------------------
DROP TABLE IF EXISTS `T_SYS_ROLE_MENU`;
CREATE TABLE `T_SYS_ROLE_MENU` (
  `R_ID` int(11) NOT NULL COMMENT '角色ID',
  `M_ID` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`R_ID`,`M_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of T_SYS_ROLE_MENU
-- ----------------------------
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '1');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '2');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '3');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '4');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '5');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '6');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '7');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '8');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '9');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '10');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '11');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '12');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '13');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '14');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '15');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '16');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '17');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '18');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '19');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '20');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '21');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '22');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '23');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '24');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '25');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '26');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '27');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '28');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '29');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '30');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '31');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('2', '32');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('3', '42');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('3', '43');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('3', '44');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('3', '45');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '33');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '34');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '35');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '36');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '37');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '38');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '39');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '40');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '41');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '46');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '47');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '48');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '49');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '50');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '51');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '52');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '53');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '54');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '55');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '56');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '57');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '58');
INSERT INTO `T_SYS_ROLE_MENU` VALUES ('4', '59');

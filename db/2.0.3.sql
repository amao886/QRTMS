DROP TABLE IF EXISTS `managing_users_tab`;
CREATE TABLE `managing_users_tab` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '关联用户ID',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `createtime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `last_login_ip` varchar(255) DEFAULT NULL COMMENT '上次登陆IP',
  `realname` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  `last_login_time` datetime DEFAULT NULL,
  `user_status` int(11) DEFAULT '1' COMMENT '用户状态 0：注销   1 正常',
  `user_type` int(11) DEFAULT '2' COMMENT '用户类型1:超管  2:普管',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2590 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user_commonly`;
CREATE TABLE `user_commonly` (
  `user_key` int(11) NOT NULL COMMENT '用户主键',
  `commonly` varchar(255) DEFAULT '' COMMENT '常用功能编号(#隔开)',
  PRIMARY KEY (`user_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE waybill_tab MODIFY COLUMN receiver_tel VARCHAR(100) DEFAULT NULL;

DROP TABLE IF EXISTS `behavior_differentiation`;
CREATE TABLE `behavior_differentiation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uri_key` varchar(255) DEFAULT NULL COMMENT 'uri标识key',
  `app_type` varchar(30) DEFAULT NULL COMMENT '所属平台 backstage:WEB端  api:微信端',
  `function_point` varchar(255) DEFAULT NULL COMMENT '功能点注释',
  `subordinate_module` varchar(255) DEFAULT NULL COMMENT '所属模块',
  PRIMARY KEY (`id`),
  KEY `key` (`uri_key`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of behavior_differentiation
-- ----------------------------
INSERT INTO `behavior_differentiation` VALUES ('1', '9772B5CD9FDEA1702F5EEDE4D26CBD0B', 'backstage', '日统计表', '日统计表');
INSERT INTO `behavior_differentiation` VALUES ('2', '7DCAA54C13459FD1233E124ED2EBD4A0', 'backstage', '日统计表详情', '日统计表');
INSERT INTO `behavior_differentiation` VALUES ('3', '32C077130CF7D4BF1B332378296202D3', 'backstage', '月统计表', '月统计表');
INSERT INTO `behavior_differentiation` VALUES ('6', '682C1CED7F232951AC2DD69C2A861B14', 'backstage', '我要发货', '发货管理');
INSERT INTO `behavior_differentiation` VALUES ('7', '5EFD915633F1E308FE3F8A9737B583BB', 'backstage', '我的任务', '发货管理');
INSERT INTO `behavior_differentiation` VALUES ('8', '80B5E40C701B79DD87C76F7A0111AA2F', 'backstage', '批量发货', '发货管理');
INSERT INTO `behavior_differentiation` VALUES ('9', '368BEAD6B1A6024F7F7077F21E9E0108', 'backstage', '模板下载', '发货管理');
INSERT INTO `behavior_differentiation` VALUES ('10', 'D06A48AEAE02F99BB4B4D14C84251523', 'backstage', '任务删除', '发货管理');
INSERT INTO `behavior_differentiation` VALUES ('11', '0A244A2147892E81350AD1510FF7A3AC', 'backstage', '任务查看', '发货管理');
INSERT INTO `behavior_differentiation` VALUES ('12', '37703074D2F677AFF8DC0E757B7ACFE8', 'backstage', '任务编辑', '发货管理');
INSERT INTO `behavior_differentiation` VALUES ('13', '722EEA08CA79F24CE24A8AEA70AAD7EB', 'backstage', '任务确认到货', '发货管理');
INSERT INTO `behavior_differentiation` VALUES ('14', 'BBAAC8FE6CEEF2743AAC179FF138F701', 'backstage', '待绑码任务报表', '发货管理');
INSERT INTO `behavior_differentiation` VALUES ('15', 'E6E261F925F8D58DC9C5C85DDC252040', 'backstage', '绑码任务', '发货管理');
INSERT INTO `behavior_differentiation` VALUES ('16', 'AB156392B40D4165DA81E42A8ABCA5EF', 'backstage', '运单指派报表', '运单调度');
INSERT INTO `behavior_differentiation` VALUES ('17', '1C1D34870D198A3A52065CB1D3153DD3', 'backstage', '运单整包指派', '运单调度');
INSERT INTO `behavior_differentiation` VALUES ('18', 'E15762F6B84B6BBAFEFA979FB5C74985', 'backstage', '路由创建', '路由管理');
INSERT INTO `behavior_differentiation` VALUES ('19', '2C70D45DA66868DE45F9B20C1BB442C8', 'backstage', '运单分享到项目组', '运单调度');
INSERT INTO `behavior_differentiation` VALUES ('20', '72E6D1ECB3DCD9B1A33C3D35A7D79B14', 'backstage', '运单取消指派', '运单调度');
INSERT INTO `behavior_differentiation` VALUES ('21', '36953171E3B287AE133756AADCE33B21', 'backstage', '日常工作报表', '工作协同');
INSERT INTO `behavior_differentiation` VALUES ('22', 'C1E33AE1876BDB81C87D58DCBD8B876B', 'backstage', '异常记录报表', '工作协同');
INSERT INTO `behavior_differentiation` VALUES ('23', '5AF91A83DB8BAB8CF69911082AA4EFC0', 'backstage', '电子回单审核报表', '电子回单审核');
INSERT INTO `behavior_differentiation` VALUES ('24', 'B4F9DA3AFD18EE21AC3501BE5237432A', 'backstage', '待处理回单报表', '电子回单审核');
INSERT INTO `behavior_differentiation` VALUES ('25', 'B7089E17C466C1D5372F8974DD43A324', 'backstage', '待处理回单查看详情', '电子回单审核');
INSERT INTO `behavior_differentiation` VALUES ('26', 'C5EBC3E805AF482D58F546CE648306F1', 'backstage', '库存回单报表', '纸质回单管理');
INSERT INTO `behavior_differentiation` VALUES ('27', '670C06CF3C99057E3A9906BA46F73913', 'backstage', '送交客户报表', '纸质回单管理');
INSERT INTO `behavior_differentiation` VALUES ('28', '7F4A855282EA4DD3AAFCF66766D95AE0', 'backstage', '历史查询报表', '纸质回单管理');
INSERT INTO `behavior_differentiation` VALUES ('29', '653CDA4C2044F10B65D8F5F56AC8F7DF', 'backstage', '统计分析报表', '纸质回单管理');
INSERT INTO `behavior_differentiation` VALUES ('30', '3863EC1C09684E03FE716D28993012F3', 'backstage', '好友管理列表', '常用设置');
INSERT INTO `behavior_differentiation` VALUES ('31', 'F1B7076A8C57E22AC1C52E062299524B', 'backstage', '添加好友', '好友管理');
INSERT INTO `behavior_differentiation` VALUES ('32', '32A197DDD7ABB726C89628CA98F29E6B', 'backstage', '批量删除好友', '好友管理');
INSERT INTO `behavior_differentiation` VALUES ('33', 'D8D630272212CBDA825E2D7FD906942E', 'backstage', '编辑好友', '好友管理');
INSERT INTO `behavior_differentiation` VALUES ('34', '4D288E6A0FBC9C554945E51D1E40CBE9', 'backstage', '路由管理报表', '路由管理');
INSERT INTO `behavior_differentiation` VALUES ('35', 'EA549CCBF0FA2CFFD929C8A7D645F928', 'backstage', '删除路由', '路由管理');
INSERT INTO `behavior_differentiation` VALUES ('36', '6DDA3EB952116C9214111E9266ABC1CC', 'backstage', '编辑路由', '路由管理');
INSERT INTO `behavior_differentiation` VALUES ('37', '4E233232F94FEE623626E5CA9D5D3CB2', 'api', '装车卸货报表', '装车卸货');
INSERT INTO `behavior_differentiation` VALUES ('38', '109594FA87414BC853FFF881A7F6EEDB', 'api', '扫码装货', '装车卸货');
INSERT INTO `behavior_differentiation` VALUES ('39', '52172FBDB9B555CB6FF62F396FD701CA', 'api', '扫码卸货', '装车卸货');
INSERT INTO `behavior_differentiation` VALUES ('40', '39A72E586423719268A1B80DD2365531', 'api', '上传回单', '上传回单');
INSERT INTO `behavior_differentiation` VALUES ('41', 'A32D6A6DCBC22DC3B81C36236CC2F92D', 'api', '历史记录', '发货清单');
INSERT INTO `behavior_differentiation` VALUES ('42', 'BE2485C1EDA24CBAD7ED0C429A6B9441', 'api', '我要发货', '我要发货');
INSERT INTO `behavior_differentiation` VALUES ('43', '65B8D66E124F6388409CAD9D01D0E878', 'api', '发货统计报表', '发货清单');
INSERT INTO `behavior_differentiation` VALUES ('44', '89A469DE54438A9FF3E83B9F56D21FB2', 'api', '收货方查货报表', '收货人查货');
INSERT INTO `behavior_differentiation` VALUES ('45', 'C0CE52B498C93364EE2110F58647F760', 'api', '项目组任务报表', '项目组任务');


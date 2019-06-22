
-- ----------------------------
-- Records of T_SYS_MENU
-- ----------------------------
INSERT INTO `T_SYS_MENU` VALUES ('1', '0', '数据统计', '', '1', 'dataCount');
INSERT INTO `T_SYS_MENU` VALUES ('2', '1', '后台首页', 'admin/business/loadBehaviorTotal', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('3', '1', '用户行为', 'admin/business/queryUserbehavior', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('4', '0', '系统管理', '', '1', 'systemManage');
INSERT INTO `T_SYS_MENU` VALUES ('5', '4', '权限管理', 'admin/permission/search', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('6', '4', '条码管理', 'admin/barcode/search', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('7', '4', '用户管理', 'admin/user/userManagement', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('8', '0', '每日统计', '', '1', 'daily-total');
INSERT INTO `T_SYS_MENU` VALUES ('9', '8', '日统计表', 'backstage/dailyTotal/searchDayCount', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('10', '8', '月统计图', 'backstage/dailyTotal/monthchart', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('11', '0', '发货管理', '', '1', 'trace');
INSERT INTO `T_SYS_MENU` VALUES ('12', '11', '我要发货', 'backstage/trace/send/goods', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('13', '11', '我的任务', 'backstage/trace/search', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('14', '11', '待绑码任务', 'backstage/driver/search', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('15', '0', '运单调度', '', '1', 'conveyance');
INSERT INTO `T_SYS_MENU` VALUES ('16', '15', '运单指派', 'backstage/conveyance/search', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('17', '0', '工作协同', '', '1', 'cooridination');
INSERT INTO `T_SYS_MENU` VALUES ('18', '17', '日常工作', 'backstage/coordination/search', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('19', '17', '异常记录', 'backstage/coordination/get/searchExceptionPage', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('20', '0', '任务监控', '', '1', 'follow-into');
INSERT INTO `T_SYS_MENU` VALUES ('21', '20', '任务监控', 'backstage/trace/follow/into', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('22', '0', '电子回单管理', '', '1', 'receipt');
INSERT INTO `T_SYS_MENU` VALUES ('23', '22', '回单审核', 'backstage/receipt/search', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('24', '22', '待处理回单', 'backstage/receipt/wait/search', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('25', '0', '纸质回单管理', '', '1', 'page-bill');
INSERT INTO `T_SYS_MENU` VALUES ('26', '25', '回单列表', 'backstage/receipt/queryReceiptList', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('27', '25', '回单扫描', 'backstage/receipt/scan/enter', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('28', '25', '统计分析', 'backstage/receipt/analyze', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('29', '0', '常用设置', '', '1', 'resize');
INSERT INTO `T_SYS_MENU` VALUES ('30', '29', '地址管理', 'backstage/customer/search?type=2', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('31', '29', '好友管理', 'backstage/friend/search', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('32', '29', '路由管理', 'backstage/route/list', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('33', '0', '订单管理', '', '1', 'orderManage');
INSERT INTO `T_SYS_MENU` VALUES ('34', '33', '电子签收管理', 'enterprise/order/view/shipper', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('35', '0', '企业管理', '', '0', 'companyManage');
INSERT INTO `T_SYS_MENU` VALUES ('36', '35', '企业管理', 'enterprise/company/view/manage', '0', null);
INSERT INTO `T_SYS_MENU` VALUES ('37', '35', '企业添加', 'enterprise/company/view/add', '0', null);
INSERT INTO `T_SYS_MENU` VALUES ('38', '35', '企业认证', 'enterprise/company/view/legalize', '0', null);
INSERT INTO `T_SYS_MENU` VALUES ('39', '0', '运营后台', '', '0', 'backstageTab');
INSERT INTO `T_SYS_MENU` VALUES ('40', '39', '企业管理', 'enterprise/backstage/view/companymanage', '0', null);
INSERT INTO `T_SYS_MENU` VALUES ('41', '39', '签收管理', 'enterprise/backstage/view/signandmanage', '0', null);
INSERT INTO `T_SYS_MENU` VALUES ('42', '0', '茅台打印', '', '1', 'printTab');
INSERT INTO `T_SYS_MENU` VALUES ('43', '42', '发货单', 'backstage/moutai/view/order', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('44', '42', '收货客户', 'backstage/moutai/view/customer', '1', null);
INSERT INTO `T_SYS_MENU` VALUES ('45', '42', '取单人', 'backstage/moutai/view/person', '1', null);



-- ----------------------------
-- Records of T_SYS_ROLE
-- ----------------------------
INSERT INTO `T_SYS_ROLE` VALUES ('1', '超级管理员', '1', '9999');
INSERT INTO `T_SYS_ROLE` VALUES ('2', '普通角色', '1', '1');
INSERT INTO `T_SYS_ROLE` VALUES ('3', '茅台角色', '1', '101');
INSERT INTO `T_SYS_ROLE` VALUES ('4', '九阳角色', '1', '102');


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


-- ----------------------------
-- Records of T_COMPANY
-- ----------------------------
INSERT INTO `T_COMPANY` VALUES ('1', '杭州九阳豆业有限公司', 'CODE123456', '', '1', '0', null, null, '2749', '2018-04-16 10:12:41', '2018-04-16 10:12:41');
INSERT INTO `T_COMPANY` VALUES ('2', '远成集团有限公司杭州分公司', 'CODE223456', '', '1', '0', null, null, '2748', '2018-04-16 10:12:41', '2018-04-16 10:12:41');
INSERT INTO `T_COMPANY` VALUES ('3', '东莞市阳子豆业有限公司', 'CODE323456', '', '1', '0', null, null, '2752', '2018-04-16 10:12:41', '2018-04-16 10:12:41');


-- ----------------------------
-- Records of T_COMPANY_SEAL
-- ----------------------------
INSERT INTO `T_COMPANY_SEAL` VALUES ('1', '1', 'upload\\sealMoulage\\20180420161116.png', '1', '2586', '2018-04-19 13:27:00', '2018-04-19 13:27:01');
INSERT INTO `T_COMPANY_SEAL` VALUES ('2', '2', 'upload\\sealMoulage\\11111111111.png', '1', '2586', '2018-04-19 13:27:00', '2018-04-19 13:27:01');
INSERT INTO `T_COMPANY_SEAL` VALUES ('3', '3', 'upload\\sealMoulage\\20180420161116.png', '1', '2586', '2018-04-19 13:27:00', '2018-04-19 13:27:01');


INSERT INTO `T_COMPANY_TEMPLATE` (`ID`, `COMPANY_ID`, `CSS_STRING`, `HTML_STRING`, `USER_ID`, `CREATE_TIME`, `UPDATE_TIME`) VALUES ('1', '0', 'LnBkZi1kaXZ7YmFja2dyb3VuZDojZmZmZmZmOyB3aWR0aDo3MjBweDt9Ci50aXRsZS1oNHt0ZXh0LWFsaWduOmNlbnRlcjsgZm9udC1zaXplOjIwcHg7Zm9udC13ZWlnaHQ6Ym9sZDt9CnRhYmxle3dpZHRoOjEwMCU7bWFyZ2luLXRvcDoxMHB4O2JvcmRlci1jb2xsYXBzZTogY29sbGFwc2U7IGJvcmRlci1zcGFjaW5nOiAwO30KdGFibGUgdHIgdGR7dGV4dC1hbGlnbjpjZW50ZXI7IGZvbnQtd2VpZ2h0Om5vcm1hbDsgaGVpZ2h0OjMwcHg7IHBhZGRpbmc6MnB4O30KdGFibGUgdHIgdGh7dGV4dC1hbGlnbjpjZW50ZXI7IGZvbnQtd2VpZ2h0Om5vcm1hbDsgaGVpZ2h0OjMwcHg7IHBhZGRpbmc6MnB4O30KLnRhYmxlLWJvcmRlcntib3JkZXItbGVmdDojMDAwMDAwIHNvbGlkIDFweDtib3JkZXItdG9wOiMwMDAwMDAgc29saWQgMXB4O30KLnRhYmxlLWJvcmRlciB0ciB0aHtib3JkZXItcmlnaHQ6IzAwMDAwMCBzb2xpZCAxcHg7Ym9yZGVyLWJvdHRvbTojMDAwMDAwIHNvbGlkIDFweDsgYmFja2dyb3VuZC1jb2xvcjogI0NDQ0NDQzsgfQoudGFibGUtYm9yZGVyIHRyIHRke2JvcmRlci1yaWdodDojMDAwMDAwIHNvbGlkIDFweDtib3JkZXItYm90dG9tOiMwMDAwMDAgc29saWQgMXB4OyB9Ci50YWJsZS1sZWZ0IHRyIHRke3RleHQtYWxpZ246bGVmdDsgIH0KLnRpcHN7IGZvbnQtc2l6ZTogMTBweDsgY29sb3I6ICMjNEY0RjRGOyB9IAoudGl0bGV7cGFkZGluZy10b3A6IDEwcHg7fSA=', 'PGRpdiBjbGFzcz0icGRmLWRpdiI+CjxkaXYgY2xhc3M9InRpdGxlLWg0Ij7kuqflk4HphY3pgIHljZU8L2Rpdj4KPHRhYmxlIGNsYXNzPSJ0YWJsZS1sZWZ0Ij4KICAgIDx0Ym9keT4KICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZCB3aWR0aD0iMTEwIj7phY3pgIHljZXlj7c6PC90ZD4KICAgICAgICAgICAgPHRkIHdpZHRoPSIzOTAiPiR7ZGVsaXZlcnlOb308L3RkPgogICAgICAgICAgICA8dGQgd2lkdGg9IjExMCI+5pel5pyfOjwvdGQ+CiAgICAgICAgICAgIDx0ZCB3aWR0aD0iMTEwIj4ke2RlbGl2ZXJ5VGltZX08L3RkPgogICAgICAgIDwvdHI+CiAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQ+6K6i5Y2V5a6i5oi3OjwvdGQ+CiAgICAgICAgICAgIDx0ZD4ke3JlY2VpdmV9PC90ZD4KICAgICAgICAgICAgPHRkPuiuouWNlee8luWPtzo8L3RkPgogICAgICAgICAgICA8dGQ+JHtvcmRlck5vfTwvdGQ+CiAgICAgICAgPC90cj4KICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZD7mlLbotKflrqLmiLc6PC90ZD4KICAgICAgICAgICAgPHRkPiR7cmVjZWl2ZX08L3RkPgogICAgICAgICAgICA8dGQ+5pS26LSn5Lq65ZGYOjwvdGQ+CiAgICAgICAgICAgIDx0ZD4ke3JlY2VpdmVyTmFtZX08L3RkPgogICAgICAgIDwvdHI+CiAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQ+54mp5rWB5ZWG5ZCN56ewOjwvdGQ+CiAgICAgICAgICAgIDx0ZD4ke2NvbnZleX08L3RkPgogICAgICAgICAgICA8dGQ+6IGU57O75pa55byPOjwvdGQ+CiAgICAgICAgICAgIDx0ZD4ke3JlY2VpdmVyQ29udGFjdH08L3RkPgogICAgICAgIDwvdHI+CiAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQ+5pS26LSn5Zyw5Z2AOjwvdGQ+CiAgICAgICAgICAgIDx0ZCBjb2xzcGFuPSIzIj4ke3JlY2VpdmVBZGRyZXNzfTwvdGQ+CiAgICAgICAgPC90cj4KICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZD7lpIfms6g6PC90ZD4KICAgICAgICAgICAgPHRkIGNvbHNwYW49IjMiPiR7cmVtYXJrfTwvdGQ+CiAgICAgICAgPC90cj4KICAgIDwvdGJvZHk+CjwvdGFibGU+CjxkaXY+6K6i5Y2V5piO57uG77yaPC9kaXY+CiAgICA8dGFibGUgY2xhc3M9InRhYmxlLWJvcmRlciI+CiAgICAgICAgPHRoZWFkPgogICAgICAgICAgICA8dHI+CiAgICAgICAgICAgICAgICA8dGggd2lkdGg9IjEwMCI+54mp5paZ57yW5Y+3PC90aD4KICAgICAgICAgICAgICAgIDx0aCB3aWR0aD0iMTAwIj7nianmlpnmj4/ov7A8L3RoPgogICAgICAgICAgICAgICAgPHRoIHdpZHRoPSI4MCI+5Lqn5ZOB5Z6L5Y+3PC90aD4KICAgICAgICAgICAgICAgIDx0aCB3aWR0aD0iODAiPuWNleS9jTwvdGg+CiAgICAgICAgICAgICAgICA8dGggd2lkdGg9IjgwIj7mlbDph488L3RoPgogICAgICAgICAgICAgICAgPHRoIHdpZHRoPSI4MCI+566x5pWwPC90aD4KICAgICAgICAgICAgICAgIDx0aCB3aWR0aD0iODAiPuS9k+enrzwvdGg+CiAgICAgICAgICAgICAgICA8dGggd2lkdGg9IjgwIj7ph43ph488L3RoPgogICAgICAgICAgICA8L3RyPgogICAgICAgIDwvdGhlYWQ+CiAgICAgICAgPHRib2R5PgogICAgICAgIDwjbGlzdCBjb21tb2RpdGllcyBhcyBjb21tb2RpdHk+CiAgICAgICAgICAgIDx0cj4KICAgICAgICAgICAgICAgIDx0ZD4ke2NvbW1vZGl0eS5jb21tb2RpdHlOb308L3RkPgogICAgICAgICAgICAgICAgPHRkPiR7Y29tbW9kaXR5LmNvbW1vZGl0eU5hbWV9PC90ZD4KICAgICAgICAgICAgICAgIDx0ZD4ke2NvbW1vZGl0eS5jb21tb2RpdHlUeXBlfTwvdGQ+CiAgICAgICAgICAgICAgICA8dGQ+JHtjb21tb2RpdHkuY29tbW9kaXR5VW5pdH08L3RkPgogICAgICAgICAgICAgICAgPHRkPiR7Y29tbW9kaXR5LnF1YW50aXR5fTwvdGQ+CiAgICAgICAgICAgICAgICA8dGQ+JHtjb21tb2RpdHkuYm94Q291bnR9PC90ZD4KICAgICAgICAgICAgICAgIDx0ZD4ke2NvbW1vZGl0eS52b2x1bWV9PC90ZD4KICAgICAgICAgICAgICAgIDx0ZD4ke2NvbW1vZGl0eS53ZWlnaHR9PC90ZD4KICAgICAgICAgICAgPC90cj4KICAgICAgICA8LyNsaXN0PgogICAgICAgIDwvdGJvZHk+CiAgICAgICAgPHRmb290PgogICAgICAgICAgICA8dHI+CiAgICAgICAgICAgICAgICA8dGQgY29sc3Bhbj0iNCI+5ZCI6K6hPC90ZD4KICAgICAgICAgICAgICAgIDx0ZD4ke3RvdGFsUXVhbnRpdHl9PC90ZD4KICAgICAgICAgICAgICAgIDx0ZD4ke3RvdGFsQm94Q291bnR9PC90ZD4KICAgICAgICAgICAgICAgIDx0ZD4ke3RvdGFsVm9sdW1lfTwvdGQ+CiAgICAgICAgICAgICAgICA8dGQ+JHt0b3RhbFdlaWdodH08L3RkPgogICAgICAgICAgICA8L3RyPgogICAgICAgIDwvdGZvb3Q+CiAgICA8L3RhYmxlPgo8dGFibGU+CiAgICA8dGJvZHk+CiAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQ+PCNpZiBleGNlcHRpb24/Pz7lvILluLg6PHNwYW4+JHtleGNlcHRpb259PC9zcGFuPjwvI2lmPjwvdGQ+CiAgICAgICAgPC90cj4KICAgIDwvdGJvZHk+CjwvdGFibGU+Cjx0YWJsZSBjbGFzcz0idGFibGUtYm9yZGVyIHRhYmxlLWxlZnQiPgogICAgPHRib2R5PgogICAgPHRyPgogICAgICAgIDx0ZCB3aWR0aD0iMzYwIj7nianmtYHllYbnrb7lrZfnm5bnq6A8L3RkPgogICAgICAgIDx0ZCB3aWR0aD0iMzYwIj4ke2NvbnZleX08L3RkPgogICAgPC90cj4KICAgIDx0cj4KICAgICAgICA8dGQgY29sc3Bhbj0iMiI+CiAgICAgICAgICAgIDxwIGNsYXNzPSJ0aXBzIj7muKnppqjmj5DnpLrvvJrnianmtYHphY3pgIHotKPku7vku6Xnrb7mlLbljZXmja7kuLrkuLvopoHkvp3mja7vvIzmlYXor7fmgqjkuKXmoLzmjInnhafmlLbotKfmoIflh4bpqozmlLbnrb7lrZfjgIHnm5bnq6Dnoa7orqTjgILlpoLmnInlvILluLjmg4XlhrXvvIzor7flj4rml7boh7TnlLXkuZ3pmLPnianmtYE8L3A+CiAgICAgICAgICAgIDxwIGNsYXNzPSJ0aXBzIj4KICAgICAgICAgICAgICAgIDxzcGFuPuadreW3nlJEQzowNTcxLTEyMzQ1Njc4PC9zcGFuPgogICAgICAgICAgICAgICAgPHNwYW4+5rWO5Y2XUkRDOjA1MzEtMTIzNDU2Nzg8L3NwYW4+CiAgICAgICAgICAgICAgICA8c3Bhbj7lub/lt55SREM6MDc1Ny0xMjM0NTY3ODwvc3Bhbj4KICAgICAgICAgICAgPC9wPgogICAgICAgIDwvdGQ+CiAgICA8L3RyPgogICAgPHRyPgogICAgICAgIDx0ZD7lrp7mlLbku7bmlbAo5aSn5YaZKTwvdGQ+CiAgICAgICAgPHRkPiR7cmVhbENvdW50fTwvdGQ+CiAgICA8L3RyPgogICAgPHRyPgogICAgICAgIDx0ZD4KICAgICAgICAgICAgPHAgY2xhc3M9InRpcHMiPuaIkeW3suehruiupOatpOmFjemAgeWNleS/oeaBr+S4juWunumZheaOpeaUtui0p+eJqeaVsOmHj+OAgeWei+WPt+OAgemFjemAgeagh+ivhijpmLLnqpzotKfnoIEp5LiA6Ie0PC9wPgogICAgICAgIDwvdGQ+CiAgICAgICAgPHRkPuaUtui0p+WNleS9jeS4muWKoeeroDwvdGQ+CiAgICA8L3RyPgogICAgPC90Ym9keT4KPC90YWJsZT4KPC9kaXY+', '2749', '2018-04-19 15:18:48', '2018-04-19 15:18:48');





-- 测试数据

-- 企业
INSERT INTO `T_COMPANY` (`ID`, `COMPANY_NAME`, `COMPANY_CODE`, `LICENSE_PATH`, `USER_ID`) VALUE (1, '九阳集团', 'CODE123456', '/license/11321321312321.PNG', 2749);
INSERT INTO `T_COMPANY` (`ID`, `COMPANY_NAME`, `COMPANY_CODE`, `LICENSE_PATH`, `USER_ID`) VALUE (2, '远成集团有限公司杭州分公司', 'CODE223456', '/license/21321321312321.PNG', 2748);
INSERT INTO `T_COMPANY` (`ID`, `COMPANY_NAME`, `COMPANY_CODE`, `LICENSE_PATH`, `USER_ID`) VALUE (3, '贵州川浩商贸有限公司', 'CODE323456', '/license/31321321312321.PNG', 2752);
INSERT INTO `T_COMPANY` (`ID`, `COMPANY_NAME`, `COMPANY_CODE`, `LICENSE_PATH`, `USER_ID`) VALUE (4, '东莞市阳子豆业有限公司', 'CODE423456', '/license/41321321312321.PNG', 2734);
INSERT INTO `T_COMPANY` (`ID`, `COMPANY_NAME`, `COMPANY_CODE`, `LICENSE_PATH`, `USER_ID`) VALUE (5, '遵义忆小豆贸易有限公司', 'CODE523456', '/license/51321321312321.PNG', 2738);
INSERT INTO `T_COMPANY` (`ID`, `COMPANY_NAME`, `COMPANY_CODE`, `LICENSE_PATH`, `USER_ID`) VALUE (6, '宜昌市金晓商贸有限公司', 'CODE623456', '/license/61321321312321.PNG', 2744);

-- 企业-员工信息
INSERT INTO `T_COMPANY_EMPLOYEE` (`COMPANY_ID`, `EMPLOYEE_ID`, `USER_ID`) VALUE (1, 2749, 2749);

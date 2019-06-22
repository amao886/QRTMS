
ALTER TABLE `T_USER_LEGALIZE` ADD `FETTLE` INT(11) DEFAULT 0 COMMENT '0:没有认证,1:已经认证';

ALTER TABLE `T_USER_LEGALIZE` MODIFY `ID_CARD_NO` VARCHAR(20) DEFAULT NULL COMMENT '身份证';
ALTER TABLE `T_COMPANY_EMPLOYEE` ADD `EMPLOYEE_TYPE` INT(11) DEFAULT 0 COMMENT '0:普通员工,1:管理';

ALTER TABLE T_COMPANY_EMPLOYEE MODIFY COLUMN AUTHORIZE_FILE VARCHAR(500) DEFAULT NULL COMMENT '授权书路径';

DROP TABLE IF EXISTS `T_COMPANY_SIGNED`;
CREATE TABLE `T_COMPANY_SIGNED` (
  `ID` bigint(20) NOT NULL COMMENT '主键id',
  `COMPANY_ID` bigint(20) NOT NULL COMMENT '企业的编码',
  `SIGN_TOTAL` int(11) NOT NULL COMMENT '签署的总份数',
  `SIGN_USED` int(11) NOT NULL COMMENT '已签署的份数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业管理-签署管理';

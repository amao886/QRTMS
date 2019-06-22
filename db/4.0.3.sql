-- 企业表变更
-- 新增字段
ALTER TABLE `T_COMPANY` ADD `CODE_ORG` VARCHAR(30) DEFAULT NULL COMMENT '组织机构代码,与信用代码二者必填一个';
ALTER TABLE `T_COMPANY` ADD `CODE_USC` VARCHAR(30) DEFAULT NULL COMMENT '社会统一信用代码,与组织机构代码二者必填一个';
ALTER TABLE `T_COMPANY` ADD `LEGAL_NAME` VARCHAR(20) NOT NULL COMMENT '法人姓名';
ALTER TABLE `T_COMPANY` ADD `LEGAL_ID_NO` VARCHAR(20) DEFAULT NULL COMMENT '法人身份证号码';
-- 删除字段
ALTER TABLE `T_COMPANY` DROP COLUMN `COMPANY_CODE`;
ALTER TABLE `T_COMPANY` DROP COLUMN `LICENSE_PATH`;
ALTER TABLE `T_COMPANY` DROP COLUMN `SIGN_USER_ID`;
ALTER TABLE `T_COMPANY` DROP COLUMN `SIGN_TIME`;

-- 企业员工表变更
ALTER TABLE `T_COMPANY_EMPLOYEE` ADD `EMPLOYEE_NAME` VARCHAR(20) NOT NULL COMMENT '员工姓名';

-- 个人认证表变更
ALTER TABLE `T_USER_LEGALIZE` ADD `BRANK_CARD_NO` VARCHAR(20) NOT NULL COMMENT '银行账号';
ALTER TABLE `T_USER_LEGALIZE` CHANGE `EMPLOYEE_NAME` `NAME` VARCHAR(10) NOT NULL COMMENT '真实姓名';


-- 企业对公银行打款校验
DROP TABLE IF EXISTS `T_COMPANY_BANK_VERIFY`;
CREATE TABLE `T_COMPANY_BANK_VERIFY` (
  `ID` BIGINT(20) NOT NULL COMMENT '编号',
  `COMPANY_ID` BIGINT(20) NOT NULL COMMENT '企业编号',
  `NAME` VARCHAR(50) DEFAULT NULL COMMENT '对公账户户名（一般来说即企业名称） ',
  `CARD_NO` VARCHAR(50) NOT NULL COMMENT '企业对公银行账号 ',
  `SUB_BRANCH` VARCHAR(50) NOT NULL COMMENT '企业银行账号开户行支行全称 ',
  `BANK` VARCHAR(50) NOT NULL COMMENT '企业银行账号开户行名称',
  `PROVICE` VARCHAR(30) NOT NULL COMMENT '企业银行账号开户行所在省份',
  `CITY` VARCHAR(30)  NOT NULL COMMENT '企业银行账号开户行所在城市',
  `PRCPTCD` VARCHAR(20) DEFAULT NULL COMMENT '企业用户对公账户所在的开户行的大额行号',
  `CREATE_ID` INT(11) NOT NULL COMMENT '创建人',
  `CREATE_TIME` DATETIME DEFAULT NOW() COMMENT '创建时间',
  `FETTLE` INT(2) DEFAULT 1 COMMENT '状态(1:打款中,2:校验中,3:校验未通过,4:校验通过)',
  `MODIFY_TIME` DATETIME DEFAULT NULL COMMENT '更新时间',
  `CHECK_AMOUNT` DOUBLE DEFAULT NULL COMMENT '校验金额',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 银行数据
DROP TABLE IF EXISTS `T_ESIGN_BRANK`;
CREATE TABLE `T_ESIGN_BRANK` (
  `PROVINCE` VARCHAR(20) NOT NULL COMMENT '省或直辖市',
  `CITY` VARCHAR(20) NOT NULL COMMENT '城市名称',
  `BRANK_TYPE` VARCHAR(100) NOT NULL COMMENT '银行类型',
  `BRANK_NAME` VARCHAR(200) NOT NULL COMMENT '银行名称',
  `BRANK_CODE` VARCHAR(50) DEFAULT NULL COMMENT '联行号（大额行号）',
  `BRANCH_NAME` VARCHAR(200) NOT NULL COMMENT '支行名称',
  PRIMARY KEY (`BRANK_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 操作记录
DROP TABLE IF EXISTS `T_OPERATE_NOTE`;
CREATE TABLE `T_OPERATE_NOTE` (
  `ID` BIGINT(20) NOT NULL COMMENT '日志编号',
  `HOST_TYPE` INT(2) NOT NULL COMMENT '关联类型',
  `HOST_ID` VARCHAR(30) NOT NULL COMMENT '关联主键编号',
  `LOG_TYPE` INT(2) NOT NULL COMMENT '日志类型',
  `LOG_CONTEXT` VARCHAR(200) NOT NULL COMMENT '日志内容',
  `ADJUNCT_KEY` VARCHAR(50) DEFAULT NULL COMMENT '附件编号',
  `CREATE_TIME` DATETIME DEFAULT NOW() COMMENT '记录时间',
  PRIMARY KEY (`ID`),
  INDEX INDEX_HOST_ID (`HOST_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 订单表修改
ALTER TABLE `T_ORDER` ADD `BIND_CODE` VARCHAR(20) DEFAULT NULL COMMENT '绑定的二维码';
ALTER TABLE `T_ORDER` ADD CONSTRAINT UK_BIND_CODE UNIQUE (`BIND_CODE`);
ALTER TABLE `T_ORDER` ADD `LOCATION` VARCHAR(300) DEFAULT NULL COMMENT '最新的定位地址';
-- 订单作废表修改
ALTER TABLE `T_ORDER_INVALID` ADD `BIND_CODE` VARCHAR(20) DEFAULT NULL COMMENT '绑定的二维码';
ALTER TABLE `T_ORDER_INVALID` ADD CONSTRAINT UK_BIND_CODE UNIQUE (`BIND_CODE`);
ALTER TABLE `T_ORDER_INVALID` ADD `LOCATION` VARCHAR(300) DEFAULT NULL COMMENT '最新的定位地址';



-- 位置轨迹
DROP TABLE IF EXISTS `T_LOCATION_TRACK`;
CREATE TABLE `T_LOCATION_TRACK` (
  `ID` BIGINT(20) NOT NULL COMMENT '轨迹主键' ,
  `HOST_TYPE` INT(2) NOT NULL COMMENT '关联类型',
  `HOST_ID` VARCHAR(30) NOT NULL COMMENT '关联主键编号',
  `USER_ID` INT(11) NOT NULL COMMENT '用户（user）主键 轨迹上传人的id' ,
  `LONGITUDE` DOUBLE NOT NULL COMMENT '经度' ,
  `LATITUDE` DOUBLE NOT NULL COMMENT '纬度' ,
  `LOCATION` VARCHAR(300) NOT NULL COMMENT '位置地址' ,
  `CREATE_TIME`  DATETIME DEFAULT NOW() COMMENT '保存时间' ,
  PRIMARY KEY (`ID`),
  INDEX `INDEX_HOST` (`HOST_TYPE`, `HOST_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 企业员工权限
DROP TABLE IF EXISTS `T_EMPLOYEE_AUTHORITY`;
CREATE TABLE `T_EMPLOYEE_AUTHORITY` (
  `ID` BIGINT(20) NOT NULL COMMENT '员工权限主键' ,
  `COMPANY_ID` BIGINT(20) NOT NULL COMMENT '企业编号',
  `EMPLOYEE_ID` INT(11) NOT NULL COMMENT '员工编号(用户ID)',
  `AUTHORITY_ID` BIGINT(20) NOT NULL COMMENT '权限编号',
  `CREATE_TIME`  DATETIME DEFAULT NOW() COMMENT '保存时间' ,
  PRIMARY KEY (`ID`),
  INDEX `INDEX_COMPANY_EMPLOYEE_ID` (`COMPANY_ID`, `EMPLOYEE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 条码
ALTER TABLE `barcode_tab` ADD COLUMN `company_id` BIGINT(20) NULL DEFAULT NULL COMMENT '公司的id';
ALTER TABLE `resourcse_app_tab` ADD COLUMN `company_id` BIGINT(20) NULL DEFAULT NULL COMMENT '公司的id';


--菜单PC类型

ALTER TABLE `T_SYS_MENU` ADD COLUMN `MENU_TYPE` INT(2)  DEFAULT 1  COMMENT '1:电脑端，2:微信端';
UPDATE T_SYS_MENU SET MENU_TYPE = 1;

--权限表用户状态

ALTER TABLE `T_COMPANY_EMPLOYEE` ADD COLUMN `USER_FETTLE` INT(2)  DEFAULT '1'  COMMENT '1:启用，0:禁用';
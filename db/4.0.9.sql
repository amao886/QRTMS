
--客户地址
ALTER TABLE `customer_tab` ADD COLUMN `company_customer_id` BIGINT DEFAULT NULL COMMENT '所属客户的编号' ;
--alter table customer_tab change company_id company_customer_id BIGINT NULL COMMENT '所属公司的编号' ;
--企业客户类型
ALTER TABLE `T_COMPANY_CUSTOMER` ADD COLUMN `TYPE` INT(11) DEFAULT 2 COMMENT '客户类型(1:发货方,2:收货方,3:承运方)' ;

-- 尉静静-上海远成物流发展有限公司
update t_company_employee set COMPANY_ID = 319482955702272 where EMPLOYEE_ID = 2936;
-- 李岩-济南远成物流发展有限公司
update t_company_employee set COMPANY_ID = 251225610330113 where EMPLOYEE_ID = 3009;
-- 陶引万-昆明远成运输有限公司
update t_company_employee set COMPANY_ID = 303457301929984 where EMPLOYEE_ID = 3010;
-- 陈睿-昆明远成运输有限公司
update t_company_employee set COMPANY_ID = 303457301929984 where EMPLOYEE_ID = 3039;


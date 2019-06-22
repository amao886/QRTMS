-- 只导出表结构不导出数据
mysqldump -hrm-bp10pzj6e903ocxb4.mysql.rds.aliyuncs.com -uycg_ksh_mysql -pF5yw4Kh8n4eXUx5dY! -P3306 --routines --default-character-set=utf8 --no-data --databases track > /home/back/db.20180615.nodata.sql
-- 只导出数据不导出表结构
mysqldump -hrm-bp10pzj6e903ocxb4.mysql.rds.aliyuncs.com --ignore-table=track.barcode_tab --ignore-table=track.sys_request_serial -uycg_ksh_mysql -pF5yw4Kh8n4eXUx5dY! -P3306 -t track > /home/back/db.20180604.data.sql


mysqldump -hrm-bp10pzj6e903ocxb4.mysql.rds.aliyuncs.com -uycg_ksh_mysql -pF5yw4Kh8n4eXUx5dY! -P3306 -t track user_tab > /home/user_tab.sql


ALTER  TABLE moutai_convey RENAME TO MOUTAI_CONVEY;
ALTER  TABLE moutai_customer RENAME TO MOUTAI_CUSTOMER;
ALTER  TABLE moutai_order RENAME TO MOUTAI_ORDER;
ALTER  TABLE moutai_taker RENAME TO MOUTAI_TAKER;
ALTER  TABLE t_company RENAME TO T_COMPANY;
ALTER  TABLE t_company_bank_verify RENAME TO T_COMPANY_BANK_VERIFY;
ALTER  TABLE t_company_employee RENAME TO T_COMPANY_EMPLOYEE;
ALTER  TABLE t_company_seal RENAME TO T_COMPANY_SEAL;
ALTER  TABLE t_company_signed RENAME TO T_COMPANY_SIGNED;
ALTER  TABLE t_company_template RENAME TO T_COMPANY_TEMPLATE;
ALTER  TABLE t_employee_authority RENAME TO T_EMPLOYEE_AUTHORITY;
ALTER  TABLE t_esign_brank RENAME TO T_ESIGN_BRANK;
ALTER  TABLE t_location_track RENAME TO T_LOCATION_TRACK;
ALTER  TABLE t_message RENAME TO T_MESSAGE;
ALTER  TABLE t_operate_note RENAME TO T_OPERATE_NOTE;
ALTER  TABLE t_order RENAME TO T_ORDER;
ALTER  TABLE t_order_commodity RENAME TO T_ORDER_COMMODITY;
ALTER  TABLE t_order_exception RENAME TO T_ORDER_EXCEPTION;
ALTER  TABLE t_order_invalid RENAME TO T_ORDER_INVALID;
ALTER  TABLE t_order_receipt RENAME TO T_ORDER_RECEIPT;
ALTER  TABLE t_order_signature RENAME TO T_ORDER_SIGNATURE;
ALTER  TABLE t_paper_receipt RENAME TO T_PAPER_RECEIPT;
ALTER  TABLE t_sign_associate RENAME TO T_SIGN_ASSOCIATE;
ALTER  TABLE t_sign_record RENAME TO T_SIGN_RECORD;
ALTER  TABLE t_signed_relation RENAME TO T_SIGNED_RELATION;
ALTER  TABLE t_sys_menu RENAME TO T_SYS_MENU;
ALTER  TABLE t_sys_role RENAME TO T_SYS_ROLE;
ALTER  TABLE t_sys_role_menu RENAME TO T_SYS_ROLE_MENU;
ALTER  TABLE t_user_legalize RENAME TO T_USER_LEGALIZE;



alter  table MOUTAI_CONVEY rename to moutai_convey;
alter  table MOUTAI_CUSTOMER rename to moutai_customer;
alter  table MOUTAI_ORDER rename to moutai_order;
alter  table MOUTAI_TAKER rename to moutai_taker;
alter  table T_COMPANY rename to t_company;
alter  table T_COMPANY_BANK_VERIFY rename to t_company_bank_verify;
alter  table T_COMPANY_EMPLOYEE rename to t_company_employee;
alter  table T_COMPANY_SEAL rename to t_company_seal;
alter  table T_COMPANY_SIGNED rename to t_company_signed;
alter  table T_COMPANY_TEMPLATE rename to t_company_template;
alter  table T_EMPLOYEE_AUTHORITY rename to t_employee_authority;
alter  table T_ESIGN_BRANK rename to t_esign_brank;
alter  table T_LOCATION_TRACK rename to t_location_track;
alter  table T_MESSAGE rename to t_message;
alter  table T_OPERATE_NOTE rename to t_operate_note;
alter  table T_ORDER rename to t_order;
alter  table T_ORDER_COMMODITY rename to t_order_commodity;
alter  table T_ORDER_EXCEPTION rename to t_order_exception;
alter  table T_ORDER_INVALID rename to t_order_invalid;
alter  table T_ORDER_RECEIPT rename to t_order_receipt;
alter  table T_ORDER_SIGNATURE rename to t_order_signature;
alter  table T_PAPER_RECEIPT rename to t_paper_receipt;
alter  table T_SIGN_ASSOCIATE rename to t_sign_associate;
alter  table T_SYS_MENU rename to t_sys_menu;
alter  table T_SYS_ROLE rename to t_sys_role;
alter  table T_SYS_ROLE_MENU rename to t_sys_role_menu;
alter  table T_USER_LEGALIZE rename to t_user_legalize;


alter  table T_ADVENTIVE rename to t_adventive;
alter  table T_ADVENTIVE_NOTE rename to t_adventive_note;
alter  table T_ADVENTIVE_PUSH rename to t_adventive_push;
alter  table T_ANTI_SORT_GOODS rename to t_anti_sort_goods;
alter  table T_AREA rename to t_area;
alter  table T_COMPANY_CONFIG rename to t_company_config;
alter  table T_COMPANY_CUSTOMER rename to t_company_customer;
alter  table T_CUSTOM_DATA rename to t_custom_data;
alter  table T_EMPLOYEE_CUSTOMER rename to t_employee_customer;
alter  table T_EMPLOYEE_SEAL rename to t_employee_seal;
alter  table T_FINANCE rename to t_finance;
alter  table T_IMPORT_TEMPLATE rename to t_import_template;
alter  table T_INBOUND_ORDER rename to t_inbound_order;
alter  table T_LOTTERY_NOTE rename to t_lottery_note;
alter  table T_MONEY_EXTRACT rename to t_money_extract;
alter  table T_ORDER_CONVEY rename to t_order_convey;
alter  table T_ORDER_DELIVER rename to t_order_deliver;
alter  table T_ORDER_EXTRA rename to t_order_extra;
alter  table T_ORDER_SHARE rename to t_order_share;
alter  table T_OUTBOUND_DETAILS rename to t_outbound_details;
alter  table T_OUTBOUND_ORDER rename to t_outbound_order;
alter  table T_PERSONAL_SEAL rename to t_personal_seal;
alter  table T_PLAN_COMMODITY rename to t_plan_commodity;
alter  table T_PLAN_DESIGNATE rename to t_plan_designate;
alter  table T_PLAN_ORDER rename to t_plan_order;
alter  table T_RESOURCE_CHANGE rename to t_resource_change;
alter  table T_TEMPLATE_DETAIL rename to t_template_detail;
alter  table T_TEMPLATE_PROPERTY rename to t_template_property;
alter  table T_USER_RESOURCE rename to t_user_resource;
alter  table T_VEHICLE rename to t_vehicle;
alter  table T_VEHICLE_DESIGNATE rename to t_vehicle_designate;


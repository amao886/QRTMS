
ALTER TABLE customer_tab SET  ADD fullAddress VARCHAR(350) DEFAULT NULL COMMENT '省+市+区+详细地址';
UPDATE customer_tab SET fullAddress = CONCAT_WS('', province, city, district, address) WHERE fullAddress IS NULL;


-- v1.6
-- 2017-10-23  
ALTER TABLE address_tab ADD verify_status INT(11) DEFAULT NULL COMMENT '审核状态;1:合格,0:不合格';
ALTER TABLE address_tab ADD verify_remark VARCHAR(200) DEFAULT NULL COMMENT '审核备注,不合格原因';
ALTER TABLE address_tab ADD verify_uid INT(11) DEFAULT NULL COMMENT '审核人ID';
ALTER TABLE address_tab ADD verify_date datetime DEFAULT NULL COMMENT '审核时间';



-- 2017-10-23
-- 回单数量和已审核数量
CREATE OR REPLACE VIEW waybill_receipt_verify_view AS
SELECT
  COUNT(*) AS verify_total,
  SUM( IF ( IFNULL(address.verify_status, -1) >= 0, 1, 0 ) ) AS verify_count,
  MIN(receipt.createtime) AS updatetime, 
  MAX(address.verify_date) AS verify_date,
  receipt.waybillid
FROM
	receipt_tab receipt
LEFT JOIN address_tab address ON address.receiptid = receipt.id
GROUP BY receipt.waybillid;


-- 任务单视图
CREATE OR REPLACE VIEW waybill_comprehensive_view AS
SELECT
	bar.id AS barcode_id,
	bar.barcode AS barcode_barcode,
	bar.bindstatus AS barcode_bindstatus,
	waybill.id AS waybill_id,
	waybill.delivery_number AS waybill_delivery_number,
	waybill.order_summary AS waybill_order_summary,
	waybill.weight AS waybill_weight,
	waybill.volume AS waybill_volume,
	waybill.number AS waybill_number,
	waybill.remark AS waybill_remark,
	waybill.address AS waybill_address,
	waybill.arrivaltime AS waybill_arrivaltime,
	waybill.actual_arrival_time AS waybill_actual_arrival_time,
	waybill.createtime AS waybill_createtime,
	customer.id AS customer_id,
	customer.company_name AS customer_company_name,
	customer.contacts AS customer_contacts,
	customer.contact_number AS customer_contact_number,
	customer.tel AS customer_tel,
	customer.province AS customer_province,
	customer.city AS customer_city,
	customer.district AS customer_district,
	customer.address AS customer_address,
    CONCAT_WS('', customer.province, customer.city, customer.district, customer.address) AS customer_full_address,
	customer.longitude AS customer_longitude,
	customer.latitude AS customer_latitude,
	gp.id AS group_id,
	gp.group_name,
	IF ( wrvv.verify_total = wrvv.verify_count, 1, 0 ) AS receipt_status,	
	wrvv.verify_total AS receipt_verify_total, 
	wrvv.verify_count AS receipt_verify_count,
  wrvv.verify_date AS receipt_verify_date,
  wrvv.updatetime AS receipt_upload_time
FROM
	waybill_tab waybill
LEFT JOIN barcode_tab bar ON waybill.barcodeid = bar.id
LEFT JOIN customer_tab customer ON waybill.customerid = customer.id
LEFT JOIN group_tab gp ON waybill.groupid = gp.id
LEFT JOIN waybill_receipt_verify_view wrvv ON waybill.id = wrvv.waybillid;
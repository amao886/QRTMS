package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`waybill_receipt_status`")
public class WaybillReceiptStatus extends BaseEntity {

	private static final long serialVersionUID = 1638399205185500256L;

	@Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 运单主键
     */
    @Column(name = "`waybill_id`")
    private Integer waybillId;

    /**
     * 回单状态(1:已回收,2:已送客户,3:已退供应商,4:客户退回)
     */
    @Column(name = "`receipt_status`")
    private Integer receiptStatus;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    private Date modifyTime;

    /**
     * 操作人编号
     */
    @Column(name = "`user_id`")
    private Integer userId;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取运单主键
     *
     * @return waybill_id - 运单主键
     */
    public Integer getWaybillId() {
        return waybillId;
    }

    /**
     * 设置运单主键
     *
     * @param waybillId 运单主键
     */
    public void setWaybillId(Integer waybillId) {
        this.waybillId = waybillId;
    }

    /**
     * 获取回单状态(1:已回收,2:已送客户,3:已退供应商,4:客户退回)
     *
     * @return receipt_status - 回单状态(1:已回收,2:已送客户,3:已退供应商,4:客户退回)
     */
    public Integer getReceiptStatus() {
        return receiptStatus;
    }

    /**
     * 设置回单状态(1:已回收,2:已送客户,3:已退供应商,4:客户退回)
     *
     * @param receiptStatus 回单状态(1:已回收,2:已送客户,3:已退供应商,4:客户退回)
     */
    public void setReceiptStatus(Integer receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取操作人编号
     *
     * @return user_id - 操作人编号
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置操作人编号
     *
     * @param userId 操作人编号
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
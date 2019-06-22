package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`receipt_scan_batch`")
public class ReceiptScanBatch extends BaseEntity {

	private static final long serialVersionUID = -8492967152443771160L;

	@Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 批次号
     */
    @Column(name = "`batch_number`")
    private String batchNumber;

    /**
     * 运单ID
     */
    @Column(name = "`waybill_id`")
    private Integer waybillId;

    /**
     * 创建时间
     */
    @Column(name = "`createTime`")
    private Date createtime;

    /**
     * 用户ID
     */
    @Column(name = "`user_id`")
    private Integer userId;

    /**
     * 项目组ID
     */
    @Column(name = "`group_id`")
    private Integer groupId;

    /**
     * 操作状态
     */
    private Integer receiptStatus;

    public Integer getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(Integer receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

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
     * 获取批次号
     *
     * @return batch_number - 批次号
     */
    public String getBatchNumber() {
        return batchNumber;
    }

    /**
     * 设置批次号
     *
     * @param batchNumber 批次号
     */
    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    /**
     * 获取运单ID
     *
     * @return waybill_id - 运单ID
     */
    public Integer getWaybillId() {
        return waybillId;
    }

    /**
     * 设置运单ID
     *
     * @param waybillId 运单ID
     */
    public void setWaybillId(Integer waybillId) {
        this.waybillId = waybillId;
    }

    /**
     * 获取创建时间
     *
     * @return createTime - 创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置创建时间
     *
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取项目组ID
     *
     * @return group_id - 项目组ID
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * 设置项目组ID
     *
     * @param groupId 项目组ID
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
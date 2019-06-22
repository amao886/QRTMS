package com.ycg.ksh.entity.persistent.depot;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "`T_OUTBOUND_DETAILS`")
public class OutboundDetail extends BaseEntity {

    @Id
    @Column(name = "`KEY`")
    private Long key;

    /**
     * 批次号
     */
    @Column(name = "`BATCH_NUMBER`")
    private String batchNumber;

    /**
     * 品名
     */
    @Column(name = "`MATERIAL_NAME`")
    private String materialName;

    /**
     * 出库数量
     */
    @Column(name = "`OUTBOUND_QUANTITY`")
    private Integer outboundQuantity;

    /**
     * 出库ID
     */
    @Column(name = "`OUTBOUND_ID`")
    private Long outboundId;

    /**
     * @return KEY
     */
    public Long getKey() {
        return key;
    }

    /**
     * @param key
     */
    public void setKey(Long key) {
        this.key = key;
    }

    /**
     * 获取批次号
     *
     * @return BATCH_NUMBER - 批次号
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
     * 获取品名
     *
     * @return MATERIAL_NAME - 品名
     */
    public String getMaterialName() {
        return materialName;
    }

    /**
     * 设置品名
     *
     * @param materialName 品名
     */
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    /**
     * 获取出库数量
     *
     * @return OUTBOUND_QUANTITY - 出库数量
     */
    public Integer getOutboundQuantity() {
        return outboundQuantity;
    }

    /**
     * 设置出库数量
     *
     * @param outboundQuantity 出库数量
     */
    public void setOutboundQuantity(Integer outboundQuantity) {
        this.outboundQuantity = outboundQuantity;
    }

    /**
     * 获取出库ID
     *
     * @return OUTBOUND_ID - 出库ID
     */
    public Long getOutboundId() {
        return outboundId;
    }

    /**
     * 设置出库ID
     *
     * @param outboundId 出库ID
     */
    public void setOutboundId(Long outboundId) {
        this.outboundId = outboundId;
    }
}
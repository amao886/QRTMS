package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`T_ORDER_RECEIPT`")
public class OrderReceipt extends BaseEntity {
    /**
     * 订单编号
     */
    @Id
    @Column(name = "`ORDER_NO`")
    private Long orderNo;

    /**
     * 回单状态(0:未生成电子回单;1:已生成电子回单;2:收货方已签;3:承运商已签;4:双方都已签)
     */
    @Column(name = "`FETTLE`")
    private Integer fettle;

    /**
     * 电子回单存储路径
     */
    @Column(name = "`PDF_PATH`")
    private String pdfPath;

    /**
     * 创建人
     */
    @Column(name = "`USER_ID`")
    private Integer userId;

    /**
     * 创建时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "`UPDATE_TIME`")
    private Date updateTime;

    public OrderReceipt() {
    }

    public OrderReceipt(Long orderNo, String pdfPath, Date updateTime) {
        this.orderNo = orderNo;
        this.pdfPath = pdfPath;
        this.updateTime = updateTime;
    }

    public OrderReceipt(Long orderNo, Integer fettle, Date updateTime) {
        this.orderNo = orderNo;
        this.fettle = fettle;
        this.updateTime = updateTime;
    }

    /**
     * 获取订单编号
     *
     * @return ORDER_NO - 订单编号
     */
    public Long getOrderNo() {
        return orderNo;
    }

    /**
     * 设置订单编号
     *
     * @param orderNo 订单编号
     */
    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取电子回单存储路径
     *
     * @return PDF_PATH - 电子回单存储路径
     */
    public String getPdfPath() {
        return pdfPath;
    }

    /**
     * 设置电子回单存储路径
     *
     * @param pdfPath 电子回单存储路径
     */
    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    /**
     * 获取创建人
     *
     * @return USER_ID - 创建人
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置创建人
     *
     * @param userId 创建人
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取创建时间
     *
     * @return CREATE_TIME - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return UPDATE_TIME - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getFettle() {
        return fettle;
    }

    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }
}
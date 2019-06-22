package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 运单
 */
@Table(name = "`conveyance_tab`")
public class Conveyance extends BaseEntity {

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 父运单编号
     */
    @Column(name = "`parent_key`")
    private Long parentKey;

    /**
     * 任务单ID,关联运单表
     */
    @Column(name = "`waybill_key`")
    private Integer waybillKey;
    /**
     * 任务单号(条码号),关联运单表
     */
    @Column(name = "`barcode`")
    private String barcode;
    /**
     * 送货单号,关联运单表
     */
    @Column(name = "`delivery_number`")
    private String deliveryNumber;

    /**
     * 运单号,用户输入,系统不生成
     */
    @Column(name = "`conveyance_number`")
    private String conveyanceNumber;

    /**
     * 创建人用户ID
     */
    @Column(name = "`owner_key`")
    private Integer ownerKey;

    /**
     * 创建者分享到的项目组ID
     */
    @Column(name = "`owner_group_key`")
    private Integer ownerGroupKey;

    /**
     * 生成时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 指派状态(0:初始状态,1:创建指派,2:承运人指派)
     */
    @Column(name = "`assign_fettle`")
    private Integer assignFettle;

    /**
     * 指派状态变更时间
     */
    @Column(name = "`assign_fettle_time`")
    private Date assignFettleTime;

    /**
     * 发站
     */
    @Column(name = "`start_station`")
    private String startStation;

    /**
     * 发站简称
     */
    @Column(name = "`simple_start_station`")
    private String simpleStartStation;

    /**
     * 到站
     */
    @Column(name = "`end_station`")
    private String endStation;

    /**
     * 到站简称
     */
    @Column(name = "`simple_end_station`")
    private String simpleEndStation;

    /**
     * 运单状态(20:待运输,30:运输中,35:送达)
     */
    @Column(name = "`conveyance_fettle`")
    private Integer conveyanceFettle;

    /**
     * 运单状态变更时间
     */
    @Column(name = "`conveyance_fettle_time`")
    private Date conveyanceFettleTime;

    /**
     * 是否有子节点
     */
    @Column(name = "`have_child`")
    private Boolean haveChild;

    /**
     * 下一个节点ID
     */
    @Column(name = "`next_key`")
    private Long nextKey;

    /**
     * 节点层级
     */
    @Column(name = "`node_level`")
    private Integer nodeLevel;

    /**
     * 联系人名称
     */
    @Column(name = "`contact_name`")
    private String contactName;

    /**
     * 联系人电话
     */
    @Column(name = "`contact_phone`")
    private String contactPhone;

    /**
     * 联系人所属组织
     */
    @Column(name = "`organization`")
    private String organization;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取父运单编号
     *
     * @return parent_key - 父运单编号
     */
    public Long getParentKey() {
        return parentKey;
    }

    /**
     * 设置父运单编号
     *
     * @param parentKey 父运单编号
     */
    public void setParentKey(Long parentKey) {
        this.parentKey = parentKey;
    }

    /**
     * 获取任务单号(条码号),关联运单表
     *
     * @return barcode - 任务单号(条码号),关联运单表
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * 设置任务单号(条码号),关联运单表
     *
     * @param barcode 任务单号(条码号),关联运单表
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * 获取运单号,用户输入,系统不生成
     *
     * @return conveyance_number - 运单号,用户输入,系统不生成
     */
    public String getConveyanceNumber() {
        return conveyanceNumber;
    }

    /**
     * 设置运单号,用户输入,系统不生成
     *
     * @param conveyanceNumber 运单号,用户输入,系统不生成
     */
    public void setConveyanceNumber(String conveyanceNumber) {
        this.conveyanceNumber = conveyanceNumber;
    }

    /**
     * 获取创建人用户ID
     *
     * @return owner_key - 创建人用户ID
     */
    public Integer getOwnerKey() {
        return ownerKey;
    }

    /**
     * 设置创建人用户ID
     *
     * @param ownerKey 创建人用户ID
     */
    public void setOwnerKey(Integer ownerKey) {
        this.ownerKey = ownerKey;
    }

    /**
     * 获取创建者分享到的项目组ID
     *
     * @return owner_group_key - 创建者分享到的项目组ID
     */
    public Integer getOwnerGroupKey() {
        return ownerGroupKey;
    }

    /**
     * 设置创建者分享到的项目组ID
     *
     * @param ownerGroupKey 创建者分享到的项目组ID
     */
    public void setOwnerGroupKey(Integer ownerGroupKey) {
        this.ownerGroupKey = ownerGroupKey;
    }

    /**
     * 获取生成时间
     *
     * @return create_time - 生成时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置生成时间
     *
     * @param createTime 生成时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取指派状态(0:初始状态,1:创建指派,2:承运人指派)
     *
     * @return assign_fettle - 指派状态(0:初始状态,1:创建指派,2:承运人指派)
     */
    public Integer getAssignFettle() {
        return assignFettle;
    }

    /**
     * 设置指派状态(0:初始状态,1:创建指派,2:承运人指派)
     *
     * @param assignFettle 指派状态(0:初始状态,1:创建指派,2:承运人指派)
     */
    public void setAssignFettle(Integer assignFettle) {
        this.assignFettle = assignFettle;
    }

    /**
     * 获取指派状态变更时间
     *
     * @return assign_fettle_time - 指派状态变更时间
     */
    public Date getAssignFettleTime() {
        return assignFettleTime;
    }

    /**
     * 设置指派状态变更时间
     *
     * @param assignFettleTime 指派状态变更时间
     */
    public void setAssignFettleTime(Date assignFettleTime) {
        this.assignFettleTime = assignFettleTime;
    }

    /**
     * 获取发站
     *
     * @return start_station - 发站
     */
    public String getStartStation() {
        return startStation;
    }

    /**
     * 设置发站
     *
     * @param startStation 发站
     */
    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    /**
     * 获取发站简称
     *
     * @return simple_start_station - 发站简称
     */
    public String getSimpleStartStation() {
        return simpleStartStation;
    }

    /**
     * 设置发站简称
     *
     * @param simpleStartStation 发站简称
     */
    public void setSimpleStartStation(String simpleStartStation) {
        this.simpleStartStation = simpleStartStation;
    }

    /**
     * 获取到站
     *
     * @return end_station - 到站
     */
    public String getEndStation() {
        return endStation;
    }

    /**
     * 设置到站
     *
     * @param endStation 到站
     */
    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    /**
     * 获取到站简称
     *
     * @return simple_end_station - 到站简称
     */
    public String getSimpleEndStation() {
        return simpleEndStation;
    }

    /**
     * 设置到站简称
     *
     * @param simpleEndStation 到站简称
     */
    public void setSimpleEndStation(String simpleEndStation) {
        this.simpleEndStation = simpleEndStation;
    }

    /**
     * 获取运单状态(20:待运输,30:运输中,35:送达)
     *
     * @return conveyance_fettle - 运单状态(20:待运输,30:运输中,35:送达)
     */
    public Integer getConveyanceFettle() {
        return conveyanceFettle;
    }

    /**
     * 设置运单状态(20:待运输,30:运输中,35:送达)
     *
     * @param conveyanceFettle 运单状态(20:待运输,30:运输中,35:送达)
     */
    public void setConveyanceFettle(Integer conveyanceFettle) {
        this.conveyanceFettle = conveyanceFettle;
    }

    /**
     * 获取运单状态变更时间
     *
     * @return conveyance_fettle_time - 运单状态变更时间
     */
    public Date getConveyanceFettleTime() {
        return conveyanceFettleTime;
    }

    /**
     * 设置运单状态变更时间
     *
     * @param conveyanceFettleTime 运单状态变更时间
     */
    public void setConveyanceFettleTime(Date conveyanceFettleTime) {
        this.conveyanceFettleTime = conveyanceFettleTime;
    }

    /**
     * 获取是否有子节点
     *
     * @return have_child - 是否有子节点
     */
    public Boolean getHaveChild() {
        return haveChild;
    }

    /**
     * 设置是否有子节点
     *
     * @param haveChild 是否有子节点
     */
    public void setHaveChild(Boolean haveChild) {
        this.haveChild = haveChild;
    }

    /**
     * 获取下一个节点ID
     *
     * @return next_key - 下一个节点ID
     */
    public Long getNextKey() {
        return nextKey;
    }

    /**
     * 设置下一个节点ID
     *
     * @param nextKey 下一个节点ID
     */
    public void setNextKey(Long nextKey) {
        this.nextKey = nextKey;
    }

    /**
     * 获取节点层级
     *
     * @return node_level - 节点层级
     */
    public Integer getNodeLevel() {
        return nodeLevel;
    }

    /**
     * 设置节点层级
     *
     * @param nodeLevel 节点层级
     */
    public void setNodeLevel(Integer nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    /**
     * 获取联系人名称
     *
     * @return contact_name - 联系人名称
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * 设置联系人名称
     *
     * @param contactName 联系人名称
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * 获取联系人电话
     *
     * @return contact_phone - 联系人电话
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * 设置联系人电话
     *
     * @param contactPhone 联系人电话
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    /**
     * 获取联系人所属组织
     *
     * @return organization - 联系人所属组织
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * 设置联系人所属组织
     *
     * @param organization 联系人所属组织
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Integer getWaybillKey() {
        return waybillKey;
    }

    public void setWaybillKey(Integer waybillKey) {
        this.waybillKey = waybillKey;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }
}
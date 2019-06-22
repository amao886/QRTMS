package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;
/**
 * 
 * TODO 货物明细表
 * <p>
 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-12 14:47:10
 */
@Table(name = "`goods`")
public class Goods extends BaseEntity{
    
	private static final long serialVersionUID = 8864620172158200821L;

	@Id
	@Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 任务单id
     */
    @Column(name = "`waybillid`")
    private Integer waybillid;

    /**
     * 物料名称
     */
    @Column(name = "`goods_name`")
    private String goodsName;

    /**
     * 客户料号
     */
    @Column(name = "`goods_type`")
    private String goodsType;

    /**
     * 货物数量
     */
    @Column(name = "`goods_quantity`")
    private Integer goodsQuantity;

    /**
     * 货物重量
     */
    @Column(name = "`goods_weight`")
    private Double goodsWeight;

    /**
     * 货物体积
     */
    @Column(name = "`goods_volume`")
    private Double goodsVolume;

    /**
     * 摘要
     */
    @Column(name = "`summary`")
    private String summary;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "`update_time`")
    private Date updateTime;

    public Goods() {
        super();
    }

    public Goods(Integer waybillid) {
        super();
        this.waybillid = waybillid;
    }

    public Goods(Integer goodsQuantity, Double goodsWeight, Double goodsVolume) {
        this.goodsQuantity = goodsQuantity;
        this.goodsWeight = goodsWeight;
        this.goodsVolume = goodsVolume;
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
     * 获取任务单id
     *
     * @return waybillid - 任务单id
     */
    public Integer getWaybillid() {
        return waybillid;
    }

    /**
     * 设置任务单id
     *
     * @param waybillid 任务单id
     */
    public void setWaybillid(Integer waybillid) {
        this.waybillid = waybillid;
    }

    /**
     * 获取物料名称
     *
     * @return goods_name - 物料名称
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * 设置物料名称
     *
     * @param goodsName 物料名称
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    /**
     * 获取客户料号
     *
     * @return goods_type - 客户料号
     */
    public String getGoodsType() {
        return goodsType;
    }

    /**
     * 设置客户料号
     *
     * @param goodsType 客户料号
     */
    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    /**
     * 获取货物数量
     *
     * @return goods_quantity - 货物数量
     */
    public Integer getGoodsQuantity() {
        return goodsQuantity;
    }

    /**
     * 设置货物数量
     *
     * @param goodsQuantity 货物数量
     */
    public void setGoodsQuantity(Integer goodsQuantity) {
        this.goodsQuantity = goodsQuantity;
    }

    /**
     * 获取货物重量
     *
     * @return goods_weight - 货物重量
     */
    public Double getGoodsWeight() {
        return goodsWeight;
    }

    /**
     * 设置货物重量
     *
     * @param goodsWeight 货物重量
     */
    public void setGoodsWeight(Double goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    /**
     * 获取货物体积
     *
     * @return goods_volume - 货物体积
     */
    public Double getGoodsVolume() {
        return goodsVolume;
    }

    /**
     * 设置货物体积
     *
     * @param goodsVolume 货物体积
     */
    public void setGoodsVolume(Double goodsVolume) {
        this.goodsVolume = goodsVolume;
    }

    /**
     * 获取摘要
     *
     * @return summary - 摘要
     */
    public String getSummary() {
        return summary;
    }

    /**
     * 设置摘要
     *
     * @param summary 摘要
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
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
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
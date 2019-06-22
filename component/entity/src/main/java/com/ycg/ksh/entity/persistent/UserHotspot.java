package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "`user_hotspot_tab`")
public class UserHotspot extends BaseEntity {
    /**
     * 主键
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户主键
     */
    @Column(name = "`user_id`")
    private Integer userId;

    /**
     * 热点类型
     */
    @Column(name = "`associate_type`")
    private Integer associateType;

    /**
     * 热点关联key
     */
    @Column(name = "`associate_key`")
    private String associateKey;

    /**
     * 热点数
     */
    @Column(name = "`hotspot_count`")
    private Long hotspotCount;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户主键
     *
     * @return user_id - 用户主键
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户主键
     *
     * @param userId 用户主键
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取热点类型
     *
     * @return associate_type - 热点类型
     */
    public Integer getAssociateType() {
        return associateType;
    }

    /**
     * 设置热点类型
     *
     * @param associateType 热点类型
     */
    public void setAssociateType(Integer associateType) {
        this.associateType = associateType;
    }

    /**
     * 获取热点关联key
     *
     * @return associate_key - 热点关联key
     */
    public String getAssociateKey() {
        return associateKey;
    }

    /**
     * 设置热点关联key
     *
     * @param associateKey 热点关联key
     */
    public void setAssociateKey(String associateKey) {
        this.associateKey = associateKey;
    }

    /**
     * 获取热点数
     *
     * @return hotspot_count - 热点数
     */
    public Long getHotspotCount() {
        return hotspotCount;
    }

    /**
     * 设置热点数
     *
     * @param hotspotCount 热点数
     */
    public void setHotspotCount(Long hotspotCount) {
        this.hotspotCount = hotspotCount;
    }
}
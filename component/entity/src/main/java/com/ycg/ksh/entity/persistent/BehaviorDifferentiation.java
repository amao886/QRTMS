package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;

/**
 * 用户行为区分
 * <p>
 *
 * @author wangke
 * @date 2018/3/14 9:56
 */
@Table(name = "`behavior_differentiation`")
public class BehaviorDifferentiation extends BaseEntity {


    private static final long serialVersionUID = 6863337198412903826L;

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * url 键
     */
    @Column(name = "`uri_key`")
    private String uriKey;

    /**
     * 功能点注释
     */
    @Column(name = "`function_point`")
    private String functionPoint;

    /**
     * 所属平台
     */
    @Column(name = "`app_type`")
    private String appType;

    /**
     * 所属模块
     */
    @Column(name = "`subordinate_module`")
    private String subordinateModule;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUriKey() {
        return uriKey;
    }

    public void setUriKey(String uriKey) {
        this.uriKey = uriKey;
    }

    public String getFunctionPoint() {
        return functionPoint;
    }

    public void setFunctionPoint(String functionPoint) {
        this.functionPoint = functionPoint;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getSubordinateModule() {
        return subordinateModule;
    }

    public void setSubordinateModule(String subordinateModule) {
        this.subordinateModule = subordinateModule;
    }
}
package com.ycg.ksh.entity.persistent.support;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/30
 */

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 省市区
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/30
 */
@Table(name = "`T_AREA`")
public class Area extends BaseEntity {

    @Id
    @Column(name = "`ID`")
    private Integer id;


    @Column(name = "`CODE`")
    private Integer code;

    @Column(name = "`PID`")
    private Integer parentId;
    @Column(name = "`NAME`")
    private String name;
    @Column(name = "`SUB`")
    private Boolean subordinate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSubordinate() {
        return subordinate;
    }

    public void setSubordinate(Boolean subordinate) {
        this.subordinate = subordinate;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

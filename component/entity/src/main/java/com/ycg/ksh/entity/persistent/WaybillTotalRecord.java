package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 每日统计记录实体
 * <p>
 */
@Table(name = "`waybill_total_record`")
public class WaybillTotalRecord extends BaseEntity {

    private static final long serialVersionUID = -7653845931346297608L;

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 最后一次执行时间
     */
    @Column(name = "`last_hole_time`")
    private Date lastHoleTime;

    /**
     * 生成时间
     */
    @Column(name = "`createTime`")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getLastHoleTime() {
        return lastHoleTime;
    }

    public void setLastHoleTime(Date lastHoleTime) {
        this.lastHoleTime = lastHoleTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
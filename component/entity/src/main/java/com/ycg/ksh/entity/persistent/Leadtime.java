package com.ycg.ksh.entity.persistent;

import javax.persistence.*;

@Table(name = "`leadtime_tab`")
public class Leadtime {
    @Column(name = "`shipcity`")
    private String shipcity;

    @Column(name = "`descity`")
    private String descity;

    @Column(name = "`lt`")
    private Integer lt;

    /**
     * @return shipcity
     */
    public String getShipcity() {
        return shipcity;
    }

    /**
     * @param shipcity
     */
    public void setShipcity(String shipcity) {
        this.shipcity = shipcity;
    }

    /**
     * @return descity
     */
    public String getDescity() {
        return descity;
    }

    /**
     * @param descity
     */
    public void setDescity(String descity) {
        this.descity = descity;
    }

    /**
     * @return lt
     */
    public Integer getLt() {
        return lt;
    }

    /**
     * @param lt
     */
    public void setLt(Integer lt) {
        this.lt = lt;
    }
}
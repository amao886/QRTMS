package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`transition_exception_tab`")
public class TransitionException extends BaseEntity {

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 二维码
     */
    @Column(name = "`barcode`")
    private String barcode;

    /**
     * 上报时间
     */
    @Column(name = "`report_time`")
    private Date reportTime;

    /**
     * 异常内容
     */
    @Column(name = "`content`")
    private String content;

    /**
     * 上报人用户ID
     */
    @Column(name = "`user_id`")
    private Integer userId;

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
     * 获取二维码
     *
     * @return barcode - 二维码
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * 设置二维码
     *
     * @param barcode 二维码
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * 获取上报时间
     *
     * @return report_time - 上报时间
     */
    public Date getReportTime() {
        return reportTime;
    }

    /**
     * 设置上报时间
     *
     * @param reportTime 上报时间
     */
    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    /**
     * 获取异常内容
     *
     * @return content - 异常内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置异常内容
     *
     * @param content 异常内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取上报人用户ID
     *
     * @return user_id - 上报人用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置上报人用户ID
     *
     * @param userId 上报人用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
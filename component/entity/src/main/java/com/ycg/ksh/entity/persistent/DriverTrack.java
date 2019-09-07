package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Table(name = "`driver_track_tab`")
public class DriverTrack extends BaseEntity {

	private static final long serialVersionUID = -8201069399138381276L;

	@Id
	@Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 上报时间
     */
    @Column(name = "`report_time`")
    private Date reportTime;

    /**
     * 上报地址
     */
    @Column(name = "`report_loaction`")
    private String reportLoaction;

    /**
     * 上报人用户ID
     */
    @Column(name = "`user_id`")
    private Integer userId;
    /**
     * 经度
     */
    @Column(name = "`longitude`")
    private String longitude;

    /**
     * 纬度
     */
    @Column(name = "`latitude`")
    private String latitude;
    
    /**
     * 二维码
     */
    @Column(name = "`barcode`")
    private String barcode;
    
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
     * 获取上报地址
     *
     * @return report_loaction - 上报地址
     */
    public String getReportLoaction() {
        return reportLoaction;
    }

    /**
     * 设置上报地址
     *
     * @param reportLoaction 上报地址
     */
    public void setReportLoaction(String reportLoaction) {
        this.reportLoaction = reportLoaction;
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

    /**
     * 获取卸货时间
     *
     * @return longitude - 卸货时间
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 设置卸货时间
     *
     * @param longitude 卸货时间
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * 获取送货单号
     *
     * @return latitude - 经度
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 设置送货单号
     *
     * @param latitude 纬度
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    
    public String getLoaclTime() {
    	if(reportTime != null) {
    		long dtime = System.currentTimeMillis() - reportTime.getTime();
        	long d = TimeUnit.MILLISECONDS.toHours(dtime);
        	if(d < 24) {
        		d = TimeUnit.MILLISECONDS.toHours(dtime);
        		if(d < 1) {
        			d = TimeUnit.MILLISECONDS.toMinutes(dtime);
        			if(d < 1) {
        				return "刚刚定位";
        			}
        			if(d >= 1) {
        				return String.format("%d分钟前定位", d);
        			}
        		}
        		if( d >= 1) {
        			return String.format("%d小时前定位", d);
        		}
        	}else {
        		d = TimeUnit.MILLISECONDS.toDays(dtime);
        		if(d < 30) {
            		return String.format("%d天前定位", d);
            	}
            	if(d >= 30) {
            		return String.format("%d月前定位", d / 30);
            	}
        	}
    	}
    	return "";
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
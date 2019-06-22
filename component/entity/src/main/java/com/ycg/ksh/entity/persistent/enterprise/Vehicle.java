package com.ycg.ksh.entity.persistent.enterprise;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 车辆管理
 *
 * @author: wangke
 * @create: 2018-10-19 09:53
 **/
@Table(name = "`T_VEHICLE`")
public class Vehicle extends BaseEntity {

    /**
     * 编号
     */
    @Id
    @Column(name = "`KEY`")
    private Long key;

    /**
     * 车型
     */
    @Column(name = "`CAR_MODEL`")
    private String carModel;

    /**
     * 车长
     */
    @Column(name = "`CAR_LENGTH`")
    private Double carLength;

    /**
     * 车辆载重
     */
    @Column(name = "`VEHICLE_LOAD`")
    private Double vehicleLoad;

    /**
     * 体积
     */
    @Column(name = "`VOLUME`")
    private Double volume;

    /**
     * 总载重
     */
    @Column(name = "`TOTAL_LOAD`")
    private Double totalLoad;

    /**
     * 总体积
     */
    @Column(name = "`TOTAL_VOLUME`")
    private Double totalVolume;

    /**
     * 创建时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    /**
     * 车牌号
     */
    @Column(name = "`CAR_NO`")
    private String carNo;

    /**
     * 司机姓名
     */
    @Column(name = "`DRIVER_NAME`")
    private String driverName;

    /**
     * 司机联系电话
     */
    @Column(name = "`DRIVER_NUMBER`")
    private String driverNumber;

    /**
     * 派车状态：0未派车 1已派车 2派车确认
     */
    @Column(name = "`CAR_STATUS`")
    private Integer carStatus;

    /**
     * 创建人
     */
    @Column(name = "`CREATE_USER`")
    private Integer createUser;

    /**
     * 发货ID
     */
    @Column(name = "`SHIPPER_ID`")
    private Long shipperId;

    public Long getShipperId() {
        return shipperId;
    }

    public void setShipperId(Long shipperId) {
        this.shipperId = shipperId;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Integer getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(Integer carStatus) {
        this.carStatus = carStatus;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public Double getCarLength() {
        return carLength;
    }

    public void setCarLength(Double carLength) {
        this.carLength = carLength;
    }

    public Double getVehicleLoad() {
        return vehicleLoad;
    }

    public void setVehicleLoad(Double vehicleLoad) {
        this.vehicleLoad = vehicleLoad;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getTotalLoad() {
        return totalLoad;
    }

    public void setTotalLoad(Double totalLoad) {
        this.totalLoad = totalLoad;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }
}

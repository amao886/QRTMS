package com.ycg.ksh.entity.persistent.moutai;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`MOUTAI_TAKER`")
public class Taker extends BaseEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "`ID`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属单位
     */
    @Column(name = "`CONVEY_ID`")
    private Long conveyId;

    /**
     * 取单人名称
     */
    @Column(name = "`TAKE_NAME`")
    private String takeName;

    /**
     * 取单人联系电话
     */
    @Column(name = "`TAKE_PHONE`")
    private String takePhone;

    /**
     * 取单人身份证号码
     */
    @Column(name = "`TAKE_IDCARE`")
    private String takeIdcare;

    /**
     * 状态
     */
    @Column(name = "`FETTLE`")
    private Integer fettle;

    /**
     * 创建人
     */
    @Column(name = "`CREATE_USER_ID`")
    private Integer createUserId;

    /**
     * 创建时间
     */
    @Column(name = "`CREATE_TIME`")
    private Date createTime;

    /**
     * 获取编号
     *
     * @return ID - 编号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置编号
     *
     * @param id 编号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取所属单位
     *
     * @return CONVEY_ID - 所属单位
     */
    public Long getConveyId() {
        return conveyId;
    }

    /**
     * 设置所属单位
     *
     * @param conveyId 所属单位
     */
    public void setConveyId(Long conveyId) {
        this.conveyId = conveyId;
    }

    /**
     * 获取取单人名称
     *
     * @return TAKE_NAME - 取单人名称
     */
    public String getTakeName() {
        return takeName;
    }

    /**
     * 设置取单人名称
     *
     * @param takeName 取单人名称
     */
    public void setTakeName(String takeName) {
        this.takeName = takeName;
    }

    /**
     * 获取取单人联系电话
     *
     * @return TAKE_PHONE - 取单人联系电话
     */
    public String getTakePhone() {
        return takePhone;
    }

    /**
     * 设置取单人联系电话
     *
     * @param takePhone 取单人联系电话
     */
    public void setTakePhone(String takePhone) {
        this.takePhone = takePhone;
    }

    /**
     * 获取取单人身份证号码
     *
     * @return TAKE_IDCARE - 取单人身份证号码
     */
    public String getTakeIdcare() {
        return takeIdcare;
    }

    /**
     * 设置取单人身份证号码
     *
     * @param takeIdcare 取单人身份证号码
     */
    public void setTakeIdcare(String takeIdcare) {
        this.takeIdcare = takeIdcare;
    }

    /**
     * 获取状态
     *
     * @return FETTLE - 状态
     */
    public Integer getFettle() {
        return fettle;
    }

    /**
     * 设置状态
     *
     * @param fettle 状态
     */
    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    /**
     * 获取创建人
     *
     * @return CREATE_USER_ID - 创建人
     */
    public Integer getCreateUserId() {
        return createUserId;
    }

    /**
     * 设置创建人
     *
     * @param createUserId 创建人
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 获取创建时间
     *
     * @return CREATE_TIME - 创建时间
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
}
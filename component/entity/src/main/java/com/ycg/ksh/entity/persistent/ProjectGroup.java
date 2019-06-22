package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.StringUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * 项目组
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-10-12 17:46:04
 */
@Table(name = "`group_tab`")
public class ProjectGroup extends BaseEntity {

	private static final long serialVersionUID = 1513893575254262856L;

	@Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 创建人
     */
    @Column(name = "`userid`")
    private Integer userid;

    /**
     * 组名称
     */
    @Column(name = "`group_name`")
    private String groupName;

    /**
     * 组人数
     */
    @Column(name = "`count`")
    private Integer count;

    /**
     * 组二维码
     */
    @Column(name = "`qr_code`")
    private String qrCode;

    /**
     * 创建时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    /**
     * 修改时间
     */
    @Column(name = "`updatetime`")
    private Date updatetime;

    @Column(name = "`startGoodsTime`")
    private String startgoodstime;

    @Column(name = "`endGoodsTime`")
    private String endgoodstime;

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
     * 获取创建人
     *
     * @return userid - 创建人
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置创建人
     *
     * @param userid 创建人
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取组名称
     *
     * @return group_name - 组名称
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 设置组名称
     *
     * @param groupName 组名称
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 获取组人数
     *
     * @return count - 组人数
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置组人数
     *
     * @param count 组人数
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 获取组二维码
     *
     * @return qr_code - 组二维码
     */
    public String getQrCode() {
        return qrCode;
    }

    /**
     * 设置组二维码
     *
     * @param qrCode 组二维码
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * 获取创建时间
     *
     * @return createtime - 创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置创建时间
     *
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取修改时间
     *
     * @return updatetime - 修改时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 设置修改时间
     *
     * @param updatetime 修改时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * @return startGoodsTime
     */
    public String getStartgoodstime() {
        return startgoodstime;
    }

    /**
     * @param startgoodstime
     */
    public void setStartgoodstime(String startgoodstime) {
        this.startgoodstime = startgoodstime;
    }

    /**
     * @return endGoodsTime
     */
    public String getEndgoodstime() {
        return endgoodstime;
    }

    /**
     * @param endgoodstime
     */
    public void setEndgoodstime(String endgoodstime) {
        this.endgoodstime = endgoodstime;
    }
    
    public Integer getStartHour() {
    	if(StringUtils.isNotBlank(startgoodstime) && startgoodstime.length() >= 2) {
    		return Integer.valueOf(startgoodstime.substring(0, 2));
    	}
    	return 0;
    }
    public Integer getEndHour() {
    	if(StringUtils.isNotBlank(endgoodstime) && endgoodstime.length() >= 2) {
    		return Integer.valueOf(endgoodstime.substring(0, 2));
    	}
    	return 0;
    }
}
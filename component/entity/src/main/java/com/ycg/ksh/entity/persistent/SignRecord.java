package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`T_SIGN_RECORD`")
public class SignRecord extends BaseEntity{
    /**
	 * serialVersionUID : TODO Add description
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-13 14:47:58
	 */
	private static final long serialVersionUID = 8032408901021241262L;

	/**
     * 编号
     */
	@Id
    @Column(name = "`ID`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 签署关系编号
     */
    @Column(name = "`RELATION_ID`")
    private Long relationId;

    /**
     * 创建时间
     */
    @Column(name = "`CREATETIME`")
    private Date createtime;

    /**
     * 签收开始时间
     */
    @Column(name = "`STARTTIME`")
    private Date starttime;

    /**
     * 签收结束时间
     */
    @Column(name = "`ENDTIME`")
    private Date endtime;

    /**
     * 累计签收份数
     */
    @Column(name = "`SIGEN_COUNT`")
    private Integer sigenCount;

    /**
     * 修改人
     */
    @Column(name = "`MODIFY_ID`")
    private Integer modifyId;

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
     * 获取签署关系编号
     *
     * @return RELATION_ID - 签署关系编号
     */
    public Long getRelationId() {
        return relationId;
    }

    /**
     * 设置签署关系编号
     *
     * @param relationId 签署关系编号
     */
    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    /**
     * 获取创建时间
     *
     * @return CREATETIME - 创建时间
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
     * 获取签收开始时间
     *
     * @return STARTTIME - 签收开始时间
     */
    public Date getStarttime() {
        return starttime;
    }

    /**
     * 设置签收开始时间
     *
     * @param starttime 签收开始时间
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * 获取签收结束时间
     *
     * @return ENDTIME - 签收结束时间
     */
    public Date getEndtime() {
        return endtime;
    }

    /**
     * 设置签收结束时间
     *
     * @param endtime 签收结束时间
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    /**
     * 获取累计签收份数
     *
     * @return SIGEN_COUNT - 累计签收份数
     */
    public Integer getSigenCount() {
        return sigenCount;
    }

    /**
     * 设置累计签收份数
     *
     * @param sigenCount 累计签收份数
     */
    public void setSigenCount(Integer sigenCount) {
        this.sigenCount = sigenCount;
    }

    /**
     * 获取修改人
     *
     * @return MODIFY_ID - 修改人
     */
    public Integer getModifyId() {
        return modifyId;
    }

    /**
     * 设置修改人
     *
     * @param modifyId 修改人
     */
    public void setModifyId(Integer modifyId) {
        this.modifyId = modifyId;
    }
}
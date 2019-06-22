package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`app_version`")
public class AppVersion extends BaseEntity{
    /**
	 * serialVersionUID : TODO Add description
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-04-20 15:02:33
	 */
	private static final long serialVersionUID = 305126599178437565L;

	/**
     * 主键
     */
	@Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 版本号
     */
    @Column(name = "`version_number`")
    private String versionNumber;


    @Column(name = "`url`")
    private String url;

    /**
     * 版本类型
     */
    @Column(name = "`version_type`")
    private String versionType;

    /**
     * 创建时间
     */
    @Column(name = "`createtime`")
    private Date createtime;

    
    /**
     * 是否强制更新
     */
    @Column(name = "`forced_update`")
    private Boolean forcedUpdate;
    
    /**
     * 升级描述
     */
    @Column(name = "`description_content`")
    private String descriptionContent;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取升级描述
     *
     * @return description_content - 升级描述
     */
    public String getDescriptionContent() {
        return descriptionContent;
    }

    /**
     * 设置升级描述
     *
     * @param descriptionContent 升级描述
     */
    public void setDescriptionContent(String descriptionContent) {
        this.descriptionContent = descriptionContent;
    }
    
    /**
     * 获取升级描述
     *
     * @return version_type - 版本类型
     */
	public String getVersionType() {
		return versionType;
	}
	/**
     * 设置版本类型
     *
     * @param versionType 版本类型
     */
	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	/**
	 * 获取创建时间
	 * @return the createtime
	 */
	public Date getCreatetime() {
		return createtime;
	}

	/**
	 * 设置创建时间
	 * @param createtime the createtime to set
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	/**
	 * 获取版本好
	 * @return the versionNumber
	 */
	public String getVersionNumber() {
		return versionNumber;
	}

	/**
	 * 设置版本号
	 * @param versionNumber the versionNumber to set
	 */
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * 获取强制更新标记
	 * @return the forcedUpdate
	 */
	public Boolean getForcedUpdate() {
		return forcedUpdate;
	}

	/**
	 * 设置强制更新标记
	 * @param forcedUpdate the forcedUpdate to set
	 */
	public void setForcedUpdate(Boolean forcedUpdate) {
		this.forcedUpdate = forcedUpdate;
	}
}
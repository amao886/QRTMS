package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "`image_storage_tab`")
public class ImageStorage extends BaseEntity {

	private static final long serialVersionUID = -7760741513483679424L;

	@Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 图片类型(1:过渡期回单,...)
     */
    @Column(name = "`image_type`")
    private Integer imageType;

    /**
     * 关联
     */
    @Column(name = "`associate_key`")
    private String associateKey;

    /**
     * 图片存储时间
     */
    @Column(name = "`storage_time`")
    private Date storageTime;

    /**
     * 图片存储地址
     */
    @Column(name = "`storage_path`")
    private String storagePath;

    
    public ImageStorage() {
		super();
	}

	public ImageStorage(Integer imageType, Serializable associateKey) {
		super();
		this.imageType = imageType;
		this.associateKey = String.valueOf(associateKey);
	}

    public ImageStorage(Integer imageType, Serializable associateKey,String storagePath) {
        super();
        this.imageType = imageType;
        this.associateKey = String.valueOf(associateKey);
        this.storagePath = storagePath;
        this.setStorageTime(new Date());
    }

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
     * 获取图片类型(1:过渡期回单,...)
     *
     * @return image_type - 图片类型(1:过渡期回单,...)
     */
    public Integer getImageType() {
        return imageType;
    }

    /**
     * 设置图片类型(1:过渡期回单,...)
     *
     * @param imageType 图片类型(1:过渡期回单,...)
     */
    public void setImageType(Integer imageType) {
        this.imageType = imageType;
    }

    /**
     * 获取关联
     *
     * @return associate_key - 关联
     */
    public String getAssociateKey() {
        return associateKey;
    }

    /**
     * 设置关联
     *
     * @param associateKey 关联
     */
    public void setAssociateKey(String associateKey) {
        this.associateKey = associateKey;
    }

    /**
     * 获取图片存储时间
     *
     * @return storage_time - 图片存储时间
     */
    public Date getStorageTime() {
        return storageTime;
    }

    /**
     * 设置图片存储时间
     *
     * @param storageTime 图片存储时间
     */
    public void setStorageTime(Date storageTime) {
        this.storageTime = storageTime;
    }

    /**
     * 获取图片存储地址
     *
     * @return storage_path - 图片存储地址
     */
    public String getStoragePath() {
        return storagePath;
    }

    /**
     * 设置图片存储地址
     *
     * @param storagePath 图片存储地址
     */
    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }
}
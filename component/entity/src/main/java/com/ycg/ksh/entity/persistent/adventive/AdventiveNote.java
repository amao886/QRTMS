package com.ycg.ksh.entity.persistent.adventive;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.constant.ObjectType;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@Table(name = "`T_ADVENTIVE_NOTE`")
public class AdventiveNote extends BaseEntity {

    /**
     * 编号
     */
    @Id
    @Column(name = "`ID`")
    private Long id;
    /**
     * 推送对象
     */
    @Column(name = "`ADVENTIVE_ID`")
    private Long adventiveId;

    /**
     * 记录类型
     */
    @Column(name = "`NOTE_TYPE`")
    private String noteType;

    /**
     * 推送的数据编号
     */
    @Column(name = "`OBJECT_KEY`")
    private String objectKey;

    /**
     * 最后一次推送的时间
     */
    @Column(name = "`LAST_TIME`")
    private Date lastTime;

    /**
     * 备注
     */
    @Column(name = "`REMARK`")
    private String remark;

    /**
     * 状态(0:可用,1:不可用,99:放弃推送)
     */
    @Column(name = "`FETTLE`")
    private Integer fettle;
    /**
     * 处理次数
     */
    @Column(name = "`COUNT`")
    private Integer count;//处理次数


    /**
     * 对方的关联编号
     */
    @Column(name = "`OPPOSITE_KEY`")
    private String oppositeKey;


    public AdventiveNote() {
    }

    public AdventiveNote(Long adventiveId, ObjectType noteType, Serializable objectKey) {
        this.adventiveId = adventiveId;
        this.noteType = noteType.name();
        this.objectKey = String.valueOf(objectKey);
    }

    public AdventiveNote(Long id, Long adventiveId, ObjectType noteType, Serializable objectKey, Serializable oppositeKey) {
        this(adventiveId, noteType, objectKey);
        this.id = id;
        this.oppositeKey = String.valueOf(oppositeKey);
    }

    @Override
    public String toString() {
        return "Note{ accessKey=" + adventiveId + ", type=" + noteType + ", key=" + objectKey + ", noteKey=" + id +" }";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取推送对象
     *
     * @return ADVENTIVE_ID - 推送对象
     */
    public Long getAdventiveId() {
        return adventiveId;
    }

    /**
     * 设置推送对象
     *
     * @param adventiveId 推送对象
     */
    public void setAdventiveId(Long adventiveId) {
        this.adventiveId = adventiveId;
    }

    /**
     * 获取记录类型
     *
     * @return NOTE_TYPE - 记录类型
     */
    public String getNoteType() {
        return noteType;
    }

    /**
     * 设置记录类型
     *
     * @param noteType 记录类型
     */
    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    /**
     * 获取推送的数据编号
     *
     * @return OBJECT_KEY - 推送的数据编号
     */
    public String getObjectKey() {
        return objectKey;
    }

    /**
     * 设置推送的数据编号
     *
     * @param objectKey 推送的数据编号
     */
    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    /**
     * 获取最后一次推送的时间
     *
     * @return LAST_TIME - 最后一次推送的时间
     */
    public Date getLastTime() {
        return lastTime;
    }

    /**
     * 设置最后一次推送的时间
     *
     * @param lastTime 最后一次推送的时间
     */
    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * 获取备注
     *
     * @return REMARK - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取状态(0:可用,1:不可用,99:放弃推送)
     *
     * @return FETTLE - 状态(0:可用,1:不可用,99:放弃推送)
     */
    public Integer getFettle() {
        return fettle;
    }

    /**
     * 设置状态(0:可用,1:不可用,99:放弃推送)
     *
     * @param fettle 状态(0:可用,1:不可用,99:放弃推送)
     */
    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    public Integer getCount() {
        return Optional.ofNullable(count).orElse(0);
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getOppositeKey() {
        return oppositeKey;
    }

    public void setOppositeKey(String oppositeKey) {
        this.oppositeKey = oppositeKey;
    }
}
package com.ycg.ksh.entity.service.plan;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.constant.CoreConstants;

/**
 * 发货计划查询
 *
 * @Auther: wangke
 * @Date: 2018/9/12 16:59
 * @Description:
 */
public class PlanSearch extends BaseEntity {

    private Integer uKey; //用户编号
    private Integer partner;//


    private String likeString; //发货单号，收货客户，运输路线

    private Integer ordinateState; //接单状态
    private Integer subordinateState; //下级接单状态

    private Integer[] recives; //接单状态

    private Boolean generate;//是否生成发货单

    private Long companyId; //公司ID

    private Long lastCompnayKey;//来源

    private Boolean allocate;// 是否分配

    private Integer carStatus;// 派车状态

    public void ordinateState(boolean allocate) {
        if (ordinateState != null && ordinateState >= 0) {
            if (CoreConstants.PLAN_ACCEPT_STATUS_NOT == ordinateState) {//当前未接单
                recives = new Integer[]{ CoreConstants.PLAN_ACCEPT_STATUS_NOT };
            } else if (CoreConstants.PLAN_ACCEPT_STATUS_ALREADY == ordinateState) {
                if (subordinateState == null) {//下级全部状态
                    recives = new Integer[]{ CoreConstants.PLAN_ACCEPT_STATUS_ALREADY, CoreConstants.PLAN_ACCEPT_STATUS_SUB };
                } else if (CoreConstants.PLAN_ACCEPT_STATUS_NOT == subordinateState) {
                    if(allocate){
                        recives = new Integer[]{ CoreConstants.PLAN_ACCEPT_STATUS_NOT, CoreConstants.PLAN_ACCEPT_STATUS_ALREADY };//下级未接单
                    }else{
                        recives = new Integer[]{ CoreConstants.PLAN_ACCEPT_STATUS_NOT };//下级已接单
                    }
                } else {
                    if(allocate){
                        recives = new Integer[]{ CoreConstants.PLAN_ACCEPT_STATUS_SUB };//下级已接单
                    }else{
                        recives = new Integer[]{ CoreConstants.PLAN_ACCEPT_STATUS_ALREADY, CoreConstants.PLAN_ACCEPT_STATUS_SUB };//下级已接单
                    }
                }
            }
        } else if (subordinateState != null) {
            if (CoreConstants.PLAN_ACCEPT_STATUS_NOT == subordinateState) {
                if(allocate){
                    recives = new Integer[]{ CoreConstants.PLAN_ACCEPT_STATUS_NOT, CoreConstants.PLAN_ACCEPT_STATUS_ALREADY };//下级未接单
                }else{
                    recives = new Integer[]{ CoreConstants.PLAN_ACCEPT_STATUS_NOT };//下级已接单
                }
            } else {
                if(allocate){
                    recives = new Integer[]{ CoreConstants.PLAN_ACCEPT_STATUS_SUB };//下级已接单
                }else{
                    recives = new Integer[]{ CoreConstants.PLAN_ACCEPT_STATUS_ALREADY, CoreConstants.PLAN_ACCEPT_STATUS_SUB };//下级已接单
                }
            }
        }
    }

    public Integer getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(Integer carStatus) {
        this.carStatus = carStatus;
    }

    public Integer getPartner() {
        return partner;
    }

    public void setPartner(Integer partner) {
        this.partner = partner;
    }

    public Boolean getAllocate() {
        return allocate;
    }

    public void setAllocate(Boolean allocate) {
        this.allocate = allocate;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getuKey() {
        return uKey;
    }

    public void setuKey(Integer uKey) {
        this.uKey = uKey;
    }

    public String getLikeString() {
        return likeString;
    }

    public void setLikeString(String likeString) {
        this.likeString = likeString;
    }

    public Integer[] getRecives() {
        return recives;
    }

    public void setRecives(Integer[] recives) {
        this.recives = recives;
    }

    public Boolean getGenerate() {
        return generate;
    }

    public void setGenerate(Boolean generate) {
        this.generate = generate;
    }

    public Long getLastCompnayKey() {
        return lastCompnayKey;
    }

    public void setLastCompnayKey(Long lastCompnayKey) {
        this.lastCompnayKey = lastCompnayKey;
    }

    public Integer getOrdinateState() {
        return ordinateState;
    }

    public void setOrdinateState(Integer ordinateState) {
        this.ordinateState = ordinateState;
    }

    public Integer getSubordinateState() {
        return subordinateState;
    }

    public void setSubordinateState(Integer subordinateState) {
        this.subordinateState = subordinateState;
    }
}

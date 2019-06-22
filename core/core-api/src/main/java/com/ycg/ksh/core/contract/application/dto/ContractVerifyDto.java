package com.ycg.ksh.core.contract.application.dto;

import com.ycg.ksh.common.entity.BaseEntity;

import java.time.LocalDateTime;

/**
 * 合同的审核
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
public class ContractVerifyDto extends BaseEntity {

    private Integer verifyStatus;//审核状态--------------审核状态
    private LocalDateTime verifyTime;//审核时间--------------审核时间
    private String reason;//不通过的原因--------------不通过的原因

    public ContractVerifyDto() {
    }

    public ContractVerifyDto(Integer verifyStatus, LocalDateTime verifyTime, String reason) {
        this.verifyStatus = verifyStatus;
        this.verifyTime = verifyTime;
        this.reason = reason;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public LocalDateTime getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(LocalDateTime verifyTime) {
        this.verifyTime = verifyTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

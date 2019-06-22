package com.ycg.ksh.core.contract.domain.model;

import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.core.common.domain.Model;
import com.ycg.ksh.core.util.Constants;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 合同的审核
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
public class ContractVerify extends Model {

    private long id;
    private int verifyStatus;//审核状态--------------审核状态
    private LocalDateTime verifyTime;//审核时间--------------审核时间
    private String reason;//不通过的原因--------------不通过的原因
    private Integer userId;//审核人编号--------------审核人编号


    public ContractVerify(int verifyStatus, LocalDateTime verifyTime, String reason, Integer userId) {
        this.setVerifyStatus(verifyStatus);
        this.setVerifyTime(verifyTime);
        this.setReason(reason);
        this.setUserId(userId);
    }
    public ContractVerify(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContractVerify)) return false;
        ContractVerify that = (ContractVerify) o;
        return verifyStatus == that.verifyStatus && Objects.equals(verifyTime, that.verifyTime) && Objects.equals(reason, that.reason) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(verifyStatus, verifyTime, reason, userId);
    }

    private long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    private void setVerifyStatus(int verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    private void setVerifyTime(LocalDateTime verifyTime) {
        this.verifyTime = verifyTime;
    }

    private void setReason(String reason) {
        if (Constants.CONTRACT_VERIFY_REFUSE == verifyStatus) {
            Assert.notBlank(reason, "审核不通过时，原因不能为空");
        }
        this.reason = reason;
    }

    private void setUserId(Integer userId) {
        Assert.notBlank(userId, "审核人的用户编号不能为空");
        this.userId = userId;
    }

    public int getVerifyStatus() {
        return verifyStatus;
    }

    public LocalDateTime getVerifyTime() {
        return verifyTime;
    }

    public String getReason() {
        return reason;
    }

    public Integer getUserId() {
        return userId;
    }
}

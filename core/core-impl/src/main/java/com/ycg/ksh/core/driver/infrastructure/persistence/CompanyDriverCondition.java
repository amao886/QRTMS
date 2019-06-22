package com.ycg.ksh.core.driver.infrastructure.persistence;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.core.util.Constants;

/**
 * 企业司机查询条件
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/05 0005
 */
public class CompanyDriverCondition extends BaseEntity {

    protected  Long companyKey;

    protected String likeString;
    protected Integer status;//状态---待审核、合格、不合格

    public static CompanyDriverCondition build(Long companyKey, String likeString, Integer status) {

        CompanyDriverCondition condition = new CompanyDriverCondition();

        condition.companyKey = companyKey;
        condition.likeString = likeString;
        condition.status = status;

        return condition;
    }
}

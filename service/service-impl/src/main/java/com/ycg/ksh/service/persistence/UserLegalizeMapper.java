package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.UserLegalize;
import tk.mybatis.mapper.common.Mapper;

public interface UserLegalizeMapper extends Mapper<UserLegalize> {

    /**
     * 查询身份证号是否存在
     *
     * @param idCard
     * @return
     */
    public Integer checkIdCardCount(String idCard);
}
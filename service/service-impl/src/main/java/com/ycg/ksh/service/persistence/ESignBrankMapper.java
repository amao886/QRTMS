package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.ESignBrank;
import tk.mybatis.mapper.common.Mapper;

public interface ESignBrankMapper extends Mapper<ESignBrank> {

    /**
     * 根据行号获取银行信息
     * @param code
     * @return
     */
    ESignBrank getByBrankCode(String code);

}
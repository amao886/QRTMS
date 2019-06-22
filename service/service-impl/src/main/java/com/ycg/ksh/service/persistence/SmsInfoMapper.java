package com.ycg.ksh.service.persistence;

import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.persistent.SmsInfo;
import tk.mybatis.mapper.common.Mapper;

public interface SmsInfoMapper extends Mapper<SmsInfo> {

    /**
     *  根据用户ID和接收人手机号查询相关记录
     * @Author：wangke
     * @description：
     * @Date：14:01 2017/12/7
     */
    int querySendCountById(SmsInfo smsInfo) throws ParameterException;
}
package com.ycg.ksh.service.persistence.enterprise;

import com.ycg.ksh.entity.persistent.enterprise.CompanyConfig;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

/**
 * 企业配置接口
 * @Auther: wangke
 * @Date: 2018/9/17 13:50
 * @Description:
 */
public interface CompanyConfigMapper extends Mapper<CompanyConfig> {

    Collection<String> listConfigKeys(Long companyKey);

    void inserts(Collection<CompanyConfig> configs);


    void updateBykey(CompanyConfig config);

}

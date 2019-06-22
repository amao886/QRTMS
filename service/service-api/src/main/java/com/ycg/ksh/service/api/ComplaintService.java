package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.ComplaintAlliance;
import com.ycg.ksh.entity.service.enterprise.ComplaintSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public interface ComplaintService {

    Logger logger = LoggerFactory.getLogger(ComplaintService.class);

    /**
     * 客戶投诉
     *
     * @param orderKeys
     * @param userKey
     * @throws ParameterException
     * @throws BusinessException
     */
    void modifyComplaint(Integer userKey, String content, Collection<Long> orderKeys) throws ParameterException, BusinessException;

    /**
     * 列表查询
     *
     * @param search
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    CustomPage<ComplaintAlliance> pageComplaint(ComplaintSearch search, PageScope pageScope) throws ParameterException, BusinessException;

}

package com.ycg.ksh.service.persistence.enterprise;

import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.enterprise.TemplateDetail;

import java.util.Collection;

public interface TemplateDetailMapper extends CustomMapper<TemplateDetail> {

    /**
     * 查询已存在的模板详情
     * @param templateId
     * @return
     */
    Collection<Long> listIdsByTemplateId(Long templateId);

    /**
     * 批量删除模板
     * @param ids
     */
    void deleteTemplate(Collection<Long> ids);
}
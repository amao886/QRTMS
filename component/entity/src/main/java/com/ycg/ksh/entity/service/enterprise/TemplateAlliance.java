package com.ycg.ksh.entity.service.enterprise;
/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/9
 */

import com.ycg.ksh.entity.persistent.enterprise.ImportTemplate;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Collection;

/**
 * &#x5bfc;&#x5165;&#x6a21;&#x677f;&#x4fe1;&#x606f;
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/9
 */
public class TemplateAlliance extends ImportTemplate {

    private Collection<TemplateDescribe> describes;

    private Integer[] emptyColumns;

    public TemplateAlliance() { }

    public TemplateAlliance(ImportTemplate template) throws Exception {
        this();
        BeanUtils.copyProperties(this, template);
    }

    public Collection<TemplateDescribe> getDescribes() {
        return describes;
    }

    public void setDescribes(Collection<TemplateDescribe> describes) {
        this.describes = describes;
    }

    public Integer[] getEmptyColumns() {
        return emptyColumns;
    }

    public void setEmptyColumns(Integer[] emptyColumns) {
        this.emptyColumns = emptyColumns;
    }
}

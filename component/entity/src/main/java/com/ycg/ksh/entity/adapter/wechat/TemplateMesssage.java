package com.ycg.ksh.entity.adapter.wechat;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/26
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 微信模板消息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/26
 */
public class TemplateMesssage extends BaseEntity {

    private TemplateType templateType;//模板

    private String touser;//接收者openid
    private String url;//模板跳转链接
    private Map<String, TemplateDataValue> templateDatas; //模板数据

    public TemplateMesssage() {
    }

    public TemplateMesssage(String touser) {
        this.touser = touser;
    }

    public TemplateMesssage(String touser, TemplateType templateType) {
        this.touser = touser;
        this.templateType = templateType;
    }

    public TemplateMesssage(String touser, TemplateType templateType, String url) {
        this.touser = touser;
        this.templateType = templateType;
        this.url = url;
    }

    public void addData(String key, TemplateDataValue value){
        if(templateDatas == null){
            templateDatas = new HashMap<String, TemplateDataValue>();
        }
        templateDatas.put(key, value);
    }

    public String templateKey(String pkey){
        return Optional.ofNullable(templateType).map(t->t.key(pkey)).orElse(StringUtils.EMPTY);
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, TemplateDataValue> getTemplateDatas() {
        return templateDatas;
    }

    public void setTemplateDatas(Map<String, TemplateDataValue> templateDatas) {
        this.templateDatas = templateDatas;
    }
}

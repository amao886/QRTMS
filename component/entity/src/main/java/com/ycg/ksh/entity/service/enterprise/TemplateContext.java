package com.ycg.ksh.entity.service.enterprise;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/13
 */

import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 模板操作的上下文
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/13
 */
public class TemplateContext extends BaseEntity {

    private Integer operatorKey;
    private Long templateKey;
    private Long companyKey;
    private Long uniqueKey;//当前会话的唯一标识
    private Integer eventKey;

    private PartnerType partnerType;

    private TemplateAlliance alliance;
    private Collection<TemplateDescribe> describes;
    private Map<Integer, TemplateDescribe> mappings;//序号映射
    private Map<Long, TemplateDescribe> mappingKeys;//编号映射

    private Collection<Integer> uniques;//参与唯一合并的列

    public TemplateContext() {
    }

    public TemplateContext(Integer operatorKey) {
        this.operatorKey = operatorKey;
    }

    public TemplateContext(Integer operatorKey, Long templateKey, Long companyKey) {
        this.operatorKey = operatorKey;
        this.templateKey = templateKey;
        this.companyKey = companyKey;
    }

    public TemplateContext(Integer operatorKey, PartnerType partnerType, Long templateKey) {
        this.partnerType = partnerType;
        this.operatorKey = operatorKey;
        this.templateKey = templateKey;
    }

    public TemplateContext(Integer operatorKey, PartnerType partnerType, Long companyKey, Long templateKey) {
        this.partnerType = partnerType;
        this.operatorKey = operatorKey;
        this.companyKey = companyKey;
        this.templateKey = templateKey;
    }


    public TemplateContext(Integer operatorKey, Long templateKey) {
        this.operatorKey = operatorKey;
        this.templateKey = templateKey;
    }

    public Integer getCategory(){
        if(alliance != null){
            return alliance.getCategory();
        }
        return null;
    }

    public boolean is(Integer category){
        return alliance.getCategory() != null && alliance.getCategory() - category == 0;
    }

    public Collection<TemplateDescribe> describes(){
        return alliance.getDescribes();
    }

    public int size(){
        return describes() == null ? 0 : describes().size();
    }

    public Collection<Integer> uniques(){
        return uniques;
    }

    public TemplateDescribe mappingKey(Long key){
        return mappingKeys.get(key);
    }

    public Long getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(Long uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public TemplateDescribe mapping(Integer index){
        return mappings.get(index);
    }

    public Integer getStartRow() {
        if(alliance != null){
            return alliance.getStartRow();
        }
        return 0;
    }

    public Integer getStartColumn() {
        if(alliance != null){
            return alliance.getStartColumn();
        }
        return 0;
    }

    public Integer getOperatorKey() {
        return operatorKey;
    }

    public void setOperatorKey(Integer operatorKey) {
        this.operatorKey = operatorKey;
    }

    public Long getTemplateKey() {
        return templateKey;
    }

    public void setTemplateKey(Long templateKey) {
        this.templateKey = templateKey;
    }

    public Long getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Long companyKey) {
        this.companyKey = companyKey;
    }

    public TemplateAlliance getAlliance() {
        return alliance;
    }

    public void setAlliance(TemplateAlliance alliance) {
        this.alliance = alliance;
        if(alliance != null){
            if(describes() == null || describes().isEmpty()){
                mappings = Optional.ofNullable(mappings).orElse(Collections.emptyMap());
                mappingKeys = Optional.ofNullable(mappingKeys).orElse(Collections.emptyMap());
                uniques = Optional.ofNullable(uniques).orElse(Collections.emptyList());
            }else{
                mappings = describes().stream().collect(Collectors.toMap(TemplateDescribe::getPositionIndex, TemplateDescribe -> TemplateDescribe));
                mappingKeys = describes().stream().collect(Collectors.toMap(TemplateDescribe::getDetailKey, TemplateDescribe -> TemplateDescribe));
                uniques = describes().stream().filter(TemplateDescribe::getUnique).map(TemplateDescribe::getPositionIndex).collect(Collectors.toList());
            }
        }
    }

    public PartnerType getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(PartnerType partnerType) {
        this.partnerType = partnerType;
    }

    public Integer getEventKey() {
        return eventKey;
    }

    public void setEventKey(Integer eventKey) {
        this.eventKey = eventKey;
    }
}

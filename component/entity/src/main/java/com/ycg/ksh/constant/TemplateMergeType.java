package com.ycg.ksh.constant;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/28
 */

import com.google.common.collect.Lists;
import com.ycg.ksh.entity.common.constant.Constants;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 模板合并的方式
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/28
 */
public enum TemplateMergeType {
    DONOT(0, new HashMap<Integer, Collection<Long>>(){
        {
            put(Constants.TEMPLATE_CATEGORY_SHIP, Lists.newArrayList());
            put(Constants.TEMPLATE_CATEGORY_PLAN, Lists.newArrayList());
        }
    }),//不合并
    DELIVERYNO(1, new HashMap<Integer, Collection<Long>>(){
        {
            put(Constants.TEMPLATE_CATEGORY_SHIP, Lists.newArrayList(228659327393793L));
            put(Constants.TEMPLATE_CATEGORY_PLAN, Lists.newArrayList(202L));
        }
    }),//按送货单号合并
    RECEIVE(2, new HashMap<Integer, Collection<Long>>(){
        {
            put(Constants.TEMPLATE_CATEGORY_SHIP, Lists.newArrayList(228659327393795L, 228659327393796L, 228659327393797L, 228659327393798L));
            put(Constants.TEMPLATE_CATEGORY_PLAN, Lists.newArrayList(204L, 205L, 206L, 207L));
        }
    });//按收货信息合并

    private int code;

    private Map<Integer, Collection<Long>> uniques;

    TemplateMergeType(int code, Map<Integer, Collection<Long>> uniques) {
        this.code = code;
        this.uniques = uniques;
    }

    public final static TemplateMergeType convert(Integer code){
        if(code != null ){
            for (TemplateMergeType fettle : values()) {
                if(fettle.code - code == 0){
                    return fettle;
                }
            }
        }
        return TemplateMergeType.DONOT;
    }

    public boolean unique(int ctype, Long key){
        return uniques.get(ctype).contains(key);
    }

    public int getCode() {
        return code;
    }

    public boolean isDonot() {
        return DONOT == this;
    }
    public boolean isDeliveryNo() {
        return DELIVERYNO == this;
    }
    public boolean isReceive() {
        return RECEIVE == this;
    }
}

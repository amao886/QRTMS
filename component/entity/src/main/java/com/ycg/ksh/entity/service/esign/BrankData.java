package com.ycg.ksh.entity.service.esign;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/23
 */

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Collection;
import java.util.Map;

/**
 * 银行数据
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/23
 */
public class BrankData extends BaseEntity {

    private Collection<String> branks;//银行名称
    private Collection<String> provinces;//省或直辖市
    private Map<String, Collection<String>> citys;//省市映射数据

    public BrankData() { }

    public BrankData(Collection<String> branks, Collection<String> provinces, Map<String, Collection<String>> citys) {
        this.branks = branks;
        this.provinces = provinces;
        this.citys = citys;
    }

    public Collection<String> getBranks() {
        return branks;
    }

    public void setBranks(Collection<String> branks) {
        this.branks = branks;
    }

    public Collection<String> getProvinces() {
        return provinces;
    }

    public void setProvinces(Collection<String> provinces) {
        this.provinces = provinces;
    }

    public Map<String, Collection<String>> getCitys() {
        return citys;
    }

    public void setCitys(Map<String, Collection<String>> citys) {
        this.citys = citys;
    }
}

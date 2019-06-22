package com.ycg.ksh.core.driver.domain.model;

import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.encrypt.MD5;
import com.ycg.ksh.core.common.domain.Model;

import java.util.Objects;
import java.util.Optional;

/**
 * 司机路线信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/03 0003
 */
public class CompanyDriverRoute extends Model {

    private String id;
    private Long driverKey; //司机标识-------所属企业司机的唯一标识
    private Integer type;//路线类型-------长途、短途
    private String start; //路线起点-------省市区，三级行政区划格式
    private String end;//路线终点-------省市区，三级行政区划格式

    public CompanyDriverRoute() {
    }

    public CompanyDriverRoute(Integer type, String start, String end) {
        this.setType(type);
        this.setStart(start);
        this.setEnd(end);
    }

    public CompanyDriverRoute(Long driverKey, Integer type, String start, String end) {
        this.setDriverKey(driverKey);
        this.setType(type);
        this.setStart(start);
        this.setEnd(end);
    }
    public CompanyDriverRoute modify(Long driverKey){
        return new CompanyDriverRoute(driverKey, type, start, end);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyDriverRoute)) return false;
        CompanyDriverRoute that = (CompanyDriverRoute) o;
        return Objects.equals(driverKey, that.driverKey) && Objects.equals(type, that.type) && Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverKey, type, start, end);
    }

    private void setDriverKey(Long driverKey) {
        Assert.notBlank(driverKey, "司机编号不能为空");
        this.driverKey = driverKey;
    }

    private void setType(Integer type) {
        Assert.notBlank(type, "路线类型不能为空");
        this.type = type;
    }

    private void setStart(String start) {
        Assert.notBlank(start, "起点不能为空");
        this.start = start;
    }

    private void setEnd(String end) {
        Assert.notBlank(end, "终点不能为空");
        this.end = end;
    }
    protected void setId(String id) {
        this.id = id;
    }

    protected String getId() {
        return Optional.ofNullable(id).orElse(MD5.encrypt(driverKey +"#"+ type +"#"+ start +"#"+ end));
    }
    public Long getDriverKey() {
        return driverKey;
    }

    public Integer getType() {
        return type;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}

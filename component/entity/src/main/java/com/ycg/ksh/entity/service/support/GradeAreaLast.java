package com.ycg.ksh.entity.service.support;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/4
 */

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * @TODO 类描述
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/4
 */
public class GradeAreaLast extends BaseEntity {

    private Integer code;
    private String name;

    public GradeAreaLast() {
    }

    public GradeAreaLast(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

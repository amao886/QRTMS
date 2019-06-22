package com.ycg.ksh.entity.service.support;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/4
 */

import com.google.common.collect.Lists;

import java.util.Collection;

/**
 * 省市区
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/4
 */
public class GradeArea extends GradeAreaLast {

    private static final Collection<GradeAreaLast> NULL = Lists.newArrayList(new GradeAreaLast(0, ""));

    private Collection<GradeAreaLast> sub;

    public GradeArea() {
    }

    public GradeArea(Integer code, String name, Collection<GradeAreaLast> sub) {
        super(code, name);
        this.sub = sub;
    }

    public GradeArea(Integer code, String name) {
        super(code, name);
        this.sub = NULL;
    }

    public Collection<GradeAreaLast> getSub() {
        return sub;
    }

    public void setSub(Collection<GradeAreaLast> sub) {
        this.sub = sub;
    }
}

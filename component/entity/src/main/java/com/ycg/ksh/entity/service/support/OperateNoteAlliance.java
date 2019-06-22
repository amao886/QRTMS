package com.ycg.ksh.entity.service.support;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/15
 */

import com.ycg.ksh.common.util.BeanUtils;
import com.ycg.ksh.entity.persistent.OperateNote;
import com.ycg.ksh.entity.service.ConciseUser;

/**
 * 操作类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/15
 */
public class OperateNoteAlliance extends OperateNote {

    private String area;

    private ConciseUser user;

    public OperateNoteAlliance() {
    }

    public OperateNoteAlliance(OperateNote operateNote, ConciseUser user) throws Exception {
        this(operateNote);
        this.user = user;
    }

    public OperateNoteAlliance(OperateNote operateNote) throws Exception {
        BeanUtils.build().copyProperties(this, operateNote);
    }

    public ConciseUser getUser() {
        return user;
    }

    public void setUser(ConciseUser user) {
        this.user = user;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}

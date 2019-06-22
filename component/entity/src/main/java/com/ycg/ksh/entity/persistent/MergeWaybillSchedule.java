package com.ycg.ksh.entity.persistent;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 发货计划扩展类
 *
 * @Author:wangke
 * @Description:
 * @Date:Create in 17:28 2017/11/23
 * @Modified By:
 */
public class MergeWaybillSchedule extends WaybillSchedule {

    private static final long serialVersionUID = -6069645710627107862L;
    private ProjectGroup group; //组

    public MergeWaybillSchedule(WaybillSchedule waybillSchedule) throws Exception {
        super();
        BeanUtils.copyProperties(this, waybillSchedule);
    }

    public ProjectGroup getGroup() {
        return group;
    }

    public void setGroup(ProjectGroup group) {
        this.group = group;
    }
}

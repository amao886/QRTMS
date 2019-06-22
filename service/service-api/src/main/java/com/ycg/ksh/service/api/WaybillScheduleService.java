/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 09:28:52
 */
package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.MergeWaybillSchedule;
import com.ycg.ksh.entity.persistent.WaybillSchedule;
import com.ycg.ksh.entity.service.PageScope;

import java.util.Collection;
import java.util.List;

/**
 * 发货计划业务逻辑接口
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 09:28:52
 */
public interface WaybillScheduleService {

    /***
     * 根据ID查询发货计划详情
     * @param id
     * @return
     */
    MergeWaybillSchedule queryWaybillScheduleById(Integer id) throws Exception;

    /**
     * 发货计划修改
     *
     * @Author：wangke
     * @description：
     * @Date：14:23 2017/11/23
     */
    int updateWaybillSchedule(WaybillSchedule waybillSchedule) throws ParameterException, BusinessException;
    
    /**
     * 发货计划批量保存
     * @Author：wangke
     * @description：
     * @Date：14:23 2017/11/23
     */
    void saveBatchSchedule(List<WaybillSchedule> list) throws ParameterException, BusinessException;
    
    /**
     * 发货计划保存
     * @Author：wangke
     * @description：
     * @Date：14:23 2017/11/23
     */
    int saveSchedule(WaybillSchedule waybillSchedule) throws ParameterException, BusinessException;


    /**
     * 查询发货计划列表
     *
     * @Author：wangke
     * @description：
     * @Date：17:43 2017/11/23
     */
    Collection<MergeWaybillSchedule> queryWaybillScheduleList(WaybillSchedule waybillSchedule) throws ParameterException;


    /**
     * 分页查询发货计划
     *
     * @Author：wangke
     * @description：
     * @Date：17:59 2017/11/23
     */
    CustomPage<MergeWaybillSchedule> pageWaybillSchedule(WaybillSchedule waybillSchedule, PageScope pageScope) throws ParameterException;

}

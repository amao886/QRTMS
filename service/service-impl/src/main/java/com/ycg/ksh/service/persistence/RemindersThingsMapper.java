package com.ycg.ksh.service.persistence;


import com.github.pagehelper.Page;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.persistent.RemindersThings;
import com.ycg.ksh.entity.service.MergeRemindersThings;
import com.ycg.ksh.entity.service.ReminderSearch;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

/***
 * 日常工作持久类
 */
public interface RemindersThingsMapper extends Mapper<RemindersThings> {


    /**
     * 多条件查询运单信息
     * 指定用户创建的、指派给指定用户的、指定用所在项目组的
     * @param search
     * @return
     */
    Collection<MergeRemindersThings> listRemindersThingsBySomething(ReminderSearch search);

    /**
     * 多条件分页查询运单信息
     * 指定用户创建的、指派给指定用户的、指定用所在项目组的
     * @param search
     * @param bounds
     * @return
     */
    Page<MergeRemindersThings> listRemindersThingsBySomething(ReminderSearch search, RowBounds bounds);
    /**
     * 查询待办事项
     *
     * @param userKey
     * @param count
     * @return
     * @author wangke
     */
    Collection<MergeRemindersThings> queryTodos(Integer userKey, Integer count);

    /**
     * 进度跟踪列表
     * @param userKey
     * @param count
     * @return
     */
    Collection<MergeRemindersThings> queryFollows(Integer userKey, Integer count);

    /**
     * 跟进任务单查询
     * @param waybillKey
     * @param status
     * @param type
     * @return
     */
    Collection<RemindersThings> listByWaybillKey(Integer waybillKey, Integer status, Integer type);
    /**
     * 修改处理状态
     *
     * @param id
     */
    void updateProcessingStatus(Integer id);

    /**
     * 根据用户ID和运单ID查询当前运单催办状态
     *
     * @param things
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<RemindersThings> getRemindersTypeList(RemindersThings things);

    /**
     * @param conveyanceKey
     * @return
     */
    Collection<RemindersThings> listByParentConveyanceKey(Long conveyanceKey);
    /**
     * 查询所有未处理的消息
     * @param conveyanceKey
     * @return
     */
    Collection<RemindersThings> listByConveyanceKey(Long conveyanceKey);
}

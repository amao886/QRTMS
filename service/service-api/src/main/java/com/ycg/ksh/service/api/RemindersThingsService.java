package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.entity.persistent.Conveyance;
import com.ycg.ksh.entity.persistent.RemindersThings;
import com.ycg.ksh.entity.persistent.WaybillTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/***
 * 日常工作接口类
 */
public interface RemindersThingsService {

    final Logger logger = LoggerFactory.getLogger(RemindersThingsService.class);


    Conveyance getByTodoKey(Integer todoKey) throws ParameterException, BusinessException;
    /**
     * 上报位置
     * @param userId
     * @param waybillTrack
     * @param msgKey
     * @param punctual
     * @param selectTime
     */
    void location(Integer userId, WaybillTrack waybillTrack, Integer msgKey, Boolean punctual, Date selectTime) throws ParameterException, BusinessException;
    /**
     * 查询待办列表
     * @param userKey
     * @return
     */
    Collection<MergeRemindersThings> listTodos(Integer userKey) throws ParameterException, BusinessException;
    /**
     * 进度跟踪列表查询
     *
     * @param userKey 用戶ID
     * @param count   数量
     * @return
     * @author wangke
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<MergeRemindersThings> listFollows(Integer userKey, Integer count) throws ParameterException, BusinessException;

    /***
     * 查询催办对象User
     * @author wangke
     * @date 2018/2/28 10:13
     * @param id
     * @return
     */
    List<MergeUserConveyance> queryUserList(Integer userKey, Long id) throws ParameterException, BusinessException;

    /**
     * 转发到项目组
     *
     * @param uKey     操作人用户ID
     * @param groupKey 项目组ID
     * @param reminder 要转发的待办信息
     * @throws ParameterException
     * @throws BusinessException
     */
    void forwardProjectGroup(Integer uKey, Integer groupKey, RemindersThings reminder) throws ParameterException, BusinessException;

    /**
     * 转发到下级
     *
     * @param uKey            操作人用户ID
     * @param subordinateKeys 下级运单编号集合
     * @param reminder        要转发的待办信息
     * @throws ParameterException
     * @throws BusinessException
     */
    void forwardSubordinate(Integer uKey, Collection<Long> subordinateKeys, RemindersThings reminder) throws ParameterException, BusinessException;

    /**
     * 根据运单ID查询催办详情
     *
     * @param id
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @author wangke
     * @date 2018/3/1 9:32
     */
    RemindersThings getRemindersInfoByConId(Long id) throws ParameterException, BusinessException;

    /**
     * 根据用户ID和运单ID查询当前运单催办状态
     *
     * @param things
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<RemindersThings> getRemindersTypeList(RemindersThings things) throws ParameterException, BusinessException;

    /**
     * 分页查询运单信息
     *
     * @param search
     * @param scope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    CustomPage<WaybillConveyance> pageConveyance(ConveyanceSearch search, PageScope scope) throws ParameterException, BusinessException;

    /**
     * 分页查询待办事项
     *
     * @param search
     * @param scope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    CustomPage<MergeRemindersThings> listMergeRemindersThings(Integer ukey, ReminderSearch search, PageScope scope) throws ParameterException, BusinessException;

    /**
     * 根据待办主键查询待办操作日志
     * @param uKey
     * @param todoKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<MergeTodoLog> listTodologByKey(Integer uKey, Integer todoKey) throws ParameterException, BusinessException;
}

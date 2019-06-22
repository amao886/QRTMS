package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.common.constant.TodoFettle;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.LocalCacheManager;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.DateFormatUtils;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.service.persistence.RemindersThingsMapper;
import com.ycg.ksh.service.persistence.TodoLogMapper;
import com.ycg.ksh.service.api.*;
import com.ycg.ksh.service.observer.ReceiptObserverAdapter;
import com.ycg.ksh.service.observer.TrackObserverAdapter;
import com.ycg.ksh.service.observer.WaybillObserverAdapter;
import com.ycg.ksh.service.support.assist.LocalCacheFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 日常工作接口实现类
 *
 * @author wangke
 * @create 2018-02-27 9:33
 **/
@Service("ksh.core.service.remindersThingsService")
public class RemindersThingsServiceImpl implements RemindersThingsService, ReceiptObserverAdapter, TrackObserverAdapter, WaybillObserverAdapter {

    @Resource
    RemindersThingsMapper remindersThingsMapper;

    @Resource
    TodoLogMapper todoLogMapper;

    @Resource
    FriendsService friendsService;

    @Resource
    WaybillTrackService trackService;

    @Resource
    ConveyanceService conveyanceService;

    @Resource
    UserService userService;


    private void log(Integer uKey, Integer todoKey, Integer logType){
        TodoLog todoLog = new TodoLog();
        todoLog.setLogTime(new Date());
        todoLog.setLogType(logType);
        todoLog.setTodoKey(todoKey);
        todoLog.setUserKey(uKey);
        todoLogMapper.insert(todoLog);
    }

    private void log(Integer uKey, Integer logType, Collection<Integer> todoKeys){
        if(CollectionUtils.isNotEmpty(todoKeys)){
            Collection<TodoLog> collection = new ArrayList<TodoLog>(todoKeys.size());
            Date ctime = new Date();
            for (Integer todoKey : todoKeys) {
                TodoLog todoLog = new TodoLog();
                todoLog.setLogTime(ctime);
                todoLog.setLogType(logType);
                todoLog.setTodoKey(todoKey);
                todoLog.setUserKey(uKey);
                collection.add(todoLog);
            }
            todoLogMapper.insertBatch(collection);
        }
    }

    @Override
    public Conveyance getByTodoKey(Integer todoKey) throws ParameterException, BusinessException {
        Assert.notBlank(todoKey, "待办事项编号不能为空");
        RemindersThings remindersThings = remindersThingsMapper.selectByPrimaryKey(todoKey);
        if(remindersThings == null){
            throw new BusinessException("待办事项获取异常");
        }
        return conveyanceService.queryConveyanceInfo(remindersThings.getConveyanceId());
    }

    /**
     * 根据待办主键查询待办操作日志
     *
     * @param uKey
     * @param todoKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Collection<MergeTodoLog> listTodologByKey(Integer uKey, Integer todoKey) throws ParameterException, BusinessException {
        Assert.notBlank(todoKey, "待办事项主键不能为空");
        Collection<TodoLog> collection = todoLogMapper.selectByTodoKey(todoKey);
        Collection<MergeTodoLog> results = new ArrayList<MergeTodoLog>(collection.size());
        LocalCacheManager<AssociateUser> associateUserCache = LocalCacheFactory.createFriendUserCache(friendsService);
        for (TodoLog todoLog : collection) {
            try {
                MergeTodoLog mergeTodoLog = new MergeTodoLog(todoLog);
                mergeTodoLog.setUser(associateUserCache.get(uKey, mergeTodoLog.getUserKey()));
                TodoFettle fettle = TodoFettle.convert(mergeTodoLog.getLogType());
                if(fettle.notRead()){
                    mergeTodoLog.setTypeString("发送");
                }else if(fettle.read()){
                    mergeTodoLog.setTypeString("查看");
                }else if(fettle.urge()){
                    mergeTodoLog.setTypeString("已转发");
                }else{
                    mergeTodoLog.setTypeString("已完成");
                }
                mergeTodoLog.setTimeString(DateFormatUtils.format(mergeTodoLog.getLogTime()));
                results.add(mergeTodoLog);
            } catch (Exception e) { }
        }
        return results;
    }

    /**
     * 确认收货通知
     * <p>
     *
     * @param context 任务单上下文
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-15 11:51:57
     */
    @Override
    public void onWaybillFettleChange(WaybillContext context) throws BusinessException {
        if (context.getWaybillStatus().receive()) {//确认收货
            Collection<RemindersThings> collection = remindersThingsMapper.listByWaybillKey(context.getId(), TodoFettle.HANDLE.getCode(), null);
            if(CollectionUtils.isNotEmpty(collection)){
                Collection<Integer> collectionKeys = new ArrayList<Integer>(collection.size());
                for (RemindersThings remindersThings : collection) {
                    collectionKeys.add(remindersThings.getId());
                }
                Example example = new Example(RemindersThings.class);
                example.createCriteria().andIn("id", collectionKeys);
                RemindersThings update = new RemindersThings();
                update.setProcessingStatus(TodoFettle.COMPLETE.getCode());
                update.setUpdateTime(new Date());
                remindersThingsMapper.updateByExampleSelective(update, example);
                log(context.getUserKey(),TodoFettle.HANDLE.getCode(), collectionKeys);
            }
        }
    }

    /**
     * 回单上传通知
     * <p>
     *
     * @param context 任务单上下文
     * @param receipt 回单信息
     * @param count   回单数量
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-15 11:02:13
     */
    @Override
    public void notifyUploadReceipt(WaybillContext context, WaybillReceipt receipt, Integer count) throws BusinessException {
        Collection<RemindersThings> collection = remindersThingsMapper.listByWaybillKey(context.getId(), TodoFettle.HANDLE.getCode(), 2);
        if(CollectionUtils.isNotEmpty(collection)){
            Collection<Integer> collectionKeys = new ArrayList<Integer>(collection.size());
            for (RemindersThings remindersThings : collection) {
                collectionKeys.add(remindersThings.getId());
            }
            Example example = new Example(RemindersThings.class);
            example.createCriteria().andIn("id", collectionKeys);
            RemindersThings update = new RemindersThings();
            update.setProcessingStatus(TodoFettle.COMPLETE.getCode());
            update.setUpdateTime(new Date());
            remindersThingsMapper.updateByExampleSelective(update, example);
            log(context.getUserKey(),TodoFettle.HANDLE.getCode(), collectionKeys);
        }
    }

    /**
     * 通知任务单位置上报
     * <p>
     *
     * @param context
     * @param waybillTrack
     * @param driverScan
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-15 12:54:43
     */
    @Override
    public void notifyLocationReport(WaybillContext context, WaybillTrack waybillTrack, boolean driverScan) throws BusinessException {
        if (driverScan) {
            Collection<RemindersThings> collection = remindersThingsMapper.listByWaybillKey(context.getId(), TodoFettle.HANDLE.getCode(), 1);
            if(CollectionUtils.isNotEmpty(collection)){
                Collection<Integer> collectionKeys = new ArrayList<Integer>(collection.size());
                for (RemindersThings remindersThings : collection) {
                    collectionKeys.add(remindersThings.getId());
                }
                Example example = new Example(RemindersThings.class);
                example.createCriteria().andIn("id", collectionKeys);
                RemindersThings update = new RemindersThings();
                update.setProcessingStatus(TodoFettle.COMPLETE.getCode());
                update.setUpdateTime(new Date());
                remindersThingsMapper.updateByExampleSelective(update, example);
                log(context.getUserKey(),TodoFettle.HANDLE.getCode(), collectionKeys);
            }
        }
    }

    /**
     * 上报位置
     *
     * @param waybillTrack
     * @param msgKey
     * @param punctual
     * @param selectTime
     */
    @Override
    public void location(Integer userId, WaybillTrack waybillTrack, Integer msgKey, Boolean punctual, Date selectTime) throws ParameterException, BusinessException {
        trackService.saveLoactionReport(userId, waybillTrack);
        if(msgKey != null){
            RemindersThings updater = new RemindersThings();
            updater.setId(msgKey);
            updater.setMsgRemark("不能准时到达");
            if(selectTime != null){
                updater.setMsgRemark(updater.getMsgRemark() + ",预计"+ DateFormatUtils.format(selectTime,"yyyy-MM-dd")+ "到达");
            }
            remindersThingsMapper.updateByPrimaryKeySelective(updater);
        }
    }

    /**
     * 查询待办列表
     *
     * @param userKey
     * @return
     */
    @Override
    public Collection<MergeRemindersThings> listTodos(Integer userKey) throws ParameterException, BusinessException {
        Assert.notBlank(userKey, "用户ID为空");
        Collection<MergeRemindersThings> reminders = remindersThingsMapper.queryTodos(userKey, 0);
        if (CollectionUtils.isNotEmpty(reminders)) {
            try {
                Collection<Integer> collection = new ArrayList<Integer>();
                for (MergeRemindersThings reminder : reminders) {
                    if (TodoFettle.convert(reminder.getProcessingStatus()).notRead()) {
                        collection.add(reminder.getId());
                    }
                    reminder.setForward(reminder.getGroupKey() == null || reminder.getGroupKey() <=0 || reminder.getHaveChild());
                }
                if (CollectionUtils.isNotEmpty(collection)) {
                    Example example = new Example(RemindersThings.class);
                    example.createCriteria().andIn("id", collection);

                    RemindersThings updater = new RemindersThings();
                    updater.setProcessingStatus(TodoFettle.READ.getCode());
                    updater.setUpdateTime(new Date());
                    remindersThingsMapper.updateByExampleSelective(updater, example);
                    log(userKey,TodoFettle.READ.getCode(), collection);
                }
            } catch (Exception e) {
                logger.warn("更新已读待办事项异常 userKey:{} type:{}", userKey, e.getMessage());
            }
        }
        return reminders;
    }

    /**
     * 进度跟踪列表查询
     *
     * @param userKey 用戶ID
     * @param count   数量
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @author wangke
     */
    @Override
    public Collection<MergeRemindersThings> listFollows(Integer userKey, Integer count) throws ParameterException, BusinessException {
        Assert.notBlank(userKey, "用户ID为空");
        if(count == null || count <= 0){
            count = 3;
        }
        return remindersThingsMapper.queryFollows(userKey, count);
    }

    @Override
    public List<MergeUserConveyance> queryUserList(Integer userKey, Long parentKey) throws BusinessException {
        Collection<Conveyance> conveyanceList = conveyanceService.listConveyanceByParentKey(parentKey);
        List<MergeUserConveyance> userConveyances = new ArrayList<MergeUserConveyance>();
        if (null != conveyanceList) {
            LocalCacheManager<AssociateUser> associateUserCache = LocalCacheFactory.createFriendUserCache(friendsService);
            for (Conveyance conveyance : conveyanceList) {
                MergeUserConveyance mergeUserConveyance = new MergeUserConveyance();
                AssociateUser associateUser = associateUserCache.get(userKey, conveyance.getOwnerKey());
                mergeUserConveyance.setConveyanceId(conveyance.getId());
                mergeUserConveyance.setUnamezn(associateUser.getUnamezn());
                mergeUserConveyance.setId(associateUser.getId());
                userConveyances.add(mergeUserConveyance);
            }
        }
        return userConveyances;
    }

    /**
     * 转发到项目组
     *
     * @param uKey     操作人用户ID
     * @param groupKey 项目组ID
     * @param reminder 要转发的待办信息
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void forwardProjectGroup(Integer uKey, Integer groupKey, RemindersThings reminder) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人编号不能为空");
        Assert.notBlank(groupKey, "项目组编号不能为空");
        Assert.notNull(reminder, "要转发的待办信息不能为空");
        completeSomething(reminder);
        reminder.setSendkey(uKey);
        Collection<Long> conveyanceKeys = new ArrayList<Long>() {{
            add(reminder.getConveyanceId());
        }};
        conveyanceService.shareGroup(uKey, conveyanceKeys, groupKey);
        if(reminder.getId() != null && reminder.getId() > 0){
            log(uKey, reminder.getId(), TodoFettle.URGE.getCode());
        }
    }


    private RemindersThings loadBySomething(Integer uKey, Long conveyanceKey, Integer type){
        Example example = new Example(RemindersThings.class);
        example.orderBy("id").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sendkey", uKey).andEqualTo("conveyanceId", conveyanceKey).andEqualTo("msgType", type);
        List<RemindersThings> collection = remindersThingsMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(collection)){
            if(collection.size() > 1){
                Collection<Integer> deleteKeys = new ArrayList<Integer>(collection.size() - 1);
                for (int i = 1; i < collection.size(); i++) {
                    deleteKeys.add(collection.get(i).getId());
                }
                Example deleteExample = new Example(RemindersThings.class);
                deleteExample.createCriteria().andIn("id", deleteKeys);
                remindersThingsMapper.deleteByExample(deleteExample);
            }
            return collection.get(0);
        }
        return null;
    }

    /**
     * 转发到下级
     *
     * @param uKey            操作人用户ID
     * @param subordinateKeys 下级运单编号集合
     * @param reminder        要转发的待办信息
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void forwardSubordinate(Integer uKey, Collection<Long> subordinateKeys, RemindersThings reminder) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人编号不能为空");
        Assert.notEmpty(subordinateKeys, "下级信息不能为空");
        Assert.notNull(reminder, "要转发的待办信息不能为空");
        completeSomething(reminder);
        reminder.setSendkey(uKey);
        //修改处理状态
        if (reminder.getId() != null && reminder.getId() > 0) {
            RemindersThings remindersThings = new RemindersThings();
            remindersThings.setId(reminder.getId());
            remindersThings.setProcessingStatus(TodoFettle.URGE.getCode());
            remindersThings.setUpdateTime(new Date());
            remindersThingsMapper.updateByPrimaryKeySelective(remindersThings);//转催
            log(uKey, reminder.getId(), TodoFettle.URGE.getCode());
        }
        Date ctime = new Date();
        for (Long subordinateKey : subordinateKeys) {
            Conveyance conveyance = conveyanceService.queryConveyanceInfo(subordinateKey);
            if(conveyance != null){
                if(conveyance.getOwnerKey() - uKey == 0){
                    throw new BusinessException("不能转发给自己");
                }
                //如果已经存在消息,就更新消息，反之新增一条消息
                RemindersThings exister = loadBySomething(uKey, reminder.getConveyanceId(), reminder.getMsgType());
                if(exister == null){
                    RemindersThings newInsert = new RemindersThings();
                    newInsert.setProcessingStatus(TodoFettle.NOT_READ.getCode());
                    newInsert.setParentNewsId(reminder.getParentNewsId());
                    newInsert.setCreatetime(ctime);
                    newInsert.setMsgRemark(reminder.getMsgRemark());
                    newInsert.setConveyanceId(subordinateKey);
                    newInsert.setSendkey(reminder.getSendkey());
                    newInsert.setMsgType(reminder.getMsgType());
                    newInsert.setUpdateTime(ctime);
                    remindersThingsMapper.insert(newInsert);
                    log(uKey, newInsert.getId(), TodoFettle.NOT_READ.getCode());
                }else{
                    exister.setProcessingStatus(TodoFettle.NOT_READ.getCode());
                    exister.setUpdateTime(ctime);
                    remindersThingsMapper.updateByPrimaryKeySelective(exister);
                    log(uKey, exister.getId(), TodoFettle.NOT_READ.getCode());
                }
            }
        }
    }

    private void completeSomething(RemindersThings reminder) {
        if (reminder.getConveyanceId() != null && reminder.getConveyanceId() > 0) {
            reminder.setParentNewsId(0);
        } else {
            RemindersThings exister = remindersThingsMapper.selectByPrimaryKey(reminder.getId());
            if (exister == null) {
                throw new BusinessException("消息获取异常");
            }
            reminder.setConveyanceId(exister.getConveyanceId());
            reminder.setMsgType(exister.getMsgType());
            reminder.setParentNewsId(exister.getId());
        }
        if(reminder.getMsgType() == null ||
                (reminder.getMsgType() != 1 && reminder.getMsgType() != 2)){
            reminder.setMsgType(1);
        }
    }

    @Override
    public RemindersThings getRemindersInfoByConId(Long id) throws ParameterException, BusinessException {
        RemindersThings remindersThings = new RemindersThings();
        remindersThings.setConveyanceId(id);
        return remindersThingsMapper.selectOne(remindersThings);
    }

    @Override
    public Collection<RemindersThings> getRemindersTypeList(RemindersThings things) throws ParameterException, BusinessException {
        return remindersThingsMapper.getRemindersTypeList(things);
    }
    /**
     * 分页查询待办事项
     *
     * @param search
     * @param scope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public CustomPage<MergeRemindersThings> listMergeRemindersThings(Integer ukey, ReminderSearch search, PageScope scope) throws ParameterException, BusinessException {
        if (search == null) {
            search = new ReminderSearch();
        }
        Page<MergeRemindersThings> page = remindersThingsMapper.listRemindersThingsBySomething(search, new RowBounds(scope.getPageNum(), scope.getPageSize()));
        if(CollectionUtils.isNotEmpty(page)){
            LocalCacheManager<AssociateUser> associateUserCache = LocalCacheFactory.createFriendUserCache(friendsService);
            for (MergeRemindersThings reminder : page) {
                reminder.setOwner(associateUserCache.get(ukey, reminder.getOwnerKey()));
            }
        }
        return new CustomPage<MergeRemindersThings>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }

    @Override
    public CustomPage<WaybillConveyance> pageConveyance(ConveyanceSearch search, PageScope scope) throws ParameterException, BusinessException {
        if (search == null) {
            search = new ConveyanceSearch();
        }
        search.setCancel(false);//不查询已经取消的运单
        CustomPage<WaybillConveyance> page = conveyanceService.listWaybillConveyance(search, scope);
        handleSomething(search.getCreateKey(), page.getCollection(), true);
        return page;
    }

    private void handleSomething(Integer uKey, Collection<WaybillConveyance> conveyances, boolean parent) {
        if (CollectionUtils.isNotEmpty(conveyances)) {
            for (WaybillConveyance conveyance : conveyances) {
                Collection<RemindersThings> collection = remindersThingsMapper.listByConveyanceKey(conveyance.getId());
                if (CollectionUtils.isNotEmpty(collection)) {
                    Integer status = null;
                    for (RemindersThings reminder : collection) {
                        if(parent){
                            status = (reminder.getSendkey() - uKey == 0) ? 1 : 9;//跟进 : 待办
                        }else if(reminder.getConveyanceId() - conveyance.getId() == 0){
                            status = (reminder.getSendkey() - uKey == 0) ? 11 : 22;//已催 : 待办
                        }
                        if(1 == reminder.getMsgType()){ //上报位置
                            conveyance.setAddressStatus(status);
                            conveyance.setAddressTime(reminder.getUpdateTime());
                        }else{
                            conveyance.setReceiptStatus(status);
                            conveyance.setReceiptTime(reminder.getUpdateTime());
                        }
                    }
                }
                handleSomething(uKey, conveyance.getChildren(), false);
            }
        }
    }
}

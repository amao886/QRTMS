/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 09:29:24
 */
package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.entity.persistent.MergeWaybillSchedule;
import com.ycg.ksh.entity.persistent.ProjectGroup;
import com.ycg.ksh.entity.persistent.WaybillSchedule;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.service.persistence.ProjectGroupMapper;
import com.ycg.ksh.service.persistence.WaybillScheduleMapper;
import com.ycg.ksh.service.api.UserService;
import com.ycg.ksh.service.api.WaybillScheduleService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 发货计划业务逻辑实现
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
 * 2017-11-23 09:29:24
 */
@Service("ksh.core.service.waybillScheduleService")
public class WaybillScheduleServiceImpl implements WaybillScheduleService {

    protected final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private WaybillScheduleMapper waybillScheduleMapper;

    @Resource
    ProjectGroupMapper projectGroupMapper;

    @Override
    public MergeWaybillSchedule queryWaybillScheduleById(Integer id) throws Exception {

        WaybillSchedule waybillSchedule = waybillScheduleMapper.selectByPrimaryKey(id);
        if (null == waybillSchedule) {
            throw new BusinessException("对象不存在!");
        }
        MergeWaybillSchedule mergeWaybillSchedule = new MergeWaybillSchedule(waybillSchedule);
        ProjectGroup projectGroup = projectGroupMapper.selectByPrimaryKey(waybillSchedule.getGroupid());
        if (null != projectGroup) {
            mergeWaybillSchedule.setGroup(projectGroup);
        } else {
            throw new BusinessException("组不存在!");
        }
        return mergeWaybillSchedule;
    }

    @Override
    public int updateWaybillSchedule(WaybillSchedule waybillSchedule) throws ParameterException, BusinessException {
        Assert.notNull(waybillSchedule, "实体对象不能为空");
        Assert.notBlank(waybillSchedule.getId(), "修改对象编号不能为空");
        WaybillSchedule existSchedule = waybillScheduleMapper.selectByPrimaryKey(waybillSchedule.getId());
        if (existSchedule == null) {
            throw new BusinessException("要更新的对象不存在!");
        }
        return waybillScheduleMapper.updateByPrimaryKeySelective(waybillSchedule);

    }

    @Override
    public Collection<MergeWaybillSchedule> queryWaybillScheduleList(WaybillSchedule waybillSchedule)
            throws ParameterException {
        Collection<WaybillSchedule> waybillScheduleCollection = waybillScheduleMapper
                .queryWaybillScheduleList(waybillSchedule);
        Collection<MergeWaybillSchedule> collection = new ArrayList<MergeWaybillSchedule>(
                waybillScheduleCollection.size());
        Map<Integer, ProjectGroup> cache = new HashMap<Integer, ProjectGroup>();
        for (WaybillSchedule results : waybillScheduleCollection) {
            try {
                MergeWaybillSchedule mergeWaybillSchedule = new MergeWaybillSchedule(results);
                ProjectGroup group = cache.get(results.getGroupid());
                if (group == null) {
                    group = projectGroupMapper.selectByPrimaryKey(results.getGroupid());
                }
                mergeWaybillSchedule.setGroup(group);
                collection.add(mergeWaybillSchedule);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cache = null;
        return collection;
    }

    @Override
    public CustomPage<MergeWaybillSchedule> pageWaybillSchedule(WaybillSchedule waybillSchedule, PageScope pageScope)
            throws ParameterException {
        if (pageScope == null) {
            pageScope = new PageScope(10, 1);
        }
        pageScope.validate(10, 1);
        Page<WaybillSchedule> page = waybillScheduleMapper.queryWaybillScheduleList(waybillSchedule,
                new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));

        Collection<MergeWaybillSchedule> collection = new ArrayList<MergeWaybillSchedule>(page.size());
        Map<Integer, ProjectGroup> cache = new ConcurrentHashMap<Integer, ProjectGroup>();
        for (WaybillSchedule results : page) {
            try {
                MergeWaybillSchedule mergeWaybillSchedule = new MergeWaybillSchedule(results);
                ProjectGroup group = cache.get(results.getGroupid());
                if (group == null) {
                    group = projectGroupMapper.selectByPrimaryKey(results.getGroupid());

                }
                mergeWaybillSchedule.setGroup(group);
                collection.add(mergeWaybillSchedule);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cache = null;
        return new CustomPage<MergeWaybillSchedule>(page.getPageNum(), page.getPageSize(), page.getTotal(), collection);
    }

    @Override
    public void saveBatchSchedule(List<WaybillSchedule> list) throws ParameterException, BusinessException {
        waybillScheduleMapper.saveBatchSchedule(list);
    }

    @Override
    public int saveSchedule(WaybillSchedule waybillSchedule) throws ParameterException, BusinessException {
        return waybillScheduleMapper.insert(waybillSchedule);
    }
}

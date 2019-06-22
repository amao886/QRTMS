package com.ycg.ksh.service.api;

import com.ycg.ksh.entity.common.constant.WaybillFettle;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.Conveyance;
import com.ycg.ksh.entity.service.ConveyanceSearch;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.Station;
import com.ycg.ksh.entity.service.WaybillConveyance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/23
 */
public interface ConveyanceService {

    final Logger logger = LoggerFactory.getLogger(ConveyanceService.class);


    /**
     * 更改运单状态
     * @param uKey
     * @param conveyanceKey
     * @throws ParameterException
     * @throws BusinessException
     */
    WaybillFettle changeFettle(Integer uKey, Long conveyanceKey) throws ParameterException, BusinessException;
    /**
     * 根据运单编号验证始发地和目的地
     *
     * @param conveyanceKeys
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public Station validate(Collection<Long> conveyanceKeys) throws ParameterException, BusinessException;

    /**
     * 根据条件分页查询任务单-运单信息
     *
     * @param search
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public CustomPage<WaybillConveyance> listWaybillConveyance(ConveyanceSearch search, PageScope scope) throws ParameterException, BusinessException;

    public Collection<WaybillConveyance> listWaybillConveyance(ConveyanceSearch search) throws ParameterException, BusinessException;

    public Collection<Conveyance> listConveyanceByParentKey(Long parentKey) throws ParameterException, BusinessException;
    /**
     * 保存路由指派
     *
     * @param uKey           当前操作用户ID
     * @param conveyanceKeys 运单编号
     * @param routeKey       路由编号
     * @throws ParameterException
     * @throws BusinessException
     */
    public void saveRouteAssign(Integer uKey, Collection<Long> conveyanceKeys, Integer routeKey) throws ParameterException, BusinessException;


    /**
     * 保存整包指派
     *
     * @param uKey           当前操作用户ID
     * @param conveyanceKeys 运单编号
     * @param userKey        用户ID
     * @throws ParameterException
     * @throws BusinessException
     */
    public void saveWholeAssign(Integer uKey, Collection<Long> conveyanceKeys, Integer userKey) throws ParameterException, BusinessException;

    /**
     * 分享到项目组
     *
     * @param uKey           操作用户ID
     * @param conveyanceKeys 运单ID
     * @param groupKey       要分享的项目组ID
     * @throws ParameterException
     * @throws BusinessException
     */
    public void shareGroup(Integer uKey, Collection<Long> conveyanceKeys, Integer groupKey) throws ParameterException, BusinessException;

    /**
     * 修改运单号
     *
     * @param uKey
     * @param conveyanceKey
     * @param number
     * @throws ParameterException
     * @throws BusinessException
     */
    public void editNumber(Integer uKey, Long conveyanceKey, String number) throws ParameterException, BusinessException;


    /**
     * 修改承运人
     *
     * @param uKey
     * @param cconveyanceKey
     * @param ownerKey
     * @throws ParameterException
     * @throws BusinessException
     */
    public void editAssign(Integer uKey, Long cconveyanceKey, Integer ownerKey) throws ParameterException, BusinessException;

    /**
     * 取消指派
     *
     * @param uKey
     * @param conveyanceKeys
     * @throws ParameterException
     * @throws BusinessException
     */
    public void cancelAssign(Integer uKey, Collection<Long> conveyanceKeys) throws ParameterException, BusinessException;


    /**
     * 查询运单详情
     *
     * @param id
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @author wangke
     * @date 2018/2/28 9:30
     */
    public Conveyance queryConveyanceInfo(Long id) throws ParameterException, BusinessException;

}

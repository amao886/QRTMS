/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 09:57:30
 */
package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.DriverContainer;
import com.ycg.ksh.entity.persistent.DriverTrack;
import com.ycg.ksh.entity.service.DriverContainerSearch;
import com.ycg.ksh.entity.service.MergeDriverContainer;
import com.ycg.ksh.entity.service.MergeDriverTrack;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import com.ycg.ksh.entity.service.enterprise.OrderSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * 司机装车
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 09:57:30
 */
public interface DriverService {

    final Logger logger = LoggerFactory.getLogger(DriverService.class);

    /**
     * 根据barcode查询装车详情
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-14 15:22:02
     * @param barcode
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    MergeDriverContainer getMergeByBarcode(String barcode) throws ParameterException, BusinessException;
    /**
     * 根据装车ID查询创车详情
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-14 14:42:06
     * @param dKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    MergeDriverContainer getMergeByKey(Long dKey) throws ParameterException, BusinessException;
    /**
     * 根据装货人查询
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-14 12:35:46
     * @param loadKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<DriverContainer> listByLoadKey(Integer loadKey) throws ParameterException, BusinessException;
    /**
     * 查询装车数量
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-11 14:27:37
     * @param userKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Integer loadCount(Integer userKey) throws ParameterException, BusinessException;

    /**
     * 装车
     * <p>
     *
     * @param uKey
     * @param code
     * @return 不为空时需要上传送货单图片
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 15:28:20
     */
    String load(Integer uKey, String code) throws ParameterException, BusinessException;

    /**
     * 卸货
     * <p>
     *
     * @param uKey
     * @param code
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 15:25:39
     */
    String unload(Integer uKey, String code) throws ParameterException, BusinessException;

    /**
     * 分页查询司机装车信息
     * <p>
     *
     * @param serach
     * @param pageScope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 16:15:49
     */
    CustomPage<MergeDriverContainer> pageByDriver(DriverContainerSearch serach, PageScope pageScope) throws ParameterException, BusinessException;


    CustomPage<DriverContainer> pageBySomething(DriverContainerSearch serach, PageScope pageScope) throws ParameterException, BusinessException;

    /**
     * 查询未绑定的任务单
     *
     * @Author：wangke
     * @description：
     * @Date：9:09 2018/1/10
     */
    List<DriverContainer> queryDriverContainerList(Integer userKey, DriverContainerSearch search) throws ParameterException, BusinessException;

    /**
     * 保存送货单图片
     * <p>
     *
     * @param uKey
     * @param barcode
     * @param paths
     * @param fromWx  是否要从微信服务器下载
     * @throws ParameterException
     * @throws BusinessException
     */
    void saveDeliveryImage(Integer uKey, String barcode, Collection<String> paths, boolean fromWx) throws ParameterException, BusinessException;


    /**
     * 司机定位
     * @param uKey
     * @param barcode
     * @param driverTrack
     * @throws ParameterException
     * @throws BusinessException
     */
    void saveLoaction(Integer uKey, String barcode, DriverTrack driverTrack) throws ParameterException, BusinessException;

    /**
     * 根据条码查询司机对该条码的定位信息
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-14 14:54:49
     * @param uKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<MergeDriverTrack> listDriverTrack(String barcode) throws ParameterException, BusinessException;
    /**
     * 查询司机的最后一次定位
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-14 14:51:05
     * @param uKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    MergeDriverTrack lastDriverTrack(Integer uKey) throws ParameterException, BusinessException;


    /**
     * 根据条件分页查需订单运输记录
     * @param search 查询条件(search.userKey 不可以为空)
     * @param scope  分页信息
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    CustomPage<OrderAlliance> pageOrderDeliver(OrderSearch search, PageScope scope) throws ParameterException, BusinessException;
    /**
     * 根据条件查需订单运输记录
     * @param search 查询条件(search.userKey 不可以为空)
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<OrderAlliance> listOrderDeliver(OrderSearch search) throws ParameterException, BusinessException;}

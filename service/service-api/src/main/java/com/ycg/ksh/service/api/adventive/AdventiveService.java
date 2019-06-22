package com.ycg.ksh.service.api.adventive;

import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.entity.persistent.adventive.Adventive;
import com.ycg.ksh.entity.persistent.adventive.AdventivePull;
import com.ycg.ksh.service.core.entity.service.adventive.AdventiveAlliance;
import com.ycg.ksh.service.core.entity.service.adventive.AdventiveOrder;
import com.ycg.ksh.service.core.entity.service.adventive.AdventiveReceipt;
import com.ycg.ksh.service.core.entity.service.adventive.AdventiveTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 对外相关服务
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/06/24
 */
public interface AdventiveService {

    final Logger logger = LoggerFactory.getLogger(AdventiveService.class);

    AdventiveAlliance loadAdventiveAlliance(Long adventiveKey) throws ParameterException, BusinessException;
    /**
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<Adventive> list() throws ParameterException, BusinessException;
    /**
     * 新增或者更新
     * @param adventive
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    Adventive saveOrUpdate(Adventive adventive) throws ParameterException, BusinessException;

    /**
     * 新增或者更新
     * @param pull
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    AdventivePull saveOrUpdate(AdventivePull pull) throws ParameterException, BusinessException;
    /**
     * @param key
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    Adventive getAdventive(Long key) throws ParameterException, BusinessException;

    /**
     * 验签
     *
     * @param accessKey
     * @param signVal
     * @param parameters
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    Adventive validate(Long accessKey, String signVal, Map<String, Object> parameters) throws ParameterException, BusinessException;

    /**
     * 强制推送某条记录
     *
     * @param noteKey
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    void enforcePushNote(Long noteKey) throws ParameterException, BusinessException;

    /**
     * 接受外部订单数据
     *
     * @param accessKey
     * @param adventiveOrders
     * @param partner
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    void acceptOrders(Long accessKey, Collection<AdventiveOrder> adventiveOrders, PartnerType partner) throws ParameterException, BusinessException;


    /**
     * 根据订单编号查询订单状态
     * @param accessKey
     * @param orderKeys
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    Map<Long, Integer> listOrderFettleByKeys(Long accessKey, Collection<Long> orderKeys) throws ParameterException, BusinessException;

    /**
     * 根据送货单号查询订单状态
     * @param accessKey
     * @param deliveryNos
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    Map<String, Integer> listOrderFettleByDeliveryNos(Long accessKey, Collection<String> deliveryNos) throws ParameterException, BusinessException;

    /**
     * 根据订单编号查询订单数据
     * @param accessKey
     * @param orderKeys
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<AdventiveOrder>  listOrderByKeys(Long accessKey, Collection<Long> orderKeys) throws ParameterException, BusinessException;

    /**
     * 根据订单号查询订单数据
     * @param accessKey
     * @param orderNo
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    Map<Long, String>  listDeliveryNoByOrderNo(Long accessKey, String orderNo) throws ParameterException, BusinessException;

    /**
     * 根据送货单号查询订单数据
     * @param accessKey
     * @param deliveryNos
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<AdventiveOrder>  listOrderDeliveryNos(Long accessKey, Collection<String> deliveryNos) throws ParameterException, BusinessException;

    /**
     * 根据订单编号查询订单轨迹数据
     * @param accessKey
     * @param orderKey
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<AdventiveTrack> listTrackByKey(Long accessKey, Long orderKey) throws ParameterException, BusinessException;

    Collection<AdventiveTrack> listTrackByDeliveryNo(Long accessKey, String deliveryNo) throws ParameterException, BusinessException;
    /**
     * 查询轨迹信息
     * @param accessKey
     * @param keys
     * @param qlmKey
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    Map<Serializable, Collection<AdventiveTrack>> mapTracks(Long accessKey, Collection<? extends Serializable> keys, boolean qlmKey) throws ParameterException, BusinessException;


    /**
     * 根据订单编号查询回单数据
     * @param accessKey
     * @param orderKey
     *
     * @return
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<AdventiveReceipt> listReceiptByKey(Long accessKey, Long orderKey) throws ParameterException, BusinessException;

    Collection<AdventiveReceipt> listReceiptByDeliveryNo(Long accessKey, String deliveryNo) throws ParameterException, BusinessException;

    public Map<Serializable, Collection<AdventiveReceipt>> mapReceipts(Long accessKey, Collection<? extends Serializable> keys, boolean qlmKey) throws ParameterException, BusinessException;
}
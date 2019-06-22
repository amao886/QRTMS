package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.constant.OrderEventType;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.entity.persistent.Order;
import com.ycg.ksh.entity.persistent.OrderCommodity;
import com.ycg.ksh.entity.persistent.enterprise.AbstractOrder;
import com.ycg.ksh.entity.persistent.enterprise.OrderExtra;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import com.ycg.ksh.entity.service.enterprise.OrderConcise;
import com.ycg.ksh.entity.service.enterprise.OrderSearch;
import com.ycg.ksh.entity.service.enterprise.OrderTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * 订单相关业务逻辑接口
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/12
 */
public interface OrderService {

    Logger logger = LoggerFactory.getLogger(OrderService.class);


    /**
     * 绑定二维码
     *
     * @param uKey     操作人用户编号
     * @param orderKey 订单编号
     * @param qrcode   二维码
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    void bindCode(Integer uKey, Long orderKey, String qrcode) throws ParameterException, BusinessException;

    /**
     * 查询订单明细
     *
     * @param uKey     操作人用户编号
     * @param orderKey 订单编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    Collection<OrderCommodity> listCommodity(Integer uKey, Long orderKey) throws ParameterException, BusinessException;


    /**
     * 保存订单
     *
     * @param uKey      操作人用户编号
     * @param partner   角色
     * @param template  订单信息
     * @param eventType 保存订单事件
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    public void saveOrder(Integer uKey, PartnerType partner, OrderTemplate template, OrderEventType eventType) throws ParameterException, BusinessException;

    /**
     * 修改订单
     *
     * @param uKey     操作人用户编号
     * @param template 订单信息
     * @param partner  角色
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    public void modifyOrder(Integer uKey, PartnerType partner, OrderTemplate template) throws ParameterException, BusinessException;

    /**
     * @param orderKeys
     * @param orderExtra
     * @throws ParameterException
     * @throws BusinessException
     */
    public void modifyOrderExtra(Collection<Long> orderKeys, OrderExtra orderExtra) throws ParameterException, BusinessException;


    /**
     * 应收计算
     *
     * @param orderKey
     * @param orderExtra
     * @throws ParameterException
     * @throws BusinessException
     */
    public void modifyOrderExtra(Collection<OrderExtra> orderExtra) throws ParameterException, BusinessException;

    /**
     * 订单查询
     *
     * @param uKey   操作人编号
     * @param search 查询参数
     * @param scope  分页数据
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    public CustomPage<OrderAlliance> pageOrder(Integer uKey, OrderSearch search, PageScope scope) throws ParameterException, BusinessException;


    /**
     * 查询选中的订单列表
     *
     * @param uKey     操作人用户编号
     * @param orderKey 订单编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @returns
     */
    public Collection<OrderAlliance> electoralOrderList(Integer uKey, Collection<Long> orderKey) throws ParameterException, BusinessException;


    /**
     * 根据订单号查询
     *
     * @param partner
     * @param companyKey
     * @param orderNo
     * @return
     * @throws BusinessException
     */
    public Collection<Order> listByOrderNo(PartnerType partner, Long companyKey, String orderNo) throws BusinessException;

    /**
     * 根据送货单号查询
     *
     * @param partner
     * @param companyKey
     * @param deliveryNos
     * @return
     * @throws BusinessException
     */
    public Collection<Order> listByDeliveryNo(PartnerType partner, Long companyKey, Collection<String> deliveryNos) throws BusinessException;

    /**
     * 批量联盟订单信息
     *
     * @param uKey   用户编号
     * @param orders 订单信息
     * @param flags  要联盟的数据
     * @return
     * @throws BusinessException 逻辑异常
     */
    public Collection<OrderAlliance> alliance(Integer uKey, Collection<? extends AbstractOrder> orders, Integer... flags) throws BusinessException;

    /**
     * 联盟订单信息
     *
     * @param uKey  用户编号
     * @param order 订单信息
     * @param flags 要联盟的数据
     * @return
     * @throws BusinessException 逻辑异常
     */
    public OrderAlliance alliance(Integer uKey, AbstractOrder order, Integer... flags) throws BusinessException;

    /**
     * 按需加载订单详情
     *
     * @param userKey  操作人用户编号
     * @param orderKey 订单编号
     * @param flags    要加的数据标记
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    OrderAlliance alliance(Integer userKey, Long orderKey, Integer... flags) throws ParameterException, BusinessException;

    /**
     * 根据二维码查询订单数据
     *
     * @param qrcode 二维码
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    Order getOrderByCode(String qrcode) throws ParameterException, BusinessException;

    /**
     * 根据订单编号查询订单数据
     *
     * @param orderKey 订单编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    Order getOrderByKey(Long orderKey) throws ParameterException, BusinessException;

    /**
     * 订单详情查询
     *
     * @param userKey 操作人编号
     * @param qrcode  二维码
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @author wangke
     */
    OrderAlliance getAllianceByCode(Integer userKey, String qrcode) throws ParameterException, BusinessException;

    /**
     * 订单详情查询
     *
     * @param userKey   操作人编号
     * @param orderKey  订单编号
     * @param available true:正常的订单,false:作废的
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @author wangke
     */
    OrderAlliance getOrderAlliance(Integer userKey, Long orderKey, boolean available) throws ParameterException, BusinessException;

    /**
     * 生成电子回单
     *
     * @param userKey  操作人用户编号
     * @param orderKey 订单编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    public void buildReceipt(Integer userKey, Long orderKey) throws ParameterException, BusinessException;

    /**
     * 批量生成电子回单
     *
     * @param userKey   操作人用户编号
     * @param orderKeys 订单编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    public int buildReceipt(Integer userKey, Collection<Long> orderKeys) throws ParameterException, BusinessException;

    /**
     * 订单删除
     *
     * @param uKey      操作人用户编号
     * @param orderKeys 要作废的订单编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    void deleteOrders(Integer uKey, PartnerType partnerType, List<Long> orderKeys) throws BusinessException, ParameterException;

    /**
     * 订单作废
     *
     * @param uKey      操作人用户编号
     * @param orderKeys 要作废的订单编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    void invalid(Integer uKey, Collection<Long> orderKeys) throws BusinessException, ParameterException;

    /**
     * 按发货方、收货发、承运方、发货日期分组，分页查询订单简洁信息
     * <p>
     *
     * @param uKey   操作人用户编号
     * @param search 订单查询条件
     * @param scope  分页信息
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-05-15 13:10:08
     */
    public CustomPage<OrderConcise> pageOrderConcise(Integer uKey, OrderSearch search, PageScope scope) throws ParameterException, BusinessException;


    /**
     * 查询订单信息
     *
     * @param uKey        操作人用户编号
     * @param orderSearch 订单查询条件
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    public Collection<Order> listOrder(Integer uKey, OrderSearch orderSearch) throws ParameterException, BusinessException;

    /**
     * @param uKey        操作人用户编号
     * @param orderSearch 订单查询条件
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    public Collection<OrderAlliance> listOrderAlliance(Integer uKey, OrderSearch orderSearch) throws ParameterException, BusinessException;

    /**
     * @param integer 操作人用户编号
     * @param search  订单查询条件
     * @param scope   分页信息
     * @return
     */
    CustomPage<OrderAlliance> pageBindOrder(Integer integer, OrderSearch search, PageScope scope);

    /**
     * @param uKey     操作人用户编号
     * @param orderIds 订单编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    void modifyOrderArrivelStatus(Integer uKey, List<Long> orderIds) throws ParameterException, BusinessException;

    /**
     * @param userId  操作人用户编号
     * @param orderId 订单编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    void modifyOneOrderArrivelStatus(Integer userId, Long orderId) throws ParameterException, BusinessException;

    /**
     * 根据送货编号和送货单号查询唯一订单
     * <p>
     *
     * @param shipperId  货主企业编号
     * @param deliveryNo 送货单号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    Order getOrderBySD(Long shipperId, String deliveryNo) throws ParameterException, BusinessException;


    /**
     * 根据订单编号查询订单额外信息
     *
     * @param orderKey 订单编号
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    OrderExtra getOrderExtraByOrderKey(Long orderKey) throws ParameterException, BusinessException;


    /**
     * 开发请不要使用.给余维测试使用.用于批量绑单推送数据给tms使用
     */
    void bindCodeForTest(Integer uKey, List<Long> orderKeys, List<String> qrcodes);


    /**
     * 修改运单号
     *
     * @param orderKey   订单ID
     * @param deliveryNo 运单编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    void updateDeliveryNo(Long orderKey, String deliveryNo) throws ParameterException, BusinessException;

    /**
     * 评价
     *
     * @param orderKey
     * @param evaluation
     * @throws ParameterException
     * @throws BusinessException
     */
    void modifyEvaluation(Long orderKey, Integer evaluation) throws ParameterException, BusinessException;


    /**
     * 异常签收
     *
     * @param orderKeys
     * @param content
     * @throws ParameterException
     * @throws BusinessException
     */
    void modifyException(Collection<Long> orderKeys, String content, Integer userKey, Integer type) throws ParameterException, BusinessException;
}

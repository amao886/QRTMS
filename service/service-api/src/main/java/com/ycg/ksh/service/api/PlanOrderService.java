package com.ycg.ksh.service.api;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/12
 */

import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.MessageException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.OrderCommodity;
import com.ycg.ksh.entity.persistent.enterprise.CustomData;
import com.ycg.ksh.entity.persistent.enterprise.OrderExtra;
import com.ycg.ksh.entity.persistent.plan.PlanCommodity;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;
import com.ycg.ksh.entity.service.enterprise.OrderTemplate;
import com.ycg.ksh.entity.service.enterprise.TemplateDescribe;
import com.ycg.ksh.entity.service.plan.PlanAlliance;
import com.ycg.ksh.entity.service.plan.PlanSearch;
import com.ycg.ksh.entity.service.plan.PlanTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 发货计划
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/12
 */
public interface PlanOrderService {

    Logger logger = LoggerFactory.getLogger(PlanOrderService.class);

    /**
     * 删除发货计划
     * @param uKey
     * @param planKey
     *
     * @throws ParameterException
     * @throws BusinessException
     */
    void delete(Integer uKey, Long planKey) throws ParameterException, BusinessException;
    /**
     * 联合计划详情
     *
     * @param uKey
     * @param planKey
     * @param flags
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public PlanAlliance alliance(Integer uKey, Long planKey, Integer... flags) throws ParameterException, BusinessException;

    /**
     * 查询计划明细
     *
     * @param uKey    操作人用户编号
     * @param planKey 计划编号
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public Collection<PlanCommodity> listCommodity(Integer uKey, Long planKey) throws ParameterException, BusinessException;

    /**
     * 生成发货单
     *
     * @param uKey
     * @param planKey
     * @param template
     * @param orderExtra
     * @param commodities
     * @param customDatas
     * @param partner
     * @throws ParameterException
     * @throws BusinessException
     */
    public void generate(Integer uKey, Long planKey, OrderTemplate template, OrderExtra orderExtra, Collection<OrderCommodity> commodities, Collection<CustomData> customDatas, PartnerType partner) throws ParameterException, BusinessException;

    /**
     * 生成发货单
     *
     * @param uKey     操作人编号
     * @param partner  操作角色
     * @param planKeys 发货计划编号
     * @throws ParameterException
     * @throws BusinessException
     */
    void generates(Integer uKey, PartnerType partner, Collection<Long> planKeys, Long templateKey) throws ParameterException, BusinessException;


    /**
     * 功能描述: 发货计划查询
     *
     * @param search
     * @param scope
     * @return
     * @auther: wangke
     * @date: 2018/9/13 14:30
     */
    CustomPage<PlanAlliance> pagePlanOrder(PartnerType partner, PlanSearch search, PageScope scope) throws ParameterException, BusinessException;


    /**
     * 功能描述:查询来源下拉框
     *
     * @param companyKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @auther: wangke
     * @date: 2018/9/14 11:50
     */
    Collection<CompanyConcise> pullParameter(Long companyKey) throws ParameterException, BusinessException;


    /**
     * 用发货计划填充模板内容
     *
     * @param uKey        操作用户编号
     * @param planKey     计划编号
     * @param templateKey 模板编号
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<TemplateDescribe> fillTemplateByPlanKey(Integer uKey, Long planKey, Long templateKey) throws ParameterException, BusinessException;

    /**
     * 功能描述: 指派物流商
     *
     * @param userKey       操作用户编号
     * @param planKeys      计划编号
     * @param customerKey   指派的物流商客户编号
     * @param driverName    司机姓名
     * @param driverContact 司机联系方式
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public int designate(Integer userKey, Collection<Long> planKeys, Long customerKey, String driverName, String driverContact) throws ParameterException, BusinessException;

    /**
     * 接单
     *
     * @param uKey
     * @param planKeys
     * @throws ParameterException
     * @throws BusinessException
     */
    public void receive(Integer uKey, Collection<Long> planKeys) throws ParameterException, BusinessException;

    /**
     * 发货单/发货计划修改
     * 功能描述:
     *
     * @param uKey         用户ID
     * @param planTemplate 发货计划单
     * @param orderExtra   额外数据
     * @param commodities  物料数据
     * @param customDatas  自定义字段
     * @throws ParameterException
     * @throws BusinessException
     * @auther: wangke
     * @date: 2018/9/21 13:43
     */
    void modifyPlanOrder(Integer uKey, PlanTemplate planTemplate, PartnerType partnerType, OrderExtra orderExtra, Collection<PlanCommodity> commodities,
                         Collection<CustomData> customDatas) throws MessageException, ParameterException, BusinessException;

    /**
     * 单个/批量派车
     * @param orders
     * @param orderExtra
     * @throws ParameterException
     * @throws BusinessException
     */
    void midifyCarStatus(Collection<Long> orders, OrderExtra orderExtra) throws ParameterException, BusinessException;

}

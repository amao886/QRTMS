package com.ycg.ksh.service.api;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.moutai.Convey;
import com.ycg.ksh.entity.persistent.moutai.Customer;
import com.ycg.ksh.entity.persistent.moutai.Order;
import com.ycg.ksh.entity.persistent.moutai.Taker;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.moutai.MaotaiOrder;
import com.ycg.ksh.entity.service.moutai.MoutaiCustomerSearch;
import com.ycg.ksh.entity.service.moutai.OrderPrint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 茅台打印
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/17
 */
public interface MoutaiService {

    final Logger logger = LoggerFactory.getLogger(MoutaiService.class);

    /**
     * 批量导入订单
     * @param uKey
     * @param conveyKey
     * @param mapData
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public Map<String, String> saveOrders(Integer uKey, Long conveyKey, Map<String, Order> mapData) throws ParameterException, BusinessException;


    /**
     * 批量导入客户信息
     * @param uKey
     * @param mapData
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public Map<String, String> saveCustomers(Integer uKey, Map<String, Customer> mapData) throws ParameterException, BusinessException;

    /**
     * 茅台打印--发货单--查询所有承运单位
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    List<Convey> listMoutaiConvey() throws ParameterException, BusinessException;
    /**
     * 茅台打印--发货单--发货单查询
     * @param search
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    CustomPage<Order> queryOrderList(MaotaiOrder search,PageScope pageScope) throws ParameterException, BusinessException;

    /**
     * 给发货方增加发货仓库
     * @param depot
     * @param ids
     * @throws ParameterException
     * @throws BusinessException
     */
    void addDepot(String depot, List<String> ids)throws ParameterException, BusinessException;

    /**
     * User: wyj
     *     根据多条id查询发货单记录
     */
    Collection<OrderPrint> getOrderPrintDataById(Collection<Long> ids, String username) throws ParameterException, BusinessException,Exception;

    Order searchOneOrderById(Long id)  throws ParameterException, BusinessException;
    /**
     * User: wyj
     * 更新发货单
     */
    void updateOrderById(Order order) throws ParameterException, BusinessException;
    /**
     * User: wyj
     * 批量删除发货单
     */
    void deleteOrderById(List<String> ids) throws ParameterException, BusinessException;

    CustomPage<Customer> queryCustomerList(MoutaiCustomerSearch search, PageScope pageScope);

    Customer searchOneCustomerById(String customerNo);
    /**
     * User: wyj
     * 更新收货客户
     */
    void updateCustomerById(Customer customer);
    /**
     * User: wyj
     * 增加收货客户
     */
    void addCustomer(Customer customer) throws ParameterException, BusinessException;

    void deleteCustomerBycustomerNos(List<String> customerNos);

    CustomPage<Taker> queryTakerList(PageScope pageScope);

    void addTaker(Taker taker) throws ParameterException, BusinessException ;

    Taker searchOneTakerById(Long id);

    void updateTakerById(Taker taker) throws ParameterException, BusinessException ;

    /**
     * User: wyj
     * 修改打印状态
     */
    void updateOrderPrintSign(List<Long> listIds);

    List<Long> checkPrintDataByIds(Collection<Long> ids);

    Collection<String> listMoutaiCustomerKey();

    /**
     * 生成word
     * @param uKey
     * @param orderKeys
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    public FileEntity buildWord(Integer uKey, Collection<Long> orderKeys) throws ParameterException, BusinessException;

}

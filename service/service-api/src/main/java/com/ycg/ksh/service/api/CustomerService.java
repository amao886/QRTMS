/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 14:52:44
 */
package com.ycg.ksh.service.api;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.Company;
import com.ycg.ksh.entity.persistent.Customer;
import com.ycg.ksh.entity.persistent.enterprise.CompanyCustomer;
import com.ycg.ksh.entity.service.AddressSearch;
import com.ycg.ksh.entity.service.MergeCustomer;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.CustomerAddress;
import com.ycg.ksh.entity.service.enterprise.CustomerAlliance;
import com.ycg.ksh.entity.service.enterprise.CustomerConcise;
import com.ycg.ksh.entity.service.enterprise.CustomerSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * TODO 客户业务接口
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 14:52:44
 */
public interface CustomerService {

    final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    int DEFAULT_FENCE_RADIUS = 5;//默认的电子围栏半径

    /**
     * 关联客户查询列表
     * <p>
     *
     * @param search
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 15:51:25
     */
    Collection<Customer> queryCustomer(AddressSearch search) throws ParameterException, BusinessException;

    /**
     * 查询项目组对应的客户列表
     * <p>
     *
     * @param search
     * @return
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 11:47:37
     */
    CustomPage<Customer> pageQueryCustomer(AddressSearch search, PageScope pageScope) throws ParameterException, BusinessException;

    /**
     * 分页查询根据热点排序
     *
     * @param search
     * @param pageScope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    CustomPage<Customer> pageQueryByHotspot(AddressSearch search, PageScope pageScope) throws ParameterException, BusinessException;

    /**
     * 查询项目组对应的客户列表
     * <p>
     *
     * @param search
     * @return
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 11:47:37
     */
    Collection<MergeCustomer> queryMergeCustomer(AddressSearch search) throws ParameterException, BusinessException;

    /**
     * 查询项目组对应的客户列表
     * <p>
     *
     * @param search
     * @return
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 11:47:37
     */
    CustomPage<MergeCustomer> pageQueryMergeCustomer(AddressSearch search, PageScope pageScope) throws ParameterException, BusinessException;

    /**
     * 根据主键查询客户信息
     * <p>
     *
     * @param id 主键
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 16:26:53
     */
    Customer queryByKey(Integer id) throws ParameterException, BusinessException;

    /**
     * TODO 客户信息添加
     * <p>
     *
     * @param belongType 地址所属
     * @param customer   客户实体
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 16:30:13
     */
    void save(Integer belongType, Customer customer) throws ParameterException, BusinessException;

    /**
     * 客户信息修改
     * <p>
     *
     * @param customer
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 16:31:15
     */
    void update(Customer customer) throws ParameterException, BusinessException;
    /**
     *
     * 电子围栏状态修改
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 16:43:43
     * @param customer
     * @param fenceGrate
     * @throws ParameterException
     * @throws BusinessException
     */
    /**
     * 开启\关闭 电子围栏
     * <p>
     *
     * @param customerId 客户编号
     * @param status     电子围栏状态
     * @return
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 11:49:39
     */
    public Integer updateGrate(Integer customerId, Integer status) throws ParameterException, BusinessException;

    /**
     * TODO 删除常用地址
     * <p>
     *
     * @param customer
     * @throws ParameterException
     * @throws BusinessException
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-22 19:05:34
     */
    void delete(Customer customer) throws ParameterException, BusinessException;


    /**************************************************************************************************************************/
    /***************企业客户****************************************************************************************************/
    /**************************************************************************************************************************/
    /**
     * 分页查询企业客户信心
     *
     * @param search
     * @param pageScope
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    CustomPage<Customer> pageCompanyCustomer(AddressSearch search, PageScope pageScope) throws ParameterException, BusinessException;

    /**
     * 新增客户关系
     *
     * @param uKey         操作人用户编号
     * @param customerName 要添加的客户企业名称
     * @param type         客户类型
     * @throws ParameterException 参数错误
     * @throws BusinessException  逻辑错误
     */
    void saveCustomer(Integer uKey, String customerName, Integer type, String scanPhone) throws ParameterException, BusinessException;

    /**
     * 新增客户关系
     *
     * @param uKey       操作人用户编号
     * @param ownerKey
     * @param company
     * @param type       客户类型
     * @param sourceType
     * @throws ParameterException 参数错误
     * @throws BusinessException  逻辑错误
     */
    CompanyCustomer saveCustomer(Integer uKey, Long ownerKey, Company company, Integer type, Integer sourceType)
            throws ParameterException, BusinessException;

    /**
     * 修改客户信息
     *
     * @param uKey
     * @param key
     * @param customerName
     * @param type
     * @throws ParameterException
     * @throws BusinessException
     */
    void editCustomer(Integer uKey, Long key, String customerName, Integer type) throws ParameterException, BusinessException;

    /**
     * 取消客户关系
     *
     * @param uKey        操作人用户编号
     * @param customerKey 要取消关系的编号
     * @throws ParameterException 参数错误
     * @throws BusinessException  逻辑错误
     */
    void cancleRelation(Integer uKey, Long customerKey) throws ParameterException, BusinessException;

    /**
     * 删除客户
     *
     * @param uKey         操作人用户编号
     * @param customerKeys 要删除的关系编号
     * @throws ParameterException 参数错误
     * @throws BusinessException  逻辑错误
     */
    void deleteCustomer(Integer uKey, Collection<Long> customerKeys) throws ParameterException, BusinessException;

    /**
     * 验证是否是客户关系
     *
     * @param ownerKey   当前企业编号
     * @param companyKey 要验证的企业编号
     * @return true:是客户关系，false:不是客户关系
     * @throws ParameterException 参数错误
     * @throws BusinessException  逻辑错误
     */
    boolean isCustomer(Long ownerKey, Long companyKey) throws ParameterException, BusinessException;

    /**
     * 根据客户名称查询客户企业信息
     *
     * @param ownerKey     当前企业编号
     * @param customerName 客户名称
     * @return 企业信息
     * @throws ParameterException 参数错误
     * @throws BusinessException  逻辑错误
     */
    Company loadCompanyByCustomer(Long ownerKey, String customerName) throws ParameterException, BusinessException;

    /**
     * 加载客户信息
     *
     * @param uKey
     * @param ownerKey     所属企业编号
     * @param customerName 客户名称
     * @param customerType
     * @param sourceType
     * @param insert       true:没有找到时新增，false:没有找到部处理
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    CompanyCustomer loadCustomer(Integer uKey, Long ownerKey, String customerName, Integer customerType, Integer sourceType, boolean insert) throws ParameterException, BusinessException;

    /**
     * 分页查询客户关系
     *
     * @param search 查询条件
     * @param scope  分页信息
     * @return 满足条件的企业客户信息
     * @throws ParameterException 参数错误
     * @throws BusinessException  逻辑错误
     */
    CustomPage<CustomerAlliance> pageCustomers(CustomerSearch search, PageScope scope) throws ParameterException, BusinessException;

    /**
     * 根据用户ID和客户名称查询客户ID集合
     *
     * @param companyKey
     * @param likeName
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<Long> listKeyLikeName(Long companyKey, String likeName) throws ParameterException, BusinessException;

    /**
     * 根据用户ID和客户名称查询客户ID集合
     *
     * @param userKey
     * @param likeName
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<Long> listKeyLikeName(Integer userKey, String likeName) throws ParameterException, BusinessException;

    /**
     * @param customerKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    CompanyCustomer getCompanyCustomer(Long customerKey) throws ParameterException, BusinessException;


    CustomerAlliance allianceCompanyCustomer(Integer uKey, Long customerKey) throws ParameterException, BusinessException;

    /**
     * 关联企业
     *
     * @param customerKey
     * @param companyKey
     * @throws ParameterException
     * @throws BusinessException
     */
    void associateCompany(Long customerKey, Long companyKey) throws ParameterException, BusinessException;

    FileEntity buildQrcode(Integer uKey, Long customerKey, String url) throws ParameterException, BusinessException;

    /**
     * 根据员工编号查询员工有权限的客户编号
     *
     * @param employeeKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<Long> listCustomerAuthoritys(Integer employeeKey) throws ParameterException, BusinessException;

    /**
     * 更新员工的客户权限
     *
     * @param uKey
     * @param customerKeys
     * @throws ParameterException
     * @throws BusinessException
     */
    void modifyCustomerAuthoritys(Integer employeeKey, Collection<Long> customerKeys) throws ParameterException, BusinessException;

    /**
     * 收货客户地址保存
     *
     * @param uKey
     * @param customer
     * @throws ParameterException
     * @throws BusinessException
     */
    void saveAddress(Integer uKey, Customer customer) throws ParameterException, BusinessException;

    /**
     * 收货客户地址更新
     *
     * @param uKey
     * @param customer
     * @throws ParameterException
     * @throws BusinessException
     */
    void editAddress(Integer uKey, Customer customer) throws ParameterException, BusinessException;

    /**
     * 收货客户地址删除
     *
     * @param uKey
     * @param ids
     * @throws ParameterException
     * @throws BusinessException
     */
    void deleteAddress(Integer uKey, Collection<Integer> ids) throws ParameterException, BusinessException;

    /**
     * @param uKey
     * @param companyName
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Customer getAddressByCompanyName(Integer uKey, String companyName) throws ParameterException, BusinessException;

    /**
     * 查询企业客户信息(主要用于其他业务选择客户时使用)
     *
     * @param uKey
     * @param likeName
     * @param type     1:发货方,2:收货方,3:承运方;
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<CustomerAlliance> searchCustomerBySomething(CustomerSearch search) throws ParameterException, BusinessException;


    /**
     * @param search
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<CustomerAddress> searchAddressBySomething(AddressSearch search) throws ParameterException, BusinessException;


    /**
     * 根据客户编号查询最新的地址
     *
     * @param search
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    Customer lastAddressByCustomerKey(Long customerKey) throws ParameterException, BusinessException;

    /**
     * 获取客户信息
     *
     * @param customerKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    CustomerConcise loadCustomerConcise(Long customerKey) throws ParameterException, BusinessException;


    CustomerConcise loadCustomerConcise(Long key, boolean reg)throws ParameterException, BusinessException;
}

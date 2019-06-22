package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.constant.CompanyConfigType;
import com.ycg.ksh.constant.OrderRoleType;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.persistent.enterprise.CompanyConfig;
import com.ycg.ksh.entity.service.CompanyBank;
import com.ycg.ksh.entity.service.CompanySearch;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 */
public interface CompanyService {

    Logger logger = LoggerFactory.getLogger(CompanyService.class);


    /**
     * 修改企业名称
     *
     * @param uKey        当前用户
     * @param companyName 企业名称
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    void modifyName(Integer uKey, String companyName) throws ParameterException, BusinessException;

    /**
     * 查询企业简要信息
     *
     * @param key 企业编号
     * @return
     */
    CompanyConcise getCompanyConciseByKey(Long key);

    /**
     * 获取电子回单模板
     *
     * @param ckey 企业编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    CompanyTemplate getTemplateByCompanyByKey(Long ckey) throws ParameterException, BusinessException;


    /**
     * 根据企业编号获取所属公司信息
     *
     * @param ckey 企业编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    Company getCompanyByKey(Long ckey) throws ParameterException, BusinessException;


    /**
     * @param ukey 用户编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    Company assertCompanyByUserKey(Integer ukey) throws ParameterException, BusinessException;

    /**
     * 根据用户编号获取所属公司信息
     *
     * @param ukey 用户编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    Company getCompanyByUserKey(Integer ukey) throws ParameterException, BusinessException;


    /**
     * 根据公司名称获取公司信息
     *
     * @param companyName 企业名称
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    Company getCompanyByName(String companyName) throws ParameterException, BusinessException;


    /**
     * 后台添加企业
     *
     * @param company 企业信息
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    void saveByBackstage(Company company) throws ParameterException, BusinessException;

    /**
     * 添加企业信息
     * <p>
     *
     * @param uKey        用户编号
     * @param company     企业信息
     * @param customerKey 要关联的客户编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    void save(Integer uKey, Company company, Long customerKey) throws ParameterException, BusinessException;

    /**
     * 企业分页查询
     * <p>
     *
     * @param companySearch 企业查询信息
     * @param pageScope     分页信息
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-05-02 16:33:47
     */
    CustomPage<Company> searchPage(CompanySearch companySearch, PageScope pageScope) throws ParameterException, BusinessException;

    /**
     * 获取用户的员工信息
     *
     * @param userId 用户编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    CompanyEmployee getCompanyEmployee(Integer userId) throws ParameterException, BusinessException;

    /**
     * 获取企业管理员
     *
     * @param companyKey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    CompanyEmployee getCompanyAdmin(Long companyKey) throws ParameterException, BusinessException;

    /**
     * 获取公章信息
     *
     * @param comanySealKey 公章编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    CompanySeal getCompanySealByKey(Long comanySealKey) throws ParameterException, BusinessException;

    /**
     * 获取公章信息
     *
     * @param comanyKey 企业编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    @Deprecated
    CompanySeal getComanySealByCompanyKey(Long comanyKey) throws ParameterException, BusinessException;


    /**
     * 查询公司下员工信息
     *
     * @param uKey       用户编号
     * @param companyKey 企业编号
     * @param likeString 企业名称模糊查询字符串
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @author wangke
     */
    Collection<EmployeeAlliance> queryEmployee(Integer uKey, Long companyKey, String likeString) throws ParameterException, BusinessException;

    /**
     * 查询员工权限
     *
     * @param employeeId 员工编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @author wangke
     * @date 2018/6/5 16:31
     */
    EmployeeAlliance getEmployeeAlliance(Integer employeeId) throws ParameterException, BusinessException;

    /**
     * 添加企业员工
     *
     * @param uKey         用户编号
     * @param employeeName 员工名称
     * @param mobile       手机号
     * @param authoritys   系统权限编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @author wangke
     */
    Integer addPersonnel(Integer uKey, String employeeName, String mobile, Collection<Integer> authoritys) throws ParameterException, BusinessException;

    /**
     * 更新员工信息
     *
     * @param uKey         用户编号
     * @param employee     员工信息
     * @param authoritys   系统权限
     * @param customerKeys 客户权限
     * @param sealKeys     公章权限
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @author dingxf
     */
    void modifyEmployee(Integer uKey, CompanyEmployee employee, Collection<Integer> authoritys, Collection<Long> customerKeys, Collection<Long> sealKeys) throws ParameterException, BusinessException;

    /**
     * 更新员工权限信息
     *
     * @param uKey        用户编号
     * @param employeeKey 员工编号
     * @param authoritys  系统权限
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @author dingxf
     */
    void modifyEmployeeAuthoritys(Integer uKey, Integer employeeKey, Collection<Integer> authoritys) throws ParameterException, BusinessException;


    /**
     * 删除企业员工
     *
     * @param uKey        用户编号
     * @param employeeKey 员工编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @author wangke
     */
    void deletePersonnel(Integer uKey, Integer employeeKey) throws ParameterException, BusinessException;

    /**
     * 企业个人签署取消授权
     *
     * @param uKey        用户编号
     * @param employeeKey 员工编号
     * @param seals       公章编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    void unsignatureAuth(Integer uKey, Integer employeeKey, Collection<Long> seals) throws ParameterException, BusinessException;

    /**
     * 企业个人签署授权
     *
     * @param uKey        操作人编号
     * @param employeeKey 员工编号
     * @param seals       印章编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    void signatureAuth(Integer uKey, Integer employeeKey, Collection<Long> seals) throws ParameterException, BusinessException;

    /**
     * 企业签署查询
     *
     * @param companyName 公司名称
     * @param pageScope   分页信息
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    CustomPage<CompanySignedResult> searchCompanySigned(String companyName, PageScope pageScope) throws ParameterException, BusinessException;

    /**
     * 查询所有公司
     *
     * @return
     */
    Collection<Company> listCompany();

    /**
     * 根据公司名称模糊查询
     * <p>
     *
     * @param companyName 企业名称
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    Collection<Company> listCompanyByName(String companyName) throws ParameterException, BusinessException;

    /**
     * 增加签署次数
     *
     * @param id
     * @param presentNum
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    void presentSignedNum(Long id, Integer presentNum) throws ParameterException, BusinessException;

    /**
     * 修改企业信息
     * <p>
     *
     * @param uKey    用户编号
     * @param company 企业信息
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    void updateCompany(Integer uKey, Company company) throws ParameterException, BusinessException;

    /**
     * 添加对公账户信息
     * <p>
     *
     * @param uKey              用户编号
     * @param companyBankVerify 对公打款信息
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    public void saveCompanyBankVerify(Integer uKey, CompanyBankVerify companyBankVerify) throws ParameterException, BusinessException;

    /**
     * 查询企业打款信息
     * <p>
     *
     * @param userKey 用户编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-05-28 14:08:21
     */
    public CompanyBank queryCompanyBank(Integer userKey) throws ParameterException, BusinessException;

    /**
     * 根据公司编号查询企业校验信息
     * <p>
     *
     * @param companyKey 企业编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    public CompanyBankVerify queryByCompanyKey(Integer uKey, Long companyKey) throws ParameterException, BusinessException;

    /**
     * 打款金额认证
     * <p>
     *
     * @param uKey 用户编号
     * @param cash 金额
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    public String verifyCash(Integer uKey, Double cash) throws ParameterException, BusinessException;

    /**
     * 创建公司印章
     *
     * @param companySeal 企业印章信息
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @author wangke
     * @date 2018/6/12 13:24
     */
    void saveCompanySeal(CompanySeal companySeal) throws ParameterException, BusinessException;


    /**
     * 查询公司印章列表
     *
     * @param cKey       企业编号
     * @param uKey       用户编号
     * @param likeString 印章名称模糊查询字符串
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @author wangke
     */
    Collection<CompanySeal> listCompanySeal(Long cKey, Integer uKey, String likeString) throws ParameterException, BusinessException;


    /**
     * 删除公司印章
     *
     * @param uKey    用户编号
     * @param sealKey 公章编号
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     * @author wangke
     */
    void deleteCompanySeal(Integer uKey, Long sealKey) throws ParameterException, BusinessException;

    /**
     * 根据用户ID获取授权印章信息
     *
     * @param userKey 用户编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    Collection<CompanySeal> getCompanySeal(Integer userKey) throws ParameterException, BusinessException;

    /**
     * 根据企业和员工编号查询已授权的公章编号
     *
     * @param companyId  企业编号
     * @param employeeId 员工编号
     * @return
     * @throws ParameterException 参数异常
     * @throws BusinessException  逻辑异常
     */
    Collection<Long> listCompanySealKeys(Long companyId, Integer employeeId) throws ParameterException, BusinessException;

    /**
     * 功能描述: 发货通知设置
     *
     * @param uKey
     * @param configs
     * @throws ParameterException
     * @throws BusinessException
     */
    void modifyConfig(Integer uKey, Collection<CompanyConfig> configs) throws ParameterException, BusinessException;

    /**
     * 查询企业配置信息
     *
     * @param uKey
     * @throws ParameterException
     * @throws BusinessException
     */
    Collection<CompanyConfig> listConfig(Integer uKey) throws ParameterException, BusinessException;

    CompanyConfig  loadCompanyConfig(Long companyKey, CompanyConfigType configType) throws ParameterException, BusinessException;
    /**
     * 跟踪设置
     *
     * @param frequency
     * @throws ParameterException
     * @throws BusinessException
     */
    void trackingSetting(Integer uKey, String frequency) throws ParameterException, BusinessException;
}

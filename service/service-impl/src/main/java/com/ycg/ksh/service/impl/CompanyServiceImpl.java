package com.ycg.ksh.service.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/12
 */

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.ycg.ksh.adapter.api.AuthenticateService;
import com.ycg.ksh.adapter.api.ESignService;
import com.ycg.ksh.adapter.api.MessageQueueService;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.extend.mybatis.CustomExample;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.extend.rabbitmq.MediaMessage;
import com.ycg.ksh.common.extend.rabbitmq.RabbitMessageListener;
import com.ycg.ksh.common.system.FrontUtils;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SecurityTokenUtil;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.*;
import com.ycg.ksh.common.util.encrypt.MD5;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.constant.*;
import com.ycg.ksh.entity.adapter.esign.Enterprise;
import com.ycg.ksh.entity.adapter.esign.SealMoulage;
import com.ycg.ksh.entity.adapter.esign.Signer;
import com.ycg.ksh.entity.adapter.wechat.TemplateDataValue;
import com.ycg.ksh.entity.adapter.wechat.TemplateMesssage;
import com.ycg.ksh.entity.adapter.wechat.TemplateType;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.persistent.enterprise.CompanyConfig;
import com.ycg.ksh.entity.persistent.enterprise.CompanyCustomer;
import com.ycg.ksh.entity.persistent.enterprise.EmployeeCustomer;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.entity.service.barcode.BarcodeContext;
import com.ycg.ksh.entity.service.barcode.CompanyCodeContext;
import com.ycg.ksh.entity.service.enterprise.*;
import com.ycg.ksh.entity.service.esign.CompanySigner;
import com.ycg.ksh.entity.service.esign.ReceiptSignature;
import com.ycg.ksh.entity.service.plan.PlanAlliance;
import com.ycg.ksh.service.persistence.*;
import com.ycg.ksh.service.persistence.enterprise.CompanyConfigMapper;
import com.ycg.ksh.service.persistence.enterprise.EmployeeCustomerMapper;
import com.ycg.ksh.service.api.*;
import com.ycg.ksh.service.observer.*;
import com.ycg.ksh.service.support.assist.DrawSeal;
import com.ycg.ksh.service.util.O;
import com.ycg.ksh.service.util.P;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 企业相关业务逻辑实现
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/12
 */
@Service("ksh.core.service.companyService")
public class CompanyServiceImpl implements CompanyService, RabbitMessageListener, BarcodeObserverAdapter, OrderObserverAdapter, UserObserverAdapter, ReceiptObserverAdapter, PlanOrderObserverAdapter {

    @Resource
    CompanyMapper companyMapper;
    @Resource
    CompanyTemplateMapper templateMapper;
    @Resource
    CompanyEmployeeMapper employeeMapper;
    @Resource
    CompanySealMapper sealMapper;
    @Resource
    OrderSignatureMapper signatureMapper;
    @Resource
    CacheManager cacheManager;
    @Resource
    MessageQueueService queueService;
    @Resource
    CompanySignedMapper signedMapper;
    @Resource
    UserService userService;
    @Resource
    PermissionService permissionService;
    @Resource
    UserLegalizeMapper legalizeMapper;

    @Resource
    AuthenticateService authenticateService;
    @Resource
    ESignService esignService;
    @Resource
    SupportService supportService;
    @Resource
    CustomerService customerService;

    @Resource
    CompanyBankVerifyMapper companyBankVerifyMapper;
    @Resource
    EmployeeAuthorityMapper employeeAuthorityMapper;
    @Resource
    EmployeeSealMapper employeeSealMapper;
    @Resource
    EmployeeCustomerMapper employeeCustomerMapper;
    @Resource
    CompanyConfigMapper companyConfigMapper;

    @Autowired(required = false)
    Collection<CompanyObserverAdapter> observerAdapters;

    private void updateCompany(Company company) {
        companyMapper.updateByPrimaryKeySelective(company);
    }

    /**
     * 查询企业简要信息
     *
     * @param key
     * @return
     */
    @Override
    public CompanyConcise getCompanyConciseByKey(Long key) {
        try {
            return companyMapper.getCompanyConciseByKey(key);
        } catch (Exception e) {
            logger.error("查询企业简要信息异常 {}", key, e);
            throw new BusinessException("查询企业简要信息异常");
        }
    }

    /**
     * 获取电子回单模板
     *
     * @param ckey
     * @return
     */
    @Override
    public CompanyTemplate getTemplateByCompanyByKey(Long ckey) throws ParameterException, BusinessException {
        Assert.notBlank(ckey, "公司编号不能为空");
        return Optional.ofNullable(templateMapper.getByCompanyKey(ckey)).orElseGet(() -> {
            return defaultCompanyTemplate();
        });
    }

    private CompanyTemplate defaultCompanyTemplate() throws ParameterException, BusinessException {
        try {
            return templateMapper.getByCompanyKey(0L);//companyTemplateCache.get(0L);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("查询默认模板异常", e);
            throw new BusinessException("加载默认模板异常");
        }
    }


    private boolean isCompanyEmployee(Long companyKey, Integer uKey, EmployeeType employeeType) throws ParameterException, BusinessException {
        try {
            CompanyEmployee employee = getCompanyEmployee(uKey);
            if (employee != null && employee.getCompanyId() - companyKey == 0) {
                EmployeeType type = EmployeeType.convert(employee.getEmployeeType());
                if (employeeType == EmployeeType.MANAGE) {
                    return type.isManage();
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("检查企业人员异常 {} {}", companyKey, uKey, e);
            throw new BusinessException("检查企业人员异常");
        }
    }

    /**
     * 根据企业编号获取所属公司信息
     *
     * @param ckey
     * @return
     */
    @Override
    public Company getCompanyByKey(Long ckey) throws ParameterException, BusinessException {
        Assert.notBlank(ckey, "公司编号不能为空");
        return Optional.ofNullable(companyMapper.selectByPrimaryKey(ckey)).orElseThrow(() -> {
            return new BusinessException("企业信息不存在");
        });
    }

    /**
     * @param ukey
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Company assertCompanyByUserKey(Integer ukey) throws ParameterException, BusinessException {
        return Optional.ofNullable(getCompanyByUserKey(ukey)).orElseThrow(() -> {
            return new BusinessException("当前用户没有所属企业");
        });
    }

    /**
     * 根据用户编号获取所属公司信息
     *
     * @param ukey
     * @return
     */
    @Override
    public Company getCompanyByUserKey(Integer ukey) throws ParameterException, BusinessException {
        Assert.notBlank(ukey, "用户编号不能为空");
        try {
            return companyMapper.getCompanyByUserKey(ukey);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("根据用户编号加载其公司信息异常 {}", ukey, e);
            throw new BusinessException("根据用户编号加载其公司信息异常");
        }
    }

    /**
     * 根据公司名称获取公司信息
     *
     * @param companyName
     * @return
     */
    @Override
    public Company getCompanyByName(String companyName) throws ParameterException, BusinessException {
        Assert.notBlank(companyName, "企业名称不能为空");
        try {
            return companyMapper.getCompanyByName(companyName);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("根据企业名称加载公司信息异常 {}", companyName, e);
            throw new BusinessException("根据企业名称加载公司信息异常");
        }
    }

    /**
     * 获取用户的员工信息
     *
     * @param userId
     * @return
     */
    @Override
    public CompanyEmployee getCompanyEmployee(Integer userId) throws ParameterException, BusinessException {
        Assert.notBlank(userId, "用户编号不能为空");
        try {
            return employeeMapper.selectByPrimaryKey(userId);
        } catch (Exception e) {
            logger.error("根据用户编号加载器员工信息异常 {}", userId, e);
            throw new BusinessException("根据用户编号加载器员工信息异常");
        }
    }

    @Override
    public CompanyEmployee getCompanyAdmin(Long companyKey) throws ParameterException, BusinessException {
        Assert.notBlank(companyKey, "企业编号不能为空");
        try {
            return employeeMapper.firstAdminCompany(companyKey);
        } catch (Exception e) {
            logger.error("根据企业编号查询管理员信息异常 {}", companyKey, e);
            throw new BusinessException("根据企业编号查询管理员信息异常");
        }
    }

    /**
     * 获取公章信息
     *
     * @param comanySealKey
     * @return
     */
    @Override
    public CompanySeal getCompanySealByKey(Long comanySealKey) throws ParameterException, BusinessException {
        try {
            return sealMapper.selectByPrimaryKey(comanySealKey);
        } catch (Exception e) {
            logger.error("根据公章编号获取公章信息异常 {}", comanySealKey, e);
            throw new BusinessException("根据公章编号获取公章信息异常");
        }
    }

    @Override
    public CompanySeal getComanySealByCompanyKey(Long comanyKey) throws ParameterException, BusinessException {
        try {
            return sealMapper.getCompanySealByCompanyKey(comanyKey);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("根据企业编号获取公章信息异常 {}", comanyKey, e);
            throw new BusinessException("根据企业编号获取公章信息异常");
        }
    }

    /**
     * 后台添加企业
     *
     * @param company
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void saveByBackstage(Company company) throws ParameterException, BusinessException {
        Assert.notNull(company, "企业信息不能为空");
        company.setCompanyName(StringUtils.trim(company.getCompanyName()));
        Assert.notBlank(company.getCompanyName(), "企业名称不能为空");
        Assert.notBlank(company.getLegalName(), "企业法人姓名不能为空");
        Assert.notBlank(company.getCodeOrg(), company.getCodeUsc(), "组织机构代码和社会统一信用代码必须填写一个");
        try {
            if (null != getCompanyByName(company.getCompanyName())) {
                throw new BusinessException("该企业已存在!!!");
            }
            company.setId(Globallys.nextKey());
            company.setCreateTime(new Date());
            company.setUpdateTime(company.getCreateTime());
            company.setUserId(0);
            companyMapper.insertSelective(company);
            //临时做法生成印章
            buildSeal(company);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("后台添加企业异常 {}", company, e);
            throw new BusinessException("后台添加企业异常");
        }
    }

    private boolean containsCompanyName(Long companyKey, String companyName) {
        Example example = new Example(CompanyCustomer.class);
        example.createCriteria()
                .andEqualTo("name", companyName)
                .andEqualTo("ownerKey", companyKey);
        return CollectionUtils.isEmpty(employeeMapper.selectByExample(example));
    }

    /**
     * @param uKey
     * @param company
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void save(Integer uKey, Company company, Long customerKey) throws ParameterException, BusinessException {
        Assert.notNull(uKey, "操作人不能为空");
        Assert.notNull(company, "企业信息不能为空");
        Assert.notBlank(company.getCompanyName(), "企业名称不能为空");
        //Assert.notBlank(company.getLegalName(), "企业法人姓名不能为空");
        //Assert.notBlank(company.getCodeOrg(), company.getCodeUsc(), "组织机构代码和社会统一信用代码必须填写一个");
        try {
            if (null != getCompanyByName(company.getCompanyName())) {
                throw new BusinessException("该企业已存在!!!");
            }
            CompanyEmployee employee = getCompanyEmployee(uKey);
            if (employee != null) {
                throw new BusinessException("当前用户已经有所属企业,不可重复操作");
            }
            User user = userService.getUserByKey(uKey);
            String uname = user.getUnamezn();
            Validator validator = Validator.CHINESENAME;
            if (!validator.verify(uname)) {
                throw new BusinessException("当前用户名称不合要求,请修改为真实姓名后操作");
            }
            company.setId(Globallys.nextKey());
            company.setCreateTime(new Date());
            company.setUpdateTime(company.getCreateTime());
            company.setLegalName(Optional.ofNullable(company.getLegalName()).orElse(""));//法人姓名默认为空
            company.setLegalIdNo(Optional.ofNullable(company.getLegalIdNo()).orElse(""));
            companyMapper.insertSelective(company);

            //设置当前用户为管理员
            employee = new CompanyEmployee();
            employee.setCompanyId(company.getId());
            employee.setCreateTime(company.getCreateTime());
            employee.setEmployeeId(user.getId());
            employee.setEmployeeType(EmployeeType.MANAGE.getCode());//管理员
            employee.setUserFettle(1);//启用
            employee.setUserId(user.getId());
            employee.setEmployeeName(user.getUnamezn());

            employeeMapper.insert(employee);
            if (CollectionUtils.isNotEmpty(observerAdapters)) {
                for (CompanyObserverAdapter observerAdapter : observerAdapters) {
                    observerAdapter.notifyCompanyRegister(uKey, company.getId(), customerKey);
                }
            }
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("添加企业异常 {} {}", company, uKey, e);
            throw new BusinessException("添加企业异常", e);
        }
    }

    private void buildSeal(Company company) {
        CompanySeal companySeal = sealMapper.getCompanySealByCompanyKey(company.getId());
        if (companySeal == null) {
            try {
                BufferedImage bufferedImage = ImageUtils.createSeal(company.getCompanyName(), "签署测试章", null, 400, 400);
                FileEntity fileEntity = new FileEntity();
                fileEntity.setDirectory(SystemUtils.fileRootPath());
                fileEntity.setSubDir(Directory.SEAL.getDir());
                fileEntity.setSuffix("png");
                fileEntity.setFileName(FileUtils.appendSuffix(MD5.encrypt(company.getCompanyName()), fileEntity.getSuffix()));
                ImageIO.write(bufferedImage, "PNG", new File(fileEntity.getPath()));

                companySeal = new CompanySeal();
                companySeal.setId(Globallys.nextKey());
                companySeal.setCompanyId(company.getId());
                companySeal.setSealData(FileUtils.path(fileEntity.getSubDir(), fileEntity.getFileName()));
                companySeal.setUserId(company.getUserId());
                companySeal.setCreateTime(company.getUpdateTime());
                companySeal.setUpdateTime(company.getUpdateTime());
                companySeal.setFettle(1);

                sealMapper.insertSelective(companySeal);
            } catch (Exception e) {
                logger.error("企业印章生成异常", e);
            }
        }
    }

    /**
     * 企业分页查询
     *
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-05-03 11:18:06
     * @see com.ycg.ksh.service.api.CompanyService#searchPage(CompanySearch, PageScope)
     * <p>
     */
    @Override
    public CustomPage<Company> searchPage(CompanySearch companySearch, PageScope pageScope)
            throws ParameterException, BusinessException {
        try {
            Page<Company> page = companyMapper.queryPage(companySearch, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
            return new CustomPage<>(page.getPageNum(), page.getPageSize(), page.getTotal(), page.getResult());
        } catch (Exception e) {
            logger.error("searchPage()===> companySearch:{},pageScope:{},e:{}", companySearch, pageScope, e);
            throw BusinessException.dbException("企业信息查询异常");
        }
    }


    @Override
    public Collection<EmployeeAlliance> queryEmployee(Integer uKey, Long companyKey, String likeString) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人编号不能为空");
        Assert.notBlank(companyKey, "公司ID为空");
        CustomExample example = new CustomExample(CompanyEmployee.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("companyId", companyKey);
        likeString = StringUtils.trim(likeString);
        if (StringUtils.isNotBlank(likeString)) {
            criteria.andLike("employeeName", likeString);
        }
        example.orderBy("createTime").desc();
        return Optional.ofNullable(employeeMapper.selectByExample(example)).map(es -> {
            return es.stream().map(e -> alliance(e)).collect(Collectors.toList());
        }).orElse(Collections.emptyList());
    }

    private EmployeeAlliance alliance(CompanyEmployee employee) {
        EmployeeAlliance alliance = new EmployeeAlliance(employee);
        ConciseUser user = userService.getConciseUser(employee.getEmployeeId());
        if (user != null) {
            alliance.setUnamezn(user.getUnamezn());
            alliance.setMobilephone(user.getMobilephone());
            alliance.setHeadImg(user.getHeadImg());
        }
        UserLegalize legalize = userService.getUserLegalize(employee.getEmployeeId());
        if (legalize != null) {
            alliance.setBrankCardNo(legalize.getBrankCardNo());
            alliance.setFettle(legalize.getFettle());
            alliance.setIdCardNo(legalize.getIdCardNo());
            alliance.setLegalizeTime(legalize.getLegalizeTime());
            alliance.setName(legalize.getName());
        }
        return alliance;
    }

    @Override
    public EmployeeAlliance getEmployeeAlliance(Integer employeeId) throws ParameterException, BusinessException {
        Assert.notBlank(employeeId, "员工ID为空");
        CompanyEmployee employee = getCompanyEmployee(employeeId);
        if (null == employee) {
            throw new BusinessException("未找到该员工信息");
        }
        try {
            return alliance(employee);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("查询员工信息异常 {} {} {}", employeeId, e);
            throw new BusinessException("查询员工信息异常");
        }
    }

    @Override
    public Integer addPersonnel(Integer uKey, String employeeName, String mobile, Collection<Integer> authoritys) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人用户编号不能为空");
        //判断管理员是否加入公司
        CompanyEmployee maneger = getCompanyEmployee(uKey);
        if (maneger == null) {
            throw new BusinessException("当前用户尚未加入公司");
        }
        /*if (!EmployeeType.convert(maneger.getEmployeeType()).isManage()) {
            throw new BusinessException("非公司管理员不能进行该操作");
        }*/
        Validator validator = Validator.MOBILE;
        if (!validator.verify(mobile)) {
            throw new ParameterException(mobile, validator.getMessage("员工手机号"));
        }
        User user = userService.getUserByMobile(mobile);
        if (null == user || null == user.getId()) {
            throw new BusinessException("当前手机号没有绑定用户");
        }
        validator = Validator.CHINESENAME;
        if (!validator.verify(employeeName)) {
            throw new ParameterException(employeeName, validator.getMessage("员工姓名"));
        }
        CompanyEmployee employee = getCompanyEmployee(user.getId());
        if (employee != null) {
            throw new BusinessException("该用户已经加入企业");
        }
        employee = new CompanyEmployee(maneger.getCompanyId(), user.getId());
        employee.setEmployeeName(employeeName);
        employee.setUserId(uKey);
        employee.setCreateTime(new Date());
        employee.setUserFettle(EmployeeFettle.ENABLE.getCode());//启用
        employee.setEmployeeType(EmployeeType.COMMON.getCode());//普通员工
        employeeMapper.insertSelective(employee);
        if (CollectionUtils.isNotEmpty(authoritys)) {
            permissionService.saveAuthoritys(uKey, maneger.getCompanyId(), user.getId(), authoritys);
        }
        return employee.getEmployeeId();
    }

    /**
     * 更新员工信息
     *
     * @param uKey
     * @param employee
     * @param authoritys   系统权限
     * @param customerKeys 客户权限
     * @param sealKeys     公章权限
     * @throws ParameterException
     * @throws BusinessException
     * @author dingxf
     */
    @Override
    public void modifyEmployee(Integer uKey, CompanyEmployee employee, Collection<Integer> authoritys, Collection<Long> customerKeys, Collection<Long> sealKeys) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人编号为空");
        try {
            CompanyEmployee manager = getCompanyEmployee(uKey);
            if (manager == null) {
                throw new BusinessException("当前用户不是企业员工不能操作");
            }
            Assert.notNull(employee, "员工信息不能为空");
            Assert.notBlank(employee.getEmployeeId(), "员工编号不能为空");
            CompanyEmployee exister = getCompanyEmployee(employee.getEmployeeId());
            if (exister == null) {
                throw new BusinessException("该员工已经不在当前企业");
            }
            if (manager.getCompanyId() - exister.getCompanyId() != 0) {
                throw new BusinessException("对方不是当前用户所属公司员工");
            }
            if (StringUtils.isNotBlank(employee.getEmployeeName())) {
                Validator validator = Validator.CHINESENAME;
                if (!validator.verify(employee.getEmployeeName())) {
                    throw new ParameterException(employee.getEmployeeName(), validator.getMessage("员工姓名"));
                }
            }
            if (employee.getUserFettle() != null) {
                employee.setUserFettle(EmployeeFettle.convert(employee.getUserFettle()).getCode());
            }
            employeeMapper.updateByPrimaryKeySelective(employee);
            if (authoritys != null) {
                permissionService.saveAuthoritys(uKey, exister.getCompanyId(), exister.getEmployeeId(), authoritys);
            }
            if (customerKeys != null) {
                customerService.modifyCustomerAuthoritys(exister.getEmployeeId(), customerKeys);
            }
            if (sealKeys != null) {
                sealAuth(exister, sealKeys);
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("更新员工信息异常");
        }
    }

    /**
     * 更新员工权限信息
     *
     * @param uKey
     * @param employeeKey
     * @param authoritys  系统权限
     * @throws ParameterException
     * @throws BusinessException
     * @author dingxf
     */
    @Override
    public void modifyEmployeeAuthoritys(Integer uKey, Integer employeeKey, Collection<Integer> authoritys) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人编号为空");
        try {
            CompanyEmployee manager = getCompanyEmployee(uKey);
            if (manager == null) {
                throw new BusinessException("当前用户不是企业员工不能操作");
            }
            Assert.notBlank(employeeKey, "员工编号不能为空");
            CompanyEmployee exister = getCompanyEmployee(employeeKey);
            if (exister == null) {
                throw new BusinessException("该员工已经不在当前企业");
            }
            if (manager.getCompanyId() - exister.getCompanyId() != 0) {
                throw new BusinessException("对方不是当前用户所属公司员工");
            }
            if (authoritys != null) {
                permissionService.saveAuthoritys(uKey, exister.getCompanyId(), exister.getEmployeeId(), authoritys);
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("更新员工系统权限异常");
        }
    }

    @Override
    public boolean handleMessage(String messageType, String messageKey, Object object) throws BusinessException {
        if (StringUtils.equalsIgnoreCase(messageType, EmployeeCustomer.class.getName())) {
            try {
                EmployeeCustomer customer = (EmployeeCustomer) object;
                CompanyEmployee employee = getCompanyEmployee(customer.getEmployeeKey());
                if (employee != null && EmployeeType.convert(employee.getEmployeeType()).isCommon()) {
                    if (employeeCustomerMapper.selectCount(customer) <= 0) {
                        customer.setId(Globallys.nextKey());
                        employeeCustomerMapper.insert(customer);
                    }
                }
                return true;
            } catch (Exception ex) {
                throw new BusinessException("处理员工客户权限消息异常", ex);
            }
        }
        return false;
    }

    @Override
    public void deletePersonnel(Integer uKey, Integer employeeKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作用户编号不能为空");
        Assert.notBlank(employeeKey, "员工编号不能为空");
        try {
            CompanyEmployee manager = getCompanyEmployee(uKey);
            if (manager == null || !EmployeeType.convert(manager.getEmployeeType()).isManage()) {
                throw new BusinessException("当前用户不是企业管理员不能操作");
            }
            CompanyEmployee employee = getCompanyEmployee(employeeKey);
            if (employee == null || employee.getCompanyId() - manager.getCompanyId() != 0) {
                throw new BusinessException("该员工不是当前用户所属企业员工");
            }
            relieveRelation(employee);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("删除企业人员异常 {} {}", uKey, employeeKey, e);
            throw new BusinessException("删除企业人员异常");
        }
    }

    public void relieveRelation(CompanyEmployee employee) {
        //删除企业和用户关联
        employeeMapper.deleteByPrimaryKey(employee.getEmployeeId());

        //个人和印章关联
        //清除员工公章授权表(T_EMPLOYEE_SEAL)
        EmployeeSeal employeeSeal = new EmployeeSeal(employee.getCompanyId(), employee.getEmployeeId());
        employeeSealMapper.delete(employeeSeal);

        //清除员工权限授权表(T_EMPLOYEE_AUTHORITY)
        EmployeeAuthority employeeAuthority = new EmployeeAuthority(employee.getCompanyId(), employee.getEmployeeId());
        employeeAuthorityMapper.delete(employeeAuthority);

        //清除员工客户授权表(T_EMPLOYEE_CUSTOMER)
        EmployeeCustomer employeeCustomer = new EmployeeCustomer(null, employee.getEmployeeId(), null);
        employeeCustomerMapper.delete(employeeCustomer);
    }

    private void deleteEmployeeSeals(Integer employeeId, Collection<Long> sealKeys) {
        if (CollectionUtils.isEmpty(sealKeys)) {
            return;
        }
        Example example = new Example(EmployeeSeal.class);
        example.createCriteria().andEqualTo("employeeId", employeeId).andIn("authorizeSealId", sealKeys);
        employeeSealMapper.deleteByExample(example);
    }

    public void unsignatureAuth(Integer uKey, Integer employeeKey, Collection<Long> seals) throws ParameterException, BusinessException {
        if (CollectionUtils.isEmpty(seals)) {
            return;
        }
        Assert.notBlank(uKey, "操作用户编号不能为空");
        Assert.notBlank(employeeKey, "员工编号不能为空");
        try {
            CompanyEmployee employee = getCompanyEmployee(employeeKey);
            if (employee == null || !isCompanyEmployee(employee.getCompanyId(), uKey, EmployeeType.MANAGE)) {
                throw new BusinessException("不是该员工所属公司管理员,不能取消授权");
            }
            deleteEmployeeSeals(employeeKey, seals);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("取消授权异常 {} {}", uKey, employeeKey, seals, e);
            throw new BusinessException("取消授权异常");
        }
    }

    @Override
    public void signatureAuth(Integer uKey, Integer employeeKey, Collection<Long> seals) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作用户编号不能为空");
        Assert.notBlank(employeeKey, "员工编号不能为空");
        Assert.notEmpty(seals, "至少选择一个印章");
        try {
            CompanyEmployee employee = getCompanyEmployee(employeeKey);
            if (employee == null || !isCompanyEmployee(employee.getCompanyId(), uKey, EmployeeType.MANAGE)) {
                throw new BusinessException("不是该员工所属公司管理员,不能授权");
            }
            sealAuth(employee, seals);
        } catch (BusinessException | ParameterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("开通授权异常 {} {}", uKey, employeeKey, seals, e);
            throw new BusinessException("开通授权异常");
        }
    }

    private void sealAuth(CompanyEmployee employee, Collection<Long> seals) {
        Collection<Long> deleteKeys = new ArrayList<Long>();
        Collection<EmployeeSeal> existers = employeeSealMapper.select(new EmployeeSeal(employee.getCompanyId(), employee.getEmployeeId()));
        if (CollectionUtils.isNotEmpty(existers)) {
            existers.forEach(e -> {
                if (!seals.contains(e.getAuthorizeSealId())) {
                    deleteKeys.add(e.getAuthorizeSealId());
                } else {
                    seals.remove(e.getAuthorizeSealId());
                }
            });
        }
        Date date = new Date();
        for (Long sealKey : seals) {
            if (null != sealMapper.selectByPrimaryKey(sealKey)) {
                employeeSealMapper.insertSelective(new EmployeeSeal(Globallys.nextKey(), employee.getCompanyId(), employee.getEmployeeId(), sealKey, date));
            }
        }
        if (CollectionUtils.isNotEmpty(deleteKeys)) {
            deleteEmployeeSeals(employee.getEmployeeId(), deleteKeys);
        }
        try {
            if (!userService.isLegalize(employee.getEmployeeId())) {//没有实名认证
                User user = userService.getUserByKey(employee.getEmployeeId());
                if (StringUtils.isNotBlank(user.getUsername())) {
                    Company company = getCompanyByKey(employee.getCompanyId());
                    TemplateMesssage messsage = new TemplateMesssage(user.getUsername(), TemplateType.LEGALIZE);
                    messsage.addData("first", new TemplateDataValue("您被" + company.getCompanyName() + "授权为签章人员", "#173177"));
                    messsage.addData("keyword1", new TemplateDataValue(company.getCompanyName(), "#173177"));
                    messsage.addData("keyword2", new TemplateDataValue(DateUtils.formatDate(date, "yyyy年MM月dd日 HH点mm分"), "#173177"));
                    messsage.addData("remark", new TemplateDataValue("请尽快完成实名认证,感谢使用合同物流管理平台平台", "#173177"));
                    messsage.setUrl(FrontUtils.personalAuth(SecurityTokenUtil.createToken(user.getId().toString()), user.getId()));

                    queueService.sendNetMessage(new MediaMessage(Globallys.UUID(), messsage));
                }
            }
        } catch (Exception e) {
            logger.error("公章授权发送模板消息异常", e);
        }
    }

    @Override
    public Collection<Company> listCompany() {
        return companyMapper.selectAll();
    }


    @Override
    public CustomPage<CompanySignedResult> searchCompanySigned(String companyName, PageScope pageScope) {
        if (pageScope == null) {
            pageScope = PageScope.DEFAULT;
        }
        CustomExample example = new CustomExample(Company.class);
        example.createCriteria().andLike("companyName", companyName);
        Map<Long, String> companyMap = companyMapper.selectByExample(example).
                parallelStream().collect(Collectors.toMap(company -> company.getId(), company -> company.getCompanyName()));

        if (companyMap.size() > 0) {
            CustomExample customExample = new CustomExample(CompanySigned.class);
            customExample.createCriteria().andIn("companyId", companyMap.values());
            Page<CompanySigned> page = signedMapper.selectPageByExample(customExample, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
            Collection<CompanySignedResult> result = Lists.newArrayList();
            for (CompanySigned signed : page) {
                result.add(new CompanySignedResult(companyMap.get(signed.getCompanyId()), signed));
            }
            return new CustomPage<CompanySignedResult>(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
        }
        return null;

    }

    @Override
    public void presentSignedNum(Long id, Integer presentNum) {
        Assert.notBlank(id, "id不能为空");
        signedMapper.updateCompanySignNum(id, presentNum);
    }

    @Override
    public void updateCompany(Integer uKey, Company company) throws ParameterException, BusinessException {

        Assert.notNull(company, "企业信息不能为空");
        Assert.notBlank(company.getCompanyName(), "企业名称不能为空");
        Assert.notBlank(company.getLegalName(), "企业法人姓名不能为空");
        Assert.notBlank(company.getCodeOrg(), company.getCodeUsc(), "组织机构代码和社会统一信用代码必须填写一个");

        UserLegalize legalize = userService.getUserLegalize(uKey);
        if (legalize == null || LegalizeFettle.convert(legalize.getFettle()).isVerify()) {
            throw new BusinessException("当前用户尚未通过实名认证");
        }
        CompanyEmployee manager = getCompanyEmployee(uKey);
        if (manager == null || !EmployeeType.convert(manager.getEmployeeType()).isManage() || manager.getCompanyId() - company.getId() != 0) {
            throw new BusinessException("当前用户不是企业管理员不能操作");
        }
        Validator validator = Validator.IDCARD;
        if (StringUtils.isNotBlank(company.getLegalIdNo()) && !validator.verify(company.getLegalIdNo())) {
            throw new BusinessException(validator.getMessage("证件号码"));
        }
        Company exister = getCompanyByKey(company.getId());
        if (exister == null) {
            throw new BusinessException("获取企业信息异常");
        }
        if (!company.getCompanyName().equals(exister.getCompanyName())) {
            if (null != getCompanyByName(company.getCompanyName()))
                throw new BusinessException("该企业已存在!!!");
        }
        company.setFettle(legalize(company, null, legalize).getCode());
        company.setSignFettle(CompanySignFettle.NOTOPEN.getCode());
        company.setUpdateTime(new Date());
        updateCompany(company);
        //modifyCompanySeal(uKey, company.getId(), sealPath);
    }

    private CompanyFettle legalize(Company company, CompanyBankVerify verify, UserLegalize legalize) {
        CompanyFettle fettle = CompanyFettle.convert(company.getFettle());
        if (fettle.isRegistered()) {
            Enterprise enterprise = Enterprise.base(company.getCompanyName(), company.getCodeOrg(), company.getCodeUsc(), company.getLegalName(), company.getLegalIdNo());
            if (SystemUtils.esignEnable()) {
                authenticateService.legalize(enterprise);
            }
            fettle = CompanyFettle.BASE;
            supportService.saveOperateNote(legalize.getId(), OperateType.ESIGN, company.getId(), ESignEventType.ENTERPRISEBASE, enterprise.toString());
        }
        if (verify != null) {//对公打款校验
            if (fettle.isBase()) {//基础信息校验通过，设置打款信息
                Enterprise enterprise = Enterprise.brank(verify.getName(), verify.getCardNo(), verify.getBank(), verify.getPrcptcd(), verify.getSubBranch(), verify.getProvice(), verify.getCity());
                if (SystemUtils.esignEnable()) {
                    enterprise = authenticateService.legalize(enterprise);
                    if (enterprise != null) {
                        supportService.saveSignAssociate(new SignAssociate(ObjectType.BANKVERIFY, verify.getId(), Signer.YUNHETONG, enterprise.getRequestKey(), legalize.getId()));
                    }
                }
                fettle = CompanyFettle.BRANK;
                supportService.saveOperateNote(legalize.getId(), OperateType.ESIGN, company.getId(), ESignEventType.ENTERPRISEBRANK, enterprise.toString());
            } else if (fettle.isBrank()) {//打款信息已经设置好, 金额校验
                if (SystemUtils.esignEnable()) {
                    SignAssociate signAssociate = supportService.getSignAssociate(ObjectType.BANKVERIFY, verify.getId());
                    if (signAssociate != null) {
                        Enterprise enterprise = Enterprise.cash(signAssociate.getThirdObjectKey(), String.valueOf(verify.getCheckAmount()));
                        enterprise = authenticateService.legalize(enterprise);
                        supportService.saveOperateNote(legalize.getId(), OperateType.ESIGN, company.getId(), ESignEventType.ENTERPRISECASH, enterprise.toString());
                        if (StringUtils.isNotBlank(enterprise.getErrorMsg())) {
                            fettle = CompanyFettle.BASE;
                            verify.setErrorMsg(enterprise.getErrorMsg());
                            verify.setFettle(CompanBankVerifyFettle.VFAILED.getCode());
                        }
                    }
                }
                fettle = fettle.isBrank() ? CompanyFettle.VALIDATE : fettle;
            }
        }
        if (fettle.isValidate()) {//校验通过创建企业用户
            SignAssociate signAssociate = supportService.getSignAssociate(ObjectType.COMPANY, company.getId());
            if (signAssociate == null) {
                Enterprise enterprise = Enterprise.create(company.getCompanyName(), company.getCodeOrg(), company.getCodeUsc(), legalize.getMobilePhone());
                if (SystemUtils.esignEnable()) {
                    enterprise = esignService.buildEnterprise(Signer.YUNHETONG, Enterprise.create(company.getCompanyName(), company.getCodeOrg(), company.getCodeUsc(), legalize.getMobilePhone()));
                    if (enterprise != null) {
                        supportService.saveSignAssociate(new SignAssociate(ObjectType.COMPANY, company.getId(), Signer.YUNHETONG, enterprise.getRequestKey(), legalize.getId()));
                    }
                }
                supportService.saveOperateNote(legalize.getId(), OperateType.ESIGN, company.getId(), ESignEventType.ENTERPRISECREATE, enterprise.toString());
            }
        }
        return fettle;
    }

    private void modifyCompanySeal(Integer uKey, Long companyKey, String sealPath) {
        CompanySeal companySeal = sealMapper.getCompanySealByCompanyKey(companyKey);
        if (companySeal == null) {
            Assert.notBlank(sealPath, "公章图片不能为空");
            companySeal = new CompanySeal();
            companySeal.setId(Globallys.nextKey());
            companySeal.setCompanyId(companyKey);
            companySeal.setSealData(sealPath);
            companySeal.setFettle(1);
            companySeal.setUpdateTime(new Date());
            companySeal.setUserId(uKey);
            sealMapper.insertSelective(companySeal);
        } else {
            if (StringUtils.isBlank(companySeal.getSealData())) {
                Assert.notBlank(sealPath, "公章图片不能为空");
                companySeal.setSealData(sealPath);
            }
            companySeal.setUpdateTime(new Date());
            sealMapper.updateByPrimaryKeySelective(companySeal);
        }
    }

    /**
     * 用户事件通知
     * <p>
     *
     * @param authorizeUser
     * @param type          事件类型
     * @throws BusinessException
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-28 10:53:18
     */
    @Override
    public void initializeUser(AuthorizeUser authorizeUser, Integer type) throws BusinessException {
        if (CoreConstants.USER_LOGIN - type == 0) {//用户登陆-加载企业员工信息
            Company company = getCompanyByUserKey(authorizeUser.getId());
            if (company != null) {
                authorizeUser.setCompanyKey(company.getId());
                authorizeUser.setCompanyName(company.getCompanyName());
                authorizeUser.setEmployee(getCompanyEmployee(authorizeUser.getId()));
            }
        }
    }


    @Override
    public void notifyBarcodeValidate(Barcode barcode, BarcodeContext validate) {
        if (validate != null && validate instanceof CompanyCodeContext) {
            CompanyCodeContext context = (CompanyCodeContext) validate;
            context.setAllowBind(isCompanyEmployee(barcode.getCompanyId(), context.getUserKey(), EmployeeType.COMMON));
            if (context.getRoleType() == null && context.isAllowBind()) {//有绑定权限的，必定是货主
                context.setRoleType(OrderRoleType.SHIPPER);
            }
        }
    }

    /**
     * 对公账户信息添加
     *
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-05-25 14:24:26
     * <p>
     */
    @Override
    public void saveCompanyBankVerify(Integer uKey, CompanyBankVerify companyBankVerify) throws ParameterException, BusinessException {
        Assert.notBlank(companyBankVerify.getName(), "对公账户户名不能为空");
        Assert.notBlank(companyBankVerify.getCardNo(), "企业对公银行账号不能为空");
        Assert.notBlank(companyBankVerify.getProvice(), "开户行所在省不能为空");
        Assert.notBlank(companyBankVerify.getCity(), "开户行所在市不能为空");
        Assert.notBlank(companyBankVerify.getBank(), "开户行支行名称不能为空");
        Assert.notBlank(companyBankVerify.getPrcptcd(), "开户行支行全称不能为空");
        try {
            UserLegalize legalize = userService.getUserLegalize(uKey);
            if (legalize == null || LegalizeFettle.convert(legalize.getFettle()).isVerify()) {
                throw new BusinessException("当前用户尚未通过实名认证");
            }
            CompanyEmployee manager = getCompanyEmployee(uKey);
            if (manager == null || !EmployeeType.convert(manager.getEmployeeType()).isManage()) {
                throw new BusinessException("当前用户不是企业管理员不能操作");
            }
            Company existCompany = getCompanyByKey(companyBankVerify.getCompanyId());
            if (existCompany == null) {
                throw new BusinessException("企业尚未注册");
            }
            if (manager.getCompanyId() - existCompany.getId() != 0) {
                throw new BusinessException("当前用户不是企业管理员不能操作");
            }
            ESignBrank signBrank = supportService.getESignBrankByPrcptcd(companyBankVerify.getPrcptcd());
            if (signBrank == null) {
                throw new BusinessException("当前操作不支持该银行");
            }
            companyBankVerify.modify(signBrank.getBrankName(), signBrank.getBrankCode(), signBrank.getBranchName(), signBrank.getProvince(), signBrank.getCity());
            companyBankVerify.setModifyTime(new Date());
            companyBankVerify.setFettle(1);//打款中
            companyBankVerify.setCreateId(uKey);
            //新增
            companyBankVerify.setId(Globallys.nextKey());
            companyBankVerify.setCreateTime(companyBankVerify.getModifyTime());
            if (companyBankVerifyMapper.insertSelective(companyBankVerify) > 0) {
                //更新企业状态
                Company company = new Company();
                company.setId(companyBankVerify.getCompanyId());
                company.setUpdateTime(new Date());
                company.setFettle(legalize(existCompany, companyBankVerify, legalize).getCode());
                updateCompany(company);
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("saveCompanyBankVerify()===>: companyBankVerify {}", companyBankVerify, e);
            throw BusinessException.dbException("对公账户信息添加异常");
        }
    }

    @Override
    public CompanyBank queryCompanyBank(Integer userKey) throws ParameterException, BusinessException {
        Assert.notBlank(userKey, "用户编号不能为空");
        if (logger.isDebugEnabled())
            logger.debug("queryCompanyBank()======>userKey:{}", userKey);
        Company company = companyMapper.getCompanyByUserKey(userKey);
        if (company == null) {
            throw new BusinessException("当前用户没有所属企业");
        }
        CompanyBankVerify bankVerify = companyBankVerifyMapper.queryByCompanyKey(company.getId());
        if (bankVerify != null) {
            return new CompanyBank(bankVerify, company);
        }
        return null;
    }

    /**
     * 打款金额校验业务
     *
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-05-29 10:56:30
     * <p>
     */
    @Override
    public String verifyCash(Integer uKey, Double cash) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人不能为空");
        if (cash == null || cash <= 0) {
            throw new ParameterException(cash, "校验金额填写有误");
        }
        try {
            UserLegalize legalize = userService.getUserLegalize(uKey);
            if (legalize == null || LegalizeFettle.convert(legalize.getFettle()).isVerify()) {
                throw new BusinessException("当前用户尚未通过实名认证");
            }
            Boolean result = true;
            CompanyEmployee manager = getCompanyEmployee(uKey);
            if (manager == null || !EmployeeType.convert(manager.getEmployeeType()).isManage()) {
                throw new BusinessException("当前用户不是企业管理员不能操作");
            }
            Company companyExister = getCompanyByKey(manager.getCompanyId());
            CompanyFettle companyFettle = CompanyFettle.convert(companyExister.getFettle());
            if (companyFettle.isValidate()) {
                throw new BusinessException("当前企业打款金额校验已经通过,无需重复校验");
            }
            if (companyFettle.isFaile()) {
                throw new BusinessException("当前企业认证失败,请重新发起认证流程");
            }
            if (!companyFettle.isBrank()) {
                throw new BusinessException("当前企业尚未进行对公打款认证操作");
            }
            CompanyBankVerify bankVerify = companyBankVerifyMapper.queryByCompanyKey(manager.getCompanyId());
            if (bankVerify == null) {
                throw new BusinessException("当前企业尚未进行对公打款认证操作");
            }
            //result = (bankVerify.getCheckAmount() != null && bankVerify.getCheckAmount() - cash == 0);

            bankVerify.setCheckAmount(cash);
            bankVerify.setModifyTime(new Date());

            Company company = new Company();
            company.setId(manager.getCompanyId());
            company.setUpdateTime(bankVerify.getModifyTime());
            company.setFettle(legalize(companyExister, bankVerify, legalize).getCode());
            if (StringUtils.isBlank(bankVerify.getErrorMsg())) {
                company.setSignFettle(CompanySignFettle.OPEN.getCode());
            }
            updateCompany(company);
            companyBankVerifyMapper.updateByPrimaryKeySelective(bankVerify);
            return bankVerify.getErrorMsg();
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.debug("打款金额认证异常 ===> uKey:{},cash:{}", uKey, cash, e);
            throw BusinessException.dbException("打款金额认证异常");
        }
    }

    private CustomerConcise assemblyCompanyIntegrationy(Long companyKey, Long orderKey, Integer... flags) throws ParameterException, BusinessException {
        Company company = getCompanyByKey(companyKey);
        if (null == company) {
            throw new BusinessException("获取企业信息异常[" + companyKey + "]");
        }
        try {
            if (Arrays.binarySearch(flags, O.sign) >= 0) {
                CompanySigner companySigner = new CompanySigner(company.getId(), company.getCompanyName(), company.getId(), company.getCompanyName());
                OrderSignature signature = signatureMapper.selectByCompanyKey(companyKey, orderKey);
                if (null != signature) {
                    companySigner.setSealTime(signature.getCreateTime());
                    CompanyEmployee employee = getCompanyEmployee(signature.getUserId());
                    if (null != employee) {
                        companySigner.setEmployeeKey(employee.getEmployeeId());
                        companySigner.setEmployeeName(employee.getEmployeeName());
                    }
                    CompanySeal companySeal = getCompanySealByKey(signature.getSealId());
                    if (null != companySeal) {
                        companySigner.setSealDate(companySeal.getSealData());
                    }
                    PersonalSeal personalSeal = userService.getPersonalSealByKey(signature.getPersonalSeal());
                    if (null != personalSeal) {
                        companySigner.setUserSealPath(personalSeal.getSealImgPath());
                    }
                }
                return companySigner;
            } else {
                return new CustomerConcise(company.getId(), company.getCompanyName(), company.getId(), company.getCompanyName());
            }
        } catch (Exception e) {
            logger.error("组装企业信息异常 companyKey：{} orderKey:{}", companyKey, orderKey, e);
            throw new BusinessException("企业信息获取异常", e);
        }
    }

    @Override
    public void saveCompanySeal(CompanySeal companySeal) throws ParameterException, BusinessException {
        Assert.notBlank(companySeal.getUserId(), "操作人编号为空");
        Assert.notBlank(companySeal.getCompanyName(), "印章简称为空");
        //Assert.notBlank(companySeal.getSecurityCode(), "防伪串码为空");
        try {
            Company company = assertCompanyByUserKey(companySeal.getUserId());
            companySeal.setCompanyName(StringUtils.trim(companySeal.getCompanyName()));
            Assert.notBlank(companySeal.getCompanyName(), "印章简称为空");
            companySeal.setCopyWriting(StringUtils.trim(companySeal.getCopyWriting()));
            companySeal.setSecurityCode(StringUtils.trim(companySeal.getSecurityCode()));

            //@TODO 要判断是否是管理员，只有管理员才可以添加企业公章
            CompanyEmployee maneger = getCompanyEmployee(companySeal.getUserId());
            if (!EmployeeType.convert(maneger.getEmployeeType()).isManage()) {
                throw new BusinessException("非公司管理员不能进行该操作");
            }

            companySeal.setId(Globallys.nextKey());
            companySeal.setCreateTime(new Date());
            companySeal.setCompanyId(company.getId());
            companySeal.setUpdateTime(companySeal.getCreateTime());

            BufferedImage bufferedImage = SealBuilder.circleSeal(company.getCompanyName(), companySeal.getCopyWriting(), companySeal.getSecurityCode(), true);
            if (bufferedImage != null) {
                FileEntity fileEntity = new FileEntity();
                fileEntity.setDirectory(SystemUtils.fileRootPath());
                fileEntity.setSubDir(Directory.SEAL.getDir());
                fileEntity = DrawSeal.saveImage(fileEntity, bufferedImage);
                companySeal.setSealData(fileEntity.persistence());
            }
            SealMoulage sealMoulage = SealMoulage.enterprise("local#companyseal#" + companySeal.getId(), companySeal.getSealType(), companySeal.getCopyWriting(), companySeal.getSecurityCode());
            if (SystemUtils.esignEnable()) {
                SignAssociate associate = supportService.getSignAssociate(ObjectType.COMPANY, company.getId());
                if (associate == null) {
                    throw new BusinessException("当前企业还未开通电子签收功能");
                }
                sealMoulage.setSignerId(associate.getThirdObjectKey());
                sealMoulage = esignService.buildSeal(Signer.YUNHETONG, sealMoulage);
                if (sealMoulage == null || StringUtils.isBlank(sealMoulage.getRequestKey())) {
                    throw new BusinessException("企业印章创建异常");
                }
                supportService.saveSignAssociate(new SignAssociate(ObjectType.COMPANYSEAL, companySeal.getId(), Signer.YUNHETONG, sealMoulage.getRequestKey(), companySeal.getUserId()));
            }
            supportService.saveOperateNote(companySeal.getUserId(), OperateType.ESIGN, company.getId(), ESignEventType.SEALENTERPRIS, sealMoulage.toString());
            sealMapper.insertSelective(companySeal);
        } catch (ParameterException | BusinessException e) {
            logger.info("公司印章添加异常 companySeal : {}", companySeal, e);
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            logger.info("公司印章添加异常 companySeal : {}", companySeal, e);
            throw new BusinessException("公司印章添加异常");
        }
    }

    @Override
    public Collection<CompanySeal> listCompanySeal(Long cKey, Integer uKey, String likeString) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "员工编号不能为空");
        CompanyEmployee employee = getCompanyEmployee(uKey);
        if (employee == null) {
            return Collections.emptyList();
        }
        if (cKey == null || cKey <= 0) {
            cKey = employee.getCompanyId();
        }
        if (EmployeeType.convert(employee.getEmployeeType()).isManage()) {
            CustomExample example = new CustomExample(CompanySeal.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("companyId", cKey);
            if (StringUtils.isNotBlank(likeString)) {
                criteria.andLike("companyName", likeString);
            }
            criteria.andEqualTo("fettle", CoreConstants.COMPANY_SEAL_FETTLE_ENABLE);
            example.orderBy("createTime");
            return sealMapper.selectByExample(example);
        } else {
            return sealMapper.listCompanySeals(cKey, uKey, likeString);
        }
    }

    @Override
    public void deleteCompanySeal(Integer uKey, Long sealKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "操作人编号为空");
        Assert.notBlank(sealKey, "要删除的印章编号为空");
        try {
            CompanySeal companySeal = getCompanySealByKey(sealKey);
            if (companySeal != null) {
                if (!isCompanyEmployee(companySeal.getCompanyId(), uKey, EmployeeType.MANAGE)) {
                    throw new BusinessException("没有权限删除该印章");
                }
                companySeal.setFettle(CoreConstants.COMPANY_SEAL_FETTLE_DISABLED);
                Collection<EmployeeSeal> employeeSeals = employeeSealMapper.select(new EmployeeSeal(sealKey));
                if (CollectionUtils.isNotEmpty(employeeSeals)) {
                    Map<Integer, Set<Long>> result = employeeSeals.stream().collect(
                            Collectors.groupingBy(
                                    EmployeeSeal::getEmployeeId, Collectors.mapping(
                                            EmployeeSeal::getAuthorizeSealId, Collectors.toSet()
                                    )
                            )
                    );
                    for (Map.Entry<Integer, Set<Long>> entry : result.entrySet()) {
                        unsignatureAuth(uKey, entry.getKey(), entry.getValue());
                    }
                }
                sealMapper.updateByPrimaryKeySelective(companySeal);
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("删除公司印章异常 uKey:{} sealKey : {}", uKey, sealKey, e);
            throw new BusinessException("删除公司印章异常");
        }
    }

    @Override
    public Collection<CompanySeal> getCompanySeal(Integer userKey) throws ParameterException, BusinessException {
        return listCompanySeal(0L, userKey, null);
    }

    @Override
    public Collection<Long> listCompanySealKeys(Long companyId, Integer employeeId) throws ParameterException, BusinessException {
        Assert.notBlank(employeeId, "员工编号不能为空");
        CompanyEmployee employee = getCompanyEmployee(employeeId);
        if (employee == null) {
            return Collections.emptyList();
        }
        if (companyId == null || companyId <= 0) {
            companyId = employee.getCompanyId();
        }
        if (EmployeeType.convert(employee.getEmployeeType()).isManage()) {
            return Optional.ofNullable(sealMapper.select(new CompanySeal(employee.getCompanyId()))).map(ss -> {
                return ss.stream().map(s -> s.getId()).collect(Collectors.toList());
            }).orElse(Collections.emptyList());
        } else {
            return employeeSealMapper.getEmployeeSealId(employeeId, companyId);
        }
    }

    /**
     * 根据企业编号查询企业校验信息
     *
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-05 17:44:34
     * @see com.ycg.ksh.service.api.CompanyService# queryByCompanyKey(java.lang.Long)
     * <p>
     */
    @Override
    public CompanyBankVerify queryByCompanyKey(Integer uKey, Long companyKey) throws ParameterException, BusinessException {
        Assert.notBlank(companyKey, "企业编号不能为空");
        try {
            CompanyBankVerify companyBankVerify = companyBankVerifyMapper.queryByCompanyKey(companyKey);
            if (null != companyBankVerify) {
                if (CompanBankVerifyFettle.isVerify(companyBankVerify.getFettle())) {
                    getMoneyStatus(companyBankVerify, uKey);
                }
            }
            return companyBankVerify;
        } catch (ParameterException | BusinessException e) {
            logger.debug("queryByCompanyKey===> companyKey:{}", companyKey, e);
            throw new BusinessException("查询企业校验信息异常");
        }
    }


    //@TODO 查询打款状态并更新企业打款状态
    public void getMoneyStatus(CompanyBankVerify companyBankVerify, Integer uKey) {
        try {
            companyBankVerify.setFettle(CompanBankVerifyFettle.VPASS.getCode());
            if (SystemUtils.esignEnable()) {
                //打款状态校验
                SignAssociate signAssociate = supportService.getSignAssociate(ObjectType.BANKVERIFY, companyBankVerify.getId());
                if (signAssociate != null) {
                    Enterprise enterprise = Enterprise.check(signAssociate.getThirdObjectKey());
                    enterprise = authenticateService.legalize(enterprise);
                    if (null != enterprise) {
                        if (StringUtils.isNotBlank(enterprise.getErrorMsg())) {
                            companyBankVerify.setFettle(CompanBankVerifyFettle.VFAILED.getCode());
                            companyBankVerify.setErrorMsg(enterprise.getErrorMsg());
                        }
                        supportService.saveOperateNote(uKey, OperateType.ESIGN, companyBankVerify.getCompanyId(), ESignEventType.ENTERPRISECASH, enterprise.toString());
                    }
                }
            }
            companyBankVerifyMapper.updateByPrimaryKeySelective(companyBankVerify);
            if (CompanBankVerifyFettle.VFAILED.getCode() == companyBankVerify.getFettle()) {
                Company company = getCompanyByKey(companyBankVerify.getCompanyId());
                if (null != company) {
                    company.setFettle(CompanyFettle.BASE.getCode());
                    companyMapper.updateByPrimaryKeySelective(company);
                }
            }
        } catch (Exception e) {
            logger.error("查询打款状态并更新企业打款状态异常", e);
        }
    }

    /**
     * 通知回单签署
     *
     * @param signature 签署信息
     * @param exception 异常信息
     * @throws BusinessException
     */
    @Override
    public void notifySignReceipt(ReceiptEventType eventType, ReceiptSignature signature, String exception) throws
            BusinessException {
        if (eventType.isValidate()) {//签署准备
            CompanyEmployee employee = getCompanyEmployee(signature.getUserId());
            if (null == employee) {
                throw new BusinessException("当前用户没有所属公司不能进行该操作");
            }
            signature.setEmployee(employee);
            Company company = getCompanyByKey(employee.getCompanyId());
            if (null == company) {
                throw new BusinessException("获取所属企业信息异常");
            }
            signature.setCompany(company);
            signature.setCompanyId(company.getId());

            EmployeeSeal employeeSeal = new EmployeeSeal(company.getId(), employee.getEmployeeId());
            if (signature.getSealId() == null || signature.getSealId() <= 0) {
                if (employeeSealMapper.selectCount(employeeSeal) <= 0) {
                    throw new BusinessException("您未被授权签署企业章，请联系管理员进行授权后再来签署");
                }
            } else {
                employeeSeal.setAuthorizeSealId(signature.getSealId());
                if (employeeSealMapper.selectCount(employeeSeal) <= 0) {
                    throw new BusinessException("该用户没有使用当前公章的权限");
                }
                CompanySeal companySeal = getCompanySealByKey(signature.getSealId());
                if (null == companySeal) {
                    throw new BusinessException("获取公章信息异常");
                }
                signature.setCompanySeal(companySeal);
            }
        }
    }

    /**
     * 更新订单
     *
     * @param uKey      当前操作人的用户编号
     * @param order     当前订单基本信息
     * @param eventType 事件类型
     * @throws BusinessException 业务逻辑异常
     */
    @Override
    public void modifyOrder(Integer uKey, Order order, OrderEventType eventType) throws BusinessException {
        logger.info("发货通知->: {}, : {}, : {}", uKey, order, eventType);
        if (order != null && eventType != null && eventType.isBindCode()) {
            try {
                Company company = getCompanyByUserKey(uKey);
                logger.info("发货通知->: company : {}", company);
                if (company == null) {
                    return;
                }
                CompanyConfig config = loadCompanyConfig(company.getId(), CompanyConfigType.SEND_NOTICE);
                logger.info("发货通知->: config : {}", config);
                if (config == null || StringUtils.isBlank(config.getConfigValue()) || StringUtils.equals("0", config.getConfigValue())) {
                    return;
                }
                CustomerConcise receiver = customerService.loadCustomerConcise(order.getReceiveId(), PartnerType.RECEIVE.registered(order.getClientType()));
                logger.info("发货通知->: receiver : {}", receiver);
                if (receiver != null && receiver.isEnterprise()) {
                    Collection<CompanyEmployee> employees = employeeMapper.listAdminCompany(receiver.getCompanyKey());
                    logger.info("发货通知->: employees : {}", employees);
                    if (CollectionUtils.isNotEmpty(employees)) {
                        TemplateMesssage messsage = new TemplateMesssage();
                        messsage.addData("first", new TemplateDataValue("物流发货通知", "#173177"));
                        messsage.addData("keyword1", new TemplateDataValue(order.getDeliveryNo(), "#173177"));
                        messsage.addData("keyword2", new TemplateDataValue(DateUtils.formatDate(order.getDeliveryTime(), "yyyy-MM-dd HH:mm:ss"), "#173177"));
                        CustomerConcise shipper = customerService.loadCustomerConcise(order.getShipperId(), PartnerType.SHIPPER.registered(order.getClientType()));
                        if (shipper != null) {
                            messsage.addData("keyword3", new TemplateDataValue(shipper.getCustomerName(), "#173177"));
                            CustomerConcise convey = customerService.loadCustomerConcise(order.getConveyId());
                        }

                        CustomerConcise convey = customerService.loadCustomerConcise(order.getConveyId(), PartnerType.CONVEY.registered(order.getClientType()));
                        if (convey != null) {
                            messsage.addData("keyword4", new TemplateDataValue(convey.getCustomerName(), "#173177"));
                            messsage.addData("keyword5", new TemplateDataValue("您好，货物已发出，请注意查收", "#173177"));
                        }

                        messsage.setTemplateType(TemplateType.DELIVERY);

                        for (CompanyEmployee employee : employees) {
                            try {
                                User u = userService.getUserByKey(employee.getEmployeeId());
                                logger.info("发货通知->: user : {}", u);
                                if (StringUtils.isNotBlank(u.getUsername())) {
                                    messsage.setTouser(u.getUsername());
                                    messsage.setUrl(FrontUtils.enterpriseBindCodeDetail(SecurityTokenUtil.createToken(u.getId().toString()), order.getId()));

                                    queueService.sendNetMessage(new MediaMessage(Globallys.UUID(), messsage));
                                }
                            } catch (Exception e) {
                                logger.error("发送发货通知异常{} {} {}", employee.getEmployeeId(), order, eventType, e);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("发送发货通知异常{} {} {}", uKey, order, eventType, e);
            }
        }
    }

    @Override
    public void modifyConfig(Integer uKey, Collection<CompanyConfig> configs) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户编号不能为空");
        try {
            Company company = assertCompanyByUserKey(uKey);
            if (CollectionUtils.isNotEmpty(configs)) {
                Collection<String> configKeys = companyConfigMapper.listConfigKeys(company.getId());
                Date cdate = new Date();
                Collection<CompanyConfig> inserts = configs.stream().filter(f -> !configKeys.contains(f.getConfigKey())).peek(f -> {
                    f.setCompanyKey(company.getId());
                    f.setUpdateTime(cdate);
                    f.setConfigValue(StringUtils.trimToEmpty(f.getConfigValue()));
                }).collect(Collectors.toList());
                Collection<CompanyConfig> updates = configs.stream().filter(f -> configKeys.contains(f.getConfigKey())).peek(f -> {
                    f.setCompanyKey(company.getId());
                    f.setUpdateTime(cdate);
                    f.setConfigValue(StringUtils.trimToEmpty(f.getConfigValue()));
                }).collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(inserts)) {
                    companyConfigMapper.inserts(inserts);
                }
                if (CollectionUtils.isNotEmpty(updates)) {
                    for (CompanyConfig update : updates) {
                        companyConfigMapper.updateBykey(update);
                    }
                }
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("更新企业配置信息异常 uKey:{} configs:{}", uKey, configs);
            throw BusinessException.dbException("更新企业配置信息异常");
        }
    }

    @Override
    public Collection<CompanyConfig> listConfig(Integer uKey) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户编号不能为空");
        try {
            Company company = assertCompanyByUserKey(uKey);
            Example example = new Example(CompanyConfig.class);
            example.createCriteria().andEqualTo("companyKey", company.getId());
            return companyConfigMapper.selectByExample(example);
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("查询企业配置信息异常 uKey:{}", uKey);
            throw new BusinessException("查询企业配置信息异常", e);
        }
    }


    @Override
    public void trackingSetting(Integer uKey, String frequency) throws ParameterException, BusinessException {
        Assert.notBlank(uKey, "用户编号不能为空");
        Assert.notBlank(frequency, "跟踪设置次数不能为空");
        Company company = assertCompanyByUserKey(uKey);
        CompanyConfig companyConfig = loadCompanyConfig(company.getId(), CompanyConfigType.TRACKING);
        try {
            if (null != companyConfig) {
                companyConfig.setConfigValue(frequency);
                companyConfig.setUpdateTime(new Date());
                companyConfigMapper.updateByPrimaryKeySelective(companyConfig);
            } else {
                companyConfigMapper.insert(new CompanyConfig(company.getId(), CompanyConfigType.TRACKING.getCode(),
                        frequency, new Date()));
            }
        } catch (ParameterException | BusinessException e) {
            logger.info("跟踪设置更新异常 uKey:{} frequency:{}", uKey, frequency);
            throw BusinessException.dbException("跟踪设置更新异常");
        }
    }

    @Override
    public CompanyConfig loadCompanyConfig(Long companyKey, CompanyConfigType configType) throws ParameterException, BusinessException {
        try {
            return companyConfigMapper.selectOne(new CompanyConfig(companyKey, configType.getCode()));
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("查询企业配置信息异常 companyKey:{}", companyKey);
            throw new BusinessException("查询企业配置信息异常", e);
        }
    }

    /**
     * 联合订单数据
     *
     * @param uKey
     * @param alliance
     * @param flags
     * @throws BusinessException
     */
    @Override
    public void allianceOrder(Integer uKey, OrderAlliance alliance, Integer... flags) throws BusinessException {
        if (Arrays.binarySearch(flags, O.enterprise) >= 0) {
            for (PartnerType partnerType : PartnerType.values()) {
                if (!partnerType.registered(alliance.getClientType())) {
                    continue;
                }
                if (partnerType.shipper()) {
                    alliance.setShipper(assemblyCompanyIntegrationy(alliance.getShipperId(), alliance.getId(), flags));
                } else if (partnerType.convey()) {
                    alliance.setConvey(assemblyCompanyIntegrationy(alliance.getConveyId(), alliance.getId(), flags));
                } else {
                    alliance.setReceive(assemblyCompanyIntegrationy(alliance.getReceiveId(), alliance.getId(), flags));
                }
            }
        }
        if (uKey != null && uKey > 0) {
            Company company = getCompanyByUserKey(uKey);
            if (company != null) {
                OrderRoleType roleType = alliance.signRoleType(company.getId());
                if (roleType != null) {
                    alliance.setIsUserType(roleType.getCode());
                }
            }
        }
    }

    /**
     * 联合计划数据
     *
     * @param uKey     当前操作人的用户编号
     * @param alliance 当前计划
     * @param flags    要加载的数据标识
     * @throws BusinessException 业务逻辑异常
     */
    @Override
    public void allianceOrder(Integer uKey, PlanAlliance alliance, Integer... flags) throws BusinessException {
        if (Arrays.binarySearch(flags, P.enterprise) >= 0) {
            if (alliance.getShipperId() == null || alliance.getShipperId() <= 0) {
                Company company = getCompanyByKey(alliance.getCompanyKey());
                alliance.setShipper(new CustomerConcise(company.getId(), company.getCompanyName(), company.getId(), company.getCompanyName()));
            }
        }
        if (alliance.getDesignate() != null && alliance.getConvey() == null) {
            Company company = getCompanyByKey(alliance.getDesignate().getCompanyKey());
            alliance.setConvey(new CustomerConcise(company.getId(), company.getCompanyName(), company.getId(), company.getCompanyName()));
        }
    }

    @Override
    public Collection<Company> listCompanyByName(String companyName) throws ParameterException, BusinessException {
        try {
            if (StringUtils.isNotBlank(companyName)) {
                Example example = new Example(Company.class);
                Criteria criteria = example.createCriteria();
                criteria.andLike("companyName", companyName);
                return companyMapper.selectByExample(example);
            } else {
                return listCompany();
            }
        } catch (Exception e) {
            logger.error("listCompanyByName===> companyName:{},e:{}", companyName, e);
            throw BusinessException.dbException("查询公司列表异常");
        }
    }

    /**
     * 修改企业名称
     *
     * @param uKey        当前用户
     * @param companyName 企业名称
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void modifyName(Integer uKey, String companyName) throws ParameterException, BusinessException {
        Assert.notBlank(companyName, "企业名称不能为空");
        //检查当前用户是不是企业管理员，只有管理员才可以修改
        try {
            CompanyEmployee manager = getCompanyEmployee(uKey);
            if (manager == null || !EmployeeType.convert(manager.getEmployeeType()).isManage()) {
                throw new BusinessException("当前用户不是企业管理员不能操作");
            }
            if (getCompanyByName(companyName) != null) {
                throw new BusinessException("当前企业名称已经存在");
            }
            Company company = assertCompanyByUserKey(uKey);
            if (company.getRenameCount() == null || company.getRenameCount() <= 0) {
                company.setCompanyName(companyName);
                company.setRenameCount(Optional.ofNullable(company.getRenameCount()).orElse(0) + 1);
                companyMapper.updateByPrimaryKeySelective(company);
            } else {
                throw new BusinessException("企业名称只允许修改一次");
            }
        } catch (ParameterException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.info("企业名称更新异常 uKeyL:{} companyName:{}", uKey, companyName);
            throw BusinessException.dbException("企业名称更新异常");
        }

        //检查 Company.renameCount ， 大于0是不能修改
        //修改企业名称并更新 Company.renameCount +1
    }


}

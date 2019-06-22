package com.ycg.ksh.core.contract.domain.model;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.core.Registrys;
import com.ycg.ksh.core.common.domain.Model;
import com.ycg.ksh.core.common.domain.Region;
import com.ycg.ksh.core.util.Constants;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 合同
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/27 0027
 */
public class Contract extends Model {

    private Long id;//合同ID--------------系统维护
    private String contractNo;//合同编号--------------不能为空
    private ValidityPeriod period;//合同有效期--------------不能为空

    private Integer contractType;//合同类型--------------货主合同、供应商合同,不能为空
    private Long companyKey;//合同我方--------------分公司,不能为空
    private String companyName;
    private Long oppositeKey;//合同对方编码--------------合同对方编码,不能为空
    private String oppositeName;//合同对方名称--------------合同对方名称,不能为空
    private Integer industryCategory;//行业类别--------------合同对方行业类别，供应商合同没有
    private Long shipperId;//发货公司ID
    private String contactName;//客户联系人
    private String telephoneNumber;//客户联系人电话
    private String mobileNumber;//客户联系人手机号
    private Region customerRegion;//客户地区
    private String customerAddress;//客户地址


    private LocalDateTime createTime;//合同创建时间--------------合同首次创建的时间
    private LocalDateTime modifyTime;//合同修改时间--------------合同最后一次修改的时间

    private List<FragmentaryValuation> fragmentaries;//合同的报价信息，零担计价配置
    private List<CommodityConfig> commodityConfigs;//合同的物料配置信息

    private Integer verifyStatus;//合同状态--------------待审核(默认)、审核未通过、审核通过
    private List<ContractVerify> contractVerifies;//合同审核记录--------------合同审核记录信息，多个

    public Contract() {
    }

    public FragmentaryValuation filterValuation(int fareType) {
        Assert.notEmpty(fragmentaries, "未能找到有效的计价配置");
        LocalDateTime ctime = LocalDate.now().atTime(0, 0, 0);
        return fragmentaries.stream().filter(c -> c.getFareType() == fareType && c.getPeriod().isEffective(ctime))
                .findAny().orElseThrow(() -> new BusinessException("未能找到有效的计价配置"));
    }

    public CommodityConfig getCommodity(String commodityCode) {
        Assert.notEmpty(commodityConfigs, "未找到匹配的物料");
        return commodityConfigs.stream().filter(c -> c.getCommodityCode().equals(commodityCode))
                .findAny().orElseThrow(() -> new BusinessException("未找到匹配的物料"));
    }

    public double expense(CommodityConfig commodityConfig, Region origin, Region destination, Integer quantity) {
        FragmentaryValuation valuation = filterValuation(commodityConfig.getFareType());
        Double number = 0D;
        if (valuation.getFareType() - Constants.PRICING_DIMENSION_WEIGHT == 0) {
            number = quantity * commodityConfig.getUnitWeight();
        } else if (valuation.getFareType() - Constants.PRICING_DIMENSION_VOLUME == 0) {
            number = quantity * commodityConfig.getUnitVolume();
        } else {
            number = new Double(quantity);
        }
        return valuation.expense(origin, destination, number);
    }


    public Contract(String contractNo, long companyKey, String companyName, int contractType, ValidityPeriod period) {
        this.setContractNo(contractNo);
        this.setPeriod(period);
        this.setContractType(contractType);
        this.setCompanyKey(companyKey);
        this.setCompanyName(companyName);
    }

    public void modifyCustomer(long shipperId, long oppositeKey, String oppositeName, Integer industryCategory, String contactName, String telephoneNumber, String mobileNumber, Region customerRegion, String customerAddress) {
        if (checkModify()) {
            this.setShipperId(shipperId);
            this.setOppositeKey(oppositeKey);
            this.setOppositeName(oppositeName);
            this.setIndustryCategory(industryCategory);
            this.setContactName(contactName);
            this.setTelephoneNumber(telephoneNumber);
            this.setMobileNumber(mobileNumber);
            this.setCustomerRegion(customerRegion);
            this.setCustomerAddress(customerAddress);
        }
    }

    /**
     * 更新合同信息基本信息
     *
     * @param contractNo   合同编号
     * @param companyKey   所属分公司编号
     * @param companyName  所属分公司名称
     * @param contractType 合同类型
     * @param period       合同有效期
     */
    public void modify(String contractNo, long companyKey, String companyName, int contractType, ValidityPeriod period) {
        if (checkModify()) {
            if (this.getCompanyKey() != null && this.getCompanyKey() > 0 && this.getCompanyKey() - companyKey != 0) {
                throw new BusinessException("该合同只能由[" + companyName + "]员工修改");
            }
            this.setContractNo(contractNo);
            this.setPeriod(period);
            this.setContractType(contractType);
            this.setCompanyKey(companyKey);
            this.setCompanyName(companyName);
        }
    }

    public void modifyValuation(List<FragmentaryValuation> fragmentaries) {
        if (Constants.CONTRACT_VERIFY_PASS == getVerifyStatus() && identical(fragmentaries)) {
            throw new BusinessException("合同已经审核通过只能修改计价配置的有效日期");
        }
        if (CollectionUtils.isNotEmpty(this.fragmentaries)) {
            this.fragmentaries.clear();
            this.fragmentaries.addAll(fragmentaries);
        } else {
            this.fragmentaries = fragmentaries;
        }
    }

    /**
     * 保存合同信息
     */
    public void save() {
        if (validateContractNo()) {
            this.setId(Registrys.contractRepository().nextIdentify());//ID要通过资源库生成
            this.setVerifyStatus(Constants.CONTRACT_VERIFY_WAIT);
            this.setCreateTime(LocalDateTime.now());
            this.setModifyTime(getCreateTime());
            Registrys.contractRepository().save(this);
        }
    }

    private boolean validateContractNo() {
        Long count = Registrys.contractQueryRepository().countByContractNo(contractNo, id);
        if (count != null && count > 0) {
            throw new BusinessException("合同编号[" + contractNo + "]已经存在");
        }
        return true;
    }

    /**
     * 更新合同信息
     */
    public void update() {
        this.setModifyTime(LocalDateTime.now());
        Registrys.contractRepository().modify(this);
    }

    /**
     * 检查是否可以修改合同信息
     *
     * @return
     */
    private boolean checkModify() {
        if (Constants.CONTRACT_VERIFY_PASS == getVerifyStatus()) {
            throw new BusinessException("合同已经审核通过不能修改");
        }
        return true;
    }

    /**
     * 校验计价配置是否有修改
     *
     * @param fragmentaries 合同的计价配置信息
     * @return
     */
    private boolean identical(Collection<FragmentaryValuation> fragmentaries) {
        boolean result = false;
        if (this.fragmentaries != null && fragmentaries != null && this.fragmentaries.size() == fragmentaries.size()) {
            List<FragmentaryValuation> fvs = fragmentaries.stream().sorted().collect(Collectors.toList());
            for (int i = 0; i < this.fragmentaries.size(); i++) {
                if ((result = this.fragmentaries.get(i).identical(fvs.get(i), true))) {
                    break;
                }
            }
            result = true;
        }
        return result;
    }

    /**
     * 修改合同物料配置信息
     *
     * @param commodityConfigs 物料配置信息
     */
    public void modify(List<CommodityConfig> commodityConfigs) {
        if (checkModify()) {
            if (CollectionUtils.isNotEmpty(this.commodityConfigs)) {
                this.commodityConfigs.clear();
            }
            this.commodityConfigs.addAll(commodityConfigs);
            this.update();
        }
    }

    /**
     * 合同审核
     *
     * @param verifyStatus 审核状态
     * @param reason       不通过原因
     * @param userId       审核人用户编号
     */
    public void verify(int verifyStatus, String reason, Integer userId) {
        if (Constants.CONTRACT_VERIFY_PASS == getVerifyStatus()) {
            throw new BusinessException("合同已经审核通过不能再审核");
        }
        this.setVerifyStatus(verifyStatus);
        if (contractVerifies == null) {
            contractVerifies = new ArrayList<ContractVerify>();
        } else {
            contractVerifies.clear();
        }
        contractVerifies.add(new ContractVerify(verifyStatus, getModifyTime(), reason, userId));
        this.update();
    }


    public void setFragmentaries(List<FragmentaryValuation> fragmentaries) {
        this.fragmentaries = (fragmentaries);
    }

    public void setCommodityConfigs(List<CommodityConfig> commodityConfigs) {
        this.commodityConfigs = (commodityConfigs);
    }

    protected void setId(Long id) {
        this.id = id;
    }

    protected void setContractNo(String contractNo) {
        Assert.notBlank(contractNo, "合同编号不能为空");
        this.contractNo = contractNo;
    }

    protected void setPeriod(ValidityPeriod period) {
        Assert.notNull(period, "合同有效期不能为空");
        this.period = period;
    }

    protected void setContractType(Integer contractType) {
        Assert.notBlank(contractType, "合同类型不能为空");
        this.contractType = contractType;
    }

    protected void setCompanyKey(Long companyKey) {
        Assert.notBlank(companyKey, "合同所属公司编号不能为空");
        this.companyKey = companyKey;
    }

    protected void setOppositeKey(Long oppositeKey) {
        Assert.notBlank(oppositeKey, "合同对方编号不能为空");
        this.oppositeKey = oppositeKey;
    }

    protected void setOppositeName(String oppositeName) {
        Assert.notBlank(oppositeName, "合同对方名称不能为空");
        this.oppositeName = oppositeName;
    }

    public Long getShipperId() {
        return shipperId;
    }

    private void setShipperId(Long shipperId) {
        this.shipperId = shipperId;
    }

    protected void setIndustryCategory(Integer industryCategory) {
        this.industryCategory = industryCategory;
    }

    protected void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    protected void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    protected void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public void setContractVerifies(List<ContractVerify> contractVerifies) {
        this.contractVerifies = contractVerifies;
    }

    public Long getId() {
        return id;
    }

    public String getContractNo() {
        return contractNo;
    }

    public ValidityPeriod getPeriod() {
        return period;
    }

    public Integer getContractType() {
        return contractType;
    }

    public Long getCompanyKey() {
        return companyKey;
    }

    public Long getOppositeKey() {
        return oppositeKey;
    }

    public String getOppositeName() {
        return oppositeName;
    }

    public Integer getIndustryCategory() {
        return industryCategory;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public Collection<FragmentaryValuation> getFragmentaries() {
        return fragmentaries;
    }

    public Collection<CommodityConfig> getCommodityConfigs() {
        return commodityConfigs;
    }

    public Integer getVerifyStatus() {
        return Optional.ofNullable(verifyStatus).orElse(Constants.CONTRACT_VERIFY_WAIT);
    }

    public Collection<ContractVerify> getContractVerifies() {
        return contractVerifies;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Region getCustomerRegion() {
        return customerRegion;
    }

    public void setCustomerRegion(Region customerRegion) {
        this.customerRegion = customerRegion;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
}

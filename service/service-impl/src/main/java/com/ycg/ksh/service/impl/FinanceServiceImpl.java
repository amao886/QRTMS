package com.ycg.ksh.service.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/10
 */

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.encrypt.MD5;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.constant.FinanceType;
import com.ycg.ksh.entity.persistent.enterprise.Finance;
import com.ycg.ksh.entity.persistent.user.MoneyExtract;
import com.ycg.ksh.service.persistence.enterprise.FinanceMapper;
import com.ycg.ksh.service.persistence.user.MoneyExtractMapper;
import com.ycg.ksh.service.api.FinanceService;
import com.ycg.ksh.service.observer.FinanceObserverAdapter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * 财务
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/10
 */
@Service("ksh.core.service.financeService")
public class FinanceServiceImpl implements FinanceService {

    private static final DateTimeFormatter DATETIMEFORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Resource
    FinanceMapper financeMapper;

    @Resource
    MoneyExtractMapper moneyExtractMapper;

    @Autowired(required = false)
    Collection<FinanceObserverAdapter> observers;

    public Finance getFinance(FinanceType financeType, Serializable objectKey) throws ParameterException, BusinessException {
        return financeMapper.getFinanceByTypeId(financeType.getCode(), objectKey);
    }


    /**
     * 通知奖励
     *
     * @param uKey
     * @param resType
     * @param value
     */
    private void modifyNoticeObserver(FinanceType financeType, Serializable ownerKey, Long changeValue) {
        if (CollectionUtils.isNotEmpty(observers)) {
            for (FinanceObserverAdapter observerAdapter : observers) {
                observerAdapter.notifyFinanceChange(financeType, ownerKey, changeValue);
            }
        }
    }

    private String moneyExtractKey(Integer uKey) {
        return MD5.encrypt(uKey + "#" + LocalDate.now().format(DATETIMEFORMATTER));
    }

    /**
     * 提现申请
     *
     * @param uKey
     * @param value
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void applyMoneyExtract(Integer uKey, Integer value) throws ParameterException, BusinessException {
        Assert.notBlank(value, "提现金额不能为空");
        String moneyExtractKey = moneyExtractKey(uKey);

        if (null != moneyExtractMapper.selectByPrimaryKey(moneyExtractKey)) {
            throw new BusinessException("每天只能提现一次,明天再来吧");
        }
        //1.通知提现，用户方需要冻结提现的金额,金额不足时抛异常
        modifyNoticeObserver(FinanceType.WALLET, uKey, 0L - value * 100);
        //2.新增提现申请记录
        //...
        MoneyExtract moneyExtract = new MoneyExtract(uKey, moneyExtractKey, Long.valueOf(value));
        moneyExtract.setHandleType(CoreConstants.RE_HANDLE_TYPE_SQ);
        moneyExtractMapper.insertSelective(moneyExtract);

    }
}

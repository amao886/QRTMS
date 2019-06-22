package com.ycg.ksh.collect.jdbc;

import com.ycg.ksh.constant.CompanyConfigType;
import com.ycg.ksh.constant.LocationType;
import com.ycg.ksh.entity.collecter.OrderCollect;
import com.ycg.ksh.entity.persistent.ApplyRes;
import com.ycg.ksh.entity.persistent.Barcode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 业务库JDBC
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/30 0030
 */
public interface ServiceJdbcTemplate extends JdbcTemplateService  {

    public LocalDateTime collectLeastTimeSummaryAssess();

    public Collection<OrderCollect> collectOrderSummaryAssess(LocalDateTime leastCreateTime, LocalDateTime lastReciveTime);

    public Collection<OrderCollect> collectOrderSummaryTrack(LocalDateTime lastTime);

    public Collection<OrderCollect> collectOrderSummaryTimeout(LocalDateTime lastTime);

    public String loadCompanyConfigValue(Long companyKey, CompanyConfigType configType);

    public Integer collectLocateCount(LocationType locationType, Serializable hostKey);

    public void modifyOrderPositioningCheck(Collection<OrderCollect> collection);

    /**
     * 批量保存条码数据
     * @param collection
     */
    public void barcode(Collection<Barcode> collection);
    /**
     * 查询申请记录
     * @param resKey
     *
     * @return
     */
    public ApplyRes getApplyRes(int resKey);
    /**
     * 更新条码申请信息
     * @param applyRes
     * @param status
     * @return
     */
    public boolean modifyAppRes(ApplyRes applyRes, int status);
}

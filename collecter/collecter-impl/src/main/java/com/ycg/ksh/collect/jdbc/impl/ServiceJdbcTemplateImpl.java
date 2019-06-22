package com.ycg.ksh.collect.jdbc.impl;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/31 0031
 */

import com.ycg.ksh.collect.jdbc.ServiceJdbcTemplate;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.constant.CompanyConfigType;
import com.ycg.ksh.constant.LocationType;
import com.ycg.ksh.constant.OrderFettleType;
import com.ycg.ksh.entity.collecter.GenerateBarcode;
import com.ycg.ksh.entity.collecter.OrderCollect;
import com.ycg.ksh.entity.persistent.ApplyRes;
import com.ycg.ksh.entity.persistent.Barcode;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;

/**
 * 业务库的JDBC操作
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/31 0031
 */
public class ServiceJdbcTemplateImpl extends AbstractJdbcTemplate implements ServiceJdbcTemplate {

    private static final String SQL_COLLECTLEASTTIMESUMMARYASSESS = "select min(CREATE_TIME) from t_order where FETTLE < ?";
    private static final String SQL_COLLECTORDERSUMMARYASSESS = "select o.ID, o.SHIPPER_ID, o.RECEIVE_ID, o.CONVEY_ID, o.CLIENT_TYPE, o.DELIVERY_TIME, o.RECEIVE_TIME, o.ARRIVAL_TIME, o.ARRIVED_TIME, o.CREATE_TIME, o.IS_RECEIPT, o.PICK_UP_WARNING, o.DELAY_WARNING, o.POSITIONING_CHECK, o.EVALUATION, o.IS_COMPLAINT, o.SIGN_FETTLE, p.CAR_TIME, d.COMPANY_KEY from t_order o LEFT JOIN t_plan_order p ON o.ID = p.ORDER_KEY LEFT JOIN t_plan_designate d ON (p.ID = d.PLAN_ID AND o.CONVEY_ID = d.LAST_COMPANY_KEY) where o.RECEIVE_TIME <= ? and o.DELIVERY_TIME >= ? and o.FETTLE = ?";
    private static final String SQL_COLLECTORDERSUMMARYTRACK = "select ID, SHIPPER_ID, RECEIVE_ID, CONVEY_ID, CLIENT_TYPE, INSERT_TYPE, POSITIONING_CHECK from t_order where FETTLE = ? and CREATE_TIME <= ?";
    private static final String SQL_COLLECTORDERSUMMARYTIMEOUT = "select ID, DELIVERY_TIME, RECEIVE_TIME, ARRIVAL_TIME, ARRIVED_TIME, CREATE_TIME from t_order where FETTLE < 4 and (DELIVERY_TIME <= ? OR ARRIVED_TIME <= ?)";
    private static final String SQL_LOADCOMPANYCONFIGVALUE = "select CONFIG_VALUE from t_company_config where COMPANY_KEY=? and CONFIG_KEY=?";
    private static final String SQL_COLLECTLOCATECOUNT = "select count(*) from t_location_track where HOST_TYPE=? and HOST_ID=?";
    private static final String SQL_MODIFYORDERPOSITIONINGCHECK = "update t_order set POSITIONING_CHECK=? where ID = ?";

    @Override
    public LocalDateTime collectLeastTimeSummaryAssess() {
        return queryForObject(SQL_COLLECTLEASTTIMESUMMARYASSESS, LocalDateTime.class, OrderFettleType.COMPLETE.getCode());
    }

    @Override
    public Collection<OrderCollect> collectOrderSummaryAssess(LocalDateTime leastCreateTime, LocalDateTime lastReciveTime) {
        try{
            return query(SQL_COLLECTORDERSUMMARYASSESS, (resultSet, i) -> new OrderCollect(
                    resultSet.getLong("ID"),
                    resultSet.getLong("SHIPPER_ID"),
                    resultSet.getLong("RECEIVE_ID"),
                    resultSet.getLong("CONVEY_ID"),
                    resultSet.getInt("CLIENT_TYPE"),
                    resultSet.getLong("COMPANY_KEY"),
                    ofNullable(resultSet.getTimestamp("DELIVERY_TIME"), resultSet.getTimestamp("CREATE_TIME")),
                    ofNullable(resultSet.getTimestamp("RECEIVE_TIME")),
                    ofNullable(resultSet.getTimestamp("ARRIVAL_TIME")),
                    ofNullable(resultSet.getTimestamp("ARRIVED_TIME")),
                    ofNullable(resultSet.getTimestamp("CREATE_TIME")),
                    resultSet.getInt("IS_RECEIPT"),
                    resultSet.getInt("PICK_UP_WARNING"),
                    resultSet.getInt("DELAY_WARNING"),
                    resultSet.getInt("POSITIONING_CHECK"),
                    resultSet.getInt("EVALUATION"),
                    resultSet.getBoolean("IS_COMPLAINT"),
                    resultSet.getInt("SIGN_FETTLE"),
                    ofNullable(resultSet.getTimestamp("CAR_TIME"))
            ), lastReciveTime.with(LocalTime.MAX).format(Globallys.DF_L_YMRHMS), leastCreateTime.with(LocalTime.MIN).format(Globallys.DF_L_YMRHMS), OrderFettleType.COMPLETE.getCode());
        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<OrderCollect> collectOrderSummaryTrack(LocalDateTime lastTime) {
        return query(SQL_COLLECTORDERSUMMARYTRACK, (rs, i) -> {
            return new OrderCollect( rs.getLong("ID"), rs.getLong("SHIPPER_ID"), rs.getLong("RECEIVE_ID"), rs.getLong("CONVEY_ID"), rs.getInt("CLIENT_TYPE"), rs.getInt("INSERT_TYPE"), rs.getInt("POSITIONING_CHECK") );
        }, OrderFettleType.ING.getCode(), lastTime.with(LocalTime.MAX).format(Globallys.DF_L_YMRHMS));
    }

    @Override
    public Collection<OrderCollect> collectOrderSummaryTimeout(LocalDateTime lastTime) {
        String localDateTime = lastTime.with(LocalTime.MAX).format(Globallys.DF_L_YMRHMS);
        return query(SQL_COLLECTORDERSUMMARYTIMEOUT, (resultSet, i) -> new OrderCollect(
                resultSet.getLong("ID"),
                ofNullable(resultSet.getTimestamp("DELIVERY_TIME"), resultSet.getTimestamp("CREATE_TIME")),
                ofNullable(resultSet.getTimestamp("RECEIVE_TIME")),
                ofNullable(resultSet.getTimestamp("ARRIVAL_TIME")),
                ofNullable(resultSet.getTimestamp("ARRIVED_TIME")),
                ofNullable(resultSet.getTimestamp("CREATE_TIME"))
        ), localDateTime, localDateTime);
    }

    @Override
    public String loadCompanyConfigValue(Long companyKey, CompanyConfigType configType) {

        return query(SQL_LOADCOMPANYCONFIGVALUE, resultSet -> {
            if(resultSet.next()){
                return resultSet.getString(1);
            }
            return null;
        }, companyKey, configType.getCode());

    }

    @Override
    public Integer collectLocateCount(LocationType locationType, Serializable hostKey) {
        return queryForObject(SQL_COLLECTLOCATECOUNT, Integer.class, locationType.getCode(), hostKey);
    }

    @Override
    public void modifyOrderPositioningCheck(Collection<OrderCollect> collection) {
        batchUpdate(SQL_MODIFYORDERPOSITIONINGCHECK, collection, 300, (preparedStatement, o) -> {
            preparedStatement.setInt(1, o.getPositioningCheck());
            preparedStatement.setLong(2, o.getId());
        });
    }

    @Override
    public void barcode(Collection<Barcode> collection) {
        String sql = "insert into  barcode_tab (resourceid, barcode, createtime, bindstatus, code_batch, groupid, userid, company_id) values (?, ?, ?, ?, ?, ?, ?, ?);";
        batchUpdate(sql, collection, 1000, (preparedStatement, code) -> {
            preparedStatement.setInt(1, code.getResourceid());
            preparedStatement.setString(2, code.getBarcode());
            preparedStatement.setTimestamp(3, new Timestamp(code.getCreatetime().getTime()));
            preparedStatement.setInt(4, code.getBindstatus());
            preparedStatement.setString(5, code.getCodeBatch());
            preparedStatement.setInt(6, code.getGroupid());
            preparedStatement.setInt(7, code.getUserid());
            preparedStatement.setLong(8, code.getCompanyId());
        });
    }

    @Override
    public ApplyRes getApplyRes(int resKey) {
        try {
            return queryForObject("select id, `number`, print_status, userid, groupid, company_id from resourcse_app_tab where id = " + resKey , new RowMapper<ApplyRes>() {
                @Override
                public ApplyRes mapRow(ResultSet resultSet, int i) throws SQLException {
                    ApplyRes res = new ApplyRes();
                    res.setId(resultSet.getInt("id"));
                    res.setNumber(resultSet.getInt("number"));
                    res.setPrintStatus(resultSet.getInt("print_status"));
                    res.setUserid(resultSet.getInt("userid"));
                    res.setGroupid(resultSet.getInt("groupid"));
                    res.setCompanyId(resultSet.getLong("company_id"));
                    return res;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean modifyAppRes(ApplyRes applyRes, int status) {
        return update("update  resourcse_app_tab set print_status=?, start_num=?, end_num=? where id=? and print_status = ?", applyRes.getPrintStatus(), applyRes.getStartNum(), applyRes.getEndNum(), applyRes.getId(), status) > 0;
    }
}

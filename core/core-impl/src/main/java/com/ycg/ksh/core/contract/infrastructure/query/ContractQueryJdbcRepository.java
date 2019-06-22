package com.ycg.ksh.core.contract.infrastructure.query;

import com.fty.baymax.sqlbuilder.OrderByType;
import com.fty.baymax.sqlbuilder.Query;
import com.fty.baymax.sqlbuilder.QueryBuilder;
import com.fty.baymax.sqlbuilder.Table;
import com.fty.baymax.sqlbuilder.condition.Conditions;
import com.fty.baymax.sqlbuilder.condition.LikeMatch;
import com.ycg.ksh.common.entity.Page;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.core.contract.search.ContractQueryRepository;
import com.ycg.ksh.core.contract.search.dto.CommodityConfigDtoJdbc;
import com.ycg.ksh.core.contract.search.dto.ContractDtoJdbc;
import com.ycg.ksh.core.contract.search.dto.IncomeRecordDtoJdbc;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * 合同计价查询实现
 *
 * @author: wangke
 * @create: 2019-01-04 10:00
 **/
@Repository
public class ContractQueryJdbcRepository implements ContractQueryRepository {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<ContractDtoJdbc> pricingSearch(CompanyConcise companyConcise, Integer contractType, String likeString, int num, int size) {
        QueryBuilder builder = Query.builder().select(
                new Table("t_contract", "o"));
        builder.and(Conditions.eq("o.company_key", companyConcise.getId()));
        if (contractType != null && contractType > 0) {
            builder.and(Conditions.eq("o.contract_type", contractType));
        }
        if (StringUtils.isNotBlank(likeString)) {
            builder.and(Conditions.or(Conditions.like("o.contract_no", likeString, LikeMatch.ANYWHERE), Conditions.like(
                    "opposite_name", likeString, LikeMatch.ANYWHERE
            )));
        }
        builder.orderBy("o.create_time", OrderByType.DESC);
        //建造查询sql语句
        Query query = builder.page((num - 1) * size, size).build();
        logger.info("select count sql -> {}", query.selectCount());
        logger.info("select sql -> {}", query.select());
        logger.info("select parameters -> {}", Arrays.toString(query.parameters()));

        //查询条数
        Long total = Optional.ofNullable(
                jdbcTemplate.queryForObject(query.selectCount(), query.parameters(), Long.class)
        ).orElse(0L);
        Collection<ContractDtoJdbc> collection = Collections.emptyList();

        if (total > 0) {
            collection = jdbcTemplate.query(query.select(), query.parameters(), (rs, i) -> {
                ContractDtoJdbc dtoJdbc = new ContractDtoJdbc();
                dtoJdbc.setId(rs.getLong("id"));
                dtoJdbc.setContractNo(rs.getString("contract_no"));
                dtoJdbc.setCustomerName(rs.getString("opposite_name"));
                dtoJdbc.setContractStartDate(Optional.ofNullable(DateUtils.toLocalDate(rs.getTimestamp("first_time"))).orElse(null));
                dtoJdbc.setContractEndDate(Optional.ofNullable(DateUtils.toLocalDate(rs.getTimestamp("second_time"))).orElse(null));
                dtoJdbc.setVerifyStatus(rs.getInt("verify_status"));
                return dtoJdbc;
            });
        }
        return new Page<ContractDtoJdbc>(num, size, total, collection);
    }

    @Override
    public long countByContractNo(String contractNo, Long exkey) {
        QueryBuilder builder = Query.builder().select(
                new Table("t_contract", "o"));
        builder.and(Conditions.eq("o.contract_no", contractNo));
        //建造查询sql语句
        Query query = builder.build();
        Long total = Optional.ofNullable(
                jdbcTemplate.queryForObject(query.selectCount(), query.parameters(), Long.class)
        ).orElse(0L);
        return total;
    }

    @Override
    public Page<CommodityConfigDtoJdbc> commoditySearch(Long contractKey, String likeString, int num, int size) {
        QueryBuilder builder = Query.builder().select(
                new Table("t_contract_commodity_config", "c"));
        builder.and(Conditions.eq("c.contract_key", contractKey));
        if (StringUtils.isNotBlank(likeString)) {
            builder.and(Conditions.or(Conditions.like("c.commodity_code", likeString, LikeMatch.ANYWHERE),
                    Conditions.like("c.commodity_name", likeString, LikeMatch.ANYWHERE)));
        }
        //建造查询sql语句
        Query query = builder.page((num - 1) * size, size).build();
        logger.info("select sql -> {}", query.select());
        logger.info("select parameters -> {}", Arrays.toString(query.parameters()));
        Long total = Optional.ofNullable(
                jdbcTemplate.queryForObject(query.selectCount(), query.parameters(), Long.class)
        ).orElse(0L);
        Collection<CommodityConfigDtoJdbc> collection = Collections.emptyList();
        if (total > 0) {
            collection = jdbcTemplate.query(query.select(), query.parameters(), (rs, i) -> {
                CommodityConfigDtoJdbc dtoJdbc = new CommodityConfigDtoJdbc();
                dtoJdbc.setId(rs.getLong("id"));
                dtoJdbc.setCommodityCode(rs.getString("commodity_code"));
                dtoJdbc.setCommodityName(rs.getString("commodity_name"));
                dtoJdbc.setFareType(rs.getInt("fare_type"));
                dtoJdbc.setUnitVolume(rs.getDouble("unit_volume"));
                dtoJdbc.setUnitWeight(rs.getDouble("unit_weight"));
                return dtoJdbc;
            });
        }
        return new Page<CommodityConfigDtoJdbc>(num, size, total, collection);
    }

    @Override
    public Page<IncomeRecordDtoJdbc> incomeRecordSearch(String likeString, Integer statuts, LocalDateTime deliveryDateStart,
                                                        LocalDateTime deliveryDateEnd, Long companKey, int num, int size) {

        QueryBuilder builder = Query.builder().select(
                new Table("t_income_record", "c"));
        builder.and(Conditions.eq("companyKey", companKey));
        if (StringUtils.isNotBlank(likeString)) {
            builder.and(Conditions.or(Conditions.like("delivery_customer", likeString, LikeMatch.ANYWHERE),
                    Conditions.like("receive_customer", likeString, LikeMatch.ANYWHERE), Conditions.like("delivery_no", likeString,
                            LikeMatch.ANYWHERE)));
        }
        if (null != statuts) {
            builder.and(Conditions.eq("confirmstate", statuts));
        }
        //建造查询sql语句
        Query query = builder.page((num - 1) * size, size).build();
        logger.info("select sql -> {}", query.select());
        logger.info("select parameters -> {}", Arrays.toString(query.parameters()));
        Long total = Optional.ofNullable(
                jdbcTemplate.queryForObject(query.selectCount(), query.parameters(), Long.class)
        ).orElse(0L);

        Collection<IncomeRecordDtoJdbc> collection = Collections.emptyList();
        if (total > 0) {
            collection = jdbcTemplate.query(query.select(), query.parameters(), (rs, i) -> {
                IncomeRecordDtoJdbc dtoJdbc = new IncomeRecordDtoJdbc();
                dtoJdbc.setSystemId(rs.getLong("order_key"));
                dtoJdbc.setDeliveryDate(Optional.ofNullable(DateUtils.toLocalDateTime(rs.getTimestamp("delivery_date"))).orElse(null));
                dtoJdbc.setDeliveryCustomer(rs.getString("delivery_customer"));
                dtoJdbc.setReceiveCustomer(rs.getString("receive_customer"));
                dtoJdbc.setDeliveryNo(rs.getString("delivery_no"));
                dtoJdbc.setTotalWeight(rs.getDouble("total_weight"));
                dtoJdbc.setTotalVolume(rs.getDouble("total_volume"));
                dtoJdbc.setTotalQuantity(rs.getInt("total_quantity"));
                dtoJdbc.setTransportReceivable(rs.getDouble("transport_receivable"));
                dtoJdbc.setOtherTotalReceivable(rs.getDouble("other_total_receivable"));
                dtoJdbc.setTotalReceivable(rs.getDouble("total_receivable"));
                dtoJdbc.setConfirmState(rs.getInt("confirmstate"));
                dtoJdbc.setOtherReceivableRemark(rs.getString("remarks"));
                dtoJdbc.setCreateTime(Optional.ofNullable(DateUtils.toLocalDateTime(rs.getTimestamp("createTime"))).orElse(null));
                return dtoJdbc;
            });
        }
        return new Page<IncomeRecordDtoJdbc>(num, size, total, collection);
    }
}

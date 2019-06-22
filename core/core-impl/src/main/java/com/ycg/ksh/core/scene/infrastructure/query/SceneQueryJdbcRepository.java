package com.ycg.ksh.core.scene.infrastructure.query;

import com.fty.baymax.sqlbuilder.*;
import com.fty.baymax.sqlbuilder.condition.Conditions;
import com.fty.baymax.sqlbuilder.condition.LikeMatch;
import com.ycg.ksh.common.entity.Page;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.core.common.domain.CustomerIdentify;
import com.ycg.ksh.core.scene.search.SceneQueryRepository;
import com.ycg.ksh.core.scene.search.dto.VehicleConfirmDto;
import com.ycg.ksh.core.scene.infrastructure.persistence.VehicleRegistrationCondition;
import com.ycg.ksh.entity.common.constant.PartnerType;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * 现场查询基础支撑jdbc实现
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/13 0013
 */
@Repository
public class SceneQueryJdbcRepository implements SceneQueryRepository {

    @Resource
    private JdbcTemplate jdbcTemplate;


    protected LocalDateTime ofNullable(Timestamp timestamp){
        return Optional.ofNullable(timestamp).map(Timestamp::toLocalDateTime).orElse(null);
    }

    @Override
    public Page<VehicleConfirmDto> searchVehicleConfirmPage(VehicleRegistrationCondition condition, int num, int size) throws ParameterException, BusinessException {
        //select o.id, o.delivery_no ,o.shipper_id, o.receive_id, o.convey_id, o.client_type, oe.care_no, oe.conveyer_name, oe.conveyer_contact, svr.arrival_time, svr.arrival_type_note, svr.in_drivername, svr.in_drivercontact, svr.in_license from t_order o left join t_order_extra oe on o.id = oe.data_key and oe.data_type = 1 left join t_scene_vehicle_registration svr on o.id = svr.order_key;
        QueryBuilder builder = Query.builder().select(
                new Table("t_order", "o").columns("id", "delivery_no", "shipper_id", "receive_id", "convey_id", "client_type","collect_time")
        ).join(
                new JoinTable(JoinType.LEFT, "t_order_extra", "oe").columns("care_no", "driver_name", "driver_contact").on(Conditions.and(Conditions.eqColumn("o.id", "oe.data_key"), Conditions.eq("oe.data_type", 1)))
        ).join(
                new JoinTable(JoinType.LEFT, "t_scene_vehicle_registration", "svr").columns("arrival_time", "arrival_type", "in_driverName", "in_driverContact", "in_license").on("o.id", "svr.order_key")
        );
        builder.and(Conditions.eq("o.shipper_id", condition.getShipperKey()));
        if(StringUtils.isNotBlank(condition.getLikeString())){
            if(CollectionUtils.isNotEmpty(condition.getCompanyKeys())){
                builder.and(Conditions.or(Conditions.like("o.delivery_no", condition.getLikeString(), LikeMatch.ANYWHERE), Conditions.in("o.receive_id", condition.getCompanyKeys()), Conditions.in("o.convey_id", condition.getCompanyKeys())));
            }else{
                builder.and(Conditions.like("o.delivery_no", condition.getLikeString(), LikeMatch.ANYWHERE));
            }
        }
        if(condition.getArrivalType() != null && condition.getArrivalType() > 0){
            builder.and(Conditions.eq("svr.arrival_type", condition.getArrivalType()));
        }
        if(condition.getStatus() != null){//1:已到车
            builder.and((condition.getStatus() - 1 == 0) ? Conditions.isNotNull("svr.order_key") : Conditions.isNull("svr.order_key"));
        }
        if(condition.getMinCollectTime() != null){
            builder.and(Conditions.ge("o.collect_time", condition.getMinCollectTime()));
        }
        if(condition.getMaxCollectTime()!= null){
            builder.and(Conditions.le("o.collect_time", condition.getMaxCollectTime()));
        }
        builder.orderBy("o.fettle", OrderByType.ASC);
        //建造查询sql语句
        Query query = builder.page((num - 1) * size, size).build();

        logger.info("select count sql -> {}", query.selectCount());
        logger.info("select sql -> {}", query.select());
        logger.info("select parameters -> {}", Arrays.toString(query.parameters()));

        //查询总数量
        Long total = Optional.ofNullable(
                jdbcTemplate.queryForObject(query.selectCount(), query.parameters(), Long.class)
        ).orElse(0L);
        Collection<VehicleConfirmDto> collection = Collections.emptyList();
        if(total > 0){
            collection = jdbcTemplate.query(query.select(), query.parameters(), (rs, i) -> {
                VehicleConfirmDto dto = new VehicleConfirmDto();
                dto.setOrderKey(rs.getLong("id"));//运单编号
                dto.setDeliveryNo(rs.getString("delivery_no"));//送货单号

                Integer clientType = rs.getInt("client_type");

                dto.setShipper(new CustomerIdentify(PartnerType.SHIPPER, rs.getLong("shipper_id"), clientType));//货主
                dto.setReceive(new CustomerIdentify(PartnerType.RECEIVE, rs.getLong("receive_id"), clientType));//收货客户
                dto.setConvey(new CustomerIdentify(PartnerType.CONVEY, rs.getLong("convey_id"), clientType));//承运商

                dto.setCollectTime(ofNullable(rs.getTimestamp("collect_time")));//要求提货时间
                dto.setDriverName(rs.getString("driver_name"));//司机名称
                dto.setDriverContact(rs.getString("driver_contact"));//司机电话
                dto.setLicense(rs.getString("care_no"));//车牌号
                dto.setArrivalTime(ofNullable(rs.getTimestamp("arrival_time")));//到车时间
                dto.setVehicleStatus((dto.getArrivalTime() == null)? 0 : 1);//到车状态
                dto.setConfirmStatus(rs.getInt("arrival_type"));//到车确认状态
                dto.setConfirmDriverName(rs.getString("in_driverName"));//到车确认司机名称
                dto.setConfirmDriverContact(rs.getString("in_driverContact"));//到车确认司机电话
                dto.setConfirmLicense(rs.getString("in_license"));//到车确认车牌号
                return dto;
            });
        }
        return new Page<VehicleConfirmDto>(num, size, total, collection);
    }
}

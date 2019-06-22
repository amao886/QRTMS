package com.ycg.ksh.core.contract.application.transform;

import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.core.contract.application.dto.ContractDto;
import com.ycg.ksh.core.contract.application.dto.IncomeRecordDto;
import com.ycg.ksh.core.contract.domain.model.CommodityConfig;
import com.ycg.ksh.core.contract.domain.model.IncomeRecord;
import com.ycg.ksh.core.contract.search.dto.ContractDtoJdbc;
import com.ycg.ksh.core.contract.search.dto.IncomeRecordDtoJdbc;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;

import javax.xml.soap.Detail;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 收支有关数据转换
 *
 * @author: wangke
 * @create: 2019-01-04 17:15
 **/

public class IncomeDtoTransformer {

    public IncomeRecord IncomeRecordDtoByOrder(IncomeRecordDto dto, OrderAlliance orderAlliance, Long companyKey) {
        return new IncomeRecord(
                orderAlliance.getShipper().getCompanyName(), orderAlliance.getReceive().getCompanyName(), orderAlliance.getDeliveryNo(),
                orderAlliance.getWeight(), orderAlliance.getVolume(), orderAlliance.getQuantity(), dto.getTransportReceivable(), dto.getOtherTotalReceivable()
                , dto.getTransportReceivable() + dto.getOtherTotalReceivable(), 0, dto.getOtherReceivableRemark(), orderAlliance.getId(),
                DateUtils.toLocalDateTime(orderAlliance.getDeliveryTime()), dto.getCreateTime(), companyKey);
    }

    public Collection<IncomeRecordDto> tranIncomeDtoJdbcList(Collection<IncomeRecordDtoJdbc> dtoJdbcs) {
        if (CollectionUtils.isNotEmpty(dtoJdbcs)) {
            return dtoJdbcs.stream().map(v -> {
                IncomeRecordDto dto = new IncomeRecordDto();
                try {
                    PropertyUtils.copyProperties(dto, v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return dto;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}

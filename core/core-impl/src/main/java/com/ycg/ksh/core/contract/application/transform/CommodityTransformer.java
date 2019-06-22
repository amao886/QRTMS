package com.ycg.ksh.core.contract.application.transform;

import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.core.contract.application.dto.CommodityConfigDto;
import com.ycg.ksh.core.contract.domain.model.CommodityConfig;
import com.ycg.ksh.core.contract.search.dto.CommodityConfigDtoJdbc;
import com.ycg.ksh.core.util.Constants;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物料数据转换
 *
 * @author: wangke
 * @create: 2019-01-10 16:21
 **/

public class CommodityTransformer {

    public List<CommodityConfig> transformerCommdityByArray(Collection<Object[]> objects) {
        List<CommodityConfig> commodityConfigList = new ArrayList<>();
        if (objects != null && objects.size() > 0) {
            CommodityConfig commodityConfig = null;
            for (Object[] object : objects) {
                commodityConfig = new CommodityConfig(object[0].toString(), object[1].toString(), Double.valueOf(object[2].toString())
                        , Double.valueOf(object[3].toString()), getFareType(object[4].toString()));
                commodityConfigList.add(commodityConfig);
            }
        }
        return commodityConfigList;
    }

    public int getFareType(String fareTypes) {
        if (StringUtils.isNotBlank(fareTypes)) {
            if (fareTypes.equals(Constants.QUANTITY_STR)) {
                return Constants.PRICING_DIMENSION_QUANTITY;
            } else if (fareTypes.equals(Constants.VEHICLE_STR)) {
                return Constants.PRICING_DIMENSION_VEHICLE;
            } else if (fareTypes.equals(Constants.VOLUME_STR)) {
                return Constants.PRICING_DIMENSION_VOLUME;
            } else {
                return Constants.PRICING_DIMENSION_WEIGHT;
            }
        }
        return 0;

    }

    public Collection<CommodityConfigDto> transformerDto(Collection<CommodityConfigDtoJdbc> dtoJdbc) {
        if (CollectionUtils.isNotEmpty(dtoJdbc)) {
            return dtoJdbc.stream().map(v -> {
                CommodityConfigDto dto = new CommodityConfigDto();
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

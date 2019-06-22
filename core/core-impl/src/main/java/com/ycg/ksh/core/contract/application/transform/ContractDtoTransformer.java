package com.ycg.ksh.core.contract.application.transform;

import com.ycg.ksh.common.util.RegionUtils;
import com.ycg.ksh.core.contract.application.dto.*;
import com.ycg.ksh.core.contract.domain.model.*;
import com.ycg.ksh.core.contract.search.dto.ContractDtoJdbc;
import com.ycg.ksh.entity.persistent.enterprise.CompanyCustomer;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 合同数据传输对象转换
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2019/01/03 0003
 */
public class ContractDtoTransformer {

    public List<FragmentaryUnitPrice> transformFragmentaryUnitPrice(List<IntervalPriceDto> dtos, RegionDto origin) {
        return dtos.stream().map(d -> new FragmentaryUnitPrice(origin.address(), d.getArea().address(), d.getCityLevel(), d.getNum())).collect(Collectors.toList());
    }

    public CapacityInterval[] transformCapacityInterval(IntervalDto[] intervals) {
        return Arrays.stream(intervals).map(i -> new CapacityInterval(i.getStartNum(), i.getEndNum())).toArray(CapacityInterval[]::new);
    }

    public FragmentaryValuation transformFragmentaryValuation(ValuationDto dto, RegionDto origin) {
        return new FragmentaryValuation(new ValidityPeriod(dto.getUseStartDate(), dto.getUseEndDate()), dto.getGoodsNature(), dto.getGoodsCalculMode(), dto.getGoodsUnit(), transformCapacityInterval(dto.getIntervals()), transformFragmentaryUnitPrice(dto.getFreightList(), origin));
    }

    public List<FragmentaryValuation> transformFragmentaryValuation(Collection<ValuationDto> dtos, RegionDto origin) {
        return dtos.stream().map(i -> transformFragmentaryValuation(i, origin)).collect(Collectors.toList());
    }

    public ContractDetailDto dtoDetailFromContract(Contract c) {
        LocalDate s = c.getPeriod().getFirstTime().toLocalDate(), e = c.getPeriod().getSecondTime().toLocalDate();
        ContractDetailDto contractDto = new ContractDetailDto();
        contractDto.base(c.getId(), s, e, c.getContractType(), c.getContractNo());
        String pp = c.getCustomerRegion().getProvince(), cc = c.getCustomerRegion().getCity(), dd = c.getCustomerRegion().getDistrict();
        contractDto.customer(c.getOppositeKey(), c.getOppositeName(), c.getIndustryCategory(), c.getContactName(), c.getTelephoneNumber(), c.getMobileNumber(), pp, cc, dd, c.getCustomerAddress(), c.getCustomerRegion().address() + c.getCustomerAddress());
        contractDto.setList(dtoFromFragmentaryValuation(c.getFragmentaries()));
        contractDto.setAuditResultList(dtoFromContractVerify(c.getContractVerifies()));
        return contractDto;
    }

    public ContractDto dtoFromContract(Contract c) {
        LocalDate s = c.getPeriod().getFirstTime().toLocalDate(), e = c.getPeriod().getSecondTime().toLocalDate();
        ContractDto contractDto = new ContractDto();
        contractDto.base(c.getId(), s, e, c.getContractType(), c.getContractNo());
        String pp = c.getCustomerRegion().getProvince(), cc = c.getCustomerRegion().getCity(), dd = c.getCustomerRegion().getDistrict();
        contractDto.customer(c.getOppositeKey(), c.getOppositeName(), c.getIndustryCategory(), c.getContactName(), c.getTelephoneNumber(), c.getMobileNumber(), pp, cc, dd, c.getCustomerAddress(), c.getCustomerRegion().address() + c.getCustomerAddress());
        return contractDto;
    }

    public List<ContractVerifyDto> dtoFromContractVerify(Collection<ContractVerify> verifies) {
        if (null != verifies && verifies.size() > 0) {
            return verifies.stream().map(v -> new ContractVerifyDto(v.getVerifyStatus(), v.getVerifyTime(), v.getReason())).collect(Collectors.toList());
        }
        return null;
    }

    public List<ValuationDto> dtoFromFragmentaryValuation(Collection<FragmentaryValuation> valuations) {
        return valuations.stream().map(this::dtoFromFragmentaryValuation).collect(Collectors.toList());
    }

    public ValuationDto dtoFromFragmentaryValuation(FragmentaryValuation v) {
        LocalDate s = v.getPeriod().getFirstTime().toLocalDate(), e = v.getPeriod().getSecondTime().toLocalDate();
        return new ValuationDto(v.getCommodityCategory(), v.getFareType(), v.getCommodityUnit(), s, e, dtoFromCapacityInterval(v.getIntervals()), dtoFromFragmentaryUnitPrice(v.getUnits()));
    }

    public IntervalDto[] dtoFromCapacityInterval(CapacityInterval[] intervals) {
        return Arrays.stream(intervals).map(i -> new IntervalDto(i.getMin(), i.getMax())).toArray(IntervalDto[]::new);
    }

    public List<IntervalPriceDto> dtoFromFragmentaryUnitPrice(Collection<FragmentaryUnitPrice> unitPrices) {
        return unitPrices.stream().map(u -> new IntervalPriceDto(toRegionDto(u.getDestination()), u.getCitylv(), u.getPrices())).collect(Collectors.toList());
    }

    public Collection<ContractDto> tranContractDtoJdbcList(Collection<ContractDtoJdbc> dtoJdbcs) {
        if (CollectionUtils.isNotEmpty(dtoJdbcs)) {
            return dtoJdbcs.stream().map(v -> {
                ContractDto dto = new ContractDto();
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

    public ContractDto tranContractDtoJdbc(ContractDtoJdbc dtoJdbc) throws Exception {
        ContractDto dto = new ContractDto();
        if (null != dtoJdbc) {
            PropertyUtils.copyProperties(dto, dtoJdbc);
        }
        return null;
    }

    private RegionDto toRegionDto(String areaString) {
        String[] areas = RegionUtils.analyze(areaString);
        return new RegionDto(areas[0], areas[1], areas[2]);
    }
}

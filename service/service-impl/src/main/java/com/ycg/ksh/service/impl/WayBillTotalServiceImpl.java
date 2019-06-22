package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.WaybillTotalView;
import com.ycg.ksh.entity.service.MergeBillTotal;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.service.persistence.WayBillTotalViewMapper;
import com.ycg.ksh.service.api.WayBillTotalService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;

/**
 * @Author:wangke
 * @Description:
 * @Date:Create in 14:28 2017/12/27
 * @Modified By:
 */
@Service("ksh.core.service.wayBillTotalService")
public class WayBillTotalServiceImpl implements WayBillTotalService {

    @Resource
    WayBillTotalViewMapper wayBillTotalViewMapper;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public CustomPage<WaybillTotalView> queryTotalPage(MergeBillTotal mergeBillTotal, PageScope pageScope) {
        if (pageScope == null) {
            pageScope = PageScope.DEFAULT;
        }
        //如果不是全部则组装数据
        if (mergeBillTotal.getFlag() != null && mergeBillTotal.getFlag() < 99) {
            if (1 == mergeBillTotal.getFlag()) {
                mergeBillTotal.setStartTime(DateUtils.getMinusTime(1, formatter));
                mergeBillTotal.setEndTime(DateUtils.getMinusTime(1, formatter));
            } else if (2 == mergeBillTotal.getFlag()) {
                mergeBillTotal.setStartTime(DateUtils.getMinusTime(7, formatter));
                mergeBillTotal.setEndTime(DateUtils.getDateTime());
            } else if (3 == mergeBillTotal.getFlag()) {
                mergeBillTotal.setStartTime(DateUtils.getMinusTime(30, formatter));
                mergeBillTotal.setEndTime(DateUtils.getMinusTime(1, formatter));
            }
        }
        if (StringUtils.isNotEmpty(mergeBillTotal.getCompanyName())) {
            mergeBillTotal.setCompanyName(StringUtils.trimToEmpty(mergeBillTotal.getCompanyName()));
        }
        Page<WaybillTotalView> page = wayBillTotalViewMapper.listPage(mergeBillTotal, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
        return new CustomPage<WaybillTotalView>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
    }
}

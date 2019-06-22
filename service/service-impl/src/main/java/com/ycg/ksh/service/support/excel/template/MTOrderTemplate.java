package com.ycg.ksh.service.support.excel.template;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/17
 */

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.entity.persistent.moutai.Order;
import com.ycg.ksh.service.support.excel.ConvertObjectByExcel;
import com.ycg.ksh.service.support.excel.Property;
import com.ycg.ksh.service.support.excel.convert.ConvertHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 茅台导入模板
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/17
 */
public class MTOrderTemplate extends ConvertObjectByExcel<Order> {

    private static final Collection<ConvertHandler> ORDER_MAPPINGS;

    static {
        //票号,客户编号,规格,销售量|瓶数,销售量|折合件数,购货单位
        ORDER_MAPPINGS = new ArrayList<ConvertHandler>(){
            private static final long serialVersionUID = 1L;
            {
                add(new ConvertHandler(new Property("scheduleNo", "调拨编号"), 0, true, new Validator[] { Validator.NOTBLANK }));
                add(new ConvertHandler(new Property("customerNo", "客户编号"), 1, new Validator[] { Validator.NOTBLANK }));
                add(new ConvertHandler(new Property("specification", "规格"), 2));
                add(new ConvertHandler(new Property("bottles", "瓶数"), 3, new Validator[] { Validator.NUMBER }));
                add(new ConvertHandler(new Property("quantity", "件数"), 4, new Validator[] { Validator.NUMBER }));
                add(new ConvertHandler(new Property("buyOrg", "购货单位"), 5));
            }
        };
    }

    @Override
    public Collection<ConvertHandler> analyzeObjects() {
        return ORDER_MAPPINGS;
    }

    @Override
    public Collection<ConvertHandler> analyzeCommodity() {
        return Collections.emptyList();
    }

    @Override
    public ConvertObjectByExcel readExcel(FileEntity fileEntity) throws Exception {
        return super.readExcel(fileEntity, 1, 6);
    }

    @Override
    public void mergeSomething(Order object, Object[] excelObjects) throws Exception { }
}

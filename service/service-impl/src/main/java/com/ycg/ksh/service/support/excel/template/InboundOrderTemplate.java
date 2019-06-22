package com.ycg.ksh.service.support.excel.template;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.entity.persistent.depot.InboundOrder;
import com.ycg.ksh.service.support.excel.ConvertObjectByExcel;
import com.ycg.ksh.service.support.excel.Property;
import com.ycg.ksh.service.support.excel.convert.ConvertHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @Auther: wangke
 * @Date: 2018/9/5 11:32
 * @Description:
 */
public class InboundOrderTemplate extends ConvertObjectByExcel<InboundOrder> {

    private static final Collection<ConvertHandler> InboundOrders;

    static {
        //"交货单号","客户名称","物料名称","实际发货时间", "拣配数量","物料号","交货数量","详细批次","车牌号"
        InboundOrders = new ArrayList<ConvertHandler>(){
            private static final long serialVersionUID = 1L;
            {
                add(new ConvertHandler(new Property("deliveryNo", "交货单号"), 0, true, new Validator[] { Validator.NOTBLANK }));//交货单号
                add(new ConvertHandler(new Property("customerName", "客户名称"), 1, true, new Validator[] { Validator.NOTBLANK }));//客户名称
                add(new ConvertHandler(new Property("materialName", "物料名称"), 2, true, new Validator[] { Validator.NOTBLANK }));//物料名称
                add(new ConvertHandler(new Property("deliveryTime", "实际发货时间"), 3, true, new Validator[] { Validator.NOTBLANK }));//实际发货时间
                add(new ConvertHandler(new Property("pickingQuantity", "拣配数量"), 4, true, new Validator[] { Validator.NOTBLANK }));//拣配数量
                add(new ConvertHandler(new Property("pickingNo", "物料号"), 5, true, new Validator[] { Validator.NOTBLANK }));//物料号
                add(new ConvertHandler(new Property("deliveryQuantity", "交货数量"), 6, true, new Validator[] { Validator.NOTBLANK }));//交货数量
                add(new ConvertHandler(new Property("batchNumber", "详细批次"), 7, true, new Validator[] { Validator.NOTBLANK }));//详细批次
                add(new ConvertHandler(new Property("licensePlate", "车牌号"), 8, new Validator[] {Validator.NOTBLANK}));//车牌号
            }
        };
    }

    @Override
    public Collection<ConvertHandler> analyzeObjects() {
        return InboundOrders;
    }

    @Override
    public Collection<ConvertHandler> analyzeCommodity() {
        return Collections.emptyList();
    }

    @Override
    public ConvertObjectByExcel readExcel(FileEntity fileEntity) throws Exception {
        return super.readExcel(fileEntity, 1, 9);
    }

    @Override
    public void mergeSomething(InboundOrder object, Object[] excelObjects) throws Exception {

    }
}

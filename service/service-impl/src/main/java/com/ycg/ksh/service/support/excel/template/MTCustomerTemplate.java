package com.ycg.ksh.service.support.excel.template;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/17
 */

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.entity.persistent.moutai.Customer;
import com.ycg.ksh.service.support.excel.ConvertObjectByExcel;
import com.ycg.ksh.service.support.excel.Property;
import com.ycg.ksh.service.support.excel.convert.ConvertHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 茅台客户导入模板
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/17
 */
public class MTCustomerTemplate extends ConvertObjectByExcel<Customer> {

    private static final Collection<ConvertHandler> MAPPINGS;

    static {
        //客户编号,省份,城市,客户名称,注册地址,联系人,联系电话,传真  .客户名称
        MAPPINGS = new ArrayList<ConvertHandler>(){
            private static final long serialVersionUID = 1L;
            {
                add(new ConvertHandler(new Property("customerNo", "客户编码"), 0, true, new Validator[] { Validator.NOTBLANK }));
                add(new ConvertHandler(new Property("province", "省"), 1, new Validator[] { Validator.NOTBLANK }));
                add(new ConvertHandler(new Property("city", "市"), 2, new Validator[] { Validator.NOTBLANK }));
                add(new ConvertHandler(new Property("customerName", "客户名称"), 3, new Validator[] { Validator.NOTBLANK }));
                add(new ConvertHandler(new Property("address", "详细地址"), 4, new Validator[] { Validator.NOTBLANK }));
                add(new ConvertHandler(new Property("contactName", "联系人"), 5));
                add(new ConvertHandler(new Property("contactPhone", "联系电话"), 6));
                add(new ConvertHandler(new Property("fax", "传真"), 7));
            }
        };
    }

    @Override
    public Collection<ConvertHandler> analyzeObjects() {
        return MAPPINGS;
    }

    @Override
    public Collection<ConvertHandler> analyzeCommodity() {
        return Collections.emptyList();
    }

    @Override
    public ConvertObjectByExcel readExcel(FileEntity fileEntity) throws Exception {
        return super.readExcel(fileEntity, 1, 8);
    }

    @Override
    public void mergeSomething(Customer object, Object[] excelObjects) throws Exception {}
}

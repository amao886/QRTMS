/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-19 11:39:51
 */
package com.ycg.ksh.service.support.excel.template;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.util.BeanUtils;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.entity.persistent.Goods;
import com.ycg.ksh.entity.service.MergeWaybill;
import com.ycg.ksh.service.support.excel.ConvertObjectByExcel;
import com.ycg.ksh.service.support.excel.Property;
import com.ycg.ksh.service.support.excel.convert.ConvertHandler;
import com.ycg.ksh.service.support.excel.convert.MobileConverter;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 从excel数据转换成运单信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-19 11:39:51
 */
public class DefaultTemplate extends ConvertObjectByExcel<MergeWaybill> {
    
    private static final Collection<ConvertHandler> WAYBILL_MAPPINGS;
    private static final Collection<ConvertHandler> GOODS_MAPPINGS;

    static {
        //"货主名称","客户料号","物料名称","收货客户", "重量（kg）","体积（m³）","数量（件）","收货地址","联系人","联系电话","发货后天数","时间点 ","订单摘要"
        WAYBILL_MAPPINGS = new ArrayList<ConvertHandler>(){
            private static final long serialVersionUID = 1L;
            {
                add(new ConvertHandler(new Property("shipperName", "货主名称"), 0, true, new Validator[] { Validator.NOTBLANK }));//货主名称
                add(new ConvertHandler(new Property("shipperAddress", "发货地址"), 1, true, new Validator[] { Validator.NOTBLANK }));//发货地址
                add(new ConvertHandler(new Property("shipperContactName", "发货联系人"), 2, true, new Validator[] { Validator.NOTBLANK }));//发货联系人
                add(new ConvertHandler(new Property("shipperContactTel", "发货联系电话"), 3, true, new Validator[] { Validator.NOTBLANK }));//发货联系电话

                add(new ConvertHandler(new Property("receiverName", "收货客户"), 4, true, new Validator[] { Validator.NOTBLANK }));//收货客户
                add(new ConvertHandler(new Property("receiveAddress", "收货地址"), 5, true, new Validator[] { Validator.NOTBLANK }));//收货地址
                add(new ConvertHandler(new Property("contactName", "联系人"), 6, true, new Validator[] { Validator.NOTBLANK }));//联系人
                add(new ConvertHandler(new Property("contactPhone", "联系电话"), 7, true, new Validator[] { Validator.NOTBLANK }, new MobileConverter()));//联系电话
                add(new ConvertHandler(new Property("arriveDay", "发货后天数"), 13, new Validator[] {Validator.NUMBER}));//发货后天数
                add(new ConvertHandler(new Property("arriveHour", "时间点"), 14, new Validator[] {Validator.NUMBER}));//时间点
            }
        };
        GOODS_MAPPINGS = new ArrayList<ConvertHandler>(){
            private static final long serialVersionUID = 1L;
            {
                add(new ConvertHandler(new Property("goodsType", "客户料号"), 8));//客户料号
                add(new ConvertHandler(new Property("goodsName", "物料名称"), 9));//物料名称
                add(new ConvertHandler(new Property("goodsWeight", "重量"), 10, new Validator[] {Validator.DECIMAL}));//重量（kg）
                add(new ConvertHandler(new Property("goodsVolume", "体积"), 11, new Validator[] {Validator.DECIMAL}));//体积（m³）
                add(new ConvertHandler(new Property("goodsQuantity", "数量"), 12, new Validator[] {Validator.NUMBER}));//数量（件）
                add(new ConvertHandler(new Property("summary", "订单摘要"), 15));//订单摘要
            }
        };
    }
    @Override
    public ConvertObjectByExcel readExcel(FileEntity fileEntity) throws Exception {
        return super.readExcel(fileEntity, 3, 15);
    }

    @Override
    public Collection<ConvertHandler> analyzeObjects() {
        return WAYBILL_MAPPINGS;
    }

    @Override
    public Collection<ConvertHandler> analyzeCommodity() {
        return GOODS_MAPPINGS;
    }

    @Override
    public void mergeSomething(MergeWaybill object, Object[] excelObjects) throws Exception {
        Collection<Goods> collection = object.getGoods();
        if(collection == null) {
            collection = new ArrayList<Goods>();
        }
        BeanUtilsBean beanUtils = BeanUtils.build();
        Goods goods = new Goods();
        for (ConvertHandler handler : analyzeCommodity()) {
            convert(goods, excelObjects, handler);
        }
        collection.add(goods);
        object.setGoods(collection);
    }
}

package com.ycg.ksh.service.support.excel.template;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/13
 */

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.entity.service.enterprise.OrderTemplate;
import com.ycg.ksh.service.support.excel.ConvertObjectByExcel;
import com.ycg.ksh.service.support.excel.Property;
import com.ycg.ksh.service.support.excel.convert.ConvertHandler;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 九阳模板
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/13
 */
public class JoyoungTemplate extends ConvertObjectByExcel<OrderTemplate> {

    private static final Collection<ConvertHandler> ORDER_MAPPINGS;
    private static final Collection<ConvertHandler> COMMODITY_MAPPINGS;

    static {
        //1配送单编号,2订单编号,3客户名称,4物流商,5物料编号,6产品描述,7单位,8数量,9箱数,10体积,11重量,12收货人,13联系方式,14收货地址,15备注1,16日期,17补发前单号,18子库名称,19OA单号
        ORDER_MAPPINGS = new ArrayList<ConvertHandler>(){
            private static final long serialVersionUID = 1L;
            {
                add(new ConvertHandler(new Property("deliveryNo", "配送单号"), 1, true, new Validator[] { Validator.NOTBLANK }));//配送单号
                add(new ConvertHandler(new Property("orderNo", "订单编号"), 2));//订单编号
                add(new ConvertHandler(new Property("receiveName", "客户名称"), 3, new Validator[] { Validator.NOTBLANK }));//客户名称
                add(new ConvertHandler(new Property("conveyName", "物流商"), 4, new Validator[] { Validator.NOTBLANK }));//物流商
                add(new ConvertHandler(new Property("receiverName", "收货联系人"), 12));//收货联系人
                add(new ConvertHandler(new Property("receiverContact", "收货联系电话"), 13));//收货联系电话
                add(new ConvertHandler(new Property("receiveAddress", "收货地址"), 14, new Validator[] { Validator.NOTBLANK }));//收货地址
                add(new ConvertHandler(new Property("remark", "备注1", true), 15));//备注1
                add(new ConvertHandler(new Property("deliveryTime", "发货日期"), 16));//发货日期
                add(new ConvertHandler(new Property("remark", "补发前单号", true), 17));//补发前单号
                add(new ConvertHandler(new Property("remark", "子库名称", true), 18));//子库名称
                add(new ConvertHandler(new Property("remark", "OA单号", true), 19));//OA单号
            }
        };
        COMMODITY_MAPPINGS = new ArrayList<ConvertHandler>(){
            private static final long serialVersionUID = 1L;
            {
                add(new ConvertHandler(new Property("commodityNo", "物料编号"), 5));//物料描述
                add(new ConvertHandler(new Property("commodityName", "物料描述"), 6));//物料描述
                add(new ConvertHandler(new Property("commodityUnit", "物料单位"), 7));//单位
                add(new ConvertHandler(new Property("quantity", "数量"), 8, new Validator[] {Validator.NUMBER}));//数量
                add(new ConvertHandler(new Property("boxCount", "箱数"), 9, new Validator[] {Validator.NUMBER}));//箱数
                add(new ConvertHandler(new Property("volume", "体积"), 10, new Validator[] {Validator.NUMBER}));//体积
                add(new ConvertHandler(new Property("weight", "重量"), 11, new Validator[] {Validator.NUMBER}));//重量
            }
        };
    }

    @Override
    public Collection<ConvertHandler> analyzeObjects() {
        return ORDER_MAPPINGS;
    }

    @Override
    public Collection<ConvertHandler> analyzeCommodity() {
        return COMMODITY_MAPPINGS;
    }

    @Override
    public ConvertObjectByExcel readExcel(FileEntity fileEntity) throws Exception {
        return super.readExcel(fileEntity, 3, 19);
    }

    @Override
    public void mergeSomething(OrderTemplate object, Object[] excelObjects) throws Exception {
        /*Collection<OrderCommodity> collection = object.getCommodities();
        if(collection == null) {
            collection = new ArrayList<OrderCommodity>();
        }
        BeanUtilsBean beanUtils = BeanUtils.build();
        OrderCommodity commodity = new OrderCommodity();
        for (ConvertHandler handler : analyzeCommodity()) {
            convert(commodity, excelObjects, handler);
        }
        collection.add(commodity);
        object.setCommodities(collection);*/
    }
}

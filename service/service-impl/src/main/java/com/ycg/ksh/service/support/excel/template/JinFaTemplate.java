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
import com.ycg.ksh.service.support.excel.convert.PhoneConverter;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 金发模板
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-19 11:39:51
 */
public class JinFaTemplate extends ConvertObjectByExcel<MergeWaybill> {
    
    private static final Collection<ConvertHandler> WAYBILL_MAPPINGS;
    private static final Collection<ConvertHandler> GOODS_MAPPINGS;

    static {
        /**
         * 业务助理手机（提货联系人）
         * 车辆预约时间
         * 装货顺序
         * 运输商
         * 要求发货时间
         * 车牌号
         * 交货单号
         * 交货单行号
         * 销售订单号
         * 包装规格
         * 送达方
         * 客户物料编码
         * 金发料号
         * 牌号
         * 发货数量
         * 单位
         * 仓库数量
         * 批次1
         * 批次数量1
         * 批次2
         * 批次数量2
         * 批次3
         * 批次数量3
         * 库位
         * 工厂
         * 合同号
         * 备注
         * 送达方地址
         * 联系人
         * 联系方式
         * 打托要求
         * 是否卸货
         * 是否贴标签
         * 运输商发货要求
         * 装货要求
         * 发货岗要求
         * 创建时间
         * 创建日期
         * 计划发货日期
         * 合理发货日期
         * 制单人
         * 业务员
         * 出口业务类型
         * 随货资料
         * 匹配结果
         */
        //"货主名称","客户料号","物料名称","收货客户", "重量（kg）","体积（m³）","数量（件）","收货地址","联系人","联系电话","发货后天数","时间点 ","订单摘要"
        WAYBILL_MAPPINGS = new ArrayList<ConvertHandler>(){
            private static final long serialVersionUID = 1L;
            {
                add(new ConvertHandler(new Property("receiverName", "收货客户"), 10, true, new Validator[] { Validator.NOTBLANK }));//收货客户
                add(new ConvertHandler(new Property("receiveAddress", "收货地址"), 27, true, new Validator[] { Validator.NOTBLANK }));//收货地址
                add(new ConvertHandler(new Property("contactName", "联系人"), 28, true, new Validator[] { Validator.NOTBLANK }));//联系人
                add(new ConvertHandler(new Property("contactPhone", "联系电话"), 29, true, new MobileConverter()));//联系电话
                add(new ConvertHandler(new Property("receiverTel", "收货方电话"), 29, new PhoneConverter()));//联系电话
                add(new ConvertHandler(new Property("orderSummary", "订单摘要"), 33));//订单摘要
            }
        };
        GOODS_MAPPINGS = new ArrayList<ConvertHandler>(){
            private static final long serialVersionUID = 1L;
            {
                add(new ConvertHandler(new Property("goodsType", "客户料号"), 13));//物料名称
                add(new ConvertHandler(new Property("goodsWeight", "重量"), 14, new Validator[] {Validator.NUMBER}));//重量（kg）
                add(new ConvertHandler(new Property("summary", "订单摘要"), 34));//订单摘要
            }
        };
    }
    @Override
    public ConvertObjectByExcel readExcel(FileEntity fileEntity) throws Exception {
        return super.readExcel(fileEntity, 2, 45);
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

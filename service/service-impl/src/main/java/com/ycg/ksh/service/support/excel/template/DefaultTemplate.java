/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-19 11:39:51
 */
package com.ycg.ksh.service.support.excel.template;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.beanutils.BeanUtilsBean;

import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.util.BeanUtils;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.entity.persistent.Goods;
import com.ycg.ksh.entity.service.MergeWaybill;
import com.ycg.ksh.service.support.excel.ConvertObjectByExcel;
import com.ycg.ksh.service.support.excel.Property;
import com.ycg.ksh.service.support.excel.convert.ConvertHandler;

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
                add(new ConvertHandler(new Property("deliveryNumber", "送货单号"), 0, true, new Validator[] { Validator.NOTBLANK }));//货主名称
                add(new ConvertHandler(new Property("shipperCode", "发货方编码"), 1, true,null,null));//发货方客户编号
                add(new ConvertHandler(new Property("shipperName", "发货方名称"), 2, true, null,null));//货主名称
                add(new ConvertHandler(new Property("shipperAddress", "发货地址"), 3, true, null,null));//发货地址
                add(new ConvertHandler(new Property("shipperContactName", "发货联系人"), 4, true, null,null));//发货联系人
                add(new ConvertHandler(new Property("shipperContactTel", "发货联系电话"), 5, true, null,null));//发货联系电话
                
                add(new ConvertHandler(new Property("receiverCode", "收货方编码"), 6, true, null,null));//收货方编号
                add(new ConvertHandler(new Property("receiverName", "收货客户"), 7, true,null,null));//收货客户
                add(new ConvertHandler(new Property("receiveAddress", "收货地址"), 8, true, null,null));//收货地址
                add(new ConvertHandler(new Property("contactName", "联系人"), 9, true, null,null));//联系人
                add(new ConvertHandler(new Property("contactPhone", "联系电话"), 10, true, null,null));//联系电话
                add(new ConvertHandler(new Property("arriveDay", "发货后天数"), 16, new Validator[] {Validator.NUMBER}));//发货后天数
                add(new ConvertHandler(new Property("arriveHour", "时间点"), 17, new Validator[] {Validator.NUMBER}));//时间点
                add(new ConvertHandler(new Property("loadNo", "装运单号"), 19));//装运号
                add(new ConvertHandler(new Property("carType", "车型"), 20));//车型
                add(new ConvertHandler(new Property("loadTime", "装运日期"), 21, new Validator[] {Validator.NOTBLANK }));//时间点
            }
        };
        GOODS_MAPPINGS = new ArrayList<ConvertHandler>(){
            private static final long serialVersionUID = 1L;
            {
                add(new ConvertHandler(new Property("goodsType", "客户料号"), 11));//客户料号
                add(new ConvertHandler(new Property("goodsName", "物料名称"), 12));//物料名称
                add(new ConvertHandler(new Property("goodsWeight", "重量"), 13, new Validator[] {Validator.DECIMAL}));//重量（kg）
                add(new ConvertHandler(new Property("goodsVolume", "体积"), 14, new Validator[] {Validator.DECIMAL}));//体积（m³）
                add(new ConvertHandler(new Property("goodsQuantity", "数量"), 15, new Validator[] {Validator.NUMBER}));//数量（件）
                add(new ConvertHandler(new Property("summary", "订单摘要"), 18));//订单摘要
            }
        };
    }
    @Override
    public ConvertObjectByExcel readExcel(FileEntity fileEntity) throws Exception {
        return super.readExcel(fileEntity, 3, 21);
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
    /**
     * 	针对收发货人信息通过编码在数据库匹配的逻辑
     * @param obj
     * @return
     */
    public String checkCustomer(Object[] obj) {
		String scode = obj[1]==null?"":obj[1].toString(),sn=obj[2]==null?"":obj[2].toString(),sa = obj[3]==null?"":obj[3].toString(), sc = obj[4]==null?"":obj[4].toString(),st = obj[5]==null?"":obj[5].toString();
		String rcode = obj[6]==null?"":obj[6].toString(),rn=obj[7]==null?"":obj[7].toString(),ra = obj[8]==null?"":obj[8].toString(), rc = obj[9]==null?"":obj[9].toString(),rt = obj[10]==null?"":obj[10].toString();
    	StringBuilder stringBuilder = new StringBuilder();
    	if(scode.isEmpty() && (sn.isEmpty() || sa.isEmpty() || sc.isEmpty() || st.isEmpty())) {
			stringBuilder.append("发货方编码或发货方信息不能为空");
		}
		if(rcode.isEmpty() && (rn.isEmpty() || ra.isEmpty() || rc.isEmpty() || rt.isEmpty())) {
			if(stringBuilder.length()>0) {
				stringBuilder.append(",");
			}
			stringBuilder.append("收货方编码或收货方信息不能为空");
		}
    	return stringBuilder.toString();
    }
}

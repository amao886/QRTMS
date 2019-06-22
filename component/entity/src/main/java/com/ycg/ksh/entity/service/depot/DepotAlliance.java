package com.ycg.ksh.entity.service.depot;

import com.ycg.ksh.entity.persistent.depot.InboundOrder;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Collection;

/**
 * 入库单整合类
 *
 * @Auther: wangke
 * @Date: 2018/9/18 10:55
 * @Description:
 */
public class DepotAlliance extends InboundOrder {
    private String uname; // 操作人姓名
    private Integer outboundQuantity;//出库数量
    private String receiveName;//收货名称
    private String shipperName;//发货名称
    private String dateFormat;//分组日期
    private Integer count; //总数量

    private Collection<OutBoundPrintAlliance> printSummary; // 分组货物汇总

    public Collection<OutBoundPrintAlliance> getPrintSummary() {
        return printSummary;
    }

    public void setPrintSummary(Collection<OutBoundPrintAlliance> printSummary) {
        this.printSummary = printSummary;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public static DepotAlliance buildAlliance(InboundOrder inboundOrder) throws Exception {
        if (inboundOrder instanceof DepotAlliance) {
            return (DepotAlliance) inboundOrder;
        }
        if (inboundOrder instanceof InboundOrder) {
            return new DepotAlliance((InboundOrder) inboundOrder);
        }
        return new DepotAlliance(inboundOrder);
    }

    public DepotAlliance(InboundOrder inboundOrder) throws Exception {
        this();
        BeanUtils.copyProperties(this, inboundOrder);
    }

    public DepotAlliance() {
        count = 0;
    }

    public Integer getOutboundQuantity() {
        return outboundQuantity;
    }

    public void setOutboundQuantity(Integer outboundQuantity) {
        this.outboundQuantity = outboundQuantity;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}

package com.ycg.ksh.entity.service.depot;

import com.ycg.ksh.common.entity.BaseEntity;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

/**
 * @Auther: wangke
 * @Date: 2018/9/19 13:16
 * @Description:
 */
public class OutBoundPrintAlliance extends BaseEntity {

    private String batchNumber; //批次号

    private Integer outboundQuantity;//出库数量

    private String materialName; //品名

    private Integer summaryQuantity; //汇总数量

    private Collection<OutBoundPrintAlliance> printDetail;

    public Collection<OutBoundPrintAlliance> getPrintDetail() {
        return printDetail;
    }

    public void setPrintDetail(Collection<OutBoundPrintAlliance> printDetail) {
        if (CollectionUtils.isNotEmpty(printDetail)) {
            printDetail.forEach(a -> {
                setSummaryQuantity(summaryQuantity + a.getOutboundQuantity());
            });
        }
        this.printDetail = printDetail;
    }

    public OutBoundPrintAlliance() {
        summaryQuantity = 0;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Integer getSummaryQuantity() {
        return summaryQuantity;
    }

    public void setSummaryQuantity(Integer summaryQuantity) {
        this.summaryQuantity = summaryQuantity;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getOutboundQuantity() {
        return outboundQuantity;
    }

    public void setOutboundQuantity(Integer outboundQuantity) {
        this.outboundQuantity = outboundQuantity;
    }
}

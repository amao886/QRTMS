package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/29
 */

/**
 * 运单节点
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/29
 */
public class ConveyanceNode {

    private Long parentKey;
    private Long conveyanceKey;
    private Integer fettle;
    private Boolean haveChild;

    public Long getParentKey() {
        return parentKey;
    }

    public void setParentKey(Long parentKey) {
        this.parentKey = parentKey;
    }

    public Long getConveyanceKey() {
        return conveyanceKey;
    }

    public void setConveyanceKey(Long conveyanceKey) {
        this.conveyanceKey = conveyanceKey;
    }

    public Integer getFettle() {
        return fettle;
    }

    public void setFettle(Integer fettle) {
        this.fettle = fettle;
    }

    public Boolean getHaveChild() {
        return haveChild;
    }

    public void setHaveChild(Boolean haveChild) {
        this.haveChild = haveChild;
    }
}

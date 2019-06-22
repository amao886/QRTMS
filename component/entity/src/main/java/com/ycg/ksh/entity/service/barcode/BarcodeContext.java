/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-21 12:44:28
 */
package com.ycg.ksh.entity.service.barcode;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.constant.BarCodeFettle;

/**
 * 条码状态验证后的结果信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-21 12:44:28
 */
public abstract class BarcodeContext extends BaseEntity {

    private static final long serialVersionUID = 6516549415768972075L;

    private Integer userKey;
	private String barcode;//条码号
	private BarCodeFettle codeFettle;//条码的状态
	private boolean allowBind;//是否允许绑定
	private Integer waitBindCount;//待绑单单数量
	private Integer loadKey;//装车人
	private Integer unloadKey;//卸货人
    private boolean needBingImage;//是否需要绑码图片

    public BarCodeFettle fettle(){
    	return codeFettle;
	}

    public abstract boolean isComplete();

    public abstract boolean isCompany();

	public Integer getUserKey() {
		return userKey;
	}

	public void setUserKey(Integer userKey) {
		this.userKey = userKey;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public boolean isAllowBind() {
		return allowBind;
	}

	public void setAllowBind(boolean allowBind) {
		this.allowBind = allowBind;
	}

	public Integer getWaitBindCount() {
		return waitBindCount;
	}

	public void setWaitBindCount(Integer waitBindCount) {
		this.waitBindCount = waitBindCount;
	}

	public Integer getLoadKey() {
		return loadKey;
	}

	public void setLoadKey(Integer loadKey) {
		this.loadKey = loadKey;
	}

	public Integer getUnloadKey() {
		return unloadKey;
	}

	public void setUnloadKey(Integer unloadKey) {
		this.unloadKey = unloadKey;
	}

    public boolean isNeedBingImage() {
        return needBingImage;
    }

    public void setNeedBingImage(boolean needBingImage) {
        this.needBingImage = needBingImage;
    }

	public BarCodeFettle getCodeFettle() {
		return codeFettle;
	}

	public void setCodeFettle(BarCodeFettle codeFettle) {
		this.codeFettle = codeFettle;
	}
}

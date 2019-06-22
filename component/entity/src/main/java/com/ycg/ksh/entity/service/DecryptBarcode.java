/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 13:33:46
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 条码信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-10 13:33:46
 */
public class DecryptBarcode extends BaseEntity {

	private static final long serialVersionUID = 2512467000786140371L;
	
	private String code;
	private boolean decrypt;
	
	
	public DecryptBarcode(String code, boolean decrypt) {
		super();
		this.code = code;
		this.decrypt = decrypt;
	}
	
	public DecryptBarcode(String code) {
		super();
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean isDecrypt() {
		return decrypt;
	}
	public void setDecrypt(boolean decrypt) {
		this.decrypt = decrypt;
	}
	
}

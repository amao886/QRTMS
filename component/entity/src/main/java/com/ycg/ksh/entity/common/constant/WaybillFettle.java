package com.ycg.ksh.entity.common.constant;

/**
 * 码的状态
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-08-29 13:43:26
 */
public enum WaybillFettle {
	//0取消，10未绑定，20已绑定，30运输中，35已送达，40已到货
	CANCEL(0, "取消"), UNBIND(10, "未绑码"), BOUND(20, "已绑码"),ING(30, "正在运输中"),ARRIVE(35, "已送达"),RECEIVE(40, "已收货");
	
	private int code;
	private String desc;
	
	private WaybillFettle(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	public final static WaybillFettle convert(Integer code){
		if(code != null ){
			for (WaybillFettle fettle : values()) {
				if(fettle.code - code == 0){
					return fettle;
				}
			}
		}
		return WaybillFettle.UNBIND;
	}
	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public boolean cancel() {
		return CANCEL == this;
	}
	public boolean unbind() {
		return UNBIND == this;
	}
	public boolean bind() {
		return BOUND == this;
	}
	public boolean ing() {
		return ING == this;
	}
	public boolean arrive() {
		return ARRIVE == this;
	}
	public boolean receive() {
		return RECEIVE == this;
	}


	/**
	 * Returns the name of this enum constant, as contained in the
	 * declaration.  This method may be overridden, though it typically
	 * isn't necessary or desirable.  An enum type should override this
	 * method when a more "programmer-friendly" string form exists.
	 *
	 * @return the name of this enum constant
	 */
	@Override
	public String toString() {
		return String.valueOf(code);
	}
}


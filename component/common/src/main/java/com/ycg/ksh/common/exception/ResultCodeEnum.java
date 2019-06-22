package com.ycg.ksh.common.exception;

/**
  * @Description: 异常返回的时候使用到的状态码和对应的名字
  * @author <a href="mailto:工号108252">万玉杰</a>
  * @date 2017年7月20日 上午11:25:52
  * @version 1
  *
  */
public enum ResultCodeEnum {
	
	/**请求成功*/
	SUCCESS("SxFFF-000000","请求成功"),
	
	/**必填项错误*/
	REQUIRED_ERROR("Px000-000001","必填项错误"),
	
	/**JSON/XML解析错误*/
	PARSE_ERROR("Px000-000002","JSON/XML解析错误"),
	
	/**数值范围溢出错误*/
	NUM_RANGE_OVERFLOW_ERROR("Px000-000003","数值范围溢出错误"),
	
	/**索引越界错误*/
	INDEX_CROSS_ERROR("Px000-000004","索引越界错误"),
	
	/**多个同名参数错误*/
	MULTIPLE_PARAMETER_ERROR("Px000-000005","多个同名参数错误"),
	
	/**字符串长度过长/过短错误*/
	STRING_LENGTH_ERROR("Px000-000006","字符串长度过长/过短错误"),
	
	/**字符串内容不符合规则错误*/
	STRING_REG_ERROR("Px000-000007","字符串内容不符合规则错误"),

	/**缺少参数错误*/
	MISSING_PARAMETER_ERROR("Px000-000008","缺少参数错误"),

	/**文件类型不合法错误*/
	ILLEGAL_FILE_TYPE_ERROR("Px000-000009","文件类型不合法错误"),
	
	/**文件名错误*/
	FILE_NAME_ERROR("Px000-000010","文件名错误"),
	
	/**文件大小超限错误*/
	FILE_SIZE_ERROR("Px000-000011","文件大小超限错误"),
	
	/**文件媒体类型错误*/
	FILE_MEDIA_TYPE_ERROR("Px000-000012","文件媒体类型错误"),
	
	/**图片尺寸错误*/
	PICTURE_SIZE_ERROR("Px000-000013","图片尺寸错误"),
	
	/**图片格式错误*/
	PICTURE_FORMAT_ERROR("Px000-000014","图片格式错误"),
	
	/**视频格式错误*/
	VIDEO_FORMAT_ERROR("Px000-000015","视频格式错误"),
	
	/**语音格式错误*/
	VOICE_FORMAT_ERROR("Px000-000016","语音格式错误"),
	
	/**文件格式转换错误*/
	FILE_FORMAT_ERROR("Px000-000017","文件格式转换错误"),
	
	/**系统配置信息错误*/
	SYSTEM_CONFIGURATION_ERROR("Px000-000018","系统配置信息错误"),
	
	/**URL过长错误*/
	URL_TOO_LONG_ERROR("Px000-000019","URL过长错误"),
	
	/**数据库无法连接错误*/
	DATABASE_CONNECT_ERROR("Hx000-000001","数据库无法连接错误"),
	
	/**连接中断错误*/
	CONNECTION_INTERRUPT_ERROR("Hx000-000002","连接中断错误"),
	
	/**磁盘满错误*/
	DISK_FULL_ERROR("Hx000-000003","磁盘满错误"),
	
	/**网络不稳定错误*/
	NETWORK_INSTABILITY_ERROR("Hx000-000004","网络不稳定错误"),
	
	/**无可用服务错误*/
	NO_SERVICE_ERROR("Hx000-000005","无可用服务错误"),
	
	/**服务调用方权限不足错误*/
	INSUFFICIENT_AUTHORITY("Hx000-000006","服务调用方权限不足错误"),
	
	/**数据库内部错误*/
	DATABASE_INTERNAL_ERROR("Hx000-000007","数据库内部错误"),
	
	/**账户或者密码错误*/
	ACCOUNT_OR_PASSWORD_ERROR("Dx000-000001","账户或者密码错误"),
	
	/**验证码错误/过期*/
	VERIFICATION_CODE_ERROR("Dx000-000002","验证码错误/过期"),
	
	/**未登录错误*/
	NOT_LOGIN_ERROR("Dx000-000003","未登录错误"),
	
	/**权限不足错误*/
	INSUFFICIENT_PERMISSIONS("Dx000-000004","权限不足错误"),
	
	/**令牌失效错误*/
	TOKEN_FAILURE_ERROR("Dx000-000005","令牌失效错误"),
	
	/**无证书或证书过期错误*/
	CERTIFICATE_CHECK_ERROR("Dx000-000006","无证书或证书过期错误"),
	
	/**账户被禁用/冻结错误*/
	ACCOUNT_ERROR("Dx000-000007","账户被禁用/冻结错误"),
	
	/**密码过期错误*/
	PASSWORD_ERROR("Dx000-000008","密码过期错误"),
	
	/**弱口令错误*/
	WEAK_PASSWORD_ERROR("Dx000-000009","弱口令错误"),
	
	/**接口调用次数限制错误*/
	INTERFACE_LIMIT_ERROR("Dx000-000010","接口调用次数限制错误"),
	
	/**USB key检测错误*/
	USB_DETECTION_ERROR("Dx000-000011","USB key检测错误"),
	
	/**密码修改错误*/
	PASSWORD_MODIFICATION_ERROR("Dx000-000012","密码修改错误"),
	
	/**请求超时*/
	REQUEST_TIMEOUT("T-FFF-000000","请求超时");
	private String code;
	
	private String codeName;
	
	ResultCodeEnum(String code, String codeName) {
		this.code = code;
		this.codeName = codeName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	
	
}

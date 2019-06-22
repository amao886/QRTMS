package com.ycg.ksh.api.mobile.util;

import com.ycg.ksh.common.system.SystemKey;
import com.ycg.ksh.common.system.SystemUtils;
import org.apache.commons.lang.StringUtils;

import java.net.URLEncoder;

/**
 * 跟前端有关的工具，比如组装前端跳转URL
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:43:20
 */
public abstract class FrontUtils {

	private static final String FRONT_PAGE_INDEX = "index.html";//前端首页
	private static final String FRONT_PAGE_PEOPLECENTER = "view/partener/peopleCenter.html";//前端个人中心页
	private static final String FRONT_PAGE_BINDPHONE = "view/partener/bindPhone.html";//前端绑定手机号页
	private static final String FRONT_PAGE_TASKDETAIL = "view/partener/myTaskDetails.html";//前端任务详情页
	private static final String FRONT_PAGE_BARCODE_BIND = "view/partener/bindBarCode.html";//前端条码绑定页面
	private static final String FRONT_PAGE_BARCODE_BIND_LIST = "view/partener/bindBarCodeList.html";//前端条码绑定页面
	private static final String FRONT_PAGE_POSITIONINFO ="view/partener/positionInfo.html";//前端定位详情页
	private static final String FRONT_PAGE_POSITIONINFO_TRANSITION ="view/partener/positionInfoCopy.html";//前端临时定位详情页
	private static final String FRONT_PAGE_MYPROJECTTEAM ="view/partener/myProjectTeam.html";//我的项目组
	private static final String FRONT_PAGE_ADDRESS ="view/partener/address.html";//派送地址
	private static final String FRONT_PAGE_SCANSUCCESS ="view/partener/scanSuccess.html";//派送地址
	private static final String FRONT_PAGE_DRIVERRECEIPT ="view/partener/uploadReceipt.html";//上传装车回单页面
	private static final String FRONT_PAGE_BINDIMAGE ="view/partener/uploadCodePhoto.html";//上传绑码图片页面
	
	/**
	 * 前端基础地址
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:43:56
	 * @return
	 */
	public static String base() {
		return SystemUtils.get(SystemKey.FRONT_HOST_DOMAIN);
	}
	
	/**
	 * 前端首页
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:44:07
	 * @param token
	 * @return
	 */
	public static String index(String token) {
		return SystemUtils.buildUrl(base(), FRONT_PAGE_INDEX) + "?token="+ token +"&v="+ System.nanoTime();
	}
	/**
	 * 前端任务详情页
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:44:19
	 * @param token
	 * @param shareId
	 * @param waybillId
	 * @return
	 */
	public static String taskDetail(String token, String shareId, Object waybillId) {
		if(StringUtils.isNotBlank(shareId)) {
			return SystemUtils.buildUrl(base(), FRONT_PAGE_TASKDETAIL) + "?token="+ token +"&shareId="+ shareId +"&waybillId="+ waybillId +"&v="+ System.nanoTime();
		}else {
			return SystemUtils.buildUrl(base(), FRONT_PAGE_TASKDETAIL) + "?token="+ token +"&waybillId="+ waybillId +"&v="+ System.nanoTime();
		}
	}
	/**
	 * 前端手机绑定页
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:44:31
	 * @param token
	 * @param state
	 * @param barcode
	 * @param groupId
	 * @return
	 */
	public static String bing(String token, String state, String barcode, String groupId) {
		return SystemUtils.buildUrl(base(), FRONT_PAGE_BINDPHONE) + "?token="+ token + "&state=" + state + "&bindCode="+ barcode +"&groupId="+groupId +"&v="+ System.nanoTime();
	}
	
	/**
	 * 前端绑定手机号页面
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-16 13:32:42
	 * @param token  用户标识
	 * @param callback 绑定成功后的回调地址,经过url转码的
	 * @return
	 */
	public static String bingPhone(String token, String callback) {
		if(StringUtils.isNotBlank(callback)) {
			try {
				callback = URLEncoder.encode(callback, "utf8");
			} catch (Exception e) { }
			return SystemUtils.buildUrl(base(), FRONT_PAGE_BINDPHONE) + "?token="+ token +"&callback="+ callback +"&v="+ System.nanoTime();
		}else {
			return SystemUtils.buildUrl(base(), FRONT_PAGE_BINDPHONE) + "?token="+ token +"&v="+ System.nanoTime();
		}
	}
	/**
	 * 当状态为分享时进入绑定手机号
	 * @param token
	 * @param state
	 * @param shareId
	 * @param waybillId
	 * @return
	 */
	public static String bing_share(String token, String state, String shareId, String waybillId) {
		return SystemUtils.buildUrl(base(), FRONT_PAGE_BINDPHONE) + "?token="+ token + "&state=" + state + "&shareId="+ shareId +"&waybillId="+waybillId +"&v="+ System.nanoTime();
	}
	/**
	 * 前端个人中心页
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:44:43
	 * @param token
	 * @return
	 */
	public static String center(String token) {
		return SystemUtils.buildUrl(base(), FRONT_PAGE_PEOPLECENTER) + "?token="+ token +"&v="+ System.nanoTime();
	}
	
	/**
	 * 前端我的项目组页面
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:44:43
	 * @param token
	 * @return
	 */
	public static String projectTeam(String token) {
		return SystemUtils.buildUrl(base(), FRONT_PAGE_MYPROJECTTEAM) + "?token="+ token +"&v="+ System.nanoTime();
	}
	/**
	 * 前端绑定条码页
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:44:56
	 * @param token
	 * @param barcode
	 * @return
	 */
	public static String bindBarcodes(String token, String barcode, Integer groupId) {
		return SystemUtils.buildUrl(base(), FRONT_PAGE_BARCODE_BIND_LIST) + "?token="+token +"&bindCode="+ barcode +"&groupId=" + groupId +"&v="+ System.nanoTime();
	}
	public static String bindBarcode(String token, String barcode, Integer groupId) {
		return SystemUtils.buildUrl(base(), FRONT_PAGE_BARCODE_BIND) + "?token="+token +"&bindCode="+ barcode +"&groupId=" + groupId +"&v="+ System.nanoTime();
	}

	/**
	 * 前端定位页
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:45:08
	 * @param token
	 * @param barcode
	 * @return
	 */
	public static String positioninfo(String token, String barcode) {
		return SystemUtils.buildUrl(base(), FRONT_PAGE_POSITIONINFO) + "?token="+token +"&bindCode="+ barcode +"&v="+ System.nanoTime();
	}
	
	
	/**
	 * 派送地址
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-10 15:02:11
	 * @param token
	 * @return
	 */
	public static String mapaddress(String token) {
		return SystemUtils.buildUrl(base(), FRONT_PAGE_ADDRESS) + "?token=" + token +"&v="+ System.nanoTime();
	}
	/**
	 * 扫描添加好友
	 * TODO Add description
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-06 11:30:19
	 * @param token
	 * @param friendId
	 * @return
	 */
	public static String addFriends(String token,String friendId){	
		return SystemUtils.buildUrl(base(), FRONT_PAGE_SCANSUCCESS) + "?token="+token +"&friendId=" + friendId +"&v="+ System.nanoTime();
	}
	
	public static String driverreceipt(String token, String barcode){	
		return SystemUtils.buildUrl(base(), FRONT_PAGE_DRIVERRECEIPT) + "?token="+token +"&barcode=" + barcode +"&v="+ System.nanoTime();
	}
	
	public static String bindimage(String token, String barcode){	
		return SystemUtils.buildUrl(base(), FRONT_PAGE_BINDIMAGE) + "?token="+token +"&barcode=" + barcode +"&v="+ System.nanoTime();
	}

	public static String transitionPosition(String token, String barcode){
		return SystemUtils.buildUrl(base(), FRONT_PAGE_POSITIONINFO_TRANSITION) + "?token="+token +"&barcode=" + barcode +"&v="+ System.nanoTime();
	}

}

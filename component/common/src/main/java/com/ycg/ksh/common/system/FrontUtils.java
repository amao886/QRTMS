package com.ycg.ksh.common.system;

import com.ycg.ksh.common.util.encrypt.RSA;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 跟前端有关的工具，比如组装前端跳转URL
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:43:20
 */
public abstract class FrontUtils {

    private static final String FRONT_PAGE_HOME = "home.html";//前端首页测试
    private static final String FRONT_PAGE_INDEX = "index.html";//前端首页
    private static final String FRONT_PAGE_CENTER = "view/user/peopleCenter.html";//前端个人中心页
    private static final String FRONT_PAGE_PEOPLECENTER = "view/partener/peopleCenter.html";//前端个人中心页
    private static final String FRONT_PAGE_BINDPHONE = "view/partener/scanCodeSigninOne.html";//前端绑定手机号页
    private static final String FRONT_PAGE_TASKDETAIL = "view/partener/myTaskDetails.html";//前端任务详情页
    private static final String FRONT_PAGE_BARCODE_BIND = "view/partener/bindBarCode.html";//前端条码绑定页面
    private static final String FRONT_PAGE_BARCODE_BIND_LIST = "view/partener/bindBarCodeList.html";//前端条码绑定页面
    private static final String FRONT_PAGE_POSITIONINFO = "view/partener/positionInfo.html";//前端定位详情页
    private static final String FRONT_PAGE_POSITIONINFO_TRANSITION = "view/partener/positionInfoCopy.html";//前端临时定位详情页
    private static final String FRONT_PAGE_MYPROJECTTEAM = "view/partener/myProjectTeam.html";//我的项目组
    private static final String FRONT_PAGE_ADDRESS = "view/partener/address.html";//派送地址
    private static final String FRONT_PAGE_SCANSUCCESS = "view/partener/scanSuccess.html";//派送地址
    private static final String FRONT_PAGE_DRIVERRECEIPT = "view/partener/uploadReceipt.html";//上传装车回单页面
    private static final String FRONT_PAGE_BINDIMAGE = "view/partener/uploadCodePhoto.html";//上传绑码图片页面
    private static final String FRONT_PAGE_ELE_RECEIPT = "view/partener/electronicReceipt.html";//电子回单详情页
    private static final String FRONT_PAGE_PERSONALAUTH = "view/partener/certification.html";


    private static final String ENTERPRISE_BINDCODE = "view/partener/enterprise/bindCode.html";//绑单
    private static final String ENTERPRISE_BINDCODEDETAIL = "view/partener/enterprise/bindCodeDetail.html";//订单详情
    private static final String ENTERPRISE_LOCATION = "view/partener/enterprise/locationSuccess.html";//上报位置
    private static final String ENTERPRISE_BINDTIPS = "view/partener/enterprise/bindTips.html";//绑码提示

    private static final String COMPANY_TOADDCOMPANY = "view/partener/scanCodeSigninTwo.html";//跳转到新增企业页面

    private static final String SUCCESS_PAGE = "view/partener/receiveCustomerScanCodeSuccess.html";//操作成功时跳转到成功页面
    private static final String FAILURE_PAGE = "view/partener/receiveCustomerScanCodeFail.html";//操作失败时跳转到失败页面


    private static final String REGISTER_PAGE = "view/partener/bindPhoneOne.html";//非微信注册
    private static final String SACN_REGISTER_PAGE = "view/partener/bindPhoneTwo.html";//微信扫码注册
    private static final String INVITEASK_PAGE = "view/driver/askBecomeDriver.html";//处理邀请，邀请合作司机



    public static String pdaIndex(String token){
        return SystemUtils.buildUrl(SystemUtils.get(SystemKey.WEIXIN_CALLBACK_DOMAIN), "pda/index.html") + "?token="+ token;
    }

    /**
     * 前端基础地址
     * <p>
     *
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:43:56
     */
    public static String base() {
        return SystemUtils.get(SystemKey.FRONT_HOST_DOMAIN);
    }

    /**
     * 前端首页
     * <p>
     *
     * @param token
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:44:07
     */
    public static String index(String token) {
        return SystemUtils.buildUrl(base(), FRONT_PAGE_HOME) + "?token=" + token + "&v=" + System.nanoTime();
        //return SystemUtils.buildUrl(base(), FRONT_PAGE_INDEX) + "?token="+ token +"&v="+ System.nanoTime();
    }

    /**
     * 前端任务详情页
     * <p>
     *
     * @param token
     * @param shareId
     * @param waybillId
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:44:19
     */
    public static String taskDetail(String token, String shareId, Object waybillId) {
        if (StringUtils.isNotBlank(shareId)) {
            return SystemUtils.buildUrl(base(), FRONT_PAGE_TASKDETAIL) + "?token=" + token + "&shareId=" + shareId + "&waybillId=" + waybillId + "&v=" + System.nanoTime();
        } else {
            return SystemUtils.buildUrl(base(), FRONT_PAGE_TASKDETAIL) + "?token=" + token + "&waybillId=" + waybillId + "&v=" + System.nanoTime();
        }
    }

    /**
     * 前端手机绑定页
     * <p>
     *
     * @param token
     * @param state
     * @param barcode
     * @param groupId
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:44:31
     */
    public static String bing(String token, String state, String barcode, String groupId) {
        return SystemUtils.buildUrl(base(), FRONT_PAGE_BINDPHONE) + "?token=" + token + "&state=" + state + "&bindCode=" + barcode + "&groupId=" + groupId + "&v=" + System.nanoTime();
    }

    /**
     * 前端绑定手机号页面
     * <p>
     *
     * @param token    用户标识
     * @param callback 绑定成功后的回调地址,经过url转码的
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-16 13:32:42
     */
    public static String bingPhone(String token, String callback) {
        if (StringUtils.isNotBlank(callback)) {
            try {
                callback = URLEncoder.encode(callback, "utf8");
            } catch (Exception e) {
            }
            return SystemUtils.buildUrl(base(), FRONT_PAGE_BINDPHONE) + "?token=" + token + "&callback=" + callback + "&v=" + System.nanoTime();
        } else {
            return SystemUtils.buildUrl(base(), FRONT_PAGE_BINDPHONE) + "?token=" + token + "&v=" + System.nanoTime();
        }
    }

    /**
     * 当状态为分享时进入绑定手机号
     *
     * @param token
     * @param state
     * @param shareId
     * @param waybillId
     * @return
     */
    public static String bing_share(String token, String state, String shareId, String waybillId) {
        return SystemUtils.buildUrl(base(), FRONT_PAGE_BINDPHONE) + "?token=" + token + "&state=" + state + "&shareId=" + shareId + "&waybillId=" + waybillId + "&v=" + System.nanoTime();
    }

    /**
     * 前端个人中心页
     * <p>
     *
     * @param token
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:44:43
     */
    public static String center(String token) {
        return SystemUtils.buildUrl(base(), FRONT_PAGE_PEOPLECENTER) + "?token=" + token + "&v=" + System.nanoTime();
    }

    /**
     * 前端我的项目组页面
     * <p>
     *
     * @param token
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:44:43
     */
    public static String projectTeam(String token) {
        return SystemUtils.buildUrl(base(), FRONT_PAGE_MYPROJECTTEAM) + "?token=" + token + "&v=" + System.nanoTime();
    }

    /**
     * 前端绑定条码页
     * <p>
     *
     * @param token
     * @param barcode
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:44:56
     */
    public static String bindBarcodes(String token, String barcode, Integer groupId) {
        return SystemUtils.buildUrl(base(), FRONT_PAGE_BARCODE_BIND_LIST) + "?token=" + token + "&bindCode=" + barcode + "&groupId=" + groupId + "&v=" + System.nanoTime();
    }

    public static String bindBarcode(String token, String barcode, Integer groupId) {
        return SystemUtils.buildUrl(base(), FRONT_PAGE_BARCODE_BIND) + "?token=" + token + "&bindCode=" + barcode + "&groupId=" + groupId + "&v=" + System.nanoTime();
    }

    /**
     * 前端定位页
     * <p>
     *
     * @param token
     * @param barcode
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-22 11:45:08
     */
    public static String positioninfo(String token, String barcode) {
        return SystemUtils.buildUrl(base(), FRONT_PAGE_POSITIONINFO) + "?token=" + token + "&bindCode=" + barcode + "&v=" + System.nanoTime();
    }


    /**
     * 派送地址
     * <p>
     *
     * @param token
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-10 15:02:11
     */
    public static String mapaddress(String token) {
        return SystemUtils.buildUrl(base(), FRONT_PAGE_ADDRESS) + "?token=" + token + "&v=" + System.nanoTime();
    }

    /**
     * 扫描添加好友
     * TODO Add description
     * <p>
     *
     * @param token
     * @param friendId
     * @return
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-06 11:30:19
     */
    public static String addFriends(String token, String friendId) {
        return SystemUtils.buildUrl(base(), FRONT_PAGE_SCANSUCCESS) + "?token=" + token + "&friendId=" + friendId + "&v=" + System.nanoTime();
    }

    public static String driverreceipt(String token, String barcode) {
        return SystemUtils.buildUrl(base(), FRONT_PAGE_DRIVERRECEIPT) + "?token=" + token + "&barcode=" + barcode + "&v=" + System.nanoTime();
    }

    public static String bindimage(String token, String barcode) {
        return SystemUtils.buildUrl(base(), FRONT_PAGE_BINDIMAGE) + "?token=" + token + "&barcode=" + barcode + "&v=" + System.nanoTime();
    }

    public static String transitionPosition(String token, String barcode) {
        return SystemUtils.buildUrl(base(), FRONT_PAGE_POSITIONINFO_TRANSITION) + "?token=" + token + "&barcode=" + barcode + "&v=" + System.nanoTime();
    }

    public static String eleReceipt(String token, Long orderKey) {
        return SystemUtils.buildUrl(base(), FRONT_PAGE_ELE_RECEIPT) + "?token=" + token + "&id=" + orderKey + "&v=" + System.nanoTime();
    }

    public static String personalAuth(String token, Integer userId) {
        return SystemUtils.buildUrl(base(), FRONT_PAGE_PERSONALAUTH) + "?token=" + token + "&userID=" + userId + "&v=" + System.nanoTime();
    }

    /**
     * 绑单
     *
     * @param token
     * @param qrcode
     * @return
     */
    public static String enterpriseBindCode(String token, String qrcode) {
        return SystemUtils.buildUrl(base(), ENTERPRISE_BINDCODE) + "?token=" + token + "&qrcode=" + qrcode + "&v=" + System.nanoTime();
    }

    /**
     * 订单详情
     *
     * @param token
     * @param orderKey
     * @return
     */
    public static String enterpriseBindCodeDetail(String token, Long orderKey) {
        return SystemUtils.buildUrl(base(), ENTERPRISE_BINDCODEDETAIL) + "?token=" + token + "&orderKey=" + orderKey + "&v=" + System.nanoTime();
    }

    /**
     * 上报位置
     *
     * @param token
     * @param qrcode
     * @return
     */
    public static String enterpriseLocation(String token, String qrcode) {
        return SystemUtils.buildUrl(base(), ENTERPRISE_LOCATION) + "?token=" + token + "&qrcode=" + qrcode + "&v=" + System.nanoTime();
    }

    /**
     * 绑码提示
     *
     * @param token
     * @param qrcode
     * @return
     */
    public static String enterpriseBindtips(String token, String qrcode) {
        return SystemUtils.buildUrl(base(), ENTERPRISE_BINDTIPS) + "?token=" + token + "&qrcode=" + qrcode + "&v=" + System.nanoTime();
    }

    public static String addCompany(String token, Long customerKey) {
        return SystemUtils.buildUrl(base(), COMPANY_TOADDCOMPANY) + "?token=" + token + "&customerKey=" + customerKey + "&v=" + System.nanoTime();
    }

    public static String failurePage(String token, String message) {
        try {
            message = URLEncoder.encode(message, RSA.ENCODING);
        } catch (UnsupportedEncodingException e) {
        }
        return SystemUtils.buildUrl(base(), FAILURE_PAGE) + "?token=" + token + "&message=" + message + "&v=" + System.nanoTime();
    }

    public static String successPage(String token, String message) {
        try {
            message = URLEncoder.encode(message, RSA.ENCODING);
        } catch (UnsupportedEncodingException e) {
        }
        return SystemUtils.buildUrl(base(), SUCCESS_PAGE) + "?token=" + token + "&message=" + message + "&v=" + System.nanoTime();
    }

    public static String registerpage(Long inviteKey) {
        return SystemUtils.buildUrl(base(), REGISTER_PAGE) + "?inviteKey=" + inviteKey+ "&v=" + System.nanoTime();
    }

    public static String sacnregisterpage(String uuid, String callback) {
        return SystemUtils.buildUrl(base(), SACN_REGISTER_PAGE) + "?uuid=" + uuid  + "&callback=" + callback +  "&v=" + System.nanoTime();
    }
    public static String inviteaskpage(Long inviteKey, String token) {
        return SystemUtils.buildUrl(base(), INVITEASK_PAGE) + "?token=" + token + "&inviteKey=" + inviteKey + "&v=" + System.nanoTime();
    }
}

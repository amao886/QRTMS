package com.ycg.ksh.common.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.common.exception.BusinessException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * http接口帮助类
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-08-28 09:07:52
 */
public abstract class InterfaceHepler {

    private static final Logger logger = LoggerFactory.getLogger(InterfaceHepler.class);

    public static String domain() {
        String domain = getServiceDomain();
        if (domain.startsWith("https") || domain.startsWith("http")) {
            return SystemUtils.normalize(domain);
        } else {
            return "http://" + domain;
        }
    }

    /**
     * 组装完整的请求url
     * <p>
     *
     * @param method_url
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-08-28 11:16:29
     */
    public static String buildUrl(String method_url) {
        return SystemUtils.buildUrl(domain(), method_url);
    }

    /**
     * 获取 dubbo服务http地址
     * <p>
     *
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-08-28 10:07:14
     */
    public static String getServiceDomain() {
        return SystemUtils.getCallBackDomain();
    }

    public static String request(String json, String method) {
        try {
            String url = buildUrl(method);
            logger.debug("==> url {} parmas {}", url, json);
            HttpClient httpClient = new HttpClient();
            PostMethod postMethod = new PostMethod(buildUrl(method));
            postMethod.getParams().setContentCharset("UTF-8");
            postMethod.addRequestHeader("Content-Type", "application/json; charset=UTF-8");
            postMethod.setRequestEntity(new StringRequestEntity(json, "application/json", "UTF-8"));
            httpClient.executeMethod(postMethod);
            int statusCode = postMethod.getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return postMethod.getResponseBodyAsString();
            } else {
                throw new BusinessException(String.valueOf(statusCode), "服务请求异常");
            }
        } catch (IOException e) {
            logger.error("服务请求异常 {}", e.getMessage(), e);
            throw new BusinessException("服务请求异常");
        }
    }

    public static String requestString(String json, String method) {
        String resultString = request(json, method);
        if (resultString != null) {
            return validate(resultString).getString("resultInfo");
        }
        return null;
    }

    public static JSONObject requestObject(final String json, String method) {
        return objectResultInfo(validate(request(json, method)));
    }

    public static JSONArray requestArray(final String json, String method) {
        return arrayResultInfo(validate(request(json, method)));
    }

    public static JSONObject validate(String resultString) {
        JSONObject jsonObject = JSONObject.parseObject(resultString);
        String resultCode = jsonObject.getString("resultCode");// 状态码
        String reason = jsonObject.getString("reason");// 返回消息
        logger.debug("<== resultCode {} reason {}", resultCode, reason);
        if (200 == Integer.parseInt(resultCode)) {
            return jsonObject;
        } else {
            throw new BusinessException(resultCode, reason);
        }
    }

    public static JSONArray arrayResultInfo(JSONObject jsonObject) {
        return jsonObject.getJSONArray("resultInfo");
    }

    public static JSONObject objectResultInfo(JSONObject jsonObject) {
        return jsonObject.getJSONObject("resultInfo");
    }


    public static final String URL_WAYBILL_BY_ID = "WayBill/queryById";
    public static final String URL_WAYBILL_DETAIL_SHARE_QUERY = "shares/query";
    public static final String URL_WAYBILL_SEARCH_BYIDS = "WayBill/searchByMultipleId";
    public static final String URL_GET_WAYBILL_ID = "WayBill/getWayBillId";
    public static final String URL_WAYBILL_STATISTICS_WAYBILL = "WayBill/statisticsWaybill";


    public static final String URL_DRIVER_WAYBILL_QUERY = "DriverWayBillService/query";
    public static final String URL_LOCATION = "DriverWayBillService/queryhistory";
    public static final String URL_UNLOAD = "DriverWayBillService/end";

    public static final String URL_USER_QUERY_BY_ID = "user/querybyId";
    public static final String URL_USER_MODIFYSUBSCRIBE = "user/modifySubscribe";

    public static final String URL_BIND_BARCODE = "WayBill/querylist";
    public static final String URL_BATCH_LOCATION = "track/savelist";


    public static final String URL_RESOURCE_GROUP_CHECK = "GroupService/isGroupMermers";
    public static final String URL_RESOURCE_GROUP_DETAIL_BY_BARCODE = "GroupService/querybyBarCode";


    public static final String URL_GROUP_MEMBER_ADD = "GroupMember/save";
    public static final String URL_ADD_ACCOUNT = "AccountBook/addAccountInfo";
    public static final String URL_QUERY_ACCOUNTS_WITH_WAYBILLS = "AccountBook/queryAccountsWithWayBills";

    public static final String URL_SEARCH_BILL = "DriverWayBillService/querySearchhistory";

    public static final String URL_GET_WAYBILL_CODE = "WayBill/getWayBillCode";


    //回单扫描
    public static final String URL_WAYBILL_RECEIPT_STATUS = "ReceiptService/singleScan";
    //获取批次号
    public static final String URL_QUERY_BATCH_NUMBER = "ReceiptScanBatchService/queryBatchNumber";

    //批量修改回单状态
    public static final String URL_MODIFY_RECEIPT_STATUS = "ReceiptService/modifyReceiptStatus";
}

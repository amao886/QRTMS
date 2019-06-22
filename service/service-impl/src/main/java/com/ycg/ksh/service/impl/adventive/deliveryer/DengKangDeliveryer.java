package com.ycg.ksh.service.impl.adventive.deliveryer;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/18
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ycg.ksh.common.extend.http.HttpClient;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.util.encrypt.Base64;
import com.ycg.ksh.entity.persistent.adventive.Adventive;
import com.ycg.ksh.entity.persistent.adventive.AdventiveNote;
import com.ycg.ksh.service.core.entity.service.adventive.AdventiveTrack;
import com.ycg.ksh.service.impl.adventive.AdventiveDeliveryer;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 * 默认推送器
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/18
 */
@Service("ksh.core.service.adventive.deliveryer.dengkang")
public class DengKangDeliveryer implements AdventiveDeliveryer {

    private static final String CODE = "DENGKANG";
    private static final String CREDENTIALS = "RFC_CONN:dcrfc&*(conn12)";//用户名:密码
    private static final String AUTHORIZATION = "Basic " + Base64.encode(CREDENTIALS.getBytes());

    /**
     * 验证是否可以处理
     *
     * @param adventive
     * @param adventiveNote
     * @return
     */
    @Override
    public boolean validate(Adventive adventive, AdventiveNote adventiveNote) {
        if (adventiveNote != null && adventive != null) {
            return CODE.equalsIgnoreCase(adventive.getHisCode());
        }
        return false;
    }

    /**
     * 推送处理
     * @param adventive
     * @param adventiveNote
     * @param object
     * @return
     */
    public boolean process(Adventive adventive, AdventiveNote adventiveNote, Object object){
        if(StringUtils.isNotBlank(adventive.getDeliveryUrl())){//登陆后wsdl文件中的soap:address
            try{
                String jsonString = toJsonString(adventiveNote, object);//传给wsdl的数据
                System.out.println("request -> " + jsonString);
                HttpClient httpClient = HttpClient.createHttpClient(adventive.getDeliveryUrl(), HttpClient.Type.POST);
                httpClient.property("Authorization", AUTHORIZATION);//auth
                httpClient.property("Content-Type", "text/xml;charset=UTF-8");
                httpClient.setParameterString(xmlString(jsonString));
                String result = httpClient.request();
                System.out.println("response -> " + result);//要解析返回的数据 xml格式  找到关注的数据
                if(StringUtils.isNotBlank(result) && !result.contains("faultcode")){
                    return true;
                }
            } catch (Exception e){
                logger.error("数据推送异常 {}", adventiveNote, e);
            }
        }
        return false;
    }

    private String toJsonString(AdventiveNote adventiveNote, Object object) throws JsonProcessingException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        ObjectNode objectNode = Globallys.createObjectNode();
        LocalDateTime ctime = LocalDateTime.now();
        objectNode.putObject("CONTROL").put("SYSID", "YCWL").put("IFID", "OUT_TO_SAP_WAREHOUSE_STOCK").put("IFNO", "2134567").put("SCENEID", "WL009").put("SUBSCENEID", "").put("SMTYPE", "").put("SUSER", "CML").put("SDATE", ctime.format(dateformatter)).put("STIME", ctime.format(timeformatter)).put("SDATATYPE", "json").put("KEYDATA", adventiveNote.getOppositeKey());
        ObjectNode dataNode = objectNode.putArray("DATA").addObject();
        dataNode.putObject("HEAD").put(OPPOSITE_KEY, adventiveNote.getOppositeKey());
        ArrayNode itemNotes = dataNode.putArray("ITEM");
        if (object instanceof AdventiveTrack) {
            AdventiveTrack track = (AdventiveTrack) object;
            itemNotes.addObject().put("LOCATION", track.getLocation()).put("REPORTTIME", format.format(track.getReportTime()));
        }
        return Globallys.toJsonString(objectNode);
    }

    private String xmlString(String inputJsonString){
        StringBuilder builder = new StringBuilder();
        builder.append("<soap-env:Envelope xmlns:soap-env=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        builder.append("<soap-env:Header/>");
        builder.append("<soap-env:Body>");
        builder.append("<ns1:ZDATA_TO_SAP_SYNC2 xmlns:ns1='urn:sap-com:document:sap:rfc:functions'>");
        builder.append("<IN_JSON>" + inputJsonString + "</IN_JSON>");
        builder.append("</ns1:ZDATA_TO_SAP_SYNC2>");
        builder.append("</soap-env:Body>");
        builder.append("</soap-env:Envelope>");
        return builder.toString();
    }

/*
        <soap-env:Envelope xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/">
            <soap-env:Header/>
            <soap-env:Body>
                <soap-env:Fault>
                    <faultcode>
                        soap-env:Client
                        </faultcode>
                    <faultstring xml:lang="en">
                        Web 服务处理错误; 提供者端 Web 服务错误日志中的更多详细信息 （世界协调时间时戳 20180720033005；事务标识 4BE0B8A35A0F0410E005B13E9901DC74）
                    </faultstring>
                    <detail/>
                </soap-env:Fault>
            </soap-env:Body>
        </soap-env:Envelope>
        */
}

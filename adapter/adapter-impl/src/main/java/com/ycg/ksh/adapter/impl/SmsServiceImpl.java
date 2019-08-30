/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 10:08:22
 */
package com.ycg.ksh.adapter.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ycg.ksh.adapter.api.SmsService;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.extend.http.HttpClient;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.Validator;

/**
 * 短信接口实现
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-20 10:08:22
 */
@Service("ksh.net.service.smsService")
public class SmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    public static final String SMS_USERNAME = "sms.username";
    public static final String SMS_PASSWORD = "sms.password";
    public static final String SMS_SEND_URL = "sms.send.url";//短信发送
    public static final String SMS_YE_URL = "sms.ye.url";//短信获取余额
    public static final String SMS_SEND_NUMBER = "sms.send.number";//发送短信次数

    @Resource
    CacheManager cacheManager;

    /**
     * @developer Create by djq at 2017-12-20 10:08:22
     * @see SmsService#randomCode(int)
     * <p>
     */
    @Override
    public String randomCode(int count) {
        StringBuilder vcode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            vcode.append((int) (Math.random() * 9));
        }
        return vcode.toString();
    }

    private void sendRequest(String mobile, String content) throws ParameterException, BusinessException {
        logger.debug("发送短信请求信息 {} {}", mobile, content);
        HttpClient httpClient = HttpClient.createHttpClient(SystemUtils.get(SMS_SEND_URL),HttpClient.Type.POST);
        httpClient.property("ContentType", ContentType.create("text/xml"));
        httpClient.property("charset", "GB2312");
    	httpClient.setParameterString(content);
        try {
            String result = httpClient.post();
            logger.debug("发送短信请求信息 {}", httpClient);
            if (!"00".equals(result)) {
            	throw new BusinessException("发送短信异常: " + result);
            }
        } catch (Exception e) {
            logger.error("发送短信异常 {} {}", mobile, content, e);
            throw new BusinessException("发送短信异常");
        }
    }

    /**
     * 机密处理
     * @param encryptContent
     * @return
     */
    private String MD5(String encryptContent) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(encryptContent.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString().toUpperCase();

        } catch (NoSuchAlgorithmException e) {
        	logger.error("发送短信异常 {}", encryptContent ,e);
            throw new BusinessException("短信密码加密异常");
        }
        return result;
    }

    // 密码采用md5加密
    private String createSubmitXml(String mobile, String content) {
        StringBuffer sp = new StringBuffer();
        sp.append(String.format("<Group Login_Name=\"%s\" Login_Pwd=\"%s\" InterFaceID=\"0\" OpKind=\"0\">",
        		SystemUtils.get(SMS_USERNAME), MD5(SystemUtils.get(SMS_PASSWORD))));
        sp.append(String.format("<E_Time>%s</E_Time>", ""));
        sp.append("<Item>");
        sp.append("<Task>");
        sp.append("<Recive_Phone_Number>" + mobile+ "</Recive_Phone_Number>");
        sp.append("<Content><![CDATA[" + content + "]]></Content>");
        sp.append("<Search_ID>1</Search_ID>");
        sp.append("</Task>");
        sp.append("</Item>");
        sp.append("</Group>");

        return sp.toString();
    }


    /**
     * 发送短信验证码
     *
     * @param mobile
     * @param template
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public String sendCode(String mobile, String template) throws ParameterException, BusinessException {
        String code = randomCode(9);
        sendRequest(mobile, createSubmitXml(mobile, String.format(template, code)));
        cacheManager.set(SystemUtils.smsKey(mobile), code, 10L, TimeUnit.MINUTES);
        return code;
    }

    /**
     * 发送短信验证码
     *
     * @param mobile
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public String sendSmsCode(String mobile) throws ParameterException, BusinessException {
        Assert.notBlank(mobile, "手机号不能为空");
        Assert.regx(mobile, Validator.REGEX_MOBILE, Constant.CELL_PHONE_FORMAT_ERROR);
        Integer number = cacheManager.get(SMS_SEND_NUMBER+mobile);
        if (number == null){
            number = 1;
            cacheManager.set(SMS_SEND_NUMBER+mobile,number,30L, TimeUnit.MINUTES);
        }else if(number < 6){
            number++;
            cacheManager.set(SMS_SEND_NUMBER+mobile,number);
        }else{
            throw new BusinessException("该手机号发送次数过多，请稍后再试！");
        }
        String code = randomCode(6);
        String sendContent = String.format(Constant.SMS_CODE_STRING, code);
        sendRequest(mobile, createSubmitXml(mobile, sendContent));
        cacheManager.set(SystemUtils.smsKey(mobile), code, 10L, TimeUnit.MINUTES);
        return code;
    }

	@Override
	public void sendmsg(String mobile, String msg) throws ParameterException, BusinessException {
		sendRequest(mobile, createSubmitXml(mobile, msg));
	}
}

package com.ycg.ksh.api;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/22
 */

import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.exception.ExceptionUtil;
import com.ycg.ksh.common.exception.TMCException;
import com.ycg.ksh.common.log.LogAspect;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemKey;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.util.encrypt.MD5;
import com.ycg.ksh.entity.adapter.EmailMessage;
import com.ycg.ksh.common.extend.rabbitmq.MediaMessage;
import com.ycg.ksh.common.extend.rabbitmq.QueueKeys;
import com.ycg.ksh.adapter.api.MessageQueueService;
import com.ycg.ksh.entity.persistent.SysRequestSerial;
import com.ycg.ksh.service.api.SysRequestSerialService;
import is.tagomor.woothee.Classifier;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 控制层方法拦截器
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/22
 */
public class ControllerAspect extends LogAspect {

    final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    private static final Pattern PATH_PATTERN = Pattern.compile("(\\/[a-zA-Z-]+)+");

    @Resource
    SysRequestSerialService serialService;
    @Resource
    private MessageQueueService queueService;


    /**
     * @param proceedingJoinPoint 当前进程中的连接点
     * @return
     * @throws Throwable
     */
    public Object aroundAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object object = null;
        Throwable throwable = null;
        long stime = System.currentTimeMillis();
        try {
            Object[] args = proceedingJoinPoint.getArgs();
            if (args != null && args.length > 0){
                object = proceedingJoinPoint.proceed(args);
            }else{
                object = proceedingJoinPoint.proceed();
            }
        }catch (Throwable ex){
            throw (throwable = ex);
        } finally {
            buildRequestSerial(proceedingJoinPoint, object, throwable, stime);
        }
        return object;
    }

    /**
     * @param proceedingJoinPoint
     * @param returnValue
     * @param throwable
     * @param stime
     */
    private void buildRequestSerial(final ProceedingJoinPoint proceedingJoinPoint, final Object returnValue, final Throwable throwable, long stime){
        HttpServletRequest request = findRequest(proceedingJoinPoint);
        Globallys.executor().execute(() -> {
            try {
                Date cdate = new Date();
                SysRequestSerial serial = new SysRequestSerial();
                serial.setTime(cdate.getTime() - stime);
                serial.setDateTime(cdate);
                serial.setClassName(proceedingJoinPoint.getTarget().getClass().getName());
                Signature signature = proceedingJoinPoint.getSignature();
                serial.setSignature(signature.getName());
                String ct = serial.getContentType();
                if(ct != null && StringUtils.contains(ct.toLowerCase(), "json")){
                    serial.setArgs(parameters((MethodSignature) signature, proceedingJoinPoint.getArgs()));
                } else {
                    serial.setArgs(parameters(request));
                }
                if(request != null){
                    Object category = RequestUitl.getValue(request, RequestUitl.CATEGORY_KEY);
                    if(category != null){
                        serial.setAppType(category.toString());
                    }
                    Object guestsKey = RequestUitl.getValue(request, RequestUitl.GUESTS_KEY);
                    if(guestsKey != null){
                        serial.setUserKey(guestsKey.toString());
                    }
                    serial.setSessionKey(RequestUitl.getSessionId(request));
                    serial.setUri(request.getRequestURI());
                    serial.setHost(RequestUitl.getRemoteHost(request));
                    serial.setMethod(request.getMethod());
                    serial.setAgentString(request.getHeader("User-Agent"));
                    serial.setContentType(request.getHeader("Content-Type"));
                }

                if(returnValue != null){
                    if(returnValue instanceof JsonResult){
                        JsonResult result = (JsonResult) returnValue;
                        serial.setReturnValue("success:"+ result.isSuccess()+", message:"+ result.getMessage());
                    }else{
                        serial.setReturnValue(returnValue.getClass().getSimpleName());
                    }
                }
                completeSomething(serial);
                if(throwable != null){
                    serial.setException(throwable.getClass().getName());
                    if(throwable instanceof TMCException){
                        serial.setException(serial.getException() + "->"+ throwable.getMessage());
                    }else{
                        /*try{
                            EmailMessage message = EmailMessage.controller(SystemUtils.get(SystemKey.SYS_CURRENT_ENV));
                            message.setSubject("合同物流管理平台异常通知 ---> "+ message.getEnvironment());
                            message.setTime(System.currentTimeMillis());
                            message.setExceptions(serial.toMap());
                            message.setOtherString(ExceptionUtil.printStackTraceToString(throwable));
                            queueService.sendNetMessage(new MediaMessage(QueueKeys.MESSAGE_TYPE_EMAIL, StringUtils.UUID(), message));
                        }catch (Exception e){
                            e.printStackTrace();
                        }*/
                    	throw new Exception(throwable.getMessage());
                    }
                }


                StringBuilder builder = new StringBuilder();
                builder.append("\r\n").append("++++++++++++++++++++++++++++++++++++++++++++请求日志+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                builder.append("\r\n").append(log(serial));
                builder.append("\r\n").append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                logger.info(builder.toString());

                if(StringUtils.isNotBlank(serial.getAppType())){
                    serialService.save(serial);
                }
            } catch (Exception e) {
                logger.error("接口访问信息记录异常", e);
            }
        });
    }

    private void completeSomething(final SysRequestSerial serial){
        if(StringUtils.isNotBlank(serial.getAgentString())){
            Map<String, String> agents = Classifier.parse(serial.getAgentString());
            if(agents != null) {
                serial.setOs(agents.get("os") +"-"+ agents.get("os_version"));
                serial.setBrowser(agents.get("name") +"-"+ agents.get("version"));
            }
        }
        if(StringUtils.isNotBlank(serial.getUri())) {
            Matcher matcher = PATH_PATTERN.matcher(serial.getUri());
            if(matcher.find()) {
                serial.setStandardUri(matcher.group());
            }else {
                serial.setStandardUri(serial.getUri());
            }
            serial.setUriKey(MD5.encrypt(serial.getStandardUri()).toUpperCase());
        }
        if(StringUtils.isBlank(serial.getContentType())){
            serial.setContentType("application/x-www-form-urlencoded");
        }
    }

    private HttpServletRequest findRequest(ProceedingJoinPoint proceedingJoinPoint){
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            if(arg instanceof HttpServletRequest){
                return (HttpServletRequest) arg;
            }
        }
        return null;
    }

    private String parameters(MethodSignature signature, Object[] args){
        StringBuilder _builder = new StringBuilder();
        String[] names = signature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if(arg instanceof HttpServletRequest || arg instanceof HttpServletResponse || arg instanceof Model){
                continue;
            }
            _builder.append(names[i]).append("=").append(arg).append("、");
        }
        if(_builder.length() > 0){
            _builder.deleteCharAt(_builder.length() - 1);
        }
        return _builder.toString();
    }

    private String parameters(HttpServletRequest request) {
        if(request != null){
            StringBuilder _builder = new StringBuilder();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                _builder.append(paramName).append("=").append(request.getParameter(paramName)).append("、");
            }
            if(_builder.length() > 0){
                _builder.deleteCharAt(_builder.length() - 1);
            }
            return _builder.toString();
        }
        return null;
    }

    private StringBuilder log(SysRequestSerial serial) {
        StringBuilder builder = new StringBuilder();
        if(StringUtils.isNotEmpty(serial.getHost())){
            builder.append("客户端IP\t").append(serial.getHost());
        }
        if(StringUtils.isNotEmpty(serial.getUri())){
            builder.append("\r\n请求地址\t").append(serial.getUri());
        }
        if(StringUtils.isNotEmpty(serial.getMethod())){
            builder.append("\r\n请求方式\t").append(serial.getMethod());
        }
        if(StringUtils.isNotEmpty(serial.getContentType())){
            builder.append("\r\n数据格式\t").append(serial.getContentType());
        }
        if(StringUtils.isNotEmpty(serial.getSessionKey())){
            builder.append("\r\n会话标识\t").append(serial.getSessionKey());
        }
        if(StringUtils.isNotEmpty(serial.getAppType())){
            builder.append("\r\n请求类别\t").append(serial.getAppType());
        }
        if(StringUtils.isNotEmpty(serial.getUserKey())){
            builder.append("\r\n用户标识\t").append(serial.getUserKey());
        }
        builder.append("\r\n类名方法\t").append(serial.getClassName()).append("->").append(serial.getSignature());
        if(StringUtils.isNotEmpty(serial.getArgs())){
            builder.append("\r\n请求参数\t").append(serial.getArgs());
        }
        if(StringUtils.isNotEmpty(serial.getReturnValue())){
            builder.append("\r\n返回数据\t").append(serial.getReturnValue());
        }
        if(StringUtils.isNotEmpty(serial.getException())){
            builder.append("\r\n执行异常\t").append(serial.getException());
        }
        builder.append("\r\n方法耗时\t").append(serial.getTime()).append("毫秒");
        if(StringUtils.isNotEmpty(serial.getAgentString())){
            builder.append("\r\nAgent\t").append(serial.getAgentString());
        }
        return builder;
    }
}

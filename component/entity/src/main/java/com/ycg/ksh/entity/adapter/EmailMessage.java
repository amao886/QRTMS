package com.ycg.ksh.entity.adapter;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/20
 */

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.DateFormatUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常消息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/20
 */
public class EmailMessage extends BaseEntity {


    private static final String DATAFORMAT = "yyyy-MM-dd HH:mm:ss";


    private String terminal;//终端
    private String environment;//环境
    private Long time;//时间
    private String subject;//
    private String otherString;//

    public EmailMessage() {
    }

    private EmailMessage(String terminal, String environment) {
        this.terminal = terminal;
        this.environment = environment;
    }

    public static EmailMessage service(String environment) {
        return new EmailMessage("服务端逻辑层", environment);
    }
    public static EmailMessage controller(String environment) {
        return new EmailMessage("服务端控制层", environment);
    }
    public static EmailMessage mobile(String environment) {
        return new EmailMessage("移动端", environment);
    }

    public void add(String key, Object value){
        if(exceptions ==  null){
            exceptions = new HashMap<String, Object>();
        }
        exceptions.put(key, value);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String context(){
        StringBuilder builder = new StringBuilder();
        builder.append("====================================================================\n");
        builder.append("位置:").append(terminal).append("\n");
        builder.append("环境:").append(environment).append("\n");
        builder.append("时间:").append(DateFormatUtils.format(time, DATAFORMAT)).append("(").append(time).append(")").append("\n");
        builder.append("====================================================================\n");
        for (Map.Entry<String, Object> entry : exceptions.entrySet()) {
            if(entry.getValue() != null){
                builder.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
            }
        }
        builder.append("====================================================================\n");
        builder.append("异常堆栈:").append(otherString).append("\n");
        builder.append("====================================================================\n");
        return builder.toString();
    }

    private Map<String, Object> exceptions;

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Map<String, Object> getExceptions() {
        return exceptions;
    }

    public void setExceptions(Map<String, Object> exceptions) {
        this.exceptions = exceptions;
    }

    public String getOtherString() {
        return otherString;
    }

    public void setOtherString(String otherString) {
        this.otherString = otherString;
    }
}

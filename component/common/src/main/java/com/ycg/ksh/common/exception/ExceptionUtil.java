package com.ycg.ksh.common.exception;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/21
 */

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/21
 */
public class ExceptionUtil {

    public static String printStackTraceToString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try(PrintWriter writer = new PrintWriter(sw, true)){
            throwable.printStackTrace(writer);
            writer.flush();
            return sw.getBuffer().toString();
        }finally {
            try{
                sw.close();
            }catch (Exception e){}
        }
    }

}

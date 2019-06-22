/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-29 16:16:11
 */
package com.ycg.ksh.common.barcode;

import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.StringUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 条码生成器
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-29 16:16:11
 */
public class CodeBuilder {
    
    private static final char INIT = '0';
    
    private static final int day_len = 5;
    private int len;
    private long max;
    private char[] chars;
    private long daypart;
    private long seqpart;
    
    public static CodeBuilder build() {
        return build(6, null);
    }
    
    public static CodeBuilder build(String lastCode) {
        return build(6, lastCode);
    }
    
    public static CodeBuilder build(int len, String lastCode) {
        return new CodeBuilder(len, lastCode);
    }
    
    private CodeBuilder(int len, String lastCode) {
        super();
        this.len = len;
        if(StringUtils.isNotBlank(lastCode)) {
            if(!Pattern.matches("^\\d{"+ (day_len + len) +",}$", lastCode)) {
                throw new ParameterException("最后的条码号["+ lastCode +"]不满足格式") ;
            }
            this.daypart = this.daypart(lastCode);
            this.seqpart = this.seqpart(lastCode);
        }else {
            this.daypart = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis());
            this.seqpart = 0L;
        }
        this.max = this.value(len, 9);
        this.chars = new char[day_len + len];
        this.init();
    }

    private CodeBuilder() {
        super();
    }
    
    private long value(int count, int num) {
        long value = 0;
        for (int i = 0; i < count; i++) {
            value += num * (Math.pow(10, i));
        }
        return value;
    }
    public String next() {
        if(seqpart > max) {
            throw new ParameterException("超出范围");
        }
        char[] temps = chars(daypart);
        System.arraycopy(temps, 0, chars, day_len - temps.length, temps.length);
        temps = chars(++seqpart);
        System.arraycopy(temps, 0, chars, day_len + len - temps.length, temps.length);
        return codeString();
    }
    
    private String codeString() {
        String code = new String(chars);
        init();
        return code;
    }
    
    private void init() {
        Arrays.fill(chars, INIT);
    }
    
    private char[] chars(long number) {
        return String.valueOf(number).toCharArray();
    }
    
    private long daypart(String codeString) {
        return Long.parseLong(codeString.substring(0, day_len));
    }
    private long seqpart(String codeString) {
        return Long.parseLong(codeString.substring(day_len, day_len + len));
    }
}

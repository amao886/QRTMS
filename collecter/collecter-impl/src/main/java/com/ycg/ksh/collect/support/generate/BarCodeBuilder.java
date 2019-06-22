package com.ycg.ksh.collect.support.generate;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/19 0019
 */

import com.ycg.ksh.common.barcode.CodeBuilder;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 条码生成
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/19 0019
 */
public class BarCodeBuilder {

    private static final int SEQ_LEN = 6;
    private static final String FORMAT = "%d%0"+ SEQ_LEN +"d";

    private long minCode;
    private long maxCode;
    private long daypart;
    private long seqpart;
    private long count;

    public static long day(long millis){
        return TimeUnit.MILLISECONDS.toDays(millis);
    }

    public BarCodeBuilder(long day, long max) {
        super();
        this.daypart = day;
        this.seqpart = max;
    }
    private long format() {
        return Long.parseLong(String.format(FORMAT, daypart, seqpart));
    }

    public long next() {
        seqpart++;
        if(minCode <= 0){
            minCode = seqpart;
        }
        maxCode = seqpart;
        count++;
        return format();
    }

    public Collection<Long> build(int _count){
        Collection<Long> collection = new ArrayList<Long>(_count);
        for (int i = 0; i < _count; i++) {
            collection.add(next());
        }
        return collection;
    }

    public String batch(){
        return String.format(FORMAT, daypart, minCode);
    }

    public String minCode(){
        return String.format(FORMAT, daypart, minCode);
    }

    public String maxCode(){
        return String.format(FORMAT, daypart, maxCode);
    }


    public long getMinCode() {
        return minCode;
    }

    public long getMaxCode() {
        return maxCode;
    }

    public long getCount() {
        return count;
    }
}

/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-07-04 19:06:32
 */
package com.ycg.ksh.common.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Stack;

/**
 * 任意进制转换工具
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-07-04 19:06:32
 */
public class HexConvert {

    private static final char[] DIGTHS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String from10ToString(long num, int base){
        if(base > 16){
            throw new RuntimeException("进制数超出范围，base<=16");
        }
        Stack<Character> stack = new Stack<Character>();
        while(num != 0){
            stack.push(DIGTHS[(int) (num % base)]);
            num /= base;
        }
        StringBuilder builder = new StringBuilder();
        while(!stack.isEmpty()){
            builder.append(stack.pop());
        }
        return builder.toString();
    }

    /**
     * 任意进制转换
     * <pre>
     *  2  : 二进制
     *  8  : 八进制
     *  10 : 十进制
     *  16 : 十六进制
     * </pre>
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-07-04 19:34:48
     * @param num 要转换的数字字符串
     * @param srcHex 当前数字的进制
     * @param destHex 要转换的进制
     * @return 转换后的字符串
     */
    public static String convert(Serializable serializable, int srcHex, int destHex){
        String numberString = String.valueOf(serializable);
        if(srcHex == destHex){
            return numberString;
        }
        char[] chars = String.valueOf(numberString).toUpperCase().toCharArray();
        int len = chars.length;
        if(destHex != 10){//目标进制不是十进制 先转化为十进制
            numberString = convert(numberString, srcHex, 10);
        }else{
            int n = 0;
            for(int i = len - 1; i >= 0; i--){
                n += Arrays.binarySearch(DIGTHS, chars[i]) * Math.pow(srcHex, len - i - 1);
            }
            return n + "";
        }
        return from10ToString(Long.valueOf(numberString), destHex);
    }

    public static void main(String[] args) {
        System.out.println(HexConvert.convert(10, 10, 2));
    }
}

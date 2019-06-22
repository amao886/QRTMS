package com.ycg.ksh.common.util;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/25
 */

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 地区工具类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/25
 */
public class RegionUtils {
    public static final String EMPTY = "";

    public static final String CH = "中国";

    public static final char SPILT = '-';

    public static final Pattern completion = Pattern.compile("\\(.*\\)");
    public static final Pattern regex = Pattern.compile("(?<province>[^省]+自治区|上海(市?)|天津(市?)|重庆(市?)|北京(市?)|.*?省|.*?行政区|河北|山西|内蒙古|辽宁|吉林|黑龙江|江苏|浙江|安徽|福建|江西|山东|河南|湖北|湖南|广东|广西|广西壮族|海南|四川|贵州|云南|西藏|陕西|甘肃|青海|宁夏|宁夏回族|新疆|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.*?行政区划|.+盟|市辖区|.*?市|.*?县|.+区)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)");

    private static final Map<String, String> SPECIAL = new HashMap<String, String>();
    private static final Map<String, String> MUNICIPALITY = new HashMap<String, String>();
    private static final Map<Object, String> completions = new HashMap<Object, String>();

    static {
        SPECIAL.put("北京市", "北京");
        SPECIAL.put("上海市", "上海");
        SPECIAL.put("重庆市", "重庆");
        SPECIAL.put("天津市", "天津");
        SPECIAL.put("广西壮族", "广西壮族自治区");
        SPECIAL.put("广西", "广西壮族自治区");
        SPECIAL.put("西藏", "西藏自治区");
        SPECIAL.put("内蒙古", "内蒙古自治区");
        SPECIAL.put("新疆", "新疆维吾尔自治区");
        SPECIAL.put("宁夏回族", "宁夏回族自治区");
        SPECIAL.put("宁夏", "宁夏回族自治区");

        MUNICIPALITY.put("北京市", "北京");
        MUNICIPALITY.put("上海市", "上海");
        MUNICIPALITY.put("重庆市", "重庆");
        MUNICIPALITY.put("天津市", "天津");

        completions.put(Pattern.compile("河北|山西|辽宁|吉林|黑龙江|江苏|浙江|安徽|福建|江西|山东|河南|湖北|湖南|广东|海南|四川|贵州|云南|陕西|甘肃|青海|"), "省");
        completions.put(Pattern.compile("西藏|广西壮族|内蒙古|宁夏回族|新疆维吾尔|"), "自治区");
        completions.put("广西", "壮族自治区");
        completions.put("宁夏", "回族自治区");
        completions.put("新疆", "维吾尔自治区");
    }

    public static String merge(String province, String city, String district){
        return merge(SPILT, province, city, district);
    }

    public static String merge(char spilt, String...regions){
        return merge(regions, spilt);
    }

    public static String mergeEmpty(String...regions){
        return StringUtils.join(regions);
    }

    public static String merge(String[] regions, char spilt){
        if(validate(regions)){
            return StringUtils.join(regions, spilt);
        }
        return null;
    }
    public static String merge(String[] regions){
        return merge(regions, SPILT);
    }

    public static String[] split(String source){
        return source.split("[^\\u4e00-\\u9fa5]+");
    }

    public static boolean validate(String[] regions){
        if(regions == null || regions.length < 3){
            return false;
        }
        for (String region : regions) {
            if(StringUtils.isBlank(region)){
                return false;
            }
        }
        return true;
    }

    public static String simple(String source){
        if(StringUtils.isNotBlank(source)) {
            String[] arrays = split(source);
            if(arrays != null && validate(arrays)) {
                if(arrays.length >= 3){
                    return simple(arrays[0], arrays[1], arrays[2]);
                }else{
                    return arrays[0];
                }
            }
        }
        return null;
    }
    public static String simple(String[] regions){
        if(validate(regions)){
            if(StringUtils.isNotBlank(regions[0])){
                String _city = SPECIAL.get(regions[0]);
                if(StringUtils.isBlank(_city)){
                    _city = SPECIAL.get(regions[0] + regions[1]);
                }
                if(StringUtils.isNotBlank(_city)){
                    return _city;
                }
            }
            return regions[1];
        }
        return null;
    }
    public static String simple(String province, String city, String district){
        if(StringUtils.isNotBlank(province)){
            String _city = SPECIAL.get(province);
            if(StringUtils.isBlank(_city)){
                _city = SPECIAL.get(province + city);
            }
            if(StringUtils.isNotBlank(_city)){
                return _city;
            }
        }
        return city;
    }


    public static String replace(String address){
        if(StringUtils.isNotBlank(address)){
            return address.replaceAll("[^\u4E00-\u9FA5]+", "");
        }
        return address;
    }

    public static String analyze(String[] address){
        return String.join(EMPTY, address);
    }

    public static String[] analyze(String address){
        return Stream.of(analyze(address, true)).limit(3).toArray(String[]::new);
    }

    public static String analyze(String address, int level){
        return Stream.of(analyze(address, true)).limit(level).collect(Collectors.joining());
    }


    public static String[] analyze(String address, boolean last){
        String[] arrays = new String[last ? 4 : 3];
        Arrays.fill(arrays, EMPTY);
        address = Optional.ofNullable(address).map(a->{
            if(a.startsWith(CH)){
                a = a.replace(CH, EMPTY);
            }
            return deleteBrackets(a);
        }).orElse(EMPTY);
        Matcher m = regex.matcher(address);
        if(m.find()){
            arrays[0] = Optional.ofNullable(m.group("province")).orElse(EMPTY);
            arrays[1] = Optional.ofNullable(m.group("city")).orElse(EMPTY);
            arrays[2] = Optional.ofNullable(m.group("county")).orElse(EMPTY);
            if(last){
                arrays[3] = Optional.of(address.replaceAll(arrays[0] + arrays[1] + arrays[2], "")).orElse(EMPTY);
            }
            for (int i = 0; i < arrays.length; i++) {
                arrays[i] = StringUtils.trim(arrays[i]);
            }
            dosomething(arrays);
        }else{
            if(last){
                arrays[3] = StringUtils.trim(address);
            }
        }
        return arrays;
    }

    public static String deleteBrackets(String source){
        if(StringUtils.isNotBlank(source)){
            return source.replaceAll("\\(.*\\)", "");
        }
        return source;
    }
    private static void dosomething(String[] arrays){
        for (Map.Entry<Object, String> entry : completions.entrySet()) {
            Object key = entry.getKey();
            if(key instanceof Pattern){
                if(((Pattern) key).matcher(arrays[0]).matches()){
                    arrays[0] = arrays[0] +  entry.getValue();
                }
            }else{
                if(StringUtils.equals(arrays[0], entry.getKey().toString())){
                    arrays[0] = arrays[0] +  entry.getValue();
                }
            }
        }
        if(StringUtils.equelsOne(arrays[0], "上海市", "北京市", "重庆市", "天津市")){
            if(!StringUtils.equals(arrays[0], arrays[1])){
                arrays[2] = arrays[1];
                arrays[1] = arrays[0];
                arrays[0] = SPECIAL.getOrDefault(arrays[0], arrays[0]);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(RegionUtils.analyze("北京市青浦区阳城路200号", true)));
        System.out.println(Arrays.toString(RegionUtils.analyze("宁夏石家庄市长安区长安新村", true)));
        System.out.println(RegionUtils.analyze("宁夏石家庄市长安区长安新村", 1));
        System.out.println(RegionUtils.analyze("发货地址", 3));
        System.out.println(RegionUtils.analyze("上海市普陀区桃浦镇水厂路239号(普陀区桃浦镇水厂路239号)", 2));
        System.out.println(RegionUtils.analyze(RegionUtils.deleteBrackets("上海市普陀区桃浦镇水厂路239号(普陀区桃浦镇水厂路239号)"), 2));
        //System.out.println(Arrays.toString(RegionUtils.analyze("上海市青浦区阳城路200号", true)));
    }
}

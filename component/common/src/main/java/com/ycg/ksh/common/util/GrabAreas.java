package com.ycg.ksh.common.util;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/27
 */

import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @TODO 类描述
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/8/27
 */
public class GrabAreas {

    private static final Pattern keyregx = Pattern.compile("\\d+");
    private static final Pattern nameregx = Pattern.compile("[\\u4e00-\\u9fa5]+");
    private static final Pattern commonregx = Pattern.compile("<td><a href='(\\d+/)?\\d+.html'>[\\u4e00-\\u9fa5]+(<br/>)?</a></td>");
    private static final Pattern commonregx2 = Pattern.compile("<td>\\d+</td><td>[\\u4e00-\\u9fa5]+</td>");



    private static final String p = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/index.html";
    //<td><a href='11.html'>北京市<br/></a></td><td>
    private static final String c = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/%s.html";
    //<td><a href='42/4201.html'>武汉市</a></td>
    private static final String d = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/%s/%s.html";
    //<td>420101000000</td><td>市辖区</td></tr>
    //<td><a href='01/420102.html'>江岸区</a></td>
    private static final String s = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/%s/%s/%s.html";
    //<td><a href='02/420102002.html'>大智街办事处</a></td>
    private static final String cc = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/%s/%s/%s/%s.html";
    //<td>420102002003</td><td>111</td><td>保成社区</td>

    private static Collection<String[]> load(String urlString, int count) throws Exception {
        Collection<String[]> collection = new ArrayList<String[]>();
        URL url = new URL(urlString);
        HttpURLConnection httpUrl = (HttpURLConnection)url.openConnection();
        try(InputStream is = httpUrl.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is,"gb2312"))){
            String line;
            Pattern pattern = Pattern.compile("\\d{"+ count +",}+");
            while ((line = br.readLine()) != null) {
                Matcher matcher = commonregx.matcher(line);
                boolean find = false;
                while (matcher.find()){
                    find = true;
                    String str = matcher.group();
                    if(str != null && str.length() > 0){
                        collection.add(new String[]{key(str, pattern), name(str, nameregx)});
                    }
                }
                if(!find){
                     matcher = commonregx2.matcher(line);
                    while (matcher.find()){
                        String str = matcher.group();
                        if(str != null && str.length() > 0){
                            collection.add(new String[]{key(str, keyregx), name(str, nameregx)});
                        }
                    }
                }
            }
        }
        return collection;
    }

    private static String key(String str, Pattern pattern){
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()){
            return matcher.group();
        }
        return null;
    }
    private static String key(String str, int count){
        return key(str, Pattern.compile("\\d{"+ count +"}+"));
    }

    private static String name(String str, Pattern pattern){
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()){
            return matcher.group();
        }
        return null;
    }

    private static final String  sql = "INSERT INTO T_AREA (ID, `NAME`, PID) VALUE ('%s', '%s', '%s');";


    public static Collection<Object> load(String fileName) throws Exception {
        try(BufferedWriter out = new BufferedWriter(new FileWriter(fileName))){
            Collection<String[]> provinces = load(p, 2);
            int p = 1;
            System.out.println(provinces.stream().map(ps -> ps[0]).collect(Collectors.toList()));
            for (String[] province : provinces) {
                Integer pkey = Integer.parseInt(province[0]);
//                if(pkey != 44 && pkey != 46){
//                    continue;
//                }
                System.out.println("开始处理"+ province[0] +"->"+ province[1]);
                out.write(String.format(sql, province[0], province[1], "0"));
                out.newLine();
                Collection<String[]> citys = load(String.format(c, province[0]), 4);
                int c = 1;
                for (String[] city : citys) {
                    out.write(String.format(sql, city[0], city[1], province[0]));
                    out.newLine();
                    Collection<String[]> districts = load(String.format(d, province[0], city[0]), 6);
                    for (String[] district : districts) {
                        out.write(String.format(sql, district[0], district[1], city[0]));
                        out.newLine();
                        /*
                        Collection<String[]> streets = load(String.format(s, province[0], city[0].substring(2), district[0]), 8);
                        for (String[] street : streets) {
                            System.out.println("\t\t\t街-> "+ street[0] +"-"+ street[1]);
                            out.write("\t\t\t街-> "+ street[0] +"-"+ street[1]);
                            out.newLine();
                        }
                        */
                    }
                    Thread.sleep(2000);
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        GrabAreas.load("D:\\workspace\\projects\\ycg\\master\\area.sql");
    }
}

package com.ycg.ksh.common.util;

public abstract class ZookeeperUtil {

	public static final String SEPARATOR = "/";  
	  
    /** 
     * 转换path为zk的标准路径 以/开头,最后不带/ 
     */  
    public static String normalize(String path) {  
        if(path == null){
            return "";
        }
        String temp = path;
        if(!path.startsWith(SEPARATOR)) {  
            temp = SEPARATOR + path;  
        }  
        if(path.endsWith(SEPARATOR)) {  
            temp = temp.substring(0, temp.length() - 1);  
            return normalize(temp);  
        }else {  
            return temp;  
        }  
    } 
    
    public static String last(String path){
        String temp = normalize(path);
        return temp.substring(temp.lastIndexOf(SEPARATOR) + 1);
    }
    
  
    /** 
     * 链接两个path,并转化为zk的标准路径 
     */  
    public static String contact(String path1,String path2){  
        return normalize(path1) + normalize(path2);  
    }

    public static String contact(String...paths){
        StringBuilder builder = new StringBuilder();
        for (String path : paths) {
            builder.append(normalize(path));
        }
        return builder.toString();
    }

}

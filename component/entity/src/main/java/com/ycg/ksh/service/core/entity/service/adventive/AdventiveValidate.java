package com.ycg.ksh.service.core.entity.service.adventive;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.constant.ObjectType;
import com.ycg.ksh.entity.persistent.adventive.AdventivePull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;

public class AdventiveValidate extends BaseEntity {


    private static final String EMPTY = "";
    public static final Pattern regex = Pattern.compile("S(?<s>\\d+)#R(?<r>\\d+)#C(?<c>\\d+)");



    //S:发货方,R:收货方,C:承运商
    private static final String S = "S";
    private static final String R = "R";
    private static final String C = "C";

    private String[] includes;//包含的
    private String[] excludes;//排除的
    private Collection<ObjectType> dataTypes;//关注的数据

    /**
     * 初始化校验
     * @param pull
     * @return
     */
    public static AdventiveValidate initialize(AdventivePull pull){
        AdventiveValidate validate = new AdventiveValidate();
        validate.includes = items(pull.getIncludes());
        validate.excludes = items(pull.getExcludes());
        validate.dataTypes = datatypes(pull.getDataTypes());
        return validate;
    }

    public static Collection<ObjectType> datatypes(String configString){
        if(StringUtils.isNotBlank(configString)){
            Collection<ObjectType> dataTypes = new ArrayList<ObjectType>();
            StringTokenizer tokenizer = new StringTokenizer(StringUtils.trim(configString), ";");
            while(tokenizer.hasMoreElements()){
                dataTypes.add(ObjectType.convert(StringUtils.trim(tokenizer.nextToken()).toUpperCase()));
            }
            return dataTypes;
        }
        return null;
    }

    public static Collection<String[]> collection(String configString){
        if(StringUtils.isNotBlank(configString)){
            Collection<String[]> includes = new ArrayList<String[]>();
            StringTokenizer tokenizer = new StringTokenizer(StringUtils.trim(configString), ";");
            while(tokenizer.hasMoreElements()){
                includes.add(items(tokenizer.nextToken()));
            }
            return includes;
        }
        return null;
    }


    private static String[] items(String itemString){
        String[] items = new String[]{EMPTY, EMPTY, EMPTY};
        if(StringUtils.isNotBlank(itemString)){
            StringTokenizer tokenizer = new StringTokenizer(StringUtils.trim(itemString), "#");
            while(tokenizer.hasMoreElements()){
                String itemKey = StringUtils.trim(tokenizer.nextToken());
                if(StringUtils.isNotBlank(itemKey)){
                    String type = itemKey.substring(0, 1), key = StringUtils.trim(itemKey.substring(1));
                    if(S.equalsIgnoreCase(type)){
                        items[0] = key;
                    }else if(R.equalsIgnoreCase(type)){
                        items[1] = key;
                    }else{
                        items[2] = key;
                    }
                }
            }
        }
        return items;
    }


    public boolean validate(Serializable _shipperKey, Serializable _receiveKey, Serializable _conveyKey, ObjectType objectType){
        boolean validate = false;
        if(CollectionUtils.isNotEmpty(dataTypes)){
            validate = dataTypes.stream().anyMatch(d -> d == objectType);
        }
        if(validate && ArrayUtils.isNotEmpty(includes)){
            validate = validate(includes, _shipperKey, _receiveKey, _conveyKey);
        }
        if(validate && ArrayUtils.isNotEmpty(excludes)){
            validate = !validate(excludes, _shipperKey, _receiveKey, _conveyKey);
        }
        return validate;
    }

    private boolean validate(String[] arrays, Serializable _shipperKey, Serializable _receiveKey, Serializable _conveyKey){
        boolean validate = false;
        if(StringUtils.isNotBlank(arrays[0])){
            validate = StringUtils.equalsIgnoreCase(arrays[0] , String.valueOf(_shipperKey));
        }
        if(StringUtils.isNotBlank(arrays[1])){
            validate = StringUtils.equalsIgnoreCase(arrays[1] , String.valueOf(_receiveKey));
        }
        if(StringUtils.isNotBlank(arrays[2])){
            validate = StringUtils.equalsIgnoreCase(arrays[2] , String.valueOf(_conveyKey));
        }
        return validate;
    }
}

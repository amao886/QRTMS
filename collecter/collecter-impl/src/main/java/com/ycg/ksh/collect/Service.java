package com.ycg.ksh.collect;

import org.apache.commons.lang.ArrayUtils;

public class Service {


    public static void main(String[] args) {
        new com.ycg.ksh.common.dubbo.Service("collect", ArrayUtils.isNotEmpty(args) ? args[0] : "dev").execute();
    }
}
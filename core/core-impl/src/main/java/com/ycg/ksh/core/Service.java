package com.ycg.ksh.core;

import org.apache.commons.lang.ArrayUtils;

public class Service {

    public static void main(String[] args) {
        new com.ycg.ksh.common.dubbo.Service("core", ArrayUtils.isNotEmpty(args) ? args[0] : "dev").execute();
    }
}
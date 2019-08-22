package com.ycg.ksh.service;

import org.apache.commons.lang.ArrayUtils;

public class Service {

    public static void main(String[] args) {
        new com.ycg.ksh.common.dubbo.Service("service", ArrayUtils.isNotEmpty(args) ? args[0] : "local").execute();
    }
}
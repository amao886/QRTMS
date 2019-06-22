package com.ycg.ksh.service.support.excel;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/27
 */
public class Property {

    private String name;
    private String showName;
    private boolean append;

    public Property(String name, String showName) {
        this.name = name;
        this.showName = showName;
    }
    public Property(String name, String showName, boolean append) {
        this.name = name;
        this.showName = showName;
        this.append = append;
    }

    public String getName() {
        return name;
    }
    public String getShowName() {
        return showName;
    }

    public boolean isAppend() {
        return append;
    }
}

package com.ycg.ksh.service.support.assist;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * User: wyj
 * 茅台仓库,方便管理和数据库的下标与文字转换
 */


public enum MaotaiDepot {
    DEFAULT(0,"无仓库"),
    MAOTAI(1,"茅台库"),
    GUIZHOU(2,"贵州库"),
    ZHENGZHOU(3,"郑州库"),
    SHENYANG(4,"沈阳库"),
    BEIJING(5,"北京库"),
    SHANGHAI(6,"上海库"),
    HANGZHOU(7,"杭州库"),
    XIAN(8,"西安库"),
    LANZHOU(9,"兰州库"),
    ZHUHAI(10,"珠海库")
    ;
    MaotaiDepot(Integer index,String depot){
        this.depot = depot;
        this.index = index;
    }
    //下标
    private Integer index;
    //仓库名称
    private String depot;

    public Integer getIndex() {
        return index;
    }
    public void setIndex(Integer index) {
        this.index = index;
    }
    public String getDepot() {
        return depot;
    }
    public void setDepot(String depot) {
        this.depot = depot;
    }

    public static  Integer searchIndexFromDepot(String depot){
        for (MaotaiDepot maotaiDepot : MaotaiDepot.values()) {
            if(maotaiDepot.getDepot().equals(depot)){
                return maotaiDepot.getIndex();
            }
        }
        return MaotaiDepot.DEFAULT.getIndex();
    }

    public static String searchDepotFromIndex(Integer index){
        for (MaotaiDepot maotaiDepot : MaotaiDepot.values()) {
            if(maotaiDepot.getIndex() == index){
                return maotaiDepot.getDepot();
            }
        }
        return MaotaiDepot.DEFAULT.getDepot();
    }

    public static Map<Integer, MaotaiDepot> toMap(){
        return Arrays.stream(values()).collect(Collectors.toMap(t ->{
            return t.index;
        }, Function.identity()));
    }
}

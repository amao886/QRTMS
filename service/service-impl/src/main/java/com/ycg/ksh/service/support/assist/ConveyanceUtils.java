package com.ycg.ksh.service.support.assist;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/14
 */

import com.ycg.ksh.entity.persistent.Conveyance;

/**
 * 运单工具类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/3/14
 */
public class ConveyanceUtils {

    public static boolean isRoot(Conveyance conveyance){
        return conveyance.getParentKey() == null || conveyance.getParentKey() <= 0;
    }
}

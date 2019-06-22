/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:08:14
 */
package com.ycg.ksh.adapter.api;

import com.ycg.ksh.entity.adapter.baidu.WordResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 百度相关接口
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:08:14
 */
public interface BaiduService {
    Logger logger = LoggerFactory.getLogger(BaiduService.class);
    
    /**
     * 通用文字识别（含位置信息版）
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:29:03
     * @param path
     * @return
     */
    public WordResult general(String path);
    
    /**
     * 通用文字识别（含位置信息版）
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:29:07
     * @param bytes
     * @return
     */
    public WordResult general(byte[] bytes);
    
    /**
     * 通用文字识别（含位置高精度版）
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:29:29
     * @param path
     * @return
     */
    public WordResult accurateGeneral(String path);
    
    /**
     * 通用文字识别（含位置高精度版）
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:29:34
     * @param bytes
     * @return
     */
    public WordResult accurateGeneral(byte[] bytes);
    
    /**
     * 通用文字识别（高精度版）
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:28:42
     * @param path
     * @return
     */
    public WordResult basicAccurateGeneral(String path);
    
    /**
     * 通用文字识别（高精度版）
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:28:46
     * @param bytes
     * @return
     */
    public WordResult basicAccurateGeneral(byte[] bytes);
}

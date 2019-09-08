package com.ycg.ksh.service.persistence;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.data.repository.query.Param;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.Barcode;
import com.ycg.ksh.entity.service.BarcodeSearch;
import com.ycg.ksh.entity.service.MergeBarcode;

import tk.mybatis.mapper.common.Mapper;

/**
 * 条码持久类
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:17:14
 */
public interface BarcodeMapper extends Mapper<Barcode> {

    /**
     * 根据条码号更新条码状态
     * <p>
     *
     * @param barcode 条码号
     * @param status  状态
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 13:20:29
     */
    void modifyStatus(String barcode, Integer status);

    /**
     * 根据条码号查询条码信息
     * <p>
     *
     * @param barcode 条码号
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 13:21:06
     */
    Barcode getByCode(String barcode);

    /**
     * 计算项目组条码使用情况
     * <p>
     *
     * @param groupId 项目组编号
     * @return 统计数据{number:未使用,useCount:已使用,totalNum:总数}
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 11:47:45
     */
    Map<String, Number> countByGroup(Integer groupId);

    /**
     * 计算个人条码使用情况
     * <p>
     *
     * @param groupId 项目组编号
     * @return 统计数据{number:未使用,useCount:已使用,totalNum:总数}
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 11:47:45
     */
    Map<String, Number> countByUser(Integer userId);

    /**
     * 查询指定日期最后的条码
     * <p>
     *
     * @param dateString 日期
     * @param length     长度
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-29 17:38:45
     */
    String selectLastCode(String dateString, Integer length);

    /**
     * 批量新增
     * <p>
     *
     * @param barcodes
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-29 17:24:28
     */
    void inserts(Collection<Barcode> barcodes);


    /**
     * 分页查询
     * <p>
     *
     * @param search
     * @param bounds
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-12 09:15:40
     */
    Page<Barcode> listBySomething(BarcodeSearch search, RowBounds bounds);

    /**
     * 查询条码列表
     *
     * @Author：wangke
     * @description：
     * @Date：12:18 2018/1/7
     */
    List<Map<String, Object>> barcodeListByResId(Integer resId);

    /**
     * 更新条码状态
     *
     * @param barcode
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-02 18:49:56
     */
    void updateStatusById(Barcode barcode);
    
    /**
     * 
     * TODO 根据条码号段、公司查询所属企业条码
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-08-07 18:20:20
     * @param barcode
     * @param bounds
     * @return
     */
    Page<MergeBarcode> queryBarcodeToCompany(BarcodeSearch barcode, RowBounds bounds);
    
    /**
     * 	查询当前项目组最后一个条码
     * @param gKey
     * @return
     */
    List<Barcode> queryOneBarcodeByGroupId(Integer gKey); 
}
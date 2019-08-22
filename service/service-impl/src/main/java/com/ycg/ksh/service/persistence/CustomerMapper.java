package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.Customer;
import com.ycg.ksh.entity.service.AddressSearch;
import org.apache.ibatis.session.RowBounds;

import java.util.Collection;

/**
 * 客户持久类
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:17:54
 */
public interface CustomerMapper extends CustomMapper<Customer> {


    /**
     * 查询常用地址,根据使用度排序
     * @param search
     * @param bounds
     * @return
     */
    Page<Customer> selectCustomer(AddressSearch search, RowBounds bounds);
    /**
     * 
     * 关联客户查询
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 15:45:29
     * @param search 查询条件
     * @return
     */
    Collection<Customer> queryCustomer(AddressSearch search);
    /**
     * 
     * 客户信息分页查询
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-26 14:19:57
     * @param search 查询条件
     * @param bounds 分页条件
     * @return
     */
    Page<Customer> queryCustomer(AddressSearch search, RowBounds bounds);
    
    /**
     * 分页查询企业客户地址信息
     *   
     * @param search
     * @param bounds
     * @return
     */
    Page<Customer> queryCompanyCustomer(AddressSearch search, RowBounds bounds);
    
    /**
     * 根据客户编号查询
     * @param customerCode
     * @return
     */
    Customer queryCustomerByCode(String customerCode);
}
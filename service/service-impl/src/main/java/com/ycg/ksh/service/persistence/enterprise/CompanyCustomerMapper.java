package com.ycg.ksh.service.persistence.enterprise;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.enterprise.CompanyCustomer;
import com.ycg.ksh.entity.service.AddressSearch;
import com.ycg.ksh.entity.service.enterprise.CustomerAddress;
import com.ycg.ksh.entity.service.enterprise.CustomerConcise;
import com.ycg.ksh.entity.service.enterprise.CustomerSearch;
import org.apache.ibatis.session.RowBounds;

import java.util.Collection;

public interface CompanyCustomerMapper extends CustomMapper<CompanyCustomer> {

    Collection<Long> listKeyByCompanyKey(Long companyKey, String likeName);

    Collection<Long> listKeyByName(Integer userKey, String likeName);

    /**
     * 获取客户信息
     * @param customerKey
     *
     * @return
     */
    CustomerConcise loadCustomerConcise(Long customerKey);

    /**
     * 查询企业客户信息
     *
     * @param search 查询条件
     * @return
     */
    Collection<CompanyCustomer> selectBySomething(CustomerSearch search);


    /**
     * 分页查询企业客户信息
     *
     * @param search    查询条件
     * @param rowBounds 分页信息
     * @return
     */
    Page<CompanyCustomer> selectBySomething(CustomerSearch search, RowBounds rowBounds);


    /**
     * 查询客户地址信息
     *
     * @param search
     * @return
     */
    Collection<CustomerAddress> selectCustomerAddressBySomething(AddressSearch search);

    /**
     * 分页查询客户地址信息
     *
     * @param search    查询条件
     * @param rowBounds 分页信息
     * @return
     */
    Page<CustomerAddress> selectCustomerAddressBySomething(AddressSearch search, RowBounds rowBounds);

    CompanyCustomer getByOwnerKeyAndName(Long ownerKey, String name);
}
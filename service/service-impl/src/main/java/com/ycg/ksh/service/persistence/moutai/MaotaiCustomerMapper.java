package com.ycg.ksh.service.persistence.moutai;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.moutai.Customer;
import com.ycg.ksh.entity.service.moutai.MoutaiCustomerSearch;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface MaotaiCustomerMapper extends Mapper<Customer> {

    Collection<String> listKeys();

    void inserts(Collection<Customer> collection);

    Page<Customer> queryCustomerList(MoutaiCustomerSearch search, RowBounds rowBounds);

    void deleteBatchBycustomerNos(@Param("customerNos") Collection<String> customerNos);
}
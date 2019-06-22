package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.Company;
import com.ycg.ksh.entity.service.CompanySearch;
import com.ycg.ksh.entity.service.enterprise.CompanyConcise;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface CompanyMapper extends Mapper<Company> {


    /**
     * 查询简要信息
     * @param key
     * @return
     */
    CompanyConcise getCompanyConciseByKey(Long key);

    /**
     * 根据公司名称模糊查询公司编号
     *
     * @param companyName
     * @return
     */
    Collection<Long> listCompanyKeyByName(String companyName);

    /**
     * 根据用户编号获取所属公司信息
     *
     * @param ukey
     * @return
     */
    Company getCompanyByUserKey(Integer ukey);


    /**
     * 根据公司名称获取公司信息
     *
     * @param companyName
     * @return
     */
    Company getCompanyByName(String companyName);
    /**
     * 企业查询分页
     * TODO Add description
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-05-02 17:27:32
     * @param companySearch
     * @param rowBounds
     * @return
     */
    Page<Company> queryPage(CompanySearch companySearch, RowBounds rowBounds);

    /**
     * 按照公司名称进行模糊查询
     * @param companyName
     * @return
     */
    Collection<Company> getCompanyLikeCompanyName(String companyName);
}
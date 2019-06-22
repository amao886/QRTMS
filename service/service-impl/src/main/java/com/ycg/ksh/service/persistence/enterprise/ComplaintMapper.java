package com.ycg.ksh.service.persistence.enterprise;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.extend.mybatis.CustomMapper;
import com.ycg.ksh.entity.persistent.enterprise.Complaint;
import com.ycg.ksh.entity.service.enterprise.ComplaintAlliance;
import com.ycg.ksh.entity.service.enterprise.ComplaintSearch;
import org.apache.ibatis.session.RowBounds;

public interface ComplaintMapper extends CustomMapper<Complaint> {

    /**
     * 发货方客户投诉记录
     *
     * @param search
     * @param rowBounds
     * @return
     */
    Page<ComplaintAlliance> complaintsShipper(ComplaintSearch search, RowBounds rowBounds);

    /**
     * 客户投诉记录
     *
     * @param search
     * @param rowBounds
     * @return
     */
    Page<ComplaintAlliance> complaintReceive(ComplaintSearch search, RowBounds rowBounds);

    /**
     * 物流商客户投诉记录
     *
     * @param search
     * @param rowBounds
     * @return
     */
    Page<ComplaintAlliance> complaintsConvey(ComplaintSearch search, RowBounds rowBounds);
}
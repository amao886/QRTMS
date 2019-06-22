package com.ycg.ksh.service.persistence;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.persistent.DriverContainer;
import com.ycg.ksh.entity.service.DriverContainerSearch;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface DriverContainerMapper extends Mapper<DriverContainer> {

	/**
	 * 根据条码查询装车但未卸车数
	 * @param barcode
	 * @return
	 */
	Integer countUnloadByCode(String barcode);

	/**
	 * 查询第一次装车信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-12 16:04:31
	 * @param barcode
	 * @return
	 */
	DriverContainer first(String barcode);
	/**
	 * 查询最后一次装车信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-12 16:04:31
	 * @param barcode
	 * @return
	 */
	DriverContainer last(String barcode);

    /**
     * 多条件查询装车信息，不分页
     * <p>
     *
     * @param serach
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 10:58:10
     */
    List<DriverContainer> listBySomething(DriverContainerSearch serach);

	Page<DriverContainer> listBySomething(DriverContainerSearch serach, RowBounds bounds);
    /**
     * 多条件查询装车信息，分页
     * <p>
     *
     * @param serach
     * @param bounds
     * @return
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 10:58:29
     */
    Page<DriverContainer> listByDriver(DriverContainerSearch serach, RowBounds bounds);
}
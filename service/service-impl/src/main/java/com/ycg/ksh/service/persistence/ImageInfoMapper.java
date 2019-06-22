package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.ImageInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 图片信息持久类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-17 09:22:46
 */
public interface ImageInfoMapper extends Mapper<ImageInfo> {
	
	
	/**
	 * 更新回单状态
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-30 15:59:17
	 * @param waybillId
	 * @param userId
	 * @param status
	 * @return
	 */
	int updateVerifyStatusByWaybillId(Integer waybillId, Integer userId, Integer status);
	/**
	 * 根据异常编号查询回单图片信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-30 11:19:17
	 * @param exceptionId
	 * @return
	 */
	List<ImageInfo> selectByExceptionId(Integer exceptionId);
	/**
	 * 根据回单编号查询回单图片信息
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-30 11:11:54
	 * @param receiptId
	 * @return
	 */
	List<ImageInfo> selectByReceiptId(Integer receiptId);
	/**
	 * 批量保存
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 11:42:20
	 * @param imageInfos 图片信息
	 */
	void inserts(Collection<ImageInfo> imageInfos);
}
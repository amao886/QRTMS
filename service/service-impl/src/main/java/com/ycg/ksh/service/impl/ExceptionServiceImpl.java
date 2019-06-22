/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 13:58:07
 */
package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.LocalCacheManager;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.adapter.api.WeChatApiService;
import com.ycg.ksh.entity.persistent.*;
import com.ycg.ksh.service.persistence.ImageInfoMapper;
import com.ycg.ksh.service.persistence.TransitionExceptionMapper;
import com.ycg.ksh.service.persistence.UserMapper;
import com.ycg.ksh.service.persistence.WaybillExceptionMapper;
import com.ycg.ksh.service.api.BarCodeService;
import com.ycg.ksh.service.api.ExceptionService;
import com.ycg.ksh.service.api.FriendsService;
import com.ycg.ksh.service.api.ImageStorageService;
import com.ycg.ksh.service.observer.WaybillObserverAdapter;
import com.ycg.ksh.service.support.assist.LocalCacheFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * 异常业务逻辑实现
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-23 13:58:07
 */
@Service("ksh.core.service.exceptionService")
public class ExceptionServiceImpl implements ExceptionService, WaybillObserverAdapter {

	@Resource
	protected WaybillExceptionMapper exceptionMapper;
	@Resource
	protected TransitionExceptionMapper transitionMapper;
	@Resource
	protected ImageInfoMapper imageInfoMapper;
	@Resource
	protected UserMapper userMapper;

	@Resource
	protected WeChatApiService apiService;
	@Resource
	protected ImageStorageService imageStorageService;
	@Resource
	protected BarCodeService barcodeService;
	@Resource
	protected FriendsService friendsService;

	@Override
	public void saveException(TransitionException exception, Collection<String> collection, boolean fromWx) throws ParameterException, BusinessException {
		Assert.notNull(exception, "异常信息不能为空");
		Assert.notBlank(exception.getBarcode(), "条码信息不能为空");
		Assert.notBlank(exception.getUserId(), "操作用户不能为空");
		Assert.notBlank(exception.getContent(), "异常内容不能为空");
		try {
			Barcode barcode = barcodeService.getBarcode(exception.getBarcode());
			if (barcode == null) {
				throw new ParameterException(exception.getBarcode(), "无效的条码号");
			}
			exception.setBarcode(barcode.getBarcode());
			exception.setId(Globallys.nextKey());
			exception.setReportTime(new Date());
			if (transitionMapper.insertSelective(exception) > 0) {
				if(CollectionUtils.isNotEmpty(collection)) {
                    if(fromWx){
                        collection = apiService.downImages(collection);
                    }
					if(CollectionUtils.isNotEmpty(collection)) {
						imageStorageService.save(Constant.IMAGE_TYPE_TRANSITION_EXCEPTION, exception.getId(), collection);
					}
				}
			}
		} catch (BusinessException | ParameterException e) {
			throw e;
		} catch (Exception e) {
			logger.error("saveException -> {} paths:{}", exception, collection, e);
			throw BusinessException.dbException("异常信息保存异常");
		}
	}

	@Override
	public void saveWaybillException(WaybillException exception, Collection<String> collection, boolean fromWx) throws ParameterException, BusinessException {
		Assert.notNull(exception, "异常信息不能为空");
		Assert.notNull(exception.getContent(), "异常描述不能为空");
		try {
			exception.setCreatetime(new Date());
			if(exceptionMapper.insertSelective(exception) > 0 && CollectionUtils.isNotEmpty(collection)) {
				if(fromWx){
					collection = apiService.downImages(collection);
				}
            	if(CollectionUtils.isNotEmpty(collection)) {
            		Collection<ImageInfo> imageInfos = new ArrayList<ImageInfo>(collection.size());
    				for (String path : collection) {
    					imageInfos.add(new ImageInfo(exception.getId(), path, exception.getCreatetime()));
    				}
    				imageInfoMapper.inserts(imageInfos);
            	}
			}
		} catch (Exception e) {
			throw BusinessException.dbException("运单异常信息保存异常");
		}
	}

	@Override
	public List<MergeWaybillException> listByWaybillId(Integer waybillId) throws ParameterException, BusinessException {
		Assert.notNull(waybillId, "运单编号不能为空");
		try {
			List<WaybillException> exceptions = exceptionMapper.selectByWaybillId(waybillId);
			if(CollectionUtils.isNotEmpty(exceptions)) {
				List<MergeWaybillException> mergeWaybillExceptions = new ArrayList<MergeWaybillException>(exceptions.size());
				LocalCacheManager<AssociateUser> cache = LocalCacheFactory.createUserCache(userMapper);
				for (WaybillException exception : exceptions) {
					MergeWaybillException merge = new MergeWaybillException(exception);
					merge.setImages(imageInfoMapper.selectByExceptionId(exception.getId()));
					merge.setUser(cache.get(exception.getUserid()));
					mergeWaybillExceptions.add(merge);
				}
				return mergeWaybillExceptions;
			}
		} catch (Exception e) {
			logger.error("查询运单异常信息异常, waybillId : {}", waybillId, e);
			throw BusinessException.dbException("查询运单异常信息异常");
		}
		return null;
	}

	private Collection<MergeTransitionException> listTransitionException(String barcode) throws ParameterException, BusinessException {
		if(StringUtils.isNotBlank(barcode)) {
			try {
				Example receiptExample = new Example(TransitionReceipt.class);
				Example.Criteria criteria = receiptExample.createCriteria();
				criteria.andEqualTo("barcode", barcode);
				receiptExample.orderBy("reportTime").asc();
				List<TransitionException> transitionExceptions = transitionMapper.selectByExample(receiptExample);
				if(CollectionUtils.isNotEmpty(transitionExceptions)) {
					Collection<MergeTransitionException> collection = new ArrayList<MergeTransitionException>(transitionExceptions.size());
					LocalCacheManager<AssociateUser> associateUserCache = LocalCacheFactory.createUserCache(userMapper);
					for (TransitionException transitionException : transitionExceptions) {
						MergeTransitionException mergeTransitionException = new MergeTransitionException(transitionException);
						mergeTransitionException.setReporter(associateUserCache.get(mergeTransitionException.getUserId()));
						mergeTransitionException.setImages(imageStorageService.list(Constant.IMAGE_TYPE_TRANSITION_EXCEPTION, mergeTransitionException.getId()));

						collection.add(mergeTransitionException);
					}
					return collection;
				}
			} catch (BusinessException | ParameterException e) {
				throw e;
			} catch (Exception e) {
				logger.error("listTransitionException -> barcode:{}", barcode, e);
				throw BusinessException.dbException("查询临时异常信息异常");
			}
		}
		return Collections.emptyList();
	}

    /**
     * @see WaybillObserverAdapter#mergeWaybill(MergeWaybill, WaybillAssociate)
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 16:59:47
     */
    @Override
    public void onMergeWaybill(MergeWaybill waybill, WaybillAssociate associate) throws BusinessException {
        if(associate.isAssociateException()) {
            waybill.setExceptions(listByWaybillId(waybill.getId()));
        }
    }

	/**
	 * 任务单绑定或更新完成事件
	 * <p>
	 *
	 * @param context 任务单上下文
	 * @param binding true:绑定，false:更新
	 * @throws BusinessException
	 */
	@Override
	public void onCompleteWaybill(WaybillContext context, boolean binding) throws BusinessException {
		try {
			if(binding && StringUtils.isNotBlank(context.getBarcode())) {
				Collection<MergeTransitionException> transitionExceptions = listTransitionException(context.getBarcode());
				if (CollectionUtils.isNotEmpty(transitionExceptions)) {
					for (MergeTransitionException transitionException : transitionExceptions) {
						WaybillException waybillException = new WaybillException();
						waybillException.setCreatetime(transitionException.getReportTime());
						waybillException.setUserid(transitionException.getUserId());
						if(transitionException.getReporter() != null) {
							waybillException.setUname(transitionException.getReporter().getEncryptName());
						}
						waybillException.setWaybillid(context.getId());
						if (exceptionMapper.insertSelective(waybillException) > 0 && CollectionUtils.isNotEmpty(transitionException.getImages())) {
							Collection<ImageInfo> imageInfos = new ArrayList<ImageInfo>(transitionException.getImages().size());
							for (ImageStorage image : transitionException.getImages()) {
								ImageInfo imageInfo = new ImageInfo();
								imageInfo.setCreatetime(new Date());
								imageInfo.setPath(image.getStoragePath());
								imageInfo.setReceiptid(waybillException.getId());
								imageInfos.add(imageInfo);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("临时异常复制异常 {}", context, e);
			throw new BusinessException("更新任务单处理异常信息时异常 ");
		}
	}

	/**
	 * 分页查询异常信息列表
	 *
	 * @param ukey
	 * @param search
	 * @param scope
	 * @return
	 */
	@Override
	public CustomPage<MergeExceptionRepor> pageConveyanceException(Integer ukey, ExceptionSearch search, PageScope scope) throws ParameterException, BusinessException {
		if (scope == null) {
			scope = PageScope.DEFAULT;
		}
		search.setSendKey(ukey);
		Page<MergeExceptionRepor> page = exceptionMapper.searchExceptionPage(search, new RowBounds(scope.getPageNum(), scope.getPageSize()));
		if (CollectionUtils.isNotEmpty(page)) {
			LocalCacheManager<AssociateUser> associateUserCache = LocalCacheFactory.createFriendUserCache(friendsService);
			for (MergeExceptionRepor mergeExceptionRepor : page) {
				AssociateUser associateUser = associateUserCache.get(ukey, mergeExceptionRepor.getOwnerKey());
				if(associateUser != null){
					mergeExceptionRepor.setAssignName(associateUser.getUnamezn());
				}
				mergeExceptionRepor.setImages(imageInfoMapper.selectByExceptionId(mergeExceptionRepor.getId()));
			}
		}
		return new CustomPage<MergeExceptionRepor>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
	}
}

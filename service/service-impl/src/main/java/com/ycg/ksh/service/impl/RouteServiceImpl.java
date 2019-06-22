package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.ycg.ksh.entity.common.constant.RouteLineType;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.LocalCacheManager;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.validate.Validator;
import com.ycg.ksh.entity.service.*;
import com.ycg.ksh.entity.persistent.Route;
import com.ycg.ksh.entity.persistent.RouteLine;
import com.ycg.ksh.service.persistence.RouteLineMapper;
import com.ycg.ksh.service.persistence.RouteMapper;
import com.ycg.ksh.service.api.FriendsService;
import com.ycg.ksh.service.api.RouteService;
import com.ycg.ksh.service.api.UserService;
import com.ycg.ksh.service.support.assist.LocalCacheFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service("ksh.core.service.routeService")
public class RouteServiceImpl implements RouteService {

	@Resource
	private RouteMapper routeMapper;
	@Resource
	private RouteLineMapper routeLineMapper;
	@Resource
	private UserService userService;
	@Resource
	private FriendsService friendsService;

	private void validate(Collection<MergeRouteLine> routeLines){
		Collection<Integer> collection = new ArrayList<Integer>(routeLines.size());
		int count = 0;
		for (MergeRouteLine routeLine : routeLines) {
			if(routeLine.getLineType() == 2){
				count = count + 1;
			}
			if(routeLine.getUserId() == null || routeLine.getUserId() <= 0){
				continue;
			}
			if(routeLine.getLineType() == 3){
				continue;
			}
			if (collection.contains(routeLine.getUserId())){
				throw new BusinessException("同一路由不能有两个相同的承运人");
			}
			collection.add(routeLine.getUserId());
		}
		if(count <= 0){
			throw new BusinessException("至少输入一个中转点");
		}
	}

	/**
	 * 新增路由
	 * @see com.ycg.ksh.service.api.RouteService#saveRoute(Route, java.util.Collection)
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-25 16:35:07
	 */
	@Override
	public void saveRoute(Route route, Collection<MergeRouteLine> routeLines) throws ParameterException, BusinessException {
		Assert.notNull(route, "路由信息不能为空");
		Assert.notBlank(route.getRouteName(), "路由名称不能为空");
		Assert.notEmpty(routeLines, "路由节点不能为空");
		Validator validator = Validator.NORMALWORD;
		if(!validator.verify(route.getRouteName())){
			throw new ParameterException(route.getRouteName(), validator.getMessage("路由名称"));
		}
		//validate(routeLines);
		try {
			validate(routeLines);
			route.setCreatetime(new Date());
			route.setUpdatetime(route.getCreatetime());
			if(routeMapper.insert(route)>0){
				RouteLine previous = null;
				for (MergeRouteLine routeLine : routeLines) {
					previous = saveOrModifyData(routeLine, route.getId(), previous);
				}
			}
		}catch(ParameterException | BusinessException e){
			throw e;
		} catch (Exception e) {
			logger.error("addRoute -> {} {}",route, routeLines, e);
			throw BusinessException.dbException("添加路由异常");
		}
	}
	/**
	 * 
	 * TODO Add 数据保存
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-28 13:48:01
	 * @param temRouteLine
	 * @param routeId
	 * @param routeLine
	 * @return
	 */
	private RouteLine saveOrModifyData(MergeRouteLine mergeLine, Integer routeId, RouteLine previous) {
		RouteLine newLine = new RouteLine();
		newLine.setId(mergeLine.getId());
		newLine.setUserId(mergeLine.getUserId());
		newLine.setRouteId(routeId);
		newLine.setLineType(mergeLine.getLineType());
		if(previous != null){
			newLine.setPid(previous.getId());
		}else{
			newLine.setPid(0);
		}
		splitAddress(mergeLine);
		newLine.setProvince(mergeLine.getProvince());
		newLine.setCity(mergeLine.getCity());
		newLine.setDistrict(mergeLine.getDistrict());
		newLine.setCreatetime(new Date());
		if(newLine.getId() == null || newLine.getId() <= 0){
			routeLineMapper.insertSelective(newLine);
		}else{
			routeLineMapper.updateByPrimaryKey(newLine);
		}
		return newLine;
	}
	/**
	 * 
	 * TODO 地址分割赋值
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-28 13:35:37
	 * @param routeLine
	 */
	private void splitAddress(MergeRouteLine routeLine) {
		if(StringUtils.isNotEmpty(routeLine.getAddress())){
			String [] arry = routeLine.getAddress().split("/");
			int length = arry.length;
			switch (length) {
			case 1:
				routeLine.setProvince(arry[0]);
				break;
			case 2:
				routeLine.setProvince(arry[0]);
				routeLine.setCity(arry[1]);
				break;
			case 3:
				routeLine.setProvince(arry[0]);
				routeLine.setCity(arry[1]);
				routeLine.setDistrict(arry[2]);
				break;
			default:
				break;
			}
		}
	}
	/**
	 * 修改路由
	 * @see com.ycg.ksh.service.api.RouteService#updateRoute(Route, java.util.Collection)
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-25 16:34:58
	 */
	@Override
	public void updateRoute(Route route, Collection<MergeRouteLine> routeLines) throws ParameterException, BusinessException {
		Assert.notNull(route, "路由信息不能为空");
		Assert.notBlank(route.getId(), "路由编号不能为空");
		Assert.notBlank(route.getRouteName(), "路由名称不能为空");
		Assert.notEmpty(routeLines, "路由节点不能为空");
		Validator validator = Validator.NORMALWORD;
		if(!validator.verify(route.getRouteName())){
			throw new ParameterException(route.getRouteName(), validator.getMessage("路由名称"));
		}
		validate(routeLines);
		try {
			route.setUpdatetime(new Date());
			routeMapper.updateByPrimaryKeySelective(route);
			Collection<Integer> updates = new ArrayList<Integer>();
			RouteLine previous = null;
			for (MergeRouteLine line : routeLines) {
				previous = saveOrModifyData(line, route.getId(), previous);
				if(previous != null){
					updates.add(previous.getId());
				}
			}
			Collection<RouteLine> existers = routeLineMapper.queryListByRouteId(route.getId());
			for (RouteLine exister : existers) {
				if(!updates.contains(exister.getId())){
					routeLineMapper.deleteByPrimaryKey(exister.getId());
				}
			}
		}catch (ParameterException | BusinessException e) {
			throw e;
		} catch (Exception e) {
			logger.error("updateRoute -> {} {}", route, routeLines, e);
			throw BusinessException.dbException("修改路由线路异常");
		}
	}
	/**
	 * 查询当前用户所有路由
	 * @see com.ycg.ksh.service.api.RouteService#queryRouteList(Route, PageScope)
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-25 16:34:37
	 */
	@Override
	public CustomPage<MergeRoute> queryRouteList(Route routeSearch,PageScope scope) throws ParameterException, BusinessException {
		try {
			Page<Route> page = routeMapper.queryPageRoute(routeSearch, new RowBounds(scope.getPageNum(), scope.getPageSize()));
			Collection<MergeRoute> list= new ArrayList<MergeRoute>();
			for (Route route : page) {
				MergeRoute mergeRoute = new MergeRoute(route);
				Collection<RouteLine> routeLines = routeLineMapper.queryListByRouteId(route.getId());
				if(CollectionUtils.isNotEmpty(routeLines)){
					LocalCacheManager<AssociateUser> associateUserCache = LocalCacheFactory.createFriendUserCache(friendsService);
					Collection<MergeRouteLine> mergeRouteLines = new ArrayList<MergeRouteLine>(routeLines.size());
					for (RouteLine routeLine : routeLines) {
						MergeRouteLine mergeRouteLine = new MergeRouteLine(routeLine);
						if(null != routeLine.getUserId()){
							AssociateUser user = associateUserCache.get(routeSearch.getUserId(), routeLine.getUserId());
							if(user != null){
								mergeRouteLine.setUserNick(user.getUnamezn());
								mergeRouteLine.setMobile(user.getMobilephone());
								mergeRouteLine.setUser(user);
							}
						}
						mergeRouteLines.add(mergeRouteLine);
					}
					mergeRoute.setRouteLines(mergeRouteLines);
				}
				list.add(mergeRoute);
			}
			return new CustomPage<MergeRoute>(page.getPageNum(), page.getPageSize(), page.getTotal(), list);
		} catch (Exception e) {
			logger.error("queryRouteList ",e);
			throw BusinessException.dbException("常用路由查询异常");
		}
	}
	/**
	 * 查询单条路由
	 * @see com.ycg.ksh.service.api.RouteService#getByKey(java.lang.Integer)
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-25 16:34:12
	 */
	@Override
	public MergeRoute getByKey(Integer uKey, Integer routeKey) throws ParameterException, BusinessException {
		try {
			Route route = routeMapper.selectByPrimaryKey(routeKey);
			Assert.notNull(route, "路由信息不存在");
			MergeRoute mergeRoute = new MergeRoute(route);
			Collection<RouteLine> routeLines = routeLineMapper.queryListByRouteId(route.getId());
			if(CollectionUtils.isNotEmpty(routeLines)){
				LocalCacheManager<AssociateUser> associateUserCache = LocalCacheFactory.createFriendUserCache(friendsService);
				Collection<MergeRouteLine> mergeRouteLines = new ArrayList<MergeRouteLine>(routeLines.size());
				for (RouteLine routeLine : routeLines) {
					MergeRouteLine mergeRouteLine = new MergeRouteLine(routeLine);
					if(null != routeLine.getUserId()){
						AssociateUser user = associateUserCache.get(uKey, routeLine.getUserId());
						if(user != null){
							mergeRouteLine.setUserNick(user.getUnamezn());
							mergeRouteLine.setMobile(user.getMobilephone());
							mergeRouteLine.setUser(user);
						}
					}
					mergeRouteLines.add(mergeRouteLine);
				}
				mergeRoute.setRouteLines(mergeRouteLines);
			}
			return mergeRoute;
		} catch (Exception e) {
			logger.error("getByKey routeKey:{}", routeKey, e);
			throw BusinessException.dbException("查询路由信息异常");
		}
	}

	/**
	 * 组装路由链
	 *
	 * @param routeKey
	 * @return 返回的是起点路由链
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	@Override
	public MergeRouteLink buildRouteLink(MergeRoute mergeRoute) throws ParameterException, BusinessException {
		//存储在map中,方便下面查找
		Map<Integer, MergeRouteLine> cache = new HashMap<Integer, MergeRouteLine>(mergeRoute.getRouteLines().size());
		MergeRouteLine lastRoute = null;//找到最后一个节点
		for (MergeRouteLine line : mergeRoute.getRouteLines()) {
			cache.put(line.getId(), line);
			RouteLineType lineType = RouteLineType.convert(line.getLineType());
			if(lineType.end()){
				lastRoute = line;
			}else if(line.getUserId() == null || line.getUserId() <= 0){
				throw new BusinessException("该路由有中转点没有选择承运人");
			}
		}
		MergeRouteLink current = null, prveitem = null;
		do {
			if(current == null){
				current = new MergeRouteLink(lastRoute);
			}else{
				prveitem = current;
				current = new MergeRouteLink(cache.get(prveitem.self().getPid()));
				prveitem.setPrevRoute(current);
				current.setNextRoute(prveitem);
			}
		} while (null != current.self().getPid() && current.self().getPid()  > 0);
		return current;
	}

	/**
	 * 查询活跃路由(经常使用的)
	 *
	 * @param uKey
	 * @return
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	@Override
	public Collection<Route> listActiveRoutes(Integer uKey, Integer count) throws ParameterException, BusinessException {
		Assert.notBlank(uKey, "用户ID不能为空");
		if(null != count && count > 0){
			Route search = new Route();
			search.setUserId(uKey);
			return routeMapper.queryPageRoute(search, new RowBounds(1, 5));
		}else{
			Example example = new Example(Route.class);
			example.createCriteria().andEqualTo("userId", uKey);
			return routeMapper.selectByExample(example);
		}
	}

	/**
	 * 更新节点承运人信息
	 *
	 * @param uKey     当前操作用户ID
	 * @param nodeKey  路由节点ID
	 * @param tuserKey 承运人用户ID
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	@Override
	public void updateTransporter(Integer uKey, Integer nodeKey, Integer tuserKey) throws ParameterException, BusinessException {
		Assert.notBlank(uKey, "当前操作用户不能为空");
		Assert.notBlank(nodeKey, "路由节点ID不能为空");
		Assert.notBlank(tuserKey, "承运人信息不能为空");
		try {
			RouteLine line = routeLineMapper.selectByPrimaryKey(nodeKey);
			if(null == line){
                throw new BusinessException("路由节点不存在");
            }
            if(validate(line.getRouteId(), tuserKey)){
				throw new BusinessException("同一路由不能有两个相同的承运人");
			}
			Route route = new Route();
			route.setUserId(uKey);
			route.setId(line.getRouteId());
			if(routeMapper.selectCount(route) <= 0){
                throw new BusinessException("没有权限更改该路由信息");
            }
			RouteLine updater = new RouteLine();
			updater.setId(line.getId());
			updater.setUserId(tuserKey);
			routeLineMapper.updateByPrimaryKeySelective(updater);
		}catch (BusinessException | ParameterException e) {
			throw e;
		} catch (Exception e) {
			logger.error("update transporter -> uKey:{} nodeKey:{} tuserKey:{}", uKey, nodeKey, tuserKey, e);
			throw BusinessException.dbException("更新节点承运人信息异常");
		}
	}
	/**
	 * 
	 * @see com.ycg.ksh.service.api.RouteService#deleteRoute(java.lang.Integer)
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-28 16:15:45
	 */
	@Override
	public void deleteRoute(Integer routeKey) throws ParameterException, BusinessException {
		try {
			Assert.notBlank(routeKey, "路由主键不能为空");
			RouteLine routeLine = new RouteLine();
			routeLine.setRouteId(routeKey);
			routeLineMapper.delete(routeLine);
			routeMapper.deleteByPrimaryKey(routeKey);
		} catch (Exception e) {
			logger.error("deleteRoute()====> routeKey:{}, e:{}",routeKey, e);
			throw BusinessException.dbException("删除路由异常");
		}
	}

	private boolean validate(Integer routeKey, Integer ukey){
		Example example = new Example(RouteLine.class);
		example.createCriteria().andEqualTo("routeId", routeKey).andEqualTo("userId", ukey);
		return routeLineMapper.selectCountByExample(example) > 0;
	}
}

package com.ycg.ksh.service.api;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.entity.persistent.Route;
import com.ycg.ksh.entity.service.MergeRoute;
import com.ycg.ksh.entity.service.MergeRouteLine;
import com.ycg.ksh.entity.service.MergeRouteLink;
import com.ycg.ksh.entity.service.PageScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public interface RouteService {

	final Logger logger = LoggerFactory.getLogger(RouteService.class);
	/**
	 * 
	 * TODO 路由添加
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-25 14:37:00
	 * @param route 路由实体
	 * @param routeLines 路由线路实体
	 * @param isNotDel 是否有删除节点
	 * @throws ParameterException 参数异常
	 * @throws BusinessException 参数异常
	 */
	void saveRoute(Route route,Collection<MergeRouteLine> routeLines) throws ParameterException, BusinessException;
	/**
	 * 
	 * TODO 路由修改
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-25 14:37:04
	 * @param route 路由实体
	 * @param routeLines 路由线路实体
	 * @throws ParameterException 参数异常
	 * @throws BusinessException 参数异常
	 */
	void updateRoute(Route route, Collection<MergeRouteLine> routeLines) throws ParameterException, BusinessException;
	/**
	 * 
	 * TODO 路由查询
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-25 14:37:08
	 * @param route 路由实体
	 * @param scope 分页实体
	 * @return
	 * @throws ParameterException 参数异常
	 * @throws BusinessException 参数异常
	 */
	CustomPage<MergeRoute> queryRouteList(Route route,PageScope scope) throws ParameterException, BusinessException;
	/**
	 * 根据路由ID查询路由信息
	 * @param uKey  路由ID
	 * @param routeKey  路由ID
	 * @return
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	MergeRoute getByKey(Integer uKey, Integer routeKey) throws ParameterException, BusinessException;

	/**
	 * 组装路由链
	 * @param mergeRoute
	 * @return 返回的是起点路由链
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	MergeRouteLink buildRouteLink(MergeRoute mergeRoute) throws ParameterException, BusinessException;

	/**
	 * 查询活跃路由(经常使用的)
	 * @param uKey
	 * @param count
	 * @return
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	Collection<Route> listActiveRoutes(Integer uKey, Integer count) throws ParameterException, BusinessException;

	/**
	 * 更新节点承运人信息
	 * @param uKey  当前操作用户ID
	 * @param nodeKey 路由节点ID
	 * @param tuserKey 承运人用户ID
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	void updateTransporter(Integer uKey, Integer nodeKey, Integer tuserKey) throws ParameterException, BusinessException;

	/**
	 * 
	 * TODO 根据路由主键删除路由
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-28 16:11:18
	 * @param routeKey
	 * @throws ParameterException
	 * @throws BusinessException
	 */
	void deleteRoute(Integer routeKey) throws ParameterException, BusinessException;
}

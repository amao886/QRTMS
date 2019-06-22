package com.ycg.ksh.api.backstage.controller;

import com.alibaba.fastjson.JSONArray;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.Route;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.MergeRoute;
import com.ycg.ksh.entity.service.MergeRouteLine;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.service.api.RouteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * 
 * TODO 路由控制类
 * <p>
 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-01-23 10:30:23
 */
@Controller("backstage.route.controller")
@RequestMapping("/backstage/route")
public class RouteController extends BaseController {
	
	@Resource
	RouteService routeService;

	@RequestMapping(value = "/select")
	@ResponseBody
	public JsonResult select(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
		JsonResult jsonResult = new JsonResult();
		User u = RequestUitl.getUserInfo(request);
        Route route = body.toJavaBean(Route.class);
        route.setUserId(u.getId());
        Integer pageNum = body.getInteger("num");
        Integer pageSize = body.getInteger("size");
        jsonResult.put("result", routeService.queryRouteList(route, new PageScope(pageNum, pageSize)));
		return jsonResult;
	}

	@RequestMapping(value = "/list")
	public String route(Model model, HttpServletRequest request) throws Exception {
		RequestObject params = new RequestObject(request.getParameterMap());
		User user = RequestUitl.getUserInfo(request);
		Route route = params.toJavaBean(Route.class);
		route.setUserId(user.getId());
		Integer pageNum = params.getInteger("pageNum");
		Integer pageSize = params.getInteger("pageSize");
		model.addAttribute("page", routeService.queryRouteList(route, new PageScope(pageNum, pageSize)));
		model.addAttribute("search", params);
		return "/backstage/route/route";
	}
	
	@RequestMapping(value = "/addView")
	public String addView(Model model, HttpServletRequest request) throws Exception {
		return "/backstage/route/addRoute";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult save(@RequestBody RequestObject params, Model model, HttpServletRequest request) throws Exception {
		Assert.notEmpty(params, Constant.PARAMS_ERROR);
		Assert.isTrue(params.containsKey("routeLines"), Constant.PARAMS_ERROR);
		User user = RequestUitl.getUserInfo(request);
		Route route = params.toJavaBean(Route.class);
		Collection<MergeRouteLine> routeLines = JSONArray.parseArray(params.get("routeLines"), MergeRouteLine.class);
		route.setUserId(user.getId());
		routeService.saveRoute(route, routeLines);
		return JsonResult.SUCCESS;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult update(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
		logger.info("update -> {}", object);
		Assert.notNull(object, Constant.PARAMS_ERROR);
		Route route = new Route();
		route.setId(object.getInteger("id"));
		Assert.notBlank(route.getId(), "路由编号不能为空");
		route.setRouteName(StringUtils.trimToEmpty(object.get("routeName")));
		Assert.notBlank(route.getRouteName(), "路由名称不能为空");
		String lineString = StringUtils.trimToEmpty(object.get("lines"));
		Assert.notBlank(lineString, "路由节点不能为空");
		Collection<MergeRouteLine> lines = JSONArray.parseArray(lineString, MergeRouteLine.class);
		System.out.println(lines);
		routeService.updateRoute(route, lines);
		return JsonResult.SUCCESS;
	}
	
	@RequestMapping(value = "/query/{routeKey}")
	@ResponseBody
	public JsonResult query(@PathVariable Integer routeKey,Model model, HttpServletRequest request) throws Exception {
		JsonResult jsonResult = new JsonResult();
		User user = RequestUitl.getUserInfo(request);
		MergeRoute mergeRoute = routeService.getByKey(user.getId(),routeKey);
		jsonResult.put("mergeRoute", mergeRoute);
		return jsonResult;
	}


	@RequestMapping(value = "/update/user", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult updateUser(@RequestBody RequestObject body, Model model, HttpServletRequest request) throws Exception {
		Assert.notEmpty(body, Constant.PARAMS_ERROR);
		Integer nodeKey = body.getInteger("nodeKey");
		Assert.notBlank(nodeKey, "路由节点ID不能为空");
		Integer userKey = body.getInteger("userKey");
		Assert.notBlank(userKey, "请选择一个承运人");
		User user = RequestUitl.getUserInfo(request);
		routeService.updateTransporter(user.getId(), nodeKey, userKey);
		return JsonResult.SUCCESS;
	}

	
	@RequestMapping(value = "/editView")
	public String editView(Model model, HttpServletRequest request) throws Exception {
		String routeKey = request.getParameter("id");
		Assert.notBlank(routeKey, Constant.PARAMS_ERROR);
		User user = RequestUitl.getUserInfo(request);
		MergeRoute mergeRoute = routeService.getByKey(user.getId(),Integer.valueOf(routeKey));
		model.addAttribute("mergeRoute", mergeRoute);
		if(logger.isDebugEnabled()){
			logger.debug("edit mergeRoute:{}",mergeRoute);
		}
		return "/backstage/route/editRoute";
	}
	
	@RequestMapping(value = "/delete/{routeKey}")
	@ResponseBody
	public JsonResult delete(@PathVariable Integer routeKey, Model model, HttpServletRequest request) throws Exception {
		routeService.deleteRoute(routeKey);
		return JsonResult.SUCCESS;
	}

}

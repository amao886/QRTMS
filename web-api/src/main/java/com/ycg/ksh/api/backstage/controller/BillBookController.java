package com.ycg.ksh.api.backstage.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.system.InterfaceHepler;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.ProjectGroup;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.persistent.Waybill;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.WaybillContext;
import com.ycg.ksh.entity.service.WaybillSerach;
import com.ycg.ksh.service.api.ProjectGroupService;
import com.ycg.ksh.service.api.WaybillService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 账单
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-17 14:32:04
 */
@Controller("backstage.barcode.controller")
@RequestMapping("/backstage/billbook")
public class BillBookController extends BaseController {

	@Resource
	private WaybillService waybillService;
	@Resource
	private ProjectGroupService projectGroupService;
	
	//查询账单
    @RequestMapping(value = "/search")
    public String billBookSearch(Model model, HttpServletRequest request) throws Exception {
    	RequestObject body = new RequestObject(request.getParameterMap());
    	logger.info("-----------billBookSearch------------{}", body);
		User u = RequestUitl.getUserInfo(request);
		List<ProjectGroup> groupList = projectGroupService.listByUserKey(u.getId());
		if (CollectionUtils.isNotEmpty(groupList)) {
			model.addAttribute("groups", groupList);
			Integer groupId = body.getInteger("groupid");
			
			if(groupId == null || groupId <= 0) {
				groupId = RequestUitl.getGroupKey(request, groupList.get(0).getId());
				body.put("groupid", groupId);
			}else {
				RequestUitl.modifyGroupKey(request, groupId);
			}
			
			WaybillSerach serach = new WaybillSerach();
			serach.setGroupId(body.getInteger("groupid"));
			serach.setUserId(u.getId());
			serach.setLikeString(body.get("likeString"));
			serach.setWaybillFettles(StringUtils.integerArray(body.get("waybillFettles")));
			serach.setReceiptFettles(StringUtils.integerArray(body.get("receiptFettles")));
			Boolean delay = body.getBoolean("delay");
			if(delay != null){
				serach.setDelay(delay ? 1 : 2);
			}else{
				serach.setDelay(0);
			}
			serach.setTime(body.getDate("bindStartTime"), body.getDate("bindEndTime"), -30);
		
			body.put("bindStartTime", DateUtils.formatDate(serach.getFirstTime(), "yyyy-MM-dd"));
			body.put("bindEndTime", DateUtils.formatDate(serach.getSecondTime(), "yyyy-MM-dd"));
			
			Integer pageSize = body.getInteger("size");
			Integer pageNum = body.getInteger("num");

			WaybillContext context = WaybillContext.buildContext(u, serach, new PageScope(pageNum, pageSize));
			CustomPage<Waybill> pages = waybillService.pageWaybill(context);
			if(CollectionUtils.isNotEmpty(pages.getCollection())) {
				List<Integer> waybillIds = new ArrayList<Integer>();
				for (Waybill waybill : pages.getCollection()) {
					waybillIds.add(waybill.getId());
				}
				model.addAttribute("accounts", queryAccountInfos(u.getId(), waybillIds));
			}
			model.addAttribute("page", pages);
		}
		model.addAttribute("search", body);
		return "/backstage/billbook/manage";
	}
    
    private Map<Integer, JSONObject> queryAccountInfos(Integer userId, List<Integer> waybills){
    	if(CollectionUtils.isNotEmpty(waybills)) {
			JSONObject object = new JSONObject();
			object.put("userId", userId);
			object.put("waybills", waybills);
			String accountJson = object.toJSONString();
			logger.info("传输数据:" + accountJson);
			JSONArray accounts = InterfaceHepler.requestArray(accountJson, InterfaceHepler.URL_QUERY_ACCOUNTS_WITH_WAYBILLS);
			if(CollectionUtils.isNotEmpty(accounts)) {
				Map<Integer, JSONObject> maps = new HashMap<Integer, JSONObject>(accounts.size());
				for (int i = 0; i < accounts.size(); i++) {
					JSONObject account = accounts.getJSONObject(i);
					maps.put(account.getInteger("waybillid"), account);
				}
				return maps;
			}
		}
    	return Collections.emptyMap();
    }
    //新增记账
	/*
    @RequestMapping(value = "/save")
    @ResponseBody
    public JsonResult saveBill(@RequestBody AccountInfo bill, HttpServletRequest request) throws Exception {
    	logger.info("-----------------saveBill----------------->{}", bill);
    	User u = RequestUitl.getUserInfo(request);
    	Assert.notNull(bill, "客户信息不能为空");
    	if(bill.getExpenditure() != null) {
    		Assert.notBetween(bill.getExpenditure(), 0, 999999.99);
    	}
    	if(bill.getIncome() != null) {
    		Assert.notBetween(bill.getIncome(), 0, 999999.99);
    	}
    	
    	bill.setUserid(u .getId());
		String billJson = JSONObject.toJSONString(bill);
		logger.info("---传输数据：---" + billJson);
		InterfaceHepler.requestObject(billJson, InterfaceHepler.URL_ADD_ACCOUNT);
        return JsonResult.SUCCESS;
    }
    */
}

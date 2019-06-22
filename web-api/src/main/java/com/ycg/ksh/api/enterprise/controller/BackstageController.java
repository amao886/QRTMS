package com.ycg.ksh.api.enterprise.controller;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/13
 */

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.entity.persistent.Company;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.CompanySearch;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.OrderSearch;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.MessageService;
import com.ycg.ksh.service.api.OrderService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

/**
 * 后台相关控制器
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/13
 */
@Controller("enterprise.backstage.controller")
@RequestMapping("/enterprise/backstage")
public class BackstageController extends BaseController {

    @Resource
    protected CompanyService companyService;
    @Resource
    protected MessageService messageService;
    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/view/{viewkey}")
    public String shipperManage(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        if("signandmanage".equals(viewkey)){
            searchCompanySigned(request,model,new PageScope());
        }
    	return "/enterprise/backstage/"+ viewkey;
    }

    @RequestMapping(value = "/search")
    @ResponseBody
    public JsonResult search(@RequestBody(required=false) RequestObject params, Model model, HttpServletRequest request) throws Exception{
        if(params==null){
            params = new RequestObject();
        }
        JsonResult jsonResult = new JsonResult();
        User user = loadUser(request);
        CompanySearch companySearch = params.toJavaBean(CompanySearch.class);
        companySearch.setUserId(user.getId());
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");
        CustomPage<Company> page = companyService.searchPage(companySearch, new PageScope(pageNum, pageSize));
        jsonResult.put("search", companySearch);
        jsonResult.put("results", page);
        return jsonResult;
    }

    @RequestMapping(value = "/manage/shipper")
    @ResponseBody
    public JsonResult manageShipper(@RequestBody(required=false) RequestObject params, HttpServletRequest request) throws Exception{
        if(params==null){
            params = new RequestObject();
        }
        JsonResult jsonResult = new JsonResult();

        jsonResult.put("results", Collections.emptyList());
        return jsonResult;
    }

    /**
     * @Description: 企业签署 - 查询签署的查询
     * @author wyj
     * @date 2018/5/4 13:34
     */
    @RequestMapping("/companySigned/manage/search")
    public String searchCompanySigned(HttpServletRequest request, Model model, PageScope pageScope){
        pageScope.validate(10,1);
        String companyName= request.getParameter("companyName");
        if(companyName == null){
            companyName = "";
        }else{
            try {
                companyName = new String (companyName.getBytes("iso8859-1"),"utf-8").trim();
            } catch (UnsupportedEncodingException e) { }
        }
        CustomPage customPage = companyService.searchCompanySigned(companyName, pageScope);
        model.addAttribute("page", customPage);
        model.addAttribute("companyName", companyName);
        return "/enterprise/backstage/signandmanage";
    }

    /**
     * @Description: 赠送签署次数
     * @author wyj
     * @date 2018/5/4 16:09
     */
    @ResponseBody
    @RequestMapping("/companySigned/presentNum")
    public JsonResult presentSignedNum(HttpServletRequest request){
        RequestObject object = new RequestObject(request.getParameterMap());
        Long id = object.getLong("id");
        Integer presentNum = object.getInteger("presentNum");
        try{
            companyService.presentSignedNum(id,presentNum);
            return  JsonResult.SUCCESS;
        }catch (ParameterException e){
            return new JsonResult(false,e.getFriendlyMessage());
        }
    }
    /**
     * 
     * TODO 企业认证
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-05-07 14:18:03
     * @param request
     * @return
     */
    @RequestMapping(value="/authentication")
    @ResponseBody
    public JsonResult authentication(@RequestBody RequestObject params, HttpServletRequest request) throws Exception{
    	Assert.notEmpty(params, Constant.PARAMS_ERROR);
    	if (logger.isDebugEnabled()) {
			logger.info("----authentication--->params{}:", params);
		}
    	Company company = params.toJavaBean(Company.class);
        User user = loadUser(request);
    	companyService.updateCompany(user.getId(), company);
    	return JsonResult.SUCCESS;
    }
    
    /**
     * 
     * TODO 分配电子签收认证权限
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-05-08 11:38:32
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update/electronic/receipt")
    @ResponseBody
    public JsonResult updateElectronicReceipt(HttpServletRequest request) throws Exception{
    	RequestObject params = new RequestObject(request.getParameterMap());
    	Assert.notEmpty(params, Constant.PARAMS_ERROR);
    	if (logger.isDebugEnabled()) {
			logger.info("----updateElectronicReceipt--->params{}:", params);
		}
    	Company company = params.toJavaBean(Company.class);
    	company.setSignFettle(1);//分配电子签收认证权限
    	//companyService.updateCompany(company);
    	return JsonResult.SUCCESS;
    }
    
    /**
     * 
     * TODO 运营后台发货管理
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-07-17 14:15:07
     * @param body
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delivery/search")
    @ResponseBody
    public JsonResult searchDeliveryManage(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
    	OrderSearch search = null;
    	if(body == null){
    		search = new OrderSearch();
    	}else{
    		search = body.toJavaBean(OrderSearch.class);
    	}
    	search.setAll(true);
        search.setTime(body.getInteger("timeType"));
        Integer fettle = body.getInteger("fettle");
        Integer receiptFettle = body.getInteger("receiptFettle");
        if (null != fettle) {
            search.setFettles(convertOrderFettles(fettle));
        }
        if ( 0 == receiptFettle) {
        	search.setUploadReceipt(false);
        }else if( 1 == receiptFettle){
        	search.setUploadReceipt(true);
        }
        PageScope scope = new PageScope(body.getInteger("num"), body.getInteger("size"));
        logger.info("shipper manage -> {}", body);
        JsonResult result = new JsonResult();
        result.put("page", orderService.pageOrder(loadUserKey(request), search, scope));
        result.put("search", search);
        String companyName = body.get("companyName");
        result.put("companys", companyService.listCompanyByName(companyName));
        return result;
    }
}

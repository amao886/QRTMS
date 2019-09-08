/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-03 15:25:38
 */
package com.ycg.ksh.api.admin.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.ApplyRes;
import com.ycg.ksh.entity.persistent.Company;
import com.ycg.ksh.entity.persistent.ProjectGroup;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.BarCodeDescription;
import com.ycg.ksh.entity.service.BarcodeSearch;
import com.ycg.ksh.entity.service.MergeApplyRes;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.service.api.BarCodeService;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.ProjectGroupService;
import com.ycg.ksh.service.api.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.List;

/**
 * 条码管理控制层
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-03 15:25:38
 */
@Controller("admin.barcode.controller")
@RequestMapping("/admin/barcode")
public class BarCodeController extends BaseController {

    @Resource
    BarCodeService barCodeService;

    @Resource
    ProjectGroupService projectGroupService;

    @Resource
    UserService userService;

    @Resource
    CompanyService companyService;

    /**
     * 条码管理列表页跳转 --旧版本,有用户使用(保留) 项目组、个人相关
     *
     * @Author：wangke
     * @description：
     * @Date：13:35 2018/1/4
     */
    @RequestMapping(value = "/search/old")
    public String search(Model model, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        Integer pageSize = body.getInteger("size");
        Integer pageNum = body.getInteger("num");
        model.addAttribute("search", body);
        model.addAttribute("userId",RequestUitl.getUserInfo(request).getId());
        model.addAttribute("results", barCodeService.pageApplyResList(new MergeApplyRes(), new PageScope(pageNum, pageSize) ,1)); //旧版,无公司id
        return "/admin/barcode/manage_old";
    }

    /**
     * 查询所有分配给企业的条码记录
     *
     * @Author：wyj
     * @description：
     * @Date: 2018-05-24 11:29:08
     */
    @RequestMapping(value = "/search") 
    public String searchv2(Model model, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        Integer pageSize = body.getInteger("size");
        Integer pageNum = body.getInteger("num");
        String companyName = body.get("companyName");
        Collection<Company> companyList = companyService.listCompanyByName(companyName);
        MergeApplyRes mergeApplyRes = body.toJavaBean(MergeApplyRes.class);
        model.addAttribute("companys", companyList);
        model.addAttribute("userId",RequestUitl.getUserInfo(request).getId());
        model.addAttribute("search", body);
        model.addAttribute("results", barCodeService.pageApplyResList(mergeApplyRes, new PageScope(pageNum, pageSize) ,2)); //新版.有公司id
        return "/admin/barcode/manage_new";
    }
    
    /**
     * 查询所有申请记录 V3.2.4
     * TODO Add description
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-08-02 13:56:13
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/all/search")
    public String searchAll(Model model, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        Integer pageSize = body.getInteger("size");
        Integer pageNum = body.getInteger("num");
        model.addAttribute("userId",RequestUitl.getUserInfo(request).getId());
        model.addAttribute("results", barCodeService.pageApplyResList(new MergeApplyRes(), new PageScope(pageNum, pageSize) ,1)); //旧版,无公司id
        return "/admin/barcode/manage_old";
    	/*RequestObject body = new RequestObject(request.getParameterMap());
    	Integer pageSize = body.getInteger("size");
    	Integer pageNum = body.getInteger("num");
    	String unamezn = body.get("uname");
        String mobilephone = body.get("mobilephone");
    	model.addAttribute("search",body);
    	model.addAttribute("userId",RequestUitl.getUserInfo(request).getId());
    	model.addAttribute("results", barCodeService.pageAllApplyRes(unamezn, mobilephone,new PageScope(pageNum, pageSize))); //新版.有公司id
    	return "/admin/barcode/manage";*/
    }
    /**
     * 
     * TODO Add description
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-08-09 11:23:13
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/allocated/search")
    public String searchCom(Model model, HttpServletRequest request) throws Exception {
    	RequestObject body = new RequestObject(request.getParameterMap());
    	Integer pageSize = body.getInteger("size");
    	Integer pageNum = body.getInteger("num");
    	model.addAttribute("search",body);
    	String companyName = body.get("companyName");
        Collection<Company> companyList = companyService.listCompanyByName(companyName);
        model.addAttribute("companys", companyList);
        model.addAttribute("results", barCodeService.pageBarcodeToCompany(body.toJavaBean(BarcodeSearch.class), new PageScope(pageNum, pageSize))); //新版.有公司id
    	return "/admin/barcode/manage_company";
    }

    @ResponseBody
    @RequestMapping(value = "/companyList")
    public JsonResult searchCompanyName(){
        JsonResult result = JsonResult.SUCCESS;
        result.put("companyList", companyService.listCompany());
        return result;
    }

    /**
     * 根据手机号查询用户所在组
     *
     * @Author：wangke
     * @description：
     * @Date：17:12 2018/1/4
     */
    @RequestMapping(value = "ajaxGroupList/{mobilephone}")
    @ResponseBody
    public JsonResult queryGroupList(@PathVariable String mobilephone) {
        logger.info("--------------ajaxGroupList----------------", mobilephone);
        Assert.notNull(mobilephone, Constant.PARAMS_ERROR);
        JsonResult jsonResult = new JsonResult();
        User u = userService.getUserByMobile(mobilephone);
        List<ProjectGroup> groupList = projectGroupService.listByUserKey(u.getId());
        jsonResult.put("group", groupList);
        return jsonResult;
    }


    /**
     * 申请二维码
     *
     * @Author：wangke
     * @description：
     * @Date：17:25 2018/1/4
     */
    @RequestMapping(value = "saveApplyRes")
    @ResponseBody
    public JsonResult saveApplyRes(@RequestBody RequestObject object) throws ReflectiveOperationException {
        logger.info("--------------saveApplyRes--------------", object);
        Assert.notNull(object, Constant.PARAMS_ERROR);
        User u = userService.getUserByMobile(object.get("mobilephone"));
        barCodeService.applyRes(u.getId(), object.toJavaBean(ApplyRes.class));
        return JsonResult.SUCCESS;
    }
    /**
     * 申请二维码
     *
     * @Author：wangke
     * @description：
     * @Date：17:25 2018/1/4
     */
    @RequestMapping(value = "saveApplyResv2")
    @ResponseBody
    public JsonResult saveApplyResv2(@RequestBody RequestObject object,HttpServletRequest request) throws ReflectiveOperationException {
        logger.info("--------------saveApplyRes--------------", object);
        Assert.notNull(object, Constant.PARAMS_ERROR);
        barCodeService.applyRes(Integer.parseInt(RequestUitl.getUserID(request)), object.toJavaBean(ApplyRes.class));
        return JsonResult.SUCCESS;
    }



    /**
     * 条码变更-项目组版
     *
     * @Author：wangke
     * @description：
     * @Date：15:44 2018/1/6
     */
    @RequestMapping(value = "changeBarcodeOld")
    @ResponseBody
    public JsonResult changeBarcodeOld(@RequestBody RequestObject object, HttpServletRequest request) throws ReflectiveOperationException {
        barCodeService.updateBarCodeChangeOld(object.toJavaBean(MergeApplyRes.class));
        return JsonResult.SUCCESS;
    }
    /**
     * 条码变更-企业版
     *
     * @Author：wangke
     * @description：
     * @Date：15:44 2018/1/6
     */
    @RequestMapping(value = "changeBarcode")
    @ResponseBody
    public JsonResult changeBarcode(@RequestBody RequestObject object, HttpServletRequest request) throws ReflectiveOperationException {
    	Assert.notNull(object, Constant.PARAMS_ERROR);
    	ApplyRes applyRes = object.toJavaBean(ApplyRes.class);
    	applyRes.setUserid(loadUserKey(request));//操作人
    	barCodeService.updateBarCodeChange(applyRes);
        return JsonResult.SUCCESS;
    }

    /**
     * 根据用户查询用户信息，联想
     *
     * @Author：wangke
     * @description：
     * @Date：10:43 2018/1/5
     */
    @RequestMapping(value = "queryUserByMobile")
    @ResponseBody
    public JsonResult queryUserByMobile(@RequestBody RequestObject object) {
        logger.info("----------------queryUserByMobile-------------", object);
        List<User> users = userService.getUserListByMobile(object.get("mobilephone"));
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("users", users);
        return jsonResult;
    }


    /**
     * 根据用户ID 查询所在组信息
     *
     * @Author：wangke
     * @description：
     * @Date：10:30 2018/1/6
     */
    @RequestMapping(value = "queryGroupByUserId/{userid}")
    @ResponseBody
    public JsonResult queryGroupByUserId(@PathVariable Integer userid) {
        Assert.notBlank(userid, Constant.PARAMS_ERROR);
        List<ProjectGroup> lists = projectGroupService.listByUserKey(userid);
        JsonResult jsonResult = new JsonResult();
        if (CollectionUtils.isEmpty(lists)) {
            jsonResult.modify(false, "当前用户无关联组，请分配个人用户");
            return jsonResult;
        } else {
            jsonResult.put("groups", lists);
            return jsonResult;
        }
    }

    /***
     * 条码下载
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "download/{resid}")
    @ResponseBody
    public JsonResult preserve(@PathVariable Integer resid, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User u = RequestUitl.getUserInfo(request);
    	FileEntity fileEntity = barCodeService.buildPDF(u.getId(), resid);
    	JsonResult jsonResult = new JsonResult();
        if (null != fileEntity && StringUtils.isNotBlank(fileEntity.getPath())) {
            jsonResult.put("file", fileEntity.getPath());
            jsonResult.put("count", fileEntity.getCount());
            jsonResult.put("size", fileEntity.getSize());
            jsonResult.put("fileName", fileEntity.getFileName());
            jsonResult.put("url", FileUtils.buildDownload(fileEntity.getPath(), FileUtils.appendSuffix("二维码PDF文件", fileEntity.getSuffix()), true));
        } else {
            jsonResult.modify(false, "文件下载异常");
        }
        return jsonResult;
    }
    
    @RequestMapping(value = "build/ready")
    @ResponseBody
    public JsonResult buildReady(@RequestBody RequestObject requestObject, HttpServletRequest request) {
        Integer applyId = requestObject.getInteger("appresid");
        Assert.notBlank(applyId, "申请编号不能为空");
        
        User user = RequestUitl.getUserInfo(request);
        
        barCodeService.saveBuildReady(user.getId(), applyId);
        
    	return JsonResult.SUCCESS;
    }

    /**
     *  查询二维码的详细信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/description/{barcode}")
    @ResponseBody
    public JsonResult BarcodeDescription(@PathVariable String barcode, HttpServletRequest request,HttpServletResponse response){
        BarCodeDescription description = barCodeService.getBarcodeDescription(barcode);
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("description", description);
        logger.debug(jsonResult.toString());
        return  jsonResult;
    }


}

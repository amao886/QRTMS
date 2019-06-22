package com.ycg.ksh.api.enterprise.controller;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/12
 */

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.extend.FileUploadHelper;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.service.util.O;
import com.ycg.ksh.service.util.P;
import com.ycg.ksh.service.util.Transform;
import com.ycg.ksh.entity.persistent.OrderCommodity;
import com.ycg.ksh.entity.persistent.enterprise.CustomData;
import com.ycg.ksh.entity.persistent.enterprise.OrderExtra;
import com.ycg.ksh.entity.persistent.plan.PlanCommodity;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.enterprise.CustomerSearch;
import com.ycg.ksh.entity.service.enterprise.OrderTemplate;
import com.ycg.ksh.entity.service.plan.PlanAlliance;
import com.ycg.ksh.entity.service.plan.PlanSearch;
import com.ycg.ksh.entity.service.plan.PlanTemplate;
import com.ycg.ksh.service.api.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 发货计划相关控制层
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/9/12
 */
@Controller("enterprise.plan.controller")
@RequestMapping("/enterprise/plan")
public class PlanController extends BaseController {

    @Resource
    FileService fileService;
    @Resource
    PlanOrderService planOrderService;
    @Resource
    CompanyService companyService;
    @Resource
    CustomerService customerService;
    @Resource
    TemplateService templateService;

    /**
     * 计划管理入口
     *
     * @return
     */
    @RequestMapping(value = "/manage/{viewkey}")
    public String manageView(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        return "/enterprise/plan/" + viewkey + "/manage";
    }

    @RequestMapping(value = "/operate/{viewkey}")
    public String operateView(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        return "/enterprise/plan/" + viewkey;
    }


    /**
     * 发货计划导入
     *
     * @param file
     * @param uKey
     * @param templateKey
     * @param partnerType
     * @return
     * @throws IOException
     */
    private JsonResult importByFile(File file, Integer uKey, Long templateKey, PartnerType partnerType) throws IOException {
        Assert.notNull(file, "请上传一个文件!!!");
        Assert.notBlank(templateKey, "请选择一个导入模板");
        FileEntity fileEntity = new FileEntity(file);
        String path = fileEntity.getPath();
        try {
            logger.info("开始处理文件 -> {}", fileEntity);
            fileEntity = fileService.saveByTemplateFile(uKey, partnerType, templateKey, fileEntity);
            logger.info("文件处理完成 -> {}", fileEntity);
        } finally {
            FileUtils.forceDelete(new File(path));
        }
        JsonResult jsonResult = new JsonResult();
        if (fileEntity != null && fileEntity.getFailureCount() != null && fileEntity.getFailureCount() > 0) {
            jsonResult.modify(false, "存在异常数据</br>成功导入" + fileEntity.getSuccessCount() + "条,失败" + fileEntity.getFailureCount() + "条</br>请下载处理后上传导入");
            fileEntity.setUrl(FileUtils.buildDownload(fileEntity.getPath(), FileUtils.appendSuffix(fileEntity.getAliasName(), fileEntity.getSuffix())));
            jsonResult.put("result", fileEntity);
        } else {
            jsonResult.message("操作成功</br>成功导入" + fileEntity.getSuccessCount() + "条");
        }
        return jsonResult;
    }

    @RequestMapping(value = "/shipper/import/excel", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult shipperImportExcel(HttpServletRequest request) throws Exception {
        File file = FileUploadHelper.saveExcel(request, SystemUtils.directoryTemp());
        RequestObject requestObject = new RequestObject(request.getParameterMap());
        return importByFile(file, loadUserKey(request), requestObject.getLong("templateKey"), PartnerType.SHIPPER);
    }

    @RequestMapping(value = "/coveyer/import/excel", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult coveyerImportExcel(HttpServletRequest request) throws Exception {
        File file = FileUploadHelper.saveExcel(request, SystemUtils.directoryTemp());
        RequestObject requestObject = new RequestObject(request.getParameterMap());
        return importByFile(file, loadUserKey(request), requestObject.getLong("templateKey"), PartnerType.CONVEY);
    }

    /**
     * 保存订单(非模板)
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/shipper/generate/order", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult shipperSaveOrder(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        logger.info("generate order -> {}", requestObject);
        buildOrder(PartnerType.SHIPPER, loadUserKey(request), requestObject, true);
        return JsonResult.SUCCESS;
    }

    @RequestMapping(value = "/coveyer/generate/order", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult coveyerSaveOrder(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        logger.info("generate order -> {}", requestObject);
        buildOrder(PartnerType.CONVEY, loadUserKey(request), requestObject, true);
        return JsonResult.SUCCESS;
    }

    private void buildOrder(PartnerType partnerType, Integer userKey, RequestObject requestObject, boolean insert) {
        Long planKey = requestObject.getLong("planKey");
        Assert.notBlank(planKey, "发货计划不能为空");
        String orderString = requestObject.get("order");
        Assert.notBlank(orderString, "订单信息不能为空");
        OrderTemplate orderTemplate = Globallys.toJavaObject(orderString, OrderTemplate.class);
        String commodityString = requestObject.get("commodities");
        Assert.notBlank(commodityString, "货物信息不能为空");
        Collection<OrderCommodity> commodities = Globallys.toJavaObjects(commodityString, OrderCommodity.class);
        Assert.notEmpty(commodities, "货物信息不能为空");
        commodities = commodities.stream().filter(c -> !O.isEmptyCommodity(c)).collect(Collectors.toList());
        Assert.notEmpty(commodities, "货物信息不能为空");
        OrderExtra extra = null;
        String extraString = requestObject.get("extra");
        if (StringUtils.isNotBlank(extraString)) {
            extra = Globallys.toJavaObject(extraString, OrderExtra.class);
        }
        Collection<CustomData> customDatas = null;
        String customDataString = requestObject.get("customDatas");
        if (StringUtils.isNotBlank(customDataString)) {
            customDatas = Globallys.toJavaObjects(customDataString, CustomData.class);
        }
        planOrderService.generate(userKey, planKey, orderTemplate, extra, commodities, customDatas, partnerType);
    }

    /**
     * 发货方-批量生成发货单
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/shipper/generate/barch", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult shipperBatchGenerate(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        Long templateKey = requestObject.getLong("templateKey");
        //Assert.notBlank(templateKey, "请选择发货模板");
        Collection<Long> planKeys = StringUtils.longCollection(requestObject.get("planKeys"));
        Assert.notEmpty(planKeys, "至少选择一项要生成发货单的发货计划");
        planOrderService.generates(loadUserKey(request), PartnerType.SHIPPER, planKeys, templateKey);
        return JsonResult.SUCCESS;
    }

    /**
     * 承运方-批量生成发货单
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/coveyer/generate/barch", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult conveyBatchGenerate(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        Long templateKey = requestObject.getLong("templateKey");
        //Assert.notBlank(templateKey, "请选择发货模板");
        Collection<Long> planKeys = StringUtils.longCollection(requestObject.get("planKeys"));
        Assert.notEmpty(planKeys, "至少选择一项要生成发货单的发货计划");
        planOrderService.generates(loadUserKey(request), PartnerType.CONVEY, planKeys, templateKey);
        return JsonResult.SUCCESS;
    }

    /**
     * 功能描述: 发货方-发货计划列表查询
     *
     * @param object
     * @param request
     * @return
     * @auther: wangke
     * @date: 2018/9/14 9:04
     */
    @RequestMapping(value = "/shipper/search")
    @ResponseBody
    public JsonResult shipperSearch(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        PlanSearch planSearch = object.toJavaBean(PlanSearch::new);
        planSearch.setuKey(loadUserKey(request));
        PageScope scope = new PageScope(object.getInteger("num"), object.getInteger("size"));
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("results", planOrderService.pagePlanOrder(PartnerType.SHIPPER, planSearch, scope));
        return jsonResult;
    }

    /**
     * 功能描述: 承运方-发货计划列表查询
     *
     * @param object
     * @param request
     * @return
     * @auther: wangke
     * @date: 2018/9/14 9:04
     */
    @RequestMapping(value = "/coveyer/search")
    @ResponseBody
    public JsonResult coveyerSearch(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        PlanSearch planSearch = object.toJavaBean(PlanSearch::new);
        planSearch.setuKey(loadUserKey(request));
        PageScope scope = new PageScope(object.getInteger("num"), object.getInteger("size"));
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("results", planOrderService.pagePlanOrder(PartnerType.CONVEY, planSearch, scope));
        return jsonResult;
    }

    /**
     * 功能描述: 发货计划来源参数查询
     *
     * @param request
     * @return
     * @auther: wangke
     * @date: 2018/9/14 9:04
     */
    @RequestMapping(value = "/list/source")
    @ResponseBody
    public JsonResult pullParameter(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("pulls", planOrderService.pullParameter(companyService.assertCompanyByUserKey(loadUserKey(request)).getId()));
        return jsonResult;
    }

    /**
     * 功能描述: 指派物流商
     *
     * @param object
     * @param request
     * @return
     * @auther: wangke
     * @date: 2018/9/14 8:56
     */
    @RequestMapping(value = "/designate")
    @ResponseBody
    public JsonResult designate(@RequestBody RequestObject object, HttpServletRequest request) {
        Collection<Long> planKeys = StringUtils.longCollection(object.get("planKeys"));
        Assert.notEmpty(planKeys, "请至少选择一项需要分配的发货计划");
        Long customerKey = object.getLong("customerKey");
        Assert.notNull(customerKey, "请选择物流商");
        String driverName = object.get("driverName");
        String driverContact = object.get("driverContact");
        int count = planOrderService.designate(loadUserKey(request), planKeys, customerKey, driverName, driverContact);
        return new JsonResult(true, "成功分配[" + count + "]发货计划");
    }


    /**
     * 功能描述: 发货计划详情查询
     *
     * @param request
     * @return
     * @auther: wangke
     * @date: 2018/9/14 9:04
     */
    @RequestMapping(value = "/convert/order")
    @ResponseBody
    public JsonResult convertOrder(@RequestBody RequestObject requestObject, HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        Long planKey = requestObject.getLong("planKey");
        Assert.notBlank(planKey, "发货计划编号不能为空");
        PlanAlliance alliance = planOrderService.alliance(loadUserKey(request), planKey, P.DETAILS);
        jsonResult.put("planKey", planKey);
        jsonResult.put("results", Transform.transform(alliance));

        return jsonResult;
    }

    /**
     * 接单
     *
     * @param object
     * @param request
     * @return
     */
    @RequestMapping(value = "/recive")
    @ResponseBody
    public JsonResult receive(@RequestBody RequestObject object, HttpServletRequest request) {
        Collection<Long> planKeys = StringUtils.longCollection(object.get("planKeys"));
        Assert.notEmpty(planKeys, "至少选择一项要接单的数据");
        planOrderService.receive(loadUserKey(request), planKeys);
        return JsonResult.SUCCESS;
    }

    @RequestMapping(value = "/detail/{planKey}")
    @ResponseBody
    public JsonResult receive(@PathVariable Long planKey, HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        Assert.notBlank(planKey, "计划编号不能为空");
        jsonResult.put("result", planOrderService.alliance(loadUserKey(request), planKey, P.DETAILS));
        return jsonResult;
    }


    @RequestMapping(value = "/delete/{planKey}")
    @ResponseBody
    public JsonResult delete(@PathVariable Long planKey, HttpServletRequest request) {
        Assert.notBlank(planKey, "计划编号不能为空");
        planOrderService.delete(loadUserKey(request), planKey);
        return JsonResult.SUCCESS;
    }

    /**
     * 查询企业客户集合
     *
     * @param object
     * @param request
     * @return
     */
    @RequestMapping(value = "/listCustomer")
    @ResponseBody
    public JsonResult listCustomer(@RequestBody RequestObject object, HttpServletRequest request) {
        CustomerSearch search = new CustomerSearch();
        search.setUserKey(loadUserKey(request));
        search.setType(3);
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("results", customerService.searchCustomerBySomething(search));
        return jsonResult;
    }
    /**
     * 功能描述: 发货方/发货计划编辑
     *
     * @param object
     * @param request
     * @return
     * @auther: wangke
     * @date: 2018/9/21 8:39
     */
    @RequestMapping(value = "/shipper/edit")
    @ResponseBody
    public JsonResult shipperEdit(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        OrderExtra orderExtra = Globallys.toJavaObject(object.get("orderExtra"), OrderExtra.class);
        Collection<PlanCommodity> planCommodity = Globallys.toJavaObjects(object.get("planCommodity"), PlanCommodity.class);
        Assert.notEmpty(planCommodity, "物料数据不能为空");
        Collection<CustomData> customDatas = Globallys.toJavaObjects(object.get("customDatas"), CustomData.class);
        PlanTemplate order = Globallys.toJavaObject(object.get("planOrder"), PlanTemplate.class);
        Assert.notNull(order, "订单信息不能为空");
        planOrderService.modifyPlanOrder(loadUserKey(request), order, PartnerType.SHIPPER, orderExtra, planCommodity, customDatas);
        return JsonResult.SUCCESS;
    }

    /**
     * 功能描述: 承运方/发货计划编辑
     *
     * @param object
     * @param request
     * @return
     * @auther: wangke
     * @date: 2018/9/21 8:39
     */
    @RequestMapping(value = "/convey/edit")
    @ResponseBody
    public JsonResult conveyEdit(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        OrderExtra orderExtra = Globallys.toJavaObject(object.get("orderExtra"), OrderExtra.class);
        Collection<PlanCommodity> planCommodity = Globallys.toJavaObjects(object.get("planCommodity"), PlanCommodity.class);
        Assert.notEmpty(planCommodity, "物料数据不能为空");
        Collection<CustomData> customDatas = Globallys.toJavaObjects(object.get("customDatas"), CustomData.class);
        PlanTemplate order = Globallys.toJavaObject(object.get("planOrder"), PlanTemplate.class);
        Assert.notNull(order, "订单信息不能为空");
        planOrderService.modifyPlanOrder(loadUserKey(request), order, PartnerType.CONVEY, orderExtra, planCommodity, customDatas);
        return JsonResult.SUCCESS;
    }


    /**
     * 单个/批量派车
     *
     * @param object
     * @param request
     * @return
     */
    @RequestMapping(value = "/car/send")
    @ResponseBody
    public JsonResult sendCar(@RequestBody RequestObject object, HttpServletRequest request) {
        Collection<Long> orders = StringUtils.longCollection(object.get("orders"));
        Assert.notEmpty(orders, "请至少选择一条需要派车的计划");
        String extraStr = object.get("extra");
        OrderExtra orderExtra = Globallys.toJavaObject(extraStr, OrderExtra.class);
        Assert.notNull(orderExtra, "派车信息不能为空");
        planOrderService.midifyCarStatus(orders, orderExtra);
        return JsonResult.SUCCESS;
    }

}

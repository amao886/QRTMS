package com.ycg.ksh.api.backstage.controller;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/17
 */

import com.alibaba.fastjson.JSONArray;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.FileUploadHelper;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.moutai.Customer;
import com.ycg.ksh.entity.persistent.moutai.Order;
import com.ycg.ksh.entity.persistent.moutai.Taker;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.moutai.MaotaiOrder;
import com.ycg.ksh.entity.service.moutai.MoutaiCustomerSearch;
import com.ycg.ksh.service.api.FileService;
import com.ycg.ksh.service.api.MoutaiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * 茅台
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/17
 */
@Controller("backstage.moutai.controller")
@RequestMapping("/backstage/moutai")
public class MoutaiController extends BaseController {

    private static final String FALSE_RESULT = "FALSE";
    @Resource
    protected FileService fileService;


    @Resource
    private MoutaiService moutaiService;
    /**
     * 茅台打印
     * @return
     */
    @RequestMapping(value = "/view/{viewkey}")
    public String views(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        if("order".equals(viewkey)){
            queryOrderList(new MaotaiOrder(), model, request, new PageScope());
            String filePath = SystemUtils.fileRootPath(Directory.TEMPLATE.sub("mt_order_template.xlsx"));
            model.addAttribute("template", FileUtils.buildDownload(filePath, "发货计划模板.xlsx", false));

        }
        if("customer".equals(viewkey)){
            queryCustomerList(new MoutaiCustomerSearch(),model,request,new PageScope());
            String filePath = SystemUtils.fileRootPath(Directory.TEMPLATE.sub("mt_customer_template.xlsx"));
            model.addAttribute("template", FileUtils.buildDownload(filePath, "客户模板.xlsx", false));
        }
        if("person".equals(viewkey)){
            queryTakerList(model, request, new PageScope());
        }
        return "/moutai/"+ viewkey;
    }

    @RequestMapping(value = "/order/import/excel", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult orderImportExcel(HttpServletRequest request) throws Exception {
        String conveyKeyString = request.getParameter("conveyKey");
        Assert.notBlank(conveyKeyString, "请选择一个承运单位");
        Long conveyKey = Long.parseLong(conveyKeyString);
        File file = FileUploadHelper.saveExcel(request, SystemUtils.directoryTemp());
        Assert.notNull(file, "请上传一个文件!!!");
        FileEntity fileEntity = new FileEntity(file);
        String path = fileEntity.getPath();
        try {
            fileEntity = fileService.saveOrderByFile(loadUserKey(request), conveyKey, fileEntity);
        } finally {
            FileUtils.forceDelete(new File(path));
        }
        JsonResult jsonResult = new JsonResult();
        if (StringUtils.isNotBlank(fileEntity.getPath())) {
            jsonResult.modify(false, "存在异常数据</br>成功导入"+ fileEntity.getSuccessCount() +"条,失败"+ fileEntity.getFailureCount() +"条</br>请下载处理后上传导入");
            fileEntity.setUrl(FileUtils.buildDownload(fileEntity.getPath(), FileUtils.appendSuffix("导入异常数据", fileEntity.getSuffix())));
            jsonResult.put("result", fileEntity);
        } else {
            jsonResult.message("操作成功</br>成功导入"+ fileEntity.getSuccessCount() +"条,失败"+ fileEntity.getFailureCount() +"条");
        }
        return jsonResult;
    }

    @RequestMapping(value = "/customer/import/excel", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult customerImportExcel(HttpServletRequest request) throws Exception {
        File file = FileUploadHelper.saveExcel(request, SystemUtils.directoryTemp());
        Assert.notNull(file, "请上传一个文件!!!");
        FileEntity fileEntity = new FileEntity(file);
        String path = fileEntity.getPath();
        try {
            fileEntity = fileService.saveCustomerByFile(loadUserKey(request), fileEntity);
        } finally {
            FileUtils.forceDelete(new File(path));
        }
        JsonResult jsonResult = new JsonResult();
        if (StringUtils.isNotBlank(fileEntity.getPath())) {
            jsonResult.modify(false, "存在异常数据</br>成功导入"+ fileEntity.getSuccessCount() +"条,失败"+ fileEntity.getFailureCount() +"条</br>请下载处理后上传导入");
            fileEntity.setUrl(FileUtils.buildDownload(fileEntity.getPath(), FileUtils.appendSuffix("导入异常数据", fileEntity.getSuffix())));
            jsonResult.put("result", fileEntity);
        } else {
            jsonResult.message("操作成功</br>成功导入"+ fileEntity.getSuccessCount() +"条,失败"+ fileEntity.getFailureCount() +"条");
        }
        return jsonResult;
    }

    /**
     * 发货单查询
     * User: wyj
     * Date: 2018-04-18
     */
    @RequestMapping("/queryDeliveryList")
    public String queryOrderList(MaotaiOrder search, Model model, HttpServletRequest request, PageScope pageScope){
        pageScope.validate(10,1);
        if (search.getStartTime() == null && search.getEndTime() == null){
            search.setEndTime(Date.valueOf(LocalDate.now()));
            search.setStartTime(Date.valueOf(LocalDate.now().minusDays(6)));
        }
        CustomPage<Order> list = moutaiService.queryOrderList(search,pageScope);
        model.addAttribute("page",list);
        model.addAttribute("search", search);
        model.addAttribute("conveyList", moutaiService.listMoutaiConvey());
        //2018-05-10 需求:客户编码背景修改,并且可以单击增加收货人
        model.addAttribute("customerKeyList",  moutaiService.listMoutaiCustomerKey());
        return "/moutai/order";
    }

    /**
     * 仓库保存
     * User: wyj
     * Date: 2018-04-19
     */
    @RequestMapping("/add/depot")
    @ResponseBody
    public JsonResult addDepot(@RequestBody RequestObject requestObject, HttpServletRequest request, Model model){
        String depot = requestObject.get("depot");  //仓库
        List<String> ids = JSONArray.parseArray(requestObject.get("ids"), String.class);
        try{
            moutaiService.addDepot(depot,ids);
            return JsonResult.SUCCESS;
        }catch(BusinessException | ParameterException e){
            return new JsonResult(FALSE_RESULT, e.getFriendlyMessage());
        }catch (Exception e){
            return new JsonResult(FALSE_RESULT, Constant.SYSTEM_ERROR);
        }
    }

    /**
     * 发货单查看
     * User: wyj
     * Date: 2018-04-19
     */
    @RequestMapping("/editOrder/{id}")
    @ResponseBody
    public JsonResult searchOneOrder(@PathVariable Long id,Model model,HttpServletRequest request){
        JsonResult result = JsonResult.SUCCESS;
        result.put("order",moutaiService.searchOneOrderById(id));
        return result;
    }

    @RequestMapping("/order/update")
    @ResponseBody
    public JsonResult updateOrderById(HttpServletRequest request){
        Order order = null;
        try{
            RequestObject requestObject = new RequestObject(request.getParameterMap());
            order = requestObject.toJavaBean(Order.class);
        }catch (ReflectiveOperationException e) {
            return new JsonResult(FALSE_RESULT,Constant.PARAMS_ERROR);
        }
        try {
            moutaiService.updateOrderById(order);
            return JsonResult.SUCCESS;
        }catch (BusinessException | ParameterException e){
            return new JsonResult(FALSE_RESULT,e.getFriendlyMessage());
        }catch (Exception e){
            return new JsonResult(FALSE_RESULT, Constant.SYSTEM_ERROR);
        }
    }
    @RequestMapping("/order/delete")
    @ResponseBody
    public JsonResult deleteConvey(@RequestBody RequestObject requestObject ,HttpServletRequest request){
        List<String> ids = JSONArray.parseArray(requestObject.get("ids"), String.class);
        try{
            moutaiService.deleteOrderById(ids);
            return JsonResult.SUCCESS;
        }catch(BusinessException | ParameterException e){
            return new JsonResult(FALSE_RESULT, e.getFriendlyMessage());
        }catch (Exception e){
            return new JsonResult(FALSE_RESULT, Constant.SYSTEM_ERROR);
        }
    }

    /**
     * 跳转到打印页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/order/toPrint")
    public String orderPrint( HttpServletRequest request,Model model){
        RequestObject object = new RequestObject(request.getParameterMap());
        Assert.notNull(object, Constant.PARAMS_ERROR);
        Collection<Long> ids = StringUtils.longCollection(object.get("ids"));
        logger.debug("order print page -> {} ,ids -> {}", object,ids);
        model.addAttribute("ids", ids);
        return "/moutai/order_print";
    }
  /**
     * 校验是否可以打印
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("order/checkPrintData")
    @ResponseBody
    public JsonResult checkPrintData(@RequestBody RequestObject requestObject, HttpServletRequest request,Model model){
        List<Long> listIds= JSONArray.parseArray(requestObject.get("ids"),Long.class);
        List<Long> resultList = moutaiService.checkPrintDataByIds(listIds);
        if(resultList.size() > 0){
            JsonResult result = new JsonResult(false,"存在有收货客户编码未维护、承运单位当前取货人未启用的数据");
            return result;
        }else{
            return JsonResult.SUCCESS;
        }
    }

    @RequestMapping(value = "/export/word", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult exportWord(@RequestBody RequestObject object, HttpServletRequest request){
        logger.debug("export word -> {}", object);
        JsonResult jsonResult = new JsonResult();
        Assert.notNull(object, Constant.PARAMS_ERROR);
        String ids = object.get("ids");
        Assert.notBlank(ids,"请选择要打印的单号");
        List<Long> listIds= JSONArray.parseArray(ids,Long.class);
        Assert.notEmpty(listIds,"请选择要打印的单号");
        FileEntity fileEntity = moutaiService.buildWord(loadUserKey(request), listIds);
        if (StringUtils.isNotBlank(fileEntity.getPath())) {
            fileEntity.setUrl(FileUtils.buildDownload(fileEntity.getPath(), FileUtils.appendSuffix("WORD存档文件", fileEntity.getSuffix())));
            jsonResult.put("result", fileEntity);
        } else {
            jsonResult.message("操作成功,稍后再试!");
        }
        return jsonResult;
    }

    @RequestMapping(value = "/print/orders", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getOrderPrintData(@RequestBody RequestObject object, HttpServletRequest request){
        logger.debug("print order -> {}", object);
        JsonResult jsonResult = new JsonResult();
        Assert.notNull(object, Constant.PARAMS_ERROR);
        String ids = object.get("ids");
        List<Long> listIds= JSONArray.parseArray(ids,Long.class);
        Assert.notEmpty(listIds,"请选择要打印的单号");
        String username = RequestUitl.getUserInfo(request).getUnamezn();
        try{
            JsonResult result = JsonResult.SUCCESS;
            result.put("orders", moutaiService.getOrderPrintDataById(listIds, username));
            return result;
        }catch (BusinessException | ParameterException e){
            return new JsonResult(FALSE_RESULT, e.getFriendlyMessage());
        }catch (Exception e){
            return new JsonResult(FALSE_RESULT, Constant.SYSTEM_ERROR);
        }
    }
    @RequestMapping(value = "/order/printSign", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getOrderPrintSign(@RequestBody RequestObject object, HttpServletRequest request){
        List<Long> listIds= JSONArray.parseArray(object.get("ids"),Long.class);
        try{
            moutaiService.updateOrderPrintSign(listIds);
            return  JsonResult.SUCCESS;
        }catch (Exception e){
            return new JsonResult(FALSE_RESULT, Constant.SYSTEM_ERROR);
        }
    }


    /**
     * 收获客户查询
     * User: wyj
     * Date: 2018-04-18
     */
    @RequestMapping("/queryCustomerList")
    public String queryCustomerList(MoutaiCustomerSearch search, Model model, HttpServletRequest request, PageScope pageScope){
        pageScope.validate(10,1);
        CustomPage<Customer> list = moutaiService.queryCustomerList(search,pageScope);
        model.addAttribute("page",list);
        model.addAttribute("search", search);
        return "/moutai/customer";
    }



    /**
     * 收货客户查看
     * User: wyj
     * Date: 2018-04-19
     */
    @RequestMapping("/editCustomer/{customerNo}")
    @ResponseBody
    public JsonResult searchOneCustomer(@PathVariable String customerNo,Model model,HttpServletRequest request){
        JsonResult result = JsonResult.SUCCESS;
        result.put("customer",moutaiService.searchOneCustomerById(customerNo));
        return result;
    }

    @RequestMapping("/customer/add")
    @ResponseBody
    public JsonResult addCustomer(@RequestBody RequestObject object,HttpServletRequest request,Model model){
        Customer customer = null;
        try {
            customer = object.toJavaBean(Customer.class);
        } catch (ReflectiveOperationException e) {
            return new JsonResult(FALSE_RESULT,Constant.PARAMS_ERROR);
        }
        try {
            customer.setCreateUserId(loadUserKey(request));
            moutaiService.addCustomer(customer);
            return JsonResult.SUCCESS;
        } catch (BusinessException| ParameterException e) {
            return new JsonResult(FALSE_RESULT,e.getFriendlyMessage());
        }catch(Exception e) {
            return new JsonResult(FALSE_RESULT,Constant.SYSTEM_ERROR);
        }


    }
    @RequestMapping("/customer/update")
    @ResponseBody
    public JsonResult updateCustomerById(HttpServletRequest request){
        Customer customer = null;
        try {
            RequestObject requestObject = new RequestObject(request.getParameterMap());
            customer = requestObject.toJavaBean(Customer.class);
        } catch (ReflectiveOperationException e) {
            return new JsonResult(FALSE_RESULT,Constant.PARAMS_ERROR);
        }

        try {
            moutaiService.updateCustomerById(customer);
            return JsonResult.SUCCESS;
        }catch (BusinessException | ParameterException e){
            return new JsonResult(FALSE_RESULT,e.getFriendlyMessage());
        }catch (Exception e){
            return new JsonResult(FALSE_RESULT, Constant.SYSTEM_ERROR);
        }
    }
    @RequestMapping("/customer/delete")
    @ResponseBody
    public JsonResult deleteCustomer(@RequestBody RequestObject requestObject ,HttpServletRequest request){
        List<String> customerNos = JSONArray.parseArray(requestObject.get("customerNos"), String.class);
        try{
            moutaiService.deleteCustomerBycustomerNos(customerNos);
            return JsonResult.SUCCESS;
        }catch(BusinessException | ParameterException e){
            return new JsonResult(FALSE_RESULT, e.getFriendlyMessage());
        }catch (Exception e){
            return new JsonResult(FALSE_RESULT, Constant.SYSTEM_ERROR);
        }
    }

    @RequestMapping("/queryTakerList")
    public String queryTakerList(Model model, HttpServletRequest request, PageScope pageScope){
        pageScope.validate(10,1);
        CustomPage<Taker> list = moutaiService.queryTakerList(pageScope);
        model.addAttribute("page",list);
        model.addAttribute("conveyList", moutaiService.listMoutaiConvey());
        model.addAttribute("search",pageScope);
        return "/moutai/person";
    }

    @RequestMapping("/taker/add")
    @ResponseBody
    public JsonResult addTaker(@RequestBody RequestObject requestObject, HttpServletRequest request, Model model){
        Taker taker = null;
        try{
            taker = requestObject.toJavaBean(Taker.class);
        }catch (ReflectiveOperationException e) {
            return new JsonResult(FALSE_RESULT,Constant.PARAMS_ERROR);
        }
        try {
            taker.setCreateUserId(loadUserKey(request));
            moutaiService.addTaker(taker);
            return JsonResult.SUCCESS;
        } catch (BusinessException | ParameterException e){
            return new JsonResult(FALSE_RESULT,e.getFriendlyMessage());
        }catch (Exception e){
            return new JsonResult(FALSE_RESULT, Constant.SYSTEM_ERROR);
        }
    }
    /**
     * 取货人查看
     * User: wyj
     * Date: 2018-04-19
     */
    @RequestMapping("/editTaker/{id}")
    @ResponseBody
    public JsonResult searchOneTaker(@PathVariable Long id,Model model,HttpServletRequest request){
        JsonResult result = JsonResult.SUCCESS;
        result.put("taker",moutaiService.searchOneTakerById(id));
        return result;
    }

    @RequestMapping("/taker/update")
    @ResponseBody
    public JsonResult updateTaker(HttpServletRequest request){
        Taker taker = null;
        try{
             RequestObject requestObject = new RequestObject(request.getParameterMap());
             taker = requestObject.toJavaBean(Taker.class);
         }catch (ReflectiveOperationException e) {
             return new JsonResult(FALSE_RESULT,Constant.PARAMS_ERROR);
         }
         try {
            moutaiService.updateTakerById(taker);
            return JsonResult.SUCCESS;
        }catch (BusinessException | ParameterException e){
            return new JsonResult(FALSE_RESULT,e.getFriendlyMessage());
        }catch (Exception e){
            return new JsonResult(FALSE_RESULT, Constant.SYSTEM_ERROR);
        }
        }

}

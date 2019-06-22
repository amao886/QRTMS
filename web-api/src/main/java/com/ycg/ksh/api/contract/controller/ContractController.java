package com.ycg.ksh.api.contract.controller;

import com.alibaba.fastjson.JSON;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.extend.FileUploadHelper;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.core.contract.application.ContractApplicationService;
import com.ycg.ksh.core.contract.application.dto.ContractDetailDto;
import com.ycg.ksh.core.contract.application.dto.ContractVerifyDto;
import com.ycg.ksh.core.contract.application.dto.IncomeRecordDto;
import com.ycg.ksh.core.contract.application.dto.ValuationDto;
import com.ycg.ksh.entity.service.PageScope;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;


/**
 * 合同
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2019/01/03 0003
 */
@Controller("contract.controller")
@RequestMapping("/contract")
public class ContractController extends BaseController {

    @Resource
    ContractApplicationService applicationService;

    /**
     * 合同信息详情
     */
    @RequestMapping(value = "/get/{contractKey}")
    @ResponseBody
    public JsonResult detail(@PathVariable Long contractKey, HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("result", applicationService.getContract(contractKey));
        return jsonResult;
    }

    /**
     * 新增合同信息
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public JsonResult save(@RequestBody ContractDetailDto contractDto, HttpServletRequest request) throws Exception {
        Assert.notBlank(contractDto.getContractNo(), "合同编号不能为空");
        Assert.notBlank(contractDto.getContractType(), "合同类型不能为空");
        Assert.notBlank(contractDto.getCustomerCode(), "客户编号不能为空");
        Assert.notBlank(contractDto.getCustomerName(), "客户名称不能为空");
        Assert.notNull(contractDto.getContractEndDate(), "结束时间不能为空");
        Assert.notNull(contractDto.getContractStartDate(), "开始时间不能为空");
        Assert.notEmpty(contractDto.getList(), "至少需要有一个计价配置");
        applicationService.save(loadCompanyConcise(request), loadUserKey(request), contractDto.contractDto(), contractDto.getList());
        return JsonResult.SUCCESS;
    }

    /**
     * 更新合同信息
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public JsonResult update(@RequestBody ContractDetailDto contractDto, HttpServletRequest request) throws Exception {
        Assert.notBlank(contractDto.getId(), "合同编号不能为空");
        Assert.notBlank(contractDto.getContractNo(), "合同编号不能为空");
        Assert.notBlank(contractDto.getContractType(), "合同类型不能为空");
        Assert.notBlank(contractDto.getCustomerCode(), "客户编号不能为空");
        Assert.notBlank(contractDto.getCustomerName(), "客户名称不能为空");
        Assert.notNull(contractDto.getContractEndDate(), "结束时间不能为空");
        Assert.notNull(contractDto.getContractStartDate(), "开始时间不能为空");
        Assert.notEmpty(contractDto.getList(), "至少需要有一个计价配置");
        applicationService.modify(loadCompanyConcise(request), loadUserKey(request), contractDto.contractDto(), contractDto.getList());
        return JsonResult.SUCCESS;
    }

    /**
     * 审核合同信息
     */
    @RequestMapping(value = "/verify")
    @ResponseBody
    public JsonResult verify(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        Long contractKey = object.getLong("contractKey");
        Assert.notBlank(contractKey, "合同编号不能为空");
        String verifyDto = object.get("verifyDto");
        ContractVerifyDto contractVerifyDto = new ContractVerifyDto();
        if (StringUtils.isNotBlank(verifyDto)) {
            contractVerifyDto = Globallys.toJavaObject(verifyDto, ContractVerifyDto.class);
        }
        Assert.notNull(contractVerifyDto, "合同审核信息不能为空");
        applicationService.verify(loadUserKey(request), contractKey, contractVerifyDto);
        return JsonResult.SUCCESS;
    }


    /**
     * 合同计价列表查询
     *
     * @param requestObject
     * @return
     */
    @RequestMapping(value = "/pricing/search")
    @ResponseBody
    public JsonResult search(@RequestBody RequestObject requestObject, HttpServletRequest request) {
        String likeString = requestObject.get("searchText");
        Integer contractType = requestObject.getInteger("contractType");
        PageScope scope = new PageScope(requestObject.getInteger("num"), requestObject.getInteger("size"));
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("result", applicationService.searchContract(loadCompanyConcise(request), contractType, likeString, scope));
        return jsonResult;
    }

    /**
     * 查询计算应收详情
     *
     * @return
     */
    @RequestMapping(value = "/receivable/details")
    @ResponseBody
    public JsonResult receivableDetails(@RequestBody RequestObject requestObject, HttpServletRequest request) {
        Collection<Long> orderKeys = StringUtils.longCollection(requestObject.get("systemId"));
        Assert.notEmpty(orderKeys, "请至少选择一条订单信息");
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("result", applicationService.receivableDetails(orderKeys, loadCompanyConcise(request), loadUserKey(request)));
        return jsonResult;
    }

    /**
     * 批量计算单笔应收
     *
     * @return
     */
    @RequestMapping(value = "/singleReceivable")
    @ResponseBody
    public JsonResult singleReceivable(@RequestBody RequestObject requestObject, HttpServletRequest request) {
        Assert.notNull(requestObject, Constant.PARAMS_ERROR);
        Collection<IncomeRecordDto> dtos = JSON.parseArray(requestObject.get("list"), IncomeRecordDto.class);
        if (CollectionUtils.isEmpty(dtos)) {
            return new JsonResult().modify(false, "请至少选择一条订单");
        }
        applicationService.singleReceivable(loadUserKey(request), loadCompanyConcise(request), dtos);
        return JsonResult.SUCCESS;
    }

    /**
     * 物料配置导入
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/import/excel", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult importCommodity(HttpServletRequest request) throws Exception {
        RequestObject requestObject = new RequestObject(request.getParameterMap());
        File file = FileUploadHelper.saveExcel(request, SystemUtils.directoryTemp());
        Assert.notNull(file, "请上传一个文件!!!");
        Long contractKey = requestObject.getLong("id");
        Assert.notBlank(contractKey, "合同编号不能为空");
        FileEntity fileEntity = new FileEntity(file);
        applicationService.batchImportCommodity(contractKey, fileEntity);
        return JsonResult.SUCCESS;
    }

    /**
     * 物料列表查询
     *
     * @param body
     * @param request
     * @return
     */
    @RequestMapping(value = "/commodity/search")
    @ResponseBody
    public JsonResult commodityManage(@RequestBody RequestObject body, HttpServletRequest request) {
        Long contractKey = body.getLong("id");
        Assert.notBlank(contractKey, "物料编号不能为空");
        String likeString = body.get("searchText");
        PageScope pageScope = new PageScope(body.getInteger("currentPage"), body.getInteger("pageSize"));
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("result", applicationService.commoditySearch(contractKey, likeString, pageScope));
        return jsonResult;
    }

    /**
     * 收支列表查询
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/income/search")
    @ResponseBody
    public JsonResult incomeManage(@RequestBody RequestObject body, HttpServletRequest request) {
        String likeString = body.get("likeString");
        Integer statuts = body.getInteger("confirmState");
        LocalDateTime deliveryDateStart = Optional.ofNullable(body.getLocalDateTime("deliveryDateStart")).orElse(null),
                deliveryDateEnd = Optional.ofNullable(body.getLocalDateTime("deliveryDateEnd")).orElse(null);
        PageScope pageScope = new PageScope(body.getInteger("currentPage"), body.getInteger("pageSize"));
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("result", applicationService.incomeSearch(likeString, statuts, deliveryDateStart,
                deliveryDateEnd, pageScope, loadCompanyKey(request)));
        return JsonResult.SUCCESS;
    }

}
